package com.gv.steel.system.user.service;


import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysOauthClientDetails;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface SysOauthClientDetailsService extends BaseService<SysOauthClientDetails> {

    /**
     * 通过ID删除客户端
     *
     * @param id
     * @return
     */
    Boolean removeClientDetailsById(Long id);

    /**
     * 修改客户端信息
     *
     * @param sysOauthClientDetails
     * @return
     */
    Boolean updateClientDetailsById(SysOauthClientDetails sysOauthClientDetails);

    /**
     * 清除客户端缓存
     */
    void clearClientCache();

}
