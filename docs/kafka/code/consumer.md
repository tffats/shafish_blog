kafka中的consumer会消费一个或多个partition，每次消费完对应partition的消息后，都需要自动/手动提交对于的offset，用以标记下次消息消费的始点。

## 不论partition，自动提交offset
``` java
static void automaticOffsetCommitting() {
    // https://kafka.apache.org/35/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "192.168.0.103:9092");
    props.setProperty("group.id", "test");
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "1000");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("test-fisha-io"));
    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records)
            System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
    }
}
```

## 手动提交offset
``` java
static void manualOffsetCommitting() {
    // https://kafka.apache.org/35/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "192.168.0.103:9092");
    props.setProperty("group.id", "test");
    props.setProperty("enable.auto.commit", "false");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("test-fisha-io"));
// 批量消费完成后再提交offset
//        final int minBatchSize = 200;
//        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();

    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
//                buffer.add(record);
        }
        // 手动提交
        consumer.commitAsync();
//            if (buffer.size() >= minBatchSize) {
//                // insertIntoDb(buffer); 如果数据库插入失败则不提交offset，避免数据被消费掉
//                consumer.commitSync();
//                buffer.clear();
//            }

    }
}
```

## 按照partition分开消费
``` java
static void manualOffsetCommittingWithPartition() {
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "192.168.0.103:9092");
    props.setProperty("group.id", "test");
    props.setProperty("enable.auto.commit", "false");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("test-fisha-io"));

    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        // 循环每个partition，再分别进行消费（多线程下消费）
        Set<TopicPartition> partitions = records.partitions();
        partitions.forEach(partition -> {
            List<ConsumerRecord<String, String>> pRecords = records.records(partition);
            for (ConsumerRecord<String, String> record : pRecords) {
                System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
            }
            // 计算当前消费的offset
            long offset = pRecords.get(pRecords.size() - 1).offset();
            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
            offsets.put(partition, new OffsetAndMetadata(offset + 1)); // 下一次消费需要从当前offset+1开始，避免最后一个数据重复消费
            consumer.commitSync(offsets);
            System.out.println("=====" + partition + "=====");
        });
    }
}
```

## 指定partition消费
针对前面partition分开消费时，某个partition消费失败的情况，重新消费该partition数据
``` java
static void manualOffsetCommittingWithSomePartition() {
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "192.168.0.103:9092");
    props.setProperty("group.id", "test");
    props.setProperty("enable.auto.commit", "false");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    // 指定需要订阅的partition
    TopicPartition p0 = new TopicPartition("test-fisha-io", 0); // topic,Partition ID
    TopicPartition p1 = new TopicPartition("test-fisha-io", 1); // topic,Partition ID
    TopicPartition p2 = new TopicPartition("test-fisha-io", 2); // topic,Partition ID

    // consumer.subscribe(Arrays.asList("test-fisha-io"));
    /**
     * 只消费 0 partition
     */
    consumer.assign(Arrays.asList(p0));

    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

        Set<TopicPartition> partitions = records.partitions();
        partitions.forEach(partition -> {
            List<ConsumerRecord<String, String>> pRecords = records.records(partition);
            for (ConsumerRecord<String, String> record : pRecords) {
                System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
            }
            // 计算当前消费的offset
            long offset = pRecords.get(pRecords.size() - 1).offset();
            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
            offsets.put(partition, new OffsetAndMetadata(offset + 1)); // 下一次消费需要从当前offset+1开始，避免最后一个数据重复消费
            consumer.commitSync(offsets);
            System.out.println("=====" + partition + "=====");
        });
    }
}
```

## 每个partition对应一个consumer线程消费

## 一个线程接收数据后，再使用线程池创建线程消费