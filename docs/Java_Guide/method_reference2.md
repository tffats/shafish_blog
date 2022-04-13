---
title: Java案例解析-构造函数引用
hide:
  - navigation
  - toc
---

[Back](/Java_Guide/#二方法引用){ .md-button}

!!! note "构造函数引用，木错，就是拿来创建对象实例那个构造函数。也就是说new实例这个操作也能被简化（可以这么理解）"
ps：其实构造函数引用也是方法引用的其中一种用法，通过关键字 new 调用构造函数。

下面先创建一个Person基础类，包括：name属性、无参构造器、有参构造器、属性get/set方法、hashCode、equals、toString等

``` java linenums="1" title="0-1 Person.java"
public class Person {

    private String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + "]";
    }

}
```

## 一、语法：`类名::new`

先上示例：指定属性值数组创建对应的实例

``` java linenums="1" title="1-1 使用Lambda表达式的方式创建多个实例"
public class Test {
  public static void main(String[] args) {
    List<String> names = Arrays.asList("shafish", "graham", "fisha","xxx");
    List<Person> people = names.stream()
      .map(name -> new Person(name)) // (1)
      .collect(Collectors.toList());
  }
}
```

1.  使用lambda表达式创建对象，map方法接收Function函数式接口，其方法为：`R apply(T t);`表示接收一个参数后可以返回另一个类型的返回值。

``` java linenums="1" title="1-2 使用构造函数引用的方式创建多个实例"
public class Test {
  public static void main(String[] args) {
    List<String> names = Arrays.asList("shafish", "graham", "fisha","xxx");
    List<Person> people = names.stream()
      .map(Person::new) // (1)
      .collect(Collectors.toList());
  }
}
```

1.  这就是构造函数引用的用法

!!! danger "Person::new 的作用是引用了 Person 类中的构造函数。跟lambda表达式一样，构造函数引用调用的构造器由上下文决定。因为map方法接收Function函数式接口，其只有一个入参，所以`Person::new`调用的构造器为`Person(String name)`"

## 二、构造函数引用的用法

### 1.复制构造函数

``` java linenums="1" title="2-1 比如有一个Person实例，先将其转换为流，再转换回列表"
Person before = new Person("shafish");

List<Person> people = Stream.of(before)
    .collect(Collectors.toList());
Person after = people.get(0);

assertTrue(before == after); // (1)
before.setName("graham"); // (2)
assertEquals("shafish", after.getName()); // (3)
```

1.  true
2.  name被改为graham
3.  相等得

``` java linenums="1" title="2-2 给定一个Person实例，创建一个属性值相同的新实例"
Person before = new Person("shafish");

List<Person> people = Stream.of(before)
      .map(Person::new) // (1)
      .collect(Collectors.toList());

Person after = people.get(0);
assertFalse(before == after); // (2)
assertEquals(before, after); // (3)

before.setName("Rear Admiral Dr. Grace Murray Hopper");
assertFalse(before.equals(after)); // (4)
```

1.  根据传入的流数据，以构造函数引用方式创建新实例
2.  true，before和after是不同的
3.  属性值是相等得
4.  before更改后，name属性值就不同了，before和after是两个实例

### 2.配合数组使用

``` java linenums="1" title="2-3 给定属性值集合，创建一个实例数组"
List<String> names = Arrays.asList("shafish", "graham", "fisha","xxx");
Person[] people = names.stream()
    .map(Person::new)
    .toArray(Person[]::new);  // (1)
```

1.  toArray方法接收IntFunction函数式接口，跟Function不同的是：IntFunction可以接收一个数组类型参数

[Back](/Java_Guide/#二方法引用){ .md-button}