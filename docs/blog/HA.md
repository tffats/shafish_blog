---
title: 系统高可用
description: 容器, 高可用
hide:
  - navigation
---

## 一、

## 二、网络服务

### 1.HTTPS 请求优化分析

一个完整、未复用连接的 HTTPS 请求需要经过以下 5 个阶段：DNS 域名解析、TCP 握手、SSL 握手、服务器处理、内容传输。

除服务器处理比较可控外，其他都理想为固定，请求一次往返时间为RTT，则服务延迟时间为：4 * RTT + 后端业务处理时间

``` shell title="curl-format.txt" 
# 各阶段延迟时间
    time_namelookup:  %{time_namelookup}\n
       time_connect:  %{time_connect}\n
    time_appconnect:  %{time_appconnect}\n
      time_redirect:  %{time_redirect}\n
   time_pretransfer:  %{time_pretransfer}\n
 time_starttransfer:  %{time_starttransfer}\n
                    ----------\n
         time_total:  %{time_total}\n
```

``` shell
curl -w "@curl-format.txt" -o /dev/null -s 'https://www.baidu.com/'

    time_namelookup:  0.007414
       time_connect:  0.015604
    time_appconnect:  0.034267
      time_redirect:  0.000000
   time_pretransfer:  0.034285
 time_starttransfer:  0.042809
                    ----------
         time_total:  0.042835
```

| :变量名称:           | :耗时:                                                                      |
| ------------------ | ------------------------------------------------------------------------- |
| time_namelookup    | 从请求开始到域名解析完成的耗时                                            |
| time_connect       | 从请求开始到 TCP 三次握手完成的耗时                                       |
| time_appconnect    | 从请求开始到 TLS 握手完成的耗时                                           |
| time_pretransfer   | 从请求开始到向服务器发送第一个 GET/POST 请求开始之前的耗时                |
| time_redirect      | 重定向耗时，包括到内容传输前的重定向的 DNS 解析、TCP 连接、内容传输等时间 |
| time_starttransfer | 从请求开始到内容传输前的耗时                                              |
| time_total         | 从请求开始到完成的总耗时                                                  |

| :耗时:                                                     | :说明:                                       |
| ---------------------------------------------------------- | -------------------------------------------- |
| 域名解析耗时 = time_namelookup                             | 域名 NS 及本地 LocalDNS 解析耗时             |
| TCP 握手耗时 = time_connect - time_namelookup              | 建立 TCP 连接时间                            |
| SSL 耗时 = time_appconnect - time_connect                  | TLS 握手以及加解密处理                       |
| 服务器处理请求耗时 = time_starttransfer - time_pretransfer | 服务器响应第一个字节到全部传输完成耗时       |
| TTFB = time_starttransfer - time_appconnect                | 服务器从接收请求到开始到收到第一个字节的耗时 |
| 总耗时 = time_total                                        | 整个 HTTPS 的请求耗时                        |


ref:

- https://www.thebyte.com.cn