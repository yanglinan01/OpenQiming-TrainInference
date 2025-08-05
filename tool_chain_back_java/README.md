# LLM Tool Chain Backend Java

[![Java](https://img.shields.io/badge/Java-1.8+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.8-green.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-blue.svg)](https://spring.io/projects/spring-cloud)
[![Maven](https://img.shields.io/badge/Maven-3.6+-orange.svg)](https://maven.apache.org/)

## 📖 项目简介

LLM Tool Chain Backend Java 是一个基于 Spring Cloud 微服务架构的大语言模型工具链后端系统。该系统为AI模型训练、部署、管理和监控提供了完整的微服务解决方案。



## 🚀 快速开始

### 💻 本地开发

#### 1. 环境要求

- JDK 1.8+
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+
- Nacos 2.0+

#### 2. 克隆项目
```bash
git clone https://github.com/your-username/tool_chain_back_java.git
cd tool_chain_back_java
```

#### 3. Nacos服务搭建
```bash
# 下载Nacos
wget https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz
tar -xvf nacos-server-2.2.3.tar.gz
cd nacos/bin

# 启动Nacos（单机模式）
./startup.sh -m standalone

# 访问Nacos控制台
# http://localhost:8848/nacos
# 用户名/密码: nacos/nacos
```

#### 4. PostgreSQL搭建
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib

# CentOS/RHEL
sudo yum install postgresql postgresql-server
sudo postgresql-setup initdb
sudo systemctl start postgresql
sudo systemctl enable postgresql

# 创建用户和数据库
sudo -u postgres psql
CREATE USER llm_user WITH PASSWORD 'your_password';
CREATE DATABASE llm_tool_chain OWNER llm_user;
GRANT ALL PRIVILEGES ON DATABASE llm_tool_chain TO llm_user;
\q
```

#### 5. Redis搭建
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install redis-server

# CentOS/RHEL
sudo yum install redis
sudo systemctl start redis
sudo systemctl enable redis

# 验证Redis运行状态
redis-cli ping
# 应该返回: PONG
```

#### 6. 配置数据库
```sql
-- 创建数据库
CREATE DATABASE llm_tool_chain;

-- 执行初始化脚本
psql -h localhost -U your_username -d llm_tool_chain -f scripts/sql/dump-llm.sql
```

#### 7. 启动服务
```bash
# 编译项目
mvn clean install

# 启动各个服务
cd llm-modules/llm-metadata-service && mvn spring-boot:run
cd llm-modules/llm-train-service && mvn spring-boot:run
cd llm-modules/llm-log-service && mvn spring-boot:run
cd llm-modules/llm-job-service && mvn spring-boot:run
cd llm-modules/llm-web && mvn spring-boot:run
cd llm-intf/llm-intf-restful-service && mvn spring-boot:run
```

**前后台启动后，本地浏览器登录地址：**
```
http://localhost:8080/home?Authorization=1890280795059261440
```

### 🐳 Docker部署

#### 0. 基础服务部署（Nacos、PostgreSQL、Redis、Elasticsearch）

可直接使用官方镜像快速启动基础服务：

```bash
# 启动Nacos
docker run -d --name nacos -e MODE=standalone -p 8848:8848 nacos/nacos-server:v2.2.3

# 启动PostgreSQL
docker run -d --name postgres -e POSTGRES_DB=llm_tool_chain -e POSTGRES_USER=llm_user -e POSTGRES_PASSWORD=your_password -p 5432:5432 -v pgdata:/var/lib/postgresql/data postgres:13

# 启动Redis
docker run -d --name redis -p 6379:6379 redis:6-alpine

# 启动Elasticsearch（如需，示例为7.17.10，注意内存和虚拟内存配置）
docker run -d --name es -e \"discovery.type=single-node\" -e ES_JAVA_OPTS=\"-Xms512m -Xmx512m\" -p 9200:9200 -p 9300:9300 elasticsearch:7.17.10
```

> **注意**：如需持久化数据，可为各服务挂载本地数据卷。

---

#### 1. 模块服务打包

在构建Docker镜像之前，需要先对各个模块进行打包：

```bash
# 编译整个项目
mvn clean install -DskipTests

# 打包各个服务模块
cd llm-modules/llm-web
mvn clean package -DskipTests
cp target/llm-web-*.jar ../../scripts/llm-web/llm-web.tar.gz

cd ../llm-metadata-service
mvn clean package -DskipTests
cp target/llm-metadata-service-*.jar ../../scripts/llm-metadata-service/llm-metadata-service.tar.gz

cd ../llm-train-service
mvn clean package -DskipTests
cp target/llm-train-service-*.jar ../../scripts/llm-train-service/llm-train-service.tar.gz

cd ../llm-log-service
mvn clean package -DskipTests
cp target/llm-log-service-*.jar ../../scripts/llm-log-service/llm-log-service.tar.gz

cd ../llm-job-service
mvn clean package -DskipTests
cp target/llm-job-service-*.jar ../../scripts/llm-job-service/llm-job-service.tar.gz

cd ../../llm-intf/llm-intf-restful-service
mvn clean package -DskipTests
cp target/llm-intf-restful-service-*.jar ../../scripts/llm-intf-restful-service/llm-intf-restful-service.tar.gz

# 打包其他服务模块
cd ../../scripts/llm-api-service
mvn clean package -DskipTests
cp target/llm-api-service-*.jar ./llm-api-service.tar.gz

cd ../llm-intf-ws-service
mvn clean package -DskipTests
cp target/llm-intf-ws-service-*.jar ./llm-intf-ws-service.tar.gz

cd ../llm-register-service
mvn clean package -DskipTests
cp target/llm-register-service-*.jar ./llm-register-service.tar.gz
```

#### 2. 构建服务镜像

项目为每个微服务提供了独立的Dockerfile，位于 `scripts` 目录下：

```bash
# 构建所有服务镜像
cd scripts

# 构建Web服务镜像
cd llm-web
docker build -t llm-web:latest .

# 构建元数据服务镜像
cd ../llm-metadata-service
docker build -t llm-metadata-service:latest .

# 构建训练服务镜像
cd ../llm-train-service
docker build -t llm-train-service:latest .

# 构建日志服务镜像
cd ../llm-log-service
docker build -t llm-log-service:latest .

# 构建任务调度服务镜像
cd ../llm-job-service
docker build -t llm-job-service:latest .

# 构建接口服务镜像
cd ../llm-intf-restful-service
docker build -t llm-intf-restful-service:latest .

# 构建API服务镜像
cd ../llm-api-service
docker build -t llm-api-service:latest .

# 构建WebSocket服务镜像
cd ../llm-intf-ws-service
docker build -t llm-intf-ws-service:latest .

# 构建注册服务镜像
cd ../llm-register-service
docker build -t llm-register-service:latest .
```

#### 3. Docker Compose部署

创建 `docker-compose.yml` 文件：

```yaml
version: '3.8'
services:
  llm-web:
    image: llm-web:latest
    container_name: llm-web
    ports:
      - "8080:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos
      - postgres
      - redis

  llm-metadata-service:
    image: llm-metadata-service:latest
    container_name: llm-metadata-service
    ports:
      - "8081:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos
      - postgres

  llm-train-service:
    image: llm-train-service:latest
    container_name: llm-train-service
    ports:
      - "8082:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos
      - postgres

  llm-log-service:
    image: llm-log-service:latest
    container_name: llm-log-service
    ports:
      - "8083:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos
      - postgres

  llm-job-service:
    image: llm-job-service:latest
    container_name: llm-job-service
    ports:
      - "8084:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos
      - postgres

  llm-intf-restful-service:
    image: llm-intf-restful-service:latest
    container_name: llm-intf-restful-service
    ports:
      - "8085:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos

  llm-api-service:
    image: llm-api-service:latest
    container_name: llm-api-service
    ports:
      - "8086:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos

  llm-intf-ws-service:
    image: llm-intf-ws-service:latest
    container_name: llm-intf-ws-service
    ports:
      - "8087:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos

  llm-register-service:
    image: llm-register-service:latest
    container_name: llm-register-service
    ports:
      - "8088:8080"
    environment:
      - NACOS_ADDR=nacos:8848
      - NACOS_NAMESPACE=public
      - NACOS_USER=nacos
      - NACOS_PASSWORD=nacos
    depends_on:
      - nacos

  # 基础服务
  nacos:
    image: nacos/nacos-server:v2.2.3
    container_name: nacos
    ports:
      - "8848:8848"
    environment:
      - MODE=standalone
    volumes:
      - nacos-data:/home/nacos/data
      - nacos-logs:/home/nacos/logs

  postgres:
    image: postgres:13
    container_name: postgres
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=llm_tool_chain
      - POSTGRES_USER=llm_user
      - POSTGRES_PASSWORD=your_password
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./scripts/sql/dump-llm.sql:/docker-entrypoint-initdb.d/dump-llm.sql

  redis:
    image: redis:6-alpine
    container_name: redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data

volumes:
  nacos-data:
  nacos-logs:
  postgres-data:
  redis-data:
```

```

#### 3. 启动服务

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f llm-web

# 停止所有服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v
```

#### 4. 使用部署脚本

项目提供了便捷的部署脚本：

```bash
cd docker

# 启动所有模块
./deploy.sh modules

# 停止所有服务
./deploy.sh stop

# 删除所有容器
./deploy.sh rm

# 开启所需端口
./deploy.sh port
```

## 🏗️ 架构设计

### 🎯 核心功能

#### 登录鉴权及用户管理
- 提供用户信息存储功能
- 提供用户登录认证接口
- 支持自定义登录页面或单点登录(SSO)集成

#### 数据集管理页面
- 提供交互式数据集创建和管理页面
- 支持数据集的增删改查操作
- 可视化数据集管理界面

#### 模型训练页面
- 提供交互式模型训练页面
- 支持选择基础模型（如：启明大模型、Qwen2.5等）
- 支持多种微调方法（如：全参数、Lora等）
- 可视化训练配置和参数设置

#### 推理部署页面
- 提供交互式推理模型部署页面
- 支持模型部署和卸载操作
- 可视化部署状态和配置管理

#### 模型评估页面
- 提供交互式模型评估页面
- 支持数据集评估功能
- 支持C-EVAL评估功能
- 可视化评估结果和指标

#### 系统监控页面
- 提供各种系统监控指标的展示页面
- 实时监控系统运行状态
- 可视化监控数据和告警信息


### 项目结构说明

- **llm-beans**: 包含所有实体类、DTO、VO等业务对象
- **llm-common**: 公共组件，包含工具类、配置类、缓存等
- **llm-feign**: 服务间通信的Feign客户端接口
- **llm-modules**: 核心业务服务，每个服务都是独立的微服务
- **llm-intf**: 对外提供的RESTful接口服务

### 代码生成

项目内置代码生成器，可以快速生成Controller、Service、Mapper等代码：

### 🔧 配置说明

> ⚠️ **注意**: 本地启动和Docker部署前，请先了解以下配置信息。

#### Nacos基础配置

配置属性来自 `scripts` 目录下 `nacos` 目录下的 `nacos_config_export.zip` 文件。

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:public}
      config:
        server-addr: ${NACOS_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:public}
        username: ${NACOS_USER:nacos}
        password: ${NACOS_PASSWORD:nacos}
        file-extension: yml
```

#### 数据库初始化

初始化语句来自 `scripts` 目录下 `sql` 目录下的 `dump-llm.sql` 文件。

```sql
-- 创建数据库
CREATE DATABASE llm_tool_chain;

-- 执行初始化脚本
psql -h localhost -U your_username -d llm_tool_chain -f scripts/sql/dump-llm.sql
```

### 📊 监控和日志

#### 日志配置

```bash
export LOG_OPTS="-Dlogging.level.com.ctdi=info"
```

### 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证

## 📞 联系方式

- 项目维护者: [Your Name]
- 邮箱: [your.email@example.com]
- 项目地址: [https://github.com/your-username/tool_chain_back_java]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户。

---