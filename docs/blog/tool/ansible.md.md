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
- 2. **Modules**：执行任务时使用的具体功能单元。`ping` 模块测试连接，`yum` 模块安装软件包，`template` 模块渲染模板文件
- 3. **Playbook**：使用 YAML 格式编写的文件。定义了一系列任务，告诉 Ansible 要做什么以及怎么做
- 4. **Task**：Playbook 中的一个具体操作步骤。
- 5. **Variable**：用于存储值，使 Playbook 更加灵活和可重用。
- 6. **Facts**：Ansible 自动收集的关于被管理节点的信息，如操作系统版本、IP 地址、CPU 架构等。
- 7. **Handler**：特殊的任务，通常用于重启服务
- 8. **Role**：一种组织 Playbook 和相关文件（如变量、模板、任务等）的方式，使得代码更模块化、可重用。

二、安装

``` shell
brew install ansible
```

三、