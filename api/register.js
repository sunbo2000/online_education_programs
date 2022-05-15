import request from '@/utils/request'

export default {
    // 根据手机号发验证码
    sendMessage(phoneNumber) {
        return request({
            url: `/eduMsm/msm/${phoneNumber}`,
            method: 'get'
        })
    },
    // 注册验证
    register(registerVo) {
        return request({
            url: '/eduCenter/member/register',
            method: 'post',
            data: registerVo
        })
    },
}