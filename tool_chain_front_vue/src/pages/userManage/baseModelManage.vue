<template>
  <div>
    <div class="createTrainTasks">模型管理</div>
    <div class="trainTasksBlock">
      <common-table
          :data="allModelList"
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
              </el-form>
            </el-row>
            <div style="margin-left:auto;cursor: pointer;">
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
          </template>
          <template #name="{ row }">
            <span>{{ row.name }}</span>
          </template>
          <template #typeName="{ row }">
            <span>{{ row.typeName }}</span>
          </template>
          <template #belongName="{ row }">
            <span>{{ row.belongName }}</span>
          </template>
          <template #regionName="{ row }">
            <span>{{ row.regionName }}</span>
          </template>
          <template #operation="{ row }">
            <span
                style="color: #216EFF; cursor: pointer; font-size: 100%;"
                @click="establish(row)">
              权限分配
            </span>
            <span
                style="color: #216EFF; cursor: pointer; font-size: 100%; margin:0 20px;"
                :class="{ 'disabled': row.toDeleteDisabled }"
                @click="row.toDeleteDisabled ? null : openAll(row.id)">
              一键开放
            </span>
            <span
                :style="{ color: row.status === 0 ? '#216EFF' : '#AFB4BA', fontSize: '100%', cursor: row.status === 0 ? 'pointer' : 'not-allowed' }"
                :class="{ deployClickable: row.status === 0 }"
                @click="row.status === 0 && deployNow(row.id)"
            >
              {{ row.status === 0 ? '立即部署' : '已部署' }}
            </span>
          </template>
      </common-table>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="trainTasksDialogVisible" v-loading="getTrainTasksDetailLoading">
      <div class="tasksBlock">
        <div class="allTableHead">
          <el-row :gutter="10" class="mb8">
            <el-form :model="dialogSearchParam" label-width="120px" :inline="true" class="form-container">
              <el-form-item label="人力账号：">
                <el-input
                    class="input-item"
                    placeholder="请输入人力账号"
                    v-model="dialogSearchParam.employeeNumber"
                ></el-input>
              </el-form-item>
              <el-form-item label="真实名称：">
                <el-input
                    class="input-item"
                    placeholder="请输入用户名"
                    v-model="dialogSearchParam.name"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <div class="search-button">
                  <el-button class="btn" type="primary" @click="queryDialogList()">查询</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-row>
        </div>
        <el-table ref="userTable" :data="allUserList" max-height="284" border style="width: 100%;"
                  v-loading="getUserListLoading" @selection-change="handleSelectionChange">

          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="employeeNumber" label="人力账号" align="center">
          </el-table-column>
          <el-table-column prop="userName" label="用户名" align="center">
          </el-table-column>
          <el-table-column prop="name" label="真实姓名" align="center">
          </el-table-column>
          <el-table-column prop="corpName" label="公司" align="center" width="300">
          </el-table-column>
        </el-table>
        <div class="pager-bar">
          <el-pagination
              v-if="paginationsDialog.total > 0"
              @size-change="handleSizeChangeDialog"
              @current-change="handleCurrentChangeDialog"
              :current-page="dialogSearchParam.pageNum"
              :page-sizes="paginationsDialog.page_sizes"
              :page-size="dialogSearchParam.pageSize"
              :layout="paginationsDialog.layout"
              :total="paginationsDialog.total"
          ></el-pagination>
        </div>
        <template>
          <div>
            <el-radio-group v-model="dialogParam.dataType" @change=""
                            style="display: flex;align-items: center;height: auto;margin: 10px 0 30px 0;">
              <el-radio :label="item.dictItemValue" v-for="(item, index) in tableData" :key="index"
                        :disabled="item.dictItemLabel !== '模型训练'"
                        style="margin: 0 98px 0 0">
                {{ item.dictItemLabel }}
              </el-radio>
            </el-radio-group>
          </div>
        </template>

        <div>
          <el-tag
              v-for="user in selectedUsers"
              :key="user.id"
              closable
              @close="removeUser(user)"
              style="margin-right: 20px; margin-bottom: 15px;"
          >
            {{ user.userName }}
          </el-tag>
        </div>

      </div>

      <divider></divider>
      <!--      //两个按钮，确认和取消-->
      <div style="display: flex; justify-content: flex-end; align-items: center;">
        <el-button class="cancleButton" @click="cancel()">取消</el-button>
        <el-button class="confirmButton" @click="confirm()" style="margin-left: 24px;">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {authQueryPage, oneClickBinding, batchBinding} from "@api/userManager";
