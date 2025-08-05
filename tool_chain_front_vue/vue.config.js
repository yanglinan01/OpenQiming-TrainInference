const Setting = require('./src/setting.env');
const { name } = require('./package.json');
// 拼接路径
const resolve = dir => require('path').join(__dirname, dir);

// 增加环境变量
process.env.VUE_APP_VERSION = require('./package.json').version;
console.log(process.env.NODE_ENV)
process.env.VUE_APP_BUILD_TIME = require('dayjs')().format('YYYY-M-D HH:mm:ss');

const port = 8080; // dev port

module.exports = {
    publicPath: Setting.publicPath,
    lintOnSave: Setting.lintOnSave,
    outputDir: Setting.outputDir,
    assetsDir: Setting.assetsDir,
    filenameHashing: true,
    runtimeCompiler: true,
    productionSourceMap: false,
    devServer: {
        publicPath: Setting.publicPath,
        hot: true,
        disableHostCheck: true,
        port,
        overlay: {
            warnings: false,
            errors: true
        },
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        open: true    //启动项目时浏览器自动启动
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@': resolve('src'),
            }
        },
        output: {
            // 把子应用打包成 umd 库格式
            library: `${name}-[name]`,
            libraryTarget: 'umd',
            jsonpFunction: `webpackJsonp_${name}`,
        },
        devtool:"source map"
    },
    chainWebpack: config => {
        // 不编译 iView Pro
        config.module
            .rule('js')
            .test(/\.jsx?$/)
            .exclude
            .add(resolve('src/libs/iview-pro'))
            .end();
        // 使用 iView Loader
        config.module
            .rule('vue')
            .test(/\.vue$/)
            .use('iview-loader')
            .loader('iview-loader')
            .tap(() => {
                return Setting.iviewLoaderOptions
            })
            .end();
        // markdown
        config.module
            .rule('md')
            .test(/\.md$/)
            .use('text-loader')
            .loader('text-loader')
            .end();
        // i18n
        config.module
            .rule('i18n')
            .resourceQuery(/blockType=i18n/)
            .use('i18n')
            .loader('@kazupon/vue-i18n-loader')
            .end();
        // image exclude
        const imagesRule = config.module.rule('images');
        imagesRule
            .test(/\.(png|jpe?g|gif|webp|svg)(\?.*)?$/)
            .exclude
            .add(resolve('src/assets/svg'))
            .end();
        // 重新设置 alias
        config.resolve.alias
            .set('@api', resolve('src/api'));
        // node
        config.node
            .set('__dirname', true)
            .set('__filename', true);
        // 判断是否需要加入模拟数据
        if (false && Setting.isMock) {
            const entry = config.entry('app');
            if (Setting.isMock) {
                entry
                    .add('@/mock')
                    .end()
            }
        }
    }
};
