---
title: About
vssue: ""
hide:
  - navigation
  - toc
---

## 扯乎

:bell:这配色，精心挑选，匠心制作，没毛病铁子:bell:

终于正式使用静态博客啦 :sparkler:  来一起嗨起来:tada: :tada: :tada:

??? quote "ignore it"

    {--之前一直在用typecho，它的后台管理、文章发布什么的也用习惯了，在动态博客中确实是轻量级别的好系统。--}

    {--但系捏，这些系统始终有个缺点：版本升级需要留意一系列问题。也说不上缺点，主要是很多动态博客系统都可以用第三方主题，系统更新了新版本框架，好家伙，用的主题就得重新搞适配。--}

    {--有人就说了（并没有）：你不更新框架不就好了嘛，那是你还没遇到服务器过期，续费还老贵的情况，不差钱的直接续下费，啥事都不用折腾（主要还是多少有点强迫症）。--}

    {--去隔壁厂弄个新号做点微薄贡献，这一弄，好家伙，想着都换服务器勒，不neng latest lts版本有点说不过去，上了最新系统框架，结果用的主题没适配（果然主题不能光好看啊），当时光留意备份文章数据，主题设置还忘记了备份。想着问题也不算大，自己动手适配主题应该也还行，下班后学了几天php，nginx+fpm环境搭起来、语义语法过一遍，翻了翻主题代码，好了直接萎了，看不懂，没法下手。没必要，确实没必要。--}

    {--最后本着多学习学习前端zhishi的态度（bing bu shi），在docsify和mkdocs中选了后者，文档也比较全面，一天时间搭好还写了使用文档弄了github action。总之整挺好，不出意外会一直用下去。--}

之前的typecho还保留着，不过用了默认的主题，旧主题中设置的很多样式没解析出来，后续处理下失效的图片链接就这样了，闲的无聊欢迎去康康哟：[https://blog.shafish.cn/](https://blog.shafish.cn/){target=_blank}

![附上一张江湖流传出来某人平平无奇的普通长发帅图](https://picture.cdn.shafish.cn/blog/head.png){align=right width="300" }

至于本人介绍：只是一个平平无奇的骚年而已

## 问题

- [x] 评论出现Failed to load comments，{==疑似shafish.cn及tffats.github.io/shafish_blog使用同仓库issue造成（待排查）==}
    - [x] 评论vssue options中添加`admins: ['shafishcn']`解决。
    - [x] 评语：不要根据经验理所当然（你也没多少这方面的经验，好吧），先去项目issue里找找相似问题再说。另外，国内开源项目注意中英关键字问题，Failed to load comments翻译叫`评论加载失败`。

## Todo-博客改造

- 2021-12
    - 25/26
        - [x] 优化ci流程（利用github action构建好内容后同步到服务器，避免服务器clone仓库再重复构建，减轻服务器压力）
        - [x] 添加archvie.shafish.cn版权说明
        - [ ] 屏蔽archvie.shafish.cn无用户访问
        - [x] 博客添加video.js
        - [x] 修复文件创建、修改显示时间
        - [x] 添加图床
        - [x] 博客图片可点击放大
        - [x] 仓库移交tffats管理
        - [x] 设置shafish.cn跟tffats.github.io/shafish_blog同步评论
        - [ ] 添加shafish_blog合并到main时code review流程
