package com.gv.steel.system.im.service;


import com.farsunset.cim.model.Message;

public interface APNsService {

    void push(Message message, String deviceToken);
}
