package com.lifelight.common.tools.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class ConnUtil {
	private static Logger logger = Logger.getLogger(ConnUtil.class);
	private static final String SERVLET_POST = "POST";
	private static final String SERVLET_GET = "GET";
	private static final String SERVLET_DELETE = "DELETE";
	private static final String SERVLET_PUT = "PUT";

	/*
	 * 传入map、整理出&格式
	 */
	private static String prepareParam(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		if (paramMap.isEmpty()) {
			return "";
		} else {
			for (String key : paramMap.keySet()) {
				String value = (String) paramMap.get(key);
				if (sb.length() < 1) {
					sb.append(key).append("=").append(value);
				} else {
					sb.append("&").append(key).append("=").append(value);
				}
			}
			return sb.toString();
		}
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String postJson(String target, Map map)
			throws ClientProtocolException, IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost method = new HttpPost(target);

		JSONObject jsonParam = new JSONObject();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key;

			key = it.next().toString();

			Object obj = map.get(key);
			if (obj instanceof String) {
				String value = (String) map.get(key);
				jsonParam.put(key, value);
			}

			if (obj instanceof Integer) {
				int value = (Integer) map.get(key);
				jsonParam.put(key, value);
			}
			if (obj instanceof String[]) {
				String[] value = (String[]) map.get(key);
				jsonParam.put(key, value);
			}
			if (obj instanceof int[]) {
				int[] value = (int[]) map.get(key);
				jsonParam.put(key, value);
			}

		}

		StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		method.setEntity(entity);
		HttpResponse result = httpClient.execute(method);

		// 请求结束，返回结果
		String resData = EntityUtils.toString(result.getEntity());
		return resData;
	}

	// 得到JSONObject(Post方式)
	// 此方法此处未调用测试
	public static String getJSONObjectByPost(String path, Map<String, String> paramsHashMap,
			String encoding) {
		JSONObject resultJsonObject = null;
		List<NameValuePair> nameValuePairArrayList = new ArrayList<NameValuePair>();
		// 将传过来的参数填充到List<NameValuePair>中
		if (paramsHashMap != null && !paramsHashMap.isEmpty()) {
			for (Map.Entry<String, String> entry : paramsHashMap.entrySet()) {
				nameValuePairArrayList
						.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		UrlEncodedFormEntity entity = null;
		StringBuilder entityStringBuilder = new StringBuilder();
		try {
			// 利用List<NameValuePair>生成Post请求的实体数据
			// 此处使用了UrlEncodedFormEntity!!!
			entity = new UrlEncodedFormEntity(nameValuePairArrayList, encoding);
			HttpPost httpPost = new HttpPost(path);
			// 为HttpPost设置实体数据
			httpPost.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			// HttpClient发出Post请求
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 得到httpResponse的实体数据
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					try {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8 * 1024);

						String line = null;
						while ((line = bufferedReader.readLine()) != null) {
							entityStringBuilder.append(line);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityStringBuilder.toString();
	}

	public static String sendPost(String url, String param) {
		String result = "";
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/octet-stream;charset=UTF-8");
			// 设置超时时间
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			os = conn.getOutputStream();

			// 发送请求参数
			String requestParam = "{\"head\":{\"productId\":\"1\",\"clientId\":\"eeeeeffffgggg\",\"deviceId\":\"0354188046626235\",\"commandId\":1000,\"timeStamp\":123456},\"body\":"
					+ param + "}";
			// String requestParam = param;
			logger.info("入参：" + requestParam);
			// byte[] in =
			// ZipUtil.compress(requestParam.toString().getBytes("UTF-8"));
			os.write(requestParam.getBytes("UTF-8"));
			// flush输出流的缓冲
			os.flush();
			// 定义BufferedReader输入流来读取URL的响应

			int outLen = conn.getContentLength();
			is = conn.getInputStream();
			// byte[] UTF8Str = ZipUtil.decompress(is, outLen);
			baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			// System.out.println(baos.toString());
			// result = new String(baos.toString().getBytes(),"UTF-8");
			byte[] lens = baos.toByteArray();
			result = new String(lens, "UTF-8");

			baos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPostWithOutHead(String url, String param) {
		String result = "";
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			// 设置超时时间
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			os = conn.getOutputStream();

			// 发送请求参数
			// String requestParam =
			// "{\"head\":{\"productId\":\"1\",\"clientId\":\"eeeeeffffgggg\",\"deviceId\":\"0354188046626235\",\"commandId\":1000,\"timeStamp\":123456},\"body\":"
			// + param + "}";
			String requestParam = param;
			logger.info("入参：" + requestParam);
			os.write(requestParam.getBytes("UTF-8"));
			// flush输出流的缓冲
			os.flush();
			// 定义BufferedReader输入流来读取URL的响应

			int outLen = conn.getContentLength();
			is = conn.getInputStream();
			// byte[] UTF8Str = ZipUtil.decompress(is, outLen);
			baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			// System.out.println(baos.toString());
			// result = new String(baos.toString().getBytes(),"UTF-8");
			byte[] lens = baos.toByteArray();
			result = new String(lens, "UTF-8");

			baos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url;
			logger.info("入参：" + urlName);
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPostString(String urlStr, Map<String, Object> paramMap)
			throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_POST);
		String paramStr = prepareParam(paramMap);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(paramStr.toString().getBytes("UTF-8"));
		os.close();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		// System.out.println(result);
		br.close();
		return result;
	}

	public static String sendPostStringWithHeader(String urlStr, Map<String, Object> header,
			Map<String, Object> paramMap) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_POST);
		String paramStr = prepareParam(paramMap);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		// 设置请求头信息
		if (header != null && header.size() > 0) {
			Iterator<String> keySetIt = header.keySet().iterator();
			while (keySetIt.hasNext()) {
				String key = keySetIt.next();
				conn.setRequestProperty(key, header.get(key).toString());
			}
		}
		OutputStream os = conn.getOutputStream();
		os.write(paramStr.toString().getBytes("UTF-8"));
		os.close();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		// System.out.println(result);
		br.close();
		return result;
	}

	public static void sendPut(String urlStr, Map<String, Object> paramMap) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_PUT);
		String paramStr = prepareParam(paramMap);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(paramStr.toString().getBytes("UTF-8"));
		os.close();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		// System.out.println(result);
		br.close();

	}

	public static String sendPutString(String urlStr, String paramStr) throws Exception {
		logger.info("入参：" + paramStr);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_PUT);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(paramStr.toString().getBytes("UTF-8"));
		os.close();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;

	}

	public static void doDelete(String urlStr, Map<String, Object> paramMap) throws Exception {
		String paramStr = prepareParam(paramMap);
		if (paramStr == null || paramStr.trim().length() < 1) {

		} else {
			urlStr += "?" + paramStr;
		}
		System.out.println(urlStr);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(SERVLET_DELETE);

		if (conn.getResponseCode() == 200) {
			System.out.println("成功");
		} else {
			System.out.println(conn.getResponseCode());
		}
	}

	public static void doDelete(String urlStr, String paramStr) throws Exception {
		logger.info("入参：" + paramStr);
		if (paramStr == null || paramStr.trim().length() < 1) {
		} else {
			urlStr += "?" + paramStr;
		}
		System.out.println(urlStr);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(SERVLET_DELETE);

		if (conn.getResponseCode() == 200) {
			System.out.println("成功");
		} else {
			System.out.println(conn.getResponseCode());
		}
	}

	public static String sendPostWithOutHeaderAndJson(String url, String param) {
		String result = "";
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			// 设置超时时间
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			os = conn.getOutputStream();

			// 发送请求参数
			// String requestParam =
			// "{\"head\":{\"productId\":\"1\",\"clientId\":\"eeeeeffffgggg\",\"deviceId\":\"0354188046626235\",\"commandId\":1000,\"timeStamp\":123456},\"body\":"
			// + param + "}";
			String requestParam = param;
			logger.info("入参：" + requestParam);
			// byte[] in =
			// ZipUtil.compress(requestParam.toString().getBytes("UTF-8"));
			os.write(requestParam.getBytes("UTF-8"));
			// flush输出流的缓冲
			os.flush();
			// 定义BufferedReader输入流来读取URL的响应

			int outLen = conn.getContentLength();
			is = conn.getInputStream();
			// byte[] UTF8Str = ZipUtil.decompress(is, outLen);
			baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			// System.out.println(baos.toString());
			// result = new String(baos.toString().getBytes(),"UTF-8");
			byte[] lens = baos.toByteArray();
			result = new String(lens, "UTF-8");

			baos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPutWithOutHeaderAndJson(String path, String obj) {
		StringBuffer sbuffer = new StringBuffer("");
		try {
			// 创建连接
			URL url = new URL(path);
			HttpURLConnection connection;

			// 添加 请求内容
			connection = (HttpURLConnection) url.openConnection();
			// 设置http连接属性
			connection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
			connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
			connection.setRequestMethod("PUT"); // 可以根据需要 提交
												// GET、POST、DELETE、PUT等http提供的功能

			connection.setRequestProperty("Host", "*******"); // 设置请
																// 求的服务器网址，域名，例如***.**.***.***
			connection.setRequestProperty("Content-Type", " application/json");// 设定
																				// 请求格式
																				// json，也可以设定xml格式的
			connection.setRequestProperty("Accept-Charset", "UTF-8"); // 设置编码语言
			connection.setRequestProperty("X-Auth-Token", "token"); // 设置请求的token
			connection.setRequestProperty("Connection", "keep-alive"); // 设置连接的状态
			connection.setRequestProperty("Transfer-Encoding", "chunked");// 设置传输编码
			connection.setRequestProperty("Content-Length", obj.toString().getBytes().length + ""); // 设置文件请求的长度
			connection.setReadTimeout(10000);// 设置读取超时时间
			connection.setConnectTimeout(10000);// 设置连接超时时间
			connection.connect();
			OutputStream out = connection.getOutputStream();// 向对象输出流写出数据，这些数据将存到内存缓冲区中
			out.write(obj.toString().getBytes()); // out.write(new
													// String("测试数据").getBytes());
													// //刷新对象输出流，将任何字节都写入潜在的流中
			out.flush();
			// 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
			out.close();
			// 读取响应
			if (connection.getResponseCode() == 200) {
				// 从服务器获得一个输入流
				InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());// 调用HttpURLConnection连接对象的getInputStream()函数,
																									// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
				BufferedReader reader = new BufferedReader(inputStream);
				String lines;

				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "UTF-8");
					sbuffer.append(lines);
				}
				reader.close();
			} else {
				logger.info("请求失败" + connection.getResponseCode());
			}
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbuffer.toString();
	}

	/*
	 * 测试路径 public static String dcenterhost = "http://s-user.semioe.com";
	 * public static String serverHost = "http://s-api.semioe.com";
	 */
	/* 生产路径 */
	public static String dcenterhost = "http://user.semioe.com";
	public static String serverHost = "http://api.semioe.com";

	public static void main(String[] args) throws Exception {

		long starttime = System.currentTimeMillis();
		int i = 1;

		switch (i) {
		// -------------------------------------------------研究者后台及微信端登录、注册、找回密码接口---------------------------------------------------------------------------------------------//
		// --------------后台用户注册流程使用接口start--------------//
		case 1:
			// 验证邮箱是否存在
			/*
			 * 入参：邮箱 {"email":"15146631005@139.com"}
			 * 返回值：{"error_code":-1,"message":"params invalid"} ，其中error_code =
			 * 0表示用户已经存在（也表示调用接口成功、这群傻逼，竟然这样定义接口）,其他则表示不存在
			 */
			logger.info("验证邮箱是否存在:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/email/exist", "{\"email\":\"123456789@qq.com\"}"));
			break;

		case 2:
			// 医生端注册
			/*
			 * 入参：inviteCode:'1231', 邀请码，此处已经和孟祥村讨论过了，写死：yP1R
			 * （测试环境）:yP1R。（生产环境）：aMvv email:'xxx@semioe.com', 邮箱 password:''
			 * 密码，明文 {\
			 * "inviteCode\":\"yP1R\",\"email\":\"15146631005@139.com\",\"password\":\"123456\"}
			 * 返回值：{"error_code":0,"message":"successful"} ，其中error_code =
			 * 0表示调用成功
			 */
			logger.info("医生端注册:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/register/email",
					"{\"inviteCode\":\"yP1R\",\"email\":\"115146631005@139.com\",\"password\":\"123456\"}"));
			break;
		// --------------后台用户注册流程使用接口end--------------//

		// --------------微信端用户、后台用户登录流程使用接口start--------------//
		case 3:
			// 登录
			/*
			 * 入参：username:'', 用户名 password:'' 密码
			 * {\"username\":\"15146631005@139.com\",\"password\":\"123456\"}
			 * 返回值：{"error_code":0,"message":"successful","token":
			 * "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGQwYzgxZGExNjliNDAxMzQ3YzhmYTUiLCJpYXQiOjE0OTAwNzgwNDYsImV4cCI6MTUwNTYzMDA0Nn0.5Uel9IrQwWQZGg1EFAJz5PKSUq2uzlxzf-g9ODAD424"}
			 * ，其中error_code = 0表示调用成功（登录成功） token：是系统的唯一索引，登录之后调用其他接口时的必须参数
			 */
			logger.info("登录:" + ConnUtil.sendPostWithOutHeaderAndJson(dcenterhost + "/api/v1/login",
					"{\"username\":\"123456789@qq.com\",\"password\":\"Abc123\"}"));
			break;
		case 4:
			// 通过登录颁发的token来获取用户信息
			/*
			 * 入参：token:'', 登录接口获得 {\
			 * "token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGQwYzgxZGExNjliNDAxMzQ3YzhmYTUiLCJpYXQiOjE0OTAwNzgwNDYsImV4cCI6MTUwNTYzMDA0Nn0.5Uel9IrQwWQZGg1EFAJz5PKSUq2uzlxzf-g9ODAD424\"}
			 * 返回值：{"error_code":0,"message":"getting successfully.","result":{
			 * "_id":"58d0c81da169b401347c8fa5","name":"用户25731","phone":"",
			 * "roles":["doctor"]}} 其中error_code =
			 * 0表示调用成功（登录成功），result为用户的基本信息，_id为当前登录用户的id，name：名称，phone：手机号。
			 * roles不用关心
			 */
			logger.info("通过登录颁发的token来获取用户信息:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/user_info",
					"{\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODdjOWJkYzY5MWExYjFiNDFhZWIzZWMiLCJpYXQiOjE0OTAxODEyMTcsImV4cCI6MTUwNTczMzIxN30.PpFVNrxOqpcAvl9Gg5rxKWDccVrqHhaaLGrbljx0UGI\"}"));
			break;

		// --------------微信端用户用户登录流程使用接口end--------------//

		// --------------后台用户找回或修改密码使用接口start--------------//
		case 5:
			// 同步其他端密码（在登录和注册的流程中描述：发送邮箱验证码（自己做下发）并成功验证之后，调用该接口、、如果是已经登录的话，需要修改密码则直接调用该接口）
			/*
			 * 入参：email:'', 邮箱 password:'' 新密码
			 * {"email":"15146631005@139.com","password":"123456"}
			 * 返回值：{"error_code":0,"message":"successful"} 其中error_code =
			 * 0表示调用成功（修改成功）
			 */
			logger.info("同步其他端密码:"
					+ ConnUtil.sendPostWithOutHeaderAndJson(dcenterhost + "/api/v1/email/syncPwd",
							"{\"email\":\"15146631005@139.com\",\"password\":\"654321\"}"));
			break;
		// --------------后台用户找回或修改密码使用接口end--------------//
		// -------------------------------------------------研究者后台及微信端登录、注册、找回密码接口---------------------------------------------------------------------------------------------//

		// -------------------------------------------------受试者微信端接口---------------------------------------------------------------------------------------------//
		// ---------------受试者前台注册接口start--------------//
		case 15:
			/*
			 * 发送手机验证码 post 请求地址 /api/sms/requestVerifyCode 入参：phone 手机号码
			 * {"phone":"15146631005"}
			 * 返回值：{"error_code":0,"message":"send code successful"
			 * ,"verifyToken":
			 * "+086:15146631005:1490924581989:c046b85e94120e0fe56ea66fb45aef09a4f11fd6"}
			 * 其中error_code = 0表示调用成功,verifyToken为返回唯一token
			 */
			logger.info("发送手机验证码:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/sms/requestVerifyCode", "{\"phone\":\"18618365193\"}"));

			break;

		case 16:
			/*
			 * 用户端注册 post 请求地址 /api/v1/register 入参：phone 手机号码 phoneCode //手机验证码
			 * password 密码 token {phone:'', phoneCode:'123125',//手机验证码
			 * password:'', token:''//该token为 发送验证码的接口返回的token} status 服务状态
			 * Number 0 未发布 1 下架 2 上架 3 删除
			 * 返回值：{"error_code":0,"message":"successful","token":
			 * "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGRkYmMwZDM5NjIxNTQ3NzMwZjk0OWYiLCJpYXQiOjE0OTA5MjY2MDUsImV4cCI6MTUwNjQ3ODYwNX0.sT2BvSJCwooi73NLR1CeD7I0V0bFGT3g4yNv18D319A"}
			 * 其中error_code = 0表示调用成功
			 */
			logger.info("用户注册:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/register",
					"{\"phone\":\"18618365193\", \"phoneCode\":\"497161\",\"password\":\"abcd1234\", \"token\":\"+086:18618365193:1492188676953:66f7c0dae864c8698981a8e04081afa689c992ed\"}"));

			break;

		case 21:
			/*
			 * 验证手机号是否存在 post 请求地址 /api/v1/checkPhone 入参：phone 手机号码
			 * {phone:'13089991005'} 返回值：不存在： { "error_code": 0, "message":
			 * "successful"} 存在 { "error_code": -1, "message":
			 * "The phone does exist" }
			 */
			logger.info("验证手机号是否存在:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/checkPhone", "{\"phone\":\"18945042832\"}"));

			break;

		// ---------------受试者前台注册接口end--------------//

		// ---------------受试者前台登录接口start--------------//
		case 17:
			// 登录
			/*
			 * 入参：username:'', 用户名 password:'' 密码
			 * {\"username\":\"15146631005\",\"password\":\"123456\"}
			 * 返回值：{"error_code":0,"message":"successful","token":
			 * "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGRkYmMwZDM5NjIxNTQ3NzMwZjk0OWYiLCJpYXQiOjE0OTA5MjcwMDUsImV4cCI6MTUwNjQ3OTAwNX0.n-kfSdB6XQZSAEgP7J4iPRU04Zf6XVJCoEhXlPYbOqg"}
			 * ，其中error_code = 0表示调用成功（登录成功） token：是系统的唯一索引，登录之后调用其他接口时的必须参数
			 */
			logger.info("登录:" + ConnUtil.sendPostWithOutHeaderAndJson(dcenterhost + "/api/v1/login",
					"{\"username\":\"15146631005\",\"password\":\"abc123\"}"));
			break;
		case 18:
			// 通过登录颁发的token来获取用户信息
			/*
			 * 入参：token:'', 登录接口获得 {\
			 * "token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGQwYzgxZGExNjliNDAxMzQ3YzhmYTUiLCJpYXQiOjE0OTAwNzgwNDYsImV4cCI6MTUwNTYzMDA0Nn0.5Uel9IrQwWQZGg1EFAJz5PKSUq2uzlxzf-g9ODAD424\"}
			 * 返回值：{"error_code":0,"message":"getting successfully.","result":{
			 * "_id":"58ddbc0d39621547730f949f","name":"用户05846","phone":
			 * "+08615146631005","roles":[]}} 其中error_code =
			 * 0表示调用成功（登录成功），result为用户的基本信息，_id为当前登录用户的id，name：名称，phone：手机号。
			 * roles不用关心
			 */
			logger.info("通过登录颁发的token来获取用户信息:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/user_info",
					"{\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OGRkYmMwZDM5NjIxNTQ3NzMwZjk0OWYiLCJpYXQiOjE0OTA5MjcwMDUsImV4cCI6MTUwNjQ3OTAwNX0.n-kfSdB6XQZSAEgP7J4iPRU04Zf6XVJCoEhXlPYbOqg\"}"));
			break;
		// ---------------受试者前台登录接口end--------------//

		// ---------------受试者密码找回或重置流程start-------------//
		case 19:
			/*
			 * 发送手机验证码 post 请求地址 /api/sms/requestVerifyCode 入参：phone 手机号码
			 * {"phone":"15146631005"}
			 * 返回值：{"error_code":0,"message":"send code successful"
			 * ,"verifyToken":
			 * "+086:15146631005:1490924581989:c046b85e94120e0fe56ea66fb45aef09a4f11fd6"}
			 * 其中error_code = 0表示调用成功,verifyToken为返回唯一token
			 */
			logger.info("发送手机验证码:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/sms/requestVerifyCode", "{\"phone\":\"18945042832\"}"));

			break;

		case 20:
			/*
			 * 重置密码 post 请求地址 /api/v1/resetPassword 入参：phoneCode //手机验证码
			 * password 密码 token { phoneCode:'123125',//手机验证码 token:'',//该token为
			 * 发送验证码的接口返回的token password:''}
			 * 返回值：{"error_code":0,"message":"successful"} 其中error_code =
			 * 0表示调用成功
			 */
			logger.info("发送手机验证码:" + ConnUtil.sendPostWithOutHeaderAndJson(
					dcenterhost + "/api/v1/resetPassword",
					"{ \"phoneCode\":\"425218\",\"password\":\"654321\", \"token\":\"+086:18618365193:1492190470944:38f9b4db5fd824a90d45d5ebb7a1f076ddbefae0\"}"));

			break;

		// ---------------受试者密码找回或重置流程end --------------//
		// -------------------------------------------------受试者微信端接口---------------------------------------------------------------------------------------------//
		default:
			break;
		}
		long endtime = System.currentTimeMillis();
		logger.info("耗时：" + (endtime - starttime));
	}
}
