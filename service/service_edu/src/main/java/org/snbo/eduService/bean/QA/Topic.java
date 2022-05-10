package org.snbo.eduService.bean.QA;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.snbo.eduService.bean.EduQa;
import org.snbo.eduService.bean.comment.ReplyPart;

import java.util.Date;
import java.util.List;

/**
 * @author sunbo
 * @create 2022-04-28-21:29
 */
@Data
public class Topic {

    @ApiModelProperty(value = "评论ID")
    private String id;

    @ApiModelProperty(value = "回复所属的块id, 评论的块id为0,回复的块id为父评论id")
    private String partId;

    @ApiModelProperty(value = "会员id")
    private String userId;

    @ApiModelProperty(value = "会员昵称")
    private String nickname;

    @ApiModelProperty(value = "会员头像")
    private String avatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论图片")
    private List<String> pictureList;

    @ApiModelProperty(value = "点赞数")
    private int good;

    @ApiModelProperty(value = "评论时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "回复的条数")
    private Integer total;

    private Boolean next;

    private Boolean previous;

    private Integer current;

    private Boolean showGood;

    private Integer pages;

    @ApiModelProperty(value = "评论块下的回复")
    private List<Comment> commentList;

    @ApiModelProperty(value = "是否显示回复分页条")
    private Boolean showPage;

}
