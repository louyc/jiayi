package com.lifelight.web.controller;

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
import com.lifelight.api.entity.TemplateManagement;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.TemplateManagementService;

@Controller
@RequestMapping("/template")
public class TemplateManagementController {

	private static final Logger logger = LoggerFactory.getLogger(TemplateManagementController.class);

	@Reference
	private TemplateManagementService templateManagementService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/create")
	@ResponseBody
	public Result createTemplate(HttpServletRequest request,@RequestBody TemplateManagement template) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		template.setPlatformId(Integer.parseInt(weixinId));
		return templateManagementService.createTemplateManagement(template);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteTemplate(@RequestParam("id") String id) {
		return templateManagementService.deleteTemplateManagement(id);
	}

	@RequestMapping("/update")
	@ResponseBody
	public Result updateTemplate(@RequestBody TemplateManagement template) {
		return templateManagementService.updateTemplateManagement(template);
	}

	/**
	 * selectMessageListPage
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping("/getAll")
	@ResponseBody
	public Result getTemplatePageMessages(HttpServletRequest request,@RequestBody TemplateManagement template) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		template.setPlatformId(Integer.parseInt(weixinId));
		return templateManagementService.selectTemplateManagementListPage(template);
	}
}