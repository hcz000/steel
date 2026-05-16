package com.gv.steel.system.base.consumer;

import cn.hutool.core.lang.UUID;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.redis.utils.RedisUtil;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.common.rocketmq.enums.DelayLevelEnum;
import com.gv.steel.common.rocketmq.handler.EnhanceMessageHandler;
import com.gv.steel.system.base.constant.RocketMQConstant;
import com.gv.steel.system.base.dto.TaskNoticeStatusUpdateMessageDTO;
import com.gv.steel.system.base.service.TaskNoticeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 任务通知状态更新消息消费者
 */
@Slf4j
@Service
@RocketMQMessageListener(
        topic = RocketMQConstant.PUSH_TASK_NOTICE_STATUS_UPDATE_TOPIC,
        consumerGroup = RocketMQConstant.PUSH_TASK_NOTICE_STATUS_UPDATE_GROUP,
        consumeMode = ConsumeMode.ORDERLY
)
public class TaskNoticeStatusUpdateMessageConsumer extends EnhanceMessageHandler<TaskNoticeStatusUpdateMessageDTO, BaseMessage<TaskNoticeStatusUpdateMessageDTO>> implements RocketMQListener<BaseMessage<TaskNoticeStatusUpdateMessageDTO>> {
    /**
     * 重复消息检查时间，1H
     */
    private final static Long EXPIRE_TIME = 1L;

    private final TaskNoticeService taskNoticeService;

    public TaskNoticeStatusUpdateMessageConsumer(RocketMQEnhanceTemplate rocketMQEnhanceTemplate,
                                                 TaskNoticeService taskNoticeService) {
        super(rocketMQEnhanceTemplate);
        this.taskNoticeService = taskNoticeService;
    }

    @Override
    @SneakyThrows
    protected void handleMessage(BaseMessage<TaskNoticeStatusUpdateMessageDTO> message) {
        // 1小时内不允许重复发送
        RedisUtil.setCacheObject(
                String.format(RocketMQConstant.REDIS_IDEMPOTENT_CHECK_KEY, message.getKey()),
                true,
                Duration.ofHours(EXPIRE_TIME)
        );
        try {
            taskNoticeService.updateTaskNoticeStatus(message.getBody());
        } catch (BaseException e) {
            message.setKey(UUID.randomUUID().toString(true));
            throw new RuntimeException("处理失败");
        }
    }

    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean throwException() {
        return false;
    }

    @Override
    protected int getDelayLevel() {
        return DelayLevelEnum.DELAY_10_SECONDS.getLevel();
    }

    /**
     * 过滤消息，防止重复消费
     *
     * @param message 待处理消息
     * @return true: 处理消息，false: 过滤消息
     */
    @Override
    protected boolean filter(BaseMessage<TaskNoticeStatusUpdateMessageDTO> message) {
        boolean repeatConsume = RedisUtil.hasKey(String.format(RocketMQConstant.REDIS_IDEMPOTENT_CHECK_KEY, message.getKey()));
        if (repeatConsume) {
            log.warn("topic: [{}], 重复消费[{}]消息，过滤消息！", RocketMQConstant.PUSH_TASK_NOTICE_STATUS_UPDATE_GROUP, message.getKey());
        }
        return repeatConsume;
    }

    @Override
    public void onMessage(BaseMessage<TaskNoticeStatusUpdateMessageDTO> message) {
        dispatchMessage(message);
    }
}
