package com.lifelight.dubbo.service;

import com.lifelight.api.entity.DeviceBrands;
import com.lifelight.common.result.Result;

public interface DeviceBrandsService {
	
	/**
	 * 
	 * getAllDeviceBrands:获取所有设备品牌. <br/> 
	 * @param deviceBrands
	 * @return List<DeviceBrands>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result selectDeviceBrandsListPage(DeviceBrands deviceBrands);
	
	/**
	 * 
	 * getAllDeviceFirms:获取设备品牌 无分页<br/> 
	 * @param deviceBrands
	 * @return List<deviceBrands>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getDeviceBrands(Integer platformId);
	
	/**
	 * 
	 * insertDeviceBrand:创建设备品牌. <br/> 
	 * @param deviceBrands
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result insertDeviceBrand(DeviceBrands deviceBrands);
	
	/**
	 * 
	 * updateDeviceBrand:修改设备品牌. <br/> 
	 * @param deviceBrands
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result updateDeviceBrand(DeviceBrands deviceBrands);
	
	/**
	 * 
	 * deleteDeviceBrand:删除设备品牌. <br/>  
	 * @param deviceBrands
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result deleteDeviceBrand(Integer id,Integer platformId);
	
	/**
	 * 
	 * recoverDeviceBrand:撤销设备品牌. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result recoverDeviceBrand(Integer id);
	
	/**
	 * 
	 * getDeviceBrandById:根据ID获取设备品牌信息. <br/>  
	 * @param id
	 * @return DeviceBrands
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	Result getDeviceBrandById(Integer id);
	
	/**
	 * 根据厂商名称获取品牌ID
	 * @param brandName
	 */
	DeviceBrands getDeviceBrandByName(String brandName,Integer platformId);
}