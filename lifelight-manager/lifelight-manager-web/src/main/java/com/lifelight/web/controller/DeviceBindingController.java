package com.lifelight.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.DeviceBindingUserService;
import com.lifelight.dubbo.service.DeviceInfoService;
import com.lifelight.dubbo.service.WeixinConfigureService;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Controller
@RequestMapping("/deviceBinding")
public class DeviceBindingController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceBindingController.class);
	private Map<String,WeixinProxy> wxmap= new HashMap<String,WeixinProxy>();
	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference 
	private WeixinConfigureService weixinConfigureService;
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	@Autowired
	private RedisTool redisTool;

	/*@Value("#{settings['weixin_Appid']}")
	private String weixin_Appid;
	@Value("#{settings['weixin_Secret']}")
	private String weixin_Secret;
	@Value("#{settings['weixin_Paysignkey']}")
	private String weixin_Paysignkey;
	@Value("#{settings['weixin_Mchid']}")
	private String weixin_Mchid;*/

	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/deviceBindingUser")
	@ResponseBody
	public Result deviceBindingUser(HttpServletRequest request,
			@RequestParam("deviceCode") String deviceCode,
			@RequestParam("managerId") String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == deviceCode || "".equals(deviceCode)) {
			result.setResultCode(new ResultCode("FALSE", "deviceCode为空"));
			return result;
		}

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取不到微信配置信息****************");
//			result.setResultCode(new ResultCode("FALSE", "获取不到微信配置信息"));
//			return result;
		}
		String deviceCodeCh = deviceCode.substring(deviceCode.lastIndexOf(".") + 1,
				deviceCode.length());

		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(deviceCodeCh,weixinId);

		if (null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "没有查到该设备"));
			return result;
		}
		return deviceBindingUserService.deviceBindingUser(managerId, deviceId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceBindingList")
	@ResponseBody
	public Result getDeviceBindingList(HttpServletRequest request,@RequestParam("managerId") String managerId) {
		return deviceBindingUserService.getDeviceBindingList(managerId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/relieveBinding")
	@ResponseBody
	public Result relieveBinding(@RequestParam("managerId") String managerId,
			@RequestParam("deviceId") String deviceId, @RequestParam("type") String type, 
			@RequestParam("netWorking") String netWorking,HttpServletRequest request) {
		//20160626  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String deviceCode = null;
		if("0".equals(netWorking)) {
			Result result = deviceInfoService.getDeviceInfoById(Integer.parseInt(deviceId));

			DeviceInfo deviceInfo = (DeviceInfo)result.getData();

			if(null != deviceInfo) {
				deviceCode = deviceInfo.getDeviceCode();
			}
		}
		
		WeixinProxy weixinProxy =getWeixinProxy(domainName);
		TokenHolder tokenHolder = weixinProxy.getTokenHolder();
		
		String token="";
		try {
			token = tokenHolder.getAccessToken();
		} catch (WeixinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return deviceBindingUserService.relieveBinding(managerId, deviceId, type, netWorking,token, deviceCode);
	}

	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/changeNetworking")
	@ResponseBody
	public Result changeNetworking(@RequestParam("managerId") String managerId,
			HttpServletRequest request,
			@RequestParam("deviceCode") String deviceCode,
			@RequestParam("deviceNetType") String deviceNetType) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == deviceCode || "".equals(deviceCode)) {
			result.setResultCode(new ResultCode("FALSE", "deviceCode为空"));
			return result;
		}

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}

		if (null == deviceNetType || "".equals(deviceNetType)) {
			result.setResultCode(new ResultCode("FALSE", "deviceNetType为空"));
			return result;
		}
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			result.setResultCode(new ResultCode("FALSE", "获取微信配置信息错误"));
			return result;
		}
		String deviceCodeCh = deviceCode.substring(deviceCode.lastIndexOf(".") + 1,
				deviceCode.length());
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(deviceCodeCh,weixinId);

		if (null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "没有查到该设备"));
			return result;
		}

		return deviceBindingUserService.changeNetworking(managerId, deviceId, deviceNetType);
	}

	public WeixinProxy getWeixinProxy(String domainName) {
		if (StringUtils.isNotEmpty(domainName)) {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,
					jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
					jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
			
			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);
			logger.info("domainname======================================================================================"+domainName);
			if(wxmap.get(domainName)!=null){
				return wxmap.get(domainName);
			}else{
				Result result = weixinConfigureService.queryWeicinConfigureByDomain(domainName,null);
				try {
					WeixinConfigureVO weixinConfig = null;
					if(null !=result.getData()) {
						weixinConfig = (WeixinConfigureVO)result.getData();
					}else {
						logger.info("获取微信配置信息错误****************");
						return null;
					}
					WeixinPayAccount wxpayAccount =null;
					if(null!=weixinConfig){
						// 建一个表来获取微信的全部信息。包含微信支付、imweixin
						wxpayAccount = new WeixinPayAccount(weixinConfig.getAppid().trim(), weixinConfig.getSecret().trim(),
								weixinConfig.getPaysignKey().trim(), weixinConfig.getMchid().trim(), null, null, null, null, null);
						Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);
						settings.setTokenStorager(rts);
						WeixinProxy proxy = new WeixinProxy(settings);
						wxmap.put(domainName, proxy);
					}else{
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
