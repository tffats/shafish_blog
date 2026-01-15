---
title: Ansible使用
description: ansible、运维
hide:
  - navigation
tags:
  - ansible
bcs:
  "分类": "/tags/#tag:ansible"  
---

一、核心概念
- 1. **Inventory**：定义了 Ansible 需要管理的所有主机列表。
- 2. **Modules**：执行任务时使用的具体功能单元，每个模块都封装了一个具体的操作能力。`ping` 模块测试连接，`yum` 模块安装软件包，`template` 模块渲染模板文件
- 3. **Playbook**：是一个 YAML 文件，它定义了整个自动化流程，告诉 Ansible 先做什么、后做什么，对哪些服务器（Inventory 中的组）执行一系列任务（Tasks）。
- 4. **Task**：是 Playbook 中的一个最小执行单元，调用一个模块来完成一个具体的动作。
- 5. **Variable**：用于存储值，使 Playbook 更加灵活和可重用。
- 6. **Facts**：Ansible 自动收集的关于被管理节点的信息，如操作系统版本、IP 地址、CPU 架构等。
- 7. **Handler**：通常用于重启服务。比如，修改了 Nginx 配置文件后，只有当配置文件内容真的发生了变化，才触发重启 Nginx 服务的 Handler，避免不必要的服务重启。
- 8. **Role**：一种组织 Playbook、变量、文件、模板等资源的目录结构，将复杂的功能（如部署一套完整的 Web 应用）打包起来，使其可重用、更易于管理和维护。

二、安装并配置（mac）

``` shell
brew install ansible
```

``` shell title="/opt/homebrew/etc/ansible/hosts"
[tj_servers]
47.xx.xx.xx
74.xx.xx.xx
142.xx.xx.xx
107.xx.xx.xx

[tj9]
47.xx.xx.xx

[tj10]
74.xx.xx.xx

[tj11]
142.xx.xx.xx

[tj12]
107.xx.xx.xx

[shafish]
119.xx.xx.xx

[all:vars]
ansible_ssh_private_key_file=~/.ssh/id_rsa
ansible_python_interpreter=/usr/bin/python3
ansible_user=root
```

``` shell title="/opt/homebrew/etc/ansible/ansible.cfg"
[defaults]
# 指定 inventory 文件位置 (虽然默认会在同目录找 hosts，但明确指定更好)
inventory = /opt/homebrew/etc/ansible/hosts
# 设置日志路径
log_path = /tmp/ansible.log
# 设置远程连接的超时时间
timeout = 30
# 设置远程用户 (如果在 inventory 中未指定)
# remote_user = myuser
# 禁用首次连接时的主机密钥检查 (仅在测试环境中使用，生产环境不推荐)
# host_key_checking = False
[inventory]
# enable plugins, default: 'host_list', 'script', 'auto', 'yaml', 'ini', 'toml'
enable_plugins = host_list, script, auto, yaml, ini, toml
```

``` shell
ansible --version
# 列出所有主机
ansible all --list-hosts
# 测试
ansible tj9 -m ping
```

三、