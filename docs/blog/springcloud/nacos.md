---
title: Nacos使用教程
description: SpringCloud, Nacos
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](/)

> [Nacos](https://nacos.io/){target=_blank} (Dynamic Naming and Configuration Service) 是一个开源的、易于使用的动态服务发现、配置管理和服务管理平台。
> 它帮助开发团队实现微服务架构中的服务发现、服务注册、配置管理和流量管理等功能。

- 第一节介绍了nacos的部署，需要留意下控制台的登录配置;
- 第二节介绍依赖的版本要求，需要springcloud对应springboot的版本要求;
- 第三节介绍了使用nacos作为配置中心，动态修改项目配置值，注意默认会把 `spring.application.name` 作为配置id;
- 第四节介绍了nacos的服务注册功能，也就是配置服务 `provider` 对外暴露接口，默认也是把应用名进行服务注册;
- 第五节内容就比较复杂点，使用nacos的服务发现功能，在 `consummer` 端需要用 `loadbalancer` 负载均衡组件拉取的所有服务，再选出一个服务进行http请求，当然这些都是 `openfeign` 封装好的功能，就使用方面来说是很简单的;

## 一、nacos部署
- 1.拉取官方配置
`git clone https://github.com/nacos-group/nacos-docker.git`

- 2.单机模式mysql8启动
`docker-compose -f example/standalone-mysql-8.yaml up`

- 2.集群模式
`docker-compose -f example/cluster-hostname.yaml up -d`

- 3.访问控制台
`http://127.0.0.1:8848/nacos/`

???- "问题"

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

    - 使用外部mysql（每次启动集群时 mysql 服务启动都很慢，这里直接使用外部 mysql ）
        - 部署外部mysql：地址为192.168.0.109:3306
        - 创建化数据库：nacos_devtest
        - 执行初始化脚本：https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql
        - 创建访问用户并授权操作 `nacos_devtest` 库：nacos/nacos   # 具体操作可参考 [mysql记录](../mysql.md){target=_blank}
        - 修改nacos配置（nacos服务连接数据库用的地址、端口、数据库名、账号、密码等都可以在该文件中指定）
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
<!-- https://mvnrepository.com/artifact/io.github.openfeign/feign-okhttp -->
<!-- openfeign的http请求库 -->
<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-okhttp</artifactId>
  <version>13.2.1</version>
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

``` yaml title="application.yml"
server:
  port: 8081
```

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

### 2.创建nacos配置

- 进入 `nacos` 控制台->配置管理->配置列表->创建配置 
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
- 这里简单定义了一个get请求，返回自定义的数据体

### 3.效果

加完依赖，改好配置，直接启动项目就行，可以忽略引入 `@EnableDiscoveryClient` 注解。打开nacos管理后台->服务管理->服务列表，就能看到注册到 `nacos` 中的应用了。

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

> 引入 `@EnableFeignClients` 注解会自动扫描用户定义的 `feignClient`，以生成对应的代理类进行 http 接口请求

在启动类中引入 `openfeign` 组件注解：`@EnableFeignClients`

### 3.feignClient 定义

``` java title="ConfigFeignClient.java"
@FeignClient(value = "config", contextId = "ConfigService") // , fallbackFactory = ConfigClientFallback.class
public interface ConfigFeignClient {

    @GetMapping("/config/get")
    RestResult<Object> getConfig();

}
```

- value值指定被调用的服务名，也就是 `nacos` 注册中的服务名 `spring.applicationi.name: config`;
- contextId值用当前类名即可，动态代理时指定 代理类bean的name;
- `FeignClient` 注解还有其他属性，比如：http请求配置（请求超时、请求参数对象序列化）、负载均衡处理（需引入 `loadbalancer`/`ribbon` 组件）、服务降级处理（需引入 `hystrix`/`sentienl`/`resilience4J` 组件）、请求拦截处理等等，详细在 [OpenFeign使用教程](./openfeign.md){target=_blank} 这篇文章中介绍。这里知道简单使用就行;
- 当然如果直接用 `url` 参数指定服务所在的ip:端口，则不需引入上面第二节中的负载均衡组件 `spring-cloud-starter-loadbalancer`;

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

!!! tip

    一般来说用 `nacos` 可以一步到位地管理应用配置和服务，也提供高可用部署。方便👍

ref:

- [集群提示root密码错误](https://github.com/nacos-group/nacos-docker/issues/318){target=_blank}
- [nacos官方文档](https://nacos.io/docs/latest/what-is-nacos/){target=_blank}
