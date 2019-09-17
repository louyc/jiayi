package com.lifelight.web.controller.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lifelight.api.entity.ProcedureModule;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.ProcedureModuleService;

@Controller
@RequestMapping("/procedure/module")
public class ProcedureModuleController {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureModuleController.class);

	@Reference
	private ProcedureModuleService procedureModuleService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/create")
	@ResponseBody
	public Result createProcedureModule(@RequestBody ProcedureModule procedureModule) {
		logger.info("创建流程模块：{}", procedureModule.toString());
		return procedureModuleService.createProcedureModule(procedureModule);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteProcedureModule(@RequestParam("id") String id) {
		logger.info("删除id= {} 流程模块", id);
		return procedureModuleService.deleteProcedureModule(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/update")
	@ResponseBody
	public Result updateProcedure(@RequestBody ProcedureModule procedureModule) {
		logger.info("修改流程模块：{}", procedureModule.toString());
		return procedureModuleService.updateProcedureModule(procedureModule);
	}

	@RequestMapping("/getOne")
	@ResponseBody
	public Result<ProcedureModule> getProcedureModuleById(@RequestParam("id") String id) {
		logger.info("查询id= {} 流程模块", id);
		return procedureModuleService.getProcedureModuleById(id);
	}

	/**
	 * 分页 样例 pageSize 分页大小 currentsPage 当前页数
	 * 
	 * selectProcedureListPage
	 * 
	 * @param procedure
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getPageModules")
	@ResponseBody
	public Result getPageModules(@RequestBody ProcedureModule procedureModule) {
		logger.info("查询流程模块列表 分页：{}", procedureModule.toString());
		return procedureModuleService.selectProcedureModuleListPage(procedureModule);
	}

}