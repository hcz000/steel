package com.gv.steel.system.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.common.rocketmq.base.BaseMessage;
import com.gv.steel.common.rocketmq.core.RocketMQEnhanceTemplate;
import com.gv.steel.system.base.constant.RocketMQConstant;
import com.gv.steel.system.base.convert.ProcessingContractMapper;
import com.gv.steel.system.base.dao.CustomerProfileDao;
import com.gv.steel.system.base.dao.ProcessingContractDao;
import com.gv.steel.system.base.dto.ProcessingContractDTO;
import com.gv.steel.system.base.dto.ProcessingContractQueryDTO;
import com.gv.steel.system.base.dto.ReCalChargeMessageDTO;
import com.gv.steel.system.base.entity.*;
import com.gv.steel.system.base.service.*;
import com.gv.steel.system.base.vo.ProcessingContractDetailVO;
import com.gv.steel.system.base.vo.ProcessingContractPageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 加工合同 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProcessingContractServiceImpl extends BaseServiceImpl<ProcessingContractDao, ProcessingContract> implements ProcessingContractService {
    private final ProcessingContractMapper processingContractMapper;

    private final RipCutChargeService ripCutChargeService;

    private final CrossCutChargeService crossCutChargeService;

    private final RollingChargeService rollingChargeService;

    private final AccessoryChargeService accessoryChargeService;

    private final AttachService attachService;

    private final RocketMQEnhanceTemplate rocketMQEnhanceTemplate;

	private final CustomerProfileDao customerProfileDao;

    @Override
    public PageResult<ProcessingContractPageVO> pageProcessingContract(Page<ProcessingContractPageVO> page, ProcessingContractQueryDTO dto) {
        baseDao.selectProcessingContractPage(page, dto);
        return PageResult.<ProcessingContractPageVO>builder().build().pageResult(page);
    }

    @Override
    public ProcessingContractDetailVO getProcessingContractDetailById(Long id) {
        ProcessingContract processingContract = getById(id);
        ProcessingContractDetailVO vo = processingContractMapper.convertVO(processingContract);
        return getRelationItem(vo);
    }

    @Override
    public ProcessingContractDetailVO getProcessingContractDetailByCustomerId(Long customerId) {
        ProcessingContract processingContract = getOne(Wrappers.<ProcessingContract>lambdaQuery().eq(ProcessingContract::getCustomerId, customerId));
        if (ObjUtil.isNotNull(processingContract)) {
            ProcessingContractDetailVO vo = processingContractMapper.convertVO(processingContract);
            return getRelationItem(vo);
        }
        return null;
    }

    private ProcessingContractDetailVO getRelationItem(ProcessingContractDetailVO vo) {
        String tableName = TableInfoHelper.getTableInfo(ProcessingContract.class).getTableName();
        // 合同附件
        List<Attach> attachList = attachService.list(Wrappers.<Attach>lambdaQuery()
                .eq(Attach::getTableId, vo.getId())
                .eq(Attach::getTableName, tableName)
                .orderByDesc(Attach::getCreateName)
        );
        vo.setAttachList(attachList);

        // 纵切加工费
        List<RipCutCharge> ripCutChargeList = ripCutChargeService.list(
                Wrappers.<RipCutCharge>lambdaQuery()
                        .eq(RipCutCharge::getContractId, vo.getId())
                        .orderByAsc(RipCutCharge::getMaterialId)
                        .orderByAsc(RipCutCharge::getMaterialPly1)
                        .orderByAsc(RipCutCharge::getMaterialWidth1)
        );
        vo.setRipCutChargeList(ripCutChargeList);

        // 横切加工费
        List<CrossCutCharge> crossCutChargeList = crossCutChargeService.list(
                Wrappers.<CrossCutCharge>lambdaQuery()
                        .eq(CrossCutCharge::getContractId, vo.getId())
                        .orderByAsc(CrossCutCharge::getMaterialId)
                        .orderByAsc(CrossCutCharge::getMaterialPly1)
                        .orderByAsc(CrossCutCharge::getProductWidth1)
                        .orderByAsc(CrossCutCharge::getProductLength1)
        );
        vo.setCrossCutChargeList(crossCutChargeList);

        // 压延加工费
        List<RollingCharge> rollingChargeList = rollingChargeService.list(
                Wrappers.<RollingCharge>lambdaQuery()
                        .eq(RollingCharge::getContractId, vo.getId())
                        .orderByAsc(RollingCharge::getMaterialPly1)
                        .orderByAsc(RollingCharge::getProductPly1)
        );
        vo.setRollingChargeList(rollingChargeList);

        // 辅料收费
        List<AccessoryCharge> accessoryChargeList = accessoryChargeService.list(
                Wrappers.<AccessoryCharge>lambdaQuery()
                        .eq(AccessoryCharge::getContractId, vo.getId())
                        .orderByAsc(AccessoryCharge::getOrderSort)
        );
        vo.setAccessoryChargeList(accessoryChargeList);

		CustomerProfile customerProfile = customerProfileDao.selectById(vo.getCustomerId());
		vo.setFactoryId(customerProfile.getFactoryId());

		return vo;
    }

    @Override
    public Long saveProcessContract(ProcessingContractDTO dto) {
		if (baseDao.exists(
				Wrappers.<ProcessingContract>lambdaQuery()
						.eq(ProcessingContract::getCustomerId, dto.getCustomerId())
		)) {
			throw new BaseException("该用户已存在加工合同");
		}
        ProcessingContract processingContract = processingContractMapper.convert(dto);
        save(processingContract);

        // 新增合同附件
        List<Attach> attachList = dto.getAttachList();
        if (ObjUtil.isNotEmpty(attachList)) {
            String tableName = TableInfoHelper.getTableInfo(ProcessingContract.class).getTableName();
            attachList.forEach(attach -> {
                attach.setTableId(processingContract.getId());
                attach.setTableName(tableName);
            });
            attachService.saveBatch(attachList);
        }

        // 新增纵切收费
        if (CollUtil.isNotEmpty(dto.getRipCutChargeList())) {
            ripCutChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getRipCutChargeList());
        }

        // 新增横切收费
        if (CollUtil.isNotEmpty(dto.getCrossCutChargeList())) {
            crossCutChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getCrossCutChargeList());
        }

        // 新增压延收费
        if (CollUtil.isNotEmpty(dto.getRollingChargeList())) {
            rollingChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getRollingChargeList());
        }

        // 新增辅料收费
        if (CollUtil.isNotEmpty(dto.getAccessoryChargeList())) {
            accessoryChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getAccessoryChargeList());
        }

        // 远程调用加工接口，重新计算加工费是否核算正常
        reCalculateProcessingCharge(processingContract.getCustomerId());

        return processingContract.getId();
    }

    @Override
    public Long updateProcessContract(ProcessingContractDTO dto) {
        ProcessingContract processingContract = processingContractMapper.convert(dto);
        updateById(processingContract);

        // 新增或修改合同附件
        List<Attach> attachList = dto.getAttachList();
        if (ObjUtil.isNotEmpty(attachList)) {
            String tableName = TableInfoHelper.getTableInfo(ProcessingContract.class).getTableName();
            attachList.forEach(attach -> {
                attach.setTableId(processingContract.getId());
                attach.setTableName(tableName);
            });
            attachService.subtractSaveOrUpdateBatch(processingContract.getId(), attachList, Attach::getTableId, Attach::getId);
        }

        // 新增或修改纵切收费
        ripCutChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getRipCutChargeList());

        // 新增或修改横切收费
        crossCutChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getCrossCutChargeList());

        // 新增或修改压延收费
        rollingChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getRollingChargeList());

        // 新增或修改辅料收费
        accessoryChargeService.saveOrUpdateBatch(processingContract.getId(), dto.getAccessoryChargeList());

        // 远程调用加工接口，重新计算加工费是否核算正常
        reCalculateProcessingCharge(processingContract.getCustomerId());

        return processingContract.getId();
    }

    @Override
    public boolean removeProcessContractById(Long id) {
        removeById(id);

        // 删除合同附件
        String tableName = TableInfoHelper.getTableInfo(ProcessingContract.class).getTableName();
        attachService.remove(Wrappers.<Attach>lambdaQuery().eq(Attach::getTableId, id).eq(Attach::getTableName, tableName));

        // 删除纵切收费
        ripCutChargeService.remove(Wrappers.<RipCutCharge>lambdaQuery().eq(RipCutCharge::getContractId, id));

        // 删除横切收费
        crossCutChargeService.remove(Wrappers.<CrossCutCharge>lambdaQuery().eq(CrossCutCharge::getContractId, id));

        // 删除压延收费
        rollingChargeService.remove(Wrappers.<RollingCharge>lambdaQuery().eq(RollingCharge::getContractId, id));

        // 删除辅料收费
        accessoryChargeService.remove(Wrappers.<AccessoryCharge>lambdaQuery().eq(AccessoryCharge::getContractId, id));

        return true;
    }

    private void reCalculateProcessingCharge(Long customerId) {
        ReCalChargeMessageDTO dto = new ReCalChargeMessageDTO();
        dto.setCustomerId(customerId);
        BaseMessage<ReCalChargeMessageDTO> message = new BaseMessage<>(dto);
        rocketMQEnhanceTemplate.asyncSend(RocketMQConstant.PUSH_RE_CALCULATE_PROCESSING_CHARGE_TOPIC, RocketMQConstant.DEFAULT_TAG, message);
    }
}
