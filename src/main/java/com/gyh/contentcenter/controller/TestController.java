package com.gyh.contentcenter.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.feignclient.TestBaiduFeignClient;
import com.gyh.contentcenter.feignclient.TestUserCenterFeignClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author cncoder
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestUserCenterFeignClient testUserCenterFeignClient;

    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient;

    @Autowired
    private TestService testService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Source source;

    @GetMapping("/test")
    public List<ServiceInstance> getInstance() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }

    @GetMapping("/testGet")
    public UserDto query(UserDto userDto) {
        //方式一
        return testUserCenterFeignClient.query(userDto);
        //方式二
//        return testUserCenterFeignClient.query(userDto.getId(),userDto.getWxId());
        //方式三
//        return testUserCenterFeignClient.query(userDto.getId(),userDto.getWxId());
        //方式四
//        HashMap<String, Object> map = Maps.newHashMap();
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

    @GetMapping("/test-a")
    public String testA() {
        this.testService.common();
        return "test-a";
    }

    @GetMapping("/test-b")
    public String testB() {
        this.testService.common();
        return "test-b";
    }

    @GetMapping("/test-hot")
    @SentinelResource("hot")
    public String testHot(@RequestParam(required = false) String a, @RequestParam(required = false) String b) {
        return a + "," + b;
    }

    /**
     * api模式
     *
     * @param a
     * @return
     */
    @GetMapping("/test-sentinel-api")
    public String testApi(@RequestParam(required = false) String a) {
        String resourceName = "test-sentinel-api";
        //针对来源
        ContextUtil.enter(resourceName, "test-wfw");
        Entry entry = null;
        try {
            //定义一个sentinel保护的资源，资源的名称是：test-sentinel-api
            entry = SphU.entry(resourceName);
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("参数不能为空");
            }
            return a;
        }
        //如果被保护的资源被限流或者降级了，就会抛出BlockException
        catch (BlockException e) {
            return "限流或者降级";
        }
        //IllegalArgumentException不属于BlockException子类，故需要单独拎出来
        catch (IllegalArgumentException e2) {
            //统计IllegalArgumentException【发生的次数、发生占比....】
            Tracer.trace(e2);
            return "非法参数";
        } finally {
            if (entry != null) {
                entry.close();
            }
            ContextUtil.exit();
        }
    }

    //注解模式

    /**
     * 参考：https://github.com/alibaba/Sentinel/wiki/注解支持
     * 特别地，若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑
     * <p>
     * 针对来源(SentinelResource注解暂不支持针对来源)
     *
     * @param a
     * @return
     */
    @GetMapping("/test-sentinel-resource")
//    blockHandler = "handleException",
//    blockHandlerClass = SentinelBlockHandlerClass.class
    @SentinelResource(value = "test-sentinel-resource", fallback = "fallback")
    public String testResource(@RequestParam(required = false) String a) {
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a cannot be null");
        }
        return a;
    }

    public static String handleException(String a, BlockException blockException) {
        return "限流或者降级,block";
    }

    public String fallback(String a, Throwable throwable) {
        return "限流或者降级，fallback";
    }

    /**
     * 测试@SentinelRestTemplate注解是否生效
     *
     * @param userId
     * @return
     */
    @GetMapping("/test-sentinel-restTemplate/{userId}")
    public UserDto testSentinelRestTemplate(@PathVariable Integer userId) {
        UserDto userDto = restTemplate.getForObject("http://user-center/users/{id}", UserDto.class, userId);
        return userDto;
    }

//    @GetMapping("test-stream")
//    public String testStream() {
//        source.output()
//                .send(MessageBuilder.withPayload("测试stream").build());
//        return "success";
//    }
}