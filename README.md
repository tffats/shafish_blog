[https://shafish.cn](https://shafish.cn) 博客代码

### docker部署
``` yml
# 本地测试用
git clone https://github.com/squidfunk/mkdocs-material.git
cd mkdocs-material
echo "mkdocs-git-revision-date-localized-plugin" > user-requirements.txt

docker pull squidfunk/mkdocs-material
docker build -t squidfunk/mkdocs-material .

docker run --rm -it -p 8000:8000 -v /home/shafish/Note/shafish_blog:/docs squidfunk/mkdocs-material
```