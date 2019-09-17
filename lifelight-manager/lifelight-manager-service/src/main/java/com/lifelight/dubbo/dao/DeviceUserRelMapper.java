package com.lifelight.dubbo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.DeviceUserRel;
import com.lifelight.api.entity.DeviceUserRelExample;

public interface DeviceUserRelMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	long countByExample(DeviceUserRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int deleteByExample(DeviceUserRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int insert(DeviceUserRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int insertSelective(DeviceUserRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	List<DeviceUserRel> selectByExample(DeviceUserRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	DeviceUserRel selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") DeviceUserRel record,
			@Param("example") DeviceUserRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") DeviceUserRel record,
			@Param("example") DeviceUserRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(DeviceUserRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_user_rel
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKey(DeviceUserRel record);

	List<DeviceUserRel> getDeviceBindingList(String managerId);

	int relieveBinding(DeviceUserRel deviceUserRel);

	DeviceUserRel getDeviceUserRelByMaDe(DeviceUserRel deviceUserRel);

	int updateIsBinding(DeviceUserRel deviceUserRel);

	int changeNetworking(DeviceUserRel deviceUserRel);

	String getManagerIdByDeviceId(Integer deviceId);

	int updateRelNotBinding(DeviceUserRel deviceUserRel);

	DeviceUserRel checkDeviceIsBinding(Integer deviceId);

	DeviceUserRel checkDeviceIsBindingXl(Integer deviceId);

	DeviceUserRel getDeviceIsBindingByDeviceId(Integer deviceId);

	DeviceUserRel checkDeviceIsBindingByManagerId(DeviceUserRel deviceUserRel);
}
