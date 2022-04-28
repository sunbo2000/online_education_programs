package org.snbo.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.commonutils.vo.CourseOrderVo;
import org.snbo.eduService.bean.EduCourse;
import org.snbo.eduService.bean.vo.CourseInfoVo;
import org.snbo.eduService.bean.vo.CoursePublishInfo;
import org.snbo.eduService.bean.vo.CourseQuery;
import org.snbo.eduService.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
@RestController
@RequestMapping("/eduService/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;


    @PostMapping("/{current}/{size}")
    @ApiOperation(value = "(查)多条件分页查询")
    public R getPageCourseInfo(@PathVariable Long current, @PathVariable Long size
            , @RequestBody(required = false) CourseQuery courseQuery) {

        Page<EduCourse> page = new Page<>(current, size);

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id", subjectId);
        }
        wrapper.orderByDesc("gmt_modified");
        courseService.page(page, wrapper);
        return R.ok().data("total", page.getTotal()).data("courses", page.getRecords());
    }

    @PostMapping
    @ApiOperation(value = "(增)添加课程信息")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "(查)根据课程id查询课程信息")
    public R getCourseInfo(@PathVariable String id) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfoById(id);
        return R.ok().data("courseInfo", courseInfoVo);
    }

    @PutMapping("/courseInfo")
    @ApiOperation(value = "(改)根据id修改课程信息和简介")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfo) {
        boolean flag = courseService.updateCourseInfo(courseInfo);
        if (flag) return R.ok();
        else return R.error();
    }

    @GetMapping("/getPublishCourseInfo/{id}")
    @ApiOperation(value = "(查)根据id查询最终发布需要反馈的数据")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishInfo publishCourseInfo = courseService.getPublishCourseInfo(id);
        return R.ok().data("publishCourseInfo", publishCourseInfo);
    }

    @PutMapping
    @ApiOperation(value = "(改)更改课程状态为已发布")
    public R updateStatus(@RequestBody EduCourse course) {
        course.setStatus("Normal");
        courseService.updateById(course);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "(删)根据id删除课程")
    public R deleteCourseById(@PathVariable String id) {
        courseService.removeAllInfo(id);
        return R.ok();
    }

    @GetMapping("/getOrderCourseInfo/{courseId}")
    public CourseOrderVo getOrderCourse(@PathVariable String courseId) {
        return courseService.getOrderCourseInfo(courseId);

    }

    @GetMapping("/courseCount/{day}")
    public Integer getCourseCount(@PathVariable String day){
        return courseService.getCourseCount(day);
    }
}

