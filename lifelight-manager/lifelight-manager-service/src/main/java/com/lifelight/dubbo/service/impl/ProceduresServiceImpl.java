package com.lifelight.dubbo.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.base64.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.util.ConnUtil;
import com.lifelight.common.tools.util.HttpClientUtil;
import com.lifelight.dubbo.service.ProceduresService;
import com.lifelight.util.MapToObject;

@Service
public class ProceduresServiceImpl implements ProceduresService {

	private static final Logger logger = LoggerFactory.getLogger(ProceduresServiceImpl.class);

	@Autowired
	private HttpClientUtil httpClientUtil;
	@Value("#{http['workflow-api.host']}")
	private String workflowAPI;

	@Override
	public PaginatedResult getProceduresByToken(String token, String title, String pageNumber, String pageSize) {
		logger.info("getProceduresByToken() start token=" + token + " title=" + title + " pageNumber={} pageSize={}",
				pageNumber, pageSize);
		PaginatedResult result = new PaginatedResult<>(0, 0);

		Map<String, String> header = new HashMap<String, String>();
		header.put("Authorization", "Basic " + MapToObject.byteToBase64(token + ":"));
		Map<String, String> param = new HashMap<String, String>();
		param.put("fields", "id,uid,title,created_at");
		param.put("ProcedureSearch[title]", title);
		if ("null".equals(pageNumber) || StringUtils.isEmpty(pageNumber))
			pageNumber = "1";
		param.put("page", pageNumber);
		if ("null".equals(pageSize) || StringUtils.isEmpty(pageSize))
			param.put("per-page", "50");
		else
			param.put("per-page", pageSize);

		try {
			String doGetReturnHeader = httpClientUtil.doGetReturnHeader(workflowAPI, "/v1/procedures", header, param,
					60 * 1000);
			JSONObject pageInfo = JSONObject.parseObject(doGetReturnHeader);
			String doGet = httpClientUtil.doGet(workflowAPI, "/v1/procedures", header, param, 60 * 1000);
			JSONArray dataArray = JSONArray.parseArray(doGet);

			// 提供方接口，分页大小只支持小于50，大于50的，多次请求，拼装返回结果
			int itemsCount = pageInfo.getInteger("X-Pagination-Total-Count");// 默认查询所有
			if (StringUtils.isNotEmpty(pageSize))
				itemsCount = Integer.parseInt(pageSize);
			else // 防止后续参数空指针，添加默认值
				pageSize = pageInfo.getString("X-Pagination-Total-Count");
			if (itemsCount > 50) {
				int pageCount = (int) Math.ceil((double) itemsCount / 50);
				for (int i = 2; i <= pageCount; i++) {
					param.put("page", String.valueOf(i));
					doGet = httpClientUtil.doGet(workflowAPI, "/v1/procedures", header, param, 60 * 1000);
					dataArray.addAll(JSONArray.parseArray(doGet));
				}
			}

			result = new PaginatedResult<>(dataArray, Integer.parseInt(pageNumber), Integer.parseInt(pageSize),
					pageInfo.getInteger("X-Pagination-Total-Count"));
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.error("GET 请求失败，没有查询成功：{}", e);
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}
		return result;
	}

	@Override
	public Result getProcedureDialog(String token, String userId, String doctorId, String orderId, String procedureId) {
		Result result = new Result<>(StatusCodes.OK, true);

		logger.info("调用流程列表：userId=" + userId + " doctorId=" + doctorId + " orderId=" + orderId + " procedureId="
				+ procedureId);
		logger.info("传入token：" + token);

		Map<String, Object> header = new HashMap<String, Object>();
		header.put("Authorization", "Basic " + MapToObject.byteToBase64(token + ":"));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("object_id", userId);
		param.put("doctor_id", doctorId);
		param.put("order_id", orderId);
		param.put("procedure_id", procedureId);

		try {
			String doPost = ConnUtil.sendPostStringWithHeader(workflowAPI + "/v1/procedure-dialog", header, param);
			logger.info("请求返回结果：" + doPost);
			JSONObject dataObject = JSONObject.parseObject(doPost);
			result.setData(dataObject);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.error("GET 请求失败，没有查询成功：{}", e);
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}

		return result;
	}
	
	public static void main(String[] args) {
//		String s = MapToObject.byteToBase64("eyJoZWFkIjp7InR5cGUiOiJKV1QiLCJhbGciOiJzZW1pb2UifSwibG9hZCI6eyJkYXRhIjoiNCIsImlzcyI6Ind3dy5zZW1pb2UuYWNjb3VudCIsImV4cCI6MTUyODEwNjY3MzUwNCwiaWF0IjoxNTI4MTAzNjczNTA0fX0=.881cdae171613aca9db92daba7e287dc" + ":");
//		System.out.println(s);

		String userId = "4a79c270-3225-4e16-9885-774d6ce7693d";
		String doctorId = "16b7044e-45a2-4d94-9353-c37bc842c83b";
		String orderId ="2388";
		String procedureId ="3573";
		String token = "eyJoZWFkIjp7InR5cGUiOiJKV1QiLCJhbGciOiJzZW1pb2UifSwibG9hZCI6eyJkYXRhIjoiNGE3OWMyNzAtMzIyNS00ZTE2LTk4ODUtNzc0ZDZjZTc2OTNkIiwiaXNzIjoid3d3LnNlbWlvZS5hY2NvdW50IiwiZXhwIjoxNTI4MTY2Mjg1NjY5LCJpYXQiOjE1MjgxNjMyODU2Njl9fQ==.b6c7a26752f4a45ea6c8d57b91da312f";
//		Map<String, Object> header = new HashMap<String, Object>();
		Map<String, String> header = new HashMap<String, String>();
		header.put("Authorization", "Basic " + MapToObject.byteToBase64(token + ":"));
		System.out.println(header.entrySet());
		header.put("Accept", "application/json");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("object_id", userId);
		param.put("doctor_id", doctorId);
		param.put("order_id", orderId);
		param.put("procedure_id", procedureId);
		int timeout =0;
		try {
			HttpClientUtil hcu = new HttpClientUtil();
			String doPost = hcu.doGet("http://workflow-api-test.lifelight365.com", "/v1/procedure-var-maps", header, null, timeout);
//			String doPost = ConnUtil.sendPostStringWithHeader("http://workflow-api-test.lifelight365.com" + "/v1/procedure-dialog",
//					header, param);
//			http://workflow-api.lifelight365.com/v1/procedure-var-maps
			System.out.println("请求返回结果：" + doPost);
//			JSONObject dataObject = JSONObject.parseObject(doPost);
//			result.setData(dataObject);
//			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("GET 请求失败，没有查询成功：{}"+e);
//			result.setSuccessful(false);
//			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}
		
	/*	String url = "http://workflow-api.semioe.com/v1/procedure-var-mapsGET";
		URL urlObj;
		try {
			urlObj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("GET"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			// 设置请求头信息
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization",
					"Basic " + MapToObject.byteToBase64(token + ":"));
			con.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 获取所有响应头字段
			BufferedReader in = null;
			Map<String, List<String>> map = con.getHeaderFields();
			System.out.println(map.entrySet());
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
