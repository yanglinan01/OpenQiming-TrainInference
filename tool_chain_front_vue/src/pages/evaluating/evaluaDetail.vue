<template>
  <div>
    <div class="myAbilityText">{{ establishPrompt }}</div>

    <div class="createPrompt">
      <div class="headerContent">

        <div><span style="color: #55565A;font-weight: 500;">查看评估信息</span></div>

        <div v-if="assessmenting"><span style="color: #216EFF;">系统检测到已完成人工评估</span></div>

      </div>

      <el-table :data="cardList" style="width: 100%" v-loading="getListLoading">
        <el-table-column prop="index" label="序号" align="center">
        </el-table-column>
        <el-table-column prop="problem" label="用户问题" align="center">
        </el-table-column>
        <el-table-column prop="bigModelAnswer" label="大模型答案" align="center">
        </el-table-column>
        <el-table-column prop="standardAnswer" label="标准答案" align="center">
        </el-table-column>
        <el-table-column label="用户反馈" prop="userFeedback" align="center">
          <!--          //此处数据展示为两个单选按钮-->
          <template slot-scope="scope">
            <el-radio-group size="mini" v-model="scope.row.userFeedback" @change="changeAttitude(scope.row.id, $event)">
              <el-radio label="1">赞</el-radio>
              <el-radio label="-1">踩</el-radio>
            </el-radio-group>
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
  </div>
</template>


<script>
import {queryPageDetail,action,isComplete} from "@api/evaluate";
export default {
  name: 'evaluaDetail',
  data() {
    return {
      establishPrompt: '模型评估',
      assessmenting:false,
      establishId:'',
      getListLoading: false,
      queryParam: {
        currentPage: 1,
        pageSize: 20,
      },
      cardList: [],
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },
    }
  },
  created() {
    this.establishId = this.$route.query.establishId;
    this.getList();
    this.getListDetail();
  },
  methods: {
    getListDetail(){

      let params = {
          testSetEvaluationId:this.establishId
      }
      isComplete(params).then(res => {
        if (res.success) {
          this.assessmenting = res.data
          // console.log("问答对测试数据集评估反馈成功");
        } else {
          this.assessmenting = false
          console.error('问答对测试数据集评估反馈报错', error);
        }
      }).catch(error => {
        console.error('问答对测试数据集评估反馈报错', error);
      });


    },
    getList() {
      let that = this;
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true
      let params = {
        filterMap:{
          testSetEvaluationId:that.establishId
        },
        pageParam:{
          pageSize: that.queryParam.pageSize,
          pageNum: that.queryParam.currentPage,
        },
      }
      //测试集获取列表内容
      queryPageDetail(params).then(res => {
        if (res.success) {
          that.cardList = res.data.rows
          that.paginations.total = Number(res.data.total)
          that.getListLoading = false
        } else {
          that.getListLoading = false
          console.error('测试数据集评估详情获取报错', error);
        }
      }).catch(error => {
        that.getListLoading = false
        console.error('测试数据集评估详情获取报错', error);
      });
    },
    changeAttitude(id, value) {
      // 进行其他操作，如修改 label 或更新数据
      let params = {
        id: id,
        action: value,
      }
      // 更新问答对测试数据集评估详情
      action(params).then(res => {
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.error(res.message);
        }
        this.getList()
        this.getListDetail()
      }).catch(error => {
        console.error('测试集列表报错', error);
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
}
</script>
<style lang="less" scoped>


.myAbilityText {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1c2748;
  margin-bottom: 24px;
}

.createPrompt {
  width: 100%;
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;

  .pager-bar {
    text-align: right;
    margin-top: 30px;
  }
}

.headerContent {
  margin-bottom: 16px;
  height: 32px;
  display: flex;
  display: flex;
  justify-content: space-between; /* 使两个模块左右对齐 */
  align-items: center; /* 垂直居中对齐 */
}
/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}

</style>
