import request from '@/utils/request'

export default {
  login(username, password) {
    return request({
      url: '/admin/acl/index/login',
      method: 'post',
      data: {
        username,
        password
      }
    })
  },
  getInfo(token) {
    return request({
      url: '/admin/acl/index/info',
      method: 'get',
      params: { token }
    })
  },
  logout() {
    return request({
      url: '/admin/acl/index/logout',
      method: 'post'
    })
  },
  getMenu() {
    return request({
      url: '/admin/acl/index/menu',
      method: 'get'
    })
  }
}
