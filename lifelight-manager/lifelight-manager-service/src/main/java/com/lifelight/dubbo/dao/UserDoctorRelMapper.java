package com.lifelight.dubbo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.UserDoctorRel;
import com.lifelight.api.entity.UserDoctorRelExample;
import com.lifelight.api.vo.BackstageUserInfoVO;

public interface UserDoctorRelMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	long countByExample(UserDoctorRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int deleteByExample(UserDoctorRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int insert(UserDoctorRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int insertSelective(UserDoctorRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	List<UserDoctorRel> selectByExample(UserDoctorRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	UserDoctorRel selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") UserDoctorRel record,
			@Param("example") UserDoctorRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") UserDoctorRel record,
			@Param("example") UserDoctorRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(UserDoctorRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(UserDoctorRel record);

	List<UserDoctorRel> selectByManagerId(String managerId);

	int deleteByUserId(Integer userId);

	int updateInuse(Integer id);

	List<BackstageUserInfoVO> selectByMangerId(String managerId);

	List<UserDoctorRel> selectByEntity(UserDoctorRel record);
	
	int updateExpirationDate(UserDoctorRel record);
}