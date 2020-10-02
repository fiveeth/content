package com.gyh.contentcenter.config;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * 当无法直接调用指定方法的时候，取个巧，通过继承，子类可调用父类的方法（private除外）
 *
 * @author cncoder
 */
public class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
