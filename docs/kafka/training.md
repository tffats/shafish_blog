---
title: kafka
description: linux, kafka, docker
hide:
  - navigation
---


> ***Spring for Apache Kafka***

## 一、Request
- Apache Kafka Clients 3.3.x
- Spring Framework 6.0.x
- Minimum Java version: 17

## 二、入门demo

--8<-- "kafka/training/1-demo.md:8:"

## 三、主题配置
Starting with version 2.7

??? "broker端允许自动创建主题情况下"

    ``` java title="MyTopicConfig.java" linenums="1" hl_lines="7 10"
    @Bean
    public KafkaAdmin.NewTopics topics456() {
        return new NewTopics(
                TopicBuilder.name("defaultBoth") // 主题名称
                    .build(),
                TopicBuilder.name("defaultPart")
                    .replicas(1) // 副本数量
                    .build(),
                TopicBuilder.name("defaultRepl")
                    .partitions(3) // 分区数量
                    .config(TopicConfig.MESSAGE_TIMESTAMP_TYPE_CONFIG, "CreateTime") // LOG_APPEND_TIME 设置主题消息的时间戳
                    .build());
    }
    ```

## 四、消息发送

--8<-- "kafka/training/4-sending_messages.md:8:"

## 五、消费接收

--8<-- "kafka/training/5-receiving_messages.md:8:"