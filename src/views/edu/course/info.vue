<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps
      :active="1"
      process-status="wait"
      align-center
      style="margin-bottom: 40px;"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-form
      label-width="120px"
      :model="courseInfo"
      :rules="rules"
      ref="courseInfo"
    >
      <el-form-item label="课程标题" prop="title">
        <el-input
          style="width: 600px"
          v-model="courseInfo.title"
          placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"
        />
      </el-form-item>
      <!-- 课程分类   :key 唯一标识-->
      <el-form-item label="课程分类" required>
        <el-col :span="4">
          <el-form-item prop="subjectParentId">
            <el-select
              v-model="courseInfo.subjectParentId"
              filterable
              placeholder="一级分类"
              @change="oneSubjectChange"
            >
              <el-option
                v-for="subject in oneSubjectList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <!-- 二级分类 -->
        <el-col :span="4">
          <el-form-item prop="subjectId">
            <el-select
              v-model="courseInfo.subjectId"
              filterable
              placeholder="二级分类"
              @change="refresh"
            >
              <el-option
                v-for="subject in twoSubjectList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-form-item>
      <!-- 课程讲师   :key 唯一标识-->
      <el-form-item label="课程讲师" prop="teacherId">
        <el-select
          v-model="courseInfo.teacherId"
          filterable
          placeholder="请选择"
        >
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>

      <!-- 课程简介-->
      <el-form-item label="课程简介">
        <tinymce
          :height="300"
          style="width: 1000px"
          v-model="courseInfo.description"
        />
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number
          :min="0"
          v-model="courseInfo.lessonNum"
          controls-position="right"
          placeholder="请填写课程的总课时数"
        />
      </el-form-item>

      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API + '/eduOss/fileOss'"
          class="avatar-uploader"
        >
          <img :src="courseInfo.cover" style="width:640px; height:357px" />
        </el-upload>
      </el-form-item>
      <!-- 课程价格 -->
      <el-form-item label="课程价格">
        <el-input-number
          :min="0"
          v-model="courseInfo.price"
          controls-position="right"
          placeholder="免费课程请设置为0元"
        />
        元
      </el-form-item>
      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="submitForm('courseInfo')"
          >保存并下一步</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import course from "@/api/edu/course";
import subject from "@/api/edu/subject";
import Tinymce from "@/components/Tinymce";

export default {
  components: { Tinymce },
  data() {
    return {
      courseInfo: {
        title: "",
        subjectId: "",
        subjectParentId: "",
        teacherId: "",
        lessonNum: 0,
        description: "",
        cover:
          "https://edu-mogu.oss-cn-chengdu.aliyuncs.com/2022/03/23/%E5%B0%81%E9%9D%A2.png",
        price: 0
      },
      rules: {
        title: [{ required: true, message: "请输入课程标题", tigger: "blur" }],
        subjectParentId: [
          { required: true, message: "请选择一级课程分类", trigger: "blur" }
        ],
        subjectId: [
          { required: true, message: "请选择二级课程分类", trigger: "blur" }
        ],
        teacherId:[{required: true, message: "请选择讲师", trigger: "blur"}]
      },
      saveBtnDisabled: false, // 保存按钮是否禁用
      teacherList: [], //所有讲师的集合
      oneSubjectList: [], //一级分类集合
      twoSubjectList: [], //二级分类集合
      BASE_API: process.env.BASE_API, // 接口API地址
      courseId: "" //课程id
    };
  },
  created() {
    this.init();
  },
  watch: {
    //路由变化方式,路由发生变化,方法就会执行
    $route(to, from) {
      this.init();
    }
  },
  methods: {
    init() {
      //增加数据和修改数据都需要讲师列表和分类列表
      this.getTeachers();
      this.getOneSubject();
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
        this.getCourseInfoById();
      } else {
        this.courseInfo = {
          lessonNum: 0,
          cover:
            "https://edu-mogu.oss-cn-chengdu.aliyuncs.com/2022/03/23/%E5%B0%81%E9%9D%A2.png",
          price: 0
        };
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //   //进行添加或修改操作
          this.saveOrUpdate();
          //   console.log("success"); 
        } else {
          console.log("表单内容输入有误,检查下吧");
          return false;
        }
      });
    },
    //增加或修改
    saveOrUpdate() {
      //有id即修改没有即添加
      if (this.courseInfo.id) {
        this.updateCourseInfo();
      } else {
        this.saveData();
      }
    },
    //修改课程信息
    updateCourseInfo() {
      course.updateCourseInfo(this.courseInfo).then(response => {
        this.$message.success("修改课程信息成功!");
        this.$router.push({
          path: "/course/chapter/" + this.courseId
        });
      });
    },
    //修改的时候根据id查询课程信息
    getCourseInfoById() {
      course.getCourseInfoById(this.courseId).then(response => {
        this.courseInfo = response.data.courseInfo;
        //默认的二级列表要在一级手动选择之后才有值,这里是修改给二级分类列表直接赋值
        for (var i = 0; i < this.oneSubjectList.length; i++) {
          if (this.oneSubjectList[i].id == this.courseInfo.subjectParentId) {
            this.twoSubjectList = this.oneSubjectList[i].children;
          }
        }
      });
    },
    //获取一级分类
    getOneSubject() {
      subject.getSubjectList().then(response => {
        this.oneSubjectList = response.data.subjects;
      });
    },
    //一级分类改变//value 为v-model绑定值
    oneSubjectChange(value) {
      this.courseInfo.subjectId = "";
      for (var i = 0; i < this.oneSubjectList.length; i++) {
        if (this.oneSubjectList[i].id == value) {
          this.twoSubjectList = this.oneSubjectList[i].children;
        }
      }
    },
    //强制刷新二级分类选择器,解决有数据但选不了的尴尬局面
    refresh() {
      this.$forceUpdate(); //强制更新
    },
    //获取所有讲师信息
    getTeachers() {
      course.getAllTeachers().then(response => {
        this.teacherList = response.data.teachers;
      });
    },
    //上传封面之前调用
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    //上传封面成功回调方法
    handleAvatarSuccess(res, file) {
      console.log(res); // 上传响应
      console.log(URL.createObjectURL(file.raw)); // base64编码
      this.courseInfo.cover = res.data.url;
    },
    // 保存
    saveData() {
      course.addCourseInfo(this.courseInfo).then(response => {
        this.$message.success("添加课程信息成功!");
        this.$router.push({
          path: "/course/chapter/" + response.data.courseId
        });
      });
    }
  }
};
</script>
<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>
