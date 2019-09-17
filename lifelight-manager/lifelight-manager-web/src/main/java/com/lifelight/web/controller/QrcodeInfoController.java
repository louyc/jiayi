package com.lifelight.web.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.QrApi;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.lifelight.api.entity.QrcodeInfo;
import com.lifelight.api.entity.WeixinConfigure;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.QrcodeInfoService;
import com.lifelight.dubbo.service.WeixinConfigureService;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Controller
@RequestMapping("/qrcode")
public class QrcodeInfoController {

	private static final Logger logger = LoggerFactory.getLogger(QrcodeInfoController.class);

	@Reference
	private QrcodeInfoService qrcodeInfoService;

	@Value("${redis.host}")
	private String redis_host;
	@Value("${redis.port}")
	private String redis_port;

	@Autowired
	private RedisTool redisTool;
	@Reference
	private WeixinConfigureService weixinConfigureService;
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	private Map<String,WeixinProxy> wxmap= new HashMap<String,WeixinProxy>();
	private Map<String,WeixinConfigureVO> weixinConfigmap= new HashMap<String,WeixinConfigureVO>();
	/*@Value("#{settings['weixin_Appid']}")
	private String weixin_Appid;
	@Value("#{settings['weixin_Secret']}")
	private String weixin_Secret;
	@Value("#{settings['weixin_Paysignkey']}")
	private String weixin_Paysignkey;
	@Value("#{settings['weixin_Mchid']}")
	private String weixin_Mchid;*/

	@SuppressWarnings("rawtypes")
	@RequestMapping("/createQrcodeInfo")
	@ResponseBody
	public Result createQrcodeInfo( HttpServletRequest request,@RequestBody QrcodeInfo qrcodeInfo) {
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		WeixinConfigureVO weixin =getWeixinProxy(domainName);
		return qrcodeInfoService.createQrcodeInfo(qrcodeInfo, jedisConnectionFactory.getHostName(),
				jedisConnectionFactory.getPort(),Integer.parseInt(weixinId),
				weixin.getAppid(), weixin.getSecret(),weixin.getPaysignKey(), weixin.getMchid());
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/createDoctorQrcodeInfo")
	@ResponseBody
	public Result createDoctorQrcodeInfo( HttpServletRequest request,@RequestBody QrcodeInfo qrcodeInfo) {
		logger.info("要分享的医生id:"+qrcodeInfo.getManagerId());
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		WeixinConfigureVO weixin =getWeixinProxy(domainName);
		return qrcodeInfoService.createDoctorQrcodeInfo(qrcodeInfo, jedisConnectionFactory.getHostName(),
				jedisConnectionFactory.getPort(),Integer.parseInt(weixinId),
				weixin.getAppid(), weixin.getSecret(),weixin.getPaysignKey(), weixin.getMchid());
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllQrcodeInfoListPage")
	@ResponseBody
	public Result getAllQrcodeInfoListPage( HttpServletRequest request,@RequestParam("managerId") String managerId,
			@RequestParam(value="qrType",required = false) Integer qrType,
			@RequestParam("pageSize") String pageSize, 
			@RequestParam("currentsPage") String currentsPage) {

		int currentPage = Integer.parseInt(currentsPage);

		if (currentPage <= 0) {
			currentPage = 1;
		}

		QrcodeInfo qrcodeInfo = new QrcodeInfo();

		qrcodeInfo.setShowCount(Integer.parseInt(pageSize));
		qrcodeInfo.setCurrentPage(currentPage);

		qrcodeInfo.setManagerId(managerId);
		if (qrType != null) {
			qrcodeInfo.setQrType(qrType);
		}

		return qrcodeInfoService.getAllQrcodeInfoListPage(qrcodeInfo);
	}

	@RequestMapping("/download")
	@ResponseBody
	public void createQrcodeInfo(@RequestParam("ticket") String ticket,
			@RequestParam("name") String name, HttpServletRequest request,
			HttpServletResponse response) {
		// 下载网络文件
		int bytesum = 0;
		int byteread = 0;
		OutputStream fs = null;
		InputStream inStream = null;

		try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("QrCode" + name + ".jpg", "UTF-8"));
			URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket);

			URLConnection conn = url.openConnection();
			inStream = conn.getInputStream();

			byte[] buffer = new byte[1204];
			fs = response.getOutputStream();
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 用户服务分享功能
	 *
	 * @param paramsId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getShareServiceCode")
	@ResponseBody
	public Result getShareServiceCode(@RequestParam("paramsId") String paramsId, HttpServletRequest request,
			@RequestParam("type") String type, @RequestParam("sourceUserId") String sourceUserId) {

		logger.info("QrcodeInfoController.getShareServiceCode start, type=" + type + ", paramsId=" + paramsId + ", sourceUserId=" + sourceUserId);
		//获取微信配置id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		WeixinConfigureVO weixin =getWeixinProxy(domainName);
		return qrcodeInfoService.shareServiceCode(type, paramsId, sourceUserId, jedisConnectionFactory.getHostName(),
				jedisConnectionFactory.getPort(),Integer.parseInt(weixinId),
				weixin.getAppid(), weixin.getSecret(),weixin.getPaysignKey(), weixin.getMchid());
	}


	@ResponseBody
	@RequestMapping(value = "/getQRType", method = RequestMethod.GET)
	public Result getQRType(HttpServletRequest request, HttpServletResponse response) {
		logger.info("QrcodeInfoController.getQRType start itemType=32");

		return qrcodeInfoService.getQRType();
	}
	
	
	public WeixinConfigureVO getWeixinProxy(String domainName) {
		if (StringUtils.isNotEmpty(domainName)) {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,
					jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
					jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
			
			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);
			logger.info("domainname======================================================================================"+domainName);
			if(weixinConfigmap.get(domainName)!=null){
				return weixinConfigmap.get(domainName);
			}else{
				Result result = weixinConfigureService.queryWeicinConfigureByDomain(domainName,null);
				try {
					WeixinConfigureVO weixinConfig = null;
					if(null !=result.getData()) {
						weixinConfig = (WeixinConfigureVO)result.getData();
						weixinConfigmap.put(domainName, weixinConfig);
					}else {
						logger.info("获取微信配置信息错误****************");
						return null;
					}
					WeixinPayAccount wxpayAccount =null;
					if(null!=weixinConfig){
						// 建一个表来获取微信的全部信息。包含微信支付、imweixin
						wxpayAccount = new WeixinPayAccount(weixinConfig.getAppid().trim(), weixinConfig.getSecret().trim(),
								weixinConfig.getPaysignKey().trim(), weixinConfig.getMchid().trim(), null, null, null, null, null);
						Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);
						settings.setTokenStorager(rts);
						WeixinProxy proxy = new WeixinProxy(settings);
						wxmap.put(domainName, proxy);
					}else{
						return null;
					}
				} catch (Exception e) {
					return null;
				}
				return weixinConfigmap.get(domainName);
			}			
		} else {
			return null;
		}
	}
	
}