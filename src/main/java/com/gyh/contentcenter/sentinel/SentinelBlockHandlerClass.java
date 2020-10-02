package com.gyh.contentcenter.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author cncoder
 */
public class SentinelBlockHandlerClass {

    public static String handleException(String a, BlockException blockException) {
        return "限流了,block";
    }
}
