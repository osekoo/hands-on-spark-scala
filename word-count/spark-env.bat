@echo off
docker rm -f spark-env
docker network create teaching
docker run -it --rm --name spark-env --network teaching -v "%cd%":/workspace -h localhost -p 7077:7077 -p 4040:4040 -p 8080:8080 -p 8081:8081 -e SPARK_MODE=env ghcr.io/osekoo/spark:3.5
