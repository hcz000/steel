package com.gv.steel.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gv.steel.common.core.context.UserInfoContextHolder;
import com.gv.steel.common.mybatis.constant.MetaObjectAutoFillConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    /**
     * 填充值，先判断 元数据中是否存在属性值
     *
     * @param fieldName  待填充的属性名
     * @param fieldValue 属性值
     * @param metaObject 对象元数据
     * @param isCover    是否覆盖原始值
     */
    private static void fillValIfNullByName(String fieldName, Object fieldValue, MetaObject metaObject, boolean isCover) {
        // 没有属性
        if (!metaObject.hasGetter(fieldName)) {
            return;
        }

        // 存在值
        Object oldValue = metaObject.getValue(fieldName);
        String oldValueStr = StrUtil.str(oldValue, Charset.defaultCharset());
        if (StrUtil.isNotBlank(oldValueStr) && !isCover) {
            return;
        }

        // field 值类型相同时，设置新值
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldValue)) {
            metaObject.setValue(fieldName, fieldValue);
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("mybatis plus fill insert data...");
        }

        LocalDateTime now = LocalDateTime.now();

        fillValIfNullByName(MetaObjectAutoFillConstant.CREATE_TIME, now, metaObject, false);
        fillValIfNullByName(MetaObjectAutoFillConstant.CREATE_BY, getUserId(), metaObject, false);
        // 创建人名称
        fillValIfNullByName(MetaObjectAutoFillConstant.CREATE_NAME, getNickname(), metaObject, false);

        fillValIfNullByName(MetaObjectAutoFillConstant.VERSION, 1, metaObject, false);
        fillValIfNullByName(MetaObjectAutoFillConstant.DELETE_FLAG, 0, metaObject, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("mybatis plus fill update data...");
        }

        LocalDateTime now = LocalDateTime.now();

        fillValIfNullByName(MetaObjectAutoFillConstant.UPDATE_TIME, now, metaObject, true);
        fillValIfNullByName(MetaObjectAutoFillConstant.UPDATE_BY, getUserId(), metaObject, true);
        // 修改人名称
        fillValIfNullByName(MetaObjectAutoFillConstant.UPDATE_NAME, getNickname(), metaObject, true);
    }

    /**
     * 获取用户ID
     *
     * @return 当前登录的用户ID
     */
    private Long getUserId() {
        return UserInfoContextHolder.currentUserId();
    }

    /**
     * 获取用户名
     *
     * @return 当前登录的用户名
     */
    private String getNickname() {
        return UserInfoContextHolder.currentNickName();
    }
}
