package org.snbo.eduService.service;

import org.snbo.eduService.bean.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-15
 */
public interface EduCommentService extends IService<EduComment>{
    Map<String,Object> getCommentsByCourseId(String courseId, Long current, Long size);
    void saveComment(EduComment comment, HttpServletRequest request);
}
