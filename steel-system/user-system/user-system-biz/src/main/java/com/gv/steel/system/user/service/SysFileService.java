package com.gv.steel.system.user.service;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysFile;
import com.gv.steel.system.user.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件管理
 */
public interface SysFileService extends BaseService<SysFile> {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    Result<FileVO> uploadFile(MultipartFile file);

    Result<List<FileVO>> uploadFiles(List<MultipartFile> files);

    /**
     * 读取文件
     *
     * @param bucket   桶名称
     * @param fileName 文件名称
     * @param response 输出流
     */
    void getFile(String bucket, String fileName, HttpServletResponse response);

    /**
     * 通过文件ID读取文件
     *
     * @param id       文件ID
     * @param response 输出流
     */
    void downloadById(Long id, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    Boolean deleteFile(Long id);

    /**
     * 获取外网访问地址
     *
     * @param bucket
     * @param fileName
     * @return
     */
    String onlineFile(String bucket, String fileName);
}
