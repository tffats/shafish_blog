---
title: Loadbalancer使用教程
description: SpringCloud, Loadbalancer
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](../../index.md)


> [`Spring Cloud Loadbalancer`](https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html){target=_blank} 是 `Ribbon` 不维护后，Spring cloud 官方提供的一个负载均衡组件，位于 `spring-cloud-commons` 下。Spring Cloud Balancer 提供了多种负载均衡算法的实现，如轮询、随机等，并且可以与Spring Cloud的其他组件（如服务注册与发现）集成使用。通过Spring Cloud Balancer，开发者可以更容易地实现微服务架构中的负载均衡策略，提高系统的性能和可靠性。

## 一、nacos接入

> 使用nacos的服务注册与发现功能即可。在 `Springcloud` 中使用服务发现组件后，`openfeign` 会自动进行服务发现（也就是选服务再发起 `http` 请求）。

[Nacos使用教程](./nacos.md){target=_blank}

## 二、loadbalancer 依赖

``` xml
<!-- 服务请求负载均衡依赖 -->
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-loadbalancer -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-loadbalancer</artifactId>
  <version>4.1.2</version>
</dependency>
```

## 三、服务提供

> 这里需要注意：你需要启动两个项目作为服务提供者，可以在父项目中创建两个模块项目，也可以直接用 idea 分别启动两个项目。
> 这两个项目的依赖、配置、对外暴露的接口地址都相同，只有接口返回的内容不一样（是为了后续区分请求了哪个服务）

???- "1. maven 依赖"

    ``` xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-config -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        <version>2023.0.1.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        <version>2023.0.1.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
        <version>4.1.2</version>
    </dependency>
    ```

???- "2. 项目配置"

    ``` yaml title="application.yml"
    server:
      port: 0 # 作为微服务提供者，可以使用随机端口，因为调用方只需要知道服务名称就能通过服务发现获取对应ip端口
    ```

    ``` yaml title="bootstrap.yml"
    spring:
      application:
        name: config
      cloud:
        nacos:
          config:
            server-addr: 127.0.0.1:8848 # nacos 配置中心地址、账号、密码、配置文件格式
            username: nacos
            password: nacos
            file-extension: yaml
          discovery: 
            server-addr: 127.0.0.1:8848 # nacos 服务注册中心地址、账号、密码
            username: nacos
            password: nacos
    ```

???- "3. 项目A：对外服务接口"

    ``` java title="ConfigController.java"
    @RestController
    @RequestMapping("/config")
    public class ConfigController {

        // @Autowired
        // private IoConfig ioConfig;

        @GetMapping("/get")
        public RestResult<Object> get() {  // RestResult 封装了统一的返回格式，自行用 String 替换就行
            return new RestResult<>(true, "msg:success", "data:from p1"); //  + ioConfig.getName()
        }
    }
    ```

???- "4. 项目B：对外服务接口"

    ``` java title="ConfigController.java"
    @RestController
    @RequestMapping("/config")
    public class ConfigController {

        // @Autowired
        // private IoConfig ioConfig;

        @GetMapping("/get")
        public RestResult<Object> get() {  // RestResult 封装了统一的返回格式，自行用 String 替换就行
            return new RestResult<>(true, "msg:success", "data:from p2"); //  + ioConfig.getName()
        }
    }
    ```

## 四、服务消费

> 开始创建消费者项目

???- "1. maven 依赖"

    > 这里需要引入 nacos 进行服务发现、openfeign 进行声明式服务请求并使用 okhttp 作为 http 请求工具、loadbalancer 进行负载均衡、sentinel 先不管

    ``` xml
    <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-bootstrap</artifactId>
			<version>4.1.2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery -->
		<dependency>
			<groupId>com.alibaba.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
			<version>2023.0.1.0</version>
		</dependency>

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

		<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-loadbalancer -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-loadbalancer</artifactId>
			<version>4.1.2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-sentinel -->
		<!-- <dependency>
			<groupId>com.alibaba.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
			<version>2023.0.1.0</version>
		</dependency> -->
    ```

???- "2. 项目配置"

    ``` yaml title="application.yml"
    server:
      port: 8082  # 这里就需要指定具体端口了，因为是对外（前端/网关）提供请求的
    ```

    ``` yaml title="bootstrap.yml"
    spring:
      application:
        name: io-config-consummer
      cloud:
        nacos:
          discovery:
            server-addr: 127.0.0.1:8848
            username: nacos
            password: nacos
        openfeign:  # openfeign 的一些配置，先忽略
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

???- "3. 项目启动配置"

    ``` java
    @SpringBootApplication
    @EnableDiscoveryClient // 可省略
    @EnableFeignClients  // 注入 openfeign 相关对象
    public class IoConfigConsummerApplication {

      public static void main(String[] args) {
        SpringApplication.run(IoConfigConsummerApplication.class, args);
      }

    }    
    ```

???- "4. openfeign client 定义与使用"    

    ``` java title="ConfigFeignClient.java"
    @FeignClient(value = "config", contextId = "ConfigService") // , configuration = ConfigClientConfig.class, fallbackFactory = ConfigClientFallback.class
    public interface ConfigFeignClient {

        @GetMapping("/config/get")
        RestResult<Object> getConfig();

    }    
    ```
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

???- "服务请求测试"    

    - 浏览器请求：`http://localhost:8082/get`

    ![](https://file.cdn.shafish.cn/blog/blog/springcloud/loadbalancer/1718205025591645.png){loading=lazy : .zoom}
    ![](https://file.cdn.shafish.cn/blog/blog/springcloud/loadbalancer/1718205035280155.png){loading=lazy : .zoom}

    可以看到请求消费端请求同一个服务接口时，分别轮询调用了启动的两个服务（服务A返回：data:from p1 / 服务B返回：data:from p2）


在以上的示例中，对于 loadbalancer 组件的使用好像就引入对应依赖就行，事实也确实是这样。因为 openfeign 会自动调用启动的负载均衡组件进行服务调用，而 loadbalancer 默认规则是 **轮询** 调用，因此，你就可以看到上面的两个服务提供方是依次循环被请求的。

