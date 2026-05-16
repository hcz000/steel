package com.gv.steel.system.user.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.user.dto.UserDTO;
import com.gv.steel.system.user.entity.SysUser;
import com.gv.steel.system.user.vo.UserDetailVO;
import com.gv.steel.system.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 */
@Mapper
public interface SysUserDao extends BaseDao<SysUser> {

    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO getUserVoByUsername(String username);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param page    分页
     * @param userDTO 查询参数
     * @return list
     */
    Page<UserVO> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return userVo
     */
    UserDetailVO getUserVoById(Long id);

    /**
     * 查询用户列表
     *
     * @param userDTO 查询条件
     * @return
     */
    List<UserVO> selectVoList(@Param("query") UserDTO userDTO);

}
