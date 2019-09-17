package com.lifelight.dubbo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.api.entity.ManagerInfoExample;
import com.lifelight.api.entity.ManagerInfoExample.Criteria;
import com.lifelight.api.vo.ManagerInfoVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.util.JWTTokenUtil;
import com.lifelight.common.tools.util.Md5Util;
import com.lifelight.common.tools.util.ValidateUtil;
import com.lifelight.dubbo.dao.ManagerInfoMapper;
import com.lifelight.dubbo.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {

	private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

	public static ResultCode AccountNull = new ResultCode("ACCOUNT_NULL", "账号不存在！");
	public static ResultCode PasswordError = new ResultCode("PWD_ERROR", "密码错误！");
	public static ResultCode PermissionDenied = new ResultCode("PERMISSION_DENIED", "权限不足！");
	public static ResultCode ParamError = new ResultCode("PARAM_ERROR", "密码错误,参数不能为空！");
	public static ResultCode EmailExists = new ResultCode("EMAIL_EXISTS", "邮箱已注册！");
	public static ResultCode MobileExists = new ResultCode("MOBILE_EXISTS", "手机号已注册！");
	public static ResultCode IDcardExists = new ResultCode("IDCODE_EXISTS", "身份证号已注册！");

	@Autowired
	private ManagerInfoMapper managerInfoMapper;

	@Override
	public ManagerInfo getManagerInfoByPrimaryKey(String id) {
		ManagerInfo managerInfo = managerInfoMapper.selectByPrimaryKey(id);
		logger.info("开始查询用户信息，查询条件ID为:" + id);
		logger.info("查询结果：" + managerInfo.toString());
		return managerInfo;
	}

	@Override
	public ManagerInfo getManagerInfoByMobile(String mobile, int platformId) {
		logger.info("开始查询用户信息，查询条件手机号为:" + mobile);
		ManagerInfoExample example = new ManagerInfoExample();
		Criteria certeria = example.createCriteria();
		certeria.andMobileEqualTo(mobile);
		if(platformId >0) {
			certeria.andPlatformIdEqualTo(platformId);
		}
		List<ManagerInfo> list = managerInfoMapper.selectByExample(example);
		if (list.size() == 0) {
			return null;
		}
		logger.info("查询结果：" + list.get(0).toString());
		return list.get(0);
	}

	@Override
	public ManagerInfo getManagerInfoByEmail(String email, int platformId) {
		logger.info("开始查询用户信息，查询条件邮箱为:" + email);
		ManagerInfoExample example = new ManagerInfoExample();
		example.createCriteria().andEmailEqualTo(email).andPlatformIdEqualTo(platformId);
		List<ManagerInfo> list = managerInfoMapper.selectByExample(example);
		if (list.size() == 0) {
			return null;
		}
		logger.info("查询结果：" + list.get(0).toString());
		return list.get(0);
	}

	@Override
	public ManagerInfo getManagerInfoByIDcard(String idCode, int platformId) {
		logger.info("开始查询用户信息，查询条件身份证号为:" + idCode);
		ManagerInfoExample example = new ManagerInfoExample();
		example.createCriteria().andIdCodeEqualTo(idCode).andPlatformIdEqualTo(platformId);
		List<ManagerInfo> list = managerInfoMapper.selectByExample(example);
		if (list.size() == 0) {
			return null;
		}
		logger.info("查询结果：" + list.get(0).toString());
		return list.get(0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result login(String userName, String password, int platformId) {
		Result result = new Result<>(StatusCodes.OK, true);

		ManagerInfo managerInfo = null;
		// 判断账户是邮箱还是手机号
		if (ValidateUtil.checkEmail(userName))
			managerInfo = this.getManagerInfoByEmail(userName, platformId);
		else
			managerInfo = this.getManagerInfoByMobile(userName, platformId);

		if (managerInfo == null) {// 密码错误
			return new Result(StatusCodes.OK, false, AccountNull);
		}
		if (!Md5Util.getBaseMDCode(password).equals(managerInfo.getPassword())) {// 密码错误
			return new Result(StatusCodes.OK, false, PasswordError);
		} else {
			try {
				String token = JWTTokenUtil.createTokenStr(managerInfo.getId());
				result.setToken(token);
			} catch (Exception e) {
				logger.error("登录成功，生成token失败：{}", e);
			}
			result.setResultCode(new ResultCode("SUCCESS", "登录成功！"));
		}

		result.setData(managerInfo.getId());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result regist(ManagerInfo info) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null != info) {

			// UUID 作为用户id
			info.setId(UUID.randomUUID().toString());
			// 设置注册时间
			info.setCreateTime(new Date());
			info.setPassword(Md5Util.getBaseMDCode(info.getPassword()));
			// 验证用户是否已注册
			ManagerInfo managerInfo = null;
			// 判断账户是邮箱还是手机号
			if (StringUtils.isNotEmpty(info.getEmail())) {
				managerInfo = this.getManagerInfoByEmail(info.getEmail(), info.getPlatformId());
				if (null != managerInfo) {
					result = new Result(StatusCodes.OK, false, EmailExists);
					result.setData(info.getId());
					return result;
				}
			}
			if (StringUtils.isNotEmpty(info.getMobile())) {
				managerInfo = this.getManagerInfoByMobile(info.getMobile(), info.getPlatformId());
				if (null != managerInfo) {
					result = new Result(StatusCodes.OK, false, MobileExists);
					result.setData(info.getId());
					return result;
				}
			}
			if (StringUtils.isNotEmpty(info.getIdCode())) {
				managerInfo = this.getManagerInfoByIDcard(info.getIdCode(), info.getPlatformId());
				if (null != managerInfo) {
					result = new Result(StatusCodes.OK, false, IDcardExists);
					result.setData(info.getId());
					return result;
				}
			}

			// 存库
			int line = managerInfoMapper.insertSelective(info);
			if (line > 0) {
				result.setData(info.getId());
				result.setResultCode(new ResultCode("SUCCESS", "注册成功！"));
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result updatePassword(String id, String oldPassword, String newPassword) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == id || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
			return new Result<>(StatusCodes.OK, false, ParamError);
		}
		ManagerInfo managerInfo = this.getManagerInfoByPrimaryKey(id);
		if (null == managerInfo) {// 账号不存在
			return new Result<>(StatusCodes.OK, false, AccountNull);
		} else if (!Md5Util.getBaseMDCode(oldPassword).equals(managerInfo.getPassword())) {// 密码错误
			return new Result(StatusCodes.OK, false, PasswordError);
		} else {
			// 加密
			managerInfo.setPassword(Md5Util.getBaseMDCode(newPassword));
			managerInfo.setUpdateTime(new Date());
			int line = managerInfoMapper.updateByPrimaryKeySelective(managerInfo);
			if (line > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "密码修改成功！"));
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result forgetPassword(String userName, String password, int platformId) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			return new Result<>(StatusCodes.OK, false, ParamError);
		}
		ManagerInfo managerInfo = null;
		// 判断账户是邮箱还是手机号
		if (ValidateUtil.checkEmail(userName))
			managerInfo = this.getManagerInfoByEmail(userName, platformId);
		else
			managerInfo = this.getManagerInfoByMobile(userName, platformId);

		if (managerInfo == null) {// 账号不存在
			return new Result(StatusCodes.OK, false, AccountNull);
		}

		// 修改密码
		managerInfo.setPassword(Md5Util.getBaseMDCode(password));
		managerInfo.setUpdateTime(new Date());
		int line = managerInfoMapper.updateByPrimaryKeySelective(managerInfo);
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "密码修改成功！"));
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result update(ManagerInfo info) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null != info) {
			// 验证用户邮箱手机号的唯一性
			ManagerInfo managerInfo = null;
			if (StringUtils.isNotEmpty(info.getEmail())) {
				managerInfo = this.getManagerInfoByEmail(info.getEmail(), info.getPlatformId());
				if (null != managerInfo) {
					return new Result<>(StatusCodes.OK, false, EmailExists);
				}
			}
			if (StringUtils.isNotEmpty(info.getMobile())) {
				managerInfo = this.getManagerInfoByMobile(info.getMobile(), info.getPlatformId());
				if (null != managerInfo) {
					return new Result<>(StatusCodes.OK, false, MobileExists);
				}
			}

			// 存库
			info.setPassword(null);// 修改资料时，不能修改密码
			info.setUpdateTime(new Date());
			int line = managerInfoMapper.updateByPrimaryKeySelective(info);
			if (line > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
			}
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getManagerInfoByToken(String token) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 解析 token,获取用户id
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");

		ManagerInfoVO managerInfoVO = managerInfoMapper.getUserInfo(id);
		if (null != managerInfoVO) {
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
			result.setData(managerInfoVO);
		} else {
			return new Result(StatusCodes.OK, false, AccountNull);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getManagerInfoById(String id) {

		Result result = new Result<>(StatusCodes.OK, true);
		ManagerInfoVO managerInfoVO = managerInfoMapper.getUserInfo(id);
		if (null != managerInfoVO) {
			result.setData(managerInfoVO);
		}

		return result;
	}

}