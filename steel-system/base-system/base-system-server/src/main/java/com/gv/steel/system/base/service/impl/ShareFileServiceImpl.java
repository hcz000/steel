package com.gv.steel.system.base.service.impl;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.FolderDao;
import com.gv.steel.system.base.dao.ShareFileDao;
import com.gv.steel.system.base.entity.Folder;
import com.gv.steel.system.base.entity.ShareFile;
import com.gv.steel.system.base.service.ShareFileService;
import com.gv.steel.system.base.vo.FolderFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 共享文件表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShareFileServiceImpl extends BaseServiceImpl<ShareFileDao, ShareFile> implements ShareFileService {

    private final FolderDao folderDao;

    @Override
    public List<FolderFileVO> findFileByName(String fileName) {
        return baseDao.selectFileListByName(fileName);
    }

    @Override
    public boolean saveShareFile(ShareFile shareFile) {
        buildFolderPath(shareFile);
        return save(shareFile);
    }

    @Override
    public boolean batchSaveShareFile(List<ShareFile> shareFiles) {
        shareFiles.forEach(this::buildFolderPath);
        return saveBatch(shareFiles);
    }

    private void buildFolderPath(ShareFile shareFile) {
        if (CommonConstants.TREE_ROOT_ID == shareFile.getFolderId()) {
            shareFile.setFolderIdGroup(CommonConstants.TREE_ROOT_ID + "");
            shareFile.setFolderNameGroup("共享文件");
        } else {
            Folder folder = folderDao.selectById(shareFile.getFolderId());
            shareFile.setFolderIdGroup(folder.getParentIdGroup() + "," + folder.getId());
            shareFile.setFolderNameGroup(folder.getParentNameGroup() + "," + folder.getFolderName());
        }
    }
}
