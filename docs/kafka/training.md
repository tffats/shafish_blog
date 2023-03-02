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
> Spring提供了`KafkaTemplate`封装类，可以方便得发送消息给主题。

> 提供了KafkaTemplate的子类`RoutingKafkaTemplate`，可以根据不同的主题选择不同Producer进行发送

### 1.KafkaTemplate

??? "KafkaTemplate中提供消息发送的方法"

    ``` java title="KafkaTemplate.java" linenums="1"
    CompletableFuture<SendResult<K, V>> sendDefault(V data);

    CompletableFuture<SendResult<K, V>> sendDefault(K key, V data);

    CompletableFuture<SendResult<K, V>> sendDefault(Integer partition, K key, V data);

    CompletableFuture<SendResult<K, V>> sendDefault(Integer partition, Long timestamp, K key, V data);

    CompletableFuture<SendResult<K, V>> send(String topic, V data);

    CompletableFuture<SendResult<K, V>> send(String topic, K key, V data);

    CompletableFuture<SendResult<K, V>> send(String topic, Integer partition, K key, V data);

    CompletableFuture<SendResult<K, V>> send(String topic, Integer partition, Long timestamp, K key, V data);

    CompletableFuture<SendResult<K, V>> send(ProducerRecord<K, V> record);

    CompletableFuture<SendResult<K, V>> send(Message<?> message);

    Map<MetricName, ? extends Metric> metrics();

    List<PartitionInfo> partitionsFor(String topic);

    <T> T execute(ProducerCallback<K, V, T> callback);

    // Flush the producer.

    void flush();

    interface ProducerCallback<K, V, T> {

        T doInKafka(Producer<K, V> producer);

    }
    ```

