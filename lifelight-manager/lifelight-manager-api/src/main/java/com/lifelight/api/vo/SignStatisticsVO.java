package com.lifelight.api.vo;

import java.io.Serializable;
import java.util.List;

import com.lifelight.common.pageUtil.PageInfo;

public class SignStatisticsVO implements Serializable{

	private String doctorId;
	private String orgId;
	private String orgName;
	private Integer countSum; //有偿签约包数
	private Integer finishSum;//已完成数
	private Integer countItemSum;//内含项目总数
	private Integer finishItemSum;//执行项目条数
	private String finishRate;//服务包完成率
	private String startDate;
	private String endDate;
	private String signPersonType;
	private String finishStatus;
	private Integer itemId;
	private String itemName;
	private List<SignStatisticsVO> list;
	
	public Integer getCountItemSum() {
		return countItemSum;
	}
	public void setCountItemSum(Integer countItemSum) {
		this.countItemSum = countItemSum;
	}
	public Integer getFinishItemSum() {
		return finishItemSum;
	}
	public void setFinishItemSum(Integer finishItemSum) {
		this.finishItemSum = finishItemSum;
	}
	public List<SignStatisticsVO> getList() {
		return list;
	}
	public void setList(List<SignStatisticsVO> list) {
		this.list = list;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getSignPersonType() {
		return signPersonType;
	}
	public void setSignPersonType(String signPersonType) {
		this.signPersonType = signPersonType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getCountSum() {
		return countSum;
	}
	public void setCountSum(Integer countSum) {
		this.countSum = countSum;
	}
	public Integer getFinishSum() {
		return finishSum;
	}
	public void setFinishSum(Integer finishSum) {
		this.finishSum = finishSum;
	}
	public String getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}
	
	
}
