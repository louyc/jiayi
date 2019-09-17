package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.vo.ApiUserInvoiceVO;
import com.lifelight.common.result.Result;

public interface ApiUserInvoiceService {

	/**
	 * updateInvoice：完善用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateInvoice(ApiUserInvoiceVO invoiceInfo);
	
	/**
	 * queryUserInvoice：查询用户发票信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	List<ApiUserInvoiceVO> queryInvoiceByManagerId(String managerId);
	
	

}