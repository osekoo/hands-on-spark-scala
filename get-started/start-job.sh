#!/bin/bash

docker build -t spark-app .

docker-compose up spark-worker -d
docker-compose up spark-app