package org.snbo.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.snbo.commonutils.R;
import org.snbo.eduService.bean.EduVideo;
import org.snbo.eduService.feignclient.VodClient;
import org.snbo.eduService.mapper.EduVideoMapper;
import org.snbo.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {


    @Autowired
    private VodClient client;

    //根据课程id删除该课程所有小节
    @Override
    public void removeByCourseId(String courseId) {
        //根据视频id删除阿里云视频
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.select("video_source_id");
        wrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);
        //
        List<String> videoIds = new ArrayList<>();
        for (EduVideo video : eduVideos) {
            String videoId = video.getVideoSourceId();
            if (!StringUtils.isEmpty(videoId)) {
                videoIds.add(videoId);
            }
        }
        if (!videoIds.isEmpty()) {
            //调用另一个模块删除视频的接口
            R result = client.deleteVideoByIds(videoIds);
            if (result.getCode() == 20001) {
                throw new MoguException(20001, result.getMessage() + ",熔断器...");
            }
        }
        //删除小节
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", courseId);
        baseMapper.delete(wrapper1);

    }
}
