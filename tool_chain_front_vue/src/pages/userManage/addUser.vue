<template>
  <div>
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-position="top">
      <el-form-item label="人力账号" prop="employeeNumber">
        <el-input v-model="formData.employeeNumber" placeholder="请输入"></el-input>
<!--        <div style="color: #84868c;font-size: 12px;">支持中英文、数字，不多于20个字</div>-->
      </el-form-item>
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="formData.userName" placeholder="请输入"></el-input>
<!--        <div style="color: #84868c;font-size: 12px;">支持中英文、数字，不多于20个字</div>-->
      </el-form-item>
      <el-form-item label="真实名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入"></el-input>
        <!--        <div style="color: #84868c;font-size: 12px;">支持中英文、数字，不多于20个字</div>-->
      </el-form-item>
      <el-form-item label="省份" prop="regionName">
        <el-input v-model="formData.regionName" placeholder="请输入"></el-input>
<!--        <div style="color: #84868c;font-size: 12px;">支持中英文、数字，不多于20个字</div>-->
      </el-form-item>
      <el-divider></el-divider>
      <div style="text-align: center">
        <el-button @click="onClose(true)" style="background: #F6F6F9;">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="onSubmit()" style="background: #216EFF;">创建
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script>

import {addUser, authQueryPage} from "@api/userManager";

export default {
  name: "addUser",
  data() {
    return {
      submitLoading: false,
      formData: {
        employeeNumber:'',
        userName: '',
        name:'',
        regionName: '',
        // mobile:'',
        toolAuth:'',
        agentAuth:''
      },
      Auth:[],
      rules: {
        employeeNumber: [
          {required: true, message: '请输入人力账号', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        userName: [
          {required: true, message: '请输入用户名，仅支持中文', trigger: 'blur'},
            //正则表达式要求只能为汉字
          {pattern: /^[\u4e00-\u9fa5]+$/, message: '只允许输入中文', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入真实名称，仅支持中文', trigger: 'blur'},
          //正则表达式要求只能为汉字
          {pattern: /^[\u4e00-\u9fa5]+$/, message: '只允许输入中文', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        regionName: [
          {required: true, message: '请输入省份', trigger: 'blur'},
          {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'}
        ]
      }
    };
  },
  methods: {
    onSubmit() {
      let that = this;
      this.$refs["formDataRef"].validate(async valid => {
        if (valid) {
          if (that.submitLoading) {
            return;
          }
          that.submitLoading = true
          if(that.Auth.length==0){
            that.formData.agentAuth=1
            that.formData.toolAuth=1
          }else if(that.Auth.length === 1 && that.Auth.includes(0)){
            that.formData.agentAuth=1
            that.formData.toolAuth=0
          }else if(that.Auth.length === 1 && that.Auth.includes(1)){
            that.formData.agentAuth=0
            that.formData.toolAuth=1
          }else if(that.Auth.length==2){
            that.formData.agentAuth=0
            that.formData.toolAuth=0
          }
          let params = that.formData
          addUser(params).then((res) => {
            if (res.success) {
              that.submitLoading = false
              that.$message.success("新增成功");
              that.onClose()
            } else {
              that.submitLoading = false
              that.$message.error('查询失败');
            }
          })
              .catch((err) => {
                that.submitLoading = false
                console.log(err, "失败");
              });
        } else {
          return false;
        }
      });
    },
    onClose(isCancel) {
      if (isCancel) {
        this.$emit("cancelDialog");
      } else {
        this.$emit("closeDialog");
      }

    }
  }
}
</script>


<style lang="less" scoped>
.el-form-item {
  margin-bottom: 10px;
  height: auto;
}

/deep/ .el-divider--horizontal {
  margin: 24px -24px 9px -24px;
  width: auto;

}
</style>
