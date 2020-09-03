package com.gyh.contentcenter.service.share.impl;

import com.gyh.contentcenter.domain.dto.content.ShareDto;
import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.domain.entity.share.Share;
import com.gyh.contentcenter.mapper.share.ShareMapper;
import com.gyh.contentcenter.service.share.ShareService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author guoyihua
 * @date 2020-07-28 10:32
 */
@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ShareDto findById(Integer id) {
        //根据id获取share
        Share share = shareMapper.selectByPrimaryKey(id);
        //根据用户id获取用户信息
        Integer userId = share.getUserId();
        UserDto userDto = restTemplate.getForObject("http://localhost:9080/users/{id}",
                UserDto.class, userId);
        //封装返回信息
        ShareDto shareDto = new ShareDto();
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }
}
