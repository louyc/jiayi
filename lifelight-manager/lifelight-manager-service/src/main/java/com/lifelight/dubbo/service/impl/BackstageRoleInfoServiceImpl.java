package com.lifelight.dubbo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.BackstageRoleInfo;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.BackstageRoleInfoMapper;
import com.lifelight.dubbo.service.BackstageRoleInfoService;

@Service
public class BackstageRoleInfoServiceImpl implements BackstageRoleInfoService {
	
	
	@Autowired
	private BackstageRoleInfoMapper backstageRoleInfoMapper;

	/**
	 * 查询用户角色列表
	 * 
	 * @author xyx
	 */
	@Override
	public Result getRoleInfoArray(Integer platformId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<BackstageRoleInfo> brList = backstageRoleInfoMapper.selectListByConditions(platformId);

		result.setData(brList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	/**
	 * 添加新用户角色
	 * @author xyx
	 */
	@Override
	public Result addRoleInfo(BackstageRoleInfo roleInfo) {
		// 验证用户名是否为空
		String itemName = roleInfo.getItemName();
		if (StringUtils.isEmpty(itemName)) {
			Result result = new Result<>(StatusCodes.OK, false);
			result.setResultCode(new ResultCode("NAME_NULL", "角色名称不能为空！"));
			return result;
		}
		// 验证角色名称是否已经存在
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("itemName", itemName);
//		queryMap.put("weixinId", roleInfo.getWeixinId());
		Long countNum = backstageRoleInfoMapper.countRoleByItemName(queryMap);
		if (countNum > 0) {
			Result result = new Result<>(StatusCodes.OK, false);
			result.setResultCode(new ResultCode("NAME_EXISTS", "角色名称已存在！"));
			return result;
		}
		// 添加
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		roleInfo.setCreateTime(now);
		roleInfo.setUpdateTime(now);
		roleInfo.setInUse(1);
		System.out.println(roleInfo);
		int row = backstageRoleInfoMapper.insertSelective(roleInfo);
		Result result = new Result<>(StatusCodes.OK, true);
		if (row > 0)
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		else
			result.setResultCode(new ResultCode("FAIL", "添加失败！"));
		return result;
	}

	/**
	 * 修改用户角色
	 * @author xyx
	 */
	@Override
	public Result updateRoleInfo(BackstageRoleInfo roleInfo) {
		// 验证用户名是否为空
		String itemName = roleInfo.getItemName();
		if (StringUtils.isEmpty(itemName)) {
			Result result = new Result<>(StatusCodes.OK, false);
			result.setResultCode(new ResultCode("NAME_NULL", "角色名称不能为空！"));
			return result;
		}
		// 验证角色名称是否已经存在
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("itemName", itemName);
//		queryMap.put("weixinId", backstageRoleInfoMapper.selectByPrimaryKey(roleInfo.getId()).getWeixinId());
		// 验证角色名称是否已经存在
		Long countNum = backstageRoleInfoMapper.countRoleByItemName(queryMap);
		if (countNum > 0) {
			Result result = new Result<>(StatusCodes.OK, false);
			result.setResultCode(new ResultCode("NAME_EXISTS", "角色名称已存在！"));
			return result;
		}
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		roleInfo.setUpdateTime(now);
		int row = backstageRoleInfoMapper.updateByPrimaryKey(roleInfo);
		Result result = null;
		if(row>0){
			result = new Result<>(StatusCodes.OK, true);
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		}else{
			result = new Result<>(StatusCodes.OK, false);
			result.setResultCode(new ResultCode("FAIL", "修改失败！"));
		}
		return result;
	}
	
	

	@Override
	public Result deleteRoleInfo(BackstageRoleInfo roleInfo) {
		// TODO Auto-generated method stub
		// 1. 验证该角色有没有在使用
		return null;
	}
	
	/**
	 * 根据id获取用户角色
	 * @author xyx
	 * @param id
	 * @return
	 */
	@Override
	public Result getRoleInfoById(Integer id) {
		BackstageRoleInfo backstageRoleInfo =  backstageRoleInfoMapper.selectByPrimaryKey(id);
		Result result = new Result<>(StatusCodes.OK, true);
		result.setResultCode(new ResultCode("SUCCESS", "获取成功！"));
		result.setData(backstageRoleInfo);
		return result;
	}
	
	/**
	 * 用户管理获取角色列表
	 */
	@Override
	public Result selectListByUserManage(Integer platformId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<BackstageRoleInfo> brList = backstageRoleInfoMapper.selectListByUserManage(platformId);

		result.setData(brList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}
}
