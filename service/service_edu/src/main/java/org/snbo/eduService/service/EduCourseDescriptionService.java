package org.snbo.eduService.service;

import org.snbo.eduService.bean.EduCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
public interface EduCourseDescriptionService extends IService<EduCourseDescription> {
    void removeByCourseId(String courseId);
}
