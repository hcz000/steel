package com.gv.steel.common.mybatis.strategy;

import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;

public class UserDataColumnStrategy extends AbstractDataColumnStrategy {
    @Override
    public Expression handleColumn() {
        String alias = getAlias();
        String column = getColumn();

        if (StrUtil.isNotBlank(alias)) {
            column = StrUtil.join(SPOT, alias, column);
        }

        Expression where = getWhere();

        Long userId = UserInfoContextHolder.currentUserId();

        if (userId == null) {
            throw new BaseException(MsgUtils.getMessage("sys.user.no.access"));
        }

        EqualsTo equalsTo = new EqualsTo();
        equalsTo.withLeftExpression(new Column(column));
        equalsTo.withRightExpression(new LongValue(userId));

        if (null == where) {
            // 不存在 where 条件
            return new Parenthesis(equalsTo);
        } else {
            // 存在 where 条件 and 处理
            return new AndExpression(where, equalsTo);
        }
    }
}
