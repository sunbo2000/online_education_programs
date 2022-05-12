import request from '@/utils/request'

export default {
    // 添加小节信息
    addVideo(video) {
        return request({
            url: 'eduService/video',
            method: 'post',
            data: video
        })
    },
    //删除小节信息
    removeVideoById(videoId) {
        return request({
            url: 'eduService/video/' + videoId,
            method: 'delete',
        })
    },
    //更改小节信息
    updateVideo(video) {
        return request({
            url: 'eduService/video',
            method: 'put',
            data: video
        })
    },
    getVideoById(videoId){
        return request({
            url: `eduService/video/${videoId}`,
            method: 'get'
        })
    },
    //删除视频
    deleteAliyunVod(videoId){
        return request({
            url: 'eduVod/video/'+videoId,
            method: 'delete'
        })
    }
}