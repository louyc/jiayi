package com.lifelight.dubbo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.DeviceType;
import com.lifelight.api.entity.DeviceTypeExample;

public interface DeviceTypeMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	long countByExample(DeviceTypeExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int deleteByExample(DeviceTypeExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int insert(DeviceType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int insertSelective(DeviceType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	List<DeviceType> selectByExample(DeviceTypeExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	DeviceType selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") DeviceType record, @Param("example") DeviceTypeExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") DeviceType record, @Param("example") DeviceTypeExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(DeviceType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table device_type
	 * @mbg.generated
	 */
	int updateByPrimaryKey(DeviceType record);

	List<DeviceType> selectDeviceTypeListPage(DeviceType deviceType);
	
	DeviceType getDeviceTypeByName(DeviceType deviceType);
	
	DeviceType getDeviceTypeBy(DeviceType deviceType);
}