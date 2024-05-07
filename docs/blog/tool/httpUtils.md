---
title: http请求工具
description: retrofit2 使用示例
icon: material/emoticon-happy
#status: new
hide:
  - navigation
---

[ :fishing_pole_and_fish: ](/)

## 一、了解

`OkHttp` 是一个高效的HTTP客户端，使得在应用程序中执行网络请求变得更加简单和高效。`Retrofit` 是一个基于 `OkHttp` 的网络请求库，提供了更高层次的抽象和封装。网络请求用它们就完了。

## 二、添加依赖

``` xml title="pom.xml"
<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>33.2.0-jre</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-jackson -->
    <dependency>
        <groupId>com.squareup.retrofit2</groupId>
        <artifactId>converter-jackson</artifactId>
        <version>2.11.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-scalars -->
    <dependency>
        <groupId>com.squareup.retrofit2</groupId>
        <artifactId>converter-scalars</artifactId>
        <version>2.11.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.squareup.retrofit2/adapter-rxjava3 -->
    <dependency>
        <groupId>com.squareup.retrofit2</groupId>
        <artifactId>adapter-rxjava3</artifactId> 
<!--			<artifactId>adapter-java8</artifactId>-->  <!-- (1) -->
        <version>2.11.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit -->
    <dependency>
        <groupId>com.squareup.retrofit2</groupId>
        <artifactId>retrofit</artifactId>
        <version>2.11.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/io.reactivex.rxjava3/rxjava -->
    <dependency>
        <groupId>io.reactivex.rxjava3</groupId>
        <artifactId>rxjava</artifactId>
<!--			<version>3.1.8</version>-->
    </dependency>
```

1.   如果是jdk8则使用 `adapter-java8`

<!-- more -->

## 三、使用

``` java title="RetrofitUtils.java"
public class RetrofitUtils {

    public static Retrofit getRetrofit(String baseUrl, int timeOut) {
        return getRetrofit(baseUrl, timeOut, JacksonConverterFactory.create(), null);
    }

    public static Retrofit getRetrofit(String baseUrl, int timeOut, retrofit2.Converter.Factory converterFactory, List<Interceptor> interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder().
                connectTimeout(timeOut, TimeUnit.SECONDS).
                readTimeout(timeOut, TimeUnit.SECONDS).
                writeTimeout(timeOut, TimeUnit.SECONDS);

        if (HCollectionUtils.isNotEmpty(interceptors)) {
            okHttpClientBuilder.interceptors().addAll(interceptors);
        }

        OkHttpClient client = okHttpClientBuilder.build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl) // 设置网络请求的Url地址
                .client(client)
                // 支持java8平台 Java8CallAdapterFactory
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                // 设置json数据解析器
                .addConverterFactory(converterFactory);
        return builder.build();
    }

    public static <T> T checkAndGetData(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                    throw new RuntimeException("http call failed, response: " + response.toString() + ", errorBody: " + errorBody.string());
                } else {
                    throw new RuntimeException("http call failed, response: " + response.toString());
                }
            }
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

``` java title="OAuthClient.class"
public interface OAuthClient {
    @POST
    @FormUrlEncoded
    Call<Object> getOAuthToken(@Url String url, @FieldMap Map<String, Object> map, @Header("Authorization") String authorization);

    @GET
    Call<Object> get(@Url String url, @Header("Authorization") String authorization);

    @GET
    Call<Object> get(@Url String url);  // (1)

    @OPTIONS
    Call<Object> options(@Url String url, @Header("Authorization") String authorization);

    @OPTIONS
    Call<String> optionsString(@Url String url, @Header("Authorization") String authorization);

    @GET
    Call<String> getString(@Url String url, @Header("Authorization") String authorization);

    @POST
    Call<Object> post(@Url String url, @Body Map<String, Object> data, @Header("Authorization") String authorization);

    @POST
    Call<Object> post(@Url String url, @Header("Authorization") String authorization);

    @POST
    Call<Object> postCt(@Url String url, @Body Map<String, Object> data, @Header("Authorization") String authorization,
                        @Header("Content-Type") String contentType);

    @POST
    Call<Object> postList(@Url String url, @Body List<Object> data, @Header("Authorization") String authorization);

    @POST
    Call<Object> postSign(@Url String url, @Body Map<String, Object> data, @Header("Authorization") String authorization,
                          @Header("sign") String sign, @Header("timestamp") Long timestamp);

    @POST
    Call<String> postString(@Url String url, @Body Map<String, Object> data, @Header("Authorization") String authorization);

    @DELETE
    Call<Object> delete(@Url String url, @Header("Authorization") String authorization);

    @HTTP(method = "DELETE", hasBody = true)
    Call<Object> deleteHasBody(@Url String url, @Body Map<String, Object> data, @Header("Authorization") String authorization);

}
```

1.  普通的Get请求

``` java title="RetrofitUtilsTest.java"
public class RetrofitUtilsTest {

    private Retrofit apis;
    private String httpDomain;

    @BeforeEach
    public void getApi() {
        httpDomain = "https://api.apihubs.cn/";
        apis = RetrofitUtils.getRetrofit(httpDomain, 60);
    }

    @Test
    public void wcTest() {
        try {
            OAuthClient client = apis.create(OAuthClient.class);
            Call<Object> call = client.get(httpDomain + "holiday/get");
            Object rsl = RetrofitUtils.checkAndGetData(call);

            System.out.println((HJsonUtils.toJson(rsl)));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
```

