package com.xxl.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface XxlJobLockDao {
    String selectForUpdate(@Param("lockName") String lockName);
}
