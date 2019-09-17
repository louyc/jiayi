package com.lifelight.api.entity;

import java.util.ArrayList;
import java.util.List;

public class HandleLogExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table handle_log
	 * @mbg.generated
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table handle_log
	 * @mbg.generated
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table handle_log
	 * @mbg.generated
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public HandleLogExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
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
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table handle_log
	 * @mbg.generated
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table handle_log
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

		public Criteria andDataIdIsNull() {
			addCriterion("data_id is null");
			return (Criteria) this;
		}

		public Criteria andDataIdIsNotNull() {
			addCriterion("data_id is not null");
			return (Criteria) this;
		}

		public Criteria andDataIdEqualTo(Integer value) {
			addCriterion("data_id =", value, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdNotEqualTo(Integer value) {
			addCriterion("data_id <>", value, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdGreaterThan(Integer value) {
			addCriterion("data_id >", value, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("data_id >=", value, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdLessThan(Integer value) {
			addCriterion("data_id <", value, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdLessThanOrEqualTo(Integer value) {
			addCriterion("data_id <=", value, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdIn(List<Integer> values) {
			addCriterion("data_id in", values, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdNotIn(List<Integer> values) {
			addCriterion("data_id not in", values, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdBetween(Integer value1, Integer value2) {
			addCriterion("data_id between", value1, value2, "dataId");
			return (Criteria) this;
		}

		public Criteria andDataIdNotBetween(Integer value1, Integer value2) {
			addCriterion("data_id not between", value1, value2, "dataId");
			return (Criteria) this;
		}

		public Criteria andTableNameIsNull() {
			addCriterion("table_name is null");
			return (Criteria) this;
		}

		public Criteria andTableNameIsNotNull() {
			addCriterion("table_name is not null");
			return (Criteria) this;
		}

		public Criteria andTableNameEqualTo(String value) {
			addCriterion("table_name =", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameNotEqualTo(String value) {
			addCriterion("table_name <>", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameGreaterThan(String value) {
			addCriterion("table_name >", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameGreaterThanOrEqualTo(String value) {
			addCriterion("table_name >=", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameLessThan(String value) {
			addCriterion("table_name <", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameLessThanOrEqualTo(String value) {
			addCriterion("table_name <=", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameLike(String value) {
			addCriterion("table_name like", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameNotLike(String value) {
			addCriterion("table_name not like", value, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameIn(List<String> values) {
			addCriterion("table_name in", values, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameNotIn(List<String> values) {
			addCriterion("table_name not in", values, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameBetween(String value1, String value2) {
			addCriterion("table_name between", value1, value2, "tableName");
			return (Criteria) this;
		}

		public Criteria andTableNameNotBetween(String value1, String value2) {
			addCriterion("table_name not between", value1, value2, "tableName");
			return (Criteria) this;
		}

		public Criteria andHandleTypeIsNull() {
			addCriterion("handle_type is null");
			return (Criteria) this;
		}

		public Criteria andHandleTypeIsNotNull() {
			addCriterion("handle_type is not null");
			return (Criteria) this;
		}

		public Criteria andHandleTypeEqualTo(String value) {
			addCriterion("handle_type =", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeNotEqualTo(String value) {
			addCriterion("handle_type <>", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeGreaterThan(String value) {
			addCriterion("handle_type >", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeGreaterThanOrEqualTo(String value) {
			addCriterion("handle_type >=", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeLessThan(String value) {
			addCriterion("handle_type <", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeLessThanOrEqualTo(String value) {
			addCriterion("handle_type <=", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeLike(String value) {
			addCriterion("handle_type like", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeNotLike(String value) {
			addCriterion("handle_type not like", value, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeIn(List<String> values) {
			addCriterion("handle_type in", values, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeNotIn(List<String> values) {
			addCriterion("handle_type not in", values, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeBetween(String value1, String value2) {
			addCriterion("handle_type between", value1, value2, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleTypeNotBetween(String value1, String value2) {
			addCriterion("handle_type not between", value1, value2, "handleType");
			return (Criteria) this;
		}

		public Criteria andHandleUserIsNull() {
			addCriterion("handle_user is null");
			return (Criteria) this;
		}

		public Criteria andHandleUserIsNotNull() {
			addCriterion("handle_user is not null");
			return (Criteria) this;
		}

		public Criteria andHandleUserEqualTo(String value) {
			addCriterion("handle_user =", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserNotEqualTo(String value) {
			addCriterion("handle_user <>", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserGreaterThan(String value) {
			addCriterion("handle_user >", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserGreaterThanOrEqualTo(String value) {
			addCriterion("handle_user >=", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserLessThan(String value) {
			addCriterion("handle_user <", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserLessThanOrEqualTo(String value) {
			addCriterion("handle_user <=", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserLike(String value) {
			addCriterion("handle_user like", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserNotLike(String value) {
			addCriterion("handle_user not like", value, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserIn(List<String> values) {
			addCriterion("handle_user in", values, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserNotIn(List<String> values) {
			addCriterion("handle_user not in", values, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserBetween(String value1, String value2) {
			addCriterion("handle_user between", value1, value2, "handleUser");
			return (Criteria) this;
		}

		public Criteria andHandleUserNotBetween(String value1, String value2) {
			addCriterion("handle_user not between", value1, value2, "handleUser");
			return (Criteria) this;
		}

		public Criteria andMassageIsNull() {
			addCriterion("massage is null");
			return (Criteria) this;
		}

		public Criteria andMassageIsNotNull() {
			addCriterion("massage is not null");
			return (Criteria) this;
		}

		public Criteria andMassageEqualTo(String value) {
			addCriterion("massage =", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageNotEqualTo(String value) {
			addCriterion("massage <>", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageGreaterThan(String value) {
			addCriterion("massage >", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageGreaterThanOrEqualTo(String value) {
			addCriterion("massage >=", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageLessThan(String value) {
			addCriterion("massage <", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageLessThanOrEqualTo(String value) {
			addCriterion("massage <=", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageLike(String value) {
			addCriterion("massage like", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageNotLike(String value) {
			addCriterion("massage not like", value, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageIn(List<String> values) {
			addCriterion("massage in", values, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageNotIn(List<String> values) {
			addCriterion("massage not in", values, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageBetween(String value1, String value2) {
			addCriterion("massage between", value1, value2, "massage");
			return (Criteria) this;
		}

		public Criteria andMassageNotBetween(String value1, String value2) {
			addCriterion("massage not between", value1, value2, "massage");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table handle_log
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
     * This class corresponds to the database table handle_log
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}