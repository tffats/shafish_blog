---
title: Jenkins部署[Ubuntu]
hide:
  - navigation
---

# Jenkins使用[Ubuntu]

[Back](/blog/#12月份){ .md-button}

## 零、开场白
Jenkins是一款需要部署到服务器上的项目持续集成工具，可以与Github等仓库关联使用。

所谓持续开发大概就是每完成一个小功能就进行一次构建和测试，测试通过就合并到主干中，测出问题就马上解决。而不是先把大功能全部开发完成再合并到主干，这样没出现问题还好，出了问题一般也是比较大点的问题，排查起来也可能相对比较繁琐。

而Jenkins、Travis CI、[Github-actions](github_action.md)等持续集成工具就是干这种事得，可以让项目集成、部署、测试等化繁为简，很高效的哟。

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

- jenkins运行logs（查看管理员密码）：`/var/log/jenkins/jenkins.log` 
- jenkins配置文件： `/etc/default/jenkins`
- jenkins服务管理
    - 加入开机启动：`sudo systemctl daemon-reload`
    - 启动：`sudo systemctl start jenkins`
    - 暂停：`sudo systemctl stop jenkins`
    - 查看状态：`sudo systemctl status jenkins`

## 三、Jenkins介绍
### 1. 授权
Jenkins可以与很多第三方网站、应用进行关联使用，对应就需要用到认证、授权等操作。

支持认证类型：

- Secret text - API token (e.g. a GitHub personal access token), 类型GitHub App 认证的appId、Secret
- Username and password, 用户名+密码
- Secret file - which is essentially secret content in a file, emmm暂时没用过
- SSH Username with private key - an SSH public/private key pair, ssh连接
- Certificate - a PKCS#12 certificate file and optional password, or 也暂时没用过
- Docker Host Certificate Authentication credentials. 也暂时没用过

操作：Dashboard->Manage Jenkins->(Security)Manage Credentials->Jenkins->Global credentials (unrestricted)->Add Credentials(左边栏)->选择对应Kind自行创建即可。

图像步骤: [https://www.jenkins.io/doc/book/using/using-credentials/#adding-new-global-credentials](https://www.jenkins.io/doc/book/using/using-credentials/#adding-new-global-credentials){target=_blank}


## 四、Pipeline

## 五、Blue Ocean

## 、问题

- It appears that your reverse proxy setup is broken
    -  [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/){target=_blank}
    - [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/){target=_blank}
    - Jenkins的反向代理设置存在问题。