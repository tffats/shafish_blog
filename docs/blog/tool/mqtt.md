---
title: MQTT使用
description: MQTT使用记录
hide:
  - navigation
---

## Broker、调试工具安装

> `broker` 这里选择 `emqx`。具体选型参考：[Broker 选型指南](https://www.emqx.com/zh/resources/a-practical-guide-to-mqtt-broker-selection){target=_blank}

``` shell title="docker-compose.yml"
services:
  emqx1:
    image: emqx/emqx:5.8.6
    container_name: my-mqtt
    healthcheck:
      test: ["CMD", "/opt/emqx/bin/emqx", "ctl", "status"]
      interval: 5s
      timeout: 25s
      retries: 5
    ports:
      - 1883:1883
      - 8083:8083
      - 8084:8084
      - 8883:8883
      - 18083:18083
    volumes:
      - emqx1-data:/opt/emqx/data
      - emqx1-log:/opt/emqx/log

volumes:
    emqx1-data:
    emqx1-log:
```

> `mqtt` 调试工具选择 `mqttx`

``` shell

```