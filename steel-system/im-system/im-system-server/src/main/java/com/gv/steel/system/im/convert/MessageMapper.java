package com.gv.steel.system.im.convert;

import com.farsunset.cim.model.Message;
import com.gv.steel.common.core.constant.CommonConstants;
import com.gv.steel.system.im.dto.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = CommonConstants.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {
    Message convert(MessageDTO msg);

    List<Message> convertList(List<MessageDTO> msgList);
}
