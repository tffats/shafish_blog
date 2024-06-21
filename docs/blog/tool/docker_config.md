---
title: 容器日常
authors:
    - shafish
date:
    created: 2022-07-10
    updated: 2023-10-25
categories:
    - docker
    - linux
---

[ :fishing_pole_and_fish: ](../../index.md)

!!! note "shafish docker hub"
    [https://hub.docker.com/u/shafish](https://hub.docker.com/u/shafish){target="_blank"}

## 零、安裝
> 或者直接安装官网安装：[https://docs.docker.com/engine/](https://docs.docker.com/engine/){target="_blank"}

``` shell
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

``` shell title="archlinux"
# https://wiki.archlinux.org/title/Docker

sudo pacman -S docker docker-compose
yay -S docker-rootless-extras

sudo echo "your_username:165536:65536" > /etc/subuid
sudo echo "your_username:165536:65536" > /etc/subgid

sudo systemctl enable docker
sudo systemctl start docker

export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/docker.sock
```

## 一、迁移overlay2磁盘

- 1.停止docker服务

``` shell
systemctl stop docker
# systemctl stop docker.socket
```

- 2.迁移旧数据到新目录

``` shell
# 迁移到新的硬盘
mkdir -p /data/docker
rsync -avz /var/lib/docker /data/docker/
```

- 3.配置docker启动文件

``` shell title="/etc/systemd/system/docker.service.d/devicemapper.conf"
[Service]
ExecStart=
ExecStart=/usr/bin/dockerd --graph=/data/docker
```

- 4.重新加载docker服务

``` shell
systemctl daemon-reload
systemctl restart docker
systemctl enable docker
```

- 5.检查是否迁移成功

``` shell
docker info
docker images
```

- 6.回滚

先停止 docker 服务，再删除 docker 启动文件，再重启 docker 服务即可恢复到原来。