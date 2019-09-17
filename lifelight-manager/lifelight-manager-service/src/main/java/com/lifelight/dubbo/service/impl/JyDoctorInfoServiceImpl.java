package com.lifelight.dubbo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.JyDoctorInfo;
import com.lifelight.api.entity.JyOrgDocRel;
import com.lifelight.api.entity.JySignTypeRel;
import com.lifelight.api.vo.JyDoctorInfoVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.BackstageUserInfoMapper;
import com.lifelight.dubbo.dao.JyOrgDocRelMapper;
import com.lifelight.dubbo.dao.JySignTypeRelMapper;
import com.lifelight.dubbo.service.JyDoctorInfoService;

@Service
public class JyDoctorInfoServiceImpl implements JyDoctorInfoService {

	@Autowired
	private JyOrgDocRelMapper jyOrgDocRelMapper;
	
	@Autowired
	private JySignTypeRelMapper jySignTypeRelMapper;
	
	@Autowired
	private BackstageUserInfoMapper backstageUserInfoMapper;

	@Override
	public Result<List<JyDoctorInfoVO>> findJyDoctorInfo(JyDoctorInfo entity) {
		Result<List<JyDoctorInfoVO>> result = new Result<>(StatusCodes.OK, true);
		List<JyDoctorInfoVO> doctorList =  jyOrgDocRelMapper.findJyDoctorInfo(entity);
		result.setData(doctorList);
		return result;
	}
	
	@Override
	public PaginatedResult<JyDoctorInfoVO> getJySignDoctorInfo(JyDoctorInfo entity) {
		//创建返回对象
		PaginatedResult<JyDoctorInfoVO> result = null;
		List<JyDoctorInfoVO> doctorInfoArray = new ArrayList<>();
		//分页查询数据
		if (0 < entity.getCurrentPage() && 0 < entity.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) jyOrgDocRelMapper.countJySignDoctor(entity);
			//计算分页
			int beginSize = (entity.getCurrentPage()-1) * entity.getShowCount();
			entity.setCurrentResult(beginSize);
			//查询列表
			doctorInfoArray = jyOrgDocRelMapper.selectJySignDoctor(entity);
			result = new PaginatedResult<JyDoctorInfoVO>(doctorInfoArray,
					entity.getCurrentPage(), entity.getShowCount(), totalCount);
		}else{
			doctorInfoArray = jyOrgDocRelMapper.selectJySignDoctor(entity);
			result = new PaginatedResult<>(doctorInfoArray,-1, -1,doctorInfoArray.size());
		}
		
		return result;
	}

	@Override
	public Result<Object> changeSignType(Integer signId , List<Integer> types,Integer platformId) {
		Result<Object> result = new Result<>(StatusCodes.OK, true);
		// 查询关系数据
		JyOrgDocRel jyOrgDocRel = jyOrgDocRelMapper.selectByPrimaryKey(signId);
		// 验证数据
		if(jyOrgDocRel == null){
			result.setResultCode(new ResultCode("fell", "修改失败，关系数据不存在"));
			return result;
		}
		
		if(jyOrgDocRel.getOrgId() == null || "".equals(jyOrgDocRel.getOrgId())){
			result.setResultCode(new ResultCode("fell", "修改失败，关联机构不存在"));
			return result;
		}
		BackstageUserInfo backUser = backstageUserInfoMapper.selectByPrimaryKey(jyOrgDocRel.getOrgId());
		
		if(backUser == null){
			result.setResultCode(new ResultCode("fell", "修改失败，关联机构不存在"));
			return result;
		}
		
		if(!"4".equals(backUser.getType())){
			result.setResultCode(new ResultCode("fell", "修改失败，非家医机构不得修改医生类型"));
			return result;
		}
		
		// 清空关系对象关联的所有医生类型
		jySignTypeRelMapper.deleteBySignRelId(signId);
		// 批量添加关系数据
		if(types != null && types.size() > 0){
			List<JySignTypeRel> typeList = new ArrayList<>();
			for(Integer jyType : types){
				typeList.add(new JySignTypeRel(signId, jyOrgDocRel.getOrgId(), jyOrgDocRel.getDoctorId(), jyType));
			}
			// 批量添加数据
			jySignTypeRelMapper.insertList(typeList);
		}
		result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		return result;
	}
}
