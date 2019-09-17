package com.lifelight.dubbo.service;

import com.lifelight.api.entity.XlPersonSchedule;
import com.lifelight.api.vo.XlPersonContractVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.api.vo.XlPersonScheduleVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface XlPersonScheduleService {

	/**
	 * 创建个人检查进度信息
	 * 
	 * @param message
	 * @return
	 */
	Result createPersonSchedule(XlPersonSchedule contract);
	
	/**
	 * 删除个人检查进度信息
	 * 
	 * @param id
	 * @return
	 */
	Result deletePersonSchedule(String id);

	/**
	 * 修改个人检查进度信息
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updatePersonSchedule(XlPersonScheduleVO contract);
	
	/**
	 * 查询单人检查进度信息 list
	 * 
	 * @param message
	 * @return
	 */
	Result selectPersonSchedule(String documentId,Integer platformId);
	/**
	 * 查询单人检查进度信息
	 * 
	 * @param message
	 * @return
	 */
	Result queryOneScheduleItem(String id);

	/**
	 * 查询多人检查进度 分页
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<XlPersonDocumentVO> selectPersonScheduleListPage( XlPersonDocumentVO documentVO);
	
	/**
	 * 修改个人检查审核状态
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updateAuditStatus(XlPersonContractVO xlp);
	/**
	 * 查询个人进度
	 * 
	 * @param id
	 * @return
	 */
	Result queryOneSchedule(String id);
	
}