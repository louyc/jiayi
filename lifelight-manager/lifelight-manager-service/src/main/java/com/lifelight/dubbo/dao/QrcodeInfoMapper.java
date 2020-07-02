package com.lifelight.dubbo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.QrcodeInfo;
import com.lifelight.api.entity.QrcodeInfoExample;

public interface QrcodeInfoMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	long countByExample(QrcodeInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int deleteByExample(QrcodeInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int insert(QrcodeInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int insertSelective(QrcodeInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	List<QrcodeInfo> selectByExample(QrcodeInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	QrcodeInfo selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") QrcodeInfo record, @Param("example") QrcodeInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") QrcodeInfo record, @Param("example") QrcodeInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(QrcodeInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	int updateByPrimaryKey(QrcodeInfo record);

	int getQrcodeInfoByName(String name, String id);
	
	List<QrcodeInfo> getAllQrcodeInfoListPage(QrcodeInfo qrcodeInfo);
	
	/* 根据推广人，医生id 查询推广二维码 */
	QrcodeInfo getQrcodeInfoByPrname(String name, String id);
}