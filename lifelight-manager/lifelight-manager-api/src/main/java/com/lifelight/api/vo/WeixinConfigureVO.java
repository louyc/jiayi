package com.lifelight.api.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.entity.WeixinTemplate;
import com.lifelight.common.pageUtil.PageInfo;

public class WeixinConfigureVO extends WeixinConfigure{
   
	private List<WeixinMenuVO> listMenu;
	
	private List<WeixinTemplate> listTemp;

	public List<WeixinMenuVO> getListMenu() {
		return listMenu;
	}

	public void setListMenu(List<WeixinMenuVO> listMenu) {
		this.listMenu = listMenu;
	}

	public List<WeixinTemplate> getListTemp() {
		return listTemp;
	}
	public void setListTemp(List<WeixinTemplate> listTemp) {
		this.listTemp = listTemp;
	}
	
	
}