---
title: kafka-demo
description: linux, kafka, docker
hide:
  - navigation
---

??? ":one:.Maven依赖"

    ``` xml title="pom.xml" linenums="1"
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    ```

??? ":two:.Springboot配置文件"
    ``` yaml title="application.yml" linenums="1"
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
    ```

??? ":three:.主题配置"
    ``` java title="TopicConfig.java" linenums="1"
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


??? ":four:.消费端"

    ``` java title="ConsumerListener.java" linenums="1"
    @Component
    public class ConsumerListener {
        @KafkaListener(topics = {KafkaProperties.devTopic}, groupId = KafkaProperties.consumerGroupId)
        void listener(ConsumerRecord<String, String> record) {
            Optional<String> message = Optional.ofNullable(record.value());
            if (message.isPresent()) {
                System.out.println(message.get());
            }
        }
    }
    ```

??? ":five:.生产端"

    ``` java title="MessageQuery.java" linenums="1"
    public record MessageQuery(String message) {
    }
    ```
    ``` java title="MessageController.java" linenums="1"
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

!!! tip

    > 在broker允许自动创建主题情况下，可NewTopic中配置主题名称、分区数量、分区副本数;

    > 可以使用kafka提供的shell查看信息消费：`./kafka-console-consumer.sh --bootstrap-server 192.168.0.161:9092 --topic shafish-dev --from-beginning`
