package com.gv.steel.system.base.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.steel.common.redis.utils.RedisUtil;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.common.rocketmq.handler.EnhanceMessageHandler;
import com.gv.steel.system.base.constant.RocketMQConstant;
import com.gv.steel.system.base.convert.TaskNoticeMapper;
import com.gv.steel.system.base.dto.TaskNoticeMessageDTO;
import com.gv.steel.system.base.entity.TaskNotice;
import com.gv.steel.system.base.producer.TaskNoticeMessageProducer;
import com.gv.steel.system.base.service.TaskNoticeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 任务通知消息消费者
 */
@Slf4j
@Service
@RocketMQMessageListener(
        topic = RocketMQConstant.PUSH_TASK_NOTICE_TOPIC,
        consumerGroup = RocketMQConstant.PUSH_TASK_NOTICE_CONSUMER_GROUP,
        consumeMode = ConsumeMode.ORDERLY
)
public class TaskNoticeMessageConsumer extends EnhanceMessageHandler<List<TaskNoticeMessageDTO>, BaseMessage<List<TaskNoticeMessageDTO>>> implements RocketMQListener<BaseMessage<List<TaskNoticeMessageDTO>>> {

    private final TaskNoticeService taskNoticeService;

    private final TaskNoticeMessageProducer messageProducer;

    private final TaskNoticeMapper taskNoticeMapper;

    private final ObjectMapper objectMapper;

    public TaskNoticeMessageConsumer(RocketMQEnhanceTemplate rocketMQEnhanceTemplate,
                                     TaskNoticeService taskNoticeService,
                                     TaskNoticeMessageProducer messageProducer,
                                     TaskNoticeMapper taskNoticeMapper,
                                     ObjectMapper objectMapper) {
        super(rocketMQEnhanceTemplate);
        this.taskNoticeService = taskNoticeService;
        this.messageProducer = messageProducer;
        this.taskNoticeMapper = taskNoticeMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    protected void handleMessage(BaseMessage<List<TaskNoticeMessageDTO>> message) {
        // 1小时内不允许重复发送
        RedisUtil.setCacheObject(
                String.format(RocketMQConstant.REDIS_IDEMPOTENT_CHECK_KEY, message.getKey()),
                true,
                Duration.ofHours(1L)
        );
        List<TaskNoticeMessageDTO> body = message.getBody();
        List<TaskNotice> taskNoticeList = taskNoticeMapper.convertList(body);

        // 保存通知消息内容
        taskNoticeService.saveNotice(taskNoticeList);
    }

    /**
     * 过滤消息，防止重复消费
     *
     * @param message 待处理消息
     * @return true: 处理消息，false: 过滤消息
     */
    @Override
    protected boolean filter(BaseMessage<List<TaskNoticeMessageDTO>> message) {
        boolean repeatConsumer = RedisUtil.hasKey(String.format(RocketMQConstant.REDIS_IDEMPOTENT_CHECK_KEY, message.getKey()));
        if (repeatConsumer) {
            log.warn("[{}] 重复消费，过滤消息！", message.getKey());
        }
        return repeatConsumer;
    }

    @Override
    public void onMessage(BaseMessage<List<TaskNoticeMessageDTO>> message) {
        dispatchMessage(message);
    }
}
