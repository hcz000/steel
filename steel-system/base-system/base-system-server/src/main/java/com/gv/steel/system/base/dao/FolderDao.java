package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.Folder;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 共享文件夹表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Mapper
public interface FolderDao extends BaseDao<Folder> {

}
