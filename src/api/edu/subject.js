import request from '@/utils/request'

export default {
    // 获取所有课程分类
    getSubjectList() {
        return request({
            url: '/eduService/subjects',
            method: 'get',
        })
    },
}