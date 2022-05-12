<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps
      :active="2"
      process-status="wait"
      align-center
      style="margin-bottom: 40px;"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>
    <el-button type="text" @click="openAddDialog">添加章节</el-button>

    <!-- 章节嵌套表单 -->
    <el-dialog title="添加章节" :visible.sync="dialogFormVisible">
      <el-form :model="chapter" ref="chapter" :rules="rules1">
        <el-form-item
          label="章节名称"
          :label-width="formLabelWidth"
          prop="title"
        >
          <el-input v-model="chapter.title" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="章节排序" :label-width="formLabelWidth">
          <el-input-number
            v-model="chapter.sort"
            :min="1"
            controls-position="center"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitFormChapter('chapter')"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 小节嵌套表单 -->
    <el-dialog title="添加课时" :visible.sync="videoDialogFormVisible">
      <el-form :model="video" ref="video" :rules="rules2">
        <el-form-item
          label="课时标题"
          :label-width="formLabelWidth"
          prop="title"
        >
          <el-input v-model="video.title" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="课时排序" :label-width="formLabelWidth">
          <el-input-number
            v-model="video.sort"
            :min="1"
            controls-position="center"
          />
        </el-form-item>
        <el-form-item label="是否免费" :label-width="formLabelWidth">
          <el-radio-group v-model="video.isFree">
            <el-radio :label="0">免费</el-radio>
            <el-radio :label="1">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频" :label-width="formLabelWidth">
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API + '/eduVod/video'"
            :limit="1"
            class="upload-demo"
          >
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">
                最大支持1G，<br />
                支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br />
                GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br />
                MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br />
                SWF、TS、VOB、WMV、WEBM 等视频格式上传
              </div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="videoDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitFormVideo('video')"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 章节 -->
    <ul class="chanpterList">
      <li v-for="chapter in chapterAndVideoList" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button
              type="text"
              @click="openVideoAddDialog(chapter.id, chapter.children)"
              >添加课时</el-button
            >
            <el-button
              style=""
              type="text"
              @click="openUpdateDialog(chapter.id)"
              >编辑</el-button
            >
            <el-button type="text" @click="removeChapter(chapter.id)"
              >删除</el-button
            >
          </span>
        </p>
        <!-- 视频 -->
        <ul class="chanpterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>
              {{ video.title }}
              <span class="acts">
                <el-button type="text" @click="openVideoUpdateDialog(video.id)"
                  >编辑</el-button
                >
                <el-button type="text" @click="removeVideo(video.id)"
                  >删除</el-button
                >
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next"
        >下一步</el-button
      >
    </div>
  </div>
</template>
<script>
import chapter from "@/api/edu/chapter";
import video from "@/api/edu/video";

