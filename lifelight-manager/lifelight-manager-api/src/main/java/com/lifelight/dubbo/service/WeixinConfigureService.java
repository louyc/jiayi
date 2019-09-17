package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.BackstageMenuInfo;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.entity.WeixinHomepageIcon;
import com.lifelight.api.entity.WeixinMenu;
import com.lifelight.api.entity.WeixinTemplate;
import com.lifelight.api.vo.WeixinMenuVO;
import com.lifelight.api.vo.WeixinTemplateVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface WeixinConfigureService {

	/**
	 * 新增微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	Result createWeixinConfigure(WeixinConfigure weixinConfigure);
	
	/**
	 * 删除微信配置
	 * 
	 * @param id
	 * @return
	 */
	Result deleteWeixinConfigure(String id,String inUse);

	/**
	 * 修改微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	Result updateWeixinConfigure(WeixinConfigure weixinConfigure);

	/**
	 * 查询微信配置分页  生效的
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	PaginatedResult<WeixinConfigure> selectWeixinConfigureListPage(WeixinConfigure weixinConfigure);
	/**
	 * 主键查询微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	WeixinConfigure selectWeixinConfigureBykey(String id);
	/**
	 * 查询所有微信配置  生效和不生效的
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	List<WeixinConfigure> selectWeixinConfigureAll();
	
	/**
	 * 查询微信配置信息 根据domainName
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	Result queryWeicinConfigureByDomain(String domainName,String id);
	/**
	 * 查询微信菜单
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	PaginatedResult<WeixinMenuVO> selectPageWinxinMenuListPage(WeixinMenu weixinMenu);
	
	/**
	 * getWeixinMenuInfoByNameAndParent:根据菜单+父节点查询对应菜单信息. <br/>
	 * 
	 * @return WeixinMenu
	 * @author lyc
	 */
	WeixinMenu getWeixinMenuInfoByNameAndParent(WeixinMenuVO menuVO);
	
	/**
	 * weixinMenuCreate:添加菜单. <br/>
	 * 
	 * @return WeixinMenu
	 * @author lyc
	 */
	Result weixinMenuCreate(WeixinMenuVO menuVO);
	
	/**
	 * modifyWeixinMenu:菜单更新 <br/>
	 * 
	 * @param info
	 * @return Result
	 * @author lyc
	 */
	Result modifyWeixinMenu(WeixinMenuVO menuVO);
	/**
	 * removeWeixinMenu:菜单删除<br/>
	 * 
	 * @param info
	 * @return Result
	 * @author lyc
	 */
	Result removeWeixinMenu(Integer id);
	
	/**
	 * 查询微信摸版
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	PaginatedResult<WeixinTemplate> selectPageWinxinTemplateListPage(WeixinTemplate weixinTemplate);
	/**
	 * 修改微信摸版id
	 * 
	 * @param updateWinxinTemplate
	 * @return
	 */
	Result updateWinxinTemplate(WeixinTemplateVO weixinTemplate);
	/**
	 * 删除微信摸版id
	 * 
	 * @param delWinxinTemplate
	 * @return
	 */
	Result delWinxinTemplate(String id);
	/**
	 * 新增微信摸版id
	 * 
	 * @param addWinxinTemplate
	 * @return
	 */
	Result addWinxinTemplate(WeixinTemplateVO weixinTemplate);
	
	
	
	/**
	 * 查询微信首页图标
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	Result getWinxinIcons(WeixinHomepageIcon weixinHomepageIcon);
	/**
	 * 修改微信首页图标
	 * 
	 * @param updateWinxinTemplate
	 * @return
	 */
	Result updateWinxinIcon(WeixinHomepageIcon weixinHomepageIcon);
	/**
	 * 删除微信首页图标
	 * 
	 * @param delWinxinTemplate
	 * @return
	 */
	Result deleteWinxinIcon(WeixinHomepageIcon weixinHomepageIcon);
	/**
	 * 新增微信首页图标
	 * 
	 * @param addWinxinTemplate
	 * @return
	 */
	Result addWinxinIcon(WeixinHomepageIcon weixinHomepageIcon);
	
	
}