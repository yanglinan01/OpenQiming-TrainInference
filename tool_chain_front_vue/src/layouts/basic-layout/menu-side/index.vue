<template>
  <div>
    <div class="i-layout-sider-logo" :class="{ 'i-layout-sider-logo-dark': siderTheme === 'dark' }">
      <transition name="fade-quick">
        <i-link to="/" v-show="!hideLogo">
          <img src="@/assets/images/logo-small.png" v-if="menuCollapse">
          <img src="@/assets/images/logo.png" v-else-if="siderTheme === 'light'">
          <img src="@/assets/images/logo-dark.png" v-else>
        </i-link>
      </transition>
    </div>
    <Menu
        ref="menu"
        class="i-layout-menu-side i-scrollbar-hide"
        :theme="siderTheme"
        :accordion="menuAccordion"
        :active-name="activePath"
        :open-names="openNames"
        width="auto">
      <template v-if="!menuCollapse" v-for="(item, index) in newFilterSider">
        <div v-if="item.title !='系统管理'">
          <i-menu-side-item
            v-if="item.children === undefined || !item.children.length"
            :menu="item" :key="index"
            @click.native="handleMenuItemClick(item)"/>
        <i-menu-side-submenu v-else :menu="item" :key="index"
                             @click.native="handleMenuItemClick(item)"/>
        </div>
        <div v-if="(item.title == '系统管理') &&hideUserTitle==0">
          <i-menu-side-item
            v-if="item.children === undefined || !item.children.length"
            :menu="item" :key="index"
            @click.native="handleMenuItemClick(item)"/>
        <i-menu-side-submenu v-else :menu="item" :key="index"
                             @click.native="handleMenuItemClick(item)"/>
        </div>

      </template>
      <template v-else>
        <Tooltip :content="tTitle(item.title)" placement="right"
                 v-if="item.children === undefined || !item.children.length" :key="index">
          <i-menu-side-item :menu="item" hide-title
                            @click.native="handleMenuItemClick(item)"/>
        </Tooltip>
        <i-menu-side-collapse v-else :menu="item" :key="index" top-level
                              @click.native="handleMenuItemClick(item)"/>
      </template>
    </Menu>
  </div>
</template>
<script>
import iMenuSideItem from './menu-item';
import iMenuSideSubmenu from './submenu';
import iMenuSideCollapse from './menu-collapse';
import tTitle from '../mixins/translate-title';
import {mapState, mapGetters} from 'vuex';
import util from "@/libs/util";
import {menuClickLogAdd} from "@api/operationCenter";
import {projectUserRoleJudgeManager} from "@api/memberManagement";
import {psQueryList} from "@api/projectSpace";

