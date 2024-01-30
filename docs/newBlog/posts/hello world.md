---
draft: false 
date: 2024-01-31 
categories:
  - Hello
  - World
---

# Hello world!

Skip to content
跳到内容
Material for MkDocs
Built-in blog plugin
Type to start searching
squidfunk/mkdocs-material

    9.5.6
    17.2k
    3.3k

    Home
     家
    Getting started
     开始
    Setup
     设置
    Plugins
     插件
    Reference
     参考
    Insiders
     业内 人士
    Blog
     博客

    Plugins  插件

Blog  博客
Group  群
Info  信息
Meta  元
Offline  离线
Optimize  优化
Privacy  隐私
Projects  项目
Search  搜索
Social  社会的
Tags  标签
Typeset  排版
Requirements  要求

Image processing  图像处理

            Caching  缓存
<!-- more -->
Table of contents
目录

    Objective  目的
        How it works  运作方式
        When to use it
        何时使用
    Configuration  配置
        Navigation  导航
        General  常规
            enabled
            blog_dir
            blog_toc
        Posts  职位
            post_dir
            post_date_format
            post_url_date_format
            post_url_format
            post_url_max_categories
            post_slugify
            post_slugify_separator
            post_excerpt
            post_excerpt_max_authors
            post_excerpt_max_categories
            post_excerpt_separator
            post_readtime
            post_readtime_words_per_minute
        Archive  档案
            archive
            archive_name
            archive_date_format
            archive_url_date_format
            archive_url_format
            archive_pagination
            archive_pagination_per_page
            archive_toc
        Categories  类别
            categories
            categories_name
            categories_url_format
            categories_slugify
            categories_slugify_separator
            categories_sort_by
            categories_sort_reverse
            categories_allowed
            categories_pagination
            categories_pagination_per_page
            categories_toc
        Authors  作者
            authors
            authors_file
            authors_profiles
            authors_profiles_name
            authors_profiles_url_format
            authors_profiles_pagination
            authors_profiles_pagination_per_page
            authors_profiles_toc
        Pagination  分页
            pagination
            pagination_per_page
            pagination_url_format
            pagination_format
            pagination_if_single_page
            pagination_keep_content
        Drafts  草稿
            draft
            draft_on_serve
            draft_if_future_date
    Usage  用法
        Metadata  元数据
            authors
            categories
            date
            draft
            links
            readtime
            slug

Built-in blog plugin¶ 内置博客插件 ¶

The blog plugin makes it very easy to build a blog, either as a sidecar to your documentation or as the main thing. Focus on your content while the plugin does all the heavy lifting, generating a view of all latest posts, archive and category pages, configurable pagination and much more.
博客插件使构建博客变得非常容易，无论是作为文档的附属工具还是作为主要内容。专注于您的内容，而插件会完成所有繁重的工作，生成所有最新帖子、存档和类别页面的视图、可配置的分页等等。
Objective¶ 目标 ¶
How it works¶ 工作原理 ¶

The plugin scans the configured posts directory for .md files from which paginated views1 are automatically generated. If not configured otherwise, the plugin expects that your project has the following directory layout, and will create any missing directories or files for you:
该插件会扫描配置 posts 的目录，以查找 .md 自动生成分页视图 1 的文件。如果未进行其他配置，则插件希望您的项目具有以下目录布局，并将为您创建任何缺少的目录或文件：

.
├─ docs/
│  └─ blog/
│     ├─ posts/
│     └─ index.md
└─ mkdocs.yml

The index.md file in the blog directory is the entry point to your blog – a paginated view listing all posts in reverse chronological order. Besides that, the plugin supports automatically creating archive and category pages that list a subset of posts for a time interval or category.
blog 目录中的 index.md 文件是博客的入口点 - 一个分页视图，按时间倒序列出所有帖子。除此之外，该插件还支持自动创建存档和类别页面，这些页面列出了某个时间间隔或类别的帖子子集。

Post URLs are completely configurable, no matter if you want your URLs to include the post's date or not. Rendered dates always display in the locale of the site language of your project. Like in other static blog frameworks, posts can be annotated with a variety of metadata, allowing for easy integration with other built-in plugins, e.g., the social and tags plugin.
帖子 URL 是完全可配置的，无论您是否希望您的 URL 包含帖子的日期。呈现的日期始终以项目网站语言的区域设置显示。与其他静态博客框架一样，帖子可以使用各种元数据进行注释，从而可以轻松地与其他内置插件集成，例如社交和标签插件。

Posts can be organized in nested folders with a directory layout that suits your specific needs, and can make use of all components and syntax that Material for MkDocs offers, including admonitions, annotations, code blocks, content tabs, diagrams, icons, math, and more.
帖子可以组织在嵌套文件夹中，其目录布局适合您的特定需求，并且可以利用 Material for MkDocs 提供的所有组件和语法，包括告诫、注释、代码块、内容选项卡、图表、图标、数学等。
When to use it¶
何时使用 ¶

If you want to add a blog to your project, or migrate from another blog framework to Material for MkDocs because of its excellent technical writing capabilities, this plugin is a great choice, as it integrates perfectly with many other built-in plugins:
如果您想将博客添加到您的项目中，或者由于其出色的技术写作能力而从另一个博客框架迁移到 Material for MkDocs，这个插件是一个不错的选择，因为它与许多其他内置插件完美集成：

  Built-in meta plugin 内置元插件

