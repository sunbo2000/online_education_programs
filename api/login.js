import request from '@/utils/request'

export default {
    // 登录
    loginCheck(userInfo) {
        return request({
            url: "/eduCenter/member/login",
            method: 'post',
            data: userInfo
        })
    },
    //根据 token 值,获取用户信息
    getLoginUserInfo() {
        return request({
            url: "/eduCenter/member/getInfo",
            method: 'get'
        })
    }
}