<template>
  <div class="model-monitoring-container">
    <div class="model-monitoring-block">
      <div @click="goBack">
        <img src="../../assets/images/icon-back.png" alt="左箭头图片"slot="reference" style="vertical-align: middle;">
        <span style="margin-left: 10px;color: #1C2748;opacity: 0.4;vertical-align: middle;">返回模型监控</span></div>
      <div class="model-monitoring-block-title">
        <span style="font-size: 16px;color: #1C2748;">调用日志</span>
      </div>
      <div class="header-content" >
        <el-form :model="primaryData" label-width="100px" style="height:40px;margin-left: -20px;">
          <el-form-item label="模型选择：">
            <el-select v-model="primaryData.modelId" filterable placeholder="请选择" style="min-width:400px;">
              <el-option
                  v-for="item in modelList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  @click.native="modelSelectChange(item.id)">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      <div class="block" style="font-size: 14px">
          <el-date-picker
              v-model="hourlyCallTime"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              @change="modelSelectChange()"
          >
          </el-date-picker>
           </div>
      </div>
      <el-table :data="allModelList" style="width: auto;margin-bottom: 45px;" v-loading="getListLoading">
        <el-table-column prop="sendTime" label="时间" align="center">
        </el-table-column>
        <el-table-column prop="sendMessage" label="入参" align="center">
        </el-table-column>
        <el-table-column prop="responseMessage" label="出参" align="center">
        </el-table-column>
      </el-table>
      <div class="pager-bar">
        <el-pagination
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
  </div>

</template>

<script>
import {queryTrainTaskList} from "@api/trainTask";
import {queryModelChatLog} from "@api/logCenter";
export default {
  name: "logDetail",
  data() {
    return {
      establishPrompt: '',
      primaryData: {
        modelId: ''},
      modelList: [
        // 示例数据
        { id: 1, name: '模型1' },
        { id: 2, name: '模型2' },
        { id: 3, name: '模型3' }
      ],
      // hourlyCallTime:['2024-11-26 00:00:00','2024-11-27 00:00:00'],
      hourlyCallTime: this.getTodayMidnight(),
      allModelList: [],
      getListLoading: false,
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "->,total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      queryParam: {
        pageNum: 1,
        pageSize: 10,
      },

    }
  },
  created() {
    this.queryTrainTaskList()();
  },
  methods: {
    getTodayMidnight() {
      const today = new Date();
      // today.setHours(0, 0, 0, 0);
      const tomorrow = new Date(today);
      tomorrow.setDate(today.getDate() + 1);

      const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day} 00:00:00`;
      };

      // console.log('日期：',[formatDate(today), formatDate(tomorrow)])
      return [formatDate(today), formatDate(tomorrow)];
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
            that.modelSelectChange(that.primaryData.modelId)
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    modelSelectChange() {
      let that = this;
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true;
      let params = {
        filterMap: {
          modelChatId: this.primaryData.modelId,
          // modelChatId: 1839191337673703424,
          startTime: this.hourlyCallTime[0],
          endTime: this.hourlyCallTime[1]
        },
        withDict: true,
        pageParam:{
          ...that.queryParam
        }
      };
      queryModelChatLog(params).then((res) => {
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
    goBack() {
      this.$router.go(-1)
    }
  },
  handleCurrentChange(val) {
    this.queryParam.pageNum = val;
    this.modelSelectChange();
  },
  handleSizeChange(val) {
    this.queryParam.pageSize = val;
    this.modelSelectChange();
  },
}
</script>

<style scoped>
.model-monitoring-container {
  height: auto;

}
.model-monitoring-block {
  width: 100%;
  background: #FFFFFF;
  box-shadow: 0px 2px 32px 0px rgba(196, 209, 223, 0.5);
  border-radius: 16px;
  margin-bottom: 20px;
  display: block;
  padding: 30px;
}
.model-monitoring-block-title{
  font-family: SourceHanSansCN, SourceHanSansCN;
  font-weight: 400;
  font-size: 16px;
  color: #1C2748;
  line-height: 24px;
  text-align: left;
  font-style: normal;
  margin: 30px 0 0 0;
}
.header-content{
  display: flex;
  height: 40px;
  align-items: center;
  justify-content: space-between;
  padding: 0 10px 0 10px;
  margin: 30px 0;

}

.pager-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  /*margin-top: 97px;*/
  /*text-align: center;*/
}
/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}
/deep/.el-table thead {
  color: rgba(0,0,0,0.88);
}
/deep/ .el-form-item__label {
  text-align: right;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: #1C2748;
  line-height: 40px;
  padding: 0 12px 0 0;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
</style>
