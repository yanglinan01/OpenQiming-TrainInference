#!/bin/bash
# 脚本用途：选择性地批量构建JAR应用的Docker镜像
# 设置默认参数
# 默认的JAR文件目录，可以通过参数覆盖
JAR_DIR="/home/shuzhi/docker/jars"
IMAGE_BASE_NAME="llm"
# 存放Dockerfile的目录
DOCKERFILE_DIR="./Dockerfile"
JAR_MAPPING_FILE="jar_mapping.txt"
# 记录构建结果
RESULT_FILE="build_results.txt"
echo "$JAR_DIR";
echo "$IMAGE_BASE_NAME";
echo "$DOCKERFILE_DIR";
echo "$RESULT_FILE";
# 检查参数数量 ,如果没传参数，报错退出
# exit 1 命令用于立即终止脚本的执行
if [ $# -lt 1 ]; then
	echo "Error: You must provide at least one argument.";
	exit 1;
fi
docker build -t llm-web:1.0 --build-arg llm-web.jar .