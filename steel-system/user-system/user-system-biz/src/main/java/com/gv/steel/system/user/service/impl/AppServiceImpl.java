package com.gv.steel.system.user.service.impl;

import com.gv.steel.common.core.api.Result;
import com.gv.steel.system.user.dao.SysUserDao;
import com.gv.steel.system.user.dto.AppSmsDTO;
import com.gv.steel.system.user.service.AppService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 手机登录相关业务实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {

    private final RedisTemplate redisTemplate;

    private final SysUserDao userMapper;

//	private final SmsClient smsClient;

    /**
     * 发送手机验证码 TODO: 调用短信网关发送验证码,测试返回前端
     *
     * @param sms 手机号
     * @return code
     */
    @Override
    public Result<Boolean> sendSmsCode(AppSmsDTO sms) {
//		Object codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + sms.getPhone());
//
//		if (codeObj != null) {
//			log.info("手机号验证码未过期:{}，{}", sms.getPhone(), codeObj);
//			return Result.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodeConstants.SYS_APP_SMS_OFTEN));
//		}
//
//		// 校验手机号是否存在 sys_user 表
//		if (sms.getExist()
//				&& !userMapper.exists(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getPhone, sms.getPhone()))) {
//			return Result.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodeConstants.SYS_APP_PHONE_UNREGISTERED, sms.getPhone()));
//		}
//
//		String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
//		log.info("手机号生成验证码成功:{},{}", sms.getPhone(), code);
//		redisTemplate.opsForValue()
//			.set(CacheConstants.DEFAULT_CODE_KEY + sms.getPhone(), code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
//
//		// 调用短信通道发送
//		this.smsClient.sendCode(code, sms.getPhone());
//		return Result.ok(Boolean.TRUE, code);
        // TODO 发送短信
        return Result.failed();
    }

    /**
     * 校验验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    @Override
    public boolean check(String phone, String code) {
//		Object codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + phone);
//
//		if (Objects.isNull(codeObj)) {
//			return false;
//		}
//		return codeObj.equals(code);
        // TODO 校验手机号
        return false;
    }

}
