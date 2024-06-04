---
title: OpenFeign使用教程
description: SpringCloud, OpenFeign
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](/)

> OpenFeign 是一个声明式的 HTTP 客户端，它允许开发者以简单的接口定义来集成 HTTP 服务，就像调用本地方法一样，无需处理复杂的 HTTP 细节。
> 它是 Spring Cloud 的一部分，主要用于简化微服务架构中的服务调用。

## 一、nacos接入

> 使用nacos的服务注册与发现功能即可。在 `Springcloud` 中使用服务发现组件后，`openfeign` 会自动拉取服务进行调用。

[nacos 使用](./nacos.md){taget=_blank}

## 二、OpenFeign依赖

``` xml
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
<!-- 服务服务熔断依赖 feign-hystrix -->
<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-sentinel -->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
  <version>2023.0.1.0</version>
</dependency>
```

> 注意⚠️：负载均衡组件 `ribbon` 和 服务熔断组件 `hystrix` 已经不维护了，这里用 `loadbalancer` 和 `sentinel` 代替。（当然目前 `feign-hystrix` 还是被海量使用der）

## 三、服务提供

> 新建模块后引入上面的依赖

``` yaml title="bootstrap.yml"
spring:
  application:
    name: config
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
- 该项目模块就定义了一个get请求接口

## 四、使用Feign进行服务请求

> 新建模块后同样引入上面的依赖

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
- value：调用的服务名称（也就是第三点中定义的 `spring.application.name` ）
- contextId：跟接口名一致就行，动态代理生成的 bean 名称
- configuration：重写http请求服务时的配置，默认使用 `FeignClientsConfiguration` 配置，一般可以配置编解码、超时、重试之类的
- fallbackFactory：服务熔断配置
- url：如果不使用服务发现组件，可以直接用url指定请求根地址
- path：统一接口前缀

???- "config and fallback"

  ``` java title="ConfigClientConfig.java"
  public class ConfigClientConfig {
    @Bean
    public Retryer feignRetryer() {
        //最大请求次数为5，初始间隔时间为100ms，下次间隔时间1.5倍递增，重试间最大间隔时间为1s，
        return new Retryer.Default();
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
```

ref:
- [使用文档](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/){target=_blank}
- [源码流程](https://blog.csdn.net/qq_43799161/article/details/130108131){target=_blank}