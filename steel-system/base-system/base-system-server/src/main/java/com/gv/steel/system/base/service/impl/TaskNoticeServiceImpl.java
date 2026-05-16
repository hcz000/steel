package com.gv.steel.system.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.core.util.ResultOps;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.TaskNoticeDao;
import com.gv.steel.system.base.dto.TaskNoticeStatusUpdateMessageDTO;
import com.gv.steel.system.base.entity.TaskNotice;
import com.gv.steel.system.base.enums.TaskNoticeStatusEnum;
import com.gv.steel.system.base.producer.TaskNoticeMessageProducer;
import com.gv.steel.system.base.service.TaskNoticeService;
import com.gv.steel.system.im.constant.ActionConstant;
import com.gv.steel.system.im.constant.ContentFormat;
import com.gv.steel.system.im.dto.MessageDTO;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.feign.RemoteUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务通知表(收发件信箱) 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskNoticeServiceImpl extends BaseServiceImpl<TaskNoticeDao, TaskNotice> implements TaskNoticeService {
    private final RemoteUserService remoteUserService;

    private final ObjectMapper objectMapper;

    private final TaskNoticeMessageProducer messageProducer;

    @Override
    @SneakyThrows
    public boolean saveNotice(List<TaskNotice> taskNoticeList) {
        List<Long> tableIds = taskNoticeList.stream().map(TaskNotice::getTableId).distinct().collect(Collectors.toList());
        if (tableIds.size() > 1) {
            log.warn("多个业务的待办任务不能同时处理");
            return false;
        }
        Long tableId = taskNoticeList.get(0).getTableId();
        String tableName = taskNoticeList.get(0).getTableName();

        List<TaskNotice> dbList = list(Wrappers.<TaskNotice>lambdaQuery()
                .eq(TaskNotice::getTableId, tableId)
                .eq(TaskNotice::getTableName, tableName)
        );

        if (CollUtil.isEmpty(dbList)) {
            setReceiveName(taskNoticeList);
            boolean result = saveBatch(taskNoticeList);
            if (result) {
                sendNotice(taskNoticeList);
            }
            return result;
        }

        List<Long> receiveIds = taskNoticeList.stream()
                .map(TaskNotice::getReceiveId).collect(Collectors.toList());

        // 删除的记录
        List<Long> delIds = dbList.stream()
                .filter(taskNotice -> !receiveIds.contains(taskNotice.getReceiveId()))
                .map(TaskNotice::getId).collect(Collectors.toList());

        // 先删除再保存，防止重复
        if (CollUtil.isNotEmpty(delIds)) {
            baseDao.deleteByPhysics(delIds);
        }

        List<Long> dbReceiveIds = dbList.stream().map(TaskNotice::getReceiveId).collect(Collectors.toList());

        // 新增的记录
        List<TaskNotice> addList = taskNoticeList.stream()
                .filter(taskNotice -> !dbReceiveIds.contains(taskNotice.getReceiveId()))
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(addList)) {
            log.warn("通知已存在，不再处理！");
            return true;
        }

        setReceiveName(addList);

        boolean result = saveBatch(addList);

        if (result) {
            sendNotice(addList);
        }
        return result;
    }

    public void sendNotice(List<TaskNotice> taskNotices) {
        taskNotices.forEach(task -> {
            // 发送IM消息到IM服务
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setReceiver(Long.toString(task.getReceiveId()));
            messageDTO.setSender(Long.toString(task.getCreateBy()));
            messageDTO.setTitle(MsgUtils.getMessage("new.task.notice.title"));
            messageDTO.setAction(ActionConstant.NEW_TASK_NOTICE_ACTION);
            messageDTO.setFormat(ContentFormat.JSON);
            try {
                messageDTO.setContent(objectMapper.writeValueAsString(task));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            messageProducer.sendIMMessage(messageDTO);
        });
    }

    @Override
    public boolean updateTaskNoticeStatus(TaskNoticeStatusUpdateMessageDTO body) {

        // 获取待办的任务，如果不存在则不处理
        TaskNotice taskNotice = getOne(Wrappers.<TaskNotice>lambdaQuery()
                .eq(TaskNotice::getTableId, body.getTableId())
                .eq(TaskNotice::getTableName, body.getTableName())
                .eq(TaskNotice::getReceiveId, body.getReceiveId()));

        if (ObjUtil.isNull(taskNotice)) {
            throw new BaseException(String.format("任务不存在，tableId: %d, tableName: %s, receivedId: %d，抛出异常等待重试。", body.getTableId(), body.getTableName(), body.getReceiveId()));
        }

        // 已处理的任务，不处理
        if (TaskNoticeStatusEnum.PENDING.getStatus() != taskNotice.getStatus()) {
            return true;
        }

        // 作废，则删除待办
        if (TaskNoticeStatusEnum.INVALID.getStatus() == body.getStatus()) {
            return removeById(taskNotice);
        }

        // 更新任务状态
        taskNotice.setStatus(body.getStatus());
        taskNotice.setFinishTime(body.getFinishTime());
        updateById(taskNotice);
        // 通知任务发起人审批结果
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(taskNotice.getId());
        messageDTO.setFormat(ContentFormat.JSON);
        messageDTO.setTitle(MsgUtils.getMessage("task.notice.handled.title", taskNotice.getTitle(), TaskNoticeStatusEnum.getDesc(body.getStatus())));
        try {
            messageDTO.setContent(objectMapper.writeValueAsString(taskNotice));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        messageDTO.setAction(ActionConstant.TASK_NOTICE_STATUS_UPDATE_ACTION);
        messageDTO.setSender(String.valueOf(taskNotice.getReceiveId()));
        messageDTO.setReceiver(String.valueOf(taskNotice.getCreateBy()));
        messageProducer.sendIMMessage(messageDTO);
        // 通知任务处理人更新待办栏
        messageDTO = new MessageDTO();
        messageDTO.setReceiver(String.valueOf(taskNotice.getReceiveId()));
        messageDTO.setSender(String.valueOf(taskNotice.getCreateBy()));
        messageDTO.setAction(ActionConstant.TASK_DONE_ACTION);
        messageDTO.setFormat(ContentFormat.JSON);
        try {
            messageDTO.setContent(objectMapper.writeValueAsString(taskNotice));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        messageProducer.sendIMMessage(messageDTO);

        return true;
    }

    private void setReceiveName(List<TaskNotice> addList) {
        addList.forEach(taskNotice -> {
            Result<SysUser> userResult = remoteUserService.getBaseInfo(taskNotice.getReceiveId());
            ResultOps.of(userResult).getData()
                    .ifPresent(
                            userInfo -> taskNotice.setReceiveName(userInfo.getNickname())
                    );
            taskNotice.setStatus(TaskNoticeStatusEnum.PENDING.getStatus());
        });
    }
}
