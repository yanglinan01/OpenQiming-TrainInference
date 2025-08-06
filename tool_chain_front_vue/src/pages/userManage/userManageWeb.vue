<template>
  <div>
    <div class="createTrainTasks">用户管理</div>
    <div class="trainTasksBlock">
      <common-table
          :data="allTasksList"
          :loading="getListLoading"
          :columns="tableColumns"
          :pagination="paginations"
          :queryParam="queryParam"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
          <template #query>
          <div class="allTasksTableHead">
            <el-row :gutter="10" class="mb8">
              <el-form :model="queryParam" ref="logform" label-width="120px" :inline="true">
                <template v-for="col in tableColumns">
                  <template v-if="col.visible && col.search">
                  <el-form-item :label="col.label+'：'">
                    <el-input
                      v-if="col.searchType === 'input'"
                      :key="col.prop + '-input'"
                      v-model="queryParam[col.prop]"
                      :placeholder="'请输入' + col.label"
                      style="width: 180px; margin-right: 8px;"
                      @keyup.enter.native="queryList"
                    />
                  </el-form-item>
                  </template>
                </template>
                <el-button class="btn" type="primary" @click="queryList()" style="margin-left: 30px;height: 40px;">查询</el-button>
                <el-button class="btn" type="primary" @click="addList()" style="margin-left: 30px;height: 40px;">新增</el-button>
              </el-form>
            </el-row>
            <div style="display: flex;">
              <div style="margin-left:15px;cursor: pointer;">
                <el-dropdown trigger="click">
                  <span class="el-dropdown-link">
                    <i class="el-icon-setting"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="col in tableColumns" :key="col.prop">
                      <el-checkbox v-model="col.visible">{{ col.label }}</el-checkbox>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
          </div>
          </template>
          <template #employeeNumber="{ row }">
            <span>{{ row.employeeNumber }}</span>
          </template>
          <template #userName="{ row }">
            <span>{{ row.userName }}</span>
          </template>
          <template #name="{ row }">
            <span>{{ row.name }}</span>
          </template>
          <template #corpName="{ row }">
            <span>{{ row.corpName }}</span>
          </template>
          <template #systemAuth="{ row }">
            <span>{{ row.systemAuth === '0' ? '是' : '否' }}</span>
          </template>
          <template #operation="{ row }">
            <el-button
                type="primary"
                size="mini"
                :disabled="row.toDeleteDisabled"
                @click="deleteRow(row.id)"
            >
              删除
            </el-button>
          </template>
      </common-table>
    </div>

    <el-dialog
        :title="getTitle()"
        :visible.sync="dialogShow"
        :close-on-click-modal="false"
        :before-close="cancelDialog"
        >
      <component
          :is="getComponentName()"
          @closeDialog="closeDialog()"
          @cancelDialog="cancelDialog()"
          :key="Math.random()">
      </component>
    </el-dialog>
  </div>
</template>
<script>
import {authQueryPage, deleteUser, changeUserBaseAuth,importUser,queryOrderAccountPage,submitOrderAccount} from "@api/userManager";
import addUser from './addUser.vue'
import { forEach } from "lodash";
import CommonTable from '../../components/commonTable/index.vue';

