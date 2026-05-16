package com.gv.steel.system.base.dto;


import com.gv.steel.common.mybatis.annotation.query.Query;
import com.gv.steel.common.mybatis.annotation.query.QueryOrder;
import com.gv.steel.common.mybatis.annotation.query.QuerySort;
import com.gv.steel.common.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(name = "TodoTaskNoticeQueryDTO", description = "待办任务通知查询DTO")
public class SendTaskNoticeQueryDTO implements Serializable {
    private static final long serialVersionUID = -4221854501452113245L;

    @Query(expression = QueryType.LIKE)
    @Schema(description = "任务标题")
    private String title;

    @Query(expression = QueryType.BETWEEN)
    @Schema(description = "任务创建时间")
    private LocalDate[] createTime;

    @Query(expression = QueryType.IN)
    @Schema(description = "任务状态")
    private List<Integer> status;

    @Query(expression = QueryType.LIKE)
    @Schema(description = "接收人名称")
    private String receiveName;

    @Query(expression = QueryType.EQ, column = "createBy")
    @Schema(description = "当前用户ID", hidden = true)
    private Long userId;

    @QuerySort("createTime")
    @Schema(description = "排序字段", hidden = true)
    private String sortColumn;

    @QueryOrder("desc")
    @Schema(description = "排序顺序", hidden = true)
    private String sortOrder;
}
