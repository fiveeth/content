package com.gyh.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 扩展ribbon支持nacos的权重
 * <p>
 * 这个类仅提供一种思路，早期的Spring Cloud Alibaba版本中Nacos权重配置对Spring Cloud Alibaba无效，
 * 但是这个问题已经得到了修复，参考NacosRule类
 * <p>
 * Nacos支持权重配置，这是个比较实用的功能，例如：
 * 把性能差的机器权重设低，性能好的机器权重设高，让请求优先打到性能高的机器上去；某个实例出现异常时，把权重设低，排查问题，问题排查完再把权重恢复；
 * 想要下线某个实例时，可先将该实例的权重设为0，这样流量就不会打到该实例上了——此时再去关停该实例，这样就能实现优雅下线啦。当然这是为Nacos量身定
 * 制的优雅下线方案——Spring Cloud中，要想实现优雅下线还有很多姿势，详见：《实用技巧：Spring Cloud中，如何优雅下线微服务？》
 * 然而测试发现，Nacos权重配置对Spring Cloud Alibaba无效。也就是说，不管在Nacos控制台上如何配置，调用时都不管权重设置的。
 * 所以通过整合Ribbon的方式，实现自定义的权重负载均衡规则
 * <p>
 * Ribbon本身也有一个权重负载均衡的规则WeightedResponseTimeRule，但是这个是根据每个服务器的响应时间计算权重，根据这里得出的权重来选择服务器，
 * 响应时间越短的服务器被选择的概率越大
 * <p>
 *
 * @author cncoder
 */
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件，并且初始化NacosWeightedRule
    }

    @Override
    public Server choose(Object o) {
        try {
            //获取ribbon的入口,但是因为ILoadBalancer没有getName这个API,所以将ILoadBalancer强转为BaseLoadBalancer
            //ILoadBalancer loadBalancer = this.getLoadBalancer();
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //log.info("lb={}", loadBalancer);
            //获取想要请求的微服务的名称
            String name = loadBalancer.getName();
            //实现负载均衡算法，可以自己实现，也可以采用nacos client已经实现的负载均衡算法
            //拿到服务发现的相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            //nacos client自动根据基于权重的负载均衡算法，给我们选择一个实例
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("选择的实例是：port={}，instance={}", instance.getPort(), instance);
            //instance转换为server
            return new NacosServer(instance);
        } catch (NacosException e) {
            return null;
        }
    }
}
