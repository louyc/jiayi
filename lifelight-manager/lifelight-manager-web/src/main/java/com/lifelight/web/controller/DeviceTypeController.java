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
import com.lifelight.api.entity.DeviceType;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.DeviceTypeService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/deviceType")
public class DeviceTypeController {
	private static final Logger logger = LoggerFactory.getLogger(DeviceTypeController.class);

	@Reference
	private DeviceTypeService deviceTypeService;
	@Autowired
	private RedisTool redisTool;
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllDeviceType")
	@ResponseBody
	public Result getAllDeviceType(HttpServletRequest request,@RequestBody DeviceType deviceType) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceType.setPlatformId(Integer.parseInt(weixinId));
		return deviceTypeService.getAllDeviceType(deviceType);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceType")
	@ResponseBody
	public Result insertDeviceType(HttpServletRequest request,@RequestBody DeviceType deviceType) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceType.setPlatformId(Integer.parseInt(weixinId));
		return deviceTypeService.insertDeviceType(deviceType);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceType")
	@ResponseBody
	public Result updateDeviceType(HttpServletRequest request,@RequestBody DeviceType deviceType) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceType.setPlatformId(Integer.parseInt(weixinId));
		return deviceTypeService.updateDeviceType(deviceType);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceType")
	@ResponseBody
	public Result deleteDeviceType(@RequestParam("id") Integer id) {
		return deviceTypeService.deleteDeviceType(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceType")
	@ResponseBody
	public Result recoverDeviceType(@RequestParam("id") Integer id) {
		return deviceTypeService.recoverDeviceType(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceTypeById")
	@ResponseBody
	public Result getDeviceTypeById(@RequestParam("id") Integer id) {
		return deviceTypeService.getDeviceTypeById(id);
	}
}