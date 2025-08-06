<template>
  <div>
    <div class="createTrainTasks">模型管理</div>
    <div class="trainTasksBlock">
      <div @click="goBack">
        <img src="../../assets/images/icon-back.png" alt="左箭头图片" slot="reference" style="vertical-align: middle;">
        <span style="margin-left: 10px;color: #1C2748;opacity: 0.4;vertical-align: middle;">返回模型{{ modelName }}</span>
      </div>
      <div class="allTasksTableHead">
        <el-row :gutter="10" class="mb8" style="height:40px;">
          <el-form :model="dialogSearchParam" label-width="120px" :inline="true">
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
                <el-button class="btn" type="primary" @click="queryDialogList()"
                           style="margin-left: 30px;height: 40px;">查询
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-row>

      </div>

      <div class="permission-container">
        <div class="left-section">
      <el-table ref="userTable" :data="allUserList" style="width: auto"
                v-loading="getUserListLoading" @selection-change="handleSelectionChange">

        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="employeeNumber" label="人力账号" align="center">
        </el-table-column>
        <el-table-column prop="userName" label="用户名" align="center">
        </el-table-column>
        <el-table-column prop="name" label="真实姓名" align="center">
        </el-table-column>
        <el-table-column prop="corpName" label="公司" align="center">
        </el-table-column>
        <el-table-column label="模型权限状态" align="center" width="300">
          <template slot-scope="scope">
            <span v-if="scope.row.reasonAuth === '0'">推理权限 已开通</span>
            <span v-else>推理权限 未开通</span>
            <span style="margin-left: 20px;"></span>
            <span v-if="scope.row.trainAuth === '0'">训练权限 已开通</span>
            <span v-else>训练权限 未开通</span>
          </template>
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
      </div>
<!--        中间的图标-->
        <div class="separator">

          <div>
            <img
                :src="topArrowSrc"
                alt="上休眠箭头图片"
                @click="toggleTopArrow"
                :style="{ marginLeft: '13px', marginTop: '94px', cursor:'pointer'}"
            >
          </div>
          <div>
<!--            <img src="../../assets/images/left-double-arrows.png" alt="中间休眠箭头图片" slot="reference" style="margin-left: 13px;margin-top: 84px;">-->
            <img
                :src="midArrowSrc"
                alt="中间休眠箭头图片"
                @click="toggleMidArrow"
                :style="{ marginLeft: '13px', marginTop: '84px', cursor:'pointer' }"
            >
          </div>

          <div>
            <img
                :src="bottomArrowSrc"
                alt="下休眠箭头图片"
                @click="toggleBottomArrow"
                :style="{ marginLeft: '13px', marginTop: '84px', cursor:'pointer' }"
            >
          </div>
        </div>
        <div class="right-section">
          <!-- 右边的内容 -->
          <div class="elTagTitle">训练权限</div>
          <div class="elTagInput">
            <el-tag
                v-for="user in perSelectedUsers"
                :key="user.id"
                closable
                @close="removePreUser(user)"
                style="margin-right: 20px; margin-bottom: 15px;"
            >
              {{ user.userName }}
            </el-tag>
          </div>

          <div class="elTagTitle" style="margin-top: 30px;">推理权限</div>
          <div class="elTagInput">
            <el-tag
                v-for="user in inferSelectedUsers"
                :key="user.id"
                closable
                @close="removeInferUser(user)"
                style="margin-right: 20px; margin-bottom: 15px;"
            >
              {{ user.userName }}
            </el-tag>
          </div>
          <div style="display: flex; justify-content: center; align-items: center;margin-top: 20px;">
            <el-button class="cancleButton" @click="cancel()">取消</el-button>
            <el-button class="confirmButton" @click="confirm()" :loading="getTrainTasksDetailLoading" :disabled="isConfirmButtonDisabled" style="margin-left: 24px;">确认</el-button>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script>
import {batchBindingAll, queryPageByModel} from "@api/userManager";

