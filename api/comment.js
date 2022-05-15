import request from '@/utils/request'

export default {
    getComment(courseId, current, size) {
        return request({
            url: `/eduService/comment/${courseId}/${current}/${size}`,
            method: 'get',
        })
    },
    addComment(comment) {
        return request({
            url: "/eduService/comment",
            method: 'post',
            data: comment
        })
    },
    getReplayPage(partId, current, size) {
        return request({
            url: `/eduService/comment/getReplayPage/${partId}/${current}/${size}`,
            method: 'get',
        })
    },
}