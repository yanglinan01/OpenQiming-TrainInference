<template>
  <div>
    <p class="logcenter-title">模型监控</p>
    <div class="logcenter-block">
      <div class="block-title">日志概览</div>
      <Row class="log-overview">

        <Col span="6">
          <div class="row-item">
            <img class="row-item-img" src="../../assets/images/icon_zong.png" >
            <div>
              <div class="row-item-content">
                <Numeral :value="mStatistics.tokenTotal ? mStatistics.tokenTotal : 0" format="000,000"></Numeral>
              </div>
              <div class="row-item-title">调用总tokens</div>
            </div>
          </div>
        </Col>

        <Col span="6">
          <div class="row-item">
            <img class="row-item-img" src="../../assets/images/icon_shuru.png" >
            <div>
              <div class="row-item-content">
                <Numeral :value="mStatistics.tokenInput ? mStatistics.tokenInput : 0" format="000,000"></Numeral>
              </div>
              <div class="row-item-title">输入tokens</div>
            </div>
          </div>
        </Col>

        <Col span="6">
          <div class="row-item">
            <img class="row-item-img" src="../../assets/images/icon_shuchu.png" >
            <div>
              <div class="row-item-content">
                <Numeral :value="mStatistics.tokenOutput ? mStatistics.tokenOutput : 0" format="000,000"></Numeral>
              </div>
              <div class="row-item-title">输出tokens</div>
            </div>
          </div>
        </Col>

        <Col span="6">
          <div class="row-item">
            <img class="row-item-img" src="../../assets/images/icon_jiekou.png" >
            <div>
              <div class="row-item-content">
                <Numeral :value="mStatistics.intfTotal ? mStatistics.intfTotal : 0" format="000,000"></Numeral>
              </div>
              <div class="row-item-title">调用接口总数</div>
            </div>
          </div>
        </Col>

      </Row>
    </div>

    <div class="logcenter-block">
      <div class="block-title">模型调用统计</div>
      <div class="row-box">
        <div><strong style="color:red">*</strong>模型选择：</div>
        <el-select v-model="mTaskId1"  filterable style="width:200px" @change="getModelRequest" >
          <el-option v-for="item in mModelList" :value="item.id" :label="item.name" :key="item.id"></el-option>
        </el-select>

        <el-radio-group class="row-flex" v-model="mModelCallType" style="margin-left: 20px;" @change="getModelRequest">
            <el-radio-button  :label="0">全部</el-radio-button>
            <el-radio-button  :label="1">模型输入</el-radio-button>
            <el-radio-button  :label="2">模型输出</el-radio-button>
        </el-radio-group>

        <el-date-picker
          v-model="mModelCallDate"
          style="margin-left: 20px;"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00', '23:59:59']"
          @change="getModelRequest">
        </el-date-picker>
      </div>

      <div id="chart1" style="height:300px; width:100%" />
    </div>


    <div class="logcenter-block">
      <div class="block-title">接口调用统计</div>
      <div class="row-box">
        <div><strong style="color:red">*</strong>模型选择：</div>
        <el-select v-model="mTaskId2" filterable style="width:200px" @change="getModelRequestChart">
          <el-option v-for="item in mModelList" :value="item.id" :label="item.name" :key="item.id"></el-option>
        </el-select>

        <el-radio-group class="row-flex" v-model="mIntfCallType" style="margin-left: 20px;" @change="getModelRequestChart">
          <el-radio-button :label="2">全部</el-radio-button>
          <el-radio-button :label="0">成功</el-radio-button>
          <el-radio-button :label="1">失败</el-radio-button>
        </el-radio-group>

        <el-date-picker
          v-model="mIntfCallDate"
          style="margin-left: 20px;"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00', '23:59:59']"
          @change="getModelRequestChart">
        </el-date-picker>
      </div>

      <div id="chart2" style="height:300px; width:100%" />

      <el-button style="margin-left: 48%; margin-top: 20px;" type="primary" size="mini" @click="checkDataList">查看数据详情</el-button>

    </div>

  </div>
</template>
<script>
import echarts from 'echarts'

import { getLogModelList, queryStatistics, queryModelRequest, queryModelRequestChart } from "@api/logCenter"

