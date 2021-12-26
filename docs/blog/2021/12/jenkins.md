---
title: Jenkins部署[Ubuntu]
---

# Jenkins使用[Ubuntu]

## 一、java安装

目前（2021-12-26）Jenkins仅支持Java8/Java11和Docker版Java17 Jenkins镜像（jenkins/jenkins:jdk17-preview）。

``` shell
sudo apt update
sudo apt install openjdk-11-jdk
```

## 二、jenkin安装
``` shell
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install jenkins
```

!!! note "安装说明"

    - Setup Jenkins as a daemon launched on start. See /etc/init.d/jenkins for more details.
    - Create a ‘jenkins’ user to run this service.

### jenkins运行logs
`/var/log/jenkins/jenkins.log`

### jenkins配置文件
 `/etc/default/jenkins`

 ### 服务管理
 - 加入开机启动：`sudo systemctl daemon-reload`
 - 启动：`sudo systemctl start jenkins`
 - 暂停：`sudo systemctl stop jenkins`
 - 查看状态：`sudo systemctl status jenkins`

 ## 三、

 ## 问题

### It appears that your reverse proxy setup is broken

ref: https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/

Jenkins的反向代理设置存在问题。