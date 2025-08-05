<template>
  <div>
    <el-card class="TopCard" style="height:40px;">
      <!-- 小喇叭 -->
      <img src="../../assets/images/announcement.png" alt="" class="announcement">
      <div class="notice-container" id="notice-container">
        <div class="notice-wrapper" id="notice-wrapper">
          <div class="notice-content" id="notice-content">
            <p>{{ showNotice }}</p>
          </div>
        </div>
      </div>
    </el-card>
    <div class="myAbility">我的能力</div>
    <div class="ability">
      <div class="abilityblock">
        <img src="../../assets/images/ability1.png" alt="" class="abilityImg">
        <div>
          <p class="abilityName">可调用模型</p>
          <p class="abilityNumb">{{ count.availableModel || 0 }}</p>
        </div>
      </div>
      <div class="abilityblock">
        <img src="../../assets/images/ability2.png" alt="" class="abilityImg">
        <div>
          <p class="abilityName">Prompt模板</p>
          <p class="abilityNumb">{{ count.prompt || 0 }}</p>
        </div>
      </div>
      <div class="abilityblock">
        <img src="../../assets/images/ability3.png" alt="" class="abilityImg">
        <div>
          <p class="abilityName">模型训练任务</p>
          <p class="abilityNumb">{{ count.trainTask || 0 }}</p>
        </div>
      </div>
      <div class="abilityblock" style="margin-right: 0px;">
        <img src="../../assets/images/ability4.png" alt="" class="abilityImg">
        <div>
          <p class="abilityName">可用插件</p>
          <p class="abilityNumb">{{ count.availablePlugin || 0 }}</p>
        </div>
      </div>
    </div>
    <div class="beginnerGuidance">新手引导</div>
    <div class="beginnerGuidanceBlock">
      <div class="structureTop">
        <img src="../../assets/images/goujian.png" alt="" class="structureImg">
        <span class="structureName">提示工程构建</span>
      </div>
      <div class="prompt">
        <div class="promptImg" @click="promptTemplate">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">Prompt 模板</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">可选择系统提供prompt模板，支持自主构建prompt模板</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
        <div class="promptImg" @click="promptOptimization">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">Prompt 优化</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">面向自建Prompt模板提供内容质量、结构优化功能</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
        <div class="promptImg" >
<!--             @click="promptAssess">-->
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">Prompt 评估</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">提供Prompt模板评估功能，分析可优化的内容</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
      </div>
    </div>
    <div class="beginnerGuidanceBlock">
      <div class="structureTop">
        <img src="../../assets/images/goujian.png" alt="" class="structureImg">
        <span class="structureName">模型训练</span>
      </div>
      <div class="prompt">
        <div class="promptImg" @click="toolData">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">准备数据集</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">根据模型训练需求，准备训练数据集</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
        <div class="promptImg" @click="toolParameters">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">配置训练任务</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">选择模型训练方式和训练数据，配置训练参数，开启模型训练</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
        <div class="promptImg" @click="toolTrain">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">查看训练结果</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">查看模型训练状态与训练结果</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
      </div>
    </div>
    <div class="beginnerGuidanceBlock">
      <div class="structureTop">
        <img src="../../assets/images/goujian.png" alt="" class="structureImg">
        <span class="structureName">模型部署</span>
      </div>
      <div class="prompt">
        <div class="promptImg" @click="modelExp">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">模型测评</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">对完成训练的模型进行测评/体验，满足需求后进行模型部署</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
        <div class="promptImg" @click="modelayOut">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">模型部署</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">进入部署页面选择待部署模型、部署服务器，勾选应用插件</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
        <div class="promptImg" @click="modelTaskManage">
          <p class="promptNameTop">
            <span class="promptDain"></span>
            <span class="promptName">部署任务管理</span>
          </p>
          <div class="promptCont">
            <p class="promptContP">可查看模型部署位置，模型部署时间，部署状态</p>
            <img class="promptContImg" src="../../assets/images/more.png" alt="">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {getMyAbilitiesByUserId} from "@api/prompt";
import {getAnnouncementContent} from "@api/announcement";
import { create } from "lodash";

