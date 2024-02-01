---
title: "格莱姆的日常"
template: no_comment.html
hide:
  - navigation
  - toc
---

??? quote "自构建"

    ``` yaml
    # 本地测试用
    git clone https://github.com/squidfunk/mkdocs-material.git
    cd mkdocs-material
    # 写入需要的插件
    echo "mkdocs-git-revision-date-localized-plugin" > user-requirements.txt

    docker build -t shafish/mkdocs-material:1.1 .

    cd /home/shafish/Note
    git clone https://github.com/tffats/shafish_blog.git
    docker run --rm -it -p 8000:8000 -v /home/shafish/Note/shafish_blog:/docs squidfunk/mkdocs-material
    ```

!!! success "docker镜像[^1]"

    `docker run --rm -it -p 8000:8000 -v /home/shafish/Note/shafish_blog:/docs shafish/mkdocs-material:1.2`

[^1]: [https://hub.docker.com/u/shafish](https://hub.docker.com/u/shafish){target="_blank"}