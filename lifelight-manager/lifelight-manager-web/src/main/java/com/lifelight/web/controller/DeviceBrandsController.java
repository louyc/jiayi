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
import com.lifelight.api.entity.DeviceBrands;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.DeviceBrandsService;
import com.lifelight.dubbo.service.DeviceFirmsService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/deviceBrands")
public class DeviceBrandsController {
	private static final Logger logger = LoggerFactory.getLogger(DeviceBrandsController.class);

	@Reference
	private DeviceBrandsService deviceBrandsService;
	@Reference
	private DeviceFirmsService deviceFirmsService;
	@Reference 
	private WeixinConfigureService weixinConfigureService;
	@Autowired
	private RedisTool redisTool;
	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectDeviceBrandsListPage")
	@ResponseBody
	public Result selectDeviceBrandsListPage(@RequestParam(value = "brandName", required = false) String brandName, 
			@RequestParam(value = "firmName", required = false) String firmName, 
			@RequestParam(value = "firmId", required = false) Integer firmId,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "currentsPage", required = false) String currentsPage,
			HttpServletRequest request) {

		DeviceBrands deviceBrands = new DeviceBrands();

		if (pageSize != null && !pageSize.isEmpty()) {
			deviceBrands.setShowCount(Integer.parseInt(pageSize));
		}

		if (currentsPage != null && !currentsPage.isEmpty()) {

			int currentPage = Integer.parseInt(currentsPage);

			if (currentPage <= 0){
				currentPage = 1;
			}
			int currentResult = (currentPage-1) * Integer.parseInt(pageSize);

			deviceBrands.setCurrentPage(currentPage);
			deviceBrands.setCurrentResult(currentResult);
		}

		if(brandName!=null){
			deviceBrands.setBrandName(brandName);
		}

		if(firmName != null){
			deviceBrands.setFirmName(firmName);
		}

		if(firmId != null){
			deviceBrands.setDeviceFirmid(firmId);
		}
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceBrands.setPlatformId(Integer.parseInt(weixinId));
		return deviceBrandsService.selectDeviceBrandsListPage(deviceBrands);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceBrand")
	@ResponseBody
	public Result insertDeviceBrand(HttpServletRequest request,
			@RequestBody DeviceBrands deviceBrands) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceBrands.setPlatformId(Integer.parseInt(weixinId));
		return deviceBrandsService.insertDeviceBrand(deviceBrands);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceBrand")
	@ResponseBody
	public Result updateDeviceBrand(HttpServletRequest request,@RequestBody DeviceBrands deviceBrands) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		deviceBrands.setPlatformId(Integer.parseInt(weixinId));
		return deviceBrandsService.updateDeviceBrand(deviceBrands);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceBrand")
	@ResponseBody
	public Result deleteDeviceBrand(HttpServletRequest request,@RequestParam("id") Integer id) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return deviceBrandsService.deleteDeviceBrand(id,Integer.parseInt(weixinId));
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceBrand")
	@ResponseBody
	public Result recoverDeviceBrand(@RequestParam("id") Integer id) {
		return deviceBrandsService.recoverDeviceBrand(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceBrandById")
	@ResponseBody
	public Result getDeviceBrandById(@RequestParam("id") Integer id) {
		return deviceBrandsService.getDeviceBrandById(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceBrands")
	@ResponseBody
	public Result getDeviceBrands(HttpServletRequest request) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return deviceBrandsService.getDeviceBrands(Integer.parseInt(weixinId));
	}
}