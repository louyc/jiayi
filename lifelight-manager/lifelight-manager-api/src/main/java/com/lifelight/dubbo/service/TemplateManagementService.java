package com.lifelight.dubbo.service;

import com.lifelight.api.entity.TemplateManagement;
import com.lifelight.common.result.Result;

public interface TemplateManagementService {

	/**
	 * 创建模板
	 * 
	 * @param message
	 * @return
	 */
	Result createTemplateManagement(TemplateManagement temple);
	/**
	 * 初始化创建2条模板
	 * 
	 * @param message
	 * @return
	 */
	Result createAllTemplateManagement(Integer platformId);
	
	/**
	 * 删除模板
	 * 
	 * @param id
	 * @return
	 */
	Result deleteTemplateManagement(String id);

	/**
	 * 修改模板
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updateTemplateManagement(TemplateManagement temple);

	/**
	 * 查询模板
	 * 
	 * @param name
	 * @return
	 */
	Result selectTemplateManagementListPage(TemplateManagement temple);
	
}