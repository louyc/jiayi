package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.common.result.Result;

public interface ManagerService {

	/**
	 * getManagerInfoByPrimaryKey:根据id查询用户信息. <br/>
	 * 
	 * @param id
	 * @return ManagerInfo
	 * @author hua
	 */
	ManagerInfo getManagerInfoByPrimaryKey(String id);

	/**
	 * getManagerInfoByMobile:根据手机号查询用户信息. <br/>
	 * 
	 * @param mobile
	 * @return ManagerInfo
	 * @author hua
	 */
	ManagerInfo getManagerInfoByMobile(String mobile, int platformId);

	/**
	 * getManagerInfoByEmail:根据邮箱查询用户信息. <br/>
	 * 
	 * @param email
	 * @return ManagerInfo
	 * @author hua
	 */
	ManagerInfo getManagerInfoByEmail(String email, int platformId);

	/**
	 * getManagerInfoByIDcard:根据身份证查询用户信息. <br/>
	 * 
	 * @param email
	 * @return ManagerInfo
	 * @author hua
	 */
	ManagerInfo getManagerInfoByIDcard(String idCode, int platformId);

	/**
	 * login:用户登录. <br/>
	 * 
	 * @param userName
	 * @param password
	 * @return Result
	 * @author hua
	 */
	Result login(String userName, String password, int platformId);

	/**
	 * regist:用户注册. <br/>
	 * 
	 * @param info
	 * @return Result
	 * @author hua
	 */
	Result regist(ManagerInfo info);

	/**
	 * updatePassword:修改密码. <br/>
	 * 
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return Result
	 * @author hua
	 */
	Result updatePassword(String id, String oldPassword, String newPassword);

	/**
	 * forgetPassword:忘记密码. <br/>
	 * 
	 * @param userName
	 * @param password
	 * @return Result
	 * @author hua
	 */
	Result forgetPassword(String userName, String password, int platformId);

	/**
	 * update:修改用户资料. <br/>
	 * 
	 * @param info
	 * @return Result
	 * @author hua
	 */
	Result update(ManagerInfo info);

	/**
	 * getManagerInfoByToken:根据token获取用户信息. <br/>
	 * 
	 * @param token
	 * @return Result
	 * @author hua
	 */
	Result getManagerInfoByToken(String token);

	/**
	 * getManagerInfoById:根据id获取用户信息. <br/>
	 * 
	 * @param id
	 * @return Result
	 * @author hua
	 */
	Result getManagerInfoById(String id);
}