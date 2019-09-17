package com.lifelight.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.BackstageUserInfo;
import com.lifelight.api.entity.UserDoctorRel;
import com.lifelight.api.entity.XlPersonContract;
import com.lifelight.api.entity.XlPersonDocument;
import com.lifelight.api.vo.BackstageUserInfoVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.BackstageUserInfoMapper;
import com.lifelight.dubbo.dao.UserDoctorRelMapper;
import com.lifelight.dubbo.dao.XlPersonContractMapper;
import com.lifelight.dubbo.dao.XlPersonDocumentMapper;
import com.lifelight.dubbo.service.UserDoctorRelService;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kwinxu on 2017/12/18.
 */
@Service
public class UserDoctorRelServiceImpl implements UserDoctorRelService {

    @Autowired
    private UserDoctorRelMapper userDoctorRelMapper;
    @Autowired
	private BackstageUserInfoMapper backUserMapper;
    @Autowired
	private XlPersonContractMapper contractMapper;
    @Autowired
	private XlPersonDocumentMapper documentMapper;
    /**
     * 添加孕产妇签约信息
     *
     * @param userDoctorRel
     * @return
     */
    @Override
    public int add(UserDoctorRel userDoctorRel) throws Exception {
        if (null == userDoctorRel.getContractedUserId()) {
            throw new Exception("缺失孕产妇档案！");
        }
        if (null == userDoctorRel.getManagerId()) {
            throw new Exception("缺少用户关联！");
        }
        if (null == userDoctorRel.getDoctorId()) {
            throw new Exception("缺少医生信息！");
        }
        userDoctorRel.setInUse(1);
        userDoctorRel.setBuildType(3);
        userDoctorRel.setCreateTime(new Date());
        return userDoctorRelMapper.insertSelective(userDoctorRel);
    }
    
    /**
	 * 查询医生
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryRel(UserDoctorRel udRel) {
		Result result = new Result<>(StatusCodes.OK, true);
		udRel.setBuildType(2);// 查询申请服务的关系
		List<UserDoctorRel> rels = userDoctorRelMapper.selectByEntity(udRel);
		if (null != rels && rels.size() > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		}
		result.setData(rels);
		return result;
	}
	
	public List<UserDoctorRel> queryDocRel(String managerId, String doctorId) {
		UserDoctorRel userRel = new UserDoctorRel();
		userRel.setManagerId(managerId);
		userRel.setDoctorId(doctorId);
		return userDoctorRelMapper.selectByEntity(userRel);
	}
	
	/**
	 * 查询医生
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryDoctors(String managerId,Integer platformId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<BackstageUserInfoVO> listBuser = new ArrayList<BackstageUserInfoVO>();
		List<BackstageUserInfoVO> returnBuser = new ArrayList<BackstageUserInfoVO>();
		listBuser = userDoctorRelMapper.selectByMangerId(managerId);
		if (null != listBuser && listBuser.size() > 0) {
			for (BackstageUserInfoVO b : listBuser) {
				if(b.getBuildType()==1) {
					String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					String outTime = new SimpleDateFormat("yyyy-MM-dd").format(b.getExpirationDate());
					if (nowTime.compareTo(outTime) >= 0) {//签约过期  
//						userDoctorRelMapper.updateInuse(b.getId());
						XlPersonContract record = new XlPersonContract();
						record.setId(b.getContractedUserId());
						record.setSignStatus("2");//签约过期
						contractMapper.updateByPrimaryKeySelective(record);
					}
					if (!StringUtil.isEmpty(b.getDesignOrgId())) {
						BackstageUserInfo user = backUserMapper
								.selectByPrimaryKey(b.getDesignOrgId());
						b.setDesignOrgName(user.getName());
					}
					returnBuser.add(b);
				}
			}
		}
		result.setData(returnBuser);
		return result;
	}
	/**
	 *查询所有和用户相关的医生信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryAllDoctors(String managerId,Integer platformId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<BackstageUserInfoVO> listBuser = new ArrayList<BackstageUserInfoVO>();
		Set<BackstageUserInfoVO> returnBuser = new HashSet<BackstageUserInfoVO>();
		listBuser = userDoctorRelMapper.selectByMangerId(managerId);
		if (null != listBuser && listBuser.size() > 0) {
			for (BackstageUserInfoVO b : listBuser) {
				if(b.getBuildType()!=2) {
					String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					String outTime = new SimpleDateFormat("yyyy-MM-dd").format(b.getExpirationDate());
					if (nowTime.compareTo(outTime) >= 0) {
						userDoctorRelMapper.updateInuse(b.getId());
						continue;
					} 
				}
				if (b.getBuildType() ==1 && !StringUtil.isEmpty(b.getDesignOrgId())) {
					BackstageUserInfo user = backUserMapper
							.selectByPrimaryKey(b.getDesignOrgId());
					b.setDesignOrgName(user.getName());
					returnBuser.add(b);
				}
				if (b.getBuildType() ==2) {
					if(!StringUtil.isEmpty(b.getDesignOrgId())) {
						BackstageUserInfo user = backUserMapper
							.selectByPrimaryKey(b.getDesignOrgId());
						b.setDesignOrgName(user.getName());
					}
					returnBuser.add(b);
				}
			}
		}
		result.setData(returnBuser);
		return result;
	}
}
