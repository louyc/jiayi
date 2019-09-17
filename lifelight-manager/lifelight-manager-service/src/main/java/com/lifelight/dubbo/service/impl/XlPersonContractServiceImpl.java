package com.lifelight.dubbo.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.dto.SignServiceProjectEnum;
import com.lifelight.api.entity.Dictionary;
import com.lifelight.api.entity.ServicePackage;
import com.lifelight.api.entity.ServicePackageRel;
import com.lifelight.api.entity.ServicePackageRelExample;
import com.lifelight.api.entity.UserDoctorRel;
import com.lifelight.api.entity.XlPersonContract;
import com.lifelight.api.entity.XlPersonContractExample;
import com.lifelight.api.entity.XlPersonDocument;
import com.lifelight.api.entity.XlPersonDocumentExample;
import com.lifelight.api.entity.XlPersonSchedule;
import com.lifelight.api.entity.XlPersonScheduleExample;
import com.lifelight.api.vo.SignStatisticsVO;
import com.lifelight.api.vo.XlPersonContractVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.util.DateTimeUtil;
import com.lifelight.dubbo.dao.DictionaryMapper;
import com.lifelight.dubbo.dao.ServicePackageMapper;
import com.lifelight.dubbo.dao.ServicePackageRelMapper;
import com.lifelight.dubbo.dao.UserDoctorRelMapper;
import com.lifelight.dubbo.dao.XlPersonContractMapper;
import com.lifelight.dubbo.dao.XlPersonDocumentMapper;
import com.lifelight.dubbo.dao.XlPersonScheduleMapper;
import com.lifelight.dubbo.service.XlPersonContractService;

import net.sf.json.JSONObject;

@Service
public class XlPersonContractServiceImpl implements XlPersonContractService {

	private static final Logger logger = LoggerFactory.getLogger(XlPersonContractServiceImpl.class);
	@Autowired
	private XlPersonContractMapper contractMapper;
	@Autowired
	private XlPersonDocumentMapper documentMapper;
	@Autowired
	private XlPersonScheduleMapper scheduleMapper;
	@Autowired
	private DictionaryMapper dictionMapper;
	@Autowired
	private UserDoctorRelMapper userDoctorRelMapper;
	@Autowired
	private ServicePackageMapper servicePackageMapper;
	@Autowired
	private ServicePackageRelMapper servicePackageRelMapper;

