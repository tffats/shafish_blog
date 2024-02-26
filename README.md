[https://shafish.cn](https://shafish.cn) 博客代码

### docker部署
``` yml
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