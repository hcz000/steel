package com.gv.steel.system.user.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.collect.Lists;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.common.oss.core.OssTemplate;
import com.gv.steel.common.oss.properties.OssProperties;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.dao.SysFileDao;
import com.gv.steel.system.user.entity.SysFile;
import com.gv.steel.system.user.service.SysFileService;
import com.gv.steel.system.user.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * 文件管理
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SysFileServiceImpl extends BaseServiceImpl<SysFileDao, SysFile> implements SysFileService {

    private final OssProperties ossProperties;

    private final OssTemplate ossTemplate;

    /**
     * 上传文件
     *
     * @param file 文件流
     * @return 文件信息 错误信息
     */
    @Override
    public Result<FileVO> uploadFile(MultipartFile file) {
        FileVO fileVO;
        try {
            fileVO = build(file);
        } catch (BaseException be) {
            throw be;
        } catch (Exception e) {
            log.error("上传文件失败，e:", e);
            throw new BaseException(e.getLocalizedMessage());
        }
        return Result.ok(fileVO);
    }

    @Override
    public Result<List<FileVO>> uploadFiles(List<MultipartFile> files) {
        List<FileVO> fileVOList = Lists.newArrayList();
        for (MultipartFile file : files) {
            try {
                fileVOList.add(build(file));
            } catch (BaseException be) {
                throw be;
            } catch (Exception e) {
                log.error("上传文件失败，e:", e);
                throw new BaseException(e.getLocalizedMessage());
            }
        }
        return Result.ok(fileVOList);
    }

    private FileVO build(MultipartFile file) throws Exception {

        String extName = FileUtil.extName(file.getOriginalFilename());

        if (StrUtil.isBlank(extName)) {
            throw new BaseException(MsgUtils.getMessage(ErrorCodeConstants.SYS_FILE_MISSING_SUFFIX));
        }

        if (!ArrayUtil.containsIgnoreCase(ossProperties.getSuffix(), extName)) {
            throw new BaseException(MsgUtils.getMessage(ErrorCodeConstants.SYS_FILE_TYPE_NOT_ALLOWS_UPLOAD, extName));
        }

        String fileName = IdUtil.simpleUUID() + StrUtil.DOT + extName;
        FileVO fileVO = new FileVO();
        fileVO.setFileUrl(String.format("/user-api/sys-file/%s/%s", ossProperties.getBucketName(), fileName));
        fileVO.setOriginalFileName(file.getOriginalFilename());
        fileVO.setFileSize(file.getSize());
        fileVO.setFileName(fileName);
        fileVO.setType(extName);

        uploadServer(file, fileVO);

        return fileVO;
    }

    private void uploadServer(MultipartFile file, FileVO fileVO) throws Exception {
        ossTemplate.putObject(ossProperties.getBucketName(), fileVO.getFileName(),
                file.getInputStream(), file.getContentType());
        // 文件管理数据记录,收集管理追踪文件
        Long fileId = fileLog(file, fileVO.getFileName());
        fileVO.setFileId(fileId);
    }

    /**
     * 读取文件
     *
     * @param bucket   桶
     * @param fileName 文件名
     * @param response 响应流
     */
    @Override
    public void getFile(String bucket, String fileName, HttpServletResponse response) {
        try (S3Object s3Object = ossTemplate.getObject(bucket, fileName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件读取异常: {}", e.getLocalizedMessage());
        }
    }

    /**
     * 通过文件ID读取文件
     *
     * @param id       文件ID
     * @param response 输出流
     */
    @Override
    public void downloadById(Long id, HttpServletResponse response) {
        SysFile sysFile = getById(id);
        Optional.ofNullable(sysFile).orElseThrow(BaseException::new);
        getFile(sysFile.getBucketName(), sysFile.getFileName(), response);
    }

    /**
     * 删除文件
     *
     * @param id 文件id
     * @return ok fail
     */
    @Override
    @SneakyThrows
    public Boolean deleteFile(Long id) {
        SysFile file = this.getById(id);
        ossTemplate.removeObject(ossProperties.getBucketName(), file.getFileName());
        return this.removeById(id);
    }

    /**
     * 文件管理数据记录,收集管理追踪文件
     *
     * @param file     上传文件格式
     * @param fileName 文件名
     */
    private Long fileLog(MultipartFile file, String fileName) {
        SysFile sysFile = new SysFile();
        sysFile.setFileName(fileName);
        sysFile.setOriginal(file.getOriginalFilename());
        sysFile.setFileSize(file.getSize());
        sysFile.setType(FileUtil.extName(file.getOriginalFilename()));
        sysFile.setBucketName(ossProperties.getBucketName());
        this.save(sysFile);
        return sysFile.getId();
    }

    /**
     * 默认获取文件的在线地址
     *
     * @param bucket   桶
     * @param fileName 文件名
     * @return url
     */
    @Override
    public String onlineFile(String bucket, String fileName) {
        return ossTemplate.getObjectURL(bucket, fileName, 7);
    }

}
