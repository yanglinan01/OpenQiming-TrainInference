<template>
  <div class="model-monitoring-container">
    <div class="model-monitoring-container-title">
      <span class="title-text">模型监控</span>
    </div>
    <div class="model-monitoring-tabs-button-block">
      <div class="block-button" style="display: flex;flex-wrap:nowrap">
        <div
            v-for="(item, index) in logButtonList"
            :key="index"
            class="block-button-item"
            :class="{'selected': item.value === logButtonSelected}"
            @click="logButtonSelect(item.value)"
        >
          <div style="display: flex;align-items: center;justify-content: center;">
            <img :src="require(`@/assets/images/monitoring/${item.icon}`)">
            <span style="user-select:none;margin-left: 10px">{{ item.label }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="model-monitoring-block-one" v-if="logButtonSelected==='1'">
      <div>
        <el-form :model="primaryData">
          <el-form-item label="模型选择：" style="width: 1127px;margin-left: 0px;">
            <el-select v-model="primaryData.modelId" placeholder="请选择" style="width: 60%;">
              <el-option
                  v-for="item in modelList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  @click.native="modelSelectChange(item.id)">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div class="log-overview">
        <table>
          <thead>
          <tr class="title-row">
            <td>日志概览</td>
          </tr>
          <tr>
            <th><img src="../../assets/images/business-icon-sales-center.png" alt="Image" class="image-cell"></th>
            <th><img src="../../assets/images/order-upload.png" alt="Image" class="image-cell"></th>
            <th><img src="../../assets/images/catalog-download.png" alt="Image" class="image-cell"></th>
            <th><img src="../../assets/images/product-checked.png" alt="Image" class="image-cell"></th>
          </tr>
          </thead>
          <tbody>
          <tr class="second-row">
            <td>调用总tokens</td>
            <td>输入总tokens</td>
            <td>输出总tokens</td>
            <td>模型调用量</td>
          </tr>
          <tr class="third-row">
            <td>{{ this.logOverviewData.totalTokens }}</td>
            <td>{{ this.logOverviewData.totalPromptTokens }}</td>
            <td>{{ this.logOverviewData.totalCompletionTokens }}</td>
            <td>{{ this.logOverviewData.totalCalls }}</td>
          </tr>
          </tbody>
        </table>
      </div>

    </div>
    <div class="model-monitoring-block" v-if="logButtonSelected==='1'">
      <div class="platform-usage-echart-block" style=" margin-bottom: 20px">
        <div style="display: flex; height: 70px;" class="echarts-head">
          <div style="font-size: 16px;color: #111D32;">模型Token统计</div>
          <div style="font-size: 14px">
            <el-radio-group v-model="modelTokenEChartCondition.radioDataSelected" size="medium"
                            @change="getEchartsmodelTokenData()">
              <el-radio-button label="1">全部</el-radio-button>
              <el-radio-button label="2">模型输入</el-radio-button>
              <el-radio-button label="3">模型输出</el-radio-button>
            </el-radio-group>
          </div>
          <div class="block" style="font-size: 14px">
            <el-date-picker
                v-model="hourlyTokenTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="getEchartsmodelTokenData()">
            </el-date-picker>
          </div>
        </div>
        <div id="modelTokenEChart" key="modelTokenEChart" ref="modelTokenEChart"
             style="width: 100%; height: 340px;"></div>
      </div>
    </div>
    <div class="model-monitoring-block" v-if="logButtonSelected==='1'">
      <div class="platform-usage-echart-block" style=" margin-bottom: 20px">
        <div style="display: flex; height: 70px;" class="echarts-head">
          <div style="font-size: 16px;color: #111D32;">模型调用量统计</div>
          <div class="block" style="font-size: 14px">
            <el-date-picker
                v-model="hourlyCallTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="getEchartsModelUsageData()"
            >
            </el-date-picker>
            <el-button type="primary" size="mini"
                       style="margin-left: 30px;width: 144px;height: 38px;background: #216EFF;border-radius: 4px;font-size: 14px;"
                       @click="establish">查看调用日志
            </el-button>
          </div>
        </div>
        <div id="modelUsageEChart" key="modelUsageEChart" ref="modelUsageEChart"
             style="width: 100%; height: 340px;"></div>
      </div>
    </div>
    <div class="model-monitoring-block" v-if="logButtonSelected==='2'" style="padding: 0">
      <div class="training-log-statistics-block" style="padding: 30px">
        模型训练任务:
        <el-select v-model="trainTaskSelectedId" filterable placeholder="请选择"
                   style="width: 40%;">
          <el-option
              v-for="item in trainTaskList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
              @click.native="trainTaskSelectChange(item.id)">
          </el-option>
        </el-select>
        <div class="training-log-statistics-content">
          <div class="training-log-statistics-content-item" v-for="(item,index) in trainingLogStatisticsList "
               :key="index">
            <div class="training-log-statistics-content-item-icon">
              <img :src="require(`@/assets/images/monitoring/${item.icon}`)">
            </div>
            <div class="training-log-statistics-content-item-title">
              {{ item.title }}
            </div>
            <div class="training-log-statistics-content-item-value">
              {{ item.value }}
            </div>
          </div>
        </div>
      </div>
      <div class="model-hyper-parameters-block" style="margin: 30px;padding-bottom: 30px">
        <el-descriptions class="margin-top" title="模型超参配置" :column="4" border>
          <el-descriptions-item v-for="(item, index) in modelHyperParameters" :key="index">
            <template slot="label">
              {{ item.displayName }}
            </template>
            {{ item.defaultValue }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
    <div class="model-monitoring-block" v-if="logButtonSelected==='2'">
      <div class="loss-analysis-echarts-block" style=" margin-bottom: 20px">
        <div style="display: flex; height: 40px" class="loss-analysis-echarts-header">
          <div style="font-size: 16px">Loss Function</div>
          <el-button type="primary" @click="lossAnalysisBlock"
                     :class="{ 'loss-analysis-button-up': lossAnalysisBlockFlag}"
                     style="margin-left: auto">
            {{ !lossAnalysisBlockFlag ? 'Loss趋势分析' : '收起Loss趋势分析' }}
          </el-button>
        </div>
        <div style="display: flex">
          <div id="lossAnalysisEChart" key="lossAnalysisEChart" ref="lossAnalysisEChart"
               :style="{width: chartWidth + '%', minHeight: 30+'vh',marginRight: 20+'px'}"></div>
          <div class="loss-analysis-content-right" v-if="lossAnalysisBlockFlag">
            <p style="">LOSS趋势分析:</p>
            <p style="margin: 20px">{{ lossAnalysisData.lossAnalysisText }}</p>
          </div>
        </div>
      </div>

    </div>
    <div class="model-monitoring-block" v-if="logButtonSelected==='2'">
      <div class="model-monitoring-block-title">
        训练日志
      </div>
      <div class="training-log-block">
        <div
            v-for="(item, index) in trainingLogList"
            :key="index"
            :class="getCssClass(index)"
            class="training-log-block-item"
        >
          {{ item }}
        </div>
      </div>
      <div class="pager-bar">
        <el-pagination
            background
            v-if="pageAllocations.total > 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryTrainTaskLogReq.page"
            :page-sizes="pageAllocations.page_sizes"
            :page-size="queryTrainTaskLogReq.size"
            :layout="pageAllocations.layout"
            :total="pageAllocations.total"
        ></el-pagination>
      </div>

    </div>

  </div>
</template>
<script>
import * as echarts from "echarts";
import elementResizeDetectorMaker from "element-resize-detector";
import {getTrainTasksDetail, queryTrainTaskList} from "@api/trainTask";
import {queryMmTrainLogList, getModelCallSummary, getHourlyTokenStats, getHourlyCallStats} from "@api/logCenter";
import {
  deployTaskQueryList
} from "@api/modelExperience";

export default {
  name: 'modelMonitoring',
  data() {
    return {
      reasoningTabsVisible: false,
      logButtonSelected: '1',
      logButtonList: [
        {value: '1', label: '模型推理日志', icon: 'reasoning_log_label.png'},
        {value: '2', label: '模型训练日志', icon: 'training_log_label.png'},
      ],
      trainTaskSelectedId: '',
      trainTaskList: [],
      trainingLogStatisticsList: [
        {
          icon: 'gallery.png',
          title: '训练状态',
          value: '训练中'
        },
        {
          icon: 'copy.png',
          title: '当前迭代次数',
          value: '1879'
        },
        {
          icon: 'time-ontime.png',
          title: '已运行时间',
          value: '108天'
        },
        {
          icon: 'time-task.png',
          title: '预计剩余训练时间',
          value: '3,123天'
        },
        {
          icon: 'product-list.png',
          title: '模型ID',
          value: 'qiming 1.5B'
        },
        {
          icon: 'product.png',
          title: '基础模型',
          value: 'qiming'
        },
        {
          icon: 'order.png',
          title: '训练数据',
          value: '91,108'
        },
        {
          icon: 'goods.png',
          title: '训练方法',
          value: 'lora'
        },
      ],
      lossAnalysisEChart: null,
      modelHyperParameters: [],
      lossAnalysisBlockFlag: false,
      lossAnalysisData: {
        xAxis: {
          data: [],
        },
        series: {
          data: [],
        },
        lossAnalysisText: ""
      },
      chartWidth: 100,
      primaryData: {
        trainTarget: '', //模型所处的地区
        modelId: ''
      },
      modelList: [
        // 示例数据
        {id: 1, name: '模型1'},
        {id: 2, name: '模型2'},
        {id: 3, name: '模型3'}
      ],
      logOverviewData: {},
      modelTokenEChartCondition: {
        radioDataSelected: '0',
      },
      modelTokenEChart: null,
      modelUsageEChart: null,
      // modelTokenEChartData: {},
      // modelUsageEChartData: {},
      modelTokenEChartData: {},
      modelUsageEChartData: {
        xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        series: [1, 132, 101, 134, 90, 230, 210]
      },
      // hourlyTokenTime: ['2024-11-26 00:00:00', '2024-11-27 00:00:00'],
      hourlyTokenTime: this.getTodayMidnight(),
      // hourlyCallTime: ['2024-11-26 00:00:00', '2024-11-27 00:00:00'],
      hourlyCallTime: this.getTodayMidnight(),
      trainingLogList: [],
      queryTrainTaskLogReq: {
        pageNum: 1,
        pageSize: 10
      },
      pageAllocations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },
    }
  },
  mounted() {
    this.deployTaskQueryList()
    this.queryTrainTaskList()
    this.$nextTick(() => {
      this.lossAnalysisEChartInit();
      this.handleResize('lossAnalysisEChart')
      this.modelTokenEChartInit()
      this.handleResize('modelTokenEChart')
      this.modelUsageEChartInit()
      this.handleResize('modelUsageEChart')
    })
  },
  created() {
    let logType=this.$route.query.logType
    if(logType){
      this.logButtonSelected = logType
    }
  },
  methods: {
    getTodayMidnight() {
      const today = new Date();
      // today.setHours(0, 0, 0, 0);
      const tomorrow = new Date(today);
      tomorrow.setDate(today.getDate() + 1);

      const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day} 00:00:00`;
      };

      return [formatDate(today), formatDate(tomorrow)];
    },
    logButtonSelect(value) {
      this.logButtonSelected = value
      if (this.lossAnalysisEChart) {
        this.lossAnalysisEChart.clear();
        this.lossAnalysisEChart = null; // 释放资源并置空
      }
      if (this.modelTokenEChart) {
        this.modelTokenEChart.clear();
        this.modelTokenEChart = null; // 释放资源并置空
      }
      if (this.modelUsageEChart) {
        this.modelUsageEChart.clear();
        this.modelUsageEChart = null; // 释放资源并置空
      }
      if (value === '1') {
        this.deployTaskQueryList()
      } else if (value === '2') {
        this.queryTrainTaskList()
      }
    },
    deployTaskQueryList(){
      // let that = this;
      let params = {
        status: 'completed'
      }
      deployTaskQueryList(params).then(res => {
            if (res.success) {
              // that.modelList = res.data
              // that.primaryData.modelId = that.modelList[0].id
              // that.modelSelectChange(that.primaryData.modelId)
              //
              this.modelList = res.data
              this.modelList = this.modelList.map(item => {
                return {...item, name: item.modelName};
              });
              this.primaryData.modelId = this.modelList[0].id
              this.modelSelectChange(this.primaryData.modelId)
            } else {
              console.error(res.message);
            }
          }
      ).catch(error => {
        console.error('查询列表报错', error);
      });
    },
    queryTrainTaskList() {
      let that = this;
      let params = {
        status: 'completed'
      }
      queryTrainTaskList(params).then((res) => {
        if (res.success) {
            that.trainTaskList = res.data
            let id = this.$route.query.id
            if(id){
              this.trainTaskSelectedId = id
              this.trainTaskSelectChange(this.trainTaskSelectedId )
            }else{
              that.trainTaskSelectedId = that.trainTaskList[0].id
              that.trainTaskSelectChange(that.trainTaskSelectedId)
            }
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    queryTrainTaskDetail() {
      let that = this;
      let params = {
        id: that.trainTaskSelectedId
      };
      getTrainTasksDetail(params).then((res) => {
        if (res.success) {
          let data = res.data;
          that.trainingLogStatisticsList[0].value = data.statusName || '未知';
          that.trainingLogStatisticsList[1].value = data.iterateCurr || '0';
          that.trainingLogStatisticsList[2].value = data.runtime || '0秒';
          that.trainingLogStatisticsList[3].value = data.remainTime || '0秒';
          that.trainingLogStatisticsList[4].value = data.id || '未知';
          that.trainingLogStatisticsList[5].value = data.modelName || '未知';
          that.trainingLogStatisticsList[6].value = data.dataSetName || '未知';
          that.trainingLogStatisticsList[7].value = data.type || '未知';
          that.modelHyperParameters = data.param
          const currentLength = that.modelHyperParameters.length;
          const remainder = currentLength % 4;
          const paddingLength = remainder === 0 ? 0 : 4 - remainder;
          for (let i = 0; i < paddingLength; i++) {
            that.modelHyperParameters.push({});
          }
          that.lossAnalysisData.lossAnalysisText = data.lossTrend
          if (res.data.trainingLossData) {
            that.lossAnalysisData.xAxis.data = data.trainingLossData.xAxisData
            that.lossAnalysisData.series.data = data.trainingLossData.seriesData
            that.lossAnalysisData.xAxis.startValue = data.trainingLossData.xAxisData[0]
            that.lossAnalysisEChartInit()
            this.handleResize('lossAnalysisEChart')
          } else {
            this.lossAnalysisEChart.clear();
            this.lossAnalysisEChart.setOption({});
          }
        } else {
          that.$message.error("查询失败");
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    trainTaskSelectChange() {
      this.queryTrainTaskDetail()
      this.queryTrainTaskLogPage()
    },
    lossAnalysisBlock() {
      this.lossAnalysisBlockFlag = !this.lossAnalysisBlockFlag
      this.chartWidth = this.lossAnalysisBlockFlag ? 60 : 100
    },
    lossAnalysisEChartInit() {
      this.lossAnalysisEChart = this.$echarts.init(document.getElementById("lossAnalysisEChart"));
      // 指定图表的配置项和数据
      let option =
          {
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '0',
              right: '3%',
              bottom: '15%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              data: this.lossAnalysisData.xAxis.data,
              axisLine: {
                show: true,
                lineStyle: {
                  color: '#DEDEDE'
                },
              },
              axisTick: {
                show: true
              },
              axisLabel: {
                color: '#333333',
              },
            },
            yAxis: {
              type: 'value',
              axisLine: {show: false},
              axisTick: {show: false},
              splitLine: {
                lineStyle: {
                  type: 'dashed'
                }
              }
            },
            dataZoom: [
              {
                startValue: this.lossAnalysisData.xAxis.startValue,
              },
              {
                type: 'inside'
              }],
            series: [
              {
                data: this.lossAnalysisData.series.data,
                type: 'line',
                color: '#0057FF',
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(
                      0, 0, 0, 1, // 渐变方向，从上到下
                      [
                        {offset: 0, color: '#4E8AFD'}, // 起始颜色
                        {offset: 1, color: '#FFFEFE'}  // 结束颜色
                      ]
                  )
                },
              }
            ]
          }
      option && this.lossAnalysisEChart.setOption(option, true);
    },
    handleResize(eChartDivId) {
      const that = this;
      const erd = elementResizeDetectorMaker();
      erd.listenTo(document.getElementById(eChartDivId), element => {
        that.$nextTick(() => {
          that.$echarts.init(document.getElementById(eChartDivId)).resize();
        });
      });
    },
    queryTrainTaskLogPage() {
      let that = this;
      let params = {
        taskId: that.trainTaskSelectedId,
        ...that.queryTrainTaskLogReq
      }
      queryMmTrainLogList(params).then((res) => {
        if (res.success) {
          that.trainingLogList = res.data.record
          that.paginations.total = Number(res.data.total)
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    handleCurrentChange(val) {
      this.queryTrainTaskLogReq.page = val;
      this.queryTrainTaskLogPage();
    },
    handleSizeChange(val) {
      this.queryTrainTaskLogReq.size = val;
      this.queryTrainTaskLogPage();
    },
    getCssClass(index) {
      if (index % 2 === 0) {
        return 'training-log-block-item-even';
      } else {
        return 'training-log-block-item-odd';
      }
    },

    establish() {
      this.$router.push({path: '/logCenter/logDetail', query: {establishPrompt: '查看调用日志'}})
    },
    modelSelectChange() {
      this.getModelCallSummary()
      this.getEchartsmodelTokenData()
      this.getEchartsModelUsageData()
    },
    getModelCallSummary() {
      let that = this;
      let params = {
        modelId: that.primaryData.modelId
      };
      getModelCallSummary(params).then((res) => {
        if (res.success) {
          that.logOverviewData = res.data
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },

    getEchartsmodelTokenData() {
      let that = this
      let params = {
        modelId: that.primaryData.modelId,
        endTime: this.hourlyTokenTime[1],
        startTime: this.hourlyTokenTime[0]
      }
      getHourlyTokenStats(params).then((res) => {
        if (res.success) {
          let echartsDataTemp = res.data
          this.modelTokenEChartData.xAxis = echartsDataTemp.xAxisData;

          // 根据 radioDataSelected 的值决定显示哪些数据系列
          switch (that.modelTokenEChartCondition.radioDataSelected) {
            case '0':
              this.modelTokenEChartData.totalTokensSumseries = echartsDataTemp.totalTokensSum;
              this.modelTokenEChartData.promptTokensSumseries = echartsDataTemp.promptTokensSum;
              this.modelTokenEChartData.completionTokensSumseries = echartsDataTemp.completionTokensSum;
              break;
            case '1':
              this.modelTokenEChartData.totalTokensSumseries = echartsDataTemp.totalTokensSum;
              this.modelTokenEChartData.promptTokensSumseries = [];
              this.modelTokenEChartData.completionTokensSumseries = [];
              break;
            case '2':
              this.modelTokenEChartData.totalTokensSumseries = [];
              this.modelTokenEChartData.promptTokensSumseries = echartsDataTemp.promptTokensSum;
              this.modelTokenEChartData.completionTokensSumseries = [];
              break;
            case '3':
              this.modelTokenEChartData.totalTokensSumseries = [];
              this.modelTokenEChartData.promptTokensSumseries = [];
              this.modelTokenEChartData.completionTokensSumseries = echartsDataTemp.completionTokensSum;
              break;
          }
          that.modelTokenEChartInit()
          that.handleResize('modelTokenEChart')
          // console.info(JSON.stringify(that.modelTokenEChartData))
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    getEchartsModelUsageData() {
      let that = this
      let params = {
        modelId: that.primaryData.modelId,
        endTime: this.hourlyCallTime[1],
        startTime: this.hourlyCallTime[0]
      }
      getHourlyCallStats(params).then((res) => {
        if (res.success) {
          let echartsDataTemp = res.data
          this.modelUsageEChartData.xAxis = echartsDataTemp.xAxisData;
          this.modelUsageEChartData.series = echartsDataTemp.totalCalls;
          that.modelUsageEChartInit()
          that.handleResize('modelUsageEChart')
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });


      // this.modelUsageEChartData.xAxis = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
      // this.modelUsageEChartData.series = [1, 132, 101, 134, 90, 230, 210];
      // this.modelUsageEChartInit()
      // this.handleResize('modelUsageEChart')
    },
    modelTokenEChartInit() {
      this.modelTokenEChart = this.$echarts.init(document.getElementById("modelTokenEChart"));
      // 指定图表的配置项和数据
      let option =
          {
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '2%',
              right: '1%',
              top: '3%',
              bottom: '12%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              data: this.modelTokenEChartData.xAxis,
              axisLine: {
                show: true,
                lineStyle: {
                  color: '#DEDEDE'
                },
              },
              axisTick: {
                show: true
              },
              axisLabel: {
                color: '#333333',
                interval: 0, // 强制显示所有标签
                rotate: 45 // 倾斜45度
              },
            },
            yAxis: {
              type: 'value',
              axisLine: {show: false},
              axisTick: {show: false},
              splitLine: {
                lineStyle: {
                  type: 'dashed'
                }
              }
            },
            dataZoom: [
              {
                startValue: this.modelTokenEChartData.xAxis.startValue,
              },
              {
                type: 'inside'
              }],
            series: [
              {
                data: this.modelTokenEChartData.completionTokensSumseries,
                type: 'line',
                smooth: false,
                color: '#0057FF',
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(
                      0, 0, 0, 1, // 渐变方向，从上到下
                      [
                        {offset: 0, color: '#4E8AFD'}, // 起始颜色
                        {offset: 1, color: '#FFFEFE'}  // 结束颜色
                      ]
                  )
                },
              },
              {
                data: this.modelTokenEChartData.promptTokensSumseries,
                type: 'line',
                smooth: false,
                color: '#FF8700',
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(
                      0, 0, 0, 1, // 渐变方向，从上到下
                      [
                        {offset: 0, color: '#FF8700'}, // 起始颜色
                        {offset: 1, color: '#FFFEFE'}  // 结束颜色
                      ]
                  )
                },
              },
              {
                data: this.modelTokenEChartData.totalTokensSumseries,
                type: 'line',
                smooth: false,
                color: '#48D972',
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(
                      0, 0, 0, 1, // 渐变方向，从上到下
                      [
                        {offset: 0, color: '#48D972'}, // 起始颜色
                        {offset: 1, color: '#FFFEFE'}  // 结束颜色
                      ]
                  )
                },
              }
            ]
          }
      option && this.modelTokenEChart.setOption(option, true);
    },
    modelUsageEChartInit() {
      this.modelUsageEChart = this.$echarts.init(document.getElementById("modelUsageEChart"));
      // 指定图表的配置项和数据
      let option =
          {
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '2%',
              right: '1%',
              top: '3%',
              bottom: '12%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              data: this.modelUsageEChartData.xAxis,
              axisLine: {
                show: true,
                lineStyle: {
                  color: '#DEDEDE'
                },
              },
              axisTick: {
                show: true
              },
              axisLabel: {
                color: '#333333',
                interval: 0, // 强制显示所有标签
                rotate: 45 // 倾斜45度
              },
            },
            yAxis: {
              type: 'value',
              axisLine: {show: false},
              axisTick: {show: false},
              splitLine: {
                lineStyle: {
                  type: 'dashed'
                }
              }
            },
            dataZoom: [
              {
                startValue: this.modelUsageEChartData.xAxis.startValue,
              },
              {
                type: 'inside'
              }],
            series: [
              {
                data: this.modelUsageEChartData.series,
                type: 'line',
                smooth: false,
                color: '#0057FF',
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(
                      0, 0, 0, 1, // 渐变方向，从上到下
                      [
                        {offset: 0, color: '#4E8AFD'}, // 起始颜色
                        {offset: 1, color: '#FFFEFE'}  // 结束颜色
                      ]
                  )
                },
              }
            ]
          }
      option && this.modelUsageEChart.setOption(option, true);
    },
  }
  ,
}
;
</script>

<style lang="less" scoped>
.model-monitoring-container {
  height: 100%;
}

.model-monitoring-container-title {
  .title-text {
    display: flex;
    align-items: center; /* 垂直居中 */
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: bold;
    font-size: 20px;
    color: #1C2748;
    margin-bottom: 24px;
  }
}

.model-monitoring-block {
  width: 100%;
  background: #FFFFFF;
  box-shadow: 0px 2px 32px 0px rgba(196, 209, 223, 0.5);
  border-radius: 16px;
  margin-bottom: 20px;
  display: block;
  padding: 10px 30px 30px 30px;
}

.model-monitoring-block-one {
  width: 100%;
  background: #FFFFFF;
  box-shadow: 0px 2px 32px 0px rgba(196, 209, 223, 0.5);
  border-radius: 0 0 16px 16px;
  margin-bottom: 20px;
  display: block;
  padding: 30px;
  background-image: url("../../assets/images/model-based-bg.png");
  background-size: 100% 100%; /* 背景图片完全填充容器 */
  background-position: center; /* 背景图片居中 */
  background-repeat: no-repeat; /* 背景图片不重复 */


}

.model-select {
  width: 80%;
  display: block; /* 确保下拉框可见 */
}

.platform-usage-echart-block {
  width: 100%;
  height: 370px;
  //background-color: #FBFBFB;
}

.echarts-head {
  display: flex;
  height: 60px;
  align-items: center;
  justify-content: space-between;
  padding: 0 10px 0 10px;
}

.training-log-statistics-block {
  width: 100%;
  background: url('../../assets/images/monitoring/training_log.png') center / cover no-repeat;
}

.training-log-statistics-content {
  display: flex;
  width: 60%;
  align-items: baseline;
  flex-wrap: wrap;
  flex-direction: row;
}

.training-log-statistics-content-item {
  display: flex;
  flex-direction: column;
  width: 20%;
  margin: 20px 20px 0 0;
}

.training-log-statistics-content-item-icon {

}

.training-log-statistics-content-item-title {
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-size: 12px;
}

.training-log-statistics-content-item-value {
  background-image: linear-gradient(180deg,
  rgba(71, 28, 255, 1) 0,
  rgba(33, 110, 255, 1) 100%);
  overflow-wrap: break-word;
  color: rgba(28, 39, 72, 1);
  font-size: 20px;
  font-family: SourceHanSansCN-Bold;
  font-weight: 700;
  text-align: left;
  line-height: 30px;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/deep/ .el-descriptions__title {
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  color: #111D32;
  line-height: 24px;
  text-align: left;
  font-style: normal;
}

/deep/ .el-descriptions .is-bordered {
  align-items: stretch;

}

/deep/ .el-descriptions .is-bordered .el-descriptions-item__cell {
  padding: 10px;
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.88);
  line-height: 22px;
  text-align: center;
  font-style: normal;
  text-transform: none;
  height: auto;
}

/deep/ .el-descriptions-item__label.is-bordered-label {
  width: 150px; /* label 区域的宽度 */
  font-weight: bold; /* label 文字加粗 */
  background-color: #FAFAFA;
  min-height: 50px; /* 设置最小高度 */
  word-break: break-all;
}

/deep/ .el-descriptions-item__content {
  width: 150px; /* 描述文字的宽度 */
  color: #666; /* 描述文字颜色 */
  background-color: #FFFFFF;
  min-height: 50px; /* 设置最小高度 */
  word-break: break-all;
}

.log-overview {
  width: 100%;
  max-width: 800px;
}

table {
  width: 100%;
  border-collapse: collapse;
  //  去除边框
}

th, td {
  padding: 8px;
  //border: 1px solid #ddd;
  text-align: left;
  border: none; /* 去掉单元格的边框 */

}

td {
  width: 150px; /* 调整列的宽度，可以根据需要调整具体值 */
  border: none; /* 去掉单元格的边框 */
}

.image-cell {
  width: 100%;
  height: auto;
  display: block;
}

.second-row td {
  font-size: 12px;
  color: #1C2748;
  text-align: left;

}

.title-row td {
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  color: #111D32;
  line-height: 24px;
  text-align: left;
  font-style: normal;

}

.third-row td {
  font-family: SourceHanSansCN;
  font-weight: bold;
  font-size: 20px;
  line-height: 30px;
  text-align: left;
  font-style: normal;
  background: linear-gradient(180deg, #471CFF 0%, #216EFF 100%); /* 从上到下的渐变 */
  background-clip: text;
  color: #471CFF; /* Fallback color */

}

.image-cell {
  width: 28px;
  height: 28px;
}

.loss-analysis-button-up {
  background: #D8DCE6;
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 14px;
  color: #1C2748;
  text-align: left;
  font-style: normal;
  border: none;
}

.loss-analysis-echarts-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  color: #111D32;
  line-height: 24px;
  text-align: left;
  font-style: normal;
}

.loss-analysis-content-right {
  white-space: pre-line;
  width: 40%;
  background: #FAFAFA;
  box-shadow: inset 0px -1px 0px 0px #F0F0F0;
  padding: 20px;
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  color: #111D32;
  line-height: 24px;
  text-align: left;
  font-style: normal;
}

.model-monitoring-block-title {
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  color: #111D32;
  line-height: 24px;
  text-align: left;
  font-style: normal;
  margin-bottom: 20px;
}

.training-log-block-item {
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.88);
  padding: 10px;
  text-align: left;
  font-style: normal;
  text-transform: none;

}

.training-log-block-item-even {
  background: #FAFAFA;
  box-shadow: inset 0px -1px 0px 0px #F0F0F0;
}

.training-log-block-item-odd {
  background: #FFFFFF;
  box-shadow: inset 0px -1px 0px 0px #F0F0F0;
}

.model-monitoring-tabs-button-block {
  width: 100%;
  background: #E6F0FD;
  border-radius: 16px 16px 0px 0px;
  color: #909BAA;
}

.block-button-item {
  width: 50%;
  padding: 12px;
}

.block-button-item.selected {
  background: #FFFFFF;
  border-radius: 16px 16px 0px 0px;
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: bold;
  font-size: 16px;
  color: #216EFF;
  line-height: 24px;
  text-align: left;
  font-style: normal;
}
/deep/ .el-form-item__label {
  text-align: right;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: #1C2748;
  line-height: 40px;
  padding: 0 12px 0 0;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
/deep/.el-radio-button--small .el-radio-button__inner {
 text-align: center;
  font-size: 14px;
}
/deep/.el-radio-button__orig-radio:checked+.el-radio-button__inner {
  color: #216EFF;
  background-color: #E8EFFF;
  border-color: #409EFF;}

/deep/.el-date-editor .el-range-input {
  color: #1C2748 ;
}
</style>
