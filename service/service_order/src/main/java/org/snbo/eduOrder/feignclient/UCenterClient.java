package org.snbo.eduOrder.feignclient;

import org.snbo.commonutils.vo.UCenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author sunbo
 * @create 2022-04-17-21:32
 */
@Component
@FeignClient(name = "service-uCenter")
public interface UCenterClient {
    @GetMapping("/eduCenter/member/{id}")
    UCenterMember getMemberInfoById(@PathVariable("id") String id);
}
