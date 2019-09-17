package com.lifelight.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.http.weixin.JsonResult;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.MenuApi;
import com.foxinmy.weixin4j.mp.api.TmplApi;
import com.foxinmy.weixin4j.mp.message.TemplateMessage;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.lifelight.api.entity.ApiUserInfo;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.api.entity.DeviceUserRel;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ApiUserInfoService;
import com.lifelight.dubbo.service.DeviceBindingUserService;
import com.lifelight.dubbo.service.DeviceInfoService;
import com.lifelight.dubbo.service.DeviceSyncService;
import com.lifelight.dubbo.service.WeixinConfigureService;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 设备同步接口 
 */
@Controller
@RequestMapping("/deviceSync")
public class DeviceSyncController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceSyncController.class);

	@Autowired
	private RedisTool redisTool;
	@Reference
	private DeviceSyncService deviceSyncService;
	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	@Reference
	private ApiUserInfoService apiUserSerivce;
	@Reference
	private WeixinConfigureService weixinConfigureService;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	private Map<String,WeixinProxy> wxmap= new HashMap<String,WeixinProxy>();
	private Map<String,WeixinConfigureVO> weixinConfigmap= new HashMap<String,WeixinConfigureVO>();
	//	@Autowired
	//	private RedisTool redisTool;
	/**
	 * 设备同步接口 主要同步 卡片机 手环 血糖仪
	 * @param type 设备类型      KPJ : 卡片机   XTY : 血糖仪  SH : 蓝牙手环  TWJ : 体温计 KT:康泰血压计
	 * @param data 数据JSON
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/deviceSyncInit")
	@ResponseBody
	public Result deviceSyncInit(HttpServletRequest request,
			@RequestParam("type") String type, @RequestParam("data") String data) throws IllegalStateException, IOException {
		Result result = new Result<>(StatusCodes.OK, true);
		logger.info("同步类型："+type+"  同步数据："+data);
		if(null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}

		if(null == data || "".equals(data)) {
			result.setResultCode(new ResultCode("FALSE", "data为空"));
			return result;
		}  
		String weixinId =null;

		JSONObject jb = JSONObject.parseObject(data.trim());
		// 根据设备 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(jb.getString("deviceCode"),weixinId);
		// deviceId 不存在 说明设备没有加入后台
		if(null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "未找到该设备"));
			return result;
		}
		// 数据同步实现
		return deviceSyncService.deviceSyncInit(type, deviceId, data);
	}
	
	/**
	 * 设备同步接口 同步微信小程序步数
	 * @param data 数据JSON
	 * @param managerId  用户id
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/deviceSyncStep")
	@ResponseBody
	public Result deviceSyncStep(HttpServletRequest request,
			@RequestParam("managerId") String managerId, @RequestParam("data") String data) throws IllegalStateException, IOException {
		Result result = new Result<>(StatusCodes.OK, true);
		logger.info("用户id："+managerId+"  同步数据："+data);
		if(StringUtil.isEmpty(managerId)) {
			result.setStatus(StatusCodes.PARSER_ERROR);
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}
		if(StringUtil.isEmpty(data)) {
			result.setStatus(StatusCodes.PARSER_ERROR);
			result.setResultCode(new ResultCode("FALSE", "data为空"));
			return result;
		}  
		return deviceSyncService.deviceSyncStep(managerId, data);
	}

	/**
	 * 设备同步接口 仅支持一体机同步
	 */
	@SuppressWarnings({ "unlikely-arg-type", "rawtypes" })
	@RequestMapping("/deviceSyncByYtj")
	@ResponseBody
	public Map<String, Object> deviceSyncByYtj(HttpServletResponse response,
			String deviceCode, String type, String data, String userMobile, String time, HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		if(null == deviceCode || "".equals(deviceCode)){
			result.put("message", "deviceCode为空");
			result.put("error_code", "-1");
			return result;
		}
		if(null == type || "".equals(type)){
			result.put("message", "type为空");
			result.put("error_code", "-1");
			return result;
		}
		if(null == data || "".equals(data)){
			result.put("message", "data为空");
			result.put("error_code", "-1");
			return result;
		}
		if(!StringUtil.isEmpty(userMobile) && "12345678910".equals(userMobile)) {
			userMobile = null;
		}
		logger.info("域名：：：："+request.getServerName());
		String weixinId = null;
		String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim(),weixinId);

		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "未找到该设备");
			result.put("error_code", "-1");
			return result;
		}
		// 根据 deviceId 校验 该设备是否投放
		Result resultData = deviceInfoService.getDeviceInfoById(deviceId);
		DeviceInfo deviceInfo = (DeviceInfo)resultData.getData();
		weixinId = String.valueOf(deviceInfo.getPlatformId());
		if(null == deviceInfo.getManagerId() || "".equals(deviceInfo.getManagerId())) {
			result.put("message", "该设备未投放");
			result.put("error_code", "-1");
			return result;
		}
		// 设备投放地 ID
		String locationId = deviceInfo.getManagerId();

		// 根据 userId（managerId） 设备ID 获取当前绑定关系
		DeviceUserRel deviceUserRel = deviceBindingUserService.checkDeviceIsBinding(userMobile, deviceId,weixinId);

		// 判断设备是否绑定
		if(null == deviceUserRel || "".equals(deviceUserRel) || "F".equals(deviceUserRel.getIsBinding())){
			result.put("code", "-1");
			result.put("message", "设备未绑定");
			return result;
		}
		String backString ="";
		if(weixinId.equals("1")) {//扁鹊大哥  提示检测结果
			ApiUserInfo user = apiUserSerivce.getCurrentUserInfo(deviceUserRel.getManagerId());
			String checkName="";
			String color="";
			String tsType="";
			// 一体机数据转 JSON
			JSONObject jb = JSONObject.parseObject(data.trim());
			switch(type) {
			// 血压
			case "blood_pressure":
				String backString1 ="";
				String backString2 ="";
				tsType = "BP";
				//sys 收缩压        dia   舒张压
				int statusVal=0;
				double sys = Double.valueOf(jb.get("systolic_blood_pressure").toString());
				double dia = Double.valueOf(jb.get("diastolic_blood_pressure").toString());
				checkName="血压	"+"收缩压 :"+sys+" 舒张压:"+dia;
				double count1 = 60;
				double count2 = 90;
				if( dia < count1 ){
					backString1 = "舒张压:偏低";
					statusVal = 0;
				}else if( count1 <= dia && dia <= count2 ){
					backString1 = "舒张压:正常";
					statusVal = 1;
				}else if( dia > count2 ){
					backString1 = "舒张压:偏高";
					statusVal = 2;
				}
				double count3 = 90;
				double count4 = 140;
				int statusVal1 = 0;
				if( sys < count3 ){
					backString2 = "收缩压:偏低";
					statusVal1 = 0;
				}else if( count3 <= sys && sys <= count4 ){
					backString2 = "收缩压:正常";
					statusVal1 = 1;
				}else if( sys > count4 ){
					backString2 = "收缩压:偏高";
					statusVal1 = 2;
				}
				if( statusVal == 1 && statusVal1 == 1 ){
					color ="#008000";
					backString = "血压正常";
				}else{
					color ="#FF0000";
					backString = backString1 + "/" + backString2 + "；"
							+ "建议您定期到医院、社区卫生所测量血压，并做好记录，定期与医生联系及时寻求必要的指导和帮助";
				}
				break;
				// 血糖
			case "glucose_metabolism_analysis":
				tsType = "BG";
				double gmaValue = Double.valueOf(jb.get("blood_glucose").toString());
				double gma1 = 3.9;
				double gma2 = 6.1;
				double gma3 = 7.0;
				double gma4 = 8.4;
				double gma5 = 11.1;
				checkName="血糖	"+"浓度:"+gmaValue+"mmol/L";
				if( gmaValue < gma1 ){
					backString = "血糖偏低，可能因饥饿或剧烈运动导致，可能会出现头晕、乏力等症状，请您注意休息，补充营养";
				}else if( gma1 <= gmaValue && gmaValue <= gma2 ){
					color ="#008000";
					backString = "血糖正常";
				}else if( gmaValue >gma2 && gmaValue < gma3 ){
					color ="#FF0000";
					backString = "血糖偏高，请您适量运动，注意饮食";
				}else if(gmaValue >= gma3 && gmaValue < gma4 ){
					color ="#FF0000";
					backString = "疑似轻度糖尿病患者,血糖偏高，请您适量运动，注意饮食";
				}else if(gmaValue >= gma4 && gmaValue < gma5){
					color ="#FF0000";
					backString = "疑似中度糖尿病患者,血糖偏高，请您适量运动，注意饮食";
				}else if(gmaValue >= gma5){
					color ="#FF0000";
					backString = "疑似重度糖尿病患者,血糖偏高，请您适量运动，注意饮食";
				}
				break;	
				// 总胆固醇
			case "total_cholesterol_analysis":
				tsType = "CHOL";
				double tcaValue = Double.valueOf(jb.get("total_cholesterol").toString());
				checkName="总胆固醇	"+"浓度:"+tcaValue+"mmol/L\"";
				int age = 0;
				try {
					if(null!=user.getbirthday()) {
						age = getAge(user.getbirthday());
					}else {
						age = 19;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				double tca1 = 2.86;
				double tca2 = 5.98;
				double tca3 = 3.12;
				double tca4 = 5.2;
				if(age >=18){ /*成人*/
					if( tcaValue < tca1 ){
						color ="#FF0000";
						backString = "您的总胆固醇偏低，请注意补充营养，或者就医诊断。";
					}else if( tca1 <= tcaValue && tcaValue <= tca2 ){
						color ="#008000";
						backString = "您的总胆固醇正常，请继续保持，平时注意控制饮食，适量运动。";
					}else if( tcaValue > tca2 ){
						color ="#FF0000";
						backString = "您的总胆固醇偏高，请注意控制饮食、保持运动，或者就医诊断。";
					}
				}else if( age <18 ){
					if( tcaValue < tca3 ){
						color ="#FF0000";
						backString = "您的总胆固醇偏低，请注意补充营养，或者就医诊断。";
					}else if( tca3 <= tcaValue && tcaValue <= tca4 ){
						color ="#008000";
						backString = "您的总胆固醇正常，请继续保持，平时注意控制饮食，适量运动。";
					}else if( tcaValue > tca4 ){
						color ="#FF0000";
						backString = "您的总胆固醇偏高，请注意控制饮食、保持运动，或者就医诊断。";
					}
				}
				break;	
				//bmi
			case "body_composition_analysis":
				tsType = "BMI";
				String height = jb.get("height").toString();
				String weight = jb.get("weight").toString();
				double bmi = Double.valueOf(jb.get("BMI").toString());
				logger.info("BMI "+"数值:"+bmi+"  身高:"+height+"cm"+"	体重:"+weight+"kg");
				checkName="BMI "+"数值:"+bmi+"  身高:"+height+"cm"+"	体重:"+weight+"kg";
				double bmi1 = 18.5;
				double bmi2 = 24;
				double bmi3 = 27;
				//比较结果
				if(bmi < bmi1){
					color ="#FF0000";
					backString = "偏瘦，相关疾病发病的危险性低，但其它疾病危险性增加";
				}else if(bmi1 <= bmi && bmi < bmi2){
					color ="#008000";
					backString = "正常，相关疾病发病的危险性正常";
				}else if(bmi2 <= bmi && bmi <= bmi3){
					color ="#FF0000";
					backString = "超重偏胖，相关疾病发病的危险性增加";
				}else if(bmi > bmi3){
					color ="#FF0000";
					backString = "肥胖，相关疾病发病的危险性严重增加";
				}
				break;
				// 血氧饱和度
			case "blood_oxygen":
				tsType = "BO";
				Double bloodOxygen = Double.valueOf(jb.get("blood_oxygen").toString());
				checkName="血氧饱和度    "+"浓度:"+bloodOxygen+"%";
				if(bloodOxygen <= 90){
					color ="#FF0000";
					backString = "低血氧症,血氧饱和度异常，请及时就医";
				}else if(bloodOxygen > 90 && bloodOxygen <= 95){
					color ="#FF0000";
					backString = "供氧不足,血氧饱和度轻度异常，建议重新测量";
				}else if(bloodOxygen >= 95 ){
					color ="#008000";
					backString = "血氧饱和度正常";
				}else {
					color ="#FF0000";
					backString = "血氧饱和度异常，请及时就医";
				}
				break;
				// 尿酸
			case "renal_function":
				tsType = "UA";
				Double rf = Double.valueOf(jb.get("urine_uric_acid").toString());
				checkName="尿酸    "+"浓度:"+rf+"mmol/L";
				String sex = user.getSex();
				Double rf1 = 0.149;
				Double rf2 = 0.416;
				Double rf3 = 0.089;
				Double rf4 = 0.357;
				if(!StringUtil.isEmpty(sex) && sex.equals("1")){
					if( rf <  rf1 ){
						color ="#FF0000";
						backString = "您的尿酸偏低，请注意保持营养均衡，或者就医诊断。";
					}else if( rf1 <= rf && rf <= rf2 ){
						color ="#008000";
						backString = "您的尿酸正常，请继续保持，平时注意控制饮食，适量运动。";
					}else if( rf > rf2 ){
						color ="#FF0000";
						backString = "您的尿酸偏高，请注意控制饮食、保持运动，或者就医诊断。";
					}
				}else if(!StringUtil.isEmpty(sex) && sex.equals("2")){
					if( rf < rf3 ){
						color ="#FF0000";
						backString = "您的尿酸偏低，请注意保持营养均衡，或者就医诊断。";
					}else if( rf3 <= rf && rf <= rf4 ){
						color ="#008000";
						backString = "您的尿酸正常，请继续保持，平时注意控制饮食，适量运动。";
					}else if( rf > rf4 ){
						color ="#FF0000";
						backString = "您的尿酸偏高，请注意控制饮食、保持运动，或者就医诊断。";
					}
				}
				break;
			default:
				break;	
			}
			if(!StringUtil.isEmpty(tsType)) {
				logger.info("检测结果：：：："+backString+"  type"+tsType);
				//20160626  查询微信公众号信息
				String domainName = request.getServerName();
				WeixinProxy wxProxy = getWeixinProxy(domainName,response);
				if(null ==wxProxy) {
					logger.info("获取微信配置信息错误****************");
				}
				// 推送openid
				String touser = user.getOpenId();
				// 时间
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateTime = formatter.format(new Date());
				// 创建链接微信
				TmplApi tmplApi = new TmplApi(wxProxy.getTokenHolder());
				String url="http://manager.lifelight365.com/m/healthFileData.html?type="+tsType;
				logger.info("创建微信模板：touser=" + touser + " template_id=" + "" + " url=" + url);
				// 微信定义模板
				TemplateMessage tplMessage = new TemplateMessage(touser, "swk9RHXIlwqEJ3grMrquCFqiFBOsu7MVbLPR9Ri1gtM" , url);
				tplMessage.pushItem("first", "您好!您之前进行的检验项目已出结果,详情如下");
				tplMessage.pushItem("test", color, checkName); // 检查项目
				tplMessage.pushItem("testdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // 医生名称
				tplMessage.pushItem("remark", color, "检查结论："+backString);

				logger.info("发送微信消息 ：{}", tplMessage);
				try {
					JsonResult resultWeixin = tmplApi.sendTmplMessage(tplMessage);
					logger.info("微信返回  result=" + resultWeixin.getCode());
					if (resultWeixin.getCode() == 0) {
						// 发送成功
						logger.info("消息发送成功！");
					} else {
						logger.info("微信消息发送失败！");
					}
				} catch (Exception e) {
					logger.error("消息发送失败， {}", e);
					logger.info("微信消息发送失败！");
				}
			}
			
		}
		return deviceSyncService.deviceSyncByYtj(deviceId, locationId, type, data, deviceUserRel.getManagerId(), time);
	}

	/**
	 * 设备同步接口 主要同步 免疫定量分析仪
	 * @param type 设备类型
	 * @param data 数据JSON
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/deviceSyncByImmunoassay")
	@ResponseBody
	public Result deviceSyncByImmunoassay(HttpServletRequest request,
			@RequestParam("deviceCode") String deviceCode, @RequestParam("data") String data, @RequestParam("type") String type) throws IllegalStateException, IOException {
		Result result = new Result<>(StatusCodes.OK, true);

		if(null == deviceCode || "".equals(deviceCode)) {
			result.setResultCode(new ResultCode("FALSE", "deviceCode为空"));
			return result;
		}

		if(null == data || "".equals(data)) {
			result.setResultCode(new ResultCode("FALSE", "data为空"));
			return result;
		}

		if(null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			result.setResultCode(new ResultCode("FALSE", "获取微信配置信息错误"));
			return result;
		}
		// 截取 deviceCode 获取 设备码
		String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());

		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim(),weixinId);

		if(null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "未找到该设备"));
		}
		// 根据 deviceId 校验 该设备是否投放
		//		Result resultData = deviceInfoService.getDeviceInfoById(deviceId);
		//		DeviceInfo deviceInfo = (DeviceInfo)resultData.getData();
		//		
		//		if(null == deviceInfo.getManagerId() || "".equals(deviceInfo.getManagerId())) {
		//			result.setResultCode(new ResultCode("FALSE", "该设备未投放"));
		//			return result;
		//		}

		// 根据 userId（managerId） 设备ID 获取当前绑定关系
		DeviceUserRel deviceUserRel = deviceBindingUserService.checkDeviceIsBinding(deviceId);

		// 判断设备是否绑定
		if(null == deviceUserRel || "".equals(deviceUserRel) || "F".equals(deviceUserRel.getIsBinding())){
			result.setResultCode(new ResultCode("FALSE", "设备未绑定"));
		}

		// 数据同步实现   deviceSyncService.deviceSyncByImmunoassay(deviceId, data)
		return deviceSyncService.deviceSyncByImmunoassay(deviceId, deviceUserRel.getManagerId(), data, type);
	}

	//由出生日期获得年龄  
	public static  int getAge(Date birthDay) throws Exception {  
		Calendar cal = Calendar.getInstance();  
		if (cal.before(birthDay)) {  
			throw new IllegalArgumentException(  
					"The birthDay is before Now.It's unbelievable!");  
		}  
		int yearNow = cal.get(Calendar.YEAR);  
		int monthNow = cal.get(Calendar.MONTH);  
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
		cal.setTime(birthDay);   

		int yearBirth = cal.get(Calendar.YEAR);  
		int monthBirth = cal.get(Calendar.MONTH);  
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);   

		int age = yearNow - yearBirth;  

		if (monthNow <= monthBirth) {  
			if (monthNow == monthBirth) {  
				if (dayOfMonthNow < dayOfMonthBirth) age--;  
			}else{  
				age--;  
			}  
		}  
		return age;  
	}  
	public WeixinProxy getWeixinProxy(String domainName,HttpServletResponse response) {
		if (StringUtils.isNotEmpty(domainName)) {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,
					jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
					jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());

			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);
			logger.info("domainname======================================================================================"+domainName);
			PrintWriter out =null;
			if(wxmap.get(domainName)!=null){
				return wxmap.get(domainName);
			}else{
				Result result = weixinConfigureService.queryWeicinConfigureByDomain(domainName,null);
				try {
					out = response.getWriter();
					WeixinConfigureVO weixinConfig = null;
					if(null !=result.getData()) {
						weixinConfig = (WeixinConfigureVO)result.getData();
						weixinConfigmap.put(domainName, weixinConfig);
					}else {
						logger.info("获取微信配置信息错误****************");
						return null;
					}
					WeixinPayAccount wxpayAccount =null;
					if(null!=weixinConfig){
						// 建一个表来获取微信的全部信息。包含微信支付、imweixin
						wxpayAccount = new WeixinPayAccount(weixinConfig.getAppid().trim(), weixinConfig.getSecret().trim(),
								weixinConfig.getPaysignKey().trim(), weixinConfig.getMchid().trim(), null, null, null, null, null);
						Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);
						settings.setTokenStorager(rts);
						WeixinProxy proxy = new WeixinProxy(settings);
						out.print(proxy.getTokenHolder().getToken());
						wxmap.put(domainName, proxy);
					}else{
						return null;
					}
				} catch (Exception e) {
					return null;
				}finally {
					if(out!=null) {
						out.close();
						out = null;
					}
				}
				return wxmap.get(domainName);
			}			
		} else {
			return null;
		}
	}

}