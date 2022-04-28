package org.snbo.eduService.feignclient;

import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sunbo
 * @create 2022-04-01-21:48
 */
@Component
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class)
public interface VodClient  {
    //定义要调用的方法的路径
    @DeleteMapping("/eduVod/video/{id}")
    R deleteVideo(@PathVariable("id") String id);

    //根据id删除多个视频
    @DeleteMapping("/eduVod/video")
    R deleteVideoByIds(@RequestParam("videoIdList") List<String> videoIdList);
}
