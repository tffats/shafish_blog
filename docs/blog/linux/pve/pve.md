## pve镜像

https://www.proxmox.com/en/downloads/proxmox-virtual-environment/iso

## 写盘工具

https://github.com/balena-io/etcher

## pve修改中科大源

https://mirrors.ustc.edu.cn/help/proxmox.html#proxmox

``` shell
sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list.d/debian.sources

cat > /etc/apt/sources.list.d/pve-no-subscription.sources <<EOF
Types: deb
URIs: https://mirrors.ustc.edu.cn/proxmox/debian/pve
Suites: trixie
Components: pve-no-subscription
Signed-By: /usr/share/keyrings/proxmox-archive-keyring.gpg
EOF

if [ -f /etc/apt/sources.list.d/ceph.sources ]; then
  CEPH_CODENAME=`ceph -v | grep ceph | awk '{print $(NF-1)}'`
  source /etc/os-release
  cat > /etc/apt/sources.list.d/ceph.sources <<EOF
Types: deb
URIs: https://mirrors.ustc.edu.cn/proxmox/debian/ceph-$CEPH_CODENAME
Suites: $VERSION_CODENAME
Components: no-subscription
Signed-By: /usr/share/keyrings/proxmox-archive-keyring.gpg
EOF
fi

sed -i.bak 's|http://download.proxmox.com|https://mirrors.ustc.edu.cn/proxmox|g' /usr/share/perl5/PVE/APLInfo.pm

apt update
apt dist-upgrade
```

## 优化脚本

https://github.com/a904055262/PVE-manager-status/blob/main/showtempcpufreq.sh
https://raw.githubusercontent.com/oneclickvirt/pve/main/scripts/build_backend.sh

``` shell
# 备份
wget https://open.cdn.shafish.cn/build_backend.sh
wget https://open.cdn.shafish.cn/showtempcpufreq.sh
```

## 开启CPU节能模式

``` shell
apt install linux-cpupower powertop -y
# 高性能模式
cpupower  frequency-set -g performance
# 省电模式
cpupower  frequency-set -g powersave
```

## 删除 local-lvm合并至 local分区

``` shell
lvremove pve/data
lvextend -rl +100%FREE pve/root
#resize2fs /dev/mapper/pve-root
```