package com.lifelight.web.controller.workflow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.FbProcedureVarMap;
import com.lifelight.api.entity.Message;
import com.lifelight.api.entity.OrderInfo;
import com.lifelight.api.vo.ServiceInfoVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.BackstageUserInfoService;
import com.lifelight.dubbo.service.FbProcedureVarMapService;
import com.lifelight.dubbo.service.MessageService;
import com.lifelight.dubbo.service.OrderInfoService;
import com.lifelight.dubbo.service.ServiceService;

@Controller
@RequestMapping("/procedureVar")
public class FbProcedureVarMapController {

	private static final Logger logger = LoggerFactory.getLogger(FbProcedureVarMapController.class);

	@Reference
	private FbProcedureVarMapService procedureVarService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/create")
	@ResponseBody
	public Result createProcedureVar(HttpServletRequest request,@RequestBody FbProcedureVarMap procedureVar) {
		//查询当前域名微信配置表id
		logger.info("createProcedureVar  ：：：："+procedureVar.getEnName());
		return procedureVarService.createProcedureVar(procedureVar);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteProcedureVar(@RequestParam("id") String id) {
		logger.info("deleteProcedureVar  id：：：："+id);
		return procedureVarService.deleteProcedureVar(id);
	}

	@RequestMapping("/update")
	@ResponseBody
	public Result updateProcedureVar(@RequestBody FbProcedureVarMap procedureVar) {
		logger.info("updateProcedureVar  id：：：："+procedureVar.getId());
		return procedureVarService.updateProcedureVar(procedureVar);
	}


	/**
	 * 分页 样例 pageSize 分页大小 currentsPage 当前页数
	 * 
	 * selectMessageListPage
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping("/getPageProcedureVar")
	@ResponseBody
	public Result getPageProcedureVar(HttpServletRequest request,@RequestBody FbProcedureVarMap procedureVar) {
		//查询当前域名微信配置表id
		logger.info("getPageProcedureVar "+procedureVar.getShowCount());
		return procedureVarService.selectProcedureVarListPage(procedureVar);
	}

}