package org.snbo.eduService.mapper;

import org.snbo.commonutils.vo.CourseOrderVo;
import org.snbo.eduService.bean.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.snbo.eduService.bean.frontVo.CourseFrontInfoVo;
import org.snbo.eduService.bean.vo.CoursePublishInfo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishInfo getPublishCourseInfo(String courseId);
    CourseFrontInfoVo getFrontCourseInfo(String courseId);
    CourseOrderVo getOrderCourseInfo(String courseId);
    Integer getCourseCount(String day);
}
