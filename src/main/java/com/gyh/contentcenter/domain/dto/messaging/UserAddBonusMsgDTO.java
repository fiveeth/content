package com.gyh.contentcenter.domain.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cncoder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO {
    /**
     * 需要添加积分的用户
     */
    private Integer userId;
    /**
     * 加多少积分
     */
    private Integer bonus;
}
