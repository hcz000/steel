package com.gv.steel.system.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.convert.FolderFileMapper;
import com.gv.steel.system.base.dao.FolderDao;
import com.gv.steel.system.base.entity.Folder;
import com.gv.steel.system.base.entity.ShareFile;
import com.gv.steel.system.base.service.FolderService;
import com.gv.steel.system.base.service.ShareFileService;
import com.gv.steel.system.base.vo.FolderFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 共享文件夹表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FolderServiceImpl extends BaseServiceImpl<FolderDao, Folder> implements FolderService {

    private final ShareFileService shareFileService;

    private final FolderFileMapper folderFileMapper;

    @Override
    public List<FolderFileVO> listFolderFile(Long folderId) {
        List<FolderFileVO> voList = Lists.newArrayList();
        List<Folder> folderList = list(Wrappers.<Folder>lambdaQuery().eq(Folder::getParentId, folderId).orderByAsc(Folder::getOrderSort));
        voList.addAll(folderFileMapper.convertFolderList(folderList));
        List<ShareFile> fileList = shareFileService.list(Wrappers.<ShareFile>lambdaQuery().eq(ShareFile::getFolderId, folderId));
        voList.addAll(folderFileMapper.convertFileList(fileList));
        return voList;
    }

    @Override
    public List<FolderFileVO> getListByName(String fileName) {
        return shareFileService.findFileByName(fileName);
    }

    @Override
    public boolean removeFolderById(Long id) {
        List<Folder> folderList = list(Wrappers.<Folder>lambdaQuery().eq(Folder::getId, id));
        getFoldersRecursively(id, folderList);

        if (CollUtil.isNotEmpty(folderList)) {
            List<Long> folderIds = folderList.stream().map(Folder::getId).collect(Collectors.toList());
            removeByIds(folderIds);
            shareFileService.remove(Wrappers.<ShareFile>lambdaQuery().in(ShareFile::getFolderId, folderIds));
        }
        return true;
    }

    @Override
    public boolean saveFolder(Folder folder) {
        if (CommonConstants.TREE_ROOT_ID == folder.getParentId()) {
            folder.setParentIdGroup(CommonConstants.TREE_ROOT_ID + "");
            folder.setParentNameGroup("共享文件");
        } else {
            Folder parentFolder = getById(folder.getParentId());
            folder.setParentIdGroup(parentFolder.getParentIdGroup() + "," + parentFolder.getId());
            folder.setParentNameGroup(parentFolder.getParentNameGroup() + "," + parentFolder.getFolderName());
        }

        return save(folder);
    }

    @Override
    public boolean updateFolderById(Folder folder) {
        Folder oldFolder = getById(folder.getId());

        if (!StrUtil.equals(oldFolder.getFolderName(), folder.getFolderName())) {
            updateRelation(folder);
        }

        return updateById(folder);
    }

    private void updateRelation(Folder folder) {
        List<Folder> relFolderList = list(Wrappers.<Folder>lambdaQuery()
                .like(Folder::getParentIdGroup, folder.getId().toString()));

        if (CollUtil.isNotEmpty(relFolderList)) {
            String[] split = relFolderList.get(0).getParentIdGroup().split(",");
            int index = ArrayUtil.indexOf(split, folder.getId().toString());
            relFolderList.forEach(relFolder -> {
                String[] nameGroup = relFolder.getParentNameGroup().split(",");
                nameGroup[index] = folder.getFolderName();
                relFolder.setParentNameGroup(String.join(",", nameGroup));
            });
            updateBatchById(relFolderList);
        }

        List<ShareFile> relShareFileList = shareFileService.list(Wrappers.<ShareFile>lambdaQuery()
                .like(ShareFile::getFolderIdGroup, folder.getId().toString()));

        if (CollUtil.isNotEmpty(relShareFileList)) {
            String[] split = relShareFileList.get(0).getFolderIdGroup().split(",");
            int index = ArrayUtil.indexOf(split, folder.getId().toString());
            relShareFileList.forEach(relShareFile -> {
                String[] nameGroup = relShareFile.getFolderNameGroup().split(",");
                nameGroup[index] = folder.getFolderName();
                relShareFile.setFolderNameGroup(String.join(",", nameGroup));
            });
            shareFileService.updateBatchById(relShareFileList);
        }
    }

    /**
     * 递归获取所有文件夹
     *
     * @param folderId   文件夹id
     * @param folderList 文件夹列表
     */
    private void getFoldersRecursively(Long folderId, List<Folder> folderList) {
        List<Folder> folderList1 = list(Wrappers.<Folder>lambdaQuery().eq(Folder::getParentId, folderId).orderByAsc(Folder::getOrderSort));
        folderList1.forEach(folder -> {
            getFoldersRecursively(folderId, folderList);
        });
        folderList.addAll(folderList1);
    }
}
