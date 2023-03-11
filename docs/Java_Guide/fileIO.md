---
title: Java案例解析-文件IO
hide:
  - navigation
  - toc
---

[:material-backburger: /Java_Guide](/Java_Guide/#七文件IO){ .md-button}

Java在1.4时引入了NIO（非阻塞IO，也可以叫NewIO）；Java1.7时扩展了NIO，包含文件、目录相关操作；而1.8进一步强化了IO，引入了流相关操作。

## 一、文件读取：lines

!!! note "以流的形式返回文件内容，因为Stream 接口实现了 AutoCloseable 接口，当 try 代码块完成时，系统会自动关闭流与词典文件（不用手动close）"

``` java linenums="1" title="1-1"
try (Stream<String> lines = Files.lines(Paths.get("/usr/share/dict/cracklib-small")) {  // (1)
// try (Stream<String> lines =new BufferedReader(new FileReader("/usr/share/dict/words")).lines()){ // (2)
    lines.filter(s -> s.length() > 6)  // (3)
        .sorted(Comparator.comparingInt(String::length).reversed())  // (4)
        .limit(10) // (5)
        .forEach(w -> System.out.printf("%s (%d)%n", w, w.length()));
} catch (IOException e) {
    e.printStackTrace();
}
```

1.  将单词作为字符串流进行检索
2.  也可以使用BufferedReader对文件进行流操作
3.  筛掉长度不足 6 个字符的单词
4.  按长度对这些单词做降序排序
5.  获取前 10 个单词

## 二、目录检索：list

``` java linenums="1" title="2-1 打印 src/ main/java 目录中所有文件和文件夹的名称"
try (Stream<Path> list = Files.list(Paths.get("src/main/java"))) {  // (1)
    list.forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

1.  非目录执行list方法会抛出 NotDirectoryException

## 三、目录遍历：walk

``` java linenums="1" title="3-1 方法定义"
public static Stream<Path> walk(Path start,  
                                FileVisitOption... options) throws IOException  // (1)
public static Stream<Path> walk(Path start,
                                int maxDepth,
                                FileVisitOption... options)
                         throws IOException
```

1.  第二个参数FileVisitOption可检测是否存在循环，一旦检测到循环，程序将抛出 FileSystemLoopException，省略就好
2.  maxDepth可指定遍历的深度，如果遍历所有则直接填Integer.MAX_VALUE

``` java linenums="1" title="3-2 通过walk方法进行目录树的遍历"
try (Stream<Path> paths = Files.walk(Paths.get("src/main/java"))) {  // (1)
    paths.forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

## 四、文件查找：find

``` java linenums="1" title="4-1 方法定义"
public static Stream<Path> find(Path start,
                                int maxDepth,
                                BiPredicate<Path, BasicFileAttributes> matcher,  // (1)
                                FileVisitOption... options)
                         throws IOException
```

1.  对于每条路径，find 方法都会调用 BiPredicate （返回布尔值）进行评估

``` java linenums="1" title="4-2 输出非目录文件"
try (Stream<Path> paths = Files.find(Paths.get("src/main/java"), Integer.MAX_VALUE,
        (path, attributes) -> !attributes.isDirectory() && path.toString().contains("fileio"))) {
    paths.forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

[:material-page-previous: Optional类的使用](optional.md){ .md-button}  [:material-page-next: Time时间处理](time.md){ .md-button .md-button-right}