package org.snbo.eduService.controller;


import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.eduService.bean.EduVideo;
import org.snbo.eduService.feignclient.VodClient;
import org.snbo.eduService.service.EduVideoService;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
@RestController
@RequestMapping("/eduService/video")
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient client;

    @PostMapping
    @ApiOperation(value = "(增)添加小节信息")
    public R saveVideo(@RequestBody EduVideo video) {
        videoService.save(video);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "(删)删除小节信息")
    public R deleteVideo(@PathVariable String id) {
        //先删视频再删小节
        //根据小节id得到视频id
        String videoSourceId = videoService.getById(id).getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            R result = client.deleteVideo(videoSourceId);
            if (result.getCode() == 20001){
                throw new MoguException(20001,result.getMessage()+" 熔断器...");
            }
        }
        videoService.removeById(id);
        return R.ok();
    }

    @PutMapping
    @ApiOperation(value = "(改)更改小节信息")
    public R updateVideo(@RequestBody EduVideo video) {
        videoService.updateById(video);
        return R.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "(查)根据id查询小节")
    public R getById(@PathVariable String id) {
        EduVideo video = videoService.getById(id);
        return R.ok().data("video", video);
    }
}

