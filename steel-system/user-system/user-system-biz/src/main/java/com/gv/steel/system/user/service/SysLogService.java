package com.gv.steel.system.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.dto.SysLogDTO;
import com.gv.steel.system.user.entity.SysLog;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 */
public interface SysLogService extends BaseService<SysLog> {

    /**
     * 分页查询日志
     *
     * @param page
     * @param sysLog
     * @return
     */
    Page<SysLog> getLogByPage(Page<SysLog> page, SysLogDTO sysLog);

    /**
     * 列表查询日志
     *
     * @param sysLog 查询条件
     * @return List
     */
    List<SysLog> getLogList(SysLogDTO sysLog);

}
