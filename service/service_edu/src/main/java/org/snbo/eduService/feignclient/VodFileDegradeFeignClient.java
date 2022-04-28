package org.snbo.eduService.feignclient;

import org.snbo.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sunbo
 * @create 2022-04-02-15:19
 */

@Component
public class VodFileDegradeFeignClient implements VodClient{
    //出错后会执行
    @Override
    public R deleteVideo(String id) {
        return R.error().message("删除视频出错");
    }

    @Override
    public R deleteVideoByIds(List<String> videoIdList) {
        return R.error().message("删除多个视频出错");
    }
}
