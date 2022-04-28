package org.snbo.eduStatistics.controller;


import org.snbo.commonutils.R;
import org.snbo.eduStatistics.bean.vo.ChartQueryVo;
import org.snbo.eduStatistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/eduStatistics/daily")
public class DailyController {

    @Autowired
    private DailyService dailyService;

//    //查询某一天的数据
//    @GetMapping("/{day}")
//    public R getDailyData(@PathVariable String day) {
//        Daily daily = dailyService.getDailyData(day);
//        return R.ok().data("daily", daily);
//    }

    //查询某一时间段的某个数据
    @PostMapping
    public R showData(@RequestBody ChartQueryVo chartQuery) {
        Map<String, Object> map = dailyService.getShowData(chartQuery);
        return R.ok().data(map);
    }

}

