---
title: 聊天机器人
authors:
    - shafish
date:
    created: 2024-03-11
draft: true    
categories:
    - qq机器人
    - python
tags:
  - 工具
bcs:
  "分类": "/tags/#tag:工具"
---

## 一、先导

QQ官方并没有对个人开发开放基本功能（好像个人申请只能用于QQ频道），加上官方针对开源协议库的围追堵截，非官方的QQ机器人不能100%可靠运行。

但随着官方推出NTQQ统一了各系统，限制局面被打破。

> QQ官方最新推出的 NTQQ 客户端使用了 electron 技术，其分为前后端两个部分, 前端是使用 Web 技术开发的 UI 界面供用户交互，后端使用 nodejs addons 技术包装了一个库来处理客户端逻辑和与服务端通信。

> 我们将NTQQ 客户端前端删除只与后端库交互, 并引出 API 来为我们的Bot服务，即可开启QQ机器人玩法。

<!-- more -->

## 二、搭建Lagrange.OneBot

> Lagrange.Core 是一个开源的 NTQQ 协议实现，用于充当客户端后端角色。

> 运行 Lagrange 后，修改 appsettings.json 暴露接口即可，具体业务操作交给 Nonebot2 实现

``` shell
docker run -td --network host -v /home/shafish/Data/docker/lagrange:/app/data -e UID=$UID -e GID=$(id -g) ghcr.io/lagrangedev/lagrange.onebot:edge
```

``` json title="appsettings.json"
{
    "$schema": "https://raw.githubusercontent.com/LagrangeDev/Lagrange.Core/master/Lagrange.OneBot/Resources/appsettings_schema.json",
    "Logging": {
        "LogLevel": {
            "Default": "Information"
        }
    },
    "SignServerUrl": "https://sign.lagrangecore.org/api/sign/30366",
    "SignProxyUrl": "",
    "MusicSignServerUrl": "",
    "Account": {
        "Uin": 0,
        "Password": "",
        "Protocol": "Linux",
        "AutoReconnect": true,
        "GetOptimumServer": true
    },
    "Message": {
        "IgnoreSelf": true,
        "StringPost": false
    },
    "QrCode": {
        "ConsoleCompatibilityMode": true
    },
    "Implementations": [
        {
            "Type": "ReverseWebSocket",
            "Host": "127.0.0.1",
            "Port": 8080,
            "Suffix": "/onebot/v11/ws",
            "ReconnectInterval": 5000,
            "HeartBeatInterval": 5000,
            "AccessToken": "shafish"
        },
	    {
            "Type": "ForwardWebSocket",
            "Host": "127.0.0.1",
            "Port": 8081,
            "HeartBeatInterval": 5000,
            "HeartBeatEnable": true,
            "AccessToken": "shafish"
        }
    ]
}
```

- ReverseWebSocket 端口要与 Nonebot2 运行端口一致，默认8080; 
- Lagrange 容器运行后，注册一个新QQ号充当机器人，使用 `docker logs 容器名` 展示二维码，并扫描登录即可。（QQ登录时记得勾选）

## 三、创建Nonebot2项目

环境按照官方文档安装，要求Python 3.9 及以上版本（配置终端代理加快依赖包下载）

- 创建项目：`nb create`
- 运行：
  ``` shell
  cd 项目名称
  nb run --reload
  ```



ref:

- [帮大忙](https://blog.csdn.net/m0_66648798/article/details/141038846){target=_blank}
- [Lagrange.Core在线配置](https://lagrangedev.github.io/lagrange-config-generator/){target=_blank}
- [Lagrange.OneBot Docker文档](https://github.com/LagrangeDev/Lagrange.Core/blob/master/Docker_zh.md){target=_blank}
- [nonebot文档](https://nonebot.dev/docs/){target=_blank}

