package org.snbo.eduOrder.service;

import org.snbo.eduOrder.bean.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-17
 */
public interface OrderService extends IService<Order> {
    String createOrder(String courseId, String memberId);

    Boolean isBuy(String courseId, String memberId);
}
