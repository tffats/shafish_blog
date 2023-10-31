---
title: Archlinx主力机配置记录
description: archlinx, i3wm
hide:
  - navigation
---

## 硬件环境
- CPU:AMD5900X
- 主板:华擎x570太极雷蛇
- 显卡:3060Ti 8G
- 台式机！！！

## 一、archlinx系统安装
视频参考：[https://www.bilibili.com/video/BV1J34y1f74E/](https://www.bilibili.com/video/BV1J34y1f74E/){target=_blank}

### 1. archlinux USB引导安装

???+ "把arch镜像写入U盘就行"

    - 插入U盘
    - 下载写盘工具[BalenaEtcher](https://etcher.balena.io/){target=_blank} 或者 [Rufus](https://rufus.ie/){target=_blank}
    - 下载[Archlinux镜像](https://archlinux.org/download/){target=_blank}

### 2. 设置U盘启动，进入live环境

???+ "F2 bro"

    - 根据主板进入bios（一般是F2或者F12），禁用安全启动，并选择U盘启动

    启动后出现引导加载程序菜单，选择Arch Linux install medium 确认就行

### 3. 硬盘分区配置(整个磁盘，非双系统)

???+ "zhitai yyds"

    内存>=16G建议就别创建交换空间了，如果确实程序需要高内存，建议直接加内存。

    - 查看硬盘：`lsblk`（磁盘对应映射文件为/dev/sdx或者/dev/nvmexxx）
    - 创建分区：`cfdisk /dev/nvme0n1`
        - efi引导分区：500M (nvme0n1p1)
        - /根分区：剩余所有容量 (nvme0n1p2)
    - 分区格式化
        - 根分区格式化：`mkfs.ext4 /dev/nvme0n1p2`
        - efi分区格式化：`mkfs.fat -F 32 /dev/nvme0n1p1`
    - 分区挂载（挂载分区一定要遵循顺序，先挂载根分区，再挂载引导分区）
        - 挂在根分区：`mount /dev/nvme0n1p2 /mnt`
        - 挂在引导分区：`mount --mkdir /dev/nvme0n1p1 /mnt/boot`

### 4. 安装系统包

???+ "linux-zen是华点"

    这里选择安装[linux-zen内核](https://wiki.archlinuxcn.org/wiki/%E5%86%85%E6%A0%B8){target=_blank}，需要特别留意待会显卡驱动的安装。amd-ucode是amd的微码包。

    - `pacstrap -K /mnt base linux-zen linux-firmware base-devel amd-ucode`

### 5. 配置默认挂载目录
- `genfstab -U /mnt >> /mnt/etc/fstab`

### 6. 配置系统根目录环境
- `arch-chroot /mnt`

#### 6.1 时区配置
- `ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime`
- `hwclock --systohc`

#### 6.2 本地化配置

???+ "先选择en_US"

    - 安装编辑器：`pacman -S vim`
    - 修改配置文件：`vim /etc/locale.gen`（取消掉 en_US.UTF-8 UTF-8前的注释符#）
    - 生成locale信息：`locale-gen`
    - 修改配置文件：`vim /etc/locale.conf`（添加LANG=en_US.UTF-8）

#### 6.3 网络配置

???+ "NetworkManager不错"

    [https://wiki.archlinuxcn.org/wiki/NetworkManager](https://wiki.archlinuxcn.org/wiki/NetworkManager){target=_blank}

    - 配置主机名：`vim /etc/hostname`（添加主机名）
    - `pacman -S networkmanager nm-connection-editor network-manager-applet`
    - `systemctl enable NetworkManager.service`

#### 6.4 root用户配置

???+ "以后要把自定义用户加到wheel组"

    - `passwd`
    - `pacman -S sudo`
    - `visudo`（去掉%wheel ALL=(ALL:ALL) ALL 注释）

#### 6.5 开机引导配置

???+ "efi+GRUB的组合"

    [https://wiki.archlinuxcn.org/wiki/GRUB](https://wiki.archlinuxcn.org/wiki/GRUB){target=_blank}

    - `pacman -S grub efibootmgr`
    - `grub-install --target=x86_64-efi --efi-directory=/boot --bootloader-id=GRUB`
    - `grub-mkconfig -o /boot/grub/grub.cfg`

### 7. 系统重启
- `exit`
- `reboot`

## 二、系统配置
### 添加普通用户
### 窗口管理器-I3WM
### 显示管理器-SDDM
### 蓝牙
### 声音
### 字体

## 三、软件配置

### 显卡驱动
### 输入法
### polybar
https://github.com/polybar/polybar-scripts

#### redshift
https://github.com/VineshReddy/polybar-redshift

#### usb
https://github.com/polybar/polybar-scripts/tree/master/polybar-scripts/system-usb-udev

KERNEL=="sd*", ACTION=="add", ATTR{removable}=="1", \
    RUN+="/home/shafish/.config/polybar/script/system-usb-udev.sh --update"
KERNEL=="sd*", ACTION=="remove", \
    RUN+="/home/shafish/.config/polybar/script/system-usb-udev.sh --update"

### qq
安装：`yay -S linuxqq`
#### 问题：
- 环境：archlinux+i3wm
- 问题：每次qq接收消息都会闪退
- 解决：
    - 用终端输入linuxqq运行，闪退时查看运行log（鼠标滚轮滚了几十秒才看到在开头报了error）
    - 报错信息：[29188:1027/223745.914909:ERROR:libnotify_notification.cc(49)] notify_notification_show: domain=2117 code=2 message="GDBus.Error:org.freedesktop.DBus.Error.ServiceUnknown: The name org.freedesktop.Notifications was not provided by any .service files"
    - 谷歌搜这个报错，找到https://wiki.archlinux.org/title/Desktop_notifications#Standalone，完美解决


*[BalenaEtcher]: Linux系统下建议的写盘工具
*[Rufus]: Window系统下建议的写盘工具
*[Archlinux镜像]: 选China下载