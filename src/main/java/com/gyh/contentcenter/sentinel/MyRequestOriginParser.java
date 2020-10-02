package com.gyh.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 区分来源
 *
 * @author cncoder
 * @date 2020/10/2 9:46
 */
//@Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        //从请求参数中获取名为origin的参数并返回（但是，在生产环境中还是建议放在请求头里面）
        //如果获取不到origin参数，就抛出异常
        //String origin = httpServletRequest.getHeader("origin");
        String origin = httpServletRequest.getParameter("origin");
        if (StringUtils.isBlank(origin)) {
            throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
