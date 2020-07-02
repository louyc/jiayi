package com.lifelight.api.entity;

import java.util.Date;

import com.lifelight.common.pageUtil.PageInfo;

public class DeviceDefinitionProject extends PageInfo{
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.definition_id
     *
     * @mbg.generated
     */
    private Integer definitionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.project_name
     *
     * @mbg.generated
     */
    private String projectName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.project_unit
     *
     * @mbg.generated
     */
    private String projectUnit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column device_definition_project.isDel
     *
     * @mbg.generated
     */
    private String isdel;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.id
     *
     * @return the value of device_definition_project.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.id
     *
     * @param id the value for device_definition_project.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.definition_id
     *
     * @return the value of device_definition_project.definition_id
     *
     * @mbg.generated
     */
    public Integer getDefinitionId() {
        return definitionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.definition_id
     *
     * @param definitionId the value for device_definition_project.definition_id
     *
     * @mbg.generated
     */
    public void setDefinitionId(Integer definitionId) {
        this.definitionId = definitionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.project_name
     *
     * @return the value of device_definition_project.project_name
     *
     * @mbg.generated
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.project_name
     *
     * @param projectName the value for device_definition_project.project_name
     *
     * @mbg.generated
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.project_unit
     *
     * @return the value of device_definition_project.project_unit
     *
     * @mbg.generated
     */
    public String getProjectUnit() {
        return projectUnit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.project_unit
     *
     * @param projectUnit the value for device_definition_project.project_unit
     *
     * @mbg.generated
     */
    public void setProjectUnit(String projectUnit) {
        this.projectUnit = projectUnit == null ? null : projectUnit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.create_time
     *
     * @return the value of device_definition_project.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.create_time
     *
     * @param createTime the value for device_definition_project.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.update_time
     *
     * @return the value of device_definition_project.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.update_time
     *
     * @param updateTime the value for device_definition_project.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column device_definition_project.isDel
     *
     * @return the value of device_definition_project.isDel
     *
     * @mbg.generated
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column device_definition_project.isDel
     *
     * @param isdel the value for device_definition_project.isDel
     *
     * @mbg.generated
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_definition_project
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
        DeviceDefinitionProject other = (DeviceDefinitionProject) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDefinitionId() == null ? other.getDefinitionId() == null : this.getDefinitionId().equals(other.getDefinitionId()))
            && (this.getProjectName() == null ? other.getProjectName() == null : this.getProjectName().equals(other.getProjectName()))
            && (this.getProjectUnit() == null ? other.getProjectUnit() == null : this.getProjectUnit().equals(other.getProjectUnit()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsdel() == null ? other.getIsdel() == null : this.getIsdel().equals(other.getIsdel()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_definition_project
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDefinitionId() == null) ? 0 : getDefinitionId().hashCode());
        result = prime * result + ((getProjectName() == null) ? 0 : getProjectName().hashCode());
        result = prime * result + ((getProjectUnit() == null) ? 0 : getProjectUnit().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsdel() == null) ? 0 : getIsdel().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_definition_project
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
        sb.append(", definitionId=").append(definitionId);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectUnit=").append(projectUnit);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isdel=").append(isdel);
        sb.append("]");
        return sb.toString();
    }
}