export default {
  name: "permissionAssignment",
  data() {
    return {
      modelName: this.$route.query.modelName,
      modelId: this.$route.query.modelId,
      dialogSearchParam: {
        employeeNumber: null,
        name: null,
        pageNum: 1,
        pageSize: 10,
        dataType:'1'
      },
      allUserList: [
        // {
        //   id:1,
        //   employeeNumber: '123456',
        //   userName: '张三',
        //   name: '张三1',
        //   corpName: '腾讯科技',
        //   reasonAuth: '0',
        //   trainAuth: '1'
        // },
        // {
        //   id:2,
        //   employeeNumber: '123456',
        //   userName: '张三2',
        //   name: '张三2',
        //   corpName: '腾讯科技',
        //   reasonAuth: '0',
        //   trainAuth: '0'
        // },
        // {
        //   id:3,
        //   employeeNumber: '123456',
        //   userName: '张三3',
        //   name: '张三3',
        //   corpName: '腾讯科技',
        //   reasonAuth: '1',
        //   trainAuth: '1'
        // }
      ],
      getUserListLoading: false,
      getTrainTasksDetailLoading:false,
      paginationsDialog: {
        total: 0, // 总数
        page_sizes: [10, 20], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      topArrowSrc: require('../../assets/images/left-double-arrow.png'),
      midArrowSrc: require('../../assets/images/left-double-arrows-sleep.png'),
      bottomArrowSrc: require('../../assets/images/left-double-arrow-sleep.png'),
      permissionType: 1,
      perSelectedUsers:[],
      perSelectedIds:[],
      inferSelectedUsers:[],
      inferSelectedIds:[],

    }
  },
  computed: {
    isConfirmButtonDisabled() {
      return this.perSelectedIds.length === 0 && this.inferSelectedIds.length === 0;
    }
  },
  created() {
    this.queryDialogList()
  },
  methods: {
    // canSelectRow(row) {
    //   console.log('row',111)
    //   if (this.permissionType == 1) {
    //     console.log('1111111111')
    //     return row.reasonAuth !== '0';
    //   } else if (this.permissionType == 2) {
    //     return row.trainAuth !== '0';
    //   } else if (this.permissionType == 3) {
    //     return !(row.reasonAuth === '0' && row.trainAuth === '0');
    //   }
    //   return true; // 默认情况下可以选中
    // },
    toggleTopArrow() {
      if (this.topArrowSrc === require('../../assets/images/left-double-arrow-sleep.png')) {
        this.topArrowSrc = require('../../assets/images/left-double-arrow.png');
        this.bottomArrowSrc = require('../../assets/images/left-double-arrow-sleep.png');
        this.midArrowSrc = require('../../assets/images/left-double-arrows-sleep.png');
        //切换图标时，列表中选中内容置空
        this.$refs.userTable.clearSelection();
        this.permissionType = 1;
        // console.log('this.permissionType',this.permissionType)
      }
      // console.log('上',this.permissionType)
    },
    toggleMidArrow() {
      if (this.midArrowSrc === require('../../assets/images/left-double-arrows-sleep.png')) {
        this.midArrowSrc = require('../../assets/images/left-double-arrows.png');
        this.bottomArrowSrc = require('../../assets/images/left-double-arrow-sleep.png');
        this.topArrowSrc = require('../../assets/images/left-double-arrow-sleep.png');
        //切换图标时，列表中选中内容置空
        this.$refs.userTable.clearSelection();
        this.permissionType = 3;
        // console.log('this.permissionType',this.permissionType)
      }
      // console.log('中',this.permissionType)
    },
    toggleBottomArrow() {
      if (this.bottomArrowSrc === require('../../assets/images/left-double-arrow-sleep.png')) {
        this.bottomArrowSrc = require('../../assets/images/left-double-arrow.png');
        this.topArrowSrc = require('../../assets/images/left-double-arrow-sleep.png');
        this.midArrowSrc = require('../../assets/images/left-double-arrows-sleep.png');
        //切换图标时，列表中选中内容置空
        this.$refs.userTable.clearSelection();
        this.permissionType = 2;
        // console.log('this.permissionType',this.permissionType)
      }
      // console.log('下',this.permissionType)
    },
    goBack() {
      this.$router.go(-1)
    },
    queryDialogList() {
      // console.log('this.modelId',this.modelId)
      let that = this;
      if (that.getUserListLoading) {
        return;
      }
      that.getUserListLoading = true;

      let params = {
        filterMap: {
          name: that.dialogSearchParam.name,
          employeeNumber: that.dialogSearchParam.employeeNumber,
          modelId: that.modelId,
          usage: that.dialogSearchParam.dataType,
        },
        pageParam: {
          pageSize: that.dialogSearchParam.pageSize,
          pageNum: that.dialogSearchParam.pageNum
        }
      };
      // 弹框查询
      queryPageByModel(params).then((res) => {
        if (res.success) {
          that.getUserListLoading = false
          that.allUserList = res.data.rows
          that.paginationsDialog.total = Number(res.data.total)
        } else {
          that.getUserListLoading = false
          that.$message.error('查询失败：'+res.message);
        }
      })
          .catch((err) => {
            that.getUserListLoading = false
            console.log(err, "失败");
          });

    },
    // 定义一个方法来更新 checkUserList
    updateperSelectedIds() {
      this.perSelectedIds = this.perSelectedUsers.map(user => user.id);
      // console.log('this.perSelectedIds',this.perSelectedIds)
    },
    updateinferSelectedIds() {
      this.inferSelectedIds = this.inferSelectedUsers.map(user => user.id);
      // console.log('this.inferSelectedIds',this.inferSelectedIds)
    },
    //对选中数据进行处理
    sectionList(selection,sectionList){
      const newSelectedUsers = sectionList;
      selection.forEach(selectedUser => {
        if (!newSelectedUsers.find(user => user.id === selectedUser.id)) {
          newSelectedUsers.push(selectedUser);
          // console.log('newSelectedUsers',newSelectedUsers)
        }
      });
      return newSelectedUsers;

    },
    handleSelectionChange(selection) {
      // console.log('选中触发')
      const latestSelectedRow = selection[selection.length - 1];
      if(this.permissionType==1){
        //判断selection.reasonAuth=0,报错“已开放权限”
        if (latestSelectedRow.trainAuth === '0') {
          this.$message({
            message: '训练权限已开放',
            type: 'warning'
          });
          selection.pop();
          //当前数据不选择
          this.$refs.userTable.toggleRowSelection(latestSelectedRow, false);
          return;
        }
        this.perSelectedUsers = this.sectionList(selection, this.perSelectedUsers);
        this.updateperSelectedIds();
      }
      else if (this.permissionType==2){
        if (latestSelectedRow.reasonAuth === '0') {
          this.$message({
            message: '推理权限已开放',
            type: 'warning'
          });
          selection.pop();
          //当前数据不选择
          this.$refs.userTable.toggleRowSelection(latestSelectedRow, false);
          // console.log('selection',selection)
          return;
        }
        this.inferSelectedUsers = this.sectionList(selection, this.inferSelectedUsers);
        this.updateinferSelectedIds();
      }else if (this.permissionType==3){
        if (!(latestSelectedRow.reasonAuth === '1' && latestSelectedRow.trainAuth === '1')) {
          this.$message({
            message: '权限已开通，请确认！',
            type: 'warning'
          });
          selection.pop();
          //当前数据不选择
          this.$refs.userTable.toggleRowSelection(latestSelectedRow, false);
          return;
        }
        this.perSelectedUsers = this.sectionList(selection, this.perSelectedUsers);
        this.inferSelectedUsers=this.perSelectedUsers
        this.updateperSelectedIds();
        // this.inferSelectedUsers = this.sectionList(selection, this.inferSelectedUsers);
        this.updateinferSelectedIds();
      }

    },

    removePreUser(user) {
      // 使用 filter 方法移除指定 id 的用户
      // console.log('移除前this.perSelectedUsers',this.perSelectedUsers)
      this.perSelectedUsers = this.perSelectedUsers.filter(u => u.id !== user.id);

      // console.log('移除后this.perSelectedUsers',this.perSelectedUsers)
      // 取消表格中的选中状态
      this.$refs.userTable.toggleRowSelection(user, false);
      // 更新 perSelectedIds
      this.updateperSelectedIds();

    },
    removeInferUser(user) {
      // 使用 filter 方法移除指定 id 的用户
      this.inferSelectedUsers = this.inferSelectedUsers.filter(u => u.id !== user.id);
      // 取消表格中的选中状态
      this.$refs.userTable.toggleRowSelection(user, false);
      // 更新 inferSelectedIds
      this.updateinferSelectedIds();
    },

    cancel() {
      this.perSelectedUsers=[]
      this.perSelectedIds=[]
      this.inferSelectedUsers=[]
      this.inferSelectedIds=[]
      this.$refs.userTable.clearSelection();
      // this.goBack()
    },
    confirm() {
    //   console.log('this.modelId',this.modelId)
      let that = this;
      let params = [
        {
          deleteBindingAll: false,
          modelId: this.modelId,
          usage: "1",
          userIdList: this.perSelectedIds
        },
        {
          deleteBindingAll: false,
          modelId: this.modelId,
          usage: "2",
          userIdList: this.inferSelectedIds
        }
      ];
      that.getTrainTasksDetailLoading = true;
      batchBindingAll(params).then((res) => {
        if (res.success) {
          that.getTrainTasksDetailLoading = false;
          that.queryDialogList();
          that.$message.success(res.message);
        } else {
          that.getTrainTasksDetailLoading = false;
          that.$message.error("添加失败");
        }
        // console.log('11111111111111')
        this.cancel()
        this.goBack()
      })
          .catch((err) => {
            that.getTrainTasksDetailLoading = false;
            console.log(err, "失败");
          });

    },
    handleCurrentChangeDialog(val) {
      this.dialogSearchParam.pageNum = val;
      this.queryDialogList();
    },
    handleSizeChangeDialog(val) {
      this.dialogSearchParam.pageSize = val;
      this.queryDialogList();
    },
  }
}
</script>

<style scoped>
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
}

