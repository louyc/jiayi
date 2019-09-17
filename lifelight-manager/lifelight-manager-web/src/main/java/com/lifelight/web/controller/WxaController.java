package com.lifelight.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.common.tools.util.ValidateUtil;
import com.lifelight.dubbo.service.ApiUserInfoService;
import com.lifelight.dubbo.service.ManagerService;

@Controller
@RequestMapping("/api/wxa")
public class WxaController {

	private static final Logger logger = LoggerFactory.getLogger(WxaController.class);

	//微信小程序
	private static final String APPID = "wx293dc5a516507073";
	private static final String SECRET = "c61be765dbee1715bc5300ab9ba10495";
	@Reference
	private ManagerService managerService;
	@Autowired
	private RedisTool redisTool;
	@Reference
	private ApiUserInfoService apiUserInfoService;
	
	/**
	 * login:用户登录 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @return Result
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/login")
	@ResponseBody
	public Result login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "randCode", required = false) String randCode){
		logger.info("WXAController.login start,openId={}", " mobile:" + mobile + "  randCode:" + randCode);
		Result result = new Result(StatusCodes.OK, true);
		try {
			ManagerInfo manager = null;
			if (ValidateUtil.checkMobileNumber(mobile)) {
				// 手机号登录，校验验证码
				String randCodeRedis = redisTool.getMapField("apiUserCertified", mobile, String.class);
				if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
					return new Result(StatusCodes.FAILED_DEPENDENCY, false, new ResultCode("FALSE", "验证码填写错误"));
				}
				manager = managerService.getManagerInfoByMobile(mobile,0);
			} else {
				return new Result(StatusCodes.FAILED_DEPENDENCY, false, new ResultCode("FALSE", "输入手机号格式不对"));
			}
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				return result = apiUserInfoService.getApiUserInfoById(manager.getId(), mobile, "", "", "", "",1);
			} else {
				manager = new ManagerInfo();
				manager.setMobile(mobile);
				manager.setPassword("000000");
				manager.setPlatformId(1);
				result = managerService.regist(manager);
				if (result.getStatus() == StatusCodes.OK) {
					return result = apiUserInfoService.getApiUserInfoById(manager.getId(), mobile, "", "", "", "",1);
				} else {
					return new Result(StatusCodes.FAILED_DEPENDENCY, false, new ResultCode("FALSE", "账户注册失败"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(StatusCodes.FAILED_DEPENDENCY, false, new ResultCode("FALSE", "登陆失败"));
		}
	}
	
	/**
	 * update:完善用户信息 <br/>
	 * 
	 * @param request
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(HttpServletRequest request, 
			@RequestBody ApiUserInfoVO info, HttpServletResponse response) {
		Result result = new Result<>(StatusCodes.OK, true);
		return apiUserInfoService.updateWxaUserInfo(info);
	}
	
	/**
	 * 登录凭证校验
	 * 
	 **/
	@RequestMapping(value = "/wxa_code2Session.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void login(HttpServletRequest request, HttpServletResponse response) {
		//微信的接口
		String code = request.getParameter("code");
		logger.info("code=" + code);
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+
				"&secret="+SECRET+"&js_code="+ code +"&grant_type=authorization_code";
		RestTemplate restTemplate = new RestTemplate();
		PrintWriter out = null;
		try {
			//进行网络请求,访问url接口
			ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);  
			//根据返回值进行后续操作 
			if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK){
				String sessionData = responseEntity.getBody();
				Gson gson = new Gson();
				//解析从微信服务器获得的openid和session_key;
				com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(sessionData);
				Map resultMap = (Map) json;
				logger.info("小程序 wxa_code2Session：： "+sessionData);
				//获取用户的唯一标识
				String openid =resultMap.get("openid").toString();
				String session_key = resultMap.get("session_key").toString();
				String unionid = resultMap.get("unionid").toString();
				if(null!=resultMap.get("errcode")) {
					String errcode = resultMap.get("errcode").toString();
					String errmsg = resultMap.get("errmsg").toString();
					logger.info(" errcode"+errcode+" errmsg"+errmsg);
				}
				logger.info("openid"+openid+" session_key"+session_key+" unionid"+unionid);
				out = response.getWriter();
				out.write(sessionData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				out.close();
				out = null;
			}
		}
	}

	/**
	 * 微信小程序  接口调用凭据
	 * 获取access_token
	 * 
	 **/
	@RequestMapping(value = "/wxa_get_access_token.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void getAccessToken(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("获取access_token");
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+APPID+"&secret="+SECRET;
		RestTemplate restTemplate = new RestTemplate();
		PrintWriter out = null;
		try {
			//进行网络请求,访问url接口
			ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);  
			//根据返回值进行后续操作 
			if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK){
				String tokenData = responseEntity.getBody();
				Gson gson = new Gson();
				//解析从微信服务器获得的access_token;
				com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(tokenData);
				Map resultMap = (Map) json;
				logger.info("小程序 access_token：： "+tokenData);
				String access_token =resultMap.get("access_token").toString();
				String expires_in = resultMap.get("expires_in").toString();
				if(null!=resultMap.get("errcode")) {
					String errcode = resultMap.get("errcode").toString();
					String errmsg = resultMap.get("errmsg").toString();
					logger.info(" errcode"+errcode+" errmsg"+errmsg);
				}
				logger.info("access_token"+access_token+" expires_in"+expires_in);
				out = response.getWriter();
				out.write(tokenData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(out!=null) {
				out.close();
				out = null;
			}
		}
	}


}
