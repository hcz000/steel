package com.gv.steel.system.base.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.dao.ProcessingContractDao;
import com.gv.steel.system.base.dao.RollingChargeDao;
import com.gv.steel.system.base.dto.RollingChargeQueryDTO;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.entity.RollingCharge;
import com.gv.steel.system.base.service.MaterialQualityService;
import com.gv.steel.system.base.service.RollingChargeService;
import com.gv.steel.system.base.utils.RangeUtil;
import com.gv.steel.system.base.vo.RollingChargePageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 压延收费 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RollingChargeServiceImpl extends BaseServiceImpl<RollingChargeDao, RollingCharge> implements RollingChargeService {
    private final MaterialQualityService materialQualityService;

    private final ProcessingContractDao processingContractDao;

    @Override
    public PageResult<RollingChargePageVO> pageRollingCharge(Page<RollingChargePageVO> page, RollingChargeQueryDTO dto) {
        baseDao.selectRollingChargePage(page, dto);
        return PageResult.<RollingChargePageVO>builder().build().pageResult(page);
    }

    @Override
    public List<RollingCharge> findListByCustomerId(Long customerId) {
        ProcessingContract contract = processingContractDao.selectOne(Wrappers.<ProcessingContract>lambdaQuery().eq(ProcessingContract::getCustomerId, customerId));
        if (ObjUtil.isNull(contract)) {
            return Lists.newArrayList();
        }

        return list(Wrappers.<RollingCharge>lambdaQuery().eq(RollingCharge::getContractId, contract.getId()));
    }

    @Override
    public boolean saveOrUpdateBatch(Long contractId, List<RollingCharge> rollingChargeList) {
        rollingChargeList.forEach(item -> item.setContractId(contractId));

        Map<Long, List<List<Range<BigDecimal>>>> hasIntersection = hasIntersection(rollingChargeList);
        if (MapUtil.isNotEmpty(hasIntersection)) {
            List<MaterialQuality> materialQualityList = materialQualityService.list(Wrappers.<MaterialQuality>lambdaQuery().in(MaterialQuality::getId, hasIntersection.keySet()));
            Map<Long, String> materialNameMap = materialQualityList.stream().collect(Collectors.toMap(MaterialQuality::getId, MaterialQuality::getName));
            String msg = RangeUtil.hasIntersectionExceptionMsg("rolling.charge.specification.range.has.intersection", hasIntersection, materialNameMap);
            throw new BaseException(msg);
        }
        return subtractSaveOrUpdateBatch(contractId, rollingChargeList, RollingCharge::getContractId, RollingCharge::getId);
    }

    /**
     * 判断各个材质的规格范围是否有交集
     *
     * @param rollingChargeList 压延收费列表
     * @return <materialId, [plyRangeList: 厚度范围集合, proPylRange: 成品厚度范围集合]>
     */
    private Map<Long, List<List<Range<BigDecimal>>>> hasIntersection(List<RollingCharge> rollingChargeList) {
        // materialId -> [plyRangeList: 厚度范围集合, proPylRange: 成品厚度范围集合]
        Map<Long, List<List<Range<BigDecimal>>>> materialSpecRangeMap = Maps.newHashMap();
        rollingChargeList.forEach(rollingCharge -> {
            Range<BigDecimal> plyRange = Range.closed(rollingCharge.getMaterialPly1(), rollingCharge.getMaterialPly2());
            Range<BigDecimal> proPylRange = Range.closed(rollingCharge.getProductPly1(), rollingCharge.getProductPly2());

            Arrays.stream(rollingCharge.getMaterialId()).forEach(materialId -> {
                if (materialSpecRangeMap.containsKey(materialId)) {
                    materialSpecRangeMap.get(materialId).get(0).add(plyRange);
                    materialSpecRangeMap.get(materialId).get(1).add(proPylRange);
                } else {
                    List<Range<BigDecimal>> plyRangeList = Lists.newArrayList(plyRange);
                    List<Range<BigDecimal>> proPlyRangeList = Lists.newArrayList(proPylRange);
                    materialSpecRangeMap.put(materialId, Lists.newArrayList(plyRangeList, proPlyRangeList));
                }
            });
        });

        return RangeUtil.hasIntersection(materialSpecRangeMap);
    }
}
