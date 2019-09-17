package com.lifelight.dubbo.service;

import com.lifelight.api.entity.DeviceType;
import com.lifelight.common.result.Result;

public interface DeviceTypeService {

	/**
	 * 
	 * getAllDeviceType:获取设备类型. <br/> 
	 * @return List<DeviceType>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getAllDeviceType(DeviceType deviceType);
	
	/**
	 * 
	 * insertDeviceType:创建设备类型. <br/> 
	 * @param deviceType
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result insertDeviceType(DeviceType deviceType);
	
	/**
	 * 
	 * updateDeviceType:修改设备类型. <br/> 
	 * @param deviceType
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result updateDeviceType(DeviceType deviceType);
	
	/**
	 * 
	 * deleteDeviceType:删除设备类型. <br/>  
	 * @param deviceType
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result deleteDeviceType(Integer id);
	
	/**
	 * 
	 * recoverDeviceType:撤销设备类型. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result recoverDeviceType(Integer id);
	
	/**
	 * 
	 * getDeviceTypeById:根据ID获取设备类型信息. <br/>  
	 * @param id
	 * @return DeviceType
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getDeviceTypeById(Integer id);
}