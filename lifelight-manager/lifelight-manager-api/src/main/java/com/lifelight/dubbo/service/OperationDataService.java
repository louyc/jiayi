package com.lifelight.dubbo.service;

import com.lifelight.api.entity.ApiUserInfo;
import com.lifelight.api.vo.ApiContractedUserVO;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.QrcodeInfoVO;
import com.lifelight.api.vo.UserCountVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;

public interface OperationDataService {

	/* 用户注册统计 */
	PaginatedResult<UserCountVO> countRegist(ApiUserInfoVO userInfo);

	PaginatedResult<ApiUserInfo> registDetaile(ApiUserInfoVO userInfo);

	/* 用户分析 */
	Result userAnalysis(ApiUserInfoVO userInfo);

	Result queryProvince(String id);

	/* 转换率统计 */
	PaginatedResult<UserCountVO> countconversion(ApiUserInfoVO userInfo);

	/* 推广码统计 */
	PaginatedResult<QrcodeInfoVO> countQrcode(QrcodeInfoVO qrcodeInfo);
	
	/* 签约统计 */
	PaginatedResult<UserCountVO> signCount(ApiUserInfoVO qrcodeInfo);
	/* 签约统计 查看明细 */
	PaginatedResult<ApiContractedUserVO> signDetaile(ApiUserInfoVO userInfo);
	/* 签约统计 分析*/
	Result signAnalysis(ApiUserInfoVO userInfo);
	/* 签约统计 家庭组明细*/
	Result familyDetail(UserCountVO userInfo);
	/* 家庭组统计*/
	Result familyAnalysis(ApiUserInfoVO userInfo);
}
