package com.gv.steel.common.mybatis.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.common.mybatis.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseServiceImpl<M extends BaseDao<T>, T extends Model<T>> extends ServiceImpl<M, T> implements BaseService<T> {
    @Autowired
    public M baseDao;

    @Override
    public boolean insertBatchColumns(Collection<T> list, int batchSize) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }

        List<List<T>> split = CollUtil.split(list, batchSize);
        for (List<T> batchList : split) {
            this.baseDao.insertBatchSomeColumn(batchList);
        }
        return true;
    }

    @Override
    public <R> boolean subtractSaveOrUpdateBatch(R relationKey, List<T> list, SFunction<T, R> relationFieldFunction, SFunction<T, R> primaryKeyFieldFunction) {
        // list 为空，删除 目标对象下的关联的数据
        if (CollUtil.isEmpty(list)) {
            remove(Wrappers.<T>lambdaQuery().eq(relationFieldFunction, relationKey));
            return true;
        }

        // db list 为空，批量新增list
        List<T> dbList = list(Wrappers.<T>lambdaQuery().eq(relationFieldFunction, relationKey));
        if (CollUtil.isEmpty(dbList)) {
            baseDao.insertBatchSomeColumn(list);
            return true;
        }

        // 取db差集进行删除
        Set<R> dbIds = dbList.stream().map(primaryKeyFieldFunction).collect(Collectors.toSet());
        Set<R> updateIds = list.stream().map(primaryKeyFieldFunction).filter(ObjUtil::isNotNull).collect(Collectors.toSet());
        Collection<R> delIds = CollUtil.subtract(dbIds, updateIds);
        if (CollUtil.isNotEmpty(delIds)) {
            removeBatchByIds(delIds);
        }

        // 新增或修改其他项
        return saveOrUpdateBatch(list);
    }
}
