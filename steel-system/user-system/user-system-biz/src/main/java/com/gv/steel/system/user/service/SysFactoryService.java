package com.gv.steel.system.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysFactory;

/**
 * <p>
 * 工厂管理 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface SysFactoryService extends BaseService<SysFactory> {

    /**
     * 工厂管理查询列表
     *
     * @param page    分页参数
     * @param factory 工厂查询参数
     * @return Page<SysFactory></>
     */
    Page<SysFactory> getPage(Page<SysFactory> page, SysFactory factory);
}
