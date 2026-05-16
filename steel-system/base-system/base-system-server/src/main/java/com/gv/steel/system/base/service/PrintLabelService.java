package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.PrintLabel;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 打印标签 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface PrintLabelService extends BaseService<PrintLabel> {

    /**
     * 通过标签ID获取模板
     *
     * @param id 标签ID
     */
    void getTemplateById(Long id, HttpServletResponse response);

    /**
     * 通过标签code获取模板
     *
     * @param code 标签code
     */
    void getTemplateByCode(String code, HttpServletResponse response);
}