!!! quote    

    > 调用`sendDefault`方法需要提前设置好默认的主题（`spring:kafka:template:default-topic: shafish-dev`）;

    > 如果配置主题时设置了`TopicConfig.MESSAGE_TIMESTAMP_TYPE_CONFIG`为`CREATE_TIME`或者`LOG_APPEND_TIME`，则会配置对应时间戳到消息中;
    ![ConsumerRecord](https://picture.cdn.shafish.cn/blog/kafka/2023-03-01_22-54.png){: .zoom}

    > 3.0后消息发送返回的是异步回调`CompletableFuture`，当发送成功或者失败时会执行配置的回调方法;
    ``` java linenums="1"
    CompletableFuture<SendResult<Integer, String>> future = template.send("myTopic", "something");
    future.whenComplete((result, ex) -> {
        ...
    });
    ```
    > 如果需要统一处理回调，可以实现`ProducerListener`方法，在其`onSuccess`与`onError`中编写回调逻辑（spring提供了一个默认实现`LoggingProducerListener`）;

??? ":one:.KafkaTemplate基本配置"

    > 可以在template中传入不同`ProducerFactory`进行不同使用场景的配置

    ``` java title="KafkaProducerConfig.java" linenums="1"
    @Configuration
    public class KafkaProducerConfig {
        @Value("${spring.kafka.bootstrap-servers}")
        private String boostrapServers;

        @Bean
        public Map<String, Object> producerConfig() {
            HashMap<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return props;
        }

        @Bean
        public ProducerFactory<String, String> producerFactory() {
            return new DefaultKafkaProducerFactory<>(producerConfig());
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }
    }
    ```
    ``` java linenums="1"
    // 发送string类型的消息
    @Bean
    public KafkaTemplate<String, String> stringTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }

    // 发送字节类型的消息
    @Bean
    public KafkaTemplate<String, byte[]> bytesTemplate(ProducerFactory<String, byte[]> pf) {
        return new KafkaTemplate<>(pf,
                Collections.singletonMap(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class));
    }
    ```

??? ":two:.异步发送消息"

    ``` java linenums="1"
    public record MessageQuery(String message) {
    }
    ```
    ``` java linenums="1" hl_lines="18 19"
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

        @PostMapping("/publishAsync")
        public void publishAsync(@RequestBody MessageQuery query) {
            CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(KafkaProperties.devTopic, query.message());
            send.whenComplete((result, ex) -> {
                if (ex == null) {
                    // handleSuccess(data);
                    System.out.println("success");
                }
                else {
                    // handleFailure(data, record, ex);
                    ex.printStackTrace();
                }
            });
        }

    }
    ```

??? ":three:.同步阻塞发送消息"

    ``` java linenums="1"
    public record MessageQuery(String message) {
    }
    ```
    ``` java linenums="1" hl_lines="19"
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

        @PostMapping("/publishSync")
        public void publishSync(@RequestBody MessageQuery query) {
            try {
                kafkaTemplate.send(KafkaProperties.devTopic, query.message()).get(10, TimeUnit.SECONDS);
                // handleSuccess(data);
                System.out.println("success");
            }
            catch (ExecutionException e) {
                // handleFailure(data, record, e.getCause());
                e.printStackTrace();
            }
            catch (TimeoutException | InterruptedException e) {
                // handleFailure(data, record, e);
                e.printStackTrace();
            }

        }

    }    
    ```

??? ":four:.同步阻塞发送消息"

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

### 2.RoutingKafkaTemplate

!!! abstract
    RoutingKafkaTemplate不支持`transactions`, `execute`, `flush`, or `metrics`等操作，具体等后续深入理解。

??? ":one:.修改ProducerFactory"

    ``` java title="KafkaProducerConfig.java" linenums="1" hl_lines="33 34 42"
    @Configuration
    public class KafkaProducerConfig {
        @Value("${spring.kafka.bootstrap-servers}")
        private String boostrapServers;

        @Autowired
        private KafkaProperties kafkaProperties;

        public Map<String, Object> producerConfig() {
            HashMap<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return props;
        }

        @Bean
        public ProducerFactory<String, String> producerFactory() {
            return new DefaultKafkaProducerFactory<>(producerConfig());
        }

        @Bean
        public ProducerFactory<Object, Object> pf() {
            return new DefaultKafkaProducerFactory<>(producerConfig());
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }

        /**
        * pf中使用了配置的producerConfig，其中的key与value都用String序列化;
        * 为了传输不同的主题信息就要配置不同的value序列化，比如这里的ByteArraySerializer
        */
        @Bean
        public RoutingKafkaTemplate routingTemplate(GenericApplicationContext context,
                                                    ProducerFactory<Object, Object> pf) {

            // Clone the PF with a different Serializer, register with Spring for shutdown
            Map<String, Object> configs = new HashMap<>(pf.getConfigurationProperties());
            configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            DefaultKafkaProducerFactory<Object, Object> bytesPF = new DefaultKafkaProducerFactory<>(configs);
            context.registerBean(DefaultKafkaProducerFactory.class, "bytesPF", bytesPF);

            Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
            map.put(Pattern.compile("shafish-byte-dev"), bytesPF); // KafkaProperties.devByteTopic
            map.put(Pattern.compile(".+"), pf); // Default PF with StringSerializer
            return new RoutingKafkaTemplate(map);
        }
    }
    ```

??? ":two:.消息生产者"

    ``` java title="MessageController.java" linenums="1" hl_lines="14"
    @RestController
    @RequestMapping("api/v1/message")
    public class MessageController {

        private RoutingKafkaTemplate routingKafkaTemplate;

        public MessageController(RoutingKafkaTemplate routingKafkaTemplate) {
            this.routingKafkaTemplate = routingKafkaTemplate;
        }

        @PostMapping("/publishByte")
        public void publishByte(@RequestBody MessageQuery query) {
            // 第二个参数就可以传入字节数组类型了
            routingKafkaTemplate.send(KafkaProperties.devByteTopic, query.message().getBytes());
            // handleSuccess(data);
            System.out.println("success");

        }

    }
    ```

??? ":three:.消息消费者"

    ``` java title="ConsumerListener.java" linenums="1"
    @Component
    public class ConsumerListener {

        @KafkaListener(topics = {"shafish-byte-dev"}, groupId = KafkaProperties.consumerGroupId)
        void byteListener(ConsumerRecord<String, String> record) {
            Optional<String> message = Optional.ofNullable(record.value());
            if (message.isPresent()) {
                System.out.println(message.get());
            }
        }

    }
    ```

### 3.DefaultKafkaProducerFactory

!!! abstract
    ProducerFactory用于创建kafka的producer实例，具体可以看`KafkaProducerConfig`相关配置。在不使用事务的情况下，kafka默认只会创建一个单例的producer实例给客户端使用，但如果其中某个客户端调用了`flush()`方法，会造成使用该producer实例的其他客户端也阻塞。

    如果配置ProducerFactory的`producerPerThread`为true，则会为每个线程都分配一个新的producer实例，避免flush阻塞问题。

??? ":one:.producer配置"

    ``` java title="KafkaProducerConfig.java" linenums="1"
    
    ```