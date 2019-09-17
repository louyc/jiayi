package com.lifelight.common.tools.util;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ValidateUtil {
	
	/**
	 * checkEmail:验证邮箱 <br/> 
	 * @param email
	 * @return boolean
	 * @exception 
	 * @author hua
	*/
	public static boolean checkEmail(String email) {
	    boolean flag = false;
	    try {
	        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	        Pattern regex = Pattern.compile(check);
	        Matcher matcher = regex.matcher(email);
	        flag = matcher.matches();
	    } catch (Exception e) {
	        flag = false;
	    }
	    return flag;
	}

	/**
	 * checkMobileNumber:验证手机号码，11位数字，1开通，第二位数必须是3456789这些数字之一 . <br/> 
	 * @param mobileNumber
	 * @return boolean
	 * @exception 
	 * @author hua
	*/
	public static boolean checkMobileNumber(String mobileNumber) {
	    boolean flag = false;
	    try {
	        Pattern regex = Pattern.compile("^1[345789]\\d{9}$");
	        Matcher matcher = regex.matcher(mobileNumber);
	        flag = matcher.matches();
	    } catch (Exception e) {
	        e.printStackTrace();
	        flag = false;

	    }
	    return flag;
	}
	
	/**
	 * checkFromUrl:验证域名是否有权限 <br/> 
	 * @param fromUrl
	 * @return boolean
	 * @author hua
	*/
	public static boolean checkFromUrl(String fromUrl){
		boolean flag = false;
		
		Resource resource = new ClassPathResource("/config.properties");
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			String[] fromUrls = props.getProperty("fromUrl").split(",");
			for(String str : fromUrls){
				if(fromUrl.equals(str)){
					flag = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
}
