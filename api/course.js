import request from '@/utils/request'

export default {
    getPageList(page, limit, courseQuery) {
        return request({
            url: `/eduService/frontCourse/${page}/${limit}`,
            method: 'post',
            data: courseQuery
        })
    },

    //查询所有课程分类信息
    getAllSubject() {
        return request({
            url: '/eduService/subjects',
            method: 'get'
        })
    },

    //获取课程详情信息
    getCourseInfoById(courseId) {
        return request({
            url: '/eduService/frontCourse/' + courseId,
            method: 'get'
        })
    },

    //单独获取章节小节信息
    getChapterAndVideo(courseId) {
        return request({
            url: '/eduService/frontCourse/getChapterAndVideo/' + courseId,
            method: 'get'
        })
    },
    //获取播放界面课程信息
    getPlayerInfo(id) {
        return request({
            url: `/eduService/frontCourse/getPlayerInfo/${id}`,
            method: 'get'
        })
    },
    //根据课程名搜索课程
    queryCourse(courseName){
      return request({
        url: '/eduService/frontCourse/?courseName='+courseName,
        method: 'get'
      })
    },
}
