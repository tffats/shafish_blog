---
title: Nacos使用教程
description: SpringCloud, Nacos
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](/)

## 一、nacos部署
- 1.拉取官方配置
`git clone https://github.com/nacos-group/nacos-docker.git`

- 2.单机模式mysql8启动
`docker-compose -f example/standalone-mysql-8.yaml up`

- 2.集群模式
`docker-compose -f example/cluster-hostname.yaml up -d`

- 3.访问控制台
`http://127.0.0.1:8848/nacos/`

???- "使用问题"

    - 配置访问账号-坑
    `https://nacos.io/zh-cn/docs/auth.html`

        - 使用 `https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql`，dockerfile中访问的在线文件 `rawxxx` 是不带用户配置的！

    - mysql健康检测
    提示root连接失败
        - 创建ping命令脚本
        ``` shell title="example/mysql_ping.sh"
        #!/bin/sh
        mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD}
        ```
        - 添加执行权限：`chmod +x mysql_ping.sh`
        - 修改dockerfile
        ``` yml title="example/cluster-hostname.yaml"
        mysql:
            container_name: mysql
            build:
                context: .
                dockerfile: ./image/mysql/5.7/Dockerfile
            image: example/mysql:5.7
            env_file:
                - ../env/mysql.env
            volumes:
                - ./mysql:/var/lib/mysql
                - ./mysql_ping.sh:/mysql_ping.sh
            ports:
                - "3306:3306"
            healthcheck:
                # test: [ "CMD", "mysqladmin" ,"ping", "-h", "localhost" ]
                test: ["CMD", "/bin/sh", "/mysql_ping.sh"]
                interval: 5s
                timeout: 10s
                retries: 10
        ```

## 二、依赖导入

- 对应springcloud版本

| springboot版本      | springcloud版本 |
| :---:        |    :----:   |
| 3.2.4      | 2023.0.1 (aka Leyton)       |

- maven依赖

``` xml title="pom.xml"
<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-config -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>2023.0.1.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
    <version>4.1.2</version>
</dependency>
```

## 三、项目接入配置中心

### 1.修改配置文件

- bootstrap配置
``` yml title="bootstrap.yml"
spring:
  application:
    name: config
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
```

- 启用 `bootstrap` 配置: 2021.0.5 版本起，Spring Cloud 不再默认启用 bootstrap 包，添加上依赖即可。
``` yml title="application.yml"
server:
  port: 8081
```

### 2.创建nacos配置

- 配置管理->配置列表->创建配置 
    - Data ID: 使用 `bootstrap.yml` 文件中配置的 `spring.application.name` 值，也就是 `config`
    - 配置格式：`yaml`
    - 配置内容：`shafish.io.config.name: "shafishshasha"`
    - 发布
![](https://file.cdn.shafish.cn/blog/blog/springcloud/nacos/%E5%9B%BE%E7%89%87.png){loading=lazy : .zoom}
![](https://file.cdn.shafish.cn/blog/blog/springcloud/nacos/1716478910905870.png){loading=lazy : .zoom}

### 3.使用配置类

- 创建配置类并注入spring中

``` java title="IoConfig.java"
@Component
@ConfigurationProperties(prefix = "shafish.io.config")
public class IoConfig {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

- 使用配置类

``` java title="ConfigController.java"
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private IoConfig ioConfig;

    @GetMapping("/get")
    public Object get() {
        return ioConfig.getName();
    }
}
```

- 修改 `nacos` 配置值，再重新访问接口看看内容是否变化。

![](https://file.cdn.shafish.cn/blog/blog/springcloud/nacos/1716479004526445.png){loading=lazy : .zoom}

ref:
- [集群提示root密码错误](https://github.com/nacos-group/nacos-docker/issues/318){target=_blank}