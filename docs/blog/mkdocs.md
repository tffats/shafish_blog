---
title: Mkdocs Material使用记录
---

# Mkdocs Material使用记录

## 一、markdown使用

### 0. 版本与升级

!!! tip

    版本升级须知：[https://squidfunk.github.io/mkdocs-material/upgrade/#upgrading-from-7x-to-8x](https://squidfunk.github.io/mkdocs-material/upgrade/#upgrading-from-7x-to-8x)

``` shell
# 查看当前安装的版本号
pip show mkdocs-material
# 升级到最新版本
pip install --upgrade mkdocs-material
```

### 1. 显示术语提示

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - abbr
  - pymdownx.snippets
```

=== "Show"

    The HTML specification is maintained by the W3C.

    *[HTML]: Hyper Text Markup Language
    *[W3C]: World Wide Web Consortium

=== "Code"

    ``` markdown
    The HTML specification is maintained by the W3C.

    *[HTML]: Hyper Text Markup Language
    *[W3C]: World Wide Web Consortium
    ```

### 2. 引入独立的术语表（忽略）

!!! note
    显示效果跟上面示例1是一样的，区别是使用`--8<--`可以把术语的说明 独立在另外的文件，不必跟术语在同一文件中。

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - abbr
  - pymdownx.snippets
```

=== "Code"

    术语文件中引入术语表： `--8<-- "includes/abbreviations.md"`
    ``` markdown
    The HTML specification is maintained by the W3C.
    --8<-- "includes/abbreviations.md"
    ```

=== "Code"

    术语表:`includes/abbreviations.md`
    ``` markdown
    *[HTML]: Hyper Text Markup Language
    *[W3C]: World Wide Web Consortium
    ```

### 3. 提示、警告栏

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - admonition
  - pymdownx.details
  - pymdownx.superfences
```

=== "note"

    !!! note

        默认note标签。Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
        nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
        massa, nec semper lorem quam in massa.

    !!! note "Phasellus posuere in sem ut cursus"

        自定义标题的note标签。Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
        nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
        massa, nec semper lorem quam in massa.

    !!! note ""

        木有标题的note标签。Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
        nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
        massa, nec semper lorem quam in massa.

    ``` markdown
    !!! note

        默认note标签。Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
        nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
        massa, nec semper lorem quam in massa.

    !!! note "Phasellus posuere in sem ut cursus"

        自定义标题的note标签。Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
        nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
        massa, nec semper lorem quam in massa.

    !!! note ""

        木有标题的note标签。Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
        nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
        massa, nec semper lorem quam in massa.
    ```

=== "abstract"

    !!! abstract

        abstract, summary, tldr.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.

    ``` markdown
    !!! abstract

        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```

=== "info"
    
    !!! info
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! info

        info, todo.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```

=== "tip"
    
    !!! tip
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! tip

        tip, hint, important.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```    

=== "success"
    
    !!! success
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! success

        success, check, done.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```       

=== "question"
    
    !!! question
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! question

        question, help, faq.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```      

=== "warning"
    
    !!! warning
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! warning

        warning, caution, attention.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```          

=== "failure"
    
    !!! failure
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! failure

        failure, fail, missing.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```      

=== "danger"
    
    !!! danger
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! danger

        danger, error.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```     

=== "bug"
    
    !!! bug
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! bug

        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```     

=== "example"
    
    !!! example
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! example

        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```     

=== "quote"
    
    !!! quote
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    
    ``` markdown
    !!! quote

        quote, cite.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor massa, nec semper lorem quam in massa.
    ```     

### 4. 按钮

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - attr_list
```

=== "普通按钮"
    
    [Subscribe to our newsletter](#){ .md-button }
    
    ``` markdown
    [Subscribe to our newsletter](#){ .md-button }
    ```     

=== "primary 按钮"
    
    [Subscribe to our newsletter](#){ .md-button .md-button--primary }
    
    ``` markdown
    [Subscribe to our newsletter](#){ .md-button .md-button--primary }
    ```         

### 5. 代码块

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - pymdownx.highlight:
      anchor_linenums: true
  - pymdownx.inlinehilite
  - pymdownx.snippets
  - pymdownx.superfences
```

=== "指定文件名"

    ``` py title="bubble_sort.py"
    def bubble_sort(items):
        for i in range(len(items)):
            for j in range(len(items) - 1 - i):
                if items[j] > items[j + 1]:
                    items[j], items[j + 1] = items[j + 1], items[j]
    ```

    ``` 
        ``` py title="bubble_sort.py"
        def bubble_sort(items):
            for i in range(len(items)):
                for j in range(len(items) - 1 - i):
                    if items[j] > items[j + 1]:
                        items[j], items[j + 1] = items[j + 1], items[j]
        ```
    ```

=== "显示行数"

    ``` py linenums="1"
    def bubble_sort(items):
        for i in range(len(items)):
            for j in range(len(items) - 1 - i):
                if items[j] > items[j + 1]:
                    items[j], items[j + 1] = items[j + 1], items[j]
    ```

    ``` 
        ``` py linenums="1"
        def bubble_sort(items):
            for i in range(len(items)):
                for j in range(len(items) - 1 - i):
                    if items[j] > items[j + 1]:
                        items[j], items[j + 1] = items[j + 1], items[j]
        ```
    ```

=== "指定某两行高亮"

    ``` py linenums="1" hl_lines="2 4"
    def bubble_sort(items):
        for i in range(len(items)):
            for j in range(len(items) - 1 - i):
                if items[j] > items[j + 1]:
                    items[j], items[j + 1] = items[j + 1], items[j]
    ```

    ``` 
        ``` py linenums="1" hl_lines="2 4"
        def bubble_sort(items):
            for i in range(len(items)):
                for j in range(len(items) - 1 - i):
                    if items[j] > items[j + 1]:
                        items[j], items[j + 1] = items[j + 1], items[j]
        ```
    ```

=== "指定连续行高亮"

    ``` py linenums="1" hl_lines="2-5"
    def bubble_sort(items):
        for i in range(len(items)):
            for j in range(len(items) - 1 - i):
                if items[j] > items[j + 1]:
                    items[j], items[j + 1] = items[j + 1], items[j]
    ```

    ``` 
        ``` py linenums="1" hl_lines="2-5"
        def bubble_sort(items):
            for i in range(len(items)):
                for j in range(len(items) - 1 - i):
                    if items[j] > items[j + 1]:
                        items[j], items[j + 1] = items[j + 1], items[j]
        ```
    ```
### 6. 内容标签栏

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - pymdownx.superfences
  - pymdownx.tabbed:
      alternate_style: true 
```

=== "Show"

    === "C"

        ``` c
        #include <stdio.h>

        int main(void) {
        printf("Hello world!\n");
        return 0;
        }
        ```

    === "C++"

        ``` c++
        #include <iostream>

        int main(void) {
        std::cout << "Hello world!" << std::endl;
        return 0;
        }
        ```

=== "Code"

    ``` 
    === "C"

        ``` c
        #include <stdio.h>

        int main(void) {
        printf("Hello world!\n");
        return 0;
        }
        ```

    === "C++"

        ``` c++
        #include <iostream>

        int main(void) {
        std::cout << "Hello world!" << std::endl;
        return 0;
        }
        ```
    ```

### 7. 表格

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - tables
```

=== "Show"

    | Method      | Description                          |
    | :-----------: | :------------------------------------: |
    | `GET`       |      Fetch resource  |
    | `PUT`       |     Update resource |
    | `DELETE`    |     Delete resource |

=== "Code"

    ``` markdown
    | Method      | Description                          |
    | :-----------: | :------------------------------------: |
    | `GET`       |      Fetch resource  |
    | `PUT`       |     Update resource |
    | `DELETE`    |     Delete resource |
    ```

### 8. 注脚
在本页的底部显示注脚内容

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - footnotes
```

=== "Show"

    Lorem ipsum[^1] dolor sit amet, consectetur adipiscing elit.[^2]

    [^1]: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
    [^2]: Lorem ipsum dolor sit amet, consectetur adipiscing elit.

=== "Code"

    ``` markdown
    Lorem ipsum[^1] dolor sit amet, consectetur adipiscing elit.[^2]

    [^1]: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
    [^2]: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
    ```

### 9. 格式化处理

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - pymdownx.critic
  - pymdownx.caret
  - pymdownx.keys
  - pymdownx.mark
  - pymdownx.tilde
```

=== "删除线、下划线、注释、高亮"

    Text can be {--deleted--} and replacement text {++added++}. This can also be
    combined into {~~one~>a single~~} operation. {==Highlighting==} is also
    possible {>>and comments can be added inline<<}.

    {==

    Formatting can also be applied to blocks by putting the opening and closing
    tags on separate lines and adding new lines between the tags and the content.

    ==}

    - H~2~0
    - A^T^A

=== "Code"

    ![](https://picture.cdn.shafish.cn/blog/mkdocs-formate.png)
    ``` markdown 
    - H~2~0
    - A^T^A
    ``` 

### 10. 图标

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - pymdownx.emoji:
      emoji_index: !!python/name:materialx.emoji.twemoji
      emoji_generator: !!python/name:materialx.emoji.to_svg
```

=== "Show"

    :smile:  :dart:

=== "Code"

    ``` markdown
    :smile:  :dart:
    ```
支持以下图标：

- :material-material-design: – [Material Design]
- :fontawesome-brands-font-awesome: – [FontAwesome]
- :octicons-mark-github-16: – [Octicons]
- github markdown：[https://github.com/zhangjw-THU/Emoji](https://github.com/zhangjw-THU/Emoji)
- github commit：[https://github.com/shafishcn/git-commit-emoji-cn](https://github.com/shafishcn/git-commit-emoji-cn)

[Material Design]: https://materialdesignicons.com/
[FontAwesome]: https://fontawesome.com/icons?d=gallery&m=free
[Octicons]: https://octicons.github.com/

### 11. 图片

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - attr_list
  - md_in_html
```

=== "markdown格式展示图片"

    ![Image title](https://dummyimage.com/300x200/eee/aaa){ align=left }

    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
    nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
    massa, nec semper lorem quam in massa.

    ```markdown
    ![Image title](https://dummyimage.com/300x200/eee/aaa){ align=left }

    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla et euismod
    nulla. Curabitur feugiat, tortor non consequat finibus, justo purus auctor
    massa, nec semper lorem quam in massa.
    ```

=== "添加图片说明"

    <figure markdown> 
        ![Image title](https://dummyimage.com/600x400/){ width="300" }
        <figcaption>Image 说明</figcaption>
    </figure>

    ``` html
    <figure markdown> 
        ![Image title](https://dummyimage.com/600x400/){ width="300" }
        <figcaption>Image 说明</figcaption>
    </figure>
    ```

=== "懒加载图片"

    ![Image title](https://dummyimage.com/600x400/){ loading=lazy }

    ``` markdown
    ![Image title](https://dummyimage.com/600x400/){ loading=lazy }
    ```

### 12. 列表

``` yaml
# mkdocs.yml 相关配置
markdown_extensions:
  - def_list
  - pymdownx.tasklist:
      custom_checkbox: true
```

=== "无序列表"

    - Nulla et rhoncus turpis. Mauris ultricies elementum leo. Duis efficitur
    accumsan nibh eu mattis. Vivamus tempus velit eros, porttitor placerat nibh
    lacinia sed. Aenean in finibus diam.

        * Duis mollis est eget nibh volutpat, fermentum aliquet dui mollis.
        * Nam vulputate tincidunt fringilla.
        * Nullam dignissim ultrices urna non auctor.

    ``` markdown
    - Nulla et rhoncus turpis. Mauris ultricies elementum leo. Duis efficitur
    accumsan nibh eu mattis. Vivamus tempus velit eros, porttitor placerat nibh
    lacinia sed. Aenean in finibus diam.

        * Duis mollis est eget nibh volutpat, fermentum aliquet dui mollis.
        * Nam vulputate tincidunt fringilla.
        * Nullam dignissim ultrices urna non auctor.
    ```
    
=== "有序列表"

    1.  Vivamus id mi enim. Integer id turpis sapien. Ut condimentum lobortis
        sagittis. Aliquam purus tellus, faucibus eget urna at, iaculis venenatis
        nulla. Vivamus a pharetra leo.

        1.  Vivamus venenatis porttitor tortor sit amet rutrum. Pellentesque aliquet
            quam enim, eu volutpat urna rutrum a. Nam vehicula nunc mauris, a
            ultricies libero efficitur sed.

        2.  Morbi eget dapibus felis. Vivamus venenatis porttitor tortor sit amet
            rutrum. Pellentesque aliquet quam enim, eu volutpat urna rutrum a.

            1.  Mauris dictum mi lacus
            2.  Ut sit amet placerat ante
            3.  Suspendisse ac eros arcu

    ``` markdown
    1.  Vivamus id mi enim. Integer id turpis sapien. Ut condimentum lobortis
        sagittis. Aliquam purus tellus, faucibus eget urna at, iaculis venenatis
        nulla. Vivamus a pharetra leo.

        1.  Vivamus venenatis porttitor tortor sit amet rutrum. Pellentesque aliquet
            quam enim, eu volutpat urna rutrum a. Nam vehicula nunc mauris, a
            ultricies libero efficitur sed.

        2.  Morbi eget dapibus felis. Vivamus venenatis porttitor tortor sit amet
            rutrum. Pellentesque aliquet quam enim, eu volutpat urna rutrum a.

            1.  Mauris dictum mi lacus
            2.  Ut sit amet placerat ante
            3.  Suspendisse ac eros arcu 
    ```
    
=== "使用定义列表"

    `Lorem ipsum dolor sit amet`

    :   Sed sagittis eleifend rutrum. Donec vitae suscipit est. Nullam tempus
        tellus non sem sollicitudin, quis rutrum leo facilisis.

    `Cras arcu libero`

    :   Aliquam metus eros, pretium sed nulla venenatis, faucibus auctor ex. Proin
        ut eros sed sapien ullamcorper consequat. Nunc ligula ante.

        Duis mollis est eget nibh volutpat, fermentum aliquet dui mollis.
        Nam vulputate tincidunt fringilla.
        Nullam dignissim ultrices urna non auctor.

    ``` markdown
    `Lorem ipsum dolor sit amet`

    :   Sed sagittis eleifend rutrum. Donec vitae suscipit est. Nullam tempus
        tellus non sem sollicitudin, quis rutrum leo facilisis.

    `Cras arcu libero`

    :   Aliquam metus eros, pretium sed nulla venenatis, faucibus auctor ex. Proin
        ut eros sed sapien ullamcorper consequat. Nunc ligula ante.

        Duis mollis est eget nibh volutpat, fermentum aliquet dui mollis.
        Nam vulputate tincidunt fringilla.
        Nullam dignissim ultrices urna non auctor.
    ```        

=== "任务列表"

    - [x] Lorem ipsum dolor sit amet, consectetur adipiscing elit
    - [ ] Vestibulum convallis sit amet nisi a tincidunt
        * [x] In hac habitasse platea dictumst
        * [x] In scelerisque nibh non dolor mollis congue sed et metus
        * [ ] Praesent sed risus massa
    - [ ] Aenean pretium efficitur erat, donec pharetra, ligula non scelerisque

    ``` markdown
    - [x] Lorem ipsum dolor sit amet, consectetur adipiscing elit
    - [ ] Vestibulum convallis sit amet nisi a tincidunt
        * [x] In hac habitasse platea dictumst
        * [x] In scelerisque nibh non dolor mollis congue sed et metus
        * [ ] Praesent sed risus massa
    - [ ] Aenean pretium efficitur erat, donec pharetra, ligula non scelerisque
    ```

### 13. MathJax

略

## 二、mkdocs.yml记录

### 1. 调整颜色

<style>
.md-typeset button[data-md-color-accent] > code {
    background-color: var(--md-code-bg-color);
    color: var(--md-accent-fg-color);
}

.md-typeset button[data-md-color-primary] > code {
    background-color: var(--md-code-bg-color);
    color: var(--md-primary-fg-color);
}
</style>

=== "调整主题颜色"

    ``` yaml
    # mkdocs.yml 相关配置
    theme:
        palette:
            scheme: default
    ```

    默认只有两个选项可选：default（亮主题）、slate（暗主题）

    <div class="mdx-switch">
    <button data-md-color-scheme="default"><code>default</code></button>
    <button data-md-color-scheme="slate"><code>slate</code></button>
    </div>

    <script>
    var buttons = document.querySelectorAll("button[data-md-color-scheme]")
    buttons.forEach(function(button) {
        button.addEventListener("click", function() {
        var attr = this.getAttribute("data-md-color-scheme")
        document.body.setAttribute("data-md-color-scheme", attr)
        var name = document.querySelector("#__code_1 code span:nth-child(7)")
        name.textContent = attr
        })
    })
    </script>

=== "调整主色调"

    ``` yaml
    # mkdocs.yml 相关配置
    theme:
        palette:
            primary: indigo
    ```

    <div class="mdx-switch">
    <button data-md-color-primary="red"><code>red</code></button>
    <button data-md-color-primary="pink"><code>pink</code></button>
    <button data-md-color-primary="purple"><code>purple</code></button>
    <button data-md-color-primary="deep-purple"><code>deep purple</code></button>
    <button data-md-color-primary="indigo"><code>indigo</code></button>
    <button data-md-color-primary="blue"><code>blue</code></button>
    <button data-md-color-primary="light-blue"><code>light blue</code></button>
    <button data-md-color-primary="cyan"><code>cyan</code></button>
    <button data-md-color-primary="teal"><code>teal</code></button>
    <button data-md-color-primary="green"><code>green</code></button>
    <button data-md-color-primary="light-green"><code>light green</code></button>
    <button data-md-color-primary="lime"><code>lime</code></button>
    <button data-md-color-primary="yellow"><code>yellow</code></button>
    <button data-md-color-primary="amber"><code>amber</code></button>
    <button data-md-color-primary="orange"><code>orange</code></button>
    <button data-md-color-primary="deep-orange"><code>deep orange</code></button>
    <button data-md-color-primary="brown"><code>brown</code></button>
    <button data-md-color-primary="grey"><code>grey</code></button>
    <button data-md-color-primary="blue-grey"><code>blue grey</code></button>
    <button data-md-color-primary="black"><code>black</code></button>
    <button data-md-color-primary="white"><code>white</code></button>
    </div>

    <script>
    var buttons = document.querySelectorAll("button[data-md-color-primary]")
    buttons.forEach(function(button) {
        button.addEventListener("click", function() {
        var attr = this.getAttribute("data-md-color-primary")
        document.body.setAttribute("data-md-color-primary", attr)
        var name = document.querySelector("#__code_2 code span:nth-child(7)")
        name.textContent = attr.replace("-", " ")
        })
    })
    </script>


=== "调整hovered、buttons、scrollbars颜色"

    ``` yaml
    # mkdocs.yml 相关配置
    theme:
        palette:
            accent: indigo
    ```

    <div class="mdx-switch">
    <button data-md-color-accent="red"><code>red</code></button>
    <button data-md-color-accent="pink"><code>pink</code></button>
    <button data-md-color-accent="purple"><code>purple</code></button>
    <button data-md-color-accent="deep-purple"><code>deep purple</code></button>
    <button data-md-color-accent="indigo"><code>indigo</code></button>
    <button data-md-color-accent="blue"><code>blue</code></button>
    <button data-md-color-accent="light-blue"><code>light blue</code></button>
    <button data-md-color-accent="cyan"><code>cyan</code></button>
    <button data-md-color-accent="teal"><code>teal</code></button>
    <button data-md-color-accent="green"><code>green</code></button>
    <button data-md-color-accent="light-green"><code>light green</code></button>
    <button data-md-color-accent="lime"><code>lime</code></button>
    <button data-md-color-accent="yellow"><code>yellow</code></button>
    <button data-md-color-accent="amber"><code>amber</code></button>
    <button data-md-color-accent="orange"><code>orange</code></button>
    <button data-md-color-accent="deep-orange"><code>deep orange</code></button>
    </div>

    <script>
    var buttons = document.querySelectorAll("button[data-md-color-accent]")
    buttons.forEach(function(button) {
        button.addEventListener("click", function() {
        var attr = this.getAttribute("data-md-color-accent")
        document.body.setAttribute("data-md-color-accent", attr)
        var name = document.querySelector("#__code_3 code span:nth-child(7)")
        name.textContent = attr.replace("-", " ")
        })
    })
    </script>


=== "自定义主题颜色"

    定义一个叫shafish的主题色调
    ``` yaml
    # mkdocs.yml 相关配置
    theme:
        palette:
            scheme: shafish
        extra_css:
            - stylesheets/extra.css # docs/stylesheets/extra.css
    ```

    ``` css
    [data-md-color-scheme="shafish"] {
        --md-primary-fg-color:        #252632;
        --md-primary-fg-color--light: #ECB7B7;
        --md-primary-fg-color--dark:  #90030C;
    }
    ```

### 2. 调整字体

支持：[Google Font ](https://fonts.google.com/)

``` yaml
# mkdocs.yml 相关配置
theme:
    font:
        text: Roboto  # 常规字体
        code: Roboto Mono  #代码块字体
```

### 3. logo、favicon

``` yaml
# mkdocs.yml 相关配置
theme:
    logo: assets/logo.png # logo
    favicon: images/favicon.png  # favicon
```

### 4. 导航栏

``` yaml
# mkdocs.yml 相关配置
theme:
  features:
    #- navigation.instant # 点击内部链接时，不用全部刷新页面
    - navigation.tracking # 在url中使用标题定位锚点
    - navigation.tabs # 顶部显示导航顶层nav（也就是第一个节点）
    - navigation.tabs.sticky # 滚动是隐藏顶部nav，需要配合navigation.tabs使用
    - navigation.sections # nav节点缩进
    - navigation.expand # 不折叠左侧nav节点
    - navigation.indexes # 指定节点index pages ，跟instant不兼容
    - toc.integrate # 右侧生产目录
    - navigation.top # 一键回顶部
```

### 5. 搜索

ref：[https://squidfunk.github.io/mkdocs-material/setup/setting-up-site-search/](https://squidfunk.github.io/mkdocs-material/setup/setting-up-site-search/)

### 6. 网站访问统计
谷歌示例

- [Google Analytics](https://developers.google.com/analytics)

``` yaml
# mkdocs.yml 相关配置
extra:
  analytics:
    provider: google
    property: G-XXXXXXXXXX
```

### 7. metadata设置

可以设置特定的key-value到markdown文档中，mkdocs再进行对应解析
``` yaml
markdown_extensions:
  - meta
```

=== "页面标题"
    ``` markdown
    ---
    title: Lorem ipsum dolor sit amet 

    ---

    # Document title
    ...
    ```

=== "页面描述"
    ``` markdown
    ---
    description: Nullam urna elit, malesuada eget finibus ut, ac tortor. 

    ---

    # Document title
    ...
    ```

=== "隐藏nav、toc"

    ``` markdown
    ---
    hide:
        - navigation
        - toc
    ---

    # Document title
    ```

=== "隐藏评论"

    请先看第10点详细说明。
    ``` markdown
    ---
    vssue: ""
    ---

    # Document title
    ...
    ```

### 8. 顶部隐藏、发布通知

``` yaml
# mkdocs.yml 相关配置
theme:
  features:
    - header.autohide
  custom_dir: overrides  # 自定义主题设置。overrides目录与docs目录同级
```

``` html
<!-- overrides/main.html文件内容 -->
{% extends "base.html" %}

{% block announce %}
  <!-- Add announcement here, including arbitrary HTML -->
  通知：啦啦啦，shafish用新博客啦！！
{% endblock %}
```

### 9. 网站底部设置

=== "声明保留版权"

    ``` yaml
    # mkdocs.yml 相关配置
    copyright: Copyright &copy; 2017 - 2020 shafish.cn
    ```
    或者 在main.html中自定义：
    ``` html
    {% extends "base.html" %}

    {% block copyright %}
    <!-- Add copyright here, including arbitrary HTML -->
    {% endblock %}
    ```

=== "右下角加个联系方式"

    ``` yaml
    # mkdocs.yml 相关配置
    extra:
        social:
            -   icon: fontawesome/solid/paper-plane
                link: mailto:<shafish_cn@163.com>
                name: 邮箱地址
    ```

### 10. 评论系统-Vssue

ref:[Vssue](https://vssue.js.org/zh/guide/)

mkdocs-material 8.x将评论改为了自定义，需要在 `mkdocs.yml ` 中设置custom_dir目录，重写partials/content.html中的内容。

=== "overrides/partials/content.html"

    ``` html
    <!--
    Copyright (c) 2016-2021 Martin Donath <martin.donath@squidfunk.com>
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.
    -->

    <!-- Edit button -->
    {% if page.edit_url %}
    <a
    href="{{ page.edit_url }}"
    title="{{ lang.t('edit.link.title') }}"
    class="md-content__button md-icon"
    >
    {% include ".icons/material/pencil.svg" %}
    </a>
    {% endif %}

    <!--
    Hack: check whether the content contains a h1 headline. If it
    doesn't, the page title (or respectively site name) is used
    as the main headline.
    -->
    {% if not "\x3ch1" in page.content %}
    <h1>{{ page.title | d(config.site_name, true)}}</h1>
    {% endif %}

    <!-- Markdown content -->
    {{ page.content }}

    <!-- Source file information -->
    {% if page and page.meta and (
    page.meta.git_revision_date_localized or
    page.meta.revision_date
    ) %}
    {% include "partials/source-file.html" %}
    {% endif %}

    <!-- Get setting from mkdocs.yml, but allow page-level overrides -->
    <!-- 在不需要评论的页面设置 `vssue: "" ` 这个meta信息（需在mkdocs.yml中添加meta 这个pythonmarkdown 插件）即可关闭该页面的评论功能，比如在about页面关闭评论:

    ---
    vssue: ""
    ---

    # about

    -->
    {% set vssue = config.extra.vssue %}
    {% if page and page.meta and page.meta.vssue is string %}
    {% set vssue = page.meta.vssue %}
    {% endif %}

    <!-- Inject Disqus into current page -->
    {% if not page.is_homepage and vssue %}
        <div id="vssue"></div>

        <!--<link rel="stylesheet" href="https://unpkg.com/vssue/dist/vssue.min.css">
    OR: vue full build (runtime + compiler)  -->
    <script src="https://unpkg.com/vue/dist/vue.min.js"></script>
    <!-- Vssue Github build -->
    <script src="https://unpkg.com/vssue/dist/vssue.github.min.js"></script>

    <script>
        new Vue({
        el: '#vssue',
    
        data: {
            title: options => `${options.prefix}${document.title}`,
            
            options: {
            owner: 'shafishcn',
            repo: 'shafish_blog',
            clientId: 'xxx',
            clientSecret: 'xxx', // only required for some of the platforms
            prefix: '[BlogComment]',
            labels: [':sunny:'],
            issueContent: ({ url }) =>`这个 Issue 由 Vssue 自动创建，用来存储该页面的评论：${url}`
            },
        },

        template: `<vssue :title="title" :options="options"></vssue>`,
        })
    </script>
    {% endif %}
    ```

=== "mkdocs.yml"

    ``` yaml
    theme:
        custom_dir: overrides
    extra:
        vssue: shafish  # 随便填值，主要是设置{% set vssue = config.extra.vssue %}时，vssue初始值可用
    ```

### 11. blocks

ref: https://squidfunk.github.io/mkdocs-material/customization/

=== "使用方法"

    ``` html
    <!-- overrides/main.html ， 弄之前先配置好mkdocs.yml的custom_dir-->
    {% extends "base.html" %}

    {% block blockName %}
        xxx
    {% endblock %}
    ```

=== "mkdocs.yml"

    ``` yaml
    theme:
        custom_dir: overrides
    ```

=== "设置标题-htmltitle"

    ``` html
    {% extends "base.html" %}

    {% block htmltitle %}
        xxx
    {% endblock %}
    ```

=== "通知栏-announce"

    ``` html
    {% extends "base.html" %}

    {% block announce %}
        xxx
    {% endblock %}
    ```    


## 三、插件

https://facelessuser.github.io/pymdown-extensions/extensions


### 1. 显示文件最后修改时间

``` shell
$ pip install mkdocs-git-revision-date-localized-plugin
```

``` yaml
# mkdocs.yml 相关配置
plugins:
  - git-revision-date-localized:
      type: datetime
      fallback_to_build_date: true
      enable_creation_date: true
```

## 四、部署

[Github action部署到github page](2021/12/github_action.md)

[Jenkins部署到服务器](2021/12/jenkins.md)
