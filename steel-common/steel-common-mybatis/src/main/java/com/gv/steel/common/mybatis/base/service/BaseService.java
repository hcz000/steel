package com.gv.steel.common.mybatis.base.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

public interface BaseService<T> extends IService<T> {
    /**
     * 批次批量新增
     *
     * @param list      all list
     * @param batchSize batch size
     * @return result true or false
     */
    boolean insertBatchColumns(Collection<T> list, int batchSize);

    /**
     * <p>差量新增修改</p>
     * <p>list为空，删除数据库中的数据</p>
     * <p>需手动设置操作集合的关联值</p>
     *
     * @param relationKey             关联项
     * @param list                    操作的集合项
     * @param relationFieldFunction   关联项 属性的lambda表达式
     * @param primaryKeyFieldFunction 目标对象的 主键 属性的lambda表达式
     * @param <R>                     关联项的泛型
     * @return result true of false
     */
    <R> boolean subtractSaveOrUpdateBatch(R relationKey, List<T> list, SFunction<T, R> relationFieldFunction, SFunction<T, R> primaryKeyFieldFunction);
}
