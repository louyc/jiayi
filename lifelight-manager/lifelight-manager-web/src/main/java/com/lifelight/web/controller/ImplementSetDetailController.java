package com.lifelight.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.ImplementSetDetail;
import com.lifelight.api.vo.ImplementSetDetailVO;
import com.lifelight.api.vo.ServiceInfoVO;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ImplementSetDetailService;
import com.lifelight.dubbo.service.ServiceService;
import com.lifelight.dubbo.service.WeixinConfigureService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/implementDetail")
public class ImplementSetDetailController {

	private static final Logger logger = LoggerFactory.getLogger(ImplementSetDetailController.class);
	@Autowired
	private RedisTool redisTool;
	@Reference
	private ImplementSetDetailService implementDetailService;
	@Reference
	ServiceService serviceService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryById")
	@ResponseBody
	public Result queryById(
			@RequestParam(value = "implementId", required = false) String implementId) {
		return implementDetailService.getImplementDetail(implementId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryByEntity")
	@ResponseBody
	public Result queryByEntity(HttpServletRequest request,@RequestBody ImplementSetDetailVO implementSet) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));
		return implementDetailService.getImplementDetail(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryServiceAndImplement")
	@ResponseBody
	public Result queryServiceAndImplement(HttpServletResponse response,HttpServletRequest request,
			ImplementSetDetailVO implementSet) {
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if (null != implementSet.getServiceType() && implementSet.getServiceType() > 0) {
			serviceVO.setServiceType(implementSet.getServiceType());
		}
		if (implementSet.getShowCount() < 1) {
			implementSet.setShowCount(1000);
		}
		if (!StringUtil.isEmpty(implementSet.getTitle())) {
			implementSet.setTitle(implementSet.getTitle() + "%");
			serviceVO.setName(implementSet.getTitle() + "%");
		}
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));
		serviceVO.setCurrentPage(implementSet.getCurrentPage());
		serviceVO.setShowCount(implementSet.getShowCount());
		serviceVO.setInUse(1);
		serviceVO.setPlatformId(Integer.parseInt(weixinId));
		List<Integer> listStatus = new ArrayList<Integer>();
		listStatus.add(2);
		serviceVO.setStatusList(listStatus);
		serviceVO.setServiceAttribute(1);
		Result result = new Result<>(StatusCodes.OK, true);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		Map<String, Object> map = new HashMap<String, Object>();
		PaginatedResult<ImplementSetDetailVO> resultImplement = (PaginatedResult<ImplementSetDetailVO>) implementDetailService
				.queryServiceAndImplement(implementSet);
		PaginatedResult<ServiceInfoVO> resultService = (PaginatedResult<ServiceInfoVO>) serviceService
				.getServiceInfoListPage(serviceVO);
		if (null != resultImplement.getItems()) {
			map.put("implement", resultImplement.getItems());
		} else {
			map.put("implement", "");
		}
		if (null != resultImplement.getItems()) {
			map.put("service", resultService.getItems());
		} else {
			map.put("service", "");
		}
		map.put("totalItemsCount", resultService.getTotalItemsCount());
		map.put("pageNumber", resultService.getPageNumber());
		map.put("pagesCount", resultService.getPagesCount());
		result.setData(map);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addDetail")
	@ResponseBody
	public Result addDetail(HttpServletRequest request,@RequestBody ImplementSetDetail implementSet) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		implementSet.setPlatformId(Integer.parseInt(weixinId));
		return implementDetailService.implementDetailCreate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDetail")
	@ResponseBody
	public Result updateDetail(@RequestBody ImplementSetDetail implementSet) {
		return implementDetailService.implementDetailUpdate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/removeDetail")
	@ResponseBody
	public Result removeDetail(@RequestParam("id") String id) {
		return implementDetailService.implementDetailRemove(id);
	}

}