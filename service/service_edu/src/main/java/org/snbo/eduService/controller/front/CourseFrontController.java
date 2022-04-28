package org.snbo.eduService.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.snbo.commonutils.R;
import org.snbo.eduService.bean.EduCourse;
import org.snbo.eduService.bean.chapter.ChapterVo;
import org.snbo.eduService.bean.frontVo.CourseFrontInfoVo;
import org.snbo.eduService.bean.frontVo.CourseQueryVo;
import org.snbo.eduService.bean.frontVo.PlayerCourseVo;
import org.snbo.eduService.service.EduChapterService;
import org.snbo.eduService.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

/**
 * @author sunbo
 * @create 2022-04-10-20:08
 */
@RestController
@RequestMapping("/eduService/frontCourse")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;


    @PostMapping("/{current}/{size}")
    public R getCourseInfo(@PathVariable Long current, @PathVariable Long size,
                           @RequestBody(required = false) CourseQueryVo courseQueryVo) {

        Page<EduCourse> coursePage = new Page<>(current, size);
        Map<String, Object> map = courseService.getFrontCourseInfoPage(coursePage, courseQueryVo);
        return R.ok().data(map);
    }

    @GetMapping("/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {
        //课程详情信息
        CourseFrontInfoVo courseInfo = courseService.getBaseCourseInfo(courseId);
        //课程章节信息
        List<ChapterVo> chapterAndVideo = chapterService.getChapterAndVideo(courseId);
        return R.ok().data("courseInfo", courseInfo).data("chapterAndVideoInfo", chapterAndVideo);
    }

    @GetMapping("/getChapterAndVideo/{id}")
    public R getChapterAndVideo(@PathVariable String id) {
        List<ChapterVo> chapterAndVideo = chapterService.getChapterAndVideo(id);
        return R.ok().data("chapterAndVideoInfo", chapterAndVideo);
    }

    @GetMapping("/getPlayerInfo/{id}")
    public R getPlayerInfo(@PathVariable String id) {
        EduCourse course = courseService.getById(id);
        PlayerCourseVo playerCourseVo = new PlayerCourseVo();
        BeanUtils.copyProperties(course, playerCourseVo);
        return R.ok().data("courseInfo", playerCourseVo);
    }
}
