---
title: Java案例解析-方法引用
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](/Java_Guide/#二方法引用){ .md-button}

方法引用这个东西其实是函数式编程的概念，学习Java接触最多的应该是对象的引用，现在方法都用引用了？

!!! danger "其实方法引用本质上属于Lambda表达式的一种简化语法"

ps：关于更多Java的新特性，虽然在下不是编程大佬，不过稍微说说还是可以di：语言的发展感觉主要还是在保稳的同时增强其易用性，每个语言都用适合当下使用的特性，每种编程语言也几乎都需要迭代，但如果搞个大融合就没必要咧。一般来说某种语言发布新特性，除非是现象级否则其实使用它们的意愿并不是很强烈（行业不同、领域不同，意愿也不同，无意冒犯），但系Java8发布的新用法确实都很实用、使用场景也很基础很广泛。

## 一、方法引用语法

顾名思义，实例的引用是指向该实例地址的一个变量，方法引用直接理解为指向该方法的某种缩写就行了。

!!! note "方法引用有三种形式，格式都是用双冒号`::`将方法与引用分开"

### 1. 特定对象实例方法的引用：{++object::instanceMethod++}

比如`System.out::println`的用法就是System.out对象（也就是PrintStream对象）的println方法的引用。

### 2. 静态方法的引用：{++Class::staticMethod++}

比如`Math::max`就是`Math.max(x1,x2)`的引用。
### 3. 类型实例方法的引用：{++Class::instanceMethod++}

比如`String::length`是`"xxxx字符串".length()`的引用

## 二、示例

!!! note "看到上面方法引用语法应该都会疑惑，我艹，都不用入参的嘛？这也是函数式编程的特点：`在任何情况下都不能脱离上下文存在`"

下面展示一个打印集合元素的示例：

``` java linenums="1" title="2-1 PrintList.java 使用forEach方法"
public class PrintList {
    public static void main(String[] args) {
        List<String> list = List.of("a","b","c");
        list.forEach(new Consumer() {  // (1)
            @Override
            public void accept(Object t) {
               System.out.println(t);
            }
        });
    }
}
```

1.  forEach方法接收一个Consumer的函数式接口引用，其唯一的抽象方法为`void accept(Object x)`，表示只接收一个入参且返回类型为void

``` java linenums="1" title="2-2 PrintList.java 使用lambda表达式"
public class PrintList {
    public static void main(String[] args) {
        List<String> list = List.of("a","b","c");
        list.forEach(x -> System.out.println(x));  // (1)
    }
}
```

1.  lambda表达式直接对Consumer的函数式接口内的accept方法进行操作

``` java linenums="1" title="2-3 PrintList.java 使用方法引用"
public class PrintList {
    public static void main(String[] args) {
        List<String> list = List.of("a","b","c");
        list.forEach(System.out::println);  // (1)
    }
}
```

1.  直接使用对象::方法的语法进行方法引用

三个示例可以明显看得出来，代码真的是越写越简约。这里说一下方法引用的参数问题：

- 方法引用中用到的参数就是函数式接口中{++抽象方法的入参参数++}，也就是示例2-1中的accept(`Object t`)
- 当该抽象方法有多个参数时，{++第一个参数会作为方法的调用者++}，{++其余参数则作为入参参数++}

``` java linenums="1" title="2-4 多参数的方法引用"
public class PrintList {
    public static void main(String[] args) {
      List<String> list = Arrays.asList("this", "is", "a", "list", "of", "strings");
      List<String> sorted = list.stream()
              .sorted((s1, s2) -> s1.compareTo(s2))  // (1)
              .collect(Collectors.toList());

      List<String> sorted = list.stream()
              .sorted(String::compareTo) // (2)
              .collect(Collectors.toList());
  }
}
```

1.  1.使用了stream流对集合进行lambda格式的排序操作，sorted中传入的是`Comparator`函数式接口，方法为`int compare(T o1, T o2);`：可接收两个参数，并返回int值，用于比较大小
2.  2.使用了类型的实例方法引用，调用第一个元素（1中的 s1）的 compareTo 方法，并使用第二个元素 s2 作为该方法的参数

!!! tip "lambda表达式、方法引用这些用法，好像几乎都是操作{++集合++}时使用的"

[:material-page-previous: lambda表达式用法](lambda.md){ .md-button} [:material-page-next: 构造函数引用的用法](method_reference2.md){ .md-button .md-button-right}