---
title: Java案例解析-函数式接口
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](index.md#三函数式接口){ .md-button}

这篇函数式接口介绍放在lambda之前可能更好，但不想改链接，也或许会加深些印象，而且后面几章都是跟接口有关，就一起吧

## 一、函数式接口定义

!!! danger "函数式接口是一种包含{++单一抽象方法++}的接口，可以作为lambda表达式或者方法引用的目标（单一抽象方法是重点！）"

接口的方法默认都是抽象的，也就是说只要该接口内只定义了一个方法，那么就是函数式接口。

在函数式接口上可以使用`@FunctionalInterface `注解修饰，这个注解只提供验证功能，在编译时实时验证接口是否为函数式接口。

下面自定义一个函数式接口：
``` java linenums="1" title="1-1 xxxCheck.java"
@FunctionalInterface
public interface xxxCheck {
    boolean isxxx(String s);
}
```

{--讲完了，后续有能力再写个自定义的函数接口使用demo--}

## 二、Java8提供的函数式接口

（大都要配合流使用）
Java 8 专门定义了 java.util.function 包，它包含很多可重用的函数式接口，主要分为以下四类：

### 1.Consumer纯消费类型

!!! danger "传入一个泛型参数，不返回任何值"

``` java linenums="1" title="2-1-1 Consumer.java 函数式接口定义"
@FunctionalInterface
public interface Consumer<T> {

    void accept(T t); // (1)

    default Consumer<T> andThen(Consumer<? super T> after) {  // (2)
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```

1.  函数式接口中单一的抽象方法
2.  复合操作的默认方法，暂时不用管

集合的`void forEach(Consumer<? super T> action)`方法就接收了Consumer类型的函数式接口引用：
``` java linenums="1" title="2-1-2 Consumer用法"
List<String> strings = Arrays.asList("this", "is", "a", "list", "of", "strings");

strings.forEach(new Consumer<String>() {
    @Override
    public void accept(String s) {
        System.out.println(s);
    }
});

strings.forEach(s -> System.out.println(s));
strings.forEach(System.out::println);
```
可以看到`forEach`方法内对集合元素进行了处理，但并没有进行返回处理。

Consumer还有其他四中扩展类型（用法都是一样的，就参数的区别）：

| 函数式接口      | 唯一方法                          |
| :-----------: | :------------------------------------: |
| `IntConsumer`       |      void accept(int x) -- 接收Integer类型参数  |
| `DoubleConsumer`       |     void accept(double x) --  接收Double类型参数|
| `LongConsumer`    |     void accept(long x) --  接收Long类型参数|
| `BiConsumer`    |     void accept(T t, U u) -- 可以接收两个不同类型的参数进行处理 |

### 2.Supplier纯生产类型

!!! danger "不传入参数，但会返回一个值"

``` java linenums="1" title="2-1-1 Supplier.java 函数式接口定义"
@FunctionalInterface
public interface Supplier<T> {
    T get(); //(1)
}
```

1.  函数式接口中单一的抽象方法

``` java linenums="1" title="2-2-1"
Logger logger = Logger.getLogger("...");

DoubleSupplier randomSupplier = new DoubleSupplier() {  // (1)
    @Override
    public double getAsDouble() {
        return Math.random(); // 或者某个固定值
    }
};

randomSupplier = () -> Math.random();
randomSupplier = Math::random;
logger.info(randomSupplier); // (2)
```

1.  Supplier的其中一个扩展，返回的是double类型
2.  logger.info日志输出方法接收Supplier类型

Supplier一样还有其他四中扩展类型（用法都是一样的，就返回类型的区别）：

| 函数式接口      | 唯一方法                          |
| :-----------: | :------------------------------------: |
| `IntSupplier`       |      int getAsInt() -- 返回Integer类型值  |
| `DoubleSupplier`       |     double getAsDouble() --  返回Double类型值|
| `LongSupplier`    |     long getAsLong() --  返回Long类型值|
| `BooleanSupplier`    |     boolean getAsBoolean() -- 返回布尔类型值 |

!!! note "接收Supplier类型的方法确实之前都没怎么见过，2-2-1示例的logger.info是一个。当然了，知道怎么用就行（微笑）"

### 3.Predicate布尔判断类型

!!! danger "传入一个参数，返回一个布尔值，主要用于{++流数据的筛选++}"
ps：把`流stream`当成集合来看就行了。

``` java linenums="1" title="2-3-1 Predicate.java 函数式接口定义"
@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t); // (1)

    // 其他默认、静态方法
}
```

1.  函数式接口中单一的抽象方法

``` java linenums="1" title="2-3-2 给定一个名称集合，可以通过流处理找出所有具有特定长度的实例"
List<String> names = List.of("shafish","graham","other");
List<String> realNames = list.stream().filter(s -> !s.equals("other")).collect(Collectors.toList());  // 意思是返回shafish和graham组成的集合
```

!!! note "还可以把常用的包含判断逻辑的Predicate赋给常量（实际真有这么用？）"

### 4.Function函数类型

!!! danger "传入一个参数，返回一个值，常用于流的map方法，进行取值/类型转换"

``` java linenums="1" title="2-4-1 Function.java 函数式接口定义"
@FunctionalInterface
public interface Function<T, R> {

    R apply(T t); // (1)

    // 其他默认、静态方法
}
```

1.  函数式接口中单一的抽象方法，将 T 类型的泛型输入参数转换为 R 类型的泛型输出值

``` java linenums="1" title="2-4-2 获取集合元素对应的长度"
List<String> names = Arrays.asList("Mal", "Wash", "Kaylee", "Inara",
        "Zoë", "Jayne", "Simon", "River", "Shepherd Book");

List<Integer> nameLengths = names.stream()
        .map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) { // (1)
                return s.length();
            }
        })
        .collect(Collectors.toList());

nameLengths = names.stream()
        .map(s -> s.length()) 
        .collect(Collectors.toList());

nameLengths = names.stream()
        .map(String::length)
        .collect(Collectors.toList());

System.out.printf("nameLengths = %s%n", nameLengths);
// nameLengths == [3, 4, 6, 5, 3, 5, 5, 5, 13]
```

1.  接收单个集合元素（String），返回该元素长度（Integer）

Function分了一大堆扩展，都是指定参数类型、返回值类型有关的：

| 函数式接口      | 唯一方法                          |
| :-----------: | :------------------------------------: |
|IntFunction|R apply(int value)|
|DoubleFunction|R apply(double value)|
|LongFunction|R apply(long value)|
|ToIntFunction|int applyAsInt(T value)|
|ToDoubleFunction|double applyAsDouble(T value)|
|ToLongFunction|long applyAsLong(T value)|
|DoubleToIntFunction|int applyAsInt(double value)|
|DoubleToLongFunction|long applyAsLong(double value)|
|IntToDoubleFunction|double applyAsDouble(int value)|
|IntToLongFunction|long applyAsLong(int value)|
|LongToDoubleFunction|double applyAsDouble(long value)|
|LongToIntFunction|int applyAsInt(long value)|
|BiFunction|R apply(T t, U u)|

!!! note "如果入参与返回类型为同一个类型可以用`java.util.function.UnaryOperator`接口，如果入参与返回类型同为int、double、long这三个基本类型，则对应IntBinaryOperator、DoubleBinaryOperator 与 LongBinaryOperator 接口"

### 5. 用的时候只需要查看对应函数式接口类型中的方法入参、返回值就行

[:material-page-previous: 构造函数引用的用法](method_reference2.md){ .md-button}  [:material-page-next: 接口新特性-默认方法](default_interface.md){ .md-button .md-button-right}