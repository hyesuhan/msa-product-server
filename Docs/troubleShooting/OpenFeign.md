`Invalid HTTP method: PATCH executing PATCH http://product-service/products/1/stock`

일단 지금 createOrder에서 product 를 불러 getProduct를 호출한 후
decreaseStock를 통해 재고를 감소시키고 주문을 생성하고 있다.

@PatchMapping("/{productId}/stock")


## 문제 원인
지금 이렇게 되어 있는데 찾아보니

Java 기본 HttpURLConnection이 Patch 메서드를 지원하지 않는다.

OpenFeign 기본 클라이언트가 HttpURLConnection을 사용하기 때문에 발생하는 문제이다.

## 해결 방법

찾아보니 feign-okhttp 라이브러리가 있다.

이는 Feign이사용하는 HTTP 클라이언트만 교체해 준다.

`implementation 'io.github.openfeign:feign-okhttp'`

지금 현재는 `PATCH`를 호출하는 쪽인 Order Service에서 이를 허용해서 해결할 수 있다.

또한 아래와 같이 yaml 파일을 수정하면 될 것 같다.

```yaml
spring:
  cloud:
    openfeign:
      okhttp:
        enabled: true
```


### 그래도 인식이 안되는데요?

뭔가 예상은 했었다.. 분명 dependencies 는 추가가 되었는데 yaml 파일에서 위가 인식이 안되었다.

Spring Cloud 버전에 따라 property 명이 다를 수 있는데

대신 `@Bean` 등록해주면 되지 않을까? 싶었다.

```groovy
feign.RetryableException: Invalid HTTP method: PATCH
java.net.ProtocolException: Invalid HTTP method: PATCH
at feign.Client$Default.convertAndSend(Client.java:176)

```

아직도 `PATCH` 를 제대로 처리하지 못한 모습이다.

```java
@Bean
public okhttp3.OkHttpClient okHttpClient() {
    return new okhttp3.OkHttpClient();
}
```
`feign-okhttp` 의존성도 있고, `@Bean` 등록도 했는데 여전히 Default로 연결되었다.

> Feign은 내부적으로 feign.Client 인터페이스 구현체를 찾는데, okhttp3.OkHttpClient 는 feign.Client를 구현하지 않아서 인식을 못 하는 것 같다.


그래서 대신 다음과 같이 Configuration을 적용했다.

```java
@Bean
public feign.okhttp.OkHttpClient feignOkHttpClient() {
    return new feign.okhttp.OkHttpClient();
}
```


이제 적용은 되었는데 이런!!!

`java.net.UnknoewnHostException: 알려진 호스트가 없습니다. (product-service)`

아니 이제는 product-service 자체를 인식 못하는 새로운 문제가 발생했다.

이유는 OkHttp가 product-service 를 DNS로 호출하려 해서 생기는 문제이다...
product-service는 DNS 호스트가 아닌 Eureka 서비스 명이다!!


다시 로드 밸런서를 수동으로 연동해주어야 한다.

FeignConfig에서 직접 bean 등록을 하면 Spring Cloud LoadBalancer가 끊이기 아래와 같이 수정했다. (가로채는 문제)

```java
@Bean
    public feign.okhttp.OkHttpClient feignOkHttpClient(
            okhttp3.OkHttpClient okHttpClient) {
        return new feign.okhttp.OkHttpClient(okHttpClient);
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
    }
```


그리고 ProductClient 에서 Configuration을 제거했다.

와우! 그냥 안됨 ㅋㅋㅋㅋ ... ㅠㅠㅠ

이 버전에서는 OkHttp + LoadBalancer 동시 사용하는 게 @Bean 방식으로는 안전하지 않은 것 같습니다..

Spring Cloud LoadBalancer 체인 자체가 feign.okhttp 적용하면 bypass가 되버리네요...

차라리 PATCH 를 POST로 수정합시다. -돌고 돌아 원점-

