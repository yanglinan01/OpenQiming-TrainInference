<template>
  <div>
    <div class="logcenter-block">
      <div class="block-title">接口调用统计</div>
      <div class="row-box">
        <div><strong style="color:red">*</strong>模型选择：</div>
        <el-select v-model="mTaskId" style="width:200px" @change="getModelRequestList">
          <el-option v-for="item in mModelList" :value="item.id" :label="item.name" :key="item.id"></el-option>
        </el-select>

        <el-radio-group class="row-flex" v-model="mIntfCallType" style="margin-left: 20px;" @change="getModelRequestList">
            <el-radio-button :label="2">全部</el-radio-button>
            <el-radio-button :label="0">成功</el-radio-button>
            <el-radio-button :label="1">失败</el-radio-button>
        </el-radio-group>

        <el-date-picker
          v-model="mIntfCallDate"
          style="margin-left: 20px;"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00', '23:59:59']"
          @change="getModelRequestList">
        </el-date-picker>
      </div>

      <el-table
          :data="mRowList"
          style="width: 96%; margin: 20px 3%;"
          border
          element-loading-text="拼命加载中">
          <el-table-column align="center" prop="taskName" label="模型" show-overflow-tooltip />
          <el-table-column align="center" prop="intfCallDate" label="时间" show-overflow-tooltip />
          <el-table-column align="center" prop="intfCallTypeStr" label="调用状态" show-overflow-tooltip>
            <template #default="scope">
              <div :class=" scope.row.intfCallTypeStr=='成功' ? 'text-green' : 'text-red'" >{{ scope.row.intfCallTypeStr }}</div>
            </template>
          </el-table-column> 
          <el-table-column align="center" prop="remark" label="备注" show-overflow-tooltip />
        </el-table>

        <el-pagination
          v-if="mRowList.length > 0"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="mCurrentPage"
          :page-size="mPageSize"
          :total="mTotalPages"
          layout="->,total, sizes, prev, pager, next, jumper">
        </el-pagination>

    </div>

  </div>
</template>
<script>

import { getLogModelList, queryModelRequestList } from "@api/logCenter"

export default {
  name: 'modelMonitorList',
  data() {
    return {
      mModelList: [],
      
      mTaskId: "",
      mIntfCallType: 2,//接口调用类型：默认0 接口调用状态;0:成功,1:失败,2:全部
      mIntfCallDate: [],//查询天数：默认1 1：今天 2：近2天 3：近3天 7:近7天 15:近15天

      mCurrentPage: 1,
      mPageSize: 10,
      mTotalPages: 1,
      mRowList: [],
    };
  },
  created() {
    this.mIntfCallDate = this.getDefaultDate();
    getLogModelList({status:"completed"}).then(res => {
      if(res.success){
        this.mModelList = [];
        for(let i = 0; i < res.data.length; i++){
          let item = res.data[i];
          this.mModelList.push({id: item.id, name: item.name})
        }
        if(this.mModelList.length>0){
          this.mTaskId = this.mModelList[0].id;
        }else{
          this.mTaskId = null;
        }
        this.getModelRequestList();
      }
    });
  },

  methods: {
    // 监听pageSize改变
    handleSizeChange(newSize) {
      this.mPageSize = newSize;
      this.getModelRequestList();
    },
    // 监听currentPage改变
    handleCurrentChange(page) {
      this.mCurrentPage = page
      this.getModelRequestList()
    },

    getDefaultDate(){
      let now = new Date();
      let year = now.getFullYear();
      let month = now.getMonth() + 1;
      let day = now.getDate();
      let date = year + "-" + month + "-" + day;
      return [date + " 00:00:00", date + " 23:59:59"]
    },
    
    getModelRequestList(){
      let params = {
        intfCallType: this.mIntfCallType,
        currentPage: this.mCurrentPage,
        pageSize: this.mPageSize,
        startTime: this.mIntfCallDate[0],
        endTime: this.mIntfCallDate[1],
        intfCallType: this.mIntfCallType,
        taskId: this.mTaskId,
      }
      queryModelRequestList(params).then(res => {
        if(res.success){
          this.mRowList = res.data.rows;
          this.mTotalPages = parseInt(res.data.pages);
        }
      });
    },

  }
}
</script>
<style lang="less" scoped>
.logcenter-block {
  width: 100%;
  border-radius: 8px;
  padding: 10px;
  margin-top: 10px;
  background-color: rgba(255, 255, 255, 1);
  .block-title{
    font-weight: bold;
    font-size: 16px;
  }

  .row-box{
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 10px;
    .row-flex{
      display: flex;
      flex: 1;
      justify-content: right;
    }
  }

  .text-green{
    color: green;
  }
  .text-red{
    color: red;
  }
}
</style>
