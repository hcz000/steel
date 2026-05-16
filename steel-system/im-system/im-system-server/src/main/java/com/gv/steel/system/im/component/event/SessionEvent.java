package com.gv.steel.system.im.component.event;

import com.gv.steel.system.im.entity.Session;
import org.springframework.context.ApplicationEvent;

public class SessionEvent extends ApplicationEvent {
    private static final long serialVersionUID = 31379725852200537L;

    public SessionEvent(Session session) {
        super(session);
    }

    @Override
    public Session getSource() {
        return (Session) source;
    }

}
