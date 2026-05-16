package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.TaskNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务通知表(收发件信箱) Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Mapper
public interface TaskNoticeDao extends BaseDao<TaskNotice> {

    boolean deleteByPhysics(@Param("taskIds") List<Long> taskIds);
}
