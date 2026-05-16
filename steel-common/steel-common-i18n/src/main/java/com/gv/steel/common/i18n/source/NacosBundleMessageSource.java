package com.gv.steel.common.i18n.source;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertiesPersister;
import org.springframework.lang.Nullable;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NacosBundleMessageSource extends AbstractResourceBasedMessageSource {
    private static final String PROPERTIES_SUFFIX = ".properties";
    /**
     * nacos配置中心管理类
     */
    private final NacosConfigManager nacosConfigManager;
    /**
     * nacos同步配置执行器，使用系统统一的线程池
     */
    private final Executor nacosConfigAsyncExecutor;
    /**
     * 配置所属分组
     */
    private String nacosGroup = "I18N_GROUP";
    /**
     * 并发刷新标识
     */
    private boolean concurrentRefresh = true;

    private final PropertiesPersister propertiesPersister = ResourcePropertiesPersister.INSTANCE;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * Cache to hold filename lists per Locale
     */
    private final ConcurrentMap<String, Map<Locale, List<String>>> cachedFilenames = new ConcurrentHashMap<>();
    /**
     * Cache to hold already loaded properties per filename
     */
    private final ConcurrentMap<String, NacosBundleMessageSource.PropertiesHolder> cachedProperties = new ConcurrentHashMap<>();
    /**
     * Cache to hold already loaded properties per filename
     */
    private final ConcurrentMap<Locale, NacosBundleMessageSource.PropertiesHolder> cachedMergedProperties = new ConcurrentHashMap<>();

    /**
     * cache to listen already properties filename
     */
    private final Vector<String> cacheListenerFileName = new Vector<>();

    @Nullable
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        if (getCacheMillis() < 0) {
            NacosBundleMessageSource.PropertiesHolder propHolder = getMergedProperties(locale);
            String result = propHolder.getProperty(code);
            if (result != null) {
                return result;
            }
        } else {
            for (String basename : getBasenameSet()) {
                List<String> filenames = calculateAllFilenames(basename, locale);
                for (String filename : filenames) {
                    NacosBundleMessageSource.PropertiesHolder propHolder = getProperties(filename);
                    String result = propHolder.getProperty(code);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        if (getCacheMillis() < 0) {
            NacosBundleMessageSource.PropertiesHolder propHolder = getMergedProperties(locale);
            MessageFormat result = propHolder.getMessageFormat(code, locale);
            if (result != null) {
                return result;
            }
        } else {
            for (String basename : getBasenameSet()) {
                List<String> filenames = calculateAllFilenames(basename, locale);
                for (String filename : filenames) {
                    NacosBundleMessageSource.PropertiesHolder propHolder = getProperties(filename);
                    MessageFormat result = propHolder.getMessageFormat(code, locale);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    protected NacosBundleMessageSource.PropertiesHolder getMergedProperties(Locale locale) {
        NacosBundleMessageSource.PropertiesHolder mergedHolder = this.cachedMergedProperties.get(locale);
        if (mergedHolder != null) {
            return mergedHolder;
        }

        Properties mergedProps = newProperties();
        long latestTimestamp = -1;
        String[] basenames = StringUtils.toStringArray(getBasenameSet());
        ;
        for (int i = basenames.length - 1; i >= 0; i--) {
            List<String> filenames = calculateAllFilenames(basenames[i], locale);
            for (int j = filenames.size() - 1; j >= 0; j--) {
                String filename = filenames.get(j);
                NacosBundleMessageSource.PropertiesHolder propHolder = getProperties(filename);
                if (propHolder.getProperties() != null) {
                    mergedProps.putAll(propHolder.getProperties());
                    if (propHolder.getFileTimestamp() > latestTimestamp) {
                        latestTimestamp = propHolder.getFileTimestamp();
                    }
                }
            }
        }

        mergedHolder = new NacosBundleMessageSource.PropertiesHolder(mergedProps, latestTimestamp);
        NacosBundleMessageSource.PropertiesHolder existing = this.cachedMergedProperties.putIfAbsent(locale, mergedHolder);
        if (existing != null) {
            mergedHolder = existing;
        }
        return mergedHolder;
    }

    /**
     * 强制刷新
     *
     * @param fileName 文件名
     * @param config   变更后的配置内容
     */
    public void forceRefresh(String fileName, String config) throws IOException {
        synchronized (this) {
            Properties props = newProperties();
            if (!StringUtils.hasText(config)) {
                Set<String> basenameSet = getBasenameSet();
                basenameSet.forEach(bsn -> {
                    String i18nFileName = bsn.substring(bsn.lastIndexOf("/") + 1);
                    String substring = fileName.substring(fileName.lastIndexOf(i18nFileName)).replace(PROPERTIES_SUFFIX, "");
                    this.cachedProperties.remove(bsn.replace(i18nFileName, substring));
                });

                this.cachedMergedProperties.clear();
                return;
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            this.propertiesPersister.load(props, reader);

            long fileTimestamp = -1;
            NacosBundleMessageSource.PropertiesHolder propHolder = new NacosBundleMessageSource.PropertiesHolder(props, fileTimestamp);
            Set<String> basenameSet = getBasenameSet();
            basenameSet.forEach(bsn -> {
                String i18nFileName = bsn.substring(bsn.lastIndexOf("/") + 1);
                String substring = fileName.substring(fileName.lastIndexOf(i18nFileName)).replace(PROPERTIES_SUFFIX, "");
                this.cachedProperties.put(bsn.replace(i18nFileName, substring), propHolder);
            });

            this.cachedMergedProperties.clear();
        }
    }

    protected List<String> calculateAllFilenames(String basename, Locale locale) {
        Map<Locale, List<String>> localeMap = this.cachedFilenames.get(basename);
        if (localeMap != null) {
            List<String> filenames = localeMap.get(locale);
            if (filenames != null) {
                return filenames;
            }
        }

        // Filenames for given Locale
        List<String> filenames = new ArrayList<>(7);
        filenames.addAll(calculateFilenamesForLocale(basename, locale));

        // Filenames for default Locale, if any
        Locale defaultLocale = getDefaultLocale();
        if (defaultLocale != null && !defaultLocale.equals(locale)) {
            List<String> fallbackFilenames = calculateFilenamesForLocale(basename, defaultLocale);
            for (String fallbackFilename : fallbackFilenames) {
                if (!filenames.contains(fallbackFilename)) {
                    // Entry for fallback locale that isn't already in filenames list.
                    filenames.add(fallbackFilename);
                }
            }
        }

        // Filename for default bundle file
        filenames.add(basename);

        if (localeMap == null) {
            localeMap = new ConcurrentHashMap<>();
            Map<Locale, List<String>> existing = this.cachedFilenames.putIfAbsent(basename, localeMap);
            if (existing != null) {
                localeMap = existing;
            }
        }
        localeMap.put(locale, filenames);
        return filenames;
    }

    protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
        List<String> result = new ArrayList<>(3);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        StringBuilder temp = new StringBuilder(basename);

        temp.append('_');
        if (!language.isEmpty()) {
            temp.append(language);
            result.add(0, temp.toString());
        }

        temp.append('_');
        if (!country.isEmpty()) {
            temp.append(country);
            result.add(0, temp.toString());
        }

        if (!variant.isEmpty() && (!language.isEmpty() || !country.isEmpty())) {
            temp.append('_').append(variant);
            result.add(0, temp.toString());
        }

        return result;
    }

    protected NacosBundleMessageSource.PropertiesHolder getProperties(String filename) {
        NacosBundleMessageSource.PropertiesHolder propHolder = this.cachedProperties.get(filename);
        long originalTimestamp = -2;

        if (propHolder != null) {
            originalTimestamp = propHolder.getRefreshTimestamp();
            if (originalTimestamp == -1 || originalTimestamp > System.currentTimeMillis() - getCacheMillis()) {
                // Up to date
                return propHolder;
            }
        } else {
            propHolder = new NacosBundleMessageSource.PropertiesHolder();
            NacosBundleMessageSource.PropertiesHolder existingHolder = this.cachedProperties.putIfAbsent(filename, propHolder);
            if (existingHolder != null) {
                propHolder = existingHolder;
            }
        }

        // At this point, we need to refresh...
        if (this.concurrentRefresh && propHolder.getRefreshTimestamp() >= 0) {
            // A populated but stale holder -> could keep using it.
            if (!propHolder.refreshLock.tryLock()) {
                // Getting refreshed by another thread already ->
                // let's return the existing properties for the time being.
                return propHolder;
            }
        } else {
            propHolder.refreshLock.lock();
        }
        try {
            NacosBundleMessageSource.PropertiesHolder existingHolder = this.cachedProperties.get(filename);
            if (existingHolder != null && existingHolder.getRefreshTimestamp() > originalTimestamp) {
                return existingHolder;
            }
            return refreshProperties(filename);
        } finally {
            propHolder.refreshLock.unlock();
        }
    }

    /**
     * 刷新多语言配置
     *
     * @param filename 需刷新的文件名，即：ncaos config data-id
     * @return file propHolder
     */
    protected NacosBundleMessageSource.PropertiesHolder refreshProperties(String filename) {
        long refreshTimestamp = (getCacheMillis() < 0 ? -1 : System.currentTimeMillis());

        long fileTimestamp = -1;

        PropertiesHolder propHolder = null;

        // 先通过本地获取
        Resource resource = resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
        if (resource.exists()) {
            try {
                fileTimestamp = resource.lastModified();
                Properties props = loadProperties(resource);
                propHolder = new NacosBundleMessageSource.PropertiesHolder(props, fileTimestamp);
            } catch (IOException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Could not get properties [" + filename + "] form local file, error: " + ex.getLocalizedMessage());
                }
            }
        }

        if (propHolder == null) {
            String nacosConfigDataId = filename.replace(ResourceLoader.CLASSPATH_URL_PREFIX, "").replace("/", "_");
            try {
                Properties props = loadProperties(nacosConfigDataId);
                propHolder = new NacosBundleMessageSource.PropertiesHolder(props, fileTimestamp);
            } catch (IOException | NacosException | NullPointerException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Could not get properties [" + nacosConfigDataId + "] form nacos, error: " + ex.getLocalizedMessage());
                }
                // Empty holder representing "not valid".
                propHolder = new NacosBundleMessageSource.PropertiesHolder();
            }
        }

        propHolder.setRefreshTimestamp(refreshTimestamp);
        this.cachedProperties.put(filename, propHolder);
        return propHolder;
    }

    /**
     * 根据配置文件从本地获取
     *
     * @param resource
     * @return
     * @throws IOException
     */
    protected Properties loadProperties(Resource resource) throws IOException {
        Properties props = newProperties();
        try (InputStream is = resource.getInputStream()) {
            String encoding = getDefaultEncoding();
            if (encoding != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
                }
                this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Loading properties [" + resource.getFilename() + "]");
                }
                this.propertiesPersister.load(props, is);
            }
            return props;
        }
    }

    /**
     * 根据文件从nacos中加载多语言配置文件
     *
     * @param fileName
     * @return 配置类对象
     * @throws IOException
     * @throws NacosException
     */
    protected Properties loadProperties(String fileName) throws IOException, NacosException {
        setListenerNacosI18nProperties(fileName);
        Properties props = newProperties();
        String config = nacosConfigManager.getConfigService().getConfig(fileName + PROPERTIES_SUFFIX, nacosGroup, 5000);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        this.propertiesPersister.load(props, reader);
        return props;
    }

    protected void setListenerNacosI18nProperties(String fileName) throws NacosException {
        if (cacheListenerFileName.contains(fileName)) {
            return;
        }
        // 监听文件变化，实时更新国际化配置文件内容
        nacosConfigManager.getConfigService().addListener(fileName + PROPERTIES_SUFFIX, nacosGroup, new Listener() {
            @Override
            public Executor getExecutor() {
                return nacosConfigAsyncExecutor;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Loading nacos config properties [" + fileName + "], content " + configInfo);
                    }
                    // 更新对应配置信息，可以带入对应时区信息，做到仅更新对应时区的信息，避免全量的开销
                    forceRefresh(fileName + PROPERTIES_SUFFIX, configInfo);
                } catch (Exception e) {
                    logger.error("国际化配置监听异常", e);
                }
            }
        });
        // 添加至已监听的文件列表，防止重新监听
        cacheListenerFileName.add(fileName);
    }

    /**
     * 新建一个配置类对象
     *
     * @return new properties
     */
    protected Properties newProperties() {
        return new Properties();
    }

    @Data
    public class PropertiesHolder {
        /**
         * 配置内容-key,vlaue形式
         */
        private final Properties properties;

        /**
         * 文件时间
         */
        private final long fileTimestamp;

        /**
         * 刷新时间
         */
        private volatile long refreshTimestamp = -2;

        private final ReentrantLock refreshLock = new ReentrantLock();

        /**
         * Cache to hold already generated MessageFormats per message code.
         */
        private final ConcurrentMap<String, Map<Locale, MessageFormat>> cachedMessageFormats =
                new ConcurrentHashMap<>();

        public PropertiesHolder() {
            this.properties = null;
            this.fileTimestamp = -1;
        }

        public PropertiesHolder(Properties properties, long fileTimestamp) {
            this.properties = properties;
            this.fileTimestamp = fileTimestamp;
        }

        /**
         * 根据key获取内容
         *
         * @param code key
         * @return format message
         */
        public String getProperty(String code) {
            if (this.properties == null) {
                return null;
            }
            return this.properties.getProperty(code);
        }

        /**
         * 根据语言环境和内容key获取对应的多语言内容
         *
         * @param code   内容key
         * @param locale 语言环境
         * @return 多语言内容
         */
        public MessageFormat getMessageFormat(String code, Locale locale) {
            if (this.properties == null) {
                return null;
            }
            //根据code获取多语言，一个code对应多个国家语言内容
            Map<Locale, MessageFormat> localeMap = this.cachedMessageFormats.get(code);
            //再根据语言环境locale获取对应国家的语言内容
            if (localeMap != null) {
                MessageFormat result = localeMap.get(locale);
                if (result != null) {
                    return result;
                }
            }
            //如果根据语言环境locale未从缓存中获取，则从配置中获取对应的内容
            String msg = this.properties.getProperty(code);
            if (msg != null) {
                //如果code还没有缓存起来,创建一个新的多语言缓存容器(数据为空,语言环境locale与内容关联的缓存),将其赋值给localeMap
                if (localeMap == null) {
                    localeMap = new ConcurrentHashMap<>();
                    Map<Locale, MessageFormat> existing = this.cachedMessageFormats.putIfAbsent(code, localeMap);
                    if (existing != null) {
                        localeMap = existing;
                    }
                }
                //把locale与语言内容挂钩,然后存入缓存
                MessageFormat result = createMessageFormat(msg, locale);
                localeMap.put(locale, result);
                return result;
            }
            return null;
        }
    }
}
