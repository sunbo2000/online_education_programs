package org.snbo.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.snbo.commonutils.R;
import org.snbo.eduService.bean.EduTeacher;
import org.snbo.eduService.bean.vo.TeacherQuery;
import org.snbo.eduService.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-03-17
 */

@Api(value = "讲师管理")
@RestController
@RequestMapping("/eduService/teachers")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "(查)查询所有讲师信息")
    @GetMapping
    public R findAll() {
        //调用list方法查询
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("teachers", list);
    }

    @ApiOperation(value = "(删)逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师id") @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("(查)分页查询讲师信息")
    @GetMapping("page/{current}/{size}")
    public R pageList(@PathVariable Long current, @PathVariable Long size) {
        //创建分页对象
        Page<EduTeacher> teacherPage = new Page<>(current, size);
        // 会把所有的数据返回到 teacherPage 里面, 包括记录数, 表内信息, 是否有下一行等
        teacherService.page(teacherPage, null);
        long total = teacherPage.getTotal();//获取总记录数
        List<EduTeacher> list = teacherPage.getRecords();//获取数据集合
        return R.ok().data("total", total).data("teachers", list);
    }

    @ApiOperation("(查)多条件组合分页查询")
    @PostMapping("/pageCondition/{current}/{size}")
    public R pageCondition(@PathVariable Long current, @PathVariable Long size,
                           @RequestBody(required = false) TeacherQuery teacherQuery) {
        //page对像
        Page<EduTeacher> pageCondition = new Page<>(current, size);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询,条件判断代替动态 sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //条件判断
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        //大于等于
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        //小于等于
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //排序
        wrapper.orderByDesc("gmt_modified");

        teacherService.page(pageCondition, wrapper);
        //获取总记录数
        long total = pageCondition.getTotal();
        //获取数据集合
        List<EduTeacher> list = pageCondition.getRecords();

        return R.ok().data("total", total).data("teachers", list);
    }

    //添加讲师
    @ApiOperation("(增)添加讲师信息")
    @PostMapping
    public R saveTeacher(@RequestBody EduTeacher teacher) {
        boolean flag = teacherService.save(teacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //修改功能

    //先查后改
    @ApiOperation("(查)根据id查询讲师信息")
    @GetMapping("/{id}")
    public R getTeacherById(@PathVariable Long id) {
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    @ApiOperation("(改)根据id修改讲师信息")
    @PutMapping
    public R update(@RequestBody EduTeacher teacher) {
        boolean flag = teacherService.updateById(teacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

