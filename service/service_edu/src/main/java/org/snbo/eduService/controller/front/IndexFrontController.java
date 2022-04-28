package org.snbo.eduService.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.xpath.internal.operations.Number;
import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.eduService.bean.EduCourse;
import org.snbo.eduService.bean.EduTeacher;
import org.snbo.eduService.service.EduCourseService;
import org.snbo.eduService.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sunbo
 * @create 2022-04-03-16:19
 */

@RestController
@RequestMapping("/eduService/front/index")
public class IndexFrontController {

    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    //查询前八条课程,前四名老师
    @GetMapping
    @ApiOperation(value = "(查)查询首页课程和名师信息")
    public R index(){
        //前八条课程记录
        List<EduCourse> courses = courseService.listIndexInfo();
        //前四条讲师记录
        List<EduTeacher> teachers = teacherService.listIndexInfo();
        return R.ok().data("courses",courses).data("teachers",teachers);
    }
}
