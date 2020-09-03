package com.gyh.contentcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author guoyihua
 * @date 2020-07-28 8:57
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test")
    public List<ServiceInstance> getInstance(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }
}