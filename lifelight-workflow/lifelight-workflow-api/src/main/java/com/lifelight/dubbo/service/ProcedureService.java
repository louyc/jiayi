package com.lifelight.dubbo.service;

import com.lifelight.api.entity.Procedure;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface ProcedureService {

	/**
	 * 创建流程
	 * 
	 * @param procedure
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result createProcedure(Procedure procedure);

	/**
	 * 删除流程
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result deleteProcedure(String id);

	/**
	 * 修改流程
	 * 
	 * @param procedure
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result updateProcedure(Procedure procedure);
	
	/**
	 * 根据id查询流程
	 * 
	 * @param id
	 * @return
	 */
	Result<Procedure> getProcedureById(String id);

	/**
	 * 查询流程 分页
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<Procedure> selectProcedureListPage(Procedure procedure);

	/**
	 * 分享流程
	 * 
	 * @param id
	 * @param users
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result shareProcedure(String id, String users);
	
	/**
	 * 分享流程
	 * 
	 * @param procedureId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result getBesharedUsers(String procedureId);
	
	/**
	 * 获取流程对话的下一步
	 * 
	 * @param id
	 * @param key
	 * @param procedureId
	 * @param data
	 * @param domainName
	 * @return
	 */
	Result<String> getNextStep(String id, String key, String procedureId, String managerId, String data, String domainName);

}