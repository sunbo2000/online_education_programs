package org.snbo.eduStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.snbo.commonutils.TimeUtils;
import org.snbo.eduStatistics.bean.Daily;
import org.snbo.eduStatistics.bean.vo.ChartQueryVo;
import org.snbo.eduStatistics.feignclient.CenterMemberClient;
import org.snbo.eduStatistics.feignclient.CourseClient;
import org.snbo.eduStatistics.mapper.DailyMapper;
import org.snbo.eduStatistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-04-19
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private CenterMemberClient client;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public Boolean createData(String day) {
        //获取注册人数
        Integer registerCount = client.countRegister(day);
        //获取课程观看次数
        Integer courseCount = courseClient.getCourseCount(day);
        //登录次数
        int loginCount;
        String loginCountStr = redisTemplate.opsForValue().get(TimeUtils.getLoginCountName(day));
        if (StringUtils.isEmpty(loginCountStr)) {
            loginCount = 0;
        } else {
            loginCount = Integer.parseInt(loginCountStr);
        }
        //播放次数
        int videoCount;
        String videoCountStr = redisTemplate.opsForValue().get(TimeUtils.getVideoCountName(day));
        if (StringUtils.isEmpty(videoCountStr)) {
            videoCount = 0;
        } else {
            videoCount = Integer.parseInt(videoCountStr);
        }
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        Daily daily = baseMapper.selectOne(wrapper);
        if (daily == null) {
            daily = new Daily();
            daily.setRegisterNum(registerCount);
            daily.setCourseNum(courseCount);
            daily.setLoginNum(loginCount);
            daily.setVideoViewNum(videoCount);
            daily.setDateCalculated(day);
            int insert = baseMapper.insert(daily);
            return insert > 0;
        } else {
            daily.setRegisterNum(registerCount);
            daily.setCourseNum(courseCount);
            daily.setLoginNum(loginCount);
            daily.setVideoViewNum(videoCount);
            int update = baseMapper.update(daily, wrapper);
            return update > 0;
        }

    }


    @Override
    public Daily getDailyData(String day) {
        //如果查询今天的数据,需要实时建立且查询
        if (day.equals(LocalDate.now().toString())) {
            createData(day);
        }
        //查询之前数据,直接查表
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> getShowData(ChartQueryVo chartQuery) {
        //如果查询今天的数据,需要实时建立且查询
        if (chartQuery.getEnd().equals(LocalDate.now().toString())) {
            createData(chartQuery.getEnd());
        }

        //后面判断类型会用到
        List<String> dateList = new ArrayList<>();
        List<Integer> loginNumList = null;
        List<Integer> registerNumList = null;
        List<Integer> courseNumList = null;
        List<Integer> videoNumList = null;

        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.select("date_calculated", String.join(",", chartQuery.getTypeList()));
        wrapper.between("date_calculated", chartQuery.getStart(), chartQuery.getEnd());
        List<Daily> dailies = baseMapper.selectList(wrapper);

        //根据类型 new 集合
        for (String type : chartQuery.getTypeList()) {
            switch (type) {
                case "login_num":
                    loginNumList = new ArrayList<>();
                    break;
                case "register_num":
                    registerNumList = new ArrayList<>();
                    break;
                case "video_view_num":
                    videoNumList = new ArrayList<>();
                    break;
                case "course_num":
                    courseNumList = new ArrayList<>();
                    break;
            }
        }
        for (Daily daily : dailies) {
            dateList.add(daily.getDateCalculated());

            if (loginNumList != null) loginNumList.add(daily.getLoginNum());

            if (registerNumList != null) registerNumList.add(daily.getRegisterNum());

            if (videoNumList != null) videoNumList.add(daily.getVideoViewNum());

            if (courseNumList != null) courseNumList.add(daily.getCourseNum());

        }

        Map<String, Object> map = new HashMap<>();
        map.put("dateList", dateList);
        if (loginNumList != null) map.put("loginNumList", loginNumList);
        if (registerNumList != null) map.put("registerNumList", registerNumList);
        if (videoNumList != null) map.put("videoNumList", videoNumList);
        if (courseNumList != null) map.put("courseNumList", courseNumList);
        return map;
    }
}
