package com.lifelight.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.api.entity.DeviceUserRel;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ApiUserInfoService;
import com.lifelight.dubbo.service.DeviceBindingUserService;
import com.lifelight.dubbo.service.DeviceInfoService;
import com.lifelight.dubbo.service.ManagerService;
import com.lifelight.dubbo.service.XlPersonDocumentService;

@Controller
@RequestMapping("/deviceProvide")
public class DeviceProvideController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceProvideController.class);
	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	@Reference
	private ApiUserInfoService apiUserInfoService;
//	@Reference
//	private ApiContractedUserService apiContractedUserService;
	@Reference
	private XlPersonDocumentService personDocumentService;
	@Reference
	private ManagerService managerService;
	@Autowired
	private RedisTool redisTool;
	/*  思创接口 START  */
    /**
     * 获取设备二维码接口
     */
    @RequestMapping("/getQRImg")
    @ResponseBody
    public Map<String, Object> getQRImg(String deviceCode, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }

        StringBuffer path = request.getRequestURL();  
        String tempContextUrl = path.delete(path.length() - request.getRequestURI().length(), path.length()).append("/").toString();
        String url = tempContextUrl +  "ytjQrcode.jsp?deviceCode=" + deviceCode.substring(deviceCode.trim().lastIndexOf(".")+1, deviceCode.length());
        
        result.put("url", url);
        result.put("message", "查询成功");
        result.put("code", "0");
        return result;
    }
    
    /**
     * 验证 开/关屏
     */
    @SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/getScreenState")
    @ResponseBody
    public Map<String, Object> getScreenState(HttpServletRequest request,
    		String deviceCode){
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> action = new HashMap<String, Object>();
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        try {
      //查询当前域名微信配置表id
      	logger.info("域名：：：："+request.getServerName());
//      	String domainName = request.getServerName();
      	String weixinId = null;
//      	if(StringUtil.isEmpty(weixinId)) {
//      		logger.info("获取微信配置信息错误****************");
//      		result.put("message", "获取微信配置信息错误");
//            result.put("error_code", "-1");
//            return result;
//      	}
        // 截取deviceCode 
        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim(),weixinId);
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "此设备暂未投放");
            result.put("error_code", "-1");
            return result;
		}	
		
		// 根据 deviceId 获取已经绑定的用户关系
		DeviceUserRel deviceUserRel = deviceBindingUserService.checkDeviceIsBinding(deviceId);
		
		// 屏幕状态定义字段
		String str = null;
		// 关系为空 返回关屏
		if(null == deviceUserRel) {
			str = "closeTheScreen";
			
			action.put("action", str);
	        action.put("userId", "");
	        action.put("userName", "");
	        action.put("userSex", "");
	        action.put("userAge", "");
	        
	        result.put("result", action);
	        result.put("message", "查询成功");
	        result.put("error_code", "0");
	        return result;
        }
		// IsBinding 为 F 关屏  T 开屏 A 管理员
        if("F".equals(deviceUserRel.getIsBinding())){
            str = "closeTheScreen";
        }else if("T".equals(deviceUserRel.getIsBinding())){
        	str = "openTheScreen";
        }else if("A".equals(deviceUserRel.getIsBinding())) {
        	str = "allSystem";
        }
        action.put("action", str);
        if(StringUtil.isEmpty(deviceUserRel.getUserMobile())) {
        	action.put("userId","12345678910");
        }else {
        	action.put("userId",deviceUserRel.getUserMobile());
        }
        action.put("userName", deviceUserRel.getUserName());
        
        String sex = null;
        if("1".equals(deviceUserRel.getUserSex())) {
        	sex = "男";
        }else if("2".equals(deviceUserRel.getUserSex())) {
        	sex = "女";
        }
        
        action.put("userSex", sex);
        try {
			action.put("userAge", getAge(deviceUserRel.getUserBirthday()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        result.put("result", action);
        result.put("message", "查询成功");
        result.put("error_code", "0");
        }catch(Exception e) {
        		e.printStackTrace();
        }
        return result;
    }

    /**
     * 设备锁屏
     */
    @SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/closeDevice")
    @ResponseBody
    public Map<String, Object> closeDevice(HttpServletRequest request,String deviceCode, String userId){
        Map<String, Object> result = new HashMap<String, Object>();
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        //查询当前域名微信配置表id
      	logger.info("域名：：：："+request.getServerName());
      	String weixinId = null;

        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code,weixinId);
				
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "此设备暂未投放");
            result.put("error_code", "-1");
            return result;
		}	
		Result resultData = deviceInfoService.getDeviceInfoById(deviceId);
		DeviceInfo deviceInfo = (DeviceInfo)resultData.getData();
		// 此 userId 为一体机传过来的参与  具体意义为 用户手机号 
		// 身份证登陆 要验证 
