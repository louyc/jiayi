package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ReportInfo;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface ReportInfoService {

	/** 根据订单id查询报表列表  */
	PaginatedResult<ReportInfo> getReportListByOrderId(ReportInfo reportInfo);
	
	/** 保存报表数据 */
	Result<ReportInfo> addReportInfo(ReportInfo reportInfo);
	
}
