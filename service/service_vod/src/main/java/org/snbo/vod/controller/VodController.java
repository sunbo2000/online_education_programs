package org.snbo.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.snbo.vod.service.VodService;
import org.snbo.vod.utils.ConstantVodUtils;
import org.snbo.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author sunbo
 * @create 2022-03-31-19:30
 */
@RestController
@RequestMapping("/eduVod/video")
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping
    @ApiOperation(value = "(增)上传课时视频")
    public R uploadVideo(@RequestBody MultipartFile file) {
        //返回视频id,根据id可以获得url和播放证书
        String videoId = vodService.saveVideo(file);
        return R.ok().data("videoId", videoId);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据视频id从阿里云删除视频")
    public R deleteVideo(@PathVariable String id) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request里面设置视频id
            request.setVideoIds(id);
            //调用初始化对象方法实现删除
            client.getAcsResponse(request);
            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MoguException(20001, "删除视频失败");
        }
    }

    //@RequestParam 也是post或put请求,但类型不是json
    //@RequestBody 是json数据通过post或put请求提交时用来接收的
    @DeleteMapping
    @ApiOperation(value = "批量删除阿里云视频")
    public R deleteVideoByIds(@RequestParam("videoIdList") List<String> videoIdList) {
        vodService.removeVideos(videoIdList);
        return R.ok();
    }

    @GetMapping("/{videoSourceId}")
    public R getAuth(@PathVariable String videoSourceId) {
        String playAuth = vodService.getPlayAuth(videoSourceId);
        return R.ok().data("playAuth", playAuth);
    }
}
