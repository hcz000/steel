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
import com.gv.steel.system.base.dao.CrossCutChargeDao;
import com.gv.steel.system.base.dao.ProcessingContractDao;
import com.gv.steel.system.base.dto.CrossCutChargeQueryDTO;
import com.gv.steel.system.base.entity.CrossCutCharge;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.entity.ProcessingContract;
import com.gv.steel.system.base.service.CrossCutChargeService;
import com.gv.steel.system.base.service.MaterialQualityService;
import com.gv.steel.system.base.utils.RangeUtil;
import com.gv.steel.system.base.vo.CrossCutChargePageVO;
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
 * 横切收费 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CrossCutChargeServiceImpl extends BaseServiceImpl<CrossCutChargeDao, CrossCutCharge> implements CrossCutChargeService {
    private final MaterialQualityService materialQualityService;

    private final ProcessingContractDao processingContractDao;

    @Override
    public PageResult<CrossCutChargePageVO> pageCrossCut(Page<CrossCutChargePageVO> page, CrossCutChargeQueryDTO dto) {
        baseDao.selectCrossCutPage(page, dto);
        return PageResult.<CrossCutChargePageVO>builder().build().pageResult(page);
    }

    @Override
    public List<CrossCutCharge> findListByCustomerId(Long customerId) {
        ProcessingContract contract = processingContractDao.selectOne(Wrappers.<ProcessingContract>lambdaQuery().eq(ProcessingContract::getCustomerId, customerId));
        if (ObjUtil.isNull(contract)) {
            return Lists.newArrayList();
        }

        return list(Wrappers.<CrossCutCharge>lambdaQuery().eq(CrossCutCharge::getContractId, contract.getId()));
    }

    @Override
    public boolean saveOrUpdateBatch(Long contractId, List<CrossCutCharge> crossCutChargeList) {
        crossCutChargeList.forEach(item -> item.setContractId(contractId));

        Map<Long, List<List<Range<BigDecimal>>>> hasIntersection = hasIntersection(crossCutChargeList);
        if (MapUtil.isNotEmpty(hasIntersection)) {
            List<MaterialQuality> materialQualityList = materialQualityService.list(Wrappers.<MaterialQuality>lambdaQuery().in(MaterialQuality::getId, hasIntersection.keySet()));
            Map<Long, String> materialNameMap = materialQualityList.stream().collect(Collectors.toMap(MaterialQuality::getId, MaterialQuality::getName));
            String msg = RangeUtil.hasIntersectionExceptionMsg("cross.cut.charge.specification.range.has.intersection", hasIntersection, materialNameMap);
            throw new BaseException(msg);
        }
        return subtractSaveOrUpdateBatch(contractId, crossCutChargeList, CrossCutCharge::getContractId, CrossCutCharge::getId);
    }

    /**
     * 判断各个材质的规格范围是否有交集
     *
     * @param crossCutChargeList 横切收费列表
     * @return <materialId, [plyRangeList: 厚度范围集合, widthRangeList: 宽度范围集合]>
     */
    private Map<Long, List<List<Range<BigDecimal>>>> hasIntersection(List<CrossCutCharge> crossCutChargeList) {
        // materialId -> [plyRangeList: 厚度范围集合, widthRangeList: 宽度范围集合, lengthRangeList: 长度范围集合]
        Map<Long, List<List<Range<BigDecimal>>>> materialSpecRangeMap = Maps.newHashMap();
        crossCutChargeList.forEach(crossCutCharge -> {
            Range<BigDecimal> plyRange = Range.closed(crossCutCharge.getMaterialPly1(), crossCutCharge.getMaterialPly2());
            Range<BigDecimal> widthRange = Range.closed(crossCutCharge.getProductWidth1(), crossCutCharge.getProductWidth2());
            Range<BigDecimal> lengthRange = Range.closed(crossCutCharge.getProductLength1(), crossCutCharge.getProductLength2());

            Arrays.stream(crossCutCharge.getMaterialId()).forEach(materialId -> {
                if (materialSpecRangeMap.containsKey(materialId)) {
                    materialSpecRangeMap.get(materialId).get(0).add(plyRange);
                    materialSpecRangeMap.get(materialId).get(1).add(widthRange);
                    materialSpecRangeMap.get(materialId).get(2).add(lengthRange);
                } else {
                    List<Range<BigDecimal>> plyRangeList = Lists.newArrayList(plyRange);
                    List<Range<BigDecimal>> widthRangeList = Lists.newArrayList(widthRange);
                    List<Range<BigDecimal>> lengthRangeList = Lists.newArrayList(lengthRange);
                    materialSpecRangeMap.put(materialId, Lists.newArrayList(plyRangeList, widthRangeList, lengthRangeList));
                }
            });
        });

        return RangeUtil.hasIntersection(materialSpecRangeMap);
    }
}
