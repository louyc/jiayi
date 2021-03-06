package com.lifelight.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lifelight.api.entity.DepartmentDic;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.DepartmentDicService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/deptdic")
public class DepartmentDicController {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentDicController.class);

	@Reference
	private DepartmentDicService departmentDicService;
	@Reference 
	private WeixinConfigureService weixinConfigureService;
	/**
	 * 添加新部门
	 * 
	 * @param request
	 * @param response
	 * @param departmentDic
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Result insert(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "name", required = true) String name) {
		logger.info("DepartmentDicController.insert parament:", name);
		DepartmentDic departmentDic = new DepartmentDic();
		departmentDic.setId(id);
		departmentDic.setName(name);
		departmentDic.setCreateTime(new Date());
		Result result = departmentDicService.insert(departmentDic);
		return result;
	}

	/**
	 * 获取部门
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public Result getAllDept(HttpServletRequest request, HttpServletResponse response) {
		String in_use = request.getParameter("in_use");
		Result result = departmentDicService.getAllDept(in_use != null ? Integer.valueOf(in_use) : 1);
		return result;
	}

}