The meta plugin makes it easy to apply metadata to a subset of posts, including authors, tags, categories, draft status, as well as social card layouts.
元插件可以轻松地将元数据应用于帖子的子集，包括作者、标签、类别、草稿状态以及社交卡片布局。

Simpler organization, categorization and management of post metadata
更简化帖子元数据的组织、分类和管理

  Built-in social plugin 内置社交插件

The social plugin automatically generates beautiful and customizable social cards for each post and page, showing as previews on social media.
社交插件会自动为每个帖子和页面生成精美且可自定义的社交卡片，并在社交媒体上显示为预览。

Links to your blog render beautiful social cards when shared on social media
在社交媒体上分享时，指向您博客的链接会呈现精美的社交卡片

  Built-in optimize plugin
内置优化插件

The optimize plugin automatically identifies and optimizes all media files that you reference in your project by using compression and conversion techniques.
optimize 插件使用压缩和转换技术自动识别和优化您在项目中引用的所有媒体文件。

Your blog loads faster as smaller images are served to your users
随着向用户提供较小的图像，您的博客加载速度更快

      Built-in tags plugin 内置标签插件

    The tags plugin allows to categorize posts alongside with pages in your project, to improve their discoverability and connect posts to your documentation.
    标签插件允许将帖子与项目中的页面一起分类，以提高它们的可发现性并将帖子连接到您的文档。

    Your documentation's tag system integrates with your blog
    文档的标签系统与您的博客集成

Configuration¶ 配置 ¶

9.2.0
blog – built-in
9.2.0 博客 – 内置

As with all built-in plugins, getting started with the blog plugin is straightforward. Just add the following lines to mkdocs.yml, and you can start writing your first post:
与所有内置插件一样，博客插件的入门非常简单。只需将以下行添加到 mkdocs.yml 中，您就可以开始撰写您的第一篇文章：

plugins:
  - blog

The blog plugin is built into Material for MkDocs and doesn't need to be installed.
博客插件内置于 Material for MkDocs 中，无需安装。
Navigation¶ 导航 ¶

If you do not have site navigation configured in your mkdocs.yml then there is nothing more to do. The blog archive and category pages will automatically appear underneath the automatically generated navigation.
如果您没有在您的 mkdocs.yml 网站中配置网站导航，那么就没有什么可做的了。博客存档和类别页面将自动显示在自动生成的导航下方。

If you do have a navigation structure defined then you will need to specify where the blog should appear in this. Create a navigation section with an index page for the blog:
如果您确实定义了导航结构，则需要指定博客应在此处显示的位置。创建包含博客索引页的导航部分：

theme:
  name: material
  features:
    - navigation.indexes
nav:
  - ...
  - Blog:
    - blog/index.md

The archive and category pages will appear within that section as subsections beneath pages in the blog section. In this case, they would appear after index.md. The path to the index.md file must match blog_dir. This means that you can name the blog navigation entry anything you like: 'Blog' or 'News' or perhaps 'Tips'.
存档和类别页面将在该部分中显示为博客部分页面下方的子部分。在这种情况下，它们将出现在 index.md 之后。 index.md 文件的路径必须与blog_dir匹配。这意味着您可以将博客导航条目命名为您喜欢的任何名称：“博客”或“新闻”或“提示”。
General¶ 常规 ¶

The following settings are available:
可以使用以下设置：
enabled¶

9.2.0

true

Use this setting to enable or disable the plugin when building your project. It's normally not necessary to specify this setting, but if you want to disable the plugin, use:
使用此设置可在生成项目时启用或禁用插件。通常不需要指定此设置，但如果要禁用插件，请使用：

plugins:
  - blog:
      enabled: false

blog_dir¶

9.2.0

blog

Use this setting to change the path where your blog is located in the docs directory. The path is included in the generated URLs as a prefix for all posts and views. You can change it with:
使用此设置可更改博客在 docs 目录中所在的路径。该路径将作为所有帖子和视图的前缀包含在生成的 URL 中。您可以使用以下命令进行更改：
Documentation + Blog 文档 + 博客
Blog only 仅限博客

plugins:
  - blog:
      blog_dir: blog

The provided path is resolved from the docs directory.
提供的路径是从 docs 目录中解析的。
blog_toc¶

9.2.0

false

Use this setting to leverage the table of contents to display post titles in views. This might be useful, if your post excerpts are rather long. If you want to enable it, use:
使用此设置可利用目录在视图中显示帖子标题。如果您的帖子摘录很长，这可能会很有用。如果要启用它，请使用：

plugins:
  - blog:
      blog_toc: true

Posts¶ 帖子 ¶

The following settings are available for posts:
以下设置可用于帖子：
post_dir¶

9.2.0

{blog}/posts
# dxxxx
Use this setting to change the folder where your posts are located. It's normally not necessary to change this setting, but if you want to rename the folder or change its file system location, use:
使用此设置可更改帖子所在的文件夹。通常不需要更改此设置，但如果要重命名文件夹或更改其文件系统位置，请使用：

plugins:
  - blog:
      post_dir: "{blog}/articles"

Note that the posts directory is solely used for post organization – it is not included in post URLs, since they are automatically and comfortably generated by this plugin.
请注意，该 posts 目录仅用于帖子组织 - 它不包含在帖子 URL 中，因为它们是由此插件自动且舒适地生成的。

The following placeholders are available:
可以使用以下占位符：

    blog – blog directory
    blog – blog 目录

The provided path is resolved from the docs directory.
提供的路径是从 docs 目录中解析的。
post_date_format¶

