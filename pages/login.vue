<template>
  <div class="main" style="height: 650px; width: 500px;">
    <div class="title">
      <nuxt-link style="text-decoration: none" class="active" to="/login">登录账号</nuxt-link>
      <span>·</span>
      <nuxt-link style="text-decoration: none" to="/register">注册账号</nuxt-link>
    </div>
    <div class="sign-up-container">
      <el-form ref="userForm" :model="user" hide-required-asterisk="true" align="left" label-position="top">
        <el-form-item class="item" label="账号" prop="mobile" :rules="[
              {
                required: true,
                message: '请输入手机号码',
                trigger: 'blur',
              },
              { validator: checkPhone, trigger: 'blur' },
            ]">
          <el-input type="text" placeholder="手机号" v-model="user.mobile"/>
        </el-form-item>
        <el-form-item class="item" label="密码" prop="password" :rules="[
              { required: true, message: '请输入密码', trigger: 'blur' },
              { min: 6, message: '密码至少 6 位', trigger: 'blur' },
              { max: 16, message: '密码最多 16 位', trigger: 'blur' },
            ]">
          <el-input type="password" placeholder="密码" v-model="user.password"/>
        </el-form-item>
        <div class="btn">
          <input type="button" class="sign-in-button" value="登录" @click="submitForm('userForm')"/>
        </div>
      </el-form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li>
            <a id="weixin" class="weixin" href="http://175.178.189.89:9001/api/ucenter/wx/login"><i
              class="iconfont icon-weixin"/></a>
          </li>
          <li>
            <a id="qq" class="qq" @click="qqLogin()"><i class="iconfont icon-qq"/></a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";
import login from "@/api/login";
import cookie from "js-cookie";
import wxLogin from "~/api/wxLogin";

export default {
  layout: "sign",
  data() {
    return {
      user: {
        mobile: "",
        password: "",
      },
      loginInfo: {},
    };
  },
  methods: {
    //
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.submitLogin();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    wxLogin() {
      wxLogin.toWxLogin().then(response => {
      })
    },
    qqLogin() {
      this.$message.warning("开发中,敬请期待...");
    },
    //1.调用登录方法,进行登录,得到返回的token字符串
    submitLogin() {
      login.loginCheck(this.user).then((response) => {
        if (response.data.success) {
          //2.将获取的token字符串放进cookie里面
          //cookie名称, cookie值,作用范围
          cookie.set("mogu_token", response.data.data.token, {
            domain: "175.178.189.89",
          });
          //3.请求发送之前被拦截,在请求头里加入token信息
          //4.调用token获取用户信息,为了首页显示
          login.getLoginUserInfo().then((response) => {
            this.loginInfo = response.data.data.loginInfo;
            //这里要将json对象转换成json字符串
            cookie.set("mogu_ucenter", JSON.stringify(this.loginInfo), {
              domain: "175.178.189.89",
            });
            window.location.href = "/";
          });
        } else {
          this.$message.error(response.data.message);
        }
      });
    },
    //检查号码格式
    checkPhone(rule, value, callback) {
      //debugger
      if (!/^1[34578]\d{9}$/.test(value)) {
        return callback(new Error("手机号码格式不正确"));
      }
      return callback();
    },
  },
};
</script>
<!--
	.item: 是绑定class的
	.el-form-item__label: 自动匹配form表单中label的(注意:中间是连续的两个'_')
-->
<style>
.item .el-form-item__label {
  margin-bottom: -10px;
  font-size: 18px;
  font-weight: bolder;
}

.el-form-item__error {
  z-index: 9999999;
}
</style>
