package com.lifelight.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.vo.DeviceDataVO;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.DeviceDataService;

@Controller
@RequestMapping("/deviceData")
public class DeviceDataController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDataController.class);
	@Reference
	private DeviceDataService deviceDataService;
	/**
	 * 获取用户所有的测量数据
	 * 
	 * @param managerId
	 * @param time
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getMeasureData")
	@ResponseBody
	public Result getMeasureData(HttpServletRequest request,
			@RequestParam("managerId") String managerId,
			@RequestParam("time") String time) {
		return deviceDataService.getMeasureData(managerId, time);
	}

	/**
	 * 获取用户测试数据
	 * 
	 * @param managerId
	 * @param time
	 * @param type
	 *            类型 0: 传 0 只查询观察项 1: 全查询
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getHistoryMeasureData")
	@ResponseBody
	public Result getHistoryMeasureData(HttpServletRequest request,
			@RequestParam("managerId") String managerId,
			@RequestParam("time") String time) {
		return deviceDataService.getHistoryMeasureData(managerId, time);
	}

	/**
	 * 根据类型 获取测量记录
	 * 
	 * @param managerId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getMeasureDataByType")
	@ResponseBody
	public Result getMeasureDataByType(HttpServletRequest request,
			@RequestParam("managerId") String managerId,
			@RequestParam("time") String time, @RequestParam("type") String type) {
		return deviceDataService.getMeasureDataByType(managerId, time, type);
	}

	/**
	 * 填写健康档案
	 * 
	 * @param managerId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertMeasureData")
	@ResponseBody
	public Result insertMeasureData(HttpServletRequest request,@RequestParam("managerId") String managerId,
			@RequestParam("type") String type, @RequestParam("data") String data) {
		return deviceDataService.insertMeasureData(managerId, type, data);
	}

	/**
	 * 根据 参数类型 用户ID 获取最新测量记录
	 * 
	 * @param managerId
	 * @param type
	 *            weight：体重 bp：血压 bo：血氧 hr：心率 sn：步数 bg：血糖
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getMeasureDataByMidType")
	@ResponseBody
	public Result getMeasureDataByMidType(HttpServletRequest request,
			@RequestParam("managerId") String managerId,
			@RequestParam("time") String time, @RequestParam("type") String type) {
		return deviceDataService.getMeasureDataByMidType(managerId, time, type);
	}

	/**
	 * 查询当前专家下的绑定用户
	 * 
	 * @param mobile
	 *            手机号 name 用户名称 managerId 专家id pageSize 每页条数 currentsPage 当前页码
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllDeviceBindingUser")
	@ResponseBody
	public Result getAllDeviceBindingUser(HttpServletRequest request,
			@RequestParam("managerId") String managerId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam("pageSize") String pageSize,
			@RequestParam("currentsPage") String currentsPage) {
		DeviceDataVO deviceData = new DeviceDataVO();
		if (!StringUtil.isEmpty(name)) {
			deviceData.setName(name);
		}
		if (!StringUtil.isEmpty(mobile)) {
			deviceData.setMobile(mobile);
		}
		deviceData.setDeviceLocationId(managerId);
		int currentPage = Integer.parseInt(currentsPage);
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int currentResult = (currentPage - 1) * Integer.parseInt(pageSize);

		deviceData.setShowCount(Integer.parseInt(pageSize));
		deviceData.setCurrentResult(currentResult);
		deviceData.setCurrentPage(currentPage);
		return deviceDataService.getAllDeviceBindingUser(deviceData);
	}

	/**
	 * 查询用户设备检测信息
	 * 
	 * @param managerId
	 *            用户id
	 * @param dataType
	 *            项目名称
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param pageSize
	 *            每页条数
	 * @param currentsPage
	 *            当前页码
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryUserInfo")
	@ResponseBody
	public Result queryUserInfo(HttpServletRequest request,
			@RequestParam("managerId") String managerId,
			@RequestParam(value = "dataType", required = false) String dataType,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("pageSize") String pageSize,
			@RequestParam("currentsPage") String currentsPage) {
		DeviceDataVO deviceData = new DeviceDataVO();
		if (!StringUtil.isEmpty(dataType)) {
			deviceData.setDataType(dataType);
		}
		if (!StringUtil.isEmpty(startTime)) {
			deviceData.setStartTime(startTime);
		}
		if (!StringUtil.isEmpty(endTime)) {
			deviceData.setEndTime(endTime);
		}
		deviceData.setManagerId(managerId);
		int currentPage = Integer.parseInt(currentsPage);
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int currentResult = (currentPage - 1) * Integer.parseInt(pageSize);

		deviceData.setShowCount(Integer.parseInt(pageSize));
		deviceData.setCurrentResult(currentResult);
		deviceData.setCurrentPage(currentPage);
		return deviceDataService.queryUserInfo(deviceData);
	}

	/**
	 * 查询项目信息
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryDeviceItems")
	@ResponseBody
	public Result queryDeviceItems() {
		return deviceDataService.queryDeviceItems();
	}

	/**
	 * 查询检测项目list  返回给流程编辑器
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryCheckDeviceItems")
	@ResponseBody
	public Result queryCheckDeviceItems() {
		return deviceDataService.queryCheckDeviceItems();
	}
	
	
	/**
	 * 小程序 :   根据手机号      获取单项所有测量记录
	 * 
	 * @param mobile
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getMeasureDataByMobile")
	@ResponseBody
	public Result getMeasureDataByMobile(HttpServletRequest request,
			@RequestParam("mobile") String mobile,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "type", required = false) String type) {
		logger.info("getMeasureDataByMobile:::"+mobile+"   "+type);
		return deviceDataService.getMeasureDataByMobile(mobile, time, type);
	}
	
	/**
	 * 小程序 :   根据手机号     查询所有最新测量数据
	 * 
	 * @param mobile
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getNewMeasureData")
	@ResponseBody
	public Result getNewMeasureData(HttpServletRequest request,
			@RequestParam("mobile") String mobile,
			@RequestParam(value = "time", required = false) String time) {
		return deviceDataService.getNewMeasureData(mobile, time);
	}
}