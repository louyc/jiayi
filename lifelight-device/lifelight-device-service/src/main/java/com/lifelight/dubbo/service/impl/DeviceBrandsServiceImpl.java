package com.lifelight.dubbo.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.DeviceBrands;
import com.lifelight.api.entity.DeviceBrandsExample;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DeviceBrandsMapper;
import com.lifelight.dubbo.dao.DeviceInfoMapper;
import com.lifelight.dubbo.service.DeviceBrandsService;

@Service
public class DeviceBrandsServiceImpl implements DeviceBrandsService {
	
	//private static final Logger logger = LoggerFactory.getLogger(DeviceBrandsServiceImpl.class);

	@Autowired
	private DeviceBrandsMapper deviceBrandsMapper;
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	
	/**
	 * 
	 * getAllDeviceBrands:获取所有设备品牌. <br/> 
	 * @return List<DeviceBrands>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result selectDeviceBrandsListPage(DeviceBrands deviceBrands){
		
		List<DeviceBrands> deviceBrandsList = deviceBrandsMapper.selectDeviceBrandsListPage(deviceBrands);
		
		//总页数
		int totalCount = deviceBrands.getTotalResult();
		
		PaginatedResult<DeviceBrands> pa = new PaginatedResult<DeviceBrands>(deviceBrandsList, deviceBrands.getCurrentPage(), deviceBrands.getShowCount(), totalCount);
		
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
    }
	
	/**
	 * 
	 * getAllDeviceFirms:获取设备品牌 无分页<br/> 
	 * @param deviceBrands
	 * @return List<deviceBrands>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getDeviceBrands(Integer platformId){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceBrandsExample example = new DeviceBrandsExample();
		example.createCriteria().andIsdelEqualTo("F").andPlatformIdEqualTo(platformId);
		List<DeviceBrands> deviceBrandsList = deviceBrandsMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(deviceBrandsList);
		return result;
    }
	
	/**
	 * 
	 * insertDeviceBrand:创建设备品牌. <br/> 
	 * @param deviceBrands
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result insertDeviceBrand(DeviceBrands deviceBrands){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == deviceBrands) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		DeviceBrands deviceBrandsByName = deviceBrandsMapper.getDeviceBrandByName(deviceBrands);
		
		if(null != deviceBrandsByName) {
			result.setResultCode(new ResultCode("FALSE", "设备品牌名称相同！"));
			return result;
		}
		
		deviceBrands.setCreateTime(new Date());
		deviceBrands.setUpdateTime(new Date());
        Integer num = deviceBrandsMapper.insertSelective(deviceBrands);
        
        if(1 == num){
        	result.setResultCode(new ResultCode("SUCCESS", "保存成功"));
        }else{
        	result.setResultCode(new ResultCode("FAILED", "保存失败"));
        }
        return result;
	}
	
	/**
	 * 
	 * updateDeviceBrand:修改设备品牌. <br/> 
	 * @param deviceBrands
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result updateDeviceBrand(DeviceBrands deviceBrands){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(deviceBrands == null ) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		DeviceBrands deviceBrandsByName = deviceBrandsMapper.getDeviceBrandBy(deviceBrands);
		
		if(null != deviceBrandsByName) {
			result.setResultCode(new ResultCode("FALSE", "设备品牌名称相同！"));
			return result;
		}
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		deviceBrands.setUpdateTime(now);
		
		Integer num = deviceBrandsMapper.updateByPrimaryKeySelective(deviceBrands);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "修改成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "修改失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * deleteDeviceBrand:删除设备品牌. <br/>  
	 * @param deviceBrands
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@Override
	public Result deleteDeviceBrand(Integer id,Integer platformId){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setBrandId(id);
		deviceInfo.setPlatformId(platformId);
		int brandCount = deviceInfoMapper.getDeviceInfoByBrandId(deviceInfo);
		
		if(brandCount > 0) {
			result.setResultCode(new ResultCode("FALSE", "设备已绑定该品牌，请勿删除！"));
			return result;
		}
		
		DeviceBrands deviceBrands = new DeviceBrands();
		deviceBrands.setId(id);
		deviceBrands.setIsdel("T");
		deviceBrands.setUpdateTime(new Date());
		
		Integer num = deviceBrandsMapper.updateByPrimaryKeySelective(deviceBrands);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "删除成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "删除失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * recoverDeviceBrand:撤销设备品牌. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result recoverDeviceBrand(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceBrands deviceBrands = deviceBrandsMapper.selectByPrimaryKey(id);
		deviceBrands.setIsdel("F");
		deviceBrands.setUpdateTime(new Date());
		
		Integer num = deviceBrandsMapper.updateByPrimaryKey(deviceBrands);
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "撤销成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "撤销失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * getDeviceBrandById:根据ID获取设备品牌信息. <br/>  
	 * @param id
	 * @return DeviceBrands
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unlikely-arg-type" })
	@Override
	public Result getDeviceBrandById(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		DeviceBrands deviceBrands = deviceBrandsMapper.getDeviceBrandById(id);
		
		result.setData(deviceBrands);
		if(null != deviceBrands){
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "查询失败"));
		}
		return result;
	}
	
	/**
	 * 根据厂商名称获取品牌ID
	 * @param brandName
	 */
	@Override
	public DeviceBrands getDeviceBrandByName(String brandName,Integer platformId){
		DeviceBrands deviceBrands = new DeviceBrands();
		deviceBrands.setBrandName(brandName);
		deviceBrands.setPlatformId(platformId);
		return deviceBrandsMapper.getDeviceBrandByName(deviceBrands);
	}
}