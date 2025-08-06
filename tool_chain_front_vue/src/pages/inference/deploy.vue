<template>
  <div>
    <div class="createTrainTasks">模型部署</div>
    <div class="trainTasksBlock">
      <div class="trainTasks">
        <el-button type="primary" @click="createTrainTasks('1')">模型推理部署</el-button>
      </div>
      <div class="allTasksTableHead">
        <div>
          <span class="blockblue"></span>
          <span class="blockName">所有任务</span>
        </div>
        <div class="allTasksTableHeadSelect">
          <el-input
              placeholder="请输入任务名称"
              v-model="queryParam.modelName"
              class="input-with-select"
              @keyup.enter.native="queryList"
          >
          </el-input>
          <i class="el-icon-search" @click="queryList()"></i>
        </div>
      </div>
      <el-table :data="allTasksList" style="width: 100%" v-loading="getListLoading">
        <el-table-column prop="modelName" label="模型名称" align="center">
        </el-table-column>
        <el-table-column prop="serverIp" label="部署位置" align="center">
        </el-table-column>
        <el-table-column prop="deployTime" label="部署时间" align="center">
        </el-table-column>
        <el-table-column align="center" label="部署状态">
          <template slot-scope="scope">
            <span
                class="cm-status"
                :style="{ color: caseStatusColorFilter(scope.row.status) }"
            >
              {{ scope.row.statusName }}
              <el-tooltip
                  v-if="scope.row.status === 'failed'"
                  class="item"
                  effect="dark"
                  :content="scope.row.result"
                  placement="top"
              >
                <i class="el-icon-warning"></i>
              </el-tooltip>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="detail(scope.row)">
              查看
            </el-button>
            <el-button
                type="primary"
                size="mini"
                :disabled="scope.row.toDeleteDisabled"
                @click="deleteRow(scope.row.id)"
            >
              删除
            </el-button>
            <el-button
                type="primary"
                size="mini"
                @click="changeDeployIsOpen(scope.row)"
                v-if="scope.row.status=='completed'||scope.row.status=='Succeeded_k8s'||scope.row.status=='Running_k8s'"
            >
              {{ scope.row.online == '1' ? '下线' : '上线' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager-bar">
        <el-pagination
            background
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
        ><span>模型名称：{{ trainTasksDetail.modelName }}</span></el-col
        >
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>部署位置：{{
            trainTasksDetail.areaName
          }} 服务器{{ trainTasksDetail.serverName }} {{ trainTasksDetail.serverIp }}</span></el-col
        >
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        >
          <span>服务器状态：</span>
        </el-col>
      </el-row>
      <template v-if="trainTasksDetail.serverStatusInfo">
        <el-row class="trainTasks-dialog">
          <el-col :span="12" class="trainTasks-dialog-content"
          ><span>CPU使用率：{{
              trainTasksDetail.serverStatusInfo.cpuUtil ? trainTasksDetail.serverStatusInfo.cpuUtil + '%' : ''
            }}</span></el-col
          >
          <el-col :span="12" class="trainTasks-dialog-content"
          ><span>内存使用率：{{
              trainTasksDetail.serverStatusInfo.memUsedUtil ? trainTasksDetail.serverStatusInfo.memUsedUtil + '%' : ''
            }}</span></el-col
          >
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="12" class="trainTasks-dialog-content"
          ><span>IP地址：{{ trainTasksDetail.serverStatusInfo.serverIp }}</span></el-col
          >
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="12" class="trainTasks-dialog-left">部署卡状态：</el-col>
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="12" class="trainTasks-dialog-content">
            NPU使用率：{{
              trainTasksDetail.serverStatusInfo.ctyunClusterNpuUtil ? trainTasksDetail.serverStatusInfo.ctyunClusterNpuUtil + '%' : ''
            }}
          </el-col>
          <el-col :span="12" class="trainTasks-dialog-content">
            NPU温度：{{
              trainTasksDetail.serverStatusInfo.ctyunClusterNpuTemp ? trainTasksDetail.serverStatusInfo.ctyunClusterNpuTemp + '°C' : ''
            }}
          </el-col>
        </el-row>
      </template>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>部署状态：{{ trainTasksDetail.statusName }}</span></el-col>
      </el-row>
      <el-row class="trainTasks-dialog">
        <el-col :span="12" class="trainTasks-dialog-left"
        ><span>备注：{{ trainTasksDetail.remarks }}</span></el-col
        >
      </el-row>

    </el-dialog>
  </div>
</template>
<script>
import {deployTaskDetail, deployTaskqueryPage, deleteById, getUsageDetail, updateOnlineStatus} from "@api/modelDeploy";

export default {
  name: "deploy",
  data() {
    return {
      toDeleteDisabled: false,
      allTasksList: [],
      getListLoading: false,
      getTrainTasksDetailLoading: false,
      trainTasksDialogVisible: false,
      trainTasksDetail: {},
      queryParam: {
        currentPage: 1,
        pageSize: 10,
        modelName: null,
      },
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    queryList() {
      this.queryParam.currentPage = 1;
      this.getList()
    },
    getList() {
      let that = this;
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true;
      let params = {
        currentPage: that.queryParam.currentPage,
        pageSize: that.queryParam.pageSize,
        modelName: that.queryParam.modelName,
      };
      deployTaskqueryPage(params).then((res) => {
        if (res.success) {
          that.getListLoading = false
          that.allTasksList = res.data.rows
          that.allTasksList.forEach(taskItem => {
            switch (taskItem.status) {
              case "deploying": //部署中
                taskItem.toDeleteDisabled = false
                break;
              case "completed":
              case "Succeeded_k8s":
              case "Running_k8s":
                taskItem.toDeleteDisabled = false
                break;
              case "failed":
                taskItem.toDeleteDisabled = false
                break;
              case "waiting":  //等待中
                if (taskItem.waitCount !== 0) {
                  taskItem.statusName = "排队中... (前面还有" + taskItem.waitCount + "个任务)"
                }
                taskItem.toDeleteDisabled = false
                break;
              default:
                taskItem.toDeleteDisabled = false
                break;
            }
          });
          that.paginations.total = Number(res.data.total)
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
    createTrainTasks(trainTaskType) {
      this.$router.push('/inference/deployServer')
    },
    detail(row) {
      let that = this;
      that.trainTasksDialogVisible = true;
      if (that.getTrainTasksDetailLoading) {
        return;
      }
      that.getTrainTasksDetailLoading = true;
      let params = {
        deployTaskId: row.id,
      };

      deployTaskDetail(params)
          .then((res) => {
            if (res.success) {
              that.trainTasksDetail = res.data;
              that.trainTasksDialogVisible = true;
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
      this.$confirm("是否从服务器上删除本任务？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            let that = this;
            let params = {
              deployTaskId: id,
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
    changeDeployIsOpen(val) {
      this.$confirm("是否修改在线状态？", "确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            let that = this;
            let params = {
              deployTaskId: val.id,
              onlineStatus: val.online==='0'? '1':'0'
            };
            updateOnlineStatus(params)
                .then((res) => {
                  if (res.success) {
                    that.getList();
                    that.$message.success(res.message);
                  } else {
                    that.$message.error("修改失败");
                  }
                })
                .catch((err) => {
                  console.log(err, "失败");
                })

          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消修改在线状态",
            });
          });
    },
    caseStatusColorFilter(val) {
      let col = null;
      switch (val) {
        case "deploying":
          col = "#E8850B";
          break;
        case "completed":
        case "Succeeded_k8s":
        case "Running_k8s":
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
      this.getList();
    },
    handleSizeChange(val) {
      this.queryParam.pageSize = val;
      this.getList();
    },
  },
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
  border-radius: 8px;
  padding: 24px;

  .allTasksTableHead {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 18px;
    margin-top: 10px;

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

  .el-button.is-disabled {
    background: #d0d5e0 !important;
    border-color: #d0d5e0 !important;
    color: #fff !important;
  }

  .pager-bar {
    text-align: right;
    margin-top: 30px;
  }
}

.trainTasks {
  .el-button--primary {
    background-color: #216eff;
    border-color: #216eff;
  }
}

/deep/ .el-dialog {
  width: 540px;
  background: #ffffff;
  border-radius: 4px;
}

/deep/ .el-dialog__header {
  width: 540px;
  height: 40px;
  line-height: 40px;
  background: #f0f3fa;
  border-radius: 4px 4px 0px 0px;
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
  padding: 22px 24px 8px;
}

.trainTasks-dialog {
  margin-bottom: 12px;
  border-radius: 4px;

  .trainTasks-dialog-left {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    width: 400px;
    font-size: 14px;
    font-weight: bold;
    color: #1c2748;
    line-height: 24px;
  }

  .trainTasks-dialog-table-row {
    text-align: center;

    .trainTasks-dialog-table-col-char {
      background-color: #f0f3fa;
      border-right: none !important;
    }

    .el-col-6 {
      line-height: 30px;
      border: 1px solid #E1E6EF;
      color: #1C2748;
      width: 50%;
    }
  }
}

.trainTasks-border .el-col-6 {
  border-top: none !important;
  border-bottom: none !important;
}

/deep/ .el-button--primary {
  background-color: #216eff;
  border-color: #216eff;
}

/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}
</style>

