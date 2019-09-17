package com.lifelight.dubbo.service;

import com.lifelight.api.vo.ServiceInfoVO;
import com.lifelight.common.result.Result;

public interface JyServiceService {

	/**
	 * getServiceInfo 查询服务信息
	 * 
	 * @param queryMap
	 * @return
	 */
	Result getServiceInfoListPage(ServiceInfoVO serviceVO);

	/**
	 * serviceInfoCreate 服务信息添加
	 * 
	 * @param service
	 * @return
	 */
	Result serviceInfoCreate(ServiceInfoVO serviceInfoVO);

	/**
	 * serviceInfoUpdate 服务信息修改
	 * 
	 * @param service
	 * @return
	 */
	Result serviceInfoUpdate(ServiceInfoVO service);

}