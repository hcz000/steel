package com.gv.steel.system.base.service;

import com.gv.steel.common.mybatis.base.service.BaseService;
import com.gv.steel.system.base.dto.TaskNoticeStatusUpdateMessageDTO;
import com.gv.steel.system.base.entity.TaskNotice;

import java.util.List;

/**
 * <p>
 * 任务通知表(收发件信箱) 服务类
 * </p>
 *
 * @author administrator
 * @since 2023-11-13
 */
public interface TaskNoticeService extends BaseService<TaskNotice> {

    boolean saveNotice(List<TaskNotice> taskNoticeList);

    boolean updateTaskNoticeStatus(TaskNoticeStatusUpdateMessageDTO body);
}
