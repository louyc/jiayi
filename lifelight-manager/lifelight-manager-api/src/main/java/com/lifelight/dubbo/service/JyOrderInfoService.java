package com.lifelight.dubbo.service;

import java.util.List;
import java.util.Map;

import com.lifelight.api.entity.OrderInfo;
import com.lifelight.api.vo.OrderInfoVO;
import com.lifelight.common.result.Result;

public interface JyOrderInfoService {

	/**
	 * 家庭医生创建订单
	 * @param serviceId
	 * @param doctorId
	 * @param userId
	 * @return
	 */
	/*Result<OrderInfo> addJyOrderInfo(Integer serviceId, String doctorId, String userId);*/
	
	/**
	 * 查询订单列表
	 * @param userId
	 * @param doctorId
	 * @param validTime
	 * @return
	 */
	Result<List<OrderInfoVO>> getJyValidUserOrder(Map<String, Object> query);
	
	/**
	 * 后台查询订单接口
	 * @param query
	 * @return
	 */
	Result<List<OrderInfoVO>> getBackValidUserOrder(Map<String, Object> query);
	
}
