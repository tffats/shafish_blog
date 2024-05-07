---
title: Mkdocs Material使用记录
authors:
    - shafish
date:
    created: 2021-12-20
    updated: 2024-02-02
categories:
    - Mkdocs
    - blog
---

[ :fishing_pole_and_fish: ](/)

## 一、markdown使用

### 0. 版本与升级

!!! tip

    版本升级须知：[https://squidfunk.github.io/mkdocs-material/upgrade/#upgrading-from-7x-to-8x](https://squidfunk.github.io/mkdocs-material/upgrade/#upgrading-from-7x-to-8x){target=_blank}

``` shell
# 查看当前安装的版本号
pip show mkdocs-material
# 升级到最新版本
pip install --upgrade mkdocs-material
```

<!-- more -->

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
theme:
  features:
    - content.code.copy
    - content.code.annotate  
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

=== "去掉代码复制按钮"

    ``` { .python .no-copy }
    def bubble_sort(items):
        for i in range(len(items)):
            for j in range(len(items) - 1 - i):
                if items[j] > items[j + 1]:
                    items[j], items[j + 1] = items[j + 1], items[j]
    ```

    ```
        ``` { .python .no-copy }
        def bubble_sort(items):
            for i in range(len(items)):
                for j in range(len(items) - 1 - i):
                    if items[j] > items[j + 1]:
                        items[j], items[j + 1] = items[j + 1], items[j]
        ```
    ```

=== "点击显示代码注解"

    > 注意要使用对应语言的注解符号对 `(num)`进行注解，比如java的//、python的#

    ``` { .java .no-copy }
    Optional<String> message = Optional.ofNullable(record.value()); // (1)
    if (message.isPresent()) {
        System.out.println(message.get());
    }
    ```

    1.  :man_raising_hand: optional判空处理.

    ```
        ``` { .java .no-copy }
        Optional<String> message = Optional.ofNullable(record.value()); // (1)
        if (message.isPresent()) {
            System.out.println(message.get());
        }
        ```

        1.  :man_raising_hand: optional判空处理.
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

    ![](https://picture.cdn.shafish.cn/blog/mkdocs-formate.png){: .zoom}
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

### 14. blog

> 博客插件，使站点blog化（文章摘要、存档、分类、分页等功能）

???- "一、blog插件引入：mkdocs.yml"

    在 `mkdocs.yml` 配置文件中引入 `blog` 插件，该插件已内置于 `Material for MkDocs 9.2.0 +`，直接用就行。 

    ``` yaml
    plugins:
        ...
        - blog
    ```

???- "二、blog目录说明"

    引入了blog插件后，如果不指定博客目录，mk会自动创建目录：
    ```
    ├─ docs/
    │  └─ blog/
    │     ├─ posts/
    │     └─ index.md
    └─ mkdocs.yml
    ```
    但是你也可以手动指定博客目录，比如：newBlog，该目录下必须包含 `posts` 目录与 `index.md`。mk会自动扫描posts目录下的.md文件作为博客视图内容；index.md文件则作为博客的入口，列出所有的博文。

    所以，如果指定特定目录作为博客目录，该目录可如下：（如果你也跟我一样，docs目录下已经占用了blog目录，要弄完第四点的 `blog_dir` 配置才能生效yo）
    ```
    ├─ docs/
    │  └─ ababababa/
    │     └─ newBlog/
    │         ├─ posts/
    │         └─ index.md
    └─ mkdocs.yml
    ```

???- "三、blog入口配置：mkdocs.yml"

    配置访问入口，也就是导航栏 `nav`。
    ``` yaml
    nav:
        - Blog:
            - ababababa/newBlog/index.md
    ```
    Blog可以随意名称，另外注意 `ababababa/newBlog/index.md` 是以docs相对定位der

???- "四、blog配置列表：mkdocs.yml"

    这里指定 `blog` 插件的各项配置，比如说博文的 显示时间格式、分页数量、存档路径等等。

    === "指定博客目录"

        ``` yaml
        plugins:
            - blog:
                blog_dir: ababababa/newBlog
        ```
        上面是指定博客目录的，如果使用默认的目录（也就是docs/blog），就是下面这样：

        ``` yaml
        plugins:
            - blog:
                blog_dir: blog
        ```
        如果只打算用blog，没有博客目录之外的文件，就可以直接这样：（建新的mkdocs就可以用这个，可惜blog插件出现晚了）

        ``` yaml
        plugins:
            - blog:
                blog_dir: .
        ```
        不出意外，当你配置完 `blog_dir`，等serve自动刷新后，再访问 `http://127.0.0.1:8000/Blog/` 就能看到博客内容了。（/Blog路径按照 `mkdocs.yml` 配置的nav 自行路由，看看你自己nav配了啥就用啥）

        这时的 `http://127.0.0.1:8000/Blog/` 是没啥内容的，因为你还没在 `ababababa/newBlog/posts` 目录下写 `.md` 博文呢。你可以看下下面第五点，或者直接复制下面内容作为 `ababababa/newBlog/posts/hello-world.md` 尝尝鲜：

        ``` markdown title="ababababa/newBlog/posts/hello-world.md"
        ---
        draft: true 
        date: 2024-01-31 
        hide:
        - navigation
        - toc
        categories:
        - Hello
        - World
        ---

        # Hello world!
        ...    
        ```

    === "博文的日期格式"

        ``` yaml
        # 2024年1月31日星期三
        plugins:
            - blog:
                post_date_format: full
        ```
        ``` yaml
        # 2024年1月31日
        plugins:
            - blog:
                post_date_format: long
        ```
        ``` yaml
        # 2024年1月31日
        plugins:
            - blog:
                post_date_format: medium
        ```
        ``` yaml
        # 2024/1/31
        plugins:
            - blog:
                post_date_format: short
        ```
        如果没有显示为中文的话，看看你配置 `language: zh` 了没。

    === "设置博文的访问路径"

        slug 是标题；date是日期；categories是标签；file直接是文件名。如果加上日期的话，文章的访问路径就会是这样：`http://127.0.0.1:8000/xxxx/2024/01/31/文章标题/`。建议直接 `slug` 就行。

        ``` yaml
        plugins:
            - blog:
                # post_url_format: "{date}/{slug}" # http://127.0.0.1:8000/xxxx/2024/01/31/hello-world/
                # post_url_format: "{categories}/{slug}" # http://127.0.0.1:8000/xxxx/Hello/world/hello-world/
                post_url_format: "{slug}"
        ```

        slug也就是文章的标题，默认是使用 `-` 拼接多个词的。比如：博文的标题是 `# hello world`，那么它的{slug}就是 `hello-world`，如果不想使用默认，也可以指定 `post_slugify_separator: xxx` 修改。

        categories也就是文章的标签，默认是使用第一个标签作为路径的。比如：博文的标签是 `Hello` 和 `world` 两个标签，如果此时的url格式是：`post_url_format: "{categories}/{slug}"`，那么此时的{categories}就是Hello，如果要显示多个标签的话，可以指定 `post_url_max_categories: num`。

        date也就是文章的创建日期，默认是 `yyyy/MM/dd` 的格式，可以指定 `post_url_date_format: yyyy/MM` 或者 `post_url_date_format: yyyy`

    === "设置是否必须设置摘要"

        不设置的话，会默认在博客首页显示文章所有内容哟

        ``` yaml
        plugins:
            - blog:
                post_excerpt: required
        ```

    === "文章摘要：默认<!-- more -->"

        截取文章前部分内容作为显示摘要，在适合的地方标上摘要符号： `<!-- more -->` 就能把从开头到摘要符号的这部分内容作为本篇博文的摘要内容。

        ``` yaml
        plugins:
            - blog:
                post_excerpt_separator: <!-- more -->
        ```

    === "显示关联的标签数量：默认5个"

        设置最多只显示两个标签

        ``` yaml
        plugins:
            - blog:
                post_excerpt_max_categories: 2
        ```

    === "显示关联的作者数量：默认1个"

        设置最多只显示两个作者

        ``` yaml
        plugins:
            - blog:
                post_excerpt_max_authors: 2
        ```

    === "文章归档名称"

        ``` yaml
        plugins:
            - blog:
                archive_name: shafish 的归档
        ```      

    === "文章归档日期格式：默认yyyy"

        按月归档，还是按年归档

        ``` yaml
        plugins:
            - blog:
                archive_date_format: MMMM yyyy
        ```             

    === "设置归档的访问路径"

        ``` yaml
        plugins:
            - blog:
                # archive_url_format: "{date}"
                archive_url_format: "archive/{date}"
        ```
        其中的 {date} 可以指定年或者月 `archive_url_date_format: yyyy/MM`

    === "文章标签名称"

        ``` yaml
        plugins:
            - blog:
                categories_name: shafish 的标签
        ```

    === "设置标签管理页的访问路径"

        ``` yaml
        plugins:
            - blog:
                categories_url_format: "category/{slug}"
        ```
        其中的 {slug} 就是文章的标签，跟文章标题一样，默认使用 `-` 分词，也可以使用 `categories_slugify_separator: xxx` 特别指定。

    === "规定博客使用的标签"

        不匹配就不会显示生效，比如规定了只有两个 `hello` `world` 这两个标签，所有文章就只能用这两个标签。

        ``` yaml
        plugins:
            - blog:
                categories_allowed:
                    - hello
                    - world
        ```        

    === "文章作者列表"

        可以选择该列表中配置好的作者（作者名称、头像、url等等都可以在这个文件配置）

        ``` yaml
        plugins:
            - blog:
                authors_file: "{blog}/.authors.yml"
        ```
        这里的 `{blog}` 指的是之前配置的博客目录。（也就是跟 `posts` 目录同级，在该同级目录中创建 `.authors.yml` 文件就行）
        ```
        ├─ docs/
        │  └─ ababababa/
        │     └─ newBlog/
        │         ├─ posts/
        │         ├─ .authors.yml   
        │         └─ index.md
        └─ mkdocs.yml
        ```
        ``` yaml title=".authors.yml"
        authors:
            shafish:
                name: shafish        # Author name
                description: a man # Author description
                avatar: http://xxx         # Author avatar
                # slug: url           # Author profile slug
                url: https://github.com/shafishcn   # Author website URL
        ```

    === "设置博文每页显示的文章数：默认10条"

        ``` yaml
        plugins:
            - blog:
                pagination_per_page: 15
        ```

    === "页码显示格式"

        下面的配置会显示成这样：`1 2 3 .. n > >>` 能点击到最后一页，emmmm

        ``` yaml
        plugins:
            - blog:
                pagination_format: "$link_first $link_previous ~2~ $link_next $link_last"
        ```

    === "是否开启草稿"

        草稿，也就是非正式的博文

        ``` yaml
        plugins:
            - blog:
                draft: true
        ```

    === "是否显示草稿：默认显示"

        ``` yaml
        plugins:
            - blog:
                draft: flase
        ```

    === "将草稿转为正式文章"

        标记文章为草稿，并设置文章的日期为明天，当今天部署更新好博客后，这篇文章就不会显示，当符合设置的日期才显示。（这个可以）

        ``` yaml
        plugins:
            - blog:
                draft_if_future_date: true
        ```          

???- "五、blog编写格式：posts目录"

    也就是`posts` 目录下的md文件的编写规范。

    === "设置博文作者"

        ``` markdown
        ---
        authors:
            - shafish
        ---

        # Post title
        ...
        ```
        作者列表得记得先在 `.authors.yml` 定义好

    === "设置博文标签"

        ``` markdown
        ---
        categories:
            - Search
            - Performance
        ---

        # Post title
        ...
        ```

    === "设置博文日期：必填"

        ``` markdown
        ---
        date:
            created: 2024-01-31
            updated: 2024-02-01
        ---

        # Post title
        ...
        ```

    === "设置博文为草稿"

        ``` markdown
        ---
        draft: true
        ---

        # Post title
        ...
        ```
    
    === "其他"
    
        略过

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

### 11. 评论系统-Giscus


### 12. blocks

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

ref: https://timvink.github.io/mkdocs-git-revision-date-localized-plugin/options/

=== "安装时间插件"

    ``` shell
    $ pip install mkdocs-git-revision-date-localized-plugin
    ```

=== "mkdocs.yml设置"

    ``` yaml
    # mkdocs.yml 相关配置
    plugins:
    - git-revision-date-localized:
        type: datetime
        #fallback_to_build_date: true
        enable_creation_date: true
    ```

=== "注意事项"

    ``` yaml
    # github action ci时需要注意事项
    # 指定容器当前时区
    env:
    TZ: Asia/Shanghai

    # checkout时需要指定fetch全部commit信息
    - name: Checkout master
    uses: actions/checkout@v2
    with:
        fetch-depth: 0 # 默认为1,表示只拉去最新commit信息，会导致当前文件显示的创建时间、更新时间都为commit的时间
    ```

### 2. 图片点击放大

=== "mkdocs.yml设置"

    ``` yaml
    extra_css:
        - stylesheets/extra.css
    extra_javascript:
        - javascripts/extra.js
    ```

=== "stylesheets/extra.css"

    ``` css
    /* 图片放大start */
    .shadow {
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    }

    .zoom {
        transition: transform ease-in-out 0.5s;
        cursor: zoom-in;
    }

    .image-zoom-large {
        transform: scale(1.9);
        cursor: zoom-out;
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
        z-index: 100;
        position: relative;
    }
    /* 图片放大end*/
    ```

=== "javascripts/extra.js"

    ``` js
    document.querySelectorAll('.zoom').forEach(item => {
        item.addEventListener('click', function () {
            this.classList.toggle('image-zoom-large');
        })
    });
    ```

=== "使用"

    ``` markdown
    ![](png src){: .zoom}
    ```

## 四、部署

[Github action部署到github page](./github_action.md)

[Jenkins部署到服务器](./jenkins.md)