9.2.0

long

Use this setting to change the date format of posts. This plugin uses babel to render dates in the configured site language. You can use babel's pattern syntax or the following shortcodes:
使用此设置可更改帖子的日期格式。此插件使用 babel 以配置的站点语言呈现日期。你可以使用 babel 的模式语法或以下简码：
Monday, January 31, 2024
週一， 31 一月 2024
January 31, 2024 1月 31， 2024
Jan 31, 2024 1月 31， 2024
1/31/24

plugins:
  - blog:
      post_date_format: full

Note that depending on the site language, results might look different for other languages.
请注意，根据网站语言的不同，其他语言的结果可能会有所不同。
post_url_date_format¶

9.2.0

yyyy/MM/dd

Use this setting to change the date format used in post URLs. The format string must adhere to babel's pattern syntax and should not contain whitespace. Some popular choices:
使用此设置可更改帖子网址中使用的日期格式。格式字符串必须遵循 babel 的模式语法，并且不应包含空格。一些受欢迎的选择：
blog/2024/01/31/
/ 博客/2024/01/31//
blog/2024/01/
/ 博客/2024/01//
blog/2024/
/ 博客/2024//

plugins:
  - blog:
      post_url_date_format: yyyy/MM/dd

If you want to remove the date from post URLs, e.g., when your blog features mostly evergreen content, you can remove the date placeholder from the post_url_format format string.
如果您想从帖子 URL 中删除日期，例如，当您的博客主要包含常青内容时，您可以从格式字符串中删除 date post_url_format 占位符。
post_url_format¶

9.2.0

{date}/{slug}

Use this setting to change the format string that is used when generating post URLs. You can freely combine placeholders, and join them with slashes or other characters:
使用此设置可以更改生成帖子 URL 时使用的格式字符串。您可以自由组合占位符，并用斜杠或其他字符连接它们：
blog/2024/
/ 博客/2024//
blog/
/ 博客//

plugins:
  - blog:
      post_url_format: "{slug}"

The following placeholders are available:
可以使用以下占位符：

    categories – Post categories, slugified with categories_slugify
    categories – 帖子类别，用 categories_slugify
    date – Post date, formatted with post_url_date_format
    date – 发布日期，格式化为 post_url_date_format
    slug – Post title, slugified with post_slugify, or explicitly set via slug metadata property
    slug – 帖子标题，带有 post_slugify 或通过 slug 元数据属性显式设置
    file – Post filename without .md file extension
    file – 发布不带 .md 文件扩展名的文件名

If you remove the date placeholder, make sure that post URLs don't collide with URLs of other pages hosted under the blog directory, as this leads to undefined behavior.
如果移除占 date 位符，请确保帖子 URL 不会与 blog 目录下托管的其他网页的 URL 发生冲突，因为这会导致未定义的行为。
post_url_max_categories¶

9.2.0

1

Use this setting to set an upper bound for the number of categories included in post URLs if the categories placeholder is part of post_url_format and the post defines categories:
使用此设置可以设置帖子网址中包含的类别数量的上限（如果 categories 占位符是 post_url_format 其中的一部分，并且帖子定义了类别）：

plugins:
  - blog:
      post_url_format: "{categories}/{slug}"
      post_url_max_categories: 2

If more than one category is given, they are joined with / after slugifying.
如果给出多个类别，则 / 在蛞蝓化后将它们连接起来。
post_slugify¶

9.2.0

pymdownx.slugs.slugify

Use this setting to change the function for generating URL-compatible slugs from post titles. By default, the slugify function from Python Markdown Extensions is used as follows:
使用此设置可更改从帖子标题生成与 URL 兼容的 slug 的功能。默认情况下，Python Markdown Extensions 中的 slugify 函数使用如下：

plugins:
  - blog:
      post_slugify: !!python/object/apply:pymdownx.slugs.slugify
        kwds:
          case: lower

The default configuration is Unicode-aware and should produce good slugs for all languages. Of course, you can also provide a custom slugification function for more granular control.
默认配置是 Unicode 感知的，应该为所有语言生成良好的 slugs。当然，您也可以提供自定义的排污功能，以实现更精细的控制。
post_slugify_separator¶

9.2.0

-

Use this setting to change the separator that is passed to the slugification function set as part of post_slugify. While the default is a hyphen, it can be set to any string, e.g., _:
使用此设置可以更改作为 的一部分传递给 的 slugization 函数集 post_slugify 的分隔符。虽然默认值为连字符，但可以将其设置为任何字符串，例如： _

plugins:
  - blog:
      post_slugify_separator: _

post_excerpt¶

9.2.0

optional

By default, the plugin makes post excerpts optional. When a post doesn't define an excerpt, views include the entire post. This setting can be used to make post excerpts required:
默认情况下，该插件将帖子摘录设置为可选。如果帖子没有定义摘录，则浏览量会包含整个帖子。此设置可用于使帖子摘录成为必需的：
Optional 自选
Required 必填

plugins:
  - blog:
      post_excerpt: required

When post excerpts are required, posts without excerpt separators raise an error. Thus, this setting is useful when you want to make sure that all posts have excerpts defined.
当需要帖子摘录时，没有摘录分隔符的帖子会引发错误。因此，当您想要确保所有帖子都定义了摘录时，此设置非常有用。
post_excerpt_max_authors¶

9.2.0

1

