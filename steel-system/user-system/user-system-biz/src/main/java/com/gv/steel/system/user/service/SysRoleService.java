package com.gv.steel.system.user.service;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.user.entity.SysRole;
import com.gv.steel.system.user.vo.RoleExcelVO;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 通过角色ID，删除角色
     *
     * @param id
     * @return
     */
    Boolean removeRoleById(Long id);

    /**
     * 导入角色
     *
     * @param excelVOList   角色列表
     * @param bindingResult 错误信息列表
     * @return ok fail
     */
    Result importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * 查询全部的角色
     *
     * @return list
     */
    List<RoleExcelVO> listRole();

}
