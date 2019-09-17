package com.lifelight.dubbo.dao;

import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.api.entity.ManagerInfoExample;
import com.lifelight.api.vo.ManagerInfoVO;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ManagerInfoMapper {

    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	long countByExample(ManagerInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int deleteByExample(ManagerInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int insert(ManagerInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int insertSelective(ManagerInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	List<ManagerInfo> selectByExample(ManagerInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	ManagerInfo selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") ManagerInfo record, @Param("example") ManagerInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") ManagerInfo record, @Param("example") ManagerInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(ManagerInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table manager_info
	 * @mbg.generated
	 */
	int updateByPrimaryKey(ManagerInfo record);

	ManagerInfoVO getUserInfo(String id);
}