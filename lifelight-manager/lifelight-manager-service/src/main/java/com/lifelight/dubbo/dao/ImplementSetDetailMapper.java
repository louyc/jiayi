package com.lifelight.dubbo.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.ImplementSetDetail;
import com.lifelight.api.entity.ImplementSetDetailExample;
import com.lifelight.api.vo.ImplementSetDetailVO;

public interface ImplementSetDetailMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	long countByExample(ImplementSetDetailExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int deleteByExample(ImplementSetDetailExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int insert(ImplementSetDetail record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int insertSelective(ImplementSetDetail record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	List<ImplementSetDetail> selectByExample(ImplementSetDetailExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	ImplementSetDetail selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") ImplementSetDetail record,
			@Param("example") ImplementSetDetailExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") ImplementSetDetail record,
			@Param("example") ImplementSetDetailExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(ImplementSetDetail record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(ImplementSetDetail record);

	int updateInUse(Map<String, Object> updateMap);

	List<ImplementSetDetail> selectByEntity(ImplementSetDetailVO entity);

	List<ImplementSetDetailVO> selectEntityByConditionListPage(ImplementSetDetailVO entity);

	List<ImplementSetDetailVO> selectEntityByCondition(ImplementSetDetailVO entity);
}