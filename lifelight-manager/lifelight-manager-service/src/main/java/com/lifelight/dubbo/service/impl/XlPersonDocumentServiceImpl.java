package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.JyDoctorInfo;
import com.lifelight.api.entity.ProcedureData;
import com.lifelight.api.entity.XlPersonDocument;
import com.lifelight.api.entity.XlPersonDocumentExample;
import com.lifelight.api.vo.ApiContractedUserVO;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.JyDoctorInfoVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.BackstageUserInfoMapper;
import com.lifelight.dubbo.dao.JyOrgDocRelMapper;
import com.lifelight.dubbo.dao.ProcedureDataMapper;
import com.lifelight.dubbo.dao.XlPersonDocumentMapper;
import com.lifelight.dubbo.service.XlPersonDocumentService;

@Service
public class XlPersonDocumentServiceImpl implements XlPersonDocumentService {

	private static final Logger logger = LoggerFactory
			.getLogger(XlPersonDocumentServiceImpl.class);
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode UserInfoAddFail = new ResultCode("USERINFO_ADD_FAIL", "添加签约用户失败");
	public static ResultCode UserInfoDelFail = new ResultCode("USERINFO_DEL_FAIL", "修改签约用户信息失败");
	@Autowired
	private XlPersonDocumentMapper documentMapper;
	@Autowired
	private BackstageUserInfoMapper userMapper;
	@Autowired
	private ProcedureDataMapper procedureMapper;
	@Autowired
	private JyOrgDocRelMapper jyOrgDocRelMapper;
	/**
	 * 保存流程数据
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result saveProcedureDate(String userId,String data) {
		Result result = new Result<>(StatusCodes.OK, true);
		ProcedureData pd = new ProcedureData();
		pd.setManagerId(userId);
		pd.setDataValue(data);
		pd.setCreateTime(new Date());
		int num = procedureMapper.insertSelective(pd);
		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}
	/**
	 * 查询流程数据
	 */
	@Override
	public PaginatedResult<ProcedureData> getProcedureDate(ProcedureData procedure) {
		List<ProcedureData> proceList = procedureMapper.selectByEntityListPage(procedure);
		// 总页数
		int totalCount = procedure.getTotalResult();

		PaginatedResult<ProcedureData> pa = new PaginatedResult<ProcedureData>(proceList,
				procedure.getCurrentPage(), procedure.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	/**
	 * 创建个人单人信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> createPersonDocument(XlPersonDocument document) {
		Result<?> result = new Result<>(StatusCodes.OK, true);
		try {
			XlPersonDocument docu = new XlPersonDocument();
			docu.setCardNum(document.getCardNum());
			List<XlPersonDocument>  xpList = documentMapper.selectDocument(docu);
			if(null!=xpList && xpList.size()>0) {
				return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_ERROR", "当前身份证已填写过个人信息！"));
			}
			XlPersonDocumentExample example = new XlPersonDocumentExample();
			example.createCriteria().andManagerIdEqualTo(document.getManagerId());
			List<XlPersonDocument> list = documentMapper.selectByExample(example);
			if(null!=list && list.size()>0) {
				return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_ERROR", "当前用户id已经填写过个人信息！"));
			}
			document.setCreateTime(new Date());
			document.setUpdateTime(new Date());
			document.setIsSign("2");
			if(null==document.getDocumentDate()) {
				document.setDocumentDate(document.getCreateTime());
			}
			if(StringUtil.isEmpty(document.getCardType())) {
				document.setCardType("1");
			}
			int num = documentMapper.insertSelective(document);

			if (num != 0) {
				result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
			} else {
				result.setResultCode(new ResultCode("FAILED", "添加失败！"));
			}
		}catch(Exception e) {
			logger.info(e.getMessage());
			if(document.getId()!=null && document.getId()>0) {
				documentMapper.deleteByPrimaryKey(document.getId());
			}
		}
		return result;
	}

	/**
	 * 删除个人单人信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deletePersonDocument(String id){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = documentMapper.deleteByPrimaryKey(Integer.parseInt(id));

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改个人单人信息信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> updatePersonDocument(XlPersonDocument document){
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (document == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		document.setUpdateTime(new Date());

		int num = documentMapper.updateByPrimaryKeySelective(document);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询单人信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result queryOnePersonDocument(String id,Integer platformId,String managerId) {

		Result result = new Result<>(StatusCodes.OK, true);
		if(!StringUtil.isEmpty(managerId)) {
			XlPersonDocumentExample example = new XlPersonDocumentExample();
			example.createCriteria().andManagerIdEqualTo(managerId);
			List<XlPersonDocument> list = documentMapper.selectByExample(example);
			if(null!=list && list.size()>0) {
				result.setData(list.get(0));
			}
		}else {
			XlPersonDocumentVO document = documentMapper.selectByPrimaryKey(Integer.parseInt(id));
			if(null!=document) {
				result.setData(document);
			}
		}
		return result;
	}
	/**
	 * 查询多人信息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result selectPersonDocument(XlPersonDocument document) {

		Result result = new Result<>(StatusCodes.OK, true);

		List<XlPersonDocument> documentList = documentMapper.selectDocument(document);

		if(null!=documentList && documentList.size()>0) {
			result.setData(documentList.get(0));
		}
		return result;
	}

	/**
	 * 查询个人单人信息 分页
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public PaginatedResult<XlPersonDocument> selectPersonDocumentListPage(XlPersonDocumentVO document) {

		List<XlPersonDocument> documentList = documentMapper.selectPersonDocumentListPage(document);

		for(XlPersonDocument xp: documentList) {
			if(!StringUtil.isEmpty(xp.getDocumentDoctor())) {
				xp.setDocumentDoctor(userMapper.selectByPrimaryKey(xp.getDocumentDoctor()).getName());
			}
			/*if(!StringUtil.isEmpty(xp.getKeyCrowdType())) {
				String keyCrowd ="";
				XlDictionary dic = new XlDictionary();
				for(String s: xp.getKeyCrowdType().split(",")) {
					dic.setItemType(1); // 重点人群
					dic.setItemId(Integer.parseInt(s));
					keyCrowd = keyCrowd +","+queryDictionary(dic).getItemName();
				}
				xp.setKeyCrowdType(keyCrowd.substring(keyCrowd.indexOf(",")+1,keyCrowd.length()));
			}*/
		}
		// 总页数
		int totalCount = document.getTotalResult();

		PaginatedResult<XlPersonDocument> pa = new PaginatedResult<XlPersonDocument>(documentList,
				document.getCurrentPage(), document.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}


	/**
	 * 根据身份证查询数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result querySignUserByEntityCard(XlPersonDocumentVO record) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfoVO user  = documentMapper.querySignUserByEntityCard(record);
		result.setData(user);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	/**
	 * 用户管理
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result getAllContractedUser(ApiContractedUserVO apiUserInfo) {

		PaginatedResult<ApiContractedUserVO> pa = null;
		int currentResult = (apiUserInfo.getCurrentPage() - 1) * apiUserInfo.getShowCount();
		apiUserInfo.setCurrentResult(currentResult);
		try {
			Pattern pattern = Pattern.compile("[0-9]*");
			List<String> doctorIdList = new ArrayList<String>();
			// 判断医生id 是否all
			if (!StringUtil.isEmpty(apiUserInfo.getDoctorId())
					&& apiUserInfo.getDoctorId().equals("all")) {
				apiUserInfo.setDoctorId(null);
			}
			if (!StringUtil.isEmpty(apiUserInfo.getDoctorId())) {
				doctorIdList.add("'" + apiUserInfo.getDoctorId() + "'");
			}
			if (!StringUtil.isEmpty(apiUserInfo.getOrgId())
					&& StringUtil.isEmpty(apiUserInfo.getDoctorId())) {
				JyDoctorInfo entity = new JyDoctorInfo();
				entity.setSignOrgId(apiUserInfo.getOrgId());
				entity.setShowCount(10000);
				entity.setCurrentPage(1);
				entity.setSignType(1);
				List<JyDoctorInfoVO> doctorInfoArray = jyOrgDocRelMapper.selectJySignDoctor(entity);
				if (null != doctorInfoArray && doctorInfoArray.size() > 0) {
					for (JyDoctorInfoVO jy : doctorInfoArray) {
						doctorIdList.add("'" + jy.getManagerId() + "'");
					}
				}
				doctorIdList.add("'" + apiUserInfo.getOrgId() + "'");
			}
			if (doctorIdList.size() > 0) {
				apiUserInfo.setDoctorIdList(doctorIdList);
			}
			String ageBegin = "";
			String ageEnd = "";
			if (!pattern.matcher(String.valueOf(apiUserInfo.getAgeId())).matches()) {
				apiUserInfo.setAgeId(null);
			} else {
				String ageId = apiUserInfo.getAgeId();
				if (ageId.equals("1")) {
					ageBegin = "0";
					ageEnd = "6";
				} else if (ageId.equals("2")) {
					ageBegin = "7";
					ageEnd = "17";
				} else if (ageId.equals("3")) {
					ageBegin = "18";
					ageEnd = "40";
				} else if (ageId.equals("4")) {
					ageBegin = "41";
					ageEnd = "65";
				} else if (ageId.equals("5")) {
					ageBegin = "65";
				} else if (ageId.equals("6")) {
					ageBegin = "-1";
				}
			}
			apiUserInfo.setAgeBegin(ageBegin);
			apiUserInfo.setAgeEnd(ageEnd);
			if (!pattern.matcher(String.valueOf(apiUserInfo.getDiseaseId())).matches()) {
				apiUserInfo.setDiseaseId(null);
			}
			if (!pattern.matcher(String.valueOf(apiUserInfo.getInUse())).matches()) {
				apiUserInfo.setInUse(null);
			}
			if (!StringUtil.isEmpty(apiUserInfo.getName())) {
				apiUserInfo.setName(apiUserInfo.getName() + "%");
			}
			List<ApiContractedUserVO> listUser = new ArrayList<ApiContractedUserVO>();
			listUser = documentMapper.getAllUserByConditionListPage(apiUserInfo);
			pa = new PaginatedResult<ApiContractedUserVO>(listUser, apiUserInfo.getCurrentPage(),
					apiUserInfo.getShowCount(), apiUserInfo.getTotalResult());
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			pa = new PaginatedResult<>(apiUserInfo.getCurrentPage(), apiUserInfo.getShowCount());
			pa.setSuccessful(false);
			pa.setResultCode(MysqlWrong);
		}
		return pa;
	}

}