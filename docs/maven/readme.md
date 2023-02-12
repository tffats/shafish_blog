---
title: Maven摘要记录
description: linux, maven, java, compile
hide:
  - navigation
---

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
> 常见的命令，提前熟悉！！

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

## 三、maven生命周期(了解)
- maven从各项目中总结后统一定义的构建过程：项目清理、初始化、编译、测试、打包、测试、部署、站点生成等步骤。
- 上面提到的插件就是对maven生命周期的具体实现：每个构建过程都绑定一个或多个插件用以具体实现。

>> 生命周期可以看作是设计模式中`模板方法`的抽象方法定义，插件就是方法的具体实现。

### 三套生命周期
>> 将项目的构建步骤分成了三个周期：clean、default、site，每个周期内的步骤都需要`按顺序`执行，每个周期都互相独立。

#### clean
- pre-clean：清理前需要完成的工作
- clean：清理上一次构建生成的文件
- post-clean：清理后需要完成的工作

> 按序执行的意思是当clean步骤被调用时，都会执行前面的步骤（先执行pre-clean再到clean步骤）。

#### default(重点)
> https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html

- process-source：处理项目主资源文件-src/main/resource
- compile：编译项目主源码-src/main/java
- precess-test-sources：处理项目测试资源文件-src/test/resource
- test-compile：编译项目测试代码-src/test/java
- test：使用单元测试框架运行测试
- package：将编译好的代码打包
- install：将包安装到maven仓库
- deploy：将包复制到远程仓库
...

#### site
- pre-sie：执行站点生成前的工作
- site：生成项目站点
- post-site：生成后的工作
- site-deploy：将站点发布到服务器

#### maven命令与生命周期的关联
命令主要通过调用生命周期的某个阶段来实现，都是基于这些步骤的简单组合。
- `mvn clean`：调用clean周期的clean步骤，此时会调用clean周期的pre-clean、clean
- `mvn test` ：调用default周期的clean步骤，会把default周期中在clean之前的所有步骤都按序执行
- `mvn clean install`：clean周期的clean步骤+default周期的install步骤
- `mvn clean deploy site-deploy`：clean周期的clean步骤+default周期的deploy步骤+site周期的site-deploy步骤。

## 四、插件(了解)
第三点提到插件是生命周期的具体实现，而生命周期可以被独立调用，所以插件也可以绑定多个周期步骤，这些步骤也被成为插件`功能`或者`插件目标`。

- `maven-dependency-plugin`插件就有多个功能
    - `dependency:analyze`：分析找出项目无用依赖
    - `dependency:tree`：列出已解析依赖

### maven内置绑定与自定义绑定
maven内置的的插件与生命周期绑定;自定义则是在pom中配置的绑定操作。

#### 内置插件绑定
- `maven-clean-plugin`：site生命周期的site步骤
- `maven-site-plugin`
    - `maven-site-plugin:site`：site生命周期的site步骤
    - `maven-site-plugin:depoy`：site生命周期的site-deploy步骤

#### 自定义绑定
``` xml
<!-- 
    创建项目的源码jar包，用jar-no-fork功能将项目主代码打包成jar文件
    将其绑定到default生命周期的verify步骤，在执行测试后、安装到仓库前创建源码jar包
 -->
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>2.1.1</version>
                <executions>
                    <execution>
                        <id>attach-source</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>jar-no-fork</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### 插件配置参数
使用-D以key=value的形式调用插件内置的配置参数。
`maven-surefire-plugin`插件提供了一个`maven-test-skip`参数，用以跳过执行测试：`mvn install -Dmaven-test-skip=true`

## 五、命令形式调用插件
> mvn [选项] [插件目标] [maven生命周期]

## 六、pom配置
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


### 1.指定插件使用的jdk版本（插件的全局配置）
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

### 3.添加默认运行的测试文件
> mvn test运行时会匹配运行`*Tests.java`格式的测试用例，但排除`*ServiceTest.java`

``` xml
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.4.1</version>
                <configuration>
                    <includes>
                        <include>**/*Tests.java</include>
                    </includes>
                    <excludes>
                        <exclude>**/*ServiceTest.java</exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
