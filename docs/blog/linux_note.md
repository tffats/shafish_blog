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

## 六、硬盘挂载
``` shell
# sudo
# 1. 插上硬盘
# 2. 查看对应设备名 (/dev/sda)
lsblk
# 3. 创建分区
fdisk /dev/sda
# 3.1 输入 n 创建一个新的分区
n
# 3.2 选择默认 p 选择主分区
p
# 3.3 输入分区号，默认从1开始，默认回车
# 3.4 first sector 起始扇区 (2048-4294967295, 默认 2048)：默认回车
# 3.5 last sector 结束扇区，默认使用剩余全部（或者自己需要多少，慢慢算）：回车
# 4. 保存设置
w
# 5.格式化分区
mkfs.ext4 /dev/sda1
# 6. 创建挂载点
mkdir /data
# 7. 挂载
mount /dev/sda1 /data
# 8. 设置开机自动挂载
# 8.1 查看新分区uuid (6ee49c14-xxxx-4bdd-xxxx-4e5afcd0e2fa)
blkid
# 8.2 备份默认配置
cp /etc/fstab /etc/fstab.bak
# 8.3 添加到fstab配置中 (要挂载的分区设备号	挂载点	文件系统类型	挂载选项	是否备份	是否检测)
echo "UUID=6ee49c14-xxxx-4bdd-xxxx-4e5afcd0e2fa /data ext4 defaults 0 0" >> /etc/fstab
# 9. 挂载配置 mount -a
# 10. 关机重启逝世
```

## 七、pve smb
``` shell
# sudo
# 1.安装smb
apt update && apt install samba -y
# 2. 验证安装 systemctl start nmbd
systemctl status nmbd
# 3. 创建共享文件夹
mkdir /data/smb/movie
# 4. 添加smb系统用户
useradd sha
smbpasswd -a sha
# 5.设置目录权限
chown -R sha /data/smb/movie
# 6. 备份默认配置
cp /etc/samba/smb.conf /etc/samba/smb.conf.bak
# 7. 开始配置
nano /etc/samba/smb.conf
```
``` conf
# 文件末尾添加
# 共享名称
[samba-movie]
# 描述
comment = for movie
# 共享路径
path = /data/smb/movie
# 要登录
guest ok = no
# 该共享设置为只读
read-only = no
# 可浏览
browsable = no
# smb用户
write list = sha
```
``` shell
# 8. 重启smb
systemctl restart smbd.service
```

## 八、挂载远程smb
``` shell
# 1. 安装挂载smb相关组件
apt install cifs-utils -y
# 2. 本地挂载目录
mkdir /data/movie
# 3. 挂载smb mount -t cifs -o username=sha,password=xxx //192.168.2.100/samba-movie /data/movie
nano /etc/fstab
//192.168.2.100/samba-movie /data/movie cifs username=sha,password=xxxx 0 0
# mount -t cifs //xxx-crf23.eu-west-1.nas.aliyuncs.com/myshare /mnt -o vers=2.0,guest,uid=0,gid=0,dir_mode=0755,file_mode=0755,mfsymlinks,cache=strict,rsize=1048576,wsize=1048576
```

## 九、scp远程传输文件
``` shell
scp /run/media/shafish/movie/xxxfile root@192.168.2.100:/data/smb/movie
```

## 十、mysql
``` shell
# 安装
apt install mysql-server
# 检查
systemctl status mysql
# 配置
mysql_secure_installation
# 授权
GRANT ALL PRIVILEGES ON *.* TO 'administrator'@'localhost' IDENTIFIED BY 'very_strong_password888';
```