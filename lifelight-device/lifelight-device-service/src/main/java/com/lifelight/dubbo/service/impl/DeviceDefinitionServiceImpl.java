package com.lifelight.dubbo.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.DeviceDefinition;
import com.lifelight.api.entity.DeviceDefinitionProject;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.api.vo.DeviceDefinitionVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DeviceDefinitionMapper;
import com.lifelight.dubbo.dao.DeviceDefinitionProjectMapper;
import com.lifelight.dubbo.dao.DeviceInfoMapper;
import com.lifelight.dubbo.service.DeviceDefinitionService;

@Service
public class DeviceDefinitionServiceImpl implements DeviceDefinitionService {

	//设备定义
	@Autowired
	private DeviceDefinitionMapper deviceDefinitionMapper;
	
	//设备定义，检测项目
	@Autowired
	private DeviceDefinitionProjectMapper deviceDefinitionProjectMapper;
	
	//设备
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;

	@Override
	public PaginatedResult<DeviceDefinitionVO> getDeviceDefinitionList(DeviceDefinition entity) {
		// 创建返回对象
		PaginatedResult<DeviceDefinitionVO> result = null;
		List<DeviceDefinitionVO> selectList = new ArrayList<>();
		// 分页查询数据
		if (0 < entity.getCurrentPage() && 0 < entity.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) deviceDefinitionMapper.countByEntity(entity);
			//计算分页
			int beginSize = (entity.getCurrentPage() - 1) * entity.getShowCount();
			entity.setCurrentResult(beginSize);
			//查询数据
			selectList = deviceDefinitionMapper.selectVoByEntity(entity);
			result = new PaginatedResult<>(selectList, entity.getCurrentPage(),
					entity.getShowCount(), totalCount);
		} else {
			selectList = deviceDefinitionMapper.selectVoByEntity(entity);
			result = new PaginatedResult<>(selectList, -1, -1, selectList.size());
		}

		return result;
	}

	@Override
	public Result<DeviceDefinition> addDeviceDefinition(DeviceDefinition entity) {
		Result<DeviceDefinition> result = new Result<>(StatusCodes.OK, true);
		//添加定义
		entity.setDefinitionCode(UUID.randomUUID().toString());
		entity.setCreateTime(new Date());
		deviceDefinitionMapper.insertSelective(entity);
		Integer id = entity.getId();  //修改id
		if(id != null){
			//判断检测项目是否存在
			if(entity.getProjectList() != null && entity.getProjectList().size() > 0){
				//循环添加关联ID
				for(DeviceDefinitionProject item : entity.getProjectList()){
					item.setDefinitionId(id);
					item.setCreateTime(new Date());
				}
				//批量保存检测项目
				deviceDefinitionProjectMapper.insertList(entity.getProjectList());
			}
			//保存完成返回结果
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
			result.setData(entity);
		}else{
			//保存失败
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "FAILED"));
		}
		
		return result;
	}

	@Override
	public Result<DeviceDefinition> updateDeviceDefinition(DeviceDefinition entity) {
		Result<DeviceDefinition> result = new Result<>(StatusCodes.OK, true);
		
		//查询数据
		DeviceDefinition edit = deviceDefinitionMapper.selectByPrimaryKey(entity.getId());
		if(edit == null){
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "修改数据不存在"));
			return result;
		}
		
		//验证状态  上架的设备不能修改
		/*if(edit.getType() == null || edit.getType() == 1 ){
			//修改失败
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "已上架设备定义不能修改"));
			return result;
		}*/
		
		//修改定义
		entity.setUpdateTime(new Date());
		deviceDefinitionMapper.updateByPrimaryKeySelective(entity);
		Integer id = entity.getId();  //修改id
		if(id != null){
			//判断检测项目是否存在
			if(entity.getProjectList() != null){
				//删除关联检测项目
				deviceDefinitionProjectMapper.deleteByDefinitionId(id);
				if(entity.getProjectList().size() > 0){
					//循环添加关联ID
					for(DeviceDefinitionProject item : entity.getProjectList()){
						item.setDefinitionId(id);
						item.setCreateTime(new Date());
					}
					//批量保存检测项目
					deviceDefinitionProjectMapper.insertList(entity.getProjectList());
				}
			}
			//保存完成返回结果
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
			result.setData(entity);
		}else{
			//修改失败
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "FAILED"));
		}
		
		return result;
	}

	@Override
	public Result deleteDeviceDefinition(Integer id,Integer platformId) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		//验证数据
		DeviceDefinition edit = deviceDefinitionMapper.selectByPrimaryKey(id);
		if(edit == null){
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "数据不存在"));
			return result;
		}
		
		//验证状态  上架的设备不能修改
		if(edit.getType() != null && edit.getType() == 1 ){
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "已上架设备定义不能删除"));
			return result;
		}
		
		//验证是否关联设备
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDefinitionId(id);
		deviceInfo.setPlatformId(platformId);
		List<DeviceInfo> deviceList = deviceInfoMapper.selectByDefinitionId(deviceInfo);
		if(deviceList != null && deviceList.size() > 0){
			result = new Result<>(StatusCodes.INTERNAL_SERVER_ERROR, false);
			result.setResultCode(new ResultCode("FAILED", "该设备定义已经关联设备，不能删除"));
			return result;
		}
		//修改状态为删除
		edit.setUpdateTime(new Date());
		edit.setIsdel("T");
		deviceDefinitionMapper.updateByPrimaryKeySelective(edit);
		
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		return result;
	}

	@Override
	public Result<DeviceDefinitionVO> getDeviceDefinitionById(Integer id) {
		Result<DeviceDefinitionVO> result = new Result<>(StatusCodes.OK, true);
		DeviceDefinitionVO vo = deviceDefinitionMapper.selectVoById(id);
		if(vo != null){
			List<DeviceDefinitionProject> projectList = deviceDefinitionProjectMapper.selectByDefinitionId(id);
			if(projectList != null && projectList.size() > 0){
				vo.setProjectList(projectList);
			}
		}
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		result.setData(vo);
		return result;
	}

}
