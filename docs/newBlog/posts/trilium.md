---
title: Trilium笔记
authors:
    - shafish
date:
    created: 2023-03-12 
    updated: 2024-02-02
categories:
    - trilium
    - 笔记
    - docker
    - linux
---

> [https://github.com/zadam/trilium](https://github.com/zadam/trilium){target=_blank}

## 一、安装
- 1.docker本地运行
``` shell
docker run -d --name trilium -p 8080:8080 -v /home/xxx/docker/trilium-data:/home/node/trilium-data zadam/trilium:latest
```
- 2.域名dns解析:[https://dns.console.aliyun.com/](https://dns.console.aliyun.com/){target=_blank}
- 3.配置反向代理:[宝塔面板](https://www.bt.cn/new/index.html){target=_blank}
- 4.域名证书:[https://freessl.cn/certlist](https://freessl.cn/certlist){target=_blank}
- 5.wss协议升级(使内容实时生效)
``` shell
location /
{
  ...
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection 'upgrade';
  ...
}
```
![](https://picture.cdn.shafish.cn/blog/2022-09-17_23-41.png)

<!-- more -->

## 二、数据备份
~/trilium-data/backup/xxx

## 三、label
- disableVersioning：设置大内容的笔记不使用版本控制
- calendarRoot：日记笔记用
- archived：归档笔记，不会被搜索到
- excludeFromExport：设置笔记不能导出
- 

## 四、笔记类型
trilium中所有的笔记都可以作为文件夹，在其中创建子文件。

### 1.Text note
### 2.Code note
### 3.Image note
### 4.File note
### 5.Render HTML note
### 6.Saved search note
### 7.Relation map note
### 8.Book note
### 9.Mermaid
### 10.Canvas note