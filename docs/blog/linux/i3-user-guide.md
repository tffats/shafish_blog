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

### 1.1 archlinux USB引导安装

???+ "把arch镜像写入U盘就行"

    - 插入U盘
    - 下载写盘工具[BalenaEtcher](https://etcher.balena.io/){target=_blank} 或者 [Rufus](https://rufus.ie/){target=_blank}
    - 下载[Archlinux镜像](https://archlinux.org/download/){target=_blank}

### 1.2 设置U盘启动，进入live环境

???+ "F2 bro"

    - 根据主板进入bios（一般是F2或者F12），禁用安全启动，并选择U盘启动

    启动后出现引导加载程序菜单，选择Arch Linux install medium 确认就行

### 1.3 硬盘分区配置(整个磁盘，非双系统)

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

### 1.4 安装系统包

???+ "linux-zen是华点"

    这里选择安装[linux-zen内核](https://wiki.archlinuxcn.org/wiki/%E5%86%85%E6%A0%B8){target=_blank}，需要特别留意待会显卡驱动的安装。amd-ucode是amd的微码包。

    - `pacstrap -K /mnt base linux-zen linux-firmware base-devel amd-ucode`

### 1.5 配置默认挂载目录
- `genfstab -U /mnt >> /mnt/etc/fstab`

### 1.6 配置系统根目录环境
- `arch-chroot /mnt`

#### 1.6.1 时区配置
- `ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime`
- `hwclock --systohc`

#### 1.6.2 本地化配置

???+ "先选择en_US"

    - 安装编辑器：`pacman -S vim`
    - 修改配置文件：`vim /etc/locale.gen`（取消掉 en_US.UTF-8 UTF-8前的注释符#）
    - 生成locale信息：`locale-gen`
    - 修改配置文件：`vim /etc/locale.conf`（添加LANG=en_US.UTF-8）

#### 1.6.3 网络配置

???+ "NetworkManager不错"

    [https://wiki.archlinuxcn.org/wiki/NetworkManager](https://wiki.archlinuxcn.org/wiki/NetworkManager){target=_blank}

    - 配置主机名：`vim /etc/hostname`（添加主机名）
    - `pacman -S networkmanager nm-connection-editor network-manager-applet`
    - `systemctl enable NetworkManager.service`

#### 1.6.4 root用户配置

???+ "以后要把自定义用户加到wheel组"

    - `passwd`
    - `pacman -S sudo`
    - `visudo`（去掉%wheel ALL=(ALL:ALL) ALL 注释）

#### 1.6.5 开机引导配置

???+ "efi+GRUB的组合"

    [https://wiki.archlinuxcn.org/wiki/GRUB](https://wiki.archlinuxcn.org/wiki/GRUB){target=_blank}

    - `pacman -S grub efibootmgr`
    - `grub-install --target=x86_64-efi --efi-directory=/boot --bootloader-id=GRUB`
    - `grub-mkconfig -o /boot/grub/grub.cfg`

### 1.7 系统重启
- `exit`
- `reboot`

## 二、系统配置

### 2.1 安装yay、archlinuxcn

???+ "yay+archlinuxcn无敌"

    - 修改pacman配置文件：`vim /etc/pacman.conf`
        ```
        [multilib]
        Include = /etc/pacman.d/mirrorlist
        [archlinuxcn]
        Server = https://mirrors.ustc.edu.cn/archlinuxcn/$arch
        ```
    - 更新下系统：`pacman -Syu`
    - 密钥更新：`pacman -S archlinuxcn-keyring`
    - yay安装：`pacman -S yay`

### 2.2 添加普通用户

???+ "shafish来了"

    - 创建用户：`useradd -m shafish`
    - 将该用户加到wheel组中：`usermod -a -G wheel`
    - 设置用户密码：`passwd shafish`
    - 创建默认目录：比如Download、Document等目录
        - 切换到新建用户：`su - shafish`
        - `sudo pacman -S xdg-user-dirs`
        - `LC_ALL=C xdg-user-dirs-update --force`

### 2.3 窗口管理器-I3WM
i3-gaps已经合并到i3wm 4.23版本，别安装i3-gaps了。
[https://wiki.archlinuxcn.org/wiki/I3](https://wiki.archlinuxcn.org/wiki/I3){target=_blank}
[https://i3wm.org/docs/userguide.html](https://i3wm.org/docs/userguide.html){target=_blank}

???+ "xorg是显示服务器，i3wm是窗口管理器。xorg是基础框架，i3wm及其系列组件则提供桌面环境支持"

    - `pacman -S xorg-server xorg-apps i3-wm i3status`

???+ "vim ~/.config/i3/config"

    ``` shell
    # 窗口隐藏
    bindsym $mod+minus move scratchpad
    bindsym $mod+plus scratchpad show
    # 边距
    gaps inner 3px
    gaps outer 3px
    gaps top 1px
    smart_gaps inverse_outer
    # 去掉窗口title
    for_window [class="^.*"] border pixel 0
    # 工作区切换
    workspace_auto_back_and_forth yes
    ```

### 2.4 显示管理器-SDDM
[https://wiki.archlinuxcn.org/wiki/SDDM](https://wiki.archlinuxcn.org/wiki/SDDM){target=_blank}

???+ "别用xorg-xinit的startx进入桌面环境了"

    - 安装sddm包：`sudo pacman -S --needed sddm qt5-graphicaleffects qt5-quickcontrols2 qt5-svg`
    - 开机启动sddm服务：`sudo systemctl enable sddm`
    - 启动服务：`sudo systemctl start sddm`
    - 创建配置目录：`sudo mkdir /etc/sddm.conf.d`
    - 生成默认配置文件：`sudo sddm --example-config > /etc/sddm.conf.d/sddm.conf`
    - 使用sddm-sugar-candy主题：
        - `cd /usr/share/sddm/themes/`
        - `sudo git clone https://framagit.org/MarianArlt/sddm-sugar-candy.git`
        - `sudo vim /etc/sddm.conf.d/sddm.conf` 中配置 Current=sddm-sugar-candy

### 2.5 终端模拟器
- `pacman -S alacritty`

### 2.6 蓝牙
[https://wiki.archlinuxcn.org/wiki/%E8%93%9D%E7%89%99](https://wiki.archlinuxcn.org/wiki/%E8%93%9D%E7%89%99){target=_blank}

- `sudo pacman -S bluez bluez-utils`
- `sudo systemctl enable bluetooth.service`
- `sudo systemctl start bluetooth.service`

### 2.7 声音
[https://wiki.archlinuxcn.org/wiki/PulseAudio](https://wiki.archlinuxcn.org/wiki/PulseAudio){target=_blank}

- `sudo pacman -S pulseaudio`
- `systemctl --user enable pulseaudio --now`
- `sudo pacman -S alsa-utils alsa-plugins volumeicon`
- `sudo pacman -S pulseaudio-bluetooth blueman`

### 2.8 字体大小
[https://wiki.archlinuxcn.org/wiki/Xrandr](https://wiki.archlinuxcn.org/wiki/Xrandr){target=_blank}

- `vim ~/.xprofile`
- `xrandr --dpi 192 &`

### 2.9 系统主题
- `pacman -S arc-icon-theme adwaita-icon-theme capitaine-cursors papirus-icon-theme arc-gtk-theme lxappearance`

## 三、软件配置

???+ "vim ~/.config/i3/config"

    ``` shell
    # 网络托盘，对应network-manager-applet包
    exec --no-startup-id nm-applet
    # 终端透明，对应picom包
    exec --no-startup-id picom -b
    # 设置壁纸，对应feh包
    exec --no-startup-id feh --randomize --bg-fill /home/shafish/Pictures/wallpaper
    # 启动fcitx5，输入法用
    exec --no-startup-id fcitx5 -d
    # 蓝牙托盘
    exec --no-startup-id blueman-applet
    # 启动polybar
    exec --no-startup-id $HOME/.config/polybar/launch.sh
    # 设置开机色温 redshift
    exec --no-startup-id redshift -P -O 4000
    # 设置启动器快捷键 rofi
    bindsym $mod+d exec "rofi -combi-modi window,drun,ssh -show combi"
    # 截图保存到指定目录
    bindsym Print exec --no-startup-id maim "/home/$USER/Pictures/$(date)"
    bindsym $mod+Print exec --no-startup-id maim --window $(xdotool getactivewindow) "/home/$USER/Pictures/$(date)"
    bindsym Shift+Print exec --no-startup-id maim --select "/home/$USER/Pictures/$(date)"
    # 截图保存到粘贴板
    bindsym Ctrl+Print exec --no-startup-id maim | xclip -selection clipboard -t image/png
    bindsym Ctrl+$mod+Print exec --no-startup-id maim --window $(xdotool getactivewindow) | xclip -selection clipboard -t image/png
    bindsym Ctrl+Shift+Print exec --no-startup-id maim --select | xclip -selection clipboard -t image/png
    ```

### 3.1 启动器（window系统的window键）
- `sudo pacman -S rofi`

### 3.2 壁纸、终端透明、装B
- `sudo pacman -S feh picom neofetch`

### 3.3 显卡驱动
[https://wiki.archlinuxcn.org/wiki/NVIDIA](https://wiki.archlinuxcn.org/wiki/NVIDIA){target=_blank}

???+ "之前安装的是linux-zen内核，注意这里是安装nvidia-dkms"

    - `sudo pacman -S linux-zen-headers nvidia-dkms nvidia-settings`
    - `vim /etc/mkinitcpio.conf`
        - 删去HOOKS那一项中的kms，阻止内核启动时加载nouveau
    - `sudo mkinitcpio -P`
    - `reboot`
    - `nvidia-smi`

### 3.4 输入法
[https://wiki.archlinuxcn.org/wiki/Fcitx5](https://wiki.archlinuxcn.org/wiki/Fcitx5){target=_blank}

???+ "安装fcitx5-input-support，就别手动配/etc/environment了"

    - `sudo pacman -S fcitx5-im fcitx5-rime fcitx5-chinese-addons rime-luna-pinyin rime-emoji`
    - `yay -S fcitx5-input-support`

### 3.5 polybar
[https://github.com/polybar/polybar-scripts](https://github.com/polybar/polybar-scripts){target=_blank}

#### redshift
[https://github.com/VineshReddy/polybar-redshift](https://github.com/VineshReddy/polybar-redshift){target=_blank}

#### usb
[https://github.com/polybar/polybar-scripts/tree/master/polybar-scripts/system-usb-udev](https://github.com/polybar/polybar-scripts/tree/master/polybar-scripts/system-usb-udev){target=_blank}

### 3.6 qq
`yay -S linuxqq`

???+ "每次qq接收消息都会闪退问题"

    - 环境：archlinux+i3wm
    - 问题：每次qq接收消息都会闪退
    - 解决：
        - 用终端输入linuxqq运行，闪退时查看运行log（鼠标滚轮滚了几十秒才看到在开头报了error）
        - 报错信息：[29188:1027/223745.914909:ERROR:libnotify_notification.cc(49)] notify_notification_show: domain=2117 code=2 message="GDBus.Error:org.freedesktop.DBus.Error.ServiceUnknown: The name org.freedesktop.Notifications was not provided by any .service files"
        - 谷歌搜这个报错，找到https://wiki.archlinux.org/title/Desktop_notifications#Standalone，完美解决

### 3.7 浏览器
- `sudo pacman -S brave firefox firefox-extension-arch-search`

### 3.8 vscode
- `yay -S visual-studio-code-bin`

### 3.9 深度图片查看器、截图、文件管理器
- `sudo pacman -S deepin-image-viewer deepin-screenshot deepin-file-manager ranger`

还是thunar好用

### 3.10 docker
- `sudo pacman -S docker docker-compose`
- `sudo systemctl enable docker`
- `sudo systemctl start docker`
- `usermod -aG docker shafish`
- `sudo systemctl restart docker`

### 3.11 stable-diffusion
- [https://github.com/pyenv/pyenv#set-up-your-shell-environment-for-pyenv](https://github.com/pyenv/pyenv#set-up-your-shell-environment-for-pyenv){target=_blank}
- [https://github.com/pyenv/pyenv-virtualenv#installing-as-a-pyenv-plugin](https://github.com/pyenv/pyenv-virtualenv#installing-as-a-pyenv-plugin){target=_blank}
- [https://github.com/AUTOMATIC1111/stable-diffusion-webui/wiki](https://github.com/AUTOMATIC1111/stable-diffusion-webui/wiki){target=_blank}

???+ "当然是拿来搞stable-diffusion"

    - `sudo pacman -S pyenv pyenv-virtualenv`
        - 配置fish环境：`vim ~/.config/fish/config.fish`
        ``` shell
        pyenv init - | source
        status --is-interactive; and pyenv virtualenv-init - | source
        ```
    - `pyenv install -v 3.10.6`
    - `pyenv virtualenv 3.10.6 sdwebui`
    - `cd ~/Project/Python`
    - `git clone https://github.com/AUTOMATIC1111/stable-diffusion-webui.git`
    - `cd stable-diffusion-webui`
    - `pyenv local sdwebui`
    - `python -V`
    - `./webui.sh --xformers` # 自行配置代理

### 3.12 轻量截图工具

- `sudo pacman -S maim xclip`

### 3.13 idea

- 官网下载

### 3.14 java

- 官网下载

### 3.15 数据库管理
sudo pacman -S dbeaver

### 3.15 手机投屏
手机启动无线调试

``` shell
sudo pacman -S scrcpy adb
adb tcpip 5555
adb connect 192.168.0.5:5555
```

### 3.16 git客户端
``` shell
sudo pacman -S lazygit
```

### 3.17 goldendict
[https://shafish.cn/english/#%E4%B8%80goldendict%E9%85%8D%E7%BD%AE](https://shafish.cn/english/#%E4%B8%80goldendict%E9%85%8D%E7%BD%AE){target=_blank}


``` shell
sudo pacman -S dictd
yay -S goldendict-ng dict-gcide aspeak 
```

sudo systemctl enable dictd.service
sudo systemctl start dictd.service

### 3.18 图片压缩
sudo pacman -S pngquant zopfli

``` shell
#!/bin/sh

# Use pngquant and zopflipng to compress png images
# The final result is similar to tinypng.com

file_seed="$(date +%s)"
pngquant --speed 1 --strip --verbose $1 -o temp_$file_seed.png
echo "========= zopflipng compressing ========= "
zopflipng -m --lossy_transparent temp_$file_seed.png compressed_$file_seed.png
echo "Deleting temp file..."
rm temp_$file_seed.png
echo "Done."
```

待续

*[BalenaEtcher]: Linux系统下建议的写盘工具
*[Rufus]: Window系统下建议的写盘工具
*[Archlinux镜像]: 选China下载