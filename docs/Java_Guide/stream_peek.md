---
title: Java案例解析-流式操作-reduce归约用法
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](/Java_Guide/#五流式操作){ .md-button}

通过之前对流的操作可以看出stream对数据的操作都是连贯的，如果没有特意处理 无法在处理流时查看各个元素的值。

下面举例子：对能被 3 整除的结果值求和，要求输出流处理时的数据：
``` java linenums="1" title="1-1 程序将打印从 start（含）到 end（含）的数字，每行一个数字"
public int sumDoublesDivisibleBy3(int start, int end) {
    return IntStream.rangeClosed(start, end)
        .map(n -> {  // (1)
            System.out.println(n);
            return n;
        })
        .map(n -> n * 2)
        .filter(n -> n % 3 == 0)
        .sum();
}
```

1.  使用println输出流处理时的元素，能让用户在不影响流处理的同时观察其内部操作

!!! danger "而本篇介绍的peek方法能在流处理时，对每个元素执行给定的操作"

``` java linenums="1" title="1-2 peek用法"
public int sumDoublesDivisibleBy3(int start, int end) {
    return IntStream.rangeClosed(start, end)
        .peek(n -> System.out.printf("original: %d%n", n))
        .map(n -> n * 2)
        .peek(n -> System.out.printf("doubled : %d%n", n))
        .filter(n -> n % 3 == 0)
        .peek(n -> System.out.printf("filtered: %d%n", n))
        .sum();
}
```

easy easy

[:material-page-previous: reduce归约用法](stream_reduce.md){ .md-button}  [:material-page-next: 更多流式操作](stream_example.md){ .md-button .md-button-right}