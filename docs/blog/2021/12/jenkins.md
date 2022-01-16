---
title: Jenkins部署[Ubuntu]
hide:
  - navigation
---

# Jenkins使用[Ubuntu]

[Back](javascript:history.back(-1)){ .md-button}

## 零、开场白
Jenkins是一款需要部署到服务器上的项目持续集成工具。

所谓持续开发大概就是每完成/改动一个小功能就进行一次项目构建和测试，直到完成所有需求，最后一个功能测试通过也表示整个项目都测试通过。（大概应该是这样）

类似Jenkins、Travis CI、[Github-actions](github_action.md)等持续集成工具就是协助干这种事得，可以让项目集成、部署、测试等化繁为简，形成规范，节约时间。

**Jenkins 主要的工作就是执行pipeline，也就是工作流。可以在Jenkins工作流中定义项目的构建、测试、交付等流程。**（工作流阔以直接理解为执行步骤～）

而工作流pipeline可以通过Jenkins提供的Web ui（Classic ui、Blue ocean）和Jenkinsfile文件进行定义。

## 一、java安装

目前（2021-12-26）Jenkins仅支持Java8/Java11和Docker版Java17 Jenkins镜像（jenkins/jenkins:jdk17-preview）。

``` shell
sudo apt update
sudo apt install openjdk-11-jdk
```

## 二、jenkin安装

