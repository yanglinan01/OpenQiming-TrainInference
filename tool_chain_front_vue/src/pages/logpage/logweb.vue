<template>
  <div class="input-container">
    <el-col>

      <div class="input-table">
        <el-row :gutter="10" class="mb8">
          <el-form :model="queryParams" ref="logform" label-width="80px" :inline="true">
            <el-form-item label="客户端IP">
              <el-input
                  class="input-item"
                  placeholder="请输入客户端IP"
                  v-model="queryParams.clientIp"
              ></el-input>
            </el-form-item>
            <el-form-item label="接口姓名">
              <el-input
                  class="input-item"
                  placeholder="请输入接口名称"
                  v-model="queryParams.interfaceName"
              ></el-input>
            </el-form-item>
            <el-form-item label="服务端IP">
              <el-input
                  class="input-item"
                  placeholder="请输入服务端IP"
                  v-model="queryParams.serverIp"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <div class="search-button">
                <el-button class="btn" type="primary" @click="queryPage">查询</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-row>
        <el-row :gutter="10" class="mb8">
          <!--        表格如下-->
          <!--          <div v-for="(item, index) in tableData" :key="index">-->
          <el-table
              :data="tableData"
              style="width: 100%">
            <el-table-column
                prop="interfaceName"
                label="接口名称"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="clientIp"
                label="客户端IP"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="serverIp"
                label="服务端IP"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="requestTime"
                label="请求时间"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="responseTime"
                label="响应时间"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="modifierId"
                label="修改人"
                align="center"
            >
            </el-table-column>
            <el-table-column
                prop="modifyDate"
                label="修改时间"
                align="center">
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    type="text"
                    @click="handleUpdate(scope.row)"
                >详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--          </div>-->
        </el-row>
      </div>
      <!--分页区域-->
      <el-row :gutter="10" class="mb8">
        <div class="block">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryParams.currentPage"
              :page-sizes="[10, 20, 50]"
              :page-size="queryParams.pageSize"
              layout="->,total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>
      </el-row>
    </el-col>

    <!-- 详情框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form class="trainTasks" ref="cardForm" :model="cardForm" label-width="120px">
        <el-row>
          <el-col :span="20">
            <el-form-item class="trainTasks-dialog" label="接口名称：" prop="interfaceName">
              <span class="train-content">{{ cardForm.interfaceName }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="20">
            <el-form-item label="接口入参：" prop="requestParams">
              <span class="train-content">{{ cardForm.requestParams }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="20">
            <el-form-item label="接口出参：" prop="responseParams">
              <span class="train-content">{{ cardForm.responseParams }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="20">
            <el-form-item label="接口执行时长：" prop="duration">
              <span class="train-content">{{ cardForm.duration }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="20">
            <el-form-item label="接口响应编码：" prop="statusCode">
              <span class="train-content">{{ cardForm.statusCode }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row class="trainTasks-dialog">
          <el-col :span="20">
            <el-form-item label="接口错误信息：" prop="errorMessage">
              <span class="train-content">{{ cardForm.errorMessage }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

import {queryPage} from "@api/log";

export default {
  name: "logweb",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 表单参数
      cardForm: {},
      // 重新渲染表格状态
      refreshTable: true,
      queryParams: {
        currentPage: 1,
        pageSize: 10,
        clientIp: undefined,
        interfaceName: undefined,
        serverIp: undefined
      },
      total: 0,
      tableData: null
    };
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询客户列表 */
    getList() {
      this.loading = true;
      // 调用查询接口
      queryPage(this.queryParams).then(response => {
        // console.log(response);
        this.tableData = response.data.rows;
        this.total = response.data.total;
        this.loading = false;
      })
    },
    queryPage() {
      this.queryParams.currentPage = 1;
      console.log('入参：', this.queryParams);
      this.getList();
    },
    // 监听pageSize改变
    handleSizeChange(newSize) {
      this.queryParams.pageSize = newSize;
      this.getList();
    },
    // 监听currentPage改变
    handleCurrentChange(page) {
      this.queryParams.currentPage = page
      this.getList()
    },
    /** 按钮操作 */
    handleUpdate(row) {
      this.open = true
      this.title = '操作日志详细'
      const fullData = this.tableData.find( item=> item.id === row.id)
      // console.log('点击详情按钮，完整数据为：', fullData)
      this.cardForm = {
        interfaceName: fullData.interfaceName,
        requestParams: fullData.requestParams,
        responseParams: fullData.responseParams,
        duration: fullData.duration,
        statusCode: fullData.statusCode,
        errorMessage: fullData.errorMessage
      }
    },
    closeDialog() {
      this.open = false
    }
  }
}
</script>

<style lang="less" scoped>
.input-container {
  display: flex;
  align-items: center;
}

.block {
  text-align: right;
  margin: 27px 0;
}

.trainTasks {
  margin: 0px 0;
  line-height: 1;
  border-radius: 0px;
  text-align: left;
  font-weight: bold;
}

.train-content {
  font-weight: normal;
}

.input-item {
  margin-right: 8px;
}

.btn {
  margin-left: 7px;
}

/deep/ .el-dialog__header {
  border-bottom: 1px solid #eee;
}
</style>
