---
title: Java案例解析-流式操作-流的创建
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](/Java_Guide/#五流式操作){ .md-button}

## 一、定义

Java 8为了函数式编程添加了流。{--对流操作的理解更多的可以把它看成一种操作方式。（屁话）--}

用集合举例，之前大多都是用下标来操作集合元素；流操作则会把集合全部元素都输出到流的操作链中，最后再根据需求在流末尾对元素进行流终止操作。

## 二、流的创建（6种）

### 1.Stream.of 方法

``` java linenums="1" title="2-1 利用 Stream.of 方法创建流"
String names = Stream.of("Gomez", "Morticia", "Wednesday", "Pugsley") // (1)
    .collect(Collectors.joining(","));  // (2)
System.out.println(names);
```

1.  of(Object ...x)方法中传入多个参数来创建流
2.  `.collect(Collectors.joining(","))`就是定义中说的`终止操作`，会把流中每个元素都用逗号拼接成一个字符串。

### 2.Arrays.stream 方法

``` java linenums="1" title="2-2 利用 Arrays.stream 方法创建流"
String[] munsters = { "Herman", "Lily", "Eddie", "Marilyn", "Grandpa" };
names = Arrays.stream(munsters) // (1)
    .collect(Collectors.joining(","));
System.out.println(names);
```

1.  使用数组工具类的stream方法创建流，其实`Stream.of`也是调用该方法来创建流得

### 3.Stream.iterate 方法

这个方法不太容易理解，它具体定义是：`static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)`，第一个元素是个初始值，第二个元素之前在函数式接口提过一嘴：UnaryOperator传入参数和输出值的类型是一致的。

iterate方法的意思是把第一个参数作为函数式接口UnaryOperator的入参，其输出的结果又作为下一次执行函数式接口UnaryOperator的入参（需要用到limit(num)方法指定迭代次数）。

``` java linenums="1" title="2-3 利用 Stream.iterate 方法创建流"
List<BigDecimal> nums = Stream.iterate(BigDecimal.ONE, n -> n.add(BigDecimal.ONE) )  // (1)
        .limit(10)  // (2)
        .collect(Collectors.toList());
System.out.println(nums);
// 打印[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

Stream.iterate(LocalDate.now(), ld -> ld.plusDays(1L))
    .limit(10)
    .forEach(System.out::println)
// 打印从当日开始之后的10天
```

1. BigDecimal用于表示精确的小数，常用于财务计算，确实存在`BigDecimal.add(x)`这个方法
2. 迭代10次，表示迭代执行10次`BigDecimal.add(x)`方法

!!! tip "如果需要根据当前值生成流的下一个值，iterate 方法将相当有用"

### 4.Stream.generate 方法

方法定义：`static <T> Stream<T> generate(Supplier<T> s)`，Supplier之前在函数式接口也说过，它没有入参，但有返回值。

``` java linenums="1" title="2-4 利用 Stream.generate 方法创建随机流 xiuxiuxiu"
Stream.generate(Math::random)
    .limit(10)
    .forEach(System.out::println)
```

### 5.从集合创建流

``` java linenums="1" title="2-5 利用 集合.stream 方法创建流"
List<String> bradyBunch = Arrays.asList("Greg", "Marcia", "Peter", "Jan", "Bobby", "Cindy");
names = bradyBunch.stream()
    .collect(Collectors.joining(","));
System.out.println(names);
// 打印Greg,Marcia,Peter,Jan,Bobby,Cindy
```

### 6.基本类型流（随便起的名）

提供了三种处理基本数据类型的流：`IntStream`、`LongStream` 和 `DoubleStream`，可以使用它们对应的range和rangeClose方法创建指定区间的流

!!! danger "使用基本类型流时需要留意封装类型"

下面有三种方法可以转换成基本类型：
``` java linenums="1" title="2-6-1 利用 IntStream、LongStream创建流-使用boxed方法转换"
List<Integer> ints = IntStream.range(10, 15) // (1)
    .boxed() // (2)
    .collect(Collectors.toList());
System.out.println(ints);
// 打印[10, 11, 12, 13, 14]

List<Long> longs = LongStream.rangeClosed(10, 15) // (3)
    .boxed()
    .collect(Collectors.toList());
System.out.println(longs);
// 打印[10, 11, 12, 13, 14, 15]
```

1.  range(a,b)方法表示区间[a,b)，或者可以用`of(xxx)`方法传入多个基本类型数据（参考2-6-3示例）
2.  基本类型装箱：将int 值转换为 Integer 实例，更具体来说是将 IntStream 转换为 Stream<Integer>。如果不转换则无法成功编译
3.  rangeClosed(a,b)表示区间[a,b]

``` java linenums="1" title="2-6-2 利用 IntStream、LongStream创建流-使用mapToObj方法转换"
List<Integer> ints = IntStream.of(3, 1, 4, 1, 5, 9) // (1)
    .mapToObj(Integer::valueOf) // (2)
    .collect(Collectors.toList())
```

1.  of方法添加指定数据到流中
2.  mapToObj 方法会将基本类型流转换为相关包装类的实例，参数中的方法引用为：`Integer.valueOf(int)`将int封装成Integer

还有最后一种：使用三参collect方法`<R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R,R> combiner)`来创建。仅记录（用是不可能用的，谁会这样用）：
``` java linenums="1" title="2-6-3 利用 IntStream、LongStream创建流-使用collect方法转换"
List<Integer> ints = IntStream.of(3, 1, 4, 1, 5, 9)
    .collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll);
```

[:material-page-previous: 接口新特性-静态方法](static_interface.md){ .md-button}  [:material-page-next: reduce归约操作](stream_reduce.md){ .md-button .md-button-right}