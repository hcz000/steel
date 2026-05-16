package com.gv.steel.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gv.steel.common.core.api.Result;
import com.gv.steel.common.core.util.MsgUtils;
import com.gv.steel.common.easyexcel.model.ErrorMessage;
import com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl;
import com.gv.steel.system.user.constant.ErrorCodeConstants;
import com.gv.steel.system.user.convert.SysRoleMapper;
import com.gv.steel.system.user.dao.SysRoleDao;
import com.gv.steel.system.user.dao.SysRoleMenuDao;
import com.gv.steel.system.user.entity.SysRole;
import com.gv.steel.system.user.entity.SysRoleMenu;
import com.gv.steel.system.user.service.SysRoleService;
import com.gv.steel.system.user.vo.RoleExcelVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    private final SysRoleMenuDao sysRoleMenuMapper;
    private final SysRoleMapper sysRoleMapper;

    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     *
     * @param id
     * @return
     */
    @Override
    public Boolean removeRoleById(Long id) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>update().lambda().eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }

    /**
     * 导入角色
     *
     * @param excelVOList   角色列表
     * @param bindingResult 错误信息列表
     * @return ok fail
     */
    @Override
    public Result<?> importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
        // 通用校验获取失败的数据
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 个性化校验逻辑
        List<SysRole> roleList = this.list();

        // 执行数据插入操作 组装 RoleDto
        for (RoleExcelVO excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();
            // 检验角色名称或者角色编码是否存在
            boolean existRole = roleList.stream()
                    .anyMatch(sysRole -> excel.getRoleName().equals(sysRole.getRoleName())
                            || excel.getRoleCode().equals(sysRole.getRoleCode()));

            if (existRole) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodeConstants.SYS_ROLE_NAMEORCODE_EXISTING, excel.getRoleName(),
                        excel.getRoleCode()));
            }

            // 数据合法情况
            if (CollUtil.isEmpty(errorMsg)) {
//                insertExcelRole(excel);
            } else {
                // 数据不合法情况
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
            }
        }
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return Result.failed(errorMessageList);
        }
        return Result.ok();
    }

    /**
     * 查询全部的角色
     *
     * @return list
     */
    @Override
    public List<RoleExcelVO> listRole() {
        List<SysRole> roleList = this.list(Wrappers.emptyWrapper());
        // 转换成execl 对象输出
        return sysRoleMapper.convertExcelVOList(roleList);
    }

    /**
     * 插入excel Role
     */
    private void insertExcelRole(RoleExcelVO excel) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(excel.getRoleName());
        sysRole.setRoleDesc(excel.getRoleDesc());
        sysRole.setRoleCode(excel.getRoleCode());
        this.save(sysRole);
    }

}
