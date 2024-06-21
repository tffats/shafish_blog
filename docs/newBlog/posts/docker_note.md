---
title: 容器日常
authors:
    - shafish
date:
    created: 2022-07-10
    updated: 2023-10-25
categories:
    - docker
    - linux
---

[ :fishing_pole_and_fish: ](/)

!!! note "shafish docker hub"
    [https://hub.docker.com/u/shafish](https://hub.docker.com/u/shafish){target="_blank"}

## 零、安裝
> 或者直接安装官网安装：[https://docs.docker.com/engine/](https://docs.docker.com/engine/){target="_blank"}

``` shell
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

``` shell title="archlinux"
# https://wiki.archlinux.org/title/Docker

sudo pacman -S docker docker-compose
yay -S docker-rootless-extras

sudo echo "your_username:165536:65536" > /etc/subuid
sudo echo "your_username:165536:65536" > /etc/subgid

sudo systemctl enable docker
sudo systemctl start docker

export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/docker.sock
```

<!-- more -->

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

## 四、OpenSumi
``` shell
docker run --name opensumi -d -p 18000:8000/tcp --restart=unless-stopped -v /opt/docker/opensumi/workspace:/workspace -v /opt/docker/opensumi/extensions:/extensions -v /opt/docker/opensumi/configs:/configs ghcr.io/opensumi/opensumi-web:latest
```

## 五、trilium
``` shell
docker run -d --name trilium -p 8099:8080 -v /home/ubuntu/docker/trilium-data:/home/node/trilium-data zadam/trilium:latest
```

``` shell title="docker-compose.yml"
version: '3'
services:
  trilium-cn:
    image: nriver/trilium-cn
    restart: always
    ports:
      - "8088:8080"
    volumes:
      # 把同文件夹下的 trilium-data 目录映射到容器内
      - ./trilium-data:/root/trilium-data
    environment:
      # 环境变量表示容器内笔记数据的存储路径
      - TRILIUM_DATA_DIR=/root/trilium-data
```

``` shell title="升级wss协议"
location /
{
  ...
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection 'upgrade';
  ...
}
```

## 六、aria2下载
```
docker run -d \
    --name aria2-pro-normal \
    --restart unless-stopped \
    --log-opt max-size=1m \
    -e UMASK_SET=022 \
    -e PUID=$UID \
    -e PGID=$GID \
    -e RPC_SECRET=shafish \
    -e RPC_PORT=6800 \
    -p 6800:6800 \
    -e LISTEN_PORT=6888 \
    -p 6888:6888 \
    -p 6888:6888/udp \
    -e SPECIAL_MODE=move \
    -v /data/docker/aria2-normal-download/config:/config \
    -v /mnt/docker-hgst/data/minio/data/download:/downloads/completed \
    -v /mnt/docker-hgst/data/minio/tem:/downloads \
    p3terx/aria2-pro:latest
```
``` shell title="docker-compose.yml"
version: '3.3'
services:
    aria2-pro:
        container_name: aria2-pro
        restart: unless-stopped
        logging:
            options:
                max-size: 100m
        environment:
            - UMASK_SET=022
            - PUID=1000
            - PGID=1000
            - RPC_SECRET=shafish
            - RPC_PORT=6800
            - LISTEN_PORT=6888
            - SPECIAL_MODE=move
        ports:
            - '6800:6800'
            - '6888:6888'
            - '6888:6888/udp'
        volumes:
            - '/home/shafish/Data/docker/aria2/config:/config'
            - '/home/shafish/Data/docker/aria2/download:/downloads/completed'
        image: 'p3terx/aria2-pro:latest'
```
nano /mnt/docker-hgst/data/docker/aria2/config/script.conf dest-dir to /completed

```
docker run -d \
    --name ariang \
    --log-opt max-size=1m \
    --restart unless-stopped \
    -p 6880:6880 \
    p3terx/ariang
```
``` shell title="docker-compose.yml"
version: '3.3'
services:
    ariang:
        container_name: ariang
        logging:
            options:
                max-size: 100m
        restart: unless-stopped
        ports:
            - '6880:6880'
        image: p3terx/ariang
```

## 七、jellyfin
```
docker run -d \
  --name=jellyfin \
  -e PUID=$UID \
  -e PGID=$GID \
  -p 8096:8096 \
  -p 8920:8920 \
  -p 7359:7359/udp \
  -p 1900:1900/udp \
  -v /data/docker/jellyfin/config:/config \
  -v /mnt/docker-hgst/video:/data/video \
  --restart unless-stopped \
  nyanmisaka/jellyfin:latest
```

提示：Database is locked，把配置文件目录修改为host目录可临时解决

## 八、LXC安装docker问题
```
docker: Error response from daemon: using mount program fuse-overlayfs: fuse: device not found, try 'modprobe fuse' first
fuse-overlayfs: cannot mount: No such file or directory
: exit status 1.
```

```
I'll close this issue with a summary of the fix:

On the Fedora LXC HOST:

root@fedora# dnf install fuse-overlayfs
root@fedora# modprobe fuse
root@fedora# vi /var/lib/lxc/<Ubuntu-container-name>/config # On the Fedora LXC Host.

Add this line to the config file and save it:

lxc.mount.entry = /dev/fuse dev/fuse none bind,create=file,rw,uid=165536,gid=165536 0 0

Install this package on the Ubuntu LXC GUEST:

root@ubuntu# apt-get install fuse-overlayfs

and finally, reboot your Ubuntu container.

I hope this helps others.

You are right. Nesting and keyctl should be enabled: Datacenter -> YourNode -> YourLXC -> Option -> Features
```
解決：功能选项中勾选嵌套

## 九、redis
> redis官方配置文件：[https://github.com/redis/redis/blob/7.2/redis.conf](https://github.com/redis/redis/blob/7.2/redis.conf){target="_blank"}

``` shell title="docker-compose.yml"
version: '3.8'
services:
    redis:
        container_name: myredis
        restart: unless-stopped
        ports:
            - '6379:6379'
        volumes:
            - '/home/shafish/Data/docker/redis:/usr/local/etc/redis'
        command: redis-server /usr/local/etc/redis/redis.conf
        image: redis:7.2
```

需要注意把 `redis.conf` 中的 `daemonize` 改为 `no`

## 十、mysql

``` shell title="docker-compose.yml"
version: '3.3'
services:
    mysql:
        container_name: mysql8
        restart: unless-stopped
        ports:
            - '3306:3306'
            - '33060:33060'
        volumes:
            - '/data/docker/mysql/conf:/etc/mysql/conf.d'
            - '/data/docker/mysql/datadir:/var/lib/mysql'
            - '/data/docker/mysql/log:/var/log/mysql'
        environment:
            - MYSQL_ROOT_PASSWORD=shafish123456
            - TZ=Asia/Shanghai
        image: mysql:8.4.0
```

## 十一、mongo

``` shell title="docker-compose.yml"
version: '3.3'
services:
    mongo:
        container_name: mongo
        ports:
            - '27017:27017'
        restart: unless-stopped
        volumes:
            - '/data/docker/mongo/db:/data/db'
            - '/data/docker/mongo/conf:/etc/mongo/mongo.conf'
        environment:
            - MONGODB_INITDB_ROOT_USERNAME=shafish
            - MONGODB_INITDB_ROOT_PASSWORD=shafish123456
        image: 'mongo:latest'
```

## 十二、minio

``` shell title="docker-compose.yml"
version: '3.3'
services:
    data:
        container_name: minio
        restart: unless-stopped
        ports:
            - '9001:9000'
            - '9091:9090'
        volumes:
            - '/mnt/docker-hgst/data/minio/data:/data'
            - '/data/docker/minio/config:/root/.minio'
        environment:
            - MINIO_SITE_NAME=minio-shafish
            - MINIO_ROOT_USER=shafish
            - MINIO_ROOT_PASSWORD=shafish123
            - MINIO_SERVER_URL=http://192.168.0.151:9091 # api
            - MINIO_BROWSER_REDIRECT_URL=http://192.168.0.151:9091
        command: server /data  --console-address ":9000" --address ":9090"
        healthcheck:
            test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
            interval: 30s
            timeout: 20s
            retries: 3
        image: quay.io/minio/minio
```

## 十三、slashbase

``` shell
mkdir slashbase && cd slashbase
curl -x socks5://127.0.0.1:1080 --location --output install.sh https://raw.githubusercontent.com/slashbaseide/slashbase/main/deploy/install.sh
chmod +x install.sh
./install.sh
```

## 十四、portainer

``` shell title="docker-compose.yml"
version: '3.3'
services:
    portainer-ce:
        ports:
            - '9000:9000'
        container_name: portainer
        restart: always
        volumes:
            - '/var/run/docker.sock:/var/run/docker.sock'
            - '/home/shafish/Data/docker/portainer/data:/data'
        image: 'portainer/portainer-ce:latest'
```

## 十五、mattermost
``` shell
docker run --name mattermost-preview-cp \
  --publish 8065:8065 \
  --add-host dockerhost:127.0.0.1 \
  --rm \
  -d mattermost/mattermost-preview

# 复制配置文件
docker container cp mattermost-preview-cp:/mm/mattermost/config /data/docker/mattermost/config/  
```

``` shell title="docker-compose.yml"
version: '3.3'
services:
    mattermost:
        container_name: mattermost-preview
        restart: unless-stopped
        ports:
            - '8065:8065'
        volumes:
            - '/data/docker/mattermost/config:/mm/mattermost/config'
            - '/data/docker/mattermost/mysql:/var/lib/mysql'
            - '/data/docker/mattermost/data:/mm/mattermost-data'
        image: mattermost/mattermost-preview
```

> 更多操作：[https://github.com/shafishcn/ToolMan/tree/master/docker](https://github.com/shafishcn/ToolMan/tree/master/docker){target=_blank}

## 十六、drawdb

``` shell
git clone https://github.com/drawdb-io/drawdb
docker build -t drawdb .
docker run -p 3000:80 drawdb
```