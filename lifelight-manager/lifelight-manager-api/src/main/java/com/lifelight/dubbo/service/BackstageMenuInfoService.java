package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.BackstageMenuInfo;
import com.lifelight.common.result.Result;

public interface BackstageMenuInfoService {

	/**
	 * getBackstageMenuInfoByName:根据角色查询对应菜单信息. <br/>
	 * 
	 * @param roleId
	 * @return List<BackstageMenuInfo>
	 * @author lyc
	 */
	Result getBackstageMenuInfoByRoleId(String roleId,Integer platformId);

	/**
	 * getBackstageMenuInfoByName:根据菜单描述查询对应菜单信息. <br/>
	 * 
	 * @param name
	 * @return BackstageMenuInfo
	 * @author lyc
	 */
	BackstageMenuInfo getBackstageMenuInfoByName(String name,Integer platformId);

	/**
	 * getBackstageMenuInfoByNameAndParent:根据菜单+父节点查询对应菜单信息. <br/>
	 * 
	 * @param name
	 * @param parentId
	 * @return BackstageMenuInfo
	 * @author lyc
	 */
	BackstageMenuInfo getBackstageMenuInfoByNameAndParent(String name, String parentId,Integer platformId);

	/**
	 * backstageMenuCreate:添加菜单. <br/>
	 * 
	 * @param name
	 * @param url
	 * @param parentId
	 * @return Result
	 * @author lyc
	 */
	Result backstageMenuCreate(String name, String url, int parentId,Integer platformId);

	/**
	 * backstageMenuUpdate:菜单更新 <br/>
	 * 
	 * @param info
	 * @return Result
	 * @author lyc
	 */
	Result backstageMenuUpdate(String name, String url, int parentId, int id);

	/**
	 * backstageMenuRemove:菜单删除
	 * 
	 * @param info
	 * @return Result
	 * @author lyc
	 */
	Result backstageMenuRemove(String name, int id,Integer platformId);

	/**
	 * backstageRoleMenuRelCreate:角色和菜单关系绑定
	 * 
	 * @param menuId
	 * @param roleId
	 * @return Result
	 * @author lyc
	 */

	Result backstageRoleMenuRelCreate(List<Object> roleId, String menuId,Integer platformId);

	/**
	 * backstageRoleMenuRelModify 修改角色 菜单关系
	 * 
	 * @param id
	 * @param menuId
	 * @param roleId
	 * @return Result
	 */

	Result backstageRoleMenuRelModify(String id, String menuId, String roleId,Integer platformId);

	/**
	 * backstageRoleMenuRelQuery 查询角色菜单关系
	 * 
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	Result backstageRoleMenuRelQuery(String menuId, String roleId,Integer platformId);

	/**
	 * backstageRoleMenuRelRemove 删除角色菜单关系
	 * 
	 * @param id
	 * @return
	 */
	Result backstageRoleMenuRelRemove(String id,Integer platformId);
	/**
	 * backstageRoleMenuRelCopy 复制管理员角色菜单关系
	 * 
	 * @param id
	 * @return
	 */
	Result backstageRoleMenuRelCopy(Integer platformId);

}