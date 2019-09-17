package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.foxinmy.weixin4j.util.StringUtil;
import com.ibm.icu.text.SimpleDateFormat;
import com.jcabi.log.Logger;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.XlPersonContract;
import com.lifelight.api.entity.XlPersonSchedule;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.DeviceDataVO;
import com.lifelight.api.vo.XlPersonContractVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.api.vo.XlPersonScheduleVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.BackstageUserInfoMapper;
import com.lifelight.dubbo.dao.DeviceDataMapper;
import com.lifelight.dubbo.dao.XlPersonContractMapper;
import com.lifelight.dubbo.dao.XlPersonDocumentMapper;
import com.lifelight.dubbo.dao.XlPersonScheduleMapper;
import com.lifelight.dubbo.service.XlPersonScheduleService;

import net.sf.json.JSONObject;

@Service
public class XlPersonScheduleServiceImpl implements XlPersonScheduleService {

	@Autowired
	private XlPersonScheduleMapper scheduleMapper;
	@Autowired
	private XlPersonContractMapper contractMapper;
	@Autowired
	private XlPersonDocumentMapper doucmentMapper;
	@Autowired
	private BackstageUserInfoMapper backUserMapper;
	@Autowired
	private BackstageUserInfoMapper backstageUserInfoMapper;
	/**
	 * 创建个人 检查进度 信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> createPersonSchedule(XlPersonSchedule schedule) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (null == schedule) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		schedule.setId(Integer.parseInt(UUID.randomUUID().toString()));
		schedule.setCreateTime(new Date());
		schedule.setUpdateTime(new Date());
		int num = scheduleMapper.insertSelective(schedule);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除个人检查进度 信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deletePersonSchedule(String id){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = scheduleMapper.deleteByPrimaryKey(Integer.parseInt(id));

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改个人检查进度 信息
	 * 
	 * @param message
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result<?> updatePersonSchedule(XlPersonScheduleVO schedule){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (schedule == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		XlPersonSchedule xlp =null;
		String name="";
		String sex="";
		String contractId ="";
		List<XlPersonScheduleVO> list = new ArrayList<XlPersonScheduleVO>();
		if(schedule.getDocumentId()!=null && schedule.getDocumentId()>0){
			XlPersonScheduleVO record = new XlPersonScheduleVO();
			record.setDocumentId(schedule.getDocumentId());
			list = scheduleMapper.selectSchedule(record);
			if(null==list || list.size()<=0) {
				return result;
			}
			contractId = list.get(0).getContractId();
			XlPersonDocumentVO document = doucmentMapper.selectByPrimaryKey(schedule.getDocumentId());
			name = document.getName();
			sex = document.getSex();
			xlp = new XlPersonSchedule();
			xlp.setContractId(contractId);
		}else {
			if(!StringUtil.isEmpty(schedule.getProjectValue())) {
				//填写单条检查项数据
				Map mapTypes = JSON.parseObject(schedule.getProjectValue());
				try {
					schedule.setCheckTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(mapTypes.get("time"))));
				}catch(Exception e){
					Logger.info(e.getMessage(), null);
					schedule.setCheckTime(new Date());
				}
				BackstageUserInfo bu = backstageUserInfoMapper.selectBySchedule(schedule.getId());
				if(null!=bu) {
					schedule.setCheckOrg(bu.getName());
				}
			}
			xlp = scheduleMapper.selectByPrimaryKey(schedule.getId());
			XlPersonContract contract = contractMapper.selectByPrimaryKey(Integer.parseInt(xlp.getContractId()));
			XlPersonDocumentVO document = doucmentMapper.selectByPrimaryKey(contract.getDocumentId());
			name = document.getName();
			sex = document.getSex();
			contractId = xlp.getContractId();
			JSONObject jsonObject = JSONObject.fromObject(xlp);
			XlPersonScheduleVO xlpVO = (XlPersonScheduleVO) JSONObject.toBean(jsonObject, XlPersonScheduleVO.class);
			list.add(xlpVO);
		}
		schedule.setUpdateTime(new Date());

		int num = 0;
		if(!StringUtil.isEmpty(schedule.getExaminationInfo())) {
			String examinationInfo =schedule.getExaminationInfo();
			for(XlPersonScheduleVO xps:list) {
				Map<String,String> personMap = new HashMap<String,String>();
				Map<String,String> resultMap = new HashMap<String,String>();
				personMap.put("name", name);
				personMap.put("sex", sex);
				personMap.put("project_type_content", xps.getProjectTypeDesc());
				personMap.put("project_type_desc", xps.getProjectTypeDesc());
				personMap.put("time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				personMap.put("certificationsUrl", "");
				personMap.put("remark", "");
				Map map = JSON.parseObject(examinationInfo);
				//创建表格对象;
				String projectValue = "";
				switch(xps.getProjectType()) {
				case"1":
					break;
				case"2":
					break;
				case"3"://尿常规
					//			{"name":"很多话","sex":null,"project_type_content":"","time":"","project_type_desc":"尿常规","remark":"",
					//				"result":"{\"RU_LEU\":\"2\",\"RU_Pro\":\"2\",\"RU_Glu\":\"2\"}","certificationsUrl":""}
					String ncg_ndb ="";
					if(!StringUtil.isEmpty(String.valueOf(map.get("ncg_ndb")))) {
						switch(String.valueOf(map.get("ncg_ndb"))) {
						case "1": ncg_ndb = "-";
						break;
						case "2": ncg_ndb = "微量";
						break;
						case "3": ncg_ndb = "+";
						break;
						case "4": ncg_ndb = "++";
						break;
						case "5": ncg_ndb = "+++";
						break;
						case "6": ncg_ndb = "++++";
						break;
						default:
							ncg_ndb = "";
							break;
						}
					}
					String ncg_nt ="";
					if(!StringUtil.isEmpty(String.valueOf(map.get("ncg_nt")))) {
						switch(String.valueOf(map.get("ncg_nt"))) {
						case "1": ncg_nt = "-";
						break;
						case "2": ncg_nt = "微量";
						break;
						case "3": ncg_nt = "+";
						break;
						case "4": ncg_nt = "++";
						break;
						case "5": ncg_nt = "+++";
						break;
						case "6": ncg_nt = "++++";
						break;
						default:
							ncg_nt = "";
							break;
						}
					}
					if(!StringUtil.isEmpty(ncg_ndb) || 
							!StringUtil.isEmpty(ncg_nt)) {
						resultMap.put("RU_LEU", ""); //尿白细胞
						resultMap.put("RU_Pro", ncg_ndb); //尿蛋白
						resultMap.put("RU_Glu", ncg_nt); //尿糖
						JSONObject resultObject = JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						JSONObject jsonObject = JSONObject.fromObject(personMap);
						projectValue = jsonObject.toString();
						xps.setProjectValue(projectValue);
						xps.setContractId(xps.getContractId());
						num = scheduleMapper.updateByPrimaryKeySelective(xps);
					}
					break;
				case"4":
					break;
				case"5":
					break;
				case"6"://血脂
					if(null!=map.get("xz_CHO") || null!=map.get("xz_HDL-C") || null!= map.get("xz_TG") || null!=map.get("xz_LDL-C")) {
						resultMap.put("BF_CHOL", String.valueOf(map.get("xz_CHO"))); //总胆固醇
						resultMap.put("BF_HDL_C", String.valueOf(map.get("xz_HDL-C")));//高密度脂蛋白胆固醇（HDL_C）
						resultMap.put("BF_TG", String.valueOf(map.get("xz_TG")));   // 总甘油三酯（TG）
						resultMap.put("BF_LDL_C", String.valueOf(map.get("xz_LDL-C"))); //低密度脂蛋白胆固醇（LDL_C）
						JSONObject resultObject = JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						JSONObject jsonObject = JSONObject.fromObject(personMap);
						projectValue = jsonObject.toString();
						xps.setProjectValue(projectValue);
						xps.setContractId(xps.getContractId());
						num = scheduleMapper.updateByPrimaryKeySelective(xps);
					}
					break;
				case"7"://肝功能
					if(null!=map.get("ggn_DBIL") || null!=map.get("ggn_ALT") || null!= map.get("ggn_AST")) {
						resultMap.put("TBLL", String.valueOf(map.get("ggn_DBIL")));//总胆红素（TBLL）
						resultMap.put("ALT", String.valueOf(map.get("ggn_ALT")));//谷丙转氨酶（ALT）
						resultMap.put("AST", String.valueOf(map.get("ggn_AST")));//谷草转氨酶（AST）
						JSONObject resultObject = JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						JSONObject jsonObject = JSONObject.fromObject(personMap);
						projectValue = jsonObject.toString();
						xps.setProjectValue(projectValue);
						xps.setContractId(xps.getContractId());
						num = scheduleMapper.updateByPrimaryKeySelective(xps);
					}
					break;
				case"8"://肾功能
					if(null!=map.get("sgn_xnsa") || null!=map.get("sgn_xqjg")) {
						resultMap.put("BUN", String.valueOf(map.get("sgn_xnsa")));//尿素氮（BUN）
						resultMap.put("Gr", String.valueOf(map.get("sgn_xqjg")));//肌酐（Gr）
						JSONObject resultObject = JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						JSONObject jsonObject = JSONObject.fromObject(personMap);
						projectValue = jsonObject.toString();
						xps.setProjectValue(projectValue);
						xps.setContractId(xps.getContractId());
						num = scheduleMapper.updateByPrimaryKeySelective(xps);
					}
					break;
				case"9":
					break;
				case"10"://一体机 血氧  不做
					break;
				case"11"://血清尿酸化验  不做
					break;
				case"12":
					break;
				case"13":
					break;
				case"14":
					break;
				case"15":
					break;
				case"16":
					break;
				case"17":
					break;
				case"18":
					break;
				case"19":
					break;
				case"20"://血糖检查
					//			{"name":"很多话","sex":null,"project_type_content":"","time":"","project_type_desc":"血糖检查","remark":"",
					//				"result":"{\"BGC_xuetang\":\"234\"}","certificationsUrl":""}
					if(null!=map.get("kfxt")) {
						resultMap.put("BGC_xuetang", String.valueOf(map.get("kfxt")));//血糖
						JSONObject resultObject = JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						JSONObject jsonObject = JSONObject.fromObject(personMap);
						projectValue = jsonObject.toString();
						xps.setProjectValue(projectValue);
						xps.setContractId(xps.getContractId());
						num = scheduleMapper.updateByPrimaryKeySelective(xps);
					}
					break;
				case"21"://糖化血红蛋白
					//{"name":"很多话","sex":null,"project_type_content":"234","time":"2018-08-22","project_type_desc":"糖化血红蛋白检查","remark":"",
					//				"result":"{\"HBA1C_HBAIC\":\"234\"}","certificationsUrl":""}
					if(null!=map.get("thxhdb")) {
						resultMap.put("HBA1C_HBAIC", String.valueOf(map.get("thxhdb")));//糖化血红蛋白（HBAIC）
						JSONObject resultObject = JSONObject.fromObject(resultMap);
						personMap.put("result", resultObject.toString());
						JSONObject jsonObject = JSONObject.fromObject(personMap);
						projectValue = jsonObject.toString();
						xps.setProjectValue(projectValue);
						xps.setContractId(xps.getContractId());
						num = scheduleMapper.updateByPrimaryKeySelective(xps);
					}
					break;
				case"22":
					break;
				case"23":
					break;
				case"24":
					break;
				case"25":
					break;
				case"26":
					break;
				case"27":
					break;
				case"28":
					break;
				case"29":
					break;
				}
			}
			XlPersonSchedule record = new XlPersonSchedule();
			record.setUpdateTime(new Date());
			record.setContractId(contractId);
			record.setExaminationInfo(examinationInfo);
			num = scheduleMapper.updatByEntity(record);
		}else {
			num = scheduleMapper.updateByPrimaryKeySelective(schedule);
		}
		if (num != 0) {
			List<XlPersonScheduleVO> listVO = scheduleMapper.selectNoFinishSchedule(xlp);
			if(!(null!=listVO && listVO.size()>0)) {
				XlPersonContract record = new XlPersonContract();
				record.setId(Integer.parseInt(xlp.getContractId()));
				record.setFinishStatus("1");
				record.setUpdateTime(new Date());
				contractMapper.updateByPrimaryKeySelective(record);
			}
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询单人检查进度 信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result selectPersonSchedule(String documentId,Integer platformId) {

		Result result = new Result<>(StatusCodes.OK, true);
		XlPersonScheduleVO xpVo = new XlPersonScheduleVO();
		xpVo.setDocumentId(Integer.valueOf(documentId));
		List<XlPersonScheduleVO> scheduleList = scheduleMapper.selectSchedule(xpVo);

		if(null!=scheduleList && scheduleList.size()>0) {
			for(XlPersonScheduleVO xv: scheduleList) {
				if(!StringUtil.isEmpty(xv.getProjectValue())) {
					Map mapTypes = JSON.parseObject(xv.getProjectValue());
					//					xv.setName(String.valueOf(mapTypes.get("name")));
					xv.setRemark(String.valueOf(mapTypes.get("remark")));
					if(!StringUtil.isEmpty(xv.getInputPerson())) {
						BackstageUserInfo user = backUserMapper.selectByPrimaryKey(xv.getInputPerson());
						if(null!=user) {
							xv.setInputPerson(user.getName());
						}else {
							xv.setInputPerson(xv.getInputPerson());
						}
					}
				}

			}
			result.setData(scheduleList);
		}
		return result;
	}
	/**
	 * 查询单人检查进度 单条检查项
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result queryOneScheduleItem(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		XlPersonSchedule schedule = scheduleMapper.selectByPrimaryKey(Integer.parseInt(id));
		result.setData(schedule);
		return result;
	}

	/**
	 * 修改个人检查审核状态
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result updateAuditStatus(XlPersonContractVO xlp) {

		Result result = new Result<>(StatusCodes.OK, true);
		xlp.setUpdateTime(new Date());
		int num = contractMapper.updateByDocumentId(xlp);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}
	/**
	 * 查询个人进度
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result queryOneSchedule(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		XlPersonScheduleVO xpVo = new XlPersonScheduleVO();
		xpVo.setDocumentId(Integer.valueOf(id));
		List<XlPersonScheduleVO> list  = scheduleMapper.selectSchedule(xpVo);
		Map<String,Object> map = new HashMap<String,Object>();
		String content = "";
		int xunjian=0;
		int xunjianFinish=0;
		String xunjianDesc = "";
		if (null!=list && list.size() != 0) {
			String title = "<font color=\"black\">"+list.get(0).getServicePackageDesc()+"</font><br>";
			for(XlPersonScheduleVO xs:list) {
				String item = "";
				if(!xs.getProjectType().equals("9")) {
					if(StringUtil.isEmpty(xs.getProjectValue()) && null==xs.getCheckTime()) {
						item = xs.getProjectTypeDesc()+": <font color=\"red\">有空项 </font><br>";
					}else {
						item = xs.getProjectTypeDesc()+": <font color=\"black\">无空项 </font><br>";
					}
				}else {
					xunjianDesc = xs.getProjectTypeDesc();
					xunjian++;
					if(StringUtil.isEmpty(xs.getProjectValue()) && null==xs.getCheckTime()) {
						xunjianFinish++;
					}
				}
				content = content+item;
			}
			if(xunjian!=0) {
				if(xunjian == xunjianFinish) {
					content = content + xunjianDesc+": <font color=\"black\">无空项 </font><br>";
				}else {
					content = content+xunjianDesc + ": <font color=\"red\">有空项 </font><br>";
				}
			}

			map.put("servicePace", title);

			map.put("project", content);
			map.put("scheduleList", list);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}
		result.setData(map);
		return result;
	}
	/**
	 * 查询检查进度 分页
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public PaginatedResult<XlPersonDocumentVO> selectPersonScheduleListPage(XlPersonDocumentVO documentVO) {

		List<XlPersonDocumentVO> scheduleList = scheduleMapper.selectPersonScheduleListPage(documentVO);
		//查询已完成进度人数
		int scheduleFinishCount = scheduleMapper.selectPersonScheduleFinish(documentVO);
		//查询当前医生或者机构下 签约人数
		int scheduleCount = scheduleMapper.selectPersonScheduleSum(documentVO);

		Map<String,Integer> countMap = new HashMap<String,Integer>();
		countMap.put("sum", scheduleCount);
		countMap.put("finishSum", scheduleFinishCount);
		countMap.put("noFinishSum", scheduleCount - scheduleFinishCount);
		// 总页数
		int totalCount = documentVO.getTotalResult();
		XlPersonScheduleVO xpVo = new XlPersonScheduleVO();
		for(XlPersonDocumentVO vo: scheduleList) {
			String systemHints ="";
			xpVo.setDocumentId(vo.getId());
			if(!StringUtil.isEmpty(documentVO.getProjectType())) {
				xpVo.setProjectType(documentVO.getProjectType());
			}
			if(!StringUtil.isEmpty(documentVO.getFinishStatus())) {
				xpVo.setFinishStatus(documentVO.getFinishStatus());
			}
			if(StringUtil.isEmpty(vo.getServicePackageDesc())) {
				vo.setServicePackageDesc("");
			}
			List<XlPersonScheduleVO> list = scheduleMapper.selectSchedule(xpVo);
			int xunjian=0;
			int xunjianFinish=0;
			String xunjianDesc ="";
			for(XlPersonScheduleVO xs:list) {
				String item="";
				if(!xs.getProjectType().equals("9")) {
					if(StringUtil.isEmpty(xs.getProjectValue()) && null==xs.getCheckTime()) {
						item = "<font color=\"red\">【"+xs.getProjectTypeDesc()+"】完成进度：0/1 </font><br>";
					}else {
						item = "<font color=\"black\">【"+xs.getProjectTypeDesc()+"】完成进度：1/1 </font><br>";
					}
				}else {
					xunjianDesc = xs.getProjectTypeDesc();
					xunjian++;
					if(!StringUtil.isEmpty(xs.getProjectValue()) && null!=xs.getCheckTime()) {
						xunjianFinish++;
					}
				}
				systemHints = systemHints+item;
			}
			if(xunjian!=0) {
				if(xunjian == xunjianFinish) {
					systemHints = systemHints + "<font color=\"black\">【"+xunjianDesc+"】完成进度："+xunjianFinish+"/"+xunjian+" </font><br>";
				}else {
					systemHints = systemHints + "<font color=\"red\">【"+xunjianDesc+"】完成进度："+xunjianFinish+"/"+xunjian+" </font><br>";
				}
			}
			vo.setSystemHints(systemHints);
		}
		PaginatedResult<XlPersonDocumentVO> pa = new PaginatedResult<XlPersonDocumentVO>(scheduleList,
				documentVO.getCurrentPage(), documentVO.getShowCount(), totalCount);

		pa.setData(countMap);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

}