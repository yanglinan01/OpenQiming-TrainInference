#!/bin/bash
PNAME=xtp-traininfer-engine
for pid in `ps -ef | grep "target=${PNAME}" | grep -v "grep" | awk ' { print $2 } '`
do
  kill -9 $pid;
  echo $pid;
done
