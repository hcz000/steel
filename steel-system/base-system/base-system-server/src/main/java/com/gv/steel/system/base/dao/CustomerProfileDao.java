package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.CustomerProfileQueryDTO;
import com.gv.steel.system.base.dto.processing.AwaitStatementCustomerQueryDTO;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.vo.CustomerProfilePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户档案（委托单位、贸易客户） Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-11-08
 */
@Mapper
public interface CustomerProfileDao extends BaseDao<CustomerProfile> {

    /**
     * 查询委托单位 page list
     *
     * @param page page
     * @param dto  query param
     * @return 委托单位，包含贸易客户列表
     */
    Page<CustomerProfilePageVO> selectDelegateCustomPage(Page<CustomerProfilePageVO> page, @Param("query") CustomerProfileQueryDTO dto);

    /**
     * 查询贸易客户 page list
     *
     * @param page page
     * @param dto  query param
     * @return 贸易客户，包含委托单位名称
     */
    Page<CustomerProfilePageVO> selectTradeCustomPage(Page<CustomerProfilePageVO> page, @Param("query") CustomerProfileQueryDTO dto);

    /**
     * 查询待对账单委托单位 page list
     *
     * @param page page
     * @param dto  query param
     * @return
     **/
    Page<CustomerProfile> selectAwaitStatementDelegateCustomer(Page<CustomerProfile> page, @Param("param") AwaitStatementCustomerQueryDTO dto);
}
