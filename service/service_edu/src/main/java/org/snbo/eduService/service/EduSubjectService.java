package org.snbo.eduService.service;

import org.snbo.eduService.bean.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.eduService.bean.subject.SubjectType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-03-23
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<SubjectType> getAllSubject();
}
