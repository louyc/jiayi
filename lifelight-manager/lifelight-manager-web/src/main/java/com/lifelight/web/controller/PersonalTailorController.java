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
import com.lifelight.api.entity.PersonalTailor;
import com.lifelight.api.vo.PersonalTailorVO;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.PersonalTailorService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/personalTailor")
public class PersonalTailorController {

	@Reference
	private PersonalTailorService personalTailorService;
	private static final Logger logger = LoggerFactory.getLogger(PersonalTailorController.class);
	@Autowired
	private RedisTool redisTool;
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryById")
	@ResponseBody
	public Result queryById(@RequestParam(value = "id", required = false) String id) {
		return personalTailorService.getPersonalTailor(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryByEntity")
	@ResponseBody
	public Result queryByEntity(HttpServletRequest request,@RequestBody PersonalTailorVO personalTailorVO) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		personalTailorVO.setPlatformId(Integer.parseInt(weixinId));
		return personalTailorService.getPersonalTailor(personalTailorVO);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addPerson")
	@ResponseBody
	public Result addDetail(HttpServletRequest request,@RequestBody PersonalTailor personalTailor) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		personalTailor.setPlatformId(Integer.parseInt(weixinId));
		return personalTailorService.personalTailorCreate(personalTailor);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updatePerson")
	@ResponseBody
	public Result updateDetail(HttpServletRequest request,@RequestBody PersonalTailor personalTailor) {
		return personalTailorService.personalTailorUpdate(personalTailor);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/removePerson")
	@ResponseBody
	public Result removeDetail(@RequestParam("id") String id) {
		return personalTailorService.personalTailorRemove(id);
	}

}