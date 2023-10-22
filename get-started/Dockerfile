FROM docker.io/ubuntu:22.04

ENV SPARK_MODE="master" \
    SPARK_MASTER_URL="spark://spark-master:7077" \
    SPARK_WORKER_INSTANCES=1 \
    SPARK_NO_DAEMONIZE=true

ARG SPARK_VERSION=3.5.0

RUN apt-get update && \
      apt-get -y install sudo


RUN  apt-get update \
  && apt-get install -y wget \
  && rm -rf /var/lib/apt/lists/*

RUN useradd -m docker && echo "docker:docker" | chpasswd && adduser docker sudo

RUN sudo apt update
RUN sudo apt install openjdk-8-jdk -y

RUN export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

RUN wget https://dlcdn.apache.org/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && tar xvf spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && sudo mv spark-${SPARK_VERSION}-bin-hadoop3 /opt/spark

RUN export SPARK_HOME=/opt/spark
RUN export PATH=$PATH:$SPARK_HOME/bin

EXPOSE 8080 7077 6066

RUN sudo apt-get update \
  && sudo apt-get install apt-transport-https curl gnupg -yqq \
  && echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list \
  && echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list \
  && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo -H gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import \
  && sudo chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg

RUN sudo apt update
RUN sudo apt install sbt -y

WORKDIR /opt/spark

COPY spark-entrypoint.sh ./

CMD ["/bin/bash", "./spark-entrypoint.sh"]