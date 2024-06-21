---
title: Java案例解析-接口新特性-默认方法
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](index.md#四接口新特性){ .md-button}

## 定义：default

- 接口默认方法是JDK8的新特性，是对接口的增强操作。
- 直接在接口中用{++default++}修饰并正常编写实现代码，默认方法{++不会破坏++}接口已有的实现。

比如某个接口同时提供了获取FirstName、LastName和获取全名的方法：
``` java linenums="1" title="1-1 Employee.java"
public interface Employee {
    String getFirst();
    String getLast();
    String getName();
}
```
如果你要使用该Employee接口，就必须实现getFirst、getLast、getName这三个方法，但是细心的你发现getName不就是(getFirst+getLast)的组合结果么。

``` java linenums="1" title="1-2 Employee.java"
public interface Employee {
    String getFirst();
    String getLast();
    default String getName() {
        return String.format("%s %s", getFirst(), getLast());
    }
}
```

!!! note "接口默认方法实现的出现就是为了解决类似这种的问题"

[:material-page-previous: 函数式接口的用法](functional_interface.md){ .md-button}  [:material-page-next: 接口新特性-静态方法](static_interface.md){ .md-button .md-button-right}