package com.gyh.contentcenter.service.share;

import com.gyh.contentcenter.domain.dto.content.ShareDto;

/**
 * @author cncoder
 * @date 2020-07-28 10:31
 */
public interface ShareService {

    /**
     * 根据id获取分享信息
     *
     * @param id
     * @return
     */
    ShareDto findById(Integer id);
}
