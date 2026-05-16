package com.gv.steel.system.im.component.redis;

import com.gv.steel.common.redis.utils.RedisUtil;
import com.gv.steel.system.im.constants.Constants;
import org.springframework.stereotype.Component;


@Component
public class KeyValueRedisTemplate {

    public void set(String key, String value) {
        RedisUtil.setCacheObject(key, value);
    }

    public String get(String key) {
        return RedisUtil.getCacheObject(key);
    }

    public String getDeviceToken(String uid) {
        return get(String.format(Constants.APNS_DEVICE_TOKEN, uid));
    }

    public void openApns(String uid, String deviceToken) {
        set(String.format(Constants.APNS_DEVICE_TOKEN, uid), deviceToken);
    }

    public void closeApns(String uid) {
        RedisUtil.deleteObject(String.format(Constants.APNS_DEVICE_TOKEN, uid));
    }


}
