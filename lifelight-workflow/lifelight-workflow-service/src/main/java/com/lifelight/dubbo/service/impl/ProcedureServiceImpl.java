package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.lifelight.api.entity.Procedure;
import com.lifelight.api.entity.ProcedureDialog;
import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.api.entity.ProcedureMessageExample;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.ProcedureDialogMapper;
import com.lifelight.dubbo.dao.ProcedureMapper;
import com.lifelight.dubbo.dao.ProcedureMessageMapper;
import com.lifelight.dubbo.service.ProcedureService;

@Service
public class ProcedureServiceImpl implements ProcedureService {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureServiceImpl.class);

	@Autowired
	private ProcedureMapper procedureMapper;
	@Autowired
	private ProcedureMessageMapper procedureMessageMapper;
	@Autowired
	private ProcedureDialogMapper procedureDialogMapper;

	/**
	 * 创建流程
	 * 
	 * @param procedure
	 * @return
	 */
	@Override
	public Result<?> createProcedure(Procedure procedure) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (null == procedure) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		procedure.setId(RandomStringUtils.randomAlphanumeric(8));
		procedure.setCreateTime(new Date());
		procedure.setUpdateTime(new Date());
		int num = procedureMapper.insertSelective(procedure);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除流程
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deleteProcedure(String id) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = procedureMapper.deleteByPrimaryKey(id);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改流程
	 * 
	 * @param procedure
	 * @return
	 */
	@Override
	public Result<?> updateProcedure(Procedure procedure) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(procedure.getId()) || StringUtils.isEmpty(procedure.getManagerId())
				|| "undefined".equals(procedure.getManagerId())) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		procedure.setUpdateTime(new Date());

		int num = procedureMapper.updateByPrimaryKeyWithBLOBs(procedure);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 根据id查询流程
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<Procedure> getProcedureById(String id) {
		Result<Procedure> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		Procedure procedure = procedureMapper.selectByPrimaryKey(id);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		result.setData(procedure);
		return result;
	}

	/**
	 * 查询流程
	 * 
	 * @param procedure
	 * @return
	 */
	@Override
	public PaginatedResult<Procedure> selectProcedureListPage(Procedure procedure) {

		List<Procedure> procedureList = procedureMapper.selectProcedureListPage(procedure);

		// 总页数
		int totalCount = procedure.getTotalResult();

		PaginatedResult<Procedure> pa = new PaginatedResult<Procedure>(procedureList, procedure.getCurrentPage(),
				procedure.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	/**
	 * 分享流程
	 * 
	 * @param id
	 * @param users
	 * @return
	 */
	@Override
	public Result<?> shareProcedure(String id, String users) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(users)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		Procedure procedure = procedureMapper.selectByPrimaryKey(id);
		List<Procedure> procedures = new ArrayList<>();

		try {
			// 一次分享给多个用户，逗号分隔
			String[] userList = users.split(",");
			for (String managerId : userList) {
				Procedure tmp = new Procedure();
				tmp.setId(RandomStringUtils.randomAlphanumeric(8));
				tmp.setManagerId(managerId);
				tmp.setTitle(procedure.getTitle());
				tmp.setContent(procedure.getContent());
				tmp.setStatus(procedure.getStatus());
				tmp.setShareFrom(procedure.getId());
				tmp.setCreateTime(new Date());
				tmp.setUpdateTime(new Date());
				procedures.add(tmp);
			}
			procedureMapper.insertByBatch(procedures);
			result.setResultCode(new ResultCode("SUCCESS", "分享成功！"));
		} catch (Exception e) {
			logger.error("批量插入失败：{}", e);
			result.setResultCode(new ResultCode("FAILED", "分享失败！"));
		}

		return result;
	}

	/**
	 * 根据流程id查询被分享用户
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result getBesharedUsers(String procedureId) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(procedureId)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		List<Map<String, Object>> procedure = procedureMapper.getBesharedUsers(procedureId);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		result.setData(procedure);
		return result;
	}

	/**
	 * 获取流程对话
	 * 
	 * @param id
	 * @param key
	 * @return
	 */
	@Override
	public Result<String> getNextStep(String id, String key, String procedureId, String managerId, String data,
			String domainName) {
		Result<String> result = new Result<>(StatusCodes.OK, true);
		JSONArray resultData = new JSONArray();

		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(key)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		// 保存会话信息
		ProcedureDialog dialog = new ProcedureDialog();
		dialog.setId(RandomStringUtils.randomAlphanumeric(8));
		dialog.setDialogId(id);
		dialog.setProcedureId(procedureId);
		dialog.setManagerId(managerId);
		dialog.setKey(key);
		dialog.setData(data);
		dialog.setCreateTime(new Date());
		dialog.setUpdateTime(new Date());
		dialog.setInUse(1);
		procedureDialogMapper.insert(dialog);

		Procedure procedure = procedureMapper.selectByPrimaryKey(procedureId);

		String[] keys = key.split(",");
		String[] datas = data.split(",");
		key = keys[0];
		data = datas[0];

		// 获取全部子节点
		String linkDataPath = "$.linkDataArray[?(@.from == " + key + ")].to";
		logger.info("procedure.getContent():{}", procedure.getContent());
		Object nextKeys = JsonPath.read(procedure.getContent(), linkDataPath);
		logger.info("子节点key:{}", nextKeys);
		JSONArray childNode = JSONArray.parseArray(JsonPath
				.read(procedure.getContent(), "$.nodeDataArray[?(@.key in " + nextKeys.toString() + ")]").toString());

		// 获取当前节点信息
		String fatherObject = JsonPath.read(procedure.getContent(), "$.nodeDataArray[?(@.key == " + key + ")]")
				.toString();
		logger.info("当前节点信息:{}", fatherObject);
		JSONObject fatherNode = JSONObject.parseObject(fatherObject.substring(1, fatherObject.length() - 1));
		// TODO
		if (fatherNode.getInteger("key") == 1) {
			// logger.info("子节点：{}", childNode.get(0));
			if (childNode.size() > 0) {
				fatherNode = childNode.getJSONObject(0);
				if (fatherNode.getInteger("propertyType") == 5) {
					resultData.add(fatherNode);
					result.setData(resultData.toString());
					return result;
				} else {
					key = fatherNode.getString("key");
					logger.info("标题的下一个子节点key={}", key);
					return getNextStep(id, key, procedureId, managerId, data, domainName);
				}
			}
		}
		// 获取当前节点类型
		int propertyType = fatherNode.getIntValue("propertyType");
		logger.info("当前节点类型:{}", propertyType);
		switch (propertyType) {
		case 0: // 单选
		case 1: // 多选
			resultData.add(fatherNode);
			resultData.addAll(childNode);
			break;
		case 2: // 路径执行 2 不需要显示
		case 4: // 数据判断 多个 不需要显示
			JSONArray childDatas = fatherNode.getJSONArray("stepData");
			for (int i = 0; i < childDatas.size(); i++) {
				Object childData = childDatas.getJSONObject(i).get("childData");
				String compareResult = Compare(childData, StringUtils.isNumeric(data) ? Integer.parseInt(data) : null);
				if (StringUtils.isNotEmpty(compareResult)) {
					logger.info("匹配到的选项内容：{}", compareResult);
					childNode = JSONArray
							.parseArray(JsonPath.read(procedure.getContent(), "$.nodeDataArray[?(@.text == \'"
									+ compareResult + "\' && @.key in " + nextKeys.toString() + ")]").toString());
					logger.info("匹配到的子节点：{}", childNode.toString());
					if (childNode != null) {
						key = childNode.getJSONObject(0).getString("key");
						logger.info("下一个子节点key={}", key);
						return getNextStep(id, key, procedureId, managerId, data, domainName);
					}
				}
			}
			break;
		case 3: // 选项 1 看情况 问题、输入显示
			if (childNode.size() > 0) {
				fatherNode = childNode.getJSONObject(0);
				for (int i = 0; i < childNode.size(); i++) {
					String options = JsonPath.read(procedure.getContent(),
							"$.linkDataArray[?(@.to == " + childNode.getJSONObject(i).getString("key") + ")].from")
							.toString();
					if (Arrays.toString(keys).replaceAll(" ", "").equals(options)) {
						fatherNode = childNode.getJSONObject(i);
						break;
					}
				}
				if (fatherNode.getInteger("propertyType") == 5)
					resultData.add(fatherNode);
				else {
					key = fatherNode.getString("key");
					logger.info("标题的下一个子节点key={}", key);
					return getNextStep(id, key, procedureId, managerId, data, domainName);
				}
			}
			break;
		case 5: // 用户输入 看情况 问题或者消息
			if (childNode.size() > 0) {
				fatherNode = childNode.getJSONObject(0);
				if (fatherNode.getInteger("propertyType") == 5)
					resultData.add(fatherNode);
				else {
					key = fatherNode.getString("key");
					logger.info("标题的下一个子节点key={}", key);
					return getNextStep(id, key, procedureId, managerId, data, domainName);
				}
			}
			break;
		case 6: // 消息设置 最后汇总显示
			JSONArray stepData = fatherNode.getJSONArray("stepData");
			for (int i = 0; i < stepData.size(); i++) {
				String content = stepData.getJSONObject(i).getJSONObject("childData").getString("contant");
				// 消息存库
				ProcedureMessage message = new ProcedureMessage();
				message.setId(RandomStringUtils.randomAlphanumeric(8));
				message.setDialogId(id);
				message.setProcedureId(procedureId);
				message.setMessageFrom(procedure.getManagerId());
				message.setMessageTo(managerId);
				message.setType(0); // 信息类型：0，即时；1，定时
				message.setContent(content);
				message.setStatus(0);
				message.setCreateTime(new Date());
				message.setUpdateTime(new Date());
				message.setInUse(1);
				message.setSendCount(1);

				procedureMessageMapper.insert(message);
			}
			if (childNode.size() > 0) {
				fatherNode = childNode.getJSONObject(0);
				if (fatherNode.getInteger("propertyType") == 5)
					resultData.add(fatherNode);
				else {
					key = fatherNode.getString("key");
					logger.info("标题的下一个子节点key={}", key);
					return getNextStep(id, key, procedureId, managerId, data, domainName);
				}
			}
			break;
		case 7: // 消息推送 不需要显示
			JSONArray scenes = fatherNode.getJSONArray("stepData").getJSONObject(0).getJSONArray("scene");
			Integer pushDataLength = fatherNode.getJSONArray("stepData").getJSONObject(0).getIntValue("pushDataLength");
			for (int i = 0; i < scenes.size(); i++) {
				JSONObject scene = scenes.getJSONObject(i);
				String pushContant = scene.getString("pushContant");
				// 流程推送
				String workflowList = scene.getString("workflowList");
				String url = "";
				if (StringUtils.isNotEmpty(workflowList)) {
					url = "http://" + domainName + "/html/medicalKnowledge/dialogue_wechart.html?id=" + workflowList;
				}
				// 消息存库
				ProcedureMessage message = new ProcedureMessage();
				message.setId(RandomStringUtils.randomAlphanumeric(8));
				message.setDialogId(id);
				message.setProcedureId(procedureId);
				message.setMessageFrom(procedure.getManagerId());
				message.setMessageTo(managerId);
				message.setType(1); // 信息类型：0，即时；1，定时
				message.setContent(pushContant);
				message.setUrl(url);
				message.setStatus(0);
				message.setCreateTime(new Date());
				message.setUpdateTime(new Date());
				message.setInUse(1);
				// 推送消息设置
				Integer pushPinlv = scene.getIntValue("pushPinlv");
				String[] pushTime = scene.getString("pushTime").split(":|：");
				Integer sendCount = pushDataLength / pushPinlv;
				message.setSendCount(sendCount);
				String cron = "0 " + Integer.parseInt(pushTime[1]) + " " + Integer.parseInt(pushTime[0]) + " 1/"
						+ pushPinlv + " * ? *";
				message.setCron(cron);

				ProcedureMessageExample example = new ProcedureMessageExample();
				example.createCriteria().andProcedureIdEqualTo(procedureId).andTypeEqualTo(message.getType())
						.andMessageFromEqualTo(procedure.getManagerId()).andMessageToEqualTo(managerId)
						.andContentEqualTo(message.getContent()).andCronEqualTo(cron).andUrlEqualTo(url)
						.andSendCountGreaterThan(0);
				if (procedureMessageMapper.selectByExample(example).size() == 0)
					procedureMessageMapper.insert(message);
			}
			if (childNode.size() > 0) {
				fatherNode = childNode.getJSONObject(0);
				if (fatherNode.getInteger("propertyType") == 5)
					resultData.add(fatherNode);
				else {
					key = fatherNode.getString("key");
					logger.info("标题的下一个子节点key={}", key);
					return getNextStep(id, key, procedureId, managerId, data, domainName);
				}
			}
			break;

		default:
			break;
		}

		if (childNode.size() == 0) {// 没有子节点，流程结束
			result.setResultCode(new ResultCode("NODE_NULL", "子节点为空，流程结束"));
		}
		result.setData(resultData.toString());
		return result;
	}

	public static String Compare(Object object, int data) {
		JSONObject jsonObject = JSONObject.parseObject(object.toString());
		logger.info("选项匹配：" + jsonObject + " ;data=" + data);
		String val = jsonObject.getString("expression");
		String content = null;
		switch (jsonObject.getIntValue("compare")) {
		case 0: // 无
			break;
		case 1: // 大于
			if (data > Integer.parseInt(val))
				content = jsonObject.getString("contant");
			break;
		case 2: // 小于
			if (data < Integer.parseInt(val))
				content = jsonObject.getString("contant");
			break;
		case 3: // 大于等于
			if (data >= Integer.parseInt(val))
				content = jsonObject.getString("contant");
			break;
		case 4: // 小于等于
			if (data <= Integer.parseInt(val))
				content = jsonObject.getString("contant");
			break;
		case 5: // 等于
			if (data == Integer.parseInt(val))
				content = jsonObject.getString("contant");
			break;
		case 6: // 不等于
			if (data != Integer.parseInt(val))
				content = jsonObject.getString("contant");
			break;
		case 7: // 范围
			String range[] = val.split(",");
			if (Integer.parseInt(range[0]) <= data && data <= Integer.parseInt(range[1]))
				content = jsonObject.getString("contant");
			break;

		default:
			break;
		}
		return content;
	}

	public static void main(String[] args) {
		String dialog = "{\"class\":\"go.GraphLinksModel\",\"nodeDataArray\":[{\"key\":1,\"text\":\"图谱名称\",\"category\":\"Loading\",\"loc\":\"5.5 -18\"},{\"text\":\"单选\",\"loc\":\"187.5 -18\",\"propertyType\":\"0\",\"key\":-2},{\"text\":\"选项\",\"loc\":\"369.5 -18\",\"propertyType\":\"3\",\"key\":-3,\"stepData\":[{\"id\":\"116\",\"name\":\"餐后血糖\",\"childData\":{\"childNodeName\":\"餐后血糖\",\"ondition\":\"0\",\"expression\":\"1\",\"contant\":\"选项1\"}}]},{\"text\":\"选项\",\"loc\":\"369.5 27\",\"propertyType\":\"3\",\"key\":-4,\"stepData\":[{\"id\":\"116\",\"name\":\"餐后血糖\",\"childData\":{\"childNodeName\":\"餐后血糖\",\"ondition\":\"0\",\"expression\":\"2\",\"contant\":\"选项2\"}}]},{\"text\":\"消息设置\",\"loc\":\"551.5 -18\",\"propertyType\":\"6\",\"key\":-5,\"stepData\":[{\"childData\":{\"childNodeName\":\"消息设置1\",\"overallScale\":\"\",\"contant\":\"棒棒的身体\"}}]},{\"text\":\"数据判断\",\"loc\":\"551.5 27\",\"propertyType\":\"4\",\"key\":-6,\"stepData\":[{\"childData\":{\"childNodeName\":\"条件1\",\"overallScale\":\"\",\"compare\":\"1\",\"expression\":\"2\",\"contant\":\"血糖有点高啊\"}},{\"childData\":{\"childNodeName\":\"条件2\",\"overallScale\":\"\",\"compare\":\"5\",\"expression\":\"2\",\"contant\":\"血糖正常\"}},{\"childData\":{\"childNodeName\":\"缺省\",\"overallScale\":\"\",\"compare\":\"2\",\"expression\":\"2\",\"contant\":\"血糖偏低\"}}]},{\"text\":\"血糖有点高啊\",\"loc\":\"733.5 27\",\"propertyType\":\"3\",\"key\":-7},{\"text\":\"血糖正常\",\"loc\":\"733.5 72\",\"propertyType\":\"3\",\"key\":-8},{\"text\":\"血糖偏低\",\"loc\":\"733.5 117\",\"propertyType\":\"3\",\"key\":-9},{\"text\":\"吃几顿\",\"loc\":\"915.5 27\",\"propertyType\":\"0\",\"key\":-10},{\"text\":\"用户输入\",\"loc\":\"1097.5 27\",\"propertyType\":\"5\",\"key\":-11,\"stepData\":[{\"id\":\"116\",\"name\":\"餐后血糖\",\"childData\":{\"childNodeName\":\"餐后血糖\",\"inputType\":\"0\",\"compare\":\"5\",\"needInput\":\"0\",\"tips\":\"请输入数字\",\"expression\":\"2\"}}]}],\"linkDataArray\":[{\"from\":1,\"to\":2},{\"from\":1,\"to\":-2},{\"from\":-2,\"to\":-3},{\"from\":-2,\"to\":-4},{\"from\":-3,\"to\":-5},{\"from\":-4,\"to\":-6},{\"from\":-6,\"to\":-7},{\"from\":-6,\"to\":-8},{\"from\":-6,\"to\":-9},{\"from\":-7,\"to\":-10},{\"from\":-10,\"to\":-11},{\"from\":-9,\"to\":-10}]}";
		List<Object> key = (List) JsonPath.read(dialog, "$.linkDataArray[?(@.from == -6 && @.to in [-8,-9])]");
		System.out.println(key);
		if (key.size() > 1) {
			System.out.println(key.get(0).toString());
		}
		String itemEmbedData = JsonPath.read(dialog, "$.nodeDataArray[?(@.key == -6)].stepData").toString();
		System.out.println(itemEmbedData);
		JSONArray items = JSONArray.parseArray(itemEmbedData.substring(1, itemEmbedData.length() - 1));
		System.out.println(items.get(0));

		String linkDataPath = "$.linkDataArray[?(@.from == -6" + ")].to";
		Object nextKeys = JsonPath.read(dialog, linkDataPath);
		System.out.println(nextKeys);
		List<Object> keys = (List) JsonPath.read(dialog,
				"$.linkDataArray[?(@.from == -6 && @.to in " + nextKeys.toString() + ")]");
		System.out.println(keys);
		JSONArray array = new JSONArray();
		array.add(key.get(0));
		array.addAll(keys);
		System.out.println(array);
		Object childNode = new Object();

		String s = "1";
		String linkDataPath2 = "$.linkDataArray[?(@.from == " + s + ")].to";
		List<Object> nextKeys2 = (List<Object>) JsonPath.read(dialog, linkDataPath2);
		System.out.println("-------------");
		System.out.println(nextKeys2);
		String childNode2 = JsonPath.read(dialog, "$.nodeDataArray[?(@.key in " + nextKeys2.toString() + ")]")
				.toString();
		JSONArray array2 = JSONArray.parseArray(childNode2);
		System.out.println(array2);
		Object object = array2.get(0);
		System.out.println(object);

		String fatherObject = JsonPath.read(dialog, "$.nodeDataArray[?(@.key == -6)]").toString();
		JSONObject fatherNode = JSONObject.parseObject(fatherObject.substring(1, fatherObject.length() - 1));
		String propertyType = fatherNode.getString("propertyType");
		System.out.println(propertyType);

		System.out.println("--------------------------childData");
		String stepData = fatherNode.getString("stepData");
		System.out.println("stepData" + stepData);
		JSONArray childDatas = JSONArray.parseArray(fatherNode.getString("stepData"));
		System.out.println("childDatas" + childDatas.toString());
		for (Object object1 : childDatas) {
			System.out.println(object1);
			Object childData = JSONObject.parseObject(object1.toString()).get("childData");
			System.out.println("childData" + childData);
			String compareResult = Compare(childData, Integer.parseInt("4"));
			System.out.println("compareResult" + compareResult);

			JSONArray childNode1 = JSONArray.parseArray(JsonPath.read(dialog,
					"$.nodeDataArray[?(@.text == \'" + compareResult + "\' && @.key in " + nextKeys.toString() + ")]")
					.toString());
			System.out.println("childNode1" + childNode1);
			if (childNode1 != null) {
				fatherNode = JSONObject.parseObject(childNode1.get(0).toString());
				String key1 = fatherNode.getString("key");
				System.out.println(key1);
				break;
			}
		}

		System.out.println("--------------------Message");
		fatherObject = JsonPath.read(dialog, "$.nodeDataArray[?(@.key == -5)]").toString();
		System.out.println(fatherObject);
		fatherNode = JSONObject.parseObject(fatherObject.substring(1, fatherObject.length() - 1));
		JSONArray messages = JSONArray.parseArray(fatherNode.getString("stepData"));
		System.out.println(messages);
		for (int i = 0; i < messages.size(); i++) {
			System.out.println(messages.getJSONObject(i).getJSONObject("childData").getString("contant"));
		}

		dialog = "{\"class\":\"go.GraphLinksModel\",\"nodeDataArray\":[{\"key\":1,\"text\":\"消息推送\",\"category\":\"Loading\",\"loc\":\"0 -29\"},{\"text\":\"消息推送\",\"loc\":\"187.5 -29\",\"propertyType\":\"7\",\"key\":-2,\"stepData\":{\"pushMethod\":\"1\",\"pushDataLength\":\"99\",\"scene\":[{\"pushPinlv\":\"10\",\"pushTime\":\"12：00\",\"pushContant\":\"场景1\",\"workflowList\":\"10\"},{\"pushPinlv\":\"20\",\"pushTime\":\"12：00\",\"pushContant\":\"场景2\",\"workflowList\":\"20\"}]}}],\"linkDataArray\":[{\"from\":1,\"to\":2},{\"from\":1,\"to\":-2}]}";
		System.out.println("--------------------MessagePush");
		fatherObject = JsonPath.read(dialog, "$.nodeDataArray[?(@.key == -2)]").toString();
		System.out.println(fatherObject);
		fatherNode = JSONObject.parseObject(fatherObject.substring(1, fatherObject.length() - 1));
		System.out.println(fatherNode.getString("stepData"));
		JSONObject pushData = fatherNode.getJSONObject("stepData");
		JSONArray scenes = pushData.getJSONArray("scene");
		Integer pushDataLength = pushData.getIntValue("pushDataLength");
		for (Object object1 : scenes) {
			JSONObject scene = JSONObject.parseObject(object1.toString());
			Integer pushPinlv = scene.getIntValue("pushPinlv");
			String[] pushTime = scene.getString("pushTime").split(":|：");
			String pushContant = scene.getString("pushContant");
			String workflowList = scene.getString("workflowList");
			int pinlv = pushDataLength / pushPinlv;
			System.out.println(pushDataLength + " " + pushPinlv + " " + pushTime[0] + ":" + pushTime[1] + " "
					+ pushContant + " " + workflowList + " " + pinlv);
			String cron = "0 " + Integer.parseInt(pushTime[1]) + " " + Integer.parseInt(pushTime[0]) + " 1/"
					+ String.valueOf(pushDataLength / pushPinlv) + " * ? *";
			System.out.println(cron);
		}

		dialog = "{ \"class\": \"go.GraphLinksModel\", \"nodeDataArray\": [ {\"key\":1, \"text\":\"多选测试\", \"category\":\"Loading\", \"loc\":\"5.5 92\"}, {\"text\":\"多选\", \"loc\":\"183 92\", \"propertyType\":\"1\", \"key\":-2}, {\"text\":\"选项1\", \"loc\":\"369.5 49\", \"propertyType\":\"3\", \"key\":-3}, {\"text\":\"选项2\", \"loc\":\"369.5 82\", \"propertyType\":\"3\", \"key\":-4}, {\"text\":\"选项3\", \"loc\":\"369.5 117\", \"propertyType\":\"3\", \"key\":-5}, {\"text\":\"单选1\", \"loc\":\"551.5 47\", \"propertyType\":\"0\", \"key\":-6}, {\"text\":\"单选2\", \"loc\":\"551.5 83\", \"propertyType\":\"0\", \"key\":-7}, {\"text\":\"单选123\", \"loc\":\"551.5 155\", \"propertyType\":\"0\", \"key\":-10}, {\"text\":\"单选12\", \"loc\":\"551.5 8\", \"propertyType\":\"0\", \"key\":-11}, {\"text\":\"单选3\", \"loc\":\"551.5 119\", \"key\":-14} ], \"linkDataArray\": [ {\"from\":1, \"to\":2}, {\"from\":1, \"to\":-2}, {\"from\":-2, \"to\":-3}, {\"from\":-2, \"to\":-4}, {\"from\":-2, \"to\":-5}, {\"from\":-3, \"to\":-6}, {\"from\":-3, \"to\":-11}, {\"from\":-3, \"to\":-10}, {\"from\":-4, \"to\":-7}, {\"from\":-4, \"to\":-11}, {\"from\":-4, \"to\":-10}, {\"from\":-5, \"to\":-14}, {\"from\":-5, \"to\":-10} ]}";
		System.out.println("----------------多选测试");
		fatherObject = JsonPath.read(dialog, "$.nodeDataArray[?(@.key == -3)]").toString();
		System.out.println(fatherObject);
		nextKeys = JsonPath.read(dialog, "$.linkDataArray[?(@.from == -3)].to");
		fatherNode = JSONObject.parseObject(fatherObject.substring(1, fatherObject.length() - 1));
		JSONArray childNode1 = JSONArray.parseArray(
				JsonPath.read(dialog, "$.nodeDataArray[?(@.key in " + nextKeys.toString() + ")]").toString());
		System.out.println(childNode1);
		if (childNode1.size() > 0) {
			String froms = "-3,-4,-5";
			String[] keys1 = froms.split(",");
			System.out.println(Arrays.toString(keys1));
			if (keys1.length > 1) {
				for (int i = 0; i < childNode1.size(); i++) {
					System.out.println(childNode1.getJSONObject(i));
					String options = JsonPath.read(dialog,
							"$.linkDataArray[?(@.to == " + childNode1.getJSONObject(i).getString("key") + ")].from")
							.toString();
					if (Arrays.toString(keys1).replaceAll(" ", "").equals(options)) {
						System.out.println(options);
						break;
					}
				}
			}
		}
	}

}