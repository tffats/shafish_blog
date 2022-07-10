---
title: 容器日常
description: linux, docker
hide:
  - navigation
---

## 一、drawio
``` shell
# 离线：http://xxxx:10180/?offline=1
docker run -it -m1g -e LETS_ENCRYPT_ENABLED=true -e PUBLIC_DNS=drawio.example.com --rm --name="draw" -p 80:80 -p 443:8443 jgraph/drawio
```

## 二、ftp
``` shell
docker run -d \
    --name="ftp" \
    -p 10021:21 \
    -p 21000-21010:21000-21010 \
    -e USERS="shafish|123456" \
    --restart=unless-stopped \
    delfer/alpine-ftp-server
```