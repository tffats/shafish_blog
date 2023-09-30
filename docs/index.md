---
title: "格莱姆的日常"
template: no_comment.html
hide:
  - navigation
  - toc
---

``` yaml
# 本地测试用
git clone https://github.com/squidfunk/mkdocs-material.git
cd mkdocs-material
echo "mkdocs-git-revision-date-localized-plugin" > user-requirements.txt

docker pull squidfunk/mkdocs-material
docker build -t squidfunk/mkdocs-material .

cd /home/shafish/Note
git clone https://github.com/tffats/shafish_blog.git
docker run --rm -it -p 8000:8000 -v /home/shafish/Note/shafish_blog:/docs squidfunk/mkdocs-material
```