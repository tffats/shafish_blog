---
title: 记一次netty内存泄漏
authors:
    - shafish
date:
    created: 2024-03-02
    updated: 2024-03-02
categories:
    - netty
    - bug
    - 内存泄漏
---

## 一、场景
目前使用基于netty的tcp长连接处理电梯秒级数据。

具体来说是处理电梯的modbus报文，这个主从协议要求获取设备的数据需要主动下发命令来触发上报。在频繁的报文接收、处理及发送中，内存泄漏叻。

## 二、特征
一般内存泄漏主要表现在服务器内存爆满，某个类实例数特别多、占有内存特别多，GC到不行时服务器自动重启，当然最明显的就是会有leak报错（确定服务器崩的大概时间点，翻下error日志，你会找到的），比如下面这样：

![](https://picture.cdn.shafish.cn/blog/bug/430939DA-DBBF-4c82-9078-C28B8340B58C.png){: .zoom}

<!-- more -->

``` java
[xxxx-xxx-xxx 04:24:33.984] [ERROR] - io.netty.util.ResourceLeakDetector.reportTracedLeak(ResourceLeakDetector.java:320) - LEAK: ByteBuf.release() was not called before it's garbage-collected. See https://netty.io/wiki/reference-counted-objects.html for more information.
Recent access records: 
Created at:
io.netty.buffer.PooledByteBufAllocator.newDirectBuffer(PooledByteBufAllocator.java:349)
io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:187)
io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:178)
io.netty.buffer.AbstractByteBufAllocator.buffer(AbstractByteBufAllocator.java:115)
com.xxx.iot.web.modules.iot.manager.plugin.tcp.netty.xxxx.write(xxx.java:20)
io.netty.channel.AbstractChannelHandlerContext.invokeWrite0(AbstractChannelHandlerContext.java:715)
io.netty.channel.AbstractChannelHandlerContext.invokeWriteAndFlush(AbstractChannelHandlerContext.java:762)
io.netty.channel.AbstractChannelHandlerContext.write(AbstractChannelHandlerContext.java:788)
io.netty.channel.AbstractChannelHandlerContext.writeAndFlush(AbstractChannelHandlerContext.java:756)
io.netty.channel.AbstractChannelHandlerContext.writeAndFlush(AbstractChannelHandlerContext.java:806)
com.xxx.iot.web.modules.iot.manager.plugin.tcp.netty.xxx.sendData(xxx.java:26)
com.xxx.iot.web.modules.iot.manager.tcp.elevator.xxx.handleData(xxxx.java:212)
com.xxx.iot.web.modules.iot.manager.plugin.tcp.netty.xxx.channelRead(xxxx.java:73)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:355)
io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:321)
io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:308)
io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:422)
io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:276)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:355)
io.netty.handler.timeout.IdleStateHandler.channelRead(IdleStateHandler.java:286)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:355)
io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:377)
io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:363)
io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163)
io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
java.lang.Thread.run(Thread.java:745)
```

## 三、泄漏点定位
> 为了便于用户发现内存泄露，Netty提供4个检测级别:

- `disabled` 完全关闭内存泄露检测
- `simple` 以约1%的抽样率检测是否泄露，默认级别
- `advanced` 抽样率同simple，但显示详细的泄露报告
- `paranoid` 抽样率为100%，显示报告信息同advanced

运行程序jar时添加命令即可：`jav xxx -Dio.netty.leakDetectionLevel=[检测级别]`，或者直接在代码中使用：`ResourceLeakDetector.setLevel(ResourceLeakLevel.检测级别);`

> 一般netty的内存泄漏，大部分都是没有及时进行byteBuf的内存释放。

在netty4后，byteBuf的回收是引用计数判断，数据读取或发送时都会使用到池化bytebuf进行缓冲，一般情况下框架会自动释放buf的引用，也就是数据正常处理完后byteBuf的引用数（refCnt）为0。

但是如果自定义的处理类继承了 `ChannelInboundHandlerAdapter`，则需要手动在channelRead中进行buf释放，而继承 `SimpleChannelInboundHandler` 则业务中不需要主动释放buf。ps: `SimpleChannelInboundHandler extends ChannelInboundHandlerAdapter`

> 但显然，这次事故中不仅池化堆外内存（PooledUnsafeDirectByteBuf）泄漏，还有 `ChannelOutboundBuffer`也非常不正常，这个buf是数据写入至发送网络前用到的，结合leak报错提到的业务代码 `xx.sendData`,可以大概率猜到内存的泄漏点了。

首先是 `PooledUnsafeDirectByteBuf` 泄漏的问题，排除漏填 `ReferenceCountUtil.release()`后，又过几遍解码处理到编码处理的业务代码，找到了问题所在：解码处理的时候有部分buf没有读取。（readBytes()）

因为业务的需求，端口需要支持多种协议解析，该解析是在解码时操作的。因为某协议的设备不会频繁上报报文，基本不会粘包，对应处理时直接过滤字节长度不符的报文了，所以并没有读取到这些过滤掉的报文，造成这部分buf泄漏。
要解决这个问题需要把过滤操作移到 `Handler.channelRead()` 中根据不同协议处理，解码的时候全部 `readBytes()` 出来就行。

第二点最明显就是发送数据到网络时造成的泄漏，或者说堵塞，可能是网络等等的问题，设备端接收报文很慢，但服务端在一股脑的发送，造成消息堆积。解决这个也简单：在发送时设置 `等待写入网络的消息数量` 阈值以及判断通道是否可写就能解决。

## 四、解决
``` java
// buf释放
ReferenceCountUtil.release(msg);
// 等待写入网络的消息数量 ==》写入时判断
channelHandlerContext.channel().unsafe().outboundBuffer().size()
// 连接通道是否写入
// 1.需要先在启动时定义高低位
serverBootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024, 8 * 1024 * 1024));
// 2.写入时判断
channelHandlerContext.channel().isWritable()
```


ref:

- [https://www.cnblogs.com/zhaoshizi/p/13097061.html](https://www.cnblogs.com/zhaoshizi/p/13097061.html){target=_blank}
- [https://netty.io/wiki/user-guide-for-4.x.html](https://netty.io/wiki/user-guide-for-4.x.html){target=_blank}
- [https://github.com/netty/netty/issues/4134](https://github.com/netty/netty/issues/4134){target=_blank}
