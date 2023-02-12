---
title: kafka
description: linux, kafka, docker
hide:
  - navigation
---


## 环境安装（Docker）

[https://github.com/shafishcn/ToolMan/blob/master/docker/kafka.md](https://github.com/shafishcn/ToolMan/blob/master/docker/kafka.md){target=_blank}

## 零、概念
> 在完成一、二、三步骤后，对照kafka-ui界面再重复阅读！！

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
### 轮询策略（默认）
顺序分配，假如一共3个分区，则第一条信息被分到0分区，第二条信息分到1分区，第三条信息分到2分区，依此循环。
轮询策略能很好将数据均匀分布，有很好的负载均衡表现。

### 随机策略
随意将消息放置在任意分区

### 按key分配（默认）
kafka中可以为消息定义key值，设置相同key的消息分配到相同的分区中，这样就保证了相同key的消息在同一个分区中顺序不会错乱。

> kafka中设置了两个默认分区策略，如果消息没有定义key则按轮询策略分配分区，如果指定了key则按key分配。

### 按地理位置分区

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

- 设置acks = all
- 设置retries为一个较大的值
- 设置unclean.leader.election.enable = false
- 设置replication.factor >= 3,将消息多保存几份
- 设置min.insync.replicas > 1,控制的是消息至少要被写入到多少个副本才算是“已提交”
- 确保消息消费完成再提交

## 五、拦截器
可应用于客户端监控、端到端系统性能检测、消息审计等功能。

- 生产者拦截器（ProducerInterceptor接口）：在发送消息前以及消息提交成功后植入你的拦截器逻辑
- 消费者拦截器（ConsumerInterceptor接口）：在消费消息前以及提交位移后编写特定逻辑