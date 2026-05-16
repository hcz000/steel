package com.gv.steel.common.easyexcel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.easyexcel.annotation.Sheet;
import com.gv.steel.common.easyexcel.convert.LocalDateStringConvert;
import com.gv.steel.common.easyexcel.convert.LocalDateTimeStringConvert;
import com.gv.steel.common.easyexcel.model.HeadGenerator;
import com.gv.steel.common.easyexcel.model.HeadMeta;
import com.gv.steel.common.easyexcel.properties.EasyExcelProperties;
import com.gv.steel.common.easyexcel.support.WriterBuilderAbstract;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {
    private final EasyExcelProperties easyExcelProperties;
    private final ObjectProvider<List<Converter<?>>> converterProvider;
    private final WriterBuilderAbstract writerBuilderAbstract;
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler;

    public void check(EasyExcelExport easyExcelExport) {
        if (easyExcelExport.sheets().length == 0) {
            log.error("@EasyExcelExport sheet字段配置不合法");
            throw new BaseException(MsgUtils.getSystemMessage(ErrorCodeConstants.DEFAULT_EXCEPTION));
        }
    }

    public void export(Object o, HttpServletResponse response, EasyExcelExport easyExcelExport) {
        this.check(easyExcelExport);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String name = (String) Objects.requireNonNull(requestAttributes).getAttribute("__EXCEL_NAME_KEY__", 0);
        if (name == null) {
            name = UUID.randomUUID().toString();
        }

        String fileName = String.format("%s%s", URLEncoder.encode(name, StandardCharsets.UTF_8), easyExcelExport.suffix().getValue());
        String contentType = MediaTypeFactory.getMediaType(fileName).map(MimeType::toString).orElse("application/vnd.ms-excel");
        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        this.write(o, response, easyExcelExport);
    }

    @SneakyThrows
    public ExcelWriter getExcelWriter(HttpServletResponse response, EasyExcelExport easyExcelExport) {
        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream()).registerConverter(LocalDateStringConvert.INSTANCE).registerConverter(LocalDateTimeStringConvert.INSTANCE).autoCloseStream(true).excelType(easyExcelExport.suffix()).inMemory(easyExcelExport.inMemory());
        if (StringUtils.hasText(easyExcelExport.password())) {
            writerBuilder.password(easyExcelExport.password());
        }

        if (ArrayUtils.isNotEmpty(easyExcelExport.include())) {
            writerBuilder.includeColumnFieldNames(Arrays.asList(easyExcelExport.include()));
        }

        if (ArrayUtils.isNotEmpty(easyExcelExport.exclude())) {
            writerBuilder.excludeColumnFieldNames(Arrays.asList(easyExcelExport.exclude()));
        }

        if (easyExcelExport.i18nHeader() && i18nHeaderCellWriteHandler == null) {
            log.warn("Export excel header internationalization is not set !");
        }

        if (easyExcelExport.i18nHeader() && i18nHeaderCellWriteHandler != null) {
            writerBuilder.registerWriteHandler(i18nHeaderCellWriteHandler);
        }

        Class<?>[] var4;
        int var5;
        int var6;
        Class<?> clazz;
        if (ArrayUtils.isNotEmpty(easyExcelExport.writeHandler())) {
            var4 = easyExcelExport.writeHandler();
            var5 = var4.length;

            for (var6 = 0; var6 < var5; ++var6) {
                clazz = var4[var6];
                writerBuilder.registerWriteHandler((WriteHandler) BeanUtils.instantiateClass(clazz));
            }
        }

        this.registerCustomConverter(writerBuilder);
        if (ArrayUtils.isNotEmpty(easyExcelExport.converter())) {
            var4 = easyExcelExport.converter();
            var5 = var4.length;

            for (var6 = 0; var6 < var5; ++var6) {
                clazz = var4[var6];
                writerBuilder.registerConverter((Converter<?>) BeanUtils.instantiateClass(clazz));
            }
        }

        String templatePath = this.easyExcelProperties.getTemplatePath();
        if (StringUtils.hasText(easyExcelExport.template())) {
            ClassPathResource classPathResource = new ClassPathResource(templatePath + File.separator + easyExcelExport.template());
            InputStream inputStream = classPathResource.getInputStream();
            writerBuilder.withTemplate(inputStream);
        }

        writerBuilder = this.writerBuilderAbstract.excelWriterBuilder(writerBuilder, response, easyExcelExport, templatePath);
        return writerBuilder.build();
    }

    public void registerCustomConverter(ExcelWriterBuilder builder) {
        this.converterProvider.ifAvailable((converters) -> {
            Objects.requireNonNull(builder);
            converters.forEach(builder::registerConverter);
        });
    }

    public WriteSheet sheet(Sheet sheet, Class<?> dataClass, String template, Class<? extends HeadGenerator> bookHeadEnhancerClass) {
        Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
        String sheetName = sheet.sheetName();
        ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo) : EasyExcel.writerSheet(sheetNo, sheetName);
        Class<? extends HeadGenerator> headGenerateClass = null;
        if (this.isNotInterface(sheet.headGenerateClass())) {
            headGenerateClass = sheet.headGenerateClass();
        } else if (this.isNotInterface(bookHeadEnhancerClass)) {
            headGenerateClass = bookHeadEnhancerClass;
        }

        if (headGenerateClass != null) {
            this.fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
        } else if (dataClass != null) {
            writerSheetBuilder.head(dataClass);
            if (sheet.excludes().length > 0) {
                writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheet.excludes()));
            }

            if (sheet.includes().length > 0) {
                writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheet.includes()));
            }
        }

        writerSheetBuilder = this.writerBuilderAbstract.excelWriterSheetBuilder(writerSheetBuilder, sheetNo, sheetName, dataClass, template, headGenerateClass);
        return writerSheetBuilder.build();
    }

    private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
        return !Modifier.isInterface(headGeneratorClass.getModifiers());
    }

    private void fillCustomHeadInfo(Class<?> dataClazz, Class<? extends HeadGenerator> headEnhancerClazz, ExcelWriterSheetBuilder writerSheetBuilder) {
        HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClazz);
        Assert.notNull(headGenerator, "The header generated bean does not exist.");
        HeadMeta head = headGenerator.head(dataClazz);
        writerSheetBuilder.head(head.getHead());
        writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
