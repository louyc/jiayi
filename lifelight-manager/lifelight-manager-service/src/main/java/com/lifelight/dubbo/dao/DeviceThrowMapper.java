package com.lifelight.dubbo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.DeviceThrow;
import com.lifelight.api.entity.DeviceThrowExample;

public interface DeviceThrowMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    long countByExample(DeviceThrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int deleteByExample(DeviceThrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int insert(DeviceThrow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int insertSelective(DeviceThrow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    List<DeviceThrow> selectByExample(DeviceThrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    DeviceThrow selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DeviceThrow record, @Param("example") DeviceThrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DeviceThrow record, @Param("example") DeviceThrowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DeviceThrow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_throw
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DeviceThrow record);
}