package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.Dictionary;
import com.lifelight.api.entity.UserDoctorRel;
import com.lifelight.api.vo.ApiContractedUserVO;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.common.result.Result;

public interface ApiContractedUserService {

	/**
	 * queryDictionaryListPage:查询字典表分页
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result queryDictionaryListPage(String typeId,String itemName,String pageSize,String currentsPage);
	/**
	 * queryDictionary:查询字典表 不分页
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result queryDictionary(String typeId,String itemName);
	/**
	 * addDictionary:增加字典表某项
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result addDictionary(Dictionary dic);
	/**
	 * delDictionary:删除字典表某项
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result delDictionary(String typeId);
	/**
	 * updateDictionary:修改字典表某项
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateDictionary(Dictionary dic);

	/**
	 * 根据用户id查询签约用户档案信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result queryUserArchives(String managerId, String id,String doctorId);

	/**
	 * 查询签约用户档案信息  查询北京
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result querySignUserByEntity(ApiContractedUserVO apiUserInfo);
	/**
	 * 查询签约用户信息 根据身份证  包括西林所有数据
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result querySignUserByEntityCard(ApiContractedUserVO apiUserInfo,String type);

	/**
	 * addUserArchives：添加签约用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result addUserArchives(ApiContractedUserVO apiUserInfo);

	/**
	 * deleteUserArchives：删除签约用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result deleteUserArchives(ApiContractedUserVO apiUserInfo);

	/**
	 * updateUserArchives：修改签约用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateUserArchives(ApiContractedUserVO apiUserInfo);

	/* 用户分析 */
	Result userAnalysis(ApiUserInfoVO userInfo);

	Result getAllContractedUser(ApiContractedUserVO userInfo);
	/**
	 * 查询用户签约的家医信息
	 * @param managerId
	 * @return
	 */
	Result queryDoctors(String managerId,Integer platformId);
	
	/**
	 * 查询所有和用户相关的医生信息  家医  孕产 申请服务
	 * @param managerId
	 * @return
	 */
	Result queryAllDoctors(String managerId,Integer platformId);

	Result queryRel(UserDoctorRel udRel);

	List<UserDoctorRel> queryDocRel(String managerId, String doctorId);
}