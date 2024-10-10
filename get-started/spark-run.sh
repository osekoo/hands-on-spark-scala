#!/bin/bash
spark-submit \
    --deploy-mode "client" \
    --master "spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077" \
    --executor-cores "4" \
    --executor-memory "1G" \
    --num-executors "2" \
    --packages "" \
    --files "data/ulysses.txt" \
    --class "WordCount" \
    "wordcount_2.12-0.1.jar"