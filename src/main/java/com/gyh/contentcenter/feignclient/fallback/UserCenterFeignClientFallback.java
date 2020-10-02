package com.gyh.contentcenter.feignclient.fallback;

import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author cncoder
 * @date 2020/9/23 15:24
 */
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {

    @Override
    public UserDto findById(Integer id) {
        UserDto userDto = new UserDto();
        userDto.setWxNickname("默认用户");
        return userDto;
    }
}
