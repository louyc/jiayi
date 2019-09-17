package com.lifelight.web.controller.jayi;

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
import com.lifelight.api.entity.JyOrgDocRel;
import com.lifelight.api.entity.JyOrgInfo;
import com.lifelight.api.entity.Message;
import com.lifelight.api.vo.JyOrgInfoVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.JyOrgDocRelService;
import com.lifelight.dubbo.service.JyOrgInfoService;
import com.lifelight.web.util.YtxSms;

@Controller
@RequestMapping("/jyOrgInfo")
public class JyOrgInfoController {

	private static final Logger logger = LoggerFactory.getLogger(JyOrgInfoController.class);

	@Reference
	private JyOrgInfoService jyOrgInfoService;

	@Reference
	private JyOrgDocRelService jyOrgDocRelService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/getJyDoctorSignOrg")
	@ResponseBody
	public Result getJyDoctorSignOrg(HttpServletRequest request,@RequestBody JyOrgInfo entity) throws Exception {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		entity.setPlatformId(Integer.parseInt(platformId));
		return jyOrgInfoService.getJyDoctorSignOrg(entity);
	}

	@RequestMapping("/getJyOrgInfoList")
	@ResponseBody
	public Result getJyOrgInfoList(HttpServletRequest request,@RequestBody JyOrgInfo entity) throws Exception {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		entity.setPlatformId(Integer.parseInt(platformId));
		return jyOrgInfoService.getJyOrgInfoList(entity);
	}

	@RequestMapping("/inviteDoctor")
	@ResponseBody
	public Result<Message> inviteDoctor(HttpServletRequest request,@RequestBody JyOrgDocRel entity) throws Exception {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Result<Message> result = jyOrgDocRelService.inviteDoctor(entity,Integer.parseInt(platformId));
		if("SUCCESS".equals(result.getResultCode().getCode())){
			//发送短信
			Message sendData = result.getData();
			logger.info("发送短信,手机号："+ sendData.getSendMobile() +"; 内容:"+sendData.getContent());
			YtxSms.sendContentMessage(sendData.getSendMobile(), YtxSms.SMS_ACCOUNT_JY_ORGSEND , sendData.getDxModel());
		}
		return result;
	}

}
