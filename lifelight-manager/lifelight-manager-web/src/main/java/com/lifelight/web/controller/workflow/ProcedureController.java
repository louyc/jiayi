package com.lifelight.web.controller.workflow;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lifelight.api.entity.Procedure;
import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.DeviceDataService;
import com.lifelight.dubbo.service.ProcedureMessageService;
import com.lifelight.dubbo.service.ProcedureService;
import com.lifelight.web.job.ProcedureMessageJob;
import com.lifelight.web.job.QuartzManager;
import com.lifelight.web.service.WeixinService;

@Controller
@RequestMapping("/procedure")
public class ProcedureController {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureController.class);

	@Resource
	private WeixinService weixinService;
	@Autowired
	private QuartzManager quartzManager;

	@Reference
	private ProcedureService procedureService;
	@Reference
	private ProcedureMessageService procedureMessageService;
	@Reference
	private DeviceDataService deviceDataService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/create")
	@ResponseBody
	public Result createProcedure(@RequestBody Procedure procedure) {
		logger.info("创建流程：{}", procedure.toString());
		return procedureService.createProcedure(procedure);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteProcedure(@RequestParam("id") String id) {
		logger.info("删除id= {} 流程", id);
		return procedureService.deleteProcedure(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/update")
	@ResponseBody
	public Result updateProcedure(@RequestBody Procedure procedure) {
		logger.info("修改流程：{}", procedure.toString());
		return procedureService.updateProcedure(procedure);
	}

	@RequestMapping("/getOne")
	@ResponseBody
	public Result<Procedure> getProcedureById(@RequestParam("id") String id) {
		logger.info("查询id= {} 流程", id);
		return procedureService.getProcedureById(id);
	}

	/**
	 * 分页 样例 pageSize 分页大小 currentsPage 当前页数
	 * 
	 * selectProcedureListPage
	 * 
	 * @param procedure
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getPageProcedures")
	@ResponseBody
	public Result getPageProcedures(@RequestBody Procedure procedure) {
		logger.info("查询流程列表 分页：{}", procedure.toString());
		return procedureService.selectProcedureListPage(procedure);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/share")
	@ResponseBody
	public Result shareProcedure(@RequestParam("id") String id, @RequestParam("users") String users) {
		logger.info("将id为 " + id + " 的流程分享给 " + users);
		return procedureService.shareProcedure(id, users);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getBesharedUsers")
	@ResponseBody
	public Result getBesharedUsers(@RequestParam("procedureId") String procedureId) {
		logger.info("查询 procedureId 为 " + procedureId + " 的流程分享到的用户");
		return procedureService.getBesharedUsers(procedureId);
	}

	@RequestMapping("/getNextStep")
	@ResponseBody
	public Result<String> getNextStep(HttpServletRequest request, @RequestParam("id") String id,
			@RequestParam("key") String key, @RequestParam("procedureId") String procedureId,
			@RequestParam("managerId") String managerId, @RequestParam(value = "data", required = false) String data) {
		logger.info("获取id为 " + id + " 的流程的下一步 " + key);
		String domainName = request.getServerName();
		Result<String> result = procedureService.getNextStep(id, key, procedureId, managerId, data, domainName);
		logger.info("查询成功：{}", result.getData());

		if (!"[]".equals(result.getData())) {
			JSONObject node = JSONArray.parseArray(result.getData()).getJSONObject(0);
			if (node.getInteger("propertyType") == 5) {
				String item = node.getJSONArray("stepData").getJSONObject(0).getString("id");
				Result measureData = deviceDataService.getMeasureDataByWorkFollow(managerId, item);
				if (measureData.getData() != null)
					node.getJSONArray("stepData").getJSONObject(0).getJSONObject("childData").put("expression",
							measureData.getData().toString());
				result.setData("[" + node.toJSONString() + "]");
			}
		}

		JSONArray resultData = new JSONArray();
		if (result.getResultCode() != null && result.getResultCode().getCode().equals("NODE_NULL")) {
			logger.info(result.getResultCode().getMessage());
			ProcedureMessage procedureMessage = new ProcedureMessage();
			procedureMessage.setDialogId(id);
			List<ProcedureMessage> procedureMessages = procedureMessageService.getUnsentList(procedureMessage)
					.getData();
			// String domainName = request.getServerName();
			for (ProcedureMessage message : procedureMessages) {
				try {
					if (message.getType() == 0) {// 即时消息发送
						resultData.add(message);
						// weixinService.sendTuisong(message.getMessageTo(), message.getMessageFrom(),
						// message.getContent(), "", domainName);
					} else if (message.getType() == 1) {// 定时推送消息，添加定时任务
						quartzManager.addJob(message.getId(), message.getId(), message.getId(), message.getId(),
								ProcedureMessageJob.class, message.getCron());
					}
				} catch (Exception e) {
					logger.error("创建定时推送失败:{}", e);
				}
			}
			result.setData(resultData.toJSONString());
		}

		return result;
	}

}