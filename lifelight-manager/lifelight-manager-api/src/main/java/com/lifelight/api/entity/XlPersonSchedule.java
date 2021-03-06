package com.lifelight.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.common.pageUtil.PageInfo;

public class XlPersonSchedule extends PageInfo{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.id
     *
     * @mbg.generated
     */
    private Integer id;
    
    private String examinationInfo;
    

    public String getExaminationInfo() {
		return examinationInfo;
	}

	public void setExaminationInfo(String examinationInfo) {
		this.examinationInfo = examinationInfo;
	}

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.contract_id
     *
     * @mbg.generated
     */
    private String contractId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.input_person
     *
     * @mbg.generated
     */
    private String inputPerson;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.check_org
     *
     * @mbg.generated
     */
    private String checkOrg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.check_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date checkTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.project_type
     *
     * @mbg.generated
     */
    
    private String servicePackage;
    
    private String servicePackageDesc;
    
    public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getServicePackageDesc() {
		return servicePackageDesc;
	}

	public void setServicePackageDesc(String servicePackageDesc) {
		this.servicePackageDesc = servicePackageDesc;
	}

	private String projectType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.project_type_desc
     *
     * @mbg.generated
     */
    private String projectTypeDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.create_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.update_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column xl_person_schedule.project_value
     *
     * @mbg.generated
     */
    private String projectValue;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.id
     *
     * @return the value of xl_person_schedule.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.id
     *
     * @param id the value for xl_person_schedule.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.contract_id
     *
     * @return the value of xl_person_schedule.contract_id
     *
     * @mbg.generated
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.contract_id
     *
     * @param contractId the value for xl_person_schedule.contract_id
     *
     * @mbg.generated
     */
    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.input_person
     *
     * @return the value of xl_person_schedule.input_person
     *
     * @mbg.generated
     */
    public String getInputPerson() {
        return inputPerson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.input_person
     *
     * @param inputPerson the value for xl_person_schedule.input_person
     *
     * @mbg.generated
     */
    public void setInputPerson(String inputPerson) {
        this.inputPerson = inputPerson == null ? null : inputPerson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.check_org
     *
     * @return the value of xl_person_schedule.check_org
     *
     * @mbg.generated
     */
    public String getCheckOrg() {
        return checkOrg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.check_org
     *
     * @param checkOrg the value for xl_person_schedule.check_org
     *
     * @mbg.generated
     */
    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg == null ? null : checkOrg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.check_time
     *
     * @return the value of xl_person_schedule.check_time
     *
     * @mbg.generated
     */
    public Date getCheckTime() {
        return checkTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.check_time
     *
     * @param checkTime the value for xl_person_schedule.check_time
     *
     * @mbg.generated
     */
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.project_type
     *
     * @return the value of xl_person_schedule.project_type
     *
     * @mbg.generated
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.project_type
     *
     * @param projectType the value for xl_person_schedule.project_type
     *
     * @mbg.generated
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.project_type_desc
     *
     * @return the value of xl_person_schedule.project_type_desc
     *
     * @mbg.generated
     */
    public String getProjectTypeDesc() {
        return projectTypeDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.project_type_desc
     *
     * @param projectTypeDesc the value for xl_person_schedule.project_type_desc
     *
     * @mbg.generated
     */
    public void setProjectTypeDesc(String projectTypeDesc) {
        this.projectTypeDesc = projectTypeDesc == null ? null : projectTypeDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.create_time
     *
     * @return the value of xl_person_schedule.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.create_time
     *
     * @param createTime the value for xl_person_schedule.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.update_time
     *
     * @return the value of xl_person_schedule.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.update_time
     *
     * @param updateTime the value for xl_person_schedule.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column xl_person_schedule.project_value
     *
     * @return the value of xl_person_schedule.project_value
     *
     * @mbg.generated
     */
    public String getProjectValue() {
        return projectValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column xl_person_schedule.project_value
     *
     * @param projectValue the value for xl_person_schedule.project_value
     *
     * @mbg.generated
     */
    public void setProjectValue(String projectValue) {
        this.projectValue = projectValue == null ? null : projectValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xl_person_schedule
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
        XlPersonSchedule other = (XlPersonSchedule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getContractId() == null ? other.getContractId() == null : this.getContractId().equals(other.getContractId()))
            && (this.getInputPerson() == null ? other.getInputPerson() == null : this.getInputPerson().equals(other.getInputPerson()))
            && (this.getCheckOrg() == null ? other.getCheckOrg() == null : this.getCheckOrg().equals(other.getCheckOrg()))
            && (this.getCheckTime() == null ? other.getCheckTime() == null : this.getCheckTime().equals(other.getCheckTime()))
            && (this.getProjectType() == null ? other.getProjectType() == null : this.getProjectType().equals(other.getProjectType()))
            && (this.getProjectTypeDesc() == null ? other.getProjectTypeDesc() == null : this.getProjectTypeDesc().equals(other.getProjectTypeDesc()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getProjectValue() == null ? other.getProjectValue() == null : this.getProjectValue().equals(other.getProjectValue()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xl_person_schedule
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getContractId() == null) ? 0 : getContractId().hashCode());
        result = prime * result + ((getInputPerson() == null) ? 0 : getInputPerson().hashCode());
        result = prime * result + ((getCheckOrg() == null) ? 0 : getCheckOrg().hashCode());
        result = prime * result + ((getCheckTime() == null) ? 0 : getCheckTime().hashCode());
        result = prime * result + ((getProjectType() == null) ? 0 : getProjectType().hashCode());
        result = prime * result + ((getProjectTypeDesc() == null) ? 0 : getProjectTypeDesc().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getProjectValue() == null) ? 0 : getProjectValue().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table xl_person_schedule
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
        sb.append(", contractId=").append(contractId);
        sb.append(", inputPerson=").append(inputPerson);
        sb.append(", checkOrg=").append(checkOrg);
        sb.append(", checkTime=").append(checkTime);
        sb.append(", projectType=").append(projectType);
        sb.append(", projectTypeDesc=").append(projectTypeDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", projectValue=").append(projectValue);
        sb.append("]");
        return sb.toString();
    }
}