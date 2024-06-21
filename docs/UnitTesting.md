---
hide:
  - navigation
---

# 单元测试

[Back](blog/index.md#3月份){ .md-button}

- 单元测试可以提升软件质量、促进代码优化、提升研发效率、增加重构自信。
- 面向对象编程中，通常最小的单元就是方法。
- 单元测试的目地是在集成测试和功能测试之前对软件中的可测试单元进行逐一检查和验证。
- 单元测试是程序功能的基本保障，产品上线前重要的一环。

## 一、单元测试基本原则

- 测试用例可自动化触发执行
- 必须使用断言进行验证
- 测试用例需保证独立性
- 测试用例不允许互相调用
- 需要保证测试粒度足够小，有助于精确定位问题

## 二、单元测试用例质量

- 边界值测试，包括循环边界、特殊取值、特殊时间点、特殊顺序等情况
- 正确的输入并得到预期的结果
- 测试用例需要与文档相结合
- 测试发现代码潜在的错误，用一些强制的错误输入，看是否得到预期的错误结果

## 三、单元测试覆盖率

- 粗粒度覆盖
    - 类覆盖
    - 方法覆盖
- 细粒度覆盖
    - 行覆盖：执行到的语句行数/可执行的语句行数
    - 分支覆盖：看是否每个判定分支都被执行到
    - 条件判定覆盖：每个条件可能的情况至少被执行一次
    - 条件组合覆盖：所有条件的各种组合情况都出现至少一次（warning：出现指数级的工作量）
    - （感觉都可以概括为符合行覆盖）

## 四、常用测试注解（JUnit5）

- @Test：注明方法为测试方法，使用框架就会自动执行这些测试方法
- @TestFactory：方法为动态测试数据源
- @ParameterizedTest：可让测试方法使用不同入参执行多次
- @RepeatedTest：自定义测试方法重复运行次数
- @BeforeEach：每个测试方法运行前，都执行该测试方法
- @AfterEach：每个测试方法运行后，都执行该测试方法
- @BeforeAll：每个测试类运行前，都执行该测试方法
- @AfterAll：每个测试类运行后，都执行该测试方法
- @Disabled：注明测试类/方法不执行
- @Nested：添加嵌套测试，组织用例结构
- @Tag：添加标签，便于选择性执行

## 五、断言与假设（JUnit5）

- 断言：当不满足条件时，测试用例会被标记为测试失败。封装在`org.junit.jupiter.api.Assertions`类中，均为静态方法
    - fail：断言测试失败
    - assertTrue/assertFalse：断言条件为真/假
    - assertNull/assertNotNull：断言指定值为null或非null
    - assertEquals/assertEqualsNotEquals：两个值相等或不相等
    - assertArrayEquals：断言数组元素全部相等
    - assertSame/assertNotSame：两个对象是否为同一个对象
    - assertThrows/assertDoesNotThrow：是否抛出一个特定类型的异常
    - assertTimeout/assertTimeoutPreemptively：是否执行超时。前者会在操作超时后继续执行，并在最终测试报告中记录实际执行的时间。后者到达指定时间后就结束执行。
    - assertIterableEquals：迭代器全部相等
    - assertLinesMatch：字符串列表全部正则匹配
    - assertAll：多个条件同时满足
- 假设：当条件不满足时，测试会直接退出，而不是测试失败。封装在`org.junit.jupiter.api.Assumptions`类中，均为静态方法
    - assumeTrue：先判定给定条件为真/假，再是否继续接下来的测试。
    - assumeFalse：

!> 对于复杂的条件判定，可使用java自行构造条件，在不符合预期时使用fail断言，将测试标记为失败 。

``` java
// 使用junit的断言
public class JUnitSampleTest {
    @Test
    public void testUsingJunitAssertThat() {
        // 字符串判断
        String s = "abcde";
        Assertions.assertTrue(s.startsWith("ab"));
        Assertions.assertTrue(s.endsWith("de"));
        Assertions.assertEquals(5, s.length()); // 第一个参数为预期结果值;第二个参数为实际结果值。

        // 数字判断
        Integer i = 50;
        Assertions.assertTrue(i > 0);
        Assertions.assertTrue(i < 100);

        // 日期判断
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 100);
        Date date3 = new Date(date1.getTime() - 100);
        Assertions.assertTrue(date1.before(date2));
        Assertions.assertTrue(date1.after(date3));

        // list判断
        List<String> list = ArraysasList("a", "b", "c", "d");
        Assertions.assertEquals("a", list.get(0));
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals("d", list.get(list.size()-1));

        // map判断
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        Set<String> set = map.keySet();
        Assertions.assertEquals(3, map.size());
        Assertions.assertTrue(set.containsAll(Arrays.asList("A", "B", "C")));
    }
}
```

## 六、流式断言（AssertJ）

允许使用匿名、链试编程进行多次断言。

``` java
// 使用AssertJ的断言
public class JUnitSampleTest {
    @Test
    public void testUsingJunitAssertThat() {
        // 字符串判断
        String s = "abcde";
        Assertions.assertThat(s).as("字符串判断：首尾及长度")
            .startsWith("ab").endsWith("de").hasSize(5);

        // 数字判断
        Integer i = 50;
        Assertions.assertThat(i).as("数字判断：大小")
            .isGreaterThan(0).isLessThan(100);

        // 日期判断
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 100);
        Date date3 = new Date(date1.getTime() - 100);
        Assertions.assertThat(date1).as("日期判断：大小")
            .isBefore(date2).isAfter(date3);

        // list判断
        List<String> list = ArraysasList("a", "b", "c", "d");
        Assertions.assertThat(list).as("list判断：首尾及长度")
            .startsWith("a").endsWith("b").hasSize(4);

        // map判断
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        Assertions.assertThat(map).as("Map判断：长度及key值")
            .hasSize(3).containsKeys("A", "B", "C");
    }
}
```

## 七、单元测试编写（JUnit5）

### 1.经典测试类结构

``` java
@DisplayName("售票测试")
public class TicketSellerTest {
    // 定义待测试类实例
    private TicketSeller ticketSeller;

    // 定义整个测试类开始前执行的操作（全局及外部资源的创建、初始化）
    @BeforeAll
    public static void init() {
        // ...
    }

    // 定义整个测试类开始后执行的操作（全局及外部资源的释放、销毁）
    @AfterAll
    public static void cleanup() {
        // ...
    }

    // 定义每个测试用例开始前执行的操作（基础数据准备）
    @BeforeEach
    public void create() {
        this.ticketSeller = new TicketSeller();
    }

    // 每个测试用例完成后执行的操作（环境清理）
    @AfterEach
    public void destroy() {
        // ...
    }

    @Test
    @DisplayName("售票余额")
    public void shouldReduceInventoryWhenTicketSoldOut() {
        ticketSeller.setInventory(10);
        ticketSeller.sell(1);
        assertThat(ticketSeller.getInventory()).isEqualTo(9);
    }

    @Test
    @DisplayName("余票不足应报错信息")
    public void shouldThrowExceptionWhenNoEnoughInventory() {
        ticketSeller.setInventory(0);
        assertThatExceptionOfType(TicketException.class)
            .isThrownBy(() -> {ticketSeller.sell(1);})
            .withMessageContaining("all ticket sold out")
            .withNoCause();
    }

    // 禁用该测试用例
    @Disabled
    @Test
    @DisplayName("退票余票增加")
    public void shouldIncreaseInventoryWhenTicketRefund() {
        ticketSeller.setInventory(10);
        ticketSeller.refund(1);
        assertThat(ticketSeller.getInventory()).isEqualTo(11);
    }
}
```

### 2.多测试用例组织

测试用例较多时可以更好组织测试的结构，但不建议超过3层嵌套

``` java
@DisplayName("交易服务测试")
public class TransactionServiceTest{

    @Nested
    @DisplayName("用户交易测试")
    class UserTransactionTest{

        @Nested
        @DisplayName("正向测试用例")
        class PositiveCase {

            @Test
            @DisplayName("交易检查应通过")
            public void shouldPassCheckWhenParameterValid() {
                //
            }
        }

        @Nested
        @DisplayName("负向测试用例")
        class NegativeCase {
            //
        }
    }

    @Nested
    @DisplayName("商家交易测试")
    class CompanyTransactionTest {
        // 
    }
}
```

### 3.分组测试

通过标签来选择执行的用例类型

``` java
@DisplayName("售票器类型测试")
public class TicketSellerTest {

    @Test
    @Tag("fast")
    @DisplayName("售票后余票")
    public void shouldReduceInventoryWhenTicketSoldOut() {
        //
    }

    @Test
    @Tag("slow")
    @DisplayName("一次购买20张票")
    public void shouldSuccessWhenBuy20TicketsOnce() {
        // 
    }

}
```

// maven插件中配置相关tag
``` xml
<build>
    <plugins>
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.0</version>
            <configuration>
                <properties>
                    <includeTags>fast</includeTags>
                    <excludeTags>slow</excludeTags>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 4.数据驱动测试

将数据的输入输出与测试逻辑分开，一次针对各种类型的输入和输出结果进行验证。

``` java
@DisplayName("售票器类型测试")
public class ExchangeRateConverterTest {

    @TestFactory
    @DisplayName("时间售票检查")
    Stream<DynamicTest> oddNumberDynamicTestWithStream() {
        ticketSeller.setCloseTime(LocalTime.of(12, 20, 25, 0));
        return Stream.of(
            Lists.list("提前购票"， LocalTime.of(12, 20, 24, 0), true),
            Lists.list("准点购买"， LocalTime.of(12, 20, 25, 0), true),
            Lists.list("晚点购票"， LocalTime.of(12, 20, 26, 0), true),
        )
        .map(data -> DynamicTest.dynamicTest((String)data.get(0), () -> assertThat(ticketSeller.cloudSellAt(data.get(1))).isEqualTo(data.get(2))));
    }
}
```
## 八、Demo

``` java
public class CoverageSampleMethods {
    public Boolean testMethod(int a, int b, int c) {
        boolean result = false;
        if(a == 1 && b == 2 || c == 3) {
            result = true;
        }
        return result;
    }
}
```

``` java
@Test
@DisplayName("biubiubiu")
void testLineCoverageSample() {
    CoverageSampleMethods coverageSampleMethods = new CoverageSampleMethods();
    Assertions.assertTrue(coverageSampleMethods.testMethod(1, 2, 0));
}

// CsvSource中指定多行数据，以满足足够的覆盖
@ParameterizedTest
@CsvSource({
    "0, 2, 3",
    "1, 0, 3"
})
void testConditionDecisionCoverageTrue(int a, int b, int c) {
    CoverageSampleMethods coverageSampleMethods = new CoverageSampleMethods();
    Assertions.assertTrue(coverageSampleMethods.testMethod(a, b, c));
}
```
