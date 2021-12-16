# 技术区简述

## mkdocs_material使用记录

### 一、markdown记录

#### 1. 鼠标经过显示术语提示

``` yml
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

#### 2. 引入独立的术语表（忽略）

!!! note
    显示效果跟上面示例1是一样的，区别是使用`--8<--`可以把术语的说明 独立在另外的文件，不必跟术语在同一文件中。

``` yml
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

#### 3. 提示、警告栏

``` yml
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

#### 4. 按钮

``` yml
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

### 二、mkdocs.yml记录

#### markdown配置
