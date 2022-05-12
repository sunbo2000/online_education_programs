<template>
  <div class="app-container">
    <!--表单-->
    <el-form :inline="true" ref="searchObj" :model="searchObj" class="demo-form-inline">

      <el-form-item prop="typeList" :rules="[{ type: 'array', required: true, message: '请至少选择一个图表元素', trigger: 'change' }]">
        <el-select v-model="searchObj.typeList" multiple placeholder="请选择图表要素" style="width: 300px">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select v-model="chartType" placeholder="请选择图表样式">
          <el-option v-for="item in optionChart" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item>
        <el-select v-model="searchObj.type" clearable placeholder="请选择">
          <el-option label="学员登录数统计" value="login_num" />
          <el-option label="学员注册数统计" value="register_num" />
          <el-option label="课程播放数统计" value="video_view_num" />
          <el-option label="每日课程数统计" value="course_num" />
        </el-select>
      </el-form-item> -->
      <el-form-item prop="start" :rules="[{ required: true, message: '请选择开始日期', trigger: 'change' } ]">
        <el-date-picker v-model="searchObj.start" type="date" placeholder="选择开始日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item prop="end" :rules="[{ required: true, message: '请选择截止日期', trigger: 'change' } ]">
        <el-date-picker v-model="searchObj.end" type="date" placeholder="选择截止日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-button :disabled="btnDisabled" type="primary" icon="el-icon-search" @click="submitForm('searchObj')">查询</el-button>
    </el-form>
    <div class="chart-container">
      <div id="chart" class="chart" style="height:500px;width:98%" />
    </div>
  </div>
</template>

<script>
import echarts from 'echarts';
import sta from "@/api/statistics";

export default {
  data() {
    return {
      searchObj: {
        typeList: [],
        start: '',
        end: ''
      },
      chartType: 'line',
      //多选参数
      options: [{
        value: 'login_num',
        label: '学员登录数统计'
      }, {
        value: 'register_num',
        label: '学员注册数统计'
      }, {
        value: 'video_view_num',
        label: '课程播放数统计'
      }, {
        value: 'course_num',
        label: '每日课程数统计'
      }],
      optionChart: [{
        value: 'line',
        label: '折线图'
      }, {
        value: 'bar',
        label: '柱状图'
      }],
      btnDisabled: false,
      chart: null,
      title: '',
      xData: [],
      loginData: [],
      registerData: [],
      videoData: [],
      courseData: []
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.showChart()
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    showChart() {
      this.loginData = []
      this.registerData = []
      this.videoData = []
      this.courseData = []
      //异步方法,不会等到出结果,所有要在response里面调用set
      sta.showData(this.searchObj).then((response) => {
        this.xData = response.data.dateList
        if (response.data.loginNumList) {
          this.loginData = response.data.loginNumList
        }
        if (response.data.registerNumList) {
          this.registerData = response.data.registerNumList
        }
        if (response.data.videoNumList) {
          this.videoData = response.data.videoNumList
        }
        if (response.data.courseNumList) {
          this.courseData = response.data.courseNumList
        }
        //调用生成图表的方法
        this.setLineChart()
      })
    },
    // 设置图标参数
    setLineChart() {
      // 基于准备好的dom，初始化echarts实例
      this.chart = echarts.init(document.getElementById('chart'))
      // console.log(this.chart)
      // 指定图表的配置项和数据
      var option = {
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar', 'stack'] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },

        // x轴是类目轴（离散数据）,必须通过data设置类目数据
        xAxis: {
          type: 'category',
          data: this.xData
        },// y轴是数据轴（连续数据）
        yAxis: [
          {
            type: 'value'
          }
        ],
        title: {
          text: '统计数据'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['登录次数', '注册次数', '播放次数', '新增课程']
        },
        // 系列列表。每个系列通过 type 决定自己的图表类型
        series: [
          {
            name: '登录次数',
            type: this.chartType,
            data: this.loginData
          },
          {
            name: '注册次数',
            type: this.chartType,
            data: this.registerData
          },
          {
            name: '播放次数',
            type: this.chartType,
            data: this.videoData
          },
          {
            name: '新增课程',
            type: this.chartType,
            data: this.courseData
          }
        ],
        dataZoom: [{
          show: true,
          height: 30,
          xAxisIndex: [
            0
          ],
          bottom: 30,
          start: 10,
          end: 80,
          handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
          handleSize: '110%',
          handleStyle: {
            color: '#d3dee5'

          },
          textStyle: {
            color: '#fff'
          },
          borderColor: '#90979c'
        },
        {
          type: 'inside',
          show: true,
          height: 15,
          start: 1,
          end: 35
        }],
      }
      this.chart.setOption(option)
    },
  }
}
</script>