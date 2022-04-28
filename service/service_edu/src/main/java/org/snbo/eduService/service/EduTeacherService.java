package org.snbo.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.snbo.eduService.bean.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-03-17
 */
public interface EduTeacherService extends IService<EduTeacher> {
    List<EduTeacher> listIndexInfo();

    Map<String,Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);

    Map<String,Object> getTeacherInfoById(String teacherId);
}
