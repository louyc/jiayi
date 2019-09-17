package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.DeviceDefinition;
import com.lifelight.api.entity.DeviceInfo;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DeviceDefinitionMapper;
import com.lifelight.dubbo.dao.DeviceInfoMapper;
import com.lifelight.dubbo.service.DeviceInfoService;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {

	//private static final Logger logger = LoggerFactory.getLogger(DeviceInfoServiceImpl.class);
	
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	@Autowired
	private DeviceDefinitionMapper deviceDefinitionMapper;
	
	/**
	 * 
	 * getAllDeviceInfo:获取所有设备. <br/> 
	 * @return List<DeviceInfo>
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result selectDeviceInfoListPage(DeviceInfo deviceInfo){
		
		List<DeviceInfo> deviceInfoList = deviceInfoMapper.selectDeviceInfoListPage(deviceInfo);
		
		//总页数
		int totalCount = deviceInfo.getTotalResult();
		
		PaginatedResult<DeviceInfo> pa = new PaginatedResult<DeviceInfo>(deviceInfoList, deviceInfo.getCurrentPage(), deviceInfo.getShowCount(), totalCount);
		
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
    }
	
	/**
	 * 
	 * insertDeviceInfo:插入设备. <br/> 
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result insertDeviceInfo(DeviceInfo deviceInfo){
		Result result = new Result<>(StatusCodes.OK, true);
		if(null == deviceInfo) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		/*DeviceInfo deviceInfoByName = deviceInfoMapper.getDeviceInfoByName(deviceInfo.getDeviceName());
		if(null != deviceInfoByName) {
			result.setResultCode(new ResultCode("FALSE", "设备名称相同！"));
			return result;
		}*/
		DeviceInfo deviceInfoByName = deviceInfoMapper.getDeviceInfoByDeviceCodeAndDefineId(deviceInfo);
		if(null != deviceInfoByName) {
			result.setResultCode(new ResultCode("FALSE", "此设备定义下机械码有重复"));
			return result;
		}
		deviceInfo.setCreateTime(new Date());
		deviceInfo.setUpdateTime(new Date());
		Integer num = deviceInfoMapper.insertSelective(deviceInfo);
		result.setData(deviceInfo);
        if(num > 0){
        	result.setResultCode(new ResultCode("SUCCESS", "创建成功"));
        }else{
        	result.setResultCode(new ResultCode("FAILED", "创建失败"));
        }
        return result;
	}
	/**
	 * 
	 * insertListDeviceInfo:批量插入设备. <br/> 
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result insertListDeviceInfo(List<DeviceInfo> deviceInfoList){
		Result result = new Result<>(StatusCodes.OK, true);
		List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
		if(null !=deviceInfoList && deviceInfoList.size()>0 ) {
			for(DeviceInfo devi: deviceInfoList) {
				DeviceInfo deviceInfoByName = deviceInfoMapper.getDeviceInfoByDeviceCodeAndDefineId(devi);
				if(null != deviceInfoByName) {
					result.setResultCode(new ResultCode("FALSE", "此设备定义下机械码有重复"));
					return result;
				}
				/*deviceInfoByName = deviceInfoMapper.getDeviceInfoByName(devi.getDeviceName());
				if(null != deviceInfoByName) {
					result.setResultCode(new ResultCode("FALSE", "文档中设备名称和数据库中有同名！"));
					return result;
				}*/
				DeviceDefinition defi = deviceDefinitionMapper.selectByPrimaryKey(devi.getDefinitionId());
				devi.setFirmId(defi.getDeviceFirmid());
				devi.setBrandId(defi.getDeviceBrandid());
//				if(defi.getType()==0) { 
//					devi.setIsPutaway("T");
//				}else {//上架
				devi.setIsPutaway("F");//下架
				devi.setIsHired("F");  //未租
//				}
				devi.setDeviceDesc(defi.getDefinitionDesc());
				devi.setDeviceSpec(defi.getDefinitionSpec());
				devi.setIsdel(defi.getIsdel());
				devi.setNetworkingType(defi.getNetworkingType());
				devi.setCreateTime(new Date());
				devi.setUpdateTime(new Date());
				deviceInfos.add(devi);
			}
		}
		if(deviceInfos.size()>0) {
			deviceInfoMapper.insertList(deviceInfos);
		}
		result.setResultCode(new ResultCode("SUCCESS", "创建成功"));
		return result;
	}
	
	/**
	 * 
	 * updateDeviceInfo:修改设备. <br/> 
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result updateDeviceInfo(DeviceInfo deviceInfo){
		Result result = new Result<>(StatusCodes.OK, true);
		if(deviceInfo == null ) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
	/*	DeviceInfo deviceInfoByName = deviceInfoMapper.getDeviceInfoBy(deviceInfo);
		if(null != deviceInfoByName) {
			result.setResultCode(new ResultCode("FALSE", "设备名称相同！"));
			return result;
		}*/
		DeviceInfo deviceInfoByName = deviceInfoMapper.getDeviceInfoByDeviceCodeAndDefineIdAndID(deviceInfo);
		if(null != deviceInfoByName) {
			result.setResultCode(new ResultCode("FALSE", "此设备定义下机械码有重复"));
			return result;
		}
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		deviceInfo.setUpdateTime(now);
		
		Integer num = deviceInfoMapper.updateByPrimaryKeySelective(deviceInfo);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "修改成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "修改失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * deleteDeviceInfo:删除设备. <br/>  
	 * @param deviceInfo
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@Override
	public Result deleteDeviceInfo(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setId(id);
		deviceInfo.setIsdel("T");
		deviceInfo.setUpdateTime(new Date());
		
		Integer num = deviceInfoMapper.updateByPrimaryKey(deviceInfo);
		
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "删除成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "删除失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * recoverDeviceInfo:撤销删除. <br/>  
	 * @param id
	 * @return Integer
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result recoverDeviceInfo(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(id);
		deviceInfo.setIsdel("F");
		deviceInfo.setUpdateTime(new Date());
		
		Integer num = deviceInfoMapper.updateByPrimaryKey(deviceInfo);
		if(num > 0){
			result.setResultCode(new ResultCode("SUCCESS", "撤销成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "撤销失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * getDeviceInfoById:根据ID获取设备详情. <br/>  
	 * @param id
	 * @return DeviceBrands
	 * @exception 
	 * @author jinlu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unlikely-arg-type" })
	@Override
	public Result getDeviceInfoById(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == id || "".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		DeviceInfo deviceInfo = deviceInfoMapper.getDeviceInfoById(id);
		
		result.setData(deviceInfo);
		if(null != deviceInfo){
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "查询失败"));
		}
		return result;
	}
	
	/**
	 * 
	 * 根据设备号 获取 设备ID
	 * @param deviceCode
	 * @return
	 */
	@Override
	public Integer getDeviceIdByDeviceCode(String deviceCode,String weixinId) {
		if(null == deviceCode || "".equals(deviceCode)) {
			return null;
		}
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDeviceCode(deviceCode);
		if(!StringUtil.isEmpty(weixinId)) {
			deviceInfo.setPlatformId(Integer.parseInt(weixinId));
		}
		return deviceInfoMapper.getDeviceIdByDeviceCode(deviceInfo);
	}
}