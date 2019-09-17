package com.lifelight.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.dto.DeviceItemEnum;
import com.lifelight.api.entity.DeviceData;
import com.lifelight.api.vo.DeviceDataVO;
import com.lifelight.api.vo.DeviceItemVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.util.DateTimeUtil;
import com.lifelight.dubbo.dao.DeviceDataMapper;
import com.lifelight.dubbo.service.DeviceDataService;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DeviceDataServiceImpl implements DeviceDataService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsInfoServiceImpl.class);

	@Autowired
	private DeviceDataMapper deviceDataMapper;

	/**
	 * 微信端 添加观察项
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result insertMeasureData(String managerId, String type, String data) {
		Result result = new Result<>(StatusCodes.OK, true);

		logger.info("根据类型 获取测量记录  managerId:{} type:{} ", managerId, type);
		logger.info("根据类型 获取测量记录  data:{}", data);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}
		if (null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}
		if (null == data || "".equals(data)) {
			result.setResultCode(new ResultCode("FALSE", "data为空"));
			return result;
		}

		JSONObject jb = JSONObject.parseObject(data.trim());
		DeviceData deviceData = new DeviceData();
		deviceData.setManagerId(managerId);
		deviceData.setCreateTime(new Date());
		deviceData.setUpdateTime(new Date());
		deviceData.setDataFrom("");
		JSONObject jbData = new JSONObject();

		switch (type) {
		case "bmi":

			Float bmi = Float.valueOf(getBmiValue(Float.valueOf(jb.getString("weValue")),
					Float.valueOf(jb.getString("heValue"))));

			jbData.put("weValue", jb.getString("weValue"));
			jbData.put("heValue", jb.getString("heValue"));

			jbData.put("value", bmi);
			jbData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jbData.put("units", DeviceItemEnum.BMI.getUnit());
			jbData.put("name", DeviceItemEnum.BMI.getName());
			jbData.put("dataType", DeviceItemEnum.BMI.toString());

			deviceData.setDataType(DeviceItemEnum.BMI.toString());

			break;

		case "bp":
			// 舒张压
			String dbpValue = jb.getString("dbpValue");
			// 收缩压
			String sbpValue = jb.getString("sbpValue");

			jbData.put("dia", jb.getString("dbpValue"));
			jbData.put("sys", jb.getString("sbpValue"));

			jbData.put("value", sbpValue + "/" + dbpValue);
			jbData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jbData.put("units", DeviceItemEnum.BP.getUnit());
			jbData.put("name", DeviceItemEnum.BP.getName());
			jbData.put("dataType", DeviceItemEnum.BP.toString());

			deviceData.setDataType(DeviceItemEnum.BP.toString());

			break;

		case "bo":
			String boValue = jb.getString("boValue");

			jbData.put("value", boValue);
			jbData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jbData.put("units", DeviceItemEnum.BO.getUnit());
			jbData.put("name", DeviceItemEnum.BO.getName());
			jbData.put("dataType", DeviceItemEnum.BO.toString());

			deviceData.setDataType(DeviceItemEnum.BO.toString());

			break;

		case "hr":
			String hrValue = jb.getString("hrValue");

			jbData.put("value", hrValue);
			jbData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jbData.put("units", DeviceItemEnum.HR.getUnit());
			jbData.put("name", DeviceItemEnum.HR.getName());
			jbData.put("dataType", DeviceItemEnum.HR.toString());

			deviceData.setDataType(DeviceItemEnum.HR.toString());

			break;

		case "sn":
			String snValue = jb.getString("snValue");

			jbData.put("value", snValue);
			jbData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jbData.put("units", DeviceItemEnum.SN.getUnit());
			jbData.put("name", DeviceItemEnum.SN.getName());
			jbData.put("dataType", DeviceItemEnum.SN.toString());

			deviceData.setDataType(DeviceItemEnum.SN.toString());

			break;

		case "bg":
			String bgValue = jb.getString("bgValue");

			DecimalFormat decimalFormat = new DecimalFormat("##0.00");
			jbData.put("value", decimalFormat.format(Float.valueOf(bgValue)));
			jbData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jbData.put("units", DeviceItemEnum.BG.getUnit());
			jbData.put("name", DeviceItemEnum.BG.getName());
			jbData.put("dataType", DeviceItemEnum.BG.toString());
			jbData.put("flag", "");

			deviceData.setDataType(DeviceItemEnum.BG.toString());

			break;

		case "sleep":
			deviceData.setDataType(DeviceItemEnum.SLEEP.toString());
			jbData.put(DeviceItemEnum.SLEEP.toString(), data);
			break;
		case "drink":
			deviceData.setDataType(DeviceItemEnum.DRINK.toString());
			jbData.put(DeviceItemEnum.DRINK.toString(), data);
			break;

		case "smoke":
			deviceData.setDataType(DeviceItemEnum.SMOKE.toString());
			jbData.put(DeviceItemEnum.SMOKE.toString(), data);
			break;

		case "medicine":
			deviceData.setDataType(DeviceItemEnum.MEDICINE.toString());
			jbData.put(DeviceItemEnum.MEDICINE.toString(), data);
			break;

		case "laboratory":
			deviceData.setDataType(DeviceItemEnum.LABORATORY_REPORT.toString());
			jbData.put(DeviceItemEnum.LABORATORY_REPORT.toString(), data);
			break;

		case "medical":
			deviceData.setDataType(DeviceItemEnum.MEDICAL_IMAGE.toString());
			jbData.put(DeviceItemEnum.MEDICAL_IMAGE.toString(), data);
			break;

		case "abnormal":
			deviceData.setDataType(DeviceItemEnum.ABNORMAL.toString());
			jbData.put(DeviceItemEnum.ABNORMAL.toString(), data);
			break;

		default:
			deviceData.setDataType(DeviceItemEnum.SPORT.toString());
			jbData.put(DeviceItemEnum.SPORT.toString(), data);
			break;
		}

		deviceData.setDataValue(jbData.toString());

		int count = deviceDataMapper.insertSelective(deviceData);

		if (count > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "添加失败！"));
		}
		return result;
	}

	/**
	 * 获取用户的测量数据 根据 用户ID 和 时间（当天） 获取该用户当当天最新的测量数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public Result getMeasureData(String managerId, String time) {
		logger.info("获取用户所有的测量数据接口  managerId:{} time:{}", managerId, time);
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}

		// 根据 用户ID 和 时间 获取改用户的检测数据
		List<DeviceData> deviceDataList = deviceDataMapper.getDataByUserTimeList(managerId, time);

		if (null == deviceDataList || deviceDataList.size() == 0) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));
			return result;
		}

		ArrayList<JSON> al = new ArrayList<>();
		JSONObject jb = new JSONObject();
		for (DeviceData item : deviceDataList) {
			al.add(jb.parseObject(item.getDataValue()));
			if(item.getDataType().equals("FP")){
				Map mapTypes = JSON.parseObject(item.getDataValue()); 
				mapTypes.put("dataType","WE");
				mapTypes.put("name", "体重");
				mapTypes.put("units", "kg");
				String we = jb.toJSONString(mapTypes);
				al.add(jb.parseObject(we));
			}
		}

		result.setData(al);
		result.setSuccessful(true);
		result.setResultCode(new ResultCode("TRUE", "查询成功"));

		return result;
	}

	/**
	 * 获取用户所有的测量数据   根据 用户ID 和 时间
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getHistoryMeasureData(String managerId, String time) {
		Result result = new Result<>(StatusCodes.OK, true);

		logger.info("获取用户所有的测量数据  managerId:{} time:{}", managerId, time);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}

		// 获取该用户的 所有 数据
		List<DeviceData> deviceDataList = null;
		if (time.length() > 8) {
			deviceDataList = deviceDataMapper.getHistoryMeasureDataByDay(managerId, time);
		} else {
			deviceDataList = deviceDataMapper.getHistoryMeasureData(managerId, time);
		}

		if (null == deviceDataList || deviceDataList.size() == 0) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));

			return result;
		}

		ArrayList<Map<String, Object>> map = new ArrayList<>();

		for (DeviceData item : deviceDataList) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(item.getCreateTime());

			JSONObject jb = JSONObject.parseObject(item.getDataValue());

			jb.put("id", item.getId());
			jb.put("day", Integer.parseInt(
					date.substring(date.lastIndexOf("-") + 1, date.lastIndexOf("-") + 3)));
			jb.put("date", DateTimeUtil.getFormatDateTime(item.getCreateTime()));
			jb.put("status", item.getConclusion());

			switch (item.getDataType()) {
			case "SLEEP":
				jb.put("type", "1");
				jb.put("dataType", "SLEEP");
				break;
			case "DRINK":
				jb.put("type", "1");
				jb.put("dataType", "DRINK");
				break;
			case "SMOKE":
				jb.put("type", "1");
				jb.put("dataType", "SMOKE");
				break;
			case "MEDICINE":
				jb.put("type", "1");
				jb.put("dataType", "MEDICINE");
				break;
			case "LABORATORY_REPORT":
				jb.put("type", "1");
				jb.put("dataType", "LABORATORY_REPORT");
				break;
			case "MEDICAL_IMAGE":
				jb.put("type", "1");
				jb.put("dataType", "MEDICAL_IMAGE");
				break;
			case "ABNORMAL":
				jb.put("type", "1");
				jb.put("dataType", "ABNORMAL");
				break;
			case "SPORT":
				jb.put("type", "1");
				jb.put("dataType", "SPORT");
				break;
			default:
				jb.put("type", "0");
				break;
			}
			map.add(jb);
		}

		result.setData(map);
		result.setSuccessful(true);
		result.setResultCode(new ResultCode("TRUE", "查询成功"));

		return result;
	}

	/**
	 * 测量数据页 的 子页 根据 用户ID 时间 数据类型 获取数据详细
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getMeasureDataByType(String managerId, String time, String type) {
		Result result = new Result<>(StatusCodes.OK, true);

		logger.info("根据类型 获取测量记录  managerId:{} type:{}", managerId, type);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}

		if (null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		List<DeviceData> deviceDataList = deviceDataMapper.getDataByType(managerId, type);

		if (null == deviceDataList || deviceDataList.size() == 0) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));

			return result;
		}
		map = assembleMeasureData(deviceDataList,type);
		
		result.setData(map);
		result.setSuccessful(true);
		result.setResultCode(new ResultCode("TRUE", "TRUE"));

		return result;
	}

	/**
	 * 根据 参数类型 用户ID 获取最新测量记录
	 * 
	 * @param managerId
	 * @param type
	 *            bmi：bmi bp：血压 bo：血氧 hr：心率 sn：步数 bg：血糖
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public Result getMeasureDataByMidType(String managerId, String time, String type) {
		logger.info("根据 参数类型 用户ID 获取最新测量记录  managerId:{} type:{} ", managerId, type);
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}
		if (null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}

		DeviceData deviceData = deviceDataMapper.getDataByUserTime(managerId, time, type);

		if (null == deviceData) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));
			return result;
		}

		ArrayList<JSON> arrayList = new ArrayList<>();
		JSONObject jb = new JSONObject();
		arrayList.add(jb.parseObject(deviceData.getDataValue()));

		if (null == arrayList || arrayList.isEmpty()) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));
		} else {
			result.setData(arrayList);
			result.setSuccessful(true);
			result.setResultCode(new ResultCode("TRUE", "查询成功"));
		}
		return result;
	}

	/**
	 * 根据身高体重获取 BMI 值
	 */
	private String getBmiValue(Float weight, Float height) {
		if (null == weight || null == height) {
			return null;
		}
		// 身高参数为 cm 需要转换成 m
		float lengthCm = height / 100;
		// bmi 计算方式 体重/身高平方
		float bmi = weight / (lengthCm * lengthCm);
		// BMI 保留两位小数
		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		return decimalFormat.format(bmi);
	}

	/**
	 * 查询当前专家下的绑定用户
	 * 
	 * @param mobile
	 *            手机号 name 用户名称 managerId 专家id pageSize 每页条数 currentsPage 当前页码
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getAllDeviceBindingUser(DeviceDataVO deviceData) {
		logger.info("查询当前专家下的绑定用户  managerId:{} type:{} ", deviceData.getManagerId());
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == deviceData.getDeviceLocationId()
				|| "".equals(deviceData.getDeviceLocationId())) {
			result.setResultCode(new ResultCode("FALSE", "专家id为空"));
			result.setSuccessful(false);
			return result;
		}
		List<DeviceDataVO> listDeviceUser = new ArrayList<DeviceDataVO>();
		List<DeviceDataVO> countListDeviceUser = new ArrayList<DeviceDataVO>();
		countListDeviceUser = deviceDataMapper.selectByCondition(deviceData);
		listDeviceUser = deviceDataMapper.selectByConditionListPage(deviceData);

		for (DeviceDataVO des : listDeviceUser) {
			des.setDeviceLocationId(des.getManagerId());
		}
		PaginatedResult<DeviceDataVO> pa = new PaginatedResult<DeviceDataVO>(listDeviceUser,
				deviceData.getCurrentPage(), deviceData.getShowCount(), countListDeviceUser.size());
		pa.setSuccessful(true);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	/**
	 * 查询用户设备检测信息
	 * 
	 * @param managerId
	 *            用户id
	 * @param dataType
	 *            项目名称
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param pageSize
	 *            每页条数
	 * @param currentsPage
	 *            当前页码
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result queryUserInfo(DeviceDataVO deviceData) {
		logger.info("查询当前专家下的绑定用户  managerId:{} type:{} ", deviceData.getDeviceLocationId());
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == deviceData.getManagerId() || "".equals(deviceData.getManagerId())) {
			result.setResultCode(new ResultCode("FALSE", "用户为空"));
			result.setSuccessful(false);
			return result;
		}
		String types = DeviceItemEnum.ALLDATA.getName().replace("[", "").replace("]", "");
		List<String> dataTypes = new ArrayList<String>();
		for (String s : types.split(",")) {
			dataTypes.add("'" + s + "'");
		}
		deviceData.setDataTypes(dataTypes);
		List<DeviceDataVO> listDeviceUser = new ArrayList<DeviceDataVO>();
		List<DeviceDataVO> countListDeviceUser = new ArrayList<DeviceDataVO>();
		countListDeviceUser = deviceDataMapper.selectMeasureInfo(deviceData);
		listDeviceUser = deviceDataMapper.selectJYMeasureInfoByConditionListPage(deviceData);

		PaginatedResult<DeviceDataVO> pa = new PaginatedResult<DeviceDataVO>(listDeviceUser,
				deviceData.getCurrentPage(), deviceData.getShowCount(), countListDeviceUser.size());
		pa.setSuccessful(true);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	/**
	 * 查询项目信息
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryDeviceItems() {
		List<DeviceItemVO> listDeviceItem = new ArrayList<DeviceItemVO>();
		String name = DeviceItemEnum.ALLDATA.getName().replace("[", "").replace("]", "");
		String desc = DeviceItemEnum.ALLDATA.getDesc().replace("[", "").replace("]", "");
		String[] itemId = name.split(",");
		String[] itemName = desc.split(",");
		for (int i = 0; i < itemId.length; i++) {
			DeviceItemVO di = new DeviceItemVO();
			di.setDeviceItemId(itemId[i]);
			di.setDeviceItemName(itemName[i]);
			listDeviceItem.add(di);
		}
		Result result = new Result<>(StatusCodes.OK, true);
		result.setData(listDeviceItem);
		result.setResultCode(new ResultCode("succ", "查询成功"));
		return result;
	}

	/**
	 * 查询检测项目list 返回给流程编辑器
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryCheckDeviceItems() {
		List<JSON> list = new ArrayList<JSON>();

		String name = DeviceItemEnum.CHECKlIST.getName().replace("[", "").replace("]", "");
		String desc = DeviceItemEnum.CHECKlIST.getDesc().replace("[", "").replace("]", "");
		String data = DeviceItemEnum.CHECKlIST.getUnit().replace("[", "").replace("]", "");
		String[] itemId = name.split(",");
		String[] itemName = desc.split(",");
		String[] itemData = data.split("\\.");
		for (int i = 0; i < itemId.length; i++) {
			JSONObject jo = new JSONObject();
			List<JSON> typeList = new ArrayList<JSON>();

			jo.put("itemId", itemId[i]);
			jo.put("itemName", itemName[i].replaceAll("\"", ""));

			String[] typeData = itemData[i].split(";");
			for (int j = 0; j < typeData.length; j++) {
				JSONObject myJson = JSONObject.parseObject(typeData[j]);
				typeList.add(myJson);
			}

			jo.put("data", typeList);
			list.add(jo);
		}
		Result result = new Result<>(StatusCodes.OK, true);
		result.setData(list);
		result.setResultCode(new ResultCode("succ", "查询成功"));
		return result;
	}

	/**
	 * 修改字段符号
	 * 
	 * @param orgStr
	 * @return
	 */
	@SuppressWarnings("unused")
	private String replaceSpecStr(String str) {
		if (null != str && !"".equals(str.trim())) {
			String regEx = "[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<<。》>、/？?]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			return m.replaceAll("");
		}
		return null;
	}



	/**
	 * 根据 参数类型 用户ID 获取最新测量记录   流程编辑器查询
	 * 
	 * @param managerId
	 * @param enName
	 *            bmi：bmi bp：血压 bo：血氧 hr：心率 sn：步数 bg：血糖
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getMeasureDataByWorkFollow(String managerId, String enName) {
		logger.info("根据 参数类型 用户ID 获取最新测量记录  managerId:{} enName:{} ", managerId, enName);
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}
		if (null == enName || "".equals(enName)) {
			result.setResultCode(new ResultCode("FALSE", "enName为空"));
			return result;
		}
		DeviceData deviceData =null;
		String value ="";
		DeviceData queryData = new DeviceData();
		queryData.setManagerId(managerId);
		switch(enName) {
		case "bg"://餐后血糖
			queryData.setDataType("bg");
			queryData.setIsAffirm("0");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("value"));
			}
			break;
		case "fastingbg"://空腹血糖
			queryData.setIsAffirm("1");
			queryData.setDataType("bg");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("value"));
			}
			break;
		case "LDL"://低密度脂蛋白
			queryData.setDataType("bf");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("LDL"));
			}
			break;
		case "HDL"://高密度脂蛋白
			queryData.setDataType("bf");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("HDL"));
			}
			break;
		case "TG"://甘油三酯
			queryData.setDataType("bf");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("TG"));
			}
			break;
		case "chol"://总胆固醇
			queryData.setDataType("bf");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("chol"));
			}
			break;
		case "bo"://血氧
			queryData.setDataType("bo");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("value"));
			}
			break;
		case "fp"://体脂率
			queryData.setDataType("fp");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("value"));
			}
			break;
		case "BMR"://基础代谢率
			queryData.setDataType("bmr");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("value"));
			}
			break;
		case "BMI"://bmi
			queryData.setDataType("bmi");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get("value"));
			}
			break;
		case "weValue"://体重
			queryData.setDataType("bmi");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get(enName));
			}
			break;
		case "heValue"://身高
			queryData.setDataType("bmi");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get(enName));
			}
			break;
		case "pr"://心率
			queryData.setDataType("bp");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get(enName));
			}
			break;
		case "dia"://舒张压
			queryData.setDataType("bp");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get(enName));
			}
			break;
		case "sys"://收缩压
			queryData.setDataType("bp");
			deviceData = deviceDataMapper.getMeasureDataByWorkFollow(queryData);
			if (null != deviceData) {
				Map map1 = JSON.parseObject(deviceData.getDataValue());
				value = String.valueOf(map1.get(enName));
			}
			break;
		default:
			break;
		}
		result.setData(value);
		result.setSuccessful(true);
		result.setResultCode(new ResultCode("TRUE", "查询成功"));
		return result;
	}
	
	/**
	 *  小程序  测量数据页 的 子页 根据 用户ID 时间 数据类型 获取数据详细
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getMeasureDataByMobile(String mobile, String time, String type) {
		Result result = new Result<>(StatusCodes.OK, true);

		logger.info("根据手机号、类型 getMeasureDataByMobile  managerId:{} type:{}", mobile, type);

		if (null == mobile || "".equals(mobile)) {
			result.setResultCode(new ResultCode("FALSE", "mobile为空"));
			return result;
		}

		if (null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}
		Date queryTime=null;
		if(!StringUtil.isEmpty(time)) {
			queryTime = DateTimeUtil.getBeforeMinutes(new Date(), Integer.parseInt(time));
		}
		Map<String, Object> map = new HashMap<String, Object>();

		List<DeviceData> deviceDataList = deviceDataMapper.getDataByMobileType(mobile, type,queryTime);

		if (null == deviceDataList || deviceDataList.size() == 0) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));

			return result;
		}
		map = assembleMeasureData(deviceDataList,type);
		
		result.setData(map);
		result.setSuccessful(true);
		result.setResultCode(new ResultCode("TRUE", "TRUE"));

		return result;
	}
	
	/**
	 * 小程序   ：获取用户的测量数据 根据 用户ID 和 时间（当天） 获取该用户当当天最新的测量数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public Result getNewMeasureData(String mobile, String time) {
		logger.info("小程序  获取用户所有的测量数据接口  mobile:{} time:{}", mobile, time);
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == mobile || "".equals(mobile)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}
		Date queryTime=null;
		if(!StringUtil.isEmpty(time)) {
			queryTime = DateTimeUtil.getBeforeMinutes(new Date(), Integer.parseInt(time));
		}
		// 根据 用户ID 和 时间 获取改用户的检测数据
		List<DeviceData> deviceDataList = deviceDataMapper.getDataByMoibleList(mobile, queryTime);

		if (null == deviceDataList || deviceDataList.size() == 0) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FALSE", "暂无数据"));
			return result;
		}

		ArrayList<JSON> al = new ArrayList<>();
		JSONObject jb = new JSONObject();
		for (DeviceData item : deviceDataList) {
			al.add(jb.parseObject(item.getDataValue()));
			if(item.getDataType().equals("FP")){
				Map mapTypes = JSON.parseObject(item.getDataValue()); 
				mapTypes.put("dataType","WE");
				mapTypes.put("name", "体重");
				mapTypes.put("units", "kg");
				String we = jb.toJSONString(mapTypes);
				al.add(jb.parseObject(we));
			}
		}

		result.setData(al);
		result.setSuccessful(true);
		result.setResultCode(new ResultCode("TRUE", "查询成功"));

		return result;
	}
	
	public Map assembleMeasureData(List<DeviceData> deviceDataList,String type) {
		ArrayList<String> xAxisArray = new ArrayList<>();
		ArrayList<Object> yAxisArray = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat decimalFormat = new DecimalFormat("##0.00");
		JSONObject jb = JSONObject.parseObject(deviceDataList.get(0).getDataValue());

		String regEx = "[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<<。》>、/？?]";
		Pattern p = Pattern.compile(regEx);

		switch (type) {
		case "BMI":

			map.put("name", DeviceItemEnum.BMI.getName());
			map.put("units", "kg");
			map.put("value", getBmiValue(Float.valueOf(jb.getString("weValue")),
					Float.valueOf(jb.getString("heValue"))));

			ArrayList<Float> weAxisArray = new ArrayList<>();
			ArrayList<Float> heAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				weAxisArray.add(Float.valueOf(jbData.getString("weValue")));
				heAxisArray.add(Float.valueOf(jbData.getString("heValue")));
			}

			map.put("yAxisArray", weAxisArray);
			map.put("heightArray", heAxisArray);
			break;
		case "BP":

			// 收缩压 / 舒张压
			map.put("name", DeviceItemEnum.BP.getName());
			map.put("units", DeviceItemEnum.BP.getUnit());
			map.put("value", jb.getString("sys") + "/" + jb.getString("dia"));

			Map<String, Object> ma = new HashMap<>();
			ArrayList<Float> yDbpAxisArray = new ArrayList<>();
			ArrayList<Float> ySbpAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				yDbpAxisArray.add(Float.valueOf(jbData.getString("dia")));
				ySbpAxisArray.add(Float.valueOf(jbData.getString("sys")));
			}

			ma.put("dbp", yDbpAxisArray);
			ma.put("sbp", ySbpAxisArray);

			map.put("yAxisArray", ma);
			break;
		case "BO":

			map.put("name", DeviceItemEnum.BO.getName());
			map.put("units", DeviceItemEnum.BO.getUnit());
			map.put("value", jb.getString("value"));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				yAxisArray.add(Float.valueOf(jbData.getString("value")));
			}

			break;

		case "HR":

			map.put("name", DeviceItemEnum.HR.getName());
			map.put("units", DeviceItemEnum.HR.getUnit());
			map.put("value", jb.getString("value"));

			ArrayList<String> hrUrlArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				yAxisArray.add(Float.valueOf(jbData.getString("value")));
				hrUrlArray.add(jbData.getString("cardEcgImgUrl"));
			}

			map.put("hrUrlArray", hrUrlArray);

			break;
		case "SN":

			String sn = jb.getString("value");

			map.put("name", DeviceItemEnum.SN.getName());
			map.put("units", DeviceItemEnum.SN.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(sn)));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));
				yAxisArray.add(decimalFormat.format(Float.valueOf(jbData.getString("value"))));
			}
			break;

		case "BG":
			Map<String, Object> beforeMap = new HashMap<String, Object>();
			Map<String, Object> afterMap = new HashMap<String, Object>();
			ArrayList<String> beforeX = new ArrayList<>();
			ArrayList<Object> beforeY= new ArrayList<>();
			ArrayList<String> afterX = new ArrayList<>();
			ArrayList<Object> afterY= new ArrayList<>();
			String bg = jb.getString("value");

			map.put("name", DeviceItemEnum.BG.getName());
			map.put("units", DeviceItemEnum.BG.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(bg)));
			map.put("flag", jb.getString("flag"));
			beforeMap.put("flag", "1");
			afterMap.put("flag", "0");

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());
				if(String.valueOf(jbData.getString("flag")).equals("0")) {  //餐后
					afterX.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));
					afterY.add(decimalFormat.format(Float.valueOf(jbData.getString("value"))));
				}else if(String.valueOf(jbData.getString("flag")).equals("1")) { //餐前
					beforeX.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));
					beforeY.add(decimalFormat.format(Float.valueOf(jbData.getString("value"))));
				}else if(String.valueOf(jbData.getString("flag")).equals("2")) { //随机

				}
			}
			afterMap.put("afterX", afterX);
			afterMap.put("afterY", afterY);
			beforeMap.put("beforeX", beforeX);
			beforeMap.put("beforeY", beforeY);

			map.put("afterMap", afterMap);
			map.put("beforeMap", beforeMap);
			break;
		case "RU":

			map.put("name", DeviceItemEnum.RU.getName());
			map.put("units", DeviceItemEnum.RU.getUnit());
			map.put("value", jb.getString("value"));

			break;
		case "CHOL":

			String chol = jb.getString("value");

			map.put("name", DeviceItemEnum.CHOL.getName());
			map.put("units", DeviceItemEnum.CHOL.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(chol)));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					yAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case "UA":

			String ua = jb.getString("value");

			map.put("name", DeviceItemEnum.UA.getName());
			map.put("units", DeviceItemEnum.UA.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(ua)));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					yAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case "BHB":

			map.put("hct", decimalFormat.format(Float.valueOf(jb.getString("hct"))));

			map.put("name", DeviceItemEnum.BHB.getName());
			map.put("units", DeviceItemEnum.BHB.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("hb"))));

			ArrayList<Object> hbAxisArray = new ArrayList<>();
			ArrayList<Object> hctAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					hbAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("hb")))));
					hctAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("hct")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.put("yAxisArray", hbAxisArray);
			map.put("hctAxisArray", hctAxisArray);

			break;
		case "BF":

			map.put("TG", decimalFormat.format(Float.valueOf(jb.getString("TG"))));
			map.put("LDL", decimalFormat.format(Float.valueOf(jb.getString("LDL"))));
			map.put("HDL", decimalFormat.format(Float.valueOf(jb.getString("HDL"))));
			map.put("CE", decimalFormat.format(Float.valueOf(jb.getString("CE"))));

			map.put("name", DeviceItemEnum.BF.getName());
			map.put("units", DeviceItemEnum.BF.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("chol"))));

			ArrayList<Object> cholAxisArray = new ArrayList<>();
			ArrayList<Object> TGAxisArray = new ArrayList<>();
			ArrayList<Object> LDLAxisArray = new ArrayList<>();
			ArrayList<Object> HDLAxisArray = new ArrayList<>();
			ArrayList<Object> CEAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					cholAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("chol")))));
					TGAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("TG")))));
					LDLAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("LDL")))));
					HDLAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("HDL")))));
					CEAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("CE")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.put("yAxisArray", cholAxisArray);
			map.put("TGAxisArray", TGAxisArray);
			map.put("LDLAxisArray", LDLAxisArray);
			map.put("HDLAxisArray", HDLAxisArray);
			map.put("CEAxisArray", CEAxisArray);
			break;
		case "TE":

			map.put("name", DeviceItemEnum.TE.getName());
			map.put("units", DeviceItemEnum.TE.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());
				xAxisArray.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));
				try {
					yAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			break;
		case "WP":
			map.put("name", DeviceItemEnum.WP.getName());
			map.put("units", DeviceItemEnum.WP.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					yAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case "MUCALE":

			map.put("name", DeviceItemEnum.MUCALE.getName());
			map.put("units", DeviceItemEnum.MUCALE.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			ArrayList<Object> muscleAxisArray = new ArrayList<>();
			ArrayList<Object> weitAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					muscleAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
					weitAxisArray.add(decimalFormat.parse(
							decimalFormat.format(Float.valueOf(jbData.getString("weight")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.put("yAxisArray", muscleAxisArray);
			map.put("weAxisArray", weitAxisArray);

			break;
		case "BONE":

			map.put("name", DeviceItemEnum.BONE.getName());
			map.put("units", DeviceItemEnum.BONE.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					yAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case "BMR":

			map.put("name", DeviceItemEnum.BMR.getName());
			map.put("units", DeviceItemEnum.BMR.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					yAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case "PIW":

			map.put("name", DeviceItemEnum.PIW.getName());
			map.put("units", DeviceItemEnum.PIW.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			ArrayList<Object> piwAxisArray = new ArrayList<>();
			ArrayList<Object> weiAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					piwAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
					weiAxisArray.add(decimalFormat.parse(
							decimalFormat.format(Float.valueOf(jbData.getString("weight")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.put("yAxisArray", piwAxisArray);
			map.put("weAxisArray", weiAxisArray);

			break;
		case "FP":

			map.put("name", DeviceItemEnum.FP.getName());
			map.put("units", DeviceItemEnum.FP.getUnit());
			map.put("value", decimalFormat.format(Float.valueOf(jb.getString("value"))));

			ArrayList<Object> fpAxisArray = new ArrayList<>();
			ArrayList<Object> fcAxisArray = new ArrayList<>();
			ArrayList<Object> vfpAxisArray = new ArrayList<>();
			ArrayList<Object> weightArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					fpAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("value")))));
					fcAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("fc")))));
					vfpAxisArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("vfp")))));
					weightArray.add(decimalFormat
							.parse(decimalFormat.format(Float.valueOf(jbData.getString("weight")))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.put("yAxisArray", fpAxisArray);
			map.put("fcAxisArray", fcAxisArray);
			map.put("vfpAxisArray", vfpAxisArray);
			map.put("weightArray", weightArray);

			break;
		case "CRP":

			map.put("name", DeviceItemEnum.CRP.getName());
			map.put("units", DeviceItemEnum.CRP.getUnit());

			// 判断 crp 是否含有 < > , 如果含有 特殊符号 则直接返回 value, 如果不含有 特殊符号 则需要转换符号
			String hsCrpValue = jb.getString("value");

			map.put("value", decimalFormat.format(Float.valueOf(replaceSpecStr(hsCrpValue))));

			ArrayList<Object> crpAxisArray = new ArrayList<>();
			ArrayList<Object> hsCrpAxisArray = new ArrayList<>();

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {
					String hsCrpValueList = jbData.getString("value");

					String crpValueList = jbData.getString("CRP");

					hsCrpAxisArray.add(decimalFormat.parse(
							decimalFormat.format(Float.valueOf(replaceSpecStr(hsCrpValueList)))));
					crpAxisArray.add(decimalFormat.parse(
							decimalFormat.format(Float.valueOf(replaceSpecStr(crpValueList)))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.put("yAxisArray", crpAxisArray);
			map.put("hsCrpAxisArray", hsCrpAxisArray);

			break;
		case "HbA1c":

			map.put("name", DeviceItemEnum.HbA1c.getName());
			map.put("units", DeviceItemEnum.HbA1c.getUnit());

			String hbA1cValue = jb.getString("value");

			map.put("value", decimalFormat.format(Float.valueOf(replaceSpecStr(hbA1cValue))));

			for (int i = deviceDataList.size() - 1; i >= 0; i--) {
				JSONObject jbData = JSONObject.parseObject(deviceDataList.get(i).getDataValue());

				xAxisArray
				.add(DateTimeUtil.getFormatDateTime(deviceDataList.get(i).getCreateTime()));

				try {

					String hbA1cValueList = jbData.getString("value");

					yAxisArray.add(decimalFormat.parse(
							decimalFormat.format(Float.valueOf(replaceSpecStr(hbA1cValueList)))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		default:
			break;
		}
		map.put("xAxisArray", xAxisArray);

		if (!"BP".equals(type) && !"BMI".equals(type) && !"BHB".equals(type) && !"BF".equals(type)
				&& !"MUCALE".equals(type) && !"PIW".equals(type) && !"FP".equals(type)
				&& !"CRP".equals(type)) {
			map.put("yAxisArray", yAxisArray);
		}
		return map;
	}
}