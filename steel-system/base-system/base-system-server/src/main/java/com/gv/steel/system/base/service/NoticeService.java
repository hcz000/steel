package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.NoticeQueryDTO;
import com.gv.steel.system.base.entity.Notice;

/**
 * <p>
 * 通知公告 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
public interface NoticeService extends BaseService<Notice> {
    String EDIT_PERMISSION_ID = "notice_edit";

    /**
     * 通知公告分页列表
     *
     * @param page 分页参数
     * @return Page<Notice>
     */
    PageResult<Notice> getPage(Page<Notice> page, NoticeQueryDTO dto);

    Notice getDetailById(Long id);
}
