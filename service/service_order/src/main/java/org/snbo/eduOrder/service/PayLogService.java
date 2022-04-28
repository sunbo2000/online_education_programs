package org.snbo.eduOrder.service;

import org.snbo.eduOrder.bean.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-17
 */
public interface PayLogService extends IService<PayLog> {
    Map<String,Object> createNative(String orderNo);

    Map<String,String> queryStatus(String orderNo);

    void updateOrderRecord(Map<String, String> map);
}
