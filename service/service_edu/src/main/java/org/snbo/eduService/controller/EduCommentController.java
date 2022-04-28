package org.snbo.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.commonutils.vo.UCenterMember;
import org.snbo.eduService.bean.EduComment;
import org.snbo.eduService.bean.comment.CommentPart;
import org.snbo.eduService.bean.comment.ReplyPart;
import org.snbo.eduService.service.EduCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-04-15
 */
@RestController
@RequestMapping("/eduService/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @PostMapping
    public R saveComment(@RequestBody EduComment comment, HttpServletRequest request) {
        commentService.saveComment(comment, request);
        return R.ok();
    }


    @GetMapping("/{courseId}/{current}/{size}")
    @ApiOperation(value = "根据课程id查询课程评论")
    public R getCourseComment(@PathVariable String courseId, @PathVariable Long current
            , @PathVariable Long size) {
        Map<String, Object> map = commentService.getCommentsByCourseId(courseId, current, size);
        return R.ok().data(map);
    }

    @ApiOperation(value = "根据评论块id获取该评论下回复的分页查询")
    @GetMapping("/getReplayPage/{partId}/{current}/{size}")
    public R getReplayPage(@PathVariable String partId, @PathVariable Long current, @PathVariable Long size) {
        Page<EduComment> page = new Page<>(current, size);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("part_id", partId);
        wrapper.orderByDesc("gmt_create");
        commentService.page(page,wrapper);
        List<EduComment> records = page.getRecords();
        List<ReplyPart> partList = new ArrayList<>();
        for (EduComment record : records) {
            ReplyPart replyPart = new ReplyPart();
            BeanUtils.copyProperties(record,replyPart);
            partList.add(replyPart);
        }
        return R.ok().data("replayList",partList);
    }
}

