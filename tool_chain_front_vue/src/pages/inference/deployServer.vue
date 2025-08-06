<template>
  <div>
    <div class="myAbility">模型推理部署</div>
    <div style="min-width: 1200px;">
      <el-form
          :model="primaryData"
          ref="donationForm"
          :rules="rules"
          class="donationForm"
          label-width="176px"
      >
        <div class="blockBg">
          <el-form-item label="模型选择：" prop="downValue" style="width: 1127px;font-weight: bold;">
            <template>
              <div>
                <el-radio-group v-model="primaryData.selectedValue" @change="queryList(primaryData.selectedValue)">
                  <el-radio :label="item.dictItemValue" v-for="(item, index) in tableData" :key="index">
                    {{ item.dictItemLabel }}
                  </el-radio>
                </el-radio-group>
              </div>
            </template>
            <div class="op-bottom-pullDown">
              <template>
                <el-select style="width: 100%" v-model="primaryData.downValue" placeholder="请选择"
                           @change="handleSelectChange">
                  <el-option
                      v-for="item in options"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </template>
            </div>
          </el-form-item>
        </div>
        <div class="blockBg">
<!--          <p class="parameName" style="font-weight: bold;">资源选择</p>-->

          <div class="square-block" v-if="trainTarget === ''">
            <p class="parameName" style="font-weight: bold;">资源选择</p>
            <div class="square-block-pullDown" style="display: flex;height:40px;margin-top: 21px;margin-left: -40px;">
              <!--              //增加两个下拉框：“地区选择”和“服务器选择”和一个“确认”按钮-->
              <div class="areaSelect-pullDown">
                <el-form-item label="地区选择：" prop="areaSelect" style="width: 400px;font-weight: bold;">
                  <el-select style="width: 100%" v-model="primaryData.areaSelect" placeholder="请选择"
                             @change="areaSelectChange">
                    <el-option
                        v-for="item in areaSelectOptions"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div class="areaSelect-pullDown">
                <el-form-item label="服务器选择：" prop="serverSelect" style="width: 400px;font-weight: bold;">
                  <el-select style="width: 100%" v-model="primaryData.serverSelect" placeholder="请选择"
                             @change="serverSelectChange">
                    <el-option
                        v-for="item in serverSelectOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div>
                <el-button class="deploy-button" type="primary" @click="confirmChange" style="margin-left: 60px;">确认
                </el-button>
              </div>
            </div>

            <div class="intelligent-agent-card-list" v-loading="getListLoading">
              <div class="intelligent-agent-card" v-for="item in intelligentAgentCardList" :key="item.id"
                   @click="selectCard(item)"
                   :class="{ 'selected': item.id === primaryData.serverCardId }">
                <div class="intelligent-agent-card-item-info">
                  <p class="intelligent-agent-card-item-title">{{ item.cardName }}</p>
                  <p class="intelligent-agent-card-item-desc">
                    NPU使用率：{{ item.npuInfo.ctyunClusterNpuUtil ? item.npuInfo.ctyunClusterNpuUtil + '%' : '' }}
                  </p>
                  <p class="intelligent-agent-card-item-desc">
                    NPU温度：{{ item.npuInfo.ctyunClusterNpuTemp ? item.npuInfo.ctyunClusterNpuTemp + '°C' : '' }}
                  </p>
                </div>
              </div>
            </div>
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

          <div class="button-container">
            <el-button class="cancel-button" @click="quxiao">取消</el-button>
            <el-button class="deploy-button" type="primary" :loading="submitLoading" @click="submit">部署</el-button>
          </div>

        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
import {
  getDictListByDictType,
  modelQueryList,
  trainTaskQueryList,
  deployTaskAdd, getDeployServerCardByIp, queryAreaList, deployServerQueryList
} from "@api/modelDeploy";

