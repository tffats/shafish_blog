---
title: OpenFeign使用教程
description: SpringCloud, OpenFeign
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](../../index.md)

> `OpenFeign` 是一个声明式的 HTTP 客户端，它允许开发者以简单的接口定义来集成 `HTTP` 服务，就像调用本地方法一样，无需处理复杂的 `HTTP` 细节。
> 它是 `Spring Cloud` 的一部分，主要用于简化微服务架构中的服务调用。

- 第一节熟悉下 `nacos` 的基本使用
- 第二节引入依赖
- 第三节写个简单的服务
- 第四节是本章主要内容，介绍了 `Openfeign` 的主要使用及配置，包括 `http` 请求配置、服务降级配置、`http` 请求认证拦截处理（就是在请求头加 `token` ）等
- 第五节介绍了 `Openfeign` 的全局配置

## 一、nacos接入

> 使用nacos的服务注册与发现功能即可。在 `Springcloud` 中使用服务发现组件后，`openfeign` 会自动进行服务发现（也就是选服务再发起 `http` 请求）。

[Nacos使用教程](./nacos.md){target=_blank}

## 二、OpenFeign依赖

``` xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
  <version>4.1.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/io.github.openfeign/feign-okhttp -->
<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-okhttp</artifactId>
  <version>13.2.1</version>
</dependency>
```

## 三、服务提供

> 新建模块后引入第二节中的依赖

``` yaml title="application.yml"
server:
  port: 0 # (1)
```

1.  项目启动时，随机选择端口，因为使用了 `nacos` 管理服务，只需知道服务名称就行

``` yaml title="bootstrap.yml"
spring:
  application:
    name: config
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
```

``` java title="ConfigController.java"
@RestController
@RequestMapping("/config")
public class ConfigController {

    @GetMapping("/get")
    public RestResult<Object> get() {
        return new RestResult<>(true, "msg:success", "data:name");
    }
}
```
- 该服务就定义了一个get请求接口

## 四、使用 OpenFeign 进行服务请求

> 新建项目模块后同样引入第二节中的依赖

``` yaml title="application.yml"
server:
  port: 8081
```

``` yaml title="bootstrap.yml"
spring:
  application:
    name: config-consumer
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
```

### 1. 注解声明

在项目启动类中引入 `openfeign` 注解：`@EnableFeignClients`

### 2. 定义 `feign client`

``` java title="ConfigFeignClient.java"
@FeignClient(value = "config", contextId = "ConfigService", configuration = ConfigClientConfig.class, fallbackFactory = ConfigClientFallback.class)
public interface ConfigFeignClient {

    @GetMapping("/config/get")
    RestResult<Object> getConfig();

}
```

`@FeignClient` 注解配置说明：

- `value`：服务提供者名称;
- `contextId`：跟接口名一致就行，动态代理生成的 bean 名称;
- `configuration`：重写 `http` 请求配置，默认使用 `FeignClientsConfiguration` 配置，一般可以配置请求的对象参数编解码（也就是序列化）、请求超时、重试、请求拦截之类的;
- `fallbackFactory`：服务降级配置;
- `url`：如果不使用服务发现组件，可以直接用url指定请求的 `ip:port`;
- `path`：统一接口前缀;

???- "http配置、降级配置、http拦截器"

    ``` java title="ConfigClientConfig.java"
    public class ConfigClientConfig {

      @Bean
      public Retryer feignRetryer() {
          //最大请求次数为5，初始间隔时间为100ms，下次间隔时间1.5倍递增，重试间最大间隔时间为1s，
          return new Retryer.Default();
      }

      @Bean
      public Request.Options options() {
          return new Request.Options(8, TimeUnit.SECONDS, 8, TimeUnit.SECONDS, true);
      }      
    }
    ```

    ``` java title="ConfigClientFallback.java"
    @Component
    public class ConfigClientFallback implements FallbackFactory<ConfigFeignClient> {
        @Override
        public ConfigFeignClient create(Throwable cause) {
            return new ConfigFeignClient() {

                @Override
                public RestResult<Object> getConfig() {
                    return new RestResult<>(false, "服务调用异常", "服务调用异常");
                }
            };
        }
    }
    ```

    ``` java title="FeignOauth2RequestInterceptor.java"
    @Configuration
    public class FeignOauth2RequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header("Authorization", "token");
        }
    }
    ```

### 3. 调用 `feign client`

``` java title="ConfigFeignService.java"
@Service
public class ConfigFeignService {

    @Autowired
    private ConfigFeignClient configFeignClient;

    public RestResult<Object> getConfig() {
        return configFeignClient.getConfig();
    }

}
```

``` java title="ConfigConsummerController.java"
@RestController
public class ConfigConsummerController {

    @Autowired
    private ConfigFeignService configFeignService;

    @GetMapping("/get")
    public RestResult<Object> get() {
        return configFeignService.getConfig();
    }

}
```

## 五、feign 全局配置

> 除了在 client 中单独配置 config，还可以直接在 `bootstrap.yml` 配置文件中进行全局配置

``` yaml title="bootstrap.yml"
spring:
  cloud:
    openfeign:
      okhttp:
        enabled: true
      compression:
        request:
          enabled: true
          mime-types: application/xml,text/xml,application/json,text/html,text/plain
          min-request-size: 512
        response:
          enabled: true
      httpclient:
        ok-http:
          read-timeout: 8000
        connection-timeout: 8000          
```

ref:

- [使用文档](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/){target=_blank}
- [源码流程](https://blog.csdn.net/qq_43799161/article/details/130108131){target=_blank}