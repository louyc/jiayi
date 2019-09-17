package com.lifelight.api.entity;

import java.io.Serializable;
import java.util.Date;

public class PersonalTailorMessage implements Serializable {
	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.id
	 *
	 * @mbg.generated
	 */
	private Integer id;
	private Integer platformId;

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.apply_type
	 *
	 * @mbg.generated
	 */
	private Integer applyType;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.title
	 *
	 * @mbg.generated
	 */
	private String personalId;

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.manager_id
	 *
	 * @mbg.generated
	 */
	private String managerId;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.name
	 *
	 * @mbg.generated
	 */
	private String name;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.mobile
	 *
	 * @mbg.generated
	 */
	private String mobile;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.is_read
	 *
	 * @mbg.generated
	 */
	private Integer isRead;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.is_handle
	 *
	 * @mbg.generated
	 */
	private Integer isHandle;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.create_time
	 *
	 * @mbg.generated
	 */
	private Date createTime;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.update_time
	 *
	 * @mbg.generated
	 */
	private Date updateTime;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column personal_tailor_message.in_use
	 *
	 * @mbg.generated
	 */
	private Integer inUse;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.id
	 *
	 * @return the value of personal_tailor_message.id
	 *
	 * @mbg.generated
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.id
	 *
	 * @param id
	 *            the value for personal_tailor_message.id
	 *
	 * @mbg.generated
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.apply_type
	 *
	 * @return the value of personal_tailor_message.apply_type
	 *
	 * @mbg.generated
	 */
	public Integer getApplyType() {
		return applyType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.apply_type
	 *
	 * @param applyType
	 *            the value for personal_tailor_message.apply_type
	 *
	 * @mbg.generated
	 */
	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.manager_id
	 *
	 * @return the value of personal_tailor_message.manager_id
	 *
	 * @mbg.generated
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.manager_id
	 *
	 * @param managerId
	 *            the value for personal_tailor_message.manager_id
	 *
	 * @mbg.generated
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId == null ? null : managerId.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.name
	 *
	 * @return the value of personal_tailor_message.name
	 *
	 * @mbg.generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.name
	 *
	 * @param name
	 *            the value for personal_tailor_message.name
	 *
	 * @mbg.generated
	 */
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.mobile
	 *
	 * @return the value of personal_tailor_message.mobile
	 *
	 * @mbg.generated
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.mobile
	 *
	 * @param mobile
	 *            the value for personal_tailor_message.mobile
	 *
	 * @mbg.generated
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.is_read
	 *
	 * @return the value of personal_tailor_message.is_read
	 *
	 * @mbg.generated
	 */
	public Integer getIsRead() {
		return isRead;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.is_read
	 *
	 * @param isRead
	 *            the value for personal_tailor_message.is_read
	 *
	 * @mbg.generated
	 */
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.is_handle
	 *
	 * @return the value of personal_tailor_message.is_handle
	 *
	 * @mbg.generated
	 */
	public Integer getIsHandle() {
		return isHandle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.is_handle
	 *
	 * @param isHandle
	 *            the value for personal_tailor_message.is_handle
	 *
	 * @mbg.generated
	 */
	public void setIsHandle(Integer isHandle) {
		this.isHandle = isHandle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.create_time
	 *
	 * @return the value of personal_tailor_message.create_time
	 *
	 * @mbg.generated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.create_time
	 *
	 * @param createTime
	 *            the value for personal_tailor_message.create_time
	 *
	 * @mbg.generated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.update_time
	 *
	 * @return the value of personal_tailor_message.update_time
	 *
	 * @mbg.generated
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.update_time
	 *
	 * @param updateTime
	 *            the value for personal_tailor_message.update_time
	 *
	 * @mbg.generated
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column personal_tailor_message.in_use
	 *
	 * @return the value of personal_tailor_message.in_use
	 *
	 * @mbg.generated
	 */
	public Integer getInUse() {
		return inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column personal_tailor_message.in_use
	 *
	 * @param inUse
	 *            the value for personal_tailor_message.in_use
	 *
	 * @mbg.generated
	 */
	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		PersonalTailorMessage other = (PersonalTailorMessage) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getApplyType() == null ? other.getApplyType() == null
						: this.getApplyType().equals(other.getApplyType()))
				&& (this.getPersonalId() == null ? other.getPersonalId() == null : this.getPersonalId().equals(other.getPersonalId()))
				&& (this.getManagerId() == null ? other.getManagerId() == null
						: this.getManagerId().equals(other.getManagerId()))
				&& (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
				&& (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
				&& (this.getIsRead() == null ? other.getIsRead() == null : this.getIsRead().equals(other.getIsRead()))
				&& (this.getIsHandle() == null ? other.getIsHandle() == null
						: this.getIsHandle().equals(other.getIsHandle()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()))
				&& (this.getUpdateTime() == null ? other.getUpdateTime() == null
						: this.getUpdateTime().equals(other.getUpdateTime()))
				&& (this.getInUse() == null ? other.getInUse() == null : this.getInUse().equals(other.getInUse()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getApplyType() == null) ? 0 : getApplyType().hashCode());
		result = prime * result + ((getPersonalId() == null) ? 0 : getPersonalId().hashCode());
		result = prime * result + ((getManagerId() == null) ? 0 : getManagerId().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
		result = prime * result + ((getIsRead() == null) ? 0 : getIsRead().hashCode());
		result = prime * result + ((getIsHandle() == null) ? 0 : getIsHandle().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
		result = prime * result + ((getInUse() == null) ? 0 : getInUse().hashCode());
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", applyType=").append(applyType);
		sb.append(", title=").append(personalId);
		sb.append(", managerId=").append(managerId);
		sb.append(", name=").append(name);
		sb.append(", mobile=").append(mobile);
		sb.append(", isRead=").append(isRead);
		sb.append(", isHandle=").append(isHandle);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", inUse=").append(inUse);
		sb.append("]");
		return sb.toString();
	}
}