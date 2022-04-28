package org.snbo.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bouncycastle.cms.jcajce.JcePasswordRecipientInfoGenerator;
import org.snbo.eduService.bean.EduCourse;
import org.snbo.eduService.bean.EduCourseDescription;
import org.snbo.eduService.bean.EduTeacher;
import org.snbo.eduService.bean.vo.CourseInfoVo;
import org.snbo.eduService.mapper.EduTeacherMapper;
import org.snbo.eduService.service.EduCourseService;
import org.snbo.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-03-17
 */

@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Autowired
    private EduCourseService courseService;

    @Override
    @Cacheable(value = "teachers", key = "'indexInfo'")
    public List<EduTeacher> listIndexInfo() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        wrapper.last("limit 4");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        baseMapper.selectPage(pageTeacher, wrapper);
        List<EduTeacher> records = pageTeacher.getRecords();
        long current = pageTeacher.getCurrent();
        long pages = pageTeacher.getPages();
        long size = pageTeacher.getSize();
        long total = pageTeacher.getTotal();

        boolean hasNext = pageTeacher.hasNext();
        boolean hasPrevious = pageTeacher.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public Map<String, Object> getTeacherInfoById(String teacherId) {
        EduTeacher eduTeacher = baseMapper.selectById(teacherId);

        //查询讲师的课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("teacher", eduTeacher);
        map.put("courses", courseList);

        return map;
    }
}