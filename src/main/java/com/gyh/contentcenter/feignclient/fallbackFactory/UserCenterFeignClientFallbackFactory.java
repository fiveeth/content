package com.gyh.contentcenter.feignclient.fallbackFactory;

import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cncoder
 * @date 2020/9/23 15:33
 */
@Slf4j
@Component
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        UserCenterFeignClient userCenterFeignClient = id -> {
            log.warn("请求被限流或者降级了:{}", throwable.getMessage());
            UserDto userDto = new UserDto();
            userDto.setWxNickname("默认用户");
            return userDto;
        };
        return userCenterFeignClient;
    }
}
