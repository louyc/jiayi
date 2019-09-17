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
import com.lifelight.api.entity.DeviceFirms;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.DeviceFirmsService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/deviceFirms")
public class DeviceFirmsController {

	@Reference
	private DeviceFirmsService deviceFirmsService;
	private static final Logger logger = LoggerFactory.getLogger(DeviceFirmsController.class);
	@Autowired
	private RedisTool redisTool;
	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectDeviceFirmsListPage")
	@ResponseBody
	public Result selectDeviceFirmsListPage(
			HttpServletRequest request,
			@RequestParam("firmName") String firmName, @RequestParam(value = "pageSize", required = true) String pageSize,
			@RequestParam(value = "currentsPage", required = true) String currentsPage) {

		DeviceFirms deviceFirms = new DeviceFirms();

		if (pageSize != null && !pageSize.isEmpty()) {
			deviceFirms.setShowCount(Integer.parseInt(pageSize));
		}

		if (currentsPage != null && !currentsPage.isEmpty()) {
			int currentPage = Integer.parseInt(currentsPage);

			if (currentPage <= 0){
				currentPage = 1;
			}

			int currentResult = (currentPage-1) * Integer.parseInt(pageSize);

			deviceFirms.setCurrentPage(currentPage);
			deviceFirms.setCurrentResult(currentResult);
		}
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceFirms.setFirmName(firmName);
		deviceFirms.setPlatformId(Integer.parseInt(weixinId));
		return deviceFirmsService.selectDeviceFirmsListPage(deviceFirms);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceFirm")
	@ResponseBody
	public Result insertDeviceFirm( HttpServletRequest request,@RequestBody DeviceFirms deviceFirms) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceFirms.setPlatformId(Integer.parseInt(weixinId));
		return deviceFirmsService.insertDeviceFirm(deviceFirms);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceFirm")
	@ResponseBody
	public Result updateDeviceFirm(HttpServletRequest request,@RequestBody DeviceFirms deviceFirms) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceFirms.setPlatformId(Integer.parseInt(weixinId));
		return deviceFirmsService.updateDeviceFirm(deviceFirms);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceFirm")
	@ResponseBody
	public Result deleteDeviceFirm(@RequestParam("id") Integer id,HttpServletRequest request) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return deviceFirmsService.deleteDeviceFirm(id,Integer.parseInt(weixinId));
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceFirm")
	@ResponseBody
	public Result recoverDeviceFirm(@RequestParam("id") Integer id) {
		return deviceFirmsService.recoverDeviceFirm(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceFirmById")
	@ResponseBody
	public Result getDeviceFirmById(@RequestParam("id") Integer id) {
		return deviceFirmsService.getDeviceFirmById(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceFirms")
	@ResponseBody
	public Result getDeviceFirms(HttpServletRequest request) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return deviceFirmsService.getDeviceFirms(Integer.parseInt(weixinId));
	}
}