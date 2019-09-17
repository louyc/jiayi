package com.lifelight.web.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.http.weixin.JsonResult;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.TmplApi;
import com.foxinmy.weixin4j.mp.message.TemplateMessage;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.lifelight.api.entity.ApiUserInfo;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.ApiUserInfoService;
import com.lifelight.dubbo.service.BackstageUserInfoService;
import com.lifelight.dubbo.service.WeixinConfigureService;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service("weixinService")
public class WeixinService {
	private static final Logger logger = LoggerFactory.getLogger(WeixinService.class);

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	@Reference
	private ApiUserInfoService apiUserInfoService;
	@Reference
	private BackstageUserInfoService backUserInfoService;
	@Reference
	private WeixinConfigureService weixinConfigureService;

	private Map<String, WeixinProxy> wxmap = new HashMap<String, WeixinProxy>();

	public void sendTuisong(String userId, String doctorId, String remark, String url, String domainName)
			throws Exception {
		logger.info("微信模板推送接口 开始");
		JSONObject jsondataObject = new JSONObject(); // 返回结果对象
		boolean goflage = true;

		logger.info("参数值：userId=" + userId + ",doctorId=" + doctorId + ",remark=" + remark + ",url=" + url
				+ ",domainName=" + domainName);
		// 20160626 查询微信公众号信息
		Result configureResult = weixinConfigureService.queryWeicinConfigureByDomain(domainName, null);
		WeixinProxy wxProxy = null;
		WeixinConfigureVO weixin = null;
		if (null != configureResult.getData()) {
			weixin = (WeixinConfigureVO) configureResult.getData();
			wxProxy = getWeixinProxy(domainName);
		} else {
			logger.info("获取微信配置信息错误****************");
			jsondataObject.put("ret_code", 40001);
			jsondataObject.put("ret_msg", "微信消息发送失败！");
			return;
		}
		// 根据用户id，查询用户信息
		ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(userId);
		if (user == null || StringUtil.isEmpty(user.getOpenId())) {
			logger.error("用户查询失败，或用户openID不存在。 用户id：" + userId);
			jsondataObject.put("ret_code", 600);
			jsondataObject.put("ret_msg", "用户查询失败，用户id：" + userId);
			goflage = false;
		}
		// 获取医生数据
		Result backResult = backUserInfoService.getOneUser(doctorId);
		if (goflage && (backResult == null || backResult.getData() == null)) {
			logger.error("医生查询失败，医生id：" + userId);
			jsondataObject.put("ret_code", 600);
			jsondataObject.put("ret_msg", "用户查询失败，用户id：" + userId);
			goflage = false;
		}

		if (goflage) {
			BackstageUserInfo doctor = (BackstageUserInfo) backResult.getData();

			// 推送openid
			String touser = user.getOpenId();

			// 时间
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = formatter.format(new Date());

			// 创建链接微信
			// WeixinProxy wxProxy = getWeixinProxy();

			TmplApi tmplApi = new TmplApi(wxProxy.getTokenHolder());

			logger.info("创建微信模板：touser=" + touser + " template_id=" + "" + " url=" + url);
			// 微信定义模板
			TemplateMessage tplMessage = new TemplateMessage(touser, weixin.getListTemp().get(0).getTemplateId(), url);
			tplMessage.pushItem("first", "问卷填写成功！");
			tplMessage.pushItem("keyword1", user.getName()); // 患者名称
			tplMessage.pushItem("keyword2", doctor.getName()); // 医生名称
			tplMessage.pushItem("keyword3", dateTime); // 时间
			tplMessage.pushItem("remark", remark);

			logger.info("发送微信消息 ：{}", tplMessage);

			try {
				JsonResult result = tmplApi.sendTmplMessage(tplMessage);
				logger.info("微信返回  result=" + result.getCode());

				if (result.getCode() == 0) {
					// 发送成功
					jsondataObject.put("ret_code", 200);
					jsondataObject.put("ret_msg", "消息发送成功！");
				} else {
					jsondataObject.put("ret_code", 600);
					jsondataObject.put("ret_msg", "微信消息发送失败！");
				}
			} catch (Exception e) {
				logger.error("消息发送失败， {}", e);
				jsondataObject.put("ret_code", 40001);
				jsondataObject.put("ret_msg", "微信消息发送失败！");
			}
		}

		logger.info(jsondataObject.toString());
		logger.info("微信模板推送接口 结束");
	}

	public WeixinProxy getWeixinProxy(String domainName) {
		if (StringUtils.isNotEmpty(domainName)) {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig, jedisConnectionFactory.getHostName(),
					jedisConnectionFactory.getPort(), jedisConnectionFactory.getTimeout(),
					jedisConnectionFactory.getPassword());

			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);
			logger.info(
					"domainname======================================================================================"
							+ domainName);
			if (wxmap.get(domainName) != null) {
				return wxmap.get(domainName);
			} else {
				Result result = weixinConfigureService.queryWeicinConfigureByDomain(domainName, null);
				try {
					WeixinConfigureVO weixinConfig = null;
					if (null != result.getData()) {
						weixinConfig = (WeixinConfigureVO) result.getData();
					} else {
						logger.info("获取微信配置信息错误****************");
						return null;
					}
					WeixinPayAccount wxpayAccount = null;
					if (null != weixinConfig) {
						// 建一个表来获取微信的全部信息。包含微信支付、imweixin
						wxpayAccount = new WeixinPayAccount(weixinConfig.getAppid().trim(),
								weixinConfig.getSecret().trim(), weixinConfig.getPaysignKey().trim(),
								weixinConfig.getMchid().trim(), null, null, null, null, null);
						Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);
						settings.setTokenStorager(rts);
						WeixinProxy proxy = new WeixinProxy(settings);
						wxmap.put(domainName, proxy);
					} else {
						return null;
					}
				} catch (Exception e) {
					return null;
				}
				return wxmap.get(domainName);
			}
		} else {
			return null;
		}
	}
}
