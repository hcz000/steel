
package com.gv.steel.system.user.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysLogDao;
import com.gv.steel.system.user.dto.SysLogDTO;
import com.gv.steel.system.user.entity.SysLog;
import com.gv.steel.system.user.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 */
@Service
@Transactional
public class SysLogServiceImpl extends BaseServiceImpl<SysLogDao, SysLog> implements SysLogService {

    @Override
    public Page<SysLog> getLogByPage(Page<SysLog> page, SysLogDTO sysLog) {
        return baseMapper.selectPage(page, buildQueryWrapper(sysLog));
    }

    /**
     * 列表查询日志
     *
     * @param sysLog 查询条件
     * @return List
     */
    @Override
    public List<SysLog> getLogList(SysLogDTO sysLog) {
        return baseMapper.selectList(buildQueryWrapper(sysLog));
    }

    /**
     * 构建查询的 wrapper
     *
     * @param sysLog 查询条件
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<SysLog> buildQueryWrapper(SysLogDTO sysLog) {
        LambdaQueryWrapper<SysLog> wrapper = Wrappers.<SysLog>lambdaQuery()
                .eq(ObjectUtil.isNotNull(sysLog.getType()), SysLog::getType, sysLog.getType())
                .like(StrUtil.isNotBlank(sysLog.getRemoteAddr()), SysLog::getRemoteAddr, sysLog.getRemoteAddr());

        if (ArrayUtil.isNotEmpty(sysLog.getCreateTime())) {
            wrapper.ge(SysLog::getCreateTime, sysLog.getCreateTime()[0])
                    .le(SysLog::getCreateTime, sysLog.getCreateTime()[1]);
        }

        wrapper.orderByDesc(SysLog::getCreateTime);

        return wrapper;
    }

}
