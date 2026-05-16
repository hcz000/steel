package com.gv.steel.system.user.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysDict;

/**
 * 字典表
 */
public interface SysDictService extends BaseService<SysDict> {

    /**
     * 根据ID 删除字典
     *
     * @param id
     * @return
     */
    boolean removeDict(Long id);

    /**
     * 更新字典
     *
     * @param sysDict 字典
     * @return
     */
    boolean updateDict(SysDict sysDict);

    /**
     * 清除缓存
     */
    boolean clearDictCache();

}
