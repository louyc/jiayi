package com.lifelight.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lifelight.api.entity.AddressCity;
import com.lifelight.api.entity.AddressProvince;
import com.lifelight.api.entity.AddressTown;
import com.lifelight.api.entity.Country;
import com.lifelight.api.entity.Nation;
import com.lifelight.common.result.Result;
import com.lifelight.dubbo.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController {

private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
	
	@Reference
	private AddressService addressService;

	@RequestMapping("/getAllProvince")
	@ResponseBody
	public Result<List<AddressProvince>> getAllProvince(){
		logger.info("AddressController.getAllProvince start, select");
		return addressService.getAllProvince();
	}
	
	@RequestMapping("/getCityByProvinceCode")
	@ResponseBody
	public Result<List<AddressCity>> getCityByProvinceCode(@RequestParam("provinceCode") String provinceCode ){
		logger.info("AddressController.getCityByProvinceCode start, provinceCode="+provinceCode);
		return addressService.getCityByProvinceCode(provinceCode);
	}
	
	@RequestMapping("/getTownByTownCode")
	@ResponseBody
	Result<List<AddressTown>> getTownByTownCode(@RequestParam("cityCode") String cityCode){
		logger.info("AddressController.getTownByTownCode start, cityCode="+cityCode);
		return addressService.getTownByTownCode(cityCode);
	};
	@RequestMapping("/getAllCountry")
	@ResponseBody
	Result<List<Country>> getAllCountry(){
		logger.info("AddressController.getAllCountry start");
		return addressService.getAllCountry();
	};
	@RequestMapping("/getAllNation")
	@ResponseBody
	Result<List<Nation>> getAllNation(){
		logger.info("AddressController.getAllNation start");
		return addressService.getAllNation();
	};
	
}
