FROM artifact.srdcloud.cn/iaicenter-llm_agent-release-docker-virtual/openjdk:8-alpine3.9

ENV TZ="Asia/Shanghai"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD llm-api-service.tar.gz /app
WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["bin/startup-k8s.sh"]