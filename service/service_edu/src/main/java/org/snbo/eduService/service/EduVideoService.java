package org.snbo.eduService.service;

import org.snbo.eduService.bean.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);
}
