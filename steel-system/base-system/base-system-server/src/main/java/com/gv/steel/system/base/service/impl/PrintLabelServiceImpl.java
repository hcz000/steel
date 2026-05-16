package com.gv.steel.system.base.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.PrintLabelDao;
import com.gv.steel.system.base.entity.PrintLabel;
import com.gv.steel.system.base.service.PrintLabelService;
import com.gv.steel.system.user.feign.RemoteFileService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * <p>
 * 打印标签 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PrintLabelServiceImpl extends BaseServiceImpl<PrintLabelDao, PrintLabel> implements PrintLabelService {
    private final RemoteFileService remoteFileService;

    @Override
    @SneakyThrows
    public void getTemplateById(Long id, HttpServletResponse response) {
        PrintLabel printLabel = getById(id);
        Optional.ofNullable(printLabel).orElseThrow(BaseException::new);
        responseTemplate(printLabel, response);
    }

    @Override
    @SneakyThrows
    public void getTemplateByCode(String code, HttpServletResponse response) {
        PrintLabel printLabel = getOne(
                Wrappers.<PrintLabel>lambdaQuery()
                        .eq(PrintLabel::getLabelCode, code)
        );
        Optional.ofNullable(printLabel).orElseThrow(BaseException::new);
        responseTemplate(printLabel, response);
    }

    private void responseTemplate(PrintLabel printLabel, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream; charset=UTF-8");
        try (Response feignResponse = remoteFileService.download(printLabel.getLabelFileId());
             InputStream is = feignResponse.body().asInputStream();
             ServletOutputStream os = response.getOutputStream()) {
            IoUtil.copy(is, os);
        }

    }
}
