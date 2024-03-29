#!/bin/bash

REPOSITORY=/home/ec2-user/kiyo
cd $REPOSITORY

APP_NAME=kiyo
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'kiyobackend-0.0.1-SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo  "> 종료할 것 없음"
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar -Dspring.config.location=/home/ec2-user/kiyo/src/main/resources/application.yml $JAR_PATH > /home/ec2-user/nohup.out 2>&1 &
echo "> finished"
exit 0