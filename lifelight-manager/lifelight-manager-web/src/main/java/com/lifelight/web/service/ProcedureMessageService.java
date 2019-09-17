package com.lifelight.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.web.dao.ProcedureMessageMapper;

@Service("pMessageService")
public class ProcedureMessageService {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureMessageService.class);

	@Autowired
	private ProcedureMessageMapper procedureMessageMapper;

	public ProcedureMessage getProcedureMessageById(String id) {
		ProcedureMessage procedureMessage = null;
		procedureMessage = procedureMessageMapper.selectByPrimaryKey(id);
		return procedureMessage;
	}

	public Boolean update(ProcedureMessage procedureMessage) {
		procedureMessage.setUpdateTime(new Date());
		int num = procedureMessageMapper.updateByPrimaryKeySelective(procedureMessage);
		if (num == 0) {
			return false;
		}
		return true;
	}

	public List<ProcedureMessage> getUnsentList(ProcedureMessage procedureMessage) {
		List<ProcedureMessage> procedureMessages = new ArrayList<ProcedureMessage>();

		try {
			procedureMessages = procedureMessageMapper.getUnsentList(procedureMessage);
		} catch (Exception e) {
			logger.error("查询失败:{}", e);
		}
		return procedureMessages;
	}
	
}
