package com.lifelight.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.vo.UserShareServiceVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ShareServiceService;

@Controller
@RequestMapping("/shareService")
public class ShareServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ShareServiceController.class);

	@Reference
	private ShareServiceService shareServiceService;

	@Autowired
	private RedisTool redisTool;
	/**
	 * 创建机构用户共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addShareServices")
	@ResponseBody
	public Result addShareServices(@RequestBody UserShareServiceVO shareService) {
		logger.debug("机构选择共享服务:" + shareService.getIdList() + " 当前用户id：" + shareService.getOrgId());
		return shareServiceService.addShareServices(shareService);
	}

	/**
	 * 查询机构用户共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getShareServices")
	@ResponseBody
	public Result getShareServices(HttpServletRequest request,@RequestBody UserShareServiceVO shareService) {
		int currentResult = (shareService.getCurrentPage() - 1) * shareService.getShowCount();
		shareService.setCurrentResult(currentResult);
		if (shareService.getShareName() != null && !shareService.getShareName().isEmpty()) {
			logger.debug("查询共享服务：姓名：" + shareService.getShareName());
			shareService.setShareName(shareService.getShareName() + "%");
		}
		if (shareService.getServiceName() != null && !shareService.getServiceName().isEmpty()) {
			logger.debug("查询共享服务：服务名称：" + shareService.getServiceName());
			shareService.setServiceName(shareService.getServiceName() + "%");
		}
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		shareService.setPlatformId(Integer.parseInt(weixinId));
		return shareServiceService.getShareServices(shareService);
	}

	/**
	 * 删除机构用户共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/removeShareServices")
	@ResponseBody
	public Result removeShareServices(@RequestBody UserShareServiceVO shareService) {
		logger.debug("要删除的服务id：" + shareService.getId());
		return shareServiceService.removeShareServices(shareService);
	}

}