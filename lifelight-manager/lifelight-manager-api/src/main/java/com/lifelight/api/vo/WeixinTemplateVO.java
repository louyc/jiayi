package com.lifelight.api.vo;

import java.util.List;

import com.lifelight.api.entity.WeixinTemplate;

public class WeixinTemplateVO extends WeixinTemplate{
   
	private List<WeixinTemplate> templateList;

	public List<WeixinTemplate> getTemplates() {
		return templateList;
	}

	public void setTemplates(List<WeixinTemplate> templates) {
		this.templateList = templates;
	}
	
}