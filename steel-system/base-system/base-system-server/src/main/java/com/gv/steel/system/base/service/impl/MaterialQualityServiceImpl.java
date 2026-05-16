package com.gv.steel.system.base.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.mybatis.base.entity.CommonModel;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.base.convert.MaterialQualityMapper;
import com.gv.steel.system.base.dao.MaterialQualityDao;
import com.gv.steel.system.base.dto.MaterialQualityDTO;
import com.gv.steel.system.base.dto.MaterialQualityPageDTO;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.entity.MaterialQualityLang;
import com.gv.steel.system.base.service.MaterialQualityLangService;
import com.gv.steel.system.base.service.MaterialQualityService;
import com.gv.steel.system.base.vo.MaterialQualityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 材质管理表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MaterialQualityServiceImpl extends BaseServiceImpl<MaterialQualityDao, MaterialQuality> implements MaterialQualityService {
    private final MaterialQualityMapper materialQualityMapper;

    private final MaterialQualityLangService materialQualityLangService;

    @Override
    public PageResult<MaterialQualityVO> getPage(Page<MaterialQuality> page, MaterialQualityPageDTO dto) {
        LambdaQueryWrapper<MaterialQuality> wrapper = getWrapper(dto);
        this.page(page, wrapper);
        Page<MaterialQualityVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<MaterialQualityVO> records = materialQualityMapper.convertVOList(page.getRecords());
        records.forEach(vo ->
                vo.setLangList(
                        materialQualityLangService.list(Wrappers.<MaterialQualityLang>lambdaQuery().eq(MaterialQualityLang::getMaterialQualityId, vo.getId()))
                )
        );
        voPage.setRecords(records);
        return PageResult.<MaterialQualityVO>builder().build().pageResult(voPage);
    }

    @Override
    public MaterialQualityVO getMaterialQualityById(Long id) {
        MaterialQuality materialQuality = getById(id);
        MaterialQualityVO materialQualityVO = materialQualityMapper.convertVO(materialQuality);
        List<MaterialQualityLang> list = materialQualityLangService.list(Wrappers.<MaterialQualityLang>lambdaQuery().eq(MaterialQualityLang::getMaterialQualityId, id));
        materialQualityVO.setLangList(list);
        return materialQualityVO;
    }

    @Override
    public List<MaterialQuality> getMaterialQualityList() {
        String languageTag = LocaleContextHolder.getLocale().toLanguageTag();
        return baseDao.selectMaterialQualityList(languageTag);
    }

    @Override
    public boolean saveMaterialQuality(MaterialQualityDTO dto) {
        validateLangList(dto);

        MaterialQuality materialQuality = materialQualityMapper.convert(dto);
        save(materialQuality);
        dto.getLangList().forEach(lang -> lang.setMaterialQualityId(materialQuality.getId()));
        materialQualityLangService.saveBatch(dto.getLangList());
        return true;
    }

    @Override
    public boolean updateMaterialQualityById(MaterialQualityDTO dto) {
        validateLangList(dto);

        MaterialQuality materialQuality = materialQualityMapper.convert(dto);
        updateById(materialQuality);
        dto.getLangList().forEach(lang -> lang.setMaterialQualityId(materialQuality.getId()));
        materialQualityLangService.subtractSaveOrUpdateBatch(dto.getId(), dto.getLangList(), MaterialQualityLang::getMaterialQualityId, MaterialQualityLang::getId);
        return true;
    }

    @Override
    public boolean removeMaterialQualityById(Long id) {
        removeById(id);
        materialQualityLangService.remove(Wrappers.<MaterialQualityLang>lambdaQuery().eq(MaterialQualityLang::getMaterialQualityId, id));
        return true;
    }

    private void validateLangList(MaterialQualityDTO dto) {
        Map<String, List<MaterialQualityLang>> listMap = dto.getLangList().stream().collect(Collectors.groupingBy(MaterialQualityLang::getLang));
        boolean duplicateFlag = listMap.entrySet().stream().anyMatch(entry -> entry.getValue().size() > 1);
        if (duplicateFlag)
            throw new BaseException(MsgUtils.getMessage("material.quality.create.language.repeat.config"));
    }

    private LambdaQueryWrapper<MaterialQuality> getWrapper(MaterialQualityPageDTO dto) {
        LambdaQueryWrapper<MaterialQuality> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(dto)) {
            if (StrUtil.isNotEmpty(dto.getName())) {
                wrapper.like(MaterialQuality::getName, dto.getName());
            }
            if (dto.getType() != null) {
                wrapper.eq(MaterialQuality::getType, dto.getType());
            }
            if (ArrayUtil.isNotEmpty(dto.getTimeZone())) {
                wrapper.between(CommonModel::getCreateTime, dto.getTimeZone()[0], dto.getTimeZone()[1]);
            }
        }

        wrapper.orderByAsc(MaterialQuality::getOrderSort);
        return wrapper;
    }
}
