---
hide:
  - navigation
---

# 单元测试

[Back](/blog/#3月份){ .md-button}

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
    - （感觉都可以为概括符合行覆盖）

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

## 五、单元测试编写（JUnit5）

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

``` java

```