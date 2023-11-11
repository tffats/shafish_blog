---
title: trojan
description: linux, docker, trojan, client, socket5, http
hide:
  - navigation
---

## 一、简介
技术开发少不了使用代理，因为国内的站点存在大量且重复的各种资料备份，但只有一手的wiki才能精准解决问题，且让你印象深刻。墙这玩意以前觉得是封锁，但久了才会发现这确实是个保护罩。具体其他自己慢慢摸索吧

这里介绍下trojan的客户端部署，之前都是手动安装trojan，改下配置文件就直接用（刚好有三个小鸡就对应安装在台式机、笔记本和pve上）。但由于jia'jing'pin'qiong，对家里台式机的上钟时间稍微做了限制，导致台式机配置的代理对内网用不了，又不可能单纯为此开个虚拟机。

所以就有了这篇内容，可以用docker部署trojan客户端，只要有代理，想开多少开多少。

## 二、使用
``` shell
docker pull shafish/trojan:1.0
docker run --name trojan -p 1088:1080 -p 1089:8118 -e REMOTE_ADDR="your ssl domain" -e REMOTE_PORT=111 -e PASSWORD="passwd" -d shafish/trojan:1.0
```

``` shell title="docker-compose.yml"
version: '3.3'
services:
    trojan:
        container_name: trojan
        restart: unless-stopped
        environment:
            - REMOTE_ADDR="your ssl domain"
            - REMOTE_PORT=111
            - PASSWORD="passwd"
        ports:
            - '1088:1080'
            - '1089:8118'
        image: shafish/trojan:1.0
```

### 2.1 说明
- 首先你得有trojan server的信息，这个自己折腾，不用代理也能查到一堆资料搞
- 容器的1080端口对应的socket5协议，8118端口则对应http协议，直接指定本机特定端口映射出来用就行
- 启动的使用需要明确指定三个变量：
    - REMOTE_ADDR：代理服务器的trojan伪装域名，例如："trojanone.com"
    - REMOTE_PORT：代理服务器的trojan对外端口，例如：111
    - PASSWORD：代理服务器的trojan密码，例如："passwd"