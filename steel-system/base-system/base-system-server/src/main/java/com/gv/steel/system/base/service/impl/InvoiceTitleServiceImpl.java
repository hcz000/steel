package com.gv.steel.system.base.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.InvoiceTitleDao;
import com.gv.steel.system.base.entity.InvoiceTitle;
import com.gv.steel.system.base.service.InvoiceTitleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 发票抬头 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class InvoiceTitleServiceImpl extends BaseServiceImpl<InvoiceTitleDao, InvoiceTitle> implements InvoiceTitleService {

}
