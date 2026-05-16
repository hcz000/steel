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
import com.gv.steel.system.base.dao.RipCutChargeDao;
import com.gv.steel.system.base.dto.RipCutChargeQueryDTO;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.entity.RipCutCharge;
import com.gv.steel.system.base.service.MaterialQualityService;
import com.gv.steel.system.base.service.RipCutChargeService;
import com.gv.steel.system.base.utils.RangeUtil;
import com.gv.steel.system.base.vo.RipCutChargePageVO;
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
 * 纵切收费 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RipCutChargeServiceImpl extends BaseServiceImpl<RipCutChargeDao, RipCutCharge> implements RipCutChargeService {
    private final MaterialQualityService materialQualityService;

    private final ProcessingContractDao processingContractDao;

    @Override
    public List<RipCutCharge> findListByCustomerId(Long customerId) {
        ProcessingContract contract = processingContractDao.selectOne(Wrappers.<ProcessingContract>lambdaQuery().eq(ProcessingContract::getCustomerId, customerId));
        if (ObjUtil.isNull(contract)) {
            return Lists.newArrayList();
        }

        return list(Wrappers.<RipCutCharge>lambdaQuery().eq(RipCutCharge::getContractId, contract.getId()));
    }

    @Override
    public boolean saveOrUpdateBatch(Long contractId, List<RipCutCharge> ripCutChargeList) {
        ripCutChargeList.forEach(item -> item.setContractId(contractId));

        Map<Long, List<List<Range<BigDecimal>>>> hasIntersection = hasIntersection(ripCutChargeList);
        if (MapUtil.isNotEmpty(hasIntersection)) {
            List<MaterialQuality> materialQualityList = materialQualityService.list(Wrappers.<MaterialQuality>lambdaQuery().in(MaterialQuality::getId, hasIntersection.keySet()));
            Map<Long, String> materialNameMap = materialQualityList.stream().collect(Collectors.toMap(MaterialQuality::getId, MaterialQuality::getName));
            String msg = RangeUtil.hasIntersectionExceptionMsg("rip.cut.charge.specification.range.has.intersection", hasIntersection, materialNameMap);
            throw new BaseException(msg);
        }

        return subtractSaveOrUpdateBatch(contractId, ripCutChargeList, RipCutCharge::getContractId, RipCutCharge::getId);
    }

    @Override
    public PageResult<RipCutChargePageVO> pageRipCutCharge(Page<RipCutChargePageVO> page, RipCutChargeQueryDTO dto) {
        baseDao.selectRipCutChargePage(page, dto);
        return PageResult.<RipCutChargePageVO>builder().build().pageResult(page);
    }

    /**
     * 判断各个材质的规格范围是否有交集
     *
     * @param ripCutChargeList 纵切收费列表
     * @return <materialId, [plyRangeList: 厚度范围集合, widthRangeList: 宽度范围集合]>
     */
    private Map<Long, List<List<Range<BigDecimal>>>> hasIntersection(List<RipCutCharge> ripCutChargeList) {
        Map<Long, List<List<Range<BigDecimal>>>> resultMap = Maps.newHashMap();
        ripCutChargeList.stream().collect(Collectors.groupingBy(RipCutCharge::getChargeType)).forEach((chargeType, charegeList) -> {
            // materialId -> [plyRangeList: 厚度范围集合, widthRangeList: 宽度范围集合]
            Map<Long, List<List<Range<BigDecimal>>>> materialSpecRangeMap = Maps.newHashMap();
            charegeList.forEach(ripCutCharge -> {
                Range<BigDecimal> plyRange = Range.closed(ripCutCharge.getMaterialPly1(), ripCutCharge.getMaterialPly2());
                Range<BigDecimal> widthRange = Range.closed(ripCutCharge.getMaterialWidth1(), ripCutCharge.getMaterialWidth2());

                Arrays.stream(ripCutCharge.getMaterialId()).forEach(materialId -> {
                    if (materialSpecRangeMap.containsKey(materialId)) {
                        materialSpecRangeMap.get(materialId).get(0).add(plyRange);
                        materialSpecRangeMap.get(materialId).get(1).add(widthRange);
                    } else {
                        List<Range<BigDecimal>> plyRangeList = Lists.newArrayList(plyRange);
                        List<Range<BigDecimal>> widthRangeList = Lists.newArrayList(widthRange);
                        materialSpecRangeMap.put(materialId, Lists.newArrayList(plyRangeList, widthRangeList));
                    }
                });
            });

            resultMap.putAll(RangeUtil.hasIntersection(materialSpecRangeMap));
        });
        return resultMap;
    }
}
