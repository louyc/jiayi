package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.jcabi.log.Logger;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.entity.WeixinConfigureExample;
import com.lifelight.api.entity.WeixinHomepageIcon;
import com.lifelight.api.entity.WeixinHomepageIconExample;
import com.lifelight.api.entity.WeixinMenu;
import com.lifelight.api.entity.WeixinTemplate;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.api.vo.WeixinMenuVO;
import com.lifelight.api.vo.WeixinTemplateVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.WeixinConfigureMapper;
import com.lifelight.dubbo.dao.WeixinHomepageIconMapper;
import com.lifelight.dubbo.dao.WeixinMenuMapper;
import com.lifelight.dubbo.dao.WeixinTemplateMapper;
import com.lifelight.dubbo.service.WeixinConfigureService;

import net.sf.json.JSONObject;

@Service
public class WeixinConfigureServiceImpl implements WeixinConfigureService {

	@Autowired
	private WeixinConfigureMapper weixinConfigureMapper;
	@Autowired
	private WeixinMenuMapper weixinMenuMapper;
	@Autowired
	private WeixinTemplateMapper weixinTemplateMapper;
	@Autowired
	private WeixinHomepageIconMapper weixinHomepageIconMapper;

	/**
	 * 新增微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public Result createWeixinConfigure(WeixinConfigure weixinConfigure){
		Result<WeixinConfigure> result = new Result<>(StatusCodes.OK, true);

		if (null == weixinConfigure) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		WeixinConfigure weixinQuery = new WeixinConfigure();
		weixinQuery.setDomainName(weixinConfigure.getDomainName());
		List<WeixinConfigure> list = weixinConfigureMapper.selectByName(weixinQuery);
		if(null!=list && list.size()>0) {
			result.setResultCode(new ResultCode("FAILED", "一个平台下只能有一条配置信息，请直接修改"));
			return result;
		}
		weixinConfigure.setCreateTime(new Date());
		weixinConfigure.setUpdateTime(new Date());
		weixinConfigure.setInUse("1");
		int num = weixinConfigureMapper.insertSelective(weixinConfigure);
		if (num != 0) {
			result.setData(weixinConfigure);
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除微信配置
	 * 
	 * @param id
	 * @return
	 */
	public Result deleteWeixinConfigure(String id,String inUse){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		WeixinConfigure con = weixinConfigureMapper.selectByPrimaryKey(Integer.valueOf(id));
//		int num = weixinConfigureMapper.deleteByPrimaryKey(Integer.parseInt(id));
		if(!StringUtil.isEmpty(inUse) && inUse.equals("1")) {
			List<WeixinConfigure> list = weixinConfigureMapper.selectByName(con);
			if(null!=list && list.size()>0) {
				result.setResultCode(new ResultCode("FAILED", "此存在相同域名的公众号"));
			}
		}
		WeixinConfigure record = new WeixinConfigure();
		record.setId(Integer.valueOf(id));
		record.setInUse(inUse);
		try {
			weixinConfigureMapper.updateByPrimaryKeySelective(record);
		}catch(Exception e) {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
			return result;
		}
		result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		return result;
	}

