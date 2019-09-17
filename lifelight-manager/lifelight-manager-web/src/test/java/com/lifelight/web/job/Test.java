package com.lifelight.web.job;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) throws BeansException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
		QuartzManager quartzManager = (QuartzManager) ctx.getBean("quartzManager");
		try {
			System.out.println("【系统启动】开始(每1秒输出一次 job2)...");

			Thread.sleep(5000);
			System.out.println("【增加job1启动】开始(每1秒输出一次)...");
			quartzManager.addJob("abc", "2", "3", "4", ProcedureMessageJob.class, "0/1 * * * * ?");
			quartzManager.addJob("bcd", "4", "6", "8", ProcedureMessageJob.class, "0/1 * * * * ?");

			Thread.sleep(5000);
			System.out.println("【修改job1时间】开始(每2秒输出一次)...");
			quartzManager.modifyJobTime("abc", "2", "3", "4", "0/4 * * * * ?");

			Thread.sleep(10000);
			System.out.println("【移除job1定时】开始...");
			quartzManager.removeJob("abc", "2", "3", "4");

			// 关掉任务调度容器
			// quartzManager.shutdownJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
