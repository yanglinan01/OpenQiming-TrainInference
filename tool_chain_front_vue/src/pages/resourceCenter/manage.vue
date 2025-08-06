<template>
  <div>
    <p class="manage-title">资源中心</p>
    <div class="manage-block">
      <div class="block-title">资源总览</div>
      <div class="resource-overview-card">
        <div class="resource-overview-card-block resource-overview-card-block-jiq-Img" style="">
          <div class="resource-overview-card-block-text">
            <p class="resource-overview-card-Name">集群</p>
            <p class="resource-overview-card-Numb">{{ resourceCount.clusterCount || '-' }}</p>
          </div>
        </div>
        <div class="resource-overview-card-block resource-overview-card-block-fwq-Img">
          <div class="resource-overview-card-block-text">
            <p class="resource-overview-card-Name">服务器</p>
            <p class="resource-overview-card-Numb">{{ resourceCount.serverCount || '-' }}</p>
          </div>
        </div>

        <div class="resource-overview-card-block resource-overview-card-block-POD-Img">
          <div class="resource-overview-card-block-text">
            <p class="resource-overview-card-Name">POD</p>
            <p class="resource-overview-card-Numb">{{ resourceCount.podCount || '-' }}</p>
          </div>
        </div>
        <div class="resource-overview-card-block resource-overview-card-block-suanli-Img" style="margin-right: 0px;">
          <div class="resource-overview-card-block-text">
            <p class="resource-overview-card-Name">可用算力卡</p>
            <p class="resource-overview-card-Numb">{{ resourceCount.availableCardCount || '-' }}</p>
          </div>
        </div>
      </div>
      <div class="resource-overview-progress">
        <div class="resource-overview-progress-block">
          <div class="resource-overview-progress-title">
            <div class="color-block" style="background-color: #4E81FE;"></div>
            CPU使用量
          </div>
          <div style="display: flex;">
            <el-progress type="circle"
                         :percentage="calculatePercentage(Number(resourceUsage.cpuUsed||0),Number(resourceUsage.cpuTotal||0))"
                         width=80
                         style="margin-left:35px;margin-right: 10px"
                         color="#4E81FE"
            ></el-progress>
            <div style="margin-top:20px ;margin-left: 5px">
              <p>总量:{{ resourceUsage.cpuTotal || 0 }}</p>
              <p>剩余量:{{ resourceUsage.cpuFree || 0 }}</p>
            </div>
          </div>
        </div>
        <div class="resource-overview-progress-block">
          <div class="resource-overview-progress-title">
            <div class="color-block" style="background-color: #8E19E2;"></div>
            GPU使用量
          </div>
          <div style="display: flex;">
            <el-progress type="circle"
                         :percentage="calculatePercentage(Number(resourceUsage.gpuUsed||0),Number(resourceUsage.gpuTotal||0))"
                         width=80
                         style="margin-left:35px;margin-right: 10px "
                         color="#8E19E2"></el-progress>
            <div style="margin-top:20px ;margin-left: 5px">
              <p>总量:{{ resourceUsage.gpuTotal || 0 }}</p>
              <p>剩余量:{{ resourceUsage.gpuFree || 0 }}</p>
            </div>
          </div>
        </div>
        <div class="resource-overview-progress-block">
          <div class="resource-overview-progress-title">
            <div class="color-block" style="background-color: #14D07F;"></div>
            内存使用量
          </div>
          <div style="display: flex;">
            <el-progress type="circle"
                         :percentage="calculatePercentage(Number(resourceUsage.memoryUsed||0),Number(resourceUsage.memoryTotal||0))"
                         width=80
                         style="margin-left:35px;margin-right: 10px"
                         color="#14D07F"></el-progress>
            <div style="margin-top:20px ;margin-left: 5px">
              <p>总量:{{ resourceUsage.memoryTotal || 0 }}</p>
              <p>剩余量:{{ resourceUsage.memoryFree || 0 }}</p>
            </div>
          </div>
        </div>
        <div class="resource-overview-progress-block">
          <div class="resource-overview-progress-title">
            <div class="color-block" style="background-color: #E16F08;"></div>
            存储使用量
          </div>
          <div style="display: flex;">
            <el-progress type="circle"
                         :percentage="calculatePercentage(Number(resourceUsage.storageUsed||0),Number(resourceUsage.storageTotal||0))"
                         width=80
                         style="margin-left:35px;margin-right: 10px "
                         color="#E16F08"></el-progress>
            <div style="margin-top:20px ;margin-left: 5px">
              <p>总量:{{ resourceUsage.storageTotal || 0 }}</p>
              <p>剩余量:{{ resourceUsage.storageFree || 0 }}</p>
            </div>
          </div>
        </div>
        <div class="resource-overview-progress-block" style="margin-right: 0">
          <div class="resource-overview-progress-title">
            <div class="color-block" style="background-color: #4E1430;"></div>
            可用服务器
          </div>
          <div style="display: flex;">
            <el-progress type="circle"
                         :percentage="calculatePercentage(Number(resourceUsage.serverUsed||0),Number(resourceUsage.serverTotal||0))"
                         width=80
                         style="margin-left:35px;margin-right: 10px"
                         color="#4E1430"></el-progress>
            <div style="margin-top:20px ;margin-left: 5px">
              <p>总量:{{ resourceUsage.serverTotal || 0 }}</p>
              <p>剩余量:{{ resourceUsage.serverFree || 0 }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="manage-block">
      <div class="block-title">集群信息</div>
      <div class="block-content">
        <div class="block-button">
          <div
              v-for="(item, index) in clusters"
              :key="index"
              class="block-button-item"
              :class="{ 'selected': item.value === clusterCode }"
              @click="getClusterDetail(item.value)">
            <el-row class="center-contents">
              <img src="../../assets/images/icon_jq.png" alt="" class="structureImg">
              <span style="user-select:none">{{ item.label }}</span>
            </el-row>
          </div>
        </div>
        <el-descriptions :column="5" style="width: 80%;font-family: SourceHanSansSC, SourceHanSansSC;">
          <el-descriptions-item label="集群省份">{{ clusterDetail.province || '-' }}</el-descriptions-item>
          <el-descriptions-item label="CPU利用率">{{ clusterDetail.cpuUsage || 0 }}</el-descriptions-item>
          <el-descriptions-item label="内存使用率">{{ clusterDetail.memoryUsage || 0 }}</el-descriptions-item>
          <el-descriptions-item label="训练服务器剩余量">{{ clusterDetail.trainServerAvail || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="推理服务器剩余量">{{ clusterDetail.inferenceServerAvail || 0 }}
          </el-descriptions-item>
        </el-descriptions>
        <div>
          <el-row>
            <el-col :span="12" style="">
              <div style="width: 95%; height: 470px;background-color: #FFFFFF">
                <div style="display: flex; padding: 10px;height: 60px" class="echarts-head">
                  <div style="margin-top: 10px">
                    <el-radio-group v-model="leftEChartCondition.radioDataSelected" size="small"
                                    @change="getClusterUsageTrend('left')">
                      <el-radio-button label="CPU"></el-radio-button>
                      <el-radio-button label="GPU"></el-radio-button>
                    </el-radio-group>
                  </div>
                  <div style="margin: 10px 20px 10px auto">
                    <el-select v-model="leftEChartCondition.predictTimeSelect" style="width:100px;"
                               @change="getClusterUsageTrend('left')">
                      <el-option v-for="item in predictTimeList" :value="item.value" :label="item.label"
                                 :key="item.value"></el-option>
                    </el-select>
                  </div>
                </div>
                <div id="leftEChart" ref="leftEChart" style="width: 100%; height: 400px;"></div>
              </div>
            </el-col>
            <el-col :span="12">
              <div style="width: 100%; height: 470px;background-color: #FFFFFF">
                <div style="display: flex; padding: 10px;height: 60px" class="echarts-head">
                  <div style="margin-top: 10px">
                    <el-radio-group v-model="rightEChartCondition.radioDataSelected" size="small"
                                    @change="getClusterUsageTrend('right')">
                      <el-radio-button label="disk">存储</el-radio-button>
                      <el-radio-button label="memory">内存</el-radio-button>
                    </el-radio-group>
                  </div>
                  <div style="margin: 10px 20px 10px auto">
                    <el-select v-model="rightEChartCondition.predictTimeSelect" style="width:100px;"
                               @change="getClusterUsageTrend('right')">
                      <el-option v-for="item in predictTimeList" :value="item.value" :label="item.label"
                                 :key="item.value"></el-option>
                    </el-select>
                  </div>
                </div>
                <div id="rightEChart" ref="rightEChart" style="width: 100%; height: 400px;"></div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div class="manage-block">
      <div class="block-title">使用资源展示</div>
      <div class="block-content">
        <span><strong style="color:red">*</strong>模型选择：</span>
        <el-select v-model="selectedModelId" style="width:400px;" filterable @change="getUsingResourceInfo()" placeholder="请选择模型">
          <el-option v-for="item in modelList" :value="item.id" :label="item.name" :key="item.id"></el-option>
        </el-select>
        <div style="padding: 5px;margin-top: 10px">
          <el-descriptions :column="2" style="width: 80%;font-family: SourceHanSansSC, SourceHanSansSC;">
            <el-descriptions-item label="集群名称">{{ usingResourceInfo.clusterName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用途">{{ usingResourceInfo.usage || '-' }}</el-descriptions-item>
            <el-descriptions-item label="算卡力">{{ usingResourceInfo.computingCard || '-' }}</el-descriptions-item>
          </el-descriptions>
          <el-descriptions :column="2" style="width: 80%;font-family: SourceHanSansSC, SourceHanSansSC;">
            <el-descriptions-item label="GPU显存使用量">{{ usingResourceInfo.gpuMemoryUsed || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="GPU显存总量">{{ usingResourceInfo.gpuMemoryTotal || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="GPU显卡温度">{{ usingResourceInfo.gpuTemperature || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="GPU功率">{{ usingResourceInfo.gpuPowerUsage || '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </div>

  </div>
</template>
<script>
import elementResizeDetectorMaker from "element-resize-detector";
import {
  clusterDetail,
  clusterUsageTrend,
  getUsingResourceInfo,
  resourceCount,
  resourceUsage
} from "@api/resourceCenter";
import {queryTrainTaskList} from "@api/trainTask";


export default {
  name: 'manage',
  data() {
    return {
      clusterCode: 'QD',
      clusters: [
        {value: 'QD', label: '青岛资源池'},
        {value: 'GZ', label: '贵州资源池'}
      ],
      leftEChart: null,
      rightEChart: null,
      leftEChartCondition: {
        radioDataSelected: 'CPU',
        predictTimeSelect: '1',
      },
      rightEChartCondition: {
        radioDataSelected: 'disk',
        predictTimeSelect: '1',
      },
      leftEChartData: {},
      rightEChartData: {},
      eChartData: {
        cpuUsed: {
          time: [],
          value: []
        },
        gpuUsed: {
          time: [],
          value: []
        },
        memoryUsed: {
          time: [],
          value: []
        },
        storageUsed: {
          time: [],
          value: []
        }
      },
      modelList: [],
      selectedModelId: null,
      predictTimeSelect: '1',
      predictTimeList: [
        {
          value: '1',
          label: '1天'
        },
        {
          value: '7',
          label: '7天'
        },
        {
          value: '15',
          label: '15天'
        },
        {
          value: '30',
          label: '30天'
        },
      ],
      resourceCount: {
        availableCardCount: 0,
        clusterCount: 0,
        podCount: 0,
        serverCount: 0
      },
      resourceUsage: {
        cpuFree: 0,
        cpuTotal: 0,
        cpuUsed: 0,
        gpuFree: 0,
        gpuTotal: 0,
        gpuUsed: 0,
        memoryFree: 0,
        memoryTotal: 0,
        memoryUsed: 0,
        serverFree: 0,
        serverTotal: 0,
        serverUsed: 0,
        storageFree: 0,
        storageTotal: 0,
        storageUsed: 0
      },
      clusterDetail: {
        clusterName: "",
        cpuUsage: 0,
        inferenceServerAvail: 0,
        memoryUsage: 0,
        province: "",
        trainServerAvail: 0
      },
      getUsingResourceInfoParam: {
        clusterCode: "",
        taskId: 0,
        taskType: "",
        usageTrendDuration: 1
      },
      usingResourceInfo: {
        clusterName: "",
        computingCard: "",
        gpuMemoryTotal: "",
        gpuMemoryUsed: "",
        gpuPowerUsage: "",
        gpuTemperature: "",
        usage: ""
      },
    };
  },
  created() {
    this.getResourceCount()
    this.getResourceUsage()
    this.getClusterDetail('QD')
    this.getModelInfoList()
  },
  methods: {
    getResourceCount() {
      let that = this;
      resourceCount().then((res) => {
        if (res.success) {
          that.resourceCount = res.data
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    getResourceUsage() {
      let that = this;
      resourceUsage().then((res) => {
        if (res.success) {
          that.resourceUsage = res.data
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    getClusterDetail(clusterCode) {
      let that = this;
      this.clusterCode = clusterCode;
      let params = {
        clusterCode: that.clusterCode
      }
      clusterDetail(params).then((res) => {
        if (res.success) {
          that.clusterDetail = res.data[0]
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
      that.getClusterUsageTrend('init')
    },
    calculatePercentage(used, total) {
      if (total === 0) return 0;
      const percentage = (used / total) * 100;
      return Math.ceil(percentage);
    },
    getUsingResourceInfo() {
      let that = this;
      const modelParam = that.modelList.find(item => item.id === that.selectedModelId);
      if (modelParam.deployTaskList[0]) {
        that.getUsingResourceInfoParam.taskType = 'interface'
        that.getUsingResourceInfoParam.taskId = modelParam.deployTaskList[0].id
        that.getUsingResourceInfoParam.clusterCode = modelParam.deployTaskList[0].deployTarget
      } else {
        that.getUsingResourceInfoParam.taskType = 'train'
        that.getUsingResourceInfoParam.taskId = modelParam.id
        that.getUsingResourceInfoParam.clusterCode = modelParam.trainTarget
      }
      that.getUsingResourceInfoParam.usageTrendDuration = 1
      getUsingResourceInfo(that.getUsingResourceInfoParam).then((res) => {
        if (res.success) {
          that.usingResourceInfo = res.data
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    getModelInfoList() {
      let that = this;
      let params = {
        resourceOccupy: '0'
      }
      queryTrainTaskList(params).then((res) => {
        if (res.success) {
          that.modelList = res.data
          if (that.modelList.length > 0) {
            that.selectedModelId = that.modelList[0].id
            that.getUsingResourceInfo()
          }
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    getClusterUsageTrend(flag) {
      let that = this
      let params = {
        clusterCode: that.clusterCode,
        duration: flag === 'right' ? that.rightEChartCondition.predictTimeSelect : that.leftEChartCondition.predictTimeSelect,
      }
      clusterUsageTrend(params).then((res) => {
        if (res.success) {
          let echartsDataTemp = res.data
          that.eChartData.cpuUsed.time = echartsDataTemp.cpuUsed.map(item => item.time);
          that.eChartData.cpuUsed.value = echartsDataTemp.cpuUsed.map(item => item.value);
          that.eChartData.gpuUsed.time = echartsDataTemp.gpuUsed.map(item => item.time);
          that.eChartData.gpuUsed.value = echartsDataTemp.gpuUsed.map(item => item.value);
          that.eChartData.memoryUsed.time = echartsDataTemp.memoryUsed.map(item => item.time);
          that.eChartData.memoryUsed.value = echartsDataTemp.memoryUsed.map(item => item.value);
          that.eChartData.storageUsed.time = echartsDataTemp.storageUsed.map(item => item.time);
          that.eChartData.storageUsed.value = echartsDataTemp.storageUsed.map(item => item.value);
          if (flag === 'init') {
            if (that.leftEChartCondition.radioDataSelected === 'CPU') {
              this.leftEChartData = {
                xAxis: {data: that.eChartData.cpuUsed.time,},
                series: [{data: that.eChartData.cpuUsed.value,}]
              }
            } else {
              this.leftEChartData = {
                xAxis: {data: that.eChartData.gpuUsed.time,},
                series: [{data: that.eChartData.gpuUsed.value,}]
              }
            }
            if (that.rightEChartCondition.radioDataSelected === 'disk') {
              this.rightEChartData = {
                xAxis: {data: that.eChartData.storageUsed.time,},
                series: [{data: that.eChartData.storageUsed.value,}]
              }
            } else {
              this.rightEChartData = {
                xAxis: {data: that.eChartData.memoryUsed.time,},
                series: [{data: that.eChartData.memoryUsed.value,}]
              }
            }

            this.leftEChartInit()
            this.rightEChartInit()
            this.handleResize('leftEChart')
            this.handleResize('rightEChart')
          }
          if (flag === 'left') {
            if (that.leftEChartCondition.radioDataSelected === 'CPU') {
              this.leftEChartData = {
                xAxis: {data: that.eChartData.cpuUsed.time,},
                series: [{data: that.eChartData.cpuUsed.value,}]
              }
            } else {
              this.leftEChartData = {
                xAxis: {data: that.eChartData.gpuUsed.time,},
                series: [{data: that.eChartData.gpuUsed.value,}]
              }
            }
            this.leftEChartInit()
            this.handleResize('leftEChart')
          }
          if (flag === 'right') {
            if (that.rightEChartCondition.radioDataSelected === 'disk') {
              this.rightEChartData = {
                xAxis: {data: that.eChartData.storageUsed.time,},
                series: [{data: that.eChartData.storageUsed.value,}]
              }
            } else {
              this.rightEChartData = {
                xAxis: {data: that.eChartData.memoryUsed.time,},
                series: [{data: that.eChartData.memoryUsed.value,}]
              }
            }
            this.rightEChartInit()
            this.handleResize('rightEChart')
          }
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });

    },
    leftEChartInit() {
      this.leftEChart = this.$echarts.init(document.getElementById("leftEChart"));
      // 指定图表的配置项和数据
      let option =
          {
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '2%',
              right: '3%',
              top: '3%',
              bottom: '10%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              data: this.leftEChartData.xAxis.data,
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
                color: '#333333'
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
                startValue: this.leftEChartData.xAxis.data[0],
              }, {
                type: 'inside'
              }],
            series: [
              {
                data: this.leftEChartData.series[0].data,
                type: 'line',
                color: '#0057FF'
              }
            ]
          }
      option && this.leftEChart.setOption(option, true);
    },
    rightEChartInit() {
      this.rightEChart = this.$echarts.init(document.getElementById("rightEChart"));
      // 指定图表的配置项和数据
      let option =
          {
            tooltip: {
              trigger: 'axis'
            },
            grid: {
              left: '2%',
              right: '3%',
              top: '3%',
              bottom: '10%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              data: this.rightEChartData.xAxis.data,
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
                color: '#333333'
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
                startValue: this.rightEChartData.xAxis.data[0],
              },
              {
                type: 'inside',
              }],
            series: [
              {
                name: this.rightEChartData.series[0].name,
                data: this.rightEChartData.series[0].data,
                type: 'line',
                color: '#14D07F'
              }
            ]
          }
      option && this.rightEChart.setOption(option, true);
    },
    handleResize(eChartDivId) {
      const that = this;
      const erd = elementResizeDetectorMaker();
      erd.listenTo(document.getElementById(eChartDivId), element => {
        let width = element.offsetWidth;//当前div的宽度
        let height = element.loffsetHeight;//当前div的高度
        that.$nextTick(() => {
          that.$echarts.init(document.getElementById(eChartDivId)).resize();
        });
      });
    },
  },
}
</script>
<style lang="less" scoped>
.manage-title {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
}

.manage-block {
  width: 100%;
  border-radius: 8px;
  padding: 20px;
  margin-top: 10px;
  background-color: rgba(255, 255, 255, 1);

  .block-title {
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 20px;
  }

  .block-content {
    background: #F7F8F9;;
    border-radius: 8px;
    padding: 20px;
  }

  .block-button {
    display: flex;

    .block-button-item {
      margin: 5px 50px 20px 0;
      width: 120px;
      height: 30px;
      background: linear-gradient(180deg, #E2EEFF 0%, #FFFFFF 100%);
      box-shadow: 0px 2px 4px 0px rgba(180, 190, 215, 0.24);
      border-radius: 4px;
      border: 1px solid;
      border-image: linear-gradient(180deg, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0.31)) 1 1;
    }

    .block-button-item.selected {
      border: 1px solid #1B66FF;
    }
  }

}

.resource-overview-card {
  display: flex;

  .resource-overview-card-block {
    flex: 1;
    width: 366px;
    height: 170px;
    margin-right: 25px;
    display: flex;
    align-items: center;
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover;
    display: flex;
    justify-content: center;

    .resource-overview-card-block-text {
      margin-left: 60px;
    }
  }

  .resource-overview-card-block-fwq-Img {
    background-image: url('../../assets/images/fwq.png');
  }

  .resource-overview-card-block-jiq-Img {
    background-image: url('../../assets/images/jiq.png');
  }

  .resource-overview-card-block-POD-Img {
    background-image: url('../../assets/images/pod.png');
  }

  .resource-overview-card-block-suanli-Img {
    background-image: url('../../assets/images/suanli.png');
  }

  .resource-overview-card-Img {
    width: 120px;
    height: 120px;
    margin-left: 50px;
    margin-right: 40px;
  }

  .resource-overview-card-Name {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    font-size: 18px;
    color: #1C2748;
  }

  .resource-overview-card-Numb {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    font-size: 30px;
    color: #216EFF;
    margin-top: 10px;
  }
}

.resource-overview-progress {
  display: flex;

  .resource-overview-progress-block {
    background-color: rgba(247, 248, 249, 1);
    border-radius: 4px;
    height: 126px;
    border: 1px solid rgba(255, 255, 255, 1);
    width: 280px;
    flex: 1;
    margin: 20px 25px 0 0;
    align-items: center;

    .resource-overview-progress-title {
      width: 100px;
      height: 20px;
      margin: 10px;
      font-family: SourceHanSansSC, SourceHanSansSC;
      font-weight: 500;
      font-size: 14px;
      color: #27292B;
      line-height: 20px;
      text-align: left;
      font-style: normal;
      display: flex;
    }
  }
}

.color-indicator {
  width: 3px;
  height: 6px;
  margin-top: 7px;
}

.center-contents {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 5px;
}

.center-contents > .el-col {
  display: flex;
  align-items: center;
  justify-content: center;
}

.structureImg {
  max-width: 100%;
  height: auto;
  margin-right: 10px;
}

.color-block {
  width: 4px;
  height: 8px;
  margin: 5px 5px 0 0;
}


/deep/ .el-descriptions {
  width: 80%;
  background-color: #F7F8F9;
}

::v-deep .el-descriptions__body {
  background: #F7F8F9 !important;
}

/deep/ .el-descriptions-item {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: 400;
  font-size: 14px;
  color: #1C2748;
  line-height: 20px;
  text-align: left;
  font-style: normal;
}

/deep/ .el-input__inner {
  height: 30px;
}

</style>
