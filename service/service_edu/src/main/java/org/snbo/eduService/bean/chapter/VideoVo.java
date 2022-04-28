package org.snbo.eduService.bean.chapter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sunbo
 * @create 2022-03-27-21:04
 */
@Data
public class VideoVo {
    private String id;
    private String title;
    @ApiModelProperty(value = "是否可以试听：1收费 0免费")
    private Integer isFree;
    @ApiModelProperty(value = "云端视频资源")
    private String videoSourceId;
}
