<template>
  <div>
    <div class="createTrainTasks">用户管理</div>
    <div class="trainTasksBlock">
      <div class="notice" v-if="orderAccountTotal>0">
        <img src="@/assets/images/notice.png" alt="">
        <span>收到{{ orderAccountTotal }}条新的权限开通请求，</span>
        <span class="noticeNode" @click="handleOpen">点击这里</span>
        <span>立即处理</span>
      </div>
      <div class="allTasksTableHead">
        <el-row :gutter="10" class="mb8">
          <el-form :model="queryParam" ref="logform" label-width="100px" :inline="true">
            <el-form-item label="用户名：">
              <el-input
                  class="input-item"
                  placeholder="请输入用户名"
                  v-model="queryParam.userName"
              ></el-input>
            </el-form-item>
            <el-form-item label="人力账号：">
              <el-input
                  class="input-item"
                  placeholder="请输入人力账号"
                  v-model="queryParam.employeeNumber"
              ></el-input>
            </el-form-item>
            <el-form-item label="真实名称：">
              <el-input
                  class="input-item"
                  placeholder="请输入用户名"
                  v-model="queryParam.name"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <div class="search-button">
                <el-button class="btn" type="primary" @click="queryList()" style="margin-left: 30px;height: 40px;">查询</el-button>
                <el-button class="btn" type="primary" @click="addList()" style="margin-left: 30px;height: 40px;">新增</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-row>
        <div>
          <el-upload
            class="upload-demo"
            action=""
            :show-file-list="false"
            :auto-upload="false"
            :on-change="upload">
            <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;width:130px;height:40px;">批量上传</el-button>
          </el-upload>
        </div>
      </div>
      <el-table :data="allTasksList" style="width: 100%" v-loading="getListLoading">
        <el-table-column prop="employeeNumber" label="用户账号" align="center">
        </el-table-column>
        <el-table-column prop="userName" label="用户名" align="center">
        </el-table-column>
        <el-table-column prop="name" label="真实姓名" align="center">
        </el-table-column>
        <el-table-column prop="corpName" label="公司" align="center" width="300">
        </el-table-column>
        <!--        //当systemAuth为1，显示为是，为0，显示为否-->
        <el-table-column label="系统管理权限" align="center">
          <template v-slot="scope">
            {{ scope.row.systemAuth === '0' ? '是' : '否' }}
          </template>
        </el-table-column>
        <el-table-column label="工具链平台权限" align="center">
          <template v-slot="scope">
            {{ scope.row.toolAuth === '0' ? '是' : '否' }}
          </template>
        </el-table-column>
        <el-table-column label="智能体平台权限" align="center">
          <template v-slot="scope">
            {{ scope.row.agentAuth === '0' ? '是' : '否' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="detail(scope.row)">
              编辑
            </el-button>
            <el-button
                type="primary"
                size="mini"
                :disabled="scope.row.toDeleteDisabled"
                @click="deleteRow(scope.row.id)"
            >
              删除
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
            :current-page="queryParam.pageNum"
            :page-sizes="paginations.page_sizes"
            :page-size="queryParam.pageSize"
            :layout="paginations.layout"
            :total="paginations.total"
        ></el-pagination>
      </div>
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

    <el-dialog title="编辑信息" :visible.sync="trainTasksDialogVisible" v-loading="getTrainTasksDetailLoading">
      <template>
        <el-checkbox-group v-model="selectedOrigins" @change="onOriginChanged"
                           style="display: flex;align-items: center;height: 40px;">
          <el-checkbox :label="1">工具链权限</el-checkbox>
          <el-checkbox :label="2">智能体权限</el-checkbox>
        </el-checkbox-group>
      </template>
      <!--      //两个按钮，确认和取消-->
      <div style="text-align: center;">
        <divider></divider>
        <el-button type="primary" @click="confirm()">确认</el-button>
        <el-button @click="cancel()">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="权限开通请求"
      :visible.sync="dialogVisibleShow"
      class="qxDialog"
      width="900px"
      :before-close="handleClose">
      <div class="dialogCenter" v-for="(item, index) in orderAccountList" :key="index">
        <p class="gdh">工单号：{{ item.orderCode }}</p>
        <el-table :data="item.openAccountInfoVoList" style="width: 100%" v-loading="getOrderAccountLoading" border>
          <el-table-column prop="toolChainName" label="待开通用户">
          </el-table-column>
          <el-table-column prop="toolChainOAName" label="人力账号">
          </el-table-column>
          <el-table-column prop="provinceCompany" label="省份/公司">
          </el-table-column>
          <el-table-column prop="toolChainPhone" label="手机号">
          </el-table-column>
          <el-table-column label="训推工具链">
            <template v-slot="scope">
              {{ scope.row.toolAuth === '1' ? '未开通' : '已开通' }}
            </template>
          </el-table-column>
          <el-table-column label="智能体工具链">
            <template v-slot="scope">
              {{ scope.row.agentAuth === '1' ? '未开通' : '已开通' }}
            </template>
          </el-table-column>
          <el-table-column label="开通需求" width="150">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.toolPermissionState">训推工具链</el-checkbox>
              <el-checkbox v-model="scope.row.agentPermissionState">智能体工具链</el-checkbox>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleShow = false">取 消</el-button>
        <el-button type="primary" @click="submitOrderAccount()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {authQueryPage, deleteUser, changeUserBaseAuth,importUser,queryOrderAccountPage,submitOrderAccount} from "@api/userManager";
import addUser from './addUser.vue'
import { forEach } from "lodash";


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
        pageNum: 1,
        pageSize: 10,
        userName: null,
        name:null,
        employeeNumber: null,
      },
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      dialogVisibleShow: false,
      getOrderAccountLoading: false,
      orderAccountTotal: 0,
      orderAccountList: [],

    };
  },
  created() {
    this.getList();
    this.queryOrderAccountPage()
  },
  components: {
    addUser
  },
  methods: {
    //权限开通弹窗
    handleOpen(){
      this.dialogVisibleShow = true
    },
    handleClose(){
      this.dialogVisibleShow = false
    },
    //分页查询门户工单完整数据
    queryOrderAccountPage(){
      let that = this;
      if (that.getOrderAccountLoading) {
        return;
      }
      that.getOrderAccountLoading = true;
      let params = {
        filterMap: {
          reviewStatus: '1',
        },
        pageParam: {
          pageSize: -1,
        }
      };
      queryOrderAccountPage(params).then((res) => {
        if (res.success) {
          that.getOrderAccountLoading = false
          for (let index = 0; index < res.data.rows.length; index++) {
            for (let j = 0; j < res.data.rows[index].openAccountInfoVoList.length; j++) {
              if (res.data.rows[index].openAccountInfoVoList[j].toolPermissionState == '0') {
                res.data.rows[index].openAccountInfoVoList[j].toolPermissionState = true
              }else{
                res.data.rows[index].openAccountInfoVoList[j].toolPermissionState = false
              }
              if (res.data.rows[index].openAccountInfoVoList[j].agentPermissionState == '0') {
                res.data.rows[index].openAccountInfoVoList[j].agentPermissionState = true
              }else{
                res.data.rows[index].openAccountInfoVoList[j].agentPermissionState = false
              }
            }
          }
          that.orderAccountList = res.data.rows
          that.orderAccountTotal = Number(res.data.total)
        } else {
          that.getOrderAccountLoading = false
          that.$message.error('查询失败');
        }
      }).catch((err) => {
            that.getOrderAccountLoading = false
            console.log(err, "失败");
          });
    },
    //提交待开通用户
    submitOrderAccount(){
      let that = this;
      for (let index = 0; index < that.orderAccountList.length; index++) {
        for (let j = 0; j < that.orderAccountList[index].openAccountInfoVoList.length; j++) {
          if (that.orderAccountList[index].openAccountInfoVoList[j].toolPermissionState == true) {
            that.orderAccountList[index].openAccountInfoVoList[j].toolPermissionState = '0'
          }else{
            that.orderAccountList[index].openAccountInfoVoList[j].toolPermissionState = '1'
          }
          if (that.orderAccountList[index].openAccountInfoVoList[j].agentPermissionState == true) {
            that.orderAccountList[index].openAccountInfoVoList[j].agentPermissionState = '0'
          }else{
            that.orderAccountList[index].openAccountInfoVoList[j].agentPermissionState = '1'
          }
        }
      }
      console.log(that.orderAccountList);
      submitOrderAccount(that.orderAccountList).then((res) => {
        if (res.success) {
          this.dialogVisibleShow = false
          this.queryOrderAccountPage()
          this.getList();
        }
      })
    },
    //弹窗的标题
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
      this.queryParam.pageNum = 1;
      this.getList()
    },
    async upload(item){
      try {
        var formData = new FormData();
        formData.append("file", item.raw);
        const res =  await importUser(formData);
        if (res.success) {
          this.$message.success("上传成功："+res.message);
          this.getList();
        } else {
          this.$message.error("上传失败："+res.message);
        }
      } catch (err) {
        console.error(err, "失败");
        this.$message.error("上传文件时出错");
      }
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
          pageNum: that.queryParam.pageNum,
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
    //给弹出框中的复选按钮赋值
    detail(row) {
      // let that = this;
      this.trainTasksDialogVisible = true;
      this.selectedOrigins = []
      this.selectedOriginsUserId = row.id
      //row.toolAuth代表1，row.agentAuth代表2，现在给that.selectedOrigins赋值,
      // 如果row.toolAuth=0则that.selectedOrigins中含有1，和row.agentAuth=0则that.selectedOrigins中含有2
      if (row.toolAuth === '0') {
        this.selectedOrigins.push(1);
      }
      if (row.agentAuth === '0') {
        this.selectedOrigins.push(2);
      }
      // console.log('Selected 按钮:', this.selectedOrigins);
    },
    //复选框触发按钮
    onOriginChanged(values) {
      // console.log('Selected values:', values);
      // console.log('Selected 按钮:', this.selectedOrigins);
    },
    confirm() {
      let that = this;
      // 如果that.selectedOrigins中含有1则row.toolAuth=0，和that.selectedOrigins中含有2则row.agentAuth=0
      if (that.selectedOrigins.includes(1)) {
        that.trainTasksDetail.toolAuth = '0';
      } else {
        that.trainTasksDetail.toolAuth = '1';
      }
      if (that.selectedOrigins.includes(2)) {
        that.trainTasksDetail.agentAuth = '0';
      } else {
        that.trainTasksDetail.agentAuth = '1';
      }

      let params = {
        id: that.selectedOriginsUserId,
        agentAuth: that.trainTasksDetail.agentAuth,
        toolAuth: that.trainTasksDetail.toolAuth,
      };

      console.log("修改权限的入参",params)
      that.getTrainTasksDetailLoading = true;
      changeUserBaseAuth(params)
          .then((res) => {
            if (res.success) {
              that.getTrainTasksDetailLoading = false;
              that.getList();
              this.trainTasksDialogVisible = false;
              that.$message.success(res.message);
            } else {
              that.getTrainTasksDetailLoading = false;
              this.trainTasksDialogVisible = false;
              that.$message.error("查询失败");
            }
          })
          .catch((err) => {
            that.getTrainTasksDetailLoading = false;
            this.trainTasksDialogVisible = false;
            console.log(err, "失败");
          });
    },
    cancel() {
      this.trainTasksDialogVisible = false;
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
      this.queryParam.pageNum = val;
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

