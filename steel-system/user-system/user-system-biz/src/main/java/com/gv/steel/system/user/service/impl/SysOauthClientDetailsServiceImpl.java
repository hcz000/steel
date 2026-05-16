package com.gv.steel.system.user.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.dao.SysOauthClientDetailsDao;
import com.gv.steel.system.user.entity.SysOauthClientDetails;
import com.gv.steel.system.user.service.SysOauthClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
@Transactional
public class SysOauthClientDetailsServiceImpl extends BaseServiceImpl<SysOauthClientDetailsDao, SysOauthClientDetails>
        implements SysOauthClientDetailsService {

    /**
     * 通过ID删除客户端
     *
     * @param id
     * @return
     */
    @Override
    public Boolean removeClientDetailsById(Long id) {
        return this.removeById(id);
    }

    /**
     * 根据客户端信息
     *
     * @param clientDetails
     * @return
     */
    @Override
    public Boolean updateClientDetailsById(SysOauthClientDetails clientDetails) {
        return this.updateById(clientDetails);
    }

    /**
     * 清除客户端缓存
     */
    @Override
    public void clearClientCache() {

    }

}
