package com.gv.steel.system.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Maps;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.core.util.ResultOps;
import com.gv.steel.common.mybatis.base.entity.CommonModel;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.common.mybatis.utils.QueryWrapperUtil;
import com.gv.steel.system.base.convert.CustomerProfileMapper;
import com.gv.steel.system.base.dao.CustomerProfileDao;
import com.gv.steel.system.base.dto.CustomerProfileDTO;
import com.gv.steel.system.base.dto.CustomerProfileQueryDTO;
import com.gv.steel.system.base.dto.CustomerProfileSelectItemQueryDTO;
import com.gv.steel.system.base.dto.excel.CustomerProfileImportDTO;
import com.gv.steel.system.base.dto.excel.CustomerRequireImportDTO;
import com.gv.steel.system.base.dto.processing.AwaitStatementCustomerQueryDTO;
import com.gv.steel.system.base.entity.*;
import com.gv.steel.system.base.enums.CustomerTypeEnum;
import com.gv.steel.system.base.enums.MonthlyStatementWayEnum;
import com.gv.steel.system.base.feign.RemoteProduceCustomerService;
import com.gv.steel.system.base.feign.RemoteProduceRawMaterialService;
import com.gv.steel.system.base.service.*;
import com.gv.steel.system.base.vo.CustomerProfileDetailVO;
import com.gv.steel.system.base.vo.CustomerProfilePageVO;
import com.gv.steel.system.base.vo.CustomerProfileSelectItemVO;
import com.gv.steel.system.user.entity.SysFactory;
import com.gv.steel.system.user.feign.RemoteFactoryService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户档案（委托单位、贸易客户） 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-11-08
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerProfileServiceImpl extends BaseServiceImpl<CustomerProfileDao, CustomerProfile> implements CustomerProfileService {

    private final RipCutRequireService ripCutRequireService;

    private final CrosscutRequireService crosscutRequireService;

    private final RollingRequireService rollingRequireService;

    private final ProcessingContractService processingContractService;

    private final AccessoryChargeService accessoryChargeService;

    private final CustomerProfileMapper customerProfileMapper;

    private final RemoteFactoryService factoryService;

    private final RemoteProduceCustomerService remoteProduceCustomerService;

    private final RemoteProduceRawMaterialService remoteProduceRawMaterialService;

    @Override
    public PageResult<CustomerProfilePageVO> pageDelegateCustomer(Page<CustomerProfilePageVO> page, CustomerProfileQueryDTO dto) {
        if (StrUtil.isNotBlank(dto.getCustomerCode())) {
            dto.setCustomerCode(dto.getCustomerCode().toUpperCase());
        }
        baseDao.selectDelegateCustomPage(page, dto);
        return new PageResult<CustomerProfilePageVO>().pageResult(page);
    }

    @Override
    public PageResult<CustomerProfilePageVO> pageTradeCustomer(Page<CustomerProfilePageVO> page, CustomerProfileQueryDTO dto) {
        baseDao.selectTradeCustomPage(page, dto);
        return new PageResult<CustomerProfilePageVO>().pageResult(page);
    }

    @Override
    public CustomerProfileDetailVO getCustomerDetailById(Long id) {
        CustomerProfile customerProfile = getById(id);
        CustomerProfileDetailVO detailVO = customerProfileMapper.convertVO(customerProfile);

        String tableName = TableInfoHelper.getTableInfo(CustomerProfile.class).getTableName();
        RipCutRequire ripCutRequire = ripCutRequireService.getByTableId(customerProfile.getId(), tableName);
        CrosscutRequire crosscutRequire = crosscutRequireService.getByTableId(customerProfile.getId(), tableName);
        RollingRequire rollingRequire = rollingRequireService.getByTableId(customerProfile.getId(), tableName);

        detailVO.setRipCutRequire(ripCutRequire);
        detailVO.setCrosscutRequire(crosscutRequire);
        detailVO.setRollingRequire(rollingRequire);

        return detailVO;
    }

    @Override
    @GlobalTransactional
    public Long saveCustomerProfileDetail(CustomerProfileDTO dto) {
        CustomerProfile customerProfile = customerProfileMapper.convert(dto);

        LambdaQueryWrapper<CustomerProfile> wrapper = Wrappers.<CustomerProfile>lambdaQuery().eq(CustomerProfile::getCustomerCode, dto.getCustomerCode());
        if (CustomerTypeEnum.DELEGATE.getType() == dto.getCustomerType()) {
            wrapper.eq(CustomerProfile::getFactoryId, dto.getFactoryId());
        }

        if (count(wrapper) > 0) {
            throw new BaseException(MsgUtils.getMessage("customer.code.exists"));
        }

        save(customerProfile);

        remoteProduceCustomerService.save(customerProfile);

        String tableName = TableInfoHelper.getTableInfo(CustomerProfile.class).getTableName();

        if (ObjUtil.isNotNull(dto.getRipCutRequire())) {
            RipCutRequire ripCutRequire = dto.getRipCutRequire();
            ripCutRequire.setTableId(customerProfile.getId());
            ripCutRequire.setTableName(tableName);
            ripCutRequireService.save(ripCutRequire);
        }

        if (ObjUtil.isNotNull(dto.getCrosscutRequire())) {
            CrosscutRequire crosscutRequire = dto.getCrosscutRequire();
            crosscutRequire.setTableId(customerProfile.getId());
            crosscutRequire.setTableName(tableName);
            crosscutRequireService.save(crosscutRequire);
        }

        if (ObjUtil.isNotNull(dto.getRollingRequire())) {
            RollingRequire rollingRequire = dto.getRollingRequire();
            rollingRequire.setTableId(customerProfile.getId());
            rollingRequire.setTableName(tableName);
            rollingRequireService.save(rollingRequire);
        }

        return customerProfile.getId();
    }

    @Override
    @GlobalTransactional
    public Long updateCustomerProfileDetail(CustomerProfileDTO dto) {
        CustomerProfile customerProfile = customerProfileMapper.convert(dto);
        updateById(customerProfile);

        remoteProduceCustomerService.update(customerProfile);

        String tableName = TableInfoHelper.getTableInfo(CustomerProfile.class).getTableName();

        if (ObjUtil.isNotNull(dto.getRipCutRequire())) {
            RipCutRequire ripCutRequire = dto.getRipCutRequire();
            ripCutRequire.setTableId(customerProfile.getId());
            ripCutRequire.setTableName(tableName);
            RipCutRequire dbRequire = ripCutRequireService.getByTableId(customerProfile.getId(), tableName);
            Optional.ofNullable(dbRequire).ifPresent(db -> ripCutRequire.setId(db.getId()));
            ripCutRequireService.saveOrUpdate(ripCutRequire);
        }

        if (ObjUtil.isNotNull(dto.getCrosscutRequire())) {
            CrosscutRequire crosscutRequire = dto.getCrosscutRequire();
            crosscutRequire.setTableId(customerProfile.getId());
            crosscutRequire.setTableName(tableName);
            CrosscutRequire dbRequire = crosscutRequireService.getByTableId(customerProfile.getId(), tableName);
            Optional.ofNullable(dbRequire).ifPresent(db -> crosscutRequire.setId(db.getId()));
            crosscutRequireService.saveOrUpdate(crosscutRequire);
        }

        if (ObjUtil.isNotNull(dto.getRollingRequire())) {
            RollingRequire rollingRequire = dto.getRollingRequire();
            rollingRequire.setTableId(customerProfile.getId());
            rollingRequire.setTableName(tableName);
            RollingRequire dbRequire = rollingRequireService.getByTableId(customerProfile.getId(), tableName);
            Optional.ofNullable(dbRequire).ifPresent(db -> rollingRequire.setId(db.getId()));
            rollingRequireService.saveOrUpdate(rollingRequire);
        }

        return customerProfile.getId();
    }

    /**
     * 获取客户选择列表
     *
     * @param dto 客户档案选择项查询数据传输对象
     * @return 客户选择列表
     */
    @Override
    public List<CustomerProfileSelectItemVO> getCustomerSelectList(CustomerProfileSelectItemQueryDTO dto) {
        List<CustomerProfileSelectItemVO> voList = Lists.newArrayList();

        // 如果是查询贸易客户，判断是否需要包含委托单位
        if (ObjUtil.isNotNull(dto.getCustomerType()) && dto.getCustomerType() == CustomerTypeEnum.TRADE.getType()
                && dto.getIncludeDelegateFlag() == CommonConstants.SUCCESS) {
            CustomerProfile customerProfile = getById(dto.getCustomerId());
            CustomerProfileSelectItemVO detailVO = customerProfileMapper.convertSelectItemVo(customerProfile);
            voList.add(detailVO);
        }

        QueryWrapper<CustomerProfile> wrapper = QueryWrapperUtil.queryWrapperHandler(dto);
        List<CustomerProfile> list = list(wrapper);
        voList.addAll(customerProfileMapper.convertSelectItemVoList(list));
        return voList;
    }

    @Override
    public List<Long> getIdListByMonthlyStatementWay(Integer monthlyStatementWay) {
        return this.list(Wrappers.<CustomerProfile>lambdaQuery()
                .eq(CustomerProfile::getCustomerType, CustomerTypeEnum.DELEGATE.getType())
                .eq(CustomerProfile::getMonthlyStatementWay, monthlyStatementWay)).stream().map(CommonModel::getId).collect(Collectors.toList());
    }

    @Override
    public boolean importCustomerProfile(List<CustomerProfileImportDTO> importData, Long factoryId) {

        List<CustomerProfile> list = list(Wrappers.<CustomerProfile>lambdaQuery().eq(CustomerProfile::getFactoryId, factoryId));
        List<String> dbCustomerCodeList = list.stream().map(CustomerProfile::getCustomerCode).collect(Collectors.toList());

        Result<SysFactory> result = factoryService.findSysFactoryById(factoryId);
        SysFactory sysFactory = ResultOps.of(result).getData().orElseThrow();
        Map<String, ProcessingContract> processingContractMap = Maps.newHashMap();
        Map<String, List<AccessoryCharge>> accessoryChargeMap = Maps.newHashMap();
        List<CustomerProfile> customerProfileList = Lists.newArrayList();
        final int[] i = {1};
        importData.forEach(data -> {
            i[0]++;
            if (StrUtil.isBlank(data.getCustomerCode())) throw new BaseException(i[0] + "客户代码为空");
            if (dbCustomerCodeList.contains(data.getCustomerCode())) return;
            CustomerProfile customerProfile = new CustomerProfile();
            customerProfile.setCustomerName(data.getCustomerName());
            customerProfile.setCustomerCode(data.getCustomerCode());
            int customerType = !"KKK".equalsIgnoreCase(data.getCustomerCode()) && "K".equalsIgnoreCase(data.getCustomerCode().substring(0, 1))
                    ? CustomerTypeEnum.TRADE.getType()
                    : CustomerTypeEnum.DELEGATE.getType();
            customerProfile.setCustomerType(customerType);
            customerProfile.setParentId(CustomerTypeEnum.DELEGATE.getType() == customerType ? CommonConstants.TREE_ROOT_ID : null);
            customerProfile.setFactoryId(factoryId);
            customerProfile.setFactoryName(sysFactory.getFactoryNickname());
            customerProfile.setMonthlyStatementWay(
                    "月底月结".equals(data.getMonthlySettlementWayDesc())
                            || "当日月结".equals(data.getMonthlySettlementWayDesc())
                            ? MonthlyStatementWayEnum.MONTH_LAST_DAY.getCode()
                            : "每月25日".equals(data.getMonthlySettlementWayDesc())
                            ? MonthlyStatementWayEnum.MONTH_25_TH.getCode()
                            : "每月20日".equals(data.getMonthlySettlementWayDesc())
                            ? MonthlyStatementWayEnum.MONTH_20_TH.getCode()
                            : "每月15日".equals(data.getProcessingCostWeighingWayDesc())
                            ? MonthlyStatementWayEnum.MONTH_15_TH.getCode()
                            : "每月10日".equals(data.getMonthlySettlementWayDesc())
                            ? MonthlyStatementWayEnum.MONTH_10_TH.getCode()
                            : "每月5日".equals(data.getMonthlySettlementWayDesc())
                            ? MonthlyStatementWayEnum.MONTH_5_TH.getCode()
                            : MonthlyStatementWayEnum.MONTH_LAST_DAY.getCode());
            customerProfile.setPaymentTerms(CommonConstants.FALSE);
            customerProfile.setProcessingCostWeighingWay(
                    "母材结算".equals(data.getProcessingCostWeighingWayDesc())
                            ? CommonConstants.FALSE
                            : CommonConstants.TRUE
            );
            customerProfile.setTaxInclusiveFlag(
                    "是".equals(data.getTaxInclusiveFlagDesc())
                            ? CommonConstants.TRUE
                            : CommonConstants.FALSE
            );
            customerProfile.setTaxRete(
                    "是".equals(data.getTaxInclusiveFlagDesc())
                            ? new BigDecimal("6")
                            : null);
            customerProfile.setPrintStorageFlag(
                    "打印".equals(data.getPrintStorageFlagDesc())
                            ? CommonConstants.TRUE
                            : CommonConstants.FALSE
            );
            customerProfile.setRawMaterialLabelId(new Long[]{});
            customerProfile.setSheetProductLabelId(new Long[]{});
            customerProfile.setStripProductLabelId(new Long[]{});
            customerProfile.setSingleStripProductLabelId(new Long[]{});
            customerProfile.setDeliveryLabelId(new Long[]{});

            customerProfile.setLabelTitle(data.getRise());

            customerProfileList.add(customerProfile);

            if (CustomerTypeEnum.DELEGATE.getType() == customerType) {
                ProcessingContract processingContract = new ProcessingContract();
                processingContract.setContractNo("10001");
                processingContract.setSignedDate(LocalDate.now());
                processingContract.setHoistingCost(data.getHoistingCost());
                processingContract.setRawStorageCost(data.getRawStorageCost());
                processingContract.setRawFreeDays(data.getRawFreeDays());
                processingContract.setShelfReturnFlag(CommonConstants.FALSE);
                processingContractMap.put(data.getCustomerCode(), processingContract);

                List<AccessoryCharge> accessoryCharges = getAccessoryCharges();
                accessoryChargeMap.put(data.getCustomerCode(), accessoryCharges);
            }
        });

        // 过滤委托单位
        List<CustomerProfile> delegateCustomerList = customerProfileList.stream().filter(customer -> CustomerTypeEnum.DELEGATE.getType() == customer.getCustomerType())
                .collect(Collectors.toList());
        saveBatch(delegateCustomerList);

        // 过滤贸易客户
        List<CustomerProfile> tradeCustomerList = customerProfileList.stream().filter(customer -> CustomerTypeEnum.TRADE.getType() == customer.getCustomerType())
                .collect(Collectors.toList());
        tradeCustomerList.forEach(tradeCustomer -> {
            delegateCustomerList.stream()
                    .filter(customer -> "KKK".equals(customer.getCustomerCode()))
                    .findFirst().ifPresentOrElse(customer -> tradeCustomer.setParentId(customer.getId()), () -> {
                        list.stream()
                                .filter(customer -> "KKK".equals(customer.getCustomerCode()))
                                .findFirst().ifPresent(customer -> tradeCustomer.setParentId(customer.getId()));
                    });

            tradeCustomer.setLabelTitle("新余高万昌".equals(tradeCustomer.getLabelTitle()) ? "新余高万昌新材料有限公司\n" +
                    "地址:江西省新余市渝水区新余经济开发区钢城装备制造园D地块3号厂房"
                    : "高万昌".equals(tradeCustomer.getLabelTitle()) ? "苏州高万昌钢板有限公司\n" +
                    "地址:昆山市高新区迎宾中路1299号  TEL：0512-50135505  FAX：0512-50135522"
                    : "高顺昌".equals(tradeCustomer.getLabelTitle()) ? "高顺昌钢板(深圳)有限公司\n" +
                    "地址：深圳市龙华区观澜街道大水田社区裕展三路5号101    电话：0755-27337751   传真：0755-27337732"
                    : null);
        });

        saveBatch(tradeCustomerList);

        processingContractMap.forEach((customerCode, processingContract) -> {
            delegateCustomerList.stream()
                    .filter(customer -> customerCode.equals(customer.getCustomerCode()))
                    .findFirst().ifPresentOrElse(customer -> processingContract.setCustomerId(customer.getId()), () -> {
                        list.stream()
                                .filter(customer -> customerCode.equals(customer.getCustomerCode()))
                                .findFirst().ifPresent(customer -> processingContract.setCustomerId(customer.getId()));
                    });
            processingContractService.save(processingContract);
        });

        accessoryChargeMap.forEach((customerCode, accessoryChargeList) -> {
            ProcessingContract processingContract = processingContractMap.get(customerCode);
            Optional.ofNullable(processingContract).ifPresent(contract -> {
                accessoryChargeList.forEach(accessoryCharge -> {
                    accessoryCharge.setContractId(contract.getId());
                });
            });

            accessoryChargeService.saveBatch(accessoryChargeList);
        });

        return true;
    }

    @Override
    public PageResult<CustomerProfile> pageAwaitStatementDelegateCustomer(Page<CustomerProfile> page, AwaitStatementCustomerQueryDTO dto) {
        if (ArrayUtil.isEmpty(dto.getCustomerIds())) {
            return PageResult.<CustomerProfile>builder().build().pageResult(page);
        }

        log.info("委托单位ID集合为：{}", ArrayUtil.toString(dto.getCustomerIds()));
        baseDao.selectAwaitStatementDelegateCustomer(page, dto);
        return PageResult.<CustomerProfile>builder().build().pageResult(page);
    }

    @Override
    public boolean importCustomerProcessRequire(List<CustomerRequireImportDTO> importData, Long factoryId) {
        List<CustomerProfile> customerList = this.list();
        if (CollUtil.isNotEmpty(customerList)) {
            //取出表名
            String tableName = SqlHelper.table(CustomerProfile.class).getTableName();

            //取出委托单位MAP
            Map<String, CustomerProfile> delegateCustomerMap = customerList.stream().filter(customer -> CustomerTypeEnum.DELEGATE.getType() == customer.getCustomerType()
                            && customer.getFactoryId().equals(factoryId))
                    .collect(Collectors.toMap(CustomerProfile::getCustomerCode, Function.identity()));

            //取出贸易客户MAP
            Map<String, CustomerProfile> tradeCustomerMap = customerList.stream().filter(customer -> CustomerTypeEnum.TRADE.getType() == customer.getCustomerType())
                    .collect(Collectors.toMap(CustomerProfile::getCustomerCode, Function.identity()));

            //取出数据库所有的纵切加工要求
            List<RipCutRequire> ripCut = ripCutRequireService.list(Wrappers.<RipCutRequire>lambdaQuery().eq(RipCutRequire::getTableName, tableName));
            List<RipCutRequire> ripCutRes = new ArrayList<>();
            //取出数据库所有的横切加工要求
            List<CrosscutRequire> crosscut = crosscutRequireService.list(Wrappers.<CrosscutRequire>lambdaQuery().eq(CrosscutRequire::getTableName, tableName));
            List<CrosscutRequire> crosscutRes = new ArrayList<>();

            importData.forEach(data -> {
                //委托单位,区分加工厂
                if (ObjUtil.isNotNull(delegateCustomerMap)) {
                    CustomerProfile customerProfile = delegateCustomerMap.get(data.getOwnerCode());
                    if (ObjUtil.isNotNull(customerProfile)) {
                        Long delegateId = customerProfile.getId();

                        if (ObjUtil.isNotEmpty(delegateId)) {
                            //纵切
                            RipCutRequire ripCutRequire = ripCut.stream().filter(require ->
                                            require.getTableId().equals(delegateId)
                                                    && customerProfile.getFactoryId().equals(factoryId))
                                    .findAny().orElse(null);
                            if (ObjUtil.isEmpty(ripCutRequire)) {
                                ripCutRequire = new RipCutRequire();
                                ripCutRequire.setTableId(delegateId);
                                ripCutRequire.setTableName(tableName);
                                setRipCutRequire(data, ripCutRequire);
                                ripCutRes.add(ripCutRequire);
                            }

                            //横切
                            CrosscutRequire crosscutRequire = crosscut.stream().filter(require ->
                                            require.getTableId().equals(delegateId)
                                                    && customerProfile.getFactoryId().equals(factoryId))
                                    .findAny().orElse(null);
                            if (ObjUtil.isEmpty(crosscutRequire)) {
                                crosscutRequire = new CrosscutRequire();
                                crosscutRequire.setTableId(delegateId);
                                crosscutRequire.setTableName(tableName);
                                setCrosscutRequire(data, crosscutRequire);
                                crosscutRes.add(crosscutRequire);
                            }
                        }
                    }
                }
                if (ObjUtil.isNotNull(tradeCustomerMap)) {
                    CustomerProfile customerProfile = tradeCustomerMap.get(data.getOwnerCode());
                    if (ObjUtil.isNotNull(customerProfile)) {
                        Long tradeId = customerProfile.getId();
                        //贸易客户，不区分加工厂
                        if (ObjUtil.isNotEmpty(tradeId)) {
                            //纵切
                            RipCutRequire ripCutRequire = ripCut.stream().filter(require ->
                                    require.getTableId().equals(tradeId)).findAny().orElse(null);
                            if (ObjUtil.isEmpty(ripCutRequire)) {
                                ripCutRequire = new RipCutRequire();
                                ripCutRequire.setTableId(tradeId);
                                ripCutRequire.setTableName(tableName);
                                setRipCutRequire(data, ripCutRequire);
                                ripCutRes.add(ripCutRequire);
                            }

                            //横切
                            CrosscutRequire crosscutRequire = crosscut.stream().filter(require -> require.getTableId().equals(tradeId)).findAny().orElse(null);
                            if (ObjUtil.isEmpty(crosscutRequire)) {
                                crosscutRequire = new CrosscutRequire();
                                crosscutRequire.setTableId(tradeId);
                                crosscutRequire.setTableName(tableName);
                                setCrosscutRequire(data, crosscutRequire);
                                crosscutRes.add(crosscutRequire);
                            }
                        }

                    }
                }
            });
            ripCutRequireService.saveBatch(ripCutRes);
            crosscutRequireService.saveBatch(crosscutRes);
        }
        return true;
//        List<RipCutRequire> saveRipList = ripCutRes.stream().filter(rip -> ObjUtil.isNull(rip.getId())).collect(Collectors.toList());
//        List<RipCutRequire> updateRipList = ripCutRes.stream().filter(rip -> ObjUtil.isNotNull(rip.getId())).collect(Collectors.toList());
//        if (CollUtil.isNotEmpty(saveRipList)) {
//            ripCutRequireService.saveBatch(saveRipList);
//        }
//        if (CollUtil.isNotEmpty(updateRipList)) {
//            ripCutRequireService.updateBatchById(updateRipList);
//        }
//
//        List<CrosscutRequire> saveCrossList = crosscutRes.stream().filter(cross -> ObjUtil.isNull(cross.getId())).collect(Collectors.toList());
//        List<CrosscutRequire> updateCrossList = crosscutRes.stream().filter(cross -> ObjUtil.isNotNull(cross.getId())).collect(Collectors.toList());
//        if (CollUtil.isNotEmpty(saveRipList)) {
//            crosscutRequireService.saveBatch(saveCrossList);
//        }
//        if (CollUtil.isNotEmpty(updateRipList)) {
//            crosscutRequireService.updateBatchById(updateCrossList);
//        }
    }

    @Override
    public String customerFileImportDataReview(List<CustomerProfileImportDTO> importData) {
        //从数据库取出所有贸易客户信息
        List<CustomerProfile> customerList = this.list(Wrappers.<CustomerProfile>lambdaQuery()
                .eq(CustomerProfile::getCustomerType, CustomerTypeEnum.TRADE.getType()).ne(CustomerProfile::getCustomerCode, "KJZN"));
        List<CustomerProfile> delegate = this.list(Wrappers.<CustomerProfile>lambdaQuery()
                .eq(CustomerProfile::getCustomerType, CustomerTypeEnum.DELEGATE.getType()).ne(CustomerProfile::getCustomerCode, "KJZN"));
        List<String> delegateCode = delegate.stream().map(CustomerProfile::getCustomerCode).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<CustomerProfileImportDTO> delegateDataList = importData.stream().filter(data -> {
            int customerType = !ArrayUtil.contains(new String[]{"KKK", "KKS", "KKW", "KKF", "KKY"}, data.getCustomerCode()) && "K".equalsIgnoreCase(data.getCustomerCode().substring(0, 1))
                    ? CustomerTypeEnum.TRADE.getType()
                    : CustomerTypeEnum.DELEGATE.getType();
            return customerType == CustomerTypeEnum.DELEGATE.getType();
        }).collect(Collectors.toList());
        List<String> duplicatedDelegateCode = delegateDataList.stream().map(CustomerProfileImportDTO::getCustomerCode).filter(delegateCode::contains).collect(Collectors.toList());
        List<String> enduplicatedDelegateCode = delegateDataList.stream().map(CustomerProfileImportDTO::getCustomerCode).filter(data -> !delegateCode.contains(data)).collect(Collectors.toList());

        Map<String, CustomerProfile> codeMap = customerList.stream().collect(Collectors.toMap(CustomerProfile::getCustomerCode, item -> item));
        List<String> customerCodeList = customerList.stream().map(CustomerProfile::getCustomerCode).filter(StrUtil::isNotBlank).collect(Collectors.toList());

        List<CustomerProfileImportDTO> tradeDataList = importData.stream().filter(data -> {
            int customerType = !ArrayUtil.contains(new String[]{"KKK", "KKS", "KKW", "KKF", "KKY"}, data.getCustomerCode()) && "K".equalsIgnoreCase(data.getCustomerCode().substring(0, 1))
                    ? CustomerTypeEnum.TRADE.getType()
                    : CustomerTypeEnum.DELEGATE.getType();
            return customerType == CustomerTypeEnum.TRADE.getType();
        }).collect(Collectors.toList());
        final String[] customerCode = {""};
        List<String> duplicatedCode = tradeDataList.stream().map(CustomerProfileImportDTO::getCustomerCode).filter(customerCodeList::contains).collect(Collectors.toList());
        tradeDataList.forEach(trade -> {
            if (customerCodeList.contains(trade.getCustomerCode())
                    && !trade.getCustomerName().equals(codeMap.get(trade.getCustomerCode()).getCustomerName())) {
                CustomerProfile customerProfile = codeMap.get(trade.getCustomerCode());
                customerCode[0] = customerCode[0] + trade.getCustomerCode() + ",";
                System.out.printf("Code：%s，系统客户名称：%s，导入数据名称：%s \n", trade.getCustomerCode(), customerProfile.getCustomerName(), trade.getCustomerName());
            }
        });

        return "委托单位一共重复" + duplicatedDelegateCode.size() + "条" + "---不重复的：" + String.join(",", enduplicatedDelegateCode);
    }

    @Override
    @GlobalTransactional
    public boolean removeCustomerById(Long id) {
        Result<Boolean> existsResult = remoteProduceRawMaterialService.existsInventory(id);
        Boolean exists = ResultOps.of(existsResult).getData().orElseThrow();
        if (exists) {
            throw new BaseException(existsResult.getMsg());
        }
        boolean result = removeById(id);
        remoteProduceCustomerService.delete(id);
        return result;
    }

    private void setCrosscutRequire(CustomerRequireImportDTO data, CrosscutRequire crosscutRequire) {
        crosscutRequire.setThicknessTolerance(data.getJbhoudugongcha());
        crosscutRequire.setWidthTolerance(data.getJbchangdugongcha());
        crosscutRequire.setHardness(data.getJbyingdu());
        crosscutRequire.setBurr(data.getJbmaoci());
        crosscutRequire.setSideWave(data.getBianbo());
        crosscutRequire.setPackageWeight(data.getJbbaozhuangzhong());
        crosscutRequire.setRemark(data.getJbjiagongyaoqiu());
        crosscutRequire.setWeighingWay(CommonConstants.TRUE);

    }

    private void setRipCutRequire(CustomerRequireImportDTO data, RipCutRequire ripCutRequire) {
        ripCutRequire.setThicknessTolerance(data.getHoudugongcha());
        ripCutRequire.setWidthTolerance(data.getKuandugongcha());
        ripCutRequire.setHardness(data.getYingdu());
        ripCutRequire.setBurr(data.getMaoci());
        ripCutRequire.setCurvature(data.getWanqudu());
        ripCutRequire.setPackageWeight(data.getBaozhuangzhong());
        if (ObjUtil.isNotNull(data.getZuidawaijing())) {
            ripCutRequire.setMaxOutDiameter(data.getZuidawaijing());
        } else {
            ripCutRequire.setMaxOutDiameter(BigDecimal.valueOf(1200));
        }
        if (ObjUtil.isNotNull(data.getZuixiaoneijing())) {
            ripCutRequire.setMinInDiameter(data.getZuixiaoneijing());
        } else {
            ripCutRequire.setMaxOutDiameter(BigDecimal.valueOf(508));
        }
        ripCutRequire.setRemark(data.getJiagongyaoqiu());
        ripCutRequire.setWeighingWay(CommonConstants.TRUE);
    }

    @NotNull
    private static List<AccessoryCharge> getAccessoryCharges() {
        AccessoryCharge accessoryCharge0 = new AccessoryCharge();
        accessoryCharge0.setChargeItem("横切小木架");
        accessoryCharge0.setChargeWay(0);
        accessoryCharge0.setUnit(0);
        accessoryCharge0.setUnitPrice(BigDecimal.ZERO);
        accessoryCharge0.setUseScope(2);
        accessoryCharge0.setOrderSort(0);

        AccessoryCharge accessoryCharge1 = new AccessoryCharge();
        accessoryCharge1.setChargeItem("横切中木架");
        accessoryCharge1.setChargeWay(0);
        accessoryCharge1.setUnit(0);
        accessoryCharge1.setUnitPrice(BigDecimal.ZERO);
        accessoryCharge1.setUseScope(2);
        accessoryCharge1.setOrderSort(1);

        AccessoryCharge accessoryCharge2 = new AccessoryCharge();
        accessoryCharge2.setChargeItem("横切大木架");
        accessoryCharge2.setChargeWay(0);
        accessoryCharge2.setUnit(0);
        accessoryCharge2.setUnitPrice(BigDecimal.ZERO);
        accessoryCharge2.setUseScope(2);
        accessoryCharge2.setOrderSort(2);

        AccessoryCharge accessoryCharge3 = new AccessoryCharge();
        accessoryCharge3.setChargeItem("铁架");
        accessoryCharge3.setChargeWay(0);
        accessoryCharge3.setUnit(0);
        accessoryCharge3.setUnitPrice(BigDecimal.ZERO);
        accessoryCharge3.setUseScope(2);
        accessoryCharge3.setOrderSort(3);

        AccessoryCharge accessoryCharge4 = new AccessoryCharge();
        accessoryCharge4.setChargeItem("分条木架");
        accessoryCharge4.setChargeWay(0);
        accessoryCharge4.setUnit(0);
        accessoryCharge4.setUnitPrice(BigDecimal.ZERO);
        accessoryCharge4.setUseScope(2);
        accessoryCharge4.setOrderSort(4);

        AccessoryCharge accessoryCharge5 = new AccessoryCharge();
        accessoryCharge5.setChargeItem("纸筒");
        accessoryCharge5.setChargeWay(0);
        accessoryCharge5.setUnit(0);
        accessoryCharge5.setUnitPrice(BigDecimal.ZERO);
        accessoryCharge5.setUseScope(2);
        accessoryCharge5.setOrderSort(5);
        List<AccessoryCharge> accessoryCharges = Lists.newArrayList();
        accessoryCharges.add(accessoryCharge0);
        accessoryCharges.add(accessoryCharge1);
        accessoryCharges.add(accessoryCharge2);
        accessoryCharges.add(accessoryCharge3);
        accessoryCharges.add(accessoryCharge4);
        accessoryCharges.add(accessoryCharge5);
        return accessoryCharges;
    }
}
