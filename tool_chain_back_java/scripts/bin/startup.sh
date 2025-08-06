#!/bin/bash



NACOS_USER=llm_app
NACOS_PASSWORD=Llm_app@2025
NACOS_ADDR=10.142.150.112:5001
NACOS_NAMESPACE=rdt-test
JVM_OPTS="-Xms2048m -Xmx2048m"
LOG_OPTS="-Dlogging.level.com.ctdi=info"


#jar名称
MAIN_JAR=$(ls *.jar)
NACOS_OPTS=" -Dnacos.username=${NACOS_USER} -Dnacos.password=${NACOS_PASSWORD} -Dnacos.addr=${NACOS_ADDR} -Dnacos.namespace=${NACOS_NAMESPACE}  "
JAVA_OPTS=" -server -Duser.timezone=GMT+08 ${JVM_OPTS}  -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=1024M -Xss1024k -Djava.net.preferIPv4ADDresses -XX:+AggressiveOpts -XX:CompileThreshold=200 -XX:+UseBiasedLocking -XX:MaxTenuringThreshold=15 -XX:LargePageSizeInBytes=128m  -Djava.awt.headless=true "
LOGGING_OPTS=" -Dlogging.level.root=error ${LOG_OPTS} "



JAVA_OPTS=$JAVA_OPTS" "${LOGGING_OPTS}$NACOS_OPTS


nohup /app/jdk11/jdk-11.0.10/bin/java  ${JAVA_OPTS} -jar ${MAIN_JAR} $@ > ./run.log 2>&1 &
