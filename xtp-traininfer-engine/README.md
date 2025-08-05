# XTP 训练推理引擎

一个为大型语言模型（LLM）提供全面训练和推理服务的引擎，通过RESTful API提供模型训练、推理、评估和状态监控功能。系统通过Fabric8客户端操作Kubernetes（K8s）来编排训练、推理和评估的任务。

## 🚀 功能特性

- **模型训练**: 支持LoRA、全量微调和DPO训练
- **模型推理**: 支持实时推理服务
- **模型评估**: 全面的模型评估能力
- **Kubernetes集成**: 无缝的K8s 任务管理，支持分布式训练
- **多模型支持**: 支持多种Qwen模型变体
- **资源管理**: 节点信息和任务状态监控
- **RESTful API**: 清晰直观的API接口

## 🏗️ 系统架构

系统基于Spring Boot 3.3.12构建，使用以下关键技术：

- **后端框架**: Spring Boot 3.3.12 with Java 17
- **Kubernetes客户端**: Fabric8 Kubernetes Client 7.2.0
- **工具库**: Hutool 5.8.26, Lombok 1.18.32
- **数据处理**: EasyExcel 4.0.1, FastJSON 2.0.57

## 📋 环境要求

- **Java**: JDK 17或更高版本
- **Kubernetes**: 可访问的Kubernetes集群
- **Kubernetes配置**: 有效的kubeconfig文件
- **内存**: 最低2GB RAM，推荐4GB+
- **存储**: 足够的模型文件和数据集存储空间

## 🛠️ 安装和配置

### 1. 克隆仓库

```bash
git clone <repository-url>
cd xtp-traininfer-engine
```

### 2. 构建项目

```bash
mvn clean package
```

### 3. 配置Kubernetes

确保在以下位置之一有有效的`kubeconfig`文件：
- `src/main/resources/kubeConfig`
- `~/.kube/config`
- 环境变量`KUBE_CONFIG`

### 4. 配置应用

编辑`src/main/resources/application.yml`来自定义：
- 服务器端口（默认: 20038）
- 模型路径和目录
- 日志配置

### 5. 运行应用

- 使用Maven

```bash
mvn spring-boot:run
```

- 使用JAR文件

```bash
java -jar target/xtp-traininfer-engine-2.0.0.jar
```

- 使用Shell脚本

```bash
# 启动应用
sh src/main/sh/start.sh

# 停止应用
sh src/main/sh/stop.sh
```

## 📚 开发者指南

### 基础URL
```
http://localhost:20038
```

### 训练API

#### 提交训练任务
```http
POST /train/submit
Content-Type: application/json

{
  "task_id": "train_001",
  "model_name": "qwen2_7b",
  "dataset": "alpaca_zh_demo",
  "num_train_epochs": 3,
  "warmup_ratio": 0.1,
  "cutoff_len": 2048,
  "max_samples": 1000,
  "learning_rate": 0.0001,
  "per_device_train_batch_size": 4,
  "gradient_accumulation_steps": 4,
  "strategy_distributed": true,
  "strategy_deepspeed": 2,
  "strategy_train": "distributed"
}
```

#### 删除训练任务
```http
GET /train/delete/{taskId}
```

### 推理API

#### 提交推理任务
```http
POST /inference/submit
Content-Type: application/json

{
  "task_id": "infer_001",
  "model_name": "qwen2_7b",
  "model_template": "qwen2",
  "use_lora": true,
  "lora_task_id": "train_001",
  "quantization": false
}
```

#### 查询推理状态
```http
GET /inference/status/{taskId}
```

#### 删除推理任务
```http
GET /inference/delete/{taskId}
```

### 评估API

#### 提交评估任务
```http
POST /train/eval
Content-Type: application/json

{
  "task_id": "eval_001",
  "model_name": "qwen2_7b",
  "model_template": "qwen2",
  "use_lora": true,
  "lora_task_id": "train_001"
}
```

### 资源管理API

#### 获取节点信息
```http
GET /nodes/info
```

#### 获取任务信息
```http
POST /task/info
Content-Type: application/json

{
  "task_id": "task_001"
}
```

## 🏷️ 支持的模型

系统支持以下Qwen模型变体：

- `qwen1.5_14b` - Qwen1.5-14B-Chat
- `qwen2_1.5b` - Qwen2-1.5B-Instruct
- `qwen2_7b` - Qwen2-7B-Instruct
- `qwen2.5_14b` - Qwen2.5-14B-Instruct
- `qwen2_72b` - Qwen2-72B-Instruct
- `qwen2.5-72b-instruct` - Qwen2.5-72B-Instruct

## 🔧 训练方法

- **LoRA**: 低秩适应，用于高效微调
- **全量微调**: 完整的模型微调
- **DPO**: 直接偏好优化

## 🏗️ Kubernetes命名空间

系统使用以下K8s命名空间：
- `qwen2-train`: 训练任务
- `qwen2-inference`: 推理部署
- `qwen2-eval`: 评估任务

## 📊 监控和日志

- **日志位置**: `./logs/llm-train-inference.log`
- **日志轮转**: 7天保留期，最大文件大小10GB
- **控制台输出**: 带时间戳和线程信息的彩色日志

## 🔒 安全特性

- **令牌认证**: 通过`TokenAuthFilter`实现
- **输入验证**: 使用Hibernate Validator进行全面的请求验证
- **错误处理**: 全局异常处理，提供详细的错误响应

## 🐛 故障排除

### 常见问题

1. **Kubernetes连接失败**
   - 验证kubeconfig文件是否存在且有效
   - 检查集群连接性
   - 确保适当的RBAC权限

2. **Pod创建失败**
   - 检查集群中的可用资源
   - 验证命名空间是否存在
   - 检查Pod模板配置

3. **模型加载问题**
   - 验证配置中的模型路径
   - 检查模型文件可访问性
   - 确保足够的存储空间

### 日志分析

查看应用日志获取详细的错误信息：
```bash
tail -f logs/llm-train-inference.log
```

## 📞 支持

如需支持和问题咨询，请联系开发团队或在仓库中创建Issue。
