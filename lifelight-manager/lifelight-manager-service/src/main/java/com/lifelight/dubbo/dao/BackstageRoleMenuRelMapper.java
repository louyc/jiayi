package com.lifelight.dubbo.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.entity.BackstageRoleMenuRel;
import com.lifelight.api.entity.BackstageRoleMenuRelExample;

public interface BackstageRoleMenuRelMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	long countByExample(BackstageRoleMenuRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int deleteByExample(BackstageRoleMenuRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	int deleteByRoleId(Integer roleId,Integer platformId);

	int deleteByMenuId(Integer menuId,Integer platformId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int insert(BackstageRoleMenuRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int insertSelective(BackstageRoleMenuRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	List<BackstageRoleMenuRel> selectByExample(BackstageRoleMenuRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	BackstageRoleMenuRel selectByPrimaryKey(Integer id);

	List<BackstageRoleMenuRel> selectByRoleAndMenu(Map queryMap);

	List<BackstageRoleMenuRel> selectByRoleId(Integer id,Integer platformId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") BackstageRoleMenuRel record,
			@Param("example") BackstageRoleMenuRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") BackstageRoleMenuRel record,
			@Param("example") BackstageRoleMenuRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(BackstageRoleMenuRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_role_menu_rel
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(BackstageRoleMenuRel record);
	
	int copyRoleMenuRel(Integer platformId);
}