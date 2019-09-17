package com.lifelight.dubbo.service;

import com.lifelight.api.entity.FbProcedureVarMap;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface FbProcedureVarMapService {

	/**
	 * 创建流程映射
	 *  
	 * @param message
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result createProcedureVar(FbProcedureVarMap procedureVar);
	
	/**
	 * 删除流程映射
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result deleteProcedureVar(String id);

	/**
	 * 修改流程映射
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result updateProcedureVar(FbProcedureVarMap procedureVar);

	/**
	 * 查询流程映射
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<FbProcedureVarMap> selectProcedureVarListPage(FbProcedureVarMap procedureVar);
	
}