package com.lifelight.dubbo.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.BackstageMenuInfo;
import com.lifelight.api.entity.BackstageMenuInfoExample;

public interface BackstageMenuInfoMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	long countByExample(BackstageMenuInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int deleteByExample(BackstageMenuInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int insert(BackstageMenuInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int insertSelective(BackstageMenuInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	List<BackstageMenuInfo> selectByExample(BackstageMenuInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	BackstageMenuInfo selectByPrimaryKey(Integer id);

	List<BackstageMenuInfo> selectAll(Integer platformId);

	List<BackstageMenuInfo> selectChildren(Integer platformId);

	List<BackstageMenuInfo> selectByParentId(Integer parentId);

	BackstageMenuInfo selectByName(Map<String, Object> queryMap);

	BackstageMenuInfo selectByNameAndParent(Map<String, Object> queryMap);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") BackstageMenuInfo record,
			@Param("example") BackstageMenuInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") BackstageMenuInfo record,
			@Param("example") BackstageMenuInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(BackstageMenuInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_menu_info
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(BackstageMenuInfo record);

	int updateByIdOrName(BackstageMenuInfo record);
}