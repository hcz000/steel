package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.NoticeRead;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通知公告阅读情况表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Mapper
public interface NoticeReadDao extends BaseDao<NoticeRead> {

}
