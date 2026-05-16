
package com.gv.steel.common.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = -467010647247066731L;

    public SysLogEvent(SysLogEventSource source) {
        super(source);
    }

}
