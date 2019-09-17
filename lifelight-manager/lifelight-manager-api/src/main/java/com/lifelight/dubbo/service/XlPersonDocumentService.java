package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ProcedureData;
import com.lifelight.api.entity.XlPersonDocument;
import com.lifelight.api.vo.ApiContractedUserVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface XlPersonDocumentService {

	/**
	 * saveProcedureDate:保存流程数据
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result saveProcedureDate(String userId,String data);
	/**
	 * getProcedureDate:查询流程数据
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result getProcedureDate(ProcedureData procedure);
	
	/**
	 * 创建个人档案信息
	 * 
	 * @param message
	 * @return
	 */
	Result createPersonDocument(XlPersonDocument contract);
	
	/**
	 * 删除个人档案信息
	 * 
	 * @param id
	 * @return
	 */
	Result deletePersonDocument(String id);

	/**
	 * 修改个人档案信息
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updatePersonDocument(XlPersonDocument contract);
	
	/**
	 * 查询单人档案
	 * 
	 * @param message
	 * @return
	 */
	Result queryOnePersonDocument(String id,Integer platformId,String managerId);
	/**
	 * 查询多人档案信息 不分页
	 * 
	 * @param message
	 * @return
	 */
	Result selectPersonDocument(XlPersonDocument contract);
	/**
	 * 查询个人信息 根据身份证
	 * 
	 * @param message
	 * @return
	 */
	Result querySignUserByEntityCard(XlPersonDocumentVO contract);

	/**
	 * 查询多人档案信息 分页
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<XlPersonDocument> selectPersonDocumentListPage(XlPersonDocumentVO contract);

	/**
	 * 用户管理 ：机构查询用户 医生查询用户
	 * 
	 * @param userInfo
	 * @return
	 */
	Result getAllContractedUser(ApiContractedUserVO userInfo);
	
}