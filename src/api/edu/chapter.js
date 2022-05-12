import request from '@/utils/request'

export default {
    // 获取所有章节和小节
    getChapterAndVideo(courseId) {
        return request({
            url: `/eduService/chapter/getChapAndVideo/${courseId}`,
            method: 'get',
        })
    },
    //添加章节信息
    addChapter(Chapter) {
        return request({
            url: '/eduService/chapter',
            method: 'post',
            data: Chapter
        })
    },
    //根据id查询章节信息
    getChapterById(chapterId) {
        return request({
            url: '/eduService/chapter/' + chapterId,
            method: 'get',
        })
    },
    //根据id修改章节信息
    updateChapterById(chapter){
        return request({
            url: '/eduService/chapter',
            method: 'put',
            data: chapter
        })
    },
    //根据id删除章节信息
    deleteChapterById(chapterId){
        return request({
            url: `/eduService/chapter/${chapterId}`,
            method: 'delete'
        })
    }
}