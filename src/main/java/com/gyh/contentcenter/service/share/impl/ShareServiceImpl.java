package com.gyh.contentcenter.service.share.impl;

import com.gyh.contentcenter.domain.dto.content.ShareDto;
import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.domain.entity.share.Share;
import com.gyh.contentcenter.feignclient.UserCenterFeignClient;
import com.gyh.contentcenter.mapper.share.ShareMapper;
import com.gyh.contentcenter.service.share.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author cncoder
 * @date 2020-07-28 10:32
 */
@Service
@Slf4j
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Override
    public ShareDto findById(Integer id) {
        //根据id获取share
        Share share = shareMapper.selectByPrimaryKey(id);
        //根据用户id获取用户信息
        Integer userId = share.getUserId();
        //演进方式一：
//        //用户中心所有实例信息
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        //所有用户中心的实例的请求地址
//        List<String> targetURLs = instances.stream().map(instance -> instance.getUri() + "/users/{id}"
//        ).collect(Collectors.toList());
//        //获取随机下标
//        int i = ThreadLocalRandom.current().nextInt(targetURLs.size());
//        log.info("请求的目标地址：{}", targetURLs.get(i));
//        UserDto userDto = restTemplate.getForObject(targetURLs.get(i),
//                UserDto.class, userId);

        //演进方式二：
        //使用Ribbon实现负载均衡算法
//        UserDto userDto = restTemplate.getForObject("http://user-center/users/{id}",
//                UserDto.class, userId);

        //演进方式三：
        //Feign对Ribbon做了整合（之所以采用Feign是因为上面的restTemplate方式所写的代码可读性差且对于复杂的URL较难于维护）
        UserDto userDto = userCenterFeignClient.findById(userId);
        //封装返回信息
        ShareDto shareDto = new ShareDto();
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }
}
