package com.lifelight.dubbo.service;

import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.lifelight.api.entity.DeviceUserRel;
import com.lifelight.common.result.Result;

public interface DeviceBindingUserService {

	@SuppressWarnings("rawtypes")
	Result deviceBindingUser(String managerId, Integer deviceId);

	@SuppressWarnings("rawtypes")
	Result getDeviceBindingList(String managerId);

	@SuppressWarnings("rawtypes")
	Result relieveBinding(String managerId, String deviceId, String type, String netWorking, String token, String deviceCode);

	@SuppressWarnings("rawtypes")
	Result changeNetworking(String managerId, Integer deviceId, String deviceNetType);

	@SuppressWarnings("rawtypes")
	Result deleteBingingUser(Integer id);

	DeviceUserRel getDeviceIsBindingByDeviceId(Integer deviceId);

	DeviceUserRel checkDeviceIsBinding(Integer deviceId);

	DeviceUserRel checkDeviceIsBinding(String managerId, Integer deviceId,String weixinId);
}
