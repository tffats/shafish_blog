---
title: /dev/null是个啥
hide:
  - navigation
---

# /dev/null是个啥

[Back](javascript:history.back(-1)){ .md-button}

ref: [https://www.putorius.net/introduction-to-dev-null.html](https://www.putorius.net/introduction-to-dev-null.html){target=_blank}

本文介绍下什么是dev/null，以及它是如何使用der。

<span id="english">come across <span class="point">:sound:</span></span> 遇见
<span id="english">invaluable <span class="point">:sound:</span></span> 无价的
<span id="english">punch cards <span class="point">:sound:</span></span> 打孔卡
<span id="english">pseudo <span class="point">:sound:</span></span> 假的
<span id="english">suppress <span class="point">:sound:</span></span> 抑制；镇压；废止

## /dev/null是什么
在linux中，/dev/null是系统启动时生成的特殊文件，该文件没有大小，也不占任何磁盘空间，可以被系统中的所有用户读写。/dev/null被设置成设备符的形式访问，它能接受所有一切的输入，但永远返回空字符。

/dev/null可以简称空设备，或者称为位桶（bit bucket），还有人称为比特池。

## 为啥叫bit bucket
早期数据以是否穿孔的形式保存在一张纸片上，机器读取过的、没用的纸片就会丢弃到一个桶里（物理上的桶），后来的后来这个桶就被人们称为了bit bucket。
这个桶在现在电子时代对应的就是/dev/null。

## /dev/null用法
- 消除输出：`stat /etc/passwd > /dev/null`
- 使用标准error输出：`stat /etc/passwd 2> /dev/null`
- 快速清除文件内容：`cat /dev/null > xxx.log`或者`cp /dev/null xxx.log `