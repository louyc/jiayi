package com.lifelight.api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcedureMessageExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public ProcedureMessageExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(String value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(String value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(String value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(String value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(String value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(String value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLike(String value) {
			addCriterion("id like", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotLike(String value) {
			addCriterion("id not like", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<String> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<String> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(String value1, String value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(String value1, String value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andDialogIdIsNull() {
			addCriterion("dialog_id is null");
			return (Criteria) this;
		}

		public Criteria andDialogIdIsNotNull() {
			addCriterion("dialog_id is not null");
			return (Criteria) this;
		}

		public Criteria andDialogIdEqualTo(String value) {
			addCriterion("dialog_id =", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdNotEqualTo(String value) {
			addCriterion("dialog_id <>", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdGreaterThan(String value) {
			addCriterion("dialog_id >", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdGreaterThanOrEqualTo(String value) {
			addCriterion("dialog_id >=", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdLessThan(String value) {
			addCriterion("dialog_id <", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdLessThanOrEqualTo(String value) {
			addCriterion("dialog_id <=", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdLike(String value) {
			addCriterion("dialog_id like", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdNotLike(String value) {
			addCriterion("dialog_id not like", value, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdIn(List<String> values) {
			addCriterion("dialog_id in", values, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdNotIn(List<String> values) {
			addCriterion("dialog_id not in", values, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdBetween(String value1, String value2) {
			addCriterion("dialog_id between", value1, value2, "dialogId");
			return (Criteria) this;
		}

		public Criteria andDialogIdNotBetween(String value1, String value2) {
			addCriterion("dialog_id not between", value1, value2, "dialogId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdIsNull() {
			addCriterion("procedure_id is null");
			return (Criteria) this;
		}

		public Criteria andProcedureIdIsNotNull() {
			addCriterion("procedure_id is not null");
			return (Criteria) this;
		}

		public Criteria andProcedureIdEqualTo(String value) {
			addCriterion("procedure_id =", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdNotEqualTo(String value) {
			addCriterion("procedure_id <>", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdGreaterThan(String value) {
			addCriterion("procedure_id >", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdGreaterThanOrEqualTo(String value) {
			addCriterion("procedure_id >=", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdLessThan(String value) {
			addCriterion("procedure_id <", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdLessThanOrEqualTo(String value) {
			addCriterion("procedure_id <=", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdLike(String value) {
			addCriterion("procedure_id like", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdNotLike(String value) {
			addCriterion("procedure_id not like", value, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdIn(List<String> values) {
			addCriterion("procedure_id in", values, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdNotIn(List<String> values) {
			addCriterion("procedure_id not in", values, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdBetween(String value1, String value2) {
			addCriterion("procedure_id between", value1, value2, "procedureId");
			return (Criteria) this;
		}

		public Criteria andProcedureIdNotBetween(String value1, String value2) {
			addCriterion("procedure_id not between", value1, value2, "procedureId");
			return (Criteria) this;
		}

		public Criteria andMessageFromIsNull() {
			addCriterion("message_from is null");
			return (Criteria) this;
		}

		public Criteria andMessageFromIsNotNull() {
			addCriterion("message_from is not null");
			return (Criteria) this;
		}

		public Criteria andMessageFromEqualTo(String value) {
			addCriterion("message_from =", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromNotEqualTo(String value) {
			addCriterion("message_from <>", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromGreaterThan(String value) {
			addCriterion("message_from >", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromGreaterThanOrEqualTo(String value) {
			addCriterion("message_from >=", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromLessThan(String value) {
			addCriterion("message_from <", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromLessThanOrEqualTo(String value) {
			addCriterion("message_from <=", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromLike(String value) {
			addCriterion("message_from like", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromNotLike(String value) {
			addCriterion("message_from not like", value, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromIn(List<String> values) {
			addCriterion("message_from in", values, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromNotIn(List<String> values) {
			addCriterion("message_from not in", values, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromBetween(String value1, String value2) {
			addCriterion("message_from between", value1, value2, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageFromNotBetween(String value1, String value2) {
			addCriterion("message_from not between", value1, value2, "messageFrom");
			return (Criteria) this;
		}

		public Criteria andMessageToIsNull() {
			addCriterion("message_to is null");
			return (Criteria) this;
		}

		public Criteria andMessageToIsNotNull() {
			addCriterion("message_to is not null");
			return (Criteria) this;
		}

		public Criteria andMessageToEqualTo(String value) {
			addCriterion("message_to =", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToNotEqualTo(String value) {
			addCriterion("message_to <>", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToGreaterThan(String value) {
			addCriterion("message_to >", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToGreaterThanOrEqualTo(String value) {
			addCriterion("message_to >=", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToLessThan(String value) {
			addCriterion("message_to <", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToLessThanOrEqualTo(String value) {
			addCriterion("message_to <=", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToLike(String value) {
			addCriterion("message_to like", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToNotLike(String value) {
			addCriterion("message_to not like", value, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToIn(List<String> values) {
			addCriterion("message_to in", values, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToNotIn(List<String> values) {
			addCriterion("message_to not in", values, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToBetween(String value1, String value2) {
			addCriterion("message_to between", value1, value2, "messageTo");
			return (Criteria) this;
		}

		public Criteria andMessageToNotBetween(String value1, String value2) {
			addCriterion("message_to not between", value1, value2, "messageTo");
			return (Criteria) this;
		}

		public Criteria andTypeIsNull() {
			addCriterion("type is null");
			return (Criteria) this;
		}

		public Criteria andTypeIsNotNull() {
			addCriterion("type is not null");
			return (Criteria) this;
		}

		public Criteria andTypeEqualTo(Integer value) {
			addCriterion("type =", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotEqualTo(Integer value) {
			addCriterion("type <>", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeGreaterThan(Integer value) {
			addCriterion("type >", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
			addCriterion("type >=", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeLessThan(Integer value) {
			addCriterion("type <", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeLessThanOrEqualTo(Integer value) {
			addCriterion("type <=", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeIn(List<Integer> values) {
			addCriterion("type in", values, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotIn(List<Integer> values) {
			addCriterion("type not in", values, "type");
			return (Criteria) this;
		}

		public Criteria andTypeBetween(Integer value1, Integer value2) {
			addCriterion("type between", value1, value2, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotBetween(Integer value1, Integer value2) {
			addCriterion("type not between", value1, value2, "type");
			return (Criteria) this;
		}

		public Criteria andContentIsNull() {
			addCriterion("content is null");
			return (Criteria) this;
		}

		public Criteria andContentIsNotNull() {
			addCriterion("content is not null");
			return (Criteria) this;
		}

		public Criteria andContentEqualTo(String value) {
			addCriterion("content =", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentNotEqualTo(String value) {
			addCriterion("content <>", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentGreaterThan(String value) {
			addCriterion("content >", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentGreaterThanOrEqualTo(String value) {
			addCriterion("content >=", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentLessThan(String value) {
			addCriterion("content <", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentLessThanOrEqualTo(String value) {
			addCriterion("content <=", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentLike(String value) {
			addCriterion("content like", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentNotLike(String value) {
			addCriterion("content not like", value, "content");
			return (Criteria) this;
		}

		public Criteria andContentIn(List<String> values) {
			addCriterion("content in", values, "content");
			return (Criteria) this;
		}

		public Criteria andContentNotIn(List<String> values) {
			addCriterion("content not in", values, "content");
			return (Criteria) this;
		}

		public Criteria andContentBetween(String value1, String value2) {
			addCriterion("content between", value1, value2, "content");
			return (Criteria) this;
		}

		public Criteria andContentNotBetween(String value1, String value2) {
			addCriterion("content not between", value1, value2, "content");
			return (Criteria) this;
		}

		public Criteria andUrlIsNull() {
			addCriterion("url is null");
			return (Criteria) this;
		}

		public Criteria andUrlIsNotNull() {
			addCriterion("url is not null");
			return (Criteria) this;
		}

		public Criteria andUrlEqualTo(String value) {
			addCriterion("url =", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotEqualTo(String value) {
			addCriterion("url <>", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlGreaterThan(String value) {
			addCriterion("url >", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlGreaterThanOrEqualTo(String value) {
			addCriterion("url >=", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlLessThan(String value) {
			addCriterion("url <", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlLessThanOrEqualTo(String value) {
			addCriterion("url <=", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlLike(String value) {
			addCriterion("url like", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotLike(String value) {
			addCriterion("url not like", value, "url");
			return (Criteria) this;
		}

		public Criteria andUrlIn(List<String> values) {
			addCriterion("url in", values, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotIn(List<String> values) {
			addCriterion("url not in", values, "url");
			return (Criteria) this;
		}

		public Criteria andUrlBetween(String value1, String value2) {
			addCriterion("url between", value1, value2, "url");
			return (Criteria) this;
		}

		public Criteria andUrlNotBetween(String value1, String value2) {
			addCriterion("url not between", value1, value2, "url");
			return (Criteria) this;
		}

		public Criteria andStatusIsNull() {
			addCriterion("status is null");
			return (Criteria) this;
		}

		public Criteria andStatusIsNotNull() {
			addCriterion("status is not null");
			return (Criteria) this;
		}

		public Criteria andStatusEqualTo(Integer value) {
			addCriterion("status =", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusNotEqualTo(Integer value) {
			addCriterion("status <>", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusGreaterThan(Integer value) {
			addCriterion("status >", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
			addCriterion("status >=", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusLessThan(Integer value) {
			addCriterion("status <", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusLessThanOrEqualTo(Integer value) {
			addCriterion("status <=", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusIn(List<Integer> values) {
			addCriterion("status in", values, "status");
			return (Criteria) this;
		}

		public Criteria andStatusNotIn(List<Integer> values) {
			addCriterion("status not in", values, "status");
			return (Criteria) this;
		}

		public Criteria andStatusBetween(Integer value1, Integer value2) {
			addCriterion("status between", value1, value2, "status");
			return (Criteria) this;
		}

		public Criteria andStatusNotBetween(Integer value1, Integer value2) {
			addCriterion("status not between", value1, value2, "status");
			return (Criteria) this;
		}

		public Criteria andCronIsNull() {
			addCriterion("cron is null");
			return (Criteria) this;
		}

		public Criteria andCronIsNotNull() {
			addCriterion("cron is not null");
			return (Criteria) this;
		}

		public Criteria andCronEqualTo(String value) {
			addCriterion("cron =", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronNotEqualTo(String value) {
			addCriterion("cron <>", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronGreaterThan(String value) {
			addCriterion("cron >", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronGreaterThanOrEqualTo(String value) {
			addCriterion("cron >=", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronLessThan(String value) {
			addCriterion("cron <", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronLessThanOrEqualTo(String value) {
			addCriterion("cron <=", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronLike(String value) {
			addCriterion("cron like", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronNotLike(String value) {
			addCriterion("cron not like", value, "cron");
			return (Criteria) this;
		}

		public Criteria andCronIn(List<String> values) {
			addCriterion("cron in", values, "cron");
			return (Criteria) this;
		}

		public Criteria andCronNotIn(List<String> values) {
			addCriterion("cron not in", values, "cron");
			return (Criteria) this;
		}

		public Criteria andCronBetween(String value1, String value2) {
			addCriterion("cron between", value1, value2, "cron");
			return (Criteria) this;
		}

		public Criteria andCronNotBetween(String value1, String value2) {
			addCriterion("cron not between", value1, value2, "cron");
			return (Criteria) this;
		}

		public Criteria andSendCountIsNull() {
			addCriterion("send_count is null");
			return (Criteria) this;
		}

		public Criteria andSendCountIsNotNull() {
			addCriterion("send_count is not null");
			return (Criteria) this;
		}

		public Criteria andSendCountEqualTo(Integer value) {
			addCriterion("send_count =", value, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountNotEqualTo(Integer value) {
			addCriterion("send_count <>", value, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountGreaterThan(Integer value) {
			addCriterion("send_count >", value, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountGreaterThanOrEqualTo(Integer value) {
			addCriterion("send_count >=", value, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountLessThan(Integer value) {
			addCriterion("send_count <", value, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountLessThanOrEqualTo(Integer value) {
			addCriterion("send_count <=", value, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountIn(List<Integer> values) {
			addCriterion("send_count in", values, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountNotIn(List<Integer> values) {
			addCriterion("send_count not in", values, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountBetween(Integer value1, Integer value2) {
			addCriterion("send_count between", value1, value2, "sendCount");
			return (Criteria) this;
		}

		public Criteria andSendCountNotBetween(Integer value1, Integer value2) {
			addCriterion("send_count not between", value1, value2, "sendCount");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIsNull() {
			addCriterion("create_time is null");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIsNotNull() {
			addCriterion("create_time is not null");
			return (Criteria) this;
		}

		public Criteria andCreateTimeEqualTo(Date value) {
			addCriterion("create_time =", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotEqualTo(Date value) {
			addCriterion("create_time <>", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThan(Date value) {
			addCriterion("create_time >", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("create_time >=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThan(Date value) {
			addCriterion("create_time <", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
			addCriterion("create_time <=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIn(List<Date> values) {
			addCriterion("create_time in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotIn(List<Date> values) {
			addCriterion("create_time not in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeBetween(Date value1, Date value2) {
			addCriterion("create_time between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
			addCriterion("create_time not between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIsNull() {
			addCriterion("update_time is null");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIsNotNull() {
			addCriterion("update_time is not null");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeEqualTo(Date value) {
			addCriterion("update_time =", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotEqualTo(Date value) {
			addCriterion("update_time <>", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeGreaterThan(Date value) {
			addCriterion("update_time >", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("update_time >=", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeLessThan(Date value) {
			addCriterion("update_time <", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
			addCriterion("update_time <=", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIn(List<Date> values) {
			addCriterion("update_time in", values, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotIn(List<Date> values) {
			addCriterion("update_time not in", values, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeBetween(Date value1, Date value2) {
			addCriterion("update_time between", value1, value2, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
			addCriterion("update_time not between", value1, value2, "updateTime");
			return (Criteria) this;
		}

		public Criteria andInUseIsNull() {
			addCriterion("in_use is null");
			return (Criteria) this;
		}

		public Criteria andInUseIsNotNull() {
			addCriterion("in_use is not null");
			return (Criteria) this;
		}

		public Criteria andInUseEqualTo(Integer value) {
			addCriterion("in_use =", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseNotEqualTo(Integer value) {
			addCriterion("in_use <>", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseGreaterThan(Integer value) {
			addCriterion("in_use >", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseGreaterThanOrEqualTo(Integer value) {
			addCriterion("in_use >=", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseLessThan(Integer value) {
			addCriterion("in_use <", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseLessThanOrEqualTo(Integer value) {
			addCriterion("in_use <=", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseIn(List<Integer> values) {
			addCriterion("in_use in", values, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseNotIn(List<Integer> values) {
			addCriterion("in_use not in", values, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseBetween(Integer value1, Integer value2) {
			addCriterion("in_use between", value1, value2, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseNotBetween(Integer value1, Integer value2) {
			addCriterion("in_use not between", value1, value2, "inUse");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table procedure_message
	 * @mbg.generated
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table procedure_message
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}