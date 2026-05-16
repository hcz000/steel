package com.gv.steel.common.core.entity;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysFactoryInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 工厂简称
     */
    private String factoryNickname;

    /**
     * 工厂全称
     */
    private String factoryName;

    /**
     * 工厂地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 传真
     */
    private String fax;

    /**
     * 所属班组
     */
    private List<SysTeamInfo> teamInfoList = Lists.newArrayList();
}
