package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.common.result.Result;

public interface ProcedureMessageService {
	
	/**
	 * 获取未发送消息列表
	 * 
	 * @param procedureMessage
	 * @return
	 */
	Result<List<ProcedureMessage>> getUnsentList(ProcedureMessage procedureMessage);

}