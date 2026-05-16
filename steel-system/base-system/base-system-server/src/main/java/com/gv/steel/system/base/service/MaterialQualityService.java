package com.gv.steel.system.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.core.api.PageResult;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.MaterialQualityDTO;
import com.gv.steel.system.base.dto.MaterialQualityPageDTO;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.vo.MaterialQualityVO;

import java.util.List;

/**
 * <p>
 * 材质管理表 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-10-31
 */
public interface MaterialQualityService extends BaseService<MaterialQuality> {

    /**
     * 材质管理表查询列表
     *
     * @param page 分页参数
     * @param dto  查询参数
     * @return Page<MaterialQuality>
     */
    PageResult<MaterialQualityVO> getPage(Page<MaterialQuality> page, MaterialQualityPageDTO dto);

    MaterialQualityVO getMaterialQualityById(Long id);

    boolean saveMaterialQuality(MaterialQualityDTO dto);

    boolean updateMaterialQualityById(MaterialQualityDTO dto);

    boolean removeMaterialQualityById(Long id);

    List<MaterialQuality> getMaterialQualityList();
}
