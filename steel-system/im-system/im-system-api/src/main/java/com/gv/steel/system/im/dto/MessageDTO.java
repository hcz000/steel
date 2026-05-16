package com.gv.steel.system.im.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -4249839004355385102L;
    /**
     * 消息id
     */
    private Long id;

    /**
     * 消息类型，用户自定义消息类别
     */
    private String action;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容，于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
     */
    private String content;

    /**
     * 消息发送者账号
     */
    private String sender;
    /**
     * 消息发送者接收者
     */
    private String receiver;

    /**
     * content 内容格式
     */
    private String format;

    /**
     * 附加内容 内容
     */
    private String extra;
}
