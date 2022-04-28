package org.snbo.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author sunbo
 * @create 2022-03-31-19:26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//默认不加载数据库配置
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"org.snbo"})
public class VodApplication {
    public static void main(String[] args) {
        SpringApplication.run(VodApplication.class);
    }
}
