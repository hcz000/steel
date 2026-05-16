package com.gv.steel.system.base.vo;

import com.gv.steel.system.base.entity.CrosscutRequire;
import com.gv.steel.system.base.entity.RipCutRequire;
import com.gv.steel.system.base.entity.RollingRequire;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author SZB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicRequireVO implements Serializable {

    private static final long serialVersionUID = 6522715444183704669L;
    private Long factoryId;

    private CrosscutRequire crosscutRequire;

    private RipCutRequire ripCutRequire;

    private RollingRequire rollingRequire;
}
