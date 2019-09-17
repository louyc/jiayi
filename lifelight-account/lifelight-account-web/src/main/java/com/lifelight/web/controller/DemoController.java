package com.lifelight.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.DemoService;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Reference
	private DemoService demoService;
	@Autowired
    private RedisTool redisTool;

	@RequestMapping("/hello")
	@ResponseBody
	public String getManagerInfoById(@RequestParam("name") String name) {
		String result = demoService.sayHello(name);
		redisTool.put("test",name);
		return result;
	}
}