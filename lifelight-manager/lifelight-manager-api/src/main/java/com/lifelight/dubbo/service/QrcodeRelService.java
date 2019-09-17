package com.lifelight.dubbo.service;

import com.lifelight.api.entity.QrcodeRel;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.OrderInfoVO;
import com.lifelight.common.result.Result;

public interface QrcodeRelService {

	/** 添加关系数据 */
	Result<QrcodeRel> addQrcodeRel(QrcodeRel qrcodeRel);
	
	/** 用户首次注册，更新数据表 */
	Result<QrcodeRel> firstLoginUser(ApiUserInfoVO user);
	
	/** 用户首次下单，更新数据表 */
	Result<QrcodeRel> firstPayOrderUser(OrderInfoVO order);
}
