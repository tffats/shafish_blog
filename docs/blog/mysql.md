---
title: mysql记录
tags:
  - Mysql
hide:
  - navigation
bcs:
  "分类": "/tags/#tag:Mysql"  
---

## 一、ddl

### 1.建库
``` sql
CREATE DATABASE IF NOT EXISTS nacos_devtest;
```

### 2.查看编码

``` shell
show variables like 'character%';
```

- character_set_client为客户端编码方式；
- character_set_connection为建立连接使用的编码；
- character_set_database数据库的编码；
- character_set_results结果集的编码；
- character_set_server数据库服务器的编码；

### 3.建表

### 4.改表

### 5.删表

### 6.删库

## 二、运维

### 1.建用户

``` shell
create user '#userName'@'#host' identified by '#passWord';
/** 
    create user 'shafish'@'%' identified by 'kaifa123456'; 
**/
```

- host 取值：
    - %：代表通配所有host地址权限(可远程访问)
    - localhost：为本地权限(不可远程访问)
    - 指定特殊Ip访问权限 如10.138.106.102

### 2.数据库授权

``` shell
grant #auth on #databaseName.#table to '#userName'@'#host';
/** 
    grant select,insert,update,delete on sf.* to 'nacos_devtest'@'%';
**/
```

- auth：代表权限，如下
    - all privileges 全部权限
    - select,insert,update,delete 增删改查权限
    - create,show,execute 建库，查库，执行函数/存储过程等权限

- databaseName：代表数据库名#table 代表具体表，如下
    - *代表全部表
    - A,B 代表具体A,B表

- userNam： 代表用户名

- host：代表访问权限，如下：
    - %代表通配所有host地址权限(可远程访问)
    - localhost为本地权限(不可远程访问)
    - 指定特殊Ip访问权限 如10.138.106.102

### 3.刷新

``` sql
flush privileges;
```

### 4.查看授权情况
``` shell
show grants for '#userName'@'#host';

/** 
    show grants for 'shafish'@'%';
**/
```

### 5.撤销授权
``` shell
revoke #auth on #databaseName.#table from '#userName'@'#host';

/** 
    revoke select,insert,update,delete on sf.* from 'shafish'@'%';
**/
```

- auth：代表权限，如下
    - all privileges 全部权限
    - select,insert,update,delete 增删改查权限
    - create,show,execute 建库，查库，执行函数/存储过程等权限

- databaseName：代表数据库名

- table：代表具体表，如下
    - *代表全部表，A、B 代表具体A,B表

- userName：代表用户名

- host：代表访问权限，如下
    - %代表通配所有host地址权限(可远程访问)
    - localhost为本地权限(不可远程访问)
    - 指定特殊Ip访问权限 如10.138.106.102

### 6.删除用户
``` shell
drop user '#userName'@'#host';
/** 
    drop user 'shafish'@'%';
**/
```

- userName：代表用户名

- host：代表访问权限，如下
    - %代表通配所有host地址权限(可远程访问)
    - localhost为本地权限(不可远程访问)
    - 指定特殊Ip访问权限 如10.138.106.102

## 三、DML

### 1.增

### 2.删

### 3.查

#### 3.1 普通条件查询

``` shell
select column from table where condition1 and/or condition2 and/or xxx;
```

- 小于： `<`
- 等于： `=`
- 大于： `>`
- 不等于：`!=`
- 空值：`is null`
- 范围：
    - between：`between xx/开始值 and xx/结束值`
    - in：`in (值集合/子句)`
- 通配符like（使用时尽量后放，不要做第一个条件）：
    - 任意字符：`%`
    - 单个字符：`_`

tips：如需要与字符比较，该字符要加上引号。

#### 3.2 限制结果

``` shell
# 返回从第五行开始的5条数据
select column from table where xxx limit 5/num offset 5/num;
```

#### 3.3 排序

``` shell
# 存在order by时要放置在最后
select column from table where xxx order by column desc;
```

- `desc`：降序（Z-A）
- `asc`：升序（A-Z）

#### 分组

``` shell
# column1相同的值为分组（一行）
select column1 from table where xxx group by column1;
# 分组过滤
select column1 from table where xxx group by column1 having conditionxx;
```

#### 3.4 查询结果拼接

``` shell
# 
select concat(column1, '(', column2, ')') from table where xxx;
```

#### 3.5 返回字段别名

``` shell
# 
select concat(column1, '(', column2, ')') as title from table where conditionxxx;
```

#### 结果函数计算

查询结果的计算中使用到的函数。

- 求平均：`select avg(column1) from table;` 计算列1数值的平均值是多少
- 算总数：`select count(column1) from table;` 计算列1总数值
- 最大：`select max(column1) from table;`
- 最小：`select min(column1) from table;`
- 去重：`select distinct column1 from table;`

#### 多表

``` shell
# 内联接（表之间字段等值判断）
select column1,column2 from table1,table2 where table1.column1=xxxx;
```

### 4.改

ref:

- [MySQL8.0 创建用户及授权](https://juejin.cn/post/6844904051063144455){target=_blank}