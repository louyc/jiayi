package com.lifelight.web.controller.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.ApiUserInfo;
import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.ApiUserInvoiceVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.common.tools.util.DateTimeUtil;
import com.lifelight.common.tools.util.IdcardUtils;
import com.lifelight.common.tools.util.ValidateUtil;
import com.lifelight.dubbo.service.ApiUserInfoService;
import com.lifelight.dubbo.service.ApiUserInvoiceService;
import com.lifelight.dubbo.service.ManagerService;
import com.lifelight.dubbo.service.QrcodeRelService;
import com.lifelight.dubbo.service.WeixinConfigureService;
import com.lifelight.dubbo.service.XlPersonDocumentService;
import com.lifelight.web.service.TokenService;
import com.lifelight.web.util.RandomCode;
import com.lifelight.web.util.SMSUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/apiUser")
public class ApiUserController {

	private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);

	@Reference
	private ManagerService managerService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RedisTool redisTool;
	@Autowired
	private RandomCode randomCode;

	@Reference
	private ApiUserInfoService apiUserInfoService;
//	@Reference
//	private ApiContractedUserService apiContractedUserService;
	@Reference
	private XlPersonDocumentService personDocumentService;

	@Reference
	private QrcodeRelService qrcodeRelService;
	@Reference
	private ApiUserInvoiceService apiUserInvoiceService;
	@Reference
	private WeixinConfigureService weixinConfigureService;

	/**
	 * login:用户登录 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @return Result
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "openId", required = false) String openId,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "randCode", required = false) String randCode,
			@RequestParam(value = "headimgurl", required = false) String headimgurl) throws IOException {
		logger.info("ApiUserController.login start,openId={}", openId + " mobile:" + mobile + "  city:" + city);
		if (!StringUtil.isEmpty(city)) {
			city = URLDecoder.decode(city);
			logger.info("  city:" + city);
		}
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			response.sendRedirect("/m/login.html?mobile=" + mobile + "&message=" + URLEncoder.encode("获取微信配置信息失败"));
			return;
		}
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			String managerId = "";
			ManagerInfo manager = null;
			if (ValidateUtil.checkMobileNumber(mobile)) {
				// 手机号登录，校验验证码
				String randCodeRedis = redisTool.getMapField("apiUserCertified", mobile, String.class);
				if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
					response.sendRedirect("/m/login.html?mobile=" + mobile + "&message=" + URLEncoder.encode("验证码错误！")
							+ "&randCode=" + randCode);
					return;
				}
				manager = managerService.getManagerInfoByMobile(mobile,Integer.parseInt(platformId));
			} else if (IdcardUtils.validateCard(mobile)) {
				// 身份证号，直接登录
				manager = managerService.getManagerInfoByIDcard(mobile,Integer.parseInt(platformId));
				if(null == manager) {//可能是之前手机签约用户用身份证登录
					XlPersonDocumentVO xlp = new XlPersonDocumentVO();
					xlp.setCardNum(mobile);
					Result re = personDocumentService.querySignUserByEntityCard(xlp);
					if(null != re.getData()) {
						ApiUserInfoVO auiv = (ApiUserInfoVO)re.getData();
						manager = managerService.getManagerInfoByPrimaryKey(auiv.getManagerId());
						if(null!=manager) {
							ManagerInfo managerNew = new ManagerInfo();
							managerNew.setId(auiv.getManagerId());
							managerNew.setIdCode(auiv.getCardId());
							managerService.update(managerNew);
						}
					}
				}
			}else {
				response.sendRedirect(
						"/m/login.html?mobile=" + mobile + "&message=" + URLEncoder.encode("身份证格式错误"));
				return;
			}
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				managerId = manager.getId();
				result = apiUserInfoService.getApiUserInfoById(manager.getId(), mobile, openId, headimgurl, url, city,Integer.parseInt(platformId));
				// cookie 中保存用户id
				tokenService.setCookieByUserId(response, managerId);
				logger.info("保存结果：{}", result.getData());
				// 用户注册更新关注数据
			} else {
				manager = new ManagerInfo();
				if (ValidateUtil.checkMobileNumber(mobile))
					manager.setMobile(mobile);
				else if (IdcardUtils.validateCard(mobile))
					manager.setIdCode(mobile);
				manager.setPassword("000000");
				manager.setPlatformId(Integer.parseInt(platformId));
				result = managerService.regist(manager);
				if (result.getStatus() == StatusCodes.OK) {
					// 账户中心注册成功,微信后台增加用户
					managerId = result.getData().toString();
					result = apiUserInfoService.getApiUserInfoById(managerId, mobile, openId, headimgurl, url, city,Integer.parseInt(platformId));
					// cookie 中保存用户id
					tokenService.setCookieByUserId(response, managerId);
					logger.info("保存结果：{}", result.getData());
					// 用户注册更新关注数据
				} else {
					response.sendRedirect(
							"/m/login.html?mobile=" + mobile + "&message=" + URLEncoder.encode("账户中心注册失败"));
					return;
				}
			}
			if (url.contains("?")) {
				url = url + "&managerId=" + managerId;
			} else {
				url = url + "?managerId=" + managerId;
			}
			ApiUserInfoVO userVO = (ApiUserInfoVO) result.getData();
			userVO.setManagerId(managerId);
			userVO.setOpenId(openId);
			userVO.setPlatformId(Integer.parseInt(platformId));
			qrcodeRelService.firstLoginUser(userVO);
		} catch (Exception e) {
			logger.error("{}", e);
			response.sendRedirect("/m/login.html?mobile=" + mobile + "&message=" + URLEncoder.encode("登录失败"));
			return;
		}
		response.sendRedirect("/m/jump.html?url=" + url);
		return;
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
	public Result update(HttpServletRequest request, @RequestBody ApiUserInfoVO info, HttpServletResponse response) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (!StringUtil.isEmpty(info.getMobile())) {
			if (!StringUtil.isEmpty(info.getRandCode())) {
				ApiUserInfo ui = apiUserInfoService.getCurrentUserInfo(info.getManagerId());
				if (!ui.getMobile().equals(info.getMobile())) {
					String randCodeRedis = redisTool.getMapField("apiUserUpdateMobile", info.getMobile(), String.class);
					String randCode = info.getRandCode();
					if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
						result = new Result(StatusCodes.OK, false, new ResultCode("FAIL", "验证码校验失败"));
						return result;
					}
				}
			}
			String managerId = "";
			//查询当前域名微信配置表id
			logger.info("域名：：：："+request.getServerName());
			String domainName = request.getServerName();
			String platformId = redisTool.get(domainName);
			if(StringUtil.isEmpty(platformId)) {
				logger.info("获取微信配置信息错误****************");
				return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
			}
			ManagerInfo manager = managerService.getManagerInfoByMobile(info.getMobile(),Integer.parseInt(platformId));
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				managerId = manager.getId();
				logger.info("保存结果：{}", managerId);
				// cookie 中保存用户id
				tokenService.setCookieByUserId(response, managerId);
			} else {
				manager = new ManagerInfo();
				manager.setMobile(info.getMobile());
				manager.setPassword("000000");
				manager.setPlatformId(Integer.parseInt(platformId));
				result = managerService.regist(manager);
				if (result.getStatus() == StatusCodes.OK) {
					// 账户中心注册成功,微信后台增加用户
					managerId = result.getData().toString();
					// cookie 中保存用户id
					tokenService.setCookieByUserId(response, managerId);
					logger.info("保存结果：{}", result.getData());
				} else {
					result = new Result(StatusCodes.OK, false, new ResultCode("FAIL", "账户系统注册用户失败"));
					return result;
				}
			}
			info.setAccountManagerId(managerId);
			result = apiUserInfoService.updateApiUserInfo(info);
			return result;
		}
		return apiUserInfoService.updateApiUserInfo(info);
	}

	/**
	 * 微信端用户 更新发票信息
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/updateInvoice")
	@ResponseBody
	public Result updateInvoice(HttpServletRequest request, @RequestBody ApiUserInvoiceVO info,
			HttpServletResponse response) {
		logger.info("发票信息：{}", info.getManagerId() + " " + info.getTaxpayer());
		Result result = apiUserInvoiceService.updateInvoice(info);
		return result;
	}

	/**
	 * 微信端用户登录 发送验证码
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/randCode")
	@ResponseBody
	public Result sendRandomCode(HttpServletRequest request, @RequestParam("mobile") String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("apiUserCertified", mobile, String.class);
		result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
		if (StringUtils.isEmpty(randCode)) {
			// 生成随机验证码
			randCode = randomCode.createRandomByFlag(true, 6, true);
			// send
			String weixinId = redisTool.get(request.getServerName());
			if (StringUtil.isEmpty(weixinId)) {
				logger.info("获取微信配置信息错误****************");
				return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
			}
			WeixinConfigure configure = weixinConfigureService.selectWeixinConfigureBykey(weixinId);
			if (null != configure && !StringUtil.isEmpty(configure.getWeixinName())) {
				String msg = "【" + configure.getWeixinName() + "】验证码：" + randCode + "，打死都不要告诉别人哦！";
				SMSUtil.sendCode(mobile, msg);
			}
			// 存入缓存
			// 设置失效时间，验证码当天有效
			redisTool.addMap("apiUserCertified", mobile, randCode, DateTimeUtil.getNextFirstTime());
			result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,当天有效！"));
		}
		return result;
	}

	/**
	 * 微信端用户修改电话号 发送验证码
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/updateRandCode")
	@ResponseBody
	public Result sendUpdateMobileRandomCode(HttpServletRequest request, @RequestParam("mobile") String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("apiUserUpdateMobile", mobile, String.class);
		result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
		if (StringUtils.isEmpty(randCode)) {
			// 生成随机验证码
			randCode = randomCode.createRandomByFlag(true, 6, true);
			// send
			String weixinId = redisTool.get(request.getServerName());
			if (StringUtil.isEmpty(weixinId)) {
				logger.info("获取微信配置信息错误****************");
				return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
			}
			WeixinConfigure configure = weixinConfigureService.selectWeixinConfigureBykey(weixinId);
			if (null != configure && !StringUtil.isEmpty(configure.getWeixinName())) {
				String msg = "【" + configure.getWeixinName() + "】验证码：" + randCode + "，打死都不要告诉别人哦！";
				SMSUtil.sendCode(mobile, msg);
			}
			// 存入缓存
			// 设置失效时间，验证码5分钟内有效
			redisTool.addMap("apiUserUpdateMobile", mobile, randCode, DateTimeUtil.modifyMinutes(new Date(), 1));
			result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,1分钟内有效！"));
		}
		return result;
	}

	/**
	 * 添加子账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addSubAccount")
	@ResponseBody
	public Result createSubAccount(HttpServletRequest request, @RequestBody ApiUserInfoVO userInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		String managerId = "";
		ManagerInfo manager = null;
		logger.info("***************mobile::"+userInfo.getMobile()+"     cardId::"+userInfo.getCardId());
		logger.info("managerId::"+userInfo.getManagerId());
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String platformId = redisTool.get(domainName);
		if(StringUtil.isEmpty(platformId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
		}
		if (!(StringUtil.isEmpty(userInfo.getMobile()) && StringUtil.isEmpty(userInfo.getCardId()))) {
			if (!StringUtil.isEmpty(userInfo.getMobile()) && ValidateUtil.checkMobileNumber(userInfo.getMobile())) {
				// 手机号登录，校验验证码
				String mobile = userInfo.getMobile();
				if (!StringUtil.isEmpty(userInfo.getRandCode())) {
					ApiUserInfo ui = apiUserInfoService.getCurrentUserInfo(userInfo.getManagerId());
					if (!ui.getMobile().equals(mobile)) {
						String randCodeRedis = redisTool.getMapField("apiUserUpdateMobile", userInfo.getMobile(),
								String.class);
						String randCode = userInfo.getRandCode();
						if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
							return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "验证码校验失败"));
						}
					}else {
						return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "添加的子账户和当前账户手机号重复"));
					}
				}else {
					return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "验证码未输入"));
				}
				manager = managerService.getManagerInfoByMobile(mobile,Integer.parseInt(platformId));
				if (null != manager && manager.getId() != null) { // 已在账户中心注册
					managerId = manager.getId();
					//手机号   并且身份证没问题
					if(!StringUtil.isEmpty(userInfo.getCardId()) && IdcardUtils.validateCard(userInfo.getCardId())) {
						manager.setIdCode(userInfo.getCardId());
						managerService.update(manager);//更新身份证信息
					}
					return apiUserInfoService.addSubAccount(userInfo, managerId,Integer.parseInt(platformId));
				} else {
					manager = new ManagerInfo();
					manager.setMobile(mobile);
					manager.setPassword("000000");	
					if(!StringUtil.isEmpty(userInfo.getCardId()) && IdcardUtils.validateCard(userInfo.getCardId())) {
						manager.setIdCode(userInfo.getCardId());
					}
					manager.setPlatformId(Integer.parseInt(platformId));
					result = managerService.regist(manager);
					if (result.getStatus() == StatusCodes.OK) {
						// 账户中心注册成功,微信后台增加用户
						managerId = result.getData().toString();
						return apiUserInfoService.addSubAccount(userInfo, managerId,Integer.parseInt(platformId));
					} else {
						return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "账户中心注册失败"));
					}
				}
			}
			if (!StringUtil.isEmpty(userInfo.getCardId()) && IdcardUtils.validateCard(userInfo.getCardId())
					&& (StringUtil.isEmpty(userInfo.getMobile())|| !ValidateUtil.checkMobileNumber(userInfo.getMobile()))) {
				// 身份证号，直接登录
				String cardId = userInfo.getCardId();
				manager = managerService.getManagerInfoByIDcard(cardId,Integer.parseInt(platformId));
				if(null == manager) {//可能是之前手机签约用户用身份证登录
					XlPersonDocumentVO xlp = new XlPersonDocumentVO();
					xlp.setCardNum(cardId);
					Result re = personDocumentService.querySignUserByEntityCard(xlp);
					if(null != re.getData()) {
						ApiUserInfoVO auiv = (ApiUserInfoVO)re.getData();
						manager = managerService.getManagerInfoByPrimaryKey(auiv.getManagerId());
						if(null!=manager) {
							ManagerInfo managerNew = new ManagerInfo();
							managerNew.setId(auiv.getManagerId());
							managerNew.setIdCode(auiv.getCardId());
							managerService.update(managerNew);
						}
					}
				}
				if (null != manager && manager.getId() != null) { // 已在账户中心注册
					managerId = manager.getId();
					if(managerId.equals(userInfo.getManagerId())) {
						return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "添加的子账户身份证与当前账户身份证相同"));
					}
					return apiUserInfoService.addSubAccount(userInfo, managerId,Integer.parseInt(platformId));
				} else {
					manager = new ManagerInfo();
					manager.setIdCode(userInfo.getCardId());
					manager.setPassword("000000");	
					result = managerService.regist(manager);
					if (result.getStatus() == StatusCodes.OK) {
						// 账户中心注册成功,微信后台增加用户
						managerId = result.getData().toString();
						return apiUserInfoService.addSubAccount(userInfo, managerId,Integer.parseInt(platformId));
					} else {
						return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "账户中心注册失败"));
					}
				}
			}
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "输入信息有误"));
		} else {
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "身份证或者手机号必输入一个"));
//			managerId = UUID.randomUUID().toString();
//			return apiUserInfoService.addSubAccount(userInfo, managerId);
		}

	}

	/**
	 * 查询所有相关账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/querySubAccount")
	@ResponseBody
	public Result queryAllSubAccount(HttpServletRequest request, @RequestParam("managerId") String managerId) {

		return apiUserInfoService.getAllApiUserInfo(managerId);
	}

	/**
	 * 查询父账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryParentAccount")
	@ResponseBody
	public Result queryParentAccount(HttpServletRequest request, @RequestParam("managerId") String managerId) {

		return apiUserInfoService.getParentUserInfo(managerId);
	}

	/**
	 * 查询当前账户信息
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/queryAccount")
	@ResponseBody
	public Result queryAccount(HttpServletRequest request, @RequestParam("managerId") String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(managerId);
		List<ApiUserInvoiceVO> invoiceList = apiUserInvoiceService.queryInvoiceByManagerId(managerId);
		JSONObject jsonObject = JSONObject.fromObject(user);
		ApiUserInfoVO apiUser = (ApiUserInfoVO) JSONObject.toBean(jsonObject, ApiUserInfoVO.class);
		if (null != invoiceList && invoiceList.size() > 0) {
			apiUser.setInvoiceList(invoiceList);
		}
		result.setData(apiUser);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		return result;

	}

	/**
	 * 切换当前账户信息
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/changeAccount")
	@ResponseBody
	public Result changeAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("managerId") String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfo user = apiUserInfoService.changeAccount(managerId);
		tokenService.setCookieByUserId(response, managerId);
		List<ApiUserInvoiceVO> invoiceList = apiUserInvoiceService.queryInvoiceByManagerId(managerId);
		JSONObject jsonObject = JSONObject.fromObject(user);
		ApiUserInfoVO apiUser = (ApiUserInfoVO) JSONObject.toBean(jsonObject, ApiUserInfoVO.class);
		apiUser.setInvoiceList(invoiceList);
		result.setData(apiUser);
		result.setToken(redisTool.get(managerId));
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		return result;

	}

	/**
	 * 删除子账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/delSubAccount")
	@ResponseBody
	public Result removeSubAccount(HttpServletRequest request, @RequestParam("managerId") String managerId) {
		return apiUserInfoService.deleteApiUserInfo(managerId);
	}

	/**
	 * 查询所有角色关系
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryRole")
	@ResponseBody
	public Result queryAllUserRole(HttpServletRequest request) {
		return apiUserInfoService.queryAllApiRoleInfo();
	}

}
