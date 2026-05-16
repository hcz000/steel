package com.gv.steel.system.base.constant;

public interface RocketMQConstant {
    /**
     * 默认tag
     */
    String DEFAULT_TAG = "*";

    /**
     * 推送IM消息的topic
     */
    String IM_PUSH_MSG_TOPIC = "IM_PUSH_MSG_TOPIC";

    /**
     * 推送任务通知的topic
     */
    String PUSH_TASK_NOTICE_TOPIC = "PUSH_TASK_NOTICE_TOPIC";

    /**
     * 推送任务通知的消费组
     */
    String PUSH_TASK_NOTICE_CONSUMER_GROUP = "PUSH_TASK_NOTICE_CONSUMER_GROUP";

    /**
     * 推送任务通知状态更新的topic
     */
    String PUSH_TASK_NOTICE_STATUS_UPDATE_TOPIC = "PUSH_TASK_NOTICE_STATUS_UPDATE_TOPIC";

    /**
     * 推送任务通知状态更新的消费组
     */
    String PUSH_TASK_NOTICE_STATUS_UPDATE_GROUP = "PUSH_TASK_NOTICE_STATUS_UPDATE_GROUP";

    /**
     * 推送重新计算加工费的topic
     */
    String PUSH_RE_CALCULATE_PROCESSING_CHARGE_TOPIC = "RE_CALCULATE_PROCESSING_CHARGE_TOPIC";

    /**
     * 推送重新计算加工费的消费组
     */
    String PUSH_RE_CALCULATE_PROCESSING_CHARGE_GROUP = "RE_CALCULATE_PROCESSING_CHARGE_GROUP";

    /**
     * 消息幂等性检查的key
     */
    String REDIS_IDEMPOTENT_CHECK_KEY = "rocketmq:message:idempotent:%s";

    /**
     * 推送原卷到货的topic
     */
    String PUSH_RAW_ARRIVAL_TOPIC = "PUSH_RAW_ARRIVAL_TOPIC";

    /**
     * 推送原卷到货的消费组
     */
    String PUSH_RAW_ARRIVAL_GROUP = "PUSH_RAW_ARRIVAL_GROUP";
}
