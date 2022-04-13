---
title: Java案例解析-Lambda表达式-lambda定义及函数式接口
hide:
  - navigation
  - toc
---

[Back](/Java_Guide/#一lambda表达式){ .md-button}

!!! abstract "使用场景：使用lambda表达式，可以将表达式结果赋给函数式接口的引用"

    ps：其实之前有详细写过jdk8的特性，但系很多东西写完没看，用的也是基础的用法。[https://gitee.com/shafish/jdk_learn/tree/master/jdk8](https://gitee.com/shafish/jdk_learn/tree/master/jdk8){target=_blank}

## 一、函数式接口

!!! note "定义：函数式接口是一种包含`单一`抽象方法的接口（函数式接口的理解是重点）"

以Runnable接口为例，该接口只包含单一抽象方法`run()`，并返回`void`，所以Runnable接口就是一个典型的函数式接口。
Thread类构造方法接收Runnable接口的实现进行多线程操作，下面是个简单的使用例子：

``` java linenums="1" title="1-1 RunnableDemo.java"
public class RunnableDemo {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(
                    "直接new一个接口，这里是定义了一个匿名滴内部类，确实不需要这个类名");
            }
        }).start();
    }
}
```

!!! tip "更多函数式接口的说明可以查看[Java_Guide第三点](/Java_Guide/functional_interface/)查看"

## 二、Lambda表达式用法

{++函数式接口的引用++}[^1]可以用lambda表达式代替，上面`RunnableDemo`的示例可以写成下面这样：

``` java linenums="1" title="2-1 RunnableDemo.java"
public class RunnableDemo {
    public static void main(String[] args) {
        new Thread(() -> System.out.println("直接new一个接口，这里是定义了一个匿名滴内部类，确实不需要这个类名")).start();
    }
}
```

## 三、Lambda表达式格式

!!! note "Lambda表达式的格式是`(参数1,...) -> {函数体}`"

以上面Runnable示例解释：

- 括号内的参数指的是函数式接口Runnable内`run()`方法中传入的参数;
- 函数体是`run()`方法内执行的代码;
- 还有一个容易被忽略的是返回值，对应`run()`方法的返回类型。

!!! danger "Lambda表达式必须匹配函数式接口中抽象方法的参数类型和返回类型"

比较1-1示例与2-1示例，因为Runnable内`run()`木有参数，执行的代码是一句println输出，所以对应的lambda表达式就是：`() -> System.out.println("直接new一个接口，这里是定义了一个匿名滴内部类，确实不需要这个类名")`

## 四、多练练

关于函数式接口使用Lambda表达式的用法基本就这些，还有一些其他的小细节，比如函数体多行代码的情况下需要使用`;`结束符;返回类型非void情况下需要使用`return`;括号内参数不用写对应的类型等。

下面再展示一个关于FilenameFilter的示例：

``` java linenums="1" title="4-1 ListJavaFile.java"
// 输出./src/main/java目录下以.java结尾的文件文件名
public class ListJavaFile {
    public static void main(String[] args) {
        File directory = new File("./src/main/java");

        String[] names = directory.list(new FilenameFilter() {  
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });
        System.out.println(Arrays.asList(names));
    }
}
```

``` java linenums="1" title="4-2 ListJavaFile.java"
// 输出./src/main/java目录下以.java结尾的文件文件名
public class ListJavaFile {
    public static void main(String[] args) {
        File directory = new File("./src/main/java");

        String[] names = directory.list((dir, name) -> name.endsWith(".java"));
        System.out.println(Arrays.asList(names));
    }
}
```

[Back](/Java_Guide/#一lambda表达式){ .md-button}

[^1]: 函数式接口的引用：这里指1-1示例代码中的`new Runnable()/函数式接口名`