<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li>
                  <a title="全部" href="#" :class="oneIndex == -1 ? 'active' : ''" @click="getAll">全部</a>
                </li>
                <li v-for="(subject1, index) in subjectNestedList" :key="index">
                  <a
                    :class="oneIndex == index ? 'active' : ''"
                    :title="subject1.title"
                    href="#"
                    @click="searchOne(subject1.id, index)"
                  >{{ subject1.title }}</a
                  >
                </li>
              </ul>
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"></span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li v-for="(subject2, index) in subSubjectList" :key="index">
                  <a
                    :class="twoIndex == index ? 'active' : ''"
                    :title="subject2.title"
                    href="#"
                    @click="searchTwo(subject2.id, index)"
                  >{{ subject2.title }}</a
                  >
                </li>
              </ul>
            </dd>
          </dl>
          <div class="clear"></div>
        </section>
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <section class="fl">
            <ol class="js-tap clearfix">
              <li :class="viewCount != '' ? 'current bg-orange' : ''">
                <a title="关注度" href="#" @click="searchViewCount()">最热</a>
              </li>
              <li :class="gmtCreateSort != '' ? 'current bg-orange' : ''">
                <a title="最新" href="#" @click="searchGmtCreate()">最新</a>
              </li>
              <li :class="priceSort != '' ? 'current bg-orange' : ''">
                <a title="价格" href="#" @click="searchPrice()"
                >价格&nbsp;
                  <span v-if="priceSort == ''">⇅</span>
                  <span v-if="priceSort == '1'">↑</span>
                  <span v-if="priceSort == '2'">↓</span>
                </a>
              </li>
            </ol>
          </section>
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="total == 0">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam"
            >没有相关数据，小编正在努力整理 中...</span
            >
          </section>
          <!-- /无数据提示 结束-->
          <article class="comm-course-list">
            <ul class="of" id="bna">
              <li v-for="(course, index) in courseList" :key="index">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img
                      :src="course.cover"
                      class="img-responsive"
                      :alt="course.title"
                    />
                    <div class="cc-mask">
                      <a
                        :href="'/course/' + course.id"
                        title="开始学习"
                        class="comm-btn c-btn-1"
                      >开始学习</a
                      >
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a
                      href="/course/1"
                      :title="course.title"
                      class="course-title fsize18 c-333"
                    >{{ course.title }}</a
                    >
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span
                      class="fr jgTag bg-green"
                      v-if="Number(course.price) === 0"
                    >
                      <i class="c-fff fsize12 f-fA">免费</i>
                    </span>
                    <span class="fr jgTag" v-if="Number(course.price) != 0">
                      <i style="color: red; font-size: 20px"
                      >￥{{ course.price }}</i
                      >
                    </span>
                    <i
                      class="el-icon-video-play"
                      style="font-size: 13px; color: gray"
                    >&nbsp;{{ course.viewCount }}</i
                    >
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <i class="el-icon-time" style="font-size: 13px; color: gray"
                    >&nbsp;‧ {{ course.gmtCreate }} ‧</i
                    >
                  </section>
                </div>
              </li>
            </ul>
            <div class="clear"></div>
          </article>
        </div>
        <!-- 公共分页 开始 -->
        <el-pagination
          background
          layout="total, prev, pager, next, jumper"
          :total="total"
          :current-page="current"
          :page-size="size"
          style="padding: 30px 0; text-align: center"
          @current-change="getPage"
        >
        </el-pagination>
        <!-- 公共分页 结束 -->
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>
<script>
import course from "@/api/course";

export default {
  data() {
    return {
      total: 0,
      current: 1,
      size: 8,
      courseList: [],
      subjectNestedList: [], // 一级分类列表
      subSubjectList: [], // 二级分类列表
      searchObj: {}, // 查询表单对象
      oneIndex: -1,
      twoIndex: -1,
      viewCount: "",
      gmtCreateSort: "",
      priceSort: "",
    };
  },
  created() {
    let title = this.$route.query.key;
    if (title) {
      this.searchObj.title = title
    }
    this.initSubject();
    this.getPage();
  },
  methods: {
    getPage(current = 1) {
      course.getPageList(current, this.size, this.searchObj).then((response) => {
        this.courseList = response.data.data.courseInfo;
        for (let i = 0; i < this.courseList.length; i++) {
          let time = this.courseList[i].gmtCreate;
          if (time.substring(0, 4) === "2022") {
            this.courseList[i].gmtCreate = time.substring(5, 10);
          } else {
            this.courseList[i].gmtCreate = time.substring(0, 10);
          }
        }
        this.total = response.data.data.total;
      });
    },
    //查询所有一级分类
    initSubject() {
      course.getAllSubject().then((response) => {
        this.subjectNestedList = response.data.data.subjects;
      });
    },
    //点击一级分类，显示对应的二级分类，查询数据
    searchOne(subjectParentId, index) {
      this.oneIndex = index;
      this.twoIndex = -1;
      this.searchObj.subjectId = "";
      this.subSubjectList = [];
      this.searchObj.subjectParentId = subjectParentId;
      this.getPage(this.page);
      this.subSubjectList = this.subjectNestedList[index].children;
    },
    //点击二级分类，查询数据
    searchTwo(subjectId, index) {
      this.twoIndex = index;
      this.searchObj.subjectId = subjectId;
      this.getPage(this.page);
    },
    getAll() {
      //二级分类取消
      this.subSubjectList = {}
      this.oneIndex = -1
      //只是不分类了
      this.searchObj.subjectParentId = ''
      this.searchObj.subjectId = ''
      this.getPage(1);
    },
    //购买量查询
    searchViewCount() {
      if (this.viewCount == "") {
        this.viewCount = "1";
      } else {
        this.viewCount = "";
      }

      this.gmtCreateSort = "";
      this.priceSort = "";
      this.searchObj.viewCount = this.viewCount;
      this.searchObj.gmtCreateSort = this.gmtCreateSort;
      this.searchObj.priceSort = this.priceSort;
      this.getPage(this.page);
    },
    //更新时间查询
    searchGmtCreate() {
      debugger;
      this.viewCount = "";
      if (this.gmtCreateSort == "") {
        this.gmtCreateSort = "1";
      } else {
        this.gmtCreateSort = "";
      }

      this.priceSort = "";
      this.searchObj.viewCount = this.viewCount;
      this.searchObj.gmtCreateSort = this.gmtCreateSort;
      this.searchObj.priceSort = this.priceSort;
      this.getPage(this.page);
    },
    //价格查询
    searchPrice() {
      this.viewCount = "";
      this.gmtCreateSort = "";
      //1为升序,2位降序,空位不按价格排序
      if (this.priceSort == "") {
        this.priceSort = "1";
      } else if (this.priceSort == "1") {
        this.priceSort = "2";
      } else if (this.priceSort == "2") {
        this.priceSort = "";
      }
      this.searchObj.viewCount = this.viewCount;
      this.searchObj.gmtCreateSort = this.gmtCreateSort;
      this.searchObj.priceSort = this.priceSort;
      this.getPage(this.page);
    },
  },
};
</script>
<style scoped>
.active {
  color: #20aece;
}

.hide {
  display: none;
}

.show {
  display: block;
}
</style>
