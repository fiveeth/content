package com.gyh.contentcenter.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * 不加@Configuration这个注解，否则需要挪到@ComponentScan这个注解扫描的包以外（默认情况下启动类所在的包），
 * 不这样做的话会出现父子上下文重复扫描的问题
 */
public class UserCenterFeignConfig {

    @Bean
    public Logger.Level level() {
        //让feign打印请求的所有细节
        return Logger.Level.FULL;
    }
}
