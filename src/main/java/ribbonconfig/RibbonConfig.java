package ribbonconfig;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cncoder
 */
@Configuration
public class RibbonConfig {

    /**
     * 自定义负载均衡规则：RandomRule 随机请求
     *
     * @return
     */
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
//        return new NacosWeightedRule();
//        return new NacosSameClusterWeightedRule();
    }
}
