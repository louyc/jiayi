package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.ApiUserInvoice;
import com.lifelight.api.vo.ApiUserInvoiceVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.ApiUserInvoiceMapper;
import com.lifelight.dubbo.service.ApiUserInvoiceService;

@Service
public class ApiUserInvoiceServiceImpl implements ApiUserInvoiceService {

	private static final Logger logger = LoggerFactory.getLogger(ApiUserInvoiceServiceImpl.class);

	@Autowired
	private ApiUserInvoiceMapper apiUserInvoiceMapper;

	@SuppressWarnings("rawtypes")
	@Override
	public Result updateInvoice(ApiUserInvoiceVO info) {
		logger.info("更新的发票信息::" + info.getManagerId()+info.getTaxpayer());
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInvoice invoice = apiUserInvoiceMapper.selectByEntity(info);
		int i =0;
		if(null!=invoice && !StringUtil.isEmpty(invoice.getManagerId())) {
			invoice.setInvoiceTitleType(info.getInvoiceTitleType());
			invoice.setInvoiceType(info.getInvoiceType());
			invoice.setTaxpayer(info.getTaxpayer());
			invoice.setTaxpayerNumber(info.getTaxpayerNumber());
			invoice.setTicketCollectorEmail(info.getTicketCollectorEmail());
			invoice.setTicketCollectorMobile(info.getTicketCollectorMobile());
			invoice.setUpdateTime(new Date());
			i = apiUserInvoiceMapper.updateByPrimaryKeySelective(invoice);
		}else {
			info.setCreateTime(new Date());
			info.setUpdateTime(new Date());
			info.setInUse(1);
			i = apiUserInvoiceMapper.insertVO(info);
		}
		if(i>0) {
			result.setResultCode(new ResultCode("SUCCESS", "发票信息更新成功！"));
		}else {
			result = new Result(StatusCodes.FAILED_DEPENDENCY, false, new ResultCode("FAILE", "更新发票信息失败！"));
		}
		return result;
	}

	@Override
	public  List<ApiUserInvoiceVO> queryInvoiceByManagerId(String managerId) {
		List<ApiUserInvoiceVO> invoiceList = new ArrayList<ApiUserInvoiceVO>();
		invoiceList = apiUserInvoiceMapper.queryInvoiceByManagerId(managerId);
		return invoiceList;
	}


}
