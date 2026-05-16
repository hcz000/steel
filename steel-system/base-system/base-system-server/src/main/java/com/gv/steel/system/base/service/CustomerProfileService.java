package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.CustomerProfileDTO;
import com.gv.steel.system.base.dto.CustomerProfileQueryDTO;
import com.gv.steel.system.base.dto.CustomerProfileSelectItemQueryDTO;
import com.gv.steel.system.base.dto.excel.CustomerProfileImportDTO;
import com.gv.steel.system.base.dto.excel.CustomerRequireImportDTO;
import com.gv.steel.system.base.dto.processing.AwaitStatementCustomerQueryDTO;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.vo.CustomerProfileDetailVO;
import com.gv.steel.system.base.vo.CustomerProfilePageVO;
import com.gv.steel.system.base.vo.CustomerProfileSelectItemVO;

import java.util.List;

/**
 * <p>
 * 客户档案（委托单位、贸易客户） 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-11-08
 */
public interface CustomerProfileService extends BaseService<CustomerProfile> {

    PageResult<CustomerProfilePageVO> pageDelegateCustomer(Page<CustomerProfilePageVO> page, CustomerProfileQueryDTO dto);

    PageResult<CustomerProfilePageVO> pageTradeCustomer(Page<CustomerProfilePageVO> page, CustomerProfileQueryDTO dto);

    CustomerProfileDetailVO getCustomerDetailById(Long id);

    Long saveCustomerProfileDetail(CustomerProfileDTO dto);

    Long updateCustomerProfileDetail(CustomerProfileDTO dto);

    List<CustomerProfileSelectItemVO> getCustomerSelectList(CustomerProfileSelectItemQueryDTO dto);

    /**
     * 通过月结方式获取委托单位（货主）ID
     *
     * @param monthlyStatementWay 月结方式
     * @return 委托单位（货主）ID
     */
    List<Long> getIdListByMonthlyStatementWay(Integer monthlyStatementWay);

    boolean importCustomerProfile(List<CustomerProfileImportDTO> importData, Long factoryId);

    /**
     * 分页查询待对账单的委托单位（货主）
     *
     * @param page 分页参数
     * @param dto  查询条件
     * @return 分页结果
     */
    PageResult<CustomerProfile> pageAwaitStatementDelegateCustomer(Page<CustomerProfile> page, AwaitStatementCustomerQueryDTO dto);

    boolean importCustomerProcessRequire(List<CustomerRequireImportDTO> importData, Long factoryId);

    String customerFileImportDataReview(List<CustomerProfileImportDTO> importData);

    boolean removeCustomerById(Long id);
}
