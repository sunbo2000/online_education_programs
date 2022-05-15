<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程详情 开始 -->
    <section class="container">
      <section class="path-wrap txtOf hLh30">
        <a href="/" title class="c-999 fsize14">首页</a>
        \
        <a href="/course" title class="c-999 fsize14">课程列表</a>
        \
        <span class="c-333 fsize14">{{ courseInfo.subjectLevelOne }}</span>
        \
        <span class="c-333 fsize14">{{ courseInfo.subjectLevelTwo }}</span>
      </section>
      <div>
        <article class="c-v-pic-wrap" style="height: 357px">
          <section class="p-h-video-box" id="videoPlay">
            <img :src="courseInfo.cover" :alt="courseInfo.title" class="dis c-v-pic"
                 style="width: 640px; height: 357px"/>
          </section>
        </article>
        <aside class="c-attr-wrap">
          <section class="ml20 mr15">
            <h2 class="hLh30 txtOf mt15">
              <span class="c-fff fsize24">{{ courseInfo.title }}</span>
            </h2>
            <section class="c-attr-jg">
              <span class="c-fff" style="position: relative; top: -3px">价格：</span>
              <b class="c-yellow" style="font-size: 24px">￥{{ courseInfo.price }}</b>
            </section>
            <section class="c-attr-mt c-attr-undis">
              <span class="c-fff fsize14">主讲： {{ courseInfo.teacherName }}&nbsp;&nbsp;&nbsp;</span>
            </section>
            <section class="c-attr-mt of">
              <span class="ml10 vam">
                <em class="icon18 scIcon"></em>
                <a class="c-fff vam" title="收藏" href="#">收藏</a>
              </span>
            </section>
            <section class="c-attr-mt">
              <a v-if="status" :href="/player/ + courseId + '?videoSourceId=' + videoSourceId" title="立即观看"
                 target="_blank" class="comm-btn c-btn-3">立即观看</a>
              <a v-else style="cursor:pointer;" @click="createOrder()" title="立即购买" target="_blank"
                 class="comm-btn c-btn-3">立即购买</a>
            </section>
          </section>
        </aside>
        <aside class="thr-attr-box">
          <ol class="thr-attr-ol">
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">购买数</span>
                <br/>
                <h6 class="c-fff f-fM mt10">{{ courseInfo.buyCount }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">课时数</span>
                <br/>
                <h6 class="c-fff f-fM mt10">{{ courseInfo.lessonNum }}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">浏览数</span>
                <br/>
                <h6 class="c-fff f-fM mt10">{{ courseInfo.viewCount }}</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div class="clear"></div>
      </div>
      <!-- /课程封面介绍 -->
      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div class="i-box">
              <div>
                <section id="c-i-tabTitle" class="c-infor-tabTitle c-tabtitle">
                  <a name="c-i" class="current" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10 pt20">
                <div>
                  <h6 class="c-i-content c-infor-title">
                    <span>课程介绍</span>
                  </h6>
                  <div class="course-txt-body-wrap">
                    <section class="course-txt-body">
                      <!-- 将 html 内容翻译过来 -->
                      <p v-html="courseInfo.description">
                        {{ courseInfo.description }}
                      </p>
                    </section>
                  </div>
                </div>
              </article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">
            <div>
              <section class="c-infor-tabTitle c-tab-title">
                <a title href="javascript:void(0)">主讲讲师</a>
              </section>
              <section class="stud-act-list">
                <ul style="height: auto">
                  <li>
                    <div class="u-face">
                      <a :href="'/teacher/' + courseInfo.teacherId">
                        <img :src="courseInfo.avatar" width="50" height="50" :alt="courseInfo.teacherName"/>
                      </a>
                    </div>
                    <section class="hLh30 txtOf">
                      <a class="c-333 fsize16 fl" :href="'/teacher/' + courseInfo.teacherId">{{
                          courseInfo.teacherName
                        }}</a>
                    </section>
                    <section class="hLh20 txtOf">
                      <span class="c-999">{{ courseInfo.intro }}</span>
                    </section>
                  </li>
                </ul>
              </section>
            </div>
          </div>
        </aside>
        <div class="clear"></div>
      </div>
    </section>
    <!-- /课程详情 结束 -->
  </div>
</template>
<script>
import course from "@/api/course";
import order from "@/api/order";


export default {
  data() {
    return {
      videoSourceId: "",
      courseId: "",
      courseInfo: {}, //课程信息
      chapterAndVideoList: [], //章节小节
      status: ''
    };
  },
  created() {
    this.getCourseInfo();
  },
  methods: {
    createOrder() {
      order.createOrder(this.courseId).then((response) => {
        //生成订单并且获取订单号,然后跳转到订单页面
        console.log(response)
        window.location.href = '/order/' + response.data.data.orderNo;
      })
    },
    getCourseInfo() {
      this.courseId = this.$route.params.id;
      course.getCourseInfoById(this.courseId).then((response) => {
        this.courseInfo = response.data.data.courseInfo;
        // 如果课程不免费,查询用户是否购买课程
        if (this.courseInfo.price != 0) {
          order.verifyBuyStatus(this.courseId).then(response => {
            console.log(response)
            if (response.data.data.status) {
              this.status = true;
            } else {
              this.status = false;
            }
          })
        } else {
          this.status = true;
        }
        this.chapterAndVideoList = response.data.data.chapterAndVideoInfo;
        this.videoSourceId =
          this.chapterAndVideoList[0].children[0].videoSourceId;
      });
    },
  },
};
</script>
