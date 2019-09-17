package com.lifelight.api.vo;

import java.util.List;

import com.lifelight.api.entity.Dictionary;
import com.lifelight.api.entity.ServicePackage;

public class ServicePackageVO extends ServicePackage{

	private List<Dictionary> listDictionary;
	
	private String dicIds;

	public List<Dictionary> getListDictionary() {
		return listDictionary;
	}

	public void setListDictionary(List<Dictionary> listDictionary) {
		this.listDictionary = listDictionary;
	}

	public String getDicIds() {
		return dicIds;
	}

	public void setDicIds(String dicIds) {
		this.dicIds = dicIds;
	}
}
