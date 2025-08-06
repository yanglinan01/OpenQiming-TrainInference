<template>
  <div>
    <div class="model-monitoring-container">
      <div class="model-monitoring-container-title">
        <span class="title-text">模型测评</span>
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
              <img :src="require(`@/assets/images/${item.icon}`)">
              <span style="user-select:none;margin-left: 10px">{{ item.label }}</span>
            </div>
          </div>
        </div>

      </div>
      <div class="model-monitoring-block" v-if="logButtonSelected=='1'">
        <el-form
            :model="primaryData"
            ref="donationForm"
            :rules="rules"
            class="donationForm"
        >
          <div class="echarts-head" >
            <el-form-item  label="模型选择:" class="modelmxxz">
              <el-select v-model="primaryData.modelId" filterable placeholder="请选择">
                <el-option
                    v-for="item in modelList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                    @click.native="modelSelectChange(item.trainTarget)">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="测试集选择:" prop="testSetSelect">
              <el-select v-model="primaryData.testSetSelect" filterable placeholder="请选择">
                <el-option
                    v-for="item in testSetSelectOptions"
                    :key="item.id"
                    :label="item.dataSetName"
                    :value="item.id">
                </el-option>
              </el-select>

            </el-form-item>

          </div>
          <div class="echarts-head" style="display: flex; height: 60px; margin-bottom: 30px;">
            <div class="block-box">
              <div class="block">
                <div class="demonstration-container">
                <span style="font-size: 14px; font-weight: 400;color: #111D32;opacity: 0.4;">temperture
                </span>
                  <el-popover
                      placement="top"
                      content="该参数用于调整大模型输出的随机性。数值越高模型生成结果多样更高。数值越低模型生成多样性降低，生成结果更加可预测保守"
                      width="300"
                      style="font-size: 14px; color: #b4b4b4;">
                    <i class="el-icon-question" slot="reference"></i>
                  </el-popover>
                </div>
                <div class="slider-container">
                  <el-slider
                      v-model="primaryData.temperature"
                      :min="0"
                      :max="5"
                      :step="0.1"
                      style="width: 80%;"
                  >
                  </el-slider>
                  <div class="value-box">
                    <el-input-number
                        v-model="primaryData.temperature"
                        :precision="1"
                        :step="0.1"
                        :controls="false"
                        @change="handleInput1(primaryData.temperature)"
                    ></el-input-number>
                  </div>
                </div>
                <p v-if="temperatureError" style="color: red;">{{ temperatureError }}</p>
              </div>
            </div>
            <div class="block-box" style="margin: 0 100px;">
              <div class="block">
                <div class="demonstration-container">
                <span style="font-size: 14px; font-weight: 400;color: #111D32;opacity: 0.4;">max_tokens</span>
                  <el-popover
                      placement="top"
                      content="单条数据输入输出最大长度，超过该长度的数据将会被截断(单位 token)"
                      width="300"
                      style="font-size: 14px; color: #b4b4b4;">
                    <i class="el-icon-question" slot="reference"></i>
                  </el-popover>
                </div>
                <div class="slider-container">
                  <el-slider
                      v-model="primaryData.maxTokens"
                      :min="512"
                      :max="4096"
                      :step=1
                      style="width: 80%;"
                  >
                  </el-slider>
                  <div class="value-box">
                    <el-input-number
                        v-model.number="primaryData.maxTokens"
                        :controls="false"
                        @change="handleInput2(primaryData.maxTokens)"
                    ></el-input-number>
                  </div>
                </div>
                <p v-if="maxTokensError" style="color: red;">{{ maxTokensError }}</p>
              </div>
            </div>
            <div style="flex: 1;text-align: right;">
              <el-button type="primary" @click="testSetEvaluate()" :loading="evaluateLoading"
                    style="margin-left:auto;width: 140px;border-radius: 4px;">开始评估
              </el-button>
            </div>
          </div>
          <div class="model-evaluation-content">
            <el-table :data="cardList" style="width: 100%" v-loading="getListLoading">
              <el-table-column prop="modelTaskName" label="模型名称" align="center">
              </el-table-column>
              <el-table-column prop="createDate" label="评估时间" align="center">
              </el-table-column>
              <el-table-column label="训练状态" align="center">
                <template slot-scope="scope">
                              <span
                                  class="cm-status"
                                  :style="{ color: caseStatusColorFilter(scope.row.statusDictMap.id) }"
                              >
                                {{ scope.row.statusDictMap.name }}
                              </span>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="是否用于强化学习" align="center">
                <template slot-scope="scope">
                  <span v-if="scope.row.type === '0'">是</span>
                  <span v-else-if="scope.row.type === '1'">否</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="260px">
                <template slot-scope="scope">
                  <el-button class="operatorButton" type="primary" size="mini" @click="details(scope.row)" :disabled="scope.row.statusDictMap.id === 'failed'">查看
                  </el-button>
                  <el-button class="operatorButton" type="primary" size="mini" @click="download(scope.row)" :disabled="scope.row.statusDictMap.id !== 'completed'">下载
                  </el-button>
                  <el-button class="operatorButton" type="primary" size="mini" @click="modelDelete(scope.row.id)">删除
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column label="人工评估结果上传" align="center">
                <template slot-scope="scope">
                  <div class="upload-container" :class="{ disabled: scope.row.statusDictMap.id !== 'completed' }">
                    <img
                        class="uploadResult"
                        src="../../assets/images/uploadResult.png"
                        @click="scope.row.statusDictMap.id === 'completed' && showUploadDialog(scope.row.id)"
                    >
                  </div>
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

        </el-form>
      </div>
     <div class="model-monitoring-block" v-if="logButtonSelected==2">
      <div style="display: flex;align-items: center;height: 60px;color: #1C2748;">
        <div style="display: flex;align-items: center;">
          <span style="width: 95px;">模型选择：</span>
          <el-select v-model="primaryData.modelId" filterable placeholder="请选择">
            <el-option
                v-for="item in modelList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                @click.native="modelSelectChange(item.trainTarget)">
            </el-option>
          </el-select>
        </div>
        <el-button type="primary" @click="startEvaluating()"
                    style="margin-left:auto;margin-right: 45px;max-height: 40px;width: 140px;border-radius: 4px;"
                    :disabled="evaluateStatus==='evaluating'||evaluateStatus==='completed'">
           开始评估
         </el-button>
      </div>
       <el-row style="display: flex">
         <div style="color: #1C2748;font-size: 14px; margin:10px 0">
           提供中文综合评测功能，评测数据集覆盖人文、社科、理工、其他专业四大方向共计52个学科，可有效考察大模型的知识和推理能力
         </div>

       </el-row>
       <div class="model-evaluation-content">
         <div style="margin: 10px 0;">
           <div style="width: 100%">
             <div class="c-eval-evaluate-table" v-for="item in CEVALEvaluateList" :key="item.id">
               <div class="c-eval-evaluate-table-item-info">
                 <p class="c-eval-evaluate-table-item-indicators">
                   {{ item.indicators }}
                 </p>
                 <p class="c-eval-evaluate-table-item-score">
                   {{ item.score ? item.score : '0.0' }}
                 </p>
                 <p class="c-eval-evaluate-table-item-desc">
                   {{ item.desc }}
                 </p>
               </div>
             </div>
           </div>
           <div style="height: 100% ; padding-top: 24px;margin-top: 14px;border-top: 1px solid #F0F0F0;">
             <div class="average-content" style="height: 100%">
               <p class="average-score-lable">平均分:</p>
               <p class="average-score-value">{{ CEVALEvaluateData.averageScore || '-' }} </p>
             </div>
           </div>
         </div>
         <div class="evaluate-loading" v-if="evaluateStatus==='evaluating'">
           <p style="justify-content: center; align-items: center; display: grid;padding-right: 5px;">
             <img src="@/assets/images/wait.png">
           </p>
           <p style="justify-content: center; align-items: center; display: grid;">评估中...</p>
         </div>
       </div>
     </div>

    </div>

    <el-dialog title="上传人工评估结果" :visible.sync="updateDialogVisible">
      <div style="text-align: left">
        <el-upload
            class="upload-demo"
            drag
            action="null"
            ref="upload"
            :file-list="fileList"
            :before-upload="null"
            :auto-upload="false"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text"><em>点击上传</em><br>人工评估结果</div>
        </el-upload>
      </div>

      <el-divider class="evaluate-dialog-divider"></el-divider>
      <div slot="footer">
        <el-button @click="uploadCancleResult" style="width: 76px;height: 32px;background: #F6F6F9;border-radius: 4px;padding: 0">取 消</el-button>
        <el-button type="primary" @click="uploadResult" style="width: 76px;height: 32px;background: #216EFF;border-radius: 4px;padding: 0">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {getDictListByDictType} from "@api/prompt";
