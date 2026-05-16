package com.gv.steel.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysDeptRelationDao;
import com.gv.steel.system.user.entity.SysDept;
import com.gv.steel.system.user.entity.SysDeptRelation;
import com.gv.steel.system.user.service.SysDeptRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SysDeptRelationServiceImpl extends BaseServiceImpl<SysDeptRelationDao, SysDeptRelation>
        implements SysDeptRelationService {

    /**
     * 维护部门关系
     *
     * @param sysDept 部门
     */
    @Override
    public void saveDeptRelation(SysDept sysDept) {
        // 增加部门关系表
        List<SysDeptRelation> relationList = baseDao.selectList(
                        Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
                .stream()
                .map(relation -> {
                    relation.setDescendant(sysDept.getId());
                    return relation;
                })
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relationList)) {
            this.saveBatch(relationList);
        }

        // 自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getId());
        own.setAncestor(sysDept.getId());
        baseDao.insert(own);
    }

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    @Override
    public void removeDeptRelationById(Long id) {
        baseMapper.deleteDeptRelationsById(id);
    }

    /**
     * 更新部门关系
     *
     * @param relation
     */
    @Override
    public void updateDeptRelation(SysDeptRelation relation) {
        baseMapper.deleteDeptRelations(relation);
        baseMapper.insertDeptRelations(relation);
    }

}
