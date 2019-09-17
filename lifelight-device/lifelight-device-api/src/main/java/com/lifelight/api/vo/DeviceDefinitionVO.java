package com.lifelight.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.api.entity.DeviceDefinitionProject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DeviceDefinitionVO implements Serializable{

	private Integer id;

	/**
	 * 厂商ID
	 */
	private Integer deviceFirmid;

	private Integer platformId;

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	/**
	 * 厂商名称
	 */
	private String deviceFirmName;

	/**
	 * 品牌ID
	 */
	private Integer deviceBrandid;

	/**
	 * 品牌名称
	 */
	private String deviceBrandName;

	/**
	 * 品牌定义名称
	 */
	private String definitionName;

	/**
	 * 品牌定义编码
	 */
	private String definitionCode;

	/**
	 * 品牌定义规格
	 */
	private String definitionSpec;

	/**
	 * 联网类型 0：蓝牙 1：wifi
	 */
	private String networkingType;

	/**
	 * 品牌定义描述 
	 */
	private String definitionDesc;

	/**
	 * 状态：0：上架 ，1：下架
	 */
	private Integer type;

	/**
	 * 创建时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;

	/**
	 * 是否删除 F 否 T 是
	 */
	private String isdel;

	/**
	 * 检测项目列表
	 */
	private List<DeviceDefinitionProject> projectList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDeviceFirmid() {
		return deviceFirmid;
	}

	public void setDeviceFirmid(Integer deviceFirmid) {
		this.deviceFirmid = deviceFirmid;
	}

	public Integer getDeviceBrandid() {
		return deviceBrandid;
	}

	public void setDeviceBrandid(Integer deviceBrandid) {
		this.deviceBrandid = deviceBrandid;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public String getDefinitionCode() {
		return definitionCode;
	}

	public void setDefinitionCode(String definitionCode) {
		this.definitionCode = definitionCode;
	}

	public String getDefinitionSpec() {
		return definitionSpec;
	}

	public void setDefinitionSpec(String definitionSpec) {
		this.definitionSpec = definitionSpec;
	}

	public String getNetworkingType() {
		return networkingType;
	}

	public void setNetworkingType(String networkingType) {
		this.networkingType = networkingType;
	}

	public String getDefinitionDesc() {
		return definitionDesc;
	}

	public void setDefinitionDesc(String definitionDesc) {
		this.definitionDesc = definitionDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public List<DeviceDefinitionProject> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<DeviceDefinitionProject> projectList) {
		this.projectList = projectList;
	}

	public String getDeviceFirmName() {
		return deviceFirmName;
	}

	public void setDeviceFirmName(String deviceFirmName) {
		this.deviceFirmName = deviceFirmName;
	}

	public String getDeviceBrandName() {
		return deviceBrandName;
	}

	public void setDeviceBrandName(String deviceBrandName) {
		this.deviceBrandName = deviceBrandName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}	
}