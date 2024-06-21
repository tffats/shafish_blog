---
title: PVE使用
authors:
    - shafish
date:
    created: 2023-01-03
    updated: 2023-01-14
categories:
    - pve
    - 虚拟机
    - linux
---

[ :fishing_pole_and_fish: ](../../index.md)

### 一、显卡直通
[https://www.youtube.com/watch?v=5ce-CcYjqe8](https://www.youtube.com/watch?v=5ce-CcYjqe8){target=_black}
[https://www.youtube.com/watch?v=BoMlfk397h0&t=915s](https://www.youtube.com/watch?v=BoMlfk397h0&t=915s){target=_black}
[https://www.youtube.com/watch?v=_JTEsQufSx4](https://www.youtube.com/watch?v=_JTEsQufSx4){target=_black}
[https://gitlab.com/risingprismtv/single-gpu-passthrough/-/wikis/1](https://gitlab.com/risingprismtv/single-gpu-passthrough/-/wikis/1){target=_black}
``` shell
# 更新：
nano /etc/apt/sources.list.d/pve-enterprise.list 
# 注释掉 deb https://enterprise.proxmox.com/debian/pve stretch pve-enterprise
```

``` shell
echo "deb http://download.proxmox.com/debian/pve stretch pve-no-subscription" > /etc/apt/sources.list.d/pve-install-repo.list

gpg --keyserver keyserver.ubuntu.com --recv-keys 0D9A1950E2EF0603
gpg --export --armor 0D9A1950E2EF0603 | apt-key add -
apt update

echo "options vfio-pci ids=10de:2486,10de:228b  disable_vga=1" > /etc/modprobe.d/vfio.conf
nano /etc/modprobe.d/blacklist.conf
blacklist nouveau
blacklist radeon
blacklist nvidia

nano /etc/modprobe.d/kvm.conf
options kvm ignore_msrs=1

update-initramfs -u

reboot
```

直通：
1. [https://blog.51cto.com/u_12242014/2382885](https://blog.51cto.com/u_12242014/2382885){target=_black}

<!-- more -->

#### 记录
``` shell
修改文件：nano /etc/kernel/cmdline
在第一行末尾添加：quiet amd_iommu=on
添加模块：/etc/modules
vfio
vfio_iommu_type1
vfio_pci
vfio_virqfd
update-initramfs -u -k all
proxmox-boot-tool refresh
重启
```

#### n5105集显直通配置修改
``` shell
vim /etc/pve/lxc/103.conf 
lxc.apparmor.profile: unconfined
lxc.cgroup.devices.allow: a
lxc.cap.drop:
lxc.cgroup2.devices.allow: c 226:0 rwm
lxc.cgroup2.devices.allow: c 226:128 rwm
lxc.mount.entry: /dev/dri/card0 dev/dri/card0 none bind,optional,create=file
lxc.mount.entry: /dev/dri/renderD128 dev/dri/renderD128 none bind,optional,create=file
```

### 二、m2直通
``` shell
直通m2 ref:https://foxi.buduanwang.vip/virtualization/1754.html/
ls -la /dev/disk/by-id/|grep -v dm|grep -v lvm|grep -v part
qm set 102 --scsi0 /dev/disk/by-id/nvme-ZHITAI_TiPlus5000_512GB_ZTA2512KA22422018K
qm set 102 --scsi1 /dev/disk/by-id/ata-LITEON_CV3-8D256_0028023003AD

echo 0000:00:0a.0 > /sys/bus/pci/drivers/vfio-pci/unbind

echo "options vfio-pci ids=10de:2486,10de:228b" > /etc/modprobe.d/vfio.conf
```

### 三、修改本机磁盘容量
[https://www.youtube.com/watch?v=LuCXHHc2u18](https://www.youtube.com/watch?v=LuCXHHc2u18){target=_black}
``` shell
lvremove /dev/pve/data
lvresize -l +100%FREE /dev/pve/root
resize2fs /dev/mapper/pve-root
```

### 四、ct直通/dev/net/tun
装clash脚本时使用
#### 无特权容器
`vim /etc/pve/lxc/【NNN】.conf`
``` shell
lxc.hook.autodev = sh -c "modprobe tun" 
lxc.mount.entry=/dev/net/tun /var/lib/lxc/XXX/rootfs/dev/net/tun none bind,create=file
```

#### 特权容器
``` shell
# pve7+
lxc.cgroup2.devices.allow: c 10:200 rwm
lxc.hook.autodev = sh -c "modprobe tun; cd ${LXC_ROOTFS_MOUNT}/dev; mkdir net; mknod net/tun c 10 200; chmod 0666 net/tun"
# pve6
lxc.cgroup.devices.allow: c 10:200 rwm
lxc.hook.autodev = sh -c "modprobe tun; cd ${LXC_ROOTFS_MOUNT}/dev; mkdir net; mknod net/tun c 10 200; chmod 0666 net/tun"
```
ref:[https://www.cnblogs.com/lynetwork/articles/17271495.html](https://www.cnblogs.com/lynetwork/articles/17271495.html){target=_black}

### 五、容器启动失败
```
run_buffer: 321 Script exited with status 32
lxc_init: 847 Failed to run lxc.hook.pre-start for container "160"
__lxc_start: 2008 Failed to initialize container "160"
TASK ERROR: startup for container '160' failed
```

- `apt install binutils`
- `pct fsck xxx`
// docker缓存清掉
- `docker system prune --volumes`

### 六、pve更新失败
```
You are attempting to remove the meta-package 'proxmox-ve'!
xxxx
```

- `rm /var/lib/apt/lists/*`