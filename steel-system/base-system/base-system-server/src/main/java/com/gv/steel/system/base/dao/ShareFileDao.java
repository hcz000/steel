package com.gv.steel.system.base.dao;

import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.entity.ShareFile;
import com.gv.steel.system.base.vo.FolderFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 共享文件表 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Mapper
public interface ShareFileDao extends BaseDao<ShareFile> {

    List<FolderFileVO> selectFileListByName(@Param("fileName") String fileName);
}
