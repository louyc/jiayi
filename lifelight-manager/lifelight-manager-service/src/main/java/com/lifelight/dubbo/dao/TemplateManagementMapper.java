package com.lifelight.dubbo.dao;

import com.lifelight.api.entity.TemplateManagement;
import com.lifelight.api.entity.TemplateManagementExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TemplateManagementMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    long countByExample(TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int deleteByExample(TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int insert(TemplateManagement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int insertSelective(TemplateManagement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    List<TemplateManagement> selectByExampleWithBLOBs(TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    List<TemplateManagement> selectByExample(TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    TemplateManagement selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") TemplateManagement record, @Param("example") TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") TemplateManagement record, @Param("example") TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") TemplateManagement record, @Param("example") TemplateManagementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TemplateManagement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(TemplateManagement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table template_management
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TemplateManagement record);
    
    List<TemplateManagement> selectTemplates(TemplateManagement record);
}