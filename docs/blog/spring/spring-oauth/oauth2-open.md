---
title: Spring Authorization Server 使用教程
description: Spring, Oauth
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](../../../index.md)

> `Spring` 官方前一代 oauth 技术实现是 `Spring Security OAuth`，但现在已经不维护了，官方认为 `Spring Security OAuth` 更像是个产品而不是技术框架，当然还有早期的技术债等原因，所以宣布该项目于2022年5月就终止了。但是后来在社区的强烈需求下，官方还是开启了新的 oauth 落地实现，也就是现在这个 `Spring Authorization Server`。

> `Spring authorization server` 是由社区推动的一个项目，在 `Spring security` 团队的领导下基于 `Nimbus` 库重头编写，其目的主要是为 Spring 社区提供 `OAuth 2.1` 和 `OpenID Connect 1.0` 规范以及其他相关规范的实现。


## 一、基础简介
通常在传统的 `客户端-服务端` 中，客户端使用 {++用户凭证++} 请求服务端的用户资源，这些 {++用户的凭证++}(比如用户密码等) 都是明文暴露在各自的服务中的（最多存储的时候加下密），而很多用户习惯性在多数网站中用同一套账号密码。
这种情况下，只要用户有一个站点服务密码被泄露，就会导致该用户几乎所有站点服务数据的泄露。

而 oauth 协议通过在 客户端-服务端 间引入 `授权层` 来解决该问题：客户端不使用 {++用户凭证++} 来访问服务端数据，而是用一个 `访问令牌`，这个令牌是用户在授权服务器中批准确认的。

比如：用户授予相册app访问存储在云上的相片的场景，相册app不用知道用户的账号密码，只需要在授权服务器中获取该用户的授权，获取一个访问令牌，相册app拿着令牌就能访问用户授权范围内的照片，用户也可以随时撤销该相册app对照片的访问授权，让app无权访问照片。如果按照以前的账号密码模式访问，你是不是得改用户账号密码才能让app无权访问了？？

## 二、角色

OAuth 定义了4中角色：

- 资源所有者：用户
- 资源服务器：数据
- 客户端：网页、app等入口
- 授权服务器：认证中心（也就是正要弄的 `Spring authorization server`）

当然上面是些粗糙的说法，具体可以参考这里 [Oauth Roles](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07#section-1.1){target=_blank}

## 三、授权模式

OAuth 定义了客户端要通过哪种授权进行数据访问

### 3.1 授权码模式

```
 +----------+
 | Resource |
 |   Owner  |
 +----------+
       ^
       |
       |
 +-----|----+          Client Identifier      +---------------+
 | .---+---------(1)-- & Redirection URI ---->|               |
 | |   |    |                                 |               |
 | |   '---------(2)-- User authenticates --->|               |
 | | User-  |                                 | Authorization |
 | | Agent  |                                 |     Server    |
 | |        |                                 |               |
 | |    .--------(3)-- Authorization Code ---<|               |
 +-|----|---+                                 +---------------+
   |    |                                         ^      v
   |    |                                         |      |
   ^    v                                         |      |
 +---------+                                      |      |
 |         |>---(4)-- Authorization Code ---------'      |
 |  Client |          & Redirection URI                  |
 |         |                                             |
 |         |<---(5)----- Access Token -------------------'
 +---------+       (w/ Optional Refresh Token)
   v    ^
   |    |
   |    |
   |    |
   |    |
   |    |
   |    |                                       +----------+
   |    .------(7)-- Protected Resource -------<|          |
   |                                            | Resource |
   .--------------(6)-- Access token ---------->|  Server  |
                                                |          |
                                                +----------+
```

- (1) 客户端跳转到认证中心，附带跳转地址 url
- (2) 用户确认授权
- (3) 认证中心跳转到 url?code=xxx
- (4) 客户端携带 code 请求认证中心
- (5) 认证中心返回 token令牌
- (6) 客户端携带 token令牌 访问数据
- (7) 响应数据

### 授权码扩展模式-PKCE

在授权码模式的（3）中，如果使用非https地址，就有可能在传输过程中 code 被窃取，第三方恶意获得访问令牌;而且对于一些单网页应用


### 3.2 刷新令牌

### 3.3 客户端凭证

### 3.4 自定义扩展

## 四、token 生成

## 五、客户端认证方式

## 六、认证接口(端点endPoint)

## 项目环境及依赖

- 1.x 版本要求使用的 JDK17+ 

``` xml title="pom.xml"
<!-- springboot 项目，配好 parent 后直接引入就行 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-authorization-server</artifactId>
</dependency>
```

ref:
- [oauth2.1官方文档](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07)

认证中心配置jdbc客户端信息（RegisteredClient）：https://blog.csdn.net/csdnfanguyinheng/article/details/135394002

http://127.0.0.1:9000/oauth2/authorize?client_id=oidc-client&response_type=code&scope=message.read&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/oidc-client