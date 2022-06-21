---
title: Linux日常
description: linux, rsync
hide:
  - navigation
---

## 一、复制大文件显示进度

``` shell
rsync -av --progress t01/demo.zip t02/
```

## 二、系统u盘制作

``` shell
sudo dd bs=4M if=/path/to/arcolinux...iso of=/dev/sdX status=progress && sync
```

## 三、查看Manjaro安装的软件

``` shell
cat /var/log/pacman.log | grep -E installed > installed
```

## 四、文件备份

``` shell
[shafish@shafish-laptop shafish]# sudo su
[root@shafish-laptop shafish]# cd /
[root@shafish-laptop shafish]# tar cvpzf system_backup_root.tar.gz \
--exclude=/proc \
--exclude=/lost+found \
--exclude=/dev \
--exclude=/lib \
--exclude=/mnt \
--exclude=/sys \
--exclude=/home \
--exclude=/run/media \
--one-file-system \
--exclude=/system_backup_root.tar.gz \
/
[root@shafish-laptop shafish]# cd /home
[root@shafish-laptop shafish]# tar cvpzf system_backup_home.tar.gz \
--exclude=/home/shafish/node_modules \
--exclude=/home/shafish/.m2 \
--exclude=/home/shafish/snap \
--exclude=/home/system_backup_home.tar.gz \
/home
```

## 五、git代理
``` shell
git config --global http.proxy 192.168.2.1:7890
git config --global https.proxy 192.168.2.1:7890
git config --global --unset http.proxy
git config --global --unset https.proxy
```