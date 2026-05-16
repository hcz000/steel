package com.gv.steel.system.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.config.TaskExecutorConfiguration;
import com.gv.steel.common.core.lock.CommonLock;
import com.gv.steel.common.core.lock.DistributedLock;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.NoticeReadDao;
import com.gv.steel.system.base.entity.NoticeRead;
import com.gv.steel.system.base.enums.ReadFlagEnum;
import com.gv.steel.system.base.service.NoticeReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 通知公告阅读情况表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeReadServiceImpl extends BaseServiceImpl<NoticeReadDao, NoticeRead> implements NoticeReadService {
    private final static String LOCK_KEY = "notice_read_lock:%d:%d";
    private final static int LOCK_TIMEOUT = 1000;

    private final DistributedLock distributedLock;

    @Override
    @Async(TaskExecutorConfiguration.EXECUTOR_BEAN_NAME)
    public void updateReadFlag(Long noticeId, Long userId) {
        String lockKey = String.format(LOCK_KEY, noticeId, userId);
        try (CommonLock commonLock = distributedLock.tryLock(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
            if (commonLock == null) return;
            if (exists(Wrappers.<NoticeRead>lambdaQuery().eq(NoticeRead::getNoticeId, noticeId).eq(NoticeRead::getUserId, userId)))
                return;
            save(new NoticeRead(noticeId, userId, ReadFlagEnum.READ.getParam()));
        } catch (Exception e) {
            log.error("更新通知公告阅读情况表失败", e);
        }
    }
}
