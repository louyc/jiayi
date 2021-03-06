package com.lifelight.api.entity;

import java.util.Date;

public class ServicePackageRel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_package_rel.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_package_rel.package_id
     *
     * @mbg.generated
     */
    private Integer packageId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_package_rel.dictionary_id
     *
     * @mbg.generated
     */
    private Integer dictionaryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_package_rel.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column service_package_rel.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_package_rel.id
     *
     * @return the value of service_package_rel.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_package_rel.id
     *
     * @param id the value for service_package_rel.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_package_rel.package_id
     *
     * @return the value of service_package_rel.package_id
     *
     * @mbg.generated
     */
    public Integer getPackageId() {
        return packageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_package_rel.package_id
     *
     * @param packageId the value for service_package_rel.package_id
     *
     * @mbg.generated
     */
    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_package_rel.dictionary_id
     *
     * @return the value of service_package_rel.dictionary_id
     *
     * @mbg.generated
     */
    public Integer getDictionaryId() {
        return dictionaryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_package_rel.dictionary_id
     *
     * @param dictionaryId the value for service_package_rel.dictionary_id
     *
     * @mbg.generated
     */
    public void setDictionaryId(Integer dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_package_rel.create_time
     *
     * @return the value of service_package_rel.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_package_rel.create_time
     *
     * @param createTime the value for service_package_rel.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column service_package_rel.update_time
     *
     * @return the value of service_package_rel.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column service_package_rel.update_time
     *
     * @param updateTime the value for service_package_rel.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table service_package_rel
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
        ServicePackageRel other = (ServicePackageRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPackageId() == null ? other.getPackageId() == null : this.getPackageId().equals(other.getPackageId()))
            && (this.getDictionaryId() == null ? other.getDictionaryId() == null : this.getDictionaryId().equals(other.getDictionaryId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table service_package_rel
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPackageId() == null) ? 0 : getPackageId().hashCode());
        result = prime * result + ((getDictionaryId() == null) ? 0 : getDictionaryId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table service_package_rel
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
        sb.append(", packageId=").append(packageId);
        sb.append(", dictionaryId=").append(dictionaryId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}