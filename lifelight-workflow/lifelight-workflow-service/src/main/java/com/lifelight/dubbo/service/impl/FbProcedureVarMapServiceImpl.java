package com.lifelight.dubbo.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.FbProcedureVarMap;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.FbProcedureVarMapMapper;
import com.lifelight.dubbo.service.FbProcedureVarMapService;

@Service
public class FbProcedureVarMapServiceImpl implements FbProcedureVarMapService {

	@Autowired
	private FbProcedureVarMapMapper fpdMapper;

	/**
	 * 创建流程映射
	 * 
	 * @param procedureVar
	 * @return
	 */
	@Override
	public Result<?> createProcedureVar(FbProcedureVarMap procedureVar) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (null == procedureVar) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		procedureVar.setCreatedAt(new Date());
		procedureVar.setUpdatedAt(new Date());
		int num = fpdMapper.insertSelective(procedureVar);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除流程映射
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deleteProcedureVar(String id) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = fpdMapper.deleteByPrimaryKey(Integer.parseInt(id));

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改流程映射
	 * 
	 * @param procedureVar
	 * @return
	 */
	@Override
	public Result<?> updateProcedureVar(FbProcedureVarMap procedureVar) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (procedureVar == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		procedureVar.setUpdatedAt(new Date());

		int num = fpdMapper.updateByPrimaryKeySelective(procedureVar);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询流程映射
	 * 
	 * @param procedureVar
	 * @return
	 */
	@Override
	public PaginatedResult<FbProcedureVarMap> selectProcedureVarListPage(FbProcedureVarMap procedureVar) {

		List<FbProcedureVarMap> procedureVarList = fpdMapper.selectProcedureListPage(procedureVar);

		// 总页数
		int totalCount = procedureVar.getTotalResult();

		PaginatedResult<FbProcedureVarMap> pa = new PaginatedResult<FbProcedureVarMap>(procedureVarList,
				procedureVar.getCurrentPage(),
				procedureVar.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

}