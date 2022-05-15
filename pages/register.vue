<template>
  <div class="main" style="height: 770px; width: 500px">
    <div class="title">
      <nuxt-link style="text-decoration: none" to="/login">登录账号</nuxt-link>
      <span>·</span>
      <nuxt-link style="text-decoration: none" class="active" to="/register">注册账号</nuxt-link>
    </div>

    <el-form
      align="left"
      style="margin-top:-25px;"
      ref="userForm"
      :model="params"
      hide-required-asterisk="true"
      label-position="top"
    >
      <el-form-item
        class="item"
        style="margin-bottom: 5px"
        label="用户名"
        prop="nickname"
        :rules="[
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
            { min: 1, max: 16, message: '长度不能超过16位', trigger: 'blur' }
          ]"
      >
        <el-input
          type="text"
          placeholder="汉字,字母,数字,_组成,不能以_开头或结尾,最多16位"
          v-model="params.nickname"
        />
      </el-form-item>
      <el-form-item
        class="item"
        style="margin-bottom: 5px"
        label="设置密码"
        prop="password"
        :rules="[{ required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码至少6位', trigger: 'blur' },
          { max: 16, message: '密码最多16位', trigger: 'blur' }]"
      >
        <el-input
          type="password"
          placeholder="设置6~16位密码"
          v-model="params.password"
        />
      </el-form-item>
      <el-form-item
        class="item"
        style="margin-bottom: 5px"
        label="确认密码"
        prop="checkPass"
        :rules="[
            { required: true, message: '请再次输入密码', trigger: 'blur' },
            { validator: checkPassword, trigger: 'blur' },
          ]"
      >
        <el-input
          type="password"
          placeholder="确认密码"
          v-model="params.checkPass"
        />
      </el-form-item>
      <el-form-item
        class="item"
        style="margin-bottom: 5px"
        label="手机号码"
        prop="mobile"
        :rules="[
            { required: true, message: '请输入手机号码', trigger: 'blur' },
            { validator: checkPhone, trigger: 'blur' },
          ]"
      >
        <el-input
          type="text"
          placeholder="填写你常用的手机号码作为登录账号"
          v-model="params.mobile"
        />
      </el-form-item>

      <el-form-item
        class="item"
        style="margin-bottom: 5px"
        label="短信验证码"
        prop="code"
        :rules="[
            { required: true, message: '请输入验证码', trigger: 'blur' },
          ]"
      >
        <el-input
          type="text"
          placeholder="填写短信验证码"
          v-model="params.code"
          style="width: 200px"
        />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <el-button
          type="info"
          :plain="true"
          :value="codeTest"
          @click="getCodeFun()"
        >
          {{ codeTest }}
        </el-button
        >
      </el-form-item>

      <div class="btn">
        <input
          type="button"
          class="sign-up-button"
          value="注册"
          @click="submitForm('userForm')"
        />
      </div>
      <p class="sign-up-msg" style="position: relative; top: 10px">
        点击 “注册” 即表示您同意并愿意遵守简书
        <br/>
        <a target="_blank" href="http://www.jianshu.com/p/c44d171298ce"
        >用户协议</a
        >
        和
        <a target="_blank" href="http://www.jianshu.com/p/2ov8x3">隐私政策</a>
        。
      </p>
    </el-form>
    <!-- 更多注册方式 -->
    <div class="more-sign">
      <h6>社交帐号直接注册</h6>
      <ul>
        <li>y
          <a
            id="weixin"
            class="weixin"
            href="http://175.178.189.89:9001/api/ucenter/wx/login"
          ><i class="iconfont icon-weixin"
          /></a>
        </li>
        <li>
          <a id="qq" class="qq" href="#" @click="qqRegister()"
          ><i class="iconfont icon-qq"
          /></a>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";
import register from "@/api/register";

export default {
  layout: "sign",
  data() {
    return {
      params: {
        mobile: "",
        code: "",
        nickname: "",
        password: "",
        checkPass: "",
      },
      sending: true, //是否发送验证码
      second: 120, //倒计时间
      codeTest: "获取短信验证码",
    };
  },
  methods: {
    //
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.submitRegister()
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    qqRegister() {
      this.$message.warning("开发中,敬请期待......");
    },
    getCodeFun() {
      //sending = false
      //this.sending原为true,请求成功，!this.sending == true，主要是防止有人把disabled属性去掉，多次点击；
      if (!this.sending) return;
      //debugger
      // prop 换成你想监听的prop字段
      this.$refs.userForm.validateField("mobile", (errMsg) => {
        if (errMsg == "") {
          register.sendMessage(this.params.mobile).then((res) => {
            this.sending = false;
            this.timeDown();
          });
        }
      });
    },
    //提交注册表单
    submitRegister() {
      register.register(this.params).then((response) => {
        if (response.data.success) {
          this.$message.success("注册成功");
          this.$router.push({path: "/login"});
        } else {
          this.$message.error(response.data.message);
        }
      });
    },
    //倒计时
    timeDown() {
      this.codeTest = this.second + " 秒后重新获取";
      let result = setInterval(() => {
        this.second--;
        this.codeTest = this.second + " 秒后重新获取";
        if (this.second < 1) {
          clearInterval(result);
          this.sending = true;
          //this.disabled = false;
          this.second = 120;
          this.codeTest = "获取验证码";
        }
      }, 1000);
    },
    checkPhone(rule, value, callback) {
      //debugger
      if (!/^1[34578]\d{9}$/.test(value)) {
        return callback(new Error("手机号码格式不正确"));
      }
      return callback();
    },
    //二次密码输入
    checkPassword(rule, value, callback) {
      if (value !== this.params.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
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
  font-size: 15px;
  font-weight: bolder;
}
</style>
