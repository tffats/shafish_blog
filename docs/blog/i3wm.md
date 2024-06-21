---
title: i3wm使用记录
tags:
  - linux
hide:
  - navigation
---

# i3wm使用记录

[Back](index.md/#2021年文章导航){ .md-button}

[https://wiki.archlinux.org/title/I3_(%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87)](https://wiki.archlinux.org/title/I3_(%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87)){target=_blank}

[https://i3wm.org/docs/userguide.html](https://i3wm.org/docs/userguide.html){target=_blank}

## 一、使用i3status-rust

ref: [https://github.com/greshake/i3status-rust](https://github.com/greshake/i3status-rust){target=_blank}

![](https://picture.cdn.shafish.cn/blog/i3wm-i3status-rust.png){: .zoom}

- 1.安装

``` shell
sudo pacman -Syu i3status-rust
```

- 2.配置

``` shell
# 更新下搜索库数据
updatedb
# 定位i3status-rust安装目录和配置文件
locate i3status
# 创建配置文件
sudo mkdir /etc/i3status-rust
sudo cp /usr/share/doc/i3status-rust/examples/config.toml /etc/i3status-rust
```

``` title="locate 结果"
/etc/i3status.conf
/usr/bin/adjust_i3statusconf
/usr/bin/i3status
/usr/bin/i3status-rs
/usr/share/i3status-rust
/usr/share/doc/i3status-rust
/usr/share/doc/i3status-rust/examples
/usr/share/doc/i3status-rust/examples/config.toml
/usr/share/doc/i3status-rust/examples/config_icon_override.toml
/usr/share/doc/i3status-rust/examples/config_theme_override.toml
...
```

- 3.接入i3bar

``` shell
# 修改bar
vim ~/i3/config
# 主要添加 status_command path/i3status-rs path/config.toml
# path根据上面locate定位得到
```

``` config title="~/i3/config"
# Start i3bar to display a workspace bar (plus the system information i3status if available)
bar {
	font pango:DejaVu Sans Mono, FontAwesome 12
	status_command /usr/bin/i3status-rs /etc/i3status-rust/config.toml
	#status_command i3status
	#status_command exec /home/shafish/.i3/net-speed.sh
	position bottom

## please set your primary output first. Example: 'xrandr --output eDP1 --primary'
#	tray_output primary
#	tray_output eDP1

	bindsym button4 nop
	bindsym button5 nop
#   font xft:URWGothic-Book 11
	strip_workspace_numbers yes
	colors {
        	separator #666666
       		background #222222
        	statusline #dddddd
       		focused_workspace #0088CC #0088CC #ffffff
        	active_workspace #333333 #333333 #ffffff
        	inactive_workspace #333333 #333333 #888888
        	urgent_workspace #2f343a #900000 #ffffff
	}
    #colors {
        #background #222D31
        #statusline #F9FAF9
        #separator  #454947
        #border  backgr. text
        #focused_workspace  #F9FAF9 #16a085 #292F34
        #active_workspace   #595B5B #353836 #FDF6E3
        #inactive_workspace #595B5B #222D31 #EEE8D5
        #binding_mode       #16a085 #2C2C2C #F9FAF9
        #urgent_workspace   #16a085 #FDF6E3 #E5201D
    #}
}
```

- 4.i3 reload

`win+shift+r`

- 5.block配置

blocks使用文档：[https://github.com/greshake/i3status-rust/blob/master/doc/blocks.md#formatting)](https://github.com/greshake/i3status-rust/blob/master/doc/blocks.md#formatting){target=_blank}

图标：
``` 
https://wiki.archlinux.org/title/i3_(%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87)#%E7%8A%B6%E6%80%81%E6%A0%8F%E4%B8%AD%E7%9A%84%E5%9B%BE%E6%A0%87%E5%AD%97%E4%BD%93
```

``` config
theme = "solarized-dark"
icons = "awesome"

#[[block]]
#block = "speedtest"
#interval = 1800
#format = "{ping}{speed_down:4*B}{speed_up:4*B}"

[[block]]
block = "net"
device = "wlp1s0"
format = "{ssid} {speed_down;K*b} {speed_up:K*b}"
interval = 1

[[block]]
block = "disk_space"
path = "/"
info_type = "available"
alert = 10.0
format = "{icon}/{available}"

[[block]]
block = "disk_space"
path = "/home"
info_type = "available"
warning = 50.0
alert = 20.0
format = "{icon}/home {available}"

[[block]]
block = "memory"
format_mem = "{mem_used}"
#format_swap = "{swap_used}/{swap_total}({swap_used_percents})"
display_type = "memory"
icons = true
clickable = false
interval = 5
warning_mem = 60
#warning_swap = 80
critical_mem = 90
#critical_swap = 95

[[block]]
block = "cpu"
interval = 1
warning = 60
critical = 90

[[block]]
block = "load"
interval = 1
format = "{1m}"

[[block]]
block = "battery"
interval = 10
format = "{percentage}"

[[block]]
block = "time"
interval = 1
format = "%a %m-%d %H:%M:%S"
```
