package com.gyh.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常提示优化
 *
 * @author cncoder
 * @date 2020/9/30 21:31
 */
@Component
public class MyBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        ErrorMsg msg = null;
        if (e instanceof FlowException) {
            //限流
            msg = ErrorMsg.builder()
                    .status(100)
                    .msg("限流了")
                    .build();
        } else if (e instanceof DegradeException) {
            //降级
            msg = ErrorMsg.builder()
                    .status(101)
                    .msg("降级了")
                    .build();
        } else if (e instanceof ParamFlowException) {
            //热点参数
            msg = ErrorMsg.builder()
                    .status(102)
                    .msg("热点参数限流")
                    .build();
        } else if (e instanceof AuthorityException) {
            //授权
            msg = ErrorMsg.builder()
                    .status(103)
                    .msg("授权过则不满足")
                    .build();
        } else if (e instanceof SystemBlockException) {
            //系统
            msg = ErrorMsg.builder()
                    .status(104)
                    .msg("系通过则不满足")
                    .build();
        }
        //http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), msg);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorMsg {
    private Integer status;
    private String msg;
}
