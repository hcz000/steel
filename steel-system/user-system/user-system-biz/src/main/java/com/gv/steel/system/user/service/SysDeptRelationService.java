package com.gv.steel.system.user.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysDept;
import com.gv.steel.system.user.entity.SysDeptRelation;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface SysDeptRelationService extends BaseService<SysDeptRelation> {

    /**
     * 新建部门关系
     *
     * @param sysDept 部门
     */
    void saveDeptRelation(SysDept sysDept);

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    void removeDeptRelationById(Long id);

    /**
     * 更新部门关系
     *
     * @param relation
     */
    void updateDeptRelation(SysDeptRelation relation);

}
