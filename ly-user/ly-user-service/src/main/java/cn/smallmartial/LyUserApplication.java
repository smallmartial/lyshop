package cn.smallmartial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author smallmartial
 * @Date 2019/4/22
 * @Email smallmarital@qq.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.smallmartial.user.mapper")
public class LyUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyUserApplication.class,args);
    }
}