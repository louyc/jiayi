package com.lifelight.web.controller.xl;

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
import com.lifelight.api.entity.ServicePackage;
import com.lifelight.api.vo.ServicePackageVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ServicePackageService;

@Controller
@RequestMapping("/package")
public class ServicePackageController {

	private static final Logger logger = LoggerFactory.getLogger(ServicePackageController.class);

	@Reference
	private ServicePackageService servicePackageService;
	@Autowired
	private RedisTool redisTool;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/createServicePackage")
	@ResponseBody
	public Result createServicePackage(HttpServletRequest request,
			@RequestBody ServicePackageVO servicePackage) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		servicePackage.setPlatformId(Integer.parseInt(platformId));
		return servicePackageService.createServicePackage(servicePackage);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteServicePackage")
	@ResponseBody
	public Result deleteServicePackage(@RequestParam("id") Integer id) {
		return servicePackageService.deleteServicePackage(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateServicePackage")
	@ResponseBody
	public Result updateServicePackage(HttpServletRequest request,@RequestBody ServicePackageVO servicePackageVO) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		servicePackageVO.setPlatformId(Integer.parseInt(platformId));
		return servicePackageService.updateServicePackage(servicePackageVO);
	}

	/**
	 * 分页 样例
	 * pageSize 分页大小  currentsPage 当前页数
	 * 
	 * selectKeywordsListPage  （分页方法必须以 *****ListPage  为准）
	 * 实体类需继承 PageInfo
	 * 
	 * @param name
	 * @param pageSize
	 * @param currentsPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllServicePackage")
	@ResponseBody
	public Result getAllServicePackage(HttpServletRequest request,
			@RequestParam(value = "packageName", required = false) String packageName,
			@RequestParam(value = "packageId", required = false) String packageId,
			@RequestParam(value = "pageSize") String pageSize,
			@RequestParam("currentsPage") String currentsPage) {

		int currentPage = Integer.parseInt(currentsPage);

		if (currentPage <= 0){
			currentPage = 1;
		}

		ServicePackage servicePackage = new ServicePackage();

		servicePackage.setShowCount(Integer.parseInt(pageSize));
		servicePackage.setCurrentPage(currentPage);	
		if(!StringUtil.isEmpty(packageName)) {
			servicePackage.setPackageName(packageName);
		}
		if(!StringUtil.isEmpty(packageId)) {
			servicePackage.setId(Integer.parseInt(packageId));
		}
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		servicePackage.setPlatformId(Integer.parseInt(platformId));
		return servicePackageService.selectServicePackageListPage(servicePackage);
	}

}