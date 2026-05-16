package com.gv.steel.system.im.constants;

import io.netty.util.AttributeKey;

public interface Constants {

	String PUSH_MESSAGE_INNER_QUEUE = "signal/channel/PUSH_MESSAGE_INNER_QUEUE";

	String BIND_MESSAGE_INNER_QUEUE = "signal/channel/BIND_MESSAGE_INNER_QUEUE";

	String APNS_DEVICE_TOKEN = "APNS_OPEN_%s";

	AttributeKey<Long> SESSION_ID = AttributeKey.valueOf("session_id");

	String REDIS_CONSUMER = "redis";

	String ROCKETMQ_CONSUMER = "rocketmq";

	String IM_BIND_CONSUMER_GROUP = "IM_BIND_CONSUMER_GROUP";

	String IM_PUSH_CONSUMER_GROUP = "IM_PUSH_CONSUMER_GROUP";

	String IM_PUSH_MSG_TOPIC = "IM_PUSH_MSG_TOPIC";

	String IM_BIND_MSG_TOPIC = "IM_BIND_MSG_TOPIC";

}
