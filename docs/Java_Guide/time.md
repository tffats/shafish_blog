---
title: Java案例解析-时间处理
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](index.md#八Time){ .md-button}

!!! note "Java8的time包是基于Joda-Time进行构建的，强推"

## 一、创建实例

``` java linenums="1" title="1-1 now 方法根据当前日期或时间创建实例"
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

``` java linenums="1" title="1-2 of 用于生成新的值"
System.out.println("First landing on the Moon:");
LocalDate moonLandingDate = LocalDate.of(1969, Month.JULY, 20);  // (1)
LocalTime moonLandingTime = LocalTime.of(20, 18);  // (2)
System.out.println("Date: " + moonLandingDate);
System.out.println("Time: " + moonLandingTime);

System.out.println("Neil Armstrong steps onto the surface: ");
LocalTime walkTime = LocalTime.of(20, 2, 56, 150_000_000);
LocalDateTime walk = LocalDateTime.of(moonLandingDate, walkTime); // (3)
System.out.println(walk);

/*
*First landing on the Moon:
*Date: 1969-07-20
*Time: 20:18
*Neil Armstrong steps onto the surface: 
*1969-07-20T20:02:56.150
*/
```

1.  LocalDate.of方法接收年、月（枚举或整型）、日
2.  LocalTime.of方法根据可用的小时、分、秒以及纳秒值获取当前日期
3.  LocalDateTime.of方法根据可用的年、月、日、小时、分、秒以及纳秒值获取当前日期和时间

``` java linenums="1" title="1-3 使用plus with和 minus创建新实例"
System.out.println("润年二月: " + Month.FEBRUARY.length(true)); 
System.out.println("润年八月第一天: " + Month.AUGUST.firstDayOfYear(true));
System.out.println("Month.of(1): " + Month.of(1));
System.out.println("加2个月: " + Month.JANUARY.plus(2));
System.out.println("减1个月: " + Month.MARCH.minus(1));

Period period = Period.of(1, 3, 4);  // (1)
LocalDateTime start = LocalDateTime.of(2020, Month.FEBRUARY, 2, 11, 30);
LocalDateTime end = start.plus(period);
System.out.println(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString());

LocalDateTime start = LocalDateTime.of(2020, Month.FEBRUARY, 2, 11, 30);
LocalDateTime end = start.withMinute(45);  // (2)

/*
*润年二月: 29
*润年八月第一天: 214
*Month.of(1): JANUARY
*加2个月: MARCH
*减1个月: FEBRUARY
*2021-05-06T11:30:00
*/
```

1.  使用Period指定一个时间间隔：一年3个月零4天
2.  with就用来修改对应分钟（withYear、withMonth、withDayOfYear、withDayOfMonth、withHour、withSecond、withNano）

!!! danger "java.time 包中的类是不可变的，如果实例方法（如 plus、minus 或 with）试图修改某个类，将生成一个新的实例"

## 二、添加时区信息

``` java linenums="1" title="2-1"
LocalDateTime dateTime = LocalDateTime.of(2017, Month.JULY, 4, 13, 20, 10);
ZonedDateTime nyc = dateTime.atZone(ZoneId.of("Asia/Shanghai"));
System.out.println(nyc);

ZonedDateTime london = nyc.withZoneSameInstant(ZoneId.of("Europe/London"));  // (1)
System.out.println(london);

Set<String> regionNames = ZoneId.getAvailableZoneIds(); // (2)

// 2017-07-04T13:20:10+08:00[Asia/Shanghai]
// 2017-07-04T06:20:10+01:00[Europe/London]
```

1.  withZoneSameInstant可以进行时区切换
2.  查看所有时区

[:material-page-previous: 文件处理](fileIO.md){ .md-button}  [:material-page-next: Time时间处理](./time.md){ .md-button .md-button-right}