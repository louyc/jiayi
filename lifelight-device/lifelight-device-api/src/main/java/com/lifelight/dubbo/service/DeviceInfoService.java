package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.common.result.Result;

public interface DeviceInfoService {

	/**
	 * 
	 * getAllDeviceInfo:获取所有设备. <br/> 
	 * @return List<DeviceInfo>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result selectDeviceInfoListPage(DeviceInfo deviceInfo);
	
	/**
	 * 
	 * insertDeviceInfo:创建设备. <br/> 
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result insertDeviceInfo(DeviceInfo deviceInfo);
	
	/**
	 * 
	 * insertListDeviceInfo:批量创建设备.   <br/> 
	 * @param deviceInfoList
	 * @return result
	 * @exception 
	 * @author 
	 */
	@SuppressWarnings("rawtypes")
	Result insertListDeviceInfo(List<DeviceInfo> deviceInfoList);
	
	/**
	 * 
	 * updateDeviceInfo:修改设备. <br/> 
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result updateDeviceInfo(DeviceInfo deviceInfo);
	
	/**
	 * 
	 * deleteDeviceInfo:删除设备. <br/>  
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result deleteDeviceInfo(Integer id);
	
	/**
	 * 
	 * recoverDeviceInfo:撤销设备. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result recoverDeviceInfo(Integer id);
	
	/**
	 * 
	 * getDeviceInfoById:根据ID获取设备信息. <br/>  
	 * @param id
	 * @return DeviceInfo
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getDeviceInfoById(Integer id);
	
	/**
	 * 
	 * 根据设备号 获取 设备ID
	 * @param deviceCode
	 * @return
	 */
	Integer getDeviceIdByDeviceCode(String deviceCode,String weixinId);
}