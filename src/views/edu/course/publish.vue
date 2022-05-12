<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps
      :active="3"
      process-status="wait"
      align-center
      style="margin-bottom: 40px;"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <div class="ccInfo">
      <img :src="publishCourseInfo.cover" />
      <div class="main">
        <h2>{{ publishCourseInfo.title }}</h2>
        <p class="gray">
          <span>共{{ publishCourseInfo.lessonNum }}课时</span>
        </p>
        <p>
          <span
            >所属分类：{{ publishCourseInfo.oneSubject }} —
            {{ publishCourseInfo.twoSubject }}</span
          >
        </p>
        <p>课程讲师：{{ publishCourseInfo.teacherName }}</p>
        <h3 class="red">￥{{ publishCourseInfo.price }}</h3>
      </div>
    </div>
    <div>
      <el-button @click="previous">返回修改</el-button>
      <el-button
        :disabled="saveBtnDisabled"
        type="primary"
        @click="updateStatus"
        >发布课程</el-button
      >
    </div>
  </div>
</template>
<script>
import course from "@/api/edu/course";

export default {
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseId: "", //初始化一个课程id
      publishCourseInfo: {}
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      //给课程id从地址栏里赋值
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
      }
      //初始化调用获取数据
      this.getPublishCourseInfo();
    },
    //更改状态
    updateStatus() {
      this.$confirm("确认发布课程?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          course.updateCourseStatus({ id: this.courseId }).then(response => {
            this.$message.success("课程发布成功成功!");
            this.$router.push({path: '/course/list'})
          });
        })
        .catch(error => {});
    },
    //获取确认数据
    getPublishCourseInfo() {
      course.getPublishCourseInfo(this.courseId).then(response => {
        this.publishCourseInfo = response.data.publishCourseInfo;
        console.log(this.publishCourseInfo);
      });
    },
    //点击上一步方法
    previous() {
      console.log("previous");
      this.$router.push({ path: "/course/chapter/" + this.courseId });
    }
  }
};
</script>

<style scoped>
.ccInfo {
  background: #f5f5f5;
  padding: 20px;
  overflow: hidden;
  border: 1px dashed #ddd;
  margin-bottom: 40px;
  position: relative;
}
.ccInfo img {
  background: #d6d6d6;
  width: 500px;
  height: 278px;
  display: block;
  float: left;
  border: none;
}
.ccInfo .main {
  margin-left: 520px;
}
.ccInfo .main h2 {
  font-size: 28px;
  margin-bottom: 30px;
  line-height: 1;
  font-weight: normal;
}
.ccInfo .main p {
  margin-bottom: 10px;
  word-wrap: break-word;
  line-height: 24px;
  max-height: 48px;
  overflow: hidden;
}
.ccInfo .main p {
  margin-bottom: 10px;
  word-wrap: break-word;
  line-height: 24px;
  max-height: 48px;
  overflow: hidden;
}
.ccInfo .main h3 {
  left: 540px;
  bottom: 20px;
  line-height: 1;
  font-size: 28px;
  color: #d32f24;
  font-weight: normal;
  position: absolute;
}
</style>
