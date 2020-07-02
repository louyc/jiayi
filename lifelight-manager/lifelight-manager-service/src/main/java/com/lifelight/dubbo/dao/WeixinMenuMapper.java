package com.lifelight.dubbo.dao;

import com.lifelight.api.entity.BackstageMenuInfo;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.entity.WeixinMenu;
import com.lifelight.api.entity.WeixinMenuExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface WeixinMenuMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    long countByExample(WeixinMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int deleteByExample(WeixinMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int insert(WeixinMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int insertSelective(WeixinMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    List<WeixinMenu> selectByExample(WeixinMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    WeixinMenu selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") WeixinMenu record, @Param("example") WeixinMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") WeixinMenu record, @Param("example") WeixinMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WeixinMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_menu
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(WeixinMenu record);
    
    List<WeixinMenu> selectWeixinMenuListPage(WeixinMenu record);

    List<WeixinMenu> selectChildrenWeixinMenu(WeixinMenu record);

    List<WeixinMenu> selectListByContion(WeixinMenu record);
    
    WeixinMenu selectByNameAndParent(WeixinMenu record);
    
    int updateById(WeixinMenu record);
    
}