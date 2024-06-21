---
title: Java案例解析-流式操作-reduce归约用法
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](index.md#五流式操作){ .md-button}

reduce方法通常在{++需要流操作返回单一值++}的场景下使用。

## 一、reduce定义

```  java linenums="1" title="1-1 reduce的三种方法定义：入参不同"
T reduce(T identity, BinaryOperator<T> accumulator); // BinaryOperator类型表示入参类型和返回类型一致
Optional<T> reduce(BinaryOperator<T> accumulator);
<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner); // BiFunction表示参数与返回值类型都可以不同
```

## 二、reduce使用

```  java linenums="1" title="2-1 reduce(BinaryOperator<T> accumulator)实现整数求和"
int sum1 = IntStream.rangeClosed(1, 10)
    .reduce((x, y) -> x + y); // (1)
int sum2 = IntStream.rangeClosed(1, 10)
    .reduce(Integer::sum); // (2)
```

1.  这里用了IntStream的reduce方法，接收的`IntBinaryOperator`类型，这个类型表示接收的参数类型和返回值类型都是Integer。这里传入两个 int 型数据并返回二者之和。
2.  之前用到的方法引用

!!! danger " lambda 表达式中，可以将二元运算符的{++第一个参数++}视为累加器，{++第二个参数++}视为流中每个元素的值"

``` java linenums="1" title="2-2 实现整数求和，看看两个入参的流执行中第一个参数值是啥"
int sum = IntStream.rangeClosed(1, 10)
    .reduce((x, y) -> {
        System.out.printf("x=%d, y=%d%n", x, y); 
        return x + y;
    })
// 输出结果：    
x=1, y=2
x=3, y=3
x=6, y=4
x=10, y=5
x=15, y=6
x=21, y=7
x=28, y=8
x=36, y=9
x=45, y=10
　
sum=55    
```

可以看到从流的第二个数据开始，入参的第一个参数都是前一次执行的返回值（跟之前迭代创建流是一样的）。


```  java linenums="1" title="2-3 reduce(T identity, BinaryOperator<T> accumulator);实现整数×2再求和"
int doubleSum1 = IntStream.rangeClosed(1, 10)
    .reduce(0, (x, y) -> x + 2 * y); // (1)
int doubleSum2 = IntStream.rangeClosed(1, 10)
.reduce(0, Integer::sum); // (2)
```

1.  这里有两个参数，第一个参数表示传入lambda表达式的初始值，第二个参数为BinaryOperator表示接收参数与返回值类型相同。执行的时候会在第一次流操作时将x赋值0，就避免了`.reduce((x, y) -> x + 2*y)`这种因为第一个参数参与操作造成的错误。
2.  之前用到的方法引用

```  java linenums="1" title="2-4 reduce(U identity, BiFunction<U, ? super T, U> accumulator,BinaryOperator<U> combiner) 将Book添加到Map中"
public class Book {
    private Integer id;
    private String title;
    // 构造函数、getter和setter、toString、equals、hashCode…
}

// 略（往books中添加元素）
HashMap<Integer, Book> bookMap = books.stream()
    .reduce(new HashMap<Integer, Book>(),  // (1)
           (map, book) -> {  // (2)
                map.put(book.getId(), book);
                return map;
            },
            (map1, map2) -> {  // (3)
                map1.putAll(map2);
                return map1;
            }
    );
bookMap.forEach((k,v) -> System.out.println(k + ": " + v));
```

1.  创建一个Map容器，作为第二个参数的一个入参
2.  接收容器以及流数据。二元的操作的第一个参数都是执行的返回值
3.  对第二个参数执行的结果进行组合

类似上面这种需要三种参数的，第一个参数为集合的创建；第二个是给集合添加元素；第三个是进行集合的合并

我们也可以使用基本类型流提供的归约方法，比如`IntStream`：
``` java linenums="1" title="2-5 使用IntStream归约函数"
String[] strings = "this is an array of strings".split(" ");
long count = Arrays.stream(strings)
        .map(String::length)  // (1)
        .count(); // (2)
System.out.println("There are " + count + " strings");
　
int totalLength = Arrays.stream(strings)
        .mapToInt(String::length) // (3)
        .sum();  // (4)
System.out.println("The total length is " + totalLength);
　
OptionalDouble ave = Arrays.stream(strings)
        .mapToInt(String::length) 
        .average();  // (5)
System.out.println("The average length is " + ave);
　
OptionalInt max = Arrays.stream(strings)
        .mapToInt(String::length)
        .max();  // (6)
　
OptionalInt min = Arrays.stream(strings)
        .mapToInt(String::length) 
        .min(); // (7)
```

1.  获取字符串对应的长度
2.  xx
3.  基本类型转换
4.  求和
5.  求平均值
6.  求最大值
7.  求最小值

## 三、更多示例

``` java linenums="1" title="3-1 拼接字符串"
String s = Stream.of("this", "is", "a", "list")
        .reduce("", String::concat);
System.out.println(s); // thisisalist
```

``` java linenums="1" title="3-2 利用 reduce 方法对 BigDecimal 求和"
BigDecimal total = Stream.iterate(BigDecimal.ONE, n -> n.add(BigDecimal.ONE))
        .limit(10)
        .reduce(BigDecimal.ZERO, (acc, val) -> acc.add(val)); 
System.out.println("The total is " + total);
```

[:material-page-previous: 流的创建](stream_of.md){ .md-button}  [:material-page-next: peek调试](stream_peek.md){ .md-button .md-button-right}