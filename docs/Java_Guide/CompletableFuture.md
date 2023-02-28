---
title: Java CompletableFuture
description: java, 异步, 回调
hide:
  - navigation
---

> 异步回调对象

``` java
public class ForFuture {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(ForFuture::parctise);
        integerCompletableFuture.thenAccept(result -> System.out.println("成功执行方法后进行回调："+result));

        integerCompletableFuture.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
        System.out.println("hello,立刻执行");
        Thread.sleep(3000);

    }

    static int parctise() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
```