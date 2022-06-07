---
title: linux日常
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