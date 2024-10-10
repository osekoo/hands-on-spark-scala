#!/bin/bash
spark-submit \
    --deploy-mode "client" \
    --master "spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077" \
    --executor-cores "4" \
    --executor-memory "1G" \
    --num-executors "2" \
    --packages "org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.2,org.apache.spark:spark-streaming-kafka-0-10_2.12:3.5.2" \
    --files "data/ulysses.txt" \
    --class "WordCount" \
    "wordcount_2.12-0.1.jar"