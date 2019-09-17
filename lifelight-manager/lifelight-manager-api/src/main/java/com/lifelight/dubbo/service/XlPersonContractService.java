package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.XlPersonContract;
import com.lifelight.api.vo.XlPersonContractVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface XlPersonContractService {

	/**
	 * 查询服务包具体内容介绍
	 * 
	 * @param message
	 * @return
	 */
	Result queryServiceContent(String id);
	
	/**
	 * 创建个人签约信息
	 * 
	 * @param message
	 * @return
	 */
	Result createPersonContract(XlPersonContract contract);
	
	/**
	 * 删除个人签约信息
	 * 
	 * @param id
	 * @return
	 */
	Result deletePersonContract(String id);

	/**
	 * 修改个人签约信息
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updatePersonContract(XlPersonContract contract);
	/**
	 * 修改签约状态
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updateContractStatus(XlPersonContractVO contract);
	
	/**
	 * 查询单人签约信息
	 * 
	 * @param message
	 * @return
	 */
	Result selectPersonContract(XlPersonContractVO contract);

	/**
	 * 查询多人签约信息 分页
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<XlPersonDocumentVO> selectPersonContractListPage(XlPersonDocumentVO documentVO);
	
	/**
	 * 查询多人签约信息 导出
	 * 
	 * @param name
	 * @return
	 */
	Result<List<XlPersonDocumentVO>> exportAllContract(XlPersonDocumentVO documentVO);
	
	/**
	 * 服务完成率统计
	 * 
	 * @param name
	 * @return
	 */
	Result serviceStatistics(String startDate,String endDate,String doctorId,String orgId);
	
	/**
	 * 项目执行进度汇总
	 * 
	 * @param name
	 * @return
	 */
	Result executeStatistics(String startDate,String endDate,String doctorId,String orgId);
	
}