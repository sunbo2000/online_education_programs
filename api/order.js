import request from '@/utils/request'

export default {
    createOrder(courseId) {
        return request({
            url: `/eduOrder/order/${courseId}`,
            method: 'post',
        })
    },
    getOrderinfo(orderNo) {
        return request({
            url: `/eduOrder/order/${orderNo}`,
            method: 'get'
        })
    },
    //3、生成微信支付二维码
    createNative(cid) {
        return request({
            url: '/eduOrder/pay-log/createNative/' + cid,
            method: 'get'
        })
    },
    //4、根据id获取订单支付状态
    queryPayStatus(cid) {
        return request({
            url: '/eduOrder/pay-log/' + cid,
            method: 'get'
        })
    },

    verifyBuyStatus(courseId) {
        return request({
            url: `/eduOrder/order/verify/${courseId}`,
            method: 'get'
        })
    },
}