Use this setting to set an upper bound for the number of authors rendered in post excerpts. While each post may be written by multiple authors, this setting allows to limit the display to just a few or even a single author, or disable authors in post excerpts:
使用此设置可以设置文章摘录中呈现的作者数量的上限。虽然每篇文章可能由多个作者撰写，但此设置允许将显示限制为仅显示几个甚至单个作者，或禁用帖子摘录中的作者：
Render up to 2 authors
最多可呈现 2 位作者
Disable authors 禁用作者

plugins:
  - blog:
      post_excerpt_max_authors: 2

This only applies to post excerpts in views. Posts always render all authors.
这仅适用于视图中的帖子摘录。帖子始终呈现所有作者。
post_excerpt_max_categories¶

9.2.0

5

Use this setting to set an upper bound for the number of categories rendered in post excerpts. While each post may be assigned to multiple categories, this setting allows to limit the display to just a few or even a single category, or disable categories in post excerpts:
使用此设置可以设置帖子摘录中呈现的类别数的上限。虽然每个帖子可以分配给多个类别，但此设置允许将显示限制为仅几个甚至单个类别，或禁用帖子摘录中的类别：
Render up to 2 categories
渲染多达 2 个类别
Disable categories 禁用类别

plugins:
  - blog:
      post_excerpt_max_categories: 2

This only applies to post excerpts in views. Posts always render all categories.
这仅适用于视图中的帖子摘录。帖子始终呈现所有类别。
post_excerpt_separator¶

9.2.0

<!-- more -->

Use this setting to set the separator the plugin will look for in a post's content when generating post excerpts. All content before the separator is considered to be part of the excerpt:
使用此设置可设置插件在生成帖子摘录时将在帖子内容中查找的分隔符。分隔符之前的所有内容都被视为摘录的一部分：

plugins:
  - blog:
      post_excerpt_separator: <!-- more -->

It is common practice to use an HTML comment as a separator.
通常的做法是使用 HTML 注释作为分隔符。
post_readtime¶

9.2.0

true

Use this setting to control whether the plugin should automatically compute the reading time of a post, which is then rendered in post excerpts, as well as in posts themselves:
使用此设置可以控制插件是否应自动计算帖子的阅读时间，然后将其呈现在帖子摘录以及帖子本身中：

plugins:
  - blog:
      post_readtime: false

post_readtime_words_per_minute¶

9.2.0

265

Use this setting to change the number of words that a reader is expected to read per minute when computing the reading time of a post. If you want to fine-tune it, use:
使用此设置可以更改读者在计算文章阅读时间时每分钟应阅读的字数。如果要对其进行微调，请使用：

plugins:
  - blog:
      post_readtime_words_per_minute: 300

A reading time of 265 words per minute is considered to be the average reading time of an adult.
每分钟 265 个单词的阅读时间被认为是成年人的平均阅读时间。
Archive¶ 存档 ¶

The following settings are available for archive pages:
以下设置可用于存档页面：
archive¶

9.2.0

true

Use this setting to enable or disable archive pages. An archive page shows all posts for a specific interval (e.g. year, month, etc.) in reverse order. If you want to disable archive pages, use:
使用此设置可启用或禁用存档页面。存档页面以相反的顺序显示特定时间间隔（例如年、月等）的所有帖子。如果要禁用存档页面，请使用：

plugins:
  - blog:
      archive: false

archive_name¶

9.2.0

Use this setting to change the title of the archive section the plugin adds to the navigation. If this setting is omitted, it's sourced from the translations. If you want to change it, use:
使用此设置可更改插件添加到导航的存档部分的标题。如果省略此设置，则它来自翻译。如果要更改它，请使用：

plugins:
  - blog:
      archive_name: Archive

archive_date_format¶

9.2.0

yyyy

Use this setting to change the date format used for archive page titles. The format string must adhere to babel's pattern syntax. Some popular choices:
使用此设置可以更改用于存档页面标题的日期格式。格式字符串必须遵循 babel 的模式语法。一些受欢迎的选择：
2024
January 2024 2024 年 1 月

plugins:
  - blog:
      archive_date_format: MMMM yyyy

Note that depending on the site language, results might look different for other languages.
请注意，根据网站语言的不同，其他语言的结果可能会有所不同。
archive_url_date_format¶

9.2.0

yyyy

Use this setting to change the date format used for archive page URLs. The format string must adhere to babel's pattern syntax and should not contain whitespace. Some popular choices:
使用此设置可以更改用于存档页面 URL 的日期格式。格式字符串必须遵循 babel 的模式语法，并且不应包含空格。一些受欢迎的选择：
blog/archive/2024/ 博客/存档/2024/
blog/archive/2024/01/ 博客/存档/2024/01/

plugins:
  - blog:
      archive_url_date_format: yyyy/MM

archive_url_format¶

9.2.0

archive/{date}

Use this setting to change the format string that is used when generating archive page URLs. You can freely combine placeholders, and join them with slashes or other characters:
使用此设置可以更改生成存档页面 URL 时使用的格式字符串。您可以自由组合占位符，并用斜杠或其他字符连接它们：
blog/archive/2024/ 博客/存档/2024/
blog/2024/ 博客/2024/

plugins:
  - blog:
      archive_url_format: "archive/{date}"

The following placeholders are available:
可以使用以下占位符：

    date – Archive date, formatted with archive_url_date_format
    date – 存档日期，格式化为 archive_url_date_format

archive_pagination¶

insiders-4.44.0

true
内部人员-4.44.0 true

