package com.lifelight.dubbo.dao;

import com.lifelight.api.entity.Procedure;
import com.lifelight.api.entity.ProcedureExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ProcedureMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	long countByExample(ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int deleteByExample(ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int insert(Procedure record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int insertSelective(Procedure record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	List<Procedure> selectByExampleWithBLOBs(ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	List<Procedure> selectByExample(ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	Procedure selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") Procedure record, @Param("example") ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int updateByExampleWithBLOBs(@Param("record") Procedure record, @Param("example") ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") Procedure record, @Param("example") ProcedureExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(Procedure record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int updateByPrimaryKeyWithBLOBs(Procedure record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table procedure
	 * @mbg.generated
	 */
	int updateByPrimaryKey(Procedure record);

	/* 分页查询流程列表 */
	List<Procedure> selectProcedureListPage(Procedure procedure);
	
	/*批量插入*/
	void insertByBatch(List<Procedure> procedures);
	
	/*获取被分享流程的用户*/
	List<Map<String, Object>> getBesharedUsers(String procedureId);
}