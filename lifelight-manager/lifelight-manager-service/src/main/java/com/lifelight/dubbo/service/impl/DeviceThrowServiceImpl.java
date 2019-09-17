package com.lifelight.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.DeviceThrow;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DeviceThrowMapper;
import com.lifelight.dubbo.service.DeviceThrowService;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DeviceThrowServiceImpl implements DeviceThrowService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceThrowServiceImpl.class);

	@Autowired
	private DeviceThrowMapper deviceThrowMapper;

	/**
	 * 设备 用户 投放记录
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result insertDeviceThrow(String managerId, Integer deviceId, String deviceLocation) {
		logger.debug("用户id" + managerId);
		Result result = new Result<>(StatusCodes.OK, true);
		Date date = new Date();

		DeviceThrow deviceThrow = new DeviceThrow();

		deviceThrow.setCreateTime(date);
		deviceThrow.setDeviceId(deviceId);
		deviceThrow.setDeviceLocation(deviceLocation);
		deviceThrow.setDeviceLocationid(managerId);
		deviceThrow.setUpdateTime(date);

		int num = 0;
		num = deviceThrowMapper.insertSelective(deviceThrow);
		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}
}