package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ProcedureModule;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface ProcedureModuleService {

	/**
	 * 创建流程模块
	 * 
	 * @param procedureModule
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result createProcedureModule(ProcedureModule procedureModule);

	/**
	 * 删除流程模块
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result deleteProcedureModule(String id);

	/**
	 * 修改流程模块
	 * 
	 * @param procedureModule
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result updateProcedureModule(ProcedureModule procedureModule);
	
	/**
	 * 根据id查询流程模块
	 * 
	 * @param id
	 * @return
	 */
	Result<ProcedureModule> getProcedureModuleById(String id);

	/**
	 * 查询流程模块 分页
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<ProcedureModule> selectProcedureModuleListPage(ProcedureModule procedureModule);

}