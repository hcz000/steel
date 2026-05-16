package com.gv.steel.system.base.annotation;

import com.gv.steel.system.base.producer.TaskNoticeMessageProducer;
import com.gv.steel.system.base.util.SenTaskNoticeService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({TaskNoticeMessageProducer.class, SenTaskNoticeService.class})
public @interface EnableTaskNotice {
}
