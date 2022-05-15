import request from '@/utils/request'

export default {
  getTopicList(length, content) {
    return request({
      url: `/eduService/qa/topic/${length}?content=${content}`,
      method: 'get',
    })
  },
  getTopicInfo(partId) {
    return request({
      url: `/eduService/qa/${partId}`,
      method: 'get'
    })
  },
  getTopicPage(partId, current, size) {
    return request({
      url: `/eduService/qa/topics/${partId}/${current}/${size}`,
      method: 'get'
    })
  },
  getCommentPage(partId, current, size) {
    return request({
      url: `/eduService/qa/comments/${partId}/${current}/${size}`,
      method: 'get'
    })
  },
  addGood(id, goods) {
    return request({
      url: `/eduService/qa/good/${id}/${goods}`,
      method: 'put'
    })
  },
  addTopic(qa) {
    return request({
      url: '/eduService/qa',
      method: 'post',
      data: qa
    })
  },
  getCount(partId) {
    return request({
      url: '/eduService/qa/total/' + partId,
      method: 'get'
    })
  }
}
