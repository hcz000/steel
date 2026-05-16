package com.gv.steel.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Lists;
import com.gv.steel.common.core.entity.SysFactoryInfo;
import com.gv.steel.common.core.vo.UserInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户信息上下文
 */
public class UserInfoContextHolder {
    private static final ThreadLocal<UserInfo> LOCAL_CONTEXT_USER = new TransmittableThreadLocal<>();

    private UserInfoContextHolder() {
    }

    /**
     * 当前登录用户
     *
     * @return userInfo
     */
    public static UserInfo current() {
        return get();
    }

    /**
     * 当前用户ID
     *
     * @return userId
     */
    public static Long currentUserId() {
        UserInfo userInfo = get();
        return Optional.ofNullable(userInfo).map(UserInfo::getUserId).orElse(null);
    }

    /**
     * 当前用户名
     *
     * @return username
     */
    public static String currentUsername() {
        UserInfo userInfo = get();
        return Optional.ofNullable(userInfo).map(UserInfo::getUsername).orElse(null);
    }

    /**
     * 当前用户名
     *
     * @return username
     */
    public static String currentNickName() {
        UserInfo userInfo = get();
        return Optional.ofNullable(userInfo).map(UserInfo::getNickname).orElse(null);
    }

    /**
     * 当前用户的工厂列表
     *
     * @return factoryList
     */
    public static List<SysFactoryInfo> currentFactoryList() {
        return Optional.ofNullable(get()).map(UserInfo::getFactoryList).orElse(null);
    }

    /**
     * 当前用户的工厂ID列表
     *
     * @return factoryIdList
     */
    public static List<Long> currentFactoryIdList() {
        return Optional.ofNullable(get())
                .map(UserInfo::getFactoryList)
                .map(factoryList -> factoryList.stream()
                        .map(SysFactoryInfo::getId)
                        .collect(Collectors.toList())
                ).orElse(Lists.newArrayList());
    }

    public static void put(UserInfo locale) {
        LOCAL_CONTEXT_USER.set(locale);
    }

    public static UserInfo get() {
        return LOCAL_CONTEXT_USER.get() != null ? LOCAL_CONTEXT_USER.get() : null;
    }

    public static void clear() {
        LOCAL_CONTEXT_USER.remove();
    }

}
