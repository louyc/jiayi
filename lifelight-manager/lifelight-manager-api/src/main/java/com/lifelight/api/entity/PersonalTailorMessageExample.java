package com.lifelight.api.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalTailorMessageExample implements Serializable {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	protected String orderByClause;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	protected boolean distinct;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public PersonalTailorMessageExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
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
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table personal_tailor_message
	 *
	 * @mbg.generated
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the
	 * database table personal_tailor_message
	 *
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

		public Criteria andIdEqualTo(Integer value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Integer value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Integer value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Integer value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Integer value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Integer> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Integer> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Integer value1, Integer value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Integer value1, Integer value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andApplyTypeIsNull() {
			addCriterion("apply_type is null");
			return (Criteria) this;
		}

		public Criteria andApplyTypeIsNotNull() {
			addCriterion("apply_type is not null");
			return (Criteria) this;
		}

		public Criteria andApplyTypeEqualTo(Integer value) {
			addCriterion("apply_type =", value, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeNotEqualTo(Integer value) {
			addCriterion("apply_type <>", value, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeGreaterThan(Integer value) {
			addCriterion("apply_type >", value, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeGreaterThanOrEqualTo(Integer value) {
			addCriterion("apply_type >=", value, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeLessThan(Integer value) {
			addCriterion("apply_type <", value, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeLessThanOrEqualTo(Integer value) {
			addCriterion("apply_type <=", value, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeIn(List<Integer> values) {
			addCriterion("apply_type in", values, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeNotIn(List<Integer> values) {
			addCriterion("apply_type not in", values, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeBetween(Integer value1, Integer value2) {
			addCriterion("apply_type between", value1, value2, "applyType");
			return (Criteria) this;
		}

		public Criteria andApplyTypeNotBetween(Integer value1, Integer value2) {
			addCriterion("apply_type not between", value1, value2, "applyType");
			return (Criteria) this;
		}

		public Criteria andPersonalIdIsNull() {
			addCriterion("personalId is null");
			return (Criteria) this;
		}

		public Criteria andPersonalIdIsNotNull() {
			addCriterion("personalId is not null");
			return (Criteria) this;
		}

		public Criteria andPersonalIdEqualTo(String value) {
			addCriterion("personalId =", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdNotEqualTo(String value) {
			addCriterion("personalId <>", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdGreaterThan(String value) {
			addCriterion("personalId >", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdGreaterThanOrEqualTo(String value) {
			addCriterion("personalId >=", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdLessThan(String value) {
			addCriterion("personalId <", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdLessThanOrEqualTo(String value) {
			addCriterion("personalId <=", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdLike(String value) {
			addCriterion("personalId like", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdNotLike(String value) {
			addCriterion("personalId not like", value, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdIn(List<String> values) {
			addCriterion("personalId in", values, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdNotIn(List<String> values) {
			addCriterion("personalId not in", values, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdBetween(String value1, String value2) {
			addCriterion("personalId between", value1, value2, "personalId");
			return (Criteria) this;
		}

		public Criteria andPersonalIdNotBetween(String value1, String value2) {
			addCriterion("personalId not between", value1, value2, "personalId");
			return (Criteria) this;
		}

		public Criteria andManagerIdIsNull() {
			addCriterion("manager_id is null");
			return (Criteria) this;
		}

		public Criteria andManagerIdIsNotNull() {
			addCriterion("manager_id is not null");
			return (Criteria) this;
		}

		public Criteria andManagerIdEqualTo(String value) {
			addCriterion("manager_id =", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotEqualTo(String value) {
			addCriterion("manager_id <>", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdGreaterThan(String value) {
			addCriterion("manager_id >", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdGreaterThanOrEqualTo(String value) {
			addCriterion("manager_id >=", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdLessThan(String value) {
			addCriterion("manager_id <", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdLessThanOrEqualTo(String value) {
			addCriterion("manager_id <=", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdLike(String value) {
			addCriterion("manager_id like", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotLike(String value) {
			addCriterion("manager_id not like", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdIn(List<String> values) {
			addCriterion("manager_id in", values, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotIn(List<String> values) {
			addCriterion("manager_id not in", values, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdBetween(String value1, String value2) {
			addCriterion("manager_id between", value1, value2, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotBetween(String value1, String value2) {
			addCriterion("manager_id not between", value1, value2, "managerId");
			return (Criteria) this;
		}

		public Criteria andNameIsNull() {
			addCriterion("name is null");
			return (Criteria) this;
		}

		public Criteria andNameIsNotNull() {
			addCriterion("name is not null");
			return (Criteria) this;
		}

		public Criteria andNameEqualTo(String value) {
			addCriterion("name =", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotEqualTo(String value) {
			addCriterion("name <>", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameGreaterThan(String value) {
			addCriterion("name >", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameGreaterThanOrEqualTo(String value) {
			addCriterion("name >=", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLessThan(String value) {
			addCriterion("name <", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLessThanOrEqualTo(String value) {
			addCriterion("name <=", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLike(String value) {
			addCriterion("name like", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotLike(String value) {
			addCriterion("name not like", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameIn(List<String> values) {
			addCriterion("name in", values, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotIn(List<String> values) {
			addCriterion("name not in", values, "name");
			return (Criteria) this;
		}

		public Criteria andNameBetween(String value1, String value2) {
			addCriterion("name between", value1, value2, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotBetween(String value1, String value2) {
			addCriterion("name not between", value1, value2, "name");
			return (Criteria) this;
		}

		public Criteria andMobileIsNull() {
			addCriterion("mobile is null");
			return (Criteria) this;
		}

		public Criteria andMobileIsNotNull() {
			addCriterion("mobile is not null");
			return (Criteria) this;
		}

		public Criteria andMobileEqualTo(String value) {
			addCriterion("mobile =", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotEqualTo(String value) {
			addCriterion("mobile <>", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileGreaterThan(String value) {
			addCriterion("mobile >", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileGreaterThanOrEqualTo(String value) {
			addCriterion("mobile >=", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileLessThan(String value) {
			addCriterion("mobile <", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileLessThanOrEqualTo(String value) {
			addCriterion("mobile <=", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileLike(String value) {
			addCriterion("mobile like", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotLike(String value) {
			addCriterion("mobile not like", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileIn(List<String> values) {
			addCriterion("mobile in", values, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotIn(List<String> values) {
			addCriterion("mobile not in", values, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileBetween(String value1, String value2) {
			addCriterion("mobile between", value1, value2, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotBetween(String value1, String value2) {
			addCriterion("mobile not between", value1, value2, "mobile");
			return (Criteria) this;
		}

		public Criteria andIsReadIsNull() {
			addCriterion("is_read is null");
			return (Criteria) this;
		}

		public Criteria andIsReadIsNotNull() {
			addCriterion("is_read is not null");
			return (Criteria) this;
		}

		public Criteria andIsReadEqualTo(Integer value) {
			addCriterion("is_read =", value, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadNotEqualTo(Integer value) {
			addCriterion("is_read <>", value, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadGreaterThan(Integer value) {
			addCriterion("is_read >", value, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadGreaterThanOrEqualTo(Integer value) {
			addCriterion("is_read >=", value, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadLessThan(Integer value) {
			addCriterion("is_read <", value, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadLessThanOrEqualTo(Integer value) {
			addCriterion("is_read <=", value, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadIn(List<Integer> values) {
			addCriterion("is_read in", values, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadNotIn(List<Integer> values) {
			addCriterion("is_read not in", values, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadBetween(Integer value1, Integer value2) {
			addCriterion("is_read between", value1, value2, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsReadNotBetween(Integer value1, Integer value2) {
			addCriterion("is_read not between", value1, value2, "isRead");
			return (Criteria) this;
		}

		public Criteria andIsHandleIsNull() {
			addCriterion("is_handle is null");
			return (Criteria) this;
		}

		public Criteria andIsHandleIsNotNull() {
			addCriterion("is_handle is not null");
			return (Criteria) this;
		}

		public Criteria andIsHandleEqualTo(Integer value) {
			addCriterion("is_handle =", value, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleNotEqualTo(Integer value) {
			addCriterion("is_handle <>", value, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleGreaterThan(Integer value) {
			addCriterion("is_handle >", value, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleGreaterThanOrEqualTo(Integer value) {
			addCriterion("is_handle >=", value, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleLessThan(Integer value) {
			addCriterion("is_handle <", value, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleLessThanOrEqualTo(Integer value) {
			addCriterion("is_handle <=", value, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleIn(List<Integer> values) {
			addCriterion("is_handle in", values, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleNotIn(List<Integer> values) {
			addCriterion("is_handle not in", values, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleBetween(Integer value1, Integer value2) {
			addCriterion("is_handle between", value1, value2, "isHandle");
			return (Criteria) this;
		}

		public Criteria andIsHandleNotBetween(Integer value1, Integer value2) {
			addCriterion("is_handle not between", value1, value2, "isHandle");
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
	 * This class was generated by MyBatis Generator. This class corresponds to the
	 * database table personal_tailor_message
	 *
	 * @mbg.generated do_not_delete_during_merge
	 */
	public static class Criteria extends GeneratedCriteria {

		protected Criteria() {
			super();
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the
	 * database table personal_tailor_message
	 *
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
}