import {modelQueryPage} from "@api/square";
import {getDictListByDictType} from "@api/modelExperience";
import {deployTaskAdd} from "@api/modelDeploy";
import CommonTable from '../../components/commonTable/index.vue';

export default {
  name: "baseModelManage",
  components: {
    CommonTable
  },
  data() {
    return {
      dialogTitle: '',
      toDeleteDisabled: true,
      allModelList: [],
      allUserList: [],
      getListLoading: false,
      getUserListLoading: false,
      getTrainTasksDetailLoading: false,
      trainTasksDialogVisible: false,
      selectedOrigins: [],
      selectedOriginsUserId: null,
      dialogSearchParam: {
        employeeNumber: null,
        name: null,
        pageNum: 1,
        pageSize: 10,
      },
      dialogParam: {
        dataType: '1',
        modelId: null
      },
      tableData: [
        {dictItemValue: '1', dictItemLabel: '模型训练'},
        {dictItemValue: '2', dictItemLabel: '模型部署'}
      ],
      selectedUsers: [],
      checkUserList: [],
      queryParam: {
        currentPage: 1,
        pageSize: 10,
        name: null,
      },
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      paginationsDialog: {
        total: 0, // 总数
        page_sizes: [10, 20], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      tableColumns: [
        { prop: 'name', label: '模型名称', slot: 'name', sortable: false, visible: true, search: true, searchType: 'input',align:'center' },
        { prop: 'typeName', label: '模型分类', slot: 'typeName', sortable: false, visible: true, search: false,align:'center' },
        { prop: 'belongName', label: '模型归属', slot: 'belongName', sortable: false, visible: true, search: false,align:'center'},
        { prop: 'regionName', label: '构建省份', slot: 'regionName', sortable: false, visible: true, search: false,align:'center' },
        { prop: 'operation', label: '操作', slot: 'operation', visible: true, search: false,align:'center' }
      ],
    };
  },
  created() {
    this.queryModelList();
    this.getDictListByDictType()
  },
  // watch: {
  //   allUserList: {
  //     handler(newVal) {
  //       // 当 allUserList 变化时，清除表格的选中状态
  //       this.clearSelection();
  //     },
  //     deep: true
  //   }
  // },
  methods: {
    queryList() {
      this.queryParam.currentPage = 1;
      this.queryModelList()
    },
    getDictListByDictType() {
      getDictListByDictType({dictType: "MODEL_AUTH_USAGE"}).then(res => {
        if (res.success && res.data.length > 0) {
          this.tableData = res.data
          // this.queryList(this.selectedValue)
        } else {
          console.error(res.message);
        }
      }).catch(error => {
        console.error('查询列表报错', error);
      });
    },
    handleSelectionChange(selection) {
      // console.log('selection',selection)
      // this.selectedUsers=selection
      // this.checkUserList = selection.map(user => user.id); // 遍历 selection 中的 id 并赋值给 list
      // 添加新的选中值到 selectedUsers
      const newSelectedUsers = [...this.selectedUsers];
      selection.forEach(selectedUser => {
        if (!newSelectedUsers.find(user => user.id === selectedUser.id)) {
          newSelectedUsers.push(selectedUser);
        }
      });
      this.selectedUsers = newSelectedUsers;
      // console.log('this.selectedUsers',this.selectedUsers)
      //被选中的用户id列表
      this.checkUserList = this.selectedUsers.map(user => user.id);
      // console.log('this.checkUserList',this.checkUserList)
    },
    establish(item) {
      this.$router.push({path: '/userManage/permissionAssignment', query: {modelName: item.name,modelId:item.id}})
    },
    // 立即部署
    deployNow(id){
      this.$confirm('立即部署？', "确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let params = {
          modelId: id,
          deployBelong:'2'
        }
        deployTaskAdd(params)
            .then((res) => {
              if (res.success) {
                that.queryModelList();
                that.$message.success('部署成功');
              } else {
                that.$message.error(res.message);
              }
            })
            .catch((err) => {
              console.log(err, "失败");
            });
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消部署",
        });
      });

    },
    removeUser(user) {
      this.selectedUsers = this.selectedUsers.filter(u => u.id !== user.id);
      this.$refs.userTable.toggleRowSelection(user, false);
      // 更新 list
      this.checkUserList = this.selectedUsers.map(user => user.id);
      // console.log('点击取消后this.checkUserList',this.checkUserList)
    },
    // clearSelection() {
    //   this.$refs.userTable.clearSelection();
    // },
    queryModelList() {
      let that = this;
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true;
      let params = {
        name: that.queryParam.name,
        currentPage: that.queryParam.currentPage,
        pageSize: that.queryParam.pageSize,
      };
      modelQueryPage(params).then((res) => {
        if (res.success) {
          that.getListLoading = false
          that.allModelList = res.data.rows
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
    //权限分配
    assignPermissions(row) {
      this.trainTasksDialogVisible = true;
      this.dialogTitle = row.name
      this.dialogParam.modelId = row.id
      this.checkUserList = [];
      this.selectedUsers = [];
      this.dialogSearchParam = {
        employeeNumber: null,
        name: null,
        pageNum: 1,
        pageSize: 10,
      }
      this.queryDialogList();


    },
    queryDialogList() {
      let that = this;
      if (that.getUserListLoading) {
        return;
      }
      that.getUserListLoading = true;

      let params = {
        filterMap: {
          name: that.dialogSearchParam.name,
          employeeNumber: that.dialogSearchParam.employeeNumber,
          modelId: that.dialogParam.modelId,
          usage: that.dialogParam.dataType,
        },
        pageParam: {
          pageSize: that.dialogSearchParam.pageSize,
          pageNum: that.dialogSearchParam.pageNum
        }
      };
      // 弹框查询
      authQueryPage(params).then((res) => {
        if (res.success) {
          that.getUserListLoading = false
          that.allUserList = res.data.rows
          that.paginationsDialog.total = Number(res.data.total)
        } else {
          that.getUserListLoading = false
          that.$message.error('查询失败');
        }
      })
          .catch((err) => {
            that.getUserListLoading = false
            console.log(err, "失败");
          });

    },

    confirm() {
      let that = this;
      // console.log('checkUserList', that.checkUserList)
      if (that.checkUserList.length === 0) {
        that.$message.error("请选择用户");
        return;
      }
      let params = {
        deleteBindingAll: false,
        modelId: that.dialogParam.modelId,
        usage: that.dialogParam.dataType,
        userIdList: that.checkUserList
      };
      that.getTrainTasksDetailLoading = true;
      batchBinding(params).then((res) => {
        if (res.success) {
          that.getTrainTasksDetailLoading = false;
          that.queryModelList();
          this.trainTasksDialogVisible = false;
          that.$message.success(res.message);
        } else {
          that.getTrainTasksDetailLoading = false;
          this.trainTasksDialogVisible = false;
          that.$message.error("查询失败");
        }
        // that.checkUserList=[];
        // that.selectedUsers=[];

      })
          .catch((err) => {
            that.getTrainTasksDetailLoading = false;
            this.trainTasksDialogVisible = false;
            console.log(err, "失败");
          });
    },
    cancel() {
      console.log("quxiaol")
      // this.selectedUsers=[],
      // this.checkUserList=[],
      this.dialogParam.modelId = null,
          this.trainTasksDialogVisible = false;
    },
    openAll(id) {
      this.$confirm("是否开放任务？", "一键开放", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            let that = this;
            let params = {
              modelId: id,
            };
            oneClickBinding(params)
                .then((res) => {
                  if (res.success) {
                    that.queryModelList();
                    that.$message.success(res.message);
                  } else {
                    that.$message.error("开发失败");
                  }
                })
                .catch((err) => {
                  console.log(err, "失败");
                });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消开放",
            });
          });
    },

    handleCurrentChange(val) {
      this.queryParam.currentPage = val;
      this.queryModelList();
    },
    handleSizeChange(val) {
      this.queryParam.pageSize = val;
      this.queryModelList();
    },
    handleCurrentChangeDialog(val) {
      this.dialogSearchParam.pageNum = val;
      this.queryDialogList();
    },
    handleSizeChangeDialog(val) {
      this.dialogSearchParam.pageSize = val;
      this.queryDialogList();
    },
  },
};
</script>
<style lang="less" scoped>
.form-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-item {
  width: 200px; /* 根据实际需求调整宽度 */
}

