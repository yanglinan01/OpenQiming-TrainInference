<template>
  <div>
    <div class="top">
      <div class="myAbilityText">{{ establishPrompt }}</div>
      <el-select v-model="dataTemplate.valueSelected">
        <el-option
            v-for="item in dataTemplate.options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            @click.native="downloadTemplate(item)">
        </el-option>
      </el-select>
    </div>

    <div class="createPrompt">
      <el-form
          :model="primaryData"
          ref="donationForm"
          :rules="rules"
          label-width="180px"
          class="donationForm"
      >
        <el-form-item label="数据来源：" prop="origin">
          <el-radio-group v-model="primaryData.origin"
                          style="display: flex;align-items: center;height: 40px;">
            <el-radio :label="2">上传数据</el-radio>

          </el-radio-group>
        </el-form-item>
        <el-form-item label="数据集类型：" prop="dataType">
          <el-radio-group v-model="primaryData.dataType"
                          style="display: flex;align-items: center;height: 40px;">
<!--            //下一次迭代，需要将按钮修改成从数据库中获取-->
            <el-radio :label="1">Prompt+Response</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="数据集标签：" prop="setType">
          <el-radio-group v-model="primaryData.setType" @change="onSetTypeChanged"
                          style="display: flex;align-items: center;height: 40px;">
            <el-radio :label="1" :disabled="practiceOption">训练数据集</el-radio>
            <el-radio :label="2" :disabled="testOption">测试数据集</el-radio>

          </el-radio-group>
        </el-form-item>

        <el-form-item label="数据集名称：" prop="dataSetName">
          <el-input v-model="primaryData.dataSetName" placeholder="请输入数据集名称"></el-input>
          <div style="color: #84868c;font-size: 12px;">支持中英文、数字、下划线(_)，2-64个字符，不能以_开头</div>
        </el-form-item>

        <el-form-item label="数据集描述：">
          <el-input v-model="primaryData.description" placeholder="请输入数据集描述" type="textarea" :rows="3" maxlength="50"
                    show-word-limit></el-input>
        </el-form-item>
        <div v-if="primaryData.origin==2">
          <el-form-item label="上传数据集：" prop="name">
            <el-upload
                class="upload-demo"
                drag
                action="null"
                ref="upload"
                :on-remove="handleRemove"
                :http-request="upload"
                :file-list="fileList"
                :before-upload="beforeAvatarUpload"

            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将单个文件拖到此处，或<em>点击上传</em>支持xlsx格式文件，文档最大限制100MB。<br>时序数据测试数据集的有效数据不少于1024条，问答对测试数据集不得多于300条
              </div>
            </el-upload>
          </el-form-item>
        </div>
      </el-form>
      <div style="margin-top: 22px;display: flex;justify-content: center;">
        <el-button @click="cancle">取消</el-button>
        <!--          //确认按钮加一个禁用状态-->
        <el-button type="primary" :disabled="submitDisabled" :loading="submitLoading" @click="submit">确定</el-button>
      </div>

    </div>
  </div>
</template>
<script>
import {
  download,
  uploadTemp,
  add,
  cancelAddDataSet,
  deleteByDataSetFileId,
  getDocList,
  addFromKnowledgeBase
} from "@api/dataSet";
import {queryAreaList} from "@api/modelDeploy";
// import {getDictListByDictType} from "@api/modelExperience";