export default {
  name: "userManageWeb",
  data() {
    return {
      toDeleteDisabled: true,
      allTasksList: [],
      origin: null,
      getListLoading: false,
      getTrainTasksDetailLoading: false,
      trainTasksDialogVisible: false,
      dialogShow: false,
      selectedOrigins: [],
      selectedOriginsUserId: null,
      trainTasksDetail: {
        agentAuth: null,
        toolAuth: null,
      },
      queryParam: {
        currentPage: 1,
        pageSize: 10,
        userName: '',
        name:'',
        employeeNumber: '',
      },
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      getOrderAccountLoading: false,
      orderAccountTotal: 0,
      orderAccountList: [],
      tableColumns: [
        { prop: 'employeeNumber', label: '用户账号', slot: 'employeeNumber', sortable: false, visible: true, search: true, searchType: 'input',align:'center' },
        { prop: 'userName', label: '用户名', slot: 'userName', sortable: false, visible: true, search: true, searchType: 'input',align:'center' },
        { prop: 'name', label: '真实姓名', slot: 'name', sortable: false, visible: true, search: true, searchType: 'input',align:'center'},
        { prop: 'corpName', label: '公司', slot: 'corpName', sortable: false, visible: true, search: false,align:'center' },
        { prop: 'systemAuth', label: '系统管理权限', slot: 'systemAuth', sortable: false, visible: true, search: false,align:'center' },
        { prop: 'operation', label: '操作', slot: 'operation', visible: true, search: false,align:'center' }
      ],
    };
  },
  created() {
    this.getList();
  },
  components: {
    addUser,
    CommonTable
  },
  methods: {
    getTitle() {
      return "用户及权限配置";
    },
    addList() {
      this.dialogShow = true
    },
    closeDialog() {
      this.dialogShow = false
      this.getList()
    },
    cancelDialog() {
      this.dialogShow = false
    },
    queryList() {
      this.queryParam.currentPage = 1;
      this.getList()
    },
    //弹窗文件
    getComponentName() {
      return 'addUser';
    },
    getList() {
      let that = this;
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true;
      let params = {
        filterMap: {
          userName: that.queryParam.userName,
          name: that.queryParam.name,
          employeeNumber: that.queryParam.employeeNumber
        },
        pageParam: {
          pageNum: that.queryParam.currentPage,
          pageSize: that.queryParam.pageSize,
        }
      };
      authQueryPage(params).then((res) => {
        if (res.success) {
          that.getListLoading = false
          that.allTasksList = res.data.rows
          that.paginations.total = Number(res.data.total)
        } else {
          that.getListLoading = false
          that.$message.error('查询失败：'+res.message);
        }
      })
          .catch((err) => {
            that.getListLoading = false
            console.log(err, "失败");
          });
    },
    deleteRow(id) {
      this.$confirm("是否从任务上删除本用户？", "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            let that = this;
            let params = {
              userId: id,
            };
            deleteUser(params)
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
  .notice{
    width: 100%;
    height: 32px;
    background: #FFF8E8;
    border-radius: 4px;
    margin-bottom: 25px;
    padding-left: 25px;
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #111D32;
    letter-spacing: 0.5px;
    img{
      margin-right: 20px;
    }
    .noticeNode{
      color: #216EFF;
      text-decoration: underline;
      cursor: pointer;
    }
  }

  .allTasksTableHead {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: -20px;

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

.btn {
  margin-left: 7px;
}

/deep/ .el-checkbox__label {
  font-size: 16px;
  //字体加粗
  font-weight: bold;
  color: #1c2748;
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

/deep/ .el-form-item__label {
  text-align: right;
  vertical-align: middle;
  float: left;
  font-size: 15px;
  font-weight: bold;
}

/deep/ .el-button--primary {
  background-color: #216eff;
  border-color: #216eff;
  font-size: 15px;
}

/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}
/deep/ .qxDialog .el-dialog{
  border-radius: 16px;

}
/deep/ .qxDialog .el-dialog__header{
  width: 100%;
  background: #fff;
  padding: 40px 40px 0 40px;
  line-height: normal;
  height: auto;
}
/deep/ .qxDialog .el-dialog__title{
  font-weight: 600;
  font-size: 20px;
  color: #1C2748;
}
/deep/ .qxDialog .el-dialog__headerbtn{
  top: 36px;
  right: 40px;
}
/deep/ .qxDialog .el-icon-close{
  font-size: 18px;
  color: #000;
}
/deep/ .qxDialog .el-dialog__body{
  padding: 30px 40px 0 40px;
}
/deep/ .qxDialog .el-dialog__footer{
  padding: 30px 40px 40px 40px;
  margin-top: 30px;
  border-top: 1px solid rgba(107, 116, 146, 0.2);
}
/deep/ .qxDialog .dialogCenter .gdh{
  font-size: 16px;
  color: #1C2748;
  line-height: 22px;
  margin-bottom: 5px;
}
/deep/ .qxDialog .el-checkbox{
  margin-right: 0px;
}
.dialogCenter{
  margin-bottom: 25px;
}
/deep/ .qxDialog .el-checkbox__label {
  font-size: 14px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.88);
}
/deep/ .qxDialog .el-table .cell{
  color: rgba(0, 0, 0, 0.88);
}
</style>

