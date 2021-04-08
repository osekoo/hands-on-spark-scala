spark-submit \
  --master spark://localhost:7077 \
  --deploy-mode client \
  --executor-cores 2 \
  --num-executors 2 \
  --files ./ulysses.txt \
  --class WordCount \
  target/scala-2.12/get-started_2.12-0.1.jar
