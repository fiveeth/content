package com.gyh.contentcenter.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author cncoder
 */
@Service
public class TestService {

    @SentinelResource
    public void common() {
        System.out.println("common...");
    }
}
