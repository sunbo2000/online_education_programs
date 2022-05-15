import request from '@/utils/request'
import * as url from "url";

export default {
  updateInfo(user) {
    return request({
      url: '/eduCenter/member',
      method: 'put',
      data: user
    })
  },
  //根据 token 值,获取用户信息
  getLoginUserInfo() {
    return request({
      url: "/eduCenter/member/getInfo",
      method: 'get'
    })
  },
  getAccountInfo(){
    return request({
      url: '/eduCenter/member/getAccount',
      method: 'get'
    })

  }
}
