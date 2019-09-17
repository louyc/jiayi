package com.lifelight.web.controller.xl;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.foxinmy.weixin4j.util.StringUtil;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lifelight.api.entity.ManagerInfo;
import com.lifelight.api.entity.ProcedureData;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.entity.XlPersonContract;
import com.lifelight.api.entity.XlPersonDocument;
import com.lifelight.api.vo.SmsVO;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.api.vo.XlPersonContractVO;
import com.lifelight.api.vo.XlPersonDocumentVO;
import com.lifelight.api.vo.XlPersonScheduleVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.ApiContractedUserService;
import com.lifelight.dubbo.service.ManagerService;
import com.lifelight.dubbo.service.WeixinConfigureService;
import com.lifelight.dubbo.service.XlPersonContractService;
import com.lifelight.dubbo.service.XlPersonDocumentService;
import com.lifelight.dubbo.service.XlPersonScheduleService;
import com.lifelight.web.util.DateUtil;
import com.lifelight.web.util.SMSUtil;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/xl")
public class XlPersonController {

	private static final Logger logger = LoggerFactory.getLogger(XlPersonController.class);

	@Reference
	private ManagerService managerService;
	@Reference
	private XlPersonContractService personContractService;
	@Reference
	private XlPersonDocumentService personDocumentService;
	@Reference
	private XlPersonScheduleService personScheduleService;
	@Autowired
	private RedisTool redisTool;
	@Reference
	private ApiContractedUserService apiContractedUserService;
	@Reference
	private WeixinConfigureService weixinConfigureService;

