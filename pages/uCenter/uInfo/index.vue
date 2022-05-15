<template>
  <div style="height: 900px">
    <table
      style="position: absolute;top: 170px; left: 260px; width: 150px;background-color: rgb(250,250,250); border: 1px solid rgb(225,226,229)">
      <tr style="text-align: center; border-bottom: 1px solid rgb(225,226,229)">
        <p style="font-size: 18px; margin: 10px; color: gray">个人信息</p>
      </tr>
      <tr class="selected">
        <span class="el-icon-user-solid" style="color: rgb(183,228,244)"></span>
        <p style="display: inline-block; font-size: 16px; margin: 10px">我的信息</p>
      </tr>
      <tr :class="visibleSelect === 2?'select1':'select'" @mouseover="visibleSelect = 2"
          @mouseleave="visibleSelect = -1" @click="push(1)">
        <span class="el-icon-s-goods" style="color: rgb(187,187,188)"></span>
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
        <p style="font-size: 15px; padding: 2.5px; margin: 10px 10px 10px 18px;color: #00a1d6">我的信息</p>
      </tr>
      <tr>
        <div style="margin-top: 20px; margin-left: 27px;">
          <el-form label-position="right" label-width="80px" ref="userInfo" :rules="rules" :model="userInfo"
                   :hide-required-asterisk="true">
            <el-form-item label="头像:" prop="nickname">
             <img style="border-radius: 50%; width: 100px;"  :src="userInfo.avatar"/>
            </el-form-item>
            <el-form-item label="昵称:" prop="nickname">
              <el-input style="width: 300px" class="nicknameInput" maxlength="16" show-word-limit
                        v-model="userInfo.nickname"></el-input>
            </el-form-item>
            <el-form-item label="账号:">
              <span style="color: rgb(137,137,137)">{{ userInfo.id }}</span>
            </el-form-item>
            <el-form-item label="我的签名:">
              <el-input
                style="width: 500px;font-size: 16px"
                type="textarea"
                placeholder="设置您的签名( σ'ω')σ"
                v-model="userInfo.sign"
                :autosize="{ minRows: 3, maxRows: 3 }"
                resize="none"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
            <el-form-item label="性别:">
              <el-radio-group v-model="userInfo.sex" size="small" fill="#00a1d7">
                <el-radio-button label="2">男</el-radio-button>
                <el-radio-button label="1">女</el-radio-button>
                <el-radio-button label="0">保密</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期:">
              <el-date-picker
                v-model="userInfo.birth"
                type="date"
                placeholder="选择日期"
                format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-form>
        </div>
      </tr>
      <tr style="height: 201px;text-align: center; border-top: 0.05rem solid rgb(225,226,229)">
        <el-button style="margin-top: 40px;width: 120px; background-color: rgb(0,161,215); color: rgb(255,255,255)"
                   @click="submit('userInfo')">保存
        </el-button>
      </tr>
    </table>
  </div>
</template>

<script>

import cookie from "js-cookie";
import userInfo from "@/api/userInfo";

export default {
  layout: 'info',
  data() {
    return {
      urlList: ['/uCenter/uInfo', '/uCenter/cInfo', '/uCenter/psd'],
      visibleSelect: -1,
      userInfo: {
        id: '',
        nickname: '',
        age: '',
        avatar: '',
        sign: '',
        birth: ''
      },
      rules: {
        nickname: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
          {
            required: true,
            pattern: /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
            message: '名称只能包含汉字,数字,字母,_,不能以_开头或结尾',
            trigger: 'blur',
          },
          {min: 1, max: 16, message: '长度不能超过16位', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    submit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.saveInfo()
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    saveInfo() {
      userInfo.updateInfo(this.userInfo).then(response => {
        if (response.data.success) {
          this.$alert('<p>已经成功更新你的资料</p>', '提示', {
            dangerouslyUseHTMLString: true
          });
          userInfo.getLoginUserInfo().then(result => {
            this.userInfo = result.data.data.loginInfo;
            //更新cookie里面的信息
            //这里要将json对象转换成json字符串
            cookie.set("mogu_ucenter", JSON.stringify(this.userInfo), {
              domain: "175.178.189.89",
            });
          })
        } else {
          this.$message.error("修改信息失败")
        }
      })
    },
    getUserInfo() {
      let str = cookie.get("mogu_ucenter");
      if (str) {
        this.userInfo = JSON.parse(str);
      } else {
        window.location.href = '/login'
      }
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

.nicknameInput /deep/ .el-input__inner {
  height: 30px;
  width: 300px;
  font-size: 15px;
}

/deep/ .el-form-item__label {
  font-size: 15px;
}
</style>
