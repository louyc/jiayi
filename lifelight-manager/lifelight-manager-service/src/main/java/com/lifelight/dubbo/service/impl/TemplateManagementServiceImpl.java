package com.lifelight.dubbo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.Message;
import com.lifelight.api.entity.TemplateManagement;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.MessageMapper;
import com.lifelight.dubbo.dao.TemplateManagementMapper;
import com.lifelight.dubbo.service.TemplateManagementService;

@Service
public class TemplateManagementServiceImpl implements TemplateManagementService {

	@Autowired
	private TemplateManagementMapper templateMapper;

	/**
	 * 创建模板
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> createTemplateManagement(TemplateManagement template) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (null == template) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		List<TemplateManagement> list = templateMapper.selectTemplates(template);
		if(null!=list && list.size()>0) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "相同类型的模板只能创建一个"));
		}
		template.setCreateTime(new Date());
		template.setUpdateTime(new Date());
		template.setInUse("1");
		int num = templateMapper.insertSelective(template);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}
	/**
	 *  初始化创建2条模板
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> createAllTemplateManagement(Integer platformId) {
		Result<?> result = new Result<>(StatusCodes.OK, true);
		TemplateManagement template = new TemplateManagement();
		template.setPlatformId(platformId);
		List<TemplateManagement> list = templateMapper.selectTemplates(template);
		if(null!=list && list.size()>0) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "当前平台已创建过模板"));
		}
		for(int i=1;i<3;i++) {
			template.setTemplateType(String.valueOf(i));
			template.setCreateTime(new Date());
			template.setUpdateTime(new Date());
			template.setInUse("1");
			templateMapper.insertSelective(template);
		}
		return result;
	}

	/**
	 * 删除模板
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deleteTemplateManagement(String id) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = templateMapper.deleteByPrimaryKey(Integer.parseInt(id));

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改模板
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> updateTemplateManagement(TemplateManagement template) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (template == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		template.setUpdateTime(new Date());

		int num = templateMapper.updateByPrimaryKeySelective(template);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询模板
	 * 
	 * @param message
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Result selectTemplateManagementListPage(TemplateManagement template) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<TemplateManagement> templateList = templateMapper.selectTemplates(template);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		Map map = new HashMap<String,Object>();
		map.put("templateList", templateList);
		result.setData(map);
		return result;
	}

}