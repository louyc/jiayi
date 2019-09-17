package com.lifelight.dubbo.service;

public interface TagsRelService {

	/**
	 * 批量插入标签
	 * 
	 * @param tagDics
	 * @return
	 */
	public int insertTags(String[] tagArr, String managerId,Integer platformId);

	/**
	 * 解除部门关系
	 * 
	 * @param managerId
	 * @return
	 */
	public int removeRel(String managerId,Integer platformId);
}
