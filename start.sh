#!/bin/bash
cwd=`dirname $0`
pushd $cwd
nohup java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -cp xxxx-mail-api-1.0.jar cn.com.yourcompany.mailapi.HttpServer 8888 >mailapi.log 2>&1 &
popd

