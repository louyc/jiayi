package com.lifelight.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.web.job.ProcedureMessageJob;
import com.lifelight.web.job.QuartzManager;

@Service
public class InitJobService {

	private static final Logger logger = LoggerFactory.getLogger(InitJobService.class);
	@Autowired
	private ProcedureMessageService procedureMessageService;
	@Autowired
	private QuartzManager quartzManager;

	@PostConstruct
	public void init() {
		logger.info("-----开始初始化消息推送定时任务-----");
		ProcedureMessage procedureMessage = new ProcedureMessage();
		procedureMessage.setType(1);
		List<ProcedureMessage> procedureMessages = new ArrayList<>();
		procedureMessages = procedureMessageService.getUnsentList(procedureMessage);
		logger.info("需要初始化{}个推送任务", procedureMessages.size());
		for (ProcedureMessage message : procedureMessages) {
			quartzManager.removeJob(message.getId(), message.getId(), message.getId(), message.getId());
			quartzManager.addJob(message.getId(), message.getId(), message.getId(), message.getId(),
					ProcedureMessageJob.class, message.getCron());
		}
	}
}