export default {
  name: 'deployServer',
  data() {
    return {
      getListLoading: false,
      tableData: [],
      options: [],
      areaSelectOptions: [],
      serverSelectOptions: [],
      intelligentAgentCardList: [],
      primaryData: {
        //单选按钮
        selectedValue: '1',
        //modelOptions数据为下拉框点击模型传值
        modelOptions: {
          id: '',
          name: ''
        },
        downValue: '',
        serverCardId: '', // 用于存储选中的卡片ID
        areaSelect: '',  //地区选择选中值
        serverSelect: '',  //服务器选择选中值
      },
      queryParam: {
        currentPage: 1,
        pageSize: 8,
        ip: null,
      },
      paginations: {
        total: 0, // 总数
        page_sizes: [8,16,32,80], //每页显示多少条
        layout: "total, sizes, prev, pager, next, jumper", // 翻页属性
      },
      rules: {
        downValue: [
          {required: true, message: "请选择模型", trigger: "change"}
        ],
        areaSelect: [
          {required: true, message: "请选择地区", trigger: "change"}
        ],
        serverSelect: [
          {required: true, message: "请选择服务器", trigger: "change"}
        ],
      },
      submitLoading: false,
      trainTarget: ''
    }
  },
  created() {
    this.queryAreaList();
    this.getDictListByDictType();
  },
  methods: {
    //获取地区选择的下拉框
    queryAreaList() {
      queryAreaList().then(res => {
        // console.log("11111")
        if (res.success && res.data.length > 0) {
          this.areaSelectOptions = res.data
        } else {
          console.error(res.message);
        }
      }).catch(error => {
        console.error('获取地区选择的下拉框失败', error);
      });
    },
    // 地区选择下拉框,加载出服务器选择下拉框的内容
    areaSelectChange(item) {
      // console.log("22222")
      // console.log("value", item)
      this.primaryData.areaSelect = item
      this.primaryData.serverSelect=''
      // console.log("this.areaSelect", this.primaryData.areaSelect)
      deployServerQueryList({areaName: this.primaryData.areaSelect}).then(res => {
        if (res.success) {
          this.serverSelectOptions = res.data
        } else {
          console.error(res.message);
        }
      }).catch(error => {
        console.error('条件查询部署机器列表报错', error);
      });
    },
    // 服务器选择下拉框
    serverSelectChange(value) {
      // 根据value从options中找到对应的item以获取label
      // console.log("33333")
      const selectedItem = this.serverSelectOptions.find(item => item.id === value);
      if (selectedItem) {
        this.primaryData.serverSelect = selectedItem.name;
        this.queryParam.ip = selectedItem.ip;
        // console.log("this.queryParam", this.queryParam)
      }
    },
    //确认按钮，加载出卡片信息
    confirmChange() {
      let that = this;
      if (that.getListLoading) {
        return;
      }
      that.getListLoading = true
      if (that.primaryData.areaSelect == '') {
        that.$message.error('请选择地区！');
        that.getListLoading = false
        return false
      }
      if (that.primaryData.serverSelect == '') {
        that.$message.error('请选择服务器！');
        that.getListLoading = false
        return false
      }

      let params = {
        currentPage: that.queryParam.currentPage,
        pageSize: that.queryParam.pageSize,
        ip: that.queryParam.ip,
      };
      // console.log('参数：', params)
      getDeployServerCardByIp(params).then((res) => {
        if (res.success) {
          that.intelligentAgentCardList = res.data.rows
          that.paginations.total = Number(res.data.total)
          that.getListLoading = false
        } else {
          that.getListLoading = false
          that.$message.error('卡片查询失败');
        }
      })
          .catch((err) => {
            that.getListLoading = false
            console.log(err, "失败");
          });
    },
    getDictListByDictType() {
      getDictListByDictType({dictType: "MODEL_BELONG"}).then(res => {
        if (res.success && res.data.length > 0) {
          this.tableData = res.data
          this.queryList(this.primaryData.selectedValue)
        } else {
          console.error(res.message);
        }
      }).catch(error => {
        console.error('字典类型查询字典项列表报错', error);
      });
    },
    queryList(selectedValue) {
      this.options = []
      // 清空下拉框中选中内容
      this.primaryData.modelOptions.id = '';
      this.primaryData.modelOptions.name = '';
      if (selectedValue == 1) {
        let params = {
          deployable: 0
        }
        modelQueryList(params).then(res => {
          if (res.success && res.data.length > 0) {
            this.options = res.data
            // 新增：查找isActivate为0的项并设置downValue
            const inactiveOption = this.options.find(option => option.isActivate === '0');
            if (inactiveOption) {
              this.primaryData.downValue = inactiveOption.id;
              this.handleSelectChange(this.primaryData.downValue);
            }
          } else {
            // this.primaryData.modelOptions.id = '';
            // this.primaryData.modelOptions.name = '';
            console.log(res.message);
          }
        }).catch(error => {
          console.error('字典类型查询字典项列表报错', error);
        });

      } else if (selectedValue == 2) {
        this.primaryData.downValue = ''
        let params = {
          status: 'completed'
        }
        trainTaskQueryList(params).then(res => {
          // console.log("111")
          if (res.success && res.data.length > 0) {
            // console.log("222")
            this.options = res.data

            this.handleSelectChange(this.primaryData.downValue);
          } else {
            // console.log("333")
            // this.primaryData.modelOptions.id = '';
            // this.primaryData.modelOptions.name = '';
            console.log(res.message);
          }
        }).catch(error => {
          console.error('字典类型查询字典项列表报错', error);
        });
      }
    },
    handleSelectChange(value) {
      // 根据value从options中找到对应的item以获取label
      const selectedItem = this.options.find(item => item.id === value);
      if (selectedItem) {
        this.primaryData.modelOptions.id = value;
        this.primaryData.modelOptions.name = selectedItem.name;
        this.trainTarget = selectedItem.trainTarget
      }
    },
    //卡片选中
    selectCard(item) {
      this.primaryData.serverCardId = item.id;
      this.primaryData.deployServerId = item.deployServerId;
      // console.log("huanhaunahuan")
    },
    submit() {
      let that = this;
      that.$refs["donationForm"].validate(async valid => {
        if (valid) {
      // let that = this;
      if (that.submitLoading) {
        return;
      }
      that.submitLoading = true
      if (that.trainTarget === '' &&that.primaryData.serverCardId == '') {
        that.$message.error('请选择资源卡片！');
        that.submitLoading = false
        return false
      }
      let params = {
        belong: that.primaryData.selectedValue,
        modelName: that.primaryData.modelOptions.name,
        modelId: that.primaryData.modelOptions.id,
        deployServerId: that.primaryData.deployServerId,
        serverCardId: that.primaryData.serverCardId,

      }
      console.log('参数：', params)
      deployTaskAdd(params).then((res) => {
        if (res.success) {
          // console.log("888888")

          that.submitLoading = false
          that.$message.success('新增成功');
          this.$router.push({path: '/inference/deploy'})
        } else {
          that.submitLoading = false
          that.$message.error(res.message);
          that.quxiao()
        }
      })
          .catch((err) => {
            that.submitLoading = false
            console.log(err, "失败");
            that.quxiao()
          });

        } else {
          return false;
        }
      });
    },
    quxiao() {
      this.primaryData = {
        //单选按钮
        selectedValue: '1',
        //modelOptions数据为下拉框点击模型传值
        modelOptions: {
          id: '',
          name: ''
        },
        downValue: '',
        selectedCardId: '', // 用于存储选中的卡片ID
        areaSelect: '',
        serverSelect: '',
      }
      this.$router.go(-1);
    },
    handleCurrentChange(val) {
      this.queryParam.currentPage = val;
      this.confirmChange();
    },
    handleSizeChange(val) {
      this.queryParam.pageSize = val;
      this.confirmChange();
    },
  },
}
</script>
<style lang="less" scoped>
.myAbility {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
  margin-bottom: 24px;
}

