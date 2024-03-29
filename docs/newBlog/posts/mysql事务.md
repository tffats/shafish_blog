---
title: Mysql事务
authors:
    - shafish
date:
    created: 2023-03-12
    updated: 2024-02-02
categories:
    - mysql
---

> 事务是一组的逻辑操作，该操作只有执行成功和失败两种状态。

## 事务的特征

- 原子性：执行的最终状态只有成功和失败这两种；
- 一致性：事务执行之前和执行之后，数据始终处于一致的状态；
- 持久性：事务提交完成后，对数据的操作会被持久化到数据库中，且不会被回滚；
- 隔离性：并发执行的多个事务之间互不干扰。

## 事务的类型

- 扁平事务：常见的使用begin/start transaction 开始，commit/rollback结束的事务。事务全部执行成功，或全部执行失败；
- 保存点扁平事务：在扁平事务的基础上，添加有事务保存点，可以进行回滚至保存点的操作（也就是事务的部分回滚）；
    - 设置事务保存点：`savepoint pointName`
    - 回滚至保存点：`rollback to pointName`
    - 删除保存点：`release savepoint pointName`
- 链式事务：事务的提交操作与下一个事务的开始操作为原子不可分，没有间隔；
- 嵌套事务：事务操作包含内部子事务，子事务提交后，顶层事务不会全部提交，只有顶层事务提交完成后，整个事务才算提交完成；
- 分布式事务：`不同数据库`、`不同服务器`的各个分支事务要全部提交成功，或全部提交失败。

<!-- more -->

## 本地事务
程序应用与数据库存在同一服务器内，业务需求的事务操作通过本地安装的数据库事务来完成。

## 并发事务问题

- 脏写：也叫覆盖写；
- 脏读：当前事务会读取到并行事务中未提交的数据，当并行事务回滚时造成当前事务读取的数据不准确；
- 不可重复读：当前事务在并行事务更新或删除数据前后，读到的数据不一致；
- 幻读：当前事务在并行事务插入数据前后，读取的数据不一致。

## 事务的隔离级别

- 读未提交(`read uncommitted`)：读取到并行事务未提交的数据，出现脏读的情况；这种情况属于读写冲突，保证写完再读取即可；
- 读已提交(`read committed`)：oracle默认隔离级别，当前事务读取的数据在并行事务修改提交前后不一致，出现不可重复读的情况；也是读写冲突，保证读完再写；也就是当前事务开始时读取的数据不会被并行事务可见；
- 可重复读(`repeatable read`)：mysql默认隔离级别，当前事务读取的数据量可以被并行事务增加/删除，出现幻读的情况；保证按顺序读写即可；
- 串行(`serializable`)：不能同时执行两个或以上的事务。

## mysql锁

- 悲观锁：读操作和写操作都要加锁，不能同时执行；
- 乐观锁：通过添加特别字段声明数据的版本号来实现，根据版本号来判断是否修改；
- 读锁：共享锁-S锁，同一份数据可以添加多个读锁，但不能添加写锁；
- 写锁：排它锁-X锁，在添加写锁后，只有等释放该锁后，才能继续添加读锁，或者写锁；
- 表锁：对整张表添加读锁、写锁，最大粒度；规则跟上面读写锁介绍一样；
- 行锁：对表中的某行数据添加读锁、写锁，粒度最细；
- 页面锁：对页结构添加读锁、写锁，粒度中等；
- 间隙锁：在可重复读隔离级别下，对某范围数据进行读取后，并行事务不能在该范围插入或修改数据，因为会对该范围加锁；
- 临键锁：行锁和间隙锁结合。

## 死锁产生、处理

- 产生：
    - 互斥：某资源只能被一个进程使用，其他进程需要使用时只能等待；
    - 不可剥夺：资源只能由对应占有的进行主动释放，不能被其他进程强行夺走；
    - 请求与保持：在占用资源的情况下，请求被其他进程占用的资源会阻塞；
    - 循环：多个进程互相占用对方需要的资源，形成循环阻塞。

