>> Maven摘要记录

## 一、maven作用

Maven：构建、依赖管理、项目信息管理
- 构建：编译、运行单元测试、生成文档、打包、部署...
- 依赖管理：jar包下载，版本管理...
- 项目信息配置：项目坐标、项目描述等...

## 二、maven命令
### 主代码编译
`mvn clean compile`：对应执行clean、resources、compile插件的相关命令。

### 测试代码编译
`mvn clean test`：对应执行clean、resources、compile、surefire插件的相关命令。

> surefire为maven中负责执行测试的插件

### 打包
`mvn clean package`：对应执行clean、resources、compile、surefire、jar插件的相关命令。

### 使项目可被引用
`mvn clean install`：对应执行clean、resources、compile、surefire、jar、install插件的相关命令，将该jar安装到本地仓库。

## 三、pom配置
### 指定插件使用的jdk版本
maven-compiler-plugin插件

``` xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>    
```

### 生成可执行的jar包

``` xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.4.1</version>
                <configuration>
                    <!-- put your configurations here -->
                    <transformers>
                        <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                            <manifestEntries>
                                <!-- jar包的执行入口类 -->
                                <Main-Class>cn.shafish.Main</Main-Class>
                            </manifestEntries>
                        </transformer>
                    </transformers>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```