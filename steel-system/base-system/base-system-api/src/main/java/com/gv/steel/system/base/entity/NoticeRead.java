package com.gv.steel.system.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 通知公告阅读情况表
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_notice_read")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "NoticeRead对象", description = "通知公告阅读情况表")
public class NoticeRead extends Model<NoticeRead> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "通知公告ID")
    private Long noticeId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "阅读标志;（0-未读1-已读）")
    private Integer readFlag;

}
