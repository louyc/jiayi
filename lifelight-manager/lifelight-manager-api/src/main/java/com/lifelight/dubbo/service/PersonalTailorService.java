package com.lifelight.dubbo.service;

import com.lifelight.api.entity.PersonalTailor;
import com.lifelight.api.vo.PersonalTailorVO;
import com.lifelight.common.result.Result;

public interface PersonalTailorService {

	/**
	 * getPersonalTailor:查询私人订制. <br/>
	 * 
	 * @param id
	 *            主键id
	 * @return Result
	 * @author lyc
	 */
	Result getPersonalTailor(String id);

	/**
	 * getPersonalTailor:查询私人订制. <br/>
	 * 
	 * @param personalTailorVO
	 * @return Result
	 * @author lyc
	 */
	Result getPersonalTailor(PersonalTailorVO personalTailorVO);

	/**
	 * personalTailorCreate 添加私人订制
	 * 
	 * @param personalTailor
	 * @return
	 */
	Result personalTailorCreate(PersonalTailor personalTailor);

	/**
	 * personalTailorUpdate 更新私人订制
	 * 
	 * @param personalTailor
	 * @return
	 */
	Result personalTailorUpdate(PersonalTailor personalTailor);

	/**
	 * personalTailorRemove 删除私人订制
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	Result personalTailorRemove(String id);

}