#!/bin/bash

if [ "${SPARK_MODE}" == "master" ]; then
  /opt/spark/sbin/start-master.sh

elif [ "${SPARK_MODE}" == "worker" ]; then
  /opt/spark/sbin/start-worker.sh "$SPARK_MASTER_URL" && wait

elif [ "${SPARK_MODE}" == "submit" ]; then
  cd /app || exit
  if [ "$APP_BUILD_SBT" == "true" ]; then
    rm -rf /app/target
    rm -rf /app/project/target
    sbt clean
    sbt package
  fi
  /opt/spark/bin/spark-submit \
    --deploy-mode "$APP_DEPLOY_MODE" \
    --master "$SPARK_MASTER_URL" \
    --executor-cores "$SPARK_WORKER_CORES" \
    --executor-memory "$SPARK_WORKER_MEMORY" \
    --num-executors "$SPARK_WORKERS" \
    --files "$APP_FILES" \
    --class "$APP_CLASS" \
    "$APP_PACKAGE"

else
  echo "Undefined spark mode '${SPARK_MODE}'"
fi
