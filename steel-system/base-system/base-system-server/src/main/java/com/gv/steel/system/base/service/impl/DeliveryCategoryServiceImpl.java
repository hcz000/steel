package com.gv.steel.system.base.service.impl;

import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.DeliveryCategoryDao;
import com.gv.steel.system.base.entity.DeliveryCategory;
import com.gv.steel.system.base.service.DeliveryCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 出货单类型 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
public class DeliveryCategoryServiceImpl extends BaseServiceImpl<DeliveryCategoryDao, DeliveryCategory> implements DeliveryCategoryService {

}
