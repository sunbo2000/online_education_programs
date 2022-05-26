package org.snbo.aclService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author sunbo.2000
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"org.snbo"})
@MapperScan("org.snbo.aclService.mapper")
public class ServiceAclApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplication.class, args);
    }

}
