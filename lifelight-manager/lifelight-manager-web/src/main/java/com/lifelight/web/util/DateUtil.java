/*
 *  Copyright (c) 2014 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.lifelight.web.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil
{
	public static final int DEFAULT = 0;
	public static final int YM = 1;
	public static final int YMR_SLASH = 11;
	public static final int NO_SLASH = 2;
	public static final int YM_NO_SLASH = 3;
	public static final int DATE_TIME = 4;
	public static final int DATE_TIME_NO_SLASH = 5;
	public static final int DATE_HM = 6;
	public static final int TIME = 7;
	public static final int HM = 8;
	public static final int LONG_TIME = 9;
	public static final int SHORT_TIME = 10;
	public static final int DATE_TIME_LINE = 12;
	public static Map<String,String> FamilyRelationsMap = new HashMap<String,String>();  //家庭关系
	public static Map<String,String> LivingStateMap = new HashMap<String,String>();  //居住状态
	public static Map<String,String> LivingTypeMap = new HashMap<String,String>();  //居住类型
	public static Map<String,String> RegisteredResidenceMap = new HashMap<String,String>();  //户口类型
	public static Map<String,String> KeyCrowdTypeMap = new HashMap<String,String>();  //重点人群
	public static Map<String,String> DegreeEducationMap = new HashMap<String,String>();  //文化程度
	public static Map<String,String> OccupationMap = new HashMap<String,String>();  //职业关系
	public static Map<String,String> SignModeMap = new HashMap<String,String>();  //签约模式关系
	public static Map<String,String> ArrangementMap = new HashMap<String,String>();  //处置安排关系
	public static Map<String,String> ServiceModeMap = new HashMap<String,String>();  //服务方式关系
	public static Map<String,String> MedicalTypeMap = new HashMap<String,String>();  //医保类型关系
	public static Map<String,String> SignPersonTypeMap = new HashMap<String,String>();  //签约人群关系
	static {  
		FamilyRelationsMap.put("1", "本人或户主");  
		FamilyRelationsMap.put("2", "配偶");  
		FamilyRelationsMap.put("3", "子");  
		FamilyRelationsMap.put("4", "女");  
		FamilyRelationsMap.put("5", "孙子、孙女或外孙子");  
		FamilyRelationsMap.put("6", "父母");  
		FamilyRelationsMap.put("7", "祖父母或者外祖父母");  
		FamilyRelationsMap.put("8", "兄弟姐妹");  
		FamilyRelationsMap.put("9", "其他"); 
		LivingStateMap.put("1", "常驻（户籍）");
		LivingStateMap.put("2", "常驻（非户籍）");
		LivingStateMap.put("3", "驻外户籍");
		LivingStateMap.put("4", "常外出");
		LivingStateMap.put("5", "不详");
		LivingTypeMap.put("1", "流动");
		LivingTypeMap.put("2", "常驻");
		RegisteredResidenceMap.put("1", "乡村");
		RegisteredResidenceMap.put("2", "城镇");
		KeyCrowdTypeMap.put("1", "老年人");
		KeyCrowdTypeMap.put("2", "高血压");
		KeyCrowdTypeMap.put("3", "糖尿病");
		KeyCrowdTypeMap.put("4", "严重性精神病");
		KeyCrowdTypeMap.put("5", "孕产妇");
		KeyCrowdTypeMap.put("6", "儿童");
		DegreeEducationMap.put("1", "研究生");
		DegreeEducationMap.put("2", "大学本科");
		DegreeEducationMap.put("3", "大学本科和专科学校");
		DegreeEducationMap.put("4", "中等专业学校");
		DegreeEducationMap.put("5", "技工学校");
		DegreeEducationMap.put("6", "高中");
		DegreeEducationMap.put("7", "初中");
		DegreeEducationMap.put("8", "小学");
		DegreeEducationMap.put("9", "文盲");
		DegreeEducationMap.put("10", "不详");
		OccupationMap.put("1", "国家机关、党群组织、企业、事业单位负责人");
		OccupationMap.put("2", "专业技术人员");
		OccupationMap.put("3", "办事人员和有关人员");
		OccupationMap.put("4", "商业、服务业人员");
		OccupationMap.put("5", "农、林、牧、渔、水利业生产人员");
		OccupationMap.put("6", "生产、运输设备操作员及有关人员");
		OccupationMap.put("7", "军人");
		OccupationMap.put("8", "不便分类的其他从业人员");
		OccupationMap.put("9", "无职业");
		SignModeMap.put("1", "村医模式");
		SignModeMap.put("2", "团队模式");
		ArrangementMap.put("1", "持续管理");
		ArrangementMap.put("2", "居家治疗");
		ArrangementMap.put("3", "请管理团队会诊");
		ArrangementMap.put("4", "转诊门诊治疗");
		ArrangementMap.put("5", "转诊住院治疗");
		ArrangementMap.put("6", "其他");
		ServiceModeMap.put("1", "主动服务");
		ServiceModeMap.put("2", "上门服务");
		ServiceModeMap.put("3", "预约服务");
		ServiceModeMap.put("4", "电话随访");
		ServiceModeMap.put("5", "其他");
		MedicalTypeMap.put("1", "新农合");
		MedicalTypeMap.put("2", "职工医保");
		MedicalTypeMap.put("3", "居民医保");
		MedicalTypeMap.put("4", "其他或无");
		SignPersonTypeMap.put("1", "普通居民");
		SignPersonTypeMap.put("2", "老年人");
		SignPersonTypeMap.put("3", "高血压");
		SignPersonTypeMap.put("4", "糖尿病");
		SignPersonTypeMap.put("5", "孕产妇");
		SignPersonTypeMap.put("6", "0-6岁儿童");
		SignPersonTypeMap.put("7", "脑卒中");
		SignPersonTypeMap.put("8", "计划生育特殊家庭");
		SignPersonTypeMap.put("9", "精神病");
		SignPersonTypeMap.put("10", "慢性呼吸道疾病");
	}  

	public static String dateToStr(Date date, String pattern)
	{
		if ((date == null) || (date.equals("")))
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static String dateToStr(Date date) {
		return dateToStr(date, "yyyy/MM/dd");
	}

	public static String dateToStr(Date date, int type) {
		switch (type)
		{
		case 0:
			return dateToStr(date);
		case 1:
			return dateToStr(date, "yyyy/MM");
		case 2:
			return dateToStr(date, "yyyyMMdd");
		case 11:
			return dateToStr(date, "yyyy-MM-dd");
		case 3:
			return dateToStr(date, "yyyyMM");
		case 4:
			return dateToStr(date, "yyyy/MM/dd HH:mm:ss");
		case 5:
			return dateToStr(date, "yyyyMMddHHmmss");
		case 6:
			return dateToStr(date, "yyyy/MM/dd HH:mm");
		case 7:
			return dateToStr(date, "HH:mm:ss");
		case 8:
			return dateToStr(date, "HH:mm");
		case 9:
			return dateToStr(date, "HHmmss");
		case 10:
			return dateToStr(date, "HHmm");
		case 12:
			return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
		}
		throw new IllegalArgumentException("Type undefined : " + type);
	}

	/**
	 * 十进制字符串 转 16进制
	 * @param s
	 * @return
	 */
	public static String toHex16String(String s) {
		if(s.toCharArray().length==2) {
			Integer x = Integer.valueOf(s);
			s = String.valueOf(x.toHexString(x));
			DecimalFormat df=new DecimalFormat("00");
			s=df.format(Integer.parseInt(s));
		}else {
			Integer x = Integer.valueOf(s);
			s = String.valueOf(x.toHexString(x));
			DecimalFormat df=new DecimalFormat("0000");
			s=df.format(Integer.parseInt(s));
		}
		return s;
	}
	
	public static void main(String[] args) {
		String deviceCode = "23434 06b31804090550";
		String deviceCodeCh = deviceCode.split(" ")[1];
		deviceCodeCh =deviceCodeCh.substring(0, 4)+DateUtil.toHex16String(deviceCodeCh.substring(4, 6))
		+deviceCodeCh.substring(6, 10)+DateUtil.toHex16String(deviceCodeCh.substring(10, 14));
		System.out.println(""+deviceCodeCh);
	}
}