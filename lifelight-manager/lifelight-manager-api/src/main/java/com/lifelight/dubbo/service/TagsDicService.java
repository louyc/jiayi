package com.lifelight.dubbo.service;

import com.lifelight.api.entity.TagsDic;
import com.lifelight.api.entity.TagsDicExample;
import com.lifelight.common.result.Result;

public interface TagsDicService {

	/**
	 * 添加部门
	 * 
	 * @param tagsDic
	 * @return
	 */
	Result insert(TagsDic tagsDic);

	/**
	 * 查询标签
	 * 
	 * @param tagsDicExample
	 * @return
	 */
	Result getAllTags(int in_use, String name,Integer platformId);

	/**
	 * 删除标签
	 * 
	 * @param tagId
	 * @return
	 */
	Result remove(int tagId);

	/**
	 * 修改标签
	 * 
	 * @param tagId
	 * @param name
	 * @return
	 */
	Result modify(int tagId, String name);

}
