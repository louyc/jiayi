package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ImplementSet;
import com.lifelight.api.vo.ImplementSetVO;
import com.lifelight.common.result.Result;

public interface ImplementSetService {

	/**
	 * getImplementSetInfoByType:根据类型查询运行设置信息. <br/>
	 * 
	 * @param type
	 * @return Result
	 * @author lyc
	 */
	Result getImplementSetInfoByType(String type,Integer platformId);

	/**
	 * implementSetInfoCreate 添加运营设置信息
	 * 
	 * @param implementSet
	 * @return
	 */
	Result implementSetInfoCreate(ImplementSet implementSet);

	/**
	 * implementSetInfoUpdate 更新运营设置信息
	 * 
	 * @param implementSet
	 * @return
	 */
	Result implementSetInfoUpdate(ImplementSet implementSet);

	/**
	 * implementSetInfoRemove 删除运营设置信息
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	Result implementSetInfoRemove(String id);

	Result getImplementFind(String type,Integer platformId);

	Result getImplementFindDetail(ImplementSet implementSet);

	Result addImplementFind(ImplementSet implementSet);

	Result addImplementDetailFind(ImplementSetVO implementSet);
}