package com.lifelight.dubbo.service;

import com.lifelight.api.entity.DeviceFirms;
import com.lifelight.common.result.Result;

public interface DeviceFirmsService {

	/**
	 * 
	 * getAllDeviceFirms:获取设备厂商 <br/> 
	 * @param deviceFirms
	 * @return List<DeviceFirms>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result selectDeviceFirmsListPage(DeviceFirms deviceFirms);
	
	/**
	 * 
	 * getAllDeviceFirms:获取设备厂商 无分页<br/> 
	 * @param deviceFirms
	 * @return List<DeviceFirms>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getDeviceFirms(Integer platformId);
	
	/**
	 * 
	 * insertDeviceFirm:创建设备厂商. <br/> 
	 * @param deviceFirms
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result insertDeviceFirm(DeviceFirms deviceFirms);
	
	/**
	 * 
	 * updateDeviceFirm:修改设备厂商. <br/> 
	 * @param deviceFirms
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result updateDeviceFirm(DeviceFirms deviceFirms);
	
	/**
	 * 
	 * deleteDeviceFirm:删除设备厂商. <br/>  
	 * @param deviceFirms
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result deleteDeviceFirm(Integer id,Integer platformId);
	
	/**
	 * 
	 * recoverDeviceFirm:撤销设备厂商. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result recoverDeviceFirm(Integer id);
	
	/**
	 * 
	 * getDeviceFirmById:根据ID获取设备厂商信息. <br/>  
	 * @param id
	 * @return DeviceFirms
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getDeviceFirmById(Integer id);
	
	/**
	 * 根据厂商名称获取厂商ID
	 * @param firmName
	 */
	DeviceFirms getDeviceFirmByName(String firmName,Integer platformId);
}