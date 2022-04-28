package org.snbo.eduService.service;

import org.snbo.eduService.bean.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.eduService.bean.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-03-26
 */
public interface EduChapterService extends IService<EduChapter> {
    List<ChapterVo> getChapterAndVideo(String courseId);
    boolean deleteChapter(String id);
    void removeByCourseId(String courseId);
}
