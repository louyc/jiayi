package com.lifelight.api.vo;

import com.lifelight.api.entity.XlPersonSchedule;

public class XlPersonScheduleVO extends XlPersonSchedule{

	private String name;
	private String sexDesc;
	private String sex;
	private String remark;
	private Integer documentId;
	private String finishStatus;
	
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public Integer getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSexDesc() {
		return sexDesc;
	}
	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
