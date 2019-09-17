package com.lifelight.dubbo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.dto.DeviceItemEnum;
import com.lifelight.api.entity.CardApparatusData;
import com.lifelight.api.entity.DeviceData;
import com.lifelight.api.entity.DeviceDataExample;
import com.lifelight.api.entity.GlucometerData;
import com.lifelight.api.vo.DeviceDataVO;
import com.lifelight.api.vo.XlPersonContractVO;
import com.lifelight.api.vo.XlPersonScheduleVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.util.DateTimeUtil;
import com.lifelight.dubbo.dao.CardApparatusDataMapper;
import com.lifelight.dubbo.dao.DeviceDataMapper;
import com.lifelight.dubbo.dao.DeviceUserRelMapper;
import com.lifelight.dubbo.dao.GlucometerDataMapper;
import com.lifelight.dubbo.dao.XlPersonContractMapper;
import com.lifelight.dubbo.dao.XlPersonScheduleMapper;
import com.lifelight.dubbo.service.DeviceSyncService;

/**
 * 设备同步接口实现
 */
@Service
public class DeviceSyncServiceImpl implements DeviceSyncService {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DeviceSyncServiceImpl.class);

	@Autowired
	private CardApparatusDataMapper cardApparatusDataMapper;
	@Autowired
	private GlucometerDataMapper glucometerDataMapper;
	@Autowired
	private DeviceDataMapper deviceDataMapper;
	@Autowired
	private DeviceUserRelMapper deviceUserRelMapper;
	@Autowired
	private XlPersonScheduleMapper scheduleMapper;
	@Autowired
	private XlPersonContractMapper contractMapper;

	/**
	 * 设备同步接口 主要同步 卡片机 手环 血糖仪 康泰血压计 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceSyncInit(String type, Integer deviceId, String data) {
		Result result = new Result<>(StatusCodes.OK, true);
		JSONObject jb = JSONObject.parseObject(data.trim());

		boolean bo = false;

		// KPJ:卡片机   XTY:血糖仪  SH:蓝牙手环  TWJ:体温计  KT:康泰血压计
		switch(type) {
		// 卡片机
		case "KPJ":
			bo = cardDataSync(deviceId, jb);
			break;
			// 血糖仪
		case "XTY":
			try {
				bo = glucometerDataSync(deviceId, jb);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			// 蓝牙手环
		case "SH":
			bo = braceletDataSync(deviceId, jb);
			break;
			// 体温计
		case "TWJ":
			try {
				bo = temperatureDataSync(deviceId, jb);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			// 康泰血压计
		case "KT":
			bo = ktDataSync(deviceId, jb);
			break;
		default:
			break;
		}


		if(bo) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
		}else {
			result.setResultCode(new ResultCode("FAILED", "添加失败"));
		}
		return result;
	}
	/**
	 * 设备同步接口  微信小程序步数
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceSyncStep(String managerId, String data) {
		Result result = new Result<>(StatusCodes.OK, true);

		DeviceDataVO deviceData = new DeviceDataVO();
		deviceData.setManagerId(managerId);
		deviceData.setDataType("SN");
		deviceData.setStartTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		deviceData.setEndTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		List<DeviceDataVO> list = deviceDataMapper.selectMeasureInfo(deviceData);

		JSONObject jb = JSONObject.parseObject(data.trim());
		String time = String.valueOf(jb.get("timestamp"));
		String step = String.valueOf(jb.get("step"));
		int i=0;
		//		{"date":"2017-09-12 15:30:02","dataType":"SN","name":"步数","units":"步","value":"5000"}
		JSONObject jo = new JSONObject();
		jo.put("value", step);
		jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
		jo.put("units", "步");
		jo.put("name", "步数");
		jo.put("dataType", "SN");
		DeviceData record = new DeviceData();
		if(null!=list && list.size()>0) {
			record.setId(list.get(0).getId());
			record.setUpdateTime(new Date());
			record.setDataFrom("wx");
			record.setDataValue(jo.toString());
			i=deviceDataMapper.updateByPrimaryKeySelective(record);
		}else {
			record.setUpdateTime(new Date());
			record.setCreateTime(new Date());
			record.setManagerId(managerId);
			record.setDataFrom("wx");
			record.setDataType("SN");
			record.setIsAffirm("F");
			record.setDataValue(jo.toString());
			i=deviceDataMapper.insertSelective(record);
		}
		if(i>0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
		}else {
			result.setResultCode(new ResultCode("FAILED", "添加失败"));
		}
		return result;
	}

	/**
	 * 	卡片机数据处理 （主要用于测量心电数据）
	 * 
	 * 	deviceId			// 设备 id
		heartRate        	// 心率
		ecgResult		 	// 心电经算法库得出结论
		ecgImg			 	// 心电图图片   base64
	 * 
	 * @param jb
	 * @return
	 */
	private boolean ktDataSync(Integer deviceId, JSONObject jb) {
		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);
		int id = 0;

		if(null != managerId && !"".equals(managerId)) {

			Date date = new Date();
			Calendar c = new GregorianCalendar();
			c.setTime(date);//设置参数时间
			c.add(Calendar.SECOND,-30);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
			Date dateDel=c.getTime();

			DeviceDataExample example = new DeviceDataExample();
			example.createCriteria().andManagerIdEqualTo(managerId).andDeviceIdEqualTo(deviceId).
			andDataTypeEqualTo("BP").andCreateTimeBetween(dateDel, date);
			List<DeviceData> list = deviceDataMapper.selectByExample(example);
			if(null!=list && list.size()>0) {
				return true;
			}


			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(date);
			deviceData.setUpdateTime(date);
			deviceData.setDataFrom("KT");
			deviceData.setIsAffirm("T");

			deviceData.setDataType(DeviceItemEnum.BP.toString());

			JSONObject jo = new JSONObject();

			// 舒张压 diastolic_blood_pressure
			String diastolicBloodPressure = jb.get("diastolic_blood_pressure").toString();
			// 收缩压 systolic_blood_pressure
			String systolicBloodPressure = jb.get("systolic_blood_pressure").toString();
			// 脉率 pulse_rate
			String pulseRate = jb.get("pulse_rate").toString();

			jo.put("dia", diastolicBloodPressure);
			jo.put("sys", systolicBloodPressure);
			jo.put("pr", pulseRate);

			jo.put("value", systolicBloodPressure + "/" + diastolicBloodPressure);
			jo.put("date", DateTimeUtil.getFormatDateTime(date));
			jo.put("units", DeviceItemEnum.BP.getUnit());
			jo.put("name", DeviceItemEnum.BP.getName());
			jo.put("dataType", DeviceItemEnum.BP.toString());

			deviceData.setDataValue(jo.toString());

			id = deviceDataMapper.insertSelective(deviceData);
		}

		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}


	/**
	 * 	卡片机数据处理 （主要用于测量心电数据）
	 * 
	 * 	deviceId			// 设备 id
		heartRate        	// 心率
		ecgResult		 	// 心电经算法库得出结论
		ecgImg			 	// 心电图图片   base64
	 * 
	 * @param jb
	 * @return
	 */
	private boolean cardDataSync(Integer deviceId, JSONObject jb) {
		CardApparatusData cardApparatusData = new CardApparatusData();
		cardApparatusData.setDeviceId(deviceId);
		cardApparatusData.setHeartRateValue(Float.valueOf(jb.getString("heartRate")));
		cardApparatusData.setEcgResult("");
		cardApparatusData.setEcgImgUrl(jb.getString("ecgImg"));
		cardApparatusData.setEcgData(jb.getString("ecgResult"));
		cardApparatusData.setCreateTime(new Date());

		int id = cardApparatusDataMapper.insertSelective(cardApparatusData);

		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);

		if(null != managerId && !"".equals(managerId)) {
			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(new Date());
			deviceData.setUpdateTime(new Date());
			deviceData.setDataFrom(DeviceItemEnum.KPJ.toString());

			deviceData.setDataType(DeviceItemEnum.HR.toString());

			JSONObject jo = new JSONObject();

			jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jo.put("name", DeviceItemEnum.HR.getName());
			jo.put("dataType", DeviceItemEnum.HR.toString());
			jo.put("value", jb.getString("heartRate"));
			jo.put("cardEcgImgUrl", jb.getString("ecgImg"));
			jo.put("ecgResult", jb.getString("ecgResult"));
			jo.put("units", DeviceItemEnum.HR.getUnit());

			deviceData.setDataValue(jo.toString());

			deviceDataMapper.insertSelective(deviceData);
		}

		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 血糖仪设备
	 * 
	 * @param jb
	 * @return
	 * @throws ParseException 
	 */
	private boolean glucometerDataSync(Integer deviceId, JSONObject jb) throws ParseException {

		GlucometerData glucometerData = new GlucometerData();
		glucometerData.setDeviceId(deviceId);
		glucometerData.setBloodGlucoseValue(Float.valueOf(jb.getString("value")));
		glucometerData.setCreateTime(new Date());

		glucometerDataMapper.insertSelective(glucometerData);

		Integer id = null;

		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);

		if(null != managerId && !"".equals(managerId)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
			Date date = sdf.parse(jb.getString("time"));  

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR, 8);
			Date nowTime = calendar.getTime();

			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(nowTime);
			deviceData.setUpdateTime(nowTime);
			deviceData.setDataFrom(DeviceItemEnum.XTY.toString());

			deviceData.setDataType(DeviceItemEnum.BG.toString());

			JSONObject jo = new JSONObject();

			jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jo.put("name", DeviceItemEnum.BG.getName());
			jo.put("dataType", DeviceItemEnum.BG.toString());
			jo.put("value", jb.getString("value"));
			jo.put("units", DeviceItemEnum.BG.getUnit());

			deviceData.setDataValue(jo.toString());

			id = deviceDataMapper.insertSelective(deviceData);
		}

		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 蓝牙手环设备
	 * 
	 * @param jb
	 * @return
	 */
	private boolean braceletDataSync(Integer deviceId, JSONObject jb) {
		return false;
	}

	/**
	 * 蓝牙体温计设备
	 * 
	 * @param jb
	 * @return
	 * @throws ParseException 
	 */
	private boolean temperatureDataSync(Integer deviceId, JSONObject jb) throws ParseException {

		Integer id = null;

		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);

		if(null != managerId && !"".equals(managerId)) {
			Date date = new Date();

			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(date);
			deviceData.setUpdateTime(date);
			deviceData.setDataFrom(DeviceItemEnum.TWJ.toString());

			deviceData.setDataType(DeviceItemEnum.TE.toString());

			JSONObject jo = new JSONObject();

			jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jo.put("name", DeviceItemEnum.TE.getName());
			jo.put("dataType", DeviceItemEnum.TE.toString());
			jo.put("value", jb.getString("value"));
			jo.put("units", DeviceItemEnum.TE.getUnit());

			deviceData.setDataValue(jo.toString());

			id = deviceDataMapper.insertSelective(deviceData);
		}

		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 设备同步接口 仅支持一体机同步
	 */
	@Override
	public Map<String, Object> deviceSyncByYtj(Integer deviceId, String locationId,
			String type, String data, String managerId, String time) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 一体机数据转 JSON
		JSONObject jb = JSONObject.parseObject(data.trim());
		// 一体机数据整合处理 
		JSONObject jsonData = new JSONObject();

		DeviceData deviceData = new DeviceData();
		deviceData.setDeviceId(deviceId);
		deviceData.setManagerId(managerId);
		deviceData.setIsAffirm("T");
		deviceData.setDeviceLocationid(locationId);

		if("ECG_data".equals(type)) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				deviceData.setCreateTime(sdf.parse(time));
				deviceData.setUpdateTime(sdf.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			deviceData.setCreateTime(new Date());
			deviceData.setUpdateTime(new Date());
		}
		deviceData.setDataFrom(DeviceItemEnum.YTJ.toString());
		// 十二导心电数据
		if("heart_exam".equals(type)) {
			String imgUrl = jb.get("twelve_lead_electrocardiogram").toString();

			DeviceData hrData = deviceDataMapper.getDataByUserTimeByMin(deviceData.getManagerId(), time, "HR");

			if(null != hrData) {

				JSONObject jo = JSONObject.parseObject(hrData.getDataValue().trim());

				JSONObject jos = new JSONObject();

				jos.put("date", time);
				jos.put("name", DeviceItemEnum.HR.getName());
				jos.put("dataType", DeviceItemEnum.HR.toString());
				jos.put("value", jo.getString("value"));
				jos.put("cardEcgImgUrl", imgUrl);
				jos.put("ecgResult", "");
				jos.put("units", DeviceItemEnum.HR.getUnit());

				int count = deviceDataMapper.updateHrData(managerId, time, jos.toString());

				if(count > 0) {
					result.put("message", "添加成功");
					result.put("error_code", "0");
				}else {
					result.put("message", "添加失败");
					result.put("error_code", "-1");
				}
				return result;
			}else {
				result.put("message", "添加失败");
				result.put("error_code", "-1");
				return result;
			}
		}else if("body_composition_analysis".equals(type)) {
			//          // height
			String height = jb.get("height").toString();
			//          // weight
			String weight = jb.get("weight").toString();
			//          // BMI
			String bmi = jb.get("BMI").toString();
			//          // body_fat_percentage
			String bodyFatPercentage = jb.get("body_fat_percentage").toString();
			//          // water_percentage
			String waterPercentage = jb.get("water_percentage").toString();
			//          // muscle_content
			String muscleContent = jb.get("muscle_content").toString();
			//          // fat_content
			//      	String fatContent = jb.get("fat_content").toString();
			//          // basic_metabolism_rate
			String basicMetabolismRate = jb.get("basic_metabolism_rate").toString();
			//          // visceral_fat_index
			String visceralFatIndex = jb.get("visceral_fat_index").toString();
			//          // standard_weight
			//      	String standardWeight = jb.get("standard_weight").toString();
			//          // fat_control
			String fatControl = jb.get("fat_control").toString();
			//          // muscle_control
			//			String muscleControl = jb.get("muscle_control").toString();
			//          // weight_control
			//      	String weightControl = jb.get("weight_control").toString();
			//          // bone_weight
			String boneWeight = jb.get("bone_weight").toString();
			//          // fat_level
			//      	String fatLevel = jb.get("fat_level").toString();
			//          // health_score
			//      	String healthScore = jb.get("health_score").toString();
			//          // degreased_body_weight
			//      	String degreasedBodyWeight = jb.get("degreased_body_weight").toString();
			//          // body_type
			//      	String bodyType = jb.get("body_type").toString();
			//          // protein_weight
			String proteinWeight = jb.get("protein_weight").toString();

			deviceData.setDataValue(jb.toString());
			deviceData.setDataType(DeviceItemEnum.BODY.toString());
			deviceDataMapper.insertSelective(deviceData);

			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));

			// 水分比
			if(null != waterPercentage || !"".equals(waterPercentage)) {
				jsonData.put("value", waterPercentage);
				jsonData.put("units", DeviceItemEnum.WP.getUnit());
				jsonData.put("name", DeviceItemEnum.WP.getName());
				jsonData.put("dataType", DeviceItemEnum.WP.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.WP.toString());
				deviceDataMapper.insertSelective(deviceData);
			}

			// 肌肉
			if(null != muscleContent || !"".equals(muscleContent)) {
				jsonData.put("weight", weight);

				jsonData.put("value", muscleContent);
				jsonData.put("units", DeviceItemEnum.MUCALE.getUnit());
				jsonData.put("name", DeviceItemEnum.MUCALE.getName());
				jsonData.put("dataType", DeviceItemEnum.MUCALE.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.MUCALE.toString());
				deviceDataMapper.insertSelective(deviceData);
			}

			// 骨量
			if(null != boneWeight || !"".equals(boneWeight)) {
				jsonData.put("value", boneWeight);
				jsonData.put("units", DeviceItemEnum.BONE.getUnit());
				jsonData.put("name", DeviceItemEnum.BONE.getName());
				jsonData.put("dataType", DeviceItemEnum.BONE.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.BONE.toString());
				deviceDataMapper.insertSelective(deviceData);
			}

			// 基础代谢率
			if(null != basicMetabolismRate || !"".equals(basicMetabolismRate)) {
				jsonData.put("value", basicMetabolismRate);
				jsonData.put("units", DeviceItemEnum.BMR.getUnit());
				jsonData.put("name", DeviceItemEnum.BMR.getName());
				jsonData.put("dataType", DeviceItemEnum.BMR.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.BMR.toString());
				deviceDataMapper.insertSelective(deviceData);
			}

			// 蛋白质
			if(null != proteinWeight || !"".equals(proteinWeight)) {
				jsonData.put("weight", weight);

				jsonData.put("value", proteinWeight);
				jsonData.put("units", DeviceItemEnum.PIW.getUnit());
				jsonData.put("name", DeviceItemEnum.PIW.getName());
				jsonData.put("dataType", DeviceItemEnum.PIW.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.PIW.toString());
				deviceDataMapper.insertSelective(deviceData);
			}

			// 体脂
			if(null != bodyFatPercentage || !"".equals(bodyFatPercentage)) {
				jsonData.put("fc", fatControl);
				jsonData.put("vfp", visceralFatIndex);

				jsonData.put("value", bodyFatPercentage);
				jsonData.put("units", DeviceItemEnum.FP.getUnit());
				jsonData.put("name", DeviceItemEnum.FP.getName());
				jsonData.put("dataType", DeviceItemEnum.FP.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.FP.toString());
				deviceDataMapper.insertSelective(deviceData);
			}

			// BMI
			if(null != bmi || !"".equals(bmi)) {
				jsonData.put("weValue", weight);
				jsonData.put("heValue", height);
				jsonData.put("value", bmi);
				jsonData.put("units", DeviceItemEnum.BMI.getUnit());
				jsonData.put("name", DeviceItemEnum.BMI.getName());
				jsonData.put("dataType", DeviceItemEnum.BMI.toString());

				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.BMI.toString());
				deviceDataMapper.insertSelective(deviceData);
				synYTJtoScheduleData(jb,"BMI",deviceData.getManagerId());
			}

			result.put("message", "添加成功");
			result.put("error_code", "0");
			return result;

		}else {
			jsonData = disposeData(deviceData, type, jb);
		}

		switch(type) {
		// 血压
		case "blood_pressure":
			deviceData.setDataType(DeviceItemEnum.BP.toString());
			break;
			// 血氧饱和度
		case "blood_oxygen":
			deviceData.setDataType(DeviceItemEnum.BO.toString());
			break;
			// 体温
		case "temperature":
			deviceData.setDataType(DeviceItemEnum.TE.toString());
			break;
			// 尿常规
		case "urine_routine":
			deviceData.setDataType(DeviceItemEnum.RU.toString());
			break;		
			// 血糖
		case "glucose_metabolism_analysis":
			deviceData.setDataType(DeviceItemEnum.BG.toString());
			break;	
			// 总胆固醇
		case "total_cholesterol_analysis":
			deviceData.setDataType(DeviceItemEnum.CHOL.toString());
			break;	
			//  血液尿酸
		case "renal_function":
			deviceData.setDataType(DeviceItemEnum.UA.toString());
			break;	
			//  血红蛋白
		case "BloodRoutineTest":
			deviceData.setDataType(DeviceItemEnum.BHB.toString());
			break;	
			//  ECG 数据上传
		case "ECG_data":
			deviceData.setDataType(DeviceItemEnum.ECG.toString());
			break;	
			//  体脂称 健康数据
		case "body_composition_analysis":
			//deviceData.setDataType(DeviceItemEnum.BODY.toString());
			break;	
			//  血脂
		case "blood_lipid_level_analysis":
			deviceData.setDataType(DeviceItemEnum.BF.toString());
			break;	
			//  血常规
		case "blood_routine":
			deviceData.setDataType(DeviceItemEnum.BR.toString());
			break;	
			//  十二导心电 heart_exam
		default:
			break;	
		}

		deviceData.setDataValue(jsonData.toString());

		int count = deviceDataMapper.insertSelective(deviceData);

		if(count > 0) {
			result.put("message", "添加成功");
			result.put("error_code", "0");
		}else {
			result.put("message", "添加失败");
			result.put("error_code", "-1");
		}
		return result;
	}

	/**
	 * 处理数据
	 * @param type
	 * @param jb
	 * @return
	 */
	public JSONObject disposeData(DeviceData deviceData, String type, JSONObject jb) {
		JSONObject jsonData = new JSONObject();
		switch (type) {
		// 血压数据
		case "blood_pressure":

			// 舒张压 diastolic_blood_pressure
			String diastolicBloodPressure = jb.get("diastolic_blood_pressure").toString();
			// 收缩压 systolic_blood_pressure
			String systolicBloodPressure = jb.get("systolic_blood_pressure").toString();
			// 脉率 pulse_rate
			String pulseRate = jb.get("pulse_rate").toString();

			jsonData.put("dia", diastolicBloodPressure);
			jsonData.put("sys", systolicBloodPressure);
			jsonData.put("pr", pulseRate);

			jsonData.put("value", systolicBloodPressure + "/" + diastolicBloodPressure);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.BP.getUnit());
			jsonData.put("name", DeviceItemEnum.BP.getName());
			jsonData.put("dataType", DeviceItemEnum.BP.toString());
			//同步到检查表
			synYTJtoScheduleData(jb,"BP",deviceData.getManagerId());

			break;
			// 血氧数据
		case "blood_oxygen":

			// 血氧饱和度 blood_oxygen
			String bloodOxygen = jb.get("blood_oxygen").toString();
			// 脉率 pulse_rate
			String pr = jb.get("pulse_rate").toString();

			jsonData.put("pr", pr);

			jsonData.put("value", bloodOxygen);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.BO.getUnit());
			jsonData.put("name", DeviceItemEnum.BO.getName());
			jsonData.put("dataType", DeviceItemEnum.BO.toString());
			//同步到检查表
			synYTJtoScheduleData(jb,"BO",deviceData.getManagerId());
			break;
			// 体温数据
		case "temperature":

			// 体温 temperature
			String temperatureValue = jb.get("temperature").toString();

			jsonData.put("value", temperatureValue);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.TE.getUnit());
			jsonData.put("name", DeviceItemEnum.TE.getName());
			jsonData.put("dataType", DeviceItemEnum.TE.toString());
			//同步到检查表
			synYTJtoScheduleData(jb,"TE",deviceData.getManagerId());
			break;
			// 尿常规数据
		case "urine_routine":

			//	            // 尿胆原 urobilinogen
			//	        	String urobilinogen = jb.get("urobilinogen").toString();
			//	            // 尿潜血 urine_occult_blood
			//	        	String urineOccultBlood = jb.get("urine_occult_blood").toString();
			//	            // 尿胆红素 urine_bilirubin
			//	        	String urineBilirubin = jb.get("urine_bilirubin").toString();
			//	            // 尿酮体 urine_ketone
			//	        	String urineKetone = jb.get("urine_ketone").toString();
			//	            // 尿葡萄糖 urine_glucose
			//	        	String urineGlucose = jb.get("urine_glucose").toString();
			//	            // 尿蛋白 urine_protein
			//	        	String urineProtein = jb.get("urine_protein").toString();
			//	            // 尿酸碱度 urine_PH
			//	        	String urinePh = jb.get("urine_PH").toString();
			//	            // 亚硝酸盐 urine_nitrite
			//	        	String urineNitrite = jb.get("urine_nitrite").toString();
			//	            // 尿液白细胞 urine_leukocytes
			//	        	String urineLeukocytes = jb.get("urine_leukocytes").toString();
			//	            // 尿比重 urine_specific_gravity
			//	        	String urineSpecificGravity = jb.get("urine_specific_gravity").toString();
			//	            // 尿液维生素C urine_VitC
			//	        	String urineVitc = jb.get("urine_VitC").toString();

			jsonData.put("value", jb);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.RU.getUnit());
			jsonData.put("name", DeviceItemEnum.RU.getName());
			jsonData.put("dataType", DeviceItemEnum.RU.toString());
			//同步到检查表
			synYTJtoScheduleData(jb,"RU",deviceData.getManagerId());
			break;
			// 血糖数据
		case "glucose_metabolism_analysis":

			// 血糖 blood_glucose
			String bloodGlucose = jb.get("blood_glucose").toString();

			jsonData.put("value", bloodGlucose);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.BG.getUnit());
			jsonData.put("name", DeviceItemEnum.BG.getName());
			jsonData.put("dataType", DeviceItemEnum.BG.toString());
			jsonData.put("flag", jb.get("flag").toString());
			//同步到检查表
			synYTJtoScheduleData(jb,"BG",deviceData.getManagerId());
			break;
			// 总胆固醇数据
		case "total_cholesterol_analysis":

			// 总胆固醇 total_cholesterol
			String totalCholesterol = jb.get("total_cholesterol").toString();

			jsonData.put("value", totalCholesterol);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.CHOL.getUnit());
			jsonData.put("name", DeviceItemEnum.CHOL.getName());
			jsonData.put("dataType", DeviceItemEnum.CHOL.toString());		
			//同步到检查表
			synYTJtoScheduleData(jb,"CHOL",deviceData.getManagerId());
			break;
			// 血液尿酸数据
		case "renal_function":

			// 血液尿酸 serum_uric_acid
			String urineAcid = jb.get("urine_uric_acid").toString();

			jsonData.put("value", urineAcid);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.UA.getUnit());
			jsonData.put("name", DeviceItemEnum.UA.getName());
			jsonData.put("dataType", DeviceItemEnum.UA.toString());
			//同步到检查表
			synYTJtoScheduleData(jb,"UA",deviceData.getManagerId());
			break;
			// 血红蛋白数据
		case "BloodRoutineTest":

			// 血红蛋白浓度(HGB) Hemoglobin
			String bloodHemoglobin = jb.get("Hemoglobin").toString();
			// 红细比容(HCT) Hematocrit
			String bloodHematocrit = jb.get("Hematocrit").toString();

			jsonData.put("hct", bloodHematocrit);

			jsonData.put("hb", bloodHemoglobin);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", DeviceItemEnum.BHB.getUnit());
			jsonData.put("name", DeviceItemEnum.BHB.getName());
			jsonData.put("dataType", DeviceItemEnum.BHB.toString());

			break;
			// ECG_data数据
		case "ECG_data":

			//	            // ECG_rr
			//	        	String rr = jb.get("ECG_rr").toString();
			//	            // ECG_p_r
			//	        	String pr = jb.get("ECG_p_r").toString();
			//	            // ECG_qrs
			//	        	String qrs = jb.get("ECG_qrs").toString();
			//	            // ECG_qt
			//	        	String qt = jb.get("ECG_qt").toString();
			//	            // ECG_qtc
			//	        	String qtc = jb.get("ECG_qtc").toString();
			//	            // ECG_p_axis
			//	        	String pAxis = jb.get("ECG_p_axis").toString();
			//	            // ECG_qrs_axis
			//	        	String qrsAxis = jb.get("ECG_qrs_axis").toString();
			//	            // ECG_t_axis
			//	        	String tAxis = jb.get("ECG_t_axis").toString();
			//	            // ECG_rv5
			//	        	String rv5 = jb.get("ECG_rv5").toString();
			//	            // ECG_sv1
			//	        	String sv1 = jb.get("ECG_sv1").toString();
			//	            // ECG_rv5_svl
			//	        	String rv5Svl = jb.get("ECG_rv5_svl").toString();
			//	            // heart_rate
			String hr = jb.get("heart_rate").toString();

			jsonData.put("value", jb);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(deviceData.getCreateTime()));
			jsonData.put("units", DeviceItemEnum.ECG.getUnit());
			jsonData.put("name", DeviceItemEnum.ECG.getName());
			jsonData.put("dataType", DeviceItemEnum.ECG.toString());

			if(null != hr) {
				deviceData.setDataType(DeviceItemEnum.HR.toString());

				JSONObject hrObject = new JSONObject();

				hrObject.put("date", DateTimeUtil.getFormatDateTime(new Date()));
				hrObject.put("name", DeviceItemEnum.HR.getName());
				hrObject.put("dataType", DeviceItemEnum.HR.toString());
				hrObject.put("units", DeviceItemEnum.HR.getUnit());
				hrObject.put("value", hr);
				hrObject.put("cardEcgImgUrl", "");
				hrObject.put("ecgResult", "");

				deviceData.setDataValue(hrObject.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			break;
			// 体脂称数据
		case "body_composition_analysis":

			break;
			// 血脂数据
		case "blood_lipid_level_analysis":

			// triglyceride 甘油三酯
			String TG = jb.get("triglyceride").toString();
			// total_cholesterol 总胆固醇
			String CHOL = jb.get("total_cholesterol").toString();
			// low_density_lipoprotein 低密度脂蛋白胆固醇
			String LDL = jb.get("low_density_lipoprotein").toString();
			// high_density_lipoprotein 高密度脂蛋白胆固醇
			String HDL = jb.get("high_density_lipoprotein").toString();
			// cholesterol_esters
			String cholesterolEsters = jb.get("cholesterol_esters").toString();

			jsonData.put("TG", TG);
			jsonData.put("LDL", LDL);
			jsonData.put("HDL", HDL);
			jsonData.put("CE", cholesterolEsters);

			jsonData.put("chol", CHOL);
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jsonData.put("units", "mmol/L");
			jsonData.put("name", DeviceItemEnum.BF.getName());
			jsonData.put("dataType", DeviceItemEnum.BF.toString());

			//同步到检查表
			synYTJtoScheduleData(jb,"BF",deviceData.getManagerId());
			break;
			// 血常规数据		
		case "blood_routine":

			//	            // WBC
			//	            String WBC = jb.get("white_blood_cell_count").toString();
			//	            if("****".equals(WBC)){
			//	                WBC = null;
			//	            }else{
			//	            }
			//	            // RBC
			//	            String RBC = jb.get("red_blood_cell_count").toString();
			//	            if("****".equals(RBC)){
			//	                RBC = null;
			//	            }else{
			//	            }
			//	            // HB
			//	            String HB = jb.get("hemoglobin").toString();
			//	            if("****".equals(HB)){
			//	                HB = null;
			//	            }else{
			//	            }
			//	            // HCT
			//	            String HCT = jb.get("hematocrit").toString();
			//	            if("****".equals(HCT)){
			//	                HCT = null;
			//	            }else{
			//	            }
			//	            // MCV
			//	            String MCV = jb.get("red_blood_cell_mean_corpuscular_volume").toString();
			//	            if("****".equals(MCV)){
			//	                MCV = null;
			//	            }else{
			//	            }
			//	            // MCH
			//	            String MCH = jb.get("mean_corpuscular_hemoglobin").toString();
			//	            if("****".equals(MCH)){
			//	                MCH = null;
			//	            }else{
			//	            }
			//	            // MCHC
			//	            String MCHC = jb.get("mean_corpuscular_hemoglobin_concentration").toString();
			//	            if("****".equals(MCHC)){
			//	                MCHC = null;
			//	            }else{
			//	            }
			//	            // PLT
			//	            String PLT = jb.get("platelet_count").toString();
			//	            if("****".equals(PLT)){
			//	                PLT = null;
			//	            }else{
			//	            }
			//	            // LYM_ratio
			//	            String LYM_ratio = jb.get("lymphocyte_ratio").toString();
			//	            if("****".equals(LYM_ratio)){
			//	                LYM_ratio = null;
			//	            }else{
			//	            }
			//	            // MID_ratio
			//	            String MID_ratio = jb.get("intermediate_cell_ratio").toString();
			//	            if("****".equals(MID_ratio)){
			//	                MID_ratio = null;
			//	            }else{
			//	            }
			//	            // GRAN_ratio
			//	            String GRAN_ratio = jb.get("neutrophils_ratio").toString();
			//	            if("****".equals(GRAN_ratio)){
			//	                GRAN_ratio = null;
			//	            }else{
			//	            }
			//	            // LYM_tatol
			//	            String LYM_tatol = jb.get("lymphocytes").toString();
			//	            if("****".equals(LYM_tatol)){
			//	                LYM_tatol = null;
			//	            }else{
			//	            }
			//	            // MID_tatol
			//	            String MID_tatol = jb.get("intermediate_cell").toString();
			//	            if("****".equals(MID_tatol)){
			//	                MID_tatol = null;
			//	            }else{
			//	            }
			//	            // GRAN_tatol
			//	            String GRAN_tatol = jb.get("neutrophils").toString();
			//	            if("****".equals(GRAN_tatol)){
			//	                GRAN_tatol = null;
			//	            }else{
			//	            }
			//	            // RDW_SD
			//	            String RDW_SD = jb.get("red_blood_cell_volume_distributing_width_standard_deviation").toString();
			//	            if("****".equals(RDW_SD)){
			//	                RDW_SD = null;
			//	            }else{
			//	            }
			//	            // RDW_CV
			//	            String RDW_CV = jb.get("red_blood_cell_volume_distributing_width_coefficient_variation").toString();
			//	            if("****".equals(RDW_CV)){
			//	                RDW_CV = null;
			//	            }else{
			//	            }
			//	            // PDW
			//	            String PDW = jb.get("platelet_distribution_width").toString();
			//	            if("****".equals(PDW)){
			//	                PDW = null;
			//	            }else{
			//	            }
			//	            // MPV
			//	            String MPV = jb.get("mean_platelet_volume").toString();
			//	            if("****".equals(MPV)){
			//	                MPV = null;
			//	            }else{
			//	            }
			//	            // PCT
			//	            String PCT = jb.get("thrombocytocrit").toString();
			//	            if("****".equals(PCT)){
			//	                PCT = null;
			//	            }else{
			//	            }
			//	            // P_LCR
			//	            String P_LCR = jb.get("platelet_large_cell_ratio").toString();
			//	            if("****".equals(P_LCR)){
			//	                P_LCR = null;
			//	            }else{
			//	            }
			//	            // P_LCC
			//	            String P_LCC = jb.get("platelet_large_cell_count").toString();
			//	            if("****".equals(P_LCC)){
			//	                P_LCC = null;
			//	            }else{
			//	            }

			//	            jsonData.put("value", jb);
			//	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			//	            jsonData.put("units", DeviceItemEnum.BR.getUnit());
			//	            jsonData.put("name", DeviceItemEnum.BR.getName());
			//	            jsonData.put("dataType", DeviceItemEnum.BR.toString());

			break;
		default:
			break;
		}
		return jsonData;
	}

	@SuppressWarnings("unchecked")
	private void synYTJtoScheduleData(JSONObject jb,String type,String managerId) {

		logger.info("type"+type+"    jb"+jb.toJSONString()+"  managerId"+managerId);
		XlPersonContractVO vo = new XlPersonContractVO();
		vo.setManagerId(managerId);
		XlPersonContractVO contractvo = contractMapper.selectContract(vo);
		if(null!=contractvo) {//当前用户 签过家医
			Map<String,String> resultMap = new HashMap<String,String>();
			Map<String,Object> personMap = new HashMap<String,Object>();
			XlPersonScheduleVO record = new XlPersonScheduleVO();
			String contractId = String.valueOf(contractvo.getId());
			record.setContractId(contractId);
			personMap.put("name", contractvo.getName());
			personMap.put("sex", contractvo.getSex());
			personMap.put("time",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			boolean b = false;
			Map map=new HashMap<>();
			logger.info("contractId "+contractId);
			String examinfo="{\"name\":\"\",\"checkNum\":\"\",\"cardNum\":\"\",\"checkDate\":\"\",\"signOrginName\":\"\","
					+ "\"symptom\":\"\",\"tiwen\":\"\",\"mailv\":\"\",\"huxipinlv\":\"\",\"bpLeftHei\":\"\",\"bpLeftlow\":\"\","
					+ "\"bpRightHei\":\"\",\"bpRightlow\":\"\",\"height\":\"\",\"weight\":\"\",\"yaowei\":\"\",\"BMI\":\"\","
					+ "\"zhilifen\":\"\",\"yiyufen\":\"\",\"mzdlsj\":\"\",\"jcdlsj\":\"\",\"dlfs\":\"\",\"ysxg\":\"\","
					+ "\"rxyl\":\"\",\"ksxynl\":\"\",\"jynl\":\"\",\"ryjl\":\"\",\"ksyjnl\":\"\",\"ksjjnl\":\"\",\"yjzl\":\"\","
					+ "\"gongzhong\":\"\",\"cysj\":\"\",\"fenchen\":\"\",\"fenchen_fhcs_desc\":\"\",\"fswz\":\"\","
					+ "\"fswz_fhcs_desc\":\"\",\"wlys\":\"\",\"wlys_fhcs_desc\":\"\",\"hxwz\":\"\",\"hxwz_fhcs_desc\":\"\","
					+ "\"duwu\":\"\",\"duwu_fhcs_desc\":\"\",\"others\":\"\",\"others_fhcs_desc\":\"\",\"chilie\":\"\","
					+ "\"quechi1\":\"\",\"quechi2\":\"\",\"quechi3\":\"\",\"quechi4\":\"\",\"quchi1\":\"\",\"quchi2\":\"\","
					+ "\"quchi3\":\"\",\"quchi4\":\"\",\"yichijiaya1\":\"\",\"yichijiaya2\":\"\",\"yichijiaya3\":\"\","
					+ "\"yichijiaya4\":\"\",\"leftshili\":\"\",\"rightshili\":\"\",\"leftjzshili\":\"\",\"rightjzshili\":\"\","
					+ "\"yandi_desc\":\"\",\"hongmo_desc\":\"\",\"pifu_desc\":\"\",\"linbaji_desc\":\"\",\"fluoyin_desc\":\"\","
					+ "\"fhxy_desc\":\"\",\"xinlv\":\"\",\"zayin_desc\":\"\",\"fbyt_desc\":\"\",\"fbbk_desc\":\"\",\"fbgd_desc\":\"\",\"fbpd_desc\":\"\",\"fbydzy_desc\":\"\","
					+ "\"gmzz_desc\":\"\",\"ruxian\":\"\",\"fkwy_desc\":\"\",\"yindao_desc\":\"\",\"gongjing_desc\":\"\",\"gongti_desc\":\"\",\"fujian_desc\":\"\",\"chatiqita\":\"\",\"blood_xhdb\":\"\",\"blood_bxb\":\"\",\"blood_xxb\":\"\",\"blood_qita\":\"\",\"ncg_qita\":\"\",\"kfxt\":\"\",\"xindiantu_desc\":\"\",\"nwlxdb\":\"\",\"thxhdb\":\"\",\"ggn_ALT\":\"\",\"ggn_AST\":\"\",\"ggn_ALBT\":\"\",\"ggn_TBIL\":\"\",\"ggn_DBIL\":\"\",\"sgn_xqjg\":\"\",\"sgn_xnsa\":\"\","
					+ "\"sgn_xjnd\":\"\",\"ggn_xnnd\":\"\","
					+ "\"xz_CHO\":\"\",\"xz_TG\":\"\",\"xz_LDL-C\":\"\",\"xz_HDL-C\":\"\",\"xiongpian_desc\":\"\",\"Bchao_desc\":\"\",\"gjtp_desc\":\"\",\"jkfzjc_other\":\"\",\"nxgjb\":\"\",\"szjb\":\"\",\"xzjb\":\"\","
					+ "\"xgjb\":\"\",\"ybjb\":\"\",\"sjxtjb_desc\":\"\",\"qtxtjb_desc\":\"\",\"zys_enterTime1\":\"\",\"zys_exitTime1\":\"\","
					+ "\"zys_reason1\":\"\",\"zys_yljgmc1\":\"\",\"zys_bah1\":\"\",\"zys_enterTime2\":\"\",\"zys_exitTime2\":\"\",\"zys_reason2\":\"\","
					+ "\"zys_yljgmc2\":\"\",\"zys_bah2\":\"\",\"jtbcs_enterTime1\":\"\",\"jtbcs_exitTime1\":\"\",\"jtbcs_reason1\":\"\",\"jtbcs_yljgmc1\":\"\",\"jtbcs_bah1\":\"\",\"jtbcs_enterTime2\":\"\",\"jtbcs_exitTime2\":\"\","
					+ "\"jtbcs_reason2\":\"\",\"jtbcs_yljgmc2\":\"\",\"jtbcs_bah2\":\"\",\"yongyao_name1\":\"\",\"yongyao_yongfa1\":\"\",\"yongyao_meici1_1\":\"\",\"yongyao_meici1_2\":\"\",\"yongyao_meitian1\":\"\",\"yongyao_yysj1\":\"\","
					+ "\"yongyao_name2\":\"\",\"yongyao_yongfa2\":\"\",\"yongyao_meici2_1\":\"\",\"yongyao_meici2_2\":\"\",\"yongyao_meitian2\":\"\",\"yongyao_yysj2\":\"\",\"yongyao_name3\":\"\",\"yongyao_yongfa3\":\"\",\"yongyao_meici3_1\":\"\",\"yongyao_meici3_2\":\"\",\"yongyao_meitian3\":\"\",\"yongyao_yysj3\":\"\",\"yongyao_name4\":\"\","
					+ "\"yongyao_yongfa4\":\"\",\"yongyao_meici4_1\":\"\",\"yongyao_meici4_2\":\"\",\"yongyao_meitian4\":\"\",\"yongyao_yysj4\":\"\",\"yongyao_name5\":\"\",\"yongyao_yongfa5\":\"\",\"yongyao_meici5_1\":\"\",\"yongyao_meici5_2\":\"\",\"yongyao_meitian5\":\"\","
					+ "\"yongyao_yysj5\":\"\",\"yongyao_name6\":\"\",\"yongyao_yongfa6\":\"\",\"yongyao_meici6_1\":\"\",\"yongyao_meici6_2\":\"\",\"yongyao_meitian6\":\"\",\"yongyao_yysj6\":\"\",\"yimiao_ymmc1\":\"\",\"yimiao_jzrq1\":\"\",\"yimiao_jzjg1\":\"\",\"yimiao_ymmc2\":\"\",\"yimiao_jzrq2\":\"\","
					+ "\"yimiao_jzjg2\":\"\",\"yimiao_ymmc3\":\"\",\"yimiao_jzrq3\":\"\",\"yimiao_jzjg3\":\"\",\"jkpj_yichang1\":\"\",\"jkpj_yichang2\":\"\",\"jkpj_yichang3\":\"\",\"jkpj_yichang4\":\"\",\"jkzd\":\"\",\"wxyskz\":\"\",\"sex\":\"1\",\"marriageStatus\":\"\",\"occupation\":\"4\",\"statusCheck\":\"\","
					+ "\"ziliCheck\":\"\",\"oldRenzhi\":\"\",\"oldqinggan\":\"\",\"dlpl\":\"\",\"xyzk\":\"\",\"yjpl\":\"\",\"sfjj\":\"\",\"jynnsfczj\":\"\",\"zybwhysjcs\":\"\",\"fenchen_fhcs\":\"\",\"fswz_fhcs\":\"\",\"wlys_fhcs\":\"\",\"hxwz_fhcs\":\"\",\"duwu_fhcs\":\"\",\"others_fhcs\":\"\",\"kouchun\":\"\",\"yanbu\":\"\",\"tingli\":\"\","
					+ "\"ydgn\":\"\",\"yandi\":\"\",\"hongmo\":\"\",\"pifu\":\"\",\"fyzx\":\"\",\"linbaji\":\"\",\"fluoyin\":\"\",\"fhxy\":\"\",\"xinlv_select\":\"\",\"zayin\":\"\",\"fbyt\":\"\",\"fbbk\":\"\",\"fbgd\":\"\",\"fbpd\":\"\",\"fbydzy\":\"\",\"xzsz\":\"\","
					+ "\"tnbzbdmbd\":\"\",\"gmzz\":\"\",\"fkwy\":\"\",\"yindao\":\"\",\"gongjing\":\"\",\"gongti\":\"\",\"fujian\":\"\",\"ncg_ndb\":\"\",\"ncg_nt\":\"\",\"ncg_ntt\":\"\",\"ncg_nqx\":\"\",\"xindiantu\":\"\",\"dbqx\":\"\",\"HBsAg\":\"\",\"xiongpian\":\"\","
					+ "\"Bchao\":\"\",\"gjtp\":\"\",\"sjxtjb\":\"\",\"qtxtjb\":\"\",\"yongyao_fyycx1\":\"\",\"yongyao_fyycx2\":\"\",\"yongyao_fyycx3\":\"\",\"yongyao_fyycx4\":\"\",\"yongyao_fyycx5\":\"\",\"yongyao_fyycx6\":\"\",\"jkpj\":\"\"}";
			switch(type) {
			//进度和检查表
			case "RU"://尿常规
				/*record.setProjectType("3");
				List<XlPersonScheduleVO> list =scheduleMapper.selectSchedule(record);
				if(null!=list && list.size()>0 && StringUtil.isEmpty(list.get(0).getProjectValue())) {
					XlPersonScheduleVO xlvo = list.get(0);
					resultMap.put("RU_LEU", String.valueOf(jb.get("urine_leukocytes")));//尿白细胞
					resultMap.put("RU_Glu", String.valueOf(jb.get("urine_glucose")));//尿糖
					resultMap.put("RU_Pro", String.valueOf(jb.get("urine_protein")));//尿蛋白
					net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
					personMap.put("result", resultObject.toString());
					personMap.put("project_type_content", xlvo.getProjectTypeDesc());
					personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
					personMap.put("remark", "");
					net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
					String projectValue = jsonObject.toString();
					xlvo.setProjectValue(projectValue);
					xlvo.setContractId(contractId);
					xlvo.setUpdateTime(new Date());
					xlvo.setInputPerson("4");
					xlvo.setCheckTime(new Date());
					xlvo.setCheckOrg(contractvo.getSignOrgName());
					if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
						Map map = JSON.parseObject(xlvo.getExaminationInfo());
						if(StringUtil.isEmpty(map.get("ncg_ndb").toString())) { //尿蛋白
							map.put("ncg_ndb", jb.get("urine_protein"));
							b = true;
						}
						if(StringUtil.isEmpty(map.get("ncg_nt").toString())) { //尿糖
							map.put("ncg_nt", jb.get("urine_glucose"));
							b = true;
						}
						if(StringUtil.isEmpty(map.get("ncg_nqx").toString())) { //尿潜血
							map.put("ncg_nqx", jb.get("urine_occult_blood"));
							b = true;
						}
						if(StringUtil.isEmpty(map.get("ncg_ntt").toString())) { //尿酮体
							map.put("ncg_ntt", jb.get("urine_ketone"));
							b = true;
						}
						if(b) {
							xlvo.setExaminationInfo(JSON.toJSON(map).toString());
							scheduleMapper.updatByEntity(xlvo);
						}
					}
					scheduleMapper.updateByPrimaryKeySelective(xlvo);
				}*/
				break;
			case "BF"://血脂
				record.setProjectType("6");
				List<XlPersonScheduleVO> list2 =scheduleMapper.selectSchedule(record);
				if(null!=list2 && list2.size()>0) {
					if(StringUtil.isEmpty(list2.get(0).getProjectValue())) {
						XlPersonScheduleVO xlvo = list2.get(0);
						resultMap.put("BF_LDL_C", jb.get("low_density_lipoprotein").toString());//低密度脂蛋白胆固醇
						resultMap.put("BF_CHOL", jb.get("total_cholesterol").toString());//总胆固醇
						resultMap.put("BF_HDL_C", jb.get("high_density_lipoprotein").toString());//高密度脂蛋白胆固醇
						resultMap.put("BF_TG", jb.get("triglyceride").toString());//总甘油三指
						net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						personMap.put("project_type_content", xlvo.getProjectTypeDesc());
						personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
						personMap.put("remark", "");
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
						String projectValue = jsonObject.toString();
						xlvo.setProjectValue(projectValue);
						xlvo.setContractId(contractId);
						xlvo.setUpdateTime(new Date());
						xlvo.setInputPerson("4");
						xlvo.setCheckTime(new Date());
						xlvo.setCheckOrg(contractvo.getSignOrgName());
						if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
							map = JSON.parseObject(xlvo.getExaminationInfo());
						}else {
							map = JSON.parseObject(examinfo);
						}
						if(null!=map && map.size()>0) {
							if(!StringUtil.isEmpty(jb.get("total_cholesterol").toString())) { //总胆固醇
								map.put("xz_CHO", jb.get("total_cholesterol").toString());
								b = true;
							}
							if(!StringUtil.isEmpty(jb.get("triglyceride").toString())) { //甘油三酯
								map.put("xz_TG", jb.get("triglyceride").toString());
								b = true;
							}
							if(!StringUtil.isEmpty(jb.get("low_density_lipoprotein").toString())) { //低密度脂蛋
								map.put("xz_LDL-C", jb.get("low_density_lipoprotein").toString());
								b = true;
							}
							if(!StringUtil.isEmpty(jb.get("high_density_lipoprotein").toString())) { //高密度脂蛋
								map.put("xz_HDL-C", jb.get("high_density_lipoprotein").toString());
								b = true;
							}
							if(b) {
								xlvo.setExaminationInfo(JSON.toJSON(map).toString());
								scheduleMapper.updatByEntity(xlvo);
							}
						}
						scheduleMapper.updateByPrimaryKeySelective(xlvo);
					}else {
						XlPersonScheduleVO xlvo = list2.get(0);
						Map projectValueMap = JSON.parseObject(xlvo.getProjectValue());
						if(null!=projectValueMap && projectValueMap.size()>0) {
							Map map2 = JSON.parseObject(projectValueMap.get("result").toString());
							map2.put("BF_LDL_C", jb.get("low_density_lipoprotein").toString());//低密度脂蛋白胆固醇
							map2.put("BF_CHOL", jb.get("total_cholesterol").toString());//总胆固醇
							map2.put("BF_HDL_C", jb.get("high_density_lipoprotein").toString());//高密度脂蛋白胆固醇
							map2.put("BF_TG", jb.get("triglyceride").toString());//总甘油三指
							net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(map2);
							personMap.put("result", resultObject.toString());
							net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
							String projectValue = jsonObject.toString();
							xlvo.setProjectValue(projectValue);
							xlvo.setContractId(contractId);
							xlvo.setUpdateTime(new Date());
							xlvo.setInputPerson("4");
							xlvo.setCheckTime(new Date());
							xlvo.setCheckOrg(contractvo.getSignOrgName());
							if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
								map = JSON.parseObject(xlvo.getExaminationInfo());
							}else {
								map = JSON.parseObject(examinfo);
							}
							if(null!=map && map.size()>0) {
								if(!StringUtil.isEmpty(jb.get("total_cholesterol").toString())) { //总胆固醇
									map.put("xz_CHO", jb.get("total_cholesterol").toString());
									b = true;
								}
								if(!StringUtil.isEmpty(jb.get("triglyceride").toString())) { //甘油三酯
									map.put("xz_TG", jb.get("triglyceride").toString());
									b = true;
								}
								if(!StringUtil.isEmpty(jb.get("low_density_lipoprotein").toString())) { //低密度脂蛋
									map.put("xz_LDL-C", jb.get("low_density_lipoprotein").toString());
									b = true;
								}
								if(!StringUtil.isEmpty(jb.get("high_density_lipoprotein").toString())) { //高密度脂蛋
									map.put("xz_HDL-C", jb.get("high_density_lipoprotein").toString());
									b = true;
								}
								if(b) {
									xlvo.setExaminationInfo(JSON.toJSON(map).toString());
									scheduleMapper.updatByEntity(xlvo);
								}
							}
							scheduleMapper.updateByPrimaryKeySelective(xlvo);
						}
					}
				}
				break;
			case "CHOL"://胆固醇
				record.setProjectType("6");
				List<XlPersonScheduleVO> listChol =scheduleMapper.selectSchedule(record);
				if(null!=listChol && listChol.size()>0) {
					if(StringUtil.isEmpty(listChol.get(0).getProjectValue())) {
						XlPersonScheduleVO xlvo = listChol.get(0);
						resultMap.put("BF_LDL_C", "");//低密度脂蛋白胆固醇
						resultMap.put("BF_CHOL", jb.get("total_cholesterol").toString());//总胆固醇
						resultMap.put("BF_HDL_C", "");//高密度脂蛋白胆固醇
						resultMap.put("BF_TG", "");//总甘油三指
						net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						personMap.put("project_type_content", xlvo.getProjectTypeDesc());
						personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
						personMap.put("remark", "");
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
						String projectValue = jsonObject.toString();
						xlvo.setProjectValue(projectValue);
						xlvo.setContractId(contractId);
						xlvo.setUpdateTime(new Date());
						xlvo.setInputPerson("4");
						xlvo.setCheckTime(new Date());
						xlvo.setCheckOrg(contractvo.getSignOrgName());
						if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
							map = JSON.parseObject(xlvo.getExaminationInfo());
						}else {
							map = JSON.parseObject(examinfo);
						}
						if(null!=map && map.size()>0) {
							if(!StringUtil.isEmpty(jb.get("total_cholesterol").toString())) { //总胆固醇
								map.put("xz_CHO", jb.get("total_cholesterol").toString());
								b = true;
							}
							if(b) {
								xlvo.setExaminationInfo(JSON.toJSON(map).toString());
								scheduleMapper.updatByEntity(xlvo);
							}
						}
						scheduleMapper.updateByPrimaryKeySelective(xlvo);
					}else {
						XlPersonScheduleVO xlvo = listChol.get(0);
						Map projectValueMap = JSON.parseObject(xlvo.getProjectValue());
						if(null!=projectValueMap && projectValueMap.size()>0) {
							Map map2 = JSON.parseObject(projectValueMap.get("result").toString());
							map2.put("BF_CHOL", jb.get("total_cholesterol").toString());//总胆固醇
							net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(map2);
							personMap.put("result", resultObject.toString());
							net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
							String projectValue = jsonObject.toString();
							xlvo.setProjectValue(projectValue);
							xlvo.setContractId(contractId);
							xlvo.setUpdateTime(new Date());
							xlvo.setInputPerson("4");
							xlvo.setCheckTime(new Date());
							xlvo.setCheckOrg(contractvo.getSignOrgName());
							if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
								map = JSON.parseObject(xlvo.getExaminationInfo());
							}else {
								map = JSON.parseObject(examinfo);
							}
							if(null!=map && map.size()>0) {
								if(!StringUtil.isEmpty(jb.get("total_cholesterol").toString())) { //总胆固醇
									map.put("xz_CHO", jb.get("total_cholesterol").toString());
									b = true;
								}
								if(b) {
									xlvo.setExaminationInfo(JSON.toJSON(map).toString());
									scheduleMapper.updatByEntity(xlvo);
								}
							}
							scheduleMapper.updateByPrimaryKeySelective(xlvo);
						}
					}
				}
				break;
			case "BG"://血糖
				double value = Double.valueOf(jb.get("blood_glucose").toString());
				if(value>=3.9 && value<=6.1) {
					resultMap.put("xuetang", "1");//血糖
				}else {
					resultMap.put("xuetang", "2");//血糖
				}
				//血糖 更新
				record.setProjectType("2");
				List<XlPersonScheduleVO> list3 =scheduleMapper.selectSchedule(record);
				if(null!=list3 && list3.size()>0) {

					if( StringUtil.isEmpty(list3.get(0).getProjectValue())) {
						XlPersonScheduleVO xlvo = list3.get(0);
						net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						personMap.put("project_type_content", xlvo.getProjectTypeDesc());
						personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
						personMap.put("remark", "");
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
						String projectValue = jsonObject.toString();
						xlvo.setProjectValue(projectValue);
						xlvo.setContractId(contractId);
						xlvo.setUpdateTime(new Date());
						xlvo.setInputPerson("4");
						xlvo.setCheckTime(new Date());
						xlvo.setCheckOrg(contractvo.getSignOrgName());
						if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
							map = JSON.parseObject(xlvo.getExaminationInfo());
						}else {
							map = JSON.parseObject(examinfo);
						}
						if(null!=map && map.size()>0) {
							if(!StringUtil.isEmpty(jb.get("blood_glucose").toString())) { //血糖
								map.put("kfxt", jb.get("blood_glucose").toString());
								b = true;
							}
							if(b) {
								xlvo.setExaminationInfo(JSON.toJSON(map).toString());
								scheduleMapper.updatByEntity(xlvo);
							}
						}
						scheduleMapper.updateByPrimaryKeySelective(xlvo);
					}
					//一体机更新
					list3.clear();
					record.setProjectType("10");
					list3 =scheduleMapper.selectSchedule(record);
					if(null!=list3 && list3.size()>0 && StringUtil.isEmpty(list3.get(0).getProjectValue())) {
						XlPersonScheduleVO xlvo = list3.get(0);
						resultMap.put("YTJ_ru", "");//尿常规
						resultMap.put("YTJ_xuetang", jb.get("blood_glucose").toString());//血糖
						resultMap.put("YTJ_SPO2", "");//血氧饱和度
						resultMap.put("YTJ_ecg", "");//心电图检查
						resultMap.put("YTJ_bf", "");//血脂
						net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						personMap.put("project_type_content", xlvo.getProjectTypeDesc());
						personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
						personMap.put("remark", "");
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
						String projectValue = jsonObject.toString();
						xlvo.setProjectValue(projectValue);
						xlvo.setContractId(contractId);
						xlvo.setInputPerson("4");
						xlvo.setCheckTime(new Date());
						xlvo.setUpdateTime(new Date());
						xlvo.setCheckOrg(contractvo.getSignOrgName());
						scheduleMapper.updateByPrimaryKeySelective(xlvo);
					}else {
						XlPersonScheduleVO xlvo = list3.get(0);
						Map map1 = JSON.parseObject(list3.get(0).getProjectValue());
						if(null!=map1.get("result") && !StringUtil.isEmpty(map1.get("result").toString())) {
							map1 = JSON.parseObject(map1.get("result").toString());
							if(!StringUtil.isEmpty(jb.get("blood_glucose").toString())) {
								map1.put("YTJ_xuetang", jb.get("blood_glucose").toString());
								b =true;
							}
						}
						if(b) {
							net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(map1);
							String projectValue = jsonObject.toString();
							xlvo.setProjectValue(projectValue);
							xlvo.setContractId(contractId);
							xlvo.setInputPerson("4");
							xlvo.setCheckTime(new Date());
							xlvo.setUpdateTime(new Date());
							xlvo.setCheckOrg(contractvo.getSignOrgName());
							scheduleMapper.updateByPrimaryKeySelective(xlvo);
						}
					}

				}
				break;

				//进度
			case "BO"://血氧  一体机
				record.setProjectType("10");
				List<XlPersonScheduleVO> list4 =scheduleMapper.selectSchedule(record);
				if(null!=list4 && list4.size()>0) {
					if( StringUtil.isEmpty(list4.get(0).getProjectValue())) {
						XlPersonScheduleVO xlvo = list4.get(0);
						resultMap.put("YTJ_ru", "");//尿常规
						resultMap.put("YTJ_xuetang", "");//血糖
						resultMap.put("YTJ_SPO2", jb.get("blood_oxygen").toString());//血氧饱和度
						resultMap.put("YTJ_ecg", "");//心电图检查
						resultMap.put("YTJ_bf", "");//血脂
						net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						personMap.put("project_type_content", xlvo.getProjectTypeDesc());
						personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
						personMap.put("remark", "");
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
						String projectValue = jsonObject.toString();
						xlvo.setProjectValue(projectValue);
						xlvo.setContractId(contractId);
						xlvo.setInputPerson("4");
						xlvo.setCheckTime(new Date());
						xlvo.setUpdateTime(new Date());
						xlvo.setCheckOrg(contractvo.getSignOrgName());
						scheduleMapper.updateByPrimaryKeySelective(xlvo);
					}else {
						XlPersonScheduleVO xlvo = list4.get(0);
						Map map1 = JSON.parseObject(list4.get(0).getProjectValue());
						if(null!=map1.get("result") && !StringUtil.isEmpty(map1.get("result").toString())) {
							map1 = JSON.parseObject(map1.get("result").toString());
							if(!StringUtil.isEmpty(jb.get("blood_oxygen").toString())) {
								map1.put("YTJ_SPO2", jb.get("blood_oxygen").toString());
								b =true;
							}
						}
						if(b) {
							net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(map1);
							String projectValue = jsonObject.toString();
							xlvo.setProjectValue(projectValue);
							xlvo.setContractId(contractId);
							xlvo.setInputPerson("4");
							xlvo.setCheckTime(new Date());
							xlvo.setUpdateTime(new Date());
							xlvo.setCheckOrg(contractvo.getSignOrgName());
							scheduleMapper.updateByPrimaryKeySelective(xlvo);
						}
					}
				}
				break;
			case "UA"://尿酸
				record.setProjectType("11");
				List<XlPersonScheduleVO> list5 =scheduleMapper.selectSchedule(record);
				if(null!=list5 && list5.size()>0) {

					if(StringUtil.isEmpty(list5.get(0).getProjectValue())) {
						XlPersonScheduleVO xlvo = list5.get(0);
						resultMap.put("SUA_ua", jb.get("urine_uric_acid").toString());//尿常规
						net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						personMap.put("project_type_content", xlvo.getProjectTypeDesc());
						personMap.put("project_type_desc", xlvo.getProjectTypeDesc());
						personMap.put("remark", "");
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(personMap);
						String projectValue = jsonObject.toString();
						xlvo.setProjectValue(projectValue);
						xlvo.setContractId(contractId);
						xlvo.setUpdateTime(new Date());
						xlvo.setInputPerson("4");
						xlvo.setCheckTime(new Date());
						xlvo.setCheckOrg(contractvo.getSignOrgName());
						scheduleMapper.updateByPrimaryKeySelective(xlvo);
					}
				}
				break;

				//检查表
			case "TE"://体温
				List<XlPersonScheduleVO> list6 =scheduleMapper.selectSchedule(record);
				if(null!=list6 && list6.size()>0 ) {
					XlPersonScheduleVO xlvo = list6.get(0);
					xlvo.setContractId(contractId);
					xlvo.setUpdateTime(new Date());
					if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
						map = JSON.parseObject(xlvo.getExaminationInfo());
					}else {
						map = JSON.parseObject(examinfo);
					}
					if(null!=map && map.size()>0) {
						if(!StringUtil.isEmpty(jb.get("temperature").toString())) { //体温
							map.put("tiwen", jb.get("temperature").toString());
							b = true;
						}
						if(b) {
							xlvo.setExaminationInfo(JSON.toJSON(map).toString());
							scheduleMapper.updatByEntity(xlvo);
						}
					}
				}
				break;
			case "BMI"://mbi  体重  身高
				List<XlPersonScheduleVO> list7 =scheduleMapper.selectSchedule(record);
				if(null!=list7 && list7.size()>0 ) {
					XlPersonScheduleVO xlvo = list7.get(0);
					xlvo.setContractId(contractId);
					xlvo.setUpdateTime(new Date());
					if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
						map = JSON.parseObject(xlvo.getExaminationInfo());
					}else {
						map = JSON.parseObject(examinfo);
					}
					if(null!=map && map.size()>0) {
						if(!StringUtil.isEmpty(jb.get("BMI").toString())) { //mbi
							map.put("BMI", jb.get("BMI").toString());
							b = true;
						}
						if(!StringUtil.isEmpty(jb.get("weight").toString())) { //体重
							map.put("weight", jb.get("weight").toString());
							b = true;
						}
						if(!StringUtil.isEmpty(jb.get("height").toString())) { //身高
							map.put("height", jb.get("height").toString());
							b = true;
						}
						if(b) {
							xlvo.setExaminationInfo(JSON.toJSON(map).toString());
							scheduleMapper.updatByEntity(xlvo);
						}
					}
				}	
				break;
			case "BP"://血压 脉率
				List<XlPersonScheduleVO> list8 =scheduleMapper.selectSchedule(record);
				if(null!=list8 && list8.size()>0 ) {
					XlPersonScheduleVO xlvo = list8.get(0);
					xlvo.setContractId(contractId);
					xlvo.setUpdateTime(new Date());
					if(!StringUtil.isEmpty(xlvo.getExaminationInfo())) {
						map = JSON.parseObject(xlvo.getExaminationInfo());
					}else {
						map = JSON.parseObject(examinfo);
					}
					if(null!=map && map.size()>0) {
						if(!StringUtil.isEmpty(jb.get("pulse_rate").toString())) { //脉率
							map.put("mailv", jb.get("pulse_rate").toString());
							b = true;
						}
						if(b) {
							xlvo.setExaminationInfo(JSON.toJSON(map).toString());
							scheduleMapper.updatByEntity(xlvo);
						}
					}
				}	
				break;
			}
		}
	}

	/**
	 * 设备同步接口 主要同步 免疫定量分析仪
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceSyncByImmunoassay(Integer deviceId, String managerId, String data,
			String type) {
		Result result = new Result<>(StatusCodes.OK, true);

		JSONObject value = JSONObject.parseObject(data.trim());
		JSONObject jb = JSONObject.parseObject(value.getString("value").trim());

		DeviceData deviceData = new DeviceData();

		deviceData.setManagerId(managerId);
		deviceData.setDeviceId(deviceId);
		deviceData.setCreateTime(new Date());
		deviceData.setUpdateTime(new Date());
		deviceData.setDataFrom(DeviceItemEnum.MY.toString());
		JSONObject jo = new JSONObject();
		jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));

		switch (type) {
		// cTnI 心肌肌钙蛋白I
		case "1":
			deviceData.setDataType(DeviceItemEnum.cTnI.toString());

			jo.put("name", DeviceItemEnum.cTnI.getName());
			jo.put("dataType", DeviceItemEnum.cTnI.toString());
			jo.put("value", jb.getString("cTnI"));
			jo.put("units", DeviceItemEnum.cTnI.getUnit());

			break;
			// NT-proBNP N-端脑利钠肽前体
		case "2":
			deviceData.setDataType(DeviceItemEnum.NT_proBNP.toString());

			jo.put("name", DeviceItemEnum.NT_proBNP.getName());
			jo.put("dataType", DeviceItemEnum.NT_proBNP.toString());
			jo.put("value", jb.getString("NT_proBNP"));
			jo.put("units", DeviceItemEnum.NT_proBNP.getUnit());

			break;
			// CRP  hs-CRP : 超敏C反应蛋白  CRP : C反应蛋白
		case "3": 
			deviceData.setDataType(DeviceItemEnum.CRP.toString());

			jo.put("name", DeviceItemEnum.hs_CRP.getName());
			jo.put("dataType", DeviceItemEnum.CRP.toString());
			jo.put("value", jb.getString("hs-CRP"));
			jo.put("CRP", jb.getString("CRP"));
			jo.put("units", DeviceItemEnum.hs_CRP.getUnit());

			break;
			// cTnI+NT-proBNP  cTnI : 心肌肌钙蛋白I  NT-proBNP : N-端脑利钠肽前体
		case "4":
			deviceData.setDataType(DeviceItemEnum.cTnI_NT_proBNP.toString());

			jo.put("name", DeviceItemEnum.cTnI.getName());
			jo.put("dataType", DeviceItemEnum.cTnI_NT_proBNP.toString());
			jo.put("value", jb.getString("cTnI"));
			jo.put("NT_proBNP", jb.getString("NT-proBNP"));
			jo.put("units", DeviceItemEnum.cTnI.getUnit());

			break;
			// cTnI+CKMB+Myo  CK-MB : 肌酸激酶同工酶  cTnI : 心肌肌钙蛋白I  Myo : 肌红蛋白
		case "5":
			deviceData.setDataType(DeviceItemEnum.cTnI_CKMB_Myo.toString());

			jo.put("name", DeviceItemEnum.CK_MB.getName());
			jo.put("dataType", DeviceItemEnum.cTnI_CKMB_Myo.toString());
			jo.put("value", jb.getString("CK-MB"));
			jo.put("cTnI", jb.getString("cTnI"));
			jo.put("Myo", jb.getString("Myo"));
			jo.put("units", DeviceItemEnum.CK_MB.getUnit());

			break;
			// D-Dimer D-二聚体
		case "6":
			deviceData.setDataType(DeviceItemEnum.D_Dimer.toString());

			jo.put("name", DeviceItemEnum.D_Dimer.getName());
			jo.put("dataType", DeviceItemEnum.D_Dimer.toString());
			jo.put("value", jb.getString("D-Dimer"));
			jo.put("units", DeviceItemEnum.D_Dimer.getUnit());

			break;
			// PCT 降钙素原
		case "7":
			deviceData.setDataType(DeviceItemEnum.PCT.toString());

			jo.put("name", DeviceItemEnum.PCT.getName());
			jo.put("dataType", DeviceItemEnum.PCT.toString());
			jo.put("value", jb.getString("PCT"));
			jo.put("units", DeviceItemEnum.PCT.getUnit());

			break;
			// mAlb 微量白蛋白
		case "8":
			deviceData.setDataType(DeviceItemEnum.mAlb.toString());

			jo.put("name", DeviceItemEnum.mAlb.getName());
			jo.put("dataType", DeviceItemEnum.mAlb.toString());
			jo.put("value", jb.getString("mAlb"));
			jo.put("units", DeviceItemEnum.mAlb.getUnit());

			break;
			// NGAL 中性粒细胞明胶酶相关脂质运载蛋白
		case "9":
			deviceData.setDataType(DeviceItemEnum.NGAL.toString());

			jo.put("name", DeviceItemEnum.NGAL.getName());
			jo.put("dataType", DeviceItemEnum.NGAL.toString());
			jo.put("value", jb.getString("NGAL"));
			jo.put("units", DeviceItemEnum.NGAL.getUnit());

			break;
			// CysC 胱抑素C
		case "10":
			deviceData.setDataType(DeviceItemEnum.CysC.toString());

			jo.put("name", DeviceItemEnum.CysC.getName());
			jo.put("dataType", DeviceItemEnum.CysC.toString());
			jo.put("value", jb.getString("CysC"));
			jo.put("units", DeviceItemEnum.CysC.getUnit());

			break;
			// β2-MG β2-微球蛋白
		case "11":
			deviceData.setDataType(DeviceItemEnum.β2_MG.toString());

			jo.put("name", DeviceItemEnum.β2_MG.getName());
			jo.put("dataType", DeviceItemEnum.β2_MG.toString());
			jo.put("value", jb.getString("β2-MG"));
			jo.put("units", DeviceItemEnum.β2_MG.getUnit());

			break;
			// hs-CRP 高敏C反应蛋白
		case "12":
			deviceData.setDataType(DeviceItemEnum.hs_CRP.toString());

			jo.put("name", DeviceItemEnum.hs_CRP.getName());
			jo.put("dataType", DeviceItemEnum.hs_CRP.toString());
			jo.put("value", jb.getString("hs-CRP"));
			jo.put("units", DeviceItemEnum.hs_CRP.getUnit());

			break;
			// HCG 人绒毛膜促性腺激素
		case "13":
			deviceData.setDataType(DeviceItemEnum.HCG.toString());

			jo.put("name", DeviceItemEnum.HCG.getName());
			jo.put("dataType", DeviceItemEnum.HCG.toString());
			jo.put("value", jb.getString("HCG"));
			jo.put("units", DeviceItemEnum.HCG.getUnit());

			break;
			// H-FABP 心型脂肪酸结合蛋白
		case "14":
			deviceData.setDataType(DeviceItemEnum.H_FABP.toString());

			jo.put("name", DeviceItemEnum.H_FABP.getName());
			jo.put("dataType", DeviceItemEnum.H_FABP.toString());
			jo.put("value", jb.getString("H-FABP "));
			jo.put("units", DeviceItemEnum.H_FABP.getUnit());

			break;
			// BNP 脑钠肽
		case "15":
			deviceData.setDataType(DeviceItemEnum.BNP.toString());

			jo.put("name", DeviceItemEnum.BNP.getName());
			jo.put("dataType", DeviceItemEnum.BNP.toString());
			jo.put("value", jb.getString("BNP"));
			jo.put("units", DeviceItemEnum.BNP.getUnit());

			break;
			// PCT/CRP  降钙素原 超敏C反应蛋白
		case "16":
			deviceData.setDataType(DeviceItemEnum.PCT_CRP.toString());

			jo.put("name", DeviceItemEnum.PCT_CRP.getName());
			jo.put("dataType", DeviceItemEnum.PCT_CRP.toString());
			jo.put("value", jb.getString("PCT"));
			jo.put("CRP", jb.getString("CRP"));
			jo.put("units", DeviceItemEnum.PCT_CRP.getUnit());

			break;
			// CK-MB/cTnI/H-FABP 肌酸激酶同工酶 心肌肌钙蛋白I 心型脂肪酸结合蛋白
		case "17":
			deviceData.setDataType(DeviceItemEnum.CK_MB_cTnI_H_FABP.toString());

			jo.put("name", DeviceItemEnum.CK_MB.getName());
			jo.put("dataType", DeviceItemEnum.CK_MB_cTnI_H_FABP.toString());
			jo.put("value", jb.getString("CK-MB"));
			jo.put("cTnI", jb.getString("cTnI"));
			jo.put("H_FABP", jb.getString("H-FABP"));
			jo.put("units", DeviceItemEnum.CK_MB.getUnit());

			break;
			// NT-proBNP/BNP N-端脑利钠肽前体 脑钠肽
		case "18":
			deviceData.setDataType(DeviceItemEnum.NT_proBNP_BNP.toString());

			jo.put("name", DeviceItemEnum.NT_proBNP.getName());
			jo.put("dataType", DeviceItemEnum.NT_proBNP.toString());
			jo.put("value", jb.getString("NT-proBNP"));
			jo.put("BNP", jb.getString("BNP"));
			jo.put("units", DeviceItemEnum.NT_proBNP.getUnit());

			break;
			// NGAL/mAlb 中性粒细胞明胶酶相关脂质运载蛋白 微量白蛋白
		case "19":
			deviceData.setDataType(DeviceItemEnum.NGAL_mAlb.toString());

			jo.put("name", DeviceItemEnum.NGAL.getName());
			jo.put("dataType", DeviceItemEnum.NGAL_mAlb.toString());
			jo.put("value", jb.getString("NGAL"));
			jo.put("mAlb", jb.getString("mAlb"));
			jo.put("units", DeviceItemEnum.NGAL.getUnit());

			break;
			// HbA1c 糖化血红蛋白
		case "20":
			deviceData.setDataType(DeviceItemEnum.HbA1c.toString());

			jo.put("name", DeviceItemEnum.HbA1c.getName());
			jo.put("dataType", DeviceItemEnum.HbA1c.toString());
			jo.put("value", jb.getString("HbA1c"));
			jo.put("units", DeviceItemEnum.HbA1c.getUnit());

			break;
			// h-cTnI 高敏心肌肌钙蛋白I
		case "21":
			deviceData.setDataType(DeviceItemEnum.h_cTnI.toString());

			jo.put("name", DeviceItemEnum.h_cTnI.getName());
			jo.put("dataType", DeviceItemEnum.h_cTnI.toString());
			jo.put("value", jb.getString("h-cTnI"));
			jo.put("units", DeviceItemEnum.h_cTnI.getUnit());

			break;	
		default:
			break;
		}

		deviceData.setDataValue(jo.toString());
		int num = deviceDataMapper.insertSelective(deviceData);
		if(num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
		}else {
			result.setResultCode(new ResultCode("FAILED", "添加失败"));
		}
		return result;
	}
}