package org.snbo.eduStatistics.schedule;

import org.snbo.eduStatistics.service.DailyService;
import org.snbo.eduStatistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author sunbo
 * @create 2022-04-19-22:29
 */
@Component
public class ScheduleTask {

    @Autowired
    private DailyService dailyService;

    //每天 00:00:01 刷新前一天数据,当天数据实时查询
    @Scheduled(cron = "1 0 0 * * ?")
    public void createStatistics() {
        dailyService.createData(DateUtils.formatDate(DateUtils.addDays(new Date(), -1)));
    }
}
