#!/bin/bash

cd /app

spark-submit \
  --deploy-mode $SPARK_DEPLOY_MODE \
  --master $SPARK_MASTER_URL \
  --executor-cores $SPARK_WORKER_CORES \
  --executor-memory $SPARK_WORKER_MEMORY \
  --num-executors $SPARK_EXECUTORS \
  --files $SPARK_FILES \
  --class $SPARK_CLASS \
  $SPARK_PACKAGE
  