//		weixinId = String.valueOf(deviceInfo.getPlatformId());
        String managerId = apiUserInfoService.getManagerIdByMobile(userId,deviceId,weixinId);
		
        deviceBindingUserService.relieveBinding(managerId, deviceId.toString(), "F", "", null, "");

        result.put("message", "成功");
        result.put("error_code", "0");
        return result;
    }

    /**
     * 身份证开屏 
     */
    @SuppressWarnings({ "unlikely-arg-type", "rawtypes" })
	@RequestMapping("/openYtjByIdCard")
    @ResponseBody
    public Map<String, Object> openYtjByIdCard(HttpServletRequest request,
    		String cardId, String deviceCode){
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> action = new HashMap<String, Object>();
        logger.info("身份证**************"+cardId+"    "+deviceCode);
        if(null == cardId || "".equals(cardId)){
            result.put("message", "cardId为空");
            result.put("error_code", "-1");
            return result;
        }
        
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        //查询当前域名微信配置表id
      /*	logger.info("域名：：：："+request.getServerName());
      	String domainName = request.getServerName();
      	String weixinId = redisTool.get(domainName);
      	if(StringUtil.isEmpty(weixinId)) {
      		logger.info("获取微信配置信息错误****************");
      		result.put("message", "获取微信配置信息错误");
            result.put("error_code", "-1");
            return result;
      	}*/
        // 截取deviceCode 
        String weixinId = null;
        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim(),weixinId);
		logger.info("*******deviceId*******"+deviceId);
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "此设备暂未投放");
            result.put("error_code", "-1");
            return result;
		}	
		
		// 屏幕状态定义字段
		String str = null;
		// 根据身份证号 查询 managerId 不存在则关机 存在则开机
//		ApiContractedUserVO apiUserInfo = new ApiContractedUserVO();
//		apiUserInfo.setCardId(cardId);
		/*ManagerInfo ma = managerService.getManagerInfoByIDcard(cardId);
		String type = "";
		if(null==ma) {
			type = "1"; // 微信端手机登录用户
		}else {
			type = "2"; // 微信端身份证登录用户
		}*/
		XlPersonDocumentVO xp = new XlPersonDocumentVO();
		xp.setCardNum(cardId);
		Result re = personDocumentService.querySignUserByEntityCard(xp);
		logger.info("*******查询用户返回*******"+re.getData());
		if(null == re.getData()) {
			str = "closeTheScreen";
			
			action.put("action", str);
	        action.put("userId", "");
	        action.put("userName", "");
	        action.put("userSex", "");
	        action.put("userAge", "");
	        
	        result.put("result", action);
	        result.put("message", "查询成功");
	        result.put("error_code", "0");
	        return result;
		}else {
			ApiUserInfoVO auiv = (ApiUserInfoVO)re.getData();
			logger.info("*******查询ManagerId*******"+auiv.getManagerId());
			logger.info("*******查询Mobile*******"+auiv.getMobile());
			logger.info("*******查询Name*******"+auiv.getName());
			logger.info("*******查询Sex*******"+auiv.getSex());
			deviceBindingUserService.deviceBindingUser(auiv.getManagerId(), deviceId);
			
			str = "openTheScreen";
			action.put("action", str);
	        action.put("userId", auiv.getMobile());
	        action.put("userName", auiv.getName());
	        
	        String sex = null;
	        if("1".equals(auiv.getSex())) {
	        	sex = "男";
	        }else if("2".equals(auiv.getSex())) {
	        	sex = "女";
	        }
	        
	        action.put("userSex", sex);
	        
	        try {
				action.put("userAge", getAge(auiv.getBirthday()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        result.put("result", action);
	        result.put("message", "查询成功");
	        result.put("error_code", "0");
	        return result;
		}
    }
    /*  思创接口 END  */
    
    // 计算年龄
    private Integer getAge(Date birthDay) throws Exception {
    	if(null == birthDay || "".equals(birthDay)) {
    		return 0;
    	}
    	
        //获取当前系统时间
        Calendar cal = Calendar.getInstance();
        //如果出生日期大于当前时间，则抛出异常
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        //取出系统当前时间的年、月、日部分
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        //将日期设置为出生日期
        cal.setTime(birthDay);
        //取出出生日期的年、月、日部分
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) {
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
}