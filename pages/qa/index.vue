<template>
  <div>
    <!-- 评论 -->
    <div style="position: absolute; left: 300px; width: 60%;">
      <el-form @submit.native.prevent v-show="searchDis" style="position: relative; top: 30px;left: 90px "
               :inline="true" :model="query"
               class="demo-form-inline">
        <el-form-item>
          <el-input class="inputS" v-model="query.content" @keyup.enter.native="getTopicList"
                    placeholder="来找找看~"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button round type="primary" :disabled="query.content === ''" @click="getTopicList()">搜索</el-button>
        </el-form-item>
      </el-form>
      <ul>
        <li v-for="(topic, index) in topicList" v-bind:key="index"
            style="margin: 30px; padding:20px;  background-color: rgb(240,250,250); border-radius: 10px;">
          <!-- 评论内容 -->
          <el-link :underline="false" :href="'/qa/'+topic.id" style="text-decoration: none;">
            <p style="font-size: 17px;width: 645px; cursor: pointer;">{{ topic.content.substring(0, 70) }}<span
              v-if="topic.content.length>70">·····</span>
            </p>
          </el-link>
          <div class="demo-image__preview"
               style="position: relative; top:20px; display: inline-block;margin-right: 10px; width: 150px;height: 150px;"
               v-for="(url,index) in topic.pictureList" v-if="index<4" :key="index">
            <el-image
              style="width: 150px;height: 150px"
              :src=url
              fit="cover"
              :preview-src-list=topic.pictureList
            ></el-image>
          </div>
          <div v-show="topic.pictureList" style="position: relative; top: 55px; left: -15px; float: right">
            <el-avatar shape="square" :size="90" :src="topic.avatar"></el-avatar>
          </div>
          <!-- 用户名 -->
          <div style="position: relative; bottom: 0; left: 650px; width: 140px; text-align: center">
            <p style=" font-size: 13px; font-weight: 600; color: gray" class="el-icon-user">
              {{ topic.nickname }}</p>
          </div>
          <!-- 评论时间 -->
          <div style="position: relative;top:24px; left: 645px; width: 180px">
            <font class="fsize12 c-999 ml5" style="font-size: 15px;">{{
                topic.gmtCreate
              }}</font>
          </div>
          <div style="position: relative; width: 180px">
          <span style="font-size: 17px; cursor: pointer; color: gray"
                :class="topic.showGood?'el-icon-goblet-square-full':'el-icon-goblet-square'"
                @click="addGood(index)"> {{ topic.good }}</span>
            &nbsp;&nbsp;&nbsp;<span style="font-size: 17px; cursor: pointer; color: gray"
                                    class="el-icon-chat-dot-round"> {{
              topic.total
            }}</span>
          </div>
        </li>
      </ul>
      <p v-if="total > topicList.length" style="text-align: center;margin:30px; font-size: 15px">
        <el-link @click="load" :underline="false" style="text-decoration:none">点击加载更多</el-link>
      </p>
      <p v-else style="text-align: center;margin:20px;font-size: 15px">没有更多了</p>
    </div>
    <!--    ------------------------------------------------------------------------------------------------>
    <div
      style="position: fixed; right: 230px; text-align: center; top: 350px; border-radius:40px; width: 60px; height: 240px;  background-color: rgb(250,250,250)">

      <div>
        <el-button style="position: relative;top: 0px;margin-top: 7px " icon="el-icon-refresh-right" @click="flash()"
                   circle></el-button>
      </div>
      <div>
        <el-button style="position: relative;   margin-top: 20px" icon="el-icon-search" @click="showSearch()"
                   circle></el-button>
      </div>
      <div>
        <el-button style="position: relative;   margin-top: 20px" icon="el-icon-edit"
                   type="primary" circle @click="showDrawer()"></el-button>
      </div>
      <div>
        <el-backtop style="position: relative; top: 0; margin-top: 20px; left: 10px" visibility-height="0">
          <el-button icon="el-icon-arrow-up" circle></el-button>
        </el-backtop>
      </div>

    </div>

    <el-drawer
      style="width: 80%; left: 140px"
      size="50%"
      title="发表话题"
      :visible.sync="drawer"
      direction="btt"
      :before-close="handleClose">
      <div>
        <div style="position: absolute; left: 30px; ">
          <el-input
            style=" width: 820px; font-size: 18px;  margin-bottom: 20px; display: block"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 3 }"
            resize="none"
            placeholder="请输入内容"
            v-model="qa.content">
          </el-input>

          <div @click="deleteV1 = !deleteV1" style="display: inline-block">
            <el-image v-show="imgUrl[0] || imgIndex === 1" :class="deleteV1 ?'image2':'image1'" fit="cover"
                      :src="imgUrl[0]"/>
          </div>

          <div @click="deleteV2 = !deleteV2" style="display: inline-block">
            <el-image v-show="imgUrl[1] || imgIndex === 2" :class="deleteV2 ?'image2':'image1'" fit="cover"
                      :src="imgUrl[1]"/>
          </div>

          <div @click="deleteV3 = !deleteV3" style="display: inline-block">
            <el-image v-show="imgUrl[2] || imgIndex === 3" :class="deleteV3 ?'image2':'image1'" fit="cover"
                      :src="imgUrl[2]"/>
          </div>

          <div @click="deleteV4 = !deleteV4" style="display: inline-block">
            <el-image v-show="imgUrl[3] || imgIndex === 4" :class="deleteV4 ?'image2':'image1'" fit="cover"
                      :src="imgUrl[3]"/>
          </div>

        </div>
        <div style="position: absolute; right: 200px;">
          <el-button type="primary" style="width: 100px; height: 50px" @click="addTopic()">发 布</el-button>

          <el-upload
            v-if="imgIndex < 4"
            style="position: absolute; top:100px; right: 0px;"
            action="http://175.178.189.89:9001/eduOss/fileOss"
            accept="image/jpeg,image/jpg,image/png"
            :before-upload="beforeUpload"
            :show-file-list="false"
            :limit="4"
            :on-success="handleSuccess">
            <i>
              <el-button>添加图片</el-button>
            </i>
          </el-upload>

          <el-button v-show="deleteV1 || deleteV2 || deleteV3 || deleteV4" type="danger"
                     style="position: absolute; top: 150px; right: 0;" @click="handleRemove()">删除照片
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import qa from '@/api/qa';
import cookie from "js-cookie";

