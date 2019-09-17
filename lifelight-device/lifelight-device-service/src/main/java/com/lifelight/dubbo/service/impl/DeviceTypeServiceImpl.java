package com.lifelight.dubbo.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.DeviceType;
import com.lifelight.api.entity.DeviceTypeExample;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DeviceTypeMapper;
import com.lifelight.dubbo.service.DeviceTypeService;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

	//private static final Logger logger = LoggerFactory.getLogger(DeviceTypeServiceImpl.class);
	
	@Autowired
	private DeviceTypeMapper deviceTypeMapper;
	
	/**
	 * 
	 * getAllDeviceType:获取所有设备类型. <br/> 
	 * @return List<DeviceType>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getAllDeviceType(DeviceType deviceType){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceTypeExample example = new DeviceTypeExample();
		example.createCriteria().andPlatformIdEqualTo(deviceType.getPlatformId());
		List<DeviceType> deviceTypeList = deviceTypeMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(deviceTypeList);
		return result;
    }
	
	/**
	 * 
	 * insertDeviceType:添加设备类型. <br/> 
	 * @param deviceType
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result insertDeviceType(DeviceType deviceType){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == deviceType) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		DeviceType deviceTypeByName = deviceTypeMapper.getDeviceTypeByName(deviceType);
		
		if(null != deviceTypeByName) {
			result.setResultCode(new ResultCode("FALSE", "设备类型名称相同！"));
			return result;
		}
		
		deviceType.setCreateTime(new Date());
		deviceType.setUpdateTime(new Date());
		Integer num = deviceTypeMapper.insertSelective(deviceType);
        
        if(num > 0){
        	result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
        }else{
        	result.setResultCode(new ResultCode("FAILED", "添加失败"));
        }
        return result;
	}
	
	/**
	 * 
	 * updateDeviceType:修改设备类型. <br/> 
	 * @param deviceType
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result updateDeviceType(DeviceType deviceType){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(deviceType == null ) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		DeviceType deviceTypeByName = deviceTypeMapper.getDeviceTypeBy(deviceType);
		
		if(null != deviceTypeByName) {
			result.setResultCode(new ResultCode("FALSE", "设备类型名称相同！"));
			return result;
		}
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		deviceType.setUpdateTime(now);
		
		Integer num = deviceTypeMapper.updateByPrimaryKeySelective(deviceType);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "修改成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "修改失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * deleteDeviceType:删除设备类型. <br/>  
	 * @param deviceType
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@Override
	public Result deleteDeviceType(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		DeviceType deviceType = new DeviceType();
		deviceType.setIsdel("T");
		deviceType.setUpdateTime(new Date());
		
		Integer num = deviceTypeMapper.updateByPrimaryKey(deviceType);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "删除成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "删除失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * recoverDeviceType:撤销删除设备类型. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result recoverDeviceType(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceType deviceType = deviceTypeMapper.selectByPrimaryKey(id);
		deviceType.setIsdel("F");
		deviceType.setUpdateTime(new Date());
		
		Integer num = deviceTypeMapper.updateByPrimaryKey(deviceType);
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "撤销成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "撤销失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * getDeviceTypeById:根据ID获取设备类型详细. <br/>  
	 * @param id
	 * @return DeviceType
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unlikely-arg-type" })
	@Override
	public Result getDeviceTypeById(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		DeviceType deviceType = deviceTypeMapper.selectByPrimaryKey(id);
		
		result.setData(deviceType);
		if(null != deviceType){
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "查询失败"));
		}
		return result;
	}
}