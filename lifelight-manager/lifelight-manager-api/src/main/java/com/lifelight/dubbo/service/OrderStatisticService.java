/**
 * 订单统计服务
 */
package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.dto.OrderDutiesDTO;
import com.lifelight.api.dto.OrderStatisticDTO;
import com.lifelight.api.vo.OrderCountVO;
import com.lifelight.api.vo.OrderDutiesVO;
import com.lifelight.api.vo.OrderPurchaseVO;
import com.lifelight.api.vo.OrderStatisticVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface OrderStatisticService {

	/**
	 * 订单购买情况统计
	 * @param entity
	 * @return
	 */
	PaginatedResult<OrderPurchaseVO> orderPurchaseCount(OrderStatisticDTO entity);
	
	/**
	 * 购买统计详情查询
	 * @param entity
	 * @return
	 */
	PaginatedResult<OrderStatisticVO> orderPurchaseDetail(OrderStatisticDTO entity);
	
	/**
	 * 合计数量统计
	 * @param entity
	 * @return
	 */
	Result<OrderCountVO> orderPurchaseTotalCount(OrderStatisticDTO entity);
	/**
	 * 履约情况统计
	 * @param entity
	 * @return
	 */
	PaginatedResult<OrderDutiesVO> orderDutiesCount(OrderDutiesDTO entity);
	
	/**
	 * 购买情况统计 查询数据列表
	 * @param entity
	 * @return
	 */
	List<OrderPurchaseVO> orderPurchaseCountList(OrderStatisticDTO entity);
	
	/**
	 * 详情查询  查询数据列表
	 * @param entity
	 * @return
	 */
	List<OrderStatisticVO> orderPurchaseDetailList(OrderStatisticDTO entity);
	
	/**
	 * 履约情况  查询数据列表
	 * @param entity
	 * @return
	 */
	List<OrderDutiesVO> orderDutiesCountList(OrderDutiesDTO entity);
	
}
