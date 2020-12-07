package com.gyh.contentcenter.rocketmq;

import com.gyh.contentcenter.domain.dto.content.ShareAuditDTO;
import com.gyh.contentcenter.domain.entity.rocketmqTransactionLog.RocketmqTransactionLog;
import com.gyh.contentcenter.mapper.rocketmqTransactionLog.RocketmqTransactionLogMapper;
import com.gyh.contentcenter.service.share.ShareService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author cncoder
 */
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private ShareService shareService;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 执行本地事务
     *
     * @param message
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf((String) headers.get("share_id"));
        try {
            shareService.auditByIdWithRocketMqLog(shareId, transactionId, (ShareAuditDTO) arg );
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 本地事务的检查接口
     *
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        RocketmqTransactionLog rocketmqTransactionLog = new RocketmqTransactionLog();
        rocketmqTransactionLog.setTransactionId(transactionId);
        RocketmqTransactionLog result = rocketmqTransactionLogMapper.selectOne(rocketmqTransactionLog);
        if (result != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
