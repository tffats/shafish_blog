---
title: ES学习记录
description: es, js
hide:
  - navigation
---

## 一、简介

ECMAScript（简称 ES）是 JavaScript 的标准化规范，由 ECMA International 组织（具体由 TC39 委员会）维护。JavaScript 是 ECMAScript 的一种实现，其他实现还包括 ActionScript 等。ECMAScript 定义了脚本语言的语法、类型、语句、关键字等核心特性。

## 二、基础

=== "变量、常量"

    - 变量：let 变量名称 = 变量值
    - 变量：const 常量名称 = 常量值

    可以被重新赋值的就是变量

=== "数据类型"    

    - string：字符串
    - number：数值
    - boolean：布尔
    - object：对象
    - map：集合（值以key,value形式）
    - set：集合（值不允许重复）
    - array：数组（可包含各类型元素的集合）
    - function：函数（可重复执行，有输入输出的代码块）
    - class：类（模板，用于创建具有相同属性、方法的对象）

    ``` js
    let boy = {
        name: "shafish",
        age: "100"
    }
    let girl = new Map([
        ["name", "graham"],
        ["age", 100]
    ])
    let number = new Set([1,2,3,4,4])
    let arr = ['a', 'b', 'c']
    class Person {
        name
        age

        constructor(name, age) {
            this.name = name
            this.age = age
        }

        info() {
            console.log("name", this.name, "age", this.age)
        }
    }
    let person = new Person("shafish", 100)
    person.info()
    ```

## 三、基础示例

=== "函数"

    ``` js
    // 传统
    function getPxx(x) {
        return xxx;
    }
    // 匿名函数
    let getPxx = function(x) {
        return xxx;
    }
    // 箭头函数
    let getPxx = (x) => {
        return xxx;
    }
    // 隐式返回(函数体内只有一个表达式的情况下，省略{}和return)
    let getPxx = (x) => xxx
    ```

=== "数组"

    ``` js
    //定义数组
    let arr = [10, 11]

    //向数组末尾添加一个或多个元素, 并返回修改后数组的长度
    let arrLength = arr.push(12, 13)

    //向数组开头添加一个或多个元素, 并返回修改后数组的长度
    arrLength = arr.unshift(8, 9)

    //删除数组中第一个元素, 并返回被删除元素
    let delElement = arr.shift()

    //删除数组最后一个元素, 并返回被删除元素
    delElement = arr.pop()

    //删除元素, 并返回包含被删除元素的数组 splice(要删除元素的索引位置, 要删除的元素数量)
    let delArr = arr.splice(2, 2) // 删除第3和第4个元素

    //颠倒数组中元素的顺序
    arr.reverse()

    //数组中的元素按照首字母顺序排序
    let arr2 = ['banana', 'apple', 'orange']
    arr2.sort()

    //数组中的元素按照数字排序
    let arr3 = [5, 20, 13, 1, 4]
    //arr3.sort() //默认情况下 sort() 方法使用字符串排序, 导致并没有按照数字大小排序
    /*
        a - b 是一个负数, 表示 a 应该在 b 前面
        a - b 是 0, 位置保持不变
        a - b 是一个正数, 表示 a 应该在 b 后面
    */
    arr3.sort((a, b) => a - b)

    //筛选符合条件的元素, 返回一个新的数组
    let arr4 = [10, 11, 12, 13, 14, 15]
    let newArr = arr4.filter((value, index) => {
        return value > 12
    })

    //将多个数组或值合并为一个新数组
    let arr5 = ["十六", "十七", "十八"]
    newArr = arr4.concat(arr5, 19, 20) //[10, 11, 12, 13, 14, 15, '十六', '十七', '十八', 19, 20]

    //使用for...of循环遍历数组
    let arr6 = ["shafish", "shafish.cn", 100] //数组可以包含不同的数据类型
    for (let item of arr6) {
        console.log("for...of", item)
    }

    //使用forEach方法来遍历数组
    arr6.forEach((value,index) => {
        console.log("forEach", value,"index", index)
    })
    ```

=== "Set集合"

    ``` js
    //创建Set集合
    //let fruits = new Set() //创建一个空的Set集合
    let fruits = new Set(['apple', 'orange', 'banana']) //创建一个包含初始值的Set集合

    //向Set集合中添加新的元素
    fruits.add('mango')
    //fruits.add("orange") //若该元素已经存在, 则不会重复添加, 因为 Set 中的元素必须唯一

    //从Set集合中删除元素
    fruits.delete('banana')

    //检查Set集合是否包含指定元素
    console.log("fruits.has", fruits.has('banana'))

    //获取Set集合的大小
    console.log("fruits.size", fruits.size)

    //使用 Array.from() 方法将 Set集合 转换为 数组
    let arr = Array.from(fruits)

    //使用扩展运算符将 Set集合 转换为 数组
    let arr2 = [...fruits]

    //扩展运算符是用于展开可迭代对象(如数组、字符串等)
    let web = 'shafish'
    let webArr = [...web] //使用扩展运算符将 字符串 转换为 数组

    //使用for...of循环遍历 Set集合
    for (let item of fruits) {
        console.log("for...of", item)
    }

    //使用forEach方法来遍历 Set集合
    fruits.forEach(value => {
        console.log("forEach", value)
    })

    //清空 Set
    fruits.clear()

    //将 数组 转换为 Set集合 实现数组去重
    let numberArr = [1, 2, 3, 3, 2, 1]
    let numberSet = new Set(numberArr)
    ```

=== "Map集合"

    ``` js
    //创建Map集合
    //let person = new Map() //创建一个空的Map集合
    let person = new Map([
        ["name", "shafish"],
        ["gender", "男"],
        ["web", "shafish.cn"]
    ])

    //向Map集合中添加新的元素
    person.set('height', 175)
    //在Map集合中, 每个键都是唯一的, 当使用相同的键再次调用 set() 方法时, 会替换原来键对应的值
    person.set('web', "www.shafish.cn")

    //删除元素
    person.delete('gender')

    //检查Map集合是否包含指定元素
    console.log("person.has", person.has('gender'))

    //获取Map集合的大小
    console.log("person.size", person.size)

    //将Map集合转换为数组
    let arr = Array.from(person)

    //使用扩展运算符将 Map集合 转换为 数组
    let arr2 = [...person]

    //使用for...of循环遍历Map集合
    //解构可以从数组或对象中提取值并赋给变量
    //[key, value] 就是一种解构语法, 用于将 Map 集合中的键值对解构为 key 和 value 两个变量
    for (let [key, value] of person) {
        console.log("for...of", key, value)
    }

    //使用forEach方法遍历Map集合的键值对
    person.forEach((value, key) => {
        console.log("forEach", key, value)
    })

    //清空Map集合
    person.clear()
    ```

=== "对象"    

    ``` js
    let person = {
        name: "shafish",
        gender: "男",
        web: "shafish.cn",
    }

    //向对象中添加新的属性
    person.height = 175
    //在对象中，每个键都是唯一的，当使用相同的键再次赋值时，会替换原来键对应的值
    person.web = "www.shafish.cn"

    //删除属性
    delete person.gender

    //检查对象是否包含指定属性
    let has = "gender" in person

    //获取对象的属性数量
    console.log("keysArr", Object.keys(person)) //Object.keys() 用于获取对象属性名的数组
    console.log("length", Object.keys(person).length)

    //将对象转换为数组
    let arr = Object.entries(person) //Object.entries() 用于获取对象的键值对数组
    console.log("arr", arr)

    //使用for...in循环遍历对象 
    //for...of 用于遍历可迭代对象[如数组、Set、Map、字符串等]
    //for...in 用于遍历对象的可枚举属性
    for (let key in person) {
        console.log("for...in", key, person[key])
    }

    //使用forEach方法遍历对象的属性和值
    Object.entries(person).forEach(([key, value]) => {
        console.log("forEach", key, value)
    })

    //清空对象
    person = {}
    console.log("length", Object.keys(person).length)
    ```

    ``` js
    class Person {
        name
        #web //私有属性是指仅在类内部可访问和操作的属性, 外部无法直接访问和修改

        constructor(name, web) {
            this.name = name
            this.#web = web
        }

        //使用存取器 getter 获取私有属性
        get web() {
            return this.#web
        }

        //使用存取器 setter 设置私有属性
        set web(value) {
            this.#web = value
        }

        info() {
            return `姓名:${this.name} 个人网站:${this.web}`
        }
    }

    let person = new Person("shafish", "shafish.cn")

    console.log("person", person)
    console.log("web", person.web) //使用存取器 getter 获取私有属性
    console.log("info", person.info())

    person.web = "www.shafish.cn" //使用存取器 setter 设置私有属性
    console.log("web", person.web)
    ```

=== "对象"

    ``` js
    //父类
    class Person {
        name
        gender

        constructor(name, gender) {
            this.name = name
            this.gender = gender
        }

        sleep() {
            return `${this.name} 休息中...`
        }
    }

    //子类
    class David extends Person {
        web

        constructor(name, gender, web) {
            super(name, gender) //调用父类构造函数

            this.web = web
        }

        eat() {
            return `${this.name} 正在吃饭...`
        }
    }

    let david = new David("shafish", "男", "shafish.cn")

    console.log("david", david)
    console.log("web", david.web)
    console.log("eat", david.eat())
    
    console.log("gender", david.gender)
    console.log("sleep", david.sleep())
    ```

=== "解构"

    ``` js
    //解构 可以从数组或对象中提取值并赋给变量
    //--- 数组解构
    let [x, y] = [1, 2]
    console.log("x:", x, "y:", y)

    let [, , c] = [10, 20, 30]
    console.log("c:", c)

    //扩展运算符
    let [A, ...B] = [1, 2, 3, 4, 5, 6]
    console.log("A:", A, "B:", B)

    let [x2, y2 = 200] = [100] //默认值
    console.log("x2:", x2, "y2:", y2)

    //两数交换
    let x3 = 10
    let y3 = 20; //不加分号会报错
    [x3, y3] = [y3, x3]
    console.log("x3:", x3, "y3:", y3)

    //--- 对象解构
    let person = {
        name: 'shafish',
        gender: '男',
        web: 'shafish.cn'
    }

    let { name } = person
    console.log("name:", name) // 取出name值

    //重命名 name重命名为userName
    let { name: userName, gender, web } = person
    console.log("userName:", userName, "gender:", gender, "web:", web)

    //默认值
    let { address = "安徽" } = person
    console.log("address:", address)
    ```