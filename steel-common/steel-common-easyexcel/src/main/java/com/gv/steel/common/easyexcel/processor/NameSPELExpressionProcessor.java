package com.gv.steel.common.easyexcel.processor;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

public class NameSPELExpressionProcessor implements NameProcessor {
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public String doDetermineName(Object[] args, Method method, String key) {
        if (!key.contains("#")) {
            return key;
        } else {
            EvaluationContext context = new MethodBasedEvaluationContext(null, method, args, NAME_DISCOVERER);
            Object value = PARSER.parseExpression(key).getValue(context);
            return value == null ? null : value.toString();
        }
    }
}