Use this setting to enable or disable pagination for archive pages. The value of this setting is inherited from pagination, unless it's explicitly set. To disable pagination, use:
使用此设置可以启用或禁用存档页面的分页。除非显式设置，否则此设置的值继承自 pagination 。要禁用分页，请使用：

plugins:
  - blog:
      archive_pagination: false

archive_pagination_per_page¶

insiders-4.44.0

10
内部人员-4.44.0 10

Use this setting to change the number of posts rendered per archive page. The value of this setting is inherited from pagination_per_page, unless it's explicitly set. To change it, use:
使用此设置可更改每个存档页面呈现的帖子数。除非显式设置，否则此设置的值继承自 pagination_per_page 。若要更改它，请使用：

plugins:
  - blog:
      archive_pagination_per_page: 5

archive_toc¶

9.2.0

false

Use this setting to leverage the table of contents to display post titles on all archive pages. The value of this setting is inherited from blog_toc, unless it's explicitly set. To change it, use
使用此设置可利用目录在所有存档页面上显示帖子标题。除非显式设置，否则此设置的值继承自 blog_toc 。要更改它，请使用

plugins:
  - blog:
      archive_toc: true

Categories¶ 分类 ¶

The following settings are available for category pages:
以下设置可用于类别页面：
categories¶

9.2.0

true

Use this setting to enable or disable category pages. A category page shows all posts for a specific category in reverse chronological order. If you want to disable category pages, use:
使用此设置可以启用或禁用类别页面。类别页面按时间倒序显示特定类别的所有帖子。如果要禁用类别页面，请使用：

plugins:
  - blog:
      categories: false

categories_name¶

9.2.0

Use this setting to change the title of the category section the plugin adds to the navigation. If this setting is omitted, it's sourced from the translations. If you want to change it, use:
使用此设置可更改插件添加到导航的类别部分的标题。如果省略此设置，则它来自翻译。如果要更改它，请使用：

plugins:
  - blog:
      categories_name: Categories

categories_url_format¶

9.2.0

category/{slug}

Use this setting to change the format string that is used when generating category page URLs. You can freely combine placeholders, and join them with slashes or other characters:
使用此设置可更改生成类别页面 URL 时使用的格式字符串。您可以自由组合占位符，并用斜杠或其他字符连接它们：
blog/category/
/ 博客/分类//
blog/
/ 博客//

plugins:
  - blog:
      categories_url_format: "category/{slug}"

The following placeholders are available:
可以使用以下占位符：

    slug – Category, slugified with categories_slugify
    slug – 类别，用 categories_slugify

categories_slugify¶

9.2.0

pymdownx.slugs.slugify

Use this setting to change the function for generating URL-compatible slugs from categories. By default, the slugify function from Python Markdown Extensions is used as follows:
使用此设置可更改用于从类别生成与 URL 兼容的 slug 的函数。默认情况下，Python Markdown Extensions 中的 slugify 函数使用如下：

plugins:
  - blog:
      post_slugify: !!python/object/apply:pymdownx.slugs.slugify
        kwds:
          case: lower

The default configuration is Unicode-aware and should produce good slugs for all languages. Of course, you can also provide a custom slugification function for more granular control.
默认配置是 Unicode 感知的，应该为所有语言生成良好的 slugs。当然，您也可以提供自定义的排污功能，以实现更精细的控制。
categories_slugify_separator¶

9.2.0

-

Use this setting to change the separator that is passed to the slugification function set as part of categories_slugify. While the default is a hyphen, it can be set to any string, e.g., _:
使用此设置可以更改作为 的一部分传递给 的 slugization 函数集 categories_slugify 的分隔符。虽然默认值为连字符，但可以将其设置为任何字符串，例如： _

plugins:
  - blog:
      categories_slugify_separator: _

categories_sort_by¶

insiders-4.45.0

material.plugins.blog.view_name
内部人员-4.45.0 material.plugins.blog.view_name

Use this setting to specify a custom function for sorting categories. For example, if you want to sort categories by the number of posts they contain, use the following configuration:
使用此设置可以指定用于对类别进行排序的自定义函数。例如，如果要按类别包含的帖子数对类别进行排序，请使用以下配置：

plugins:
  - blog:
      categories_sort_by: !!python/name:material.plugins.blog.view_post_count

Don't forget to enable categories_sort_reverse. You can define your own comparison function, which must return something that can be compared while sorting, i.e., a string or number.
不要忘记启用 categories_sort_reverse .您可以定义自己的比较函数，该函数必须返回排序时可以比较的内容，即字符串或数字。
categories_sort_reverse¶

insiders-4.45.0

false
内部人员-4.45.0 false

Use this setting to reverse the order in which categories are sorted. By default, categories are sorted in ascending order, but you can reverse ordering as follows:
使用此设置可以颠倒类别的排序顺序。默认情况下，类别按升序排序，但您可以按如下方式反向排序：

plugins:
  - blog:
      categories_sort_reverse: true

categories_allowed¶

9.2.0

The plugin allows to check categories against a predefined list, in order to catch typos or make sure that categories are not arbitrarily added. Specify the categories you want to allow with:
该插件允许根据预定义的列表检查类别，以捕获拼写错误或确保不会任意添加类别。指定要允许的类别：

plugins:
  - blog:
      categories_allowed:
        - Search
        - Performance

The plugin stops the build if a post references a category that is not part of this list. Posts can be assigned to categories by using the categories metadata property.
如果帖子引用了不属于此列表的类别，则插件将停止构建。可以使用 categories 元数据属性将帖子分配给类别。
categories_pagination¶