export default {
  name: 'iMenuSide',
  mixins: [tTitle],
  components: {iMenuSideItem, iMenuSideSubmenu, iMenuSideCollapse},
  props: {
    hideLogo: {
      type: Boolean,
      default: false
    }
  },
  data (){
    return {
      hideUserTitle:localStorage.getItem("systemAuth")||'1',
      options: [],
      spaceValue: localStorage.getItem("projectId"),
      value: '',
      isJudgeManager: false,
      newFilterSider: []
    }
  },
  computed: {
    ...mapState('admin/layout', [
      'siderTheme',
      'menuAccordion',
      'menuCollapse'
    ]),
    ...mapState('admin/menu', [
      'activePath',
      'openNames'
    ]),
    ...mapGetters('admin/menu', [
      'filterSider'
    ])
  },
  watch: {
    $route: {
      handler() {
        this.handleUpdateMenuState();
        this.projectUserRoleJudgeManager();
      },
      immediate: true
    },
    // 在展开/收起侧边菜单栏时，更新一次 menu 的状态
    menuCollapse() {
      this.handleUpdateMenuState();
    }
  },
  created() {
    this.projectUserRoleJudgeManager();
  },
  methods: {
    handleUpdateMenuState() {
      this.$nextTick(() => {
        if (this.$refs.menu) {
          this.$refs.menu.updateActiveName();
          if (this.menuAccordion) this.$refs.menu.updateOpened();
        }
      });
    },
    onAgent() {
      let menuType = localStorage.getItem('menuType')
      if (menuType == 3) {
        let token = localStorage.getItem('toolToken')
        window.open( window.location.origin + "/agent-platform-web" + `/apps?console_token=${token}`, '_blank')
      } else {
        this.$message({
          message: '您还没有权限，请找管理员申请权限',
          type: 'warning'
        });
      }
    },
    createAgent() {
      this.$emit("createAgent");
    },
    handleMenuItemClick(item) {
      let params = {
        menuName: item.title,
        menuUrl: item.path
      }
      menuClickLogAdd(params).then().catch((err) => {
        console.log(err, "失败");
      });
    },
    projectUserRoleJudgeManager(){
      // console.log(556,this.filterSider);
      let params = {
        projectId: localStorage.getItem('projectId'),
        userId: localStorage.getItem('userId'),
      }
      projectUserRoleJudgeManager(params).then((res) => {
        if (res.success) {
          this.isJudgeManager = res.data
          if (!this.isJudgeManager) {
            this.newFilterSider = this.filterSider.filter((item, index) => {
              return item.title != "项目管理"
            });
          }else{
            this.newFilterSider = this.filterSider
          }
          // 判断 newFilterSider[0].header 是否为 'projectSpace'
          if (this.newFilterSider.length > 0 && this.newFilterSider[0].header == 'projectSpace') {
            this.getSpaceSpace(); // 执行 selectSpace()
          }
          // console.log(557,this.newFilterSider);
        } else {
          this.newFilterSider = this.filterSider
          this.$message.error('查询失败：'+res.message);
        }
      }).catch((err) => {
        this.newFilterSider = this.filterSider
        console.log(err, "失败");
      });
    },
    //获取项目空间名称
    getSpaceSpace(){
      // console.log(909090909090)
      let that = this;
      let params = {
        filterMap:{},
        sortingFields: [
          {
            "asc": false,
            "fieldName": "projectType"
          }
        ],
      }
      psQueryList(params).then(res => {
        if (res.success){
          // 将返回的数据映射成 { label, value } 格式
          //跳过个人空间
          that.options = res.data.map(item => ({
            name: item.projectName,
            id: item.projectId,
            projectType: item.projectType
          }))
        } else {
          that.$message.error('卡片列表查询失败');
        }
      })
          .catch((err) => {
            console.log(err, "卡片列表查询失败");
          });
    },
    //项目空间切换跳转
    selectSpaceTab(value){

      const selectedItem = this.options.find(item => item.id === value);
      if (!selectedItem) return;

      console.log("选中的项目：", selectedItem);
      const currentPath = this.$route.path;
        // 存储 projectId 到 localStorage
        localStorage.setItem('projectId', selectedItem.id);

        // 如果已经在 /projecthome 路由，则强制刷新页面
        if (this.$route.path === '/projecthome') {
          location.reload();
        } else {
          this.$router.push('/projecthome');
          location.reload();
        }

      // }

    }
  }
}
</script>
<style lang="less" scoped>
/deep/ .el-icon-plus {
  width: 16px;
  height: 16px;
  border-radius: 2px;
  background: #fff;
  color: #216EFF;
  line-height: 16px;
  font-weight: 600;
  font-size: 10px;
}

.agent {
  padding: 24px;
}
.space{
  padding: 24px;
}



.primaryBtn {
  width: 100%;
  background: #216EFF;
  border-radius: 6px;
}

.cssAgent {
  line-height: 24px;
  padding: 8px 14px;
  cursor: pointer;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.cssAgent:hover {
  color: #2d8cf0;
}

.cssAgentIcon {
  margin-left: 5px;
  margin-right: 13px;
}

.intelligentAgent {
  font-family: YouSheBiaoTiHei;
  font-size: 15px;
}

/deep/ .el-dialog {
  width: 100%;
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
  font-weight: bold;
}

/deep/ .el-dialog__headerbtn {
  top: 0px;
}

/deep/ .el-dialog__body {
  padding: 10px 20px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}

/deep/ .el-form--label-top .el-form-item__label {
  float: none;
  display: inline-block;
  text-align: left;
  padding: 0 0 0px;
  font-weight: bold;
  font-size: 16px;
  color: #000000FF;
}
</style>
