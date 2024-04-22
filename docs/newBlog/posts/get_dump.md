---
title: dump文件获取
authors:
    - shafish
date:
    created: 2024-04-18
categories:
    - JVM
---

> hprof文件是一种Java堆转储文件(二进制文件)，用于分析和诊断Java应用程序的内存使用情况。它包含了Java堆中的对象信息，包括对象的类型、大小、引用关系等。通常情况下，.hprof文件是由Java虚拟机（JVM）生成的，用于记录应用程序在某个时间点的内存快照。

> .hprof文件可以提供有关应用程序内存分配、对象泄漏、内存使用量等方面的详细信息。通过分析.hprof文件，开发人员可以确定内存中的对象数量、对象类型、对象之间的引用关系，以及哪些对象可能占用了大量内存或存在内存泄漏的风险。

下面介绍几种获取jvm 堆快照的方法。

## 零、获取java程序进程id
- 准备示例程序

``` shell
curl -O https://arthas.aliyun.com/math-game.jar
java -jar math-game.jar
```

- 获取进程id

``` shell
jps
```

- 结果
``` shell
➜  jvm jps
265524 math-game.jar
268296 Jps
```

<!-- more -->

## 一、JDK工具包（命令）
### 1.1 jmap

> `jmap -dump:file=<file-path> <pid>`

``` shell
jmap -dump:file=./math.hprof 265524
```

jamp可以获取本地或远程jvm运行快照，但需要注意的是jmap命令是一个实验性工具？ https://docs.oracle.com/en/java/javase/17/docs/specs/man/jmap.html

### 2. jcmd

> `jcmd <pid> GC.heap_dump <file-path>`

``` shell
jcmd 265524 GC.heap_dump ./dump.hprof
```

## 二、程序主动dump
通过配置jvm运行时参数，执行具体功能。

> `java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<file-or-dir-path>`

``` shell
java -Xmx256M -Xms256M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/jvm/log xxx.jar
```

## 三、gcore

> 针对系统无法使用jmap等工具场景下使用： `gcore <pid>`

- 生成core堆后重启系统

``` shell
# yum install -y gdb
sudo pacman -S gdb
gcore 265524
```

- 重启系统后再转成java dump

``` shell
jmap -dump:format=b,file=heap.hprof `which java` core.265524
```

ref:

- [Different Ways to Capture Java Heap Dumps](https://www.baeldung.com/java-heap-dump-capture){target=_blank}
- [由JDK bug引发的线上OOM](http://ifeve.com/%e7%94%b1jdk-bug%e5%bc%95%e5%8f%91%e7%9a%84%e7%ba%bf%e4%b8%8aoom/){target=_blank}
- [gcore提示ptrace: Operation not permitted.](https://askubuntu.com/a/148882){target=_blank}
- [jmap执行失败了，怎么获取heapdump？](https://www.cnblogs.com/codelogs/p/17323197.html){target=_blank}