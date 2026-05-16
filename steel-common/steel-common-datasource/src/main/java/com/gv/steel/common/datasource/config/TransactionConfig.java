package com.gv.steel.common.datasource.config;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TransactionConfig {
    /**
     * 事务注解 AOP 端点 表单时
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.gv.steel.**.service..*.*(..))";
    /**
     * 需要事务的条件
     */
    private static final String[] REQUIRED_RULE_TRANSACTION = new String[]{"insert*", "create*", "add*", "save*", "modify*", "update*", "del*", "delete*", "remove*"};
    /**
     * 只读的事务条件
     */
    private static final String[] READ_RULE_TRANSACTION = new String[]{"select*", "get*", "query*", "search*", "count*", "detail*", "find*", "list*", "page*"};

    /**
     * 事务管理器
     */
    private final TransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        List<RollbackRuleAttribute> rollbackRuleAttributes = new ArrayList<>();
        rollbackRuleAttributes.add(new RollbackRuleAttribute(Exception.class));

        // 需要事务的属性配置
        RuleBasedTransactionAttribute txAttrRequired = new RuleBasedTransactionAttribute();
        txAttrRequired.setName("REQUIRED");
        txAttrRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttrRequired.setRollbackRules(rollbackRuleAttributes);

        // 只读事务的属性配置
        RuleBasedTransactionAttribute txAttrRequiredReadOnly = new RuleBasedTransactionAttribute();
        txAttrRequiredReadOnly.setName("SUPPORTS");
        txAttrRequiredReadOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        txAttrRequiredReadOnly.setRollbackRules(rollbackRuleAttributes);
        txAttrRequiredReadOnly.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        // 数据库操作方法的事务配置
        Arrays.stream(REQUIRED_RULE_TRANSACTION).forEach(rule -> source.addTransactionalMethod(rule, txAttrRequired));

        // 数据库查询方法的事务配置
        Arrays.stream(READ_RULE_TRANSACTION).forEach(rule -> source.addTransactionalMethod(rule, txAttrRequiredReadOnly));

        return new TransactionInterceptor(this.transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, this.txAdvice());
    }
}
