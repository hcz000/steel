package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.ShareFile;
import com.gv.steel.system.base.vo.FolderFileVO;

import java.util.List;

/**
 * <p>
 * 共享文件表 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
public interface ShareFileService extends BaseService<ShareFile> {

    List<FolderFileVO> findFileByName(String fileName);

    boolean saveShareFile(ShareFile shareFile);

    boolean batchSaveShareFile(List<ShareFile> shareFiles);
}
