package com.lifelight.api.entity;

import java.io.Serializable;

public class OrgCommunityInfo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_community_info.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_community_info.org_id
     *
     * @mbg.generated
     */
    private String orgId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_community_info.community_name
     *
     * @mbg.generated
     */
    private String communityName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column org_community_info.in_use
     *
     * @mbg.generated
     */
    private Integer inUse;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_community_info.id
     *
     * @return the value of org_community_info.id
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_community_info.id
     *
     * @param id the value for org_community_info.id
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_community_info.org_id
     *
     * @return the value of org_community_info.org_id
     * @mbg.generated
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_community_info.org_id
     *
     * @param orgId the value for org_community_info.org_id
     * @mbg.generated
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_community_info.community_name
     *
     * @return the value of org_community_info.community_name
     * @mbg.generated
     */
    public String getCommunityName() {
        return communityName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_community_info.community_name
     *
     * @param communityName the value for org_community_info.community_name
     * @mbg.generated
     */
    public void setCommunityName(String communityName) {
        this.communityName = communityName == null ? null : communityName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column org_community_info.in_use
     *
     * @return the value of org_community_info.in_use
     * @mbg.generated
     */
    public Integer getInUse() {
        return inUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column org_community_info.in_use
     *
     * @param inUse the value for org_community_info.in_use
     * @mbg.generated
     */
    public void setInUse(Integer inUse) {
        this.inUse = inUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org_community_info
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
        OrgCommunityInfo other = (OrgCommunityInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOrgId() == null ? other.getOrgId() == null : this.getOrgId().equals(other.getOrgId()))
                && (this.getCommunityName() == null ? other.getCommunityName() == null : this.getCommunityName().equals(other.getCommunityName()))
                && (this.getInUse() == null ? other.getInUse() == null : this.getInUse().equals(other.getInUse()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org_community_info
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrgId() == null) ? 0 : getOrgId().hashCode());
        result = prime * result + ((getCommunityName() == null) ? 0 : getCommunityName().hashCode());
        result = prime * result + ((getInUse() == null) ? 0 : getInUse().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table org_community_info
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
        sb.append(", orgId=").append(orgId);
        sb.append(", communityName=").append(communityName);
        sb.append(", inUse=").append(inUse);
        sb.append("]");
        return sb.toString();
    }
}