.search-button {
  margin-left: auto; /* 将查询按钮推到最右侧 */
}

.btn {
  height: 40px;
}

.model-evaluation-content {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
}

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
  padding: 30px;

  .allTasksTableHead {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: -25px;

    .allTasksTableHeadSelect {
      /deep/ .el-input__inner {
        padding-right: 40px;
      }
    }
  }

  .el-button.is-disabled {
    background: #d0d5e0 !important;
    border-color: #d0d5e0 !important;
    color: #fff !important;
  }

  .pager-bar {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 50px;
  }
}

.deployClickable {
  cursor: pointer;
}

.tasksBlock {
  width: 100%;
  background: #ffffff;
  //border-radius: 8px;
  .allTableHead {
    margin-left: -25px;

    .allTasksTableHeadSelect {
      /deep/ .el-input__inner {
        padding-right: 40px;
      }
    }
  }
}

.el-button--primary {
  background: #1B66FF;
  border-color: #1B66FF;
  font-size: 15px;
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
  width: 850px;
  background: #ffffff;
  border-radius: 16px;
  height: auto;
}

/deep/ .el-dialog__header {
  width: 100%;
  height: 70px;
  line-height: 28px;
  background: #ffffff;
  border-radius: 16px 16px 0px 0px;
  padding: 40px 40px 0 40px;

}

