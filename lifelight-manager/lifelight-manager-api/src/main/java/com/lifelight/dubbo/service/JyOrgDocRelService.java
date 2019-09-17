package com.lifelight.dubbo.service;

import com.lifelight.api.entity.JyOrgDocRel;
import com.lifelight.api.entity.Message;
import com.lifelight.common.result.Result;
/**
 * （家庭医生）医生机构关系服务
 * @author puanl
 *
 */
public interface JyOrgDocRelService {
	
	/**
	 * 邀请医生
	 * @param orgId
	 * @param doctorId
	 * @return
	 */
	Result<Message> inviteDoctor(JyOrgDocRel entity,Integer platformId);
	
	/**
	 * 同意邀请
	 * @param entity
	 * @return
	 */
	Result<Message> agreeInvite(Integer relId,Integer platformId);
	
	/**
	 * 拒绝邀请
	 * @param entity
	 * @return
	 */
	Result<Message> refuseInvite(Integer relId,Integer platformId);
	
}
