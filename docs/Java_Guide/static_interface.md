---
title: Java案例解析-接口新特性-静态方法
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](index.md#四接口新特性){ .md-button}

## 定义：static

- 接口静态方法是JDK8的新特性，也是对接口的增强操作（可以这么理解 没毛病）。
- 在接口中用{++static++}修饰并正常编写实现代码，通过接口名访问这些静态方法。
- 静态方法{++不会破坏++}接口已有的实现，但是与接口默认方法不同的是：{++接口静态方法不能被子类重写++}。

!!! danger "可以把接口静态方法对标成工具类中的方法"

``` java linenums="1" title="1-2 Employee.java"
public interface Employee {
    String getFirst();
    String getLast();
    static String getMood() {
        return "很有精神";
    }
}
```

使用时直接：`Employee.getMood()`即可。

[:material-page-previous: 接口新特性-默认方法](default_interface.md){ .md-button}  [:material-page-next: xxx](static_interface.md){ .md-button .md-button-right}