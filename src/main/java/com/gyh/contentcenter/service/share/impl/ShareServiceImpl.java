package com.gyh.contentcenter.service.share.impl;

import com.gyh.contentcenter.domain.dto.content.ShareAuditDTO;
import com.gyh.contentcenter.domain.dto.content.ShareDto;
import com.gyh.contentcenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.gyh.contentcenter.domain.dto.user.UserDto;
import com.gyh.contentcenter.domain.entity.rocketmqTransactionLog.RocketmqTransactionLog;
import com.gyh.contentcenter.domain.entity.share.Share;
import com.gyh.contentcenter.domain.enums.AuditStatusEnum;
import com.gyh.contentcenter.feignclient.UserCenterFeignClient;
import com.gyh.contentcenter.mapper.rocketmqTransactionLog.RocketmqTransactionLogMapper;
import com.gyh.contentcenter.mapper.share.ShareMapper;
import com.gyh.contentcenter.service.share.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

/**
 * @author cncoder
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

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

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

    @Override
    public Share auditById(Integer id, ShareAuditDTO auditDTO) {
        //查询share是否存在，不存在或者审核状态不为NOT_YET则抛出异常
        Share share = shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法！改分享不存在！");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法！改分享已审核！");
        }
        //如果是PASS，那么发送消息给rocketmq，让用户中心消费，为发布人添加积分(异步的，异步的原因是这个方法主要是审核，附属操作是加积分)
        UserAddBonusMsgDTO userAddBonusMsgDTO = new UserAddBonusMsgDTO();
        userAddBonusMsgDTO.setUserId(share.getUserId());
        userAddBonusMsgDTO.setBonus(50);
        String transactionId = UUID.randomUUID().toString();
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())) {
            rocketMQTemplate.sendMessageInTransaction(
                    "tx-add-bonus-group",
                    "add-bonus",
                    MessageBuilder.withPayload(userAddBonusMsgDTO)
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .setHeader("share_id", id)
                            .build(),
                    auditDTO);
        } else {
            //如果是REJECT则单纯的审核资源
            auditByIdInDB(id, auditDTO);
        }
        return share;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(Integer id, ShareAuditDTO auditDTO) {
        Share share = new Share();
        share.setId(id);
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        share.setReason(auditDTO.getReason());
        shareMapper.updateByPrimaryKeySelective(share);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(Integer id, String transactionId, ShareAuditDTO auditDTO) {
        auditByIdInDB(id, auditDTO);

        RocketmqTransactionLog rocketmqTransactionLog = new RocketmqTransactionLog();
        rocketmqTransactionLog.setLog("审核分享");
        rocketmqTransactionLog.setTransactionId(transactionId);
        rocketmqTransactionLogMapper.insertSelective(rocketmqTransactionLog);
    }
}
