package com.gv.steel.system.im.service.impl;

import com.gv.steel.system.im.component.redis.KeyValueRedisTemplate;
import com.gv.steel.system.im.entity.Session;
import com.gv.steel.system.im.service.SessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class SessionServiceImpl implements SessionService {

    @Resource
    private KeyValueRedisTemplate keyValueRedisTemplate;


    private final String host;

    public SessionServiceImpl() throws UnknownHostException {
        host = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void add(Session session) {
        session.setBindTime(System.currentTimeMillis());
        session.setHost(host);
    }

    @Override
    public void openApns(String uid, String deviceToken) {
        keyValueRedisTemplate.openApns(uid, deviceToken);
    }

    @Override
    public void closeApns(String uid) {
        keyValueRedisTemplate.closeApns(uid);
    }
}
