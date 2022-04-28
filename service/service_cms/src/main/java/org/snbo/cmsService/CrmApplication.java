package org.snbo.cmsService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author sunbo
 * @create 2022-04-03-14:49
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.snbo"})
@MapperScan("org.snbo.cmsService.mapper")
@EnableDiscoveryClient//nacos
public class CrmApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class);
    }
}
