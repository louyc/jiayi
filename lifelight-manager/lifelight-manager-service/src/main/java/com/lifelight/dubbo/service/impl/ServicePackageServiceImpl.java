package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.Dictionary;
import com.lifelight.api.entity.ServicePackage;
import com.lifelight.api.entity.ServicePackageRel;
import com.lifelight.api.entity.ServicePackageRelExample;
import com.lifelight.api.entity.XlPersonContract;
import com.lifelight.api.entity.XlPersonContractExample;
import com.lifelight.api.vo.ServicePackageVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.DictionaryMapper;
import com.lifelight.dubbo.dao.ServicePackageMapper;
import com.lifelight.dubbo.dao.ServicePackageRelMapper;
import com.lifelight.dubbo.dao.XlPersonContractMapper;
import com.lifelight.dubbo.service.ServicePackageService;

@Service
public class ServicePackageServiceImpl implements ServicePackageService{
	
	private static final Logger logger = LoggerFactory.getLogger(ServicePackageServiceImpl.class);
	@Autowired
	private ServicePackageMapper servicePackageMapper;
	@Autowired
	private ServicePackageRelMapper servicePackageRelMapper;
	@Autowired
	private XlPersonContractMapper xlPersonContractMapper;
	@Autowired
	private DictionaryMapper dictionaryMapper;

	/**
	 * 创建服务包
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result createServicePackage(ServicePackageVO servicePackage) {
		Result result = new Result<>(StatusCodes.OK, true);

		if("".equals(servicePackage.getPackageName()) || null == servicePackage.getPackageName()) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		servicePackage.setCreateTime(now);
		servicePackage.setUpdateTime(now);
		servicePackage.setInUse(1);
		//判断是否存在重复名称
		int count_num = servicePackageMapper.getObjectCountByName(servicePackage);

		if(count_num > 0) {
			result.setResultCode(new ResultCode("FALSE", "已经存在相同名称的标签！"));
			return result;
		}
		int num = servicePackageMapper.insertSelective(servicePackage);
		if(!StringUtil.isEmpty(servicePackage.getDicIds()) && servicePackage.getDicIds().split(",").length>0) {
			for(String s: servicePackage.getDicIds().split(",")) {
				ServicePackageRel record = new ServicePackageRel();
				record.setCreateTime(new Date());
				record.setDictionaryId(Integer.parseInt(s));
				record.setPackageId(servicePackage.getId());
				servicePackageRelMapper.insert(record);
			}
		}
		if( num != 0 ) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		}else {
			result.setResultCode(new ResultCode("FALSE", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除服务包
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deleteServicePackage(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			ServicePackage sp = servicePackageMapper.selectByPrimaryKey(id);
			XlPersonContractExample query = new XlPersonContractExample();
			query.createCriteria().andServicePackageEqualTo(String.valueOf(sp.getId()));
			List<XlPersonContract> listContract = xlPersonContractMapper.selectByExample(query);
			if(null!=listContract || listContract.size()>0) {
				result.setResultCode(new ResultCode("FALSE", "当前服务包已被签约用户选择"));
			}
			sp.setInUse(0);
			servicePackageMapper.updateByPrimaryKeySelective(sp);
			ServicePackageRelExample example = new ServicePackageRelExample();
			example.createCriteria().andPackageIdEqualTo(id);
			servicePackageRelMapper.deleteByExample(example);
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		}catch(Exception e) {
			e.printStackTrace();
			result.setResultCode(new ResultCode("FALSE", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改服务包
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result updateServicePackage(ServicePackageVO servicePackage) {
		Result result = new Result<>(StatusCodes.OK, true);

		if(servicePackage == null ) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		ServicePackage sp = servicePackageMapper.getServicePackageBy(servicePackage);
		if(null != sp) {
			result.setResultCode(new ResultCode("FALSE", "已经存在相同名称的服务包！"));
			return result;
		}
		try {
			Calendar c = Calendar.getInstance();
			Date now = c.getTime();
			servicePackage.setUpdateTime(now);
			servicePackageMapper.updateByPrimaryKeySelective(servicePackage);
			//删除服务包和检查项关联表数据
			ServicePackageRelExample example = new ServicePackageRelExample();
			example.createCriteria().andPackageIdEqualTo(servicePackage.getId());
			servicePackageRelMapper.deleteByExample(example);
			//重新插入关系数据
			for(String s:servicePackage.getDicIds().split(",")) {
				ServicePackageRel record = new ServicePackageRel();
				record.setCreateTime(new Date());
				record.setPackageId(servicePackage.getId());
				record.setDictionaryId(Integer.parseInt(s));
				servicePackageRelMapper.insert(record);
			}
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		}catch(Exception e) {
			e.printStackTrace();
			result.setResultCode(new ResultCode("FALSE", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询服务包
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result selectServicePackageListPage(ServicePackage servicePackage) {
		List<ServicePackageVO> servicePackageList = servicePackageMapper.
				selectServicePackageListPage(servicePackage);
		//总页数
		int totalCount = servicePackage.getTotalResult();
		for(ServicePackageVO spvo: servicePackageList) {
			ServicePackageRelExample example = new ServicePackageRelExample();
			example.createCriteria().andPackageIdEqualTo(spvo.getId());
			List<ServicePackageRel> rel = servicePackageRelMapper.selectByExample(example);
			List<Dictionary> listDic = new ArrayList<Dictionary>();
			if(null!=rel && rel.size()>0) {
				for(ServicePackageRel sp: rel) {
					Dictionary dic = dictionaryMapper.selectByPrimaryKey(sp.getDictionaryId());
					listDic.add(dic);
				}
			}
			spvo.setListDictionary(listDic);
		}
		PaginatedResult<ServicePackageVO> pa = new PaginatedResult<ServicePackageVO>
		(servicePackageList, servicePackage.getCurrentPage(), servicePackage.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

}