export default {
  layout: "qa", //应用video布局

  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      drawer: false,
      query: {content: ''},
      size: 10,
      userInfo: "",
      topicList: [],
      total: '',
      disabled: true,
      noMore: false,
      loading: true,
      searchDis: false,
      qa: {
        userId: '',
        nickname: '',
        avatar: '',
        content: '',
        picture: '',
        partId: '0',
      },
      imgUrl: ['', '', '', ''],
      imgIndex: 0,
      deleteV1: false,
      deleteV2: false,
      deleteV3: false,
      deleteV4: false,
    };
  },
  created() {
    this.getUserInfo()
    this.getTopicList()
  },
  methods: {
    showDrawer() {
      if (this.userInfo === '') {
        this.$notify.info({
          offset: 200,
          title: '登录后即可发布话题',
          dangerouslyUseHTMLString: true,
          message: '<a href="/login" style="color: rgb(36,188,208); text-decoration:none">去登录</a>           <a href="/register" style="text-decoration:none">去注册</a>'
        });
      } else {
        this.drawer = !this.drawer
      }
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isJPG1 = file.type === 'image/jpg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isJPG1 && !isPNG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return (isJPG || isJPG1 || isPNG) && isLt2M;
    },
    handleSuccess(res, file) {
      if (res.success) {
        this.imgUrl[this.imgIndex] = res.data.url
        this.imgIndex++;
      } else {
        this.$message.error("上传失败,请稍后在试")
      }
    },
    handleRemove() {
      let i;
      if (this.deleteV4) {
        this.imgUrl[3] = ''
        this.imgIndex--;
        this.deleteV4 = false
      }
      if (this.deleteV3) {
        if (this.imgUrl[3] !== '') {
          for (i = 2; i <= 2; i++) {
            this.imgUrl[i] = this.imgUrl[i + 1]
          }
          this.imgUrl[3] = ''
        } else {
          this.imgUrl[2] = ''
        }
        this.imgIndex--;
        this.deleteV3 = false
      }
      if (this.deleteV2) {
        if (this.imgUrl[2] !== '') {
          for (i = 1; i <= 2; i++) {
            this.imgUrl[i] = this.imgUrl[i + 1];
          }
          this.imgUrl[3] = ''
        } else {
          this.imgUrl[1] = ''
        }
        this.imgIndex--
        this.deleteV2 = false
      }
      if (this.deleteV1) {
        if (this.imgUrl[1] !== '') {
          for (i = 0; i <= 2; i++) {
            this.imgUrl[i] = this.imgUrl[i + 1]
          }
          this.imgUrl[3] = ''
        } else {
          this.imgUrl[0] = ''
        }
        this.imgIndex--
        this.deleteV1 = false
      }
    },
    handleClose(done) {
      this.$confirm('确认取消发布话题？')
        .then(_ => {
          done();
        })
        .catch(_ => {
        });
    },
    addTopic() {
      if (this.imgUrl[0] !== '') {
        this.qa.picture = this.imgUrl[0]
        for (let i = 1; i < 4; i++) {
          if (this.imgUrl[i] !== '') {
            this.qa.picture = this.qa.picture + ',' + this.imgUrl[i]
          } else {
            break;
          }
        }
      }

      qa.addTopic(this.qa).then(response => {
        if (response.data.success) {
          this.$notify({
            title: "成功",
            message: "发布成功",
            type: "success",
            duration: 1000,
          });
          this.qa.content = ''
          this.qa.picture = ''
          this.imgUrl = ['', '', '', '']
          this.imgIndex = 0
          this.drawer = false
          this.getTopicList()
        }
      })
    },
    flash() {
      this.searchDis = false;
      this.query.content = ''
      if (this.size > this.topicList.length) {
        this.getTopicList()
      }
    },
    showSearch() {
      this.searchDis = !this.searchDis;
      this.query.content = '';
    },
    addGood(index) {
      var goods;
      var topic = this.topicList[index];
      var id = topic.id;
      if (!topic.showGood) {
        goods = this.topicList[index].good + 1;
        this.topicList[index].showGood = true;
      } else {
        goods = this.topicList[index].good - 1;
        this.topicList[index].showGood = false;
      }
      this.topicList[index].good = goods;

      qa.addGood(id, goods).then(response => {
      })
    },
    getTopicList() {
      qa.getTopicList(this.size, this.query.content).then((response) => {
        this.topicList = response.data.data.topicList
        this.total = response.data.data.total
      })
    },
    //加载
    load() {
      if (this.total > this.topicList.length) {
        this.size = this.size + 10;
        this.getTopicList();
      }
    },
    //获取用户头像,昵称
    getUserInfo() {
      var str = cookie.get("mogu_ucenter");
      if (str) {
        this.userInfo = JSON.parse(str);
        this.qa.userId = this.userInfo.id;
        this.qa.nickname = this.userInfo.nickname;
        this.qa.avatar = this.userInfo.avatar;
      }
    },
    //加颜色
    headerCellStyle() {
      return "background-color:rgb(241,242,243);";
    },
    cellStyle(row) {
      if (row.row.videoSourceId == this.vid) {
        return "color:#20aece; background-color:rgb(255,255,255)";
      } else {
        return "background-color:rgb(241,242,243);";
      }
    },
    rowHandleclick(row) {
      if (row.videoSourceId) {
        window.location.href =
          "/player/" + this.courseId + "?videoSourceId=" + row.videoSourceId;
      }
    },
  },
};
</script>
<style scoped>
.body-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 80px;
  left: 0;
  overflow-y: auto;
  background-color: rgb(250, 250, 250);
}

