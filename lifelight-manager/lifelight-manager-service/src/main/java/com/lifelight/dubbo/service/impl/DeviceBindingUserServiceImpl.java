package com.lifelight.dubbo.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.lifelight.api.entity.ApiUserInfo;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.DeviceUserRel;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.util.ConnUtil;
import com.lifelight.dubbo.dao.ApiUserInfoMapper;
import com.lifelight.dubbo.dao.BackstageUserInfoMapper;
import com.lifelight.dubbo.dao.DeviceUserRelMapper;
import com.lifelight.dubbo.service.DeviceBindingUserService;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class DeviceBindingUserServiceImpl implements DeviceBindingUserService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceBindingUserServiceImpl.class);
	
	@Autowired
	private DeviceUserRelMapper deviceUserRelMapper;
	@Autowired
	private BackstageUserInfoMapper backstageUserInfoMapper;
	@Autowired
	private ApiUserInfoMapper apiUserInfoMapper;
	
	/**
	 * 设备绑定 设备ID 用户ID
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceBindingUser(String managerId, Integer deviceId) {
		Result result = new Result<>(StatusCodes.OK, true);
		Date date = new Date();

		DeviceUserRel deviceUserRel = new DeviceUserRel();

		deviceUserRel.setDeviceId(deviceId);
//		deviceUserRel.setUpdateTime(date);
		// 设备之前绑定解绑
		deviceUserRelMapper.updateRelNotBinding(deviceUserRel);

		deviceUserRel.setManagerId(managerId);
		// 设备 + 当前用户 是否有绑定关系
		DeviceUserRel device = deviceUserRelMapper.getDeviceUserRelByMaDe(deviceUserRel);
		// 是否设备管理员
		BackstageUserInfo user = backstageUserInfoMapper.selectByPrimaryKey(managerId);
		if (null != user && user.getUserStatus() == 1 && user.getRoleId() == 5) { // 设备管理员
			deviceUserRel.setIsBinding("A");
		} else {
			deviceUserRel.setIsBinding("T");
		}
		deviceUserRel.setDeviceNetType("F");
		int num = 0;
		if (null == device) {
			deviceUserRel.setCreateTime(date);
			num = deviceUserRelMapper.insertSelective(deviceUserRel);
		} else {
			num = deviceUserRelMapper.relieveBinding(deviceUserRel);
		}
		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 根据 用户ID 获取改用户已绑定设备
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getDeviceBindingList(String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		List<DeviceUserRel> deviceUserRelList = deviceUserRelMapper.getDeviceBindingList(managerId);

		result.setData(deviceUserRelList);
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));

		return result;
	}

	/**
	 * 根据 设备id 获取用户信息
	 */
	@Override
	public DeviceUserRel getDeviceIsBindingByDeviceId(Integer deviceId) {

		DeviceUserRel deviceUser = deviceUserRelMapper.getDeviceIsBindingByDeviceId(deviceId);

		return deviceUser;
	}

	/**
	 * 取消绑定
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result relieveBinding(String managerId, String deviceId, String type, String netWorking, String token, String deviceCode) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		if (null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		if (null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		if (!"T".equals(type) && !"F".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		DeviceUserRel deviceUserRel = new DeviceUserRel();
		deviceUserRel.setManagerId(managerId);
		deviceUserRel.setDeviceId(Integer.parseInt(deviceId));
		deviceUserRel.setIsBinding(type);
		deviceUserRel.setUpdateTime(new Date());

		int num = deviceUserRelMapper.relieveBinding(deviceUserRel);

		if("0".equals(netWorking)) {
			logger.info("蓝牙设备解绑返回Start" );
			
			ApiUserInfo apiUserInfo = apiUserInfoMapper.selectByPrimaryKey(managerId);
			if(null != apiUserInfo) {
				/*TokenHolder tokenHolder = weixinProxy.getTokenHolder();
	
				String token = tokenHolder.getAccessToken();*/
				String url = "https://api.weixin.qq.com/device/compel_unbind?access_token=" + token;
				
				String sqBack = ConnUtil.sendPostWithOutHeaderAndJson(url, "{\"device_id\":\""+ deviceCode +"\",\"openid\":\""+ apiUserInfo.getOpenId() +"\"}");
				
				JSONObject jb = JSONObject.parseObject(sqBack.trim());
				
				logger.info("蓝牙设备解绑返回JSON：" + jb);
			}
		}
		
		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 根据 设备ID 用户ID 修改联网状态
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result changeNetworking(String managerId, Integer deviceId, String deviceNetType) {
		Result result = new Result<>(StatusCodes.OK, true);

		DeviceUserRel deviceUserRel = new DeviceUserRel();

		deviceUserRel.setDeviceId(deviceId);
		deviceUserRel.setManagerId(managerId);
		deviceUserRel.setNetworkingType(deviceNetType);
		deviceUserRel.setUpdateTime(new Date());

		int num = deviceUserRelMapper.changeNetworking(deviceUserRel);

		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 解除用户设备关系
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deleteBingingUser(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);

		DeviceUserRel deviceUserRel = new DeviceUserRel();

		deviceUserRel.setDeviceId(id);
		deviceUserRel.setIsBinding("F");
		deviceUserRel.setUpdateTime(new Date());

		int num = deviceUserRelMapper.updateByPrimaryKeySelective(deviceUserRel);

		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 验证此设备号是否绑定用户
	 */
	@Override
	public DeviceUserRel checkDeviceIsBinding(Integer deviceId) {
		
		DeviceUserRel deviceUserRel = deviceUserRelMapper.checkDeviceIsBinding(deviceId);
		if(null !=deviceUserRel && StringUtil.isEmpty(deviceUserRel.getUserName()) && StringUtil.isEmpty(deviceUserRel.getUserMobile())) {
			deviceUserRel = deviceUserRelMapper.checkDeviceIsBindingXl(deviceId);
		}
		return deviceUserRel;
	}

	/**
	 * 验证此设备号是否绑定用户
	 */
	@Override
	public DeviceUserRel checkDeviceIsBinding(String userMobile, Integer deviceId,String weixinId) {
		DeviceUserRel deviceUserRel = new DeviceUserRel();
//		if(!StringUtil.isEmpty(userMobile)) {
//			ApiUserInfo record = new ApiUserInfo();
//			record.setMobile(userMobile);
//			record.setPlatformId(Integer.parseInt(weixinId));
//			String managerId = apiUserInfoMapper.getManagerIdByMobile(record);
//			deviceUserRel.setManagerId(managerId);
//		}
		deviceUserRel.setDeviceId(deviceId);
		return deviceUserRelMapper.checkDeviceIsBindingByManagerId(deviceUserRel);
	}
	
}