package com.gyh.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 脱离ribbon使用：也就是使用feign的方式调用没有注册到ribbon中的服务链接
 * name：可以随意取名，但不能省略
 *
 * @author cncoder
 */
@FeignClient(name = "baidu", url = "https://www.baidu.com")
public interface TestBaiduFeignClient {

    /**
     * 测试
     *
     * @return
     */
    @GetMapping("/")
    String index();
}
