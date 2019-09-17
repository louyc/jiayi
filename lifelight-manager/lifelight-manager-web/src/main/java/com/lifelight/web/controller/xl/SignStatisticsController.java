package com.lifelight.web.controller.xl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.vo.SignStatisticsVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.service.XlPersonContractService;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping("/statistics")
public class SignStatisticsController {

	private static final Logger logger = LoggerFactory.getLogger(SignStatisticsController.class);
	@Reference
	private XlPersonContractService personContractService;

	/* 服务完成率统计 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/serviceStatistics")
	@ResponseBody
	public Result serviceStatistics(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "doctorId", required = false) String doctorId,
			@RequestParam(value = "orgId", required = false) String orgId) {
		logger.info("SignStatisticsController.serviceStatistics start, userInfo = {}",
				startDate+"   "+endDate);
		return personContractService.serviceStatistics(startDate,endDate,doctorId,orgId);
	}
	/* 服务完成率统计导出 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/exportServiceStatistics")
	@ResponseBody
	public Result exportServiceStatistics(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "doctorId", required = false) String doctorId,
			@RequestParam(value = "orgId", required = false) String orgId) {
		logger.info("SignStatisticsController.exportServiceStatistics start, userInfo = {}",
				startDate+"   "+endDate);
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "签约服务完成率统计（汇总）";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
//		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		result = personContractService.serviceStatistics(startDate,endDate,doctorId,orgId);
		List<SignStatisticsVO> lists = (List<SignStatisticsVO>)(result.getData());
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1")+".xls");
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableFont wf_head = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);//定义格式 字体 下划线 斜体 粗体 颜色
			WritableCellFormat wcf_head = new WritableCellFormat(wf_head);
			wcf_head.setAlignment(jxl.format.Alignment.CENTRE);
			
			WritableFont wf_merge1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD,  
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);  
			WritableCellFormat wf = new WritableCellFormat(wf_merge1);
			WritableCellFormat wfLeft = new WritableCellFormat();
			wfLeft.setAlignment(Alignment.LEFT);

			WritableSheet sheet = book.createSheet("签约服务完成率统计（汇总）", 0);
			// 2.添加表头数据
			// 列 行  到 列 行
			sheet.mergeCells(0, 0, 34, 0);
			sheet.mergeCells(0, 1, 34, 1);
			sheet.mergeCells(0, 2, 0, 3);
			sheet.mergeCells(1, 2, 1, 3);
			sheet.mergeCells(2, 2, 2, 3);
			sheet.mergeCells(3, 2, 3, 3);
			sheet.mergeCells(4, 2, 4, 3);
			sheet.mergeCells(0, 6, 34, 6);
			Label content1=new Label(0,0,"签约服务完成率统计（汇总）",wcf_head); 
			Label content2=new Label(0,1,"________年",wcf_head); 

			sheet.addCell(new Label(0,2,"分组",wcf_head));
			sheet.addCell(new Label(1,2,"机构名称",wcf_head));
			sheet.addCell(new Label(2,2,"有偿签约包数",wcf_head));
			sheet.addCell(new Label(3,2,"已完成数(包)",wcf_head));
			sheet.addCell(new Label(4,2,"服务包完成率(%)",wcf_head));
			sheet.addCell(new Label(0,6,"实际报出日期__________年___月___日    "
					+ "    单位负责人签名:______________  填表人签名:___________",wfLeft));
			sheet.addCell(content2);
			sheet.addCell(content1);

			if(null!=lists && lists.size()>0) {
				sheet.addCell(new Label(0,4,lists.get(0).getOrgName(),wf));
				sheet.addCell(new Label(1,4,"小计",wf));
				sheet.addCell(new Label(2,4,String.valueOf(lists.get(0).getCountSum()),wf));
				sheet.addCell(new Label(3,4,String.valueOf(lists.get(0).getFinishSum()),wf));
				sheet.addCell(new Label(4,4,String.valueOf(lists.get(0).getFinishRate()),wf));
				sheet.addCell(new Label(1,5,"合计",wf));
				sheet.addCell(new Label(2,5,String.valueOf(lists.get(0).getCountSum()),wf));
				sheet.addCell(new Label(3,5,String.valueOf(lists.get(0).getFinishSum()),wf));
				sheet.addCell(new Label(4,5,String.valueOf(lists.get(0).getFinishRate()),wf));

				for(int i=0;i<lists.get(0).getList().size();i++) {
					sheet.mergeCells(5+i*3, 2, 5+i*3+2, 2);
					sheet.addCell(new Label(5+i*3,2,lists.get(0).getList().get(i).getItemName(),wcf_head));
					sheet.addCell(new Label(5+i*3,3,"有偿签约数",wcf_head));
					sheet.addCell(new Label(5+i*3+1,3,"已完成数(个)",wcf_head));
					sheet.addCell(new Label(5+i*3+2,3,"执行率(%)",wcf_head));

					sheet.addCell(new Label(5+i*3,4,String.valueOf(
							lists.get(0).getList().get(i).getCountSum()),wf));
					sheet.addCell(new Label(5+i*3+1,4,String.valueOf(
							lists.get(0).getList().get(i).getFinishSum()),wf));
					if(StringUtil.isEmpty(lists.get(0).getList().get(i).getFinishRate())) {
						sheet.addCell(new Label(5+i*3+2,4,"",wf));
					}else {
						sheet.addCell(new Label(5+i*3+2,4,String.valueOf(
								lists.get(0).getList().get(i).getFinishRate()),wf));
					}

					sheet.addCell(new Label(5+i*3,5,String.valueOf(
							lists.get(0).getList().get(i).getCountSum()),wf));
					sheet.addCell(new Label(5+i*3+1,5,String.valueOf(
							lists.get(0).getList().get(i).getFinishSum()),wf));
					if(StringUtil.isEmpty(lists.get(0).getList().get(i).getFinishRate())) {
						sheet.addCell(new Label(5+i*3+2,5,"",wf));
					}else {
						sheet.addCell(new Label(5+i*3+2,5,String.valueOf(
								lists.get(0).getList().get(i).getFinishRate()),wf));
					}
				}
			}
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/* 项目执行进度汇总 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/executeStatistics")
	@ResponseBody
	public Result executeStatistics(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "doctorId", required = false) String doctorId,
			@RequestParam(value = "orgId", required = false) String orgId) {
		logger.info("SignStatisticsController.executeStatistics start, userInfo = {}",
				startDate+"   "+endDate);
		return personContractService.executeStatistics(startDate,endDate,doctorId,orgId);
	}

	/* 项目执行进度汇总导出 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/exportExecuteStatistics")
	@ResponseBody
	public Result exportExecuteStatistics(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "doctorId", required = false) String doctorId,
			@RequestParam(value = "orgId", required = false) String orgId) {
		logger.info("SignStatisticsController.exportExecuteStatistics start, userInfo = {}",
				startDate+"   "+endDate);
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "签约服务项目执行进度统计（汇总）";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		result = personContractService.executeStatistics(startDate,endDate,doctorId,orgId);
		List<SignStatisticsVO> lists = (List<SignStatisticsVO>)(result.getData());
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1")+".xls");
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableFont wf_head = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);//定义格式 字体 下划线 斜体 粗体 颜色
			WritableCellFormat wcf_head = new WritableCellFormat(wf_head);
			wcf_head.setAlignment(jxl.format.Alignment.CENTRE);
			
			WritableFont wf_merge1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD,  
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);  
			WritableCellFormat wf = new WritableCellFormat(wf_merge1);
			WritableCellFormat wfLeft = new WritableCellFormat();
			wfLeft.setAlignment(Alignment.LEFT);

			WritableSheet sheet = book.createSheet("签约服务项目执行进度统计（汇总）", 0);
			// 2.添加表头数据
			// 列 行  到 列 行
			sheet.mergeCells(0, 0, 21, 0);
			sheet.mergeCells(0, 1, 21, 1);
			sheet.mergeCells(0, 2, 0, 3);
			sheet.mergeCells(1, 2, 1, 3);
			sheet.mergeCells(2, 2, 2, 3);
			sheet.mergeCells(3, 2, 3, 3);
			sheet.mergeCells(4, 2, 4, 3);
			sheet.mergeCells(5, 2, 5, 3);
			sheet.mergeCells(0, 6, 45, 6);
			Label content1=new Label(0,0,"签约服务项目执行进度统计（汇总）",wcf_head); 
			Label content2=new Label(0,1,"________年",wcf_head); 

			sheet.addCell(new Label(0,2,"分组",wcf_head));
			sheet.addCell(new Label(1,2,"机构名称",wcf_head));
			sheet.addCell(new Label(2,2,"有偿签约包数",wcf_head));
			sheet.addCell(new Label(3,2,"内含项目总数",wcf_head));
			sheet.addCell(new Label(4,2,"执行项目总数",wcf_head));
			sheet.addCell(new Label(5,2,"执行率(%)",wcf_head));
			sheet.addCell(new Label(0,6,"实际报出日期__________年___月___日    "
					+ "    单位负责人签名:______________  填表人签名:___________",wfLeft));
			sheet.addCell(content2);
			sheet.addCell(content1);

			if(null!=lists && lists.size()>0) {
				sheet.addCell(new Label(0,4,lists.get(0).getOrgName(),wf));
				sheet.addCell(new Label(1,4,"小计",wf));
				sheet.addCell(new Label(2,4,String.valueOf(lists.get(0).getCountSum()),wf));
				sheet.addCell(new Label(3,4,String.valueOf(lists.get(0).getCountItemSum()),wf));
				sheet.addCell(new Label(4,4,String.valueOf(lists.get(0).getFinishItemSum()),wf));
				sheet.addCell(new Label(5,4,String.valueOf(lists.get(0).getFinishRate()),wf));
				sheet.addCell(new Label(1,5,"合计",wf));
				sheet.addCell(new Label(2,5,String.valueOf(lists.get(0).getCountSum()),wf));
				sheet.addCell(new Label(3,5,String.valueOf(lists.get(0).getCountItemSum()),wf));
				sheet.addCell(new Label(4,5,String.valueOf(lists.get(0).getFinishItemSum()),wf));
				sheet.addCell(new Label(5,5,String.valueOf(lists.get(0).getFinishRate()),wf));

				for(int i=0;i<lists.get(0).getList().size();i++) {
					sheet.mergeCells(6+i*4, 2, 6+i*4+3, 2);
					sheet.addCell(new Label(6+i*4,2,lists.get(0).getList().get(i).getItemName(),wcf_head));
					sheet.addCell(new Label(6+i*4,3,"有偿签约数",wcf_head));
					sheet.addCell(new Label(6+i*4+1,3,"内含项目总数(个)",wcf_head));
					sheet.addCell(new Label(6+i*4+2,3,"执行项目总数(个)",wcf_head));
					sheet.addCell(new Label(6+i*4+3,3,"执行率(%)",wcf_head));

					sheet.addCell(new Label(6+i*4,4,String.valueOf(
							lists.get(0).getList().get(i).getCountSum()),wf));
					sheet.addCell(new Label(6+i*4+1,4,String.valueOf(
							lists.get(0).getList().get(i).getCountItemSum()),wf));
					sheet.addCell(new Label(6+i*4+2,4,String.valueOf(
							lists.get(0).getList().get(i).getFinishItemSum()),wf));
					if(StringUtil.isEmpty(lists.get(0).getList().get(i).getFinishRate())) {
						sheet.addCell(new Label(6+i*4+3,4,"",wf));
					}else {
						sheet.addCell(new Label(6+i*4+3,4,String.valueOf(
								lists.get(0).getList().get(i).getFinishRate()),wf));
					}

					sheet.addCell(new Label(6+i*4,5,String.valueOf(
							lists.get(0).getList().get(i).getCountSum()),wf));
					sheet.addCell(new Label(6+i*4+1,5,String.valueOf(
							lists.get(0).getList().get(i).getCountItemSum()),wf));
					sheet.addCell(new Label(6+i*4+2,5,String.valueOf(
							lists.get(0).getList().get(i).getFinishItemSum()),wf));
					if(StringUtil.isEmpty(lists.get(0).getList().get(i).getFinishRate())) {
						sheet.addCell(new Label(6+i*4+3,5,"",wf));
					}else {
						sheet.addCell(new Label(6+i*4+3,5,String.valueOf(
								lists.get(0).getList().get(i).getFinishRate()),wf));
					}
				}
			}
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}
}
