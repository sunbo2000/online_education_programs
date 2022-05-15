<template>
  <div style="height: 900px">
    <table
      style="position: absolute;top: 170px; left: 260px; width: 150px;background-color: rgb(250,250,250); border: 1px solid rgb(225,226,229)">
      <tr style="text-align: center; border-bottom: 1px solid rgb(225,226,229)">
        <p style="font-size: 18px; margin: 10px; color: gray">个人信息</p>
      </tr>
      <tr :class="visibleSelect === 1?'select1':'select'" @mouseover="visibleSelect = 1"
          @mouseleave="visibleSelect = -1" @click="push(0)">
        <span class="el-icon-user-solid" style="color: rgb(187,187,188)"></span>
        <p style="display: inline-block; font-size: 16px; margin: 10px">我的信息</p>
      </tr>
      <tr :class="visibleSelect === 2?'select1':'select'" @mouseover="visibleSelect = 2"
          @mouseleave="visibleSelect = -1" @click="push(1)">
        <span class="el-icon-s-goods" style="color: rgb(187,187,188)"></span>
        <p style="display: inline-block;font-size: 16px; margin: 10px;">课程信息</p>
      </tr>
      <tr class="selected">
        <span class="el-icon-s-cooperation" style="color: rgb(183,228,244)"></span>
        <p style="display: inline-block;font-size: 16px; margin: 10px;">账号设置</p>
      </tr>
      <tr style="height: 600px"></tr>
    </table>
    <table
      style="position: absolute;top: 170px; left: 409.5px; width:830px;background-color: rgb(255,255,255); border: 1px solid rgb(225,226,229); border-left: 0">
      <tr>
      <tr style=" border-bottom: 1px solid rgb(225,226,229)">
      <p style="font-size: 15px; padding: 2.5px; margin: 10px 10px 10px 18px;color: #00a1d6">账号设置</p>
      </tr>
      <tr style="border-bottom: 0.05rem solid rgb(225,226,229)">
        <div v-if="accountInfo.psd" class="psd"><p class="p1">设置密码</p>
          <sapn>已设置</sapn>
          <a href="#" class="a1">修改密码</a></div>
        <div v-else class="psd"><p class="p1">设置密码</p>
          <sapn>未设置</sapn>
          <a href="#" class="a1">设置密码</a></div>
      </tr>
      <tr style=" border-bottom: 0.05rem solid rgb(225,226,229)">
        <div v-if="accountInfo.mobile" class="psd"><p class="p1">手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机</p>
          <span>{{ accountInfo.mobile }}</span><a
            href="#" class="a1">更换手机</a></div>
        <div v-else class="psd"><p class="p1">手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机</p>
          <span>未绑定</span><a
            href="#" class="a1">绑定手机</a></div>
      </tr>
      <tr style=" border-bottom: 0.05rem solid rgb(225,226,229)">
        <div v-if="accountInfo.openid" class="psd"><p class="p1">微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信</p>
          <span>已授权绑定微信账号</span><a
            href="#" class="a1">更换微信账号</a></div>
        <div v-else class="psd"><p class="p1">微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信</p>
          <span>未绑定</span>
          <a href="#" class="a1">绑定微信账号</a></div>
      </tr>
      <tr style=" border-bottom: 0.05rem solid rgb(225,226,229)">
        <div class="psd"><p class="p1">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱</p>
          <span>未绑定</span><a
            href="#" class="a1">绑定邮箱</a></div>
      </tr>
      <tr style=" border-bottom: 0.05rem solid rgb(225,226,229)">
        <div class="psd"><p class="p1">账号注销</p><a
          href="#" class="a1">立即注销</a></div>
      </tr>
      <tr style="height: 418px;text-align: center; border: 0.05rem solid rgb(225,226,229)">
      </tr>
    </table>
  </div>
</template>

<script>

import userInfo from "@/api/userInfo";

export default {
  layout: 'info',
  data() {
    return {
      urlList: ['/uCenter/uInfo', '/uCenter/cInfo', '/uCenter/psd'],
      visibleSelect: -1,
      accountInfo: '',
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      userInfo.getAccountInfo().then(response => {
        console.log(response)
        if (response.data.code === 20000) {
          this.accountInfo = response.data.data.accountInfo;
        } else {
          window.location.href = '/login'
        }
      })
    },
    push(index) {
      this.$router.push({path: this.urlList[index]});
    }
  }
}

</script>

<style scoped>
.selected {
  text-align: center;
  cursor: pointer;
  color: rgb(255, 255, 255);
  background-color: rgb(0, 161, 215);
}

.select {
  text-align: center;
  cursor: pointer;
}

.select1 {
  text-align: center;
  background-color: rgb(225, 228, 234);
  cursor: pointer;
}

.psd {
  margin: 20px;
}

.a1 {
  text-decoration: none;
  color: #1989fa;
  position: absolute;
  right: 15px;
}

.tr1 {
  text-align: center;
  border-bottom: 1px solid rgb(225, 226, 229);
}

.p1 {
  display: inline-block;
  margin-right: 300px;
}
</style>
