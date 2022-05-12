import request from '@/utils/request'

export default {
    //分页获取所有banner
    getPageBannerInfo(current, size) {
        return request({
            url: `/cmsService/bannerAdmin/${current}/${size}`,
            method: 'get'
        })
    },
    // 根据id获得banner信息
    getBannerById(id) {
        return request({
            url: `/cmsService/bannerAdmin/${id}`,
            method: 'get',
        })
    },
    //更改banner信息
    updateBanner(banner) {
        return request({
            url: '/cmsService/bannerAdmin',
            method: 'put',
            data: banner
        })
    },
    //增加banner数据
    saveBannerInfo(banner) {
        return request({
            url: '/cmsService/bannerAdmin',
            method: 'post',
            data: banner
        })
    },
    //删除banner数据
    deleteBannerInfo(id) {
        return request({
            url: '/cmsService/bannerAdmin/' + id,
            method: 'delete'
        })
    }


}