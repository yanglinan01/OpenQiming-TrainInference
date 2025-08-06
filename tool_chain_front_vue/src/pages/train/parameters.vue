<template>
  <div>
    <div class="myAbility" v-if="!reinforcementLearningFlag">创建训练任务</div>
    <div class="myAbility" v-if="reinforcementLearningFlag">创建强化学习训练任务</div>
    <div style="min-width: 1200px;">
      <el-form
          :model="primaryData"
          ref="donationForm"
          :rules="rules"
          class="donationForm"
          label-width="176px"
      >
        <div class="blockBg">
          <el-form-item label="任务名称" prop="name" style="width: 1127px;">
            <el-input v-model="primaryData.name" placeholder="请输入" show-word-limit></el-input>
            <div style="color: #84868c;font-size: 12px;">支持中英文、数字、下划线(_)，2-15个字符，不能以_开头</div>
          </el-form-item>
          <el-form-item :label="reinforcementLearningFlag ? '模型选择' : '基础模型选择'" prop="baseModelId"
                        style="width: 1127px;">
            <el-select v-model="primaryData.baseModelId" v-if="!reinforcementLearningFlag" filterable
                       placeholder="请选择"
                       style="width: 951px;">
              <el-option
                  v-for="item in modelList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  @click.native="handleModelIdChange(item)">
              </el-option>
            </el-select>
            <el-select v-model="primaryData.toReinforceLearningModelId" v-if="reinforcementLearningFlag" filterable
                       placeholder="请选择"
                       style="width: 951px;">
              <el-option
                  v-for="item in modelList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  @click.native="handleModelIdChange(item)">
              </el-option>
            </el-select>
            <div style="color: #84868c;font-size: 12px;" v-if="trainType === 'IR'">Qiming 7B可满足意图识别训练需求</div>
          </el-form-item>
        </div>
        <div class="blockBg">
          <p class="parameName">超参配置</p>
          <el-form-item label="训练方法" prop="method">
            <el-radio-group :disabled=true v-model="method" style="display: flex;align-items: center;height: 40px;">
              <el-radio :label="item.dictItemValue" v-for="(item, index) in methodList" :key="index">
                {{ item.dictItemLabel }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="超参配置" prop="param">
            <div style="color: rgba(28,39,72,0.7);font-size: 14px;">
              通过调节不同参数配置影响模型的调优过程和效果，不同参数配置训练结果不同，建议选择默认配置
            </div>
<!--            <div :class="{'parameHeight': !isShow}" class="parameTable">-->
            <div  class="parameTable">
              <el-table :data="primaryData.param" fit style="width: 100%">
                <el-table-column align="center" label="超参数" width="150">
                  <template slot-scope="scope">
                    <span style="margin-left: 10px">{{ scope.row.displayName }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="数值" width="270">
                  <template slot-scope="scope">
                    <el-input-number v-model="scope.row.defaultValue"
                                     controls-position="right"
                                     :step="scope.row.step"
                                     @change="changeNumber(scope.row)"
                                     v-if="!scope.row.componentHiddenFlag"
                                     :disabled="reinforcementLearningFlag &&scope.row.displayName==='epochs'"
                                     style="margin-left: 10px ;width:150px "
                    >
                    </el-input-number>
                    <el-select v-model="scope.row.defaultValue" placeholder="请选择"
                               v-if="scope.row.componentHiddenFlag"
                               style="margin-left: 10px ;width:150px"
                    >
                      <el-option v-for="(item, index) in scope.row.checkValue" :key="item" :label="item"
                                 :value="item"></el-option>
                    </el-select>
                    <p v-if="(!scope.row.componentHiddenFlag) && (isKeyUp && scope.row.displayName=='学习率')"
                       class="KeyUpcss">{{ KeyUpHtml }}</p>
                    <p v-if="(!scope.row.componentHiddenFlag) && (isKeyUp1 && scope.row.displayName=='迭代轮次')"
                       class="KeyUpcss">{{ KeyUpHtml1 }}</p>
                  </template>
                </el-table-column>
                <el-table-column label="说明">
                  <template slot-scope="scope">
                    <span style="margin-left: 10px">{{ scope.row.description }}</span>
                  </template>
                </el-table-column>
              </el-table>
<!--              <div class="parameTableImg" :class="{'paramepo': !isShow}">-->
<!--                <img src="../../assets/images/parameTable1.png" alt="" v-if="isShow" @click="launch()">-->
<!--                <img src="../../assets/images/parameTable2.png" alt="" v-if="!isShow" @click="launch()">-->
<!--              </div>-->
            </div>
          </el-form-item>
        </div>
        <div class="blockBg">
          <p class="parameName">数据配置</p>
          <div class="parameText">通过选择合适的数据集进行模型训练</div>
          <el-form-item label="数据类型" prop="dataType">
            <el-radio-group v-model="primaryData.dataType" @change="handleRuleTypeChange"
                            style="display: flex;align-items: center;height: 40px;">
              <el-radio :label="1" v-if="!reinforcementLearningFlag">系统数据集</el-radio>
              <el-radio :label="2">我的数据集</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="数据集选择" prop="dataSetId">
            <el-select v-model="primaryData.dataSetId" placeholder="请选择数据集"
                       style="width: 951px;position: relative;" filterable class="changeData">
              <el-option
                  v-for="item in options"
                  :key="item.id"
                  :label="item.dataSetName"
                  :value="item.id">
              </el-option>
            </el-select>
            <i class="el-input__icon el-icon-search" style="position: absolute;left: 12px;top: 0;color: #A4A9B6;"></i>
          </el-form-item>
        </div>
        <div class="blockBg">
          <p class="parameName">训练集群信息</p>
          <div v-loading="clusterDetailLoading">
            <el-row class="trainTasks-dialog">
              <el-col :span="9" class="trainTasks-dialog-left"
              ><span>集群名称：{{ Sourceoptions.clusterName }}</span></el-col
              >
              <el-col :span="9" class="trainTasks-dialog-left"
              ><span>集群省份：{{ Sourceoptions.province }}</span></el-col
              >
            </el-row>
            <el-row class="trainTasks-dialog">
              <el-col :span="9" class="trainTasks-dialog-left"
              ><span>CPU利用率：{{ Sourceoptions.cpuUsage }}</span></el-col
              >
              <el-col :span="9" class="trainTasks-dialog-left"
              ><span>内存使用率：{{ Sourceoptions.memoryUsage }}</span></el-col
              >
            </el-row>
            <el-row class="trainTasks-dialog">
              <el-col :span="9" class="trainTasks-dialog-left"
              ><span>训练服务器剩余量：{{ Sourceoptions.trainServerAvail }}</span></el-col
              >
              <el-col :span="9" class="trainTasks-dialog-left"
              ><span>推理服务器剩余量：{{ Sourceoptions.inferenceServerAvail }}</span></el-col
              >
            </el-row>
          </div>
        </div>
        <div class="blockBgButton">
          <div style="margin: 24px 0;display: flex;justify-content: center;">
            <el-button @click="quxiao">取消</el-button>
            <el-button type="primary" :loading="submitLoading" @click="submit">开始训练</el-button>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
import {getDictListByDictType} from "@api/prompt";
import {
  clusterDetail,
  queryParamListByModelId,
  queryTrainModelList,
  queryTrainTaskList,
  trainTaskAdd
} from "@api/trainTask";
import {queryList} from "@api/dataSet";

export default {
  name: 'parameters',
  data() {
    return {
      isKeyUp: false,
      isKeyUp1: false,
      clusterDetailLoading: false,
      clusterCode: '',
      Sourceoptions: [],
      KeyUpHtml: '',
      KeyUpHtml1: '',
      primaryData: {
        name: '',
        baseModelId: '',
        toReinforceLearningModelId: '',
        method: this.$route.query.method,
        param: [],
        dataType: 1,
        dataSetId: ''
      },
      rules: {
        name: [
          {
            required: true,
            trigger: "blur",
            validator: (rule, value, callback) => {
              let reg = /^(?!_)[0-9a-zA-Z_\u4e00-\u9fa5]{2,15}$/
              if (!value) {
                return callback(new Error("任务名称不能为空"))
              }
              if (!reg.test(value)) {
                return callback(new Error("支持中英文、数字、下划线(_)，2-15个字符，不能以_开头"))
              }
              return callback()
            },
          }
        ],
        baseModelId: [
          {required: true, message: "请选择基础模型", trigger: "change"}
        ],
        method: [
          {required: true, message: "请选择训练方法", trigger: "change"}
        ],
        param: [
          {required: true, message: "超参配置不能为空", trigger: "blur"}
        ],
        dataType: [
          {required: true, message: "请选择数据类型", trigger: "change"}
        ],
        dataSetId: [
          {required: true, message: "请选择数据集", trigger: "change"}
        ],
      },
      submitLoading: false,
      isShow: true,
      modelList: [],
      dictList: [],
      methodList: [],
      options: [],
      trainType: '',
      method: '',
      reinforcementLearningFlag: false,
      classify: 'method',
    }
  },
  created() {
    this.trainType = this.$route.query.trainType
    this.method = this.$route.query.method
    this.reinforcementLearningFlag = this.$route.query.reinforcementLearningFlag === 'true'
    this.classify = this.$route.query.classify
    if (this.reinforcementLearningFlag) {
      this.primaryData.dataType = 2
    }else{
      this.queryList()
    }
    this.queryModelList()

  },
  methods: {
    changeNumber(rew) {
      if (rew.displayName == '学习率') {
        if (rew.defaultValue < Number(rew.checkValue[0]) || rew.defaultValue > Number(rew.checkValue[1])) {
          this.isKeyUp = true
          this.KeyUpHtml = '*请不要超过参数范围（' + rew.checkValue[0] + '，' + rew.checkValue[1] + '）'
        } else {
          this.isKeyUp = false
        }
      } else if (rew.displayName == '迭代轮次') {
        if (rew.defaultValue < Number(rew.checkValue[0]) || rew.defaultValue > Number(rew.checkValue[1])) {
          this.isKeyUp1 = true
          this.KeyUpHtml1 = '*请不要超过参数范围（' + rew.checkValue[0] + '，' + rew.checkValue[1] + '）'
        } else {
          this.isKeyUp1 = false
        }
      } else {
        this.isKeyUp = false
        this.isKeyUp1 = false
      }
    },
    computedDefaultValue(row) {
      if (row.displayName === 'epochs') {
        return 1;
      }
      return row.defaultValue;
    },
    queryModelList() {
      let that = this;
      let params
      if (!that.reinforcementLearningFlag) {
        params = {
          trainable: '0',
          userScope: '0',
          trainType: that.$route.query.trainType
        }
        queryTrainModelList(params).then((res) => {
          if (res.success) {
            that.modelList = res.data
            that.primaryData.baseModelId = that.modelList[0].id
            that.clusterCode = that.modelList[0].trainTarget
            that.clusterDetail()
            that.getDictListByDictType(false)
          }
        })
            .catch((err) => {
              console.log(err, "失败");
            });
      } else {
        params = {
          classify: 'method',
          method: 2
        }
        queryTrainTaskList(params).then((res) => {
          if (res.success) {
            that.modelList = res.data
            that.primaryData.baseModelId = that.modelList[0].modelId
            that.primaryData.toReinforceLearningModelId = that.modelList[0].id
            that.clusterCode = that.modelList[0].trainTarget
            that.clusterDetail()
            that.getDictListByDictType(true)
            that.queryList()
          }
        })
            .catch((err) => {
              console.log(err, "失败");
            });
      }

    },
    getDictListByDictType(reinforcementLearningFlag) {
      let that = this;
      that.methodList = []
      let params = {
        dictType: 'TRAIN_TASK_METHOD',
      }
      getDictListByDictType(params).then((res) => {
        if (res.success) {
          that.dictList = res.data
          that.methodList = that.dictList
          // for (let i = 0; i < that.dictList.length; i++) {
          //   if (that.modelList[0].trainTypeIdArray.includes(that.dictList[i].dictItemValue)) {
          //     that.methodList.push(that.dictList[i])
          //   }
          // }
          // if (that.trainType === 'IR') {
          //   that.primaryData.method = that.methodList[1].dictItemValue
          // } else {
          //   that.primaryData.method = that.methodList[0].dictItemValue
          // }
          that.queryParamListByModelId()
        } else {
          that.$message.error('查询失败');
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    queryParamListByModelId() {
      let that = this;
      that.primaryData.param = []
      let params = {
        trainType: that.trainType,
        trainMethod: that.method
      }
      if (!this.reinforcementLearningFlag) {
        params.modelId = that.primaryData.baseModelId
      } else {
        params.type = 'normal'
      }
      queryParamListByModelId(params).then((res) => {
        if (res.success) {
          that.primaryData.param = res.data
          that.primaryData.param.forEach(item => {
            item.componentHiddenFlag = item.checkType === "choice";
            if (item.displayName === 'epochs') {
              item.defaultValue = 1
            }
          })
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    queryList() {
      let that = this;
      if (!that.reinforcementLearningFlag) {
        let params = {
          belong: that.primaryData.dataType,
          setType: '1'
        }
        queryList(params).then((res) => {
          if (res.success) {
            that.options = res.data
          }
        })
            .catch((err) => {
              console.log(err, "失败");
            });
      } else {
        let params = {
          belong: that.primaryData.dataType,
          enhancedTrainTaskId: that.primaryData.toReinforceLearningModelId,
          setType: '3'
        }
        queryList(params).then((res) => {
          if (res.success) {
            that.options = res.data
            that.primaryData.dataSetId = that.options[0].id
          }
        })
            .catch((err) => {
              console.log(err, "失败");
            });
      }

    },
    clusterDetail() {
      let that = this;
      if (that.clusterDetailLoading) {
        return;
      }
      that.clusterDetailLoading = true;
      let params = {
        clusterCode: that.clusterCode
      }
      clusterDetail(params).then((res) => {
        if (res.success) {
          that.Sourceoptions = res.data[0]
          that.clusterDetailLoading = false
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    handleRuleTypeChange() {
      this.queryList()
      this.primaryData.dataSetId = ''
    },
    handleModelIdChange(item) {
      this.queryParamListByModelId()
      if(this.reinforcementLearningFlag){
        this.primaryData.baseModelId = item.modelId
      }
      this.primaryData.dataSetId = ''
      //基础模型切换，调用训练集群接口
      this.clusterCode = item.trainTarget
      this.clusterDetail()
      this.queryList()
    },
    launch() {
      this.isShow = !this.isShow
    },
    submit() {
      let that = this;
      that.$refs["donationForm"].validate(async valid => {
        if (valid) {
          if (that.submitLoading) {
            return;
          }
          for (let index = 0; index < that.primaryData.param.length; index++) {
            if (that.primaryData.param[index].displayName == '学习率' || that.primaryData.param[index].displayName == '迭代轮次') {
              if (that.primaryData.param[index].defaultValue < Number(that.primaryData.param[index].checkValue[0]) || that.primaryData.param[index].defaultValue > Number(that.primaryData.param[index].checkValue[1])) {
                that.$message.error('输入参数请不要超过参数范围');
                return false
              }
            }
          }
          that.submitLoading = true
          let params = {
            name: that.primaryData.name,
            modelId: that.primaryData.baseModelId,
            parentId: that.primaryData.toReinforceLearningModelId,
            method: that.method,
            param: that.primaryData.param,
            dataSetId: that.primaryData.dataSetId,
            type: that.trainType,
            classify: that.classify
          }
          trainTaskAdd(params).then((res) => {
            if (res.success) {
              that.submitLoading = false
              that.$message.success(res.message);
              this.$router.push({path: '/train/tasks'})
            } else {
              that.submitLoading = false
              that.$message.error(res.message);
              that.quxiao()
            }
          })
              .catch((err) => {
                that.submitLoading = false
                console.log(err, "失败");
                that.$message.error(res.message);
                that.quxiao()
              });
        } else {
          return false;
        }
      });
    },
    quxiao() {
      this.primaryData = {
        name: '',
        baseModelId: '',
        toReinforceLearningModelId: '',
        method: '',
        param: [],
        dataType: 1,
        dataSetId: ''
      }
      this.$router.go(-1);
    }
  },
}
</script>
<style lang="less" scoped>
.myAbility {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
  margin-bottom: 24px;
}

.blockBg {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 24px 0 1px 0;
  margin-bottom: 24px;
}

.blockBgButton {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 1px 0 1px 0;
  margin-bottom: 24px;
}

.trainTasks-dialog {
  margin-bottom: 12px;
  border-radius: 4px;
  margin-left: 80px;

  .trainTasks-dialog-left {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 400;
    font-size: 16px;
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

.parameName {
  margin: 0 24px 15px;
  padding-bottom: 18px;
  border-bottom: 1px solid #D0D7E2;
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: 500;
  font-size: 16px;
  color: #1C2748;
}

.parameTable {
  width: 948px;
  //height: 254px;
  height: auto;
  background: #F7F8F9;
  border-radius: 2px;
  padding: 0 16px;
  overflow: hidden;
  position: relative;
}

.parameHeight {
  height: auto !important;
}

.paramepo {
  position: inherit !important;
  z-index: 9;
  margin-top: -1px;
}

.parameTableImg {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 948px;
  height: 46px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #F7F8F9;

  img {
    width: 14px;
    height: 14px;
    cursor: pointer;
  }
}

.parameText {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-size: 16px;
  color: rgba(28, 39, 72, 0.7);
  margin-left: 90px;
  margin-bottom: 5px;
}

/deep/ .el-table td.el-table__cell {
  background: #F7F8F9;
  height: 81px;
}

/deep/ .el-table th.el-table__cell.is-leaf {
  background: #F7F8F9;
  text-align: center;
}

/deep/ .el-form-item__label {
  font-size: 16px;
  color: #1C2748;
}

/deep/ .el-button--primary {
  background-color: #216EFF;
  border-color: #216EFF;
  width: 106px;
  font-size: 16px;
  margin: 0 24px;

}

/deep/ .el-table {
  border-radius: 2px;
}

/deep/ .el-table .cell {
  color: rgba(28, 39, 72, 0.8);
}

/deep/ .changeData .el-input__inner {
  padding-left: 42px;
}

/deep/ .el-button--default {
  background: #EBEBEB;
  border: 1px solid #EBEBEB;
  color: #535456;
  width: 106px;
  font-size: 16px;
  margin: 0 24px;
}

.KeyUpcss {
  color: #f33e3e;
  font-size: 12px;
}
</style>
