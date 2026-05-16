package com.gv.steel.system.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.enums.MenuTypeEnum;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.dao.SysMenuDao;
import com.gv.steel.system.user.dao.SysRoleMenuDao;
import com.gv.steel.system.user.entity.SysMenu;
import com.gv.steel.system.user.entity.SysRoleMenu;
import com.gv.steel.system.user.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    private final SysRoleMenuDao sysRoleMenuDao;

    @Override
    public Set<SysMenu> findMenuByRoleId(Long roleId) {
        return baseMapper.listMenusByRoleId(roleId);
    }

    /**
     * 新增菜单信息（默认为超级管理员设置菜单权限）
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    @Override
    public boolean saveMenu(SysMenu sysMenu) {
        save(sysMenu);
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setMenuId(sysMenu.getId());
        sysRoleMenu.setRoleId(CommonConstants.SUPER_ROLE_ID);
        return sysRoleMenuDao.insert(sysRoleMenu) > 0;
    }

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return true成功, false失败
     */
    @Override
    public Boolean removeMenuById(Long id) {
        // 查询父节点为当前节点的节点
        List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id));

        Assert.isTrue(CollUtil.isEmpty(menuList), MsgUtils.getMessage(ErrorCodeConstants.SYS_MENU_DELETE_EXISTING));

        sysRoleMenuDao.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getMenuId, id));
        // 删除当前菜单及其子菜单
        return this.removeById(id);
    }

    @Override
    public Boolean updateMenuById(SysMenu sysMenu) {
        return this.updateById(sysMenu);
    }

    /**
     * 构建树查询 1. 不是懒加载情况，查询全部 2. 是懒加载，根据parentId 查询 2.1 父节点为空，则查询ID -1
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<Tree<Long>> treeMenu(boolean lazy, Long parentId) {
        if (!lazy) {
            List<TreeNode<Long>> collect = baseMapper
                    .selectList(Wrappers.<SysMenu>lambdaQuery().orderByAsc(SysMenu::getSortOrder))
                    .stream()
                    .map(getNodeFunction())
                    .collect(Collectors.toList());

            return TreeUtil.build(collect, CommonConstants.TREE_ROOT_ID);
        }

        Long parent = parentId == null ? CommonConstants.TREE_ROOT_ID : parentId;

        List<TreeNode<Long>> collect = baseMapper
                .selectList(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, parent).orderByAsc(SysMenu::getSortOrder))
                .stream()
                .map(getNodeFunction())
                .collect(Collectors.toList());

        return TreeUtil.build(collect, parent);
    }

    /**
     * 查询菜单
     *
     * @param all      全部菜单
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<Tree<Long>> filterMenu(Set<SysMenu> all, Long parentId) {
        List<TreeNode<Long>> collect = all.stream()
                .filter(menu -> Lists.newArrayList(MenuTypeEnum.DIRECTOR.getType(), MenuTypeEnum.MENU.getType()).contains(menu.getType()))
                .map(getNodeFunction())
                .collect(Collectors.toList());
        Long parent = parentId == null ? CommonConstants.TREE_ROOT_ID : parentId;
        return TreeUtil.build(collect, parent);
    }

    @Override
    public void clearMenuCache() {

    }

    @NotNull
    private Function<SysMenu, TreeNode<Long>> getNodeFunction() {
        return menu -> {
            TreeNode<Long> node = new TreeNode<>();
            node.setId(menu.getId());
            node.setName(menu.getName());
            node.setParentId(menu.getParentId());
            node.setWeight(menu.getSortOrder());
            // 扩展属性
            Map<String, Object> extra = BeanUtil.beanToMap(menu, "icon", "path", "code", "type", "permission", "label", "sortOrder", "keepAlive", "hidden", "component", "enable");
            node.setExtra(extra);
            return node;
        };
    }

}
