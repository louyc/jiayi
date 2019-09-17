package com.lifelight.common.tools.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Httpclient 工具类
 * 
 * @author hua
 */
@Component
public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * doPost:post 请求. <br/>
	 * 
	 * @param host
	 * @param url
	 * @param header
	 * @param body
	 * @param timeout
	 * @return String
	 * @author hua
	 */
	public String doPost(String host, String url, Map<String, String> header,
			Map<String, String> body, int timeout) {

		logger.info("doPost. host={}, url={}", host, url);

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(host + url);

		if (header != null && header.size() > 0) {
			Iterator<String> keySetIt = header.keySet().iterator();
			while (keySetIt.hasNext()) {
				String key = keySetIt.next();
				BasicHeader basicHeader = new BasicHeader(key, header.get(key));
				post.addHeader(basicHeader);
			}
		}

		CloseableHttpResponse response = null;
		String content = null;
		try {
			String bodyStr = JSON.toJSONString(body);
			StringEntity se = new StringEntity(bodyStr, Charset.forName("UTF-8"));
			post.setEntity(se);

			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();

			post.setConfig(requestConfig);

			response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);

			logger.info("response={}", content);

			EntityUtils.consume(entity);
		} catch (Exception exp) {
			logger.error("请求异常：", exp);
			throw new RuntimeException("----http post exception---" + url, exp);
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				logger.error("IO 异常：", e);
			}
		}
		return content;
	}

	/**
	 * doGet:get 请求. <br/>
	 * 
	 * @param host
	 * @param url
	 * @param header
	 * @param param
	 * @param timeout
	 * @return String
	 * @author hua
	 */
	public String doGet(String host, String url, Map<String, String> header,
			Map<String, String> param, int timeout) {

		logger.info("doGet. host={}, url={}", host, url);

		// 构造HttpClient的实例
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String paramStr = "";
		if (param != null) {
			Set<String> set = param.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				Object key = it.next();
				Object value = param.get(key);
				paramStr += "&" + key + "=" + value;
			}

		}

		paramStr = paramStr.substring(1);

		HttpGet getMethod = new HttpGet(host + url + "?" + paramStr);
		String responseStr = null;

		if (header != null && header.size() > 0) {
			Iterator<String> keySetIt = header.keySet().iterator();
			while (keySetIt.hasNext()) {
				String key = keySetIt.next();
				BasicHeader basicHeader = new BasicHeader(key, header.get(key));
				getMethod.addHeader(basicHeader);
			}
		}

		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();

			getMethod.setConfig(requestConfig);

			// 执行getMethod
			CloseableHttpResponse response = httpclient.execute(getMethod);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode={}", statusCode);
			responseStr = EntityUtils.toString(response.getEntity());
			logger.info("response={}", responseStr);

		} catch (Exception exp) {
			logger.error("请求异常：", exp);
			throw new RuntimeException("----http post exception---" + url, exp);
		} finally {
			// 释放连接
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error("IO 异常：", e);
			}
		}
		return responseStr;
	}

	/**
	 * doGetReturnHeader:get 请求. <br/>
	 * 
	 * @param host
	 * @param url
	 * @param header
	 * @param param
	 * @param timeout
	 * @return String
	 * @author hua
	 */
	public String doGetReturnHeader(String host, String url, Map<String, String> header,
			Map<String, String> param, int timeout) {

		logger.info("doGetReturnHeader. host={}, url={}", host, url);

		// 构造HttpClient的实例
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String paramStr = "";
		if (param != null) {
			Set<String> set = param.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				Object key = it.next();
				Object value = param.get(key);
				paramStr += "&" + key + "=" + value;
			}

		}

		paramStr = paramStr.substring(1);

		HttpGet getMethod = new HttpGet(host + url + "?" + paramStr);
		String responseHeader = null;

		if (header != null && header.size() > 0) {
			Iterator<String> keySetIt = header.keySet().iterator();
			while (keySetIt.hasNext()) {
				String key = keySetIt.next();
				BasicHeader basicHeader = new BasicHeader(key, header.get(key));
				getMethod.addHeader(basicHeader);
			}
		}

		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();

			getMethod.setConfig(requestConfig);

			// 执行getMethod
			CloseableHttpResponse response = httpclient.execute(getMethod);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode={}", statusCode);
			responseHeader = EntityUtils.toString(response.getEntity());
			logger.info("response={}", responseHeader);
			Header[] headers = response.getAllHeaders();
			JSONObject result = new JSONObject();
			for (int i = 0; i < headers.length; i++) {
				result.put(headers[i].getName(), headers[i].getValue());
			}
			responseHeader = result.toJSONString();
			logger.info("headers={}", responseHeader);

		} catch (Exception exp) {
			logger.error("请求异常：", exp);
			throw new RuntimeException("----http post exception---" + url, exp);
		} finally {
			// 释放连接
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error("IO 异常：", e);
			}
		}
		return responseHeader;
	}

}
