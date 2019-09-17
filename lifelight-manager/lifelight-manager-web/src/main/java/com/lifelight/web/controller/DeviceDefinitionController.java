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
import com.lifelight.api.entity.DeviceDefinition;
import com.lifelight.api.vo.DeviceDefinitionVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.DeviceDefinitionService;

@Controller
@RequestMapping("/deviceDefinition")
public class DeviceDefinitionController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceDefinitionController.class);
	@Reference
	private DeviceDefinitionService deviceDefinitionService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/getDeviceDefinitionList")
	@ResponseBody
	public Result getDeviceDefinitionList( HttpServletRequest request,
			@RequestBody DeviceDefinition entity){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		entity.setPlatformId(Integer.parseInt(weixinId));
		return deviceDefinitionService.getDeviceDefinitionList(entity);
	}

	@RequestMapping("/addDeviceDefinition")
	@ResponseBody
	public Result<DeviceDefinition> addDeviceDefinition(HttpServletRequest request,
			@RequestBody DeviceDefinition entity){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		entity.setPlatformId(Integer.parseInt(weixinId));
		return deviceDefinitionService.addDeviceDefinition(entity);
	}

	@RequestMapping("/updateDeviceDefinition")
	@ResponseBody
	public Result<DeviceDefinition> updateDeviceDefinition(@RequestBody DeviceDefinition entity){
		return deviceDefinitionService.updateDeviceDefinition(entity);
	}

	@RequestMapping("/getDeviceDefinitionById")
	@ResponseBody
	public Result<DeviceDefinitionVO> getDeviceDefinitionById( @RequestParam("id") Integer id){
		return deviceDefinitionService.getDeviceDefinitionById(id);
	}

	@RequestMapping("/deleteDeviceDefinition")
	@ResponseBody
	public Result deleteDeviceDefinition(HttpServletRequest request,
			@RequestParam("id") Integer id){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return deviceDefinitionService.deleteDeviceDefinition(id,Integer.parseInt(weixinId));
	}

}