建议先参考 [https://www.jenkins.io/doc/book/installing/](https://www.jenkins.io/doc/book/installing/){target=_blank} 尝试使用docker体验一下。这里是直接安装在物理机上。

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

## 三、Jenkins使用设置
#### 1. 授权

[https://www.jenkins.io/doc/book/using/using-credentials/](https://www.jenkins.io/doc/book/using/using-credentials/){target=_blank}

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

### 2. 更换国内插件源

进入Jenkins控制面板 -> Manage Jenkins -> Manage Plugins -> （选中） Advanced -> Update Site填入：https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json

## 四、Jenkinsfile

直接以Jenkinsfile文件形式定义工作流，支持使用声明式语法和脚本式语法两种定义，其中`声明式`提供了更丰富的功能和更简易的读写习惯。

### 1. 基本节点

- pipeline节点：定义了整个构建过程，包括项目构建、测试、交付的过程。是声明式定义的节点。

- node节点：运行构建过程的环境，可以是一个机器（window、linux）或者容器（docker）。是脚本式定义的节点。

- stage节点：定义了pipeline的某个执行阶段。比如可以是：Build、Test、Deploy。是脚本式、声明式定义的节点。

- step节点：定义一个基本的任务，或者说叫命令。比如用sh执行make命令，该step就可以写成：`sh 'make'`。是脚本式、声明式定义的节点。

> 直接来康康具体语法吧，初学还是以模仿为主。

### 2. 声明式语法

**以pipeline节点作为构建定义的根节点。**

格式：
``` groovy linenums="1"
Jenkinsfile (Declarative Pipeline)
pipeline {
    agent any  //(1)
    stages {
        stage('Build') {  //(2)
            steps {
                //(3)
            }
        }
        stage('Test') {  //(4)
            steps {
                //(5)
            }
        }
        stage('Deploy') { //(6)
            steps {
                //(7)
            }
        }
    }
}
```

1.  在指定(any也就是任何环境)的环境下执行该pipeline
2.  定义Build-构建阶段
3.  定义Build构建阶段需要的命令
4.  定义Test-构建阶段
5.  定义Test构建阶段需要的命令
6.  定义Deploy-构建阶段
7.  定义Deploy构建阶段需要的命令

### 3. 脚本式语法

**一个或多个node节点组成核心的工作流。**

格式：
``` groovy linenums="1"
Jenkinsfile (Scripted Pipeline)
node {  //(1)
    stage('Build') { //(2)
        // (3)
    }
    stage('Test') { //(4)
        // (5)
    }
    stage('Deploy') { //(6)
        // (7)
    }
}
```

1.  在指定(any)的环境下执行该pipeline
2.  定义Build-构建阶段
3.  定义Build构建阶段需要的命令
4.  定义Test-构建阶段
5.  定义Test构建阶段需要的命令
6.  定义Deploy-构建阶段
7.  定义Deploy构建阶段需要的命令

### 4.示例demo

- 声明式

``` groovy linenums="1"
Jenkinsfile (Declarative Pipeline)
pipeline { // (1)
    agent any //(2)
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') { //(3)
            steps { //(4)
                sh 'make' //(5)
            }
        }
        stage('Test'){
            steps {
                sh 'make check'
                junit 'reports/**/*.xml' //(6)
            }
        }
        stage('Deploy') {
            steps {
                sh 'make publish'
            }
        }
    }
}
```

1.  包含工作流中所有的内容、命令
2.  为工作流指定一个运行环境、工作区
3.  指定构建的阶段
4.  该阶段执行的命令（多个）
5.  执行的命令（单个）
6.  JUnit 插件提供的命令

### 5.完整pipeline语法

[https://www.jenkins.io/zh/doc/book/pipeline/syntax/](https://www.jenkins.io/zh/doc/book/pipeline/syntax/){target=_blank}

## 五、Classic UI

## 六、Blue Ocean

[https://www.jenkins.io/zh/doc/book/blueocean/](https://www.jenkins.io/zh/doc/book/blueocean/){target=_blank}

在此之前Jenkins默认提供的pipeline图形操作是Classic UI，它可以方便简洁得定义工作流，但在比如工作流可视化操作方面是比较欠缺的，所以就出现了Blue Ocean。可以康康下面的官方介绍。

<video
    id="my-player"
    class="video-js vjs-big-play-centered"
    style="width: 100%;background-color:#303e2e"
    controls
    preload="auto"
    data-setup='{}'>
  <source src="https://video.cdn.shafish.cn/Jenkins%20Blue%20Ocean%20-%20Continuous%20Delivery%20for%20Every%20Team-k_fVlU1FwP4.mp4" type="video/mp4"></source>
</video>

Blue Ocean事实上是一个Jenkins插件集，需要运行在Jenkins 2.7.x或更高版本中。

### 1. 安装Blue Ocean
[https://www.jenkins.io/zh/doc/book/blueocean/getting-started/#%E5%9C%A8%E5%B7%B2%E6%9C%89jenkins%E5%AE%9E%E4%BE%8B%E4%B8%8A%E5%AE%89%E8%A3%85](https://www.jenkins.io/zh/doc/book/blueocean/getting-started/#%E5%9C%A8%E5%B7%B2%E6%9C%89jenkins%E5%AE%9E%E4%BE%8B%E4%B8%8A%E5%AE%89%E8%A3%85){target=_blank}

进入Jenkins控制面板 -> Manage Jenkins -> Manage Plugins -> （选中） available -> （输入）Blue Ocean -> （选中复选框）Blue Ocean -> （点击）Download now and install after restart

### 2. 创建pipeline

[https://www.jenkins.io/zh/doc/book/blueocean/creating-pipelines/#%E5%88%9B%E5%BB%BA%E6%B5%81%E6%B0%B4%E7%BA%BF](https://www.jenkins.io/zh/doc/book/blueocean/creating-pipelines/#%E5%88%9B%E5%BB%BA%E6%B5%81%E6%B0%B4%E7%BA%BF){target=_blank}

[https://www.jenkins.io/zh/doc/book/blueocean/pipeline-editor/#%E6%B5%81%E6%B0%B4%E7%BA%BF%E7%BC%96%E8%BE%91%E5%99%A8](https://www.jenkins.io/zh/doc/book/blueocean/pipeline-editor/#%E6%B5%81%E6%B0%B4%E7%BA%BF%E7%BC%96%E8%BE%91%E5%99%A8){target=_blank}

焯，直接看官方文档吧，写得也太细咧吧。

## 六、问题

- It appears that your reverse proxy setup is broken
    -  [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/){target=_blank}
    - [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/){target=_blank}
    - Jenkins的反向代理设置存在问题。