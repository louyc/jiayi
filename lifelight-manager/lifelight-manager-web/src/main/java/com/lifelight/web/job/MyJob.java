package com.lifelight.web.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJob {

	private static final Logger logger = LoggerFactory.getLogger(MyJob.class);

	public void startQuartz() {
		logger.info("-----定时任务运行中-----");
	}
}