export default {
  name: 'home',
  data() {
    return {
      count: {},
      // 公告展示（过长时会重复两遍）
      showNotice: '',
    }
  },
  created() {
    let toolToken = location.search.split('Authorization=')[1] || util.cookies.get('cportal')
    localStorage.setItem('toolToken',toolToken)
  },
  mounted() {
    setTimeout(() => {
      this.getMyAbilitiesByUserId()
    }, 300);

    //公告栏
    this.getnoticeData()
  },
  methods: {
    /**
     * 获取公告数据
     * 从后端获取公告内容，成功时更新公告数据，失败时显示错误消息
     */
    getnoticeData() {
      getAnnouncementContent().then((res) => {
        if (res.success) {
          this.noticeData = res.data
          // this.noticeData = ['你好啊你好你好你好啊你好你好你好年后世界第四哈佛结构i运行正你好啊你好你好你好啊你好你好你好构i运行正你好啊构i运行正你好啊构i运行正你好啊年后世界第四大v哈佛', '公告二']
          // 遍历公告数据，格式化显示内容
          this.noticeData.forEach((val, index) => {
            if (index === 0) {
              this.showNotice += '【' + (index + 1) + '】' + val;
            } else {
              this.showNotice += '\xa0\xa0\xa0\xa0\xa0\xa0【' + (index + 1) + '】' + val;
            }
          });
          this.$nextTick(() => {
            let noticeContainer = document.getElementById('notice-container').offsetWidth
            let noticeContent = document.getElementById('notice-content').offsetWidth
            if (noticeContent > noticeContainer) {
              let duration = this.showNotice.length * 500 + 'ms'; // 假设每个字符需要50ms来滚动
              document.getElementById('notice-wrapper').style.animationDuration = duration;
            }
          })
        } else {
          this.$message.error(res.message);
        }
      }).catch((err) => {
        console.log(err, "失败");
      });
    },
    getMyAbilitiesByUserId() {
      let that = this;
      getMyAbilitiesByUserId().then((res) => {
        if (res.success) {
          that.count = res.data
        } else {
          that.$message.error(res.message);
        }
      }).catch((err) => {
        console.log(err, "失败");
      });
    },
    promptTemplate() {
      this.$router.push('/auth/template')
    },
    promptOptimization() {
      this.$router.push('/auth/optimization')
    },
    promptAssess() {
      this.$router.push('/auth/assess')
    },
    toolData() {
      this.$router.push('/managementCenter/data')
    },
    toolParameters() {
      this.$router.push('/train/tasks')
    },
    toolTrain() {
      this.$router.push('/train/tasks')
    },
    modelExp() {
      this.$router.push('/evaluating/index')
    },
    modelayOut() {
      this.$router.push('/train/tasks')
    },
    modelTaskManage() {
      this.$router.push('/train/tasks')
    }
  }
}
</script>
<style lang="less" scoped>

/* 公告card */
.TopCard {
  background: rgba(33,110,255,0.06);
  box-shadow: 0px 2px 4px 0px rgba(128,177,243,0.14);
  border-radius: 20px;
  .announcement{
    margin-right: 10px;
    margin-top: 6px;
  }
  /deep/ .el-card__body {
    padding: 0 40px;
    display: flex;
    align-items: end;
  }
}
.notice-container {
  overflow: hidden;
  white-space: nowrap;
  position: relative;
  width: 100%;
}

.notice-wrapper {
  display: inline-block;
  animation-name: scroll-left;          /* 引用定义的关键帧动画名称 */
  animation-timing-function: linear; /* 设置动画速度函数为线性 */
  animation-iteration-count: infinite; /* 动画无限重复播放 */
  // animation: scroll-left 30s linear infinite;
}

@keyframes scroll-left {
  from {
    transform: translateX(30%);
  }
  to {
    transform: translateX(-100%);
  }
}

.notice-content {
  width: 100%;
  display: inline-block;
  // padding-left: 10px;
  font-size: 14px;
  color: #1B66FF;
}


.myAbility {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
  margin-top: 30px;
}

.ability {
  display: flex;

  .abilityblock {
    flex: 1;
    height: 136px;
    min-width: 318px;
    background: linear-gradient(180deg, #DEE7F5 0%, #FFFFFF 100%);
    box-shadow: 0px 5px 7px 0px rgba(180, 190, 215, 0.24);
    border-radius: 4px;
    border: 2px solid;
    border-image: linear-gradient(180deg, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0.31)) 2 2;
    margin-right: 25px;
    margin-top: 17px;
    display: flex;
    align-items: center;
  }

  .abilityImg {
    width: 120px;
    height: 120px;
    margin-left: 50px;
    margin-right: 40px;
  }

  .abilityName {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    font-size: 18px;
    color: #1C2748;
  }

  .abilityNumb {
    font-family: SourceHanSansSC, SourceHanSansSC;
    font-weight: 500;
    font-size: 30px;
    color: #216EFF;
    margin-top: 10px;
  }
}

.beginnerGuidance {
  font-family: SourceHanSansSC, SourceHanSansSC;
  font-weight: bold;
  font-size: 20px;
  color: #1C2748;
  margin-top: 40px;
  margin-bottom: 24px;
}

.beginnerGuidanceBlock {
  width: 100%;
  min-width: 1285px;
  height: 268px;
  background: #FFFFFF;
  border-radius: 10px;
  padding: 27px 38px 0;
  margin-bottom: 16px;

  .structureTop {
    display: flex;
    align-items: center;
    margin-bottom: 26px;

    .structureImg {
      width: 32px;
      height: 32px;
      margin-right: 14px;
    }

    .structureName {
      font-family: SourceHanSansSC, SourceHanSansSC;
      font-weight: 500;
      font-size: 18px;
      color: #1C2748;
    }
  }

  .prompt {
    display: flex;
    justify-content: space-between;

    .promptImg {
      min-width: 412px;
      height: 167px;
      background: url('../../assets/images/muban.png') no-repeat;
      padding: 26px 36px 0;
      cursor: pointer;

      .promptNameTop {
        margin-bottom: 19px;

        .promptDain {
          display: inline-block;
          width: 14px;
          height: 14px;
          background: #216EFF;
          opacity: 0.34;
          margin-right: 10px;
          border-radius: 14px;
        }

        .promptName {
          font-family: SourceHanSansSC, SourceHanSansSC;
          font-weight: bold;
          font-size: 18px;
          color: #216EFF;
        }
      }

      .promptCont {
        position: relative;

        .promptContP {
          width: 278px;
          font-family: SourceHanSansSC, SourceHanSansSC;
          font-weight: 400;
          font-size: 16px;
          color: #1C2748;
        }

        .promptContImg {
          width: 20px;
          height: 20px;
          position: absolute;
          right: 0;
          top: 0;
        }
      }
    }
  }
}
</style>
