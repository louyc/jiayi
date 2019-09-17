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
@RequestMapping("/huawei")
public class HuaWeiController {

	private static final Logger logger = LoggerFactory.getLogger(HuaWeiController.class);

	//微信小程序
	private static final String APPID = "wx293dc5a516507073";
	private static final String SECRET = "c61be765dbee1715bc5300ab9ba10495";
	@Reference
	private ManagerService managerService;
	@Reference
	private ApiUserInfoService apiUserInfoService;
	

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
