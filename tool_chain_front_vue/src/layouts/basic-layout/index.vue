<template>
    <Layout class="i-layout">
        <Sider v-if="!isQianKun && !isMobile && !hideSider" class="i-layout-sider" :class="siderClasses" :width="menuSideWidth">
            <i-menu-side :hide-logo="isHeaderStick && headerFix && showHeader" @createAgent="createAgent()"/>
        </Sider>
        <Layout class="i-layout-inside" :class="insideClasses">
            <transition name="fade-quick">
                <Header v-if="!isQianKun" class="i-layout-header" :class="headerClasses" :style="headerStyle" v-show="showHeader" v-resize="handleHeaderWidthChange">
                    <!-- <i-header-logo v-if="isMobile && showMobileLogo" />
                    <i-header-logo v-if="!isMobile && isHeaderStick && headerFix" />
                    <i-header-collapse v-if="(isMobile || showSiderCollapse) && !hideSider" @on-toggle-drawer="handleToggleDrawer" />
                    <i-header-reload v-if="!isMobile && showReload" @on-reload="handleReload" />
                    <i-menu-head v-if="headerMenu && !isMobile" ref="menuHead" />
                    <i-header-breadcrumb v-if="showBreadcrumb && !headerMenu && !isMobile" ref="breadcrumb" />
                    <i-header-search v-if="showSearch && !headerMenu && !isMobile && !showBreadcrumb" />
                    <div class="i-layout-header-right">
                        <i-header-search v-if="(showSearch && isMobile) || (showSearch && (headerMenu || showBreadcrumb))" />
                        <i-menu-head v-if="headerMenu && isMobile" />
                        <i-header-log v-if="isDesktop && showLog" />
                        <i-header-fullscreen v-if="isDesktop && showFullscreen" />
                        <i-header-notice v-if="showNotice" />
                        <i-header-user />
                        <i-header-i18n v-if="showI18n" />
                        <i-header-setting v-if="enableSetting && !isMobile" />
                    </div> -->
                    <div class="i-layout-header-top">
                        <div class="i-layout-header-top-left">启明网络大模型工具链</div>
                        <div class="i-layout-header-top-right">
                            <img src="../../assets/images/fanhui.png" alt="" class="i-layout-header-top-right-fanhuiImg" @click="fanhui">
                            <span class="i-layout-header-top-right-fanhui" @click="fanhui()">返回首页</span>
                            <span class="i-layout-header-top-right-line">|</span>
                            <img src="../../assets/images/touxiang.png" alt="" class="i-layout-header-top-right-touxiangImg">
                            <span class="i-layout-header-top-right-touxiang">{{ name }}</span>
                        </div>
                    </div>
                </Header>
            </transition>
            <Content class="i-layout-content" :class="contentClasses">
                <transition name="fade-quick">
                    <i-tabs v-if="tabs" v-show="showHeader" @on-reload="handleReload" />
                </transition>
                <div class="i-layout-content-main" :style="{ margin: !isQianKun ? '' : tabs ? '44px 0 0 0' : '0' }">
                    <keep-alive :include="keepAlive">
                        <router-view v-if="loadRouter" />
                    </keep-alive>
                </div>
            </Content>
            <!-- <i-copyright /> -->
        </Layout>
        <div v-if="isMobile && !hideSider">
            <Drawer v-model="showDrawer" placement="left" :closable="false" :class-name="drawerClasses">
                <i-menu-side @createAgent="createAgent()"/>
            </Drawer>
        </div>
    </Layout>
</template>
<script>
    import iMenuHead from './menu-head';
    import iMenuSide from './menu-side';
    import iHeaderLogo from './header-logo';
    import iHeaderCollapse from './header-collapse';
    import iHeaderReload from './header-reload';
    import iHeaderBreadcrumb from './header-breadcrumb';
    import iHeaderSearch from './header-search';
    import iHeaderLog from './header-log';
    import iHeaderFullscreen from './header-fullscreen';
    import iHeaderNotice from './header-notice';
    import iHeaderUser from './header-user';
    import iHeaderI18n from './header-i18n';
    import iHeaderSetting from './header-setting';
    import iTabs from './tabs';
    import iCopyright from '@/components/copyright';

    import { mapState, mapGetters, mapMutations } from 'vuex';
    import Setting from '@/setting';

    import util, { requestAnimation } from '@/libs/util';
    import { getCurrentUserInfo } from "@api/prompt";

    // import addPage from '../../pages/application/add.vue'
    export default {
        name: 'BasicLayout',
        components: { iMenuHead, iMenuSide, iCopyright, iHeaderLogo, iHeaderCollapse, iHeaderReload, iHeaderBreadcrumb, iHeaderSearch, iHeaderUser, iHeaderI18n, iHeaderLog, iHeaderFullscreen, iHeaderSetting, iHeaderNotice, iTabs, },
        data () {
            return {
                showDrawer: false,
                ticking: false,
                headerVisible: true,
                oldScrollTop: 0,
                isDelayHideSider: false, // hack，当从隐藏侧边栏的 header 切换到正常 header 时，防止 Logo 抖动
                loadRouter: true,
                name: '',
                dialogShow: false
            }
        },
        computed: {
            ...mapState('admin/layout', [
                'siderTheme',
                'headerTheme',
                'headerStick',
                'tabs',
                'tabsFix',
                'siderFix',
                'headerFix',
                'headerHide',
                'headerMenu',
                'isQianKun',
                'isMobile',
                'isTablet',
                'isDesktop',
                'menuCollapse',
                'showMobileLogo',
                'showSearch',
                'showNotice',
                'showFullscreen',
                'showSiderCollapse',
                'showBreadcrumb',
                'showLog',
                'showI18n',
                'showReload',
                'enableSetting'
            ]),
            ...mapState('admin/page', [
                'keepAlive'
            ]),
            ...mapGetters('admin/menu', [
                'hideSider'
            ]),
            // 如果开启 headerMenu，且当前 header 的 hideSider 为 true，则将顶部按 headerStick 处理
            // 这时，即使没有开启 headerStick，仍然按开启处理
            isHeaderStick () {
                let state = this.headerStick;
                if (this.hideSider) state = true;
                return state;
            },
            showHeader () {
                let visible = true;
                if (this.headerFix && this.headerHide && !this.headerVisible) visible = false;
                return visible;
            },
            headerClasses () {
                return [
                    `i-layout-header-color-${this.headerTheme}`,
                    {
                        'i-layout-header-fix': this.headerFix,
                        'i-layout-header-fix-collapse': this.headerFix && this.menuCollapse,
                        'i-layout-header-mobile': this.isMobile,
                        'i-layout-header-stick': this.isHeaderStick && !this.isMobile,
                        'i-layout-header-with-menu': this.headerMenu,
                        'i-layout-header-with-hide-sider': this.hideSider || this.isDelayHideSider
                    }
                ];
            },
            headerStyle () {
                const menuWidth = this.isHeaderStick ? 0 : this.menuCollapse ? 80 : Setting.menuSideWidth;
                return this.isMobile || !this.headerFix ? {} : {
                    width: `calc(100% - ${menuWidth}px)`
                };
            },
            siderClasses () {
                return {
                    'i-layout-sider-fix': this.siderFix,
                    'i-layout-sider-dark': this.siderTheme === 'dark'
                };
            },
            contentClasses () {
                return {
                    'i-layout-content-fix-with-header': this.headerFix,
                    'i-layout-content-with-tabs': this.tabs,
                    'i-layout-content-with-tabs-fix': this.tabs && this.tabsFix,
                    'i-layout-content-clear-padding-top-with-header': this.isQianKun
                };
            },
            insideClasses () {
                return {
                    'i-layout-inside-fix-with-sider': this.siderFix,
                    'i-layout-inside-fix-with-sider-collapse': this.siderFix && this.menuCollapse,
                    'i-layout-inside-with-hide-sider': this.hideSider,
                    'i-layout-inside-mobile': this.isMobile,
                    'i-layout-inside-clear-padding-left-with-sider': this.isQianKun
                };
            },
            drawerClasses () {
                let className = 'i-layout-drawer';
                if (this.siderTheme === 'dark') className += ' i-layout-drawer-dark';
                return className;
            },
            menuSideWidth () {
                return this.menuCollapse ? 80 : Setting.menuSideWidth;
            }
        },
        watch: {
            hideSider () {
                this.isDelayHideSider = true;
                setTimeout(() => {
                    this.isDelayHideSider = false;
                }, 0);
            },
            '$route' (to, from) {
                if (to.name === from.name) {
                    // 相同路由，不同参数，跳转时，重载页面
                    if (Setting.sameRouteForceUpdate) {
                        this.handleReload();
                    }
                }
            }
        },
        methods: {
            getCurrentUserInfo(){
                let that = this;
                getCurrentUserInfo().then((res) => {
                    if(res.success){
                        that.name = res.data.name
                        localStorage.setItem('systemAuth',res.data.systemAuth)
                        localStorage.setItem('menuType',res.data.menuType)
                        localStorage.setItem('userId',res.data.id)
                        localStorage.setItem('userInfo',JSON.stringify(res.data))
                        if(res.data.menuType == 0){
                            this.$message({
                                message: '您还没有权限，请找管理员申请权限，3秒后自动关闭页面',
                                type: 'warning'
                            });
                            setTimeout(()=>{
                                window.close();
                            },3000)
                        }else if(res.data.menuType == 2){
                          let token =  localStorage.getItem('toolToken')
                          window.location.href = window.location.origin + "/agent-platform-web"+`/apps?console_token=${token}`
                        //   window.open(window.location.origin + "/agent-platform-web"+`/apps?console_token=${token}`)
                        }
                    }else{
                        that.$message.error(res.message);
                    }
                })
                .catch((err) => {
                    console.log(err, "失败");
                });
            },
            fanhui () {
                this.$router.push({ path: '/home', query: { Authorization: localStorage.getItem('toolToken')}} )
                /* setTimeout(() => {
                    location.reload();
                }, 100); */
            },
            ...mapMutations('admin/layout', [
                'updateMenuCollapse'
            ]),
            ...mapMutations('admin/page', [
                'keepAlivePush',
                'keepAliveRemove'
            ]),
            handleToggleDrawer (state) {
                if (typeof state === 'boolean') {
                    this.showDrawer = state;
                } else {
                    this.showDrawer = !this.showDrawer;
                }
            },
            handleScroll () {
                if (!this.headerHide) return;

                const scrollTop = document.body.scrollTop + document.documentElement.scrollTop;

                if (!this.ticking) {
                    this.ticking = true;
                    requestAnimation(() => {
                        if (this.oldScrollTop > scrollTop) {
                            this.headerVisible = true;
                        } else if (scrollTop > 300 && this.headerVisible) {
                            this.headerVisible = false;
                        } else if (scrollTop < 300 && !this.headerVisible) {
                            this.headerVisible = true;
                        }
                        this.oldScrollTop = scrollTop;
                        this.ticking = false;
                    });
                }
            },
            handleHeaderWidthChange () {
                const $breadcrumb = this.$refs.breadcrumb;
                if ($breadcrumb) {
                    $breadcrumb.handleGetWidth();
                    $breadcrumb.handleCheckWidth();
                }
                const $menuHead = this.$refs.menuHead;
                if ($menuHead) {
                    // todo $menuHead.handleGetMenuHeight();
                }
            },
            handleReload () {
                // 针对缓存的页面也生效
                const isCurrentPageCache = this.keepAlive.indexOf(this.$route.name) > -1;
                const pageName = this.$route.name;
                if (isCurrentPageCache) {
                    this.keepAliveRemove(pageName);
                }
                this.loadRouter = false;
                this.$nextTick(() => {
                    this.loadRouter = true;
                    if (isCurrentPageCache) {
                        this.keepAlivePush(pageName);
                    }
                });
            },
            createAgent(){
                this.dialogShow = true
            },
            closeDialog() {
                this.dialogShow = false
            },
            cancelDialog() {
                this.dialogShow = false
            }
        },
        mounted () {
            document.addEventListener('scroll', this.handleScroll, { passive: true });
        },
        beforeDestroy () {
            document.removeEventListener('scroll', this.handleScroll);
        },
        created () {
            this.getCurrentUserInfo()
            if (this.isTablet && this.showSiderCollapse) this.updateMenuCollapse(true);
        }
    }
</script>
<style>
.i-layout-content-main{
    background: url('../../assets/images/toolBg.png') no-repeat;
    background-size: 100% 100%;
    margin: 0 !important;
    padding: 24px;
    /* min-height: 100vh; */
    overflow: hidden;
}
</style>
