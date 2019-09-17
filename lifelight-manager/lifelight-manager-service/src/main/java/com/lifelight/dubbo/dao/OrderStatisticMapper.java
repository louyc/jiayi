/**
 * 订单统计接口
 */
package com.lifelight.dubbo.dao;

import java.util.List;

import com.lifelight.api.dto.OrderDutiesDTO;
import com.lifelight.api.dto.OrderStatisticDTO;
import com.lifelight.api.vo.OrderCountVO;
import com.lifelight.api.vo.OrderDutiesVO;
import com.lifelight.api.vo.OrderPurchaseVO;
import com.lifelight.api.vo.OrderStatisticVO;

public interface OrderStatisticMapper {

	List<OrderPurchaseVO> orderPurchaseInfoListPage(OrderStatisticDTO entity);
	
	List<OrderPurchaseVO> orderPurchaseInfo(OrderStatisticDTO entity);
	
	List<OrderStatisticVO> orderPurchaseDetailListPage(OrderStatisticDTO entity);
	
	List<OrderStatisticVO> orderPurchaseDetail(OrderStatisticDTO entity);
	
	List<OrderDutiesVO> orderDutiesListPage(OrderDutiesDTO entity);
	
	List<OrderDutiesVO> orderDuties(OrderDutiesDTO entity);
	
	OrderCountVO orderPurchaseTotalCount(OrderStatisticDTO entity);
}
