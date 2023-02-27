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
??? "1.Maven依赖"

    ``` xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    ```

??? "2.properties配置文件"
    ``` yml
    spring:
      kafka:
        bootstrap-servers: 192.168.0.161:9092
        producer:
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.apache.kafka.common.serialization.StringSerializer
        consumer:
          key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          auto-offset-reset: latest
          group-id: fisha-1
    ```

??? "3.主题配置"
    ``` java
    @Configuration
    public class TopicConfig {
        @Bean
        public KafkaAdmin.NewTopics topicsCreate() {
            return new KafkaAdmin.NewTopics(
    //                TopicBuilder.name("defaultBoth")
    //                        .build(),
    //                TopicBuilder.name("defaultPart")
    //                        .replicas(1)
    //                        .build(),
                    TopicBuilder.name("shafish-dev")
                            .partitions(10)
                            .replicas(2)
                            .build());
        }
    }
    ```


??? "4.消费端"

    ``` java
    @Component
    public class ConsumerListener {
        @KafkaListener(topics = {"shafish-dev"})
        void listener(String payload) {
            System.out.println(payload);
        }
    }
    ```

??? "5.生产端"

    ``` java
    @RestController
    @RequestMapping("api/v1/message")
    public class MessageController {

        private KafkaTemplate<String, String> kafkaTemplate;

        public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        @PostMapping("/publish")
        public void publish(@RequestBody MessageQuery query) {
            kafkaTemplate.send(KafkaProperties.devTopic, query.message());
        }
    }
    ```

> 在broker允许自动创建主题情况下，NewTopic中配置主题名称、分区数量、分区副本数;
> 可以使用kafka提供的shell查看消费信息：`./kafka-console-consumer.sh --bootstrap-server 192.168.0.161:9092 --topic shafish-dev --from-beginning`
