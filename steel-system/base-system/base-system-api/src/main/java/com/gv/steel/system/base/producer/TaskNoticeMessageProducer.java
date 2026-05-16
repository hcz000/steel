package com.gv.steel.system.base.producer;

import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.system.base.constant.RocketMQConstant;
import com.gv.steel.system.base.dto.TaskNoticeMessageDTO;
import com.gv.steel.system.base.dto.TaskNoticeStatusUpdateMessageDTO;
import com.gv.steel.system.im.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务通知消息生产者
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(RocketMQEnhanceTemplate.class)
public class TaskNoticeMessageProducer {

    private final RocketMQEnhanceTemplate rocketMQEnhanceTemplate;

    /**
     * 发送任务通知消息
     *
     * @param taskNoticeMessageDTO 消息体
     */
    public void sendTaskNoticeMessage(List<TaskNoticeMessageDTO> taskNoticeMessageDTO) {
        BaseMessage<List<TaskNoticeMessageDTO>> baseMessage = new BaseMessage<>(taskNoticeMessageDTO);
        rocketMQEnhanceTemplate.asyncSend(RocketMQConstant.PUSH_TASK_NOTICE_TOPIC, RocketMQConstant.DEFAULT_TAG, baseMessage);
    }

    /**
     * 发送任务通知状态更新消息
     *
     * @param taskNoticeStatusUpdateMessageDTO 消息体
     */
    public void sendTaskNoticeStatusUpdateMessage(TaskNoticeStatusUpdateMessageDTO taskNoticeStatusUpdateMessageDTO) {
        BaseMessage<TaskNoticeStatusUpdateMessageDTO> baseMessage = new BaseMessage<>(taskNoticeStatusUpdateMessageDTO);
        rocketMQEnhanceTemplate.asyncSend(RocketMQConstant.PUSH_TASK_NOTICE_STATUS_UPDATE_TOPIC, RocketMQConstant.DEFAULT_TAG, baseMessage);
    }

    /**
     * 推送im消息
     *
     * @param messageDTO 消息体
     */
    public void sendIMMessage(MessageDTO messageDTO) {
        BaseMessage<MessageDTO> baseMessage = new BaseMessage<>(messageDTO);
        rocketMQEnhanceTemplate.asyncSend(RocketMQConstant.IM_PUSH_MSG_TOPIC, RocketMQConstant.DEFAULT_TAG, baseMessage);
    }
}
