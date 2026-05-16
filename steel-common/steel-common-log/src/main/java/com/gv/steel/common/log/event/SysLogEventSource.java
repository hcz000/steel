package com.gv.steel.common.log.event;

import com.gv.steel.system.user.entity.SysLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogEventSource extends SysLog {
    private static final long serialVersionUID = 2797859700167232293L;

    /**
     * 请求体
     */
    private Object body;
}
