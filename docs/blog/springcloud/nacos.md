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

        - 使用 `https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql` 脚本，dockerfile中访问的在线文件 `rawxxx` 是不带用户配置的！

    - mysql健康检测提示root连接失败
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

    - 使用外部mysql
        - 部署外部mysql：地址为192.168.0.109:3306
        - 创建化数据库：nacos_devtest
        - 执行初始化脚本：https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql
        - 创建访问用户并授权操作 `nacos_devtest` 库：nacos/nacos   # 具体操作可参考 [mysql记录](../mysql.md){target=_blank}
        - 修改nacos配置
          ``` title="vim nacos-docker/env/nacos-hostname.env"
          ...
          MYSQL_SERVICE_HOST=192.168.0.109
          #MYSQL_SERVICE_HOST=mysql
          MYSQL_SERVICE_DB_NAME=nacos_devtest
          MYSQL_SERVICE_PORT=3306
          MYSQL_SERVICE_USER=nacos
          MYSQL_SERVICE_PASSWORD=nacos
          ...
          ```
        - 启动集群即可：`docker-compose -f example/cluster-hostname.yaml up -d`

## 二、依赖导入

- 对应springcloud版本

| springboot版本      | springcloud版本 |
| :---:        |    :----:   |
| 3.2.4      | 2023.0.1 (aka Leyton)       |

- maven依赖

``` xml title="pom.xml"
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
    <version>4.1.2</version>
</dependency>

<!-- 动态配置依赖 -->
<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-config -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>2023.0.1.0</version>
</dependency>

<!-- 服务注册与发现依赖 -->
<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>2023.0.1.0</version>
</dependency>
<!-- 服务请求依赖 -->
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
  <version>4.1.1</version>
</dependency>
<!-- 服务请求负载均衡依赖 -->
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-loadbalancer -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-loadbalancer</artifactId>
  <version>4.1.2</version>
</dependency>
```

## 三、项目接入配置中心

### 1.修改配置文件

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

## 四、服务注册

### 1.修改配置文件

``` yaml title="bootstrap.yml"
spring:
  application:
    name: config
  cloud:
    nacos:
      discovery: # (1)
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
```

1.  如果注册中心和配置中心用的是同一个nacos, 可以把本级去掉

### 2.暴露服务接口

``` java title="ConfigController.java"
@RestController
@RequestMapping("/config")
public class ConfigController {

    // @Autowired
    // private IoConfig ioConfig;

    @GetMapping("/get")
    public RestResult<Object> get() {
    //  throw new RuntimeException();
    //  return new RestResult<>(true, "success", ioConfig.getName());
        return new RestResult<>(true, "msg:success", "data:hello,shafish");
    }
}
```
- 定义了一个get请求，返回自定义的数据体

### 3.效果

加完依赖，改好配置，启动项目就行了。打开nacos管理后台->服务管理->服务列表，就能看到启动的应用了。

![](https://file.cdn.shafish.cn/blog/blog/springcloud/nacos/1716825117445948.png){loading=lazy : .zoom}


## 五、服务发现

> 这里使用 `openfeign` 的方式请求服务。需要引入 `spring-cloud-starter-openfeign` 和 `spring-cloud-starter-loadbalancer` 组件， `openfeign` 进行声明式请求， `loadbalancer` 对provider服务进行负载均衡访问。

### 1.修改配置文件

``` yaml title="bootstrap.yml"
spring:
  application:
    name: config-consummer
  cloud:
    nacos:
      discovery: # (1)
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
    openfeign:
      okhttp: # (2)
        enabled: true
      compression: # (3)
        request:
          enabled: true
          mime-types: application/xml,text/xml,application/json,text/html,text/plain # (4)
          min-request-size: 512 # (5)
        response:
          enabled: true        
```

1.  如果注册中心和配置中心用的是同一个nacos, 可以把本级去掉
2.  启用okhttp
3.  开启请求数据的压缩功能
4.  压缩支持的MIME类型
5.  数据压缩下限 512 表示传输数据大于512,才会进行数据压缩(最小压缩值标准)

### 2.引入组件

在启动类中引入 `openfeign` 组件注解：`@EnableFeignClients`

### 3.feign定义

``` java title="ConfigFeignClient.java"
@FeignClient(value = "config", contextId = "ConfigService") // , fallbackFactory = ConfigClientFallback.class
public interface ConfigFeignClient {

    @GetMapping("/config/get")
    RestResult<Object> getConfig();

}
```

- value值指定被调用的服务名
- contextId表示当前feign名称，用于区别多个feign

### 4.服务调用

``` java title="ConfigConsummerController.java"
@RestController
public class ConfigConsummerController {

    @Autowired
    private ConfigFeignClient configFeignClient;

    @GetMapping("/get")
    public RestResult<Object> get() {
        return configFeignClient.getConfig();
    }
}
```

![](https://file.cdn.shafish.cn/blog/blog/springcloud/nacos/1716996142379471.png){loading=lazy : .zoom}

ref:
- [集群提示root密码错误](https://github.com/nacos-group/nacos-docker/issues/318){target=_blank}