package org.snbo.eduStatistics.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author sunbo
 * @create 2022-04-19-14:37
 */
@Component
@FeignClient(name = "service-uCenter")
public interface CenterMemberClient {
    @GetMapping("/eduCenter/member/countRegister/{day}")
    Integer countRegister(@PathVariable("day") String day);
}
