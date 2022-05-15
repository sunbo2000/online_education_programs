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
      <tr class="selected">
        <span class="el-icon-s-goods" style="color: rgb(183,228,244)"></span>
        <p style="display: inline-block;font-size: 16px; margin: 10px;">课程信息</p>
      </tr>
      <tr :class="visibleSelect === 3?'select1':'select'" @mouseover="visibleSelect = 3"
          @mouseleave="visibleSelect = -1" @click="push(2)">
        <span class="el-icon-s-cooperation" style="color: rgb(187,187,188)"></span>
        <p style="display: inline-block;font-size: 16px; margin: 10px;">账号设置</p>
      </tr>
      <tr style="height: 600px"></tr>
    </table>
    <table
      style="position: absolute;top: 170px; left: 409.5px; width:830px;background-color: rgb(255,255,255); border: 1px solid rgb(225,226,229); border-left: 0">
      <tr>
      <tr style=" border-bottom: 1px solid rgb(225,226,229)">
      <p style="font-size: 15px; padding: 2.5px; margin: 10px 10px 10px 18px;color: #00a1d6">课程信息</p>
      </tr>
      <tr style="height: 745px;text-align: center; border-top: 0.05rem solid rgb(225,226,229)">
      </tr>
    </table>
  </div>
</template>

<script>
import userInfo from "@/api/userInfo";
import cookie from "js-cookie";

export default {
  layout: 'info',
  data() {
    return {
      urlList: ['/uCenter/uInfo', '/uCenter/cInfo', '/uCenter/psd'],
      visibleSelect: -1,
    }
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    push(index) {
      this.$router.push({path: this.urlList[index]});
    },
    getUserInfo() {
      let str = cookie.get("mogu_ucenter");
      if (str) {
        this.userInfo = JSON.parse(str);
      } else {
        window.location.href = '/login'
      }
    },
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
</style>
