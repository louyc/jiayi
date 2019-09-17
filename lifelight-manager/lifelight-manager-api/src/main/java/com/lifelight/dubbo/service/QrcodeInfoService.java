package com.lifelight.dubbo.service;

import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.QrApi;
import com.lifelight.api.entity.QrcodeInfo;
import com.lifelight.common.result.Result;

public interface QrcodeInfoService {
	
	/**
	 * 二维码表新增
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result createQrcodeInfo(QrcodeInfo qrCodeInfo,String hostName, int port,
			Integer platformId,String Appid, String Secret,
			String Paysignkey, String Mchid);
	
	/**
	 * 医生分享二维码表新增
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Result createDoctorQrcodeInfo(QrcodeInfo qrCodeInfo,String hostName, int port,
			Integer platformId,String Appid, String Secret,
			String Paysignkey, String Mchid);
	
	/**
	 * 获取所有二维码
	 */
	@SuppressWarnings("rawtypes")
	Result getAllQrcodeInfoListPage(QrcodeInfo qrcodeInfo);
	
	/**
	 * 根据 推广人，医生id 查询推广码
	 * @author quanlj  2017-08-14
	 * @param personName
	 * @param doctorId
	 * @return
	 */
	QrcodeInfo getQrcodeByQrPerson( String personName , String doctorId ,Integer weixinId);
	
	/**
	 *  根据分享类型，生成分享二维码及信息
	 * @param type
	 * @param id
	 * @param hostName
	 * @param port
	 * @param Appid
	 * @param Secret
	 * @param Paysignkey
	 * @param Mchid
	 * @return
	 */
	Result shareServiceCode(String type , String id , String sourceUserId ,String hostName,
			int port,Integer platformId,String Appid, String Secret,
			String Paysignkey, String Mchid);


	/**
	 * 获取推广人属性列表
	 * @return
	 */
	Result getQRType();
}