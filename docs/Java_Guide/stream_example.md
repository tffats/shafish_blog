---
title: Java案例解析-流式操作-许多
hide:
  - navigation
---

[:material-backburger: /Java_Guide](index.md#五流式操作){ .md-button}

## 一、字符串与流之间的转换

流操作大多属于集合操作，但因为{++String 类实现 CharSequence 接口++}，{++引入了IntStream相关功能++}，所以在String中也能进行流相关操作。

``` java linenums="1" title="1-1 CharSequence接口方法"
default IntStream chars() // (1)
default IntStream codePoints() // (2)
```

1.  用于处理 UTF-16 编码字符，返回一个由序列中的 char 值构成的 IntStream
2.  用于处理完整的 Unicode 代码点（code point）集，返回一个由 Unicode 代码点构成的 IntStream

``` java linenums="1" title="1-2 检查字符串是否为回文字符串"
public boolean isPalindrome(String s) {
    String forward = s.toLowerCase().codePoints()  // (1)
        .filter(Character::isLetterOrDigit)   // (2)
        .collect(StringBuilder::new,   // (3)
                 StringBuilder::appendCodePoint,
                 StringBuilder::append)
        .toString();

  String backward = new StringBuilder(forward).reverse().toString();
  return forward.equals(backward);
}
```

1.  将String转为流处理
2.  确定字符是字母还是数字
3.  `<R> R collect(Supplier<R> supplier,BiConsumer<R,? super T> accumulator,BiConsumer<R,R> combiner)`其中参数主要进行处理容器创建、元素处理、元素整合返回。

## 二、获取元素数量：count

流的count方法：将流的每个元素都被映射为 1（long），然后调用mapToLong 方法生成 LongStream，最后再调用LongStream.sum 方法。{++mapToLong(e -> 1L).sum()++}

``` java linenums="1" title="2-1 统计数量"
long count = Stream.of(3, 1, 4, 1, 5, 9, 2, 6, 5).count();
count = Stream.of(3, 1, 4, 1, 5, 9, 2, 6, 5)
    .collect(Collectors.counting());
```

## 三、汇总统计

基本类型流 IntStream、DoubleStream 与 LongStream中的{++summaryStatistics方法++}用于获取流中元素的数量、总和、最小值、最大值以及平均值（只包括这几个功能）。

``` java linenums="1" title="3-1 汇总统计"
DoubleSummaryStatistics stats = DoubleStream.generate(Math::random)
    .limit(1_000_000)
    .summaryStatistics();

System.out.println(stats); 
// DoubleSummaryStatistics{count=1000000, sum=499608.317465, min=0.000001, average=0.499608, max=0.999999}
```

## 四、查找流中符合条件的第一个元素

!!! note "类似查找第一元素的操作，需要特别注意元素顺序问题"

``` java linenums="1" title="4-1 查找第一个元素"
Optional<Integer> firstEven = Stream.of(3, 1, 4, 1, 5, 9, 2, 6, 5)
    .filter(n -> n % 2 == 0)
    .findFirst();

System.out.println(firstEven);
// Optional[4]
```

## 五、anyMatch、allMatch与noneMatch方法的使用

用于流中是否有元素匹配相关条件，方法返回一个布尔值。

``` java linenums="1" title="5-1 是否为质数"
private Primes calculator = new Primes();

@Test
public void testIsPrimeUsingAllMatch() throws Exception {
    assertTrue(IntStream.of(2, 3, 5, 7, 11, 13, 17, 19)
        .allMatch(calculator::isPrime));  // (1)
}

@Test
public void testIsPrimeWithComposites() throws Exception {
    assertFalse(Stream.of(4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20)
        .anyMatch(calculator::isPrime));  // (2)
}
```

1.  仅当所有值均为质数时返回 true
2.  存在即true

## 六、排序

``` java linenums="1" title="6-1 流元素排序"
private List<String> sampleStrings =
    Arrays.asList("this", "is", "a", "list", "of", "strings");

public List<String> defaultSort() {
    Collections.sort(sampleStrings); // (1)
    return sampleStrings;
}

public List<String> defaultSortUsingStreams() {
    return sampleStrings.stream()
        .sorted()  // (2)
        .collect(Collectors.toList());
}
```

1.  调用工具类 Collections进行排序，但会修改所提供的集合
2.  不对原始集合进行修改，而是生成一个新的流

``` java linenums="1" title="6-2 根据各个球手的得分、姓氏、名字进行排序"
public class Golfer {
    private String first;
    private String last;
    private int score;

    // 其他方法
}
private List<Golfer> golfers = Arrays.asList(
    new Golfer("Jack", "Nicklaus", 68),
    new Golfer("Tiger", "Woods", 70),
    new Golfer("Tom", "Watson", 70),
    new Golfer("Ty", "Webb", 68),
    new Golfer("Bubba", "Watson", 70)
);

public List<Golfer> sortByScoreThenLastThenFirst() {
    return golfers.stream()
        .sorted(comparingInt(Golfer::getScore)  // (1)
                    .thenComparing(Golfer::getLast)
                    .thenComparing(Golfer::getFirst))
        .collect(toList());
}
/*
*Golfer{first='Jack', last='Nicklaus', score=68}
*Golfer{first='Ty', last='Webb', score=68}
*Golfer{first='Bubba', last='Watson', score=70}
*Golfer{first='Tom', last='Watson', score=70}
*Golfer{first='Tiger', last='Woods', score=70}
*/
```

1.  首先比较第一个数量，如果相同则比较第二个数量，以此类推

## 七、流转集合

``` java linenums="1" title="7-1"
List<String> superHeroes =
    Stream.of("Mr. Furious", "The Blue Raja", "The Shoveler", "The Bowler", "Invisible Boy", "The Spleen", "The Sphinx")
          .collect(Collectors.toList());

Set<String> superHeroes =
    Stream.of("Mr. Furious", "The Blue Raja", "The Shoveler", "The Bowler", "Invisible Boy", "The Spleen", "The Sphinx")
          .collect(Collectors.toSet()); // (1)

String[] wannabes =
    Stream.of("The Waffler", "Reverse Psychologist", "PMS Avenger")
          .toArray(String[]::new);

// Set<Actor> actors = mysteryMen.getActors(); // 获取某集合数据类
Map<String, String> actorMap = actors.stream()
    .collect(Collectors.toMap(Actor::getName, Actor::getRole));   // (2)
```

1.  重复的人名将在转换为 Set 时删除
2.  使用类中属性构建一个Map，toMap 方法传入两个 Function 实例作为参数，根据所提供的对象，两个函数分别生成键和值

## 八、根据键或值对 Map 排序

``` java linenums="1" title="8-1"
map.entrySet().stream()
        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))  // (1)
        .forEach(e -> System.out.printf("Length %d: %2d words%n",
            e.getKey(), e.getValue()));
```

1.  返回一个根据键进行排序的比较器

## 九、分区与分组

``` java linenums="1" title="9-1"
List<String> strings = Arrays.asList("this", "is", "a", "long", "list", "of",
        "strings", "to", "use", "as", "a", "demo");

Map<Boolean, List<String>> lengthMap = strings.stream()
    .collect(Collectors.partitioningBy(s -> s.length() % 2 == 0));  // (1)

Map<Boolean, Long> numberLengthMap = strings.stream()
    .collect(Collectors.partitioningBy(s -> s.length() % 2 == 0, Collectors.counting()));  // (2)

lengthMap.forEach((key,value) -> System.out.printf("%5s: %s%n", key, value));
// false: [a, strings, use, a]
// true: [this, is, long, list, of, to, as, demo]

numberLengthMap.forEach((k,v) -> System.out.printf("%5s: %d%n", k, v));
// false: 4
// true: 8
```

1.  `Collectors.partitioningBy`将元素拆分为满足 Predicate（true） 与不满足 Predicate（false） 的两类
2.  `Collectors.partitioningBy`方法中的第二个参数可以指定一个Collector对分类的数据进行处理

``` java linenums="1" title="9-2"
List<String> strings = Arrays.asList("this", "is", "a", "long", "list", "of",
        "strings", "to", "use", "as", "a", "demo");

Map<Integer, List<String>> lengthMap = strings.stream()
    .collect(Collectors.groupingBy(String::length));  // (1)

lengthMap.forEach((k,v) -> System.out.printf("%d: %s%n", k, v));

// 1: [a, a]
// 2: [is, of, to, as]
// 3: [use]
// 4: [this, long, list, demo]
// 7: [strings]
```

1.  `Collectors.groupingBy`返回一个 Map，其中键为分组，值为各个分组中的元素列表。同样可以指定一个Collector对分组的数据处理。

[:material-page-previous: peek调试](stream_peek.md){ .md-button}  [:material-page-next: Optional类的使用](optional.md){ .md-button .md-button-right}