export default {
  name: 'modelMonitor',
  data() {
    return {
      mModelList: [],

      mStatistics:{
        id: "",
        intfTotal: "",//调用接口总数
        tokenInput: "",//输入token数
        tokenOutput: "",//输出token数
        tokenTotal: "",//调用总token数
      },

      mChart1: null,
      mChart2: null,

      mTaskId1: null,
      mModelCallType: 0,//模型输入输出类型：默认0 0：全部 1：模型输入 2：模型输出
      mModelCallDate: [],
      mChartData1:{xData:[], yData:[]},
      
      mTaskId2: null,
      mIntfCallType: 2,//接口调用类型：默认2 接口调用状态;0:成功,1:失败,2:全部
      mIntfCallDate: [],//查询天数：默认1 1：今天 2：近2天 3：近3天 7:近7天 15:近15天
      mChartData2:{xData:[], yData:[]},

      mOption1: "",//
      mOption2: "",//表格2配置
    };
  },
  created() {
    let searchParams = JSON.parse(localStorage.getItem("searchParams"));
    if(searchParams){
      this.mTaskId1 = searchParams.taskId1;
      this.mModelCallType = searchParams.modelCallType;
      this.mModelCallDate = searchParams.modelCallDate;
      this.mTaskId2 = searchParams.taskId2;
      this.mIntfCallType = searchParams.intfCallType;
      this.mIntfCallDate = searchParams.intfCallDate;
    }else{
      this.mModelCallDate = this.getDefaultDate();
      this.mIntfCallDate = this.getDefaultDate();
    }
    localStorage.removeItem("searchParams");

    getLogModelList({status:"completed"}).then(res => {
      if(res.success){
        this.mModelList = [];
        for(let i = 0; i < res.data.length; i++){
          let item = res.data[i];
          this.mModelList.push({id: item.id, name: item.name})
        }
        if(this.mModelList.length>0){
          this.mTaskId1 = this.mModelList[0].id;
          this.mTaskId2 = this.mModelList[0].id;
        }else{
          this.mTaskId1 = null;
          this.mTaskId2 = null;
        }
        this.initChart();
        this.getModelRequest();
        this.getModelRequestChart();
      }
    });

    queryStatistics({}).then(res => {
      if(res.success){
        this.mStatistics = res.data;
      }
    })
  },

  beforeDestroy() {
    if(this.mChart1) {
      this.mChart1.dispose()
      this.mChart1 = null;
    }
    
    if(this.mChart2) {
      this.mChart2.dispose()
      this.mChart2 = null;
    }
  },

  methods: {
    getDefaultDate(){
      let now = new Date();
      let year = now.getFullYear();
      let month = now.getMonth() + 1;
      let day = now.getDate();
      let date = year + "-" + month + "-" + day;
      return [date + " 00:00:00", date + " 23:59:59"]
    },
    /**
     * 查看数据详情
     */
    checkDataList(){
      let searchParams = {
        taskId1: this.mTaskId1,
        modelCallType: this.mModelCallType,
        modelCallDate: this.mModelCallDate,
        taskId2: this.mTaskId2,
        intfCallType: this.mIntfCallType,
        intfCallDate: this.mIntfCallDate
      }
      localStorage.setItem("searchParams", JSON.stringify(searchParams));
      this.$router.push({name: 'modelMonitorList'});
    },

    initChart() {
      this.mChart1 = echarts.init(document.getElementById("chart1"));
      this.mChart2 = echarts.init(document.getElementById("chart2"));

      this.mOption1 = {
        backgroundColor: '#fff',
        title: {
          top: 0,
          text: '单位/token',
          textStyle: {
            fontWeight: 'normal',
            fontSize: 16,
          },
          left: '1%'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: '#2db7f5'
            }
          }
        },
        grid: {
          top: 40,
          left: '2%',
          right: '2%',
          bottom: '2%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          boundaryGap: false,
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          data: []
        }],
        yAxis: [{
          type: 'value',
          name: '',
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          axisLabel: {
            margin: 10,
            textStyle: {
              fontSize: 14
            }
          },
          splitLine: {
            lineStyle: {
              color: '#e8eaec'
            }
          }
        }],
        series: [{
          name: '',
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 5,
          showSymbol: false,
          lineStyle: {
            normal: {
              width: 1
            }
          },
          itemStyle: {
            normal: {
              color: 'rgb(0, 87, 255)',
              borderColor: 'rgba(0, 87, 255, 0.2)',
              borderWidth: 12

            }
          },
          data: []
        }]
      };

      this.mOption2 = {
        backgroundColor: '#fff',
        title: {
          top: 0,
          text: '调用次数',
          textStyle: {
            fontWeight: 'normal',
            fontSize: 16,
          },
          left: '1%'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: '#2db7f5'
            }
          }
        },
        grid: {
          top: 40,
          left: '2%',
          right: '2%',
          bottom: '2%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          boundaryGap: false,
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          data: []
        }],
        yAxis: [{
          type: 'value',
          name: '',
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          axisLabel: {
            margin: 10,
            textStyle: {
              fontSize: 14
            }
          },
          splitLine: {
            lineStyle: {
              color: '#e8eaec'
            }
          }
        }],
        series: [{
          name: '',
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 5,
          showSymbol: false,
          lineStyle: {
            normal: {
              width: 1
            }
          },
          // areaStyle: {
          //   normal: {
          //     color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
          //       offset: 0,
          //       color: 'rgba(48, 144, 49, 0.6)'
          //     }, {
          //       offset: 0.8,
          //       color: 'rgba(48, 144, 49, 0)'
          //     }], false),
          //     shadowColor: 'rgba(0, 0, 0, 0.1)',
          //     shadowBlur: 10
          //   }
          // },
          itemStyle: {
            normal: {
              color: 'rgb(48, 144, 49)',
              borderColor: 'rgba(48, 144, 49, 0.2)',
              borderWidth: 12

            }
          },
          data: []
        }]
      };

      this.mOption1.xAxis[0].data = [];
      this.mOption1.series[0].data = [];
      this.mChart1.setOption(this.mOption1);

      this.mOption2.xAxis[0].data = [];
      this.mOption2.series[0].data = [];
      this.mChart2.setOption(this.mOption2);
    },

    getModelRequest(){
      let params = {
        startTime: this.mModelCallDate[0],
        endTime: this.mModelCallDate[1],
        modelCallType: this.mModelCallType,
        taskId: this.mTaskId1,
      }
      queryModelRequest(params).then(res => {
        if(res.success){
          this.mChartData1.xData = [];
          this.mChartData1.yData = [];
          for(let i = 0; i < res.data.length; i++){
            let item = res.data[i];
            this.mChartData1.xData.push(item.modelCallDate);
            this.mChartData1.yData.push(item.totalToken);
          }
          this.mOption1.xAxis[0].data = this.mChartData1.xData;
          this.mOption1.series[0].data = this.mChartData1.yData;
          this.mChart1.setOption(this.mOption1);
        }
      })
    },

    getModelRequestChart(){
      let params = {
        startTime: this.mIntfCallDate[0],
        endTime: this.mIntfCallDate[1],
        intfCallType: this.mIntfCallType,
        taskId: this.mTaskId2,
      }
      queryModelRequestChart(params).then(res => {
        if(res.success){
          this.mChartData2.xData = [];
          this.mChartData2.yData = [];
          for(let i = 0; i < res.data.length; i++){
            let item = res.data[i];
            this.mChartData2.xData.push(item.modelCallDate);
            this.mChartData2.yData.push(item.totalToken);
          }
          this.mOption2.xAxis[0].data = this.mChartData2.xData;
          this.mOption2.series[0].data = this.mChartData2.yData;
          this.mChart2.setOption(this.mOption2);
        }
      })
    },
  

  }
}
</script>
<style lang="less" scoped>
.logcenter-title {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
}

.logcenter-block {
  width: 100%;
  border-radius: 8px;
  padding: 10px;
  margin-top: 10px;
  background-color: rgba(255, 255, 255, 1);
  .block-title{
    font-weight: bold;
    font-size: 16px;
  }
  .log-overview{
    margin-top: 10px;
    .row-item{
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      margin: 5px 20px;
      background: linear-gradient(to bottom, #ffffff, #f6f9ff);
      border:#E0EFFE solid 1px;
      border-radius: 10px;
    }
    .row-item-img{
      width: 56px;
      margin: 12px 20px 12px 0;
    }
    .row-item-content{ 
      font-size: 24px;
      font-weight: bold;
    }
  }

  .row-box{
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 10px;
    .row-flex{
      display: flex;
      flex: 1;
      justify-content: right;
    }
  }

}
</style>
