package com.gv.steel.common.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.gv.steel.common.core.util.ClassUtils;
import com.gv.steel.common.mybatis.annotation.DataColumn;
import com.gv.steel.common.mybatis.annotation.DataScope;
import com.gv.steel.common.mybatis.strategy.AbstractDataColumnStrategy;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class MybatisDataPermissionHandler implements DataPermissionHandler {
    private final static String COUNT_SFX = "_COUNT";

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        DataScope dataScope = getPermissionAnnotation(mappedStatementId);
        if (null == dataScope) {
            return where;
        }
        // id为执行的mapper方法的全路径名，如com.mapper.UserMapper
        log.info("解析得到全类型名称 ID:{}", mappedStatementId);
        return setWhere(where, dataScope);
    }

    /**
     * 获得权限注释
     *
     * @param mappedStatementId 映射语句Id
     * @return {@link DataScope}
     */
    private DataScope getPermissionAnnotation(String mappedStatementId) {
        try {
            //统计SQL取得注解也是实际查询id上得注解，所以需要去掉_COUNT
            if (mappedStatementId.contains(COUNT_SFX)) {
                mappedStatementId = mappedStatementId.replace(COUNT_SFX, "");
            }
            //获取类名和方法名
            String className = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
            String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
            Class<?> cls = Class.forName(className);
            DataScope classDataScope = AnnotationUtils.findAnnotation(cls, DataScope.class);
            Method method = Arrays.stream(cls.getMethods()).filter(me -> me.getName().equals(methodName)).findFirst().orElseThrow(ClassNotFoundException::new);
            DataScope methodDataScope = ClassUtils.getAnnotation(method, DataScope.class);
            return null != methodDataScope ? methodDataScope : classDataScope;
        } catch (ClassNotFoundException e) {
            log.error("@DataScope annotation class not found.");
            return null;
        }
    }

    private Expression setWhere(Expression where, DataScope dataScope) {
        DataColumn[] columns = dataScope.value();
        //如果没有添加数据权限列 返回
        if (columns.length < 1) {
            return where;
        }
        //获取所有需要处理的数据权限列，根据枚举获取SQL处理的策略
        try {
            for (DataColumn column : columns) {
                Class<? extends AbstractDataColumnStrategy> clazz = column.type().getClazz();
                AbstractDataColumnStrategy strategy = clazz.getDeclaredConstructor().newInstance();
                strategy.setAlias(column.alias());
                strategy.setColumn(column.name());
                strategy.setWhere(where);
                where = strategy.handleColumn();
            }
            return where;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("处理数据权限SQL异常 error:", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
