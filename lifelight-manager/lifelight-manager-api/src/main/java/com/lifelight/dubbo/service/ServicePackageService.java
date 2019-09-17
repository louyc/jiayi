package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ServicePackage;
import com.lifelight.api.vo.ServicePackageVO;
import com.lifelight.common.result.Result;

public interface ServicePackageService {

	/**
	 * 创建服务包
	 * 
	 * @param name
	 * @return
	 */
	Result createServicePackage(ServicePackageVO servicePackage);
	
	/**
	 * 删除服务包
	 * 
	 * @param id
	 * @return
	 */
	Result deleteServicePackage(Integer id);

	/**
	 * 修改服务包
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updateServicePackage(ServicePackageVO keywords);

	/**
	 * 查询服务包
	 * 
	 * @param name
	 * @return
	 */
	Result selectServicePackageListPage(ServicePackage keywords);
	
}