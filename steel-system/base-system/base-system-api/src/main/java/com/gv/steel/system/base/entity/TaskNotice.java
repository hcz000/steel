package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gv.steel.common.mybatis.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 任务通知表(收发件信箱)
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_task_notice")
@Schema(name = "TaskNotice对象", description = "任务通知表(收发件信箱)")
public class TaskNotice extends BaseEntity<TaskNotice> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "业务表主键ID")
    private Long tableId;

    @Schema(description = "业务表名")
    private String tableName;

    @Schema(description = "模块名称")
    private String module;

    @Schema(description = "任务标题")
    private String title;

    @Schema(description = "任务详情页")
    private String url;

    @Schema(description = "任务参数")
    private String params;

    @Schema(description = "关联范围;0：用户，1：部门")
    private Integer receiveScope;

    @Schema(description = "关联ID;receive_scope，receive_id为用户ID；receive_scope为1，receive_id为部门ID")
    private Long receiveId;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "接收人名称")
    private String receiveName;

    @Schema(description = "状态")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "发起人")
    private String createName;


    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
