package com.lifelight.api.vo;

import com.lifelight.common.pageUtil.PageInfo;

/**
 * @Description TODO 日期查询条件
 * @author xuyuxing
 * @createTime 2017年7月29日 下午2:08:28
 */
public class DateConditionsVO extends PageInfo {
	private String startDate;
	private String endDate;
	private Integer platformId;
	
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
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
}