	/**
	 * groupSMS:批量发送短信<br/>
	 * telephoneMsg:消息;phoneNum:手机号
	 * @param request
	 * @param telephoneMsg
	 * @param phoneNum
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/groupSMS", method = RequestMethod.POST)
	@ResponseBody
	public Result groupSMS(HttpServletRequest request,
			@RequestBody SmsVO smsVO) {
		logger.info("群发短信: " + smsVO.getPhoneNum()+"  "+smsVO.getTelephoneMsg());
		Result result = new Result<>(StatusCodes.OK, true);
		String mobile ="";
		for(String s: smsVO.getPhoneNum().split(",")) {
			if(!StringUtil.isEmpty(s)) {
				mobile = mobile+","+s;
			}
		}
		logger.info("群发短信&&&&&&: " + mobile+"  "+smsVO.getTelephoneMsg());
		boolean b = false;
		if(!StringUtil.isEmpty(mobile)) {
			String domainName = request.getServerName();
			String weixinId = redisTool.get(domainName);
			if(StringUtil.isEmpty(weixinId)) {
				logger.info("获取微信配置信息错误****************");
				return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
			}
			WeixinConfigure configure = weixinConfigureService.selectWeixinConfigureBykey(weixinId);
			if(null!=configure && !StringUtil.isEmpty(configure.getWeixinName())) {
				String message = "【"+configure.getWeixinName()+"】"+smsVO.getTelephoneMsg();
				b = SMSUtil.sendCode(mobile.substring(1, mobile.length()),message);
			}
		}else {
			result.setSuccessful(true);
			result.setStatus(StatusCodes.FAILED_DEPENDENCY);
			result.setResultCode(new ResultCode("NULL", "手机号为空"));
			return result;
		}
		if(b) {
			result.setResultCode(new ResultCode("SUC", "发送成功"));
			return result;
		}else {
			result.setSuccessful(false);
			result.setStatus(StatusCodes.FAILED_DEPENDENCY);
			result.setResultCode(new ResultCode("FAIL", "发送失败"));
			return result;
		}
	}
	public static void main(String[] args) {
		String mobile ="";
		for(String s: "234".split(",")) {
			if(!StringUtil.isEmpty(s)) {
				mobile = mobile+","+s;
			}
		}
		System.out.println(mobile);
	}

	/**
	 * queryDictionary:查询字典表 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryDictionary", method = RequestMethod.GET)
	@ResponseBody
	public Result queryDictionary(HttpServletRequest request,
			@RequestParam(value = "typeId", required = false) String typeId) {
		logger.debug("查询字典表typeId: " + typeId);
		if(!StringUtil.isEmpty(typeId)) {
			if(typeId.equals("1")) {
				typeId ="43";
			}
			if(typeId.equals("15")) {
				typeId ="50";
			}
		}
		return apiContractedUserService.queryDictionary(typeId, null);
	}

	/**
	 * registPersonDocument:个人信息登记 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/registPersonDocument")
	@ResponseBody
	public Result registPersonDocument(HttpServletRequest request,
			@RequestBody XlPersonDocument document) {
		logger.debug("个人信息登记: " + document.getName());
		ManagerInfo manager = null;
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
		}
		document.setPlatformId(Integer.parseInt(weixinId));
		if(!StringUtil.isEmpty(document.getManagerId())) { //C端 直接传输managerId
			return personDocumentService.createPersonDocument(document);
		}else {//web端 创建用户manager
			if(!StringUtil.isEmpty(document.getCardNum())) {
				manager = managerService.getManagerInfoByIDcard(document.getCardNum(),Integer.parseInt(weixinId));
			}
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				document.setManagerId(manager.getId());
				return personDocumentService.createPersonDocument(document);
			}else {
				// 调用账户中心接口，为用户注册
				ManagerInfo managerInfo = new ManagerInfo();
//				managerInfo.setMobile(document.getMobile());
				managerInfo.setUserName(document.getName());
				managerInfo.setPassword("000000");
				managerInfo.setIdCode(document.getCardNum());
				managerInfo.setPlatformId(Integer.parseInt(weixinId));
				Result result = managerService.regist(managerInfo);
				// 用户中心注册成功或者已注册，完善信息
				if (result.getStatus() == StatusCodes.OK) {
					logger.info("用户中心 返回结果：：："+result.isSuccessful());
					document.setManagerId(result.getData().toString());
					return personDocumentService.createPersonDocument(document);
				} else { // 失败，返回错误结果
					return result;
				}
			}
		}
	}

	/**
	 * queryOnePersonDocument:个人信息查询 个人<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryOnePersonDocument")
	@ResponseBody
	public Result queryOnePersonDocument(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "managerId", required = false) String managerId) {
		logger.debug("个人信息查询 个人: " + id);
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
		}
		return personDocumentService.queryOnePersonDocument(id,Integer.parseInt(weixinId),managerId);
	}

	/**
	 * updatePersonDocument:个人信息修改 <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatePersonDocument")
	@ResponseBody
	public Result updatePersonDocument(HttpServletRequest request,
			@RequestBody XlPersonDocument contract) {
		logger.debug("个人信息登记: " + contract.getName());
		return personDocumentService.updatePersonDocument(contract);
	}

	/**
	 * queryPersonDocument:个人信息查询 分页<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryPersonDocument")
	@ResponseBody
	public Result queryPersonDocument(HttpServletRequest request,
			@RequestBody XlPersonDocumentVO contract) {
		logger.debug("个人信息查询 分页: " + contract.getId());
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
		}
		contract.setPlatformId(Integer.parseInt(weixinId));
		return personDocumentService.selectPersonDocumentListPage(contract);
	}

	/**
	 * queryServiceContent:查询服务包具体内容介绍<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryServiceContent")
	@ResponseBody
	public Result queryServiceContent(HttpServletRequest request,
			@RequestParam(value = "id", required = true) String id) {
		logger.debug("查询服务包具体内容介绍: " + id);
		return personContractService.queryServiceContent(id);
	}

	/**
	 * registPersonContract:签约登记<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/registPersonContract")
	@ResponseBody
	public Result registPersonContract(HttpServletRequest request,
			@RequestBody XlPersonContract contract) {
		logger.debug("签约登记: " + contract.getDocumentId());
		return personContractService.createPersonContract(contract);
	}

	/**
	 * queryAllContract:查询 签约服务合同管理<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryAllContract")
	@ResponseBody
	public Result queryAllContract(HttpServletRequest request,
			@RequestBody XlPersonDocumentVO documentVO) {
		logger.debug(" 签约服务合同管理 所有: ");
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
		}
		documentVO.setPlatformId(Integer.parseInt(weixinId));
		return personContractService.selectPersonContractListPage(documentVO);
	}
	/**
	 * queryAllContract:查询 签约服务合同管理  导出<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/exportAllContract")
	@ResponseBody
	public Result exportAllContract(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "inputPerson", required = false) String inputPerson,
			@RequestParam(value = "servicePackage", required = false) String servicePackage,
			@RequestParam(value = "isPayment", required = false) String isPayment,
			@RequestParam(value = "medicalType", required = false) String medicalType,
			@RequestParam(value = "signStatus", required = false) String signStatus,
			@RequestParam(value = "finishStatus", required = false) String finishStatus,
			@RequestParam(value = "auditStatus", required = false) String auditStatus,
			@RequestParam(value = "signPersonType", required = false) String signPersonType,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "cardNum", required = false) String cardNum,
			@RequestParam(value = "healthCard", required = false) String healthCard,
			@RequestParam(value = "signStartTime", required = false) String signStartTime,
			@RequestParam(value = "signEndTime", required = false) String signEndTime
			) {
		logger.debug(" 签约服务合同管理 导出所有: ");
		Result result = new Result<>(true);
		XlPersonDocumentVO documentVO = new XlPersonDocumentVO();
		if(!StringUtil.isEmpty(year)) {
			documentVO.setYear(year);
		}
		if(!StringUtil.isEmpty(inputPerson)) {
			documentVO.setInputPerson(inputPerson);
		}
		if(!StringUtil.isEmpty(servicePackage)) {
			documentVO.setServicePackage(servicePackage);
		}
		if(!StringUtil.isEmpty(isPayment)) {
			documentVO.setIsPayment(isPayment);
		}
		if(!StringUtil.isEmpty(medicalType)) {
			documentVO.setMedicalType(medicalType);
		}
		if(!StringUtil.isEmpty(signStatus)) {
			documentVO.setSignStatus(signStatus);
		}
		if(!StringUtil.isEmpty(finishStatus)) {
			documentVO.setFinishStatus(finishStatus);
		}
		if(!StringUtil.isEmpty(auditStatus)) {
			documentVO.setAuditStatus(auditStatus);
		}
		if(!StringUtil.isEmpty(signPersonType)) {
			documentVO.setSignPersonType(signPersonType);
		}
		if(!StringUtil.isEmpty(name)) {
			documentVO.setName(name);
		}
		if(!StringUtil.isEmpty(cardNum)) {
			documentVO.setCardNum(cardNum);
		}
		if(!StringUtil.isEmpty(healthCard)) {
			documentVO.setHealthCard(healthCard);
		}
		try {
			if(!StringUtil.isEmpty(signStartTime)) {
				documentVO.setSignStartTime(new SimpleDateFormat("yyyy-MM-dd").parse(signStartTime));
			}
			if(!StringUtil.isEmpty(signEndTime)) {
				documentVO.setSignEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(signEndTime));
			}
			// 文件名
			String fileName = "乡村医生签约服务记录表.xls";
			response.setContentType("application/x-excel");
			response.setCharacterEncoding("UTF-8");
			//		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
			response.setHeader("Content-Disposition", "attachment;filename="  
					+ new String(fileName.getBytes(),"iso-8859-1") + ".xls");  
			//查询当前域名微信配置表id
			logger.info("域名：：：："+request.getServerName());
			String domainName = request.getServerName();
			String weixinId = redisTool.get(domainName);
			if(StringUtil.isEmpty(weixinId)) {
				logger.info("获取微信配置信息错误****************");
				return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
			}
			documentVO.setPlatformId(Integer.parseInt(weixinId));
			result =personContractService.exportAllContract(documentVO);
			List<XlPersonDocumentVO> documents = (List<XlPersonDocumentVO>) (result.getData());
			//			List<XlPersonDocumentVO> documents = new ArrayList<XlPersonDocumentVO>();
			//			documents.add(x);
			String[] cells = { "服务对象姓名", "性别", "身份证号","档案编号", "家庭地址","联系方式",
					"签约服务包类型","医保类型","签约人群","签约医生", "签约机构"};

			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 定义标题   格式 字体 下划线 斜体 粗体 颜色
			WritableFont wf2 = new WritableFont(WritableFont.ARIAL,14,WritableFont.BOLD,
					false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
			WritableCellFormat wcfTitle = new WritableCellFormat(wf2);
			wcfTitle.setAlignment(Alignment.CENTRE);
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);
			WritableSheet sheet = book.createSheet("家庭医生签约服务记录表", 0);
			// 2.添加表头数据
			//第1列  第1行  到 第13列  第1行 
			sheet.mergeCells(0,0,10,0);
			sheet.addCell(new Label(0, 0, "家庭医生签约服务记录表", wcfTitle));

			for (int i = 0; i < cells.length; i++) {
				sheet.addCell(new Label(i, 1, cells[i], wf));
			}
			if (null != documents && documents.size() > 0) {
				for (int i = 0; i < documents.size(); i++) {
					XlPersonDocumentVO document = documents.get(i);
					if (document != null && document.getName() != null) {
						sheet.addCell(new Label(0, i + 2, document.getName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 2, "", wf));
					}
					if (document != null && document.getSex() != null) {
						if(document.getSex().equals("1")) {
							sheet.addCell(new Label(1, i + 2, "男" + "", wf));
						}else {
							sheet.addCell(new Label(1, i + 2, "女" + "", wf));
						}
					} else {
						sheet.addCell(new Label(1, i + 2, "", wf));
					}
					if (document != null && document.getCardNum() != null) {
						sheet.addCell(new Label(2, i + 2, document.getCardNum() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 2, "", wf));
					}
					if (document != null && document.getDocumentId() != null) {
						sheet.addCell(new Label(3, i + 2, document.getDocumentId() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 2, "", wf));
					}
					if (document != null && !StringUtil.isEmpty(document.getRegisteredResidence())) {
						sheet.addCell(new Label(4, i + 2, document.getRegisteredResidence() + "", wf));
					} else {
						sheet.addCell(new Label(4, i + 2, "", wf));
					}
					if (document != null && document.getMobile() != null) {
						sheet.addCell(new Label(5, i + 2, document.getMobile() + "", wf));
					} else {
						sheet.addCell(new Label(5, i + 2, "", wf));
					}
					if (document != null && document.getServicePackageDesc() != null) {
						sheet.addCell(new Label(6, i + 2, document.getServicePackageDesc() + "", wf));
					} else {
						sheet.addCell(new Label(6, i + 2, "", wf));
					}
					if (document != null && document.getMedicalType() != null) {
						sheet.addCell(new Label(7, i + 2, document.getMedicalTypeDesc() + "", wf));
					} else {
						sheet.addCell(new Label(7, i + 2, "", wf));
					}
					if (document != null && document.getSignPersonType() != null) {
						sheet.addCell(new Label(8, i + 2, document.getSignPersonTypeDesc() + "", wf));
					} else {
						sheet.addCell(new Label(8, i + 2, "", wf));
					}
					if (document != null && document.getSignDoctorName() != null) {
						sheet.addCell(new Label(9, i + 2, document.getSignDoctorName() + "", wf));
					} else {
						sheet.addCell(new Label(9, i + 2, "", wf));
					}
					if (document != null && document.getSignOrgName() != null) {
						sheet.addCell(new Label(10, i + 2, document.getSignOrgName() + "", wf));
					} else {
						sheet.addCell(new Label(10, i + 2, "", wf));
					}
				}
			}
			book.write();
			book.close();
		}catch(Exception e) {
			e.printStackTrace();
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		result.setData(null);
		return result;
	}
	/**
	 * queryPersonContract:查询 个人  签约信息<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryPersonContract")
	@ResponseBody
	public Result queryPersonContract(HttpServletRequest request,
			@RequestParam(value = "documentId", required = false) String documentId,
			@RequestParam(value = "managerId", required = false) String managerId) {
		logger.debug("签约信息查询 个人: 档案id" + documentId);
		XlPersonContractVO contract = new XlPersonContractVO();
		if(!StringUtil.isEmpty(documentId)) {
			contract.setDocumentId(Integer.parseInt(documentId));
		}
		if(!StringUtil.isEmpty(managerId)) {
			contract.setManagerId(managerId);
		}
		return personContractService.selectPersonContract(contract);
	}

	/**
	 * updatePersonContract:签约信息修改<br/> 
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatePersonContract")
	@ResponseBody
	public Result updatePersonContract(HttpServletRequest request,
			@RequestBody XlPersonContract contract) {
		logger.debug("签约修改: " + contract.getId());
		return personContractService.updatePersonContract(contract);
	}

	/**
	 * updateContractStatus:签约状态修改<br/> 
	 *  、删除 修改
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateContractStatus")
	@ResponseBody
	public Result updateContractStatus(HttpServletRequest request,
			@RequestParam(value = "documentId", required = true) String documentId,
			@RequestParam(value = "signStatus", required = false) String signStatus,
			@RequestParam(value = "inUse", required = false) String inUse
			) {
		logger.debug("签约状态修改: " + documentId+"  "+signStatus);
		XlPersonContractVO contract = new XlPersonContractVO();
		contract.setDocumentId(Integer.valueOf(documentId));
		if(!StringUtil.isEmpty(signStatus)) {
			contract.setSignStatus(signStatus);
		}
		if(!StringUtil.isEmpty(inUse)) {
			contract.setInUse(inUse);
		}
		return personContractService.updateContractStatus(contract);
	}

	/**
	 * queryPersonSchedule:个人检查项目进度   返回list<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryPersonSchedule")
	@ResponseBody
	public Result queryPersonSchedule(HttpServletRequest request,
			@RequestParam(value = "documentId", required = true) String documentId) {
		logger.debug("个人检查项目进度    查询: " + documentId);
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return personScheduleService.selectPersonSchedule(documentId,Integer.parseInt(weixinId));
	}
	/**
	 * queryOneScheduleItem:查询个人检查项目进度    单条检查项记录<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryOneScheduleItem")
	@ResponseBody
	public Result queryOneScheduleItem(HttpServletRequest request,
			@RequestParam(value = "id", required = true) String id) {
		logger.debug("个人检查项目id  查询: " + id);
		//获取微信配置id
		return personScheduleService.queryOneScheduleItem(id);
	}

	/**
	 * updatePersonSchedule:个人检查项目进度    更新检查数据<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatePersonSchedule")
	@ResponseBody
	public Result updatePersonSchedule(HttpServletRequest request,
			@RequestBody XlPersonScheduleVO schedule) {
		logger.debug("个人检查项目进度    更新检查数据: " + schedule.getId()+" "
				+schedule.getProjectValue());
		return personScheduleService.updatePersonSchedule(schedule);
	}

	/**
	 * deletePersonSchedule:个人检查项目进度   删除<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deletePersonSchedule")
	@ResponseBody
	public Result deletePersonSchedule(HttpServletRequest request,
			@RequestParam(value = "id", required = true) String id) {
		logger.debug("个人检查项目进度    删除: " + id);
		return personScheduleService.deletePersonSchedule(id);
	}

	/**
	 * queryAllSchedule:检查所有项目进度   详情查询<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryAllSchedule")
	@ResponseBody
	public Result queryAllSchedule(HttpServletRequest request,
			@RequestBody XlPersonDocumentVO documentVO) {
		logger.debug("检查项目进度   详情查询: " + documentVO.toString());
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		documentVO.setPlatformId(Integer.parseInt(weixinId));
		return personScheduleService.selectPersonScheduleListPage(documentVO);
	}

	/**
	 * queryOneSchedule:检查某人的项目详情  页面展示汇总进度<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryOneSchedule")
	@ResponseBody
	public Result queryOneSchedule(HttpServletRequest request,
			@RequestParam(value = "id", required = true) String id) {
		logger.debug("检查项目进度   详情查询  档案id: " + id);
		return personScheduleService.queryOneSchedule(id);
	}

	/**
	 * updateAuditStatus:检查项目进度  审核状态修改  <br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateAuditStatus")
	@ResponseBody
	public Result updateAuditStatus(HttpServletRequest request,
			@RequestParam(value = "id", required = true) String id
			,@RequestParam(value = "auditStatus", required = true) String auditStatus) {
		logger.debug("检查项目进度   审核状态修改: " + id+" "+auditStatus);
		XlPersonContractVO xlp = new XlPersonContractVO();
		xlp.setDocumentId(Integer.valueOf(id));
		xlp.setAuditStatus(auditStatus);
		return personScheduleService.updateAuditStatus(xlp);
	}


	/**
	 * 流程数据上传接口
	 * 
	 * @param phone
	 * @param type
	 * @param data
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/subject/save/saveSubjects")
	@ResponseBody
	public Result saveSubjects(
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "projectId", required = false) String projectId,
			@RequestParam(value = "ssitemId", required = false) String ssitemId,
			@RequestParam(value = "datas", required = true) String datas,
			HttpServletRequest request, HttpServletResponse response) {
		//		JSONObject retJson = new JSONObject();
		//		PrintWriter out = null;
		logger.info("******************phone::"+phone);//
		logger.info("******************projectId::"+projectId);//managerId
		logger.info("******************datas::"+datas);
		return personDocumentService.saveProcedureDate(phone,datas);
	}

	/**
	 * 流程数据上传接口
	 * 
	 * @param phone
	 * @param type
	 * @param data
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/subject/getSubjects.action")
	@ResponseBody
	public Result getSubjects(@RequestBody ProcedureData procedure,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("******************managerId::"+procedure.getManagerId());//managerId
		return personDocumentService.getProcedureDate(procedure);
	}

	/**
	 * excuteSchedule:导出所有项目进度   详情查询<br/>
	 * 
	 * @param request
	 * @param typeId
	 * @return Result
	 * @author
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportSchedule")
	@ResponseBody
	public Result exportSchedule(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "doctor", required = false) String doctor,
			@RequestParam(value = "reason", required = false) String reason
			) {
		logger.info("域名：：：："+request.getServerName());
		Result result = new Result<>(StatusCodes.OK, true);
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		WeixinConfigureVO weixin = null;
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "获取微信配置信息错误"));
		}else {
			Result configureResult = weixinConfigureService.queryWeicinConfigureByDomain(null,weixinId);
			if(null !=configureResult.getData()) {
				weixin = (WeixinConfigureVO)configureResult.getData();
			}else {
				logger.info("获取微信配置信息错误****************");
				return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "菜单生成失败！"));
			}
		}
		//查询个人档案
		result =  personDocumentService.queryOnePersonDocument(id,Integer.parseInt(weixinId),null);
		XlPersonDocumentVO documentVO = (XlPersonDocumentVO)result.getData();
		//查询个人签约信息
		XlPersonContractVO contract = new XlPersonContractVO();
		contract.setDocumentId(Integer.parseInt(id));
		result = personContractService.selectPersonContract(contract);
		XlPersonContractVO contractVO = (XlPersonContractVO)result.getData();
		//查询个人进度信息
		result = personScheduleService.queryOneSchedule(id);
		List<XlPersonScheduleVO> schedultList = (List<XlPersonScheduleVO>)((Map<String,Object>)result.getData()).get("scheduleList");
		boolean b = true;
		// 实例化文档对象
		try {
			String fileName = "个人信息表.pdf";
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			// 防止中文乱码
			fileName = new String(fileName.getBytes(),"iso-8859-1");
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentType("application/octet-stream;charset=UTF-8");
			//创建 PdfWriter 对象
			Document document = new Document(PageSize.A4.rotate()); 
			PdfWriter.getInstance(document, os);
			document.open();
			//中文字符控制
			BaseFont bfChinese = BaseFont.createFont("/simsun.ttc,1", //注意这里有一个,1
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font font = new Font(bfChinese,11);
			Font fontTitle = new Font (bfChinese, 18, Font.BOLD, new CMYKColor(0, 255, 255,17));
			Font FontChinese18 = new Font(bfChinese, 18, Font.BOLD); 
			Font FontChinese13 = new Font(bfChinese, 13, Font.BOLD);
			Font FontChinese15 = new Font(bfChinese, 15, Font.ITALIC);

			//标题
			Paragraph title1 = new Paragraph("",FontChinese18);
			title1.setAlignment(Element.ALIGN_CENTER);
			document.add(new Paragraph("转至医院："+hospital,FontChinese15));
			document.add(new Paragraph("医生："+doctor,FontChinese15));
			document.add(new Paragraph("原因:"+reason,FontChinese15));
			// 第一段
			document.add(new Paragraph("1、基本信息",fontTitle));
			//创建表格对象
			PdfPTable t = new PdfPTable(4);
			t.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
			t.setHorizontalAlignment(Element.ALIGN_MIDDLE);;//垂直居中
			t.setSpacingBefore(15);
			t.setSpacingAfter(15);
			t.addCell(new Paragraph("姓名",font));
			t.addCell(new Paragraph(documentVO.getName(),font));
			t.addCell(new Paragraph("性别",font));
			if(documentVO.getSex().equals("1")) {
				t.addCell(new Paragraph("男",font));
			}else if(documentVO.getSex().equals("2")) {
				t.addCell(new Paragraph("女",font));
			}else {
				t.addCell(new Paragraph("未知",font));
			}
			t.addCell(new Paragraph("身份证号",font));
			t.addCell(new Paragraph(documentVO.getCardNum(),font));
			t.addCell(new Paragraph("出生日期",font));
			if(null!=documentVO.getBirthday()) {
				t.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(documentVO.getBirthday()),font));
			}else {
				t.addCell(new Paragraph("",font));
			}
			t.addCell(new Paragraph("联系电话",font));
			t.addCell(new Paragraph(documentVO.getMobile(),font));
			t.addCell(new Paragraph("健康卡号",font));
			t.addCell(new Paragraph(documentVO.getHealthCard(),font));
			t.addCell(new Paragraph("档案编号",font));
			t.addCell(new Paragraph(documentVO.getDocumentId(),font));
			t.addCell(new Paragraph("新农合卡号",font));
			t.addCell(new Paragraph(documentVO.getNewFarmersCard(),font));
			t.addCell(new Paragraph("医保卡号",font));
			t.addCell(new Paragraph(documentVO.getMedicalCardNumber(),font));
			t.addCell(new Paragraph("行政区划",font));
			t.addCell(new Paragraph(documentVO.getCityName(),font));
			t.addCell(new Paragraph("居住地址",font));
			t.addCell(new Paragraph(documentVO.getResidentialAddress(),font));
			t.addCell(new Paragraph("户籍地址",font));
			t.addCell(new Paragraph(documentVO.getPermanentAddress(),font));
			t.addCell(new Paragraph("工作单位",font));
			t.addCell(new Paragraph(documentVO.getWorkUnit(),font));
			t.addCell(new Paragraph("家庭关系",font));
			t.addCell(new Paragraph(DateUtil.FamilyRelationsMap.get(documentVO.getFamilyRelations()),font));
			t.addCell(new Paragraph("联系人姓名",font));
			t.addCell(new Paragraph(documentVO.getContactsName(),font));
			t.addCell(new Paragraph("联系人电话",font));
			t.addCell(new Paragraph(documentVO.getContactsMobile(),font));
			t.addCell(new Paragraph("居住状态",font));
			t.addCell(new Paragraph(DateUtil.LivingStateMap.get(documentVO.getLivingState()),font));
			t.addCell(new Paragraph("居住类型",font));
			t.addCell(new Paragraph(DateUtil.LivingTypeMap.get(documentVO.getLivingType()),font));
			t.addCell(new Paragraph("户口类型",font));
			t.addCell(new Paragraph(DateUtil.RegisteredResidenceMap.get(documentVO.getRegisteredResidence()),font));
			t.addCell(new Paragraph("来本市日期",font));
			if(null!=documentVO.getArriveDate()) {
				t.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(documentVO.getArriveDate()),font));
			}else {
				t.addCell(new Paragraph("",font));
			}
			t.addCell(new Paragraph("结婚日期",font));
			if(null!=documentVO.getArriveDate()) {
				t.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(documentVO.getDateMarriage()),font));
			}else {
				t.addCell(new Paragraph("",font));
			}
			t.addCell(new Paragraph("建档机构",font));
			t.addCell(new Paragraph(documentVO.getDocumentOrgName(),font));
			t.addCell(new Paragraph("建档医生",font));
			t.addCell(new Paragraph(documentVO.getDocumentDoctorName(),font));
			t.addCell(new Paragraph("建档日期",font));
			if(null!=documentVO.getDocumentDate()) {
				t.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(documentVO.getDocumentDate()),font));
			}else {
				t.addCell(new Paragraph(" ",font));
			}
			t.addCell(new Paragraph("医疗费用支付",font));
			if(null!=documentVO.getMedicalCosts()) {
				t.addCell(new Paragraph(String.valueOf(documentVO.getMedicalCosts()),font));
			}else {
				t.addCell(new Paragraph(" ",font));
			}
			t.addCell(new Paragraph("医疗卡号",font));
			t.addCell(new Paragraph(documentVO.getMedicalCardNumber(),font));
			t.addCell(new Paragraph("低保卡号",font));
			t.addCell(new Paragraph(documentVO.getLowInsuranceCar(),font));
			t.addCell(new Paragraph("药物过敏史",font));
			t.addCell(new Paragraph(documentVO.getDrugAllergyHistory(),font));
			t.addCell(new Paragraph("既往史",font));
			t.addCell(new Paragraph(documentVO.getPastHistory(),font));
			t.addCell(new Paragraph("母亲病史",font));
			t.addCell(new Paragraph(documentVO.getMotherMedicalHistory(),font));
			t.addCell(new Paragraph("父亲病史",font));
			t.addCell(new Paragraph(documentVO.getFatherMedicalHistory(),font));
			t.addCell(new Paragraph("有无遗传病史",font));
			t.addCell(new Paragraph(documentVO.getHeredityHistory(),font));;
			t.addCell(new Paragraph("遗传病名",font));
			t.addCell(new Paragraph(documentVO.getHeredityHistory(),font));
			t.addCell(new Paragraph("有无残疾",font));
			t.addCell(new Paragraph(documentVO.getIsDisability(),font));
			t.addCell(new Paragraph("残疾证号",font));
			t.addCell(new Paragraph(documentVO.getDisabilityCard(),font));
			t.addCell(new Paragraph("暴露史",font));
			t.addCell(new Paragraph(documentVO.getExposeHistory(),font));
			t.addCell(new Paragraph("有无手术室",font));
			if(!StringUtil.isEmpty(documentVO.getOperationHistory())) {
				JSONArray json = JSONArray.fromObject(documentVO.getOperationHistory()); // 首先把字符串转成 JSONArray  对象
				String sum="";
				if(json.size()>0){
					for(int i=0;i<json.size();i++){
						JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
						sum = sum+"名称："+job.get("name")+"  时间："+job.get("time")+"\r\n";
					}
				}
				t.addCell(new Paragraph(sum,font));
			}else {
				t.addCell(new Paragraph("",font));
			}
			t.addCell(new Paragraph("有无外伤史",font));
			if(!StringUtil.isEmpty(documentVO.getTraumaHistory())) {
				JSONArray json = JSONArray.fromObject(documentVO.getTraumaHistory()); // 首先把字符串转成 JSONArray  对象
				String sum="";
				if(json.size()>0){
					for(int i=0;i<json.size();i++){
						JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
						sum = sum+"名称："+job.get("name")+"  时间："+job.get("time")+"\r\n";
					}
				}
				t.addCell(new Paragraph(sum,font));
			}else {
				t.addCell(new Paragraph("",font));
			}
			t.addCell(new Paragraph("有无输血史",font));
			if(!StringUtil.isEmpty(documentVO.getBloodTransfusionHistory())) {
				JSONArray json = JSONArray.fromObject(documentVO.getBloodTransfusionHistory()); // 首先把字符串转成 JSONArray  对象
				String sum="";
				if(json.size()>0){
					for(int i=0;i<json.size();i++){
						JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
						sum = sum+"名称："+job.get("name")+"  时间："+job.get("time")+"\r\n";
					}
				}
				t.addCell(new Paragraph(sum,font));
			}else {
				t.addCell(new Paragraph(documentVO.getBloodTransfusionHistory(),font));
			}
			t.addCell(new Paragraph("厨房排风设施",font));
			t.addCell(new Paragraph(documentVO.getKitchenExhaust(),font));;
			t.addCell(new Paragraph("燃料类型",font));
			t.addCell(new Paragraph(documentVO.getFuelType(),font));
			t.addCell(new Paragraph("饮水",font));
			t.addCell(new Paragraph(documentVO.getWater(),font));
			t.addCell(new Paragraph("禽畜栏",font));
			t.addCell(new Paragraph(documentVO.getToilet(),font));
			t.addCell(new Paragraph("厕所",font));
			t.addCell(new Paragraph(documentVO.getToilet(),font));
			t.addCell(new Paragraph("备注",font));
			t.addCell(new Paragraph(documentVO.getRemarks(),font));
			t.addCell(new Paragraph("重点人群",font));
			if(!StringUtil.isEmpty(documentVO.getKeyCrowdType())) {
				String[] ss = documentVO.getKeyCrowdType().split(",");
				String keyCrow ="";
				for(String s: ss) {
					keyCrow = keyCrow + DateUtil.KeyCrowdTypeMap.get(s)+",";
				}
				t.addCell(new Paragraph(keyCrow.substring(0, keyCrow.length()-1),font));
			}else {
				t.addCell(new Paragraph("",font));
			}
			document.add(t);

			document.add(new Paragraph("2、签约信息",fontTitle));
			//创建表格对象
			PdfPTable t2 = new PdfPTable(4);
			t2.setSpacingBefore(15);
			t2.setSpacingAfter(15);
			t2.addCell(new Paragraph("姓名",font));
			t2.addCell(new Paragraph(contractVO.getName(),font));
			t2.addCell(new Paragraph("身份证号",font));
			t2.addCell(new Paragraph(contractVO.getCardNum(),font));
			t2.addCell(new Paragraph("性别",font));
			if(contractVO.getSex().equals("1")) {
				t2.addCell(new Paragraph("男",font));
			}else if(contractVO.getSex().equals("2")) {
				t2.addCell(new Paragraph("女",font));
			}else {
				t2.addCell(new Paragraph("未知",font));
			}
			t2.addCell(new Paragraph("年龄",font));
			t2.addCell(new Paragraph(contractVO.getAge(),font));
			t2.addCell(new Paragraph("与户主关系",font));
			t2.addCell(new Paragraph(DateUtil.FamilyRelationsMap.get(contractVO.getFamilyRelations()),font));
			t2.addCell(new Paragraph("个人健康档案编号",font));
			t2.addCell(new Paragraph(contractVO.getDocumentNum(),font));
			t2.addCell(new Paragraph("文化程度",font));
			t2.addCell(new Paragraph(DateUtil.DegreeEducationMap.get(contractVO.getDegreeEducation()),font));
			t2.addCell(new Paragraph("职业",font));
			t2.addCell(new Paragraph(DateUtil.OccupationMap.get(contractVO.getOccupation()),font));
			t2.addCell(new Paragraph("行政区划",font));
			t2.addCell(new Paragraph(documentVO.getCityName(),font));
			t2.addCell(new Paragraph("居住地址",font));
			t2.addCell(new Paragraph(contractVO.getResidentialAddress(),font));
			t2.addCell(new Paragraph("是否贫困人口",font));
			t2.addCell(new Paragraph(contractVO.getIsPoor(),font));
			t2.addCell(new Paragraph("签约日期",font));
			if(contractVO.getSignStartDate()!=null) {
				t2.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contractVO.getSignStartDate()),font));
			}else {
				t2.addCell(new Paragraph("",font));
			}
			t2.addCell(new Paragraph("联系电话",font));
			t2.addCell(new Paragraph(contractVO.getMobile(),font));
			t2.addCell(new Paragraph("家庭人口数",font));
			t2.addCell(new Paragraph(contractVO.getFamilyCount(),font));
			t2.addCell(new Paragraph("签约模式",font));
			t2.addCell(new Paragraph(DateUtil.SignModeMap.get(contractVO.getSignMode()),font));
			t2.addCell(new Paragraph("队长姓名",font));
			t2.addCell(new Paragraph(contractVO.getCaptainName(),font));
			t2.addCell(new Paragraph("医生联系电话",font));
			t2.addCell(new Paragraph(contractVO.getDoctorMobile(),font));
			t2.addCell(new Paragraph("健康状况",font));
			t2.addCell(new Paragraph(contractVO.getHealth(),font));
			t2.addCell(new Paragraph("年度",font));
			t2.addCell(new Paragraph(contractVO.getYear(),font));
			t2.addCell(new Paragraph("签约机构",font));
			t2.addCell(new Paragraph(contractVO.getSignOrgName(),font));
			t2.addCell(new Paragraph("签约医生",font));
			t2.addCell(new Paragraph(contractVO.getSignDoctorName(),font));
			t2.addCell(new Paragraph("签约开始日期",font));
			if(contractVO.getSignStartDate()!=null) {
				t2.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd").format(contractVO.getSignStartDate()),font));
			}else {
				t2.addCell(new Paragraph("",font));
			}
			t2.addCell(new Paragraph("签约结束日期",font));
			if(contractVO.getSignEndDate ()!=null) {
				t2.addCell(new Paragraph(new SimpleDateFormat("yyyy-MM-dd").format(contractVO.getSignEndDate()),font));
			}else {
				t2.addCell(new Paragraph("",font));
			}

			t2.addCell(new Paragraph("服务内容",font));
			t2.addCell(new Paragraph(contractVO.getServiceContent(),font));
			t2.addCell(new Paragraph("健康评估",font));
			t2.addCell(new Paragraph(contractVO.getHealthAssessment(),font));
			t2.addCell(new Paragraph("处置安排",font));
			t2.addCell(new Paragraph(DateUtil.ArrangementMap.get(contractVO.getArrangement()),font));
			t2.addCell(new Paragraph("服务方式",font));
			t2.addCell(new Paragraph(DateUtil.ServiceModeMap.get(contractVO.getServiceMode()),font));
			t2.addCell(new Paragraph("医保类型",font));
			t2.addCell(new Paragraph(DateUtil.MedicalTypeMap.get(contractVO.getMedicalType()),font));
			t2.addCell(new Paragraph("签约金额",font));
			t2.addCell(new Paragraph(String.valueOf(contractVO.getSignPayment()),font));
			t2.addCell(new Paragraph("居民自付金额",font));
			t2.addCell(new Paragraph(String.valueOf(contractVO.getSelfPayment()),font));
			t2.addCell(new Paragraph("农合补偿金额",font));
			t2.addCell(new Paragraph(String.valueOf(contractVO.getCompensationPayment()),font));
			t2.addCell(new Paragraph("签约人群",font));
			if(!StringUtil.isEmpty(contractVO.getSignPersonType())) {
				String[] ss = contractVO.getSignPersonType().split(",");
				String signPerson ="";
				for(String s: ss) {
					signPerson = signPerson + DateUtil.SignPersonTypeMap.get(s)+",";
				}
				t2.addCell(new Paragraph(signPerson.substring(0, signPerson.length()-1),font));
			}else {
				t2.addCell(new Paragraph("",font));
			}
			t2.addCell(new Paragraph("签约服务包",font));
			if(!StringUtil.isEmpty(contractVO.getPackageName())) {
				t2.addCell(new Paragraph(String.valueOf(contractVO.getPackageName()),font));
			}else {
				t2.addCell(new Paragraph("",font));
			}
			t2.addCell(new Paragraph("",font));
			t2.addCell(new Paragraph("",font));
			document.add(t2);
			//检查填写
			String examinationInfo ="";
			//签约进度
			Map<String,String> mapImage = new HashMap<String,String>();
			if(null!=schedultList && schedultList.size()>0) {
				document.add(new Paragraph("3、履约信息",fontTitle));
				for(XlPersonScheduleVO xv: schedultList) {
					examinationInfo = xv.getExaminationInfo();
					//{"name":"王永卫","sex":"1","project_type_content":"","time":"","project_type_desc":"尿常规","remark":"",
					//"result":"{\"RU_LEU\":\"1\",\"RU_Pro\":\"1\",\"RU_Glu\":\"1\"}","certificationsUrl":""}
					//{"name":"娄","sex":"1","project_type_content":"234243","time":"2018-06-13","project_type_desc":"血糖","remark":"234","result":"{\"xuetang\":\"1\"}",
					//"certificationsUrl":"/upFile/fb99adc1-f418-422e-9ac6-18a86a184e34.jpg"}
					if(StringUtil.isEmpty(xv.getProjectValue())) {
						continue;
					}
					PdfPTable t3 = new PdfPTable(4);
					t3.setSpacingBefore(15);
					t3.setSpacingAfter(15);//创建表格对象
					t3.addCell(new Paragraph("姓名",font));
					t3.addCell(new Paragraph(xv.getName(),font));
					t3.addCell(new Paragraph("性别",font));
					if(xv.getSex().equals("1")) {
						t3.addCell(new Paragraph("男",font));
					}else if(xv.getSex().equals("2")) {
						t3.addCell(new Paragraph("女",font));
					}else {
						t3.addCell(new Paragraph("未知",font));
					}
					t3.addCell(new Paragraph("项目",font));
					t3.addCell(new Paragraph(xv.getProjectTypeDesc(),font));
					t3.addCell(new Paragraph("执行时间",font));
					if(!StringUtil.isEmpty(xv.getProjectValue())) {
						Map map1 = JSON.parseObject(xv.getProjectValue());
						t3.addCell(new Paragraph(String.valueOf(map1.get("time")),font));
					}else {
						t3.addCell(new Paragraph("",font));
					}
					t3.addCell(new Paragraph("项目说明",font));
					t3.addCell(new Paragraph(xv.getProjectTypeDesc(),font));
					t3.addCell(new Paragraph("备注",font));
					if(!StringUtil.isEmpty(xv.getProjectValue())) {
						Map map1 = JSON.parseObject(xv.getProjectValue());
						t3.addCell(new Paragraph(String.valueOf(map1.get("remark")),font));
					}else {
						t3.addCell(new Paragraph("",font));
					}
					String type = xv.getProjectType();
					if(!StringUtil.isEmpty(xv.getProjectValue())) {
						Map map1 = JSON.parseObject(xv.getProjectValue());
						if(!StringUtil.isEmpty(String.valueOf(map1.get("certificationsUrl")))) {
							mapImage.put(xv.getProjectTypeDesc(), String.valueOf(map1.get("certificationsUrl")));
						}
						if(null!=map1.get("result") && ""!=String.valueOf(map1.get("result"))) {
							Map map2 = JSON.parseObject(String.valueOf(map1.get("result")));
							switch(type){
							case"1"://基础项目
								break;
							case"2"://血糖
								if(null!=map2.get("xuetang")) {
									t3.addCell(new Paragraph("血糖",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("xuetang")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"3"://尿常规
								if(null!=map2.get("RU_LEU")) {
									t3.addCell(new Paragraph("尿白细胞（LEU）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("RU_LEU")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("RU_Pro")) {
									t3.addCell(new Paragraph("尿蛋白（Pro）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("RU_Pro")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("RU_Glu")) {
									t3.addCell(new Paragraph("尿糖(Glu)",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("RU_Glu")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"4"://心电图
								if(null!=map2.get("ST_T")) {
									t3.addCell(new Paragraph("心率、心律、ST_T情况",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("ST_T")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"5"://肝胆胰脾
								if(null!=map2.get("FO_gdyp")) {
									t3.addCell(new Paragraph("肝胆胰脾器形状",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("FO_gdyp")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"6"://血脂
								if(null!=map2.get("BF_CHOL")) {
									t3.addCell(new Paragraph("总胆固醇（CHOL）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BF_CHOL")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("BF_HDL_C")) {
									t3.addCell(new Paragraph("高密度脂蛋白胆固醇（HDL_C）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BF_HDL_C")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("BF_TG")) {
									t3.addCell(new Paragraph("总甘油三酯（TG）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BF_TG")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("BF_LDL_C")) {
									t3.addCell(new Paragraph("低密度脂蛋白胆固醇（LDL_C",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BF_LDL_C")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"7"://肝功能
								if(null!=map2.get("TBLL")) {
									t3.addCell(new Paragraph("总胆红素（TBLL）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("TBLL")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("ALT")) {
									t3.addCell(new Paragraph("谷丙转氨酶（ALT）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("ALT")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("AST")) {
									t3.addCell(new Paragraph("谷草转氨酶（AST）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("AST")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"8"://肾功能
								if(null!=map2.get("BUN")) {
									t3.addCell(new Paragraph("尿素氮（BUN）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BUN")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("Gr")) {
									t3.addCell(new Paragraph("肌酐（Gr）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("Gr")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"9"://家庭巡检
								if(null!=map2.get("FI_jtxj")) {
									t3.addCell(new Paragraph("家庭巡诊及健教",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("FI_jtxj")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"10"://一体机检查
								if(null!=map2.get("YTJ_ru")) {
									t3.addCell(new Paragraph("尿常规",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("YTJ_ru")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("YTJ_xuetang")) {
									t3.addCell(new Paragraph("血糖",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("YTJ_xuetang")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("YTJ_SPO2")) {
									t3.addCell(new Paragraph("血氧饱和度（SPO2）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("YTJ_SPO2")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("YTJ_ecg")) {
									t3.addCell(new Paragraph("心电图检查",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("YTJ_ecg")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("YTJ_bf")) {
									t3.addCell(new Paragraph("血脂",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("YTJ_bf")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"11"://血清尿酸化验
								if(null!=map2.get("SUA_ua")) {
									t3.addCell(new Paragraph("尿酸（UA）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("SUA_ua")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"12"://双肾B超
								if(null!=map2.get("DB_man")) {
									t3.addCell(new Paragraph("双肾、输尿管、膀胱、前列腺（仅男性）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("DB_man")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"13"://彩超B超
								if(null!=map2.get("CB_women")) {
									t3.addCell(new Paragraph("子宫、卵管、卵",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("CB_women")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"14"://血常规
								if(null!=map2.get("BR_br")) {
									t3.addCell(new Paragraph("血常规",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BR_br")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"15"://凝血四项
								if(null!=map2.get("FC_pt")) {
									t3.addCell(new Paragraph("凝血酶原时间（PT",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("FC_pt")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("FC_APTT")) {
									t3.addCell(new Paragraph("活化部分凝血活酶时间（APTT）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("FC_APTT")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("FC_TT")) {
									t3.addCell(new Paragraph("凝血酶时间（TT）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("FC_TT")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("FC_FIB")) {
									t3.addCell(new Paragraph("纤维蛋白原（FIB）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("FC_FIB")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"16"://全胸片
								if(null!=map2.get("ACF_fxz")) {
									t3.addCell(new Paragraph("肺、心脏脏器形状",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("ACF_fxz")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"17"://其他系统B超
								if(null!=map2.get("OB_other")) {
									t3.addCell(new Paragraph("其他系统B超",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("OB_other")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"18"://心梗三项检查
								if(null!=map2.get("MI_CTnI")) {
									t3.addCell(new Paragraph("肌钙蛋白（CTnI）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("MI_CTnI")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("MI_CKMB")) {
									t3.addCell(new Paragraph("肌酸激酶同工酶（CKMB）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("MI_CKMB")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								if(null!=map2.get("MI_Myoo")) {
									t3.addCell(new Paragraph("肌红蛋白（Myo）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("MI_Myoo")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"19"://甲状腺彩超检查/心脏彩超检查
								if(null!=map2.get("TC_TC")) {
									t3.addCell(new Paragraph("甲状腺彩超检查/心脏彩超检查",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("TC_TC")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"20"://血糖检查
								if(null!=map2.get("BGC_xuetang")) {
									t3.addCell(new Paragraph("甲状腺彩超检查/心脏彩超检查",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("BGC_xuetang")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"21"://糖化血红蛋白检查
								if(null!=map2.get("HBA1C_HBAIC")) {
									t3.addCell(new Paragraph("糖化血红蛋白（HBAIC）",font));
									t3.addCell(new Paragraph(String.valueOf(map2.get("HBA1C_HBAIC")),font));
									t3.addCell(new Paragraph("",font));
									t3.addCell(new Paragraph("",font));
								}
								break;
							case"22"://尿微量白蛋白
								break;
							case"23"://颈部血管
								break;
							case"24"://超敏C反应蛋白测定
								break;
							case"25"://电解质测定
								break;
							case"26"://胸部CT
								break;
							case"27"://胎心检测
								break;
							case"28"://早孕甲功三项
								break;
							case"29"://孕妇免疫九项
								break;
							}
						}
					}
					document.add(t3);
				}
			}
			if(null!=mapImage && mapImage.size()>0) {
				int i=1;
				for(String mage: mapImage.keySet()) {
					//"certificationsUrl":"/upFile/29e9fe60-89b3-4e76-8252-3a8b6e3fa437.jpg;/upFile/847b42c8-414b-41cf-9f6c-174b0aa2e021.jpg"
					for(String s: mapImage.get(mage).split(";")) {
						if(!StringUtil.isEmpty(s) && s!=null && s.split("/").length>1) {
							document.add(new Paragraph("4、签约检查项上传图片",fontTitle));
							document.add(new Paragraph(i+"."+mage,FontChinese13));
							Image image = Image.getInstance("http://"+weixin.getFileHost()+s);
							image.scaleAbsolute(180f, 180f);
							document.add(image);
						}
					}
					i++;
				}
			}
			// 换行
			document.add(new Chunk("\n\n"));
			if(!StringUtil.isEmpty(examinationInfo)) {
				Map map = JSON.parseObject(examinationInfo);
				document.add(new Paragraph("5、检查填写",fontTitle));
				document.add(new Paragraph("（1）基本情况",FontChinese15));
				//创建表格对象
				PdfPTable t51 = new PdfPTable(4);
				t51.setSpacingBefore(15);
				t51.setSpacingAfter(15);
				t51.addCell(new Paragraph("基本信息",FontChinese13));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("姓名",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("name")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("name")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("体检编号",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("checkNum")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("checkNum")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("性别",font));
				if(String.valueOf(map.get("name")).equals("1")) {
					t51.addCell(new Paragraph("男",font));
				}else if(String.valueOf(map.get("name")).equals("2")) {
					t51.addCell(new Paragraph("女",font));
				}else {
					t51.addCell(new Paragraph("未知",font));
				}
				t51.addCell(new Paragraph("身份证号",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("cardNum")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("cardNum")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("婚姻状况",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("marriageStatus")))) {
					switch(String.valueOf(map.get("marriageStatus"))) {
					case "1": t51.addCell(new Paragraph("未婚",font));
					break;
					case "2": t51.addCell(new Paragraph("已婚",font));
					break;
					case "3": t51.addCell(new Paragraph("丧偶",font));
					break;
					case "4": t51.addCell(new Paragraph("离婚",font));
					break;
					case "5": t51.addCell(new Paragraph("未说明的婚姻状况",font));
					break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("职业",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("occupation")))) {
					t51.addCell(new Paragraph(DateUtil.OccupationMap.get(String.valueOf(map.get("occupation"))),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("体检日期",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("checkDate")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("checkDate")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("责任医生",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("signOrginName")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("signOrginName")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("症状",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("symptom")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("symptom")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("一般状况",FontChinese13));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("",FontChinese13));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("体温",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("tiwen")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("tiwen")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("脉率",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("mailv")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("mailv")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("呼吸频率",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("huxipinlv")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("huxipinlv")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("血压",font));
				//"bpLeftHei":"12","bpLeftlow":"12","bpRightHei":"12","bpRightlow":"12"
				if(!StringUtil.isEmpty(String.valueOf(map.get("bpLeftHei"))) ||!StringUtil.isEmpty(String.valueOf(map.get("bpLeftlow"))) 
						|| !StringUtil.isEmpty(String.valueOf(map.get("bpRightHei"))) || !StringUtil.isEmpty(String.valueOf(map.get("bpRightlow")))) {
					t51.addCell(new Paragraph("左侧：" + String.valueOf(map.get("bpLeftHei"))+ "/"+String.valueOf(map.get("bpLeftlow"))+
							"mmHg \n 右侧：" + String.valueOf(map.get("bpRightHei"))+ "/"+String.valueOf(map.get("bpRightlow"))+" mmHg",font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("身高",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("height")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("height"))+"cm",font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("体重",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("weight")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("weight"))+"kg",font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("腰围",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yaowei")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("yaowei"))+"cm",font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("体质指数(BMI)",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("BMI")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("BMI"))+"Kg/㎡",font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("老年人健康状态自我评估",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("statusCheck")))) {
					switch(String.valueOf(map.get("statusCheck"))) {
					case "1": t51.addCell(new Paragraph("满意",font));
					break;
					case "2": t51.addCell(new Paragraph("基本满意",font));
					break;
					case "3": t51.addCell(new Paragraph("说不清楚",font));
					break;
					case "4": t51.addCell(new Paragraph("不太清楚",font));
					break;
					case "5": t51.addCell(new Paragraph("不满意",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("老年人生活自理能力自我评估",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("ziliCheck")))) {
					switch(String.valueOf(map.get("ziliCheck"))) {
					case "1": t51.addCell(new Paragraph("满可自理（0~3分）",font));
					break;
					case "2": t51.addCell(new Paragraph("轻度依赖（4~8分）",font));
					break;
					case "3": t51.addCell(new Paragraph("中度依赖（9~18分）",font));
					break;
					case "4": t51.addCell(new Paragraph("不能自理（≥19分）",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("老年人认知功能",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("oldRenzhi")))) {
					switch(String.valueOf(map.get("oldRenzhi"))) {
					case "1": t51.addCell(new Paragraph("粗筛阴性",font));
					break;
					case "2": t51.addCell(new Paragraph("粗筛阳性",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("简易智力状态检查，总分",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("zhilifen")))) {
					t51.addCell(new Paragraph(String.valueOf(map.get("zhilifen")),font));
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("老年人情感状态",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("oldqinggan")))) {
					switch(String.valueOf(map.get("oldRenzhi"))) {
					case "1": t51.addCell(new Paragraph("粗筛阴性",font));
					break;
					case "2": t51.addCell(new Paragraph("粗筛阳性",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("老年人抑郁评分检查，总分",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("yiyufen")),font));
				t51.addCell(new Paragraph("生活方式",FontChinese13));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("",FontChinese13));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("锻炼频率",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("dlpl")))) {
					switch(String.valueOf(map.get("dlpl"))) {
					case "1": t51.addCell(new Paragraph("每天",font));
					break;
					case "2": t51.addCell(new Paragraph("每周一次",font));
					break;
					case "3": t51.addCell(new Paragraph("偶尔",font));
					break;
					case "4": t51.addCell(new Paragraph("不锻炼",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("每次锻炼时间",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("mzdlsj"))+"分钟",font));
				t51.addCell(new Paragraph("坚持锻炼时间",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("jcdlsj"))+"年",font));
				t51.addCell(new Paragraph("锻炼方式",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("dlfs")),font));
				t51.addCell(new Paragraph("饮食习惯",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("ysxg")),font));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("",font));
				t51.addCell(new Paragraph("吸烟状况",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("xyzk")))) {
					switch(String.valueOf(map.get("xyzk"))) {
					case "1": t51.addCell(new Paragraph("从不抽烟",font));
					break;
					case "2": t51.addCell(new Paragraph("已戒烟",font));
					break;
					case "3": t51.addCell(new Paragraph("吸烟",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("日吸烟量",font));
				t51.addCell(new Paragraph("评价"+String.valueOf(map.get("rxyl"))+"支",font));
				t51.addCell(new Paragraph("开始吸烟年龄",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("ksxynl"))+"岁",font));
				t51.addCell(new Paragraph("戒烟年龄",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("rxyl"))+"岁",font));
				t51.addCell(new Paragraph("饮酒频率",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yjpl")))) {
					switch(String.valueOf(map.get("yjpl"))) {
					case "1": t51.addCell(new Paragraph("从不",font));
					break;
					case "2": t51.addCell(new Paragraph("偶尔",font));
					break;
					case "3": t51.addCell(new Paragraph("经常",font));
					break;
					case "4": t51.addCell(new Paragraph("每天",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("日饮酒量",font));
				t51.addCell(new Paragraph("平均"+String.valueOf(map.get("ryjl"))+"两",font));
				t51.addCell(new Paragraph("开始饮酒年龄",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("ksyjnl"))+"岁",font));
				t51.addCell(new Paragraph("是否戒酒",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("sfjj")))) {
					switch(String.valueOf(map.get("sfjj"))) {
					case "1": t51.addCell(new Paragraph("未戒酒",font));
					break;
					case "2": t51.addCell(new Paragraph("已戒酒",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("开始戒酒年龄",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("ksjjnl"))+"岁",font));
				t51.addCell(new Paragraph("近一年内是否曾醉酒",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("jynnsfczj")))) {
					switch(String.valueOf(map.get("jynnsfczj"))) {
					case "1": t51.addCell(new Paragraph("否",font));
					break;
					case "2": t51.addCell(new Paragraph("是",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("饮酒种类",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("yjzl")),font));
				t51.addCell(new Paragraph("职业病危害因素接触史",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("zybwhysijs")))) {
					switch(String.valueOf(map.get("zybwhysijs"))) {
					case "1": t51.addCell(new Paragraph("否"+"(工种" + String.valueOf(map.get("gongzhong"))+"从业时间"+
					String.valueOf(map.get("cysj")) + "年)",font));
					break;
					case "2": t51.addCell(new Paragraph("是"+"(工种" + String.valueOf(map.get("gongzhong"))+"从业时间"+
							String.valueOf(map.get("cysj")) + "年)",font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph(""+"(工种" + String.valueOf(map.get("gongzhong"))+"从业时间"+
							String.valueOf(map.get("cysj")) + "年)",font));
				}
				t51.addCell(new Paragraph("毒物种类：	粉    尘",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("fenchen")),font));
				t51.addCell(new Paragraph("防护措施",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fenchen_fhcs")))) {
					switch(String.valueOf(map.get("fenchen_fhcs"))) {
					case "1": t51.addCell(new Paragraph("无",font));
					break;
					case "2": t51.addCell(new Paragraph("有"+String.valueOf(map.get("fswz_fhcs_desc")),font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("放射物质",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("fswz")),font));
				t51.addCell(new Paragraph("防护措施",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fswz_fhcs")))) {
					switch(String.valueOf(map.get("fenchen_fhcs"))) {
					case "1": t51.addCell(new Paragraph("无",font));
					break;
					case "2": t51.addCell(new Paragraph("有"+String.valueOf(map.get("wlys_fhcs_desc")),font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("物理因素",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("wlys")),font));
				t51.addCell(new Paragraph("防护措施",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("wlys_fhcs")))) {
					switch(String.valueOf(map.get("wlys_fhcs"))) {
					case "1": t51.addCell(new Paragraph("无",font));
					break;
					case "2": t51.addCell(new Paragraph("有"+String.valueOf(map.get("wlys_fhcs_desc")),font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("化学物质",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("hxwz")),font));
				t51.addCell(new Paragraph("防护措施",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("hxwz_fhcs")))) {
					switch(String.valueOf(map.get("hxwz_fhcs"))) {
					case "1": t51.addCell(new Paragraph("无",font));
					break;
					case "2": t51.addCell(new Paragraph("有"+String.valueOf(map.get("hxwz_fhcs_desc")),font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("毒    物",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("duwu")),font));
				t51.addCell(new Paragraph("防护措施",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fuwu_fhcs")))) {
					switch(String.valueOf(map.get("fuwu_fhcs"))) {
					case "1": t51.addCell(new Paragraph("无",font));
					break;
					case "2": t51.addCell(new Paragraph("有"+String.valueOf(map.get("fuwu_fhcs_desc")),font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				t51.addCell(new Paragraph("其    他",font));
				t51.addCell(new Paragraph(String.valueOf(map.get("others")),font));
				t51.addCell(new Paragraph("防护措施",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("others_fhcs")))) {
					switch(String.valueOf(map.get("others_fhcs"))) {
					case "1": t51.addCell(new Paragraph("无",font));
					break;
					case "2": t51.addCell(new Paragraph("有"+String.valueOf(map.get("others_fhcs_desc")),font));
					break;
					default:
						t51.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t51.addCell(new Paragraph("",font));
				}
				document.add(t51);

				document.add(new Paragraph("（2）检查项目",FontChinese15));
				//创建表格对象
				PdfPTable t52 = new PdfPTable(4);
				t52.setSpacingBefore(15);
				t52.setSpacingAfter(15);
				t52.addCell(new Paragraph("脏器功能",FontChinese13));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("口唇",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("kouchun")))) {
					switch(String.valueOf(map.get("kouchun"))) {
					case "1": t52.addCell(new Paragraph("红润",font));
					break;
					case "2": t52.addCell(new Paragraph("苍白",font));
					break;
					case "3": t52.addCell(new Paragraph("发干",font));
					break;
					case "4": t52.addCell(new Paragraph("皲裂",font));
					break;
					case "5": t52.addCell(new Paragraph("疱疹",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("咽部",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yanbu")))) {
					switch(String.valueOf(map.get("yanbu"))) {
					case "1": t52.addCell(new Paragraph("无充血",font));
					break;
					case "2": t52.addCell(new Paragraph("充血",font));
					break;
					case "3": t52.addCell(new Paragraph("淋巴滤泡增生",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("齿列",font));
				String chilie=String.valueOf(map.get("chilie"))+"\n"+" 缺齿："+String.valueOf(map.get("quechi1"))+String.valueOf(map.get("quechi2"))
				+String.valueOf(map.get("quechi3"))+String.valueOf(map.get("quechi4"))+"\n"+" 龋齿："+String.valueOf(map.get("quchi1"))+String.valueOf(map.get("quchi2"))
				+String.valueOf(map.get("quchi3"))+String.valueOf(map.get("quchi4"))+"\n"+" 义齿："+String.valueOf(map.get("yichijiaya1"))+String.valueOf(map.get("yichijiaya2"))
				+String.valueOf(map.get("yichijiaya3"))+String.valueOf(map.get("yichijiaya4"));
				t52.addCell(new Paragraph(chilie,font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("视力",font));
				t52.addCell(new Paragraph("左眼："+String.valueOf(map.get("leftshili"))+"右眼："+String.valueOf(map.get("rightshili")),font));
				t52.addCell(new Paragraph("矫正视力",font));
				t52.addCell(new Paragraph("左眼："+String.valueOf(map.get("rightjzshili"))+"右眼："+String.valueOf(map.get("rightjzshili")),font));
				t52.addCell(new Paragraph("听力",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("tingli")))) {
					switch(String.valueOf(map.get("tingli"))) {
					case "1": t52.addCell(new Paragraph("听见",font));
					break;
					case "2": t52.addCell(new Paragraph("听不见",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("运动功能",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("ydgn")))) {
					switch(String.valueOf(map.get("ydgn"))) {
					case "1": t52.addCell(new Paragraph("可顺利完成",font));
					break;
					case "2": t52.addCell(new Paragraph("无法独立完成其中任何一个动作",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("查体",FontChinese13));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",FontChinese13));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("眼底",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yandi")))) {
					switch(String.valueOf(map.get("yandi"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("yandi_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("yandi_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("虹膜",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("hongmo")))) {
					switch(String.valueOf(map.get("hongmo"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("hongmo_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("黄染"+String.valueOf(map.get("hongmo_desc")),font));
					break;
					case "3": t52.addCell(new Paragraph("充血"+String.valueOf(map.get("hongmo_desc")),font));
					break;
					case "4": t52.addCell(new Paragraph("其他"+String.valueOf(map.get("hongmo_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("皮肤",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("pifu")))) {
					switch(String.valueOf(map.get("pifu"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("pifu_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("潮红"+String.valueOf(map.get("pifu_desc")),font));
					break;
					case "3": t52.addCell(new Paragraph("苍白"+String.valueOf(map.get("pifu_desc")),font));
					break;
					case "4": t52.addCell(new Paragraph("发钳"+String.valueOf(map.get("pifu_desc")),font));
					break;
					case "5": t52.addCell(new Paragraph("黄染"+String.valueOf(map.get("pifu_desc")),font));
					break;
					case "6": t52.addCell(new Paragraph("色素沉着"+String.valueOf(map.get("pifu_desc")),font));
					break;
					case "7": t52.addCell(new Paragraph("其他"+String.valueOf(map.get("pifu_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("（肺）桶状胸",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fyzx")))) {
					switch(String.valueOf(map.get("fyzx"))) {
					case "1": t52.addCell(new Paragraph("否"+String.valueOf(map.get("fyzx_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("是"+String.valueOf(map.get("fyzx_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("淋巴结",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("linbaji")))) {
					switch(String.valueOf(map.get("linbaji"))) {
					case "1": t52.addCell(new Paragraph("未触及"+String.valueOf(map.get("linbaji_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("锁骨"+String.valueOf(map.get("linbaji_desc")),font));
					break;
					case "3": t52.addCell(new Paragraph("腋窝"+String.valueOf(map.get("linbaji_desc")),font));
					break;
					case "4": t52.addCell(new Paragraph("其他"+String.valueOf(map.get("linbaji_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("（肺）罗音",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fluoyin")))) {
					switch(String.valueOf(map.get("fluoyin"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("fluoyin_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("干罗音"+String.valueOf(map.get("fluoyin_desc")),font));
					break;
					case "3": t52.addCell(new Paragraph("湿罗音"+String.valueOf(map.get("fluoyin_desc")),font));
					break;
					case "4": t52.addCell(new Paragraph("其他"+String.valueOf(map.get("fluoyin_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("（肺）呼吸音",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fhxy")))) {
					switch(String.valueOf(map.get("fhxy"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("fhxy_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("fhxy_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph(" ",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph(" ",font));
				}
				t52.addCell(new Paragraph("心律",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("xinlv_select")))) {
					switch(String.valueOf(map.get("xinlv_select"))) {
					case "1": t52.addCell(new Paragraph("齐",font));
					break;
					case "2": t52.addCell(new Paragraph("不齐",font));
					break;
					case "3": t52.addCell(new Paragraph("绝对不齐",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph(" ",font));
				}
				t52.addCell(new Paragraph("心率",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("xinlv"))+"次",font));
				t52.addCell(new Paragraph("腹部压痛",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fbyt")))) {
					switch(String.valueOf(map.get("fbyt"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("fbyt_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("有"+String.valueOf(map.get("fbyt_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("杂音",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("zayin")))) {
					switch(String.valueOf(map.get("zayin"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("zayin_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("有"+String.valueOf(map.get("zayin_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("2",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("腹部肝大",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fbgd")))) {
					switch(String.valueOf(map.get("fbgd"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("fbgd_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("有"+String.valueOf(map.get("fbgd_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("腹部包块",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fbbk")))) {
					switch(String.valueOf(map.get("fbbk"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("fbbk_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("有"+String.valueOf(map.get("fbbk_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("腹部移动性浊音",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fbydzy")))) {
					switch(String.valueOf(map.get("fbydzy"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("fbydzy_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("有"+String.valueOf(map.get("fbydzy_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("腹部脾大",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fbpd")))) {
					switch(String.valueOf(map.get("fbpd"))) {
					case "1": t52.addCell(new Paragraph("无"+String.valueOf(map.get("fbpd_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("有"+String.valueOf(map.get("fbpd_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("糖尿病足背动脉搏动",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("tnbzbdmbd")))) {
					switch(String.valueOf(map.get("tnbzbdmbd"))) {
					case "1": t52.addCell(new Paragraph("未触及",font));
					break;
					case "2": t52.addCell(new Paragraph("触及双侧对称",font));
					break;
					case "3": t52.addCell(new Paragraph("触及左侧弱或消失",font));
					break;
					case "4": t52.addCell(new Paragraph("及右侧弱或消失",font));
					break;
					case "5": t52.addCell(new Paragraph("触及正常",font));
					break;
					case "6": t52.addCell(new Paragraph("减弱双侧",font));
					break;
					case "7": t52.addCell(new Paragraph("弱左侧",font));
					break;
					case "8": t52.addCell(new Paragraph("减弱右侧",font));
					break;
					case "9": t52.addCell(new Paragraph("消失双侧",font));
					break;
					case "10": t52.addCell(new Paragraph("消失左侧",font));
					break;
					case "11": t52.addCell(new Paragraph("消失右侧",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("下肢水肿",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("xzsz")))) {
					switch(String.valueOf(map.get("xzsz"))) {
					case "1": t52.addCell(new Paragraph("无",font));
					break;
					case "2": t52.addCell(new Paragraph("单侧",font));
					break;
					case "3": t52.addCell(new Paragraph("双侧不对称",font));
					break;
					case "4": t52.addCell(new Paragraph("双侧对称",font));
					break;
					default:
						t52.addCell(new Paragraph("2",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("乳腺",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("ruxian")),font));
				t52.addCell(new Paragraph("肛门指诊",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("gmzz")))) {
					switch(String.valueOf(map.get("gmzz"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("gmzz_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("触痛"+String.valueOf(map.get("gmzz_desc")),font));
					break;
					case "3": t52.addCell(new Paragraph("包块"+String.valueOf(map.get("gmzz_desc")),font));
					break;
					case "4": t52.addCell(new Paragraph("前列腺异常"+String.valueOf(map.get("gmzz_desc")),font));
					break;
					case "5": t52.addCell(new Paragraph("其他"+String.valueOf(map.get("gmzz_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("阴道",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yindao")))) {
					switch(String.valueOf(map.get("yindao"))) {
					case "1": t52.addCell(new Paragraph("未见异常"+String.valueOf(map.get("yindao_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("yindao_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("（妇科）外阴",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fkwy")))) {
					switch(String.valueOf(map.get("fkwy"))) {
					case "1": t52.addCell(new Paragraph("未见异常"+String.valueOf(map.get("fkwy_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("fkwy_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("宫体",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("gongti")))) {
					switch(String.valueOf(map.get("gongti"))) {
					case "1": t52.addCell(new Paragraph("未见异常"+String.valueOf(map.get("gongti_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("gongti_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("宫颈",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("gongjing")))) {
					switch(String.valueOf(map.get("gongjing"))) {
					case "1": t52.addCell(new Paragraph("未见异常"+String.valueOf(map.get("gongjing_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("gongjing_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("附件",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("fujian")))) {
					switch(String.valueOf(map.get("fujian"))) {
					case "1": t52.addCell(new Paragraph("未见异常"+String.valueOf(map.get("fujian_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("fujian_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("查体其他",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("chatiqita")),font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("辅助检查",FontChinese13));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("血常规",font));
				String xuechanggui = "血红蛋白："+String.valueOf(map.get("blood_xhdb"))+"g/L 白细胞："+String.valueOf(map.get("blood_bxb"))
				+"x10`9/L 血小块："+String.valueOf(map.get("blood_xxb"))+"x10`9/L 其他："+String.valueOf(map.get("blood_qita"));
				t52.addCell(new Paragraph(xuechanggui,font));
				
				t52.addCell(new Paragraph("尿常规",font));
				String ncg_ndb ="";
				String ncg_nt ="";
				String ncg_ntt ="";
				String ncg_nqx ="";
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
				if(!StringUtil.isEmpty(String.valueOf(map.get("ncg_ntt")))) {
					switch(String.valueOf(map.get("ncg_ntt"))) {
					case "1": ncg_ntt = "-";
					break;
					case "2": ncg_ntt = "微量";
					break;
					case "3": ncg_ntt = "+";
					break;
					case "4": ncg_ntt = "++";
					break;
					case "5": ncg_ntt = "+++";
					break;
					case "6": ncg_ntt = "++++";
					break;
					default:
						ncg_ntt = "";
						break;
					}
				}
				if(!StringUtil.isEmpty(String.valueOf(map.get("ncg_nqx")))) {
					switch(String.valueOf(map.get("ncg_nqx"))) {
					case "1": ncg_nqx = "-";
					break;
					case "2": ncg_nqx = "微量";
					break;
					case "3": ncg_nqx = "+";
					break;
					case "4": ncg_nqx = "++";
					break;
					case "5": ncg_nqx = "+++";
					break;
					case "6": ncg_nqx = "++++";
					break;
					default:
						ncg_nqx = "";
						break;
					}
				}
				String niaochanggui = "尿蛋白："+ncg_ndb+"  尿糖："+ncg_nt
				+" 尿酮体："+ncg_ntt+"  尿潜血："+ncg_nqx
				+" 其他："+String.valueOf(map.get("ncg_qita"));
				t52.addCell(new Paragraph(niaochanggui,font));
				t52.addCell(new Paragraph("空腹血糖",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("kfxt"))+"mmol/L",font));
				t52.addCell(new Paragraph("心电图",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("xindiantu")))) {
					switch(String.valueOf(map.get("xindiantu"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("xindiantu_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("xindiantu_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("尿微量白蛋白",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("nwlxdb"))+"mg/dL",font));
				t52.addCell(new Paragraph("大便潜血",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("dbqx")))) {
					switch(String.valueOf(map.get("dbqx"))) {
					case "1": t52.addCell(new Paragraph("阴性",font));
					break;
					case "2": t52.addCell(new Paragraph("阳性",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("糖化血红蛋白",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("thxhdb"))+"%",font));
				t52.addCell(new Paragraph("乙型肝炎表面抗体（HbsAg）",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("HBsAg")))) {
					switch(String.valueOf(map.get("HBsAg"))) {
					case "1": t52.addCell(new Paragraph("阴性",font));
					break;
					case "2": t52.addCell(new Paragraph("阳性",font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("肝功能",font));
				String gangongneng ="血清谷丙转氨酶(ALT):"+String.valueOf(map.get("ggn_ALT"))+" 血清谷草转氨酶(AST):"+String.valueOf(map.get("ggn_AST"))+" 白蛋白(ALBT):"
				+String.valueOf(map.get("ggn_ALBT"))+" \n 总胆红素(TBIL):"+String.valueOf(map.get("ggn_DBIL"))+" 结合胆红素(DBIL):"+String.valueOf(map.get("ggn_DBIL"));
				t52.addCell(new Paragraph(gangongneng,font));
				t52.addCell(new Paragraph("肾功能",font));
				String shenongneng ="血清肌酐:"+String.valueOf(map.get("sgn_xqjg"))+" 血尿素氮:"+String.valueOf(map.get("sgn_xnsa"))+" \n 钾浓度:"
						+String.valueOf(map.get("sgn_xjnd"))+" 血钠浓度:"+String.valueOf(map.get("ggn_xnnd"));
				t52.addCell(new Paragraph(shenongneng,font));
				t52.addCell(new Paragraph("血脂",font));
				String xuezhi ="总胆固醇(CHO):"+String.valueOf(map.get("xz_CHO"))+" 甘油三酯(TG):"+String.valueOf(map.get("xz_TG"))+" \n 血清低密度脂蛋白胆固醇(LDL-C):"
				+String.valueOf(map.get("xz_LDL-C"))+" 血清高密度脂蛋白胆固醇(HDL-C):"+String.valueOf(map.get("xz_HDL-C"));
				t52.addCell(new Paragraph(xuezhi,font));
				t52.addCell(new Paragraph("胸片",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("xiongpian")))) {
					switch(String.valueOf(map.get("xiongpian"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("xiongpian_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("xiongpian_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("B超",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("Bchao")))) {
					switch(String.valueOf(map.get("Bchao"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("Bchao_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("Bchao_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("宫颈涂片",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("gjtp")))) {
					switch(String.valueOf(map.get("gjtp"))) {
					case "1": t52.addCell(new Paragraph("正常"+String.valueOf(map.get("gjtp_desc")),font));
					break;
					case "2": t52.addCell(new Paragraph("异常"+String.valueOf(map.get("gjtp_desc")),font));
					break;
					default:
						t52.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t52.addCell(new Paragraph("",font));
				}
				t52.addCell(new Paragraph("（健康辅助检查）其他",font));
				t52.addCell(new Paragraph(String.valueOf(map.get("jkfzjc_other")),font));
				t52.addCell(new Paragraph("",font));
				t52.addCell(new Paragraph("",font));
				document.add(t52);

				document.add(new Paragraph("（3）其他",FontChinese15));
				//创建表格对象
				PdfPTable t53 = new PdfPTable(4);
				t53.setSpacingBefore(15);
				t53.setSpacingAfter(15);
				t53.addCell(new Paragraph("现存主要健康问题",FontChinese13));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("脑血管疾病",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("nxgjb")),font));
				t53.addCell(new Paragraph("肾脏疾病",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("szjb")),font));
				t53.addCell(new Paragraph("心脏疾病",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("xzjb")),font));
				t53.addCell(new Paragraph("血管疾病",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("xgjb")),font));
				t53.addCell(new Paragraph("眼部疾病",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("ybjb")),font));
				t53.addCell(new Paragraph("神经系统疾病",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("sjxtjb")))) {
					switch(String.valueOf(map.get("sjxtjb"))) {
					case "1": t53.addCell(new Paragraph("未发现"+String.valueOf(map.get("sjxtjb_desc")),font));
					break;
					case "2": t53.addCell(new Paragraph("有"+String.valueOf(map.get("sjxtjb_desc")),font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("其他系统疾病",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("qtxtjb")))) {
					switch(String.valueOf(map.get("qtxtjb"))) {
					case "1": t53.addCell(new Paragraph("未发现"+String.valueOf(map.get("qtxtjb_desc")),font));
					break;
					case "2": t53.addCell(new Paragraph("有"+String.valueOf(map.get("qtxtjb_desc")),font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("住院医疗情况",FontChinese13));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",FontChinese13));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("（住院史）入/出院时间",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("zys_enterTime1"))+"/"+String.valueOf(map.get("zys_exitTime1")),font));
				t53.addCell(new Paragraph("原因",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("zys_reason1")),font));
				t53.addCell(new Paragraph("医疗机构名称",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_yljgmc1")),font));
				t53.addCell(new Paragraph("病案号",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_bah1")),font));
				t53.addCell(new Paragraph("（住院史）入/出院时间",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("zys_enterTime2"))+"/"+String.valueOf(map.get("zys_exitTime2")),font));
				t53.addCell(new Paragraph("原因",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("zys_reason2")),font));
				t53.addCell(new Paragraph("医疗机构名称",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_yljgmc2")),font));
				t53.addCell(new Paragraph("病案号",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_bah2")),font));
				t53.addCell(new Paragraph("（家庭病床史）建/撤床时间",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_enterTime1"))+"/"+String.valueOf(map.get("jtbcs_exitTime1")),font));
				t53.addCell(new Paragraph("原因",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_reason1")),font));
				t53.addCell(new Paragraph("医疗机构名称",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_yljgmc1")),font));
				t53.addCell(new Paragraph("病案号",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_bah1")),font));
				t53.addCell(new Paragraph("（家庭病床史）建/撤床时间",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_enterTime2"))+"/"+String.valueOf(map.get("jtbcs_exitTime2")),font));
				t53.addCell(new Paragraph("原因",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_reason2")),font));
				t53.addCell(new Paragraph("医疗机构名称",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_yljgmc2")),font));
				t53.addCell(new Paragraph("病案号",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jtbcs_bah2")),font));
				t53.addCell(new Paragraph("主要用药情况",FontChinese13));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",FontChinese13));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("药物1",font));
				String yaowu1=String.valueOf(map.get("yongyao_name1")) +" 用法："+String.valueOf(map.get("yongyao_yongfa1"))+
						" 每次"+String.valueOf(map.get("yongyao_meici1_1"))+" "+String.valueOf(map.get("yongyao_meici1_2"))+
						" 每天" +String.valueOf(map.get("yongyao_meitian1"))+"次"+" 用药时间"+String.valueOf(map.get("yongyao_yysj1"));
				String yaowu2=String.valueOf(map.get("yongyao_name2")) +" 用法："+String.valueOf(map.get("yongyao_yongfa2"))+
						" 每次"+String.valueOf(map.get("yongyao_meici2_1"))+" "+String.valueOf(map.get("yongyao_meici2_2"))+
						" 每天" +String.valueOf(map.get("yongyao_meitian2"))+"次"+"  用药时间"+String.valueOf(map.get("yongyao_yysj2"));
				String yaowu3=String.valueOf(map.get("yongyao_name3")) +" 用法："+String.valueOf(map.get("yongyao_yongfa3"))+
						" 每次"+String.valueOf(map.get("yongyao_meici3_1"))+" "+String.valueOf(map.get("yongyao_meici3_2"))+
						" 每天" +String.valueOf(map.get("yongyao_meitian3"))+"次"+"  用药时间"+String.valueOf(map.get("yongyao_yysj3"));
				String yaowu4=String.valueOf(map.get("yongyao_name4")) +" 用法："+String.valueOf(map.get("yongyao_yongfa4"))+
						" 每次"+String.valueOf(map.get("yongyao_meici4_1"))+" "+String.valueOf(map.get("yongyao_meici4_2"))+
						" 每天" +String.valueOf(map.get("yongyao_meitian4"))+"次"+"  用药时间"+String.valueOf(map.get("yongyao_yysj4"));
				String yaowu5=String.valueOf(map.get("yongyao_name5")) +" 用法："+String.valueOf(map.get("yongyao_yongfa5"))+
						" 每次"+String.valueOf(map.get("yongyao_meici5_1"))+" "+String.valueOf(map.get("yongyao_meici5_2"))+
						" 每天" +String.valueOf(map.get("yongyao_meitian5"))+"次"+"  用药时间"+String.valueOf(map.get("yongyao_yysj5"));
				String yaowu6=String.valueOf(map.get("yongyao_name6")) +" 用法："+String.valueOf(map.get("yongyao_yongfa6"))+
						" 每次"+String.valueOf(map.get("yongyao_meici6_1"))+" "+String.valueOf(map.get("yongyao_meici6_2"))+
						" 每天" +String.valueOf(map.get("yongyao_meitian6"))+"次"+"  用药时间"+String.valueOf(map.get("yongyao_yysj6"));
				
				t53.addCell(new Paragraph(yaowu1,font));
				t53.addCell(new Paragraph("服药依从性",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yongyao_fyycx1")))) {
					switch(String.valueOf(map.get("yongyao_fyycx1"))) {
					case "1": t53.addCell(new Paragraph("规律服药",font));
					break;
					case "2": t53.addCell(new Paragraph("间断服药",font));
					break;
					case "3": t53.addCell(new Paragraph("不服药",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}

				t53.addCell(new Paragraph("药物2",font));
				t53.addCell(new Paragraph(yaowu2,font));
				t53.addCell(new Paragraph("服药依从性",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yongyao_fyycx2")))) {
					switch(String.valueOf(map.get("yongyao_fyycx2"))) {
					case "1": t53.addCell(new Paragraph("规律服药",font));
					break;
					case "2": t53.addCell(new Paragraph("简短服药",font));
					break;
					case "3": t53.addCell(new Paragraph("不服药",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("药物3",font));
				t53.addCell(new Paragraph(yaowu3,font));
				t53.addCell(new Paragraph("服药依从性",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yongyao_fyycx3")))) {
					switch(String.valueOf(map.get("yongyao_fyycx3"))) {
					case "1": t53.addCell(new Paragraph("规律服药",font));
					break;
					case "2": t53.addCell(new Paragraph("简短服药",font));
					break;
					case "3": t53.addCell(new Paragraph("不服药",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("药物4",font));
				t53.addCell(new Paragraph(yaowu4,font));
				t53.addCell(new Paragraph("服药依从性",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yongyao_fyycx4")))) {
					switch(String.valueOf(map.get("yongyao_fyycx4"))) {
					case "1": t53.addCell(new Paragraph("规律服药",font));
					break;
					case "2": t53.addCell(new Paragraph("简短服药",font));
					break;
					case "3": t53.addCell(new Paragraph("不服药",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("药物5",font));
				t53.addCell(new Paragraph(yaowu5,font));
				t53.addCell(new Paragraph("服药依从性",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yongyao_fyycx5")))) {
					switch(String.valueOf(map.get("yongyao_fyycx5"))) {
					case "1": t53.addCell(new Paragraph("规律服药",font));
					break;
					case "2": t53.addCell(new Paragraph("简短服药",font));
					break;
					case "3": t53.addCell(new Paragraph("不服药",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("药物6",font));
				t53.addCell(new Paragraph(yaowu6,font));
				t53.addCell(new Paragraph("服药依从性",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("yongyao_fyycx6")))) {
					switch(String.valueOf(map.get("yongyao_fyycx6"))) {
					case "1": t53.addCell(new Paragraph("规律服药",font));
					break;
					case "2": t53.addCell(new Paragraph("简短服药",font));
					break;
					case "3": t53.addCell(new Paragraph("不服药",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("费免疫规划预防接种史",FontChinese13));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("疫苗名称1",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_ymmc1")),font));
				t53.addCell(new Paragraph("接种日期",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_jzrq1")),font));
				t53.addCell(new Paragraph("接种机构",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_jzjg1")),font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("疫苗名称2",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_ymmc2")),font));
				t53.addCell(new Paragraph("接种日期",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_jzrq2")),font));
				t53.addCell(new Paragraph("接种机构",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_jzjg2")),font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("疫苗名称3",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_ymmc3")),font));
				t53.addCell(new Paragraph("接种日期",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_jzrq3")),font));
				t53.addCell(new Paragraph("接种机构",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("yimiao_jzjg3")),font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("",font));
				t53.addCell(new Paragraph("健康评价",font));
				if(!StringUtil.isEmpty(String.valueOf(map.get("jkpj")))) {
					switch(String.valueOf(map.get("jkpj"))) {
					case "1": t53.addCell(new Paragraph("年检无异常",font));
					break;
					case "2": t53.addCell(new Paragraph("有异常",font));
					break;
					case "3": t53.addCell(new Paragraph("体检无异常",font));
					break;
					default:
						t53.addCell(new Paragraph("",font));
						break;
					}
				}else {
					t53.addCell(new Paragraph("",font));
				}
				t53.addCell(new Paragraph("异常",font));
				t53.addCell(new Paragraph("异常1"+String.valueOf(map.get("jkpj_yichang1"))+"  异常2"+String.valueOf(map.get("jkpj_yichang2"))
				+" 异常3"+String.valueOf(map.get("jkpj_yichang3"))+" 异常4"+String.valueOf(map.get("jkpj_yichang4")),font));
				t53.addCell(new Paragraph("健康指导",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("jkzd")),font));
				t53.addCell(new Paragraph("危险因素控制",font));
				t53.addCell(new Paragraph(String.valueOf(map.get("wxyskz")),font));
				document.add(t53);
			}
			document.close();
			os.flush();
			os.close();
		}catch(Exception e) {
			System.out.println("false");
			b = false;
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("succee");
		if(!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成pdf文档失败"));
		}
		return result;
	}

}
