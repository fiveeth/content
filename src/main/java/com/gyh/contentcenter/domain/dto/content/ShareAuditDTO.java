package com.gyh.contentcenter.domain.dto.content;

import com.gyh.contentcenter.domain.enums.AuditStatusEnum;
import lombok.Data;

/**
 * @author cncoder
 */
@Data
public class ShareAuditDTO {

    /**
     * 审核状态
     */
    private AuditStatusEnum auditStatusEnum;

    /**
     * 原因（审核通过或不通过）
     */
    private String reason;
}