	/**
	 * 修改微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public Result updateWeixinConfigure(WeixinConfigure weixinConfigure) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (weixinConfigure == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		List<WeixinConfigure> list = weixinConfigureMapper.selectByName(weixinConfigure);
		if(null!=list && list.size()>0) {
			result.setResultCode(new ResultCode("FAILED", "此存在相同域名的公众号"));
		}
		weixinConfigure.setUpdateTime(new Date());
		int num = weixinConfigureMapper.updateByPrimaryKeySelective(weixinConfigure);
		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public PaginatedResult<WeixinConfigure> selectWeixinConfigureListPage(WeixinConfigure weixinConfigure) {

		List<WeixinConfigure> configureList = weixinConfigureMapper.selectWeixinConfigureListPage(weixinConfigure);
		// 总页数
		int totalCount = weixinConfigure.getTotalResult();

		PaginatedResult<WeixinConfigure> pa = new PaginatedResult<WeixinConfigure>(configureList,
				weixinConfigure.getCurrentPage(), weixinConfigure.getShowCount(), totalCount);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}
	/**
	 * 主键查询微信配置
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public WeixinConfigure selectWeixinConfigureBykey(String id) {
		if(StringUtil.isEmpty(id)) {
			return null;
		}
		WeixinConfigure configure = weixinConfigureMapper.selectByPrimaryKey(Integer.parseInt(id));
		return configure;
	}
	/**
	 * 查询微信配置all
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public List<WeixinConfigure> selectWeixinConfigureAll() {
		WeixinConfigureExample example = new WeixinConfigureExample();
		List<WeixinConfigure> list = weixinConfigureMapper.selectByExample(example);
		return list;
	}
	/**
	 * 查询微信菜单
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public PaginatedResult<WeixinMenuVO> selectPageWinxinMenuListPage(WeixinMenu weixinMenu) {
		
		weixinMenu.setCurrentPage(1);
		weixinMenu.setShowCount(10);
		List<WeixinMenu> weixinMenuList = weixinMenuMapper.selectWeixinMenuListPage(weixinMenu);
		
		List<WeixinMenuVO> returnList = new ArrayList<WeixinMenuVO>();
		// 总页数
		int totalCount = weixinMenu.getTotalResult();
		
		for(WeixinMenu wm: weixinMenuList) {
			JSONObject jsonObject = JSONObject.fromObject(wm);
			WeixinMenuVO weixinMenuVO = (WeixinMenuVO) JSONObject.toBean(jsonObject, WeixinMenuVO.class);
			wm.setParentId(wm.getId());
			List<WeixinMenu> list = weixinMenuMapper.selectListByContion(wm);
			if(null!=list && list.size()>0) {
				weixinMenuVO.setListWeixinMenu(list);
			}
			returnList.add(weixinMenuVO);
		}
		PaginatedResult<WeixinMenuVO> pa = new PaginatedResult<WeixinMenuVO>(returnList,
				weixinMenu.getCurrentPage(), weixinMenu.getShowCount(), totalCount);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@Override
	public WeixinMenu getWeixinMenuInfoByNameAndParent(WeixinMenuVO menuVO) {
		return weixinMenuMapper.selectByNameAndParent(menuVO);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Result weixinMenuCreate(WeixinMenuVO menuVO) {
		Result result = new Result<>(StatusCodes.OK, true);
		if(menuVO.getIsParent().equals("1")) {//同一公众号 添加一级菜单 不能超过3个
			WeixinMenu record = new WeixinMenu();
			record.setWeixinId(menuVO.getWeixinId());
			record.setParentId(0);
			List<WeixinMenu> list = weixinMenuMapper.selectListByContion(record);
			if(null!=list && list.size()>=3) {
				return new Result(StatusCodes.OK, false,new ResultCode("FAILD", "一个公众号一级菜单添加超过3个！") );
			}
		}
		if(menuVO.getIsParent().equals("0")) {//同一公众号 添加二级菜单 不能超过5个
			WeixinMenu record = new WeixinMenu();
			record.setParentId(menuVO.getParentId());
			record.setWeixinId(menuVO.getWeixinId());
			List<WeixinMenu> list = weixinMenuMapper.selectListByContion(record);
			if(null!=list && list.size()>=5) {
				return new Result(StatusCodes.OK, false,new ResultCode("FAILD", "一级菜单下二级菜单添加超过5个！") );
			}
		}
		Logger.info("菜单添加*****************{}",menuVO.getMenuName());
		int line =0;
		try {
			WeixinMenu menuInfo = new WeixinMenu();
			menuInfo.setMenuName(menuVO.getMenuName());
			menuInfo.setMenuUrl(menuVO.getMenuUrl());
			menuInfo.setParentId(menuVO.getParentId());
			menuInfo.setWeixinId(menuVO.getWeixinId());
			menuInfo.setCreateTime(new Date());
			menuInfo.setInUse("1");
			line = weixinMenuMapper.insertSelective(menuInfo);
			Logger.info("菜单添加*********line********{}",line+"");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加菜单成功！"));
		} else {
			result = new Result(StatusCodes.OK, false,new ResultCode("FAILD", "添加菜单失败！") );
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Result modifyWeixinMenu(WeixinMenuVO menuVO) {
		Result result = new Result<>(StatusCodes.OK, true);
		WeixinMenu menuInfo = weixinMenuMapper.selectByPrimaryKey(menuVO.getId());
		menuInfo.setId(menuVO.getId());
		menuInfo.setMenuName(menuVO.getMenuName());
		menuInfo.setParentId(menuVO.getParentId());
		menuInfo.setMenuUrl(menuVO.getMenuUrl());
		menuInfo.setUpdateTime(new Date());
		menuInfo.setInUse(menuInfo.getInUse());
		int line = weixinMenuMapper.updateByPrimaryKey(menuInfo);
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改菜单成功！"));
//			result.setData(menuInfo);
		} else {
			result = new Result(StatusCodes.OK, false,new ResultCode("FAILD", "修改菜单失败！") );
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Result removeWeixinMenu(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		WeixinMenu info = new WeixinMenu();
		info.setId(id);
		info.setUpdateTime(new Date());
		info.setInUse("0");
		int line = weixinMenuMapper.updateById(info);
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "菜单删除成功！"));
		} else {
			result = new Result(StatusCodes.OK, false, new ResultCode("FAILD", "菜单删除失败！"));
		}
		return result;
	}
	
	/**
	 * 查询微信摸版
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public PaginatedResult<WeixinTemplate> selectPageWinxinTemplateListPage(WeixinTemplate weixinTemplate) {
		weixinTemplate.setCurrentPage(1);
		weixinTemplate.setShowCount(10);
		List<WeixinTemplate> templateList = weixinTemplateMapper.selectWeixinTemplateListPage(weixinTemplate);
		// 总页数
		int totalCount = weixinTemplate.getTotalResult();
		
		PaginatedResult<WeixinTemplate> pa = new PaginatedResult<WeixinTemplate>(templateList,
				weixinTemplate.getCurrentPage(), weixinTemplate.getShowCount(), totalCount);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}
	/**
	 * 修改微信摸版id
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public Result updateWinxinTemplate(WeixinTemplateVO weixinTemplate) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			weixinTemplate.setUpdateTime(new Date());
			weixinTemplateMapper.updateByPrimaryKeySelective(weixinTemplate);
		}catch(Exception e) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "模板修改失败！"));
		}
		result.setResultCode(new ResultCode("SUCCESS", "模板修改成功！"));
		return result;
	}
	/**
	 * 删除微信摸版id
	 * 
	 * @param delWinxinTemplate
	 * @return
	 */
	public Result delWinxinTemplate(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			WeixinTemplate wt = weixinTemplateMapper.selectByPrimaryKey(Integer.parseInt(id));
			wt.setInUse("0");
			wt.setUpdateTime(new Date());
			weixinTemplateMapper.updateByPrimaryKeySelective(wt);
		}catch(Exception e) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "模板删除失败！"));
		}
		result.setResultCode(new ResultCode("SUCCESS", "模板删除成功！"));
		return result;
	}
	/**
	 * 新增微信摸版id
	 * 
	 * @param addWinxinTemplate
	 * @return
	 */
	public Result addWinxinTemplate(WeixinTemplateVO weixinTemplate) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			weixinTemplate.setCreateTime(new Date());
			weixinTemplate.setInUse("1");
			weixinTemplateMapper.insert(weixinTemplate);
		}catch(Exception e) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "模板新增失败！"));
		}
		result.setResultCode(new ResultCode("SUCCESS", "模板新增成功！"));
		return result;
	}
	/**
	 * 查询微信配置信息  根据domain
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public Result queryWeicinConfigureByDomain(String domain,String id){
		Result result = new Result<>(StatusCodes.OK, true);
		WeixinConfigure record = new WeixinConfigure();
		record.setDomainName(domain);
		if(!StringUtil.isEmpty(id)) {
			record.setId(Integer.parseInt(id));
		}
		if(!StringUtil.isEmpty(domain)) {
			record.setDomainName(domain);
		}
		List<WeixinConfigure> list= weixinConfigureMapper.selectByName(record);
		if(null!=list && list.size()==1) {
			WeixinMenu weixinMenu =new WeixinMenu();
			JSONObject jsonObject = JSONObject.fromObject(list.get(0));
			WeixinConfigureVO weixinConfigureVO = (WeixinConfigureVO) JSONObject.toBean(jsonObject, WeixinConfigureVO.class);
			weixinMenu.setWeixinId(weixinConfigureVO.getId());
			weixinMenu.setCurrentPage(1);
			weixinMenu.setShowCount(10);
			List<WeixinMenu> weixinMenuList = weixinMenuMapper.selectWeixinMenuListPage(weixinMenu);
			
			List<WeixinMenuVO> returnList = new ArrayList<WeixinMenuVO>();
			// 总页数
			int totalCount = weixinMenu.getTotalResult();
			
			for(WeixinMenu wm: weixinMenuList) {
				JSONObject jsonObjectMenu = JSONObject.fromObject(wm);
				WeixinMenuVO weixinMenuVO = (WeixinMenuVO) JSONObject.toBean(jsonObjectMenu, WeixinMenuVO.class);
				wm.setParentId(wm.getId());
				List<WeixinMenu> listMenu = weixinMenuMapper.selectListByContion(wm);
				if(null!=listMenu && listMenu.size()>0) {
					weixinMenuVO.setListWeixinMenu(listMenu);
				}
				returnList.add(weixinMenuVO);
			}
			weixinConfigureVO.setListMenu(returnList);//菜单添加
			WeixinTemplate weixinTemplate = new WeixinTemplate();
			weixinTemplate.setWeixinId(weixinConfigureVO.getId());	
			weixinTemplate.setCurrentPage(1);
			weixinTemplate.setShowCount(10);
			List<WeixinTemplate> templateList = weixinTemplateMapper.selectWeixinTemplateListPage(weixinTemplate);
			weixinConfigureVO.setListTemp(templateList);//摸版
			result.setData(weixinConfigureVO);
		}else {
			result.setData(null);
		}
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}
	
	
	/**
	 * 查询微信首页图标
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public Result getWinxinIcons(WeixinHomepageIcon weixinHomepageIcon) {
		Result result = new Result<>(StatusCodes.OK, true);
		WeixinHomepageIconExample example = new WeixinHomepageIconExample();
		example.createCriteria().andPlatformIdEqualTo(weixinHomepageIcon.getPlatformId());
		
		List<WeixinHomepageIcon> templateList = weixinHomepageIconMapper.selectByExample(example);
		result.setData(templateList);
		return result;
	}
	/**
	 * 修改微信首页图标
	 * 
	 * @param weixinConfigure
	 * @return
	 */
	public Result updateWinxinIcon(WeixinHomepageIcon weixinHomepageIcon) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			weixinHomepageIcon.setUpdateTime(new Date());
			weixinHomepageIconMapper.updateByPrimaryKeySelective(weixinHomepageIcon);
		}catch(Exception e) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "首页图标修改失败！"));
		}
		result.setResultCode(new ResultCode("SUCCESS", "首页图标修改成功！"));
		return result;
	}
	/**
	 * 删除微信首页图标
	 * 
	 * @param delWinxinTemplate
	 * @return
	 */
	public Result deleteWinxinIcon(WeixinHomepageIcon weixinHomepageIcon) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			weixinHomepageIconMapper.deleteByPrimaryKey(weixinHomepageIcon.getId());
		}catch(Exception e) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "首页图标删除失败！"));
		}
		result.setResultCode(new ResultCode("SUCCESS", "首页图标删除成功！"));
		return result;
	}
	/**
	 * 新增微信首页图标
	 * 
	 * @param addWinxinTemplate
	 * @return
	 */
	public Result addWinxinIcon(WeixinHomepageIcon weixinHomepageIcon) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			WeixinHomepageIconExample example = new WeixinHomepageIconExample();
			example.createCriteria().andPlatformIdEqualTo(weixinHomepageIcon.getPlatformId())
			.andTypeEqualTo(weixinHomepageIcon.getType());
			List<WeixinHomepageIcon> list = weixinHomepageIconMapper.selectByExample(example);
			if(null!=list && list.size()>0) {
				return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "相同类型图标已添加过！"));
			}
			weixinHomepageIcon.setCreateTime(new Date());
			weixinHomepageIconMapper.insert(weixinHomepageIcon);
		}catch(Exception e) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "首页图标新增失败！"));
		}
		result.setResultCode(new ResultCode("SUCCESS", "首页图标新增成功！"));
		return result;
	}

}