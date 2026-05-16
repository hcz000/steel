package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.SecurityConstants;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.CustomerProfile;
import com.gv.steel.system.base.vo.CustomerProfileDetailVO;
import com.gv.steel.system.base.vo.CustomerProfileSelectItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteCustomerProfileService", value = BaseAppConstant.APP_NAME)
public interface RemoteCustomerProfileService {
    @GetMapping(value = "/customer-profile/getByCode")
    Result<CustomerProfile> getCustomerProfileByCode(@RequestParam("customerCode") String customerCode, @RequestParam("factoryId") Long factoryId);

    @GetMapping(value = "/customer-profile/customer/list")
    Result<List<CustomerProfileSelectItemVO>> getCustomerList(@RequestParam("factoryId") Long factoryId, @RequestParam("customerType") Integer customerType);

    @GetMapping(value = "/customer-profile/customer/list")
    Result<List<CustomerProfileSelectItemVO>> getCustomerListBySalesmanId(@RequestParam("salesmanName") String salesmanName, @RequestParam("customerType") Integer customerType);


    @GetMapping(value = "/customer-profile/{id:\\d+}")
    Result<CustomerProfileDetailVO> findCustomerProfileById(@PathVariable("id") Long id);


    @GetMapping(value = "/customer-profile/getIdListByMonthlyStatementWaW", headers = SecurityConstants.HEADER_FROM_IN)
    Result<List<Long>> getIdListByMonthlyStatementWay(@RequestParam("monthlyStatementWay") Integer monthlyStatementWay);

    @GetMapping(value = "/customer-profile/pageAwaitStatementDelegateCustomer")
    Result<PageResult<CustomerProfile>> pageAwaitStatementDelegateCustomer(@RequestParam("current") Long current, @RequestParam("size") Long size, @RequestParam("factoryId") Long factoryId, @RequestParam("customerCode") String customerCode, @RequestParam("customerIds") String customerIds);
}