- 处理：
    - 预防：写代码的时候留意点共享资源；
    - 避免：数据操作的粒度、查询的范围尽量小点；
    - 检测：innodb引擎的等待图；
    - 解除：资源有序分配。

    ## redo log

    - 保证事务原子性、持久性的事务log，记录事务发生时的数据的修改操作。
    - log数据的刷盘设置：innodb_flush_log_at_trx_commit变量取值
        - 0：先提交事务日志到`log buffer缓存`，每隔一秒写入系统内核buffer，调用系统函数fsync()写入磁盘；
        - 1：事务日志直接写入系统内核buffer，调用系统函数fsync()写入磁盘；默认，但性能较差
        - 2：事务日志写入系统内核buffer，每隔一秒调用系统函数fsync()写入磁盘。

    ## undo log

    - 记录事务开启前的数据，和开启事务后对应相反的数据操作。
    - 比如：开启一个事务，insert一条数据，这时undo log就会记录一条delete语句，当事务回滚时，执行undo log就会把数据恢复到事务开始前的状态。

    ## bin log

    - 记录数据库表结构变更和表数据变更的二进制日志。用与主从数据库同步和数据恢复场景。
    - 3中记录设置：
        - row：直接记录修改的数据，但是在批量操作时会产生大量数据；
        - statement：记录修改数据的sql语句，但是设计系统函数的数据会出现不一致，比如`now()`函数等;
        - mix：一般情况使用statement，需要时就用row模式记录。

## mysql事务执行流程（粗略）

- 事务提交：
    - 事务开始
    - 将操作的待更新数据进行加锁
    - 记录undo log对应的redo log到内存缓存
    - 记录undo log到内存缓存
    - 记录redo log到内存缓存
    - 在内存中更新数据
    - 提交事务触发redo log刷盘
    - undo log、数据页通过检查点机制刷盘
    - 事务结束

- 事务回滚
    - mysql宕机
    - 重启mysql
    - 获取检查点
    - 使用redo log恢复数据
    - 检查事务是否已经提交
    - 已提交：直接结束
    - 未提交：使用undo log回滚数据，结束。

## spring事务

- spring框架提供事务管理功能，使用时不用手动开启、提交/回滚事务，配置好相关的事务管理后，只要在需要的地方使用@Transactional注解即可。
- spring事务回滚规则：
    - 默认是在事务执行过程中遇到runtimeException时进行回滚操作；
    - 可通过`@Transactional(rollbackFor=Exception.class)`指定遇到exception异常类或其子类时都进行回滚。
- spring事务的三大接口
    - PlatformTransactionManager事务管理器：spring的事务具体实现是通过hibernate、mybatis等框架实现的；
    - TransactionDefinition：定义了事务的相关方法、属性等；
    - TransactionStatus：定义了事务执行的状态。

## spring事务的隔离级别

- isolation_default：spring框架的默认事务隔离级别，直接使用对应数据库默认的隔离级别。
- isolation_read_uncommitted：读未提交
- isolation_read_committed：读已提交
- isolation_repeatable_read：可重复读
- isolation_serializab：按顺序的串行化执行

## spring事务的传播机制
多个事务执行场景处理，@Transactional(propagation=Propagation.REQUIRED)

- required：spring默认事务传播类型，当前操作外部存在一个事务则加入该事务，如果没有则创建新事务执行;
- requires_new：如果当前操作外部存在事务，则将外部事务挂起，重新创建一个新事务执行，等提交或回滚后再执行外部事务；
- supports：如果当前执行没有事务，则已无事务方式执行；
- mandatory：当前执行必须一个事务，如果没有就报错
- not_supported：已非事务执行
- nerver：已非事务执行，如果存在事务则报错
- nested：嵌套事务

## spring事务失效场景

- 使用了不支持事务的数据库：或者说相关的存储引擎木有支持事务
- 事务方法没有注入到spring容器中
- 事务相关的方法不是pulic修饰
- 同一个类中的事务方法调用
- 木有配置事务管理器
- 不正确捕抓异常：比如说用了try catch直接捕抓了，事务相关异常木有被spring知道
- spring中默认回滚发生runtimeException的事务（使用rollbackFor修改为 Exception.class）