## 入门使用
``` dockerfile
# Use your favorite image
FROM ubuntu
ARG S6_OVERLAY_VERSION=3.1.6.2

RUN apt-get update && apt-get install -y nginx xz-utils
RUN echo "daemon off;" >> /etc/nginx/nginx.conf
CMD ["/usr/sbin/nginx"]

ADD https://github.com/just-containers/s6-overlay/releases/download/v${S6_OVERLAY_VERSION}/s6-overlay-noarch.tar.xz /tmp
RUN tar -C / -Jxpf /tmp/s6-overlay-noarch.tar.xz
ADD https://github.com/just-containers/s6-overlay/releases/download/v${S6_OVERLAY_VERSION}/s6-overlay-x86_64.tar.xz /tmp
RUN tar -C / -Jxpf /tmp/s6-overlay-x86_64.tar.xz
ENTRYPOINT ["/init"]
```
- docker build -t demo .
- docker run -rm --name s6demo -d -p 80:80 demo
- docker top s6demo acxf

export http_proxy="http://192.168.0.109:9001"
export https_proxy="https://192.168.0.109:9001"
--http-proxy="http://192.168.0.109:9001"
--https-proxy="http://192.168.0.109:9001"
--all-proxy=http://192.168.0.109:10001
--https-proxy=http://192.168.0.109:10001

HTTP_PROXY_URL=http://192.168.0.109:10001

unset http_proxy
unset https_proxy

            - PUID=0
            - PGID=0
            - RPC_SECRET=shafish
            - RPC_PORT=6800
            - LISTEN_PORT=6888
            - SPECIAL_MODE=move
            - DISK_CACHE=256M