insiders-4.44.0

true
内部人员-4.44.0 true

Use this setting to enable or disable pagination for category pages. The value of this setting is inherited from pagination, unless it's explicitly set. To disable pagination, use:
使用此设置可以启用或禁用类别页面的分页。除非显式设置，否则此设置的值继承自 pagination 。要禁用分页，请使用：

plugins:
  - blog:
      categories_pagination: false

categories_pagination_per_page¶

insiders-4.44.0

10
内部人员-4.44.0 10

Use this setting to change the number of posts rendered per category page. The value of this setting is inherited from pagination_per_page, unless it's explicitly set. To change it, use:
使用此设置可更改每个类别页面呈现的帖子数。除非显式设置，否则此设置的值继承自 pagination_per_page 。若要更改它，请使用：

plugins:
  - blog:
      categories_pagination_per_page: 5

categories_toc¶

9.2.0

false

Use this setting to leverage the table of contents to display post titles on all category pages. The value of this setting is inherited from blog_toc, unless it's explicitly set. To change it, use:
使用此设置可利用目录在所有类别页面上显示帖子标题。除非显式设置，否则此设置的值继承自 blog_toc 。若要更改它，请使用：

plugins:
  - blog:
      categories_toc: true

Authors¶ 作者 ¶

The following settings are available for authors:
以下设置可供作者使用：
authors¶

9.2.0

true

Use this setting to enable or disable post authors. If this setting is enabled, the plugin will look for a file named .authors.yml and render authors in posts and views. Disable this behavior with:
使用此设置可启用或禁用文章作者。如果启用此设置，插件将查找一个名为 .authors.yml 的文件，并在帖子和视图中呈现作者。使用以下命令禁用此行为：

plugins:
  - blog:
      authors: false

authors_file¶

9.2.0

{blog}/.authors.yml

Use this setting to change the path of the file where the author information for your posts resides. It's normally not necessary to change this setting, but if you need to, use:
使用此设置可以更改帖子的作者信息所在的文件的路径。通常不需要更改此设置，但如果需要，请使用：

plugins:
  - blog:
      authors_file: "{blog}/.authors.yml"

The following placeholders are available:
可以使用以下占位符：

    blog – blog directory
    blog – blog 目录

The provided path is resolved from the docs directory.
提供的路径是从 docs 目录中解析的。

Format of author information
作者信息的格式

The .authors.yml file must adhere to the following format:
该 .authors.yml 文件必须遵循以下格式：
.authors.yml

authors:
  <author>:
    name: string        # Author name
    description: string # Author description
    avatar: url         # Author avatar
    slug: url           # Author profile slug
    url: url            # Author website URL

Note that <author> must be set to an identifier for associating authors with posts, e.g., a GitHub username like squidfunk. This identifier can then be used in the authors metadata property of a post. Multiple authors are supported. As an example, see the .authors.yml file we're using for our blog.
请注意， <author> 必须设置为用于将作者与帖子关联的标识符，例如 GitHub 用户名，例如 squidfunk .然后，可以在帖子的 authors 元数据属性中使用此标识符。支持多个作者。例如，请参阅我们用于博客 .authors.yml 的文件。
authors_profiles¶

insiders-4.46.0

false
内部人员-4.46.0 false

Use this setting to enable or disable automatically generated author profiles. An author profile shows all posts by an author in reverse chronological order. You can enable author profiles with:
使用此设置可启用或禁用自动生成的作者配置文件。作者个人资料按时间倒序显示作者的所有帖子。您可以使用以下方式启用作者配置文件：

plugins:
  - blog:
      authors_profiles: true

authors_profiles_name¶

insiders-4.46.0 内部人员-4.46.0

Use this setting to change the title of the authors section the plugin adds to the navigation. If this setting is omitted, it's sourced from the translations. If you want to change it, use:
使用此设置可更改插件添加到导航中的作者部分的标题。如果省略此设置，则它来自翻译。如果要更改它，请使用：

plugins:
  - blog:
      authors_profiles_name: Authors

authors_profiles_url_format¶

insiders-4.46.0

author/{slug}
内部人员-4.46.0 author/{slug}

Use this setting to change the format string that is used when generating author profile URLs. You can freely combine placeholders, and join them with slashes or other characters:
使用此设置可以更改生成作者配置文件 URL 时使用的格式字符串。您可以自由组合占位符，并用斜杠或其他字符连接它们：
blog/author/
/ 博客/作者//
blog/
/ 博客//

plugins:
  - blog:
      authors_profiles_url_format: "author/{slug}"

The following placeholders are available:
可以使用以下占位符：

    slug – Author slug or identifier from authors_file
    slug – 作者 slug 或标识符来自 authors_file
    name – Author name from authors_file
    name – 作者姓名来自 authors_file

authors_profiles_pagination¶

insiders-4.46.0

true
内部人员-4.46.0 true

Use this setting to enable or disable pagination for author profiles. The value of this setting is inherited from pagination, unless it's explicitly set. To disable pagination, use:
使用此设置可以启用或禁用作者配置文件的分页。除非显式设置，否则此设置的值继承自 pagination 。要禁用分页，请使用：

plugins:
  - blog:
      authors_profiles_pagination: false

authors_profiles_pagination_per_page¶

insiders-4.46.0

10
内部人员-4.46.0 10

