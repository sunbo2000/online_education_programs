<template>
  <div style="width: 1140px;">
    <!-- 回复 -->
    <div style="position: absolute;  left:330px; margin-top: 30px; margin-bottom: 30px;">
      <table border="1" cellspacing="0" width="800">
        <tr style="border: 1px solid DarkGray " v-for="(topic,index) in topicList" :key="index">
          <td style="background-color: rgb(250,250,250); width: 130px; height: 200px" align="center">
            <div>
              <div style="position: relative; top: 20px">
                <img :src="topic.avatar" style="width: 90px; height: 90px; border-radius: 5px">
              </div>
              <div style="position: relative; top: 0px;  line-height:15px; width: 80px">
                <font style="font-size: 10px;  color: rgb(24,156,209) ; position: relative; top: 20px; width: 80px">
                  {{ topic.nickname }}</font>
                <el-badge v-if="topic.id === partId" value="题主" class="item" type="primary"
                          style="position: relative;left: 35px; top: -15px;">
                </el-badge>
              </div>
              <br>
            </div>
          </td>
          <td>
            <div style="position: relative; top: -55px; margin: 15px;">
              <p style="font-size: 15px">{{ topic.content }}</p>
            </div>

            <div class="demo-image__preview"
                 style="position: relative; top:-55px; margin-left: 15px; margin-right: 15px"
                 v-for="(url,index) in topic.pictureList" v-if="index<4" :key="index">
              <el-image
                :src=url
                fit="contain"
                :preview-src-list=topic.pictureList
              ></el-image>
            </div>
            <!-- 评论时间 -->
            <div style="position: relative; bottom: 0; margin-bottom: 10px;left: 360px; width: 280px">
              <p style="display: inline-block; font-size: 15px; color: gray; margin-right: 20px; width: 140px">{{
                  topic.gmtCreate
                }}</p>
              <p
                style="display: inline-block; font-size: 17px; cursor: pointer; color: gray; width: 70px; margin-right: -20px"
                :class="topic.showGood?'el-icon-goblet-square-full':'el-icon-goblet-square'"
                @click="addGood(index,-1)"> {{
                  topic.good
                }}</p>
              &nbsp;&nbsp;&nbsp;<span style="font-size: 15px; cursor: pointer; color: gray; width: 50px"
                                      @click="showReply(topic,index)">回复</span>
            </div>

            <div style="position: relative; left: 15px">
              <!-- ------------------------------------------------------------------------------ -->
              <table style="border: 1px solid #E8E8E8; width:645px">
                <tr style="border: 1px solid #E8E8E8 " v-for="(comment,indexC) in topic.commentList" :key="indexC">
                  <td style="background-color: rgb(247,248,250); height: 80px">
                    <div
                      style="z-index: 2; float: left; position: relative; top: 10px; left: 10px; width: 50px; display: inline-block;">
                      <el-avatar shape="square" :size="30" :src="comment.avatar" alt="用户头像"></el-avatar>
                    </div>
                    <div
                      style="position: relative; top: 0px;  line-height:15px; width: 575px;   display: inline-block;">
                      <p
                        style="display: inline-block; font-size: 10px;  color: rgb(24,156,209) ; position: relative; top: 10px;">
                        {{ comment.nickname }}</p>
                      <p style="display: inline-block; font-size: 10px; position: relative; top: 10px;"
                         v-show="comment.toName">回复 {{ comment.toName }}</p>
                      <p style="display: inline-block; font-size: 10px; position: relative; top: 10px; ">: {{
                          comment.content
                        }}</p>
                    </div>
                    <!-- 评论时间 -->
                    <div style=" margin-top: 10px;  float: right; width: 268px">
                      <p style="display: inline-block; font-size: 10px; color: gray; margin-right: 20px; ">{{
                          comment.gmtCreate
                        }}</p>
                      <span style="font-size: 15px; cursor: pointer; color: gray; width: 60px; margin-right: -20px"
                            :class="comment.showGood?'el-icon-goblet-square-full':'el-icon-goblet-square'"
                            @click="addGood(index,indexC)"> {{ comment.good }}</span>
                      &nbsp;&nbsp;&nbsp;<span style="font-size: 10px; cursor: pointer; color: gray"
                                              @click="showReply(comment,index)">回复</span>
                    </div>

                  </td>
                </tr>
              </table>

              <!-- ------------------------------------------------ -->
              <div v-if="topic.total > 3 && !topic.showPage"
                   style="padding: 10px; background-color: rgb(247,248,250);font-size: 10px; width: 625px">
                还有{{ topic.total - 3 }}条回复，<span style="color: cadetblue; cursor: pointer"
                                                 @click="getCommentPage(1,index)">点击查看</span>
              </div>
              <div v-if="topic.showPage && topic.pages > 1" class="block"
                   style="padding: 10px; width: 625px; background-color: rgb(250,250,250)">
                <ul>
                  <li class="theLi" v-show="topic.current > 1" @click="getCommentPage(1,index)">
                    首页
                  </li>
                  <li class="theLi" v-show="topic.current > 1" @click="getCommentPage(topic.current -1,index)">
                    上一页
                  </li>
                  <li v-for="item in topic.pages" :key="item" @click="getCommentPage(item,index)"
                      :class="{liFormat: topic.current === item}"
                      style="display: inline-block; margin-right: 10px; font-size: 10px; cursor: pointer">{{ item }}
                  </li>
                  <li class="theLi"
                      v-show="topic.current < topic.pages" @click="getCommentPage(topic.current + 1,index)">下一页
                  </li>
                  <li class="theLi" v-show="topic.current < topic.pages" @click="getCommentPage(topic.pages,index)">尾页
                  </li>
                </ul>
              </div>
              <!--              回复框-->
              <div v-show="replyIndex === index"
                   style="padding: 10px 10px 10px 10px;width: 625px; background-color: rgb(247,248,250)">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 1, maxRows: 1 }"
                  resize="none"
                  :placeholder="'回复 '+qa.toName+' :'"
                  v-model="qa.content"
                  style="font-size: 13px; width: 500px;">
                </el-input>
                <el-button :disabled="qa.content.trim() === ''" type="primary"
                           style="height: 30px;margin-left: 10px; padding: 5px 10px 5px 10px" @click="addReplay()">回复
                </el-button>
              </div>
            </div>
          </td>
        </tr>
        <tr style="border: 1px solid DarkGray ">
          <td colspan="2">
            <div class="block" style="padding: 10px; background-color: rgb(250,250,250) ">
              <el-pagination
                hide-on-single-page
                prev-text="上一页"
                next-text="下一页"
                layout="prev, pager, next, jumper"
                :page-size="size"
                :total="total"
                :current-page="tCurrent"
                @current-change="getTopicList"
              >
              </el-pagination>
            </div>
          </td>
        </tr>
      </table>

    </div>

    <!--    -->
    <div
      style="position: fixed; right: 230px; text-align: center; top: 600px; width: 60px; height: 130px;  background-color: rgb(250,250,250)">
      <div>
        <el-button style="position: relative;   top: 4px" icon="el-icon-edit"
                   type="primary" @click="showDrawer()"></el-button>
      </div>
      <div>
        <el-backtop style="position: absolute; bottom: 4px; left: 10px" visibility-height="0">
          <el-button icon="el-icon-arrow-up"></el-button>
        </el-backtop>
      </div>
    </div>

    <el-drawer
      style="width: 80%; left: 140px"
      size="50%"
      title="回帖"
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
          <el-button type="primary" style="width: 100px; height: 50px" @click="addComment()">发 表</el-button>

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
      //主题内容
      topic: {},
      //主题id
      partId: '',
      size: 10,
      tCurrent: 1,
      userInfo: "",
      topicList: [],
      total: 0,
      cSize: 10,
      cTotal: 0,
      noMore: false,
      loading: true,
      qa: {
        partId: '',
        userId: '',
        nickname: '',
        avatar: '',
        content: '',
        picture: '',
        toName: ''
      },
      drawer: false,
      imgUrl: ['', '', '', ''],
      imgIndex: 0,
      deleteV1: false,
      deleteV2: false,
      deleteV3: false,
      deleteV4: false,
      replyIndex: -1,
    };
  },
  created() {
    this.partId = this.$route.params.id;
    this.getTopicList()
    this.getUserInfo()
  },
  methods: {
    showReply(item, index) {
      if (index === 0) {
        this.showDrawer()
        return;
      }
      if (item.partId === this.partId) {//回复层主,partId为层主id,层主的partId为楼主id
        this.qa.partId = item.id
        this.qa.toName = ''
      } else {
        this.qa.partId = item.partId
        this.qa.toName = item.nickname
      }
      this.qa.content = ''
      this.replyIndex = index;
    },
    addReplay() {
      qa.addTopic(this.qa).then(response => {
        if (response.data.success) {
          this.$notify({
            title: "成功",
            message: "发布成功",
            type: "success",
            duration: 1000,
          });
          this.qa.toName = ''
          this.qa.partId = ''
          this.qa.content = ''
          qa.getCount(this.topicList[this.replyIndex].id).then(response => {
            let current = Math.ceil(response.data.data.total / this.cSize)
            this.topicList[this.replyIndex].current = current
            this.getCommentPage(current, this.replyIndex)
            this.replyIndex = -1
          })

        }
      })
    },
    addComment() {
      this.qa.partId = this.partId;//partId
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
          this.qa.partId = ''
          this.qa.content = ''
          this.qa.picture = ''
          this.imgUrl = ['', '', '', '']
          this.imgIndex = 0
          this.drawer = false
          qa.getCount(this.partId).then(response => {
            this.tCurrent = Math.ceil(response.data.data.total / this.size)
            this.getTopicList(this.tCurrent)
            //跳到最后
            this.$nextTick(() => {
              window.scrollTo(0, document.body.scrollHeight)
            })
          })
        }
      })
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
    handleSuccess(res, file) {
      if (res.success) {
        this.imgUrl[this.imgIndex] = res.data.url
        this.imgIndex++;
      } else {
        this.$message.error("上传失败,请稍后在试")
      }
    },
    handleClose(done) {
      this.$confirm('确认取消回复？')
        .then(_ => {
          done();
        })
        .catch(_ => {
        });
    },
    showDrawer() {
      if (this.userInfo === '') {
        this.$notify.info({
          offset: 200,
          title: '登录后即可发布话题',
          dangerouslyUseHTMLString: true,
          message: '<a href="/login" style="color: rgb(36,188,208); text-decoration:none">去登录</a>           <a href="/register" style="text-decoration:none">去注册</a>'
        });
      } else {
        this.qa.toName = ''
        this.qa.content = ''
        this.drawer = !this.drawer
      }
    },
    addGood(index, indexC) {
      var id;
      var goods;
      if (indexC === -1) {
        var topic = this.topicList[index];
        id = topic.id;
        if (!topic.showGood) {
          goods = this.topicList[index].good + 1;
          this.topicList[index].showGood = true;
        } else {
          goods = this.topicList[index].good - 1;
          this.topicList[index].showGood = false;
        }
        this.topicList[index].good = goods;
      } else {
        var comment = this.topicList[index].commentList[indexC];
        id = comment.id;
        if (!comment.showGood) {
          goods = this.topicList[index].commentList[indexC].good + 1;
          this.topicList[index].commentList[indexC].showGood = true;
        } else {
          goods = this.topicList[index].commentList[indexC].good - 1;
          this.topicList[index].commentList[indexC].showGood = false;
        }
        this.topicList[index].commentList[indexC].good = goods;
      }
      qa.addGood(id, goods).then(response => {
      })
    },
    getCommentPage(cCurrent = 1, index) {
      qa.getCommentPage(this.topicList[index].id, cCurrent, this.cSize).then(response => {
        this.topicList[index].commentList = response.data.data.commentList;
        this.topicList[index].current = response.data.data.current;
        this.topicList[index].showPage = true;
        this.topicList[index].pages = response.data.data.pages;
        this.topicList[index].next = response.data.data.next;
        this.topicList[index].previous = response.data.data.previous;
        this.topicList[index].total = response.data.data.total
      })
    },
    getTopicList(current = 1) {
      qa.getTopicPage(this.partId, current, this.size).then((response) => {
        this.total = response.data.data.total;
        this.topicList = response.data.data.topicList
      })
      this.$nextTick(() => {
        window.scrollTo(0, 0)
      })
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
  },
};
</script>
<style scoped>
.theLi {
  background-color: rgb(255, 255, 255);
  cursor: pointer;
  display: inline-block;
  font-size: 10px;
  margin-right: 10px;
}

.liFormat {
  color: rgb(24, 156, 209);
  font-weight: bold;
}

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
