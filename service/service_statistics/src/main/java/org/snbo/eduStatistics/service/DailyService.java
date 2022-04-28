package org.snbo.eduStatistics.service;

import org.snbo.eduStatistics.bean.Daily;
import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.eduStatistics.bean.vo.ChartQueryVo;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-19
 */
public interface DailyService extends IService<Daily> {

    Boolean createData(String day);

    Daily getDailyData(String day);

    Map<String, Object> getShowData(ChartQueryVo chartQuery);

}
