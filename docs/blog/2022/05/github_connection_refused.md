---
title: 抽风的Git
description: connect to host ssh.github.com port 443, connect to host ssh.github.com port 22, Connection refused
hide:
  - navigation
---

## 一、现象
代码提交时出现`ssh: connect to host ssh.github.com port 443: Connection refused`错误

## 二、解决
### 1.换回22端口-无用
把`~/.ssh/config`文件中的
``` 
Host github.com
Hostname ssh.github.com
PreferredAuthentications pulickey
IdentityFile ~/.ssh/id_rsa
Port 443
```
去掉

### 2.指定使用https仓库-无用
`vim shafishcn/.git/config`，将`url = git@github.com:shafishcn/shafish_blog.git`改成`url = https://github.com/shafishcn/shafish_blog.git`

### 3.重新配置ssh key-发现问题
`ssh -vT git@github.com`命令显示访问的ssh.github.com ip是本地的？？？？？
``` 
shafish.cn[edit*] % ssh -vT git@github.com
OpenSSH_9.0p1, OpenSSL 1.1.1o  3 May 2022
debug1: Reading configuration data /home/shafish/.ssh/config
debug1: /home/shafish/.ssh/config line 39: Applying options for github.com
debug1: Reading configuration data /etc/ssh/ssh_config
debug1: Connecting to ssh.github.com [::1] port 443.
debug1: connect to address ::1 port 443: Connection refused
debug1: Connecting to ssh.github.com [127.0.0.1] port 443.
debug1: connect to address 127.0.0.1 port 443: Connection refused
ssh: connect to host ssh.github.com port 443: Connection refused
```

### 4.修改本地的DNS解析-没试
网上说出现这种问题大多是DNS解析被污染了。
- 查找github.com的ip，手动设置host
    - [https://www.ipaddress.com/](https://www.ipaddress.com/){target=_blank}
    - `sudo vim /etc/hosts` 添加 `140.82.114.4 github.com`

### 5.直接用代理-亲测有效随便加加攻速
- https
```
git config --global http.https://github.com.proxy socks5://127.0.0.1:1080
#取消代理
git config --global --unset http.https://github.com.proxy)
```
- ssh
`vim ~/.ssh/config`
```
Host github.com
ProxyCommand nc -X 5 -x 127.0.0.1:1080 %h %p
```
`sudo pacman -S openbsd-netcat`

ref:`https://gist.github.com/laispace/666dd7b27e9116faece6`