.theTableDiv {
  cursor: pointer;
  position: absolute;
  left: 1005px;
  top: 105px;
  width: 400px;
  border-radius: 10px;
  background-color: rgb(241, 242, 243);
}

.theTable {
  width: 100%;
  margin-bottom: 20px;
  border-radius: 10px;
  background-color: rgb(241, 242, 243);
}

.t_btn2 /deep/ .el-table,
.el-table__expanded-cell {
  background-color: transparent;
}

.t_btn2 /deep/ .el-table tr {
  background-color: transparent !important;
}

.t_btn2 /deep/ .el-table--enable-row-transition .el-table__body td,
.el-table .cell {
  background-color: transparent;
}

.t_btn2 /deep/ .el-table__row > td {
  border: none;
}

/* 清除底部横线 */
.el-table::before {
  height: 0px;
}

.lh-bj-list ul li {
  border-bottom: 1px solid #e9e9e9;
  padding: 12px 5px 2px 6px;
  position: relative;
  min-height: 5px;
}

::v-deep .company-table td,
::v-deep .company-table th.is-leaf {
  border-bottom: 1px solid transparent;
}

.inputS /deep/ .el-input__inner {
  border-radius: 20px;
  width: 600px;
  z-index: 1;
}

.image1 {
  width: 200px;
  height: 200px;
  display: inline-block;
  border: 1px solid gray;
}

.image2 {
  width: 200px;
  height: 200px;
  display: inline-block;
  border: 1px solid lightgreen;
  opacity: 0.5;
}
</style>