.blockBg {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 24px 0 1px 0;
  margin-bottom: 24px;


  .square-block {
    width: auto;
    border-radius: 8px;
    //padding: 24px;
    //padding: 17px 58px 15px 58px;
    background-color: #FCFCFC;
    height: auto;
    margin: 10px 24px 0;
    display: grid;
    //todo 字体大小不一
    .pager-bar {
      text-align: right;
      margin: 40px 40px 15px 0;

      //.el-pagination {
      //  font-size: 14px;
      //}
      ////pager-bar中的所有字体全部改成14px
      //.el-pagination__total, .el-pagination__jump, .el-pagination__sizes, .el-pagination__prev, .el-pagination__next, .el-pagination__pager {
      //  font-size: 14px;
      //}
    }

    .intelligent-agent-card-list {
      display: flex;
      flex-wrap: wrap;
      padding: 17px 58px 15px 18px;

      .intelligent-agent-card.selected {
        border: 2px solid #216EFF; /* 或者你希望的任何其他蓝色 */
      }

      .intelligent-agent-card {
        background: url('../../assets/images/promptModel.png') no-repeat;
        margin: 12px;
        padding: 20px 32px 0;
        cursor: pointer;
        width: 336px;
        height: 127px;
        box-sizing: border-box;
        display: flex;
        box-shadow: 2px 4px 5px 0px rgba(196, 209, 223, 0.27);
        border-radius: 8px;
        .intelligent-agent-card-item-info {
          width: 300px;
          display: flex;
          flex-direction: column;

          .intelligent-agent-card-item-title:before {
            content: "";
            //display: inline-block;
            width: 8px;
            height: 8px;
            background-color: #216EFF;
            margin-right: 8px; /* 可选，为了与文本保持一点间距 */
            transform: rotate(45deg); /* 旋转矩形90度 */
            //vertical-align: middle; /* 使矩形与文本垂直居中对齐 */
          }

          .intelligent-agent-card-item-title {
            display: flex;
            align-items: center; /* 垂直居中对齐 */
            width: auto;
            height: 27px;
            overflow-wrap: break-word;
            color: rgba(28, 39, 72, 1);
            font-size: 18px;
            font-family: SourceHanSansSC-Medium;
            font-weight: 800;
            text-align: left;
            white-space: nowrap;
            margin-top: 0;
            margin-bottom: 10px;
          }

          p.intelligent-agent-card-item-desc {
            width: 260px;
            height: auto;
            color: rgba(28, 39, 72, 1);
            font-size: 14px;
            font-family: SourceHanSansSC-Regular;
            font-weight: normal;
            text-align: left;
            margin: 5px 0 0 0;
          }
        }
      }

    }

  }

  /deep/ .el-radio__label {
    font-size: 16px;
  }

  /deep/ .el-input__inner {
    font-size: 16px;
  }
}


