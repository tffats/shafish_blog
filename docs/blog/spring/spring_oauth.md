---
title: Spring Authorization Server 使用教程
description: Spring, Oauth
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](/)

> `Spring Authorization Server` 是 `Spring` 官方新推出的一个框架，它提供了 `OAuth 2.1` 和 `OpenID Connect 1.0` 规范以及其他相关规范的实现。

> `Spring` 官方前一代 oauth 落地是 `Spring Security OAuth`，但现在已经不维护了，除了技术债之外，官方认为 `Spring Security OAuth` 更像是个产品而不是技术框架，所以宣布该项目于2022年5月就终止了。但是后来在社区的强烈需求下，官方还是开启了新的 oauth 落地实现，也就是现在这个 `Spring Authorization Server`。

## 项目环境及依赖

- 1.x 版本要求使用的 JDK17+ 

``` xml title="pom.xml"
<!-- springboot 项目，配好 parent 后直接引入就行 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-authorization-server</artifactId>
</dependency>
```

## 

认证中心配置jdbc客户端信息（RegisteredClient）：https://blog.csdn.net/csdnfanguyinheng/article/details/135394002

http://127.0.0.1:9000/oauth2/authorize?client_id=oidc-client&response_type=code&scope=message.read&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/oidc-client