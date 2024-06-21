---
title: Java案例解析-Optional处理
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](index.md#六Optional类){ .md-button}

!!! note "Optional 类设计用来在返回值可能合法为 null 时相关的操作。Optional 实例具有两种状态，要么是对 {++T 类型实例的引用++}，要么为{++空++}"

## 一、Optional的创建

``` java linenums="1" title="1-1 定义"
static <T> Optional<T>  empty()
static <T> Optional<T>  ofNullable(T value)
static OptionalInt    of(int value)
static OptionalLong   of(long value)
static OptionalDouble of(double value)
```

``` java linenums="1" title="1-2 Optional.of、Optional.ofNullable 或 Optional.empty 方法"
Optional xx = (value == null ? Optional.empty() : Optional.of(value));
Optional xx = Optional.ofNullable(value);
```

``` java linenums="1" title="1-3 改造bean的get方法返回Optional"
public class Department {
    private Manager boss;

    public Optional<Manager> getBoss() {
        return Optional.ofNullable(boss);
    }

    public void setBoss(Manager boss) {
        this.boss = boss;
    }
}        
```

## 二、取值

!!! note "确定 Optional 中存在值，则使用 get 方法，否则使用 orElse 方法;也可以在当值存在时使用 ifPresent 方法执行 Consumer"

``` java linenums="1" title="2-1"
Optional<String> firstEven = Stream.of("five", "even", "length", "string", "values")
          .filter(s -> s.length() % 2 == 0)
          .findFirst();

System.out.println(first.isPresent() ? first.get() : "No even length strings");   // (1)
System.out.println(firstOdd.orElse("No odd length strings")); // (2)
```

1.  isPresent 方法返回 true 时才调用 get 方法，如果Optional中没有值时直接调用了get方法，会抛出NoSuchElementException
2.  使用上比1更方便，如果包含的值存在，则 orElse 方法返回该值，否则返回提供的默认值-也就是"No odd length strings"

``` java linenums="1" title="2-2 Optional有值时使用isPresent直接进行消费"
Optional<String> first = Stream.of("five", "even", "length", "string", "values")
        .filter(s -> s.length() % 2 == 0)
        .findFirst();

first.ifPresent(val -> System.out.println("Found an even-length string"));  // (1)
```

1.  ifPresent 方法支持提供一个仅当 Optional 包含值时才执行的 Consumer

``` java linenums="1" title="2-3 orElse 与 orElseGet区别"
Optional<ComplexObject> val = values.stream.findFirst()

val.orElse(new ComplexObject());  // (1)
val.orElseGet(() -> new ComplexObject())  // (2)
```

1.  orElse方法无论Optional内是否有值都会创建一个新对象
2.  orElseGet方法只有在Optional没有值时才会执行代码创建新对象；但是如果参数都是字符串，则orElse与orElseGet没区别

## 三、抛异常

当Optional内没有值时直接调用get方法会抛出NoSuchElementException，或者使用orElseThrow自定义：

``` java linenums="1" title="3-1"
Optional<String> first = Stream.of("five", "even", "length", "string", "values")
        .filter(s -> s.length() % 2 == 0)
        .findFirst();

System.out.println(first.orElseThrow(NoSuchElementException::new));  // (1)
```

1.  orElseThrow接收由 Supplier 产生的异常，当Optional不包含值时抛出

[:material-page-previous: 流式操作示例](stream_example.md){ .md-button}  [:material-page-next: 文件处理](fileIO.md){ .md-button .md-button-right}