package com.gyh.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 这个类仅提供一种思路，参考NacosRule类，NacosRule既支持naocs权重也支持同一集群的优先调用
 */
//同一集群优先调用且扩展了ribbon支持nacos的权重
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        try {
            //获取配置文件中的集群名称
            String clusterName = nacosDiscoveryProperties.getClusterName();
            //获取想要请求的微服务的名称
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            String name = loadBalancer.getName();
            //拿到服务发现的相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            //找到指定服务所有想要请求的健康实例
            List<Instance> allInstances = namingService.getAllInstances(name, true);
            //过滤出同一集群下所有想要请求的实例
            List<Instance> sameClusterInstances = allInstances.stream().filter(instance ->
                    Objects.equals(instance.getClusterName(), clusterName)).collect(Collectors.toList());
            //如果B是空的就用A
            List<Instance> instancesToBeChosen = new ArrayList<>();
            if (CollectionUtils.isEmpty(sameClusterInstances)) {
                instancesToBeChosen = allInstances;
                log.warn("发生跨集群的调用,name={},clusterName={},allInstances={}", name, clusterName, allInstances);
            } else {
                instancesToBeChosen = sameClusterInstances;
            }
            //基于权重的负载均衡算法，返回一个实例
            Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToBeChosen);
            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}

//当无法直接调用指定方法的时候，取个巧，通过继承，子类可调用父类的方法（private除外）
class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}