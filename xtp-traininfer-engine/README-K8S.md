# 项目部署和运维手册

本文档面向开源项目的用户和开发者，用于指导如何使用 LLaMA-Factory + Kubernetes + 昇腾 NPU 环境实现大模型训练、量化和推理。

---

## 1. 项目简介

项目通过 LLaMA-Factory 接口和 `run.py` 自动生成一套推理/训练/评估配置 yaml 文件，并通过 K8s 发起任务。根据启动参数，支持 LoRA 类型、全参训练等多种模型运行方式。整体环境需要按以下章节安装。

---

## 2. 环境配置

### 重要路径：

- 基础模型目录：`/mnt/hpfs/cyc_qwen2_workspace/llm_models/`
  - Qwen1.5-14B-Chat
  - Qwen1.5-7B-Chat
  - Qwen2-1.5B-Instruct
  - Qwen2.5-14B-Instruct
  - Qwen2.5-72B-Instruct
  - 等，可以根据实际需要添加

- yaml 模板目录：`/mnt/hpfs/cyc_qwen2_workspace/qwen2_folder_modified/codes/LLaMA-Factory/examples/`
  - 评估 yaml: `auto_generate_eval_yaml/`
  - 推理 yaml: `auto_generate_inference_yaml/`
  - 训练 yaml: `auto_generate_train_yaml/`

- 各模块下，`task/{id}` 目录为各自任务自动生成的任务 yaml 目录

### 引擎 & 驱动

- 昇腾设备：Ascend 910/310
- 驱动环境：Ascend-driver-23.0.3.run
- CANN Toolkit（Toolchain）：Ascend-cann-toolkit-23.0.3.run
- 固件包（Firmware）：Ascend-firmware-910b.run
- LLaMA-Factory 已配置 `llama-cli`

### 安装样例

./Ascend-driver-*.run --full
./Ascend-cann-toolkit-*.run --full
./Ascend-firmware-*.run

---

## 3. Docker 镜像使用

- 1. 构建封装 LLaMA-Factory + torch_npu 环境镜像，在训练推理模板中使用  
- 2. 推荐 Docker 版本：20.10.12，使用 systemd 配置  
- 3. 使用 Harbor 管理本地镜像  

---

## 4. k8s安装
推荐Kubernetes v1.23.1，同时方便开发定位，推荐安装Kubectl、kubeadm

---

## 5. 机器标签设置
样例：
kubectl label nodes pm-8af70032 accelerator=huawei-Ascend910
kubectl label nodes pm-8af70032 ascend-device-plugin-ds=enabled
kubectl label nodes pm-8af70032 beta.kubernetes.io/arch=arm64
kubectl label nodes pm-8af70032 beta.kubernetes.io/os=linux
可以根据具体任务yaml配置进行调整

---

## 6. istio 网关与 VirtualService 配置

###Gateway 示例：
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: inference-gateway
spec:
  selector:
    istio: ingressgateway
  servers:
  - port:
      number: 80
      name: http
      protocol: HTTP
    hosts:
    - "*"
	
###VirtualService 示例：
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: inference-vs
spec:
  hosts:
  - "*"
  gateways:
  - inference-gateway
  http:
  - match:
    - uri:
        prefix: /inference/<taskid>
    route:
    - destination:
        host: qwen2-vllm-service
        port:
          number: 80
		  
---

## 7. run.py 脚本说明

### 功能：

自动生成 训练yaml 和推理 yaml配置，通过训推程序调用

### Py运行示例：

python run.py input_config.json
格式示例
{
  "task_id": "abc12345",
  "model_name": "qwen1.5_14b",
  "model_template": "qwen1.5_14b",
  "use_lora": false,
  "lora_task_id": "test3"
}

### 功能流程：

1.根据 model_template选择对应的 K8s yaml 模板
2.根据 model_name选择 train/inference yaml 模板
3.更新 model path, adapter 等配置项
4.生成最终 yaml

---

## 8. YAML 模板结构
ascend_template.yaml: 默认推理
ascend_template_lora.yaml: LoRA 推理
ascend_k8s_config_template.yaml: 基础 K8s 配置
ascend_k8s_config_8gpu_template.yaml: 适配大模型


---

## 9. Kubernetes 任务部署

### 创建任务：
kubectl apply -f task/abc12345/k8s_config.yaml

### 查看 Pod 运行状态：
kubectl get pods -n <your_namespace>
kubectl describe pod <pod-name> -n <your_namespace>
输出日志
kubectl logs -f <pod-name> -n <your_namespace>

---

## 10. 运维手段

### 环境查看：

npu-smi info
lsmod | grep ascend

### Prometheus 指标查询：

PromQL 查 GPU 利用率：
npu_utilization{instance=~"node08.*"}

### 常用 Pod 指标:
pod 任务处理数量
container restart 次数
GPU/NPU 资源分配