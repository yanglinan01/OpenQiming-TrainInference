#!/bin/sh

#变量说明
#1. JVM_OPTS jvm参数，比如-Xms2048m -Xmx4096 必传
#2. LOG_OPTS 日志级别 比如-Dcom.cnti.cnos.rdt 可选，不传默认日志级别为root
#3. SERVER_PORT 监听端口，不传默认8080 可选
#4. NACOS_USER、NACOS_PASSWORD、 NACOS_ADDR、NACOS_NAMESPACE必传
#5. PP_ENABLE



#默认端口
RUN_PORT=8080

if [ ! -z "${SERVER_PORT}" ]; then
  RUN_PORT="${SERVER_PORT}"
fi

#jar名称
MAIN_JAR=$(ls *.jar)
NACOS_OPTS=" -Dnacos.username=${NACOS_USER} -Dnacos.password=${NACOS_PASSWORD} -Dnacos.addr=${NACOS_ADDR} -Dnacos.namespace=${NACOS_NAMESPACE}  "
PINPIONT_OPTS=" -javaagent:/app/aiopsagent-1.8.0/aiopsagent-1.8.0.jar -Dpinpoint.applicationName=${APPLICATION_NAME} -Dpinpoint.agentId=${POD_IP}  -Dpinpoint.licence=${LICENCE}"
HOST_OPTS=" -Dserver.ip=${POD_IP} -Dserver.port=${RUN_PORT} "
JAVA_OPTS=" -server -Duser.timezone=GMT+08 ${JVM_OPTS}  -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=1024M -Xss1024k -Djava.net.preferIPv4ADDresses -XX:+AggressiveOpts -XX:CompileThreshold=200 -XX:+UseBiasedLocking -XX:MaxTenuringThreshold=15 -XX:LargePageSizeInBytes=128m  -Djava.awt.headless=true "
LOGGING_OPTS=" -Dlogging.level.root=info ${LOG_OPTS} "


if [ ! -z "${PP_ENABLE}" -a "${PP_ENABLE}" = "0" ]; then
    PINPIONT_OPTS=""
fi


JAVA_OPTS=$JAVA_OPTS" "${LOGGING_OPTS}$PINPIONT_OPTS$NACOS_OPTS$HOST_OPTS

echo $JAVA_OPTS

java ${JAVA_OPTS} -jar ${MAIN_JAR} $@
