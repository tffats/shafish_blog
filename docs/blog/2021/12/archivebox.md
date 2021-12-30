---
title: ArchiveBox使用
hide:
  - navigation
---

# ArchiveBox使用

[Back](/blog/#12月份){ .md-button}

ref: 

- [https://nixintel.info/osint-tools/make-your-own-internet-archive-with-archive-box/](https://nixintel.info/osint-tools/make-your-own-internet-archive-with-archive-box/){target=_blank}
- [https://github.com/ArchiveBox/ArchiveBox/wiki/Docker](https://github.com/ArchiveBox/ArchiveBox/wiki/Docker){target=_blank}

当你遇见他，感悟他，细心珍藏他，然后毫无疑问地，最终会遗忘了他，我说的是书签，浏览器上保存的那些成白上千的、杂乱的书签。

每次翻起都不自觉的瑟瑟发抖，不仅是忘了拿来干啥，主要还有很多经过岁月洗礼后已经GG咧，404，总会给你意外的惊喜，为啥是惊喜，因为你有了借口删掉它。:shit:

偶然在网上找到ArchiveBox，一个Open-source self-hosted web archiving（自建的内容存档？），是基于python的爬虫系统，可以把链接内容完整地保存到本地（包括文章文字、样式，甚至音视频），还提供了存档的管理后台。就非常银性化，很符合在下要求。:smirk: 那么，搞起：)

## 一、安装

推荐是用docker安装，因为方便，直接pip安装也很好，看个人习惯，翻翻上面官方链接，这里不多介绍了。
``` yaml title="docker-compose.yml"
# Usage:
#     docker-compose run archivebox init --setup
#     docker-compose up
#     echo "https://example.com" | docker-compose run archivebox archivebox add
#     docker-compose run archivebox add --depth=1 https://example.com/some/feed.rss
#     docker-compose run archivebox config --set PUBLIC_INDEX=True
#     docker-compose run archivebox help
# Documentation:
#     https://github.com/ArchiveBox/ArchiveBox/wiki/Docker#docker-compose

version: '2.4'

services:
    archivebox:
        # build: .                              # for developers working on archivebox
        image: ${DOCKER_IMAGE:-archivebox/archivebox:master}
        command: server --quick-init 0.0.0.0:8000
        ports:
            - 8800:8000
        environment:
            - ALLOWED_HOSTS=*                   # add any config options you want as env vars
            - MEDIA_MAX_SIZE=750m
            #- SAVE_WARC=False
            #- SAVE_PDF=False
            #- SAVE_SCREENSHOT=False
            #- SAVE_MEDIA=False
            - SAVE_ARCHIVE_DOT_ORG=False
            - TIMEOUT=1500
            - SEARCH_BACKEND_ENGINE=sonic     # uncomment these if you enable sonic below
            - SEARCH_BACKEND_HOST_NAME=sonic
            - SEARCH_BACKEND_PASSWORD=xxxxabc
        volumes:
            - ./data:/data
            # - ./archivebox:/app/archivebox    # for developers working on archivebox
    # To run the Sonic full-text search backend, first download the config file to sonic.cfg
    # curl -O https://raw.githubusercontent.com/ArchiveBox/ArchiveBox/master/etc/sonic.cfg
    # after starting, backfill any existing Snapshots into the index: docker-compose run archivebox update --index-only
    sonic:
        image: valeriansaliou/sonic:v1.3.0
        expose:
            - 1491
        environment:
            - SEARCH_BACKEND_PASSWORD=xxxxabc
        volumes:
            - ./sonic.cfg:/etc/sonic.cfg:ro
            - ./data/sonic:/var/lib/sonic/store

    ### Optional Addons: tweak these examples as needed for your specific use case

    # Example: Run scheduled imports in a docker instead of using cron on the
    # host machine, add tasks and see more info with archivebox schedule --help
    # scheduler:
    #    image: archivebox/archivebox:latest
    #    command: schedule --foreground --every=day --depth=1 'https://getpocket.com/users/USERNAME/feed/all'
    #    environment:
    #        - USE_COLOR=True
    #        - SHOW_PROGRESS=False
    #    volumes:
    #        - ./data:/data

    # Example: Put Nginx in front of the ArchiveBox server for SSL termination
    # nginx:
    #     image: nginx:alpine
    #     ports:
    #         - 443:443
    #         - 80:80
    #     volumes:
    #         - ./etc/nginx/nginx.conf:/etc/nginx/nginx.conf
    #         - ./data:/var/www
# Example: run all your ArchiveBox traffic through a WireGuard VPN tunnel
    # wireguard:
    #   image: linuxserver/wireguard
    #   network_mode: 'service:archivebox'
    #   cap_add:
    #     - NET_ADMIN
    #     - SYS_MODULE
    #   sysctls:
    #     - net.ipv4.conf.all.rp_filter=2
    #     - net.ipv4.conf.all.src_valid_mark=1
    #   volumes:
    #     - /lib/modules:/lib/modules
    #     - ./wireguard.conf:/config/wg0.conf:ro

    # Example: Run PYWB in parallel and auto-import WARCs from ArchiveBox
    # pywb:
    #     image: webrecorder/pywb:latest
    #     entrypoint: /bin/sh 'wb-manager add default /archivebox/archive/*/warc/*.warc.gz; wayback --proxy;'
    #     environment:
    #         - INIT_COLLECTION=archivebox
    #     ports:
    #         - 8080:8080
    #     volumes:
    #         ./data:/archivebox
    #         ./data/wayback:/webarchive
```

``` shell
# 运行
docker-compose up -d
```

文件设置了容器对外暴露本地8800端口，可以做个nginx反代，或者直接放行服务器8800访问，自己搞。

执行过程会提示输入用户名、邮箱、密码的，这个就是管理员帐号，登录管理后台用的。

## 二、保存内容

比如说想保存这篇：[http://www.ruanyifeng.com/blog/2019/09/getting-started-with-github-actions.html](http://www.ruanyifeng.com/blog/2019/09/getting-started-with-github-actions.html){target=_blank}

:octicons-light-bulb-16: **Tip** 如果网络不好，出现timeout，就把docker-compose里的TIMEOUT值升高点。

![入口](https://picture.cdn.shafish.cn/blog/archivebox-2021-12-archivebox_add.png){: .zoom loading=lazy }
![添加item](https://picture.cdn.shafish.cn/blog/archivebox-2021-12-archivebox_add2.png){: .zoom loading=lazy }
![item管理](https://picture.cdn.shafish.cn/blog/archivebox-2021-12-archivebox_add3.png){: .zoom loading=lazy }
![item详细](https://picture.cdn.shafish.cn/blog/archivebox-2021-12-archivebox_add4.png){: .zoom loading=lazy }

最后可以看到下载到本地的内容能展示多种类型，html、pdf、原生等等，直接用singlefile也就是第一个就好。或者在Archive method中选前四个就行(推荐使用)

## 三、配置

[https://github.com/ArchiveBox/ArchiveBox/wiki/Configuration](https://github.com/ArchiveBox/ArchiveBox/wiki/Configuration){target=_blank}