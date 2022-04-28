package org.snbo.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.snbo.commonutils.vo.CourseOrderVo;
import org.snbo.eduService.bean.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.eduService.bean.frontVo.CourseFrontInfoVo;
import org.snbo.eduService.bean.frontVo.CourseQueryVo;
import org.snbo.eduService.bean.vo.CourseInfoVo;
import org.snbo.eduService.bean.vo.CoursePublishInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
public interface EduCourseService extends IService<EduCourse> {
    String saveCourseInfo(CourseInfoVo courseInfoVo);
    CourseInfoVo getCourseInfoById(String courseId);
    boolean updateCourseInfo(CourseInfoVo courseInfo);
    CoursePublishInfo getPublishCourseInfo(String id);
    void removeAllInfo(String courseId);
    List<EduCourse> listIndexInfo();
    Map<String,Object> getFrontCourseInfoPage(Page<EduCourse> coursePage, CourseQueryVo courseQueryVo);
    CourseFrontInfoVo getBaseCourseInfo(String courseId);
    CourseOrderVo getOrderCourseInfo(String courseId);

    Integer getCourseCount(String day);
}
