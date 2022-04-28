package org.snbo.eduStatistics.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author sunbo
 * @create 2022-04-19-20:31
 */
@Component
@FeignClient(name = "service-edu")
public interface CourseClient {
    @GetMapping("/eduService/course/courseCount/{day}")
    Integer getCourseCount(@PathVariable("day") String day);
}
