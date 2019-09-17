package com.lifelight.web.controller;

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

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.BackstageRoleInfo;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.BackstageRoleInfoService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/role")
public class BackstageRoleInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BackstageRoleInfoController.class);

	@Reference
	private BackstageRoleInfoService backstageRoleInfoService;
	@Reference 
	private WeixinConfigureService weixinConfigureService;
	@Autowired
	private RedisTool redisTool;
	/**
	 * 获取角色管理列表（all）
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/getRoleInfo")
	@ResponseBody
	public Result getRoleInfo(HttpServletRequest request, HttpServletResponse response){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return backstageRoleInfoService.getRoleInfoArray(Integer.parseInt(weixinId));
	}

	/**
	 * 添加新角色
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/addRoleInfo")
	@ResponseBody
	public Result addRoleInfo(HttpServletRequest request,@RequestBody BackstageRoleInfo roleInfo){
		//查询当前域名微信配置表id
		/*logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}*/
//		roleInfo.setWeixinId(Integer.parseInt(weixinId));
		return backstageRoleInfoService.addRoleInfo(roleInfo);
	}

	/**
	 * 修改角色
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/updateRoleInfo")
	@ResponseBody
	public Result updateRoleInfo(@RequestBody BackstageRoleInfo roleInfo){

		return backstageRoleInfoService.updateRoleInfo(roleInfo);
	}

	/**
	 * 删除角色（未实现）
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/deleteRoleInfo")
	@ResponseBody
	public Result deleteRoleInfo(@RequestBody BackstageRoleInfo roleInfo){

		return backstageRoleInfoService.deleteRoleInfo(roleInfo);
	}

	/**
	 * 根据id查询角色信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/getRoleInfoById")
	@ResponseBody
	public Result getRoleInfoById(@RequestParam("id") Integer id){
		System.out.println(id);
		return backstageRoleInfoService.getRoleInfoById(id);
	}

	/**
	 * 用户管理中获取角色列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getRoleInfoListByUserManage")
	@ResponseBody
	public Result selectListByUserManage(HttpServletRequest request, HttpServletResponse response){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return backstageRoleInfoService.selectListByUserManage(Integer.parseInt(weixinId));
	}
}
