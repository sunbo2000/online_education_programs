package org.snbo.eduOrder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import org.snbo.eduOrder.bean.Order;
import org.snbo.eduOrder.bean.PayLog;
import org.snbo.eduOrder.mapper.PayLogMapper;
import org.snbo.eduOrder.service.OrderService;
import org.snbo.eduOrder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.snbo.eduOrder.utils.HttpClient;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-04-17
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OrderService orderService;

    //生成二维码
    @Override
    public Map<String, Object> createNative(String orderNo) {

        try {
            //如果redis里面有记录的话,直接返回就好,不必重复生成二维码(否则前端刷新一次就会有新的支付二维码)
            Map<String, Object> map3 = (Map<String, Object>) redisTemplate.opsForValue().get(orderNo);
            if (map3 != null) {
                Long time = redisTemplate.getExpire(orderNo, TimeUnit.SECONDS);
                map3.put("time", time);
                return map3;
            }

            //根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);
            //使用map设置生成二维码参数
            Map<String, String> map = new HashMap<>();
            //1、设置支付参数
            map.put("appid", "wx74862e0dfcf69954");
            map.put("mch_id", "1558950191");
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", order.getCourseTitle());//课程标题
            map.put("out_trade_no", orderNo);//订单号
            map.put("total_fee", order.getTotalFee().multiply(new
                    BigDecimal("100")).longValue() + "");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url",
                    "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            map.put("trade_type", "NATIVE");
            //发送httpclient请求,传递xml格式
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(map, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));

            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();//xml 格式返回的
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);//转换为map
            //4、封装返回结果集
            Map<String, Object> map1 = new HashMap<>();
            map1.put("out_trade_no", orderNo);
            map1.put("course_id", order.getCourseId());
            map1.put("total_fee", order.getTotalFee());
            map1.put("result_code", resultMap.get("result_code"));
            map1.put("code_url", resultMap.get("code_url"));
            map1.put("time", 1800);
            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            redisTemplate.opsForValue().set(orderNo, map1, 30, TimeUnit.MINUTES);
            return map1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MoguException(20001, "支付出问题啦");
        }
    }


    //查询订单的支付状态
    @Override
    public Map<String, String> queryStatus(String orderNo) {
        try {
            //1、封装参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //2、设置请求
            HttpClient client = new
                    HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //6、转成Map
            //7、返回
            return WXPayUtil.xmlToMap(xml);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MoguException(20001, "获取订单状态失败");
        }
    }

    //添加订单记录
    @Override
    public void updateOrderRecord(Map<String, String> map) {
        System.out.println(map);
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);
        if (order.getStatus() == 1) {
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);
        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }
}
