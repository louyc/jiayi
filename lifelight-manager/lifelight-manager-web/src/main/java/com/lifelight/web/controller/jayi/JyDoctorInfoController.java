package com.lifelight.web.controller.jayi;

import java.util.Date;
import java.util.List;

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
import com.lifelight.api.entity.JyDoctorInfo;
import com.lifelight.api.entity.Message;
import com.lifelight.api.vo.JyDoctorInfoVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.JyDoctorInfoService;
import com.lifelight.dubbo.service.JyOrgDocRelService;
import com.lifelight.web.util.YtxSms;

@Controller
@RequestMapping("/jyDoctorInfo")
public class JyDoctorInfoController {

	private static final Logger logger = LoggerFactory.getLogger(JyDoctorInfoController.class);

	@Reference
	private JyDoctorInfoService jyDoctorInfoService;

	@Reference
	private JyOrgDocRelService jyOrgDocRelService;
	@Autowired
	private RedisTool redisTool;

	@RequestMapping("/findJyDoctorInfo")
	@ResponseBody
	public Result<List<JyDoctorInfoVO>> findJyDoctorInfo(HttpServletRequest request,
			@RequestBody JyDoctorInfo entity) throws Exception {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		entity.setPlatformId(Integer.parseInt(platformId));
		return jyDoctorInfoService.findJyDoctorInfo(entity);
	}

	@RequestMapping("/getJySignDoctorInfo")
	@ResponseBody
	public Result getJySignDoctorInfo(
			HttpServletRequest request,@RequestBody JyDoctorInfo entity) throws Exception {
		if(entity.getValidTime() != null){
			entity.setValidTime(new Date());
		}
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		entity.setPlatformId(Integer.parseInt(platformId));
		return jyDoctorInfoService.getJySignDoctorInfo(entity);
	}

	@RequestMapping("/agreeInvite")
	@ResponseBody
	public Result<Message> agreeInvite(HttpServletRequest request,@RequestParam("relId") Integer relId) throws Exception {

		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Result<Message> result = jyOrgDocRelService.agreeInvite(relId,Integer.parseInt(platformId));

		if("SUCCESS".equals(result.getResultCode().getCode())){
			//发送短信
			Message sendData = result.getData();
			logger.info("发送短信,手机号："+ sendData.getSendMobile() +"; 内容:"+sendData.getContent());
			YtxSms.sendContentMessage(sendData.getSendMobile(), YtxSms.SMS_ACCOUNT_JY_DOCTORIN ,sendData.getDxModel());
		}

		return result;
	}

	@RequestMapping("/refuseInvite")
	@ResponseBody
	public Result<Message> refuseInvite(HttpServletRequest request,@RequestParam("relId") Integer relId) throws Exception {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}

		Result<Message> result = jyOrgDocRelService.refuseInvite(relId,Integer.parseInt(platformId));

		if("SUCCESS".equals(result.getResultCode().getCode())){
			//发送短信
			//			Message sendData = result.getData();
			//			logger.info("发送短信,手机号："+ sendData.getSendMobile() +"; 内容:"+sendData.getContent());
			//			YtxSms.sendContentMessage(sendData.getSendMobile(), YtxSms.SMS_ACCOUNT_JY_DOCTOROUT , sendData.getDxModel());
		}
		return result;
	}

	/**
	 * 机构修改家医类型
	 * @param signId
	 * @param types
	 * @return
	 */
	@RequestMapping("/changeSignType")
	@ResponseBody
	public Result<Object> changeSignType(HttpServletRequest request,@RequestParam("signId") Integer signId ,
			@RequestParam("types") List<Integer> types){

		logger.info("JyDoctorInfoController.changeSignType  signId="+signId+" types="+types);
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}	
		return jyDoctorInfoService.changeSignType(signId, types,Integer.parseInt(platformId));
	}

}
