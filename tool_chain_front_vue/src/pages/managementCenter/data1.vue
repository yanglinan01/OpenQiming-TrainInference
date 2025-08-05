<template>
  <div>
    <div class="te-top">
      <el-button type="primary" @click="establish">创建数据集</el-button>
      <el-select v-model="dataTemplate.valueSelected">
        <el-option
            v-for="item in dataTemplate.options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            @click.native="downloadDataTemplate(item)">
        </el-option>
      </el-select>
    </div>
    <div class="te-cont">
      <div class="te-cont-head">
        <div>
          <el-button :class="{teShow:isshow == true}" @click="systemPrompt">系统数据集</el-button>
          <el-button :class="{teShow:isshow == false}" @click="customPrompt">我的数据集</el-button>
        </div>
        <div class="te-cont-select">
          <el-input placeholder="请输入数据集名称" v-model="villageconfig.name" class="input-with-select"></el-input>
          <i class="el-icon-search" @click="queryList()"></i>
        </div>
      </div>
      <div class="template-card">
        <el-table :data="cardList" style="width: 100%">
          <el-table-column prop="dataSetName" label="数据集名称" align="center">
          </el-table-column>
          <el-table-column prop="dataType" label="数据类型" align="center">
            <template slot-scope="scope">
              {{ dataTypeMap[scope.row.dataType] }}
            </template>
          </el-table-column>
          <el-table-column prop="createDate" label="构建时间" align="center">
          </el-table-column>
          <el-table-column prop="setType" label="类型" align="center">
            <template slot-scope="scope">
               <span :style="{ color:scope.row.setType=='2' ? '#E8850B' : '#0AA64F'  }">
              {{ setTypeMap[scope.row.setType] }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="details(scope.row)">查看</el-button>
              <el-button type="primary" size="mini" @click="cardDelete(scope.row.id)" v-if="isshow == false">删除
              </el-button>
              <el-button type="primary" size="mini" @click="cardUpdate(scope.row)" v-if="isshow == false">修改
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pager-bar">
        <el-pagination
            background
            v-if="paginations.total > 0"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="villageconfig.page"
            :page-sizes="paginations.page_sizes"
            :page-size="villageconfig.size"
            :layout="paginations.layout"
            :total="paginations.total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog title="修改" :visible.sync="updateDialogVisible">
      <div style="text-align: left">
        <el-form :model="updateDataSetReq"
                 ref="donationForm"
                 :rules="rules"
                 label-width="80px"
                 class="donationForm">
          <el-form-item label="原名称">
            <el-input v-model="originalDataSetName" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="新名称" prop="updateDataSetName">
            <el-input v-model="updateDataSetReq.updateDataSetName" placeholder="请输入新名称">
            </el-input>
            <div style="color: #84868c;font-size: 12px;">支持中英文、数字、下划线(_)，2-64个字符，不能以_开头</div>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible= false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {queryPage, deleteById, queryById, updateData, download} from "@api/dataSet";
import {getDictListByDictType} from "@api/prompt";

export default {
  // name: 'data',
  data() {
    return {
      isshow: true,
      villageconfig: {
        page: 1,
        size: 10,
        name: '',
        belong: 1,  //1是系统模版，2是自定义模版
      },
      cardList: [
        {
          dataSetName: '数据集名称',
          createDate: '创建时间',
          id: 1,
          setType: 2,
          dataType: 3
        }
      ],
      paginations: {
        total: 0, // 总数
        page_sizes: [10, 20, 50, 100], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      dialogVisible: false,
      primaryData: {},
      tableData: [],
      updateDialogVisible: false,
      originalDataSetName: '',
      updateDataSetReq: {
        id: '',
        updateDataSetName: ''
      },
      submitLoading:false,
      rules: {
        updateDataSetName: [
          {
            required: true,
            trigger: "blur",
            validator: (rule, value, callback) => {
              let reg = /^(?!_)[0-9a-zA-Z_\u4e00-\u9fa5]{2,64}$/
              if (!value) {
                return callback(new Error("数据集名称不能为空"))
              }
              if (!reg.test(value)) {
                return callback(new Error("支持中英文、数字、下划线(_)，2-64个字符，不能以_开头"))
              }
              return callback()
            },
          }
        ]
      },
      dataTypeMap: null,
      setTypeMap: null,
      dataTemplate: {
        valueSelected: '数据模板下载',
        options: [
          {
            value: '1',
            label: 'Prompt+Response_训练.zip'
          },
          // {
          //   value: '2',
          //   label: 'Prompt+Category.zip'
          // },
          {
            value: '3',
            label: '流量预测.zip'
          },
          {
            value: '4',
            label: 'Prompt+Response_测试.zip'
          }
        ]
      }
    }
  },
  created() {
    if (this.$route.query.isShow == 'false') {
      this.customPrompt()
    } else {
      this.getList()
    }
    this.getDataType()
    this.getSetType()
  },
  methods: {
    queryList() {
      this.villageconfig.page = 1;
      this.getList()
    },
    systemPrompt() {
      this.isshow = true,
          this.villageconfig.belong = 1
      this.cardList = []
      this.paginations.total = 0
      this.getList()
    },
    customPrompt() {
      this.isshow = false
      this.villageconfig.belong = 2
      this.cardList = []
      this.paginations.total = 0
      this.getList()
    },
    async downloadDataTemplate(item) {
      try {
        const response = await download({param: item.value});
        const blob = new Blob([response]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', item.label);
        document.body.appendChild(link);
        link.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(link);
        this.$Message.success("下载成功");
      } catch (error) {
        // 处理下载失败的情况
        console.error('下载文件时出错：', error);
      }

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
    getSetType() {
      let that = this;
      let params = {
        dictType: 'DATA_SET_TYPE',
      }
      getDictListByDictType(params).then((res) => {
        if (res.success) {
          const setTypeList = res.data.flatMap(item => [{
            key: item.dictItemValue,
            value: item.dictItemLabel
          }])
          that.setTypeMap = setTypeList.reduce((acc, cur) => {
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
    getList() {
      let that = this;
      let params = {
        belong: that.villageconfig.belong,
        dataSetName: that.villageconfig.name,
        pageSize: that.villageconfig.size,
        currentPage: that.villageconfig.page,
      }
      queryPage(params).then((res) => {
        if (res.success) {
          that.cardList = res.data.rows
          that.paginations.total = Number(res.data.total)
        } else {
          that.$message.error('查询失败');
        }
      })
          .catch((err) => {
            console.log(err, "失败");
          });
    },
    handleCurrentChange(val) {
      this.villageconfig.page = val;
      this.getList();
    },
    handleSizeChange(val) {
      this.villageconfig.size = val;
      this.getList();
    },
    details(row) {
      const {dataType, id, setType} = row;
      this.$router.push({
        path: '/managementCenter/datasetDetails',
        query: {dataType, id, setType}
      });
    },
    quote() {
      this.dialogVisible = false
    },
    establish() {
      this.$router.push({path: '/managementCenter/dataset', query: {establishPrompt: '创建数据集'}})
    },
    cardDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        confirmButtonClass: 'buttonBg'
      }).then(() => {
        let that = this;
        let params = {
          dataSetId: id,
        }
        deleteById(params).then((res) => {
          if (res.success) {
            that.getList();
            that.$message.success(res.message);
          } else {
            that.$message.error('删除失败');
          }
        })
            .catch((err) => {
              console.log(err, "失败");
            });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    cardUpdate(row) {
      let that = this;
      that.originalDataSetName = row.dataSetName
      that.updateDialogVisible = true
      that.updateDataSetReq.id = row.id
    },
    updateData() {
      let that = this;
      that.$refs["donationForm"].validate(async valid => {
        if (valid) {
          if (that.submitLoading) {
            return;
          }
          that.submitLoading = true
          let params = {
            id: that.updateDataSetReq.id,
            dataSetName: that.updateDataSetReq.updateDataSetName
          }
          updateData(params).then((res) => {
            if (res.success) {
              that.submitLoading = false
              that.getList();
              that.$message.success(res.message);

            } else {
              that.submitLoading = false
              that.$message.error('修改名称失败');
            }

          }).catch((err) => {
            console.log(err, "失败");
            that.submitLoading = false
          });
          that.updateDialogVisible = false
        } else {
          return false;
        }
      });
    }

  }
}
</script>
<style lang="less" scoped>
.buttonBg {
  background-color: #216EFF !important;
  border-color: #216EFF !important;
}
//.donationForm {
//  width: 700px;
//  //margin: 0 auto;
//  margin: 0 24px 72px 24px;
//}
.te-top {
  display: flex;
  justify-content: space-between;
  .el-button--primary {
    background-color: #216EFF;
    border-color: #216EFF;
    margin-bottom: 20px;
  }
}

.teShow {
  background: rgba(33, 110, 255, 0.1);
  border: 1px solid #216EFF;
  color: #216EFF;
}

.te-cont {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 20px 20px 28px;

  .te-cont-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 30px;

    .te-cont-select {
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

  /deep/ .el-tabs__item.is-active {
    color: #216EFF;
  }

  /deep/ .el-tabs__active-bar {
    background-color: #216EFF;
  }

  .tab-block {
    span {
      display: inline-block;
      padding: 0 12px;
      height: 32px;
      border-radius: 16px;
      margin-right: 36px;
      line-height: 32px;
      text-align: center;
      font-family: SourceHanSansSC, SourceHanSansSC;
      font-weight: 400;
      font-size: 14px;
      color: #1C2748;
      cursor: pointer;
    }

    .tab-chenge {
      background: #E5EEFE;
      border-radius: 16px;
      color: #216EFF;
    }
  }

  .template-card {
    margin-top: 37px;
    display: flex;
    flex-wrap: wrap;

    .template-card_coqom {
      width: 375px;
      height: 210px;
      background: url('../../assets/images/promptmubanbg.png') no-repeat;
      margin-right: 12px;
      padding: 26px 32px 0;
      cursor: pointer;

      .card_coqom_head {
        margin-bottom: 25px;
        display: flex;

        .card_coqom_head_img {
          width: 24px;
          height: 24px;
          vertical-align: bottom;
          margin-right: 8px;
        }

        .card_coqom_head_name {
          font-family: SourceHanSansSC, SourceHanSansSC;
          font-weight: 500;
          font-size: 18px;
          color: #1C2748;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .des-line {
        font-family: SourceHanSansSC, SourceHanSansSC;
        font-weight: 400;
        font-size: 14px;
        color: #1C2748;
        line-height: 20px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 3;
        overflow: hidden;
        height: 60px;
      }

      .footer-line {
        img {
          width: 20px;
          height: 20px;
          float: right;
          margin-top: 24px;
          cursor: pointer;
        }
      }
    }
  }

  .pager-bar {
    text-align: right;
    margin: 27px 0;
  }
}

.trainTasks-dialog {
  margin-bottom: 12px;
  border-radius: 4px;
  float: inherit;

  .trainTasks-dialog-left {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 400;
    font-size: 14px;
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
    }
  }
}

/deep/ .el-dialog {
  border-radius: 12px;
}

/deep/ .el-dialog__body {
  padding-top: 20px;
  border-top: 1px solid #EFEDED;
}

.prompt-dialog {
  margin-bottom: 20px;

  .prompt-dialog-left {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 400;
    font-size: 16px;
    color: #1C2748;
    line-height: 24px;
    opacity: 0.7;
  }

  .prompt-dialog-right {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    font-size: 16px;
    color: #1C2748;
    line-height: 24px;
  }
}

.dialog-footer {
  span {
    display: inline-block;
    width: 134px;
    height: 40px;
    border-radius: 4px;
    border: 1px solid #216EFF;
    line-height: 40px;
    text-align: center;
    cursor: pointer;
  }

  .copyprompt {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-size: 18px;
    color: #216EFF;
  }

  .quoteprompt {
    background-color: #216EFF;
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-size: 18px;
    color: #FFFFFF;
    margin-left: 24px;
  }
}

/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #216EFF;
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

/deep/ .el-dialog {
  width: 60%;
  background: #ffffff;
  border-radius: 4px;
}

/deep/ .el-dialog__header {
  width: 100%;
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

.el-table__body, .el-table__footer, .el-table__header {
  width: auto !important;
}
.el-button.is-disabled {
  background: #d0d5e0 !important;
  border-color: #d0d5e0 !important;
  color: #fff !important;
}

</style>
