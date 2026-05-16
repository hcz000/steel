package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.entity.NoticeRead;

/**
 * <p>
 * 通知公告阅读情况表 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
public interface NoticeReadService extends BaseService<NoticeRead> {
    void updateReadFlag(Long noticeId, Long userId);
}
