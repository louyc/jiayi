package com.lifelight.api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceDefinitionExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table device_definition
	 * @mbg.generated
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table device_definition
	 * @mbg.generated
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table device_definition
	 * @mbg.generated
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public DeviceDefinitionExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
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
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_definition
	 * @mbg.generated
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table device_definition
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

		public Criteria andDeviceFirmidIsNull() {
			addCriterion("device_firmId is null");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidIsNotNull() {
			addCriterion("device_firmId is not null");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidEqualTo(Integer value) {
			addCriterion("device_firmId =", value, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidNotEqualTo(Integer value) {
			addCriterion("device_firmId <>", value, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidGreaterThan(Integer value) {
			addCriterion("device_firmId >", value, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidGreaterThanOrEqualTo(Integer value) {
			addCriterion("device_firmId >=", value, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidLessThan(Integer value) {
			addCriterion("device_firmId <", value, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidLessThanOrEqualTo(Integer value) {
			addCriterion("device_firmId <=", value, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidIn(List<Integer> values) {
			addCriterion("device_firmId in", values, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidNotIn(List<Integer> values) {
			addCriterion("device_firmId not in", values, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidBetween(Integer value1, Integer value2) {
			addCriterion("device_firmId between", value1, value2, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceFirmidNotBetween(Integer value1, Integer value2) {
			addCriterion("device_firmId not between", value1, value2, "deviceFirmid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidIsNull() {
			addCriterion("device_brandId is null");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidIsNotNull() {
			addCriterion("device_brandId is not null");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidEqualTo(Integer value) {
			addCriterion("device_brandId =", value, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidNotEqualTo(Integer value) {
			addCriterion("device_brandId <>", value, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidGreaterThan(Integer value) {
			addCriterion("device_brandId >", value, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidGreaterThanOrEqualTo(Integer value) {
			addCriterion("device_brandId >=", value, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidLessThan(Integer value) {
			addCriterion("device_brandId <", value, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidLessThanOrEqualTo(Integer value) {
			addCriterion("device_brandId <=", value, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidIn(List<Integer> values) {
			addCriterion("device_brandId in", values, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidNotIn(List<Integer> values) {
			addCriterion("device_brandId not in", values, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidBetween(Integer value1, Integer value2) {
			addCriterion("device_brandId between", value1, value2, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDeviceBrandidNotBetween(Integer value1, Integer value2) {
			addCriterion("device_brandId not between", value1, value2, "deviceBrandid");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameIsNull() {
			addCriterion("definition_name is null");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameIsNotNull() {
			addCriterion("definition_name is not null");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameEqualTo(String value) {
			addCriterion("definition_name =", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameNotEqualTo(String value) {
			addCriterion("definition_name <>", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameGreaterThan(String value) {
			addCriterion("definition_name >", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameGreaterThanOrEqualTo(String value) {
			addCriterion("definition_name >=", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameLessThan(String value) {
			addCriterion("definition_name <", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameLessThanOrEqualTo(String value) {
			addCriterion("definition_name <=", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameLike(String value) {
			addCriterion("definition_name like", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameNotLike(String value) {
			addCriterion("definition_name not like", value, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameIn(List<String> values) {
			addCriterion("definition_name in", values, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameNotIn(List<String> values) {
			addCriterion("definition_name not in", values, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameBetween(String value1, String value2) {
			addCriterion("definition_name between", value1, value2, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionNameNotBetween(String value1, String value2) {
			addCriterion("definition_name not between", value1, value2, "definitionName");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeIsNull() {
			addCriterion("definition_code is null");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeIsNotNull() {
			addCriterion("definition_code is not null");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeEqualTo(String value) {
			addCriterion("definition_code =", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeNotEqualTo(String value) {
			addCriterion("definition_code <>", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeGreaterThan(String value) {
			addCriterion("definition_code >", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeGreaterThanOrEqualTo(String value) {
			addCriterion("definition_code >=", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeLessThan(String value) {
			addCriterion("definition_code <", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeLessThanOrEqualTo(String value) {
			addCriterion("definition_code <=", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeLike(String value) {
			addCriterion("definition_code like", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeNotLike(String value) {
			addCriterion("definition_code not like", value, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeIn(List<String> values) {
			addCriterion("definition_code in", values, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeNotIn(List<String> values) {
			addCriterion("definition_code not in", values, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeBetween(String value1, String value2) {
			addCriterion("definition_code between", value1, value2, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionCodeNotBetween(String value1, String value2) {
			addCriterion("definition_code not between", value1, value2, "definitionCode");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecIsNull() {
			addCriterion("definition_spec is null");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecIsNotNull() {
			addCriterion("definition_spec is not null");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecEqualTo(String value) {
			addCriterion("definition_spec =", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecNotEqualTo(String value) {
			addCriterion("definition_spec <>", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecGreaterThan(String value) {
			addCriterion("definition_spec >", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecGreaterThanOrEqualTo(String value) {
			addCriterion("definition_spec >=", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecLessThan(String value) {
			addCriterion("definition_spec <", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecLessThanOrEqualTo(String value) {
			addCriterion("definition_spec <=", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecLike(String value) {
			addCriterion("definition_spec like", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecNotLike(String value) {
			addCriterion("definition_spec not like", value, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecIn(List<String> values) {
			addCriterion("definition_spec in", values, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecNotIn(List<String> values) {
			addCriterion("definition_spec not in", values, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecBetween(String value1, String value2) {
			addCriterion("definition_spec between", value1, value2, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andDefinitionSpecNotBetween(String value1, String value2) {
			addCriterion("definition_spec not between", value1, value2, "definitionSpec");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeIsNull() {
			addCriterion("networking_type is null");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeIsNotNull() {
			addCriterion("networking_type is not null");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeEqualTo(String value) {
			addCriterion("networking_type =", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeNotEqualTo(String value) {
			addCriterion("networking_type <>", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeGreaterThan(String value) {
			addCriterion("networking_type >", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeGreaterThanOrEqualTo(String value) {
			addCriterion("networking_type >=", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeLessThan(String value) {
			addCriterion("networking_type <", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeLessThanOrEqualTo(String value) {
			addCriterion("networking_type <=", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeLike(String value) {
			addCriterion("networking_type like", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeNotLike(String value) {
			addCriterion("networking_type not like", value, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeIn(List<String> values) {
			addCriterion("networking_type in", values, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeNotIn(List<String> values) {
			addCriterion("networking_type not in", values, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeBetween(String value1, String value2) {
			addCriterion("networking_type between", value1, value2, "networkingType");
			return (Criteria) this;
		}

		public Criteria andNetworkingTypeNotBetween(String value1, String value2) {
			addCriterion("networking_type not between", value1, value2, "networkingType");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescIsNull() {
			addCriterion("definition_desc is null");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescIsNotNull() {
			addCriterion("definition_desc is not null");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescEqualTo(String value) {
			addCriterion("definition_desc =", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescNotEqualTo(String value) {
			addCriterion("definition_desc <>", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescGreaterThan(String value) {
			addCriterion("definition_desc >", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescGreaterThanOrEqualTo(String value) {
			addCriterion("definition_desc >=", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescLessThan(String value) {
			addCriterion("definition_desc <", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescLessThanOrEqualTo(String value) {
			addCriterion("definition_desc <=", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescLike(String value) {
			addCriterion("definition_desc like", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescNotLike(String value) {
			addCriterion("definition_desc not like", value, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescIn(List<String> values) {
			addCriterion("definition_desc in", values, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescNotIn(List<String> values) {
			addCriterion("definition_desc not in", values, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescBetween(String value1, String value2) {
			addCriterion("definition_desc between", value1, value2, "definitionDesc");
			return (Criteria) this;
		}

		public Criteria andDefinitionDescNotBetween(String value1, String value2) {
			addCriterion("definition_desc not between", value1, value2, "definitionDesc");
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

		public Criteria andIsdelIsNull() {
			addCriterion("isDel is null");
			return (Criteria) this;
		}

		public Criteria andIsdelIsNotNull() {
			addCriterion("isDel is not null");
			return (Criteria) this;
		}

		public Criteria andIsdelEqualTo(String value) {
			addCriterion("isDel =", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelNotEqualTo(String value) {
			addCriterion("isDel <>", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelGreaterThan(String value) {
			addCriterion("isDel >", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelGreaterThanOrEqualTo(String value) {
			addCriterion("isDel >=", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelLessThan(String value) {
			addCriterion("isDel <", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelLessThanOrEqualTo(String value) {
			addCriterion("isDel <=", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelLike(String value) {
			addCriterion("isDel like", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelNotLike(String value) {
			addCriterion("isDel not like", value, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelIn(List<String> values) {
			addCriterion("isDel in", values, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelNotIn(List<String> values) {
			addCriterion("isDel not in", values, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelBetween(String value1, String value2) {
			addCriterion("isDel between", value1, value2, "isdel");
			return (Criteria) this;
		}

		public Criteria andIsdelNotBetween(String value1, String value2) {
			addCriterion("isDel not between", value1, value2, "isdel");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table device_definition
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
     * This class corresponds to the database table device_definition
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}