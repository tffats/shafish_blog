---
title: 报告老师，我在学外语
hide:
  - navigation
vssue: ""
---

# English

!!! note "寄语"

    外语这种东西，哼，坚持就行

## 一、Goldendict配置

??? "点我点我"

    ### 1.必读

    - [https://keatonlao.gitee.io/use-goldendict/](https://keatonlao.gitee.io/use-goldendict/){target=_blank}
    - [备份地址](https://archivebox.shafish.cn/archive/1640790082.670972/singlefile.html){target=_blank}

    ### 2.安装
    ``` shell
    ~~sudo pacman -S goldendict~~
    yay -S goldendict-webengine-git
    # 设置外部音频播放
    cvlc --play-and-stop
    ```
    ### 3.词典下载地址:
    [https://downloads.freemdict.com/%E5%B0%9A%E6%9C%AA%E6%95%B4%E7%90%86/%E5%85%B1%E4%BA%AB2020.5.11/content/0_audio/The%20little%20dict/](https://downloads.freemdict.com/%E5%B0%9A%E6%9C%AA%E6%95%B4%E7%90%86/%E5%85%B1%E4%BA%AB2020.5.11/content/0_audio/The%20little%20dict/){target=_blank}

    sha1sum：6b59cf90195580bdb8cc92f7df7bd5f1219620f9

    链接: https://pan.baidu.com/s/1Fvfz2pukHGel_QjWd7NbRA 提取码: qwer

    ### 4.具体使用
    对着[https://keatonlao.gitee.io/use-goldendict/](https://keatonlao.gitee.io/use-goldendict/){target=_blank}详细读一遍，操作一下就知道了

    ### 5.推荐一波
    木有错就是这个网站：[https://freemdict.com/](https://freemdict.com/){target=_blank}，集德、英、日、法、西班牙等学习资料，提供[论坛讨论](https://forum.freemdict.com/){target=_blank}、[资源下载](https://downloads.freemdict.com/){target=_blank}、[资源搜索](https://search.freemdict.com/){target=_blank}等一站式服务，真乃语种学习必备神器ya～

    ### 6. 自用脚本
    [python脚本](./downloads.py){target=_blank}

    ### 7. 词典
    ``` shell
    curl -L -C - "https://d.pcs.baidu.com/file/99b493602id80a965a3c08cab368b0bc?fid=1979936759-250528-150069282178055&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-wSwITx2%2FkkuHVYsJEtyN9SOmcuo%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=465739490&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.1.mdd" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    curl -L -C - "https://d.pcs.baidu.com/file/ce42a62edk295810b9e9ba6696b3bff9?fid=1979936759-250528-283167220795403&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-3PK9WzQYZUJsuaQCYCc35XQurdU%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=435507940&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.2.mdd" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    curl -L -C - "https://d.pcs.baidu.com/file/6807eaf85pd6f74487dbfc627a15750f?fid=1979936759-250528-774500207485849&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-LEWzKqLPA4rlbBr8Ga5fYlMQxk8%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=543030580&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.css" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    curl -L -C - "https://d.pcs.baidu.com/file/02b45cd93n3cf10b2068a6cdfaa3ab19?fid=1979936759-250528-853450265738698&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-0L3V%2BCMcDdDjinP0leLEaZSEpqM%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=489591605&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.js" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    curl -L -C - "https://d.pcs.baidu.com/file/f4e217a89o7ada8d299c7962a814b3c4?fid=1979936759-250528-467569182203061&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-MCNkDxNsQjbDd1C6zc2%2FnkFM08U%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=868896512&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.mdd" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    curl -L -C - "https://d.pcs.baidu.com/file/ef2324f69g2e2a483b7fb786ed53cb99?fid=1979936759-250528-718401062589650&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-234lfaPtcpWre3c9OY2%2FmJROQQE%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=771409783&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.mdx" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    curl -L -C - "https://d.pcs.baidu.com/file/c7212e429jdd15b3de5e3b365c2dee34?fid=1979936759-250528-18523052980097&dstime=1676169635&rt=sh&sign=FDtAERVJouK-DCb740ccc5511e5e8fedcff06b081203-NERL2ox9sQgF%2BrHslNguXNU2G%2Fg%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=9054865993120712354&dp-callid=0&shareid=55622936858&r=312591980&resvsflag=1-12-0-1-1-1&vuk=691799587&file_type=0" -o "oald10.png" -A "pan.baidu.com" -b "BDUSS=xXVkoyZVU0UlhYUEdYSFhQVU9ObTRWSDBGZGdSc1pUazY0YXJJU3Ayc2ozZzlrSUFBQUFBJCQAAAAAAAAAAAEAAABIKfw4srvS1M7vz7K38QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACNR6GMjUehjdU"
    ```

## 二、英语课堂

### 1.编程关键字


??? "编程关键字"

    === "1.mysql"

        --8<-- "english/mysql.md"


### 2.旅行英语


??? "旅行英语"

    === "1.在机场"

        --8<-- "english/At_the_airport.md"

    === "2.在机场续"

        --8<-- "english/At_the_airport2.md"

    === "3.海关与移民"

        --8<-- "english/Customs_and_immigration.md"

    === "4.坐出租车"

        --8<-- "english/Taking_a_Taxi.md"

    === "5.坐公交"

        --8<-- "english/public_transit.md"

    === "6.租车"

        --8<-- "english/Renting_a_car.md"

    === "7.酒店办理入住"

        --8<-- "english/Checking_into_a_hotel.md"

    === "8.酒店退房"

        --8<-- "english/Checking_out_of_a_hotel.md"

    === "9.兑换货币"

        --8<-- "english/Exchanging_Money.md"

    === "10.在餐厅"

        --8<-- "english/At_ther_restaurant.md"

    === "11.购物"

        --8<-- "english/going_shopping.md"

    === "12.观光"

        --8<-- "english/going_sightseeing.md"

    === "13.观光"

        --8<-- "english/taking_pictures.md"