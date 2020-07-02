package com.lifelight.dubbo.dao;

import com.lifelight.api.entity.WeixinMenu;
import com.lifelight.api.entity.WeixinTemplate;
import com.lifelight.api.entity.WeixinTemplateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeixinTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    long countByExample(WeixinTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int deleteByExample(WeixinTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int insert(WeixinTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int insertSelective(WeixinTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    List<WeixinTemplate> selectByExample(WeixinTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    WeixinTemplate selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") WeixinTemplate record, @Param("example") WeixinTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") WeixinTemplate record, @Param("example") WeixinTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WeixinTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_template
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(WeixinTemplate record);
    
    List<WeixinTemplate> selectWeixinTemplateListPage(WeixinTemplate record);
}