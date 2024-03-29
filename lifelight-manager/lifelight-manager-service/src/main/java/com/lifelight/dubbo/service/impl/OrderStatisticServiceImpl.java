/**
 * 订单统计服务接口实现
 */
package com.lifelight.dubbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.dto.OrderDutiesDTO;
import com.lifelight.api.dto.OrderStatisticDTO;
import com.lifelight.api.vo.OrderCountVO;
import com.lifelight.api.vo.OrderDutiesVO;
import com.lifelight.api.vo.OrderPurchaseVO;
import com.lifelight.api.vo.OrderStatisticVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.OrderStatisticMapper;
import com.lifelight.dubbo.service.OrderStatisticService;

@Service
public class OrderStatisticServiceImpl implements OrderStatisticService {

	@Autowired
	private OrderStatisticMapper orderStatisticMapper;

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<OrderPurchaseVO> orderPurchaseCount(OrderStatisticDTO entity) {
		// TODO Auto-generated method stub
		// 查询数据列表
		List<OrderPurchaseVO> puchaseList = orderStatisticMapper.orderPurchaseInfoListPage(entity);
		// 查询合计结果 
		OrderCountVO orderCount = orderStatisticMapper.orderPurchaseTotalCount(entity);
		int totalCount = entity.getTotalResult();
		PaginatedResult<OrderPurchaseVO> result = new PaginatedResult<>(puchaseList, entity.getCurrentPage(), entity.getShowCount(), totalCount);
		result.setData(orderCount);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	@Override
	public PaginatedResult<OrderStatisticVO> orderPurchaseDetail(OrderStatisticDTO entity) {
		// TODO Auto-generated method stub
		List<OrderStatisticVO> puchaseDetailList = orderStatisticMapper.orderPurchaseDetailListPage(entity);
		int totalCount = entity.getTotalResult();
		PaginatedResult<OrderStatisticVO> result = new PaginatedResult<>(puchaseDetailList, entity.getCurrentPage(), entity.getShowCount(), totalCount);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	@Override
	public Result<OrderCountVO> orderPurchaseTotalCount(OrderStatisticDTO entity) {
		// TODO Auto-generated method stub
		Result<OrderCountVO> result = new Result<>(StatusCodes.OK, true);
		OrderCountVO orderCount = orderStatisticMapper.orderPurchaseTotalCount(entity);
		result.setData(orderCount);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	@Override
	public PaginatedResult<OrderDutiesVO> orderDutiesCount(OrderDutiesDTO entity) {
		// TODO Auto-generated method stub
		List<OrderDutiesVO> dutiesList = orderStatisticMapper.orderDutiesListPage(entity);
		int totalCount = entity.getTotalResult();
		PaginatedResult<OrderDutiesVO> result = new PaginatedResult<>(dutiesList, entity.getCurrentPage(), entity.getShowCount(), totalCount);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	@Override
	public List<OrderPurchaseVO> orderPurchaseCountList(OrderStatisticDTO entity) {
		// TODO Auto-generated method stub
		return orderStatisticMapper.orderPurchaseInfo(entity);
	}

	@Override
	public List<OrderStatisticVO> orderPurchaseDetailList(OrderStatisticDTO entity) {
		// TODO Auto-generated method stub
		return orderStatisticMapper.orderPurchaseDetail(entity);
	}

	@Override
	public List<OrderDutiesVO> orderDutiesCountList(OrderDutiesDTO entity) {
		// TODO Auto-generated method stub
		return orderStatisticMapper.orderDuties(entity);
	}
	
}
