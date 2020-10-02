package com.gyh.contentcenter.feignclient;

import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.feignclient.fallbackFactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @FeignClient(name = "user-center",configuration = UserCenterFeignConfig.class) //Feign指定配置：java方式(细粒度)
 *
 * fallback、fallbackFactory不同时使用；fallbackFactory功能更强大点，可以拿到异常信息
 *
 * @author cncoder
 */
@FeignClient(name = "user-center", fallbackFactory = UserCenterFeignClientFallbackFactory.class)
public interface UserCenterFeignClient {

    /**
     * 相当于构造出http://user-center/users/{id}
     *
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDto findById(@PathVariable Integer id);
}
