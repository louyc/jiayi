package com.lifelight.dubbo.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.lifelight.api.dto.BackstageUserInfoDTO;
import com.lifelight.api.dto.Statistics;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.BackstageUserInfoExample;
import com.lifelight.api.entity.XlPersonSchedule;
import com.lifelight.api.vo.BackstageUserInfoVO;
import com.lifelight.api.vo.DateConditionsVO;

public interface BackstageUserInfoMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	long countByExample(BackstageUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int deleteByExample(BackstageUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(String managerId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int insert(BackstageUserInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int insertSelective(BackstageUserInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	List<BackstageUserInfo> selectByExample(BackstageUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	BackstageUserInfo selectByPrimaryKey(String managerId);

	List<BackstageUserInfoVO> selectInfoVOByPrimaryKey(String managerId);

	List<BackstageUserInfoVO> selectByConditionList(BackstageUserInfoVO back);

	List<BackstageUserInfoVO> selectByConditionListPage(BackstageUserInfoVO back);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") BackstageUserInfo record,
			@Param("example") BackstageUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") BackstageUserInfo record,
			@Param("example") BackstageUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(BackstageUserInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table backstage_user_info
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKey(BackstageUserInfo record);

	List<BackstageUserInfoVO> selectByCondition(Map<String, Object> conditionMap);

	List<BackstageUserInfoVO> selectByConditionLimit(Map<String, Object> conditionMap);

	/**
	 * @Description 按照日期统计注册用户
	 * @author xuyuxing
	 * @date 2017年7月29日 上午10:56:57
	 */
	List<Statistics> selectRegisterNumberListPage(DateConditionsVO dateConditionsVO);

	/**
	 * 后台用户管理列表
	 * 
	 * @param backstageUserInfoVO
	 * @return
	 */
	List<BackstageUserInfoDTO> queryUserListPage(BackstageUserInfoVO backstageUserInfoVO);
	
	/**
	 * 签约用户 查询审查进度
	 * @param managerId
	 * @return
	 */
	BackstageUserInfo selectBySchedule(Integer scheduleId);
}