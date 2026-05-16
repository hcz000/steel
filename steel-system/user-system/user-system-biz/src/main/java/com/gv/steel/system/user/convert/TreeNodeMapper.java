package com.gv.steel.system.user.convert;

import cn.hutool.core.lang.tree.TreeNode;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.user.entity.SysFactory;
import com.gv.steel.system.user.entity.SysTeam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author SZB
 */
@Mapper(componentModel = CommonConstants.SPRING)
public interface TreeNodeMapper {
    /**
     * 工厂转换为树形节点
     *
     * @param factory 工厂
     * @return List<TreeNode < Long>>
     */
    @Mapping(target = "name", source = "factoryName")
    @Mapping(target = "parentId", expression = "java(com.gv.steel.common.core.constant.CommonConstants.TREE_ROOT_ID)")
    TreeNode<Long> convertFromFactoryList(SysFactory factory);

    /**
     * 工厂转换为树形节点
     *
     * @param factoryList 工厂数组
     * @return List<TreeNode < Long>>
     */
    List<TreeNode<Long>> convertFromFactoryList(List<SysFactory> factoryList);


    /**
     * 班组转换为树形节点
     *
     * @param team 班组数组
     * @return List<TreeNode < Long>>
     */
    @Mapping(target = "name", source = "teamName")
    @Mapping(target = "parentId", source = "factoryId")
    TreeNode<Long> convertFromTeamList(SysTeam team);

    /**
     * 班组转换为树形节点
     *
     * @param teamList 班组数组
     * @return List<TreeNode < Long>>
     */
    List<TreeNode<Long>> convertFromTeamList(List<SysTeam> teamList);
}
