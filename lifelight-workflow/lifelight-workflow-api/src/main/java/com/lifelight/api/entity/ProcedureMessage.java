package com.lifelight.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.common.pageUtil.PageInfo;

public class ProcedureMessage extends PageInfo {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.id
	 * @mbg.generated
	 */
	private String id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.dialog_id
	 * @mbg.generated
	 */
	private String dialogId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.procedure_id
	 * @mbg.generated
	 */
	private String procedureId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.message_from
	 * @mbg.generated
	 */
	private String messageFrom;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.message_to
	 * @mbg.generated
	 */
	private String messageTo;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.type
	 * @mbg.generated
	 */
	private Integer type;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.content
	 * @mbg.generated
	 */
	private String content;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.url
	 * @mbg.generated
	 */
	private String url;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.status
	 * @mbg.generated
	 */
	private Integer status;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.cron
	 * @mbg.generated
	 */
	private String cron;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.send_count
	 * @mbg.generated
	 */
	private Integer sendCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.create_time
	 * @mbg.generated
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.update_time
	 * @mbg.generated
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column procedure_message.in_use
	 * @mbg.generated
	 */
	private Integer inUse;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.id
	 * @return  the value of procedure_message.id
	 * @mbg.generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.id
	 * @param id  the value for procedure_message.id
	 * @mbg.generated
	 */
	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.dialog_id
	 * @return  the value of procedure_message.dialog_id
	 * @mbg.generated
	 */
	public String getDialogId() {
		return dialogId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.dialog_id
	 * @param dialogId  the value for procedure_message.dialog_id
	 * @mbg.generated
	 */
	public void setDialogId(String dialogId) {
		this.dialogId = dialogId == null ? null : dialogId.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.procedure_id
	 * @return  the value of procedure_message.procedure_id
	 * @mbg.generated
	 */
	public String getProcedureId() {
		return procedureId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.procedure_id
	 * @param procedureId  the value for procedure_message.procedure_id
	 * @mbg.generated
	 */
	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId == null ? null : procedureId.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.message_from
	 * @return  the value of procedure_message.message_from
	 * @mbg.generated
	 */
	public String getMessageFrom() {
		return messageFrom;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.message_from
	 * @param messageFrom  the value for procedure_message.message_from
	 * @mbg.generated
	 */
	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom == null ? null : messageFrom.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.message_to
	 * @return  the value of procedure_message.message_to
	 * @mbg.generated
	 */
	public String getMessageTo() {
		return messageTo;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.message_to
	 * @param messageTo  the value for procedure_message.message_to
	 * @mbg.generated
	 */
	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo == null ? null : messageTo.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.type
	 * @return  the value of procedure_message.type
	 * @mbg.generated
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.type
	 * @param type  the value for procedure_message.type
	 * @mbg.generated
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.content
	 * @return  the value of procedure_message.content
	 * @mbg.generated
	 */
	public String getContent() {
		return content;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.content
	 * @param content  the value for procedure_message.content
	 * @mbg.generated
	 */
	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.url
	 * @return  the value of procedure_message.url
	 * @mbg.generated
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.url
	 * @param url  the value for procedure_message.url
	 * @mbg.generated
	 */
	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.status
	 * @return  the value of procedure_message.status
	 * @mbg.generated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.status
	 * @param status  the value for procedure_message.status
	 * @mbg.generated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.cron
	 * @return  the value of procedure_message.cron
	 * @mbg.generated
	 */
	public String getCron() {
		return cron;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.cron
	 * @param cron  the value for procedure_message.cron
	 * @mbg.generated
	 */
	public void setCron(String cron) {
		this.cron = cron == null ? null : cron.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.send_count
	 * @return  the value of procedure_message.send_count
	 * @mbg.generated
	 */
	public Integer getSendCount() {
		return sendCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.send_count
	 * @param sendCount  the value for procedure_message.send_count
	 * @mbg.generated
	 */
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.create_time
	 * @return  the value of procedure_message.create_time
	 * @mbg.generated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.create_time
	 * @param createTime  the value for procedure_message.create_time
	 * @mbg.generated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.update_time
	 * @return  the value of procedure_message.update_time
	 * @mbg.generated
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.update_time
	 * @param updateTime  the value for procedure_message.update_time
	 * @mbg.generated
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column procedure_message.in_use
	 * @return  the value of procedure_message.in_use
	 * @mbg.generated
	 */
	public Integer getInUse() {
		return inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column procedure_message.in_use
	 * @param inUse  the value for procedure_message.in_use
	 * @mbg.generated
	 */
	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
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
		ProcedureMessage other = (ProcedureMessage) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getDialogId() == null ? other.getDialogId() == null
						: this.getDialogId().equals(other.getDialogId()))
				&& (this.getProcedureId() == null ? other.getProcedureId() == null
						: this.getProcedureId().equals(other.getProcedureId()))
				&& (this.getMessageFrom() == null ? other.getMessageFrom() == null
						: this.getMessageFrom().equals(other.getMessageFrom()))
				&& (this.getMessageTo() == null ? other.getMessageTo() == null
						: this.getMessageTo().equals(other.getMessageTo()))
				&& (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
				&& (this.getContent() == null ? other.getContent() == null
						: this.getContent().equals(other.getContent()))
				&& (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
				&& (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
				&& (this.getCron() == null ? other.getCron() == null : this.getCron().equals(other.getCron()))
				&& (this.getSendCount() == null ? other.getSendCount() == null
						: this.getSendCount().equals(other.getSendCount()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()))
				&& (this.getUpdateTime() == null ? other.getUpdateTime() == null
						: this.getUpdateTime().equals(other.getUpdateTime()))
				&& (this.getInUse() == null ? other.getInUse() == null : this.getInUse().equals(other.getInUse()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getDialogId() == null) ? 0 : getDialogId().hashCode());
		result = prime * result + ((getProcedureId() == null) ? 0 : getProcedureId().hashCode());
		result = prime * result + ((getMessageFrom() == null) ? 0 : getMessageFrom().hashCode());
		result = prime * result + ((getMessageTo() == null) ? 0 : getMessageTo().hashCode());
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
		result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
		result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
		result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
		result = prime * result + ((getCron() == null) ? 0 : getCron().hashCode());
		result = prime * result + ((getSendCount() == null) ? 0 : getSendCount().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
		result = prime * result + ((getInUse() == null) ? 0 : getInUse().hashCode());
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", dialogId=").append(dialogId);
		sb.append(", procedureId=").append(procedureId);
		sb.append(", messageFrom=").append(messageFrom);
		sb.append(", messageTo=").append(messageTo);
		sb.append(", type=").append(type);
		sb.append(", content=").append(content);
		sb.append(", url=").append(url);
		sb.append(", status=").append(status);
		sb.append(", cron=").append(cron);
		sb.append(", sendCount=").append(sendCount);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", inUse=").append(inUse);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3888803262365547305L;
}