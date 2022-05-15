import request from '@/utils/request'

export default {
    //查询热门课程和名师
    getIndexInfo() {
        return request({
            url: '/eduService/front/index',
            method: 'get',
        })
    }
}