package com.lifelight.dubbo.dao;

import com.lifelight.api.entity.ProcedureData;
import com.lifelight.api.entity.ProcedureDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProcedureDataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    long countByExample(ProcedureDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int deleteByExample(ProcedureDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int insert(ProcedureData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int insertSelective(ProcedureData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    List<ProcedureData> selectByExample(ProcedureDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    ProcedureData selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ProcedureData record, @Param("example") ProcedureDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ProcedureData record, @Param("example") ProcedureDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProcedureData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table procedure_data
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProcedureData record);
    
    List<ProcedureData> selectByEntityListPage(ProcedureData record);
}