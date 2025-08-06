<template>
  <div>
    <div class="createTrainTasks">模型训练</div>
    <div class="trainTasksBlock">
      <div class="trainTasks">
        <el-tabs style="width: 30%" v-model="trainingType">
          <el-tab-pane label="SFT模型训练" name="method">
            <div class="flex-container">
              <i class="createTrainTasksButton" v-for="(item,index) in createTrainTasksButtonData.trainingMethodList"
                 :key="index">
                <el-button type="primary" @click="createTrainTasks(item,'false')">{{ item.dictItemLabel }}</el-button>
              </i>
            </div>
          </el-tab-pane>
          <el-tab-pane label="强化学习" name="target">
            <div class="flex-container">
              <i class="createTrainTasksButton" v-for="(item,index) in createTrainTasksButtonData.trainingTargetsList"
                 :key="index">
                <el-button type="primary" @click="createTrainTasks(item,'true')">{{ item.dictItemLabel }}</el-button>
              </i>
            </div>
          </el-tab-pane>
        </el-tabs>
        <div class="training-process-content" v-if="trainingType==='method'">
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/training_process_img_1.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <div>
                <span>上传训练数据，前往</span>
                <router-link to="/managementCenter/data" style=" white-space: nowrap; color: #216EFF">数据中心
                </router-link>
              </div>
            </div>
          </div>
          <div class="orange-line-with-arrow">
            <img src="../../assets/images/train/right-double-arrow.png" alt="">
          </div>
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/training_process_img_2.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <span>开起模型训练，选择训练方法、模型以及训练数据</span>
            </div>
          </div>
          <div class="orange-line-with-arrow">
            <img src="../../assets/images/train/right-double-arrow.png" alt="">
          </div>
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/training_process_img_3.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <span>完成模型训练，进行模型部署和体验</span>
            </div>
          </div>
        </div>
        <div class="training-process-content" v-if="trainingType==='target'">
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/strengthen_process_img_1.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <div>
                <span>上传训练数据，前往</span>
                <router-link to="/managementCenter/data" style=" white-space: nowrap;color: #216EFF">数据中心
                </router-link>
              </div>
            </div>
          </div>
          <div class="orange-line-with-arrow">
            <img src="../../assets/images/train/right-double-arrow.png" alt="">
          </div>
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/strengthen_process_img_2.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <div>
                <span>开启</span>
                <router-link to="/evaluating/index" style=" white-space: nowrap;color: #216EFF">模型测评</router-link>
                <span>,完成后下载评估结果</span>
              </div>
            </div>
          </div>
          <div class="orange-line-with-arrow">
            <img src="../../assets/images/train/right-double-arrow.png" alt="">
          </div>
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/strengthen_process_img_3.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <div>
                <span>线下进行人工评估,完成后</span>
                <router-link to="/evaluating/index" style=" white-space: nowrap;color: #216EFF">上传测评结果
                </router-link>
                <span>,并同步至数据中心</span>
              </div>
            </div>
          </div>
          <div class="orange-line-with-arrow">
            <img src="../../assets/images/train/right-double-arrow.png" alt="">
          </div>
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/strengthen_process_img_4.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <span>选定模型和数据,开启强化学习</span>
            </div>
          </div>
          <div class="orange-line-with-arrow">
            <img src="../../assets/images/train/right-double-arrow.png" alt="">
          </div>
          <div class="step_box">
            <div class="img_content">
              <img src="../../assets/images/train/strengthen_process_img_5.png" alt=""></img>
            </div>
            <div class="step_text_content">
              <span>完成模型训练,进行模型部署和体验</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="trainTasksBlock">
      <div class="allTasksTableHead">
        <div>
          <span class="blockblue"></span>
          <span class="blockName">所有任务</span>
        </div>
        <div class="allTasksTableHeadSelect">
          <el-input
              placeholder="请输入任务名称"
              v-model="queryParam.name"
              class="input-with-select"
              @keyup.enter.native="getList"
          >
          </el-input>
          <i class="el-icon-search" @click="getList()"></i>
        </div>
      </div>
      <el-table :data="allTasksList" style="width: 100%"
                v-loading="getListLoading">
        <el-table-column prop="name" label="训练模型名称" align="left">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center;">
              <span @click="detail(scope.row.id)"
                    style="cursor: pointer; text-decoration: underline;color: #1B66FF">
                {{ scope.row.name || '-' }}</span>
              <div class="reinforcement_learning_tag_block" v-if="scope.row.reinforcementLearningFlag">
                <span class="reinforcement_learning_tag_text">强化学习</span>
              </div>
              <el-button icon="el-icon-edit-outline" @click="taskUpdate(scope.row)"
                         style="padding: 0; border: 0; margin-left: auto;"></el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="模型id" align="left">
          <template slot-scope="scope">
            <span @click="routerToModelMonitoring(scope.row.id,'2')"
                  style="cursor: pointer; text-decoration: underline;color: #1B66FF">
                  {{ scope.row.id }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="modelName" label="基础模型" align="left">
          <template slot-scope="scope">
            <span>{{ scope.row.modelName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人/省份" align="left">
          <template slot-scope="scope">
             <span>
               {{ scope.row.creatorName }}/{{ scope.row.regionName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" align="left">
          <template slot-scope="scope">
            <span>{{ scope.row.createDate }}</span>
          </template>
        </el-table-column>
        <el-table-column align="left" label="训练状态">
          <template slot-scope="scope">
            <div v-if="!scope.row.reinforcementLearningFlag">
              <span class="center_dot_status"
                    :style="{ backgroundColor: caseStatusColorFilter(scope.row)}">
              </span>
              <span class="cm-status" style="margin: 0 5px">
                {{ scope.row.statusName }}
              </span>
              <span v-if="scope.row.status ==='completed'">
                {{ deployStatusTextFilter(scope.row, 'text') }}
              </span>
            </div>
            <div v-if="scope.row.reinforcementLearningFlag">
               <span class="center_dot_status"
                     :style="{ backgroundColor: caseStatusColorFilter(scope.row)}">
              </span>
              <span style="margin: 0 5px">{{ scope.row.statusName }}</span>
              <span v-if="scope.row.status ==='completed'">
                {{ deployStatusTextFilter(scope.row, 'text') }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="left">
          <template slot-scope="scope">
            <el-button type="text" size="mini" v-if="!scope.row.toDeleteDisabled"
                       @click="deleteRow(scope.row.id)">
              删除
            </el-button>
            <el-button type="text" size="mini" v-if="scope.row.toDeleteDisabled"
                       @click="terminate(scope.row.id)">
              终止训练
            </el-button>
            <el-button
                :type="getDeployButtonType(scope.row,'type')"
                size="mini"
                @click="changeDeployStatus(scope.row)"
                v-if="scope.row.status==='completed'">
              {{ getDeployButtonType(scope.row, 'text') }}
            </el-button>
            <el-button type="text" size="mini" @click="modelReleaseDialog(scope.row)"
                       v-if="scope.row.status==='completed'"
            >
              发布
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager-bar">
        <el-pagination
            v-if="paginations.total > 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryParam.currentPage"
            :page-sizes="paginations.page_sizes"
            :page-size="queryParam.pageSize"
            :layout="paginations.layout"
            :total="paginations.total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog title="查看信息" :visible.sync="trainTasksDialogVisible" v-loading="getTrainTasksDetailLoading">
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>任务名称:{{ trainTasksDetail.name }}</span></el-col
        >
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>模型ID:{{ trainTasksDetail.id }}</span></el-col
        >
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>基础模型:{{ trainTasksDetail.modelName }}</span></el-col
        >
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>训练数据:{{ trainTasksDetail.dataSetName || "-" }}</span>
        </el-col>
      </el-row>
      <el-row class="trainTasks-dialog" v-if="trainTasksDetail.deployTaskList.length>0">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>部署状态:{{ trainTasksDetail.deployTaskList[0].statusName }}</span></el-col
        >
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>部署位置:{{ trainTasksDetail.deployTaskList[0].deployTargetName || "-" }}</span>
        </el-col>
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-row class="trainTasks-dialog-left" style="margin-bottom: 5px"><span>模型超参配置:</span></el-row>
        <el-row class="trainTasks-dialog-table-row" v-for="(item,index) in trainTasksDetail.params" :key="index">
          <el-col v-for="(table,index2) in item" :key="index2" :span="12" class="table-item">
            <el-col :span="6" class="trainTasks-dialog-table-col-char">{{ table.displayName }}</el-col>
            <el-col :span="6" class="trainTasks-dialog-table-col-content">{{ table.defaultValue }}</el-col>
          </el-col>
        </el-row>
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
                v-if="trainTasksDetail.iterateTotal !== null && trainTasksDetail.iterateTotal !== ''"
        ><span>总iteration:{{ trainTasksDetail.iterateTotal }}</span></el-col
        >
        <el-col :span="12" class="trainTasks-dialog-left"
                v-if="trainTasksDetail.iterateCurr !== null && trainTasksDetail.iterateCurr !== ''"
        ><span>当前iteration:{{ trainTasksDetail.iterateCurr }}</span></el-col
        >
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>已运行时间(s):{{ trainTasksDetail.runtime }}</span></el-col
        >
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>预计剩余训练时间(s):{{ trainTasksDetail.remainTime }}</span></el-col
        >
      </el-row>
      <!--     //  添加折线图-->
      <!--      //当this.trainTasksDetail.trainingLossData为空时，不显示折线图-->
      <div v-show="trainTasksDetail.trainingLossData!=''" id="portInflowEChart" ref="portInflowEChart"
           style="width: 100%; height: 300px;"></div>

    </el-dialog>
    <el-dialog title="修改" :visible.sync="updateDialogVisible">
      <div style="text-align: left">
        <el-form :model="updateTaskReq">
          <el-form-item label="原名称">
            <el-input v-model="originalTaskName" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="新名称">
            <el-input v-model="updateTaskReq.updateTaskName" placeholder="请输入新名称">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible= false">取 消</el-button>
        <el-button type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="模型发布" :visible.sync="releaseDialogVisible">
      <el-form :model="modelReleaseReq" :rules="modelReleaseRules" ref="modelReleaseForm">
        <el-form-item label="发布类型" prop="publishTo">
          <el-radio-group v-model="modelReleaseReq.publishTo" style="width: 100%" @change="changePublishType">
            <el-radio label="0">发布到模型广场</el-radio>
            <el-radio label="1">发布到项目空间</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item  v-if="modelReleaseReq.publishTo==='1'">
          <el-select v-model="modelReleaseReq.projectSpaceId"
                     style="width: 100%"
                     filterable placeholder="请选择项目空间">
            <el-option
                v-for="item in modelReleaseProjectList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName" v-if="modelReleaseReq.publishTo==='0'">
          <el-input v-model="modelReleaseReq.modelName" placeholder="请输入模型名称"></el-input>
        </el-form-item>
        <el-form-item label="模型描述" prop="modelDesc">
          <el-input
              type="textarea"
              placeholder="请描述模型能力，适用场景及交互示例"
              maxlength="200"
              :autosize="{ minRows: 3 }"
              show-word-limit
              v-model="modelReleaseReq.modelDesc">
          </el-input>
        </el-form-item>
        <el-form-item label="出入参信息" prop="modelParams" v-if="modelReleaseReq.publishTo==='0'">
          <el-input
              type="textarea"
              placeholder="请填写请求和返回消息格式"
              maxlength="200"
              show-word-limit
              :autosize="{ minRows: 3 }"
              v-model="modelReleaseReq.modelParams">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="releaseDialogVisible= false">取 消</el-button>
        <el-button type="primary" @click="submitForm()">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import {
  checkUserTrainTaskCount,
  deleteById, getTenants,
  getTrainTasksDetail,
  terminateById, trainTaskPublish,
  trainTasksQueryPage,
  trainTaskUpdateById
} from "@api/trainTask";
import {getDictListByDictType} from "@api/prompt";
import * as echarts from 'echarts';
import {deployTaskAdd, deleteById as deleteModelDeployById} from "@api/modelDeploy";

export default {
  name: "train",
  data() {
    return {
      allTasksList: [{id: 1}],
      getListLoading: false,
      getTrainTasksDetailLoading: false,
      trainTasksDialogVisible: false,
      //折线图的数据
      // 折线图的数据
      portInflowEChart: null,
      echartPortINflowData: {},
      trainTasksDetail: {
        deployTaskList: [
          {
            deployTargetName: '',
            status: ''
          }
        ]
      },
      queryParam: {
        currentPage: 1,
        pageSize: 20,
        name: null,
      },
      pageNum: 1,
      paginations: {
        total: 1, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      trainStatus: "",
      createTrainTasksButtonList: [],
      updateDialogVisible: false,
      releaseDialogVisible: false,
      modelReleaseRules: {
        publishTo: [
          {required: true, message: "请选择发布类型", trigger: "change"}
        ],
        modelName: [
          {required: true, message: "模型名称不能为空", trigger: "blur"}
        ],
        modelDesc: [
          {required: true, message: "模型描述不能为空", trigger: "blur"}
        ],
        modelParams: [
          {required: true, message: "出入参信息不能为空", trigger: "blur"}
        ],
      },
      modelReleaseReq: {
        id: '',
        modelDesc: '',
        modelName: '',
        modelParams: '',
        projectSpaceId: '',
        publishTo: '0',
      },
      modelReleaseProjectList: [],
      originalTaskName: '',
      updateTaskReq: {
        id: '',
        updateTaskName: ''
      },
      createTrainTasksButtonData: {
        trainingMethodList: [],
        trainingTargetsList: []
      },
      trainingType: 'method',
      listRefreshFlag: false,
      intervalId: null,
    };
  },
  created() {
    this.getList();
    this.getDictListByDictType();
  },
  watch: {
    'listRefreshFlag': function (newVal) {
      try {
        if (newVal) {
          this.startListRefresh(); // 开始刷新列表
        } else {
          this.stopListRefresh(); // 停止刷新列表
        }
      } catch (error) {
        console.error('Error in watch listRefreshFlag callback:', error);
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initLineChart();
    });
    if (this.listRefreshFlag) {
      this.startListRefresh();
    }
  },
  methods: {
    initLineChart() {
      this.portInflowEChart = echarts.init(document.getElementById("portInflowEChart"));
      // console.log("?????")

      this.echartPortINflowData = {
        xAxis: {
          // data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
          data: this.trainTasksDetail.trainingLossData.xAxisData,
          // startValue: '1',
          startValue: this.trainTasksDetail.trainingLossData.xAxisData[0],
        },
        series: {
          name: '数据',
          // data: [240, 241, 242, 243, 244, 245, 246, 247, 248, 249],
          data: this.trainTasksDetail.trainingLossData.seriesData,
        }
      }

// console.log("x轴起始点：",this.echartPortINflowData.xAxis.startValue)
      const option = {
        title: {
          text: '',
          left: '2%',
          top: '3%',
          textStyle: {
            fontFamily: 'SourceHanSansSC, SourceHanSansSC',
            fontSize: 16,
            fontWeight: '540',
            color: '#1C2748',
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['数据'],
          right: '3%',
          top: '3%',
        },
        grid: {
          left: '2%',
          right: '3%',
          bottom: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.echartPortINflowData.xAxis.data,
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
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {show: false},
          axisTick: {show: false},
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          },
          name: '单位：loss',
          label: {
            position: 'right', // 可选值有 'inside', 'outside', 'end', 'start', 'right', 'left'
            offset: [0, 20] // 第一个值表示水平偏移，第二个值表示垂直偏移
          }
        },
        dataZoom: [
          {
            startValue: this.echartPortINflowData.xAxis.startValue,
          }, {
            type: 'inside'
          }],
        series: [
          {
            name: this.echartPortINflowData.series.name,
            data: this.echartPortINflowData.series.data,
            type: 'line',
            color: '#0057FF'
          }
        ]
      };

      this.portInflowEChart.setOption(option, true);

    },
    getDictListByDictType() {
      let that = this;
      let params = {
        dictType: 'MODEL_TRAIN_TYPE',
      }
      getDictListByDictType(params).then((res) => {
        if (res.success) {
          that.createTrainTasksButtonData.trainingMethodList = res.data.filter(item => item.dictItemExtField2 === 'method');
          that.createTrainTasksButtonData.trainingTargetsList = res.data.filter(item => item.dictItemExtField2 === 'learn');
        } else {
          that.$message.error('查询失败');
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    getList() {
      let that = this;
      that.listRefreshFlag = false
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true;
      that.queryParam.pageNum = that.pageNum
      let params = {
        currentPage: that.queryParam.pageNum,
        pageSize: that.queryParam.pageSize,
        name: that.queryParam.name,
      };
      trainTasksQueryPage(params).then((res) => {
        if (res.success) {
          that.getListLoading = false
          that.allTasksList = res.data.rows
          that.allTasksList.forEach(taskItem => {
            switch (taskItem.status) {
              case "training":
                taskItem.toDeleteDisabled = true
                this.listRefreshFlag = true
                break;
              case "completed":
                taskItem.toDeleteDisabled = false
                break;
              case "failed":
                taskItem.toDeleteDisabled = false
                break;
              case "waiting":
                if (taskItem.waitCount === 0) {
                  taskItem.statusName = "准备中"
                } else {
                  taskItem.statusName = "排队中... (前面还有" + taskItem.waitCount + "个任务)"
                }
                taskItem.toDeleteDisabled = false
                this.listRefreshFlag = true
                break;
              default:
                taskItem.statusName = "其他"
                taskItem.toDeleteDisabled = true
                break;
            }
            if (taskItem.classify === "learn") {
              taskItem.reinforcementLearningFlag = true;
            }
          });
          that.paginations.total = Number(res.data.total)
          that.pageNum = 1
        } else {
          that.getListLoading = false
          that.$message.error('查询失败');
        }
      })
          .catch((err) => {
            that.getListLoading = false
            console.log(err, "失败");
          });
    },
    createTrainTasks(type, flag) {
      checkUserTrainTaskCount().then((res) => {
        if (res.success) {
          this.$router.push({
            path: '/train/parameters',
            query: {
              trainType: type.dictItemValue, method: type.dictItemExtField1,
              reinforcementLearningFlag: flag, classify: type.dictItemExtField2
            }
          })
        } else {
          this.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    detail(id) {
      let that = this;
      that.trainTasksDialogVisible = true;
      if (that.getTrainTasksDetailLoading) {
        return;
      }
      that.getTrainTasksDetailLoading = true;
      let params = {
        id: id,
      };
      getTrainTasksDetail(params)
          .then((res) => {
            if (res.success) {
              that.trainTasksDetail = res.data;
              that.trainTasksDialogVisible = true;
              let paramsArr = that.trainTasksDetail.param
              that.trainTasksDetail.params = paramsArr.flatMap((item, index) =>
                  index % 2 ? [] : [paramsArr.slice(index, index + 2)]
              );
              that.getTrainTasksDetailLoading = false
              let runtimeVal = that.trainTasksDetail.runtime
              if (runtimeVal != null && runtimeVal !== "") {
                that.trainTasksDetail.runtime = (Math.round(Number(runtimeVal) * 100) / 100).toFixed(2).toString()
              }
              let remainTimeVal = that.trainTasksDetail.remainTime
              if (remainTimeVal != null && remainTimeVal !== "") {
                that.trainTasksDetail.remainTime = (Math.round(Number(remainTimeVal) * 100) / 100).toFixed(2).toString()
              }
              that.initLineChart()
              that.getTrainTasksDetailLoading = false
            } else {
              that.$message.error("查询失败");
            }
          })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    deleteRow(id) {
      this.$confirm("训练任务删除后无法找回，是否要删除？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            let that = this;
            let params = {
              id: id,
            };
            deleteById(params)
                .then((res) => {
                  if (res.success) {
                    that.getList();
                    that.$message.success(res.message);
                  } else {
                    that.$message.error("删除失败");
                  }
                })
                .catch((err) => {
                  console.log(err, "失败");
                });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    terminate(id) {
      this.$confirm(
          "选择终止训练后，如需继续训练需要构建新的训练任务，并进行训练排队，是否要终止该任务？",
          "终止训练确认",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      )
          .then(() => {
            let that = this;
            let params = {
              id: id,
            };
            terminateById(params)
                .then((res) => {
                  if (res.success) {
                    that.getList();
                    that.$message.success(res.message);
                  } else {
                    that.$message.error("操作失败");
                  }
                })
                .catch((err) => {
                  console.log(err, "操作失败");
                });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
    },
    taskUpdate(row) {
      let that = this;
      that.originalTaskName = row.name
      that.updateDialogVisible = true
      that.updateTaskReq.id = row.id
    },
    updateData() {
      let that = this;
      that.updateDialogVisible = false
      let params = {
        id: that.updateTaskReq.id,
        name: that.updateTaskReq.updateTaskName
      }
      trainTaskUpdateById(params).then((res) => {
        if (res.success) {
          that.getList();
          that.$message.success(res.message);
        } else {
          that.$message.error('修改名称失败');
        }
      }).catch((err) => {
        console.log(err, "失败");
      });
    },
    modelReleaseDialog(row) {
      let that = this;
      that.modelReleaseReq.id = row.id;
      that.releaseDialogVisible = true;
    },
    changePublishType(){
      let that = this;
      if(that.modelReleaseReq.publishTo === '1'){
        getTenants().then((res) => {
          if (res.success) {
            that.modelReleaseProjectList = res.data.filter(item => item.status ==='normal');
          } else {
            that.$message.error(res.message);
          }
        })
            .catch((err) => {
              console.log(err, "失败");
            });
      }
    },
    submitForm() {
      this.$refs["modelReleaseForm"].validate(valid => {
        if (valid) {
          this.modelRelease();
        } else {
          return false;
        }
      });
    },
    modelRelease() {
      let that = this;
      let params = that.modelReleaseReq;
      trainTaskPublish(params).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
      that.releaseDialogVisible = false;
      that.getList();
    },
    routerToModelMonitoring(id, logType) {
      this.$router.push({name: 'modelMonitoring', query: {id: id, logType: logType}});
    },
    changeDeployStatus(row) {
      let deployed = row.deployTaskList && row.deployTaskList.length > 0
      const confirmText = deployed ? '是否取消部署？' : '是否部署？';
      this.$confirm(confirmText, "确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            let that = this;
            if (!deployed) {
              let params = {
                modelId: row.id
              }
              deployTaskAdd(params).then((res) => {
                if (res.success) {
                  that.getList();
                  that.$message.success('开始部署');
                } else {
                  that.$message.error(res.message);
                }
              })
                  .catch((err) => {
                    console.log(err, "失败");
                  });

            } else {
              let params = {
                deployTaskId: row.deployTaskList[0].id
              }
              deleteModelDeployById(params).then((res) => {
                if (res.success) {
                  that.getList();
                  that.$message.success("取消部署");
                } else {
                  that.$message.error(res.message);
                }
              })
                  .catch((err) => {
                    console.log(err, "失败");
                  });
            }
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消修改",
            });
          });
    },
    caseStatusColorFilter(row) {
      let col = null;
      let val = row.status
      switch (val) {
        case "training":
          col = "#216EFF";
          break;
        case "completed":
          col = "#1BBB27";
          break;
        case "failed":
          col = "#FF2121";
          break;
        case "waiting":
          col = "#6D7B94";
          break;
      }
      return col;
    },
    deployStatusTextFilter(row, flag) {
      let deployed = row.deployTaskList && row.deployTaskList.length > 0
      let deployStatus = !deployed ? 'non-deployed' : row.deployTaskList[0].status
      let col = null;
      if (deployStatus === "non-deployed") {
        col = "#0AA64F";
      } else if (deployStatus === "completed") {
        col = "#6B7492";
      } else if (deployStatus === "waiting") {
        col = "#E8850B";
        this.listRefreshFlag = true
      } else {
        col = "#02a0ff";
        this.listRefreshFlag = true
      }
      if (flag === 'color') {
        if (row.flag) {
          col = "#A07405";
        }
        return col;
      } else {
        return !deployed ? "(未部署)" : '(' + row.deployTaskList[0].statusName + ')'
      }
    },
    getDeployButtonType(row, flag) {
      let deployed = row.deployTaskList && row.deployTaskList.length > 0
      if (flag === 'type') {
        return !deployed ? 'text' : 'text'
      } else if (flag === 'text') {
        return !deployed ? '部署' : '取消部署'
      }
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getList();
    },
    handleSizeChange(val) {
      this.queryParam.pageSize = val;
      this.getList();
    },
    startListRefresh() {
      if (this.listRefreshFlag && !this.intervalId) {
        this.intervalId = setInterval(() => {
          this.getList();
        }, 30000);
      }
    },
    stopListRefresh() {
      if (this.intervalId) {
        clearInterval(this.intervalId);
        this.intervalId = null;
      }
    },
  },
  beforeRouteLeave(to, from, next) {
    this.stopListRefresh(); // 页面离开时停止刷新列表
    next();
  },
  beforeDestroy() {
    this.stopListRefresh(); // 组件销毁前停止刷新列表
  }
};
</script>
<style lang="less" scoped>
.createTrainTasks {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1c2748;
  margin-bottom: 24px;
}

.trainTasksBlock {
  width: 100%;
  background: #ffffff;
  padding: 20px;
  margin: 30px 0;
  box-shadow: 0px 2px 32px 0px rgba(196, 209, 223, 0.5);
  border-radius: 16px;

  .allTasksTableHead {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 18px;

    .blockblue {
      display: inline-block;
      width: 3px;
      height: 10px;
      background: #3470F8;
      margin-right: 7px;
    }

    .blockName {
      font-family: SourceHanSansSC, SourceHanSansSC;
      font-weight: 500;
      font-size: 16px;
      color: #1C2748;
    }

    .allTasksTableHeadSelect {
      width: 438px;
      position: relative;

      /deep/ .el-input__inner {
        padding-right: 40px;
      }

      .el-icon-search {
        font-size: 20px;
        color: #91959d;
        position: absolute;
        right: 10px;
        top: 10px;
        cursor: pointer;
      }
    }
  }

  .pager-bar {
    text-align: center;
    margin-top: 30px;
  }
}

.trainTasks {
  display: flex;

  .training-process-content {
    margin-left: auto;
    display: flex;
    align-items: center;
    flex-direction: row;
  }

  .createTrainTasksButton {
    margin-right: 20px;

    .el-button--primary {
      background-color: #FFFFFF;
      border: 1px solid #000000;
      color: rgba(17, 29, 50, 1);
      width: 120px;
    }
  }

  .createTrainTasksButton .el-button--primary:hover {
    background-color: #1B66FF;
    border-color: #1B66FF;
    color: #FFFFFF;
  }

  .reinforcement_learning_text {
    position: absolute;
    top: 10px;
    left: 190px;
    font-family: PingFangSC, PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: #6D7B94;
    line-height: 20px;
    text-align: left;
    font-style: normal;
  }

}

.flex-container {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

/deep/ .el-dialog {
  width: 540px;
  background: #ffffff;
  border-radius: 4px;
  height: auto;
}

/deep/ .el-dialog__header {
  width: 540px;
  height: 40px;
  line-height: 40px;
  background-color: #FFFFFF;
  border-radius: 4px 4px 0px 0px;
  padding: 10px 0 0 24px;
}

/deep/ .el-dialog__title {
  font-size: 16px;
  color: #1C2748;
}

/deep/ .el-dialog__headerbtn {
  top: auto;
  right: 24px;
}

/deep/ .el-dialog__body {
  padding: 22px 22px 8px;
}

.trainTasks-dialog {
  margin-bottom: 12px;
  border-radius: 4px;

  .trainTasks-dialog-left {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 400;
    font-size: 14px;
    color: #1c2748;
    line-height: 24px;
  }

  .trainTasks-dialog-table-row {
    text-align: center;

    .trainTasks-dialog-table-col-char {
      background-color: #f0f3fa;
      border-right: none !important;
      width: 60%;
    }

    .trainTasks-dialog-table-col-content {
      width: 40%;
    }

    .el-col-6 {
      line-height: 30px;
      border: 1px solid #E1E6EF;
      color: #1C2748;
    }
  }
}

.center_dot_status {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.trainTasks-border .el-col-6 {
  border-top: none !important;
  border-bottom: none !important;
}

/deep/ .el-button--primary {
  background-color: #1B66FF;
  border-color: #1B66FF;
  color: #FFFFFF;
}

/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}

.orange-line-with-arrow {
  margin: 5px;
}


.step_box {
  width: 220px;
  border-radius: 4px;
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  flex-direction: column;

  .img_content {

  }

  .step_text_content {
    font-family: SourceHanSansSC-Regular;
    font-size: 14px;
    font-weight: normal;
    overflow-wrap: break-word;
    text-align: center; /* 确保文本水平居中 */
    padding-right: 5px;
    display: flex;
    align-items: center; /* 垂直居中对齐 */
  }


}

.reinforcement_learning_tag_block {
  background-color: rgba(232, 239, 255, 1);
  border-radius: 4px;
  height: 18px;
  width: 47px;
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  justify-content: center; /* 水平居中对齐 */
  margin-left: 10px;

  .reinforcement_learning_tag_text {
    width: 40px;
    height: 16px;
    overflow-wrap: break-word;
    color: rgba(33, 110, 255, 1);
    font-size: 10px;
    text-align: center;
    white-space: nowrap;
    line-height: 16px;
  }
}

/deep/ .el-tabs__item {
  color: rgba(186, 189, 195, 1);
}

/deep/ .el-tabs__item.is-active {
  color: #1B66FF;
}

/deep/ .el-tabs__nav-wrap::after {
  display: none;
}

/deep/ .el-tabs__active-bar {
  background-color: #1B66FF;
}

/deep/ .el-form-item {
  margin: 10px 0;
}

/deep/ .el-radio__inner::after {
  width: 6px;
  height: 6px;
}

/deep/ .el-radio__input.is-checked .el-radio__inner {
  border-color: #1B66FF;
  background: #1B66FF;
}

/deep/ .el-radio__input.is-checked + .el-radio__label {
  color: #606266;
}

/deep/ .el-button--text {
  color: #216EFF;
}
</style>
