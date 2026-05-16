package com.gv.steel.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.gv.steel.common.core.constant.CacheConstants;
import com.gv.steel.common.core.enums.DictTypeEnum;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.dao.SysDictItemDao;
import com.gv.steel.system.user.entity.SysDict;
import com.gv.steel.system.user.entity.SysDictItem;
import com.gv.steel.system.user.service.SysDictItemService;
import com.gv.steel.system.user.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字典项
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends BaseServiceImpl<SysDictItemDao, SysDictItem> implements SysDictItemService {

    private final SysDictService dictService;

    private final CacheManager cacheManager;

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    @Override
    public boolean removeDictItem(Long id) {
        // 根据ID查询字典ID
        SysDictItem dictItem = this.getById(id);
        SysDict dict = dictService.getById(dictItem.getDictId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()),
                MsgUtils.getMessage(ErrorCodeConstants.SYS_DICT_DELETE_SYSTEM));
        return this.removeById(id);
    }

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    @Override
    public boolean updateDictItem(SysDictItem item) {
        // 查询字典
        SysDict dict = dictService.getById(item.getDictId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()),
                MsgUtils.getMessage(ErrorCodeConstants.SYS_DICT_UPDATE_SYSTEM));
        return updateById(item);
    }

    @Override
    public Map<String, List<SysDictItem>> getByDictKeys(String[] keys, String language) {
        if (ArrayUtil.isEmpty(keys)) {
            return Maps.newHashMap();
        }

        Map<String, List<SysDictItem>> resultMap = Maps.newHashMap();

        String key = language + ":%s";
        Cache cache = cacheManager.getCache(CacheConstants.DICT_DETAILS);
        List<String> noCacheKeys = Stream.of(keys).filter(dictKey -> {
            if (null == cache) return true;

            String redisKey = String.format(key, dictKey);
            List<SysDictItem> list = cache.get(redisKey, List.class);
            if (CollUtil.isEmpty(list)) {
                return true;
            }
            resultMap.put(dictKey, list);
            return false;
        }).collect(Collectors.toList());

        if (CollUtil.isEmpty(noCacheKeys)) {
            return resultMap;
        }

        List<SysDictItem> list = list(new LambdaQueryWrapper<SysDictItem>()
                .in(SysDictItem::getDictKey, noCacheKeys)
                .eq(SysDictItem::getLang, language)
                .orderByAsc(SysDictItem::getSortOrder)
        );
        Map<String, List<SysDictItem>> noCacheMap = list.stream().collect(Collectors.groupingBy(SysDictItem::getDictKey));
        noCacheMap.forEach((k, v) -> {
            if (null == cache) return;
            cache.put(String.format(key, k), v);
        });
        resultMap.putAll(noCacheMap);
        return resultMap;
    }

    @Override
    public boolean saveDictItem(SysDictItem sysDictItem) {
        Optional.of(sysDictItem).map(SysDictItem::getDictId).ifPresentOrElse(dictId -> {
                },
                () -> {
                    SysDict sysDict = dictService.getOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getDictKey, sysDictItem.getDictKey()));
                    Optional.ofNullable(sysDict).ifPresentOrElse(dict -> sysDictItem.setDictId(dict.getId()), () -> {
                        throw new BaseException(MsgUtils.getMessage("sys.dict.not.exists", sysDictItem.getDictKey()));
                    });
                });
        return save(sysDictItem);
    }

    @Override
    @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#language + ':' + #key")
    public List<SysDictItem> listByKey(String key, String language) {
        return list(Wrappers.<SysDictItem>query()
                .lambda()
                .eq(SysDictItem::getDictKey, key)
                .eq(SysDictItem::getLang, language)
                .orderByAsc(SysDictItem::getSortOrder));
    }
}
