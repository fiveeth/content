package com.gyh.contentcenter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cncoder
 */
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    /**
     * 待审核
     */
    NOT_YET,
    /**
     * 审核通过
     */
    PASS,
    /**
     * 审核不通过
     */
    REJECT
}
