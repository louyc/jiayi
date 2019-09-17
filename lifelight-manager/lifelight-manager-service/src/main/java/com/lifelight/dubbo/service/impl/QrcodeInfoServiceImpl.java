package com.lifelight.dubbo.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.QrApi;
import com.foxinmy.weixin4j.mp.model.QRParameter;
import com.foxinmy.weixin4j.mp.model.QRResult;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.Dictionary;
import com.lifelight.api.entity.DictionaryExample;
import com.lifelight.api.entity.QrcodeInfo;
import com.lifelight.api.vo.GoodsInfoVO;
import com.lifelight.api.vo.ServiceInfoVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.BackstageUserInfoMapper;
import com.lifelight.dubbo.dao.DictionaryMapper;
import com.lifelight.dubbo.dao.GoodsInfoMapper;
import com.lifelight.dubbo.dao.QrcodeInfoMapper;
import com.lifelight.dubbo.dao.ServiceInfoMapper;
import com.lifelight.dubbo.service.QrcodeInfoService;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class QrcodeInfoServiceImpl implements QrcodeInfoService {

	private static final Logger logger = LoggerFactory.getLogger(QrcodeInfoServiceImpl.class);
	
	@Autowired
	private QrcodeInfoMapper qrcodeInfoMapper;
	@Autowired
	private BackstageUserInfoMapper backstageUserInfo;
	@Autowired
	private DictionaryMapper dictionaryMapper;
	private WeixinProxy weixinProxy;
	
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	/**
	 * 添加二维码
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result createQrcodeInfo(QrcodeInfo qrCodeInfo, String hostName,int port,
			Integer platformId,String Appid, String Secret,
			String Paysignkey, String Mchid) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == qrCodeInfo) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		} else if (null == qrCodeInfo.getIsPromotionName()) {
			result.setResultCode(new ResultCode("FALSE", "推广人姓名为空！"));
			return result;
		}

		// 根据二维码推广人判断重复，如果重复 则不添加
		int re_num = qrcodeInfoMapper.getQrcodeInfoByName(qrCodeInfo.getIsPromotionName(), qrCodeInfo.getManagerId());

		if (re_num > 0) {
			result.setResultCode(new ResultCode("FALSE", "此人已有二维码！"));
			return result;
		}

		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		qrCodeInfo.setCreateTime(now);
		
		// 调用微信二维码接口、生成对应的ticket，然后将ticket写入表中、传入参数有qrCodeInfo.getIsPromotionName()+qrCodeInfo.getManagerId()
		String ticket = QRCode("1,"+qrCodeInfo.getIsPromotionName()+","+qrCodeInfo.getManagerId(),
				getWeixinProxy(hostName, port,  Appid,  Secret,
				 Paysignkey,  Mchid));
		
		if(StringUtils.isNotEmpty(ticket)) {
			qrCodeInfo.setTicket(ticket);
		}
		if(qrCodeInfo.getQrType() == null){
			qrCodeInfo.setQrType(4);
		}
		int num = qrcodeInfoMapper.insertSelective(qrCodeInfo);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "添加失败！"));
		}
		return result;
	}
	/**
	 * 添加医生分享 二维码
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result createDoctorQrcodeInfo(QrcodeInfo qrCodeInfo, String hostName, int port,
			Integer platformId,String Appid, String Secret,
			String Paysignkey, String Mchid) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		if (null == qrCodeInfo) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		} else if (null == qrCodeInfo.getManagerId()) {
			result.setResultCode(new ResultCode("FALSE", "推广人为空！"));
			return result;
		}
		
		// 根据二维码推广人判断重复，如果重复 则不添加
//		int re_num = qrcodeInfoMapper.getQrcodeInfoByName(qrCodeInfo.getIsPromotionName(), qrCodeInfo.getManagerId());
		BackstageUserInfo user = backstageUserInfo.selectByPrimaryKey(qrCodeInfo.getManagerId());
		logger.info("要分享的医生信息："+user.getName()+"  ticket:"+user.getTicket());
		if (user!=null && !StringUtil.isEmpty(user.getTicket())) {
			result.setResultCode(new ResultCode("FALSE", "此人已有二维码！"));
			result.setData(user);
			return result;
		}
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		qrCodeInfo.setCreateTime(now);
		
		// 调用微信二维码接口、生成对应的ticket，然后将ticket写入表中、传入参数有qrCodeInfo.getManagerId()
		String ticket = QRCode("2,"+"2,"+qrCodeInfo.getManagerId()+",4",getWeixinProxy(hostName, port,  Appid,  Secret,
				Paysignkey,  Mchid));
		BackstageUserInfo newUser = new BackstageUserInfo();
		logger.info("生成的ticket："+ticket);
		if(StringUtils.isNotEmpty(ticket)) {
			newUser.setTicket(ticket);
		}
		newUser.setManagerId(qrCodeInfo.getManagerId());
		int num = backstageUserInfo.updateByPrimaryKeySelective(newUser);
		user.setTicket(ticket);
		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
			result.setData(user);
		} else {
			result.setResultCode(new ResultCode("FALSE", "添加失败！"));
		}
		return result;
	}

	/**
	 * 
	 * 创建永久二维码，返回二维码信息
	 * 
	 */
	public static String QRCode(String InvitationCode,WeixinProxy weixinProxy) {
		String ticket ="";
		try {
			logger.info("传给微信的信息："+InvitationCode);
			TokenHolder tokenHolder = weixinProxy.getTokenHolder();

			QrApi qrApi = new QrApi(tokenHolder);

			QRResult qrResult = qrApi.createQR(QRParameter.createPermanenceStr(InvitationCode));

			ticket = qrResult.getTicket();
			
		} catch (WeixinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticket;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Result getAllQrcodeInfoListPage(QrcodeInfo qrcodeInfo) {
		List<QrcodeInfo> qrcodeInfoList = qrcodeInfoMapper.getAllQrcodeInfoListPage(qrcodeInfo);
		
		//总页数
		int totalCount = qrcodeInfo.getTotalResult();
		
		PaginatedResult<QrcodeInfo> pa = new PaginatedResult<QrcodeInfo>(qrcodeInfoList, qrcodeInfo.getCurrentPage(), qrcodeInfo.getShowCount(), totalCount);
		
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}
	
	@Override
	public QrcodeInfo getQrcodeByQrPerson( String personName , String doctorId ,Integer platformId){
		return qrcodeInfoMapper.getQrcodeInfoByPrname(personName, doctorId);
	}
	
	/**
	 * 
	 * 创建临时二维码
	 * 
	 */
	public static String QRCodeTemporary(String InvitationCode,WeixinProxy weixinProxy) {
		String ticket ="";
		try {

			TokenHolder tokenHolder = weixinProxy.getTokenHolder();

			QrApi qrApi = new QrApi(tokenHolder);

			QRResult qrResult = qrApi.createQR(QRParameter.createTemporaryQR(2592000, InvitationCode));

			ticket = qrResult.getTicket();
			
		} catch (WeixinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticket;
	}
	
	@Autowired
	private BackstageUserInfoMapper backUserInfoMapper;  //医生&机构
	
	@Autowired
	private ServiceInfoMapper serviceInfoMapper;  //服务
	
	@Autowired
	private GoodsInfoMapper goodsInfoMapper;  //商品
	
	/**
	 * 用户分享生成临时二维码
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result shareServiceCode(String type , String id , String sourceUserId , String hostName,
			int port,Integer platformId,String Appid, String Secret,
			String Paysignkey, String Mchid){
		
		logger.info("QrcodeInfoServiceImpl.shareServiceCode start ");
		
		Result result = new Result<>(StatusCodes.OK, true);
		//信息封装
		JSONObject json = new JSONObject();  
		//
		switch (type) {
		case "1":// 服务
			ServiceInfoVO service = serviceInfoMapper.selectVOByPrimaryKey(Integer.parseInt(id));
			
			if(service!=null){
				json.put("name", service.getName());
				json.put("desc", service.getDescription());       //描述
				json.put("createName", service.getCreateName());  //创建人
			}
			break;
		case "2":// 医生
			BackstageUserInfo doctor = backUserInfoMapper.selectByPrimaryKey(id);
			if(doctor!=null){
				json.put("name", doctor.getName());
				json.put("desc", doctor.getDesc()); //用户描述
				json.put("createName", "");
			}
			break;
		case "3":// 机构
			BackstageUserInfo user = backUserInfoMapper.selectByPrimaryKey(id);
			if(user!=null){
				json.put("name", user.getName());
				json.put("desc", user.getDesc()); //用户描述
				json.put("createName", "");
			}
			break;
		case "4":// 商品
			GoodsInfoVO goods =  goodsInfoMapper.selectVoByPrimaryKey(Integer.parseInt(id));
			if(goods!=null){
				json.put("name", goods.getGoodsName());
				json.put("desc", goods.getGoodsDesc());
				json.put("createName", goods.getCreateUserDesc());  //创建人
			}
			break;
		}
		String InvitationCode = "2,"+type+","+id+","+sourceUserId; // 结构 2,type,id
		String ticket = QRCodeTemporary(InvitationCode,weixinProxy); //生产二维码
		
		json.put("ticket", ticket);
		
		logger.info("返回结果："+ json.toString());
		
		result.setData(json);
		return result;
	}

	public WeixinProxy getWeixinProxy(String hostName, int port, String Appid, String Secret,
			String Paysignkey, String Mchid) {

		if (weixinProxy != null) {
			return weixinProxy;
		} else {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,
					jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
					jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);

			WeixinPayAccount wxpayAccount = null;
			
			//建一个表来获取微信的全部信息。包含微信支付、imweixin
			wxpayAccount = new WeixinPayAccount(Appid, Secret,
					Paysignkey, Mchid, null, null, null, null, null);
			Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);

			settings.setTokenStorager(rts);

			WeixinProxy proxy = new WeixinProxy(settings);
			
			return proxy;
		}
	}
	/**
	 * 获取推广人属性列表
	 * @return
	 */
	public Result getQRType(){
		logger.info("QrcodeInfoServiceImpl.getQRType start ");
		logger.info("itemType = 32");

		Result result = new Result<>(StatusCodes.OK, true);
		DictionaryExample example = new DictionaryExample();
		example.createCriteria().andItemTypeEqualTo(32);
		List<Dictionary> dictionaryList = dictionaryMapper.selectByExample(example);
		result.setData(dictionaryList);
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		return result;
	}
}