/deep/ .el-dialog {
  width: 540px;
  background: #ffffff;
  border-radius: 4px;
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

  .trainTasks-dialog-content {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
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

//按钮格式
.button-container {
  margin: 24px 0;
  display: flex;
  justify-content: center;
  gap: 24px;
}

.cancel-button {
  width: 106px;
  height: 40px;
  background: #EBEBEB;
  border-radius: 4px;
  display: flex; /* 添加此行以启用Flexbox布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  text-align: center; /* 对于文本内容，确保水平居中 */
  font-size: 18px;
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: 500;
  color: #535456;
  font-style: normal;
}

.deploy-button {
  width: 106px;
  height: 40px;
  background: #216EFF;
  border-radius: 4px;
  display: flex; /* 添加此行以启用Flexbox布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  text-align: center; /* 对于文本内容，确保水平居中 */
  font-size: 18px;
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: 500;
  color: #FFFFFF;
  font-style: normal;
}

.parameName {
  margin: 0 24px 10px;
  padding-bottom: 18px;
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: 500;
  font-size: 16px;
  color: #1C2748;
}

.parameTable {
  width: 948px;
  height: 254px;
  background: #F7F8F9;
  border-radius: 2px;
  padding: 0 16px;
  overflow: hidden;
  position: relative;
}

.parameHeight {
  height: auto !important;
}

.paramepo {
  position: inherit !important;
  z-index: 9;
  margin-top: -1px;
}

.parameTableImg {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 948px;
  height: 46px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #F7F8F9;

  img {
    width: 14px;
    height: 14px;
    cursor: pointer;
  }
}

.parameText {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-size: 16px;
  color: rgba(28, 39, 72, 0.7);
  margin-left: 90px;
  margin-bottom: 5px;
}

/deep/ .el-table td.el-table__cell {
  background: #F7F8F9;
  height: 81px;
}

/deep/ .el-table th.el-table__cell.is-leaf {
  background: #F7F8F9;
  text-align: center;
}

/deep/ .el-form-item__label {
  font-size: 16px;
  color: #1C2748;
}

/deep/ .el-button {
  font-size: 18px;
}

/deep/ .el-table {
  border-radius: 2px;
}

/deep/ .el-table .cell {
  color: rgba(28, 39, 72, 0.8);
}

/deep/ .changeData .el-input__inner {
  padding-left: 42px;
}

/deep/ .el-button--default {
  background: #EBEBEB;
  border: 1px solid #EBEBEB;
  color: #535456;
}

.KeyUpcss {
  color: #f33e3e;
  font-size: 12px;
}
</style>