	private static final String TYPES ="2,3,4,0";
	/**
	 * 创建个人签约信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> createPersonContract(XlPersonContract contract) {
		Result<?> result = new Result<>(StatusCodes.OK, true);
		UserDoctorRel userRel = new UserDoctorRel();
		try {
			if (StringUtil.isEmpty(contract.getSignDoctorId())) {
				return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "签约医生不能为空！"));
			}
			XlPersonContractExample exampleCon = new XlPersonContractExample();
			exampleCon.createCriteria().andDocumentIdEqualTo(contract.getDocumentId()).andInUseEqualTo("1");
			List<XlPersonContract> list = contractMapper.selectByExample(exampleCon);
			if(null!=list && list.size()>0) {
				return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "当前用户已经签过约"));
			}
			String item = "";//服务项
			contract.setCreateTime(new Date());
			contract.setUpdateTime(new Date());
			if(!StringUtil.isEmpty(contract.getVariable())) {
				String variable = contract.getVariable();
				Map map1 = JSON.parseObject(variable);
				if(map1.get("variable").toString().contains("variable")) {
					map1 = JSON.parseObject(map1.get("variable").toString());
				}
				List<JSON> ss =(List<JSON>)map1.get("variable");
				for(JSON j:ss) {
					Map map =(Map)j;
					if(map.get("propertyType").toString().contains("5")) {
						item = String.valueOf(map.get("value"));
					}
				}
			}
			ServicePackage sp = null;
			if(null!=item && !StringUtil.isEmpty(item)) {
				sp =servicePackageMapper.selectByPrimaryKey(Integer.parseInt(item));
				contract.setSignPayment(sp.getPrice());
				contract.setSelfPayment(sp.getSelfPayment());
				contract.setCompensationPayment(sp.getCompensatePayment());
				contract.setServiceContent(sp.getPackageDesc());
				contract.setServicePackage(item);
			}
	
			contract.setSignStatus("1");
			contract.setFinishStatus("2");
			contract.setAuditStatus("3");
			contract.setInUse("1");
			if(null==contract.getSignDate()) {
				contract.setSignDate(contract.getCreateTime());
			}
		
			int num = contractMapper.insertSelective(contract);

			//插入医生 用户 家医关系表 user_doctor_rel
			if (num != 0) {
				XlPersonDocument xpd = new XlPersonDocument();
				xpd.setId(contract.getDocumentId());
				xpd.setIsSign("1");
				//修改档案状态为签约
				documentMapper.updateByPrimaryKeySelective(xpd);
				//服务进度更新
				if(!StringUtil.isEmpty(item)) {
					createSchedule(item, contract,sp);
				}
				XlPersonDocumentVO xpdVO = documentMapper.selectByPrimaryKey(contract.getDocumentId());
				//签约用户关系表 增加关系
				userRel.setManagerId(xpdVO.getManagerId());
				userRel.setDoctorId(contract.getSignDoctorId());
				userRel.setContractedUserId(contract.getId());
				userRel.setInUse(1);
				userRel.setCreateTime(new Date());
				userRel.setBuildType(1);
				userRel.setExpirationDate(contract.getSignEndDate());
				userDoctorRelMapper.insertSelective(userRel);
				result.setResultCode(new ResultCode("SUCCESS", "签约成功！"));
			} else {
				result.setResultCode(new ResultCode("FAILED", "签约失败！"));
			}
		}catch(Exception e) {
			logger.info(e.getMessage());
			//插入失败  撤回
			if(contract.getId()!=null && contract.getId()>0) {
				contractMapper.deleteByPrimaryKey(contract.getId());
				XlPersonDocument xpd = new XlPersonDocument();
				xpd.setId(contract.getDocumentId());
				xpd.setIsSign("2");
				//修改档案状态为未签约
				documentMapper.updateByPrimaryKeySelective(xpd);
				if(userRel!=null && userRel.getId()!=null
						&& userRel.getId()>0) {
					userDoctorRelMapper.deleteByPrimaryKey(userRel.getId());
				}
				XlPersonScheduleExample example = new XlPersonScheduleExample();
				example.createCriteria().andContractIdEqualTo(String.valueOf(contract.getId()));
				scheduleMapper.deleteByExample(example);
			}
			result.setResultCode(new ResultCode("FAILED", "签约失败！"));
		}
		return result;
	}
	//服务进度更新
	public void createSchedule(String item,XlPersonContract contract,ServicePackage sp) {
		ServicePackageRelExample example = new ServicePackageRelExample();
		example.createCriteria().andPackageIdEqualTo(Integer.parseInt(item));
		List<ServicePackageRel> listRel = servicePackageRelMapper.selectByExample(example);
		if(null!=listRel && listRel.size()>0) {
			for(ServicePackageRel spr:listRel) {
				Dictionary dic = dictionMapper.selectByPrimaryKey(spr.getDictionaryId());
				if(null!=dic) {
					XlPersonSchedule xps = new XlPersonSchedule();
					xps.setContractId(String.valueOf(contract.getId()));
					xps.setCreateTime(new Date());
					xps.setProjectType(String.valueOf(dic.getItemId()));
					xps.setServicePackage(String.valueOf(sp.getId()));
					xps.setServicePackageDesc(sp.getPackageName());
					xps.setProjectTypeDesc(dic.getItemName());
					scheduleMapper.insert(xps);
				}
			}
		}
	}
	/**
	 * 删除个人签约信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deletePersonContract(String id){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = contractMapper.deleteByPrimaryKey(Integer.parseInt(id));

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改个人签约信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> updatePersonContract(XlPersonContract contract){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (contract == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		XlPersonContract xlOld = contractMapper.selectByPrimaryKey(contract.getId());
		int m = 0;
		String item = "";//服务项
		if(!StringUtil.isEmpty(contract.getVariable())) {
			String variable = contract.getVariable();
			Map map1 = JSON.parseObject(variable);
			if(map1.get("variable").toString().contains("variable")) {
				map1 = JSON.parseObject(map1.get("variable").toString());
			}
			List<JSON> ss =(List<JSON>)map1.get("variable");
			for(JSON j:ss) {
				Map map =(Map)j;
				if(map.get("propertyType").toString().contains("5")) {
					item = String.valueOf(map.get("value"));
				}
			}
		}
		if((!StringUtil.isEmpty(item) && !StringUtil.isEmpty(xlOld.getServicePackage())
				&& !xlOld.getServicePackage().equals(item))
				|| (!StringUtil.isEmpty(item) && StringUtil.isEmpty(xlOld.getServicePackage()))) {
			ServicePackage sp = null;
			sp =servicePackageMapper.selectByPrimaryKey(Integer.parseInt(item));
			contract.setSignPayment(sp.getPrice());
			contract.setSelfPayment(sp.getSelfPayment());
			contract.setCompensationPayment(sp.getCompensatePayment());
			contract.setServicePackage(item);
			contract.setServiceContent(sp.getPackageDesc());
			scheduleMapper.deleteByContractId(contract.getId());
			//服务进度更新
			createSchedule(item, contract,sp);
		}
		if(StringUtil.isEmpty(item)) {
			m = scheduleMapper.deleteByContractId(contract.getId());
			logger.info("修改服务包 为空 删除"+m);
		}
		contract.setUpdateTime(new Date());

		int num = contractMapper.updateByPrimaryKeySelective(contract);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询单人签约信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result selectPersonContract(XlPersonContractVO contract) {

		Result result = new Result<>(StatusCodes.OK, true);

		XlPersonContractVO contractList = contractMapper.selectContract(contract);

		if(null!=contractList) {
			result.setData(contractList);
		}else {
			XlPersonDocumentVO xld = null;
			XlPersonContractVO xpc = new XlPersonContractVO();
			if(!StringUtil.isEmpty(contract.getManagerId())) {
				XlPersonDocumentExample example = new XlPersonDocumentExample();
				example.createCriteria().andManagerIdEqualTo(contract.getManagerId());
				List<XlPersonDocument> list = documentMapper.selectByExample(example);
				if(null!=list && list.size()>0) {
					JSONObject jsonObject = JSONObject.fromObject(list.get(0));
					xld = (XlPersonDocumentVO) JSONObject.toBean(jsonObject, XlPersonDocumentVO.class);
				}
			}else {
				xld = documentMapper.selectByPrimaryKey(contract.getDocumentId());
			}
			xpc.setName(xld.getName());
			xpc.setCardNum(xld.getCardNum());
			xpc.setSex(xld.getSex());
			try {
				xpc.setAge(String.valueOf(getAge(xld.getBirthday())));
			}catch(Exception e) {
				e.printStackTrace();
				xpc.setAge("0");
			}
			xpc.setFamilyRelations(xld.getFamilyRelations());
			xpc.setDocumentNum(xld.getDocumentId());
			xpc.setDegreeEducation(xld.getDegreeEducation());
			xpc.setOccupation(xld.getOccupation());
			xpc.setProvinceCode(xld.getProvinceCode());
			xpc.setTownCode(xld.getTownCode());
			xpc.setCityCode(xld.getCityCode());
			xpc.setResidentialAddress(xld.getResidentialAddress());
			xpc.setLowInsuranceCar(xld.getLowInsuranceCar());
			xpc.setVariable("");
			result.setData(xpc);
		}
		return result;
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
	/**
	 * 查询个人签约信息 分页
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public PaginatedResult<XlPersonDocumentVO> selectPersonContractListPage(XlPersonDocumentVO documentVO) {

		List<XlPersonDocumentVO> contractList = contractMapper.selectPersonContractListPage(documentVO);

		// 总页数
		int totalCount = documentVO.getTotalResult();

		PaginatedResult<XlPersonDocumentVO> pa = new PaginatedResult<XlPersonDocumentVO>(contractList,
				documentVO.getCurrentPage(), documentVO.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}
	/**
	 * 查询个人签约信息 导出 
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<List<XlPersonDocumentVO>> exportAllContract(XlPersonDocumentVO documentVO) {

		Result result = new Result<>(StatusCodes.OK, true);

		Map<Integer,String> medicTypeMap = queryDictionaryMap(49);// 医保类型
		Map<Integer,String> signPersonTypeMap = queryDictionaryMap(50);// 签约人群类型
		//		Map<Integer,String> servicePackageDescMap = queryDictionaryMap(16);// 服务包类型

		List<XlPersonDocumentVO> contractList = contractMapper.selectAllPersonContract(documentVO);

		for(XlPersonDocumentVO xp:contractList) {
			if(!StringUtil.isEmpty(xp.getMedicalType())) {
				xp.setMedicalTypeDesc(medicTypeMap.get(Integer.parseInt(xp.getMedicalType())));
			}else {
				xp.setMedicalTypeDesc("");
			}
			if(!StringUtil.isEmpty(xp.getSignPersonType())) {
				for(String s: xp.getSignPersonType().split(",")) {
					xp.setSignPersonTypeDesc(xp.getSignPersonTypeDesc()+","+signPersonTypeMap.get(Integer.parseInt(s)));
				}
				String desc = xp.getSignPersonTypeDesc();
				xp.setSignPersonTypeDesc(desc.substring(desc.indexOf(",")+1,desc.length()));
			}
			//			if(!StringUtil.isEmpty(xp.getServicePackage())) {
			//				xp.setServicePackageDesc(servicePackageDescMap.get(Integer.parseInt(xp.getServicePackage())));
			//			}
		}
		result.setData(contractList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	public Map<Integer,String> queryDictionaryMap(Integer type) {
		Dictionary dic = new Dictionary();
		dic.setItemType(type); // 医保类型
		List<Dictionary> listDic = dictionMapper.selectByDictionary(dic);
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(Dictionary xd: listDic) {
			map.put(xd.getItemId(), xd.getItemName());
		}
		return map;
	}

	@Override
	public Result queryServiceContent(String id) {
		String serviceName = "" ;//服务名称
		String item = "";//服务项
		String itemDesc ="";//服务项说明
		String payment = "";//服务收费
		String content = "";//服务介绍
		//查询签约包详细
		for(SignServiceProjectEnum ssp : SignServiceProjectEnum.values()){
			//检查进度增加明细
			if(ssp.getId().equals(id)) {
				serviceName = ssp.getName();//服务名称
				item = ssp.getItem();//服务项
				itemDesc = ssp.getItemDesc();//服务项说明
				payment = ssp.getPayment();//服务收费
				content = ssp.getServiceContent();//服务介绍
				break;
			}
		}
		Result result = new Result<>(StatusCodes.OK, true);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("serviceName", serviceName);
		map.put("item", item);
		map.put("itemDesc", itemDesc);
		map.put("payment", payment);
		map.put("content", content);
		result.setData(map);
		return result;
	}
	@Override
	public Result updateContractStatus(XlPersonContractVO contract) {
		// TODO Auto-generated method stub
		/*if(contract.getSignStatus().equals("2")) { //终止签约
			XlPersonDocument record = new XlPersonDocument();
			record.setId(contract.getDocumentId());
			record.setIsSign("2");
			documentMapper.updateByPrimaryKeySelective(record);
		}*/
		Result result = new Result<>(StatusCodes.OK, true);
		XlPersonContractVO xpVO = contractMapper.selectContract(contract);
		int num = 0;
		if(!StringUtil.isEmpty(contract.getSignStatus())) {
			if(!contract.getSignStatus().equals("3")) { //终止签约2   撤销终止1
				if(contract.getSignStatus().equals("1")) {
					if(DateTimeUtil.daysBetween(new Date(), xpVO.getSignEndDate())<=0) {
						result.setResultCode(new ResultCode("FAILED", "您当前签约已到期，无法撤销终止，可以续约或者删除再签约"));
						return result;
					}
				}
				contract.setUpdateTime(new Date());
				num = contractMapper.updateByDocumentId(contract);
				if (num != 0) {
					result.setResultCode(new ResultCode("SUCCESS", "终止/撤销 签约成功！"));
				} else {
					result.setResultCode(new ResultCode("FAILED", "终止/撤销 续约失败！"));
				}
			}else {//续约
				if(DateTimeUtil.daysBetween(new Date(), xpVO.getSignEndDate())>30) {
					result.setResultCode(new ResultCode("FAILED", "距上次签约结束超过30天，无法续约"));
					return result;
				}else {
					Calendar c = Calendar.getInstance();
					c.setTime(xpVO.getSignEndDate());
					c.add(Calendar.YEAR, 1);
					contract.setSignEndDate(c.getTime());
					contract.setSignStatus(null);
					contract.setUpdateTime(new Date());
					num = contractMapper.updateByDocumentId(contract);
					UserDoctorRel record = new UserDoctorRel();
					record.setBuildType(1);
					record.setDoctorId(xpVO.getSignDoctorId());
					record.setManagerId(xpVO.getManagerId());
					record.setExpirationDate(c.getTime());
					record.setUpdateTime(new Date());
					userDoctorRelMapper.updateExpirationDate(record);
				}
				if (num != 0) {
					result.setResultCode(new ResultCode("SUCCESS", "续约成功（直接续约1年）！"));
				} else {
					result.setResultCode(new ResultCode("FAILED", "续约失败！"));
				}
			}
		} 
		if(!StringUtil.isEmpty(contract.getInUse())) {
			num = contractMapper.updateByDocumentId(contract);
			if (num != 0) {
				XlPersonDocument xpd = new XlPersonDocument();
				xpd.setId(contract.getDocumentId());
				xpd.setIsSign("2");
				//修改档案状态为未签约
				documentMapper.updateByPrimaryKeySelective(xpd);
				result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
				UserDoctorRel record = new UserDoctorRel();
				record.setManagerId(xpVO.getManagerId());
				record.setDoctorId(xpVO.getSignDoctorId());
				record.setBuildType(1);
				List<UserDoctorRel>  rel = userDoctorRelMapper.selectByEntity(record);
				if(null!=rel && rel.size()>0) {
					rel.get(0).setInUse(0);
					rel.get(0).setUpdateTime(new Date());
					userDoctorRelMapper.updateByPrimaryKey(rel.get(0));
				}
			} else {
				result.setResultCode(new ResultCode("FAILED", "删除失败！"));
			}
		}
		return result;
	}

	/**
	 * 服务完成率统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param doctorId
	 * @param orgId
	 * @return
	 */
	@Override
	public Result serviceStatistics(String startDate,String endDate,String doctorId,String orgId) {

		Result result = new Result<>(StatusCodes.OK, true);
		SignStatisticsVO statisticVO = new SignStatisticsVO();
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		if(!StringUtil.isEmpty(startDate)) {
			statisticVO.setStartDate(startDate);
		}
		if(!StringUtil.isEmpty(endDate)) {
			statisticVO.setEndDate(endDate);
		}
		if(!StringUtil.isEmpty(doctorId)) {
			statisticVO.setDoctorId(doctorId);
		}
		if(!StringUtil.isEmpty(orgId)) {
			statisticVO.setOrgId(orgId);
		}
		List<SignStatisticsVO> returnList = new ArrayList<SignStatisticsVO>();
		List<SignStatisticsVO> contractList = contractMapper.selectAllOrgCount(statisticVO);
		if(null!=contractList && contractList.size()>0) {
			for(SignStatisticsVO ss:contractList) {
				SignStatisticsVO returnVO = new SignStatisticsVO();
				if(!StringUtil.isEmpty(doctorId)) {
					ss.setDoctorId(doctorId);
				}
				if(!StringUtil.isEmpty(startDate)) {
					ss.setStartDate(startDate);
				}
				if(!StringUtil.isEmpty(endDate)) {
					ss.setEndDate(endDate);
				}
				int paidCountNum = 0;
				int paidFinishNum = 0;
				double finishRate = 0.00;
				paidCountNum = contractMapper.selectPaidCount(ss);
				paidFinishNum = contractMapper.selectPaidFinishCount(ss);
				returnVO.setFinishSum(paidFinishNum);
				returnVO.setCountSum(paidCountNum);
				returnVO.setOrgId(ss.getOrgId());
				returnVO.setOrgName(ss.getOrgName());
				if(paidCountNum ==0 && paidCountNum ==0) {
					returnVO.setFinishRate(0+"%");
				}else {
					finishRate = new Double(paidFinishNum)/new Double(paidCountNum);
					returnVO.setFinishRate(nf.format(finishRate));
				}

				Map<Integer,String> map = queryDictionaryMap(50);//签约人群
				List<SignStatisticsVO> ItemList = new ArrayList<SignStatisticsVO>();
				for(Integer key: map.keySet()) {
					SignStatisticsVO returnItemVO = new SignStatisticsVO();
					returnItemVO.setItemId(key);
					returnItemVO.setItemName(map.get(key));
					SignStatisticsVO query = new SignStatisticsVO();
					query.setOrgId(ss.getOrgId());
					query.setDoctorId(ss.getDoctorId());
					query.setSignPersonType(String.valueOf(key));
					if(!StringUtil.isEmpty(startDate)) {
						query.setStartDate(startDate);
					}
					if(!StringUtil.isEmpty(endDate)) {
						query.setEndDate(endDate);
					}
					int paidPersonCount = 0;
					int paidPersonFinishCount = 0;
					double paidfinishRate = 0.00;
					paidPersonCount = contractMapper.selectPaidCount(query);
					paidPersonFinishCount = contractMapper.selectPaidFinishCount(query);
					returnItemVO.setCountSum(paidPersonCount);
					returnItemVO.setFinishSum(paidPersonFinishCount);
					if(paidPersonCount ==0 && paidPersonFinishCount ==0) {
						returnItemVO.setFinishRate(0+"%");
					}else {
						paidfinishRate = new Double(paidPersonFinishCount)/new Double(paidPersonCount);
						returnItemVO.setFinishRate(nf.format(paidfinishRate));
					}
					ItemList.add(returnItemVO);
				}
				returnVO.setList(ItemList);
				returnList.add(returnVO);
			}
		}
		result.setData(returnList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}
	/**
	 * 项目执行进度汇总
	 * 
	 * @param startDate
	 * @param endDate
	 * @param doctorId
	 * @param orgId
	 * @return
	 */
	@Override
	public Result executeStatistics(String startDate,String endDate,String doctorId,String orgId) {
		Result result = new Result<>(StatusCodes.OK, true);
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);//这个1的意识是保存结果到小数点后几位，但是特别声明：这个结果已经先＊100了。
		SignStatisticsVO statisticVO = new SignStatisticsVO();
		if(!StringUtil.isEmpty(startDate)) {
			statisticVO.setStartDate(startDate);
		}
		if(!StringUtil.isEmpty(endDate)) {
			statisticVO.setEndDate(endDate);
		}
		if(!StringUtil.isEmpty(doctorId)) {
			statisticVO.setDoctorId(doctorId);
		}
		if(!StringUtil.isEmpty(orgId)) {
			statisticVO.setOrgId(orgId);
		}
		List<SignStatisticsVO> returnList = new ArrayList<SignStatisticsVO>();
		List<SignStatisticsVO> contractList = contractMapper.selectAllOrgCount(statisticVO);
		if(null!=contractList && contractList.size()>0) {
			for(SignStatisticsVO ss:contractList) {
				SignStatisticsVO returnVO = new SignStatisticsVO();
				if(!StringUtil.isEmpty(doctorId)) {
					ss.setDoctorId(doctorId);
				}
				if(!StringUtil.isEmpty(startDate)) {
					ss.setStartDate(startDate);
				}
				if(!StringUtil.isEmpty(endDate)) {
					ss.setEndDate(endDate);
				}
				int paidCountNum = 0;
				int paidItemFinishNum = 0;
				int paidItemCountNum = 0;
				double finishRate = 0.00;
				paidCountNum = contractMapper.selectPaidCount(ss);
				paidItemCountNum = contractMapper.selectPaidItemCount(ss);
				paidItemFinishNum = contractMapper.selectPaidItemFinishCount(ss);
				returnVO.setFinishItemSum(paidItemFinishNum);
				returnVO.setCountSum(paidCountNum);
				returnVO.setCountItemSum(paidItemCountNum);
				returnVO.setOrgId(ss.getOrgId());
				returnVO.setOrgName(ss.getOrgName());
				
				if(paidItemFinishNum ==0 && paidItemCountNum ==0) {
					returnVO.setFinishRate(0+"%");
				}else {
					finishRate = new Double(paidItemFinishNum)/new Double(paidItemCountNum);
					returnVO.setFinishRate(nf.format(finishRate));
				}

				Map<Integer,String> map = queryDictionaryMap(50);//签约人群
				List<SignStatisticsVO> ItemList = new ArrayList<SignStatisticsVO>();
				logger.error("********5555********"+System.currentTimeMillis());
				map.put(0, "其他人群");
				for(Integer key: map.keySet()) {
					if(!TYPES.contains(String.valueOf(key))) {
						continue;
					}
					SignStatisticsVO returnItemVO = new SignStatisticsVO();
					returnItemVO.setItemId(key);
					returnItemVO.setItemName(map.get(key));
					SignStatisticsVO query = new SignStatisticsVO();
					query.setOrgId(ss.getOrgId());
					query.setDoctorId(ss.getDoctorId());
					query.setSignPersonType(String.valueOf(key));
					if(!StringUtil.isEmpty(startDate)) {
						query.setStartDate(startDate);
					}
					if(!StringUtil.isEmpty(endDate)) {
						query.setEndDate(endDate);
					}
					ss.setSignPersonType(String.valueOf(key));
					int paidPersonCount = 0;
					int paidPersonItemFinishNum = 0;
					int paidPersonItemCountNum = 0;
					double paidfinishRate = 0.00;
					paidPersonCount = contractMapper.selectPaidCount(query);
					paidPersonItemCountNum = contractMapper.selectPaidItemCount(query);
					paidPersonItemFinishNum = contractMapper.selectPaidItemFinishCount(query);					returnItemVO.setCountSum(paidPersonCount);
					returnItemVO.setFinishItemSum(paidPersonItemFinishNum);
					returnItemVO.setCountItemSum(paidPersonItemCountNum);
					if(paidPersonItemCountNum ==0 && paidPersonItemFinishNum ==0) {
						returnItemVO.setFinishRate(0+"%");
					}else {
						paidfinishRate = new Double(paidPersonItemFinishNum)/new Double(paidPersonItemCountNum);
						returnItemVO.setFinishRate(nf.format(paidfinishRate));
					}
					ItemList.add(returnItemVO);
				}
				logger.error("******66666**********"+System.currentTimeMillis());
				returnVO.setList(ItemList);
				returnList.add(returnVO);
			}
		}
		result.setData(returnList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}
	
}