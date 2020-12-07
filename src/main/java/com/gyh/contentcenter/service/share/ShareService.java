package com.gyh.contentcenter.service.share;

import com.gyh.contentcenter.domain.dto.content.ShareAuditDTO;
import com.gyh.contentcenter.domain.dto.content.ShareDto;
import com.gyh.contentcenter.domain.entity.share.Share;

/**
 * @author cncoder
 */
public interface ShareService {

    /**
     * 根据id获取分享信息
     *
     * @param id
     * @return
     */
    ShareDto findById(Integer id);

    /**
     * 审核指定内容
     *
     * @param id
     * @param auditDTO
     * @return
     */
    Share auditById(Integer id, ShareAuditDTO auditDTO);

    /**
     * 审核内容
     *
     * @param id
     * @param auditDTO
     */
    void auditByIdInDB(Integer id, ShareAuditDTO auditDTO);

    /**
     * 记录日志
     *
     * @param id
     * @param transactionId
     * @param auditDTO
     */
    void auditByIdWithRocketMqLog(Integer id, String transactionId, ShareAuditDTO auditDTO);
}
