import request from '@/utils/request'

export default {
    getPlayAuth(videoSourceId) {
        return request({
            url: `/eduVod/video/${videoSourceId}`,
            method: 'get'
        })
    }
}