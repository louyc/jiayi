package com.lifelight.dubbo.service;
import com.lifelight.api.entity.DeviceDefinition;
import com.lifelight.api.vo.DeviceDefinitionVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface DeviceDefinitionService {

	/**
	 * 分页查询数据列表
	 * @param entity
	 * @return
	 */
	PaginatedResult<DeviceDefinitionVO> getDeviceDefinitionList(DeviceDefinition entity);
	
	/**
	 * 添加数据
	 * @param dto
	 * @return
	 */
	Result<DeviceDefinition> addDeviceDefinition(DeviceDefinition dto);
	
	/**
	 * 修改数据
	 * @param dto
	 * @return
	 */
	Result<DeviceDefinition> updateDeviceDefinition(DeviceDefinition dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Result deleteDeviceDefinition(Integer id,Integer platformId);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Result<DeviceDefinitionVO> getDeviceDefinitionById(Integer id);
}
