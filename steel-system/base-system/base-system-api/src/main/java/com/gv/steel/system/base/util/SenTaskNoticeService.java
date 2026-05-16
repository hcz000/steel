package com.gv.steel.system.base.util;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.system.base.dto.TaskNoticeMessageDTO;
import com.gv.steel.system.base.dto.TaskNoticeStatusUpdateMessageDTO;
import com.gv.steel.system.base.enums.TaskNoticeStatusEnum;
import com.gv.steel.system.base.producer.TaskNoticeMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SenTaskNoticeService {

    private final TaskNoticeMessageProducer producer;

    private final ThreadPoolTaskExecutor asyncExecutor;

    private final ObjectMapper objectMapper;

    /**
     * 构建任务通知消息
     *
     * @param tableId   业务表ID
     * @param clazz     业务表实体类
     * @param title     任务标题
     * @param url       任务链接
     * @param receiveId 接收人
     * @param param     参数
     * @return task-notice
     */
    @SneakyThrows
    public TaskNoticeMessageDTO buildMessage(Long tableId, Class<?> clazz, String title, String url, Long receiveId, Map<String, Object> param) {
        TaskNoticeMessageDTO taskNotice = new TaskNoticeMessageDTO();
        taskNotice.setTableId(tableId);
        taskNotice.setTableName(TableInfoHelper.getTableInfo(clazz).getTableName());
        taskNotice.setTitle(title);
        taskNotice.setUrl(url);
        taskNotice.setParams(objectMapper.writeValueAsString(param));
        taskNotice.setReceiveId(receiveId);
        taskNotice.setCreateBy(UserInfoContextHolder.currentUserId());
        taskNotice.setCreateName(UserInfoContextHolder.currentNickName());
        return taskNotice;
    }

    /**
     * 发送任务通知
     *
     * @param taskNoticeList 通知列表
     */
    public void sendTaskNotice(List<TaskNoticeMessageDTO> taskNoticeList) {
        asyncExecutor.execute(() -> producer.sendTaskNoticeMessage(taskNoticeList));
    }

    /**
     * 发送任务完成通知到用户
     *
     * @param tableId 业务主键ID
     * @param clazz   业务表名
     */
    public void sendFinishTaskNotice(Long tableId, Class<?> clazz, TaskNoticeStatusEnum status, Long receiveId) {
        TaskNoticeStatusUpdateMessageDTO finishMessage = new TaskNoticeStatusUpdateMessageDTO();
        finishMessage.setFinishTime(LocalDateTime.now());
        finishMessage.setStatus(status.getStatus());
        finishMessage.setReceiveId(receiveId);
        finishMessage.setTableId(tableId);
        finishMessage.setTableName(TableInfoHelper.getTableInfo(clazz).getTableName());
        asyncExecutor.execute(() -> producer.sendTaskNoticeStatusUpdateMessage(finishMessage));
    }
}
