---
title: mybatis-plus记录
tags:
  - mybatis-plus
hide:
  - navigation
---

## maven依赖

``` xml
<!-- mybatis-plus库 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.6</version>
</dependency>

<!-- jdbc的mysql实现库 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<!-- lombok减少代码量 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```

## 项目结构
``` 
└── io-db
    ├── pom.xml
    └── src
        └── main
            ├── java
            │   └── cn
            │       └── shafish
            │           └── practice
            │               ├── IoDbApplication.java
            │               ├── mapper
            │               │   └── SfUserMapper.java
            │               └── model
            │                   └── entity
            │                       ├── BaseEntity.java
            │                       └── SfUser.java
            └── resources
                ├── application.yaml
                └── mapper

```

## application.yaml配置
``` yml
spring:
  application:
    name: io-db
  datasource:
    username: shafish
    password: kaifa123456
    url: jdbc:mysql://192.168.0.109:3306/sf?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## entity
## mapper
## 