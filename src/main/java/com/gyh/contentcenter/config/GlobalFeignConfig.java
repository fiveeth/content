package com.gyh.contentcenter.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class GlobalFeignConfig {

    @Bean
    public Logger.Level level() {
        //让feign打印请求的所有细节
        return Logger.Level.FULL;
    }
}
