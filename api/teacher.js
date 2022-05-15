import request from '@/utils/request'

export default {
    getPageList(page, limit) {
        return request({
            url: `/eduService/frontTeacher/${page}/${limit}`,
            method: 'get'
        })
    },
    getById(teacherId) {
        return request({
            url: `/eduService/frontTeacher/${teacherId}`,
            method: 'get'
        })
    }
}