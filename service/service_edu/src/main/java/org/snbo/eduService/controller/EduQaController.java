package org.snbo.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.snbo.commonutils.R;
import org.snbo.eduService.bean.EduQa;
import org.snbo.eduService.bean.QA.Topic;
import org.snbo.eduService.service.EduQaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-04-28
 */
@RestController
@RequestMapping("/eduService/qa")
public class EduQaController {

    @Autowired
    private EduQaService qaService;

    @PostMapping
    public R saveTopic(@RequestBody EduQa qa) {
        qaService.save(qa);
        return R.ok();
    }

    @GetMapping("/{partId}")
    public R getTopicById(@PathVariable String partId) {
        EduQa qa = qaService.getById(partId);
        Topic topic = new Topic();
        BeanUtils.copyProperties(qa, topic);
        return R.ok().data("topic", topic);
    }

    @GetMapping("topic/{size}")
    public R getTopicList(@PathVariable String size, @RequestParam("content") String content) {
        return R.ok().data(qaService.getTopic(size, content));
    }

    @GetMapping("/topics/{partId}/{current}/{size}")
    public R getTopicList(@PathVariable String partId, @PathVariable Long current,
                          @PathVariable Long size) {
        Map<String, Object> map = qaService.getTopicPage(partId, current, size);
        return R.ok().data(map);
    }

    @GetMapping("comments/{partId}/{current}/{size}")
    public R getComment(@PathVariable String partId, @PathVariable Long current, @PathVariable Long size) {
        Page<EduQa> page = new Page<>(current, size);
        Map<String, Object> map = qaService.getCommentPage(partId, page);
        return R.ok().data(map);
    }

    @GetMapping("total/{partId}")
    public R getCount(@PathVariable String partId) {
        QueryWrapper<EduQa> wrapper = new QueryWrapper<>();
        wrapper.eq("part_id", partId);
        return R.ok().data("total", qaService.count(wrapper));
    }

    @PutMapping("/good/{id}/{goods}")
    public R updateGood(@PathVariable String id, @PathVariable Integer goods) {
        EduQa qa = qaService.getById(id);
        qa.setGood(goods);
        qaService.updateById(qa);
        return R.ok();
    }
}

