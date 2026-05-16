package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.Folder;
import com.gv.steel.system.base.vo.FolderFileVO;

import java.util.List;

/**
 * <p>
 * 共享文件夹表 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
public interface FolderService extends BaseService<Folder> {

    /**
     * 列出文件夹下的文件和文件夹
     *
     * @param folderId 目录ID
     * @return 文件和文件夹列表
     */
    List<FolderFileVO> listFolderFile(Long folderId);

    /**
     * 通过文件查询共享文件列表
     *
     * @param fileName 文件名
     * @return 共享文件列表
     */
    List<FolderFileVO> getListByName(String fileName);

    /**
     * 递归删除文件夹
     *
     * @param id 文件夹ID
     * @return 是否删除成功
     */
    boolean removeFolderById(Long id);

    boolean saveFolder(Folder folder);

    boolean updateFolderById(Folder folder);
}
