package org.snbo.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.snbo.commonutils.JwtUtils;
import org.snbo.commonutils.vo.UCenterMember;
import org.snbo.eduService.bean.EduComment;
import org.snbo.eduService.bean.comment.CommentPart;
import org.snbo.eduService.bean.comment.ReplyPart;
import org.snbo.eduService.feignclient.UCenterClient;
import org.snbo.eduService.mapper.EduCommentMapper;
import org.snbo.eduService.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-04-15
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    private UCenterClient centerClient;

    @Override
    public Map<String, Object> getCommentsByCourseId(String courseId, Long current, Long size) {
        //创建评论集合
        List<CommentPart> commentPartList = new ArrayList<>();

        //分页查询
        Page<EduComment> page = new Page<>(current, size);
        //查询评论
        QueryWrapper<EduComment> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", courseId);
        wrapper1.eq("part_id", '0');
        wrapper1.orderByDesc("gmt_create");
        //分页查询
        baseMapper.selectPage(page, wrapper1);
        List<EduComment> eduComments = page.getRecords();
        //总记录数
        long total = page.getTotal();

        for (EduComment comment : eduComments) {
            CommentPart commentPart = new CommentPart();
            BeanUtils.copyProperties(comment, commentPart);
            List<ReplyPart> replayList = new ArrayList<>();
            commentPart.setReplyList(replayList);     //创建回复集合并放入评论中
            //评论放入评论集合中
            commentPartList.add(commentPart);

            //每次先查询出四条回复
            QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
            wrapper.eq("part_id", comment.getId());
            wrapper.orderByDesc("gmt_create");

            Page<EduComment> page1 = new Page<>(1,3);
            baseMapper.selectPage(page1,wrapper);
            List<EduComment> theReplays = page1.getRecords();
            long total2 = page1.getTotal();
            //回复总条数
            commentPart.setTotal(total2);

            for (EduComment theReplay : theReplays) {
                ReplyPart replyPart = new ReplyPart();
                BeanUtils.copyProperties(theReplay, replyPart);
                replayList.add(replyPart);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("commentList", commentPartList);
        map.put("total", total);
        return map;
    }

    @Override
    public void saveComment(EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            throw new MoguException(20001, "请登录");
        }
        UCenterMember member = centerClient.getMemberInfoById(memberId);

        comment.setMemberId(memberId);
        comment.setNickname(member.getNickname());
        comment.setAvatar(member.getAvatar());

        int insert = baseMapper.insert(comment);
        if (insert < 1) {
            throw new MoguException(20001, "评论失败");
        }
    }
}
