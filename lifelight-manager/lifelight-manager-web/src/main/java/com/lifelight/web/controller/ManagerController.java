package com.lifelight.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.common.tools.util.JWTTokenUtil;
import com.lifelight.dubbo.service.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Reference
	private ManagerService managerService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/")
	public String goIndex() {
		return "index";
	}

	/**
	 * getManagerInfoById:根据 id 查询用户信息 <br/>
	 * 
	 * @param request
	 * @param id
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/one")
	@ResponseBody
	public Result getManagerInfoById(HttpServletRequest request, @RequestParam("id") String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		String token = request.getHeader("X-Auth-Token");
		result = managerService.getManagerInfoById(id);
		if (StringUtils.isEmpty(result.getToken()) || !result.isSuccessful()) {
			result.setToken(token);
		}
		return result;
	}

	/**
	 * login:用户登录 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @param password
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/login/submit")
	@ResponseBody
	public Result login(HttpServletRequest request, @RequestParam("userName") String userName,
			@RequestParam("password") String password) {
		logger.info("ManagerController.login start,userName={}", userName);
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return managerService.login(userName, password,Integer.parseInt(weixinId));
	}

	/**
	 * regist:用户注册 <br/>
	 * 
	 * @param request
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/regist/submit", method = RequestMethod.POST)
	@ResponseBody
	public Result regist(HttpServletRequest request, @RequestBody ManagerInfo info) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		info.setPlatformId(Integer.parseInt(weixinId));
		return managerService.regist(info);
	}

	/**
	 * updatePassword:用户修改密码 <br/>
	 * 
	 * @param request
	 * @param oldPassword
	 * @param newPassword
	 * @return Result
	 * @exception @author
	 *                hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/password/update")
	@ResponseBody
	public Result updatePassword(HttpServletRequest request,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		Result result = new Result<>(StatusCodes.OK, true);
		String token = request.getHeader("X-Auth-Token");
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");
		result = managerService.updatePassword(id, oldPassword, newPassword);

		if (StringUtils.isEmpty(result.getToken()) || !result.isSuccessful()) {
			result.setToken(token);
		}

		return result;
	}

	/**
	 * forgetPassword:忘记密码 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @param password
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/password/forget")
	@ResponseBody
	public Result forgetPassword(HttpServletRequest request,
			@RequestParam("userName") String userName, @RequestParam("password") String password) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}	
		return managerService.forgetPassword(userName, password,Integer.parseInt(weixinId));
	}

	/**
	 * forgetPassword:重置密码 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/password/reset")
	@ResponseBody
	public Result resetPassword(HttpServletRequest request,
			@RequestParam("userName") String userName) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		// 重置默认密码为：123456
		return managerService.forgetPassword(userName, "123456",Integer.parseInt(weixinId));

	}

	/**
	 * update:修改用户信息 <br/>
	 * 
	 * @param request
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(HttpServletRequest request, @RequestBody ManagerInfo info) {

		Result result = new Result<>(StatusCodes.OK, true);
		String token = request.getHeader("X-Auth-Token");
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");
		info.setId(id);
		result = managerService.update(info);
		return result;
	}

	/**
	 * currentManager:根据 token 查询用户信息 <br/>
	 * 
	 * @param request
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/current", method = RequestMethod.POST)
	@ResponseBody
	public Result currentManager(HttpServletRequest request) {
		String token = request.getHeader("X-Auth-Token");
		return managerService.getManagerInfoByToken(token);
	}


}