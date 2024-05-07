---
title: Git客户端记录
description: lazygit 使用说明书
icon: material/emoticon-happy
#status: new
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](/)


## lazygit基本了解

`lazygit` 是一个命令行界面的 `Git` 客户端工具，其提供了一个直观的交互式界面，使用户能够更轻松地浏览、提交、分支、合并等操作，旨在帮助简化和加速 `Git` 工作流程。

lazygit将git划分为5大面板，并且通过字母绑定的方式对git命令进行了简化，下面简单了解下 `lazygit` 的页面布局：

![页面布局](../image/compressed_1714831380.png){: .zoom}

页面左侧分别是：

| 面板      | 描述 | 面板切换按钮     |
| :---:        |    :----:   |          :---: |
| 状态面板      | 显示仓库当前分支       | 1   |
| 文件面板   | 显示当前修改的文件（A表示已经添加到暂存区，？？表示新加的文件）        | 2      |
| 分支面板   | 显示仓库分支情况        | 3      |
| 提交面板   | 显示仓库提交记录        | 4      |
| 储藏面板   | stash管理        | 5      |

ps：除了按数字1～5进行面板切换外，还可以用左右键/hl键进行切换。按[]即可切换同面板内的tap选项;按？即可查看全部快捷键使用

<!-- more -->

## 使用配置

[https://github.com/jesseduffield/lazygit/blob/master/docs/Config.md](https://github.com/jesseduffield/lazygit/blob/master/docs/Config.md){target=_blank}

## 使用快捷键

[https://github.com/jesseduffield/lazygit/blob/master/docs/keybindings/Keybindings_en.md](https://github.com/jesseduffield/lazygit/blob/master/docs/keybindings/Keybindings_en.md){target=_blank}

| lazygit快捷键      | 对于git命令 | 备注     |
| :---:        |    :----:   |          :---: |
| A      | git add -A       | 添加本地全部文件   |
| c   | git commit -m ''        |  会弹出编辑框，标题填好后回车即可      |
| P   | git push        |   大写的P    |
| p   | git pull        | 小写的p      |
| space(文件面板-未暂存)   | git add xxx        | 文件面板选中某个未暂存文件后按空格键即可添加到暂存      |
| space(文件面板-已暂存)   | git rm --cached xxx        | 文件面板选中某个已暂存文件后按空格键即可删除暂存      |
| i(文件面板)   | 添加到.gitignore        |       |
| n(分支面板)   | git checkout -b xxx        | 分支面板中选中某分支按n即可      |
| space(分支面板)   | git checkout xxx        | 分支面板选中某个分支后按空格键即可切换特定分支      |
| M(分支面板)   | git merge xxx        |   （A合并到B）先切换到A分支，再选中B分支，按M键合并即可    |
| R(分支面板)   | git branch --move 旧分支名 新分支名        |   修改分支名称    |
| g(分支面板)   | git branch --move 旧分支名 新分支名        |   修改分支名称    |
| ctrl+o(分支面板)   | 复制提交hash到粘贴板        |      |

http://zhk.me/1366.html

https://juejin.cn/post/7176182782035492923

## 使用示例