export default {
  data() {
    return {
      BASE_API: process.env.BASE_API, // 接口API地址
      dialogFormVisible: false, //嵌套表单默认值
      videoDialogFormVisible: false, //小节嵌套表单
      formLabelWidth: "120px",
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseId: "", //课程id
      chapterAndVideoList: [], //章节列表
      chapter: {
        courseId: "",
        title: "",
        sort: 1
      },
      rules1: {
        title: [{ required: true, message: "请输入章节标题", trigger: "blur" }]
      },
      rules2: {
        title: [{ required: true, message: "请输入课时标题", trigger: "blur" }]
      },
      video: {
        courseId: "",
        chapterId: "",
        title: "",
        sort: 1,
        isFree: 1,
        videoSourceId: "",
        videoOriginalName: ""
      },
      fileList: []
    };
  },
  created() {
    this.init();
  },
  methods: {
    //初始化获取章节小节方法
    init() {
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
        // 根据id获取课程基本信息
        this.getChapterAndVideo();
      }
    },
    submitFormChapter(formName) {
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
    submitFormVideo(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //   //进行添加或修改操作
          this.saveOrUpdateVideo();
          //   console.log("success");
        } else {
          console.log("表单内容输入有误,检查下吧");
          return false;
        }
      });
    },
    //删除视频
    handleVodRemove() {
      video.deleteAliyunVod(this.video.videoSourceId).then(response => {
        this.$message.success("删除成功");
        this.fileList = [];
        this.video.videoSourceId = "";
        this.video.videoOriginalName = "";
      });
    },
    //删除视频前
    beforeVodRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    //上传视频之前调用的方法
    handleUploadExceed(files, fileList) {
      this.$message.warning("想要重新上传视频，请先删除已上传的视频");
    },
    //视频上传成功后调用的方法
    handleVodUploadSuccess(response, file, fileList) {
      this.video.videoSourceId = response.data.videoId;
      this.video.videoOriginalName = file.name;
    },
    //删除小节信息
    removeVideo(videoId) {
      this.$confirm("此操作将永久删除小节信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          video.removeVideoById(videoId).then(response => {
            this.$message.success("删除小节信息成功");
            this.getChapterAndVideo();
          });
        })
        .catch(error => {});
    },
    //获取章节和小节数据
    getChapterAndVideo() {
      chapter.getChapterAndVideo(this.courseId).then(response => {
        this.chapterAndVideoList = response.data.chapterAndVideo;
      });
    },
    //修改或添加小节
    saveOrUpdateVideo() {
      if (this.video.id) {
        this.updateVideo();
      } else {
        this.saveVideo();
      }
    },
    //弹出小节修改表单框
    openVideoUpdateDialog(videoId) {
      video.getVideoById(videoId).then(response => {
        this.videoDialogFormVisible = true;
        this.video = response.data.video;
        if (
          this.video.videoOriginalName != null &&
          this.video.videoOriginalName != ""
        ) {
          this.fileList = [{ name: this.video.videoOriginalName }];
        }
      });
    },
    //小节修改
    updateVideo() {
      video.updateVideo(this.video).then(response => {
        this.videoDialogFormVisible = false;
        this.$message.success("修改课时信息成功");
        this.getChapterAndVideo();
      });
    },
    //弹出小节添加表单框
    openVideoAddDialog(chapterId, chapterChildren) {
      this.video = {
        id: "", //如果修改后id有值,清空
        chapterId: chapterId,
        title: "",
        sort: chapterChildren.length + 1, //根据
        isFree: 0
      };
      this.fileList = [];
      this.videoDialogFormVisible = true;
    },
    //添加小节信息
    saveVideo() {
      this.video.courseId = this.courseId;
      video.addVideo(this.video).then(response => {
        this.videoDialogFormVisible = false;
        this.$message.success("添加小节成功");
        this.getChapterAndVideo();
      });
    },
    //************************************************************************************************ */
    //弹出章节添加表单框
    openAddDialog() {
      this.dialogFormVisible = true;
      this.chapter.id = ""; //每次修改后id会被赋值,下面的新增就会被误认为修改
      this.chapter.title = "";
      this.chapter.sort = this.chapterAndVideoList.length + 1;
    },
    //删除章节信息
    removeChapter(chapterId) {
      this.$confirm("此操作将永久删除章节信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          chapter.deleteChapterById(chapterId).then(response => {
            this.$message.success("删除章节信息成功");
            this.getChapterAndVideo();
          });
        })
        .catch(error => {});
    },
    //添加或修改
    saveOrUpdate() {
      if (this.chapter.id) {
        this.updateChapter();
      } else {
        this.saveChapter();
      }
    },
    //添加章节
    saveChapter() {
      this.chapter.courseId = this.courseId;
      chapter.addChapter(this.chapter).then(response => {
        this.dialogFormVisible = false;
        this.$message.success("添加章节成功");
        this.getChapterAndVideo();
      });
    },
    //弹出修改框
    openUpdateDialog(chapterId) {
      this.dialogFormVisible = true;
      chapter.getChapterById(chapterId).then(response => {
        this.chapter = response.data.chapter;
      });
    },
    //修改章节
    updateChapter() {
      chapter.updateChapterById(this.chapter).then(response => {
        this.dialogFormVisible = false;
        this.$message.success("修改章节信息成功");
        this.getChapterAndVideo();
      });
    },
    //点击上一步方法
    previous() {
      this.$router.push({ path: "/course/info/" + this.courseId });
    },
    //点击下一步方法
    next() {
      this.$router.push({ path: "/course/publish/" + this.courseId });
    }
  }
};
</script>
<style scoped>
.chanpterList {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}
.chanpterList li {
  position: relative;
}
.chanpterList p {
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #ddd;
}
.chanpterList .acts {
  float: right;
  font-size: 14px;
}
.videoList {
  padding-left: 50px;
}
.videoList p {
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #ddd;
}
</style>
