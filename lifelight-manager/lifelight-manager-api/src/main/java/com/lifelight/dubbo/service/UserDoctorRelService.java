package com.lifelight.dubbo.service;

import java.util.List;

import com.lifelight.api.entity.UserDoctorRel;
import com.lifelight.common.result.Result;

/**
 * Created by kwinxu on 2017/12/18.
 */
public interface UserDoctorRelService {


    /**
     * 添加孕产妇签约信息
     * @param userDoctorRel
     * @return
     */
    int add(UserDoctorRel userDoctorRel) throws Exception;
    
    Result queryRel(UserDoctorRel udRel);
    
    Result queryAllDoctors(String managerId,Integer platformId);
    
    Result queryDoctors(String managerId,Integer platformId) ;

	List<UserDoctorRel> queryDocRel(String managerId, String doctorId);
}
