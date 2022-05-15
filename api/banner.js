import request from '@/utils/request'

export default {
    getBanners() {
        return request({
            url: '/cmsService/bannerFront',
            method: 'get',
        })
    }
}