package com.lifelight.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.api.entity.ProcedureMessageExample;

public interface ProcedureMessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    long countByExample(ProcedureMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int deleteByExample(ProcedureMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int insert(ProcedureMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int insertSelective(ProcedureMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    List<ProcedureMessage> selectByExample(ProcedureMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    ProcedureMessage selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ProcedureMessage record, @Param("example") ProcedureMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ProcedureMessage record, @Param("example") ProcedureMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProcedureMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_message
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProcedureMessage record);
    
    /* 查询消息列表分页 */
	List<ProcedureMessage> selectProcedureMessageListPage(ProcedureMessage procedureMessage);
    
    /* 查询未发送消息列表 */
	List<ProcedureMessage> getUnsentList(ProcedureMessage procedureMessage);
}