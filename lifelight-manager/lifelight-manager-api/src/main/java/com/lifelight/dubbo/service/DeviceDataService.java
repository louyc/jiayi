package com.lifelight.dubbo.service;

import com.lifelight.api.vo.DeviceDataVO;
import com.lifelight.common.result.Result;

public interface DeviceDataService {

	@SuppressWarnings("rawtypes")
	Result getMeasureData(String managerId, String time);
	
	@SuppressWarnings("rawtypes")
	Result getNewMeasureData(String mobile, String time);

	@SuppressWarnings("rawtypes")
	Result getHistoryMeasureData(String managerId, String time);

	@SuppressWarnings("rawtypes")
	Result getMeasureDataByType(String managerId, String time, String type);

	@SuppressWarnings("rawtypes")
	Result getMeasureDataByMobile(String mobile, String time, String type);

	@SuppressWarnings("rawtypes")
	Result insertMeasureData(String managerId, String type, String data);

	@SuppressWarnings("rawtypes")
	Result getMeasureDataByMidType(String managerId, String time, String type);
	@SuppressWarnings("rawtypes")
	Result getMeasureDataByWorkFollow(String managerId, String enName);

	@SuppressWarnings("rawtypes")
	Result getAllDeviceBindingUser(DeviceDataVO deviceData);

	@SuppressWarnings("rawtypes")
	Result queryUserInfo(DeviceDataVO deviceData);

	@SuppressWarnings("rawtypes")
	Result queryDeviceItems();
	
	@SuppressWarnings("rawtypes")
	Result queryCheckDeviceItems();
}