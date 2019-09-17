package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ApiRoleInfo;
import com.lifelight.api.entity.ApiUserInfo;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.common.result.Result;

public interface ApiUserInfoService {

	/**
	 * getApiUserInfoById:用户登录
	 * 
	 * @param managerId
	 * @return getApiUserInfoById
	 */
	@SuppressWarnings("rawtypes")
	Result getApiUserInfoById(String managerId, String mobile, String openId, String headimgurl,
			String url, String city,Integer platformId);

	/**
	 * updateApiUserInfo：完善用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateApiUserInfo(ApiUserInfoVO apiUserInfo);

	/**
	 * updateWxaUserInfo：小程序  完善用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateWxaUserInfo(ApiUserInfoVO apiUserInfo);
	/**
	 * 删除用户
	 * 
	 * @description 把用户状态改为不可用
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result deleteApiUserInfo(String managerId);

	/**
	 * 查询所有子用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result getAllApiUserInfo(String managerId);

	/**
	 * 查询所有父用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result getParentUserInfo(String managerId);

	/**
	 * 查询当前用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	ApiUserInfo getCurrentUserInfo(String managerId);

	/**
	 * 切换当前用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	ApiUserInfo changeAccount(String managerId);

	/**
	 * 添加子账户
	 * 
	 * @param apiUserInfo
	 * @return
	 */
	Result addSubAccount(ApiUserInfoVO apiUserInfo, String managerId,Integer platformId);

	/**
	 * 创建用户、子用户关系
	 * 
	 * @param roleInfo
	 * @return
	 */
	Result createApiUserRoleInfo(ApiUserInfo roleInfo);

	/**
	 * 获取用户、子用户关系
	 * 
	 * @return
	 */
	Result queryAllApiRoleInfo();

	/**
	 * 修改用户、子用户关系
	 * 
	 * @param roleInfo
	 * @return
	 */
	Result updateAllApiRoleInfo(ApiRoleInfo roleInfo);

	/**
	 * 删除用户、子用户关系
	 * 
	 * @param id
	 * @return
	 */
	Result removeApiRoleInfo(String managerId);

	/**
	 * 根据用户手机号 获取managerId by j
	 */
	String getManagerIdByMobile(String mobile,Integer deviceId,String weixinId);

	ApiUserInfo getApiUserByOpenId(String openId);
}