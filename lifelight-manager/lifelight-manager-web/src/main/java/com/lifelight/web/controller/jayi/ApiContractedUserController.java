package com.lifelight.web.controller.jayi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.Dictionary;
import com.lifelight.api.entity.UserDoctorRel;
import com.lifelight.api.vo.ApiContractedUserVO;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.DeviceDataVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.common.tools.util.DateTimeUtil;
import com.lifelight.dubbo.service.ApiContractedUserService;
import com.lifelight.dubbo.service.DeviceDataService;
import com.lifelight.dubbo.service.JyOrderInfoService;
import com.lifelight.dubbo.service.JyServiceService;
import com.lifelight.dubbo.service.UserDoctorRelService;
import com.lifelight.dubbo.service.XlPersonDocumentService;

@Controller
@RequestMapping("/api/contracte")
public class ApiContractedUserController {

	private static final Logger logger = LoggerFactory.getLogger(ApiContractedUserController.class);

	@Reference
	private ApiContractedUserService apiContractedUserService;
	@Reference
	private UserDoctorRelService userDoctorRelService;
	@Autowired
	private RedisTool redisTool;
	@Reference
	private DeviceDataService deviceDataService;
	@Reference
	private JyOrderInfoService jyOrderInfoService;
	@Reference
	private JyServiceService jyServiceService;
	@Reference
	private XlPersonDocumentService personDocumentService;

	/**
	 * queryDictionary:查询字典表 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryDictionary", method = RequestMethod.GET)
	@ResponseBody
	public Result queryDictionary(HttpServletRequest request,
			@RequestParam(value = "typeId", required = false) String typeId,
			@RequestParam(value = "itemName", required = false) String itemName,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "currentsPage", required = false) String currentsPage) {
		logger.debug("查询字典表typeId: " + typeId+"  "+itemName);
		if(StringUtil.isEmpty(pageSize)&& StringUtil.isEmpty(currentsPage)) {
			return apiContractedUserService.queryDictionary(typeId,itemName);
		}
		return apiContractedUserService.queryDictionaryListPage(typeId,itemName,pageSize,currentsPage);
	}
	/**
	 * addDictionary:增加字典表某项 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addDictionary", method = RequestMethod.POST)
	@ResponseBody
	public Result addDictionary(HttpServletRequest request,
			@RequestBody Dictionary dic) {
		logger.debug("增加字典表typeId: " + dic.getItemType());
		return apiContractedUserService.addDictionary(dic);
	}
	/**
	 * delDictionary:删除字典表某项 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delDictionary", method = RequestMethod.GET)
	@ResponseBody
	public Result delDictionary(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id) {
		logger.debug("查询字典表typeId: " + id);
		return apiContractedUserService.delDictionary(id);
	}
	/**
	 * updateDictionary:修改字典表某项 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateDictionary", method = RequestMethod.POST)
	@ResponseBody
	public Result updateDictionary(HttpServletRequest request,
			@RequestBody Dictionary dic) {
		logger.debug("修改字典表typeId: " + dic.getId());
		return apiContractedUserService.updateDictionary(dic);
	}

	/**
	 * 根据用户id查询签约用户信息
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	/*@SuppressWarnings("rawtypes")
	@RequestMapping("/queryUserArchives")
	@ResponseBody
	public Result queryUserArchives(HttpServletRequest request,
			@RequestParam(value = "managerId", required = false) String managerId,
			@RequestParam(value = "doctorId", required = false) String doctorId,
			@RequestParam(value = "id", required = false) String id) {
		logger.debug("查询签约用户信息mangerId:" + managerId + "  id:" + id);
		return apiContractedUserService.queryUserArchives(managerId, id,doctorId);
	}*/

