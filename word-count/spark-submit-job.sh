#!/bin/bash
spark-submit \
    --deploy-mode client \
    --master "spark://localhost:7077" \
    --executor-cores 1 \
    --executor-memory 2G \
    --num-executors 8 \
    --class "MainApp" \
    "target/scala-2.12/word-count_2.12-0.1.jar" \
    1000000 \
