---
title: flyscrape
description: 网页抓取
hide:
  - navigation
tags:
  - 爬虫
bcs:
  "分类": "/tags/#tag:爬虫"    
---

## 一、简单介绍

`flyscrape` 是一款开源的命令行网页抓取工具，专为不具备高级编程技能的用户设计，旨在简化网页数据提取的过程。

局限性还是有的，很多用防护（cloudflare等）的网站会爬不了。

### 1. 安装

软件包： https://github.com/philippta/flyscrape/releases

```
tar xf flyscrape_<os>_<arch>.tar.gz
cd flyscrape_<os>_<arch>/
mv flyscrape /usr/local/bin/flyscrape
```

### 2. 一键安装脚本

``` shell
curl -fsSL https://flyscrape.com/install | bash
```

### 3. 命令

``` shell
flyscrape --help

flyscrape is a standalone and scriptable web scraper for efficiently extracting data from websites.

Usage:

    flyscrape <command> [arguments]

Commands:

    new    creates a sample scraping script
    run    runs a scraping script
    dev    watches and re-runs a scraping script

Examples:

    # Run the script.
    $ flyscrape run example.js

    # Set the URL as argument.
    $ flyscrape run example.js --url "http://other.com"

    # Enable proxy support.
    $ flyscrape run example.js --proxies "http://someproxy:8043"

    # Follow paginated links.
    $ flyscrape run example.js --depth 5 --follow ".next-button > a"

    # Set the output format to ndjson.
    $ flyscrape run example.js --output.format ndjson

    # Write the output to a file.
    $ flyscrape run example.js --output.file results.json
```

## 二、API参考

### 1. 查询接口

``` js
// <div class="element" foo="bar">Hey</div>
const el = doc.find(".element")
el.text()                                 // "Hey"
el.html()                                 // `<div class="element">Hey</div>`
el.attr("foo")                            // "bar"
el.hasAttr("foo")                         // true
el.hasClass("element")                    // true

// <ul>
//   <li class="a">Item 1</li>
//   <li>Item 2</li>
//   <li>Item 3</li>
// </ul>
const list = doc.find("ul")
list.children()                           // [<li class="a">Item 1</li>, <li>Item 2</li>, <li>Item 3</li>]

const items = list.find("li")
items.length()                            // 3
items.first()                             // <li>Item 1</li>
items.last()                              // <li>Item 3</li>
items.get(1)                              // <li>Item 2</li>
items.get(1).prev()                       // <li>Item 1</li>
items.get(1).next()                       // <li>Item 3</li>
items.get(1).parent()                     // <ul>...</ul>
items.get(1).siblings()                   // [<li class="a">Item 1</li>, <li>Item 2</li>, <li>Item 3</li>]
items.map(item => item.text())            // ["Item 1", "Item 2", "Item 3"]
items.filter(item => item.hasClass("a"))  // [<li class="a">Item 1</li>]
```

### 2. 文档解析
``` js
import { parse } from "flyscrape";

const doc = parse(`<div class="foo">bar</div>`);
const text = doc.find(".foo").text();
```

### 3. 文件下载
``` js
import { download } from "flyscrape/http";

download("http://example.com/image.jpg")              // downloads as "image.jpg"
download("http://example.com/image.jpg", "other.jpg") // downloads as "other.jpg"
download("http://example.com/image.jpg", "dir/")      // downloads as "dir/image.jpg"

// If the server offers a filename via the Content-Disposition header and no
// destination filename is provided, Flyscrape will honor the suggested filename.
// E.g. `Content-Disposition: attachment; filename="archive.zip"`
download("http://example.com/generate_archive.php", "dir/") // downloads as "dir/archive.zip"
```

## 三、完整示例

``` js
import { parse } from "flyscrape";
import { download } from "flyscrape/http";
import http from "flyscrape/http";

export const config = {
    // Specify the URL to start scraping from.
    url: "https://example.com/",

    // Specify the multiple URLs to start scraping from.   (default = [])
    urls: [                          
        "https://anothersite.com/",
        "https://yetanother.com/",
    ],

    // Enable rendering with headless browser.             (default = false)
    browser: true,

    // Specify if browser should be headless or not.       (default = true)
    headless: false,

    // Specify how deep links should be followed.          (default = 0, no follow)
    depth: 5,                        

    // Speficy the css selectors to follow.                (default = ["a[href]"])
    follow: [".next > a", ".related a"],                      
 
    // Specify the allowed domains. ['*'] for all.         (default = domain from url)
    allowedDomains: ["example.com", "anothersite.com"],              
 
    // Specify the blocked domains.                        (default = none)
    blockedDomains: ["somesite.com"],              

    // Specify the allowed URLs as regex.                  (default = all allowed)
    allowedURLs: ["/posts", "/articles/\d+"],                 
 
    // Specify the blocked URLs as regex.                  (default = none)
    blockedURLs: ["/admin"],                 
   
    // Specify the rate in requests per minute.            (default = no rate limit)
    rate: 60,                       

    // Specify the number of concurrent requests.          (default = no limit)
    concurrency: 1,                       

    // Specify a single HTTP(S) proxy URL.                 (default = no proxy)
    // Note: Not compatible with browser mode.
    proxy: "http://someproxy.com:8043",

    // Specify multiple HTTP(S) proxy URLs.                (default = no proxy)
    // Note: Not compatible with browser mode.
    proxies: [
      "http://someproxy.com:8043",
      "http://someotherproxy.com:8043",
    ],                     

    // Enable file-based request caching.                  (default = no cache)
    cache: "file",                   

    // Specify the HTTP request header.                    (default = none)
    headers: {                       
        "Authorization": "Bearer ...",
        "User-Agent": "Mozilla ...",
    },

    // Use the cookie store of your local browser.         (default = off)
    // Options: "chrome" | "edge" | "firefox"
    cookies: "chrome",

    // Specify the output options.
    output: {
        // Specify the output file.                        (default = stdout)
        file: "results.json",
        
        // Specify the output format.                      (default = json)
        // Options: "json" | "ndjson"
        format: "json",
    },
};

export default function ({ doc, url, absoluteURL }) {
  // doc              - Contains the parsed HTML document
  // url              - Contains the scraped URL
  // absoluteURL(...) - Transforms relative URLs into absolute URLs

  // Find all users.
  const userlist = doc.find(".user")

  // Download the profile picture of each user.
  userlist.each(user => {
    const name = user.find(".name").text()
    const pictureURL = absoluteURL(user.find("img").attr("src"));

    download(pictureURL, `profile-pictures/${name}.jpg`)
  })

  // Return users name, address and age.
  return {
    users: userlist.map(user => {
      const name = user.find(".name").text()
      const address = user.find(".address").text()
      const age = user.find(".age").text()

      return { name, address, age };
    })
  };
}
```