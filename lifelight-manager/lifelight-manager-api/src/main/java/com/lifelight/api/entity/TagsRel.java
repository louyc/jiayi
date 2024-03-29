package com.lifelight.api.entity;

import java.io.Serializable;

public class TagsRel implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags_rel.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags_rel.manager_id
     *
     * @mbg.generated
     */
    private String managerId;
    
    private Integer platformId;
    

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags_rel.tag_id
     *
     * @mbg.generated
     */
    private Integer tagId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags_rel.type
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags_rel.id
     *
     * @return the value of tags_rel.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags_rel.id
     *
     * @param id the value for tags_rel.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags_rel.manager_id
     *
     * @return the value of tags_rel.manager_id
     *
     * @mbg.generated
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags_rel.manager_id
     *
     * @param managerId the value for tags_rel.manager_id
     *
     * @mbg.generated
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId == null ? null : managerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags_rel.tag_id
     *
     * @return the value of tags_rel.tag_id
     *
     * @mbg.generated
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags_rel.tag_id
     *
     * @param tagId the value for tags_rel.tag_id
     *
     * @mbg.generated
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags_rel.type
     *
     * @return the value of tags_rel.type
     *
     * @mbg.generated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags_rel.type
     *
     * @param type the value for tags_rel.type
     *
     * @mbg.generated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tags_rel
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
        TagsRel other = (TagsRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getManagerId() == null ? other.getManagerId() == null : this.getManagerId().equals(other.getManagerId()))
            && (this.getTagId() == null ? other.getTagId() == null : this.getTagId().equals(other.getTagId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tags_rel
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getManagerId() == null) ? 0 : getManagerId().hashCode());
        result = prime * result + ((getTagId() == null) ? 0 : getTagId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tags_rel
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
        sb.append(", managerId=").append(managerId);
        sb.append(", tagId=").append(tagId);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }
}