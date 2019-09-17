package com.lifelight.web.controller;

import java.util.Date;
import java.util.List;

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
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.DeviceType;
import com.lifelight.api.entity.ImplementSet;
import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.entity.WeixinHomepageIcon;
import com.lifelight.api.entity.WeixinMenu;
import com.lifelight.api.entity.WeixinTemplate;
import com.lifelight.api.vo.WeixinMenuVO;
import com.lifelight.api.vo.WeixinTemplateVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.BackstageMenuInfoService;
import com.lifelight.dubbo.service.BackstageUserInfoService;
import com.lifelight.dubbo.service.DeviceTypeService;
import com.lifelight.dubbo.service.ImplementSetService;
import com.lifelight.dubbo.service.ManagerService;
import com.lifelight.dubbo.service.TemplateManagementService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/weixinConfigure")
public class WeixinConfigureController {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinConfigureController.class);
	@Reference
	private WeixinConfigureService weixinConfigureService;
	@Autowired
	private RedisTool redisTool;
	@Reference
	private ManagerService managerService;
	@Reference
	private BackstageMenuInfoService backstageMenuInfoService;
	@Reference
	private BackstageUserInfoService backstageUserInfoService;
	@Reference
	private TemplateManagementService templateManagementService;
	@Reference
	private DeviceTypeService deviceTypeService;
	@Reference
	private ImplementSetService implementService;
	
	@RequestMapping("/createWeixinConfigure")
	@ResponseBody
	public Result createWeixinConfigure(HttpServletRequest request,@RequestBody WeixinConfigure weixinConfigure) {

		Result result = new Result<>(StatusCodes.OK, true);
		result = weixinConfigureService.createWeixinConfigure(weixinConfigure);
		if(result.getResultCode().getCode().equals("SUCCESS")) {
			weixinConfigure = (WeixinConfigure)result.getData();
			String mobile = "13240130405";
			ManagerInfo manager = new ManagerInfo();
			manager.setMobile(mobile);
			manager.setPassword("qwer1234");
			manager.setPlatformId(weixinConfigure.getId());
			result = managerService.regist(manager);
			if (result.getStatus() == StatusCodes.OK) {
				// 账户中心注册成功
				BackstageUserInfo backstageUserInfo = new BackstageUserInfo();
				backstageUserInfo.setMobile(mobile);
				backstageUserInfo.setManagerId(result.getData().toString());
				backstageUserInfo.setPlatformId(weixinConfigure.getId());
				backstageUserInfo.setRoleId(4);
				backstageUserInfo.setName("管理员");
				backstageUserInfo.setUserStatus(1);
				// 初始化信息  
				// 注册管理员用户   添加角色菜单关系    添加默认基本信息和签约信息模板
				backstageUserInfoService.backstageUserRegist(backstageUserInfo);
				result = backstageMenuInfoService.backstageRoleMenuRelCopy(weixinConfigure.getId());
				templateManagementService.createAllTemplateManagement(weixinConfigure.getId());
				// 添加设备deviceType
				DeviceType deviceType = new DeviceType();
				deviceType.setCreateTime(new Date());
				deviceType.setPlatformId(weixinConfigure.getId());
				deviceType.setDeviceTypeName("一体机");
				deviceType.setIsdel("F");
				deviceTypeService.insertDeviceType(deviceType);
				deviceType.setDeviceTypeName("单片机");
				deviceTypeService.insertDeviceType(deviceType);
				// 添加运营设置 内容区
				ImplementSet implementSet = new ImplementSet();
				implementSet.setName("私人定制");
				implementSet.setPlatformId(weixinConfigure.getId());
				implementSet.setType(3);
				implementService.implementSetInfoCreate(implementSet);
				implementSet.setName("健康管理");
				implementService.implementSetInfoCreate(implementSet);
				if(result.getResultCode().getCode().equals("SUCCESS")) {
					return new Result(StatusCodes.OK, false, new ResultCode("SUCCESS", "添加成功"));
				}else {
					return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "添加菜单关系失败"));
				}
			} else {
				return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "添加超级用户失败"));
			}
		}
		return result;
	}

	@RequestMapping("/deleteWeixinConfigure")
	@ResponseBody
	public Result deleteWeixinConfigure(@RequestParam("id") String id,
			@RequestParam("inUse") String inUse) {
		return weixinConfigureService.deleteWeixinConfigure(id,inUse);
	}

	@RequestMapping("/updateWeixinConfigure")
	@ResponseBody
	public Result updateWeixinConfigure(@RequestBody WeixinConfigure weixinConfigure) {
		return weixinConfigureService.updateWeixinConfigure(weixinConfigure);
	}

	/**
	 * 分页 样例 showCount 分页大小 currentPage 当前页数
	 * 查询所有微信配置
	 * getPageWinxinConfigure
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping("/getPageWinxinConfigure")
	@ResponseBody
	public Result getPageWinxinConfigure( HttpServletRequest request,
			@RequestBody WeixinConfigure weixinConfigure) {
		List<WeixinConfigure> list= weixinConfigureService.selectWeixinConfigureAll();
		if(null!=list && list.size()>0) {
			for(WeixinConfigure wc: list) {
				if(wc.getInUse().equals("1")) {
					redisTool.put(wc.getDomainName().trim(), String.valueOf(wc.getId()));
				}else {
					redisTool.put(wc.getDomainName().trim(), "");
				}
			}
		}
		weixinConfigure.setInUse("1");
		return weixinConfigureService.selectWeixinConfigureListPage(weixinConfigure);
	}

	/**
	 * 查询所有微信菜单
	 * getPageWinxinMenu
	 * @param message
	 * @return
	 */
	@RequestMapping("/getPageWinxinMenu")
	@ResponseBody
	public Result getPageWinxinMenu(@RequestBody WeixinMenu weixinMenu) {
		return weixinConfigureService.selectPageWinxinMenuListPage(weixinMenu);
	}
	
	/**
	 * addWeixinMenu:微信菜单添加<br/>
	 * 
	 * @param request
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addWeixinMenu")
	@ResponseBody
	public Result addWeixinMenu(HttpServletRequest eixinMenuVO, HttpServletResponse response,
			@RequestBody WeixinMenuVO menuVO) {
		logger.info("weixinConfigure.addWeixinMenu start,Parameter::"+menuVO.getParentId()+"   menuName:"+
			menuVO.getMenuName()+"  weixinId:" +menuVO.getWeixinId() );
		//查询此公众号下 菜单是否已存在
		WeixinMenu menu = weixinConfigureService.getWeixinMenuInfoByNameAndParent(menuVO);
		if (null == menu) {
			return weixinConfigureService.weixinMenuCreate(menuVO);
		} else {
			return new Result(StatusCodes.OK, false, new ResultCode("MENU_FAIL", "菜单已存在！"));
		}
	}
	
	/**
	 * modifyWeixinMenu:菜单修改<br/>
	 * 
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/modifyWeixinMenu")
	@ResponseBody
	public Result modifyWeixinMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WeixinMenuVO menuVO) {
		logger.info("modifyWeixinMenu start,Parameter::"+menuVO.getMenuName());
		return weixinConfigureService.modifyWeixinMenu(menuVO);
	}
	
	/**
	 * removeWeixinMenu:菜单删除<br/>
	 * 
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/removeWeixinMenu")
	@ResponseBody
	public Result removeWeixinMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") String id) {
		logger.info("removeWeixinMenu start,id={}", id);
		return weixinConfigureService.removeWeixinMenu(Integer.parseInt(id));
	}
	
	/**
	 * 查询所有微信摸版id
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/getPageWinxinTemplate")
	@ResponseBody
	public Result getPageWinxinTemplate(@RequestBody WeixinTemplate weixinTemplate) {
		return weixinConfigureService.selectPageWinxinTemplateListPage(weixinTemplate);
	}
	
	/**
	 * 修改微信摸版id
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/updateWinxinTemplate")
	@ResponseBody
	public Result updateWinxinTemplate(@RequestBody WeixinTemplateVO weixinTemplate) {
		return weixinConfigureService.updateWinxinTemplate(weixinTemplate);
	}
	
	/**
	 * 删除微信摸版id
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/delWinxinTemplate")
	@ResponseBody
	public Result delWinxinTemplate(@RequestParam("id") String id) {
		return weixinConfigureService.delWinxinTemplate(id);
	}
	/**
	 * 新增微信摸版id
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/addWinxinTemplate")
	@ResponseBody
	public Result addWinxinTemplate(@RequestBody WeixinTemplateVO weixinTemplate) {
		return weixinConfigureService.addWinxinTemplate(weixinTemplate);
	}
	
	
	
	/**
	 * 查询所有微信首页图标
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/getWinxinIcons")
	@ResponseBody
	public Result getWinxinIcons(HttpServletRequest request,@RequestBody WeixinHomepageIcon weixinHomepageIcon ) {
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}		
		weixinHomepageIcon.setPlatformId(Integer.parseInt(weixinId));
		return weixinConfigureService.getWinxinIcons(weixinHomepageIcon);
	}
	/**
	 * 新增微信首页图标
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/addWinxinIcon")
	@ResponseBody
	public Result addWinxinIcons(HttpServletRequest request,@RequestBody WeixinHomepageIcon weixinHomepageIcon ) {
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		weixinHomepageIcon.setPlatformId(Integer.parseInt(weixinId));
		return weixinConfigureService.addWinxinIcon(weixinHomepageIcon);
	}
	/**
	 * 修改微信首页图标
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/updateWinxinIcon")
	@ResponseBody
	public Result updateWinxinIcons(@RequestBody WeixinHomepageIcon weixinHomepageIcon ) {
		return weixinConfigureService.updateWinxinIcon(weixinHomepageIcon);
	}
	/**
	 * 删除微信首页图标
	 * getPageWinxinTemplate
	 * @param message
	 * @return
	 */
	@RequestMapping("/deleteWinxinIcon")
	@ResponseBody
	public Result deleteWinxinIcons(@RequestBody WeixinHomepageIcon weixinHomepageIcon ) {
		return weixinConfigureService.deleteWinxinIcon(weixinHomepageIcon);
	}
}