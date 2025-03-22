# Kafka 架构核心结构

## 1. 核心组件
├── **生产者 (Producer)**
│   ├── 发送消息到 Topic
│   ├── 关键配置
│   │   ├── `acks` (0/1/all)
│   │   ├── `batch.size`
│   │   ├── `linger.ms`
│   │   ├── `compression.type` (snappy/lz4)
│   │   └── `max.in.flight.requests.per.connection`
├── **消费者 (Consumer)**
│   ├── 从 Topic 拉取消息
│   ├── 关键配置
│   │   ├── `group.id`
│   │   ├── `auto.offset.reset` (latest/earliest)
│   │   ├── `enable.auto.commit`
│   │   └── `max.poll.records`
├── **Broker**
│   ├── Kafka 服务节点
│   ├── 核心功能
│   │   ├── 消息存储（分区副本）
│   │   ├── 请求处理（生产/消费）
│   │   └── 副本同步（ISR 管理）
│   └── 关键配置
│       ├── `num.network.threads`
│       ├── `num.io.threads`
│       ├── `log.dirs`
│       └── `unclean.leader.election.enable`
└── **ZooKeeper**
    ├── 集群元数据管理
    ├── Broker 注册
    └── Topic 配置存储

## 2. Topic 与分区
├── **Topic**
│   ├── 逻辑消息分类
│   ├── 创建配置
│   │   ├── 分区数 (`num.partitions`)
│   │   └── 副本数 (`replication.factor`)
├── **Partition**
│   ├── 物理分片（并行处理）
│   ├── 分区策略
│   │   ├── 轮询 (Round-Robin)
│   │   ├── 哈希 (Key Hashing)
│   │   └── 自定义分区器
└── **副本 (Replica)**
    ├── Leader 副本（读写）
    └── Follower 副本（同步）

## 3. 消息存储机制
├── **日志分段 (Log Segment)**
│   ├── 文件结构
│   │   ├── .log（数据）
│   │   └── .index（索引）
├── **留存策略**
│   ├── 时间 (`log.retention.hours`)
│   └── 大小 (`log.retention.bytes`)
└── **刷盘策略**
    ├── `log.flush.interval.messages`
    └── `log.flush.interval.ms`

## 4. 高可用与容灾
├── **ISR (In-Sync Replicas)**
│   ├── 同步副本集合
│   └── Leader 选举依据
├── **副本同步**
│   ├── `replica.lag.time.max.ms`
│   └── `min.insync.replicas`
└── **故障恢复**
    ├── Broker 宕机处理
    └── 数据重新平衡

## 5. 性能调优
├── **生产者优化**
│   ├→ 提高吞吐：增大 `batch.size`、`linger.ms`
│   └→ 提升可靠性：`acks=all`、启用重试
├── **消费者优化**
│   ├→ 多线程消费：`max.poll.records`
│   └→ 避免重复消费：手动提交 Offset
└── **Broker 优化**
    ├→ 线程池调整：`num.network.threads`
    └→ 磁盘 IO：`num.io.threads`

## 6. 监控与管理
├── **监控指标**
│   ├→ 生产速率
│   ├→ 消费延迟
│   └→ Broker 负载
├── **工具**
│   ├→ Kafka Manager
│   ├→ Prometheus + Grafana
│   └→ Burrow（消费延迟）
└── **运维命令**
    ├→ `kafka-topics.sh`
    ├→ `kafka-consumer-groups.sh`
    └→ `kafka-configs.sh`

## 7. 安全机制
├── **认证**
│   ├→ SSL/TLS
│   └→ SASL（PLAIN/SCRAM）
└── **授权**
    ├→ ACL（访问控制列表）
    └→ RBAC（基于角色）

## 8. 高级特性
├── **Exactly-Once 语义**
│   ├→ 幂等生产者 (`enable.idempotence`)
│   └→ 事务 API
└── **Kafka Connect**
    ├→ 数据源/目标集成
    └→ 分布式模式

## 9. 版本特性
├── 0.11+：幂等与事务
├── 2.4+：增量 Cooperative Rebalancing
└── 3.0+：ZooKeeper 移除（KIP-500）

## 10. 最佳实践
├── **集群规划**
│   ├→ Broker 数量 >= 副本数
│   └→ 分区数估算（TPS/吞吐）
└── **版本升级**
    ├→ 滚动升级
    └→ 兼容性检查