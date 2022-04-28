package org.snbo.eduStatistics.bean.vo;

import lombok.Data;

import java.util.List;

/**
 * @author sunbo
 * @create 2022-04-21-19:58
 */
@Data
public class ChartQueryVo {
    //查询的数据类型
    private List<String> typeList;
    //开始日期
    private String start;
    //截止日期
    private String end;
}
