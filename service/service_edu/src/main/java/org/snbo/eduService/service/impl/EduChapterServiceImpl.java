package org.snbo.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.snbo.eduService.bean.EduChapter;
import org.snbo.eduService.bean.EduVideo;
import org.snbo.eduService.bean.chapter.ChapterVo;
import org.snbo.eduService.bean.chapter.VideoVo;
import org.snbo.eduService.mapper.EduChapterMapper;
import org.snbo.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.snbo.eduService.service.EduVideoService;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    @Override
    public List<ChapterVo> getChapterAndVideo(String courseId) {
        //创建章节vo集合
        List<ChapterVo> list = new ArrayList<>();
        //根据课程id查询所有章节
        QueryWrapper<EduChapter> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", courseId);
        wrapper1.orderByAsc("sort");
//        List<EduChapter> list1 = baseMapper.selectList(wrapper);
        List<EduChapter> list1 = this.list(wrapper1);
        QueryWrapper<EduVideo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        wrapper2.orderByAsc("sort");
        List<EduVideo> list2 = videoService.list(wrapper2);

        for (EduChapter chp : list1) {
            //创建一个章节vo实例
            ChapterVo chapterVo = new ChapterVo();
            //把chapter实体类的id和title赋给vo
            BeanUtils.copyProperties(chp, chapterVo);
            //小节集合
            List<VideoVo> children = new ArrayList<>();
            chapterVo.setChildren(children);
            //当前章节加入章节vo里
            list.add(chapterVo);
            String chapterId = chp.getId();
            for (EduVideo vid : list2) {
                //当前小节是属于该章节
                if (chapterId.equals(vid.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(vid, videoVo);
                    children.add(videoVo);
                }

            }
        }
        return list;
    }

    @Override
    public boolean deleteChapter(String id) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", id);
        int count = videoService.count(wrapper);
        if (count > 0) {
            throw new MoguException(20001, "不能删除");
        } else {
            int result = baseMapper.deleteById(id);
            return result > 0;
        }
    }

    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        baseMapper.delete(wrapper2);
    }
}
