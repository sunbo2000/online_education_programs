<template>
  <div class="body-bg">
    <!-- 阿里云视频播放器样式 -->
    <link rel="stylesheet" href="https://g.alicdn.com/de/prismplayer/2.8.1/skins/default/aliplayer-min.css"/>
    <!-- 阿里云视频播放器脚本 -->
    <script charset="utf-8" type="text/javascript" src="https://g.alicdn.com/de/prismplayer/2.8.1/aliplayer-min.js"/>

    <div style="position: absolute; top: 30px; left: 75px">
      <div style="font-size: 22px; position: relative; top: -10px">
        {{ courseInfo.title }}
      </div>
      <div style="font-size: 15px; color: gray; position: relative; top: -4px">
        <span class="el-icon-video-play">&nbsp;{{ courseInfo.viewCount }}</span>
        &nbsp;&nbsp;&nbsp;
        <span class="el-icon-chat-line-square">&nbsp;{{ total }}</span>
        &nbsp;&nbsp;&nbsp;
        <span class="el-icon-time">&nbsp;{{ courseInfo.gmtCreate }}</span>
      </div>
    </div>
    <!-- 定义播放器dom -->
    <div style="position: absolute; top: 105px; left: 75px" id="J_prismPlayer" class="prism-player"/>

    <div style="position: absolute; top: 650px; left: 75px; width: 900px">
      <h6 class="c-c-content c-infor-title" id="i-art-comment">
        <span class="commentTitle" style="font-size: 18px">评论</span> {{ total }}
      </h6>

      <section class="lh-bj-list pr mt20 replyhtml">
        <ul>
          <li class="unBr">
            <div style="display: inline-block; position: relative; top:-4px">
              <el-avatar :size="45" :src="userInfo.avatar" alt="用户头像"></el-avatar>
            </div>&nbsp;&nbsp;&nbsp;&nbsp;
            <div class="of" style="display: inline-block">
              <section class="n-reply-wrap">
                <fieldset>
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 3 }" resize="none" placeholder="发布一条友善的评论"
                            v-model="thecomment.content" style="width: 700px; font-size: 30px">
                  </el-input>&nbsp;&nbsp;&nbsp;&nbsp;
                  <el-button type="primary" :disabled="thecomment.content.trim() == ''"
                             style="display: inline; height: 54px; width: 100px" @click="addComment(index)">评论
                  </el-button>
                </fieldset>
              </section>
            </div>
          </li>
        </ul>
      </section>

      <!-- 评论 -->
      <br/>
      <div style="border-bottom: 1px solid #ccc"/>
      <section class="" style="position: absolute; top: 135px; left: 64px; width:800px">
        <section class="question-list lh-bj-list pr">
          <ul v-infinite-scroll="load" infinite-scroll-delay=500 infinite-scroll-disabled="disabled">
            <li v-for="(comment, index) in commentList" v-bind:key="index">
              <!-- 头像 -->
              <div style="position: relative; top: 15px; left: 0px">
                <aside class="noter-pic">
                  <el-avatar :size="45" @error="avatarErr" :src="comment.avatar" alt="用户头像"
                             style="position: relative; top: -15px; left: -63px"></el-avatar>
                </aside>

                <!-- 用户名 -->
                <div class="of">
                    <span>
                      <font style="font-size: 13px; font-weight: 600; color: gray">
                        {{ comment.nickname }}</font>
                    </span>
                </div>
                <!-- 评论内容 -->
                <div class="noter-txt mt5">
                  <p style="font-size: 15px">{{ comment.content }}</p>
                </div>
                <!-- 评论时间 -->
                <div class="of mt5" style="position: relative; left: -6px">
                  <font class="fsize12 c-999 ml5" style="font-size: 15px;margin-right:30px">{{
                      comment.gmtCreate
                    }}</font>
                  <el-link type="info" style="position: absolute; top: -1px" @click="showReplay(comment,index)">回复
                  </el-link>
                </div>
                <!-- 回复 -->
                <div v-if="comment.replyList.length != 0" style="position: relative; left: -10px">
                  <el-table :data="comment.replyList" class="company-table" style="width: 100%" :show-header="false"
                            :cell-style="'background-color:rgb(250,250,250)'">
                    <el-table-column>
                      <template slot-scope="scope">
                        <div style="display: inline-block">
                          <!-- 用户名 -->
                          <div style="
                                display: inline-block;
                                position: relative;
                                top: -3px;
                              ">
                            <el-avatar :size="25" :src="scope.row.avatar" alt="用户头像"
                                       style="position: relative; top:5px"></el-avatar>
                            <font style="
                                  font-size: 13px;
                                  font-weight: 600;
                                  color: gray;
                                ">
                              &nbsp;{{ scope.row.nickname }}</font>
                            <span>回复 @{{ scope.row.replay }} :</span>
                            <span>
                                <font style="
                                  font-size: 15px;
                                ">{{ scope.row.content }}</font>
                              </span>
                          </div>
                          <!-- 评论时间 ------------------------------------------------------------------------------------>
                          <div class="of mt5" style="position: relative; left: 27px;width: 500px">
                            <font class="fsize12 c-999 ml5" style="font-size: 15px;margin-right:30px">{{
                                scope.row.gmtCreate
                              }}</font>
                            <el-link :underline="false" type="info"
                                     style="width:100px; align:left; position: absolute; top: -1px; left: 150px"
                                     @click="showReplay(scope.row,index)">回复
                            </el-link>
                          </div>
                        </div>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div v-if="comment.total > 3 && comment.replyList.length == 3 && reCurrent == 1">
                    <font style="font-size: 14px; color:gray; position: relative; top:2px">共{{
                        comment.total
                      }}条回复，</font>
                    <el-link :underline="false" @click="showAllReplay(index)">点击查看</el-link>
                  </div>
                  <div v-if="comment.showPage">
                    <!-- 分页------------------------------------------------------------------------------------- -->
                    <el-pagination prev-text="上一页" background next-text="下一页"
                                   style="padding: 20px 0; position: relative; left: 15px" small
                                   layout="total,prev, pager, next" :page-size="reSize" :total="comment.total"
                                   @prev-click="getReplayPage(--reCurrent,index)"
                                   @next-click="getReplayPage(++reCurrent,index)">
                    </el-pagination>
                  </div>
                </div>
              </div>
              <!-- 回复输入框 _________________________________________________________________________-->
              <div v-if="index == indexReplay">
                <section class="lh-bj-list pr mt20 replyhtml">
                  <ul>
                    <li class="unBr">
                      <div style="display: inline-block; position: relative;left: -14px; top:-4px">
                        <el-avatar :size="45" :src="userInfo.avatar" alt="用户头像"></el-avatar>
                      </div>&nbsp;
                      <div class="of" style="display: inline-block;">
                        <section class="n-reply-wrap">
                          <fieldset>
                            <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 3 }" resize="none"
                                      :placeholder="'回复 @'+thereplay.replay+':'" v-model="thereplay.content"
                                      style="width: 600px; font-size: 30px">
                            </el-input>&nbsp;&nbsp;&nbsp;&nbsp;
                            <el-button type="primary" :disabled="thereplay.content.trim() === ''"
                                       style="display: inline; height: 54px; width: 100px" @click="addReplay(index)">回复
                            </el-button>
                          </fieldset>
                        </section>
                      </div>
                    </li>
                  </ul>
                </section>
              </div>
              <br/>
            </li>
          </ul>
          <p v-if="loading" style="text-align: center;margin:50px; font-size: 15px"> 正在加载...</p>
          <p v-if="noMore" style="text-align: center;margin:20px;font-size: 15px">没有更多了</p>
        </section>
      </section>
    </div>

    <!-- 课程大纲 -->
    <div class="theTableDiv">
      <el-table :data="chapterAndVideoList" class="theTable" row-key="id" height="540" border default-expand-all
                :tree-props="{ children: 'children' }" @row-click="rowHandleclick" :cell-style="cellStyle"
                :header-cell-style="headerCellStyle">
        <el-table-column label="课程选集" prop="title">
          <template slot-scope="scope">
              <span v-if="scope.row.isFree === 0" class="fr">
                <i class="free-icon vam mr10">免费试听</i> </span><em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>
            {{ scope.row.title }}
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- /课程大纲 -->
  </div>
