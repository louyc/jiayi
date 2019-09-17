package com.lifelight.dubbo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.ProcedureMessageMapper;
import com.lifelight.dubbo.service.ProcedureMessageService;

@Service
public class ProcedureMessageServiceImpl implements ProcedureMessageService {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureMessageServiceImpl.class);

	@Autowired
	private ProcedureMessageMapper procedureMessageMapper;

	/**
	 * 创建流程
	 * 
	 * @param procedure
	 * @return
	 */
	@Override
	public Result<List<ProcedureMessage>> getUnsentList(ProcedureMessage procedureMessage) {
		Result<List<ProcedureMessage>> result = new Result<>(StatusCodes.OK, true);

		if (null == procedureMessage) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		try {

			List<ProcedureMessage> procedureMessages = procedureMessageMapper.getUnsentList(procedureMessage);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
			result.setData(procedureMessages);
		} catch (Exception e) {
			logger.error("查询失败:{}", e);
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}
		return result;
	}

}