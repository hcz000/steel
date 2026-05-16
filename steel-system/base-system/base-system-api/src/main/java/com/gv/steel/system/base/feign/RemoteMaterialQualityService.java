package com.gv.steel.system.base.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.base.constant.BaseAppConstant;
import com.gv.steel.system.base.entity.MaterialQuality;
import com.gv.steel.system.base.vo.MaterialQualityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteMaterialQualityService", value = BaseAppConstant.APP_NAME)
public interface RemoteMaterialQualityService {
    @GetMapping("/material-quality/{id:\\d+}")
    Result<MaterialQualityVO> findMaterialQualityById(@PathVariable("id") Long id);

    @GetMapping("/material-quality/list")
    Result<List<MaterialQuality>> findMaterialQualityList();
}
