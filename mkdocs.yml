#
site_name: shafish.cn
site_url: https://shafish.cn
site_author: shafish
site_description: >-
  Just write something about Work、Technology and Life.

repo_url: https://github.com/tffats/shafish_blog
repo_name: tffats/shafish_blog
edit_uri: edit/main/docs/
copyright: Copyright &copy; 2023 <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener">粤ICP备2022032763号</a>

theme:
  name: material
  custom_dir: overrides
  language: zh
  logo: https://picture.cdn.shafish.cn/blog/about/head.png
  # 网站图标
  #font:
    #text: Roboto
    #code: Roboto Mono
  #favicon: assets/favicon.png
  # 网站字体
  icon:
    repo: fontawesome/brands/github
  palette:
    scheme: slate
    primary:  deep orange  # 使用root自定义 # 算了deep orange还挺好看的
    accent: deep orange
    #- media: "(prefers-color-scheme: light)" 
      #scheme: default
      #primary: teal
     #accent: teal
     #toggle:
        #icon: material/weather-night
        #name: 关灯
    #- media: "(prefers-color-scheme: dark)" 
      #scheme: slate
      #primary:  deep orange
      #accent: deep orange
      #toggle:
        #icon: material/weather-sunny
        #name: 开灯
  features:
    #- navigation.instant # without fully reloading the page
    - navigation.tracking
    - navigation.tabs # nav top目录显示在标题栏
    - navigation.tabs.sticky
    #- navigation.sections
    #- navigation.expand  # 每次刷新都展开子节点
    - navigation.indexes  # 跟navigation.instant不兼容
    #- toc.integrate  # 目录展开在字节点下
    - navigation.top # 一键回顶 top
    - search.suggest
    - search.highlight
    - search.share
    #- header.autohide
    - content.code.annotate
    - header.autohide
    - toc.separate
    - content.code.copy # 代码复制按钮
    - content.action.edit
    - content.action.view
    - navigation.footer
    - announce.dismiss

plugins:
  - search:
      separator: '[\s\u200b\-]'
  - tags
  - git-revision-date-localized:
      type: datetime
      timezone: Asia/Shanghai
      enable_creation_date: true
      exclude:
        - comments.md
  - rss:
      match_path: newBlog/posts/.* 
      # date_from_meta:
      #  as_creation: date
  - blog:
      blog_dir: newBlog
      blog_toc: true
      post_date_format: long
      post_url_date_format: yyyy/MM/dd
      post_url_format: "{slug}"
      post_excerpt: required
      post_excerpt_max_authors: 1
      post_excerpt_separator: <!-- more -->
      archive_name: shafish 的归档
      archive_toc: true
      archive_date_format: MMMM yyyy
      archive_url_date_format: yyyy/MM
      archive_url_format: "archive/{date}"
      categories_name: shafish 的标签
      categories_toc: true
      categories_url_format: "category/{slug}"
      pagination_per_page: 10
      pagination_format: "$link_first $link_previous ~2~ $link_next $link_last"
      pagination_keep_content: true
      draft: true
      draft_if_future_date: true
  #- offline
  #- group
  #- info

extra:
  social:
    - icon: fontawesome/solid/rss
      link: /feed_rss_created.xml
      name: RSS  
    - icon: fontawesome/solid/paper-plane
      link: mailto:<shafish_cn@163.com>
      name: 每天都看哟
  #vssue: shafish
  #generator: false

#extra_css:
  #- stylesheets/extra.css
  #- https://file.cdn.shafish.cn/blog/css/vssue.css
  #- https://cdn.jsdelivr.net/npm/xgplayer@3.0.5/dist/index.min.css
  #- https://file.cdn.shafish.cn/blog/css/video-js.min.css
  #- https://file.cdn.shafish.cn/blog/css/video.css

#extra_javascript:
  #- https://cdn.jsdelivr.net/npm/xgplayer@3.0.5/dist/index.min.js
  #- https://cdn.jsdelivr.net/npm/dot@1.1.3/doT.min.js
  #- javascripts/dot.js
  #- javascripts/extra.js
  #- javascripts/d.js
  #- https://file.cdn.shafish.cn/blog/js/video.min.js

markdown_extensions:
  - abbr
  - attr_list
  - pymdownx.snippets:
      auto_append:
        - english/phonetic_note.md # 英语单词注解统一放在该文件中
      base_path: ["docs/"]
      #check_paths: true
  - pymdownx.superfences:
      custom_fences:
        - name: mermaid
          class: mermaid
          format: !!python/name:pymdownx.superfences.fence_code_format
  - pymdownx.tabbed:
      alternate_style: true 
  - admonition
  - pymdownx.details  
  - pymdownx.highlight:
      anchor_linenums: true
  - pymdownx.inlinehilite
  - tables
  - footnotes
  - pymdownx.critic
  - pymdownx.caret
  - pymdownx.keys
  - pymdownx.mark
  - pymdownx.tilde  
  - pymdownx.emoji:
      emoji_index: !!python/name:material.extensions.emoji.twemoji
      emoji_generator: !!python/name:material.extensions.emoji.to_svg      
  - md_in_html
  - meta
  - def_list
  - toc:
      permalink:  🐟 # ⚓︎ # 显示锚点
      #slugify: !!python/name:pymdownx.slugs.slugify # 显示一些Unicode的标识符（可能是罗马希腊那些什么鬼符号）
      slugify: !!python/object/apply:pymdownx.slugs.slugify {kwds: {case: lower}}
  - pymdownx.tasklist:
      custom_checkbox: true  

nav:
  - Bblog:
    - newBlog/index.md
  #- 技术: blog/index.md
    #- 2022:
     # - blog/2022/index.md
      #- 第一篇文章: blog/2022/1.md
    #- 会更新区: 
      #- mkdocs使用记录: blog/mkdocs.md  
      #- i3wm使用记录: blog/i3wm.md  
    #- 2021年:
      #- blog/2021/index.md
      #- 12月份: 
        #- archivebox使用: blog/2021/12/archivebox.md
        #- jenkins使用: blog/2021/12/jenkins.md 
        #- github action使用: blog/2021/12/github_action.md 
        #- nginx使用记录: blog/2021/12/nginx_record.md
  - 外语: english/index.md
  - 生活: life/index.md
  - 留言: comments.md
  - 关于: about.md
  - Archive: http://archivebox.shafish.cn/" target="_blank
  - Lofimusic: http://lofimusic.shafish.cn/" target="_blank
  - Note: https://note.shafish.cn/" target="_blank