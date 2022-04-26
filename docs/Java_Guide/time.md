---
title: Java案例解析-时间处理
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](/Java_Guide/#八Time){ .md-button}

!!! note "Java8的time包是基于Joda-Time进行构建的，强推"

## 一、基本类

``` java linenums="1" title="1-1"
System.out.println("Instant.now():       " + Instant.now());  // (1)
System.out.println("LocalDate.now():     " + LocalDate.now());  // (2)
System.out.println("LocalTime.now():     " + LocalTime.now());  // (3)
System.out.println("LocalDateTime.now(): " + LocalDateTime.now());  // (4)
System.out.println("ZonedDateTime.now(): " + ZonedDateTime.now());  // (5)

/*
*Instant.now():       2022-04-22T14:08:25.417575Z
*LocalDate.now():     2022-04-22
*LocalTime.now():     22:08:25.506247
*LocalDateTime.now(): 2022-04-22T22:08:25.506403
*ZonedDateTime.now(): 2022-04-22T22:08:25.507579+08:00[Asia/Shanghai]
*/
```

1.  UTC（祖鲁时间），精确到纳秒
2.  日期的基本格式为 yyyy-MM-dd
3.  时间的基本格式为 hh:mm:ss.sss
4.  LocalDateTime 类将两种格式合二为一，中间用大写字母 T 隔开
5.  ZonedDateTime 类用于显示包含时区信息的日期和时间，其后添加了时区偏移量以及一个地区名

[:material-page-previous: 文件处理](fileIO.md){ .md-button}  [:material-page-next: Time时间处理](xxx.md){ .md-button .md-button-right}