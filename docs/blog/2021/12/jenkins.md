---
title: Jenkins部署[Ubuntu]
hide:
  - navigation
---

# Jenkins使用[Ubuntu]

[Back](javascript:history.back(-1)){ .md-button}

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

[https://www.jenkins.io/doc/book/installing/](https://www.jenkins.io/doc/book/installing/){target=_blank}

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


## 四、Jenkinsfile语法

pipeline持续集成流水线，定义了整个构建过程，包括项目构建、测试、交付等过程。直接理解为执行步骤就行。
可以用jenkins提供的Web ui（classic ui、blue ocean）定义执行步骤，或者直接写在Jenkinsfile文件中。

Jenkinsfile支持声明式和脚本式语法定义，`声明式`提供了更丰富的功能和更简易的读写习惯。

### 1. 基本节点

- pipeline节点：定义了整个构建过程，包括项目构建、测试、交付的过程。是声明式定义的节点。

- node节点：运行构建过程的环境，可以是一个机器（window、linux）或者容器（docker）。是脚本式定义的节点。

- stage节点：定义了pipeline的某个执行阶段。比如可以是：Build、Test、Deploy。是脚本式、声明式定义的节点。

- step节点：定义一个基本的任务，或者说叫命令。比如用sh执行make命令，该step就可以写成：`sh 'make'`。是脚本式、声明式定义的节点。

### 2. 声明式语法

pipeline节点作为构建定义的根节点。

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

1.  在指定(any)的环境下执行该pipeline
2.  定义Build-构建阶段
3.  定义Build构建阶段需要的命令
4.  定义Test-构建阶段
5.  定义Test构建阶段需要的命令
6.  定义Deploy-构建阶段
7.  定义Deploy构建阶段需要的命令

### 3. 脚本式语法

一个或多个node节点组成核心的工作流。

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
pipeline { //(1)
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
6.   JUnit 插件提供的命令

### 5.完整pipeline语法

[https://www.jenkins.io/doc/book/pipeline/syntax/](https://www.jenkins.io/doc/book/pipeline/syntax/){target=_blank}

## 五、Classic UI

## 六、Blue Ocean

## 七、问题

- It appears that your reverse proxy setup is broken
    -  [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/){target=_blank}
    - [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/){target=_blank}
    - Jenkins的反向代理设置存在问题。

尊敬的Jack、Job：

我很遗憾自己在这个时候向公司正式提出辞职。

我来公司快两年了，也很荣幸自己能够成为柠檬酱一员。在这段时间里，我学到了很多东西，也非常感激公司给予了我在这样的良好环境中，工作和学习的机会。

但是我因为个人原因需要辞职。在对我的工作方向和自己工作能力的充分衡量后，我觉得与自己的预期不相符合，出于对公司之后业务的顺利进行及个人发展的考虑，我决定了辞职。

我希望在年底前完成相关工作交接，请领导安排交接时间、交接人选。在未离开岗位之前，我一定会站好最后一班岗，尽职做好应该做的事。

望领导批准我的申请，并请协助办理相关离职手续。

祝您身体健康，事业顺心。并祝柠檬酱以后事业蓬勃发展。

申请人： 黄超俞/Graham

日期： 2022年1月9日