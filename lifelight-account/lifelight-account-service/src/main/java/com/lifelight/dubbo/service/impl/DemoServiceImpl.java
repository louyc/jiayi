package com.lifelight.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lifelight.dubbo.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
 
    public String sayHello(String name) {
        return "Hello " + name;
    }
 
}
