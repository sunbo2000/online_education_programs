<template>
  <div class="app-container">
    <template>
      <div class="app-container">
        <el-form
          label-width="400px"
          :model="teacher"
          :rules="rules"
          ref="teacher"
        >
          <el-form-item label="讲师名称" prop="name">
            <el-input v-model="teacher.name" style="width:200px" />
          </el-form-item>
          <el-form-item label="讲师排序">
            <el-input-number
              v-model="teacher.sort"
              controls-position="right"
              :min="0"
            />
          </el-form-item>
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-form-item label="讲师头衔">
            <el-select
              v-model="teacher.level"
              placeholder="请选择"
              style="width:200px"
            >
              <el-option :value="1" label="高级讲师" />
              <el-option :value="2" label="首席讲师" />
            </el-select>
          </el-form-item>
          <el-form-item label="讲师资历" prop="career">
            <el-input v-model="teacher.career" style="width:500px" />
          </el-form-item>
          <el-form-item label="讲师简介" prop="intro">
            <el-input
              v-model="teacher.intro"
              :rows="10"
              type="textarea"
              style="width:500px"
            />
          </el-form-item>
          <!-- 讲师头像 -->
          <el-form-item label="讲师头像">
            <!-- 头衔缩略图 -->
            <pan-thumb :image="teacher.avatar" />
            <!-- 文件上传按钮 -->
            <el-button
              type="primary"
              icon="el-icon-upload"
              @click="imagecropperShow = true"
              >更换头像
            </el-button>
            <!--
            v-show：是否显示上传组件
            :key：类似于id，如果一个页面多个图片上传控件，可以做区分
            :url：后台上传的url地址
            @close：关闭上传组件
            @crop-upload-success：上传成功后的回调 -->
            <image-cropper
              v-show="imagecropperShow"
              :width="300"
              :height="300"
              :key="imagecropperKey"
              :url="BASE_API + '/eduOss/fileOss'"
              field="file"
              @close="close"
              @crop-upload-success="cropSuccess"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              :disabled="saveBtnDisabled"
              type="primary"
              @click="submitForm('teacher')"
              >保存</el-button
            >
          </el-form-item>
        </el-form>
      </div>
    </template>
  </div>
</template>

<script>
import teacher from "@/api/edu/teacher";
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";

export default {
  components: { ImageCropper, PanThumb },
  data() {
    return {
      teacher: {
        //默认头像
        avatar:
          "https://edu-mogu.oss-cn-chengdu.aliyuncs.com/2022/03/23/%E5%B0%8F%E8%98%91%E8%8F%87.png"
      },
      rules: {
        name: [{ required: true, message: "请输入讲师姓名", trigger: "blur" }],
        career: [{ required: true, message: "请输入讲师资历", tigger: "blur" }],
        intro: [{ required: true, message: "请输入讲师简介", tigger: "blur" }]
      },
      //保存按钮是否禁用
      saveBtnDisabled: false,
      //上传弹框是否显示
      imagecropperShow: false,
      //上传组件的key值
      imagecropperKey: 0,
      //获取dev.evn.js里的地址
      BASE_API: process.env.BASE_API
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
      //渲染之前判断是修改页面还是添加页面,如果是修改页面就调用一下 getteacher 方法调出要修改的讲师的信息
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id;
        this.getTeacherById(id);
      } else {
        this.teacher = {
          sort: 1,
          level: 1,
          avatar:
            "https://edu-mogu.oss-cn-chengdu.aliyuncs.com/2022/03/23/%E5%B0%8F%E8%98%91%E8%8F%87.png"
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
    getTeacherById(id) {
      teacher.getTeacherById(id).then(response => {
        this.teacher = response.data.teacher;
      });
    },
    //添加讲师
    saveTeacher() {
      teacher.saveTeacher(this.teacher).then(response => {
        this.$message.success("添加成功");
        //回到列表界面,路由跳转
        this.$router.push({ path: "/teacher/table" });
      });
    },
    //修改讲师信息
    updateTeacher() {
      teacher.updateTeacher(this.teacher).then(response => {
        this.$message.success("修改成功");
        //回到列表界面,路由跳转
        this.$router.push({ path: "/teacher/table" });
      });
    },
    saveOrUpdate() {
      //判断修改还是添加讲师
      //根据是否有id判断
      if (!this.teacher.id) {
        //没有id,添加
        this.saveTeacher();
      } else {
        this.updateTeacher();
      }
    },
    //关闭上传弹框
    close() {
      this.imagecropperShow = false;
      this.imagecropperKey = this.imagecropperKey + 1;
    },
    //照片上传成功
    cropSuccess(data) {
      this.teacher.avatar = data.url;
      this.close();
    }
  }
};
</script>
