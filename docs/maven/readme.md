>> Maven摘要记录

## 一、maven作用

Maven：构建、依赖管理、项目信息管理
- 构建：编译、运行单元测试、生成文档、打包、部署...
- 依赖管理：jar包下载，版本管理...
- 项目信息配置：项目坐标、项目描述等...

maven坐标：groupId、artifactId、version、packageing、classifier
- groupId：项目
- artifactId：模块
- version：项目版本
- packageing：规定了项目的打包方式
- classifier：生成辅助内容


## 二、maven命令
### 1.主代码编译
`mvn clean compile`：对应执行clean、resources、compile插件的相关命令。

### 2.测试代码编译
`mvn clean test`：对应执行clean、resources、compile、surefire插件的相关命令。

> surefire为maven中负责执行测试的插件

### 3.打包
`mvn clean package`：对应执行clean、resources、compile、surefire、jar插件的相关命令。

### 4.使项目可被引用
`mvn clean install`：对应执行clean、resources、compile、surefire、jar、install插件的相关命令，将该jar安装到本地仓库。

### 5.生成项目骨架
`mvn archetype:generate`：选择一个已有的archetype项目骨架进行初始化

### 6.查看项目依赖列表
`mvn dependency:list`
`mvn dependency:tree`

## 三、pom配置
### 0.pom文件元素
``` xml
<project>
    <!-- 项目坐标 -->
    <groupId>项目</groupId>
    <artifactId>模块</artifactId>
    <name>项目说明名称</name>
    <version>项目版本</version>

    <!-- 变量声明 -->
    <properties>
        <xxxx>xxx</xxxx>
    </properties>

    <!-- 依赖处理 -->
    <dependencies>
        <dependency>
            <groupId>项目</groupId>
            <artifactId>模块</artifactId>
            <version>项目版本</version>
            <type>项目依赖类型</type>
            <scop>依赖作用范围</scop>
            <optional>true/false表示为可选依赖</optional>
            <exclusions>
                <exclusion>去除间接的依赖</exclusion>
            </exclusions>
        </dependency>
    </dependencies>
</project>
```

- scop：指定项目编译、测试、运行时的依赖范围（通过使用不同的classpath来实现)
    - test：将该依赖添加到测试的classpath中，项目测试时使用
    - compile：编译范围，项目编译时使用（测试、运行都需要先编译）
    - provided：编译、测试时有效，项目运行时无效
    - runtime：运行时依赖
- optional：项目a中添加了opentional为true的依赖，当项目b依赖项目a时，该依赖不会传导给项目b，项目b需要重新导入该依赖
- exclusions：去除间接的依赖


### 1.指定插件使用的jdk版本
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

### 2.生成可执行的jar包

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

## 四、使用archetype插件搭建项目骨架
> 这里的项目骨架指的是项目文件目录的层级规范。比如src/main/java中放置项目的主代码;src/test/java放置项目的测试用例代码。

### 1.项目初始化命令
`mvn archetype:generate`

### 2.选择可用的archetype骨架
默认为：`1997: remote -> org.apache.maven.archetypes:maven-archetype-quickstart (An archetype which contains a sample Maven project.)`

### 3.配置项目基本信息
提供项目对应的`groupId`、`artifactId`、`version`、`package`等信息给archetype插件，插件就会在当前目录下创建以artifactId为名称，对应archetype格式的项目。

> 还会默认添加`junit`依赖

## 五、项目分模块/包

