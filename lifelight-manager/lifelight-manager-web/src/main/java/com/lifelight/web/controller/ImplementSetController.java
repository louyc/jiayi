package com.lifelight.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.ImplementSet;
import com.lifelight.api.vo.ImplementSetVO;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ImplementSetService;
import com.lifelight.dubbo.service.WeixinConfigureService;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/implement")
public class ImplementSetController {

	private static final Logger logger = LoggerFactory.getLogger(ImplementSetController.class);
	@Reference
	private ImplementSetService implementService;
	@Autowired
	private RedisTool redisTool;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/query")
	@ResponseBody
	public Result getImplementSetInfoByType(HttpServletRequest request,
			@RequestParam(value = "type", required = false) String type) {
		if (type == null || type.isEmpty()) {
			type = "-1";
		}
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName()+"type:"+type);
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return implementService.getImplementSetInfoByType(type,Integer.parseInt(weixinId));
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/add")
	@ResponseBody
	public Result insertImplementSetInfo(HttpServletRequest request,@RequestBody ImplementSet implementSet) {
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));
		return implementService.implementSetInfoCreate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/update")
	@ResponseBody
	public Result updateImplementSetInfo(@RequestBody ImplementSet implementSet) {
		return implementService.implementSetInfoUpdate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/remove")
	@ResponseBody
	public Result deleteImplementSetInfo(@RequestParam("id") String id) {
		return implementService.implementSetInfoRemove(id);
	}

	// 20171031

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryFind")
	@ResponseBody
	public Result getImplementFind(HttpServletRequest request,@RequestParam(value = "type", required = false) String type) {
		if (type == null || type.isEmpty()) {
			type = "-1";
		}
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return implementService.getImplementFind(type,Integer.parseInt(weixinId));
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryDetailFind")
	@ResponseBody
	public Result queryFindDetailFind(HttpServletRequest request,@RequestBody ImplementSet implementSet) {
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));		
		return implementService.getImplementFindDetail(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addFind")
	@ResponseBody
	public Result addImplementFind(HttpServletRequest request,@RequestBody ImplementSet implementSet) {
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));
		return implementService.addImplementFind(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addDetailFind")
	@ResponseBody
	public Result addImplementDetailFind(HttpServletRequest request,@RequestBody ImplementSetVO implementSet) {
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));
		return implementService.addImplementDetailFind(implementSet);
	}

}