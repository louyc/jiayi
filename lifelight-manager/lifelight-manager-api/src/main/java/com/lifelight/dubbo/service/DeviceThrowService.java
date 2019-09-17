package com.lifelight.dubbo.service;

import com.lifelight.common.result.Result;

public interface DeviceThrowService {

	@SuppressWarnings("rawtypes")
	Result insertDeviceThrow(String managerId, Integer deviceId, String deviceLocation);

}