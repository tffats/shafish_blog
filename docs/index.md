---
title: "格莱姆的日常"
# template: no_comment.html
hide:
  - navigation
  - toc
---

???+ "计划"

    - [ ] 源码

???+ "小工具"

    - [轻量前端渲染框架Dot.js](newBlog/posts/dot.md)

???+ "Linux"

    - [如何在 Linux 中实时监控日志文件](https://linux.cn/article-13733-1.html){target=_blank}
    - [Linux命令-持续](newBlog/posts/linux_note.md)
    - [Archlinux+I3WM （荐）](newBlog/posts/i3-user-guide.md)
    - [PVE](newBlog/posts/pve.md)

???+ "收藏夹"

    - [笔记收藏](https://note.shafish.cn/share/dtyo3K8tZLDZ){target=_blank}
    - [i3wm在线配置效果](https://thomashunter.name/i3-configurator/){target=_blank}
    - [mkdocs material使用记录](newBlog/posts/mkdocs.md)
    - [docker记录-持续](newBlog/posts/docker_note.md)


???+ "Kafka"
    
    - [Kafka回顾](kafka/readme.md)
    - [Kafka手册.md](kafka/training.md)
    - [Kafka参数使用记录.md](kafka/properties.md)
    - [Kafka脚本使用记录.md](kafka/shell.md)

??? quote "自构建"

    ``` yaml
    # 本地测试用
    git clone https://github.com/squidfunk/mkdocs-material.git
    cd mkdocs-material
    # 写入需要的插件
    cat > user-requirements.txt <<EOF
    mkdocs-git-revision-date-localized-plugin
    mkdocs-rss-plugin
    EOF

    docker build -t shafish/mkdocs-material:1.2 .

    cd /home/shafish/Note
    git clone https://github.com/tffats/shafish_blog.git
    docker run --rm -it -p 8000:8000 -v /home/shafish/Note/shafish_blog:/docs shafish/mkdocs-material:1.2
    ```

!!! success "docker镜像[^1]"

    `docker run --rm -it -p 8000:8000 -v /home/shafish/Note/shafish_blog:/docs shafish/mkdocs-material:1.2`

[^1]: [https://hub.docker.com/u/shafish](https://hub.docker.com/u/shafish){target="_blank"}