import {getTrainTasksDetail, queryTrainTaskList} from "@api/trainTask";
import elementResizeDetectorMaker from "element-resize-detector";
import {
  addEvaluation,
  deleteById,
  exportTable,
  getLastEvalByTrainTaskId,
  queryPageList,
  trainTaskEvalAdd,
  upload
} from "@api/evaluate";
import {download, queryList as queryListDataSet} from "@api/dataSet";


export default {
  name: 'indexCopy',
  data() {
    return {
      logButtonSelected: '1',
      logButtonList:[
        {value: '1', label: '测试集评估', icon: 'testset-icon.png'},
        {value: '2', label: 'C-EVAL评估', icon: 'c-eval-icon.png'},
      ],
      primaryData: {
        trainTarget:'', //模型所处的地区
        modelId: '',
        type: '1', //强化学习的按钮初始设为未选中
        testSetSelect: '',
        //温度和多样性的默认值
        temperature: 0.8,
        maxTokens: 2048,
      },
      //温度多样性输入框的校验
      temperatureError: '',
      maxTokensError: '',
      isDisabled: false,
      rules: {
        modelId: [
          {required: true, message: "请选择基础模型", trigger: "change"}
        ],
        testSetSelect: [
          {required: true, message: "请选择测试集", trigger: "change"}
        ],
      },
      submitLoading: false,
      updateDialogVisible: false,
      fileList: [],
      selectedmodelId: null,
      isShow: true,
      modelList: [],
      dictList: [],
      methodList: [],
      options: [],
      trainType: '',
      method: '',
      CEVALEvaluateList: [
        {
          indicators: 'STEM:',
          score: '',
          desc: '科学、技术、工程和数学教育，包含计算机电气工程、化学、数学、物理等多个学科'
        },
        {
          indicators: 'Social science:',
          score: '',
          desc: '社会科学，包含政治、地理、教育学、经济学、工商管理等多个学科'
        },
        {
          indicators: 'Humanity:',
          score: '',
          desc: '人文科学，包含法律、艺术、逻辑学、语文、历史等多个学科',
        },
        {
          indicators: 'other:',
          score: '',
          desc: '环境、消防、税务、体育、医学等多个学科'
        },
      ],
      CEVALEvaluateData: {
        averageScore: ''
      },
      evaluateStatus: false,
      //数据集
      getListLoading: false,
      evaluateLoading: false,
      testSetSelectOptions: [],
      evaluateRefreshFlag: false,
      queryParam: {
        currentPage: 1,
        pageSize: 10,
      },
      cardList: [],
      paginations: {
        total: 10, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },

    }
  },
  created() {
    this.trainType = this.$route.query.trainType
    this.method = this.$route.query.method
    this.queryTrainTaskList()
    this.testSetDownList()

  },
  watch: {
    'evaluateRefreshFlag': function (newVal) {
      if (newVal) {
        this.startListRefresh(); // 开始刷新列表
      } else {
        this.stopListRefresh(); // 停止刷新列表
      }
    }
  },

  methods: {
    /**
     * 输入框输入时处理函数，确保数值在小数点后一位并处理错误提示。
     */
    handleInputTem(event, targetProperty, errorMessageProperty) {
      const rawValue = event;
      let newValue = parseFloat(rawValue);
      // 确保newValue是一个有限数字且非空
      if (isFinite(newValue) && !isNaN(newValue) && newValue >= 0 && newValue <= 5) {
        // 检查是否为0或1的特殊情况，直接保留
        if (newValue === 0 || newValue === 5) {
          this.primaryData.temperature = newValue; // 直接赋值，不进行toFixed操作
          this[errorMessageProperty] = ''; // 清除错误信息
          return; // 结束函数，避免不必要的处理
        }
        // 检查小数点后是否有超过1位的小数
        const decimalPart = newValue.toString().split('.')[1];
        if (decimalPart && decimalPart.length > 1) {
          // this[errorMessageProperty] = '*请输入最多一位小数的数字';
          this[errorMessageProperty] = '*请输入最多一位小数的数字';
        } else {
          // 对于其他情况，保留一位小数
          newValue = parseFloat(rawValue);
          this.primaryData.temperature = newValue;
          this[errorMessageProperty] = ''; // 清除错误信息
        }
      } else {
        // 输入值无效
        // this[errorMessageProperty] = '请输入0到1之间，最多一位小数的数字';
        this[errorMessageProperty] = '*请不要超过参数范围(0,5)';
      }
    },
    handleInputMaxtoken(event, targetProperty, errorMessageProperty) {
      let newValue = event;
      // 确保newValue是一个有限数字且非空
      if (isFinite(newValue) && !isNaN(newValue) && newValue >= 512 && newValue <= 4096) {
        // 检查是否为0或1的特殊情况，直接保留
        if (newValue === 512 || newValue === 4096) {
          this.primaryData.maxTokens = newValue; // 直接赋值，不进行toFixed操作
          this[errorMessageProperty] = ''; // 清除错误信息
          return; // 结束函数，避免不必要的处理
        }
        // 检查小数点后是否有小数
        const decimalPart = newValue.toString().split('.')[1];
        if (decimalPart && decimalPart.length > 0) {
          // this[errorMessageProperty] = '*请输入最多一位小数的数字';
          this[errorMessageProperty] = '*请输入整数';
        } else {
          // 对于其他情况，保留一位小数
          // newValue = parseFloat(event);
          this.primaryData.maxTokens = newValue;
          this[errorMessageProperty] = ''; // 清除错误信息
        }
      } else {
        // 输入值无效
        this[errorMessageProperty] = '*请不要超过参数范围(512,4096)';
      }
    },
    handleInput1(event) {
      this.handleInputTem(event, 'temperature', 'temperatureError');
    },
    handleInput2(event) {
      this.handleInputMaxtoken(event, 'maxTokens', 'maxTokensError');
    },
    logButtonSelect(value){
      this.logButtonSelected = value
    },
    queryTrainTaskList() {
      let that = this;
      let params = {
        status: 'completed'
      }
      queryTrainTaskList(params).then((res) => {
        if (res.success) {
          that.modelList = res.data
          that.primaryData.modelId = that.modelList[0].id
          this.modelSelectChange(that.modelList[0].trainTarget)
          this.getLastEvalByTrainTaskId()
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    startEvaluating() {
      let that = this;
      let params = {
        trainTaskId: that.primaryData.modelId,
      };
      trainTaskEvalAdd(params).then((res) => {
        if (res.success) {
          that.$message.success('开始评估成功');
          that.getLastEvalByTrainTaskId();
        } else {
          that.$message.error('开始评估失败');
        }
      }).catch((err) => {
        console.log(err, "失败");
      });
    },
    getLastEvalByTrainTaskId() {
      let that = this;
      let params = {
        trainTaskId: that.primaryData.modelId
      };
      getLastEvalByTrainTaskId(params).then((res) => {
        if (res.success) {
          that.evaluateStatus = res.data.status
          that.CEVALEvaluateList[0].score = res.data.stem
          that.CEVALEvaluateList[1].score = res.data.socialScience
          that.CEVALEvaluateList[2].score = res.data.humanity
          that.CEVALEvaluateList[3].score = res.data.other
          that.CEVALEvaluateData.averageScore = res.data.average
        } else {
          that.$message.error('查询失败');
        }
      }).catch((err) => {
        console.log(err, "失败");
      });
    },
    modelSelectChange(value) {
      this.primaryData.trainTarget=value
      this.testSetList()
      this.getLastEvalByTrainTaskId()
    },
    testSetDownList() {
      let params = {
        belong: '2',
        dataType: '1',
        setType: '2'
      }
      queryListDataSet(params).then(res => {
        if (res.success) {
          this.testSetSelectOptions = res.data
          // this.$message.success(res.message);
        } else {
          console.error('获取测试集选择的下拉框失败', res.message);
          // this.$message.error(res.message);
        }
      }).catch(error => {
        console.error('获取测试集选择的下拉框失败', error);
      });
    },
    //测试集列表内容获取
    testSetList() {
      let that = this;
      that.evaluateRefreshFlag=false
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true
      let params = {
        filterMap: {
          //that.primaryData.modelId "1808354026644094976"
          modelTaskId: that.primaryData.modelId
        },
        pageParam: {
          pageSize: that.queryParam.pageSize,
          pageNum: that.queryParam.currentPage,
        },
        withDict: true
      }

      queryPageList(params).then(res => {
        if (res.success) {
          that.cardList = res.data.rows
          that.paginations.total = Number(res.data.total)
          that.getListLoading = false
          that.cardList.forEach(taskItem => {
            switch (taskItem.statusDictMap.id) {
              case "evaluating":
                this.evaluateRefreshFlag = true
                break;
              case "waiting":
                this.evaluateRefreshFlag = true
                break;
              default:
                break;
            }
          });
          // that.$message.success("获取测试数据集评估列表成功");
        } else {
          that.getListLoading = false
          // that.$message.error("获取测试数据集评估列表失败",res.message);
          console.error("获取测试数据集评估列表失败", res.message);
        }
      }).catch(error => {
        that.getListLoading = false
        console.error('测试集列表报错', error);
      });
    },
    startListRefresh() {
      if (this.evaluateRefreshFlag) {
        this.intervalId = setInterval(() => {
          this.testSetList();
        }, 30000); // 每30秒执行一次
      }
    },
    stopListRefresh() {
      if (this.intervalId) {
        clearInterval(this.intervalId); // 清除定时器
        this.intervalId = null;
      }
    },
    //测试集模型评估查看
    details(row) {
        this.$router.push({path: '/evaluating/evaluaDetail', query: {establishId: row.id}})
    },
    async download(item) {
      try {
        // 发起请求获取文件
        const response = await exportTable({id: item.id});
        const filename = item.modelTaskName + '_' + item.dataSetName + '.xls';

        const blob = new Blob([response], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
        // 创建下载链接
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename);
        document.body.appendChild(link);
        // 触发下载
        link.click();
        // 清理资源
        window.URL.revokeObjectURL(url);
        document.body.removeChild(link);
        //提示用户
        this.$Message.success("下载成功");
      } catch (error) {
        // 处理下载失败的情况
        console.error('下载文件时出错：', error);
      }
    },
    modelDelete(value) {
      this.$confirm('确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        confirmButtonClass: 'buttonBg'
      }).then(() => {
        let that = this;
        let params = {
          id: value,
        }
        //测试集删除
        deleteById(params).then((res) => {
          if (res.success) {
            that.$message.success("删除成功");
            //刷新列表
            that.testSetList()
          } else {
            that.$message.error('删除失败');
          }

        })
            .catch((err) => {
              console.log(err, "失败");
            });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    showUploadDialog(id) {
      this.updateDialogVisible = true
      this.selectedmodelId = id
    },
    uploadResult() {
      const file = this.$refs.upload.uploadFiles[0].raw;
      if (file) {
        this.upload(file, this.selectedmodelId);
        this.updateDialogVisible = false;
        this.fileList = [];
        this.selectedmodelId=null;
      } else {
        this.$message.warning('请选择文件');
      }
    },
    uploadCancleResult(){
      this.updateDialogVisible = false;
      this.fileList = [];
      this.selectedmodelId=null;
      this.$refs.upload.clearFiles();
    },
    async upload(file,selectedmodelId) {
      // 这里是上传逻辑
      try {
        var formData = new FormData();
        formData.append("uploadFile", file);
        formData.append("id",selectedmodelId);


        const res = await upload(formData);
        if (res.success) {
          this.$message.success("上传成功");
        } else {
          this.$message.error(res.message);
        }
      } catch (err) {
        this.$message.error("上传文件时出错");

      }
    },
    // 测试集评估
    testSetEvaluate() {
      let that = this;
      that.$refs["donationForm"].validate(async valid => {
        if (valid) {
          if (that.evaluateLoading) {
            return;
          }
          that.evaluateLoading = true
          let params = {
            modelTaskId: that.primaryData.modelId,
            dataSetId: that.primaryData.testSetSelect,
            maxTokens: that.primaryData.maxTokens,
            temperature:  that.primaryData.temperature,
            type:that.primaryData.type
          }
          //测试集评估方法
          addEvaluation(params).then((res) => {
            if (res.success) {
              that.evaluateLoading = false
              that.$message.success('评估成功');
            } else {
              that.evaluateLoading = false
              that.$message.error('评估失败');
            }
            //刷新列表
            that.testSetList()
          })
              .catch((err) => {
                that.evaluateLoading = false
                console.log(err, "失败");
              });
        } else {
          return false;
        }
      });

    },
    caseStatusColorFilter(val) {
      let col = null;
      switch (val) {
        case "evaluating":
          col = "#E8850B";
          break;
        case "completed":
          col = "#0AA64F";
          break;
        case "failed":
          col = "#6D7B94";
          break;
        case "waiting":
          col = "#3470F8";
          break;
      }
      return col;
    },
    handleCurrentChange(val) {
      this.queryParam.currentPage = val;
      this.testSetList();
    },
    handleSizeChange(val) {
      this.queryParam.pageSize = val;
      this.testSetList();
    },
  },
  beforeRouteLeave(to, from, next) {
    this.stopListRefresh(); // 页面离开时停止刷新列表
    next();
  },
  beforeDestroy() {
    this.stopListRefresh(); // 组件销毁前停止刷新列表
  },


};
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
  height: auto;
  width: 100%;
  background: #FFFFFF;
  box-shadow: 0px 2px 32px 0px rgba(196, 209, 223, 0.5);
  border-radius:0px 0px  16px 16px ;
  margin-bottom: 20px;
  display: block;
  padding: 30px;
}
//.model-monitoring-tabs-button-block {
//  width: 100%;
//  background: #E6F0FD;
//  border-radius: 16px 16px 0px 0px;
//  color: #909BAA;
//}
.block-button-item{
  width: 50%;
  padding: 12px;
  cursor: pointer;
}
.block-button-item.selected{
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

.block-button-item.disabled {
  cursor: not-allowed;
  opacity: 0.5;
  pointer-events: none;
}
.echarts-head {
  width: 100%;
  display: flex;
  height: 60px;
  align-items: center;
  justify-content: space-between;
  padding: 0 10px 0 10px;
}
.slider-container {
  display: flex;
  align-items: center;
}
.block-box {
  flex: 1;

  .block {
    width: 100%;
    padding: 0;
    height:38px;

    .value-box {
      font-size: 14px;
      float: right;
      width: 20%;
      display: flex; // 启用Flexbox布局
      justify-content: center; // 水平居中
      align-items: center; // 垂直居中
      height: 38px; // 确保有定高以便垂直居中生效
      margin-left: 20px;
      padding: 0;
      background: #FFFFFF;
      border-radius: 4px;
      border: 1px solid #D8DCE6;

      /deep/ .el-input__inner {
        padding: 0;
        background-color: transparent;
        border: none; /* 确保无边框，使用外部边框 */
        box-sizing: border-box;
        color: #606266;
        text-align: center;
        font-size: 16px;
      }
    }

    /* 移除 el-input 的默认边框，使用外部容器的边框 */

    .value-box .el-input__inner {
      border: none;
      background-color: transparent; /* 或与.value-box相同的背景色，确保背景统一 */
    }
  }

  .question-icon {
    width: 14px; /* 根据图片大小调整 */
    height: 14px; /* 根据图片大小调整 */
    cursor: pointer; /* 鼠标悬停时显示指针 */
  }

  .el-dropdown-link {
    cursor: pointer;
    color: #216EFF; /* 默认灰色 */
    font-size: 14px;
    transition: color 0.3s;
  }

  .el-dropdown-link.expanded {
    color: #216EFF; /* 点击后变为蓝色 */
  }
}
.model-evaluation-content {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;

  .model-evaluation-content-right {
    width: 50%;
    background: #F8F8F8;
    border-radius: 4px;
    padding: 20px;

    .text-indent {

    }
  }



    .c-eval-evaluate-table-item-info {
      display: flex;
      align-items: center;
      padding: 15px 0;
      color: #1C2748;

      .c-eval-evaluate-table-item-indicators {
        width: 10%;
        align-items: center;
        font-family: SourceHanSansSC, SourceHanSansSC;
      }

      .c-eval-evaluate-table-item-score {
        margin: 0 5% 0 10px;
        text-align: left;
        color: #1C2748;
        width: 260px;
        height: 38px;
        line-height: 38px;
        background: #FFFFFF;
        border-radius: 4px;
        border: 1px solid #D8DCE6;
        padding: 0 10px;
      }

      .c-eval-evaluate-table-item-desc {
        width: 50%;
        margin: 0 10px;
        align-items: center;
      }
    }

  .average-content {
    display: flex;

    .average-score-lable {
      justify-content: center;
      align-items: center;
      display: grid;
      font-size: 16px;
      color: #111D32;
      padding-right: 12px;
    }

    .average-score-value {
      font-family: SourceHanSansCN, SourceHanSansCN;
      font-weight: bold;
      font-size: 20px;
      color: #1C2748;
      line-height: 30px;
      text-align: left;
      font-style: normal;
      background: linear-gradient(90deg, #471CFF 0%, #216EFF 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }

  .testSetSelect-pullDown {
    margin-bottom: 22px;
    height: 40px;
    margin-top: 10px;
    margin-left: -60px;
    display: flex;
    align-items: center;
  }

  .operatorButton {
    width: 60px;
    height: 28px;
    border-radius: 4px;
    font-size: 14px;
    padding: 0;
    margin: 0 10px;
  }
  .el-button.is-disabled {
    background: #d0d5e0 !important;
    border-color: #d0d5e0 !important;
    color: #fff !important;
  }
  .pager-bar {
    margin-top: 57px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}

.parameTable {
  width: 948px;
  height: 254px;
  background: #F7F8F9;
  border-radius: 2px;
  padding: 0 16px;
  overflow: hidden;
  position: relative;
}

.upload-container {
  position: relative;
  display: inline-block;
}

.upload-container.disabled {
  cursor: not-allowed;
}

.upload-container.disabled .uploadResult {
  opacity: 0.5; /* 图片置灰 */
}
//
//.upload-container.disabled::after {
//  content: '';
//  position: absolute;
//  top: 0;
//  left: 0;
//  right: 0;
//  bottom: 0;
//  background-color: rgba(0, 0, 0, 0.3); /* 灰色蒙层 */
//}

/deep/ .el-form-item__label {
  font-size: 14px;
  color: #1C2748;
  font-weight: 500;
  width: 128px;
}

/deep/ .el-button--primary {
  background-color: #216EFF;
  border-color: #216EFF;
}


/deep/ .changeData .el-input__inner {
  padding-left: 42px;
}

/deep/ .el-button--default {
  background: #EBEBEB;
  border: 1px solid #EBEBEB;
  color: #535456;
}

/deep/ .el-form-item {
  margin-bottom: 0;
  display: flex;
  flex: 1;
}

/deep/ .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
}

/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}


/deep/ .el-dialog {
  width: 432px;
  background: #ffffff;
  border-radius: 4px;
}

/deep/ .el-dialog__footer {
  height: 50px;
}

/deep/ .el-dialog__header {
  width: 100%;
  height: 40px;
  line-height: 40px;
  background: #f0f3fa;
  border-radius: 4px;
  padding: 0 0 0 24px;
}

/deep/ .el-dialog__title {
  font-size: 16px;
  color: #1C2748;
}

/deep/ .el-dialog__headerbtn {
  top: 0;
  right: 24px;
}

/deep/ .el-dialog__body {
  padding: 24px 24px 0;
  border-top: 1px solid #EFEDED;
}
.evaluate-dialog-divider {
  display: block;
  height: 1px;
  width: 432px;
  margin: 0 0 0 -24px;

}
.upload-demo {
  width: 384px;
  height: 100%;
  padding-bottom: 24px;
}
/deep/ .el-upload-dragger {
  width: 384px;
}
/deep/ .el-form-item__content{
  width: 100%;
}
/deep/ .el-select{
  width: 100%;
}
/deep/ .modelmxxz .el-form-item__label{
  width: 100px;
}
.evaluate-loading{
  position: relative;
  width: 240px;
  height: 40px;
  background: #E8EFFF;
  border-radius: 8px;
  border: 1px solid #BCD4FF;
  display: flex;
  align-items: center;
  padding: 9px 12px;
  font-size: 14px;
  color: rgba(0,0,0,0.88);
  top: -116px;
  left: 33%;
}
</style>
