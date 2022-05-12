import request from '@/utils/request'

export default {
    createStatistics(day) {
        return request({
            url: '/eduStatistics/daily/' + day,
            method: 'get'
        })
    },

    showData(searchObj) {
        return request({
            url: `/eduStatistics/daily`,
            method: 'post',
            data: searchObj
        })
    }
}