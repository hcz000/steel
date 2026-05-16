package com.gv.steel.common.mybatis.strategy;

import lombok.Data;
import net.sf.jsqlparser.expression.Expression;

@Data
public abstract class AbstractDataColumnStrategy {
    public static final String SPOT = ".";
    public static final String DOT = ",";

    private Expression where;

    private String alias;

    private String column;

    public abstract Expression handleColumn();
}