</template>

<script>
import video from "@/api/video";
import course from "@/api/course";
import comment from "@/api/comment";
import cookie from "js-cookie";

export default {
  layout: "video", //应用video布局
  data() {
    return {
      disabled: false,
      noMore: false,
      loading: true,
      userInfo: "",
      thecomment: {
        courseId: "",
        memberId: "",
        nickName: "",
        avatar: "",
        content: "",
      },
      indexReplay: '-1',
      thereplay: {
        partId: '',
        replay: '',
        courseId: "",
        memberId: "",
        nickName: "",
        avatar: "",
        content: "",
      },
      reSize: 10,
      reCurrent: 1,
      replayPartId: '',
      replayList: [],
      size: 10,
      commentList: [],
      total: "",
      chapterAndVideoList: [],
      courseInfo: {},
      buttonDisplay: false
    };
  },
  created() {
    debugger
    this.courseId = this.$route.params.id;
    this.getVideo();
    this.getcourseInfo();
    this.getComments();
    this.getUserInfo();
  },
  methods: {
    showAllReplay(index) {
      this.getReplayPage(this.reCurrent, index);
      this.indexPage = index;
    },
    getReplayPage(current = 1, index) {
      comment.getReplayPage(this.commentList[index].id, current, this.reSize).then(response => {
        this.commentList[index].replyList = response.data.data.replayList
        if (this.commentList[index].total > 10) {
          this.commentList[index].showPage = true;
        }
      })
    },
    //添加回复
    showReplay(item, index) {
      // 每一项的下标唯一区分回复框显示在哪
      if (item.partId == 0) { //回复楼主
        this.thereplay.partId = item.id
      } else {//非楼主
        this.thereplay.partId = item.partId
      }
      this.thereplay.content = ''
      this.thereplay.replay = item.nickname;
      this.indexReplay = index;
    },
    addReplay(index) {
      if (this.userInfo == '') {
        this.$notify.info({
          offset: 200,
          title: '请先登录',
          dangerouslyUseHTMLString: true,
          message: '<a href="/login">去登录</a>           <a href="/register">去注册</a>'
        });
        return;
      }
      this.thereplay.courseId = this.courseId;
      this.thereplay.memberId = this.userInfo.id;
      this.thereplay.nickName = this.userInfo.nickName;
      this.thereplay.avatar = this.userInfo.avatar;
      comment.addComment(this.thereplay).then((response) => {
        if (response.data.success) {
          this.$notify({
            title: "成功",
            message: "回复成功",
            type: "success",
            duration: 1000,
          });
          this.indexReplay = '-1'//不在显示
          this.thereplay.content = "";
          this.getReplayPage(this.reCurrent = 1, index);
        } else {
          this.$message.error(response.data.message);
        }
      });
    },
    //加载
    load() {
      if (this.total > this.commentList.length) {
        this.loading = true;
        this.size = this.size + 10;
        this.getComments();
      } else {
        this.loading = false;
        this.noMore = true;
        this.disabled = true;
      }

    },
    //添加评论
    addComment() {
      if (this.userInfo == '') {
        this.$notify.info({
          offset: 200,
          title: '请先登录',
          dangerouslyUseHTMLString: true,
          message: '<a href="/login">去登录</a>           <a href="/register">去注册</a>'
        });
        return;
      }
      this.thecomment.courseId = this.courseId;
      this.thecomment.memberId = this.userInfo.id;
      this.thecomment.nickName = this.userInfo.nickName;
      this.thecomment.avatar = this.userInfo.avatar;
      comment.addComment(this.thecomment).then((response) => {
        if (response.data.success) {
          this.$notify({
            title: "成功",
            message: "评论发布成功",
            type: "success",
            duration: 1000,
          });
          this.getComments();
          this.thecomment.content = ''
        } else {
          this.$message.error(response.data.message);
        }
      });
    },
    //获取用户头像,昵称
    getUserInfo() {
      var str = cookie.get("mogu_ucenter");
      if (str) {
        this.userInfo = JSON.parse(str);
      }
    },
    //获取评论
    getComments(current = 1) {
      comment.getComment(this.courseId, current, this.size).then((response) => {
        this.commentList = response.data.data.commentList;
        this.total = response.data.data.total;
      });
    },
    getcourseInfo() {
      course.getPlayerInfo(this.courseId).then((response) => {
        this.courseInfo = response.data.data.courseInfo;
      });
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
    getVideo() {
      course.getChapterAndVideo(this.courseId).then((response) => {
        this.chapterAndVideoList = response.data.data.chapterAndVideoInfo;
      });
    },
    getAuth() {
      video.getPlayAuth(this.vid).then((response) => {
        this.Auth = response.data.data.playAuth;
      });
    },
  },
  asyncData({params, query, error}) {
    return video.getPlayAuth(query.videoSourceId).then((response) => {
      return {
        vid: query.videoSourceId,
        Auth: response.data.data.playAuth,
      };
    });
  },
  /**
   * 页面渲染完成时：此时js脚本已加载，Aliplayer已定义，可以使用
   * 如果在created生命周期函数中使用，Aliplayer is not defined错误
   */
  mounted() {
    new Aliplayer(
      {
        id: "J_prismPlayer",
        vid: this.vid, // 视频id
        playauth: this.Auth, // 播放凭证
        encryptType: "1", // 如果播放加密视频，则需设置encryptType=1，非加密视频无需设
        width: "900px",
        height: "506px",
        // 以下可选设置
        qualitySort: "asc", // 清晰度排序
        mediaType: "video", // 返回音频还是视频
        autoplay: false, // 自动播放
        isLive: false, // 直播
        rePlay: false, // 循环播放
        preload: true,
        controlBarVisibility: "hover", // 控制条的显示方式：鼠标悬停
        useH5Prism: true, // 播放器类型：html5
      },
      function (player) {
        console.log("播放器创建成功");
      }
    );
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

.body-bg ::-webkit-scrollbar {
  display: none;
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
  overflow-y: hidden;
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
</style>