Use this setting to change the number of posts rendered per archive page. The value of this setting is inherited from pagination_per_page, unless it's explicitly set. To change it, use:
使用此设置可更改每个存档页面呈现的帖子数。除非显式设置，否则此设置的值继承自 pagination_per_page 。若要更改它，请使用：

plugins:
  - blog:
      authors_profiles_pagination_per_page: 5

authors_profiles_toc¶

insiders-4.46.0

false
内部人员-4.46.0 false

Use this setting to leverage the table of contents to display post titles on all author profiles. The value of this setting is inherited from blog_toc, unless it's explicitly set. To change it, use:
使用此设置可利用目录在所有作者个人资料上显示帖子标题。除非显式设置，否则此设置的值继承自 blog_toc 。若要更改它，请使用：

plugins:
  - blog:
      authors_profiles_toc: true

Pagination¶ 分页 ¶

The following settings are available for pagination:
以下设置可用于分页：
pagination¶

9.2.0

true

Use this setting to enable or disable pagination in views – generated pages that show posts or subsets of posts in reverse chronological order. If you want to disable pagination, use:
使用此设置可在视图中启用或禁用分页，视图是按时间倒序显示帖子或帖子子集的生成页面。如果要禁用分页，请使用：

plugins:
  - blog:
      pagination: false

pagination_per_page¶

9.2.0

10

Use this setting to change the number of posts rendered per page. If you have rather long post excerpts, it can be a good idea to reduce the number of posts per page:
使用此设置可更改每页呈现的帖子数。如果您有相当长的帖子摘录，减少每页的帖子数量可能是个好主意：

plugins:
  - blog:
      pagination_per_page: 5

pagination_url_format¶

9.2.0

{date}/{slug}

Use this setting to change the format string that is used when generating paginated view URLs. You can freely combine placeholders, and join them with slashes or other characters:
使用此设置可以更改生成分页视图 URL 时使用的格式字符串。您可以自由组合占位符，并用斜杠或其他字符连接它们：
blog/page/n/ 博客/page/n/
blog/n/ 博客/n/

plugins:
  - blog:
      pagination_url_format: "page/{page}"

The following placeholders are available:
可以使用以下占位符：

    page – Page number  page –页码

pagination_format¶

9.2.0

~2~

The plugin uses the paginate module to generate the pagination markup using a special syntax. Use this setting to customize how pagination is constructed. Some popular choices:
该插件使用 paginate 模块通过特殊语法生成分页标记。使用此设置可自定义分页的构造方式。一些受欢迎的选择：
1 2 3 .. n
1 2 3 ..n
1 2 3 .. n
1 2 3 ..n
1

plugins:
  - blog:
      pagination_format: "~2~"

The following placeholders are supported by paginate:
paginate 支持以下占位符：

    $first_page – Number of first reachable page
    $first_page – 第一个可访问的页面数
    $last_page – Number of last reachable page
    $last_page – 最后可访问的页数
    $page – Number of currently selected page
    $page – 当前选择的页面数
    $page_count – Number of reachable pages
    $page_count – 可访问的页面数
    $items_per_page – Maximal number of items per page
    $items_per_page – 每页的最大项目数
    $first_item – Index of first item on the current page
    $first_item – 当前页面上第一项的索引
    $last_item – Index of last item on the current page
    $last_item – 当前页面上最后一项的索引
    $item_count – Total number of items
    $item_count – 项目总数
    $link_first – Link to first page (unless on first page)
    $link_first – 链接到第一页（除非在第一页）
    $link_last – Link to last page (unless on last page)
    $link_last – 链接到最后一页（除非在最后一页）
    $link_previous – Link to previous page (unless on first page)
    $link_previous – 链接到上一页（除非在第一页）
    $link_next – Link to next page (unless on last page)
    $link_next – 链接到下一页（除非在最后一页）

pagination_if_single_page¶

9.2.0

false

Use this setting to control whether pagination should be automatically disabled when the view only consists of a single page. If you want to always render pagination, use:
使用此设置可以控制当视图仅包含单个页面时是否应自动禁用分页。如果要始终呈现分页，请使用：

plugins:
  - blog:
      pagination_if_single_page: true

pagination_keep_content¶

9.2.0

false

Use this setting to enable or disable persistence of content, i.e., if paginated views should also display the content of their containing view. If you want to enable this behavior, use:
使用此设置可以启用或禁用内容的持久性，即，分页视图是否也应显示其包含视图的内容。如果要启用此行为，请使用：

plugins:
  - blog:
      pagination_keep_content: true

Drafts¶ 草稿 ¶

The following settings are available for drafts:
以下设置可用于草稿：
draft¶

9.2.0

false

Rendering draft posts can be useful in deploy previews. Use this setting to specify whether the plugin should include posts marked as drafts when building your project:
渲染草稿帖子在部署预览中非常有用。使用此设置可以指定插件在构建项目时是否应包含标记为草稿的帖子：
Render drafts 渲染草稿
Don't render drafts 不呈现草稿

plugins:
  - blog:
      draft: true

draft_on_serve¶

9.2.0

true

Use this setting to control whether the plugin should include posts marked as drafts when previewing your site. If you don't wish to include draft posts when previewing, use:
使用此设置可以控制插件在预览网站时是否应包含标记为草稿的帖子。如果您不希望在预览时包含草稿帖子，请使用：

plugins:
  - blog:
      draft_on_serve: false

draft_if_future_date¶

9.2.0

false

