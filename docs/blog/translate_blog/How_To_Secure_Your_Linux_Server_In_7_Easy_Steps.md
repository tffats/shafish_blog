---
title: Linux Server7个简单的安全配置
description:  shafish.cn linux linux安全配置 服务器
hide:
  - navigation
---

# Linux(Ubuntu) Server7个简单的安全配置

[Back](/blog/#2022年文章导航){ .md-button}

ref: 

- [https://medium.com/servers-101/how-to-secure-your-linux-server-6026cfcdefd8](https://medium.com/servers-101/how-to-secure-your-linux-server-6026cfcdefd8){target=_blank}
- [https://sollove.com/2013/03/03/my-first-5-minutes-on-a-server-or-essential-security-for-linux-servers/](https://sollove.com/2013/03/03/my-first-5-minutes-on-a-server-or-essential-security-for-linux-servers/){target=_blank}


大多服务器都时不时被攻击过，所以这里介绍几个简单加固linux server系统安全的方法，预防比如：恶意登录、ddos等攻击。

## 准备阶段
如果宁还不熟悉关于linux的基本命令操作，先打开 [https://learncodethehardway.org/unix/bash_cheat_sheet.pdf](https://learncodethehardway.org/unix/bash_cheat_sheet.pdf){target=_blank} 浏览几遍再来。

## 配置SSH key
为了访问远程服务器，宁可以使用帐号密码登录或者使用SSH key授权登录。允许密码登录的情况下，如果设置了弱密码，那毫无疑问会招到暴力登录攻击。这里介绍下怎么配置ssh登录。

- 生成ssh密钥对：`ssh-keygen`
- 将生成的公钥配置到指定服务器：`ssh-copy-id username@remote_host`
- 登录该服务器(不需输入密码)：`ssh username@remote_host`

## 设置最新系统时间
使用`ntp`自动同步网络时间：`sudo apt install ntp`

## 查看网络开放端口
联网的机器是通过这些开放端口提供数据出入口的，黑客攻击会扫描服务器中开放的端口进行针对性攻击，所以你需要知道哪些端口是你没用到的。

查看开放端口：`sudo ss -lntup`

## 配置防火墙
防火墙控制服务的端口间通信，这里使用UFW为例。

- 安装UFW：`sudo apt-get install ufw`
- 禁止outgoing：`sudo ufw default deny outgoing comment 'deny all outgoing traffic'`  outgoing:服务器访问外部网络(端口)的配置
- 开放outgoing：`sudo ufw default allow outgoing comment 'allow all outgoing traffic'`
- 禁止incoming：`sudo ufw default deny incoming comment 'deny all incoming traffic'` incoming:外部网络访问本服务器的网络(端口)配置
- 防止ssh暴力登录：`sudo ufw limit in ssh comment 'allow SSH connections in'` attempt to log in or connect more than 6 times in 30 seconds
- 开放指定端口的outgoing：`sudo ufw allow out port/protocol comment 'xxx'`
    - port例如：`sudo ufw allow out 53 comment 'allow DNS calls out'`
    - protocol例如：`sudo ufw allow out http comment 'allow HTTP traffic out'`
- 禁止某端口的outgoing、ingoing：`sudo ufw deny port`
- 启动ufw：`sudo ufw enable`
- 查看ufw状态：`sudo ufw status`

## 预防自动脚本攻击
- [PSAD](http://www.cipherdyne.org/psad/){target=_blank}
- [Fail2Ban](https://www.fail2ban.org/){target=_blank}

