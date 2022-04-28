package org.snbo.eduOrder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.JwtUtils;
import org.snbo.commonutils.R;
import org.snbo.eduOrder.bean.Order;
import org.snbo.eduOrder.service.OrderService;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/eduOrder/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            throw new MoguException(28004, "未登录,请先登录...");
        }
        //创建订单,返回订单号
        String orderNo = orderService.createOrder(courseId, memberId);
        return R.ok().data("orderNo", orderNo);
    }

    @GetMapping("/{orderNO}")
    public R getOrderInfo(@PathVariable String orderNO) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNO);
        return R.ok().data("orderInfo", orderService.getOne(wrapper));
    }

    @ApiOperation(value = "查询用户是否购买该课程")
    @GetMapping("/verify/{courseId}")
    public R verify(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            throw new MoguException(28004, "未登录,请先登录...");
        }
        return R.ok().data("status", orderService.isBuy(courseId, memberId));
    }
}