The plugin can automatically mark posts with future dates as drafts. When the date is past today, the post is automatically included when building your project, unless explicitly marked as draft:
该插件可以自动将未来日期的帖子标记为草稿。当日期超过今天时，除非明确标记为草稿，否则在构建项目时会自动包含该帖子：

plugins:
  - blog:
      draft_if_future_date: true

Usage¶ 用法 ¶
Metadata¶ 元数据 ¶

Posts can define a handful of metadata properties that specify how the plugin renders them, in which views they are integrated, and how they are linked to each other. The metadata of each post is validated against a schema to allow for a quicker discovery of syntax errors.
帖子可以定义一些元数据属性，这些属性指定插件如何呈现它们、它们在哪些视图中集成以及它们如何相互链接。每个帖子的元数据都根据架构进行验证，以便更快地发现语法错误。

The following properties are available:
可以使用以下属性：
authors¶

9.2.0

Use this property to associate a post with authors by providing a list of identifiers as defined in the authors_file. If an author can't be resolved, the plugin will terminate with an error:
使用此属性通过提供 中定义的标识符列表，将帖子与作者相关联 authors_file 。如果无法解析作者，插件将终止并显示错误：

---
authors:
  - squidfunk 


---

# Post title
...

categories¶

9.2.0

Use this property to associate a post with one or more categories, making the post a part of the generated category page. Categories are defined as a list of strings (whitespaces are allowed):
使用此属性可将帖子与一个或多个类别相关联，使帖子成为生成的类别页面的一部分。类别定义为字符串列表（允许使用空格）：

---
categories:
  - Search
  - Performance
---

# Post title
...

If you want to prevent accidental typos assigning categories to posts, you can set a predefined list of allowed categories in mkdocs.yml by using the categories_allowed setting.
如果要防止意外拼写错误将类别分配给帖子，则可以使用该 categories_allowed 设置设置允许类别的 mkdocs.yml 预定义列表。
date¶

9.2.0

Use this property to specify a post's date. Note that this property is required, which means the build fails when it's not set. Additional dates can be set by using a slightly different syntax:
使用此属性可以指定帖子的日期。请注意，此属性是必需的，这意味着未设置此属性时生成将失败。可以使用略有不同的语法来设置其他日期：
Date 日期
Update date 更新日期
Custom date 自定义日期

---
date: 2024-01-31
---

# Post title
...

The following date formats are supported:
支持以下日期格式：

    2024-01-31
    2024-01-31T12:00:00

draft¶

9.2.0

Use this property to mark a post as draft. The plugin allows to include or exclude posts marked as drafts when building your project using the draft setting. Mark a post as draft with:
使用此属性将帖子标记为草稿。该插件允许在使用该 draft 设置构建项目时包含或排除标记为草稿的帖子。使用以下命令将帖子标记为草稿：

---
draft: true
---

# Post title
...

links¶

insiders-4.23.0 内部人员-4.23.0

Use this property to define a list of links that are rendered in the sidebar of a post. The property follows the same syntax as nav in mkdocs.yml, supporting sections and even anchors:
使用此属性可以定义在帖子的边栏中呈现的链接列表。该属性遵循与 nav 中的 mkdocs.yml 语法相同的语法，支持部分甚至锚点：
Links 链接
Links with sections 与部分的链接
Links with anchors 与锚点的链接

---
links:
  - setup/setting-up-site-search.md
  - insiders/index.md
---

# Post title
...

All relative links are resolved from the docs directory.
所有相对链接都是从 docs 目录中解析的。
readtime¶

9.2.0

Use this property to explicitly set the reading time of a post in minutes. When post_readtime is enabled, the plugin computes the reading time of a post, which can be overridden with:
使用此属性可以显式设置帖子的阅读时间（以分钟为单位）。启用后 post_readtime ，插件会计算帖子的阅读时间，该时间可以被以下命令覆盖：

---
readtime: 15
---

# Post title
...

slug¶

9.2.0

Use this property to explicitly set the slug of a post. By default, the slug of a post is automatically computed by the post_slugify function from the post's title, which can be overridden with:
使用此属性可以显式设置帖子的 slug。默认情况下，帖子的 slug 由帖子标题中的函数自动计算，该 post_slugify 函数可以用以下命令覆盖：

---
slug: help-im-trapped-in-a-universe-factory
---

# Post title
...

Slugs are passed to post_url_format.
蛞蝓被传递给 post_url_format 。

Missing something? 错过了什么？

When setting up your blog or migrating from another blog framework, you might discover that you're missing specific functionality – we're happy to consider adding it to the plugin! You can open a discussion to ask a question, or create a change request on our issue tracker, so we can find out if it might be a good fit for the plugin.
在设置您的博客或从另一个博客框架迁移时，您可能会发现您缺少特定功能——我们很高兴考虑将其添加到插件中！您可以打开讨论以提出问题，或在我们的问题跟踪器上创建更改请求，以便我们了解它是否适合该插件。

    Views are pages that are automatically generated, i.e., the entry point to your blog listing all latest posts, as well as archive and category pages that list all posts associated with them through metadata in chronological order. ↩
    视图是自动生成的页面，即列出所有最新帖子的博客入口点，以及按时间顺序通过元数据列出与其关联的所有帖子的存档和类别页面。↩

3 weeks ago
4 months ago
GitHub
squidfunk
nicfv
alexvoss

3周前4个月前GitHub
Was this page helpful?
这个页面有帮助吗？
Previous
Built-in plugins
Next
Group
Copyright © 2016 - 2024 Martin Donath
Made with Material for MkDocs Insiders
