package com.lifelight.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.common.pageUtil.PageInfo;

public class ReportInfo extends PageInfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_info.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_info.order_id
     *
     * @mbg.generated
     */
    private Integer orderId;

    private String dialogId;
    
    public String getDialogId() {
		return dialogId;
	}

	public void setDialogId(String dialogId) {
		this.dialogId = dialogId;
	}
    
	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_info.create_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_info.update_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_info.report_desc
     *
     * @mbg.generated
     */
    private String reportDesc;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_info.id
     *
     * @return the value of report_info.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_info.id
     *
     * @param id the value for report_info.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_info.order_id
     *
     * @return the value of report_info.order_id
     *
     * @mbg.generated
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_info.order_id
     *
     * @param orderId the value for report_info.order_id
     *
     * @mbg.generated
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_info.create_time
     *
     * @return the value of report_info.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_info.create_time
     *
     * @param createTime the value for report_info.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_info.update_time
     *
     * @return the value of report_info.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_info.update_time
     *
     * @param updateTime the value for report_info.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_info.report_desc
     *
     * @return the value of report_info.report_desc
     *
     * @mbg.generated
     */
    public String getReportDesc() {
        return reportDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_info.report_desc
     *
     * @param reportDesc the value for report_info.report_desc
     *
     * @mbg.generated
     */
    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc == null ? null : reportDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_info
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
        ReportInfo other = (ReportInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReportDesc() == null ? other.getReportDesc() == null : this.getReportDesc().equals(other.getReportDesc()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_info
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getReportDesc() == null) ? 0 : getReportDesc().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_info
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
        sb.append(", orderId=").append(orderId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reportDesc=").append(reportDesc);
        sb.append("]");
        return sb.toString();
    }
}