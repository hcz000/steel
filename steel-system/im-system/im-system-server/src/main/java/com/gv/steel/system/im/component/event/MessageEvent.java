package com.gv.steel.system.im.component.event;

import com.farsunset.cim.model.Message;
import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    private static final long serialVersionUID = 5543403432233807731L;

    public MessageEvent(Message message) {
        super(message);
    }

    @Override
    public Message getSource() {
        return (Message) source;
    }

}
