package com.gyh.contentcenter.controller.share;

import com.gyh.contentcenter.domain.dto.content.ShareAuditDTO;
import com.gyh.contentcenter.domain.entity.share.Share;
import com.gyh.contentcenter.service.share.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cncoder
 */
@RestController
@RequestMapping("/admin/shares")
public class ShareAdminController {

    @Autowired
    private ShareService shareService;

    /**
     * 审核指定内容
     *
     * @param id
     * @param auditDTO
     * @return
     */
    @PutMapping("/audit/{id}")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO) {
        return this.shareService.auditById(id, auditDTO);
    }
}
