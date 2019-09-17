package com.lifelight.web.util;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lifelight.common.tools.util.ConnUtil;

public class SMSUtil {

	private static final Logger logger = LoggerFactory.getLogger(SMSUtil.class);

	// 短信平台用户名
	public static final String ACCOUNT = "N6661101";
	// 短信平台密码
	public static final String PASSWORD = "QwJeyICW2b731b";
	// 短信平台 apikey
	public static final String APIKEY = "a65d71e3d95792e0410c160cee56bc7f";
	// 短信平台接口地址
	public static final String URL = "https://smssh1.253.com/msg/send/json";

	/**
	 * 发送短信验证码接口（注册验证码）
	 */
	public static boolean sendCode(String phone, String msg) {

		logger.info("mobile:{},code:{}", phone, msg);

		HashMap<String, Object> param = new HashMap<>();
		param.put("encode", "UTF-8");
		param.put("account", ACCOUNT);
		param.put("password", PASSWORD);
		param.put("phone", phone);
		//【扁鹊大哥】验证码：" + code + "，打死都不要告诉别人哦！
		param.put("msg", msg);
		param.put("report", "true");

		try {
			String result = ConnUtil.postJson(URL, param);
			logger.info("发送短信返回结果：{}", result);
			if (StringUtils.isNotBlank(result) && 0 == JSONObject.parseObject(result).getInteger("code")) {
				logger.info("--------------------sendCode-------发送短信成功------------");
				return true;
			} else {
				logger.info("-------验证码发送失败-------错误信息={}", result);
			}
		} catch (Exception e) {
			logger.error("-------验证码发送失败-------错误信息={}", e);
		}

		return false;
	}

}
