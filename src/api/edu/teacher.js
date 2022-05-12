import request from '@/utils/request'

export default {
    // 讲师列表(按条件分页查询)
    getTeacherList(current, size, teacherQuery) {
        return request({
            // url: '/eduService/teachers/pageCondition/'+current+'/'+size,
            url: `/eduService/teachers/pageCondition/${current}/${size}`,
            method: 'post',
            //data 表示把对象转换成 json 形式传递到接口里
            data: teacherQuery,
        })
    },
    removeTeacherList(id) {
        return request({
            url: `/eduService/teachers/${id}`,
            method: 'delete',
        })
    },
    saveTeacher(teacher){
        return request({
            url: '/eduService/teachers',
            method: 'post',
            data: teacher,
        })
    },
    getTeacherById(id){
        return request({
            url: `/eduService/teachers/${id}`,
            method: 'get'
        })
    },
    updateTeacher(teacher){
        return request({
            url: '/eduService/teachers',
            method: 'put',
            data: teacher
        })
    }
}