/deep/ .el-dialog__title {
  font-size: 20px;
  color: #1C2748;
}

/deep/ .el-dialog__headerbtn {
  right: 41px; /* 距离右侧41px */
  top: 40px;
}

/deep/ .el-dialog__headerbtn .el-dialog__close {
  font-size: 21px;
  color: #1C2748;
}

/deep/ .el-dialog__body {
  padding: 20px 40px 40px 40px;
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
  color: #1C2748;
  font-size: 15px;
  font-weight: bold;
}

.cancleButton {
  width: 114px;
  padding: 0;
  height: 40px;
  background: #F5F5F5;
  border-radius: 8px;
  font-size: 18px;
  color: #1C2748;
}

.confirmButton {
  width: 114px;
  padding: 0;
  height: 40px;
  background: #1B66FF;
  border-radius: 8px;
  font-size: 18px;
  color: #FFFFFF;
}

/deep/ .el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #216EFF;
  border-color: #216EFF;
}

/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}

/deep/ .el-table thead {
  color: rgba(0, 0, 0, 0.88);
}

/deep/ .el-radio__label {
  font-size: 16px;
  color: #1C2748;
}

/deep/ .el-radio__input.is-checked .el-radio__inner {
  border-color: #409EFF;
  background: #216EFF;
}

/deep/ .el-tag {
  font-size: 16px;
  background-color: #e8efff;
  color: #216EFF;
}

/deep/ .ivu-divider-horizontal {
  display: block;
  height: 1px;
  width: 850px;
  margin: 5px -40px 30px -40px;
  clear: both;
  box-shadow: 0px -2px 6px 0px rgba(107, 116, 146, 0.2);
}
</style>

