---
title: kafka
description: linux, kafka, docker
hide:
  - navigation
---


## 环境安装（Docker）

[https://github.com/shafishcn/ToolMan/blob/master/docker/kafka.md](https://github.com/shafishcn/ToolMan/blob/master/docker/kafka.md){target=_blank}

## 零、概念

- `消息`（Record）：数据实体;
- `生产者`：发布消息的应用;
- `消费者`：订阅消息的应用;（不同消费者之间消费消息互不干预）
- `主题`（Topic）：消息的逻辑分组，可以当成消息的分类;（实际使用中用来区分具体的业务）
- `消费组`：多个消费者组成的一个逻辑分组，同时消费多个分区以实现高吞吐;
- `broker`：kafka服务节点，负责接收、存储生产者发送的消息，并将消息投递给消费者;
- `分区`（Partitions）：一个有序不变的消息序列。一个或多个分区组成一个主题，主题消息可以划分给不同分区，并将不同的分区存储到不同的broker中，实现分布式存储;（每个分区都有对应的下标）
- `副本`（Replicas）：同一条消息能够被拷贝到多个地方以提供数据冗余，这些地方就是所谓的副本。每个分区都有一个或多个副本，其中1个leader,0或多个follow副本，每个副本都保存了该分区的全部数据。（kafka将一个分区的不同副本保存到不同broker中，保证数据安全）
- `ISR`（In Sync Replicas）：分区中与leader副本数据保持一定程度同步的副本集;（该集合也包括leader副本本身）
- `ACK机制`：生产者发送消息成功的响应处理;消费者消费成功的响应处理;
- `ACK偏移量`（Offset）：消息在分区中的偏移量offset
- `消费者位移`（Consumer Offset）：消费者位移，标识消费者消费进度，每个消费者都有自己的消费者位移;
- `Zookeeper`：存储主题、分区等数据，并完成broker节点监控、controller选举等工作;
- `重平衡`（Rebalance）：消费者组内某个消费者实例挂掉后，其他消费者实例自动重新分配订阅主题分区的过程;

![](https://file.cdn.shafish.cn/blog/kafka/2023-02-19_22-37.png)

## 一、部分参数
- `log.dirs`：broker需要使用的文件目录路径，在生产环境中需要配置多个路径，且目录挂载到不同的物理磁盘中。
    - 多块物理磁盘读写数据高
    - 故障转移
- `zookeeper.connect`：保存kafka集群元数据信息，zk1:2181,zk2:2181,zk3:2181
- `listeners`：监听器，外部连接者（服务api）通过指定主机名、端口来访问
- `advertised.listeners`：broker对外发布（broker之间）
- `auto.create.topics.enable`（false）：是否允许自动创建topic
- `unclean.leader.election.enable`（false）：unclean leader选举
- `auto.leader.rebalance.enable`（false）：定期进行leader选举
- `log.retention.{hours|minutes|ms}`：全局控制一条信息被保存多久，`log.retention.hours=168表示默认保存7天的数据`
- `log.retention.bytes`：指定broker为信息保存的总磁盘容量大小
- `message.max.bytes`：控制broker能接收的最大消息大小

- `compression.type`：启用指定类型的压缩算法

- `retention.ms`：规定该topic信息被保存的时长
- `retention.bytes`：规定为该topic预留多大磁盘空间
- `max.message.bytes`：决定broker能正常接收该topic最大信息的大小

- `KAFKA_HEAP_OPTS`：运行jvm堆大小
- `KAFKA_JVM_PERFORMANCE_OPTS`：指定GC参数

- `ulimit -n`：文件描述符限制
- `ZFS文件系统`
- `swap分区`：1，保留空间做观测

## 二、分区
为什么分区？
分区的作用是提供负载均衡的能力，把不同的分区放到不同节点的机器上，这样每个节点都能独立执行各自分区的读写请求。

**分区是实现负载均衡以及高吞吐量的关键**

决定生产者将信息发送到哪个分区称为分区策略。
### 1.轮询策略（默认）
顺序分配，假如一共3个分区，则第一条信息被分到0分区，第二条信息分到1分区，第三条信息分到2分区，依此循环。
轮询策略能很好将数据均匀分布，有很好的负载均衡表现。

### 2.随机策略
随意将消息放置在任意分区

### 3.按key分配（默认）
kafka中可以为消息定义key值，设置相同key的消息分配到相同的分区中，这样就保证了相同key的消息在同一个分区中顺序不会错乱。

> kafka中设置了两个默认分区策略，如果消息没有定义key则按轮询策略分配分区，如果指定了key则按key分配。

### 4.按地理位置分区

## 三、消息压缩
- 生产端压缩：对整个消息集进行压缩，并把压缩的元数据也保存在消息集中
- broker端压缩：如果broker端使用了不同于生产端的压缩算法（compression.type!=producer），或者broker端发生了消息格式的转换，那么也会触发broker端压缩。

### 压缩算法（19年）
- 吞吐量方面：LZ4 > Snappy > zstd和GZIP
- 压缩比方面，zstd > LZ4 > GZIP > Snappy

## 四、无消息丢失配置
- 当broker成功地接收到一条消息并写入到日志文件后（`acks = all`），在至少有1个broker存活情况下,这条消息永远不会丢失。;
- Producer永远要使用带有回调通知的发送API，也就是说不要使用producer.send(msg)，而要使用producer.send(msg, callback;
- Consumer端维持先消费消息（阅读），再更新位移（书签）的顺序;
- 如果是多线程异步处理消费消息，Consumer程序不要开启自动提交位移，而是要应用程序手动提交位移;

- 设置acks = all(必须等待ISR中所有副本都接收了消息后再进行处理)
- 设置retries为一个较大的值
- 设置unclean.leader.election.enable = false
- 设置replication.factor >= 3,将消息多保存几份
- 设置min.insync.replicas > 1,控制的是消息至少要被写入到多少个副本才算是“已提交”
- 确保消息消费完成再提交

## 五、拦截器
可应用于客户端监控、端到端系统性能检测、消息审计等功能。

- 生产者拦截器（ProducerInterceptor接口）：在发送消息前以及消息提交成功后植入你的拦截器逻辑
- 消费者拦截器（ConsumerInterceptor接口）：在消费消息前以及提交位移后编写特定逻辑


## 六、消息的可靠性
消息成功发送的标识：broker成功接收到一条消息并写入到日志文件，且producer接收到broker的应答。

当producer端无法确认消息提交成功时，默认会进行重试，以保证提供至少一次信息的可靠，不过这就会造成消息重复发送。

- 最多一次：消息不会重复发送，但可能会丢失
- 至少一次：消息会重复发送，但不会丢失
- 精确一次：消息不会丢失，也不会重复发送

### 精确一次发送
- 幂等性：producer发送多次消息，broker端把重复的过滤（大致）。
  - 只能实现单分区、单会话的幂等
  - `props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);`
- 事务：读已提交
  - 保证多条消息原子性写入到分区
  - 保证消费者只能看到事务成功提交的消息
  - `enable.idempotence = true`
  ``` java
  producer.initTransactions();
  try {
              producer.beginTransaction();
              producer.send(record1);
              producer.send(record2);
              producer.commitTransaction();
  } catch (KafkaException e) {
              producer.abortTransaction();
  }
  ```

> 过多的配置会导致性能变差

## 七.消费
消费组：多个消费者组成的组，组内所有消费组协调来消费主题的所有分区，但一个分区只能由组内的一个消费者来消费。

多个消费组可以订阅同一个主题，组与组彼此独立消费

消费者的数量=该消费组订阅主题的分区总数

消费位移保存在内部主题`__consumer_offsets`中

### 1.重平衡
为消费者重新分配订阅主题的分区
- 消费组内消费者数量发生变更
- 消费组订阅的主题数发生变更
- 主题的分区数发生变更

> 当触发重平衡时，所有的消费者都会停止消费，直到完成重平衡分配

减少重平衡：
- broker（coordinator）判断消费者离线的时间间隔：`session.timeout.ms`
  - `session.timeout.ms = 6000`（session.timeout.ms >= 3 * heartbeat.interval.ms）
- 消费者发送心跳请求频率：`heartbeat.interval.ms`
  - `heartbeat.interval.ms = 2000`
- 消费者消费消息的间隔，默认消费者在5分钟内无法消费完poll方法返回的消息，消费者就会发起离组请求：`max.poll.interval.ms`
  - `max.poll.interval.ms=最久逻辑处理时长`，给消费端留足够的处理时间
- 运维合理


> 记录：减少poll返回的消息数量：`max.poll.records`，默认为500条

### 2.消费位移
指明了消费者在分区中要消费的`下一条`消息所在位置（也就是说位移前的消息都是被确定提交消费成功的）

存在消息位移主要是保障消费者发生故障重启后，能读取保存的位移值，从对应位移继续消费，避免所有消息重新消费。

- 手动提交位移值：`enable.auto.commit=false`，可以避免消息在消费端接收后，但处理失败时发生消息丢失的情况，因为是处理完成后再手动提交消费位移的。
``` java
public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.161:9092");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
    KafkaConsumer consumer = new KafkaConsumer<String, String>(properties);
    consumer.subscribe(Arrays.asList("test-ken-io"));
    for(;;) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        for(ConsumerRecord<String, String> record:records) {
            System.out.println(record);
        }
        // try {
        //     // 同步阻塞，知道broker返回处理结果
        //     consumer.commitSync();
        // } catch (CommitFailedException e) {
        //     // 处理提交失败异常
        //     System.out.println(e.getMessage());
        // }
        // 异步提交位移
        onsumer.commitAsync((offsets, exception) -> {
          if (exception != null) handle(exception);
        });
    }
}
```
``` java
public class BasicConsumerHand {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.161:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        KafkaConsumer consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList("test-ken-io"));
        try {
            for(;;) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
                for(ConsumerRecord<String, String> record:records) {
                    System.out.println(record);
                }
                try {
//阶段性的消息处理，使用异步提交位移值，避免程序阻塞
                    consumer.commitASync();
                } catch (CommitFailedException e) {
                    // 处理提交失败异常
                    handle(exception);
                }
            }
        } catch (Exception e) {
            handle(exception);
        } finally {
            try {
// 消费者要关闭前，进行同步阻塞提交位移，保证保存正确的位移数据
                consumer.commitsync();
            } finally {
                consumer.close();
            }
        }

    }
}    
```
``` java
// 设置每消费100条消息就提交一次位移值
public static void main(String[] args) {

    Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
    int count = 0;

    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.161:9092");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
    KafkaConsumer consumer = new KafkaConsumer<String, String>(properties);
    consumer.subscribe(Arrays.asList("test-ken-io"));
    try {
        for(;;) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            for(ConsumerRecord<String, String> record:records) {
                System.out.println(record);
                offsets.put(new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1));
                if (count % 100 ==0) {
                    consumer.commitAsync(offsets, null);
                }
                count++;
            }
            try {
                consumer.commitAsync();
            } catch (CommitFailedException e) {
                // 处理提交失败异常
                handle(exception);
            }
        }
    } catch (Exception e) {
        handle(exception);
    } finally {
        try {
            consumer.commitSync();
        } finally {
            consumer.close();
        }
    }

}
```

- 自动提交位移值：`enable.auto.commit=true`，默认提交时间`auto.commit.interval.ms=5000`5秒自动提交一次分区消费位移值
``` java
public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.161:9092");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
    KafkaConsumer consumer = new KafkaConsumer<String, String>(properties);
    consumer.subscribe(Arrays.asList("test-ken-io"));
    for(;;) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        for(ConsumerRecord<String, String> record:records) {
            System.out.println(record);
        }
    }
}
```

### 3.内部主题
`__consumer_offsets`：kafka可以默认创建的一个普通主题，默认分配50个分区，3个副本，用来保存各消费者消费对应分区时`频繁修改`的消费位移信息
- 消息格式：
  - key：保存 消费组id+主题名+分区号
  - value：消费元数据

- 删除主题中的过期消息：kafka中有个专门的线程`Log Cleaner`对相同key进行整理，保留最新时间的消息。如果位移主题出现磁盘无限膨胀的情况，可以检查下该线程是否存活。

### 4.独立消费者
就一个消费者

### 5.多线程消费
- 多线程创建消费者，多个消费者并行处理分区消息
  - 线程数受限主题分区数（每个分区主副本只能被同消费组的某一个消费者消费）
  - 线程处理容易处理超时
- 单线程创建消费者，但使用多线程处理分区消息
  - 需维护消费顺序
  - 需维护位移提交

### 6.消费进度监控
> lag值：分区当前最新生产消息位移-消费组当前最新消费消息位移
> lead值：消费者最新消息的位移与分区当前第一条消息位移的差值

- kafka-consumer-groups
    - 命令使用：`./kafka-consumer-groups.sh --bootstrap-server 192.168.0.161:9092 --describe --group test-group（消费组）`
    - 命令会按照消费组订阅主题的分区进行展示
    - current-ofset：当前分区最新消费信息的位移值
    - log-end-offset：分区当前最新生产的消息位移值
    - lag：消费差值（20表示还有20条信息未被消费）
    - consumer-id：消费者id
- Java Consumer api
    - 查询当前分区最新消息位移：`client.listConsumerGroupOffsets(groupID);`
    - 查询消费组最新消费信息位移：`consumer.endOffsets(consumedOffset.keySet())`
- JMX
    - `kafka.consumer:type=consumer-fetch-manger-metrics,partition="", topic="", client-id=""`

## 八.分区副本
kafka主题中只有分区leader副本能提供消息生产和消费。

- 1.生产者写入消息后，消费者能马上读取到
- 2.消息读取一致性，如果分区副本也提供消费则可能出现消息未同步时消费者读取不到消息的情况

### 1.ISR
同步副本集，在集合中的副本可以理解为与leader分区是消息同步的。

> kafka判断副本是否同步的标准，不是副本间相差的消息数，而是`replica.lag.time.max.ms`(Follower副本能够落后Leader副本的最长时间间隔)

## 九.请求处理

![](https://file.cdn.shafish.cn/blog/kafka/2023-02-19_22-43.png)