export default {
  name: 'dataset',
  data() {
    return {
      establishPrompt: '创建数据集',
      testOption: false,
      categoryOption: false,
      sequeOption: false,
      practiceOption: false,
      primaryData: {
        origin: 2,
        dataSetName: '',
        dataType: 1,
        description: '',
        setType: 2
      },
      requestId: '',
      fileData: [],
      skipBeforeRemove: false,  // 添加标志变量
      rules: {
        dataSetName: [
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
        ],
        dataType: [
          {required: true, message: "请选择数据集类型", trigger: "change"}
        ],
        origin: [
          {required: true, message: "请选择数据来源", trigger: "change"}
        ],
        setType: [
          {required: true, message: "请选择数据来源", trigger: "change"}
        ],
        name: [
          {
            required: true,
            trigger: "change",
            validator: (rule, value, callback) => {
              if (this.fileData.length === 0) {
                return callback(new Error("请上传数据集"))
              }
              return callback()
            },
          }
        ],
      },
      excelDataloading: false,
      submitLoading: false,
      submitDisabled: false,
      fileList: [],
      isSameNameShow: false,
      fileFormList: [],
      remoteFilePathList: [],
      dataTemplate: {
        valueSelected: '数据模板下载',
        options: [
          {
            value: '1',
            label: 'Prompt+Response_训练.zip'
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
    // this.getDictListByDictType()
    if (this.primaryData.origin === 2) {
      this.generateUniqueID();
    }
  },
  methods: {
    getDocList() {
      if (this.excelDataloading) {
        return;
      }
      this.excelDataloading = true;
      getDocList().then(res => {
        // console.log("vasdgacbxjSBDA")
        if (res.success) {
          this.fileFormList = res.data;
          this.$message.success("获取当前用户远程数据集列表成功");
          this.excelDataloading = false;
        } else {
          this.$message.error(res.message);
          this.excelDataloading = false;
        }
      }).catch(error => {
        this.getListLoading = false
        console.error('测试集列表报错', error);
      });;
    },

    //生成RequestId
    generateUniqueID() {
      const timestamp = new Date().getTime();
      const random = Math.floor(Math.random() * 1000);
      const uniqueID = timestamp.toString() + random.toString();
      this.requestId = uniqueID;
    },
    //生成dataSetFileId
    generateFileID() {
      return new Date().getTime().toString() + Math.floor(Math.random() * 1000).toString();
    },
    //切换数据集标签
    onSetTypeChanged(value) {
      if (this.primaryData.origin === 2) {
        this.fileData = []
        //上传数据，禁用确认按钮
        this.submitDisabled = true;
        // this.generateUniqueID();
        this.primaryData.dataSetName = ''
        this.primaryData.description = ''
        this.primaryData.setType = value

        //考虑到已经上传数据集文件，再点击切换数据集标签的情况
        //已上传的文件全部清空
        let params = {
          requestId: this.requestId
        }
        cancelAddDataSet(params).then((res) => {
        })
      }

    },
    async downloadTemplate(item) {
      try {
        // 调用 download 函数并传递参数
        // console.log('开始下载');
        const response = await download({param: item.value});
        // console.log('回应：',response);
        const blob = new Blob([response]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', item.label);
        document.body.appendChild(link);
        link.click();
        // 清理
        window.URL.revokeObjectURL(url);
        document.body.removeChild(link);
        this.$Message.success("下载成功");
      } catch (error) {
        // 处理下载失败的情况
        console.error('下载文件时出错：', error);
      }
    },

    beforeAvatarUpload(file) {
      let testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "xlsx";
      const isLt100M = file.size / 1024 / 1024 < 100;
      if (!extension) {
        this.$message({
          message: "上传文件只能是xlsx格式！",
          type: "error"
        });
        return false;
      }
      if (!isLt100M) {
        this.$message({
          message: "上传文件大小不能超过100MB！",
          type: "error"
        });
        return false;
      }
      if (this.fileData.length !== 0) {
        const isSameName = this.fileData.find((item) => item.fileName === file.name);
        if (isSameName) {
          this.isSameNameShow = true
          this.$message({
            message: "上传文件不可重复！",
            type: "error"
          });
          return false;
        } else {
          this.isSameNameShow = false
        }
        return extension && isLt100M && !isSameName;
      } else {
        return extension && isLt100M;
      }
    },
    handleRemove(file) {
      // console.log("333", file, this.fileData)
      // console.log(6666, this.isSameNameShow);
      const fileDataItem = this.fileData.find(item => item.uid === file.uid);
      // console.log(1116677, fileDataItem);
      if (fileDataItem || !this.isSameNameShow) {
        const dataSetFileId = fileDataItem.dataSetFileId;
        //调用取消单个数据集文件
        deleteByDataSetFileId({dataSetFileId: dataSetFileId}).then(res => {
          if (res.success) {
            this.fileData = this.fileData.filter(item => item.uid != file.uid);
            this.$message.success("删除成功");
          } else {
            this.$message.error(res.message);
          }
        });
      }
    },
    /**
     * 批量上传—first
     * @param item
     * @returns {Promise<void>}
     */
    async upload(item) {

      try {
        // console.log('开始上传文件', item);
        var formData = new FormData();
        formData.append("file", item.file);
        formData.append("requestId", this.requestId);
        formData.append("dataSetFileId", this.generateFileID());
        formData.append("dataType", this.primaryData.dataType);
        formData.append("setType", this.primaryData.setType);


        const res = await uploadTemp(formData);
        // console.log('调用上传接口', res);
        if (res.success) {
          this.fileData.push({
            fileName: item.file.name,
            dataSetFileId: formData.get('dataSetFileId'),
            uid: item.file.uid
          });
          // console.log('555', this.fileData);
          this.submitDisabled = false;
          this.$message.success("上传成功");
        } else {
          this.fileList = this.fileList.filter(f => f.uid != item.file.uid);
          this.$message.error(res.message);
        }
      } catch (err) {
        this.fileList = this.fileList.filter(f => f.uid != item.file.uid);
        this.$message.error("上传文件时出错");
      }
    },

    //新增数据集
    submit() {
      // if (this.primaryData.origin == 2) {
      let that = this;
      that.$refs["donationForm"].validate(async valid => {
        if (valid) {
          if (that.submitLoading) {
            return;
          }
          that.submitLoading = true
          if (that.primaryData.origin == 2) {
            let params = {
              dataSetName: that.primaryData.dataSetName,
              dataType: that.primaryData.dataType,
              setType: that.primaryData.setType,
              description: that.primaryData.description,
              requestId: this.requestId
            }
            // console.log(22222222, params);
            add(params).then((res) => {

              if (res.success) {
                that.submitLoading = false
                that.$message.success(res.message);
                this.$router.push({path: '/managementCenter/data', query: {isShow: false}})
              } else {
                that.$message.error(res.message);
                that.submitLoading = false
                that.cancle()
              }
            }).catch((err) => {
              that.submitLoading = false
              console.log(err, "失败");
              that.cancle()
            });
          } else if (that.primaryData.origin == 1) {

            let params = {
              dataSetName: that.primaryData.dataSetName,
              dataType: that.primaryData.dataType,
              description: that.primaryData.description,
              //remoteFilePath为val中的ftpPath组成的列表
              // setType: that.primaryData.setType,
              remoteFilePath: that.remoteFilePathList,
            }
            console.log('addFromKnowledgeBase:', params)
            addFromKnowledgeBase(params).then((res) => {
              if (res.success) {
                that.submitLoading = false
                that.$message.success(res.message);
                this.$router.push({path: '/managementCenter/data', query: {isShow: false}})
              } else {
                that.$message.error(res.message);
                that.submitLoading = false
                that.cancle()
              }
            }).catch((err) => {
              that.submitLoading = false
              console.log(err, "失败");
              that.cancle()
            });
          }
        } else {
          return false;
        }
      });

    },
    cancle() {
      if (this.primaryData.origin == 2) {
        let params = {
          requestId: this.requestId
        }
        cancelAddDataSet(params).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.error(res.message);
          }
        })
      }
      // else if (this.primaryData.origin == 1) {
      //
      // }
      this.primaryData = {
        origin: 2,
        dataSetName: '',
        dataType: 1,
        description: '',
        setType: 2
      }
      this.fileData = [],
          this.$router.go(-1);
    }
  },
}
</script>
<style lang="less" scoped>

.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.myAbilityText {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
}


.createPrompt {
  width: 100%;
  background: #FFFFFF;
  border-radius: 8px;
  padding: 24px 0 24px 0;

  .donationForm {
    width: 700px;
    //margin: 0 auto;
    margin: 0 24px 72px 24px;
  }
}

.shili {
  text-align: right;
  line-height: 40px;
  font-size: 14px;
  color: #1C2748;

  span {
    color: #216EFF;
    cursor: pointer;
  }
}

/deep/ .el-form-item__label {
  font-size: 16px;
  color: #1C2748;
}

/deep/ .el-input__inner::-webkit-input-placeholder {
  white-space: pre-wrap; /* 保留换行符 */
}

/deep/ .el-input__inner::placeholder {
  white-space: pre-wrap; /* 保留换行符 */
}

/deep/ .el-button--primary {
  background-color: #216EFF;
  border-color: #216EFF;
  width: 106px;
  height: 40px;
  border-radius: 4px;
  margin-left: 20px;
  font-size: 18px;
  padding: 0;
}

/deep/ .el-button--default {
  background: #EBEBEB;
  border: 1px solid #EBEBEB;
  color: #535456;
  width: 106px;
  height: 40px;
  border-radius: 4px;
  margin-right: 20px;
  font-size: 18px;
  padding: 0;

}

/deep/ .el-upload {
  width: 100%;
}

/deep/ .el-upload-dragger {
  width: 100%;
  height: 200px;
  border: 1px solid #d9d9d9;
}

/deep/ .el-radio__label {
  font-size: 16px;
}
</style>
