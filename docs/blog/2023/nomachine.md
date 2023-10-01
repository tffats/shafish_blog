---
title: 远程桌面控制
hide:
  - navigation
---

## 一、nomachine介绍
这是一个内网的远程桌面控制软件，使用专有的nx协议连接，会根据网络速度和容量动态调整压缩和带宽，支持win、linux、mac、android、ios。实测使用丝般流畅（得看你的服务器带宽，3M带宽下比向日葵好）。

在内网中使用需要控制与被控端都安装nomachine，启动后会自动扫描并展示可用的nomachine服务。

它的nx协议走的是4000端口，只需对外暴露本地的4000端口就能通过网络连接远程访问！

!!! tip "被控端显示设置"
    被控端在使用nomachine时不能关闭显示器，可以设置无头(Headless)显示，或者淘宝直接买给假负载给电脑插上。

## 二、准备
使用场景：manjaro系统的笔记本远程控制ubuntu系统的台式机

- 一台公网ip的机器
- hdmi假负载
- 网络畅通

## 三、nomachine安装
### ubuntu系统
进入 [nomachine](https://downloads.nomachine.com/linux/?id=1){target=_blank} 下载好安装包，直接安装就行。

``` shell
sudo apt install ./nomachine_8.8.1_1_amd64.deb
```

### manjaro系统
因为deb不是arch系的包，这里直接编译再打包安装（需要网络代理加快速度）

``` shell
sudo pacman -S git
sudo pacman -S --needed git base-devel
git clone https://aur.archlinux.org/nomachine.git
cd nomachine
makepkg
sudo pacman -U nomachine-8.8.1-1-x86_64.pkg.tar.zst #nomachine目录下生成的zst文件
pacman -Ql nomachine
```

装好后可以内网connect测试下是否能桌面控制。

## 四、公网服务器配置
安装frps，提供内网穿透。

### frps安装
``` shell
cd /opt
wget https://github.com/fatedier/frp/releases/download/v0.51.3/frp_0.51.3_linux_amd64.tar.gz
tar zxvf frp_0.51.3_linux_amd64.tar.gz
cd frp_0.51.3_linux_amd64
```

### frps.ini配置
``` title="vim frps.ini"
[common]
bind_port = 7000 // 运行端口
token = 22BxxxxxJ // 连接token
dashboard_port = 1100 // 后台面板端口
dashboard_user = xxxh // 后台面板登录用户
dashboard_pwd = fxxxxd8 // 后台面板登录用户密码
vhost_http_port = 7086 // 域名http访问端口，给http协议用
vhost_https_port = 7088 // 域名https访问端口，给http协议用
subdomain_host = shafish.cn // 域名demain，给http协议用
enable_prometheus = true
log_file = /var/log/frps.log
log_level = info
log_max_days = 3
```

### systemctl服务配置
``` title="vim frps.service"
[Unit]
Description = frp server
After = network.target syslog.target
Wants = network.target

[Service]
Type = simple
ExecStart = /opt/frp_0.51.3_linux_amd64/frps -c /opt/frp_0.51.3_linux_amd64/frps.ini
ExecReload = /opt/frp_0.51.3_linux_amd64/frps reload -c /opt/frp_0.51.3_linux_amd64/frps.ini
Restart = on-failure
RestartSec = 5

[Install]
WantedBy = multi-user.target
```

``` shell
cp frps.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable frps
systemctl start frps
systemctl status frps
```

### 端口开放
> 主要是frps的运行端口`bind_port`

- 先在服务器控制台的安全组开好端口！
- 再到服务器防火墙开放端口！

``` shell
firewall-cmd --zone=public --query-port=7007/tcp  # 查询对应的端口是否开启
firewall-cmd --zone=public --add-port=7000/tcp --permanent # 开启某个端口
firewall-cmd --zone=public --remove-port=9200/tcp --permanent # 关闭某个端口
firewall-cmd --reload # 重启防火墙使配置生效
```

## 五、被控端：ubuntu系统设置
### frpc安装
``` shell
cd /home/shafish/Software
wget https://github.com/fatedier/frp/releases/download/v0.51.3/frp_0.51.3_linux_amd64.tar.gz
tar zxvf frp_0.51.3_linux_amd64.tar.gz
cd frp_0.51.3_linux_amd64
mkdir /home/shafish/Software/frp_0.51.3_linux_amd64/confd
```

### frpc.ini配置
``` title="vim frpc.ini"
[common]
server_addr = x.x.x.x // 公网服务器ip
server_port = 7000 // 运行端口
token = 22BxxxxxJ // 连接token
log_file = /var/log/frps.log
log_level = info
log_max_days = 3
includes = /home/shafish/Software/frp_0.51.3_linux_amd64/confd/*.ini
```

### nomachine.ini配置
``` title="vim /home/shafish/Software/frp_0.51.3_linux_amd64/confd/nomachine.ini"
[amd-ubuntu-nomachine]
type = stcp
local_ip = 127.0.0.1
local_port = 4000
sk = amd-!#$@%^xxxxx-nomachine-token
```

### systemctl服务配置
``` title="vim frpc.service"
[Unit]
Description = frp client
After = network.target syslog.target
Wants = network.target

[Service]
Type = simple
ExecStart = /home/shafish/Software/frp_0.51.3_linux_amd64/frpc -c /home/shafish/Software/frp_0.51.3_linux_amd64/frpc.ini
ExecReload = /home/shafish/Software/frp_0.51.3_linux_amd64/frpc reload -c /home/shafish/Software/frp_0.51.3_linux_amd64/frpc.ini
Restart = on-failure
RestartSec = 5

[Install]
WantedBy = multi-user.target
```

``` shell
cp frpc.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable frpc
systemctl start frpc
systemctl status frpc
```

## 六、控制端：manjaro系统设置
### frpc安装
``` shell
cd /home/shafish/Software
wget https://github.com/fatedier/frp/releases/download/v0.51.3/frp_0.51.3_linux_amd64.tar.gz
tar zxvf frp_0.51.3_linux_amd64.tar.gz
cd frp_0.51.3_linux_amd64
mkdir /home/shafish/Software/frp_0.51.3_linux_amd64/confd
```

### frpc.ini配置
``` title="vim frpc.ini"
[common]
server_addr = x.x.x.x // 公网服务器ip
server_port = 7000 // 运行端口
token = 22BxxxxxJ // 连接token
log_file = /var/log/frps.log
log_level = info
log_max_days = 3
includes = /home/shafish/Software/frp_0.51.3_linux_amd64/confd/*.ini
```

### nomachine.ini配置
``` title="vim /home/shafish/Software/frp_0.51.3_linux_amd64/confd/nomachine.ini"
[amd-manjaro-nomachine]
type = stcp
bind_addr = 127.0.0.1
bind_port = 4000
sk = amd-!#$@%^xxxxx-nomachine-token
role = visitor
server_name = amd-ubuntu-nomachine
```

### systemctl服务配置
``` title="vim frpc.service"
[Unit]
Description = frp client
After = network.target syslog.target
Wants = network.target

[Service]
Type = simple
ExecStart = /home/shafish/Software/frp_0.51.3_linux_amd64/frpc -c /home/shafish/Software/frp_0.51.3_linux_amd64/frpc.ini
ExecReload = /home/shafish/Software/frp_0.51.3_linux_amd64/frpc reload -c /home/shafish/Software/frp_0.51.3_linux_amd64/frpc.ini
Restart = on-failure
RestartSec = 5

[Install]
WantedBy = multi-user.target
```

``` shell
cp frpc.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable frpc
systemctl start frpc
systemctl status frpc
```

!!! tip 
    主要留意控制端与被控端的nomachine.ini文件配置，以上配置将ubuntu的本地4000端口映射给了manjaro的4000端口，所以在manjaro使用nomachine时只需连接本地127.0.0.1:4000就能显示并且控制ubuntu的桌面！

!!! note "frp推广"
    确实是好东西，现在[2023/10/1]的版本是0.51，估计快合并1.x了，2.x也在进行开发中。目前主要是用在内网穿透方面，听说后续会加上k8s云原生、扩展插件等功能，持续追踪，非常看好。

![](https://picture.cdn.shafish.cn/blog/10-1/2023-10-01_16-34.png)

![](https://picture.cdn.shafish.cn/blog/10-1/2023-10-01_16-32.png)