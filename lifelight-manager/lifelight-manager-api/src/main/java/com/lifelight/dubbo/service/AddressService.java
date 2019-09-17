package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.AddressCity;
import com.lifelight.api.entity.AddressProvince;
import com.lifelight.api.entity.AddressTown;
import com.lifelight.api.entity.Country;
import com.lifelight.api.entity.Nation;
import com.lifelight.common.result.Result;

public interface AddressService {
	
	/**
	 * 获取所有民族
	 * @return
	 */
	Result<List<Nation>> getAllNation();
	/**
	 * 获取所有国家
	 * @return
	 */
	Result<List<Country>> getAllCountry();
	/**
	 * 获取所有省份
	 * @return
	 */
	Result<List<AddressProvince>> getAllProvince();
	
	/**
	 * 根据省份查询城市
	 * @param provinceCode
	 * @return
	 */
	Result<List<AddressCity>> getCityByProvinceCode(String provinceCode);
	
	/**
	 * 根据城市查询区县
	 * @param cityCode
	 * @return
	 */
	Result<List<AddressTown>> getTownByTownCode(String cityCode);

}
