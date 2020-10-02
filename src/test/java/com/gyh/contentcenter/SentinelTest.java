package com.gyh.contentcenter;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

/**
 * @author cncoder
 */
@SpringBootTest
public class SentinelTest {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10000; i++) {
            String forObject = restTemplate.getForObject("http://localhost:9090/actuator/sentinel", String.class);
            Thread.sleep(500);
        }
    }

}
