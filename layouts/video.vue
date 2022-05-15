<template>
  <div class="guli-player">
    <div class="head">
      <a href="#" title="蘑菇学院" style="position: absolute; left: 10px; top: 17px">
        <img class="logo" src="~/assets/img/frontlogo.png" style="width: 130px; height: 40px" lt="蘑菇学院"/>
      </a>
      <el-breadcrumb separator="/" style="position: absolute; top: 30px; left: 160px">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/course' }">课程列表</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/course/' + courseId }">课程详情</el-breadcrumb-item>
        <el-breadcrumb-item>播放列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <ul class="h-r-login" style="position: absolute;  left: 1250px">
      <li v-if="!userInfo.id" id="no-login">
        <a href="/login" title="登录">
          <em class="icon18 login-icon">&nbsp;</em>
          <span class="vam ml5">登录</span>
        </a>
        <a href="/register" title="注册">
          <span class="vam ml5">注册</span>
        </a>
      </li>
    </ul>
    <div v-if="userInfo.id" style="position: absolute; top: 20px; left: 1300px">
      <el-dropdown @command="handleCommand" placement="bottom">
        <el-avatar :size="40" :src="userInfo.avatar" alt="用户头像"></el-avatar>
        <el-dropdown-menu slot="dropdown" style="width: 250px">
          <el-dropdown-item command="1" align="center">{{
              userInfo.nickname
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
    </div>

    <nuxt/>
  </div>
</template>
<script>
import cookie from "js-cookie";

export default {
  data() {
    return {
      courseId: "",
      userInfo: {
        id: ''
      },
    };
  },
  created() {
    this.courseId = this.$route.params.id;
    this.getUserInfo();
  },
  methods: {
    //头像下拉菜单
    handleCommand(command) {
      if (command === "1") {
      } else if (command === "2") {
        this.$router.push({path: '/uCenter/uInfo'})
      } else if (command === "3") {
        this.logout();
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
    getUserInfo() {
      var str = cookie.get("mogu_ucenter");
      if (str) {
        this.userInfo = JSON.parse(str);
      }
    },
  },
};
</script>
<style>
html,
body {
  height: 100%;
}

</style>
<style scoped>

::-webkit-scrollbar {
  display: none;
}

.head {
  height: 50px;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

.head .logo {
  height: 50px;
  margin-left: 10px;
}

.body {
  position: absolute;
  top: 50px;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}
</style>
