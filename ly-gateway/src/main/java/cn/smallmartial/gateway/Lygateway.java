package cn.smallmartial.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author smallmartial
 * @Date 2019/4/11
 * @Email smallmarital@qq.com
 */
@EnableZuulProxy
@SpringCloudApplication
public class Lygateway {
    public static void main(String[] args) {
        SpringApplication.run(Lygateway.class,args);
    }
}
