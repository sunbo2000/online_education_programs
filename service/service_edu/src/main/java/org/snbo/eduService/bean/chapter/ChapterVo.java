package org.snbo.eduService.bean.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunbo
 * @create 2022-03-27-21:04
 */
@Data
public class ChapterVo {
    //    唯一id
    private String id;
    private String title;
    //表示小节
    private List<VideoVo> children = new ArrayList<>();
}
