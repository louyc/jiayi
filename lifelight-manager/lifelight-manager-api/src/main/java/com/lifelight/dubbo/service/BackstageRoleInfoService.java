package com.lifelight.dubbo.service;
/**
 * 角色管理接口
 * @author quanlj
 *
 */

import com.lifelight.api.entity.BackstageRoleInfo;
import com.lifelight.common.result.Result;

public interface BackstageRoleInfoService {

	/**
	 * 查询角色列表
	 * 
	 * @return 
	 * @author quanlj
	 */
	Result getRoleInfoArray(Integer platformId);
	
	/**
	 * 添加角色
	 * 
	 * @param roleInfo
	 * @return 
	 * @author quanlj
	 */
	Result addRoleInfo(BackstageRoleInfo roleInfo);
	
	/**
	 * 修改角色
	 * 
	 * @param roleInfo
	 * @return 
	 * @author quanlj
	 */
	Result updateRoleInfo(BackstageRoleInfo roleInfo);
	
	/**
	 * 删除角色
	 * 
	 * @param roleInfo
	 * @return 
	 * @author quanlj
	 */
	Result deleteRoleInfo(BackstageRoleInfo roleInfo);
	
	
	/**
	 * 根据id查询角色详情
	 * @param id
	 * @return
	 */
	Result getRoleInfoById(Integer id);
	
	/**
	 * 用户管理获取角色列表
	 * @return
	 */
	Result selectListByUserManage(Integer platformId);
	
}
