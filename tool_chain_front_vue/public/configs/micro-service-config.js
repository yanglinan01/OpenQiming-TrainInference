/**
 * @Descripttion: 
 * 1. 该文件用于在前端项目部署以后无需重新编译构建镜像情况下修改前端相关变量值，修改方式如下：
 * （1）使用微服务部署（升级）功能，在【环境变量】一项中进行配置
 * （2）登录前端部署的服务器，在相关路径下找到该文件，手动进行文件覆写
 * 
 * 2. 原理说明
 * 在非严格模式下，下面的代码执行结果是在window对象声明相关属性和值：
 * window.$baseUrl: 'http://localhost:8080'
 * window.userName: 'zhangsan'
 * window._userToken: 'Adjkadiawl*&*+^7e2qKHDJKADADWAD&*&^712hkjdahkduwqoiewq012e356afdcnkjdsaiu*&*%&^%612e9'
 * 
 * 3. 使用方式
 * 在前端相关js代码中可以直接从window对象上读取到这些变量值，例如打印：
 * console.log(window.$baseUrl)
 * console.log(window.userName)
 * console.log(window._userToken)
 * 
 * 4. 特别说明
 * 如使用微服务部署（升级）的环境变量配置项注入的相关变量，默认会在你的变量名前自动拼接一个'$'符号，所以在前端使用的时候，需要使用window.$[变量名]的格式来进行引用
 */

$baseUrl = 'http://localhost:8080'
userName = 'zhangsan'
_userToken = 'Adjkadiawl*&*+^7e2qKHDJKADADWAD&*&^712hkjdahkduwqoiewq012e356afdcnkjdsaiu*&*%&^%612e9'