	/**
	 * 查询签约用户详细信息  根据身份证查询
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/querySignUserByEntity")
	@ResponseBody
	public Result querySignUserByEntity(HttpServletRequest request,
			@RequestBody XlPersonDocumentVO document) {
		logger.debug("查询签约用户信息 cardId:" + document.getCardNum());
//		return apiContractedUserService.querySignUserByEntity(signUser);
		return personDocumentService.querySignUserByEntityCard(document);
	}

	/**
	 * 用户 申请服务
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/addUserArchives")
	@ResponseBody
	public Result addUserArchives(HttpServletRequest request,
			@RequestBody ApiContractedUserVO signUser) {
		logger.debug("申请服务  用户id+医生id  ：" + signUser.getManagerId() + "  " + signUser.getDoctorId());
		/*Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.YEAR, 1);// 把日期往后增加一年.整数往后推,负数往前移动
		Date afterDate = calendar.getTime();
		signUser.setCreateTime(date);
		signUser.setInUse("1");
		signUser.setYears(1);
		signUser.setExpirationDate(afterDate);*/
		Result result = apiContractedUserService.addUserArchives(signUser);
		return result;
	}

	/**
	 * 修改签约用户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 *//*
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateUserArchives")
	@ResponseBody
	public Result updateUserArchives(HttpServletRequest request,
			@RequestBody ApiContractedUserVO signUser) {
		logger.debug("修改签约用户信息 managerId:" + signUser.getManagerId());
		return apiContractedUserService.updateUserArchives(signUser);
	}
*/
	/**
	 * 签约用户分析
	 * 
	 * 错误 未改
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/userAnalysis")
	@ResponseBody
	public Result userAnalysis(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("ApiContractedUserController.userAnalysis start, userInfo = {}",
				userInfo.toString());
		
		return new Result<>(StatusCodes.OK, true);
		/*if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		userInfo.setBuildType("1");
		userInfo.setDisplayType("1");
		return apiContractedUserService.userAnalysis(userInfo);*/
	}

	/**
	 * 用户管理 ：机构查询用户 医生查询用户
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryAllUser")
	@ResponseBody
	public Result getAllContractedUser(HttpServletRequest request,
			@RequestBody ApiContractedUserVO userInfo) {
		logger.info("ApiContractedUserController.getAllContractedUser start, userInfo = {}",
				userInfo.toString());
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
//		return apiContractedUserService.getAllContractedUserplatformId(userInfo);
		userInfo.setPlatformId(Integer.parseInt(platformId));
		return personDocumentService.getAllContractedUser(userInfo);
	}

	/**
	 * 根据用户查询签约医生信息
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryDoctors")
	@ResponseBody
	public Result queryDoctors(HttpServletRequest request,
			@RequestParam(value = "managerId", required = false) String managerId) {
		logger.info("ApiContractedUserController.queryDoctors start, userInfo = {}",
				managerId.toString());
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Result result = userDoctorRelService.queryDoctors(managerId,Integer.parseInt(platformId));

		return result;
	}

	/**
	 * 根据用户查询签约和申请服务医生信息
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryAllDoctors")
	@ResponseBody
	public Result queryAllDoctors(HttpServletRequest request,
			@RequestParam(value = "managerId", required = false) String managerId) {
		logger.info("ApiContractedUserController.queryDoctors start, userInfo = {}",
				managerId.toString());
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Result result = userDoctorRelService.queryAllDoctors(managerId,Integer.parseInt(platformId));

		return result;
	}

	/**
	 * 根据用户、医生查询是否有 申请医生关系
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryRel")
	@ResponseBody
	public Result queryRel(HttpServletRequest request, @RequestBody UserDoctorRel udRel) {
		logger.info("ApiContractedUserController.queryDoctors start, userInfo = {}",
				udRel.toString());
		Result result = userDoctorRelService.queryRel(udRel);

		return result;
	}

	/**
	 * 查询 签约用户设备检测信息
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
	@RequestMapping("/queryUserCeShi")
	@ResponseBody
	public Result queryUserInfo(@RequestParam("managerId") String managerId,
			@RequestParam(value = "doctorId", required = false) String doctorId,
			@RequestParam(value = "dataType", required = false) String dataType,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("pageSize") String pageSize,
			@RequestParam("currentsPage") String currentsPage) {
		// Result result = apiContractedUserService.queryDoctors(managerId);
		List<UserDoctorRel> rels = userDoctorRelService.queryDocRel(managerId, doctorId);
		Date signStart = new Date();
		Date signEnd = new Date();
		if (null != rels && rels.size() > 0) {
			for (UserDoctorRel udr : rels) {
				if (signStart.getTime() > udr.getCreateTime().getTime()) {
					signStart = udr.getCreateTime();
				}
				if (null != udr.getExpirationDate()
						&& signEnd.getTime() < udr.getExpirationDate().getTime()) {
					signEnd = udr.getExpirationDate();
				}
			}
		} else {
			PaginatedResult<Object> pa = new PaginatedResult<>(null, Integer.parseInt(currentsPage),
					Integer.parseInt(pageSize), 0);
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功,暂无数据！"));
			return pa;
		}
		DeviceDataVO deviceData = new DeviceDataVO();
		if (!StringUtil.isEmpty(dataType)) {
			deviceData.setDataType(dataType);
		}
		try {
			if (!StringUtil.isEmpty(startTime)) {
				if (DateTimeUtil.daysBetween(signStart,
						new SimpleDateFormat("yyyy-MM-dd").parse(startTime)) > 0) {
					deviceData.setStartTime(startTime);
				} else {
					deviceData.setStartTime(new SimpleDateFormat("yyyy-MM-dd").format(signStart));
				}
			} else {
				deviceData.setStartTime(new SimpleDateFormat("yyyy-MM-dd").format(signStart));
			}
			if (!StringUtil.isEmpty(endTime)) {
				if (DateTimeUtil.daysBetween(signEnd,
						new SimpleDateFormat("yyyy-MM-dd").parse(endTime)) > 0) {
					deviceData.setEndTime(new SimpleDateFormat("yyyy-MM-dd").format(signEnd));
				} else {
					deviceData.setEndTime(endTime);
				}
			} else {
				deviceData.setEndTime(new SimpleDateFormat("yyyy-MM-dd").format(signEnd));
			}
		} catch (ParseException e) {
			logger.info(e.getMessage());
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "时间格式化出错"));
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

}
