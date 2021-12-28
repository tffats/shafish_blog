---
title: Github Action使用记录
hide:
  - navigation
---

# Github Action

ref:

- [GitHub Actions 入门教程-阮一峰](http://www.ruanyifeng.com/blog/2019/09/getting-started-with-github-actions.html)
- [GitHub Actions官方文档](https://docs.github.com/en/actions/quickstart)
- [Github Actions语法](https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#about-yaml-syntax-for-workflows)

用法和概念基本都是参考 阮一峰 的那篇文章，所以在这里纯粹是记录下使用。

## 一、概念介绍

### workflow

工作流。持续集成一次运行的过程，就是一个 workflow。

### job

任务。一个 workflow 由一个或多个 jobs 构成，含义是一次持续集成的运行，可以完成多个任务。

### step

步骤。每个 job 由多个 step 构成，一步步完成。

### action

动作。每个 step 可以依次执行一个或多个命令（action）。
部署项目过程中的服务器登录、运行环境的部署、代码拉取、运行部署脚本等操作，在github action中都被看作为是一个个的action。

其中的大部分action都是可以被复用的：比如环境部署，基本就只有软件对应系统和版本的区别。
所以Github Action中使用到的action是可以直接引用他人写好的 action滴，可以在下面列出的仓库中找找：

- [Github官方市场](https://github.com/marketplace?type=actions)
- [Awesome actions](https://github.com/sdras/awesome-actions)

## 二、Workflow定义

在代码仓库的.github/workflows目录下定义工作流文件，文件后缀名统一为`yml`，Github会根据触发条件运行这些工作流文件。

### 基本配置字段

- `name`：指定workflow 的名称。
- `on.<push|pull_request>.<tags|branches>`：触发所在工作流执行的条件，比如 某个分支发生push/pull时。

``` yaml
# masterf分支发生push时触发工作流执行
on:
  push:
    branches:    
      - master
```

- `jobs.<job_id>.name`：需要执行的一项或多项任务。

``` yaml
# job_id 对应特定的任务，my_first_job、my_second_job
# name 是任务的说明
jobs:
  my_first_job:
    name: My first job
  my_second_job:
    name: My second job
```

- `jobs.<job_id>.needs`：指定当前任务的运行顺序。

``` yaml
# job1必须先于job2完成，而job3等待job1和job2的完成才能运行。因此，这个 workflow 的运行顺序依次为：job1、job2、job3
jobs:
  job1:
  job2:
    needs: job1
  job3:
    needs: [job1, job2]
```

- `jobs.<job_id>.runs-on`：指定运行所需要的虚拟机环境。

``` yaml
# 可选的环境如下：
ubuntu-latest，ubuntu-18.04或ubuntu-16.04
windows-latest，windows-2019或windows-2016
macOS-latest或macOS-10.14
```

- `jobs.<job_id>.steps`：指定每个 Job 的运行步骤，可以包含一个或多个步骤，步骤包含name、env、run三个字段。

- 完整的工作流定义：

``` yaml
name: Greeting from Mona
on: push

jobs:
  my-job:
    name: My Job
    runs-on: ubuntu-latest
    steps: # 定义了个 echo变量值 的job
    - name: Print a greeting
      env:
        MY_VAR: Hi there! My name is
        FIRST_NAME: Mona
        MIDDLE_NAME: The
        LAST_NAME: Octocat
      run: |
        echo $MY_VAR $FIRST_NAME $MIDDLE_NAME $LAST_NAME.
```

## 三、示例

### 1. ci.yml熟悉

``` yaml
# .github/workflows/ci.yml
name: GitHub Actions Build and Deploy Demo
on:
  push:
    branches:
      - master
jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout
      uses: actions/checkout@master # 获取源码

    - name: Build and Deploy
      uses: JamesIves/github-pages-deploy-action@master # 构建和部署
      env:
        ACCESS_TOKEN: ${{ secrets.ACCESS_TOKEN }}
        BRANCH: gh-pages
        FOLDER: build
        BUILD_SCRIPT: npm install && npm run build
```

``` yaml
name: Build app and deploy to aliyun
on:
  #监听push操作
  push:
    branches:
      # master分支，你也可以改成其他分支
      - master
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v1
    - name: Install Node.js
      uses: actions/setup-node@v1
      with:
        node-version: '12.16.2'
    - name: Install npm dependencies
      run: npm install
    - name: Run build task
      run: npm run build
    - name: Deploy to Server
      uses: easingthemes/ssh-deploy@v2.1.5
      env:
        SSH_PRIVATE_KEY: ${{ secrets.SERVER_SSH_KEY }}
        ARGS: '-rltgoDzvO --delete'
        SOURCE: dist # 这是要复制到阿里云静态服务器的文件夹名称
        REMOTE_HOST: '118.190.217.8' # 你的阿里云公网地址
        REMOTE_USER: root # 阿里云登录后默认为 root 用户，并且所在文件夹为 root
        TARGET: /root/node-server # 打包后的 dist 文件夹将放在 /root/node-server
```

### 2. 把mkdocs部署到github page

``` shell
# 初始化一个mkdocs-metereal项目
mkdir -p /home/shafish/Project/blog/shafish.cn && cd $_
mkdocs init .
# 添加mkdocs.yml的theme为material
vim mkdocs.yml
# 创建github workflow
mkdir -p .github/workflows
touch .github/workflows/ci.yml
# 提交到github仓库
# 现在github网站上创建个仓库
git remote add origin git@github.com:用户名/仓库名.git
git branch -M main
git push -u origin main
# ok，访问https://用户名.github.io/ 即可
# 如果出现问题，在Github网站仓库中的Actions选项查看执行logs。
```

``` yaml title="mkdocs.yml"
theme:
  name: material
```

``` yaml title="ci.yml"
name: blog_ci 
on:
  push:
    branches: 
      - master
      - main
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout master
        uses: actions/checkout@v2

      - name: Set up Python3.x
        uses: actions/setup-python@v2
        with:
          python-version: 3.x

      - name: Install dependencies
        run: pip install mkdocs-material
      
      - name: Deploy
        run: mkdocs gh-deploy --force
```

## 四、把mkdocs部署到云服务器

### 1. 新建部署用户
提供给Github action执行ssh、rsync等命令。

``` shell
# -s为用户登录设置shell
# -d -m 为用户创建主用户目录
# -G 把rsync添加到www组中，方便后续操作www用户资源
# -c 用户说明
sudo useradd -s /bin/bash –d  /home/rsync -m -G www -c for_github_action_remote_deployment rsync
# 设置rsync用户密码
sudo passwd rsync
# 修改站点目录组权限
sudo chmod -R 775 /www/wwwroot/shafish.cn
```

### 2. 配置用户ssh信息

``` shell
# 登录rsync用户
su - rsync
# 创建.ssh目录，保存公私钥等信息
mkdir -p ~/.ssh && cd ~/.ssh
# 生成密钥信息，shafishcn为文件名，自行填写
# 一路回车，最终生成shafishcn（私钥）和shafishcn.pub（公钥）
ssh-keygen -t rsa -f shafishcn 
# 配置连接信息
cat shafishcn.pub >> authorized_keys
# 复制私钥，github action ssh登录到服务器时用到
cat shafishcn
```

### 3. 配置Github action环境变量

打开仓库 --> Settings -->  Secrets（侧边栏中）-->Actions（为Secrets子项）-->New repository secret

- Name：环境变量名称
- Value：变量值

![](https://picture.cdn.shafish.cn/blog/github-action-2021-12-secret1.png){: .zoom}

| 变量      | 变量值                          |
| :-----------: | :------------------------------------: |
| `SERVER_HOST`       |      服务器ip  |
| `SERVER_KEY`       |     第2步中的shafishcn私钥内容 |
| `SERVER_PORT`    |     ssh端口，默认22 |
| `SERVER_USERNAME`    |   rsync   |

### 4. 部署文件

``` yaml
name: Built and pushed to the server
on:
  push:
    branches:
      - master # 提交到master分支时触发执行
      - main # 提交到main分支时触发执行

env:
  TZ: Asia/Shanghai # 设置当前环境时区

jobs:
  deploy:
    runs-on: ubuntu-latest # github action提供的容器环境
    steps:
      - name: Checkout master  # 拉仓库代码（包括所有的commit记录）到容器中（以下的所有操作都以仓库所在目录为根目录）
        uses: actions/checkout@v2
        with:
          fetch-depth: 0        

      - name: Set up Python3.x # 容器安装python3环境
        uses: actions/setup-python@v2
        with:
          python-version: 3.x

      - name: Install dependencies # 容器安装mkdocs material和用到的插件
        run: pip install mkdocs-material mkdocs-git-revision-date-localized-plugin

      - name: Build # 在容器中执行mkdocs 构建命令，构建成功生成html文件会保存在site文件夹中，执行tar将其压缩为site.tar.gz
        run: |
          mkdocs build
          tar -zcvf ./site.tar.gz ./site

      - name: upload build file to my host # 读取第3步配置的信息，调用rsync命令将site.tar.gz推送到SERVER_HOST服务器的rsync家目录
        uses: AEnterprise/rsync-deploy@v1.0
        env:
          DEPLOY_KEY: ${{ secrets.SERVER_KEY }}
          ARGS: -avz --delete --exclude='.user.ini'
          SERVER_PORT: ${{ secrets.SERVER_PORT }}
          FOLDER: ./site.tar.gz
          SERVER_IP: ${{ secrets.SERVER_HOST }}
          USERNAME: ${{ secrets.SERVER_USERNAME }}
          SERVER_DESTINATION: /home/rsync

      - name: execute script # ssh登录SERVER_HOST服务器，将site.tar.gz解压到网站根目录
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USERNAME }}
          key: ${{ secrets.SERVER_KEY }}
          script: |
            cd /www/wwwroot/shafish.cn/ && rm -rf ./*
            tar -zxvf /home/rsync/site.tar.gz -C ./ && mv site/* ./
```

### 5. 提交改动进行测试

略

![](https://picture.cdn.shafish.cn/blog/github-action-2021-12-ciresult.png)

!!! tip "项目工作流文件"

https://github.com/shafishcn/shafish_blog/tree/main/.github/workflows

  - ci.yml为github page部署：https://shafishcn.github.io/shafish_blog/
  - server_ci.yml是在下的服务器：https://shafish.cn/