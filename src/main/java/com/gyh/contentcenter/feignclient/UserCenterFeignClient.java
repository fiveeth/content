package com.gyh.contentcenter.feignclient;

import com.gyh.contentcenter.config.UserCenterFeignConfig;
import com.gyh.contentcenter.domain.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "user-center",configuration = UserCenterFeignConfig.class) //Feign指定配置：java方式(细粒度)
@FeignClient(name = "user-center")
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
