package com.lifelight.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lifelight.api.entity.Procedure;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.ProcedureService;

@Controller
@RequestMapping("/procedure")
public class ProcedureController {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureController.class);

	@Reference
	private ProcedureService procedureService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/create")
	@ResponseBody
	public Result createProcedure(@RequestBody Procedure procedure) {
		logger.info("创建流程：{}", procedure.toString());
		return procedureService.createProcedure(procedure);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteProcedure(@RequestParam("id") String id) {
		logger.info("删除id= {} 流程", id);
		return procedureService.deleteProcedure(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/update")
	@ResponseBody
	public Result updateProcedure(@RequestBody Procedure procedure) {
		logger.info("修改流程：{}", procedure.toString());
		return procedureService.updateProcedure(procedure);
	}

	@RequestMapping("/getOne")
	@ResponseBody
	public Result<Procedure> getProcedureById(@RequestParam("id") String id) {
		logger.info("查询id= {} 流程", id);
		return procedureService.getProcedureById(id);
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
	@RequestMapping("/getPageProcedures")
	@ResponseBody
	public Result getPageProcedures(@RequestBody Procedure procedure) {
		logger.info("查询流程列表 分页：{}", procedure.toString());
		return procedureService.selectProcedureListPage(procedure);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/share")
	@ResponseBody
	public Result shareProcedure(@RequestParam("id") String id, @RequestParam("users") String users) {
		logger.info("将id为 " + id + " 的流程分享给 " + users);
		return procedureService.shareProcedure(id, users);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getBesharedUsers")
	@ResponseBody
	public Result getBesharedUsers(@RequestParam("procedureId") String procedureId) {
		logger.info("查询 procedureId 为 " +procedureId + " 的流程分享到的用户");
		return procedureService.getBesharedUsers(procedureId);
	}

	@RequestMapping("/getNextStep")
	@ResponseBody
	public Result<String> getNextStep(HttpServletRequest request, @RequestParam("id") String id, @RequestParam("key") String key,
			@RequestParam("procedureId") String procedureId, @RequestParam("managerId") String managerId,
			@RequestParam(value = "data", required = false) String data) {
		logger.info("获取id为 " + id + " 的流程的下一步 " + key);
		String domainName = request.getServerName();
		return procedureService.getNextStep(id, key, procedureId, managerId, data, domainName);
	}
}