.allTasksTableHead {
  justify-content: space-between;
  margin-bottom: 30px;
  margin-top: 30px;
  margin-left: -25px;
}

.el-button--primary {
  background: #1B66FF;
  border-color: #1B66FF;
}

.pager-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
}

.permission-container {
  display: flex;
}
.elTagTitle{
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  margin-bottom: 10px;
}
.elTagInput{
  width: 260px;
  height: 160px;
  border-radius: 4px;
  border: 1px solid #D8DCE6;
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
  overflow-y: auto;
}
.cancleButton {
  width: 114px;
  height: 40px;
  padding: 0;
  background: #F5F5F5;
  border-radius: 8px;
  font-size: 18px;
  color: #1C2748;
}

.confirmButton {
  width: 114px;
  height: 40px;
  padding: 0;
  background: #1B66FF;
  border-radius: 8px;
  font-size: 18px;
  color: #FFFFFF;
}
.left-section {
  flex: 0 0 calc(100% - 310px); /* 100% - (50px + 260px) */
  /*padding-right: 10px; !* 可选：添加右侧间距 *!*/
}
.separator {
  flex: 0 0 50px; /* 宽度为 50px */
}
.right-section {
  flex: 0 0 260px;
  /*padding-left: 10px; !* 可选：添加左侧间距 *!*/
}
/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: rgba(0, 0, 0, 0.88);
}

/deep/ .el-table thead {
  color: rgba(0, 0, 0, 0.88);
}
/deep/ .el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #216EFF;
  border-color: #216EFF;
}
/deep/ .el-tag {
  font-size: 16px;
  background-color: #e8efff;
  color: #216EFF;
}
</style>
