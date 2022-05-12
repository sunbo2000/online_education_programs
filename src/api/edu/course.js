import request from '@/utils/request'

export default {
    // 添加课程信息
    addCourseInfo(courseInfo) {
        return request({
            url: '/eduService/course',
            method: 'post',
            data: courseInfo
        })
    },
    // 获取所有讲师信息
    getAllTeachers() {
        return request({
            url: '/eduService/teachers',
            method: 'get',
        })
    },
    //根据id查询课程信息
    getCourseInfoById(courseId) {
        return request({
            url: '/eduService/course/' + courseId,
            method: 'get',
        })
    },
    //修改课程信息
    updateCourseInfo(courseInfo) {
        return request({
            url: '/eduService/course/courseInfo',
            method: 'put',
            data: courseInfo
        })
    },
    //课程确认信息的获取
    getPublishCourseInfo(courseId) {
        return request({
            url: `/eduService/course/getPublishCourseInfo/${courseId}`,
            method: 'get'
        })
    },
    //更新课程状态为已发布
    updateCourseStatus(course) {
        return request({
            url: '/eduService/course',
            method: 'put',
            data: course
        })
    },
    //分页查询课程
    getcourseList(current, size, courseQuery) {
        return request({
            url: `/eduService/course/${current}/${size}`,
            method: 'post',
            data: courseQuery
        })
    },
    //根据id删除课程所有信息
    deleteById(courseId){
        return request({
            url: 'eduService/course/'+courseId,
            method: 'delete',
        })
    }

}