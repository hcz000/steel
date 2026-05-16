package com.gv.steel.common.mybatis.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.List;

public interface BaseDao<T extends Model<T>> extends BaseMapper<T> {
    int insertBatchSomeColumn(List<T> collects);
}
