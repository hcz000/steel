package com.gv.steel.system.base.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.NoticeDao;
import com.gv.steel.system.base.dto.NoticeQueryDTO;
import com.gv.steel.system.base.entity.Notice;
import com.gv.steel.system.base.enums.NoticePublishStatusEnum;
import com.gv.steel.system.base.service.NoticeReadService;
import com.gv.steel.system.base.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 通知公告 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl extends BaseServiceImpl<NoticeDao, Notice> implements NoticeService {

    private final NoticeReadService noticeReadService;

    @Override
    public PageResult<Notice> getPage(Page<Notice> page, NoticeQueryDTO dto) {
        if (!ArrayUtil.contains(Objects.requireNonNull(UserInfoContextHolder.get()).getPermissions(), EDIT_PERMISSION_ID)) {
            dto.setPublishStatus(NoticePublishStatusEnum.PUBLISHED.getParam());
        }
        dto.setUserId(UserInfoContextHolder.currentUserId());
        baseDao.getPage(page, dto);
        return PageResult.<Notice>builder().build().pageResult(page);
    }

    @Override
    public Notice getDetailById(Long id) {
        // 根据id查询详情，并设置阅读状态
        Notice notice = getById(id);
        // 查询详情，更新已读状态(异步更新)
        noticeReadService.updateReadFlag(id, UserInfoContextHolder.currentUserId());
        return notice;
    }
}
