package com.gv.steel.system.im.component.message.rocketmq;

import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.model.Message;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.common.rocketmq.handler.EnhanceMessageHandler;
import com.gv.steel.system.im.config.properties.CIMProperties;
import com.gv.steel.system.im.constants.Constants;
import com.gv.steel.system.im.convert.MessageMapper;
import com.gv.steel.system.im.dto.MessageDTO;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        consumerGroup = Constants.IM_PUSH_CONSUMER_GROUP,
        topic = Constants.IM_PUSH_MSG_TOPIC,
        messageModel = MessageModel.BROADCASTING,
        consumeThreadNumber = 5
)
@ConditionalOnProperty(prefix = CIMProperties.PREFIX, name = "consumer-type", havingValue = Constants.ROCKETMQ_CONSUMER)
public class RocketMQPushMessageListener extends EnhanceMessageHandler<MessageDTO, BaseMessage<MessageDTO>> implements RocketMQListener<BaseMessage<MessageDTO>> {
    private final SessionGroup sessionGroup;

    private final MessageMapper messageMapper;

    public RocketMQPushMessageListener(RocketMQEnhanceTemplate rocketMQEnhanceTemplate,
                                       SessionGroup sessionGroup,
                                       MessageMapper messageMapper
    ) {
        super(rocketMQEnhanceTemplate);
        this.sessionGroup = sessionGroup;
        this.messageMapper = messageMapper;
    }

    @Override
    public void onMessage(BaseMessage<MessageDTO> message) {
        dispatchMessage(message);
    }

    @Override
    protected void handleMessage(BaseMessage<MessageDTO> message) {
        Message msg = messageMapper.convert(message.getBody());
        String uid = msg.getReceiver();

        if (uid == null) {
            return;
        }

        sessionGroup.write(uid, msg);
    }
}
