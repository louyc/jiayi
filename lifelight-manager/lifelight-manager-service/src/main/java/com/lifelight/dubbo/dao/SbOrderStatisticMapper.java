/**
 * 订单统计接口
 */
package com.lifelight.dubbo.dao;

import java.util.List;

import com.lifelight.api.dto.SbOrderStatisticDTO;
import com.lifelight.api.vo.SbOrderDutiesVO;

public interface SbOrderStatisticMapper {
	
	/**
	 * 各类型维度 履约情况
	 * @return
	 */
	List<SbOrderDutiesVO> sbKeywordsDutiesList();
	
	/**
	 * 机构维度 履约情
	 * @return
	 */
	List<SbOrderDutiesVO> sbOrgDutiesList(SbOrderStatisticDTO entity);
	
	/**
	 * 签约人数合计
	 */
	Integer signUserCount();
	
	/**
	 * 机构维度 用户签约人数统计
	 * @return
	 */
	List<SbOrderDutiesVO> signOrgUserList();
	
	/**
	 * 履约人数合计
	 * @return
	 */
	Integer dutiesUserCount();
	
}
