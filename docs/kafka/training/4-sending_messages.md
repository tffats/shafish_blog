---
title: kafka-messages-send
description: linux, kafka, docker
hide:
  - navigation
---

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

### 4.ReplyingKafkaTemplate

!!! abstract

    发送请求并需要获取回复的场景下使用

??? ":one:.创建收发主题"

    `./kafka-topics.sh --create --bootstrap-server 192.168.0.162:9092 --replication-factor 2 --partitions 10 --topic shafish-dev-requests`

    `./kafka-topics.sh --create --bootstrap-server 192.168.0.162:9092 --replication-factor 2 --partitions 10 --topic shafish-dev-replies`

??? ":two:.producer配置"

    ``` java title="KafkaProducerConfig.java" linenums="1"
    @Configuration
    public class KafkaProducerConfig {
        @Value("${spring.kafka.bootstrap-servers}")
        private String boostrapServers;

        public Map<String, Object> producerConfig() {
            HashMap<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return props;
        }

        @Bean
        public ProducerFactory<String, String> producerFactory() {
            DefaultKafkaProducerFactory<String, String> objectObjectDefaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerConfig());
            //objectObjectDefaultKafkaProducerFactory.setProducerPerThread(true);
            return objectObjectDefaultKafkaProducerFactory;
        }

        /**
        * 多实例消费
        */
        @Bean
        public ConcurrentMessageListenerContainer<String, String> repliesContainer(ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
            ConcurrentMessageListenerContainer<String, String> container = containerFactory.createContainer("shafish-dev-replies"); // KafkaProperties.DEVREPLIES
            container.getContainerProperties().setGroupId("repliesGroup");
            container.setAutoStartup(false);
            return container;
        }

        @Bean
        public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(ProducerFactory<String, String> producerFactory, ConcurrentMessageListenerContainer<String, String> repliesContainer) {
            return new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
        }
    }
    ```

??? ":three:.消息生产者"

    ``` java
    @RestController
    @RequestMapping("api/v1/message")
    public class MessageController {

        @Autowire
        private ReplyingKafkaTemplate replyingKafkaTemplate;

        @PostMapping("/publishReply")
        public String publishReply(@RequestBody MessageQuery query) throws ExecutionException, InterruptedException, TimeoutException {
            ProducerRecord<String, String> record = new ProducerRecord<>("shafish-dev-requests", query.message()); //KafkaProperties.DEVREQUEST
            // record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, query.message().getBytes()));
            RequestReplyFuture<String, String, String> requestReplyFuture = replyingKafkaTemplate.sendAndReceive(record);
            SendResult<String, String> sendResult = requestReplyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
            System.out.println("send:"+sendResult.getRecordMetadata());

            ConsumerRecord<String, String> stringStringConsumerRecord = requestReplyFuture.get(10, TimeUnit.SECONDS);
            System.out.println("return value:"+stringStringConsumerRecord.value());
            return stringStringConsumerRecord.value();
        }
    }
    ```

??? ":four:.消息消费"

    ``` java
    @Component
    public class ConsumerListener {
        //KafkaProperties.DEVREQUEST
        @KafkaListener(topics = "shafish-dev-requests", groupId = KafkaProperties.consumerGroupId)
        @SendTo
        public String requestListener(ConsumerRecord<String, String> record) {
            Optional<String> message = Optional.ofNullable(record.value());
            if (message.isPresent()) {
                System.out.println(message.get());
    //            Message<String> build = MessageBuilder.withPayload(record.value().toUpperCase())
    //                    //.setHeader(KafkaHeaders.TOPIC, replyTo)
    //                    .setHeader(KafkaHeaders.KEY, 42)
    //                    //.setHeader(KafkaHeaders.CORRELATION_ID, correlation)
    //                    .build();
                return "yes";
            }
            return null;
        }

    }
    ```
