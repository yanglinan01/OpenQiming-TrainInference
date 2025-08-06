<template>
  <div>
    <div class="dataset-details">数据管理</div>
    <div class="dataset-details-block">
      <div class="dataset-details-block-head">
        <el-descriptions :column="2" style="width: 80%;font-family: SourceHanSansSC, SourceHanSansSC;">
          <el-descriptions-item label="数据集名称">{{ dataSetDetials.dataSetName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ dataSetDetials.createDate }}</el-descriptions-item>
          <el-descriptions-item label="数据类型">
            {{ dataTypeMap[dataSetDetials.dataType] }}
          </el-descriptions-item>
          <el-descriptions-item label="数据量">{{ dataSetDetials.prCount }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ dataSetDetials.description }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <el-divider style="border: 1px solid #E8F3FC;"/>
      <div class="dataset-details-block-body">
        <el-table :data="dataSetDetials.contextList" style="width: 100%" v-if="dataSetDetials.dataType==='1'">
          <template v-if="dataSetDetials.setType === '3'">
            <el-table-column prop="index" label="序号" align="center"></el-table-column>
            <el-table-column prop="problem" label="用户问题" align="center"></el-table-column>
            <el-table-column prop="bigModelAnswer" label="回答一" align="center"></el-table-column>
            <el-table-column prop="bigModelAnswer2" label="回答二" align="center"></el-table-column>
            <el-table-column prop="userFeedbackName" label="选择回答"  align="center"></el-table-column>
          </template>
          <template v-else>
            <el-table-column prop="questionId" label="序列" width="100" align="center">
            </el-table-column>
            <el-table-column prop="rank" label="rank" width="100" align="center" v-if="dataSetDetials.setType!=='2'">
            </el-table-column>
            <el-table-column prop="prompt" label="prompt" align="center">
            </el-table-column>
            <el-table-column prop="response" label="response" align="center">
            </el-table-column>
          </template>
        </el-table>
        <el-table :data="dataSetDetials.contextList" style="width: 100%" v-if="dataSetDetials.dataType==='2'">
          <el-table-column prop="id" label="序列" width="100" align="center">
          </el-table-column>
          <el-table-column prop="circuitId" label="链路编码" align="center">
          </el-table-column>
          <el-table-column prop="cirName" label="电路名称" align="center">
          </el-table-column>
          <el-table-column prop="kdevId" label="A端设备编码" align="center">
          </el-table-column>
          <el-table-column prop="devidIp" label="A端设备IP" align="center">
          </el-table-column>
          <el-table-column prop="bdevId" label="B端设备编码" align="center">
          </el-table-column>
          <el-table-column prop="bintfDescr" label="B端端口号" align="center">
          </el-table-column>
          <el-table-column prop="bdevidIp" label="B端设备IP" align="center">
          </el-table-column>
          <el-table-column prop="dinFlux" label="流入速度" align="center">
          </el-table-column>
          <el-table-column prop="doutFlux" label="流出速度" align="center">
          </el-table-column>
          <el-table-column prop="dinFluxRatio" label="流入带宽利用率" align="center">
          </el-table-column>
          <el-table-column prop="doutFluxRatio" label="流出带宽利用率" align="center">
          </el-table-column>
          <el-table-column prop="dcirbw" label="带宽组" align="center">
          </el-table-column>
          <el-table-column prop="tctime" label="采集时间" align="center">
          </el-table-column>
        </el-table>
        <el-table :data="dataSetDetials.contextList" style="width: 100%" v-if="dataSetDetials.dataType==='3'">
          <el-table-column  label="序列" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.$index + (queryParam.currentPage - 1) * queryParam.pageSize + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="questionRole" label="Role" align="center">
          </el-table-column>
          <el-table-column prop="prompt" label="prompt" align="center">
          </el-table-column>
          <el-table-column prop="category" label="Category" align="center">
          </el-table-column>
        </el-table>
      </div>
      <div class="pager-bar">
        <el-pagination
            background
            v-if="paginations.total > 0&&this.$route.query.setType!=='1'"
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
import {queryById} from "@api/dataSet";
import {getDictListByDictType} from "@api/prompt";

export default {
  name: "datasetDetails",
  data() {
    return {
      queryParam: {
        currentPage: 1,
        pageSize: 10,
        dataType: '',
        id: '',
        // 数据集分类(1训练数据集; 2测试数据集;3强化数据集)
        setType: ''
      },
      pageNum: 1,
      paginations: {
        total: 1, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      dataSetDetials:{},
      dataTypeMap: null
    };
  },
  created() {
    this.queryParam.dataType = this.$route.query.dataType;
    this.queryParam.id = this.$route.query.id;
    this.queryParam.setType = this.$route.query.setType;
    this.getDataSetDetials();
    this.getDataType();
  },
  methods: {
    getDataSetDetials() {
      let that = this;
      that.queryParam.pageNum = that.pageNum
      that.dataSetDetials=null
      let params = {
        id: that.queryParam.id,
        dataType: that.queryParam.dataType,
        setType: that.queryParam.setType,
        currentPage: that.queryParam.pageNum,
        pageSize: that.queryParam.pageSize,
      }
      queryById(params).then((res) => {
        if (res.success) {
          that.dataSetDetials = res.data
          that.paginations.total = Number(that.dataSetDetials.total)
          that.pageNum = 1
        } else {
          that.$message.error(res.message);
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });

    },
    getDataType() {
      let that = this;
      let params = {
        dictType: 'DATA_SET_DATA_TYPE',
      }
      getDictListByDictType(params).then((res) => {
        if (res.success) {
          const dataTypeList = res.data.flatMap(item => [{
            key: item.dictItemValue,
            value: item.dictItemLabel
          }])
          that.dataTypeMap = dataTypeList.reduce((acc, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {});
        } else {
          that.$message.error('查询失败');
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },

    handleCurrentChange(val) {
      this.pageNum = val;
      this.queryParam.currentPage=val
      this.getDataSetDetials();
    },
    handleSizeChange(val) {
      this.queryParam.pageSize = val;
      this.getDataSetDetials();
    },
  }

}
;
</script>
<style lang="less" scoped>
.dataset-details {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1c2748;
  margin-bottom: 24px;
}

.dataset-details-block {
  width: 100%;
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;

  .dataset-details-block-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 18px;
    margin-top: 10px;
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    font-size: 16px;
    color: #535456;
    line-height: 24px;
    text-align: left;
    font-style: normal;

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


/deep/ .el-table th.el-table__cell {
  background: #F9F9F9;
}

/deep/ .el-table td.el-table__cell div {
  color: #1C2748;
}
</style>
