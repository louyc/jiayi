package com.lifelight.dubbo.service;

import com.lifelight.api.entity.PersonalTailorMessage;
import com.lifelight.api.vo.PersonalTailorMessageVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface PersonalTailorMessageService {

	/**
	 * 添加
	 * @param personalTailorMessage
	 * @return
	 */
	Result add(PersonalTailorMessage personalTailorMessage);
	
	/**
	 * 查询
	 * @param personalTailorMessageVO
	 * @return
	 */
	PaginatedResult queryByConditions(PersonalTailorMessageVO personalTailorMessageVO);
	
	/**
	 *  阅读
	 * @param id
	 * @return
	 */
	Result read(Integer id);
	
	/**
	 * 处理
	 * @param id
	 * @return
	 */
	Result handle(Integer id);
}
