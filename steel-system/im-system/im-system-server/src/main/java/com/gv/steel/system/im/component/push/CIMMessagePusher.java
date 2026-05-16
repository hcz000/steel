package com.gv.steel.system.im.component.push;


import com.farsunset.cim.model.Message;
import com.gv.steel.system.im.entity.Session;

/*
 * 消息发送实接口
 *
 */
public interface CIMMessagePusher {

    /*
     * 向用户发送消息
     *
     * @param msg
     */
    void push(Message msg);

    void bind(Session session);

}
