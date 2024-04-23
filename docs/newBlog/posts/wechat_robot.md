---
title: 一个简单的微信机器人
authors:
    - shafish
date:
    created: 2024-03-11
draft: true    
categories:
    - 微信机器人
    - python
---

## 想法

因为本人是qq的重度玩家（window上的截图软件都是qq 笑*），而且一直有用qq做日常记录的习惯，比如日常看到的一些博文或者拿来保存一些手机截图、文件等等。但是！！qq现在我的电脑都只是提供互传功能，并没有记录查询这些玩意，就有很多以前分享过的东西不翻翻就忘了或者记得是分享过一个什么玩意，但翻记录就是翻不出来！ 无奈*

问题一直都在，想着到时再说，恰巧去年折腾博客的时候弄了一个笔记系统，也就是trilium，它功能非常丰富，但当时也是一时热度，用了几周就没继续，主要是平时长点的记录用现在的博客，一些零碎的也不会特意打开trilium网站记录，至于用trilium手机端记录什么的也没那么好体验，要用小屏做笔记，系统自带的笔记软件是最好用的。

现在只要把qq消息记录到trilium这流程打通，一切就很舒服了！

<!-- more -->

## 技术调研

网上资料很多，不过首先就排除了用官方机器人：限制太多，不够灵活，不适合个人用。

不查不知道，网上一堆的机器人框架技术，都是在跟官方斗智斗勇。最终一圈下来选了个保险点的开源微信机器人：[wxauto](https://github.com/cluic/wxauto){target=_blank} 啪啪打脸*

下面是能用的框架：
- [QQ-mirai](https://github.com/mamoe/mirai){target=_blank}
- [QQ-nonebot](https://github.com/nonebot/nonebot){target=_blank}
- [微信-wxauto](https://github.com/cluic/wxauto){target=_blank}
- [微信-XYBot](https://github.com/HenryXiaoYang/XYBot.git){target=_blank}

