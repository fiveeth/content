package com.gyh.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//负载均衡规则：优先选择同集群下，符合metadata的实例
//如果没有，就选择所有集群下，符合metadata的实例
@Slf4j
public class NacosFinalRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        try {
            //获取集群名称
            String clusterName = nacosDiscoveryProperties.getClusterName();
            //获取允许请求的目标微服务版本
            String targetVersion = this.nacosDiscoveryProperties.getMetadata().get("target-version");
            //获取目标微服务名称
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            String name = loadBalancer.getName();
            //获取服务发现的相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            //获取所有健康的待请求的实例
            List<Instance> allInstances = namingService.getAllInstances(name, true);
            //筛选元数据匹配的实例
            List<Instance> sameVersionInstances = allInstances;
            if (StringUtils.isNotBlank(targetVersion)) {
                sameVersionInstances = allInstances.stream()
                        .filter(instance -> Objects.equals(instance.getMetadata().get("version"), targetVersion))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(sameVersionInstances)) {
                    log.warn("未找到元数据匹配的目标实例！请检查配置。targetVersion = {}, instance = {}", targetVersion, allInstances);
                    return null;
                }
            }
            //筛选出同cluster下元数据匹配的实例C
            List<Instance> sameClusterAndVersionInstances = sameVersionInstances;
            if (StringUtils.isNotBlank(clusterName)) {
                sameClusterAndVersionInstances = sameVersionInstances.stream()
                        .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(sameClusterAndVersionInstances)) {
                    sameClusterAndVersionInstances = sameVersionInstances;
                    log.warn("发生跨集群调用。clusterName = {}, targetVersion = {}, clusterMetadataMatchInstances = {}", clusterName, targetVersion, sameClusterAndVersionInstances);
                }
            }
            //随机选择实例
            Instance instance = ExtendBalancer.getHostByRandomWeight2(sameClusterAndVersionInstances);
            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
