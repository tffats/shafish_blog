---
title: 容器日常
description: linux, docker
hide:
  - navigation
---

## 一、drawio
``` shell
# 离线：http://xxxx:10180/?offline=1
docker run -it -m1g -e LETS_ENCRYPT_ENABLED=true -e PUBLIC_DNS=drawio.example.com --rm --name="draw" -p 80:80 -p 443:8443 jgraph/drawio
```

## 二、ES单集群

- https://www.elastic.co/guide/en/elasticsearch/reference/7.17/docker.html
- https://www.elastic.co/guide/en/elasticsearch/reference/8.3/docker.html
- https://www.elastic.co/guide/en/kibana/8.3/docker.html
- https://www.elastic.co/guide/en/kibana/7.17/docker.html
- https://www.cnblogs.com/baoshu/p/16128127.html

### 8.3
``` shell
# 拉取es8.3.2镜像 
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.3.2
docker network create elastic

# 启动容器 8G+
docker run --name es01 --net elastic -p 9200:9200 -p 9300:9300 --restart=unless-stopped -e ES_JAVA_OPTS="-Xms1g -Xmx1g" -v /opt/docker/elasticsearch/data:/usr/share/elasticsearch/data -v /opt/docker/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d docker.elastic.co/elasticsearch/elasticsearch:8.3.2
# docker cp es01:/usr/share/elasticsearch/config/elasticsearch.yml ./config/
chmod -R 777 /opt/docker/elasticsearch/data
docker restart es01
# 证书连接
docker cp es01:/usr/share/elasticsearch/config/certs/http_ca.crt .
# 重置elastic密码 oI-X+cKUDBWkNpj*PgOX
docker exec -it es01 /usr/share/elasticsearch/bin/elasticsearch-reset-password -u elastic
# 访问
curl --cacert http_ca.crt -u elastic https://localhost:9200
```
### 7.17
``` shell
# 拉取es7.17.5镜像 
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.17.5
# 启动临时容器
docker run -d --name elasticsearch  -p 9200:9200 -p 9300:9300 -e  "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms256m -Xmx256m" docker.elastic.co/elasticsearch/elasticsearch:7.17.5
# 复制配置文件
mkdir -p /opt/docker/elasticsearch/7.17.5/{config,data,logs,plugins}
docker cp elasticsearch:/usr/share/elasticsearch/config /opt/docker/elasticsearch/7.17.5
docker cp elasticsearch:/usr/share/elasticsearch/logs /opt/docker/elasticsearch/7.17.5
docker cp elasticsearch:/usr/share/elasticsearch/data /opt/docker/elasticsearch/7.17.5
docker cp elasticsearch:/usr/share/elasticsearch/plugins /opt/docker/elasticsearch/7.17.5
# 销毁容器
docker stop elasticsearch
docker rm elasticsearch
# 运行 4G+
docker run -d --name elasticsearch7.17 \
-p 9200:9200 \
-p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms1g -Xmx1g" \
-v /opt/docker/elasticsearch/7.17.5/logs:/usr/share/elasticsearch/logs \
-v /opt/docker/elasticsearch/7.17.5/data:/usr/share/elasticsearch/data \
-v /opt/docker/elasticsearch/7.17.5/plugins:/usr/share/elasticsearch/plugins \
-v /opt/docker/elasticsearch/7.17.5/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
--restart=unless-stopped docker.elastic.co/elasticsearch/elasticsearch:7.17.5
# 重置elastic密码 oI-X+cKUDBWkNpj*PgOX
docker exec -it elasticsearch7.17 /usr/share/elasticsearch/bin/elasticsearch-setup-passwords interactive
# 查看加载的插件
docker exec -it elasticsearch7.17 /usr/share/elasticsearch/bin/elasticsearch-plugin list
```

## 三、kibana
### 8.3
``` shell
# 拉取kibana8.3.2镜像 
docker pull docker.elastic.co/kibana/kibana:8.3.2
# 启动
docker run --name kib-01 --net elastic -p 5601:5601 --restart=unless-stopped -d docker.elastic.co/kibana/kibana:8.3.2
# generate a new enrollment token
docker exec -it es01 /usr/share/elasticsearch/bin/elasticsearch-create-enrollment-token -s kibana
```
### 7.17
``` shell
# 拉取kibana7.17.5镜像 
docker pull docker.elastic.co/kibana/kibana:7.17.5
# 启动临时容器
docker run -d --name kibana -p 5601:5601 docker.elastic.co/kibana/kibana:7.17.5
# 复制配置文件
mkdir -p /opt/docker/kibana/7.17.5/config
docker cp kibana:/usr/share/kibana/config /opt/docker/kibana/7.17.5
# 销毁容器
docker stop kibana
docker rm kibana
# 运行
docker run -d --name kibana7.17 -p 5601:5601 -v /opt/docker/kibana/7.17.5/config:/usr/share/kibana/config --restart=unless-stopped docker.elastic.co/kibana/kibana:7.17.5
```

## elastic-head

## OpenSumi
``` shell
docker run --name opensumi -d -p 18000:8000/tcp --restart=unless-stopped -v /opt/docker/opensumi/workspace:/workspace -v /opt/docker/opensumi/extensions:/extensions -v /opt/docker/opensumi/configs:/configs ghcr.io/opensumi/opensumi-web:latest
```