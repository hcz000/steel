package com.gv.steel.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.entity.CommonModel;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.convert.TreeNodeMapper;
import com.gv.steel.system.user.dao.SysTeamDao;
import com.gv.steel.system.user.entity.SysFactory;
import com.gv.steel.system.user.entity.SysTeam;
import com.gv.steel.system.user.service.SysFactoryService;
import com.gv.steel.system.user.service.SysTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 班组管理 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SysTeamServiceImpl extends BaseServiceImpl<SysTeamDao, SysTeam> implements SysTeamService {

    private final SysFactoryService sysFactoryService;

    private final TreeNodeMapper treeNodeMapper;

    @Override
    public Page<SysTeam> getPage(Page<SysTeam> page, SysTeam team) {
        LambdaQueryWrapper<SysTeam> wrapper = getWrapper(team);
        return this.page(page, wrapper);
    }

    @Override
    public List<Tree<Long>> tree(List<Long> factoryIds) {
        List<SysFactory> factoryList = sysFactoryService.list(Wrappers.<SysFactory>lambdaQuery().in(CommonModel::getId, factoryIds));
        if (CollUtil.isEmpty(factoryList)) {
            throw new BaseException(MsgUtils.getMessage("factory.is.not.exist"));
        }
        List<TreeNode<Long>> treeNodeList = treeNodeMapper.convertFromFactoryList(factoryList);
        List<SysTeam> teamList = this.list(Wrappers.<SysTeam>lambdaQuery().in(SysTeam::getFactoryId, factoryList.stream().map(CommonModel::getId).collect(Collectors.toList())));
        if (CollUtil.isNotEmpty(teamList)) {
            treeNodeList.addAll(treeNodeMapper.convertFromTeamList(teamList));
        }
        if (CollUtil.isEmpty(treeNodeList)) {
            throw new BaseException(MsgUtils.getMessage("tree.is.not.exist"));
        }
        return TreeUtil.build(treeNodeList, CommonConstants.TREE_ROOT_ID);
    }

    @Override
    public List<SysTeam> listTeamByUserId(Long userId) {
        return baseDao.listTeamsByUserId(userId);
    }

    private LambdaQueryWrapper<SysTeam> getWrapper(SysTeam team) {
        LambdaQueryWrapper<SysTeam> wrapper = new LambdaQueryWrapper<>();
        if (team.getFactoryId() != null && team.getFactoryId() != 0) {
            wrapper.eq(SysTeam::getFactoryId, team.getFactoryId());
        }
        if (StrUtil.isNotEmpty(team.getTeamName())) {
            wrapper.like(SysTeam::getTeamName, team.getTeamName());
        }
        return wrapper;
    }
}
