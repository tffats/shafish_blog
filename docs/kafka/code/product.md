## producer
kafka producer线程安全，全局创建一个使用即可

``` java
@Component
public class KafkaProducerManager {

    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;

    @Bean
    public KafkaProducer kafkaProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootstrap());
        properties.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfig.getAckConfig());//kafkaProducerConfig.getAckConfig()
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "cn.shafish.kafkaprod.config.PartitionConfig");

        return new KafkaProducer<>(properties);
    }

}
```

``` java
@Service
public class MsgProductServiceImpl implements MsgProductService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;

    @Override
    public void send(String msg) {

        for (int i =60;i<70;i++) {
            String key = "key-i"+i;
            String value = "value-msg"+i;
            ProducerRecord producerRecord = new ProducerRecord<>(kafkaProducerConfig.getSendTopic(), key, value); // kafkaProducerConfig.getSendTopic()

            kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
                System.out.println(key+" partition:"+recordMetadata.partition()+" offset"+recordMetadata.offset());
            });

        }

    }
}
```