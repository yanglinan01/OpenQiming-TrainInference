FROM gz01-srdart.srdcloud.cn/qimingllm/qimingllm-lm_platform-release-docker-local/llm/nginx:1.24
RUN useradd -m -s /bin/bash qimingllm
# 切换到新创建的用户
USER qimingllm
COPY ./dist /usr/share/nginx/html
COPY ./default.conf /etc/nginx/conf.d/default.conf
COPY ./nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
