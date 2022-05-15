<template>
  <div class="cart py-container">
    <!--主内容-->
    <div class="checkout py-container pay">
      <div class="checkout-tit">
        <h4 class="fl tit-txt"><span class="success-icon"></span><span
          class="success-info">订单提交成功，请您及时付款！订单号：{{ payObj.out_trade_no }}</span>
        </h4>
        <span class="fr"><em class="sui-lead">应付金额：</em><em class="orange
money">￥{{ payObj.total_fee }}</em></span>
        <div class="clearfix"></div>
      </div>
      <div class="checkout-steps">
        <div class="fl weixin">微信支付</div>
        <div class="fl sao">
          <p class="red">请使用微信扫一扫。</p>
          <div class="fl code">
            <!-- <img id="qrious" src="~/assets/img/erweima.png" alt=""> -->
            <!-- <qriously value="weixin://wxpay/bizpayurl?pr=R7tnDpZ"
:size="338"/> -->
            <qriously :value="payObj.code_url" :size="338"/>
            <div class="saosao">
              <p>请使用微信扫一扫</p>
              <p style="color: #00bb28">扫描二维码支付</p>
            </div>
          </div>
        </div>
        <div class="clearfix"></div>
        <!-- <p><a href="pay.html" target="_blank">> 其他支付方式</a></p> -->
        <p>支付剩余时间: {{ count }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import orderApi from '@/api/order'

export default {
  //根据订单id生成微信支付二维码
  asyncData({params, error}) {
    return orderApi.createNative(params.orderNo).then(response => {
      return {
        payObj: response.data.data,
        seconds: response.data.data.time
      }
    })
  },
  data() {
    return {
      timer: null, // 定时器名称
      initQCode: '',
      timer1: '',
      timer2: '',
      seconds: 1800,
      count: ''
    }
  },
  mounted() {
    //倒数计时
    this.Time()
    //在页面渲染之后执行
    //每隔三秒，去查询一次支付状态
    this.timer1 = setInterval(() => {
      this.queryPayStatus(this.payObj.out_trade_no)
    }, 3000);
  },
  methods: {
    //查询支付状态的方法
    queryPayStatus(out_trade_no) {
      orderApi.queryPayStatus(out_trade_no).then(response => {
        if (response.data.code === 20000) {
          //如果支付成功，清除定时器
          clearInterval(this.timer1)
          clearInterval(this.timer2)
          this.$message.success(response.data.message)
          //跳转到课程详情页面观看视频
          this.$router.push({path: '/course/' + this.payObj.course_id})
        } else if (response.data.code === 20001) {
          this.$message.error(response.data.message)
        }
      })
    },
    // 天 时 分 秒 格式化函数
    countDown() {
      let m = parseInt(this.seconds / 60 % 60);
      m = m < 10 ? "0" + m : m
      let s = parseInt(this.seconds % 60);
      s = s < 10 ? "0" + s : s
      this.count = m + '分' + s + '秒'
    },
    //定时器没过1秒参数减1
    Time() {
      this.timer2 = setInterval(() => {
        this.seconds -= 1
        this.countDown()
        if (this.seconds == 0) {
          //计时完,清除计数器
          clearInterval(this.timer1)
          clearInterval(this.timer2)
          this.$message.error("支付超时")
          this.$router.push({path: '/course/' + this.payObj.course_id})
        }
      }, 1000)
    },
  }
}
</script>
