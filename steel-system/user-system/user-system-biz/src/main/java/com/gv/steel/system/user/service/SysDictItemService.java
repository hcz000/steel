package com.gv.steel.system.user.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysDictItem;

import java.util.List;
import java.util.Map;

/**
 * 字典项
 */
public interface SysDictItemService extends BaseService<SysDictItem> {

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    boolean removeDictItem(Long id);

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    boolean updateDictItem(SysDictItem item);

    Map<String, List<SysDictItem>> getByDictKeys(String[] keys, String language);

    boolean saveDictItem(SysDictItem sysDictItem);

    List<SysDictItem> listByKey(String key, String language);
}
