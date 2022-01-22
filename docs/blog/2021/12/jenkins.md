---
title: Jenkins部署[Ubuntu]
hide:
  - navigation
---

# Jenkins使用[Ubuntu]

[Back](javascript:history.back(-1)){ .md-button}

ref:

- [https://www.jenkins.io/zh/doc/](https://www.jenkins.io/zh/doc/){target=_blank}
- [https://www.jenkins.io/doc/](https://www.jenkins.io/doc/){target=_blank}
- [https://www.w3cschool.cn/jenkins/](https://www.w3cschool.cn/jenkins/){target=_blank}

## 零、开场白
Jenkins是一款自动化的项目持续集成工具。

所谓持续开发大概就是每完成/改动一个功能就触发一次项目构建和测试，能帮助开发人员及早发现项目出现的各种问题。如果没有自动化操作的话，当项目比较大、比较复杂时，构建+测试这一套流程下来，会花费不少成本😏。类似ci工具还有Travis CI、[Github-actions](github_action.md) ...，她们都可以让项目集成、部署、测试等操作更自动化，简单化。

可参考阅读：[https://xie.infoq.cn/article/4c227056798692962f4d43aef](https://xie.infoq.cn/article/4c227056798692962f4d43aef){target=_blank}

Jenkins 主要是执行**设置好的逻辑顺序工作**，把jenkins当作一个可视化的脚本编辑软件就行。官方支持多种类型的脚本编辑：freestyle、pipeline、maven等，用户可以按照自己需求、习惯选择。

Jenkins官方主要以pipeline为规范，**pipeline**也就是工作流（也叫管道流，但感觉还是`工作流`比较直白点），可以通过Web ui（[Classic ui](#五classic-ui)、[Blue ocean](#六blue-ocean)）或[Jenkinsfile](#四jenkinsfile)文件的形式编写项目的构建、测试甚至交付等流程内容。（工作流直接理解为执行步骤就行）

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
#### 1. 配置加密内容

[https://www.jenkins.io/doc/book/using/using-credentials/](https://www.jenkins.io/doc/book/using/using-credentials/){target=_blank}

Jenkins可以与很多第三方网站、应用关联使用，Jenkins提供了对应的内容加密功能。

支持加密类型：

- Secret text - API token (e.g. a GitHub personal access token), 类型GitHub App 认证的appId、Secret
- Username and password, 用户名+密码
- Secret file - which is essentially secret content in a file, emmm暂时没用过
- SSH Username with private key - an SSH public/private key pair, ssh连接
- Certificate - a PKCS#12 certificate file and optional password, or 也暂时没用过
- Docker Host Certificate Authentication credentials. 也暂时没用过

操作：Dashboard->Manage Jenkins->(Security)Manage Credentials->Jenkins->Global credentials (unrestricted)->Add Credentials(左边栏)->选择对应Kind自行创建即可。

图像步骤: [https://www.jenkins.io/doc/book/using/using-credentials/#adding-new-global-credentials](https://www.jenkins.io/doc/book/using/using-credentials/#adding-new-global-credentials){target=_blank}

使用时只需在环境节点中使用`credentials('key')`引用即可：
```
environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
}
```

### 2. 更换国内插件源

进入Jenkins控制面板 -> Manage Jenkins -> Manage Plugins -> （选中） Advanced -> Update Site填入：
https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json

### 3. 创建agents

??? "点击观看创建agents视频教程"

    <video
        id="my-player"
        class="video-js vjs-big-play-centered"
        style="width: 100%;background-color:#303e2e"
        controls
        preload="auto"
        data-setup='{}'>
    <source src="https://video.cdn.shafish.cn/How%20to%20Create%20an%20Agent%20Node%20in%20Jenkins-99DddJiH7lM.zh-Hans.mp4" type="video/mp4"></source>
    </video>

### 4. 本地化

[https://blog.51cto.com/meiling/2509470](https://blog.51cto.com/meiling/2509470){target=_blank}

[https://www.jenkins.io/doc/book/using/using-local-language/#using-local-language](https://www.jenkins.io/doc/book/using/using-local-language/#using-local-language){target=_blank}

## 四、Jenkinsfile

直接以Jenkinsfile文件形式定义工作流，这种形式支持使用声明式语法和脚本式语法两种定义，其中`声明式`提供了更丰富的功能和更简易的读写习惯。

完整pipeline语法：[https://www.jenkins.io/zh/doc/book/pipeline/syntax/](https://www.jenkins.io/zh/doc/book/pipeline/syntax/){target=_blank}

### 1. 基本节点

- pipeline节点：定义了整个构建过程，包括项目构建、测试、交付的过程。是`声明式`定义使用的节点。

    - ??? note "agent节点：定义了工作流执行的环境，可以在pipeline 节点内进行全局定义或在stage 节点内自定义"
        列出可用参数：
        - any：任何可用环境
        - none：不设置全局环境，表示在stage节点内定义执行环境
        - label：在jenkins指定标签对应的环境内执行
        - node：跟label差不多，但node可以再添加额外选项：`agent { node { label 'labelName' } } `与`agent { label 'labelName' }`是等价得
        - docker：拉取一个容器来执行工作流
        - dockerfile：用dockerfile定义一个容器来执行工作流
        ``` groovy
        pipeline {
            agent {docker 'maven:3-alpine'} // 给定名称、标签的新建容器中执行工作流内容
            stages {
                stage('Example Build') {
                    steps {
                        sh 'mvn -B clean verify'
                    }
                }
            }
        }
        ```
        ``` groovy
        pipeline {
            agent none
            stages {
                stage('Example Build') {
                    agent {docker 'maven:3-alpine'}
                    steps {
                        echo 'Hello, Maven'
                        sh 'mvn --version'
                    }
                }
                stage('Example Test') {
                    agent {docker 'openjdk:8-jre'}
                    steps {
                        echo 'Hello, JDK'
                        sh 'java -version'
                    }
                }
            }
        }    
        ```
        ``` groovy
        // 如果dockerfile就在当前项目根目录中，直接设true即可
        agent { dockerfile true }
        ```
        ``` groovy
        ...
        agent {
            // 指定默认的dockerfile名叫Dockerfile.build，在当前目录下的build目录中，还可以使用label指定镜像标签，additionalBuildArgs指定额外的指令等等
            // Equivalent to "docker build -f Dockerfile.build --build-arg version=1.0.2 ./build/
            dockerfile {
                filename 'Dockerfile.build'
                dir 'build'
                label 'my-defined-label'
                additionalBuildArgs  '--build-arg version=1.0.2'
                args '-v /tmp:/tmp'
            }
        }    
        ...
        ```

    - ??? note "stage节点：定义了pipeline的某个执行阶段"
        比如可以是：Build、Test、Deploy。脚本式、声明式定义共有的节点。
        ``` groovy
        pipeline {
            agent any
            stages {  // 包含至少一个执行阶段
                stage('Example') { // example阶段执行 echo命令
                    steps {
                        echo 'Hello World'
                    }
                }
            }
        }
        ```

    - ??? note "steps节点：定义一条或多条执行命令"
        比如用sh执行make命令，该step就可以写成：`sh 'make'`。脚本式、声明式共有的节点。
        - script：可以设置一些比较复杂的脚本
        ``` groovy
        pipeline {
            agent any
            stages {
                stage('Example') {
                    steps {
                        echo 'Hello World'

                        script {
                            def browsers = ['chrome', 'firefox']
                            for (int i = 0; i < browsers.size(); ++i) {
                                echo "Testing the ${browsers[i]} browser"
                            }
                        }
                    }
                }
            }
        }
        ```

    - ??? note "post节点：在工作流末尾根据执行状态进行对应操作"
        - always：执行完工作流后都执行post中定义的操作
        - changed：工作流执行状态发送变化时执行
        - failure：工作流执行状态为failure时执行
        - success：工作流执行状态为success时执行
        - unstable：工作流执行状态为unstable时执行
        - aborted：工作流执行状态为aborted时执行
        - fixed：当前阶段执行状态为success，前一步执行状态为failure或者unstable
        - regression：当前阶段执行状态为failure, unstable, or aborted ，前一步执行状态为success
        - unsuccessful：工作流执行状态不是success时执行
        - cleanup：在所有的post中，最后执行
        ``` groovy
        pipeline {
            agent any
            stages {
                stage('Example') {
                    steps {
                        echo 'Hello world'
                    }
                }
            }
            post {
                always {
                    echo 'I will always say hello again!'
                }
            }
        }    
        ```

    - ??? note "triggers节点：配置工作流触发条件"
        - cron：根据cron设置触发
        - pollSCM：按周期检查仓库是否存在更新，如果发生更新就触发工作流执行
        - upstream：接受逗号分隔的工作字符串、阈值，当字符串中任何作业以最小阈值结束时，流水线被重新触发。
        ``` groovy
        pipeline {
            agent any
            triggers {
                cron('H */4 * * 1-5')
            }
            stages {
                stage('Example') {
                    steps {
                        echo 'Hello World'
                    }
                }
            }
        }
        ```

    - ??? note "options节点：配置工作流执行选项"
        - buildDiscarder：保存控制台输出内容。
        - disableConcurrentBuilds：不允许同时执行该工作流，可防止同时访问共享资源。
        - overrideIndexTriggers：允许覆盖分支索引触发器。（不知道是啥意思）
        - skipDefaultCheckout：跳过从仓库拉取代码。
        - skipStagesAfterUnstable：如果该阶段运行状态为unstable，则跳过该阶段。
        - timeout：设置工作流运行的超时时间。
        - retry：工作流运行失败时，进行重试操作。
        ``` groovy
        pipeline {
            agent any
            options {
                timeout(time: 1, unit: 'HOURS') // 设置超时时间
            }
            stages {
                stage('Example') {
                    steps {
                        echo 'Hello World'
                    }
                }
            }
        }
        ```

    - ??? note "environment节点：Jenkins中设置环境变量"
        ``` groovy
        pipeline {
            agent any
            environment { // 二层节点中设置的环境变量适用于本工作流所有步骤
                CC = 'clang'
            }
            stages {
                stage('Excample') {
                    environment { // 适用于当前阶段
                        AN_ACCESS_KEY = credentials('my-prefined-secret-text')
                    }
                    steps {
                        sh 'printenv'
                    }
                }
            }
        }
        ```

    - ??? note "parameters节点：Jenkins中设置参数列表"
        - string类型：允许用户输入一个字符串。其子参数包括description、defaultValue及name
        - boolean类型：子参数为name、defaultValue及description
        - choice类型：允许用户从一个选项列表中选择。其子参数为name、choices及description
        - file类型：允许用户选择一个文件给流水线使用。其子参数包含fileLocation和description
        - text类型：允许用户输入一个多行文本。其子参数包括name、defaultValue及description
        - password类型：允许用户输入一个密码。对于密码，输入的文本被隐藏了起来。可用的子参数包括name、defaultValue及description
        - run类型：允许用户从某个任务中选择一个特定的运行。此参数可能会被用在一个测试环境之中。可用的子参数包括name、project、description及filter
        ``` groovy
        pipeline {
            agent any
            parameters {
                // 定义了一个叫PERSON的字符参数，对应值为 Mr Jenkins
                string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
            }
            stages {
                stage('Example') {
                    steps {
                        // 按照params.<parameter_name>的格式引用参数
                        echo 'Hello,${params.PERSON}'
                    }
                }
            }   
        }
        ```

    - ??? note "tool节点：配置工作流中可用的插件"
        - maven
        - jdk
        - gradle
        ``` groovy
        pipeline {
            agent any
            tools { // 设置使用maven工具
                maven 'apache-maven-3.0.1'
            }
            stages {
                stage('Example') {
                    steps {
                        sh 'mvn --version'
                    }
                }
            }
        }
        ```

    - ??? note "input节点：添加人工确认"
        - message：显示给用户的内容
        - id：默认为stage名称
        - ok：ok按钮的可选文本
        - submitter：
        - submitterParameter：环境变量的可选名称
        - parameters：可选参数列表
        ``` groovy
        pipeline {
            agent any
            stages {
                stage('Example') {
                    input {
                        message "should we continue?"
                        ok "yes, we should"
                        submitter "alice, bob"
                        parameters {
                            string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'who should i say hello to?')
                        }
                    }
                }
                steps {
                    echo "Hello, ${params.PERSON}, nice to meet you"
                }
            }
        }
        ```

    - ??? note "when节点：根据给定的条件决定是否应该执行本stage阶段"
        when 指令必须包含至少一个条件。 如果 when 指令包含多个条件, 所有的子条件必须返回True，阶段才能执行
        - branch：在多分支pipeline中匹配上特定分支时触发执行
        - environment：指定的环境变量匹配特定值时触发执行
        - expression：指定的groovy表达式为true时触发执行
        - not：指定条件为false时触发执行
        - allof：指定条件全部为true时触发执行
        - anyof：至少有一个条件为true时触发执行
        - buildingTag：在打标签时(building a tag)触发执行（？？？要看示例）
        - changelog：仓库变更日志包含给定正则表达式时触发执行
        - changeset：仓库变更文件中包含指定的文件时触发执行
        - changeRequest：代码仓库进行Pull、Merge等操作时出发执行
        - equals：当expected值与actual值相等时触发执行
        - tag：如果标签名符合特定表达式时触发执行
        - triggeredBy：通过指定的trigger触发（好家伙，某个triger触发某个when执行）
        ``` groovy
        pipeline {
            agent none
            stages {
                stage('Example Build') {
                    steps {
                        echo 'Hello World'
                    }
                }
                stage('Example Deploy') { // 指定只有在生产环境中，且当前设置的环境变量DEPLOY_TO值为production，才会触发部署操作。
                    agent {
                        label "some-label"
                    }
                    when { 
                        beforeAgent true // 执行该阶段时，先判断when中是否返回true，为true时才进入agent环境执行该阶段。
                        allOf {
                            branch 'production'
                            environment name: 'DEPLOY_TO', value: 'production'
                        }
                    }
                    steps {
                        echo 'Deploying'
                    }
                }
            }
        }
        ```

    - ??? note "Parallel节点：允许在阶段内并行执行多个阶段任务"
        ``` groovy
        pipeline {
            agent any
            stages {
                stage('Non-Parallel Stage') {
                    steps {
                        echo 'This stage will be executed first.'
                    }
                }
                stage('Parallel Stage') {
                    when {
                        branch 'master'
                    }
                    failFast true  // 当其中一个并行的进程阶段执行失败时，该parallel阶段都会被终止执行
                    parallel {
                        stage('Branch A') {
                            agent {
                                label "for-branch-a"
                            }
                            steps {
                                echo "On Branch A"
                            }
                        }
                        stage('Branch B') {
                            agent {
                                label "for-branch-b"
                            }
                            steps {
                                echo "On Branch B"
                            }
                        }
                    }
                }
            }
        }
        ```

- node节点：运行构建过程的环境，可以是一个机器（window、linux）或者容器（docker）。是`脚本式`定义使用的节点。    
    脚本式的pipeline实际是用groovy进行构建，node为脚本式pipeline的根节点。
    比如使用groovy语言的条件控制语句、异常处理语句等等
    ``` groovy
    node {
        stage('Example') {
            if (env.BRANCH_NAME == 'master') {
                echo 'I only execute on the master branch'
            } else {
                echo 'I execute elsewhere'
            }
        }
    }
    ```
    ``` groovy
    node {
        stage('Example') {
            try {
                sh 'exit 1'
            }
            catch (exc) {
                echo 'Something failed, I should sound the klaxons!'
                throw
            }
        }
    }
    ```

> 直接来康康具体语法吧，初学还是以模仿为主。

### 2. 声明式语法

**以pipeline节点作为根节点。**

格式：
``` groovy linenums="1"
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

ref：[https://gist.github.com/merikan/228cdb1893fca91f0663bab7b095757c](https://gist.github.com/merikan/228cdb1893fca91f0663bab7b095757c){target=_blank}

- 声明式

``` groovy linenums="1"
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

``` groovy title="Jenkinsfile" linenums="1"
#!/usr/bin/env groovy
pipeline {
    agent { node { label 'swarm-ci' } }  // (1)

    environment {
        TEST_PREFIX = "test-IMAGE"
        TEST_IMAGE = "${env.TEST_PREFIX}:${env.BUILD_NUMBER}"
        TEST_CONTAINER = "${env.TEST_PREFIX}-${env.BUILD_NUMBER}"
        REGISTRY_ADDRESS = "my.registry.address.com"

        SLACK_CHANNEL = "#deployment-notifications"
        SLACK_TEAM_DOMAIN = "MY-SLACK-TEAM"
        SLACK_TOKEN = credentials("slack_token")
        DEPLOY_URL = "https://deployment.example.com/"

        COMPOSE_FILE = "docker-compose.yml"
        REGISTRY_AUTH = credentials("docker-registry")
        STACK_PREFIX = "my-project-stack-name"
    }

    stages {
        stage("Prepare") {
            steps {
                bitbucketStatusNotify buildState: "INPROGRESS"
            }
        }

        stage("Build and start test image") {
            steps {
                sh "docker-composer build"
                sh "docker-compose up -d"
                waitUntilServicesReady
            }
        }

        stage("Run tests") {
            steps {
                sh "docker-compose exec -T php-fpm composer --no-ansi --no-interaction tests-ci"
                sh "docker-compose exec -T php-fpm composer --no-ansi --no-interaction behat-ci"
            }

            post { 
                always {
                    junit "build/junit/*.xml"
                    step([
                            $class              : "CloverPublisher",
                            cloverReportDir     : "build/coverage",
                            cloverReportFileName: "clover.xml"
                    ])
                }
            }
        }

        stage("Determine new version") { 
            when {
                branch "master"
            }

            steps {
                script {
                    env.DEPLOY_VERSION = sh(returnStdout: true, script: "docker run --rm -v '${env.WORKSPACE}':/repo:ro softonic/ci-version:0.1.0 --compatible-with package.json").trim()
                    env.DEPLOY_MAJOR_VERSION = sh(returnStdout: true, script: "echo '${env.DEPLOY_VERSION}' | awk -F'[ .]' '{print \$1}'").trim()
                    env.DEPLOY_COMMIT_HASH = sh(returnStdout: true, script: "git rev-parse HEAD | cut -c1-7").trim()
                    env.DEPLOY_BUILD_DATE = sh(returnStdout: true, script: "date -u +'%Y-%m-%dT%H:%M:%SZ'").trim()

                    env.DEPLOY_STACK_NAME = "${env.STACK_PREFIX}-v${env.DEPLOY_MAJOR_VERSION}"
                    env.IS_NEW_VERSION = sh(returnStdout: true, script: "[ '${env.DEPLOY_VERSION}' ] && echo 'YES'").trim()
                }
            }
        }

        stage("Create new version") {
            when {
                branch "master"
                environment name: "IS_NEW_VERSION", value: "YES"
            }

            steps {
                script {
                    sshagent(['ci-ssh']) {
                        sh """
                            git config user.email "ci-user@email.com"
                            git config user.name "Jenkins"
                            git tag -a "v${env.DEPLOY_VERSION}" \
                                -m "Generated by: ${env.JENKINS_URL}" \
                                -m "Job: ${env.JOB_NAME}" \
                                -m "Build: ${env.BUILD_NUMBER}" \
                                -m "Env Branch: ${env.BRANCH_NAME}"
                            git push origin "v${env.DEPLOY_VERSION}"
                        """
                    }
                }

                sh "docker login -u=$REGISTRY_AUTH_USR -p=$REGISTRY_AUTH_PSW ${env.REGISTRY_ADDRESS}"
                sh "docker-compose -f ${env.COMPOSE_FILE} build"
                sh "docker-compose -f ${env.COMPOSE_FILE} push"
            }
        }

        stage("Deploy to production") {
            agent { node { label "swarm-prod" } }

            when {
                branch "master"
                environment name: "IS_NEW_VERSION", value: "YES"
            }

            steps {
                sh "docker login -u=$REGISTRY_AUTH_USR -p=$REGISTRY_AUTH_PSW ${env.REGISTRY_ADDRESS}"
                sh "docker stack deploy ${env.DEPLOY_STACK_NAME} -c ${env.COMPOSE_FILE} --with-registry-auth"
            }

            post {
                success {
                    slackSend(
                            teamDomain: "${env.SLACK_TEAM_DOMAIN}",
                            token: "${env.SLACK_TOKEN}",
                            channel: "${env.SLACK_CHANNEL}",
                            color: "good",
                            message: "${env.STACK_PREFIX} production deploy: *${env.DEPLOY_VERSION}*. <${env.DEPLOY_URL}|Access service> - <${env.BUILD_URL}|Check build>"
                    )
                }

                failure {
                    slackSend(
                            teamDomain: "${env.SLACK_TEAM_DOMAIN}",
                            token: "${env.SLACK_TOKEN}",
                            channel: "${env.SLACK_CHANNEL}",
                            color: "danger",
                            message: "${env.STACK_PREFIX} production deploy failed: *${env.DEPLOY_VERSION}*. <${env.BUILD_URL}|Check build>"
                    )
                }
            }
        }
    }

    post {
        always {
            sh "docker-compose down || true"
        }

        success {
            bitbucketStatusNotify buildState: "SUCCESSFUL"
        }

        failure {
            bitbucketStatusNotify buildState: "FAILED"
        }
    }
}
```

1.  使用配置好的swarm-ci环境，具体可以查看 [如何创建agents](#3-创建agents)


``` groovy title="Jenkinsfile" linenums="1"
#!/usr/bin/env groovy
pipeline {

    /*
     * Run everything on an existing agent configured with a label 'docker'.
     * This agent will need docker, git and a jdk installed at a minimum.
     */
    agent {
        node {
            label 'docker'
        }
    }

    // using the Timestamper plugin we can add timestamps to the console log
    options {
        timestamps()
    }

    environment {
        //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
    }

    stages {
        stage('Build') {
            agent {
                docker {
                    /*
                     * Reuse the workspace on the agent defined at top-level of Pipeline but run inside a container.
                     * In this case we are running a container with maven so we don't have to install specific versions
                     * of maven directly on the agent
                     */
                    reuseNode true
                    image 'maven:3.5.0-jdk-8'
                }
            }
            steps {
                // using the Pipeline Maven plugin we can set maven configuration settings, publish test results, and annotate the Jenkins console
                withMaven(options: [findbugsPublisher(), junitPublisher(ignoreAttachments: false)]) {
                    sh 'mvn clean findbugs:findbugs package'
                }
            }
            post {
                success {
                    // we only worry about archiving the jar file if the build steps are successful
                    archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true)
                }
            }
        }

        stage('Quality Analysis') {
            parallel {
                // run Sonar Scan and Integration tests in parallel. This syntax requires Declarative Pipeline 1.2 or higher
                stage('Integration Test') {
                    agent any  //run this stage on any available agent
                    steps {
                        echo 'Run integration tests here...'
                    }
                }
                stage('Sonar Scan') {
                    agent {
                        docker {
                            // we can use the same image and workspace as we did previously
                            reuseNode true
                            image 'maven:3.5.0-jdk-8'
                        }
                    }
                    environment {
                        //use 'sonar' credentials scoped only to this stage
                        SONAR = credentials('sonar')
                    }
                    steps {
                        sh 'mvn sonar:sonar -Dsonar.login=$SONAR_PSW'
                    }
                }
            }
        }

        stage('Build and Publish Image') {
            when {
                branch 'master'  //only run these steps on the master branch
            }
            steps {
                /*
                 * Multiline strings can be used for larger scripts. It is also possible to put scripts in your shared library
                 * and load them with 'libaryResource'
                 */
                sh """
          docker build -t ${IMAGE} .
          docker tag ${IMAGE} ${IMAGE}:${VERSION}
          docker push ${IMAGE}:${VERSION}
        """
            }
        }
    }

    post {
        failure {
            // notify users when the Pipeline fails
            mail to: 'team@example.com',
                    subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                    body: "Something is wrong with ${env.BUILD_URL}"
        }
    }
}
```

## 五、Classic UI

[https://www.jenkins.io/doc/pipeline/tour/hello-world/](https://www.jenkins.io/doc/pipeline/tour/hello-world/)

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

焯，直接看官方文档吧，写得也太细咧。

## 六、实操

[https://www.jenkins.io/zh/doc/tutorials/](https://www.jenkins.io/zh/doc/tutorials/){target=_blank}

## 六、问题

- It appears that your reverse proxy setup is broken--Jenkins的反向代理设置问题
    -  [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/){target=_blank}
    - [https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-nginx/){target=_blank}