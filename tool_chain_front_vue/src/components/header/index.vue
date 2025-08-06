<template>
    <div class="i-layout-header">
        <div class="i-layout-header-top">
            <div class="i-layout-header-top-left">启明网络大模型工具链</div>
            <div class="i-layout-header-top-right">
                <img src="../../assets/images/fanhui.png" alt="" class="i-layout-header-top-right-fanhuiImg" @click="fanhuiMH()">
                <span class="i-layout-header-top-right-fanhui" @click="fanhuiMH()">返回门户</span>
                <span class="i-layout-header-top-right-line">|</span>
                <img src="../../assets/images/touxiang.png" alt="" class="i-layout-header-top-right-touxiangImg">
                <span class="i-layout-header-top-right-touxiang">{{ name }}</span>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        props:{
            name: {
                type: String,
                default: ''
            },
        },
        methods: {
            fanhuiMH () {
                window.location.href = process.env.VUE_APP_HOMEPAGE_ADDRESS;
            },
        }
    };
</script>
