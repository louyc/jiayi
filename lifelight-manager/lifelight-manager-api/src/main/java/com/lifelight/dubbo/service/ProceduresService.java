package com.lifelight.dubbo.service;

import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface ProceduresService {

	/**
	 * getProceduresByToken:根据token查询用户所有流程. <br/>
	 * 
	 * @param token,title
	 * @return Result
	 * @author hua
	 */
	PaginatedResult getProceduresByToken(String token, String title, String pageNumber,
			String pageSize);

	/**
	 * 
	 * @param token
	 * @param userId
	 * @param doctorId
	 * @param orderId
	 * @param procedureId
	 * @return
	 */
	Result getProcedureDialog(String token, String userId, String doctorId, String orderId,
			String procedureId);

}
