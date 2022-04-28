package org.snbo.eduStatistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author sunbo
 * @create 2022-04-19-14:00
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.snbo"})
@MapperScan("org.snbo.eduStatistics.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling//定时任务
public class StatisticApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticApplication.class,args);
    }
}
