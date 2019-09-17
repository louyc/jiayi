package com.lifelight.api.vo;

import java.util.List;

import com.lifelight.api.entity.WeixinMenu;

public class WeixinMenuVO extends WeixinMenu{

	private List<WeixinMenu> listWeixinMenu;
	
	private String isParent;  //1 一级菜单     2：二级菜单
	
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public List<WeixinMenu> getListWeixinMenu() {
		return listWeixinMenu;
	}
	public void setListWeixinMenu(List<WeixinMenu> listWeixinMenu) {
		this.listWeixinMenu = listWeixinMenu;
	}


}