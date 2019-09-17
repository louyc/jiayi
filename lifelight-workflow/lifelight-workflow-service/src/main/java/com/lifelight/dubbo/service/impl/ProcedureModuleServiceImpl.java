package com.lifelight.dubbo.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.ProcedureModule;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.ProcedureDialogMapper;
import com.lifelight.dubbo.dao.ProcedureMapper;
import com.lifelight.dubbo.dao.ProcedureMessageMapper;
import com.lifelight.dubbo.dao.ProcedureModuleMapper;
import com.lifelight.dubbo.service.ProcedureModuleService;

@Service
public class ProcedureModuleServiceImpl implements ProcedureModuleService {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureModuleServiceImpl.class);

	@Autowired
	private ProcedureModuleMapper procedureModuleMapper;

	/**
	 * 创建流程模块
	 * 
	 * @param ProcedureModule
	 * @return
	 */
	@Override
	public Result<?> createProcedureModule(ProcedureModule ProcedureModule) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (null == ProcedureModule) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		ProcedureModule.setId(RandomStringUtils.randomAlphanumeric(8));
		ProcedureModule.setCreateTime(new Date());
		ProcedureModule.setUpdateTime(new Date());
		int num = procedureModuleMapper.insertSelective(ProcedureModule);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除流程模块
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deleteProcedureModule(String id) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = procedureModuleMapper.deleteByPrimaryKey(id);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改流程模块
	 * 
	 * @param ProcedureModule
	 * @return
	 */
	@Override
	public Result<?> updateProcedureModule(ProcedureModule ProcedureModule) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(ProcedureModule.getId())) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		ProcedureModule.setUpdateTime(new Date());

		int num = procedureModuleMapper.updateByPrimaryKeyWithBLOBs(ProcedureModule);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 根据id查询流程模块
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<ProcedureModule> getProcedureModuleById(String id) {
		Result<ProcedureModule> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		ProcedureModule ProcedureModule = procedureModuleMapper.selectByPrimaryKey(id);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		result.setData(ProcedureModule);
		return result;
	}

	/**
	 * 查询流程模块
	 * 
	 * @param ProcedureModule
	 * @return
	 */
	@Override
	public PaginatedResult<ProcedureModule> selectProcedureModuleListPage(ProcedureModule ProcedureModule) {

		List<ProcedureModule> ProcedureModuleList = procedureModuleMapper
				.selectProcedureModuleListPage(ProcedureModule);

		// 总页数
		int totalCount = ProcedureModule.getTotalResult();

		PaginatedResult<ProcedureModule> pa = new PaginatedResult<ProcedureModule>(ProcedureModuleList,
				ProcedureModule.getCurrentPage(), ProcedureModule.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}
}