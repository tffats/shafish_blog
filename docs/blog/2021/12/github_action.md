
# Github Action

ref:

- http://www.ruanyifeng.com/blog/2019/09/getting-started-with-github-actions.html

用法和概念基本都是参考 阮一峰 的这篇文章，所以在这里纯粹是记录下使用。

## 概念介绍

### workflow

工作流。持续集成一次运行的过程，就是一个 workflow。

### job

任务。一个 workflow 由一个或多个 jobs 构成，含义是一次持续集成的运行，可以完成多个任务。

### step

步骤。每个 job 由多个 step 构成，一步步完成。

### action

动作。每个 step 可以依次执行一个或多个命令（action）。
部署项目过程中的服务器登录、运行环境的部署、代码拉取、运行部署脚本等操作，在github action中都被看作为是一个个的action。
其中的大部分操作都是可以被复用的：比如环境部署，基本就只有软件对应系统和版本的区别。
所以Github Action中使用到的action是可以直接引用他人写好的 action滴，可以在下面列出的仓库中找找：

- [Github官方市场](https://github.com/marketplace?type=actions)
- [awesome actions] (https://github.com/sdras/awesome-actions)

## Workflow定义

在代码仓库的.github/workflows目录下定义，后缀名统一为.yml，Github会自动运行这些yml文件。

### 基本配置字段

- `name`：指定workflow 的名称。
- `on.<push|pull_request>.<tags|branches>`：触发所在工作流执行的条件，比如 某个分支发生push/pull时。

``` yml
# masterf分支发生push时触发工作流执行
on:
  push:
    branches:    
      - master
```

- `jobs.<job_id>.name`：需要执行的一项或多项任务。

``` yml
# job_id 对应特定的任务，my_first_job、my_second_job
# name 是任务的说明
jobs:
  my_first_job:
    name: My first job
  my_second_job:
    name: My second job
```

- `jobs.<job_id>.needs`：指定当前任务的运行顺序。

``` yml
# job1必须先于job2完成，而job3等待job1和job2的完成才能运行。因此，这个 workflow 的运行顺序依次为：job1、job2、job3
jobs:
  job1:
  job2:
    needs: job1
  job3:
    needs: [job1, job2]
```

- `jobs.<job_id>.runs-on`：指定运行所需要的虚拟机环境。

``` yml
# 可选的环境如下：
ubuntu-latest，ubuntu-18.04或ubuntu-16.04
windows-latest，windows-2019或windows-2016
macOS-latest或macOS-10.14
```

- `jobs.<job_id>.steps`：指定每个 Job 的运行步骤，可以包含一个或多个步骤，步骤包含name、env、run三个字段。

- 完整的工作流定义：

``` yml
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

## 示例

### ci.yml熟悉

``` yml
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

``` yml
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

### 把mkdocs部署到github page

