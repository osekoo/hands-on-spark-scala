spark-submit \
  --master spark://localhost:7077 \
  --deploy-mode cluster \
  --executor-cores 4 \
  --num-executors 1 \
  --files ./ulysses.txt \
  --class WordCount \
  target/scala-2.12/get-started_2.12-0.1.jar
