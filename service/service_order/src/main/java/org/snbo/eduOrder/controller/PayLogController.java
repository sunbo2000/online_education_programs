package org.snbo.eduOrder.controller;


import org.snbo.commonutils.R;
import org.snbo.eduOrder.bean.PayLog;
import org.snbo.eduOrder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/eduOrder/pay-log")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成二维码
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        Map<String, Object> map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    //查询订单支付状态
    @GetMapping("/{orderNo}")
    public R getStatus(@PathVariable String orderNo) {
        Map<String, String> map = payLogService.queryStatus(orderNo);
        if (map == null) {
            return R.error().message("支付出错啦");
        }
        if (map.get("trade_state").equals("SUCCESS")) {
            //添加记录到支付表,更改订单表状态
            payLogService.updateOrderRecord(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }


}

