<template>
  <div class="app-container">
    <el-form label-width="400px">
      <el-form-item label="信息描述">
        <el-tag type="info">excel模版说明</el-tag>
        &nbsp;&nbsp;
        <el-tag>
          <i class="el-icon-download" />
          <a
            :href="
              'https://edu-mogu.oss-cn-chengdu.aliyuncs.com/excel/demo.xlsx'
            "
            >点击下载模版</a
          >
        </el-tag>
      </el-form-item>
      <el-form-item label="选择Excel">
        <el-upload
          ref="upload"
          :auto-upload="false"
          :on-success="fileUploadSuccess"
          :on-error="fileUploadError"
          :before-upload="judgeFileType"
          :disabled="importBtnDisabled"
          :limit="1"
          :action="BASE_API + '/eduService/subjects'"
          name="file"
          accept=".xls,.xlsx"
        >
          <el-button slot="trigger"  type="primary"
            >选取文件</el-button
          >
          <div slot="tip" class="el-upload__tip">选取.xls或.xlsx格式文件</div>
          <el-button
            style="margin-left: 10px;"

            type="success"
            @click="submitUpload"
            >{{ fileUploadBtnText }}</el-button
          >
        </el-upload>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      BASE_API: process.env.BASE_API, // 接口API地址
      fileUploadBtnText: "上传到服务器", // 按钮文字
      importBtnDisabled: false, // 按钮是否禁用,
    };
  },
  methods: {
    //提交前检查文件格式
    judgeFileType(file) {
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "xls";
      const extension2 = testmsg === "xlsx";
      if (!extension && !extension2) {
        this.$message.warning("请上传.xls或.xlsx格式文件!");
      }
      return extension || extension2;
    },
    //提交文件
    submitUpload() {
      var files = this.$refs.upload.uploadFiles; //获取文件判断是否为空
      if (files.length == 0) {
        this.$message.error("请选取文件");
      } else {
        this.$refs.upload.submit();
      }
    },
    //上传成功
    fileUploadSuccess() {
      this.importBtnDisabled = true; //上传成功后禁用按钮
      this.$message.success("添加课程分类成功");
      //跳转到课程分类列表
      this.$router.push({ path: "/subject/list" });
    },
    //上传失败
    fileUploadError() {
      this.$message.warning("添加课程分类失败");
    }
  }
};
</script>
