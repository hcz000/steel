package com.gv.steel.system.user.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysTeam;

import java.util.List;

/**
 * <p>
 * 班组管理 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface SysTeamService extends BaseService<SysTeam> {

    /**
     * 班组管理查询列表
     *
     * @param page 分页参数
     * @param team 班组查询参数
     * @return Page<SysTeam>
     */
    Page<SysTeam> getPage(Page<SysTeam> page, SysTeam team);

    /**
     * 通过工厂ID获取班组选择树形
     *
     * @param factoryIds 工厂ID数组
     * @return 树形
     */
    List<Tree<Long>> tree(List<Long> factoryIds);

    /**
     * 通过用户ID获取班组列表
     *
     * @param userId 用户ID
     * @return 班组列表
     */
    List<SysTeam> listTeamByUserId(Long userId);
}
