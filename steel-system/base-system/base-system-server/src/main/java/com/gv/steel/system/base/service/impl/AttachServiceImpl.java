package com.gv.steel.system.base.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.AttachDao;
import com.gv.steel.system.base.entity.Attach;
import com.gv.steel.system.base.service.AttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Slf4j
@Service
@Transactional
public class AttachServiceImpl extends BaseServiceImpl<AttachDao, Attach> implements AttachService {

}
