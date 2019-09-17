package com.lifelight.dubbo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.AddressCity;
import com.lifelight.api.entity.AddressCityExample;
import com.lifelight.api.entity.AddressProvince;
import com.lifelight.api.entity.AddressProvinceExample;
import com.lifelight.api.entity.AddressTown;
import com.lifelight.api.entity.AddressTownExample;
import com.lifelight.api.entity.Country;
import com.lifelight.api.entity.CountryExample;
import com.lifelight.api.entity.Nation;
import com.lifelight.api.entity.NationExample;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.AddressCityMapper;
import com.lifelight.dubbo.dao.AddressProvinceMapper;
import com.lifelight.dubbo.dao.AddressTownMapper;
import com.lifelight.dubbo.dao.CountryMapper;
import com.lifelight.dubbo.dao.NationMapper;
import com.lifelight.dubbo.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Autowired
	private AddressProvinceMapper addressProvinceMapper;
	@Autowired
	private AddressCityMapper addressCityMapper;
	@Autowired
	private AddressTownMapper addressTownMapper;
	@Autowired
	private NationMapper nationMapper;
	@Autowired
	private CountryMapper countryMapper;
	
	@Override
	public Result<List<Nation>> getAllNation() {
		
		logger.info("AddressServiceImpl.getAllProvince START");
		
		Result<List<Nation>> result = new Result<>(StatusCodes.OK, true);
		List<Nation> provinceList = nationMapper.selectByExample(new NationExample());
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(provinceList);
		
		return result;
	}
	@Override
	public Result<List<Country>> getAllCountry() {
		
		logger.info("AddressServiceImpl.getAllProvince START");
		
		Result<List<Country>> result = new Result<>(StatusCodes.OK, true);
		List<Country> provinceList = countryMapper.selectByExample(new CountryExample());
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(provinceList);
		
		return result;
	}
	@Override
	public Result<List<AddressProvince>> getAllProvince() {
		
		logger.info("AddressServiceImpl.getAllProvince START");
		
		Result<List<AddressProvince>> result = new Result<>(StatusCodes.OK, true);
		List<AddressProvince> provinceList = addressProvinceMapper.selectByExample(new AddressProvinceExample());
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(provinceList);
		
		return result;
	}

	@Override
	public Result<List<AddressCity>> getCityByProvinceCode(String provinceCode) {
		
		logger.info("AddressServiceImpl.getAllProvince START provinceCode="+provinceCode);
		
		Result<List<AddressCity>> result = new Result<>(StatusCodes.OK, true);
		
		AddressCityExample example = new AddressCityExample();
		example.createCriteria().andProvincecodeEqualTo(provinceCode);
		List<AddressCity> cityList = addressCityMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(cityList);
		
		return result;
	}

	@Override
	public Result<List<AddressTown>> getTownByTownCode(String cityCode) {

		logger.info("AddressServiceImpl.getAllProvince START cityCode="+cityCode);
		
		Result<List<AddressTown>> result = new Result<>(StatusCodes.OK, true);
		
		AddressTownExample example = new AddressTownExample();
		example.createCriteria().andCitycodeEqualTo(cityCode);
		List<AddressTown> townList = addressTownMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(townList);
		
		return result;
	}

}
