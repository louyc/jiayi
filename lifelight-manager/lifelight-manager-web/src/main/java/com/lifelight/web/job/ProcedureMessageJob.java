package com.lifelight.web.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifelight.api.entity.ProcedureMessage;
import com.lifelight.web.service.ProcedureMessageService;
import com.lifelight.web.service.WeixinService;

public class ProcedureMessageJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(ProcedureMessageJob.class);

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		String messageId = jobExecutionContext.getJobDetail().getKey().getName();
		logger.info("开始推送 id = {} 的消息", messageId);
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
		ProcedureMessageService procedureMessageService = (ProcedureMessageService) ctx.getBean("pMessageService");
		ProcedureMessage procedureMessage = procedureMessageService.getProcedureMessageById(messageId);
		logger.info("推送内容：{}", procedureMessage.getContent());

		WeixinService weixinService = (WeixinService) ctx.getBean("weixinService");
		try {
			if (procedureMessage.getSendCount() > 0) {
				weixinService.sendTuisong(procedureMessage.getMessageTo(), procedureMessage.getMessageFrom(), procedureMessage.getContent(),
						procedureMessage.getUrl(), "manager.lifelight365.com");
				// 推送成功，剩余次数减一
				int remainSendCout = procedureMessage.getSendCount() - 1;
				procedureMessage.setSendCount(remainSendCout);
				procedureMessageService.update(procedureMessage);
			} else // 剩余次数为0，推送任务结束
			{
				QuartzManager quartzManager = (QuartzManager) ctx.getBean("quartzManager");
				quartzManager.removeJob(messageId, messageId, messageId, messageId);
			}
			
			ctx.close();
		} catch (Exception e) {
			logger.error("消息推送失败：{}", e);
		}

	}
}