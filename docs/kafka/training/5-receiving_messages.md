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

    > 当监听消息时，需要提供一个监听者去接收消息，下面是提供的八种监听接口：

    ``` java
    public interface MessageListener<K, V> { 

        void onMessage(ConsumerRecord<K, V> data);

    }

    public interface AcknowledgingMessageListener<K, V> { 

        void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment);

    }

    public interface ConsumerAwareMessageListener<K, V> extends MessageListener<K, V> { 

        void onMessage(ConsumerRecord<K, V> data, Consumer<?, ?> consumer);

    }

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