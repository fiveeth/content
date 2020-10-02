package com.gyh.contentcenter.feignclient;

import com.gyh.contentcenter.domain.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 使用Feign构造多参数的请求
 *
 * @author cncoder
 */
@FeignClient(name = "user-center")
public interface TestUserCenterFeignClient {
    /**
     * Get请求:方式一(推荐)
     *
     * @param userDto
     * @return
     */
    @GetMapping("/testGet")
    UserDto query(@SpringQueryMap UserDto userDto);

//    /**
//     * Get请求:方式二(推荐)
//     *
//     * @param id
//     * @param wxId
//     * @return
//     */
//    @GetMapping("/testGet")
//    UserDto query(@RequestParam("id") Integer id, @RequestParam("wxId") String wxId);

//    /**
//     * Get请求:方式三(不推荐，复杂的URL难维护)
//     *
//     * @param id
//     * @param wxId
//     * @return
//     */
//    @GetMapping("/testGet?id={id}&wxId={wxId}")
//    UserDto query(@PathVariable("id") Integer id, @PathVariable("wxId") String wxId);

//    /**
//     * Get请求:方式四(不推荐，可读性不好)
//     *
//     * @param map
//     * @return
//     */
//    @GetMapping("/testGet")
//    UserDto query(@RequestParam Map<String, Object> map);

    /**
     * post请求
     *
     * @param userDto
     * @return
     */
    @PostMapping("/testPost")
    UserDto queryPost(@RequestBody UserDto userDto);
}
