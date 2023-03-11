---
title: kafka-messages-receive
description: linux, kafka, docker
hide:
  - navigation
---

!!! abstract

    > 可以通过配置`MessageListenerContainer`监听器或者使用`@KafkaListener`注解的方式接收kafka消息。

### 1.message listener

??? "八种消息监听"

    > 当监听消息时，需要提供一个监听者去接收消息，提供了八种监听接口：

    ``` java linenums="1"
    // 接收一条消息，自动提交消费记录
    public interface MessageListener<K, V> { 
 
        void onMessage(ConsumerRecord<K, V> data);

    }

    // 接收一条消息，手动提交消费记录
    public interface AcknowledgingMessageListener<K, V> { 

        void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment);

    }

    // 接收一条消息，自动提交消费记录，并附带消费者相关信息
    public interface ConsumerAwareMessageListener<K, V> extends MessageListener<K, V> { 

        void onMessage(ConsumerRecord<K, V> data, Consumer<?, ?> consumer);

    }

    // 接收一条消息，手动提交消费记录，并附带消费者相关信息
    public interface AcknowledgingConsumerAwareMessageListener<K, V> extends MessageListener<K, V> { 

        void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer);

    }

    public interface BatchMessageListener<K, V> { 

        void onMessage(List<ConsumerRecord<K, V>> data);

    }

    public interface BatchAcknowledgingMessageListener<K, V> { 

        void onMessage(List<ConsumerRecord<K, V>> data, Acknowledgment acknowledgment);

    }

    public interface BatchConsumerAwareMessageListener<K, V> extends BatchMessageListener<K, V> { 

        void onMessage(List<ConsumerRecord<K, V>> data, Consumer<?, ?> consumer);

    }

    public interface BatchAcknowledgingConsumerAwareMessageListener<K, V> extends BatchMessageListener<K, V> { 

        void onMessage(List<ConsumerRecord<K, V>> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer);

    }
    ```

    > 方法中的consumer不是线程安全的

### 2.message listener containers

!!! abstract "两种实现类"

    > 提供了两个listenerContainer实现类：`KafkaMessageListenerContainer`和`ConcurrentMessageListenerContainer`

    > `KafkaMessageListenerContainer`在单线程中接收订阅主题的所有消息;`ConcurrentMessageListenerContainer`提供一个或多个`KafkaMessageListenerContainer`实例去消费消息

??? abstract "KafkaMessageListenerContainer"

    ``` java
    public KafkaMessageListenerContainer(ConsumerFactory<K, V> consumerFactory,
                ContainerProperties containerProperties)
    ```
    ``` java
    // 传入指定分区下标，明确该container使用哪个分区。默认使用负数，表示当前主题最后一个分区
    public ContainerProperties(TopicPartitionOffset... topicPartitions)

    public ContainerProperties(String... topics)

    public ContainerProperties(Pattern topicPattern)
    ```