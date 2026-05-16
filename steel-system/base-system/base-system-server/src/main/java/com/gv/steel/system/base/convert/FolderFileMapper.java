package com.gv.steel.system.base.convert;

import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.base.entity.Folder;
import com.gv.steel.system.base.entity.ShareFile;
import com.gv.steel.system.base.vo.FolderFileVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FolderFileMapper {
    @Mapping(source = "folderName", target = "name")
    @Mapping(target = "type", expression = "java(com.gv.steel.system.base.enums.FolderFileEnum.FOLDER.getParam())")
    FolderFileVO convertFolder(Folder folder);

    List<FolderFileVO> convertFolderList(List<Folder> folders);

    @Mapping(source = "attachName", target = "name")
    @Mapping(source = "attachType", target = "fileType")
    @Mapping(source = "attachSize", target = "fileSize")
    @Mapping(target = "type", expression = "java(com.gv.steel.system.base.enums.FolderFileEnum.FILE.getParam())")
    FolderFileVO convertFolder(ShareFile file);

    List<FolderFileVO> convertFileList(List<ShareFile> folders);
}
