package com.gv.steel.system.user.feign;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.constant.UserAppConstant;
import com.gv.steel.system.user.entity.SysDictItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 查询参数相关
 */
@FeignClient(contextId = "remoteDictService", value = UserAppConstant.APP_NAME)
public interface RemoteDictService {

    /**
     * 通过字典类型查找字典
     *
     * @param type 字典类型
     * @return 同类型字典
     */
    @GetMapping("/dict/key/{type}")
    Result<List<SysDictItem>> getDictByType(@PathVariable("type") String type);

}
