---
title: mysql记录
tags:
  - mysql
hide:
  - navigation
---

## ddl

### 建库
``` sql
CREATE DATABASE IF NOT EXISTS sf;
```

### 查看编码

``` shell
show variables like 'character%';
```

- character_set_client为客户端编码方式；
- character_set_connection为建立连接使用的编码；
- character_set_database数据库的编码；
- character_set_results结果集的编码；
- character_set_server数据库服务器的编码；

## 运维

### 建用户

``` shell
create user '#userName'@'#host' identified by '#passWord';
/** 
    create user 'shafish'@'%' identified by 'kaifa123456'; 
**/
```

- %代表通配所有host地址权限(可远程访问)
- localhost为本地权限(不可远程访问)
- 指定特殊Ip访问权限 如10.138.106.102

### 数据库授权

``` shell
grant #auth on #databaseName.#table to '#userName'@'#host';
/** 
    grant select,insert,update,delete on sf.* to 'shafish'@'%';
**/
```

- #auth 代表权限，如下
    all privileges 全部权限
    select,insert,update,delete 增删改查权限
    create,show,execute 建库，查库，执行函数/存储过程等权限

- #databaseName 代表数据库名#table 代表具体表，如下
    *代表全部表
    A,B 代表具体A,B表

- #userName 代表用户名

- #host 代表访问权限，如下
    %代表通配所有host地址权限(可远程访问)
    localhost为本地权限(不可远程访问)
    指定特殊Ip访问权限 如10.138.106.102

### 刷新

``` sqshelll
flush privileges;
```

### 查看授权情况
``` shell
show grants for '#userName'@'#host';

/** 
    show grants for 'shafish'@'%';
**/
```

### 撤销授权
``` shell
revoke #auth on #databaseName.#table from '#userName'@'#host';

/** 
    revoke select,insert,update,delete on sf.* from 'shafish'@'%';
**/
```

- #auth 代表权限，如下
    all privileges 全部权限
    select,insert,update,delete 增删改查权限
    create,show,execute 建库，查库，执行函数/存储过程等权限

- #databaseName 代表数据库名#table 代表具体表，如下

- *代表全部表
    A,B 代表具体A,B表

- #userName 代表用户名

- #host 代表访问权限，如下
    %代表通配所有host地址权限(可远程访问)
    localhost为本地权限(不可远程访问)
    指定特殊Ip访问权限 如10.138.106.102

### 删除用户
``` shell
drop user '#userName'@'#host';
/** 
    drop user 'shafish'@'%';
**/
```

- #userName 代表用户名

- #host 代表访问权限，如下
    %代表通配所有host地址权限(可远程访问)
    localhost为本地权限(不可远程访问)
    指定特殊Ip访问权限 如10.138.106.102

ref:

- https://juejin.cn/post/6844904051063144455