### 4.添加jacoco生成测试覆盖率文件
> `target/site/jacoco`下生成html文件

``` xml
<project>
    ...
    <build>
        <plugins>
                <plugin>
                    <groupId>org.jacoco</groupId>
                    <artifactId>jacoco-maven-plugin</artifactId>
                    <version>${jacoco.version}</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>prepare-agent</goal>
                            </goals>
                        </execution>
                        <execution>
                            <id>generate-code-coverage-report</id>
                            <phase>test</phase>
                            <goals>
                                <goal>report</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
        </plugins>
    </build>
</project>                
```

### 5.生成项目站点
> `mvn site`

``` xml
<project>
    ...
    <build>
        <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-site-plugin</artifactId>
                    <version>4.0.0-M4</version>
                </plugin>                          
        </plugins>
    </build>
    <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.4.1</version>
            </plugin>               
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jxr-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <aggregate>true</aggregate>
                </configuration>
            </plugin>  
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>3.2.1</version>
                <configuration>xxx
            </plugin>  
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-pmd-plugin</artifactId>
                <version>3.20.0</version>
                <configuration>
                    <rulesets>xxx
                    <aggregate>true</aggregate>
                </configuration>
            </plugin>              
        </plugins>
    </reporting>
</project>                   
```

## 七、使用archetype插件搭建项目骨架
> 这里的项目骨架指的是项目文件目录的层级规范。比如src/main/java中放置项目的主代码;src/test/java放置项目的测试用例代码。

### 1.项目初始化命令
`mvn archetype:generate`

### 2.选择可用的archetype骨架
默认为：`1997: remote -> org.apache.maven.archetypes:maven-archetype-quickstart (An archetype which contains a sample Maven project.)`

### 3.配置项目基本信息
提供项目对应的`groupId`、`artifactId`、`version`、`package`等信息给archetype插件，插件就会在当前目录下创建以artifactId为名称，对应archetype格式的项目。

> 还会默认添加`junit`依赖

## 八、测试
maven的`maven-surefire-plugin`的test功能默认运行项目`src/test/java`目录下 `*Test.java`、`Test*.java`、`*TestCase.java`的测试类。

### 运行指定测试用例
`mvn test -Dtest=xxxTest,xxxxTest,shafish*Test`

### 测试报告
运行`mvn test`后默认会在`target/sruefires-reports`目录下生成对应（txt、xml格式）的测试报告。

### 测试覆盖率
配置好jacoco插件后，运行`mvn test`后会在`target/site/jacoco`目录下生成报告文件

## 九、maven属性
### 内置属性
- ${basedir}:项目根目录
- ${version}:项目版本号

### pom属性
> 引用pom文件中对应元素的值，格式：${project.artifactId}

- `${project.build.sourceDirectory}`:项目主代码目录 src/main/java
- `${project.build.testSourceDirectory}`:测试代码目录 src/test/java
- `${project.build.directory}`:项目构建输出目录 target
- `${project.outputDirectory}`:项目主代码编译输出目录 target/classes
- `${project.testOutDirectory}`:项目测试代码编译输出目录 target/test-classes
- `${project.groupId}`:项目groupId
- `${project.artifactId}`:项目artifactId
- `${project.version}`:项目版本号
- `${project.builid.finalName}`:项目打包文件名称，默认${project.artifactId}-${project.version}

### 自定义属性
pom文件中在properties定义的propertie，`${propertie}`

### settings属性
引用pom.xml文件中的xml值，使用时需加前缀 settings，`${settings.localRepository}`

### java系统属性
使用`mvn help:system`查看java系统属性

### 环境变量属性
> 环境变量以`env.`开头，`${env.JAVA_HOME}`java_home环境变量值