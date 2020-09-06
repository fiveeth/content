package ribbonconfig;

import com.gyh.contentcenter.config.NacosWeightedRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {

    //自定义负载均衡规则：RandomRule 随机请求
    //小技巧：查看接口的实现类 ctrl+alt+b
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
//        return new NacosWeightedRule();
    }
}
