package org.snbo.eduOrder.service.impl;

import ch.qos.logback.classic.joran.JoranConfigurator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.snbo.commonutils.vo.CourseOrderVo;
import org.snbo.commonutils.vo.UCenterMember;
import org.snbo.eduOrder.bean.Order;
import org.snbo.eduOrder.feignclient.EduClient;
import org.snbo.eduOrder.feignclient.UCenterClient;
import org.snbo.eduOrder.mapper.OrderMapper;
import org.snbo.eduOrder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.snbo.eduOrder.utils.OrderNoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-04-17
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;
    @Autowired
    private UCenterClient centerClient;

    @Override
    public String createOrder(String courseId, String memberId) {
        //远程调用获取用户信息和课程信息
        UCenterMember member = centerClient.getMemberInfoById(memberId);
        CourseOrderVo orderCourse = eduClient.getOrderCourse(courseId);
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setMemberId(member.getId());
        order.setNickname(member.getNickname());
        order.setMobile(member.getMobile());
        order.setCourseId(orderCourse.getId());
        order.setCourseTitle(orderCourse.getTitle());
        order.setCourseCover(orderCourse.getCover());
        order.setTotalFee(orderCourse.getPrice());
        order.setTeacherName(orderCourse.getTeacherName());

        order.setStatus(0); //默认未支付
        order.setPayType(1); //默认微信支付
        baseMapper.insert(order);
        return order.getOrderNo();
    }

    @Override
    public Boolean isBuy(String courseId, String memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        Integer integer = baseMapper.selectCount(wrapper);
        return integer > 0;
    }
}
