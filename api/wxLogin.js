import request from '@/utils/request'

export default {
  toWxLogin() {
    return request({
      url: '/api/ucenter/wx/login',
      method: 'get',
    })
  }
}
