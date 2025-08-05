#!/bin/bash
LANG=en_US.UTF-8;
export LANG;
JAVA_HOME=/data/tool-chain/app/jdk-17.0.12
WORK_HOME=/data/tool-chain/xtp-traininfer-engine
LOG_HOME=${WORK_HOME}/logs
PNAME=xtp-traininfer-engine
CLASSPATH=${WORK_HOME}/conf

for jar in ${WORK_HOME}/libs/*.jar
do
  CLASSPATH=${CLASSPATH}:${jar}
done
cd ${WORK_HOME}
sh sh/stop.sh
sleep 1

${JAVA_HOME}/bin/java -Dtarget=${PNAME} -Xms2048m -Xmx4096m -classpath ${CLASSPATH} com.ctdi.llmtc.xtp.traininfer.TrainInferApplication >> ${LOG_HOME}/start.log`date +%y%m%d` &

