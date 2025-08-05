FROM artifact.srdcloud.cn/iaicenter-llm_agent-release-docker-virtual/nginx/nginx:1.24.0

ENV TZ="Asia/Shanghai"
ENV VUE_APP_SERVER_ROOT="/XXX"

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN rm -f /usr/share/nginx/html/index.html
RUN rm -f /etc/nginx/conf.d/default.conf
# 指定路径
COPY ./nginx.conf /etc/nginx/nginx.conf
# 复制dist文件到路径
COPY ./dist/ /usr/share/nginx/html/
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
