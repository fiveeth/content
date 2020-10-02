package com.gyh.contentcenter.controller.share;

import com.gyh.contentcenter.domain.dto.content.ShareDto;
import com.gyh.contentcenter.service.share.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cncoder
 * @date 2020-07-28 10:30
 */
@RestController
@RequestMapping("/shares")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @GetMapping("{id}")
    public ShareDto findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }
}
