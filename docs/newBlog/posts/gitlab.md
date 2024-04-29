---
title: 本地gitlab搭建
authors:
    - shafish
date:
    created: 2024-04-29
    updated: 2024-04-29
categories:
    - gitlab
---

![](https://file.cdn.shafish.cn/blog/blog/git/%E5%9B%BE%E7%89%87.png){: .zoom}

## 一、配置docker环境

- [Docker Engine](https://docs.docker.com/engine/install){target=_blank}

- [Docker Compose](https://docs.docker.com/compose/){target=_blank}

## 二、修改服务器ssh端口

gitlab使用SSH协议进行代码的版本控制和远程访问，默认会占用ssh的22端口。官方建议是修改ssh服务的端口：

``` shell title="vim /etc/ssh/sshd_config"
Port 2424
```
重启ssh服务：`systemctl restart ssh`

<!-- more -->

## 三、配置gitlab挂载目录

- 创建数据目录：`mkdir /data/docker/gitlab`

- 配置通用变量：
``` shell title="vim ~/.bashrc"
export GITLAB_HOME=/data/docker/gitlab
``` 

- 配置生效：`source ~/.bashrc`

- 目录说明：

| 宿主机      | 容器                          | 描述                          |
| :-----------: | :------------------------------------: | :------------------------------------: |
| `$GITLAB_HOME/data`       | /var/opt/gitlab  | 保存代码数据  |
| `$GITLAB_HOME/logs`       | /var/log/gitlab | 保存log数据  |
| `$GITLAB_HOME/config`    | /etc/gitlab | 保存配置数据  |

## 四、gitlab版本选择

> 直接最新(24-4-28)稳定版：`docker pull gitlab/gitlab-ce:16.11.1-ce.0`

- [商业版本-ee](https://hub.docker.com/r/gitlab/gitlab-ee/tags/){target=_blank}

- [社区版本-ce](https://hub.docker.com/r/gitlab/gitlab-ce/tags/){target=_blank}

## 五、安装

``` shell title="vim /data/docker/gitlab/docker-compose.yml"
version: '3.6'
services:
  gitlab:
    image: gitlab/gitlab-ce:16.11.1-ce.0
    container_name: gitlab
    restart: always
    hostname: 'gitlab.example.com'
    environment:
      TZ: 'Asia/Shanghai'
      GITLAB_OMNIBUS_CONFIG: |
        # Add any other gitlab.rb configuration here, each on its own line
        external_url 'http://192.168.0.109:7080'
        gitlab_rails['time_zone'] = 'Asia/Shanghai'
    ports:
      - '192.168.0.109:7080:7080'
      - '192.168.0.109:7443:443'
      - '192.168.0.109:22:22'
    volumes:
      - '$GITLAB_HOME/config:/etc/gitlab'
      - '$GITLAB_HOME/logs:/var/log/gitlab'
      - '$GITLAB_HOME/data:/var/opt/gitlab'
    shm_size: '1g'
```

- 运行：`docker compose up -d`
- 耐心等个2min+
- 可以看看执行log：`docker logs -f gitlab`

## 六、root登录

- 访问gitlab： `192.168.0.109:7080`
- 查看root密码（容器启动24h后会自动删除）：`docker exec -it gitlab grep 'Password:' /etc/gitlab/initial_root_password` 

## 七、SMTP邮件配置

- 进入gitlab容器：`docker exec -it gitlab bash`
- 修改 `gitlab.rb` 配置文件
``` rb title="editor /etc/gitlab/gitlab.rb"
gitlab_rails['smtp_enable'] = true
gitlab_rails['smtp_address'] = "smtp.qq.com"
gitlab_rails['smtp_port'] = 465
gitlab_rails['smtp_user_name'] = "shafish@qq.com"
gitlab_rails['smtp_password'] = "授权码"
gitlab_rails['smtp_domain'] = "smtp.qq.com"
gitlab_rails['smtp_authentication'] = "login"
gitlab_rails['smtp_enable_starttls_auto'] = true
gitlab_rails['smtp_tls'] = false
gitlab_rails['gitlab_email_from'] = 'shafish@qq.com'  #注意这个一定要填写，不然会报502错误
gitlab_rails['smtp_pool'] = true
```
- 重启服务：`gitlab-ctl reconfigure`

## 八、升级

- [Upgrade](https://docs.gitlab.com/ee/install/docker.html#upgrade){target=_blank}

ref:

- [Install GitLab by using Docker](https://docs.gitlab.com/ee/install/docker.html){target=_blank}
- [gitlab smtp](https://docs.gitlab.com/omnibus/settings/smtp.html){target=_blank}