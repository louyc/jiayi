package com.lifelight.lyc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifelight.dubbo.service.BackstageMenuInfoService;

public class TestController {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-context.xml", "spring-mvc.xml", "spring-redis.xml" });

		BackstageMenuInfoService bmi = (BackstageMenuInfoService) context
				.getBean("BackstageMenuInfoService");
		bmi.getBackstageMenuInfoByRoleId("1",1);
	}
}
