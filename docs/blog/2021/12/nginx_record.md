---
title: nginx使用记录
hide:
  - navigation
---

# nginx使用记录

[Back](../../index.md#2021年文章导航){ .md-button}

???+ abstract
	仅做笔记，为了有个小小印象。

## 一、安装依赖库

```shell
sudo pacman -S wget gcc-c++ ncurses ncurses-devel cmake make perl bison openssl openssl-devel \
gcc* libxml2 libxml2-devel curl-devel libjpeg* libpng* freetype* autoconf automake zlib* fiex* \
libxml* libmcrypt* libtool-ltdl-devel* libaio libaio-devel bzr libtool
```

## 二、configure配置

使用`configure`指定配置项，可以用`./configure --help`查看，对应参数分为四大类：

### 1.路径相关参数：

  - `--prefix=path`：安装的根目录，缺省默认目录为：`/usr/local/nginx`，该目录指下面出现到的<prefix>，bulingbuling。
  - `--sbin-path=path`：nginx相关二进制文件路径，`<prefix>/sbin/nginx`
  - `--conf-path=path`：nginx配置文件路径，`<prefix>/conf/nginx.conf`
  - `--error-log-path=PATH`：error日志文件路径，`<prefix>/logs/error.log`
  - `--pid-path=PATH`：pid文件的存放路径，文件以ascii码存放nginx master进程id，master之后会讲到（非常重要），`<prefix>/logs/nginx.pid`
  - `--lock-path=PATH`：lock文件的存放路径，`<prefix>/logs/nginx.lock`
  - `--builddir=DIR`：configura命令执行和程序编译时产生临时文件的存放目录，`<prefix>/objs`
  - `--with-perl_modules_path=PATH`：perl module存放路径
  - `--with-perl=PATH`：perl binary存放路径，执行perl脚本需要此设置
  - `--http-log-path=PATH`：access日志存放路径，会记录每个http请求的访问记录，`<prefix>/logs/access.log`
  - `--http-client-body-temp-path=PATH`：保存http请求包体等临时内容，`<prefix>/client_body_temp`
  - `--http-proxy-temp-path=PATH`：做反向代理时，上游服务器的http请求包体，`<prefix>/proxy_temp`
  - `--http-fastcgi-temp-path=PATH`：fastcgi临时文件存放目录，`<prefix>/fastcgi_temp`
  - `--http-uwsgi-temp-path=PATH`：uwsgi临时文件存放目录，`<prefix>/uwsgi_temp`
  - `--http-scgi-temp-path=PATH`：scgi临时文件存放目录，`<prefix>/scgi_temp`

### 2.编译相关参数：

  - `--with-cc=PATH`：c编译器的路径
  - `--with-cpp=PATH`：c预编译器的路径
  - `--with-cc-opt=OPTIONS`：在nginx编译时指定一些编译选项
  - `--with-ld-opt=OPTIONS`：加入生成二进制执行文件前的一些指定的链接文件
  - `--with-cpu-opt=CPU`：指定cpu处理器架构，pentium, pentiumpro, pentium3, pentium4, athlon, opteron, amd64, sparc32, sparc64, ppc64

### 3.相关（常用）软件库配置参数：

  - pcre库
    - `--without-pcre`：设置配置文件中不使用正则表达式
    - `--with-pcre`：强制使用pcre库
    - `--with-pcre=DIR`：指定pcre库的源码目录，编译nginx时进入该目录编译pcre
    - `--with-pcre-opt=OPTIONS`：编译pcre时指定编译选项
  - openssl库
    - `--with-openssl=DIR`：指定openssl库的源码目录，编译nginx时进入该目录编译openssl。如需要https支持，需要安装该库
    - `--with-openssl-opt=OPTIONS`：编译openssl时指定编译选项
  - atomic原子库
    - `--with-libatomic`：强制使用atomic库
    - `--with-libatomic=DIR`：atomic库所在位置
  - 散列函数库
    - `--with-MD5=DIR`：指定md5库源码目录，编译nginx时进入该目录编译md5。（没特殊需求，可用nginx自带的md5算法）
    - `--with-MD5-opt=OPTIONS`：编译md5时指定编译选项
    - `--with-MD5-asm`：使用md5的汇编源码
    - `--with-SHA1=DIR`：指定SHA1库源码目录，编译nginx时进入该目录编译SHA1。（openssl有sha1的实现，已经安装openssl直接用）
    - `--with-SHA1-opt=OPTIONS`：编译SHA1时指定编译选项
    - `--with-SHA1-asm`：使用SHA1的汇编源码
  - zlib库
    - `--with-zlib=DIR`：指定zlib库源码目录，编译nginx时进入该目录编译zlib。（需要gzip压缩，就需该库支持）
    - `--with-zlib-opt=OPTIONS`：编译zlib时指定编译选项
    - `--with-zlib-asm=CPU`：指定对特定cpu职业zlib汇编优化功能，pentium, pentiumpro。
  - 。。。

### 4.模块相关参数：又分为五大模块

  - 事件模块（具体事件驱动特点，另说）
    - `--with-rtsig_module`：使用rtsig module处理事件驱动
    - `--with-select_module`：使用select 多路复用module处理事件渠道
    - `--without-select_module`：不安装select module
    - `--with-poll_module`：使用poll module处理事件驱动
    - `--without-poll_module`：不安装poll module
    - `--with-aio_module`：使用aio处理事件驱动，只能再freebsd系统中使用
  - 默认安装进http模块（nginx安装时默认安装的http相关功能，如果不需要就用`--without-http-xxx`去除）
    - `--without-http_chatset_module`：不安装http chatset模块，模块可以将服务器发出的http响应重编码
    - `--without-http_gzip_module`：不安装gzip模块，模块可以对响应内容进行压缩
    - `--without-http_ssi_module`：不安装ssi模块，模块可以在响应中加入特定内容
    - `--without-http_userid_module`：不安装userid模块，模块可以根据请求头的字段信息进行用户认证，确定请求是否合法
    - `--without-http_access_module`：不安装access模块，模块可以根据ip显示客户端访问
    - `--without-http_auth_basic_module`：不安装auth basic模块，模块可以提高简单的用户名、密码认证
    - `--without-http_autoindex_module`：不安装autoindex模块，模块提供简单的目录浏览功能
    - `--without-http_geo_module`：不安装geo模块，模块可以根据不同地区响应不同内容
    - `--without-http_map_module`：不安装map模块，模块可以创建key-value映射表
    - `--without-http_split_clients_module`：不安装split client模块，模块可以根据客户端信息（ip、header信息、cookie等）区分处理
    - `--without-http_referer_module`：不安装referer模块，模块可根据请求的referer字段拒绝请求
    - `--without-http_rewrite_module`：不安装rewrite模块，模块提供重定向功能
    - `--without-http_proxy_module`：不安装proxy模块，模块提供反代理功能
    - `--without-http_fastcgi_module`：不安装fastcgi模块，
    - `--without-http_uwsgi_module`：不安装uwsgi模块，
    - `--without-http_scgi_module`：不安装scgi模块，
    - `--without-http_memecached_module`：不安装memecached模块，缓存
    - `--without-http_limit_zone_module`：不安装limit zone模块，模块可以限制ip并发连接数量，比如设置一个ip只能一个连接
    - `--without-http_limit_req_module`：不安装limit req模块，模块可以限制ip并发请求数量
    - `--without-http_empty_gif_module`：不安装empty gif模块，模块可以在接收到无效请求时返回一个1*1像素的图片
    - `--without-http_browser_module`：不安装browser模块，模块可根据请求中的user-agent字段识别浏览器
    - `--without-http_upstream_ip_hash_module`：不安装upstream ip hash模块，模块可以对ip进行哈希，让请求转发给对应ip响应，负载均衡用。
    - 。。。
  - 不默认安装进http模块（nginx安装时默认不会安装的http功能，如果需要就用`--with-http-xxx`添加）
    - `--with-http_ssl_module`：安装ssl模块，模块提供https服务（需要在config设置时配置openssl库）
    - `--with-http_realip_module`：安装realip模块，模块可以从请求header中获取客户端真正ip
    - `--with-http_addition_module`：安装addition模块，模块可以在响应头部、尾部添加内容
    - `--with-http_xslt_module`：安装xslt模块，模块可以响应xml时加入xsl渲染（需要在config设置时配置libxml2库、libxslt库）
    - `--with-http_image_filter_module`：安装image filter模块，模块可以将图片（jpeg、png、gif）实时压缩为指定大小进行响应（libgd库）
    - `--with-http_geoip_module`：安装geoip模块，模块可以根据ip获取对应实际地理位置
    - `--with-http_sub_module`：安装sub模块，模块可以替换响应的指定字符串
    - `--with-http_dav_module`：安装ssl模块，模块可以让nginx支持webdav标准，使用put、delete、copy、move等请求
    - `--with-http_mp4_module`：安装mp4模块
    - `--with-http_gzip_static_module`：安装gzip static模块，不同gzip模块，static的可以在进行gzip压缩前检查是否存在该响应的缓存，减少cpu压缩压力
    - `--with-http_random_index_module`：安装random index模块，模块可以在客户端访问某目录时，随机返回该目录中某个文件
    - `--with-http_secure_link_module`：安装secure link模块，模块可以验证请求是否有效
    - `--with-http_degradation_module`：安装degradation模块，模块针对特殊系统做调用优化（非linux）
    - `--with-http_sub_status_module`：安装sub status模块，模块可以提供nginx运行时性能统计信息
    - `--with-google_perftools_module`：安装google perftool模块，模块提供谷歌性能测试工具
    - 。。。
  - mail模块
    - `--with-mail`：安装邮件服务器的反代模块，使用imap、pop3、smtp等协议
    - `--with-mail_ssl_module`：模块可以让协议基于https使用（openssl库）
    - `--without-mail_imap_module`：不使用imap协议
    - `--without-mail_pop3_module`：不使用pop3协议
    - `--without-mail_smtp_module`：不使用smtp协议
  - 其他
    - `--with-debug`：nginx研究、调试用
    - `--add-module=PATH`：在nginx中加入第三方模块
    - `--without-http`：禁用http服务
    - `--without-http-cache`：禁用http的缓存服务
    - `--without-file-aio`：文件异步io处理
    - `--with-ipv6`：支持ipv6
    - `--user=USER`：指定worker进程运行时所属的用户（非root）
    - `--group=GROUP`：指定worker进程运行时所属的用户组

## 三、编译安装
- 进入nginx目录
- 配置：`./configure [options]`
- 编译：`make`
- 将编译好的二进制运行文件复制到配置好的目录：`make install`

> 这里重点介绍下configure配置：
> 根据前面的`configure配置`选择自己需要的库、功能，先使用系统包管理器安装依赖库，再配置configure。
> 下面展示几个示例：

### 示例a
```shell
cd /home/shafish/software
wget https://nginx.org/download/nginx-1.20.2.tar.gz
tar -zxvf nginx-1.20.2.tar.gz
cd nginx-1.20.2

# pcre 重定向时使用正则匹配
# openssl 使用https
sudo pacman -S pcre* openssl*

./configure --prefix=/usr/local/nginx-1.20.2 \ # 指定安装的根目录
--with-http_ssl_module  \ # 安装ssl
--with-http_spdy_module \ # SPDY可以缩短网页的加载时间
--with-http_stub_status_module \ # nginx监控模块
--with-pcre \ # 启用pcre

sudo make
sudo make install
```

### 示例b
```shell
cd /home/shafish/software
wget https://nginx.org/download/nginx-1.20.2.tar.gz
tar -zxvf nginx-1.20.2.tar.gz
cd nginx-1.20.2

# 安装openssl库
sudo pacman -S openssl*
# 下载第三方库的源码，对库进行编译使用
wget http://labs.frickle.com/files/ngx_cache_purge-1.3.tar.gz
tar -xzvf ngx_cache_purge-1.3.tar.gz

./configure --prefix=/usr/local/nginx-1.20.2 \ # 指定安装的根目录
–with-http_stub_status_module \ # 启用 nginx 状态模块
–with-http_ssl_module \ # 启用 SSL 模块
–with-http_realip_module \ # 启用 realip 模块（将用户 IP 转发给后端服务器）
–add-module=./ngx_cache_purge-1.3 # 添加缓存清除扩展模块

sudo make
sudo make install
```
### 示例c
```shell
--prefix=/usr/local/nginx --user=www --group=www --with-http_stub_status_module --with-http_v2_module --with-http_ssl_module --with-http_gzip_static_module --with-http_realip_module --with-http_flv_module --with-http_mp4_module --with-openssl=../openssl-1.0.2p --with-pcre=../pcre-8.42 --with-pcre-jit --with-ld-opt=-ljemalloc
```

## 四、为已安装的Nginx动态添加模块
安好nginx后，过个几个月，再加多几个需求，对吧，你懂的。

nginx添加模块还是要重新编译才行。

### 加ngx_http_google_filter_module
```shell
# 进入nginx安装目录，看上面示例a b
cd cd /home/shafish/software/nginx-1.20.2
# 下载需要的模块源码
git clone https://github.com/cuber/ngx_http_google_filter_module
# 查看安装时的配置
./nginx -V
# ./configure --prefix=/usr/local/nginx-1.20.2 –with-http_stub_status_module –with-http_ssl_module –with-http_realip_module –add-module=./ngx_cache_purge-1.3
# 加入新模块配置
./configure --prefix=/usr/local/nginx-1.20.2 –with-http_stub_status_module –with-http_ssl_module –with-http_realip_module –add-module=./ngx_cache_purge-1.3 \ 
--add-module=/usr/local/nginx/ngx_http_google_filter_module
# 执行编译
sudo make
# 替换nginx二进制文件，木有make install哟
mv ./sbin/nginx ./sbin/nginx.bak
cp ./objs/nginx ./sbin/
./nginx -V
```

## 五、nginx命令
> 默认安装路径：`--prefix=/usr/local/nginx`

- 启动nginx：`/usr/local/nginx/sbin/nginx` （默认加载的是/usr/local/nginx/conf/nginx.conf配置）
- 启动时指定配置：`/usr/local/nginx/sbin/nginx -c /tmp/nginx.conf` （-c指定配置文件路径）
- 测试配置文件是否正确：`/usr/local/nginx/sbin/nginx -t`
- 显示编译阶段用的参数：`/usr/local/nginx/sbin/nginx -V`
- “优雅”地停止服务：`/usr/local/nginx/sbin/nginx -s quit`
- 运行时重载配置文件：`/usr/local/nginx/sbin/nginx -s reload`
- 不重启升级nginx版本：

## 六、nginx基本的配置

### 1.用于调试、问题定位的配置

| 配置项目                      | 说明                                                                                                                |
| :---------------------------- | :------------------------------------------------------------------------------------------------------------------ |
| `daemon on|off;`              | 是否以守护进程方式运行Nginx（默认on）                                                                               |
| `master_process on|off;`      | 是否以master/worker方式工作（默认on）                                                                               |
| `error_log /path/file level;` | 大于或等于该级别的日志都会被输出到/path/file文件中。level取值：debug、info、notice、warn、error、crit、alert、emerg |
| `debug_connection[IP|CIDR]`   | 必须放在events{...}中才有效，仅仅来自规定的ip请求才会输出debug日志                                                  |
| `worker_rlimit_core size;`    | 限制coredump核心转储文件的大小                                                                                      |
| `working_directory path;`     | 指定coredump文件生成目录                                                                                            |

### 2.运行必备配置

| 配置项目                          | 说明                                               |
| :-------------------------------- | :------------------------------------------------- |
| `env VAR|VAR=VALUE`               | 定义环境变量(env TESTPATH=/tmp/;)                  |
| `include/path/file;`              | 嵌入其他配置文件(相对目录：即nginx.conf所在的目录) |
| `pid path/file;`                  | 保存master进程id的pid文件路径                      |
| `user username[groupname];`       | Nginx worker进程运行的用户及用户组                 |
| `worker_rlimit_nofile limit;`     | 指定Nginx worker进程可以打开的最大句柄描述符个数   |
| `worker_rlimit_sigpending limit;` | 设置每个用户发往Nginx的信号队列的大小              |

### 3.性能优化配置

| 配置项目                                  | 说明                                                                  |
| :---------------------------------------- | :-------------------------------------------------------------------- |
| `worker_processes number;`                | Nginx worker进程个数(一般跟核数相同即可)                              |
| `worker_cpu_affinity cpumask[cpumask...]` | [Linux]绑定Nginx worker进程到指定的CPU内核                            |
| `ssl_engine device;`                      | SSL硬件加速(openssl engine -t查看是否有加速设备)                      |
| `timer_resolution t;`                     | 系统调用gettimeofday的执行频率(时间能准一点)                          |
| `worker_priority nice;`                   | Nginx worker进程优先级设置，–20~+19，–20是最高优先级，+19是最低优先级 |
| `worker_rlimit_sigpending limit;`         | 设置每个用户发往Nginx的信号队列的大小                                 |

### 4.事件类型配置

| 配置项目                                                   | 说明                                                                                                                                                   |
| :--------------------------------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------- |
| `accept_mutex[on|off];`                                    | 默认on，是否打开accept锁，负载均衡锁                                                                                                                   |
| `lock_file path/file;`                                     | lock文件的路径，文件锁                                                                                                                                 |
| `accept_mutex_delay Nms;`                                  | 使用accept锁后到真正建立连接之间的延迟时间，如果有一个worker进程试图取accept锁而没有取到，它至少要等accept_mutex_delay定义的时间间隔后才能再次试图取锁 |
| `multi_accept[on|off];`                                    | 默认off，批量建立新连接                                                                                                                                |
| `use[kqueue|rtsig|epoll|/dev/poll|select|poll|eventport];` | 选择事件模型，默认会自动使用最适合的事件模型                                                                                                           |
| `worker_connections number;`                               | 每个worker的最大连接数                                                                                                                                 |

## 七、nginx http模块配置
nginx主要靠ngx_http_core_module模块实现。

### 1.模块变量

![](https://picture.cdn.shafish.cn/blog/nginx-2021-12-nginx_http_variable1.png){: .zoom}

![](https://picture.cdn.shafish.cn/blog/nginx-2021-12-nginx_http_variable2.png){: .zoom}

### 2.虚拟主机与请求的分发
每个server块都是一个虚拟主机。

- 监听端口：`listen address:port[default(deprecated in 0.8.21)|default_server|[backlog=num|rcvbuf=size|sndbuf=size|accept_filter=filter|deferred|bind|ipv6only=[on|off]|ssl]];`
    - `default/default_server`：将所在的server块作为nginx默认虚拟主机，否则默认是选配置中第一个server块der（当一个请求无法匹配配置文件中的所有主机域名时，就会选用默认的虚拟主机）。
    - `backlog=num`：表示TCP中backlog队列的大小，如果backlog队列已满，还有新的客户端试图通过三次握手建立TCP连接，这时客户端将会建立连接失败。
    - `rcvbuf=size`：设置监听句柄的SO_RCVBUF参数。
    - `sndbuf=size`：设置监听句柄的SO_SNDBUF参数。
    - `deferred`：适用于大并发，当请求数据来临时，worker进程才会开始处理这个连接。
    - `bind`：绑定当前端口/地址对，同时对一个端口监听多个地址时才会生效。
    - `ssl`：在当前监听的端口上建立的连接必须基于SSL协议。

> 在开始处理一个HTTP请求时，Nginx会取出header头中的Host，与每个server中的server_name进行匹配，以此决定到底由哪一个server块来处理这个请求。

- 主机名称：`server_name name[...];`
    - 多域名对应一个服务器ip，可以根据server块中定义的server_name来处理对应域名的请求。

- 设置散列桶占用的内存大小：`server_names_hash_bucket_size size;`
    - Nginx使用散列表来存储server name，提高快速寻找到相应server name的能力。

- `server_names_hash_max_size`
    - 默认：512，越大，消耗的内存就越多，但散列key的冲突率则会降低，server name检索速度也更快。

- 重定向主机名称的处理：`server_name_in_redirect on|off;`
    - 默认：on，表示在重定向请求时会使用server_name里配置的第一个主机名代替原先请求中的Host头部。

- uri请求匹配：`location[=|~|~*|^~|@]/uri/{...}`
    - `=`：把URI作为字符串进行匹配。
    ```shell
    location = / {
      # 只有当用户请求是/时，才会使用该location下的配置
      …
    }
    ```
    - `~`：表示匹配URI时是字母大小写敏感的。
    - `~*`：表示匹配URI时忽略字母大小写问题。
    - `^~`：表示匹配URI时只需要其前半部分与uri参数匹配即可。
    ```shell
    location ^~ /images/ {
      # 以/images/开始的请求都会匹配上
      …
    }
    ```
    - `@`：表示仅用于Nginx服务内部请求之间的重定向，不直接处理用户请求。
    - `正则表达式`：用正则匹配呗。
    ```shell
    location ~* \.(gif|jpg|jpeg)$ {
      # 匹配以 .gif、.jpg、.jpeg结尾的请求
      …
    }
    ```

### 3.文件路径的定义
- 指定根目录：`root path;`
    - 默认：root html
    ```shell
    location /download/ {
      # 请求的URI是/download/index/test.html，服务器返回/opt/web/html/download/index/test.html文件的内容
      root /opt/web/html/;
    }
    ```

- 将uri请求目录映射为根目录：`alias path;`
```shell
location /conf {
  # 请求的URI是/conf/nginx.conf，服务器返回/usr/local/nginx/conf/nginx.conf，将/conf映射为了/usr/local/nginx/conf/
  alias /usr/local/nginx/conf/;
}
```

- 访问首页：`index file...;`
    - 默认：index index.html;

- 根据HTTP返回码重定向页面：`error_page code[code...][=|=answer-code]uri|@named_location`
```shell
error_page 404 /404.html;
error_page 502 503 504 /50x.html;
error_page 403 http://example.com/forbidden.html;
# 重定向到另一个location中进行处理
error_page 404 = @fallback;

location @fallback (
  proxy_pass http://backend;
)
```

- 是否允许递归使用error_page：`recursive_error_pages[on|off];`
    - 默认：recursive_error_pages off;

- 寻找文件：`try_files path1[path2] uri;`
  ```shell
  # 按照顺序找这些文件，文件存在就返回，结束请求，如果都找不到就进入设置好的重定向处理。
  try_files /system/maintenance.html $uri $uri/index.html $uri.html @other;

  location @other {
    proxy_pass http://backend;
  }
  ```

### 4.内存及磁盘资源的分配
> 处理请求时内存、磁盘资源分配的配置项

- HTTP包体只存储到磁盘文件中：`client_body_in_file_only on|clean|off;`
    - 默认：client_body_in_file_only off;
    - 当设置为on|clean时，请求包体会存储在磁盘文件中。

- HTTP包体尽量写入到一个内存buffer中：`client_body_in_single_buffer on|off;`
    - 默认：client_body_in_single_buffer off;
    - 如果包体内容大过client_body_buffer_size，包体内容就存储在磁盘文件中

- 存储HTTP头部的内存buffer大小：`client_header_buffer_size size;`
    - 默认：client_header_buffer_size 1k;
    - 正常情况下Nginx接收用户请求中HTTP header部分（包括HTTP行和HTTP头部）时分配的内存buffer大小

- 存储超大HTTP头部的内存buffer大小：`large_client_header_buffers number size;`
    - 默认：large_client_header_buffers 48k;
    - 定义了Nginx接收一个超大HTTP头部请求的buffer个数和每个buffer的大小

- 存储HTTP包体的内存buffer大小：`client_body_buffer_size size;`
    - 默认：client_body_buffer_size 8k/16k;
    - 定义了Nginx接收HTTP包体的内存缓冲区大小

- HTTP包体的临时存放目录：`client_body_temp_path dir-path[level1[level2[level3]]]`
    - client_body_temp_path /opt/nginx/client_temp 1 2;

- 指定为连接成功的请求分配内存池初始大小：`connection_pool_size size;`
    - 默认：connection_pool_size 256;

- 开始处理请求，为每个请求分配的内存池大小：`request_pool_size size;`
    - 默认：request_pool_size 4k;

### 5.网络连接的设置
- 读取HTTP头部的超时时间：`client_header_timeout time（默认单位：秒）;`
    - 默认：client_header_timeout 60;
- 读取HTTP包体的超时时间：`client_body_timeout time（默认单位：秒）;`
    - 默认：client_body_timeout 60;
- 发送响应的超时时间：`send_timeout time;`
   - 默认：send_timeout 60;
- `reset_timeout_connection on|off;`
    - 默认：reset_timeout_connection off;
    - 连接超时，直接向用户发送RST重置包，而不是四次挥手关闭连接。

- `lingering_close off|on|always;`
    - 默认：lingering_close on;
    - 控制Nginx关闭用户连接的方式。
    - always表示关闭用户连接前必须无条件地处理连接上所有用户发送的数据
    - off表示关闭连接时完全不管连接上是否已经有准备就绪的来自用户的数据
    - on是中间值，一般情况下在关闭连接前都会处理连接上的用户发送的数据

- `lingering_time time;`  
    - 默认：lingering_time 30s;
    - 跟用户文件上传相关，如果经过lingering_time后，上传还在继续，则会关闭连接

- `lingering_timeout time;`
    - 默认：lingering_timeout 5s;
    - 关闭连接前，检测是否有用户发送的数据到达服务器

- 对某些浏览器禁用keepalive功能：`keepalive_disable[msie6|safari|none]...`
    - 默认：keepalive_disablemsie6 safari
    - 让多个请求复用一个HTTP长连接，这个功能对服务器的性能提高是很有帮助的

- keepalive超时时间：`keepalive_timeout time（默认单位：秒）;`
    - 默认：keepalive_timeout 75;
    - 一个keepalive连接在闲置超过一定时间后（默认的是75秒），服务器和浏览器都会去关闭这个连接。

- 一个keepalive长连接上允许承载的请求最大数：`keepalive_requests n;`
    - 默认：keepalive_requests 100;

- 对keepalive连接是否使用TCP_NODELAY选项：`tcp_nodelay on|off;`
    - 默认：tcp_nodelay on;

- `tcp_nopush on|off;`
    - 默认：tcp_nopush off;
    - 打开tcp_nopush后，将会在发送响应时把整个响应包头放到一个TCP包中发送

### 6.MIME类型的设置
- MIME type与文件扩展的映射：`type{...};`
```js
types {
  text/html html;
  text/html conf;
  image/gif gif;
  image/jpeg jpg;
}
```

- 默认MIME type：`default_type MIME-type;`
   - 默认：default_type text/plain;

- `types_hash_bucket_size size;`
    - 默认：types_hash_bucket_size 32|64|128;
    - 设置每个散列桶占用的内存大小，快速寻找到相应MIME type，使用散列表来存储MIMEtype与文件扩展名

- `types_hash_max_size size;`
    - 默认：types_hash_max_size 1024;
    - types_hash_max_size越大，就会消耗更多的内存，但散列key的冲突率会降低，检索速度就更快

### 7.对客户端请求的限制
- 按HTTP方法名限制用户请求：`limit_except method...{...}`
```shell
# 禁止GET方法和HEAD方法，允许其他HTTP方法
limit_except GET {
  allow 192.168.1.0/32;
  deny all;
}
```

- HTTP请求包体的最大值：`client_max_body_size size;`
    - 默认：client_max_body_size 1m;
    - 可以告诉用户请求过大不被接受

- 对请求的限速：`limit_rate speed;`
    - 默认：limit_rate 0;
    - 限制每秒传输的字节数

- 超过多大后限速：`limit_rate_after time;`
    - 默认：limit_rate_after 1m;   

### 8.文件操作的优化
- sendfile系统调用：`sendfile on|off;`
    - 默认：sendfile off;
    - 减少内核态与用户态间的文件复制次数（零拷贝）
  
- AIO系统调用：`aio on|off;`
    - 是否在FreeBSD或Linux系统上启用内核级别的异步文件I/O功能
    - 与sendfile功能是互斥的

- `directio size|off;`
    - 默认：directio off;
    - 读取文件，缓冲区大小为size，通常对大文件的读取速度有优化作用。
    - 与sendfile功能是互斥的

- `directio_alignment size;`
    - 默认：directio_alignment 512;
    - 与directio配合使用，指定以directio方式读取文件时的对齐方式（4k对齐等）

- 打开文件缓存：`open_file_cache max=N[inactive=time]|off;`
    - 默认：open_file_cache off;

- 是否缓存打开文件错误的信息：`open_file_cache_errors on|off;`
    - 默认：open_file_cache_errors off;
    - 是否在文件缓存中缓存打开文件时出现的找不到路径、没有权限等错误信息

- 不被淘汰的最小访问次数：`open_file_cache_min_uses number;`
    - 默认：open_file_cache_min_uses 1;
    - 与open_file_cache中的inactive参数配合使用。如果在inactive指定的时间段内，访问次数超过了open_file_cache_min_uses指定的最小次数，那么将不会被淘汰出缓存

- 检验缓存中元素有效性的频率：`open_file_cache_valid time;`
    - 默认：open_file_cache_valid 60s;
    - 默认为每60秒检查一次缓存中的元素是否仍有效

### 9.对客户端请求的特殊处理
- 忽略不合法的HTTP头部：`ignore_invalid_headers on|off;`
    - 默认：ignore_invalid_headers off;
    - off表示允许不合法的请求头

- HTTP头部是否允许下划线：`underscores_in_headers on|off;`
    - 默认：underscores_in_headers off;
    - off表示不允许有下划线

- 对If-Modified-Since头部的处理策略：`if_modified_since[off|exact|before];`
    - 默认：if_modified_since exact;
    - 上次请求获取内容的时间

- 文件未找到时是否记录到error日志：`log_not_found on|off;`
    - 默认：log_not_found on;

- 合并请求中相邻的/：`merge_slashes on|off;`
   - 默认：merge_slashes on;

- DNS解析地址：`resolver address...;`
- DNS解析的超时时间：`resolver_timeout time;`
    - 默认：resolver_timeout 30s;