package com.gv.steel.system.base.dto;

import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.entity.MaterialQualityLang;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "MaterialQualityDto", description = "材质信息")
public class MaterialQualityDTO extends MaterialQuality {
    private static final long serialVersionUID = 1L;

    @Schema(name = "langList", description = "材质语言列表")
    private List<MaterialQualityLang> langList = new ArrayList<>();
}
