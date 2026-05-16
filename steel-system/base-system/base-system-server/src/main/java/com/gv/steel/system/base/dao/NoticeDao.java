package com.gv.steel.system.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gv.steel.common.mybatis.base.dao.BaseDao;
import com.gv.steel.system.base.dto.NoticeQueryDTO;
import com.gv.steel.system.base.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 通知公告 Dao 接口
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Mapper
public interface NoticeDao extends BaseDao<Notice> {

    /**
     * 获取通知公告分页
     *
     * @param page 分页参数
     * @param dto  查询条件
     * @return Page<Notice>
     */
    Page<Notice> getPage(Page<Notice> page, @Param("param") NoticeQueryDTO dto);
}
