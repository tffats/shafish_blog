---
title: Vue学习记录
description: vue, node, js
hide:
  - navigation
---

## 一、环境搭建

### 1. Vue库引入(示例用)

[js文件下载](https://cn.vuejs.org/guide/quick-start.html#using-vue-from-cdn){target=_blank}

=== "传统模式"

    ``` js title="demo1.html" linenums="1" hl_lines="8"
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title> <!-- 传统模式（script引入js） --> 
        <script src="vue.global.js"></script> 
    </head>

    <body>
        <div id="app">
            {{msg}}
        </div>

        <script>
            // 结构赋值
            const {createApp, reactive} = Vue

            createApp({
                // 设置响应式数据、方法等
                setup() {
                    return {
                        msg: "shafish"
                    }
                }
            }).mount("#app")
        </script>
    </body>

    </html>
    ```



=== "模块开发"

    ``` html title="demo2.html" linenums="1" hl_lines="17"
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
    </head>

    <body>
        <div id="app">
            {{msg}}
        </div>

        <script>
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {
                    return {
                        msg: "shafish"
                    }
                }
            }).mount("#app")
        </script>
        
    </body>

    </html>
    ```

### 2. Node环境搭建

``` shell
pacman -S nodejs npm
# 修改国内源
npm config set registry https://registry.npmmirror.com/
```

### 3. 创建Vue项目

``` shell
npm create vite@latest

> npx
> create-vite

✔ Project name: … vue-demo
✔ Select a framework: › Vue
✔ Select a variant: › JavaScript

Scaffolding project in /home/shafish/Project/Vue/vue-demo...

Done. Now run:

  cd vue-demo
  npm install
  npm run dev
```

## 二、基础指令

[内置指令文档](https://cn.vuejs.org/api/built-in-directives.html#built-in-directives)

=== "v-on"

    用于监听用户事件，比如鼠标点击、键盘up/down等。`v-on:` 可缩写为 `@`

    ``` html linenums="1"
    <body>

        <div id="app">
            {{msg}} <br>

            <h2>{{web.title}}</h2>
            <h2>{{web.url}}</h2>

            <button v-on:click="edit">修改</button> <br>
            <button @click="edit">修改(简写@)</button>
        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const web = reactive({
                        title: "graham",
                        url: "shafish.cn"
                    })

                    const edit = () => {
                        web.url = "www.shafish.cn"
                    }

                    return {
                        msg: "shafish",
                        web,
                        edit
                    }
                }
            }).mount("#app")
        </script>
    </body>    
    ```

=== "v-bind"

    用于绑定标签的属性，可更新属性、给组件传参等。`v-bind:` 可缩写为 `：`

    ``` html linenums="1"
    <body>

        <div id="app">
            <h3>动态绑定标签的value属性</h3>

            <input type="text" value="shafish.cn"> <br>
            <input type="text" v-bind:value="web.url"> <br>
            <!-- v-bind 简写 -->
            <input type="text" :value="web.url"> <br>

        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const web = reactive({
                        url: "shafish.cn",
                        fontStatus: true
                    })

                    return {
                        web
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```

=== "v-for"

    用于数组集合的遍历，结合当前标签或模板进行多次渲染。

    ``` html linenums="1"
    <body>

        <div id="app">
            
            <ul>
                <li v-for="value in data.number">
                    {{value}}
                </li>
            </ul>

            <ul>
                <li v-for="(value,index) in data.number">
                    {{index}} {{value}}
                </li>
            </ul>

            <ul>
                <li v-for="(value, key, index) in data.user">
                    {{index}} {{key}} {{value}}
                </li>
            </ul>

            <ul>
                <li v-for="(value, key, index) in data.teacher" :title="value.name" :key="value.id">
                    {{index}} {{key}} {{value.id}} {{value.name}} {{value.web}}
                </li>
            </ul>

            <ul>
                <!-- template 标签不会渲染到页面中 -->
                <template v-for="(value, key, index) in data.user">
                    <li v-if="index ==1">{{index}} {{key}} {{value}}</li>
                </template>
            </ul>

        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const data = reactive({
                        number: ["10", "11", "12"],
                        user: {
                            name: "shafish",
                            gender: "nan"
                        },
                        teacher: [
                            {id: 1, name: "fishaT", web: "fishaT.com"},
                            {id: 2, name: "grahamT", web: "grahamT.com"},
                        ]
                    })

                    return {
                        data
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```

=== "v-if"

    当指令的表达式计算为真（truthy）时，Vue 会将元素插入 DOM；当表达式为假（falsy）时，元素将不会被渲染。

    ``` html linenums="1"
    <body>

        <div id="app">
            {{web.show}} <br>

            <!-- 适用于频繁显示隐藏 -->
            <p v-show="web.show">show-隐藏文字</p> 
            <p v-if="web.show">if-隐藏文字</p> 
            
            <button v-on:click="toggle">修改</button>

            <p v-if="web.user < 1000">正常网站</p> 
            <p v-else-if="web.user > 1000 && web.user < 10000">优秀网站</p> 
            <p v-else="web.user > 10000">nb网站</p> 

        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const web = reactive({
                        show: true,
                        user: 200
                    })

                    const toggle = () => {
                        web.show = !web.show
                    }
                    return {
                        web,
                        toggle
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```    

=== "v-show"

    如果 web.show 为 true，则元素显示；如果为 false，则元素隐藏（display: none）。
    提供了一种快速切换元素显示状态的方法，适用于那些不需要从 DOM 中添加或移除，但需要根据条件显示或隐藏的场景。

    ``` html linenums="1"
    <body>

        <div id="app">
            {{web.show}} <br>

            <!-- 适用于频繁显示隐藏 -->
            <p v-show="web.show">show-隐藏文字</p> 
            <p v-if="web.show">if-隐藏文字</p> 
            
            <button v-on:click="toggle">修改</button>

        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const web = reactive({
                        show: true
                    })

                    const toggle = () => {
                        web.show = !web.show
                    }
                    return {
                        web,
                        toggle
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```    

=== "v-model"

    用于试图（用户操作）与数据的双向绑定。v-model 可以与修饰符一起使用，以改变其行为：

    - .lazy：在默认情况下，v-model 在 input 事件中同步输入框的值，使用 .lazy 修饰符后，同步将在 change 事件中进行。
    - .number：输入字符串转为有效的数字。
    - .trim：过滤用户输入，去除首尾空格。

    ``` html linenums="1"
    <body>

        <div id="app">
            <h3>文本框:{{data.text}}</h3>
            <h3>单选:{{data.radio}}</h3>
            <h3>复选框:{{data.checkbox}}</h3>
            <h3>记住密码:{{data.remeber}}</h3>
            <h3>下拉框:{{data.select}}</h3>
            <hr>
            <!-- 单向数据绑定,数据改变时，试图会自动改变 -->
            单向数据绑定：<input type="text" :value="data.text"> <br>
            <!-- 对于 input type="text"， v-model绑定该标签的value元素 -->
            双向数据绑定（实时渲染）：<input type="text" v-model="data.text"><br>
            双向数据绑定（失去焦点/回车后再渲染）：<input type="text" v-model.lazy="data.text">
            <hr>

            <input type="radio" v-model="data.radio" value="1"> 写作
            <input type="radio" v-model="data.radio" value="2"> 画画
            <input type="radio" v-model="data.radio" value="3"> 运动
            <hr>

            <input type="checkbox" v-model="data.checkbox" value="a"> 写作
            <input type="checkbox" v-model="data.checkbox" value="b"> 画画
            <input type="checkbox" v-model="data.checkbox" value="c"> 运动
            <hr>

            <input type="checkbox" v-model="data.remeber"> 记住密码
            <hr>

            <select v-model="data.select">
                <option value="">请选择</option>
                <option value="a">写作</option>
                <option value="b">画画</option>
                <option value="c">运动</option>
            </select>
        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const data = reactive({
                        text: "shafish", //文本框
                        radio: "", // 单选
                        checkbox: [], // 复选框
                        remeber: false, // 记住密码
                        select: ""  // 下拉框
                    })

                    return {
                        data
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```

=== "其他"

    再说

## 三、核心属性/方法

=== "computed"

    会根据响应式数据的变化自动重新计算值

    ``` html linenums="1"
    <body>

        <div id="app">
            调用了两次，执行两次
            <h3>{{add()}}</h3>
            <h3>{{add()}}</h3>

            调用了两次，执行一次，因为计算数据没有发生变化，直接用的缓存
            <h3>{{sub}}</h3>
            <h3>{{sub}}</h3>
        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive, computed } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const data = reactive({
                        x: 10,
                        y: 20
                    })

                    let add = () => {
                        console.log("add")
                        return data.x + data.y
                    }

                    const sub = computed(()=>{
                        console.log("sub")
                        return data.x - data.y
                    })

                    return {
                        data,
                        add,sub
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```            

=== "watch"

    观察和响应 Vue 实例上的数据变动，当需要在数据变化时执行异步操作或开销较大的操作时，watch 非常有用

    ``` html linenums="1"
    <body>

        <div id="app">
            兴趣：
            <select v-model="hobby">
                <option value="">请选择</option>
                <option value="1">写作</option>
                <option value="2">画画</option>
                <option value="3">运动</option>
            </select>

            年：
            <select v-model="date.year">
                <option value="">请选择</option>
                <option value="2023">2023</option>
                <option value="2024">2024</option>
                <option value="2025">2025</option>
            </select>

            月：
            <select v-model="date.month">
                <option value="">请选择</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
            </select>
        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive, ref, watch } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const hobby = ref("")

                    const date = reactive({
                        year: "2024",
                        month: "12"
                    })

                    // 监听/onchange
                    watch(hobby, (newVal, oldVal) => {
                        console.log(oldVal, "==>", newVal)
                    })

                    // 监听/onchange
                    watch(() => date.year, (newVal, oldVal) => {
                        console.log(oldVal, "==>", newVal)

                        if(date.year == "2024") {
                            console.log("2024")
                        }
                    })

                    return {
                        date,
                        hobby
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```    

=== "watchEffect"

    自动监听，监听所有

    ``` html linenums="1"
    <body>

        <div id="app">
            兴趣：
            <select v-model="hobby">
                <option value="">请选择</option>
                <option value="1">写作</option>
                <option value="2">画画</option>
                <option value="3">运动</option>
            </select>

            年：
            <select v-model="date.year">
                <option value="">请选择</option>
                <option value="2023">2023</option>
                <option value="2024">2024</option>
                <option value="2025">2025</option>
            </select>

            月：
            <select v-model="date.month">
                <option value="">请选择</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
            </select>
        </div>

        <script type="module">
            // 结构赋值
            import { createApp, reactive, ref, watchEffect } from './vue.esm-browser.js'

            createApp({
                // 设置响应式数据、方法等
                setup() {

                    const hobby = ref("")

                    const date = reactive({
                        year: "2023",
                        month: "11"
                    })

                    watchEffect(() => {
                        console.log("监听开始")

                        if(hobby.value == "2") {
                            console.log("画画")
                        }
                        if(date.year == "2024") {
                            console.log("2024")
                        }
                        if(date.month == "12") {
                            console.log("12")
                        }

                        console.log("监听结束")
                    })

                    return {
                        date,
                        hobby
                    }
                }
            }).mount("#app")
        </script>
    </body>
    ```    

=== "其他"

    再说        

## 四、组件

=== "组件定义"

    组件可以将UI拆分为独立、可复用的代码片段。就是 `.vue` 结尾的文件，其固有的结构如下示：

    ``` vue title="src/components/admin/Test.vue" linenums="1"
    <template>
        test测试
    </template>

    <script setup>

    </script>

    <style scoped>

    </style>
    ```

=== "组件导入"

    在 `home.vue` 中导入 Test子组件

    ``` vue title="src/views/admin/home.vue" linenums="1" hl_lines="2 6"
    <template>
        <Test />
    </template>

    <script setup>
    import Test from '@/components/admin/home/Test.vue';
    </script>

    <style scoped>

    </style>    
    ```

=== "父组件传数据给子组件"

    在 `home.vue` 父组件中传递参数给子组件，有两种方法传参
    
    - 在标签中 `key=value` 形式传参
    - 在标签中 `v-bind="对象"` 形式传参(:为其简写形式)

    ``` vue title="src/views/admin/home.vue" linenums="1" hl_lines="2 4-5"
    <template>
        <Test propsName="shafish" propsUrl="shafish.cn"/>
        home.vue 原来内容
        <!-- <Test2 v-bind="props"/> -->
        <Test2 :="props"/>
    </template>

    <script setup>
    improt { reactive } from 'vue'
    import Test from '@/components/admin/home/Test.vue';
    import Test2 from '@/components/admin/home/Test2.vue';

    const props = reactive({
        user: "graham",
        url: "graham.cn"
    })
    </script>

    <style scoped>

    </style>    
    ```

    在 `Test.vue` 子组件中用 `defineProps` 以数组形式取出即可

    ``` vue title="src/components/admin/Test.vue" linenums="1" hl_lines="6"
    <template>
        test测试
    </template>

    <script setup>
        const props = defineProps(["propsName", "propsUrl"])
        console.log(props)
    </script>

    <style scoped>

    </style>
    ```    

    在 `Test2.vue` 子组件中用 `defineProps` 以对象形式取出即可

    ``` vue title="src/components/admin/Test2.vue" linenums="1" hl_lines="6-13"
    <template>
        test2测试
    </template>

    <script setup>
        const props = defineProps({
            user: String,
            url: {
                type: String,
                required: true,
                default: "graham.cn"
            }
        })
        console.log(props)
    </script>

    <style scoped>

    </style>
    ```    

=== "子组件传数据给父组件"

    在 `Test3.vue` 子组件中用 `defineEmits` `emits` 中定义好即可

    ``` vue title="src/components/admin/Test3.vue" linenums="1" hl_lines="6-7"
    <template>
        test3测试
    </template>

    <script setup>
        const emits = defineEmits(["getWeb", "propsUrl"])
        emits("getWeb", {name: "shafish"} )
    </script>

    <style scoped>

    </style>
    ```      

    在父组件中使用 @ 接收对应事件

    ``` vue title="src/views/admin/home.vue" linenums="1" hl_lines="2 8-10"
    <template>
        <Test3 @getWeb="emitGetWeb"/>
    </template>

    <script setup>
    import Test3 from '@/components/admin/home/Test3.vue';

    const emitGetWeb = (data) => {
        console.log(data)
    }

    </script>

    <style scoped>

    </style>    
    ```      

=== "跨组件传数据"

    可以将父组件数据传给其下的所有组件（子组件、孙子组件等等）。

    在父组件中引入 `provide` 进行声明即可

    ``` vue title="src/views/admin/home.vue" linenums="1" hl_lines="6 18"
    <template>
        <Test4 />
    </template>

    <script setup>
    import { provide } from 'vue'
    import Test4 from '@/components/admin/home/Test4.vue';

    const web = {
        name: "shafish",
        url: "fisha.cn"
    }

    const userAdd = () => {
        console.log("add ++")
    }

    provide("provideWeb", web)
    provide("provideFuncUserAdd", userAdd)

    </script>

    <style scoped>

    </style>    
    ```       

    在子组件中引入 `inject` 接收即可。

    ``` vue title="src/components/admin/Test4.vue" linenums="1" hl_lines="9 11"
    <template>
        test4测试

        <button @click="userAdd">按钮点击触发父级函数</button>

    </template>

    <script setup>
        import { inject } from 'vue'

        const web = inject("provideWeb")
        console.log(web)

        const userAdd = inject("provideFuncUserAdd")
    </script>

    <style scoped>

    </style>
    ```   

=== "插槽"

    在父组件中定义代码片段，可在子组件中任意位置插入使用。

    - 匿名插槽：在子组件标签中编写内容，子组件对应位置引入 `<slot />` 即可
    - 具名插槽：在子组件标签中编写template内容，并配置 `v-slot:插槽名称`（简写为 #插槽名称），子组件对应位置引入 `<slot name="插槽名称" />` 即可。其也可接收子组件传递的数据


    ``` vue title="src/views/admin/home.vue" linenums="1" hl_lines="3-5 8-11"
    <template>
    父组件中内容a
        <Test5>
            <a href="shafish.cn"> 子组件Test5中显示的内容 </a>
        </Test5>
    父组件中内容b
        <Test6>
            <!-- <template v-slot:url> -->
            <template #url="data">
                Test6子组件传来的数据：{{data.url}}
                <a href="shafish.cn"> 子组件Test6中显示的内容 </a>
            </template>
        </Test6>
    父组件中内容c
    </template>

    <script setup>
    import Test5 from '@/components/admin/home/Test5.vue';
    import Test6 from '@/components/admin/home/Test6.vue';

    </script>

    <style scoped>

    </style>    
    ```        
    
    在 Test5子组件中使用匿名插槽

    ``` vue title="src/components/admin/Test5.vue" linenums="1" hl_lines="4"
    <template>
        子组件内容a

        <slot />

        子组件内容b
    </template>

    <script setup>

    </script>

    <style scoped>

    </style>
    ```   

    在 Test6子组件中使用具名插槽，以 `name="插槽名称"` 形式引入。并且定义 `url` 数据向父组件传递值

    ``` vue title="src/components/admin/Test6.vue" linenums="1" hl_lines="4"
    <template>
        子组件内容a

        <slot name="url" url="shafish.cn" />

        子组件内容b
    </template>

    <script setup>

    </script>

    <style scoped>

    </style>
    ```   

## 五、生命周期函数

组件从创建到销毁过程中调用的 `hook` 函数

``` vue
<template>

</template>

<script setup>
    import { onBeforeMounted, onMounted, onBeforeUpdated, onUpdated, onBeforeUnmounted, onUnmounted, onErrorCaptured } from 'vue'

    onMounted(() => {

    })

    onUpdated(() => {
        
    })
    
    ...   
</script>

<style scoped>

</style>
```

=== "挂载阶段"

    - `onBeforeMounted`: 当前组件未渲染到 `DOM`，可以执行一些初始值操作
    - `onMounted`: 当前组件挂载到 `DOM` 并完成渲染时触发，拉取组件数据

=== "更新阶段"

    - `onBeforeUpdated`：组件即将重新渲染时调用，执行一些参数判断
    - `onUpdated`：组件发生更新(响应式数据发生变化)时触发，数据监听等

=== "卸载阶段"

    - `onBeforeUnmounted`：组件从 `DOM` 销毁前调用，资源释放等操作
    - `onUnmounted`：组件从 `DOM` 移除并销毁后触发   

=== "错误处理"

    - `onErrorCaptured`: 捕获到错误时触发

## 六、项目要点记录

### 1. 目录结构

项目下src的目录结构

``` shell
src
├── App.vue      
├── main.js         # Vite 项目的入口文件，初始化 Vue 应用（创建 Vue 实例、注册全局插件、挂载应用到 DOM 等）
├── .env            # 项目环境变量定义
├── index.html      # 首页内容
├── jsconfig.json   # JavaScript 项目配置文件，主要用于为 VS Code 提供智能提示、代码导航和代码检查等功能
├── vite.config.js  # Vite 项目的配置文件，用于自定义 Vite 的行为
├── package-lock.json # npm自动生成的文件，用于锁定依赖包的版本
├── package.json    # 项目的核心配置文件，用于定义项目的元数据、依赖、脚本和其他配置
├── api             # 存放 api 接口相关代码
│   ├── aAPI.js       # a模块的api封装
│   └── bAPI.js       # b模块的api封装               
├── assets          # 存放静态资源
│   └── admin           # 项目平台的区分
│       ├── css     
│       └── js
├── components      # 存放全局可复用的组件
│   └── admin
├── router          # 存放路由的定义和配置
│   └── index.js        # 项目路由文件
├── stores          # 存放状态管理相关代码
│   └── admin
│       ├── axxx.js # a状态库定义    
│       └── bxxx.js # b状态库定义
├── utils           # 工具
│   └── xxxUtil.js
└── views           # 存放页面级别的组件
    └── admin
```

### 2. 配置环境变量

项目下创建 `.env` 文件即可，需要注意定义的变量需要以 `VITE_`开头

```
VITE_API_URL=http://127.0.0.1:8008
```

???- "使用示例" 

    使用时固定： `import.meta.env.VITE_`开头

    ``` js linenums="1" hl_lines="5"
    import axios from 'axios'
        
    const axiosInstance = axios.create({  // axios 实例  
        //baseURL: "http://127.0.0.1:8008",
        baseURL: import.meta.env.VITE_API_URL,
        timeout: 5000
    })
    ```

### 3. 配置pinia

安装：`npm install pinia pinia-plugin-persistedstate`

=== "项目导入"

    ``` js title="src/main.js" linenums="1"
    ...
    //Pinia
    import { createPinia } from 'pinia' //导入Pinia的createPinia方法,用于创建Pinia实例(状态管理库)
    import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
    const pinia = createPinia() //创建一个Pinia实例, 用于在应用中集中管理状态(store)
    pinia.use(piniaPluginPersistedstate) //将持久化存储插件添加到 pinia 实例上
    ...
    app.use(pinia) //将 Pinia 实例注册到 Vue 应用中
    ...
    ```

=== "创建用户相关的pinia store"

    ``` js title="src/stores/admin/admin.js" linenums="1"
    import { reactive } from 'vue'
    import { defineStore } from 'pinia'

    const useAdminStore = defineStore('admin', () => {
        const data = reactive({
            name:"",
            token:"",
            expireDate: "" //过期日期
        })

        const save = (name,token,expireDate) => {
            data.name = name
            data.token = token
            data.expireDate = expireDate
        }

        return {
            data,
            save
        }
    },
    {
        persist: true //持久化存储到 localStorage 中
    })

    export { useAdminStore }        
    ```    

=== "登录页中使用"

    ``` vue title="src/views/admin/login.vue" linenums="1" hl_lines="6 8-9"
    <template>
        <h3>登录页面</h3>
    </template>

    <script setup>
    import { useAdminStore } from '@/stores/admin/admin.js'

    const adminStore = useAdminStore()
    adminStore.save("shafish", "fisha_token", "2024-12")
    console.log(adminStore.data);
    </script>

    <style scoped>

    </style>    
    ```


### 4. 配置路由router

``` shell
npm install vue-router@4
```

=== "创建路由文件"

    ``` js title="src/router/index.js" linenums="1"
    import { createRouter, createWebHistory } from "vue-router"
    import { useAdminStore } from "@/stores/admin/admin.js" // 前面的 pinia

    // 路由规则
    const routes = [
        // 首页跳转
        {
            path: "/",
            redirect: "/admin"
        },
        // 登录页
        {
            path: "/login", // 对应路径
            component: () => import("../views/admin/login.vue") // 对应组件
        },
        // 后台页
        {
            path: "/admin", // 对应路径
            component: () => import("../views/admin/home.vue")， // 对应组件
            meta: {requiresAuth: true}， // 访问当前页面是否需要认证
            children: [ // 设置子路由，记得在 home.vue 的对应渲染位置添加 <router-view />
                // 管理员
                {
                    path: "adminstrator/add", // 访问路径：http://localhost:5173/admin/adminstrator/add
                    component: () => import("@/views/admin/adminstrator/add.vue") // 对应组件
                },
                {
                    path: "adminstrator/list",
                    component: () => import("@/views/admin/adminstrator/list.vue") // 对应组件
                },
                // 类别管理
                {
                    path: "category/list",
                    component: () => import("@/views/admin/category/list.vue") // 对应组件
                }
            ]
        }
    ]

    // 路由器
    const router = createRouter({
        history: createWebHistory(),
        routes
    })

    // 设置全局前置守卫(可用于页面拦截)
    // to：跳转后的页面
    // from：跳转前的页面
    // next：放行
    router.beforeEach((to, from, next) => {
        if (to.meta.requiresAuth) {
            console.log("访问的页面需要校验")

            const adminStore = useAdminStore()
            // 判断本地 token 是否存在，不存在就跳回登录
            if (adminStore.data.token === "") {
                router.push("/login")   // 页面跳转
            }
            next()
        } else {
            console.log("访问的页面无需校验")
            next()
        }

    })

    // 模块导出
    export default router    
    ```

=== "导入路由"

    ``` js title="src/main.js" linenums="1"
    ...
    import router from './router'
    ...
    app.use(router)
    ...
    ```

=== "创建路由对应页面"

    ``` vue title="src/views/admin/login.vue" linenums="1"
    <template>
        <h3>登录页面</h3>
    </template>

    <script setup>

    </script>

    <style scoped>

    </style>    
    ```

=== "渲染路由组件"

    ``` vue title="src/App.vue" linenums="1" hl_lines="5"
    <script setup>
    </script>

    <template>
        <router-view /> <!-- 指定渲染位置。把 login.vue 渲染到这里 -->
    </template>

    <style scoped>

    </style>   
    ```

=== "路由传参示例1：query字符串"

    路由文件中指定访问路由：`/content` 

    ``` js title="src/router/index.js" linenums="1" hl_lines="5 6"
    ...
    // 路由规则
    const routes = [
        {
            path: "/content", // 对应路径，完整访问路径 http://127.0.0.1:8001/content
            component: () => import("@/views/admin/content.vue") // 对应组件
        }
    ]
    ...
    ```

    当用户访问 `http://127.0.0.1:8001/content?name=shafish&nickName=graham` 时，对应 `content.vue` 中获取参数操作如下：
    
    - 在 `template` html内容中使用 `{{$route.query.x x x}}` 获取参数值
    - 在 `script` 中需要导入 `useRoute` 并初始化后才能用

    ``` vue title="src/views/admin/content.vue" linenums="1" hl_lines="2-3 12-13"
    <template>
        name: {{$route.query.name}} 
        nickName: {{$route.query.nickName}}
    </template>

    <script setup>
        // 导入路由
        import { useRoute } from 'vue-router'
        // 初始化
        const route = useRoute()
        // 获取参数
        let name = route.query.name
        let nickName = route.query.nickName
    </script>

    <style scoped>

    </style>
    ```

=== "路由传参示例2：路径传参"

    路由文件中指定访问路由：`/view` 

    ``` js title="src/router/index.js" linenums="1" hl_lines="9 10"
    ...
    // 路由规则
    const routes = [
        {
            path: "/content", // 对应路径，完整访问路径 http://127.0.0.1:8001/content
            component: () => import("@/views/admin/content.vue") // 对应组件
        },
        {
            path: "/view/:id", // 对应路径，完整访问路径 http://127.0.0.1:8001/view/1
            component: () => import("@/views/admin/view.vue") // 对应组件
        },
        {
            // 如果 :name 后不加 ? 则表示name为必传
            path: "/view2/:id/name/:name?", // 对应路径，完整访问路径 http://127.0.0.1:8001/view2/1/name/shafish
            component: () => import("@/views/admin/view2.vue") // 对应组件
        }        
    ]
    ...
    ```

    当用户访问 `http://127.0.0.1:8001/view/1` 时，对应 `view.vue` 中获取参数操作如下：
    
    - 在 `template` html内容中使用 `{{$route.params.id}}` 获取参数值
    - 在 `script` 中需要导入 `useRoute` 并初始化后才能用

    而当用户访问 `http://127.0.0.1:8001/view2/1/name/shafish` 时，对应 `view2.vue` 跟 `view.vue` 中操作类似

    ``` vue title="src/views/admin/view.vue" linenums="1" hl_lines="11"
    <template>
        id: {{$route.params.id}}
        <!-- name: {{$route.params.name}} -->
    </template>

    <script setup>
        // 导入路由
        import { useRoute } from 'vue-router'
        // 初始化
        const route = useRoute()
        // 获取参数
        let id = route.params.id
        // let name = route.params.name
    </script>

    <style scoped>

    </style>
    ```    

=== "router-link 标签使用"

    ``` vue title="src/views/admin/demo.vue" linenums="1"
    <template>
        <router-link to="/content?name=shafish&nickName=graham">query传参</router-link>
        <router-link to="/view/1">路径传参</router-link>

        <router-link :to="{path: '/content', query: {name: 'shafish', nickName: 'graham'}}">query传参(动态数据绑定)</router-link>
        <router-link :to="{name: 'member', params: {id: 1}}">:to 路径传参(使用路由命名)</router-link>
    </template>

    <script setup>

    </script>

    <style scoped>

    </style>    
    ```

    `router-link` 使用 `:to` 路径传参时还需要给对应路由命名

    ``` js title="src/router/index.js" linenums="1" hl_lines="10"
    ...
    // 路由规则
    const routes = [
        {
            path: "/content", // 对应路径，完整访问路径 http://127.0.0.1:8001/content
            component: () => import("@/views/admin/content.vue") // 对应组件
        },
        {
            path: "/view/:id", // 对应路径，完整访问路径 http://127.0.0.1:8001/view/1
            name: "member",
            component: () => import("@/views/admin/view.vue") // 对应组件
        },
        {
            // 如果 :name 后不加 ? 则表示name为必传
            path: "/view2/:id/name/:name?", // 对应路径，完整访问路径 http://127.0.0.1:8001/view2/1/name/shafish
            component: () => import("@/views/admin/view2.vue") // 对应组件
        }        
    ]
    ...
    ```

=== "路由跳转"

    初始化 `router` 后 `router.put` 即可，如果是返回上一级则是：`router.go(-1)`。

    注意：`import { useRoute, useRouter } from 'vue-router'` useRoute 用于获取参数， useRouter 用户路由跳转

    ``` vue title="src/views/admin/demo.vue" linenums="1"
    <template>
        <button @click="goTo"> 点击跳转 </button>
    </template>

    <script setup>
    import { useRouter } from 'vue-router'

    //初始化
    const router = useRouter()

    const goTo = () => {
        router.put("/content?name=shafish&nickName=graham")
    }
    </script>

    <style scoped>

    </style>    
    ```    


### 5. 配置路由@

用绝对路径取代路由规则中的相对路径

=== "渲染路由组件"

    ``` js title="src/vite.config.js" linenums="1"
    ...
    import path from 'path'

    // https://vite.dev/config/
    export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
        '@': path.resolve(__dirname, 'src')
        }
    }
    })   
    ```

=== "路径自动提示"

    ``` js title="src/jsconfig.json" linenums="1"
    {
        "compilerOptions": {
            "baseUrl": ".",
            "paths": {
                "@/*": ["src/*"] // 配置@指向src相关目录
            }
        }
    }
    ```    

=== "修改路由文件"

    ``` js title="src/router/index.js" linenums="1" hl_lines="7"
    import { createRouter, createWebHistory } from "vue-router"

    // 路由规则
    const routes = [
        {
            path: "/login", // 对应路径
            // component: () => import("../views/admin/login.vue") // 对应组件
            component: () => import("@/views/admin/login.vue") // 对应组件
        }
    ]

    // 路由器
    const router = createRouter({
        history: createWebHistory(),
        routes
    })

    // 模块导出
    export default router    
    ```

### 6. 安装element-plus

安装：`npm install element-plus --save`

=== "项目导入"

    ``` js title="src/main.js" linenums="1"
    ...
    //整体导入 ElementPlus 组件库
    import ElementPlus from 'element-plus' //导入 ElementPlus 组件库的所有模块和功能 
    import 'element-plus/dist/index.css' //导入 ElementPlus 组件库所需的全局 css 样式
    import * as ElementPlusIconsVue from '@element-plus/icons-vue' //导入 ElementPlus 组件库中的所有图标
    import zhCn from 'element-plus/dist/locale/zh-cn.mjs' //导入 ElementPlus 组件库的中文语言包
    ...
    //注册 ElementPlus 组件库中的所有图标到全局 Vue 应用中
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    app.use(ElementPlus, { //将 ElementPlus 插件注册到 Vue 应用中
        locale: zhCn // 设置 ElementPlus 组件库的区域语言为中文简体
    })
    ...
    ```

### 7. 配置axios

安装：`npm install axios`

=== "发起请求"

    ``` js title="src/views/admin/login.vue" linenums="1" hl_lines="28-29 59-72"
    <template>
        <div class="fish-login">
            <el-form :model="data" :rules="rules" ref="ruleFormRef">

                <div class="title">
                    FISH_CMS
                </div>

                <el-form-item label="账号" prop="name">
                    <el-input :prefix-icon="User" v-model="data.name" />
                </el-form-item>

                <el-form-item label="密码" prop="password">
                    <el-input type="password" :prefix-icon="Lock" placeholder="输入密码" show-password v-model="data.password" />
                </el-form-item>
                
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">Create</el-button>
                </el-form-item>            
            </el-form>
        </div>
    </template>

    <script setup>
        import '@/assets/admin/css/login.css' //导入样式
        import { reactive, ref } from 'vue'
        import { User,Lock } from '@element-plus/icons-vue' //图标
        import { ElMessage } from 'element-plus' // 弹窗提示
        import axios from 'axios' 

        const data = reactive({
            name: "",
            password: ""
        })

        const rules = {
            name: [
                {required: true, message: '请输入用户名', trigger: 'blur'},
                {min: 2, max: 10, message: '2~10内', trigger: 'blur'}
            ],
            password: [
                {required: true, message: '请输入密码', trigger: 'blur'}
            ]
        }

        // 引用form组件
        const ruleFormRef = ref()

        const onSubmit = () => {
            // console.log(data)
            // valid: 是否检验通过; fields：对应警告信息
            ruleFormRef.value.validate((valid, fields) => {
                console.log(valid, fields);
                
                if(!valid) { // 如果检验不通过则中断执行
                    return
                }

                axios.post('http://127.0.0.1:8008/api/adm/login', data).then(response => {
                    console.log(response.data);
                    
                    if(!response.data.status) {
                        ElMessage.error(response.data.msg)
                        return 
                    }

                    let token = response.data.data.token
                    console.log(token);
                }).catch(err => {
                    console.log(err);
                    
                })
            })
        }
    </script>

    <style scoped>

    </style>    
    ```

### 8. 项目打包

``` shell
# 生成 dist 目录
npm run build
```