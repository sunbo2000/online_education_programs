<template>
  <client-only>
    <div class="in-wrap">
      <!-- 公共头引入 -->
      <header id="header">
        <section class="container">
          <h1 id="logo">
            <a href="#" title="蘑菇学院">
              <img
                src="~/assets/img/frontlogo.png"
                width="100%"
                alt="蘑菇学院"
              />
            </a>
          </h1>
          <div class="h-r-nsl">
            <ul class="nav">
              <router-link to="/" tag="li" active-class="current" exact>
                <a>首页</a>
              </router-link>
              <router-link to="/course" tag="li" active-class="current">
                <a>课程</a>
              </router-link>
              <router-link to="/teacher" tag="li" active-class="current">
                <a>名师</a>
              </router-link>
              <!-- <router-link to="/article" tag="li" active-class="current">
                <a>文章</a>
              </router-link> -->
              <router-link to="/qa" tag="li" active-class="current">
                <a>话题</a>
              </router-link>
            </ul>
            <!-- / nav -->
            <!-- / nav -->
            <ul class="h-r-login">
              <li v-if="!loginInfo.id" id="no-login">
                <a href="/login" title="登录">
                  <em class="icon18 login-icon">&nbsp;</em>
                  <span class="vam ml5">登录</span>
                </a>
                <a href="/register" title="注册">
                  <span class="vam ml5">注册</span>
                </a>
              </li>
              <li v-if="loginInfo.id" id="is-login-one" class="mr10">
                <a id="headerMsgCountId" href="#" title="消息">
                  <em class="icon18 news-icon">&nbsp;</em>
                </a>
                <q class="red-point" style="display: none">&nbsp;</q>
              </li>
              <li v-if="loginInfo.id" id="is-login-two" class="h-r-user">
                <el-dropdown @command="handleCommand" placement="bottom">
                  <el-avatar
                    :size="40"
                    :src="loginInfo.avatar"
                    alt="用户头像"
                  ></el-avatar>
                  <el-dropdown-menu slot="dropdown" style="width: 250px">
                    <el-dropdown-item command="1" align="center">{{
                        loginInfo.nickname
                      }}
                    </el-dropdown-item>
                    <el-dropdown-item
                      command="2"
                      divided
                      icon="el-icon-user"
                      style="left: 15px; width: 180px"
                    >
                      个人中心
                    </el-dropdown-item>
                    <el-dropdown-item
                      command="3"
                      divided
                      icon="el-icon-turn-off"
                      style="left: 15px; width: 180px"
                    >
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </li>
              <!-- /未登录显示第1 li；登录后显示第2，3 li -->
            </ul>
            <aside class="h-r-search">
              <form action="#" method="post">
                <label class="h-r-s-box">
                  <input
                    type="text"
                    placeholder="搜索课程"
                    name="queryCourse.courseName"
                    v-model="courseName"
                    @keyup.enter="search(courseName)"
                  />
                  <a :href="'/course?key=' +courseName">
                    <button :disabled="courseName !== ''" type="submit" class="s-btn">
                      <em class="icon18">&nbsp;</em>
                    </button>
                  </a>
                </label>
              </form>
            </aside>
          </div>
          <aside class="mw-nav-btn">
            <div class="mw-nav-icon"></div>
          </aside>
          <div class="clear"></div>
        </section>
      </header>
      <!-- /公共头引入 -->

      <nuxt/>
      <!-- 公共底引入 -->
      <footer id="footer">
        <section class="container">
          <div class>
            <h4 class="hLh30">
              <span class="fsize18 f-fM c-999">友情链接</span>
            </h4>
            <ul class="of flink-list">
              <li>
                <a href="#" title="snbo" target="_blank"
                >snbo</a
                >
              </li>
            </ul>
            <div class="clear"></div>
          </div>
          <div class="b-foot">
            <section class="fl col-7">
              <section class="mr20">
                <section class="b-f-link">
                  <a href="#" title="关于我们" target="_blank">关于我们</a>|
                  <a href="#" title="联系我们" target="_blank">联系我们</a>|
                  <a href="#" title="帮助中心" target="_blank">帮助中心</a>|
                  <a href="#" title="资源下载" target="_blank">资源下载</a>|
                  <span>服务热线：17691362069</span>
                  <span>Email：2211915633@qq.com</span>
                </section>
                <section class="b-f-link mt10">
                  <span>©2022课程版权均归蘑菇学院所有</span>
                </section>
              </section>
            </section>
            <aside class="fl col-3 tac mt15">
              <section class="gf-tx">

                <el-popover
                  :visible-arrow="vis"
                  placement="top"
                  width="280"
                  trigger="hover">
                  <div style="text-align: center;display: inline-block">
                    <img style="width: 125px; height: 125px;" src="~/assets/img/all.png"/>
                    <p>微信服务号</p>
                  </div>
                  <div style="text-align: center; display: inline-block">
                    <img style="width: 120px; height: 120px;" src="~/assets/img/own.png"/>
                    <p>个人账号</p>
                  </div>
                  <div slot="reference">
                    <img src="~/assets/img/wx-icon.png" alt/>
                  </div>
                </el-popover>


              </section>
              <section class="gf-tx">
                <span>
                  <img src="~/assets/img/wb-icon.png" alt/>
                </span>
              </section>
            </aside>
            <div class="clear"></div>
          </div>
        </section>
      </footer>
      <!-- /公共底引入 -->
    </div>
  </client-only>
</template>
<script>
import '~/assets/css/reset.css'
import '~/assets/css/theme.css'
import '~/assets/css/global.css'
import '~/assets/css/web.css'
import '~/assets/css/base.css'
import '~/assets/css/activity_tab.css'
import '~/assets/css/bottom_rec.css'
import '~/assets/css/nice_select.css'
import '~/assets/css/order.css'
import '~/assets/css/swiper-3.3.1.min.css'
import "~/assets/css/pages-weixinpay.css"
import cookie from "js-cookie";
import login from "@/api/login";

export default {
  data() {
    return {
      courseName: '',
      vis: false,
      token: "",
      loginInfo: {
        id: "",
        age: "",
        avatar: "",
        mobile: "",
        nickname: "",
        sex: "",
      },
    };
  },
  created() {
    this.getInfo();
  },
  methods: {
    search(courseName) {
      window.open('/course?key=' + courseName, "_blank")
    },
    //头像下拉菜单
    handleCommand(command) {
      if (command === "1") {
      } else if (command === "2") {
        this.$router.push({path: '/uCenter/uInfo'})
      } else if (command === "3") {
        this.logout();
      }
    },
    //从cookie中获取用户信息
    getInfo() {
      var userStr = cookie.get("mogu_ucenter");
      //把json形式的字符串转换成json对象,之前框架自动将后端字符串转换成了json对象可以直接获取,
      //而cookie里面存的是字符串,所以要进行转换
      if (userStr) {
        this.loginInfo = JSON.parse(userStr);
      } else {
        this.$router.push({path: '/login'})
      }
    },
    //退出登录
    logout() {
      //清空cookie值
      cookie.set("mogu_token", "", {domain: "175.178.189.89",});
      cookie.set("mogu_ucenter", "", {domain: "175.178.189.89",});
      //回到首页面
      window.location.href = "/";
    },
  },
};
</script>
