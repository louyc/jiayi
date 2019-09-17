package com.lifelight.web.controller.jayi;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.vo.OrderInfoVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.JyOrderInfoService;
import com.lifelight.web.controller.OrderInfoController;

@Controller
@RequestMapping("/jyOrderInfo")
public class JyOrderInfoController {

	private static final Logger logger = LoggerFactory.getLogger(JyOrderInfoController.class);

	@Reference
	private JyOrderInfoService jyOrderInfoService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/getJyValidUserOrder")
	@ResponseBody
	public Result<List<OrderInfoVO>> getJyValidUserOrder(
			HttpServletRequest request,
			@RequestParam(value="userId",required=true) String userId ,
			@RequestParam(value="doctorId",required=true) String doctorId ,
			@RequestParam(value="serviceName",required=false) String serviceName ,
			@RequestParam(value="buyStartTime",required=false) String buyStartTime ,
			@RequestParam(value="buyEndTime",required=false) String buyEndTime ) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

		Map<String, Object> query = new HashMap<String, Object>();

		if( null!= userId && !userId.isEmpty() ){
			query.put("userId", userId);
		}
		if( null!= doctorId && !doctorId.isEmpty() ){
			query.put("doctorId", doctorId);
		}
		if( null!= serviceName && !serviceName.isEmpty() ){
			query.put("serviceName", serviceName);
		}

		if( null!= buyStartTime && !buyStartTime.isEmpty() ){
			query.put("buyStartTime", sdf.parse( buyStartTime ));
		}

		if( null!= buyEndTime && !buyEndTime.isEmpty() ){
			query.put("buyEndTime", sdf.parse( buyEndTime ));
		}
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		query.put("platformId", Integer.parseInt(platformId));
		return jyOrderInfoService.getJyValidUserOrder(query);
	}

	@RequestMapping("/getBackValidUserOrder")
	@ResponseBody
	public Result<List<OrderInfoVO>> getBackValidUserOrder(
			HttpServletRequest request,
			@RequestParam(value="userId",required=true) String userId ,
			@RequestParam(value="doctorId",required=true) String doctorId ,
			@RequestParam(value="serviceName",required=false) String serviceName ,
			@RequestParam(value="buyStartTime",required=false) String buyStartTime ,
			@RequestParam(value="buyEndTime",required=false) String buyEndTime ) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

		Map<String, Object> query = new HashMap<String, Object>();

		if( null!= userId && !userId.isEmpty() ){
			query.put("userId", userId);
		}
		if( null!= doctorId && !doctorId.isEmpty() ){
			query.put("doctorId", doctorId);
		}
		if( null!= serviceName && !serviceName.isEmpty() ){
			query.put("serviceName", serviceName);
		}

		if( null!= buyStartTime && !buyStartTime.isEmpty() ){
			query.put("buyStartTime", sdf.parse( buyStartTime ));
		}

		if( null!= buyEndTime && !buyEndTime.isEmpty() ){
			query.put("buyEndTime", sdf.parse( buyEndTime ));
		}
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		query.put("platformId", Integer.parseInt(platformId));
		return jyOrderInfoService.getBackValidUserOrder(query);
	}

}
