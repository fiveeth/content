package com.gyh.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author cncoder
 */
@SpringBootApplication
@MapperScan(basePackages = "com.gyh.contentcenter.mapper")
//@EnableFeignClients(defaultConfiguration = GlobalFeignConfig.class)//Feign指定配置：java方式(全局)
@EnableFeignClients
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    /**
     * 注入类型为RestTemplate,名为restTemplate的bean
     * LoadBalanced：为RestTemplate整合Ribbon
     * SentinelRestTemplate：为RestTemplate整合Sentinel, 相关源码:SentinelBeanPostProcessor
     *
     * @return
     */
    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}