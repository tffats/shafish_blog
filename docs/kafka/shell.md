---
title: kafka
description: linux, kafka, docker
hide:
  - navigation
---


## 一、显示消费信息
`./kafka-console-consumer.sh --bootstrap-server 192.168.0.161:9092 --topic shafish-dev --from-beginning`

## 二、创建主题
`./kafka-topics.sh --create --bootstrap-server 192.168.0.162:9092 --replication-factor 2 --partitions 3 --topic shafish-dev`

./kafka-topics.sh --create --bootstrap-server 192.168.0.162:9092 --replication-factor 2 --partitions 10 --topic shafish-dev-requests
./kafka-topics.sh --create --bootstrap-server 192.168.0.162:9092 --replication-factor 2 --partitions 10 --topic shafish-dev-replies