package com.gyh.contentcenter.controller;

import com.google.common.collect.Maps;
import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.feignclient.TestBaiduFeignClient;
import com.gyh.contentcenter.feignclient.TestUserCenterFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author guoyihua
 * @date 2020-07-28 8:57
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestUserCenterFeignClient testUserCenterFeignClient;

    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient;

    @GetMapping("/test")
    public List<ServiceInstance> getInstance() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }

    @GetMapping("/testGet")
    public UserDto query(UserDto userDto) {
        return testUserCenterFeignClient.query(userDto);//方式一
//        return testUserCenterFeignClient.query(userDto.getId(),userDto.getWxId());//方式二
//        return testUserCenterFeignClient.query(userDto.getId(),userDto.getWxId());//方式三
//        HashMap<String, Object> map = Maps.newHashMap();//方式四
//        map.put("id", userDto.getId());
//        map.put("wxId", userDto.getWxId());
//        return testUserCenterFeignClient.query(map);
    }

    @PostMapping("/testPost")
    public UserDto queryPost(@RequestBody UserDto userDto) {
        return testUserCenterFeignClient.queryPost(userDto);
//        return testUserCenterFeignClient.query(userDto.getId(),userDto.getWxId());
//        HashMap<String, Object> map = Maps.newHashMap();
//        map.put("id", userDto.getId());
//        map.put("wxId", userDto.getWxId());
//        return testUserCenterFeignClient.query(map);
    }

    @GetMapping("/baidu")
    public String baidu() {
        return testBaiduFeignClient.index();
    }
}