package com.gv.steel.system.im.service;


import com.gv.steel.system.im.entity.Session;

/**
 * 存储连接信息，便于查看用户的链接信息
 */
public interface SessionService {

    void add(Session session);

    void openApns(String uid, String deviceToken);

    void closeApns(String uid);
}
