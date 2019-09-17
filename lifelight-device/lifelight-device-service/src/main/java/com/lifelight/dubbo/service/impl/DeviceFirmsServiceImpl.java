package com.lifelight.dubbo.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.DeviceBrands;
import com.lifelight.api.entity.DeviceFirms;
import com.lifelight.api.entity.DeviceFirmsExample;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DeviceBrandsMapper;
import com.lifelight.dubbo.dao.DeviceFirmsMapper;
import com.lifelight.dubbo.dao.DeviceInfoMapper;
import com.lifelight.dubbo.service.DeviceFirmsService;

@Service
public class DeviceFirmsServiceImpl implements DeviceFirmsService {

	//private static final Logger logger = LoggerFactory.getLogger(DeviceFirmsServiceImpl.class);
	
	@Autowired
	private DeviceFirmsMapper deviceFirmsMapper;
	@Autowired
	private DeviceBrandsMapper deviceBrandsMapper;
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	
	/**
	 * 
	 * getAllDeviceFirms:获取设备厂商. <br/> 
	 * @param deviceFirms
	 * @return List<DeviceFirms>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result selectDeviceFirmsListPage(DeviceFirms deviceFirms){
		
		List<DeviceFirms> deviceFirmsList = deviceFirmsMapper.selectDeviceFirmsListPage(deviceFirms);
		
		//总页数
		int totalCount = deviceFirms.getTotalResult();
		
		PaginatedResult<DeviceFirms> pa = new PaginatedResult<DeviceFirms>(deviceFirmsList, deviceFirms.getCurrentPage(), deviceFirms.getShowCount(), totalCount);
		
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
    }
	
	/**
	 * 
	 * getAllDeviceFirms:获取设备厂商 无分页. <br/> 
	 * @param deviceFirms
	 * @return List<DeviceFirms>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getDeviceFirms(Integer platformId){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceFirmsExample example = new DeviceFirmsExample();
		example.createCriteria().andIsdelEqualTo("F").andPlatformIdEqualTo(platformId);
		List<DeviceFirms> deviceFirmsList = deviceFirmsMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(deviceFirmsList);
		return result;
    }
	
	/**
	 * 
	 * insertDeviceFirm:添加设备厂商. <br/> 
	 * @param deviceFirms
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result insertDeviceFirm(DeviceFirms deviceFirms){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == deviceFirms) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		DeviceFirms deviceFirmsByName = deviceFirmsMapper.getDeviceFirmByName(deviceFirms);
		
		if(null != deviceFirmsByName) {
			result.setResultCode(new ResultCode("FALSE", "设备厂商名称相同！"));
			return result;
		}
		
		deviceFirms.setCreateTime(new Date());
		deviceFirms.setUpdateTime(new Date());
        Integer num = deviceFirmsMapper.insertSelective(deviceFirms);
        
        if(num > 0){
        	result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
        }else{
        	result.setResultCode(new ResultCode("FAILED", "添加失败"));
        }
        return result;
	}
	
	/**
	 * 
	 * updateDeviceFirm:修改设备厂商. <br/> 
	 * @param deviceFirms
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result updateDeviceFirm(DeviceFirms deviceFirms){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(deviceFirms == null ) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		DeviceFirms deviceFirmsByName = deviceFirmsMapper.getDeviceFirmBy(deviceFirms);
		
		if(null != deviceFirmsByName) {
			result.setResultCode(new ResultCode("FALSE", "设备厂商名称相同！"));
			return result;
		}
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		deviceFirms.setUpdateTime(now);
		
		Integer num = deviceFirmsMapper.updateByPrimaryKeySelective(deviceFirms);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "修改成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "修改失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * deleteDeviceFirm:删除设备厂商. <br/>  
	 * @param deviceFirms
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@Override
	public Result deleteDeviceFirm(Integer id,Integer platformId){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		DeviceBrands deviceBrands = new DeviceBrands();
		deviceBrands.setPlatformId(platformId);
		deviceBrands.setDeviceFirmid(id);
		int firmsCount = deviceBrandsMapper.getDeviceBrandsByFirmsId(deviceBrands);
		
		if(firmsCount > 0) {
			result.setResultCode(new ResultCode("FALSE", "设备品牌已绑定该厂商，请勿删除！"));
			return result;
		}
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setFirmId(id);
		deviceInfo.setPlatformId(platformId);
		int deviceCount = deviceInfoMapper.getDeviceInfoByFirmsId(deviceInfo);
		
		if(deviceCount > 0) {
			result.setResultCode(new ResultCode("FALSE", "设备已绑定该厂商，请勿删除！"));
			return result;
		}
		
		DeviceFirms deviceFirms = new DeviceFirms();
		deviceFirms.setId(id);
		deviceFirms.setIsdel("T");
		deviceFirms.setUpdateTime(new Date());
		
		Integer num = deviceFirmsMapper.updateByPrimaryKeySelective(deviceFirms);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "删除成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "删除失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * recoverDeviceFirm:取消删除. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result recoverDeviceFirm(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceFirms deviceFirms = deviceFirmsMapper.selectByPrimaryKey(id);
		deviceFirms.setIsdel("F");
		deviceFirms.setUpdateTime(new Date());
		
		Integer num = deviceFirmsMapper.updateByPrimaryKey(deviceFirms);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "撤销成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "撤销失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * getDeviceFirmById:根据ID获取设备厂商信息. <br/>  
	 * @param id
	 * @return DeviceFirms
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unlikely-arg-type" })
	@Override
	public Result getDeviceFirmById(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		DeviceFirms deviceFirms = deviceFirmsMapper.selectByPrimaryKey(id);
		
		result.setData(deviceFirms);
		if(null != deviceFirms){
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "查询失败"));
		}
		return result;
	}
	
	/**
	 * 根据厂商名称获取厂商ID
	 * @param firmName
	 */
	@Override
	public DeviceFirms getDeviceFirmByName(String firmName,Integer platformId){
		DeviceFirms deviceFirms = new DeviceFirms();
		deviceFirms.setFirmName(firmName);
		deviceFirms.setPlatformId(platformId);
		return deviceFirmsMapper.getDeviceFirmByName(deviceFirms);
	}
}