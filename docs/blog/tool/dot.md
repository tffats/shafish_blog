---
title: Dot模板小工具
hide:
  - navigation
  - toc
---

!!! quote

    轻量前端渲染框架doT，适用自定义数据和模板简单渲染的场合，详情语法及使用参考dot官网[^1]与美团相关解析[^2]

### 一、dot模板

<div class="highlight"><pre id="__code_0"><span></span><button class="md-clipboard md-icon" title="复制" data-clipboard-target="#__code_0 > textarea"></button><textarea id="fisha_dot_templ" rows="15" style="width:100%"><div>
    <p>name:{{= it.name}}</p>
    <p>age:{{= it.age}}</p>
    <p>hello:{{= it.sayHello() }}</p>
    {{~ it.arr:item}}
    <p>INSERT INTO aria2_file(REVISION,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME,NAME) VALUES('{{=item.version}}','{{=item.create}}','{{=item.createTime}}','{{=item.update}}','{{=item.updateTime}}','{{=item.name}}');</p>
    {{~}}
</div></textarea></pre></div>

### 二、dot模板数据
***仅仅支持json格式数据***

<div class="highlight"><pre id="__code_1"><span></span><button class="md-clipboard md-icon" title="复制" data-clipboard-target="#__code_1 > textarea"></button><textarea id="fisha_dot_jsondata" rows="15" style="width:100%">{
  "name":"stringParams1",
  "stringParams1":"stringParams1_value",
  "age": 21,
  "stringParams2":1,
  "arr":[
    {"version":0, "create":"shafish", "createTime":"2023-06-10 15:41:57", "update":"shafish", "updateTime":"2023-06-10 15:45:57", "name":"shafish26", "text":"val1值"}
  ]
}</textarea></pre></div>

### 三、结果
[执行][execu]{ .md-button}

<div class="highlight"><pre id="__code_2"><span></span><button class="md-clipboard md-icon" title="复制" data-clipboard-target="#__code_2 > code"></button><code id="fisha_dot_view"></code></pre></div>

<!-- <div id="fisha_dot_view" style="width:100%;border:2px solid #ff6e42;padding: 10px;border-radius:20px;"></div> -->

### 四、内置函数

``` js
sayHello = function () {
    return this[this.name]
}
```

[execu]: javascript:refreshDot()

[^1]: [https://github.com/olado/doT](https://github.com/olado/doT){target=_blank}
[^2]: [https://tech.meituan.com/dot.html](https://tech.meituan.com/